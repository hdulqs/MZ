/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年4月1日 下午7:26:24
 */
package com.mz.exchange.account.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.mz.account.fund.model.AppAccount;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.fund.model.AppTransaction;
import com.mz.account.remote.RemoteAppAccountService;
import com.mz.account.remote.RemoteAppOurAccountService;
import com.mz.account.remote.RemoteAppTransactionService;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.exchange.account.service.ExAmineOrderService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;
import com.mz.ico.coinAccountColdRecord.model.AppIcoCoinAccountColdRecord;
import com.mz.ico.coinAccountHotRecord.model.AppIcoCoinAccountHotRecord;
import com.mz.ico.coinTransaction.model.AppIcoCoinTransaction;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.exchange.account.service.ExDmColdAccountRecordService;
import com.mz.exchange.account.service.ExDmHotAccountRecordService;
import com.mz.ico.coinAccount.service.AppIcoCoinAccountService;
import com.mz.ico.coinAccountColdRecord.service.AppIcoCoinAccountColdRecordService;
import com.mz.ico.coinAccountHotRecord.service.AppIcoCoinAccountHotRecordService;
import com.mz.ico.coinTransaction.service.AppIcoCoinTransactionService;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年4月1日 下午7:26:24
 */

@Service("examineOrderService")
public class ExAmineOrderServiceImpl implements ExAmineOrderService {

  @Resource(name = "exDmColdAccountRecordService")
  public ExDmColdAccountRecordService exDmColdAccountRecordService;

  @Resource(name = "exDmHotAccountRecordService")
  public ExDmHotAccountRecordService exDmHotAccountRecordService;

  @Resource(name = "exDmTransactionService")
  public ExDmTransactionService exDmTransactionService;

  @Resource(name = "exDigitalmoneyAccountService")
  public ExDigitalmoneyAccountService exDigitalmoneyAccountService;

  @Resource
  private AppIcoCoinAccountService appIcoCoinAccountService;

  @Resource
  private AppIcoCoinAccountHotRecordService appIcoCoinAccountHotRecordService;

  @Resource
  private AppIcoCoinAccountColdRecordService appIcoCoinAccountColdRecordService;

  @Resource
  private AppIcoCoinTransactionService appIcoCoinTransactionService;

  @Resource
  private MessageProducer messageProducer;

  @Override
  public String pasePutOrder(Long id) {
    ExDmTransaction exDmTransaction = exDmTransactionService.get(id);
    Integer i = exDmTransaction.getStatus();
    if (i.intValue() == 2) {
      return "NO";
    }
    Long accountId = exDmTransaction.getAccountId();
    if (accountId != null) {
      ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(accountId);
      Integer tp = exDmTransaction.getTransactionType();
      //充币
      if (tp == 1) {
        //要发消息
        return "NO";

      }
      //提币
      if (tp == 2) {

        BigDecimal transactionMoney = exDmTransaction.getTransactionMoney();
        BigDecimal coldMoney = exDigitalmoneyAccount.getColdMoney();
        BigDecimal l = coldMoney.subtract(transactionMoney);
        exDigitalmoneyAccount.setColdMoney(l);

        //----发送mq消息----start
        //冷账户增加
        Accountadd accountadd2 = new Accountadd();
        accountadd2.setAccountId(exDigitalmoneyAccount.getId());
        accountadd2.setMoney(
            exDmTransaction.getTransactionMoney().subtract(exDmTransaction.getFee())
                .multiply(new BigDecimal(-1)));
        accountadd2.setMonteyType(2);
        accountadd2.setAcccountType(1);
        accountadd2.setRemarks(33);
        accountadd2.setTransactionNum(exDmTransaction.getTransactionNum());

        //手续费 -- 冷
        Accountadd accountaddf2 = new Accountadd();
        accountaddf2.setAccountId(exDigitalmoneyAccount.getId());
        accountaddf2.setMoney(exDmTransaction.getFee().multiply(new BigDecimal(-1)));
        accountaddf2.setMonteyType(2);
        accountaddf2.setAcccountType(1);
        accountaddf2.setRemarks(34);
        accountaddf2.setTransactionNum(exDmTransaction.getTransactionNum());

        List<Accountadd> list = new ArrayList<Accountadd>();
        list.add(accountadd2);
        list.add(accountaddf2);

        //----发送mq消息----end

        // 修改可用余额
        //exDigitalmoneyAccountDao.updateByPrimaryKeySelective(exDigitalmoneyAccount);

        // 修改订单
        exDmTransaction.setStatus(2);
        exDmTransaction.setUserId(exDigitalmoneyAccount.getCustomerId());
        exDmTransactionService.update(exDmTransaction);
        RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
            .getBean("remoteAppOurAccountService");
        //我方提币账户
        AppOurAccount ourAccount = remoteAppOurAccountService
            .findAppOurAccount(ContextUtil.getWebsite(), exDmTransaction.getCoinCode(),
                Integer.valueOf("1"));

        remoteAppOurAccountService
            .changeCountToOurAccoun(ourAccount, exDmTransaction, exDmTransaction.getOutAddress(),
                "提币记录", "");

        remoteAppOurAccountService
            .changeCountToOurAccoun(ourAccount, exDmTransaction, exDmTransaction.getOutAddress(),
                "提币手续费记录", "fee");

        messageProducer.toAccount(JSON.toJSONString(list));
        return "OK";
      } else {
        return "NO";
      }
    } else {
      return "NO";
    }
  }

  @Override
  public List<ExDmTransaction> getlistByapply(Long customerId, String currencyType,
      String website) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    //提币2审核中1，4所有记录
    QueryFilter withdrawedtfilter = new QueryFilter(ExDmTransaction.class);
    withdrawedtfilter.addFilter("customerId=", customerId);
    withdrawedtfilter.addFilter("status_in", "1,4");
    withdrawedtfilter.addFilter("transactionType_in", "2");
    withdrawedtfilter.addFilter("currencyType=", currencyType);
    withdrawedtfilter.addFilter("website=", website);
    List<ExDmTransaction> widthedtlist = exDmTransactionService.find(withdrawedtfilter);
    return widthedtlist;

  }


  @Override
  public String pasePutOrderToAppAccount(Long id) {
    RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil
        .getBean("remoteAppAccountService");
    RemoteAppTransactionService remoteAppTransactionService = (RemoteAppTransactionService) ContextUtil
        .getBean("remoteAppTransactionService");
    ExDmTransaction exDmTransaction = exDmTransactionService.get(id);
    Integer i = exDmTransaction.getStatus();
    if (i == 2) {
      return "NO";
    }

    ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.findByOrderId(id);

    Integer tp = exDmTransaction.getTransactionType();
    if (tp == 1) {
      AppAccount appAccount = remoteAppAccountService
          .getByCustomerIdAndType(exDmTransaction.getCustomerId(),
              exDmTransaction.getCurrencyType(), exDmTransaction.getWebsite());

      remoteAppAccountService
          .inComeToHotAccount(appAccount.getId(), exDmTransaction.getTransactionMoney(),
              exDmTransaction.getTransactionNum(), "币转钱", null, null);

      // 修改订单
      exDmTransaction.setStatus(2);
      exDmTransaction.setUserId(exDigitalmoneyAccount.getCustomerId());
      exDmTransactionService.update(exDmTransaction);

      //关键的:把币转成资金要生成一条资金充值业务单子
      AppTransaction appTransaction = new AppTransaction();
      appTransaction.setAccountId(appAccount.getId());
      appTransaction.setCurrencyType(exDmTransaction.getCurrencyType());
      appTransaction.setWebsite(exDmTransaction.getWebsite());
      appTransaction.setCustomerId(exDmTransaction.getCustomerId());
      appTransaction.setRemark("币转钱");
      appTransaction.setStatus(2);
      appTransaction.setTransactionMoney(exDmTransaction.getTransactionMoney());
      appTransaction.setTransactionType(3);
      appTransaction.setTrueName(exDmTransaction.getTrueName());
      appTransaction.setTransactionNum(exDmTransaction.getTransactionNum());
      appTransaction.setUserName(exDmTransaction.getCustomerName());
      appTransaction.setUserId(exDmTransaction.getCustomerId());
      appTransaction.setFee(new BigDecimal("0"));
      remoteAppTransactionService.save(appTransaction);

      return "OK";
    }
    if (tp == 2) {
      AppAccount appAccount = remoteAppAccountService
          .getByCustomerIdAndType(exDmTransaction.getCustomerId(),
              exDmTransaction.getCurrencyType(), exDmTransaction.getWebsite());

      System.out.println("手动提币");
      remoteAppAccountService.unfreezeAccountThem(appAccount.getId(),
          exDmTransaction.getTransactionMoney().subtract(exDmTransaction.getFee()),
          exDmTransaction.getTransactionNum(), "提币金额", null, null);
      //收费费
      remoteAppAccountService.unfreezeAccountThem(appAccount.getId(), exDmTransaction.getFee(),
          exDmTransaction.getTransactionNum(), "提币手续费", null, null);
      //修改订单状态
      AppTransaction appTransaction = remoteAppTransactionService
          .getByTransactionNum(exDmTransaction.getTransactionNum());
      appTransaction.setStatus(2);
      remoteAppTransactionService.update(appTransaction);
      // 修改订单
      exDmTransaction.setStatus(2);
      exDmTransaction.setUserId(exDigitalmoneyAccount.getCustomerId());
      exDmTransactionService.update(exDmTransaction);
      return "OK";
    } else {
      return "NO";
    }

  }

  @Override
  public String paseStopeOrderToAppAccount(Long id) {

    ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.findByOrderId(id);
    ExDmTransaction exDmTransaction = exDmTransactionService.get(id);
    Integer i = exDmTransaction.getStatus();

    if (i != 1) {
      return "NO";
    }
    Integer tp = exDmTransaction.getTransactionType();
    if (tp == 1) {
      exDmTransaction.setStatus(3);
      exDmTransaction.setUserId(exDigitalmoneyAccount.getCustomerId());
      exDmTransactionService.update(exDmTransaction);
      return "OK";
    }
    if (tp == 2) {

      RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil
          .getBean("remoteAppAccountService");
      RemoteAppTransactionService remoteAppTransactionService = (RemoteAppTransactionService) ContextUtil
          .getBean("remoteAppTransactionService");
      AppAccount appAccount = remoteAppAccountService
          .getByCustomerIdAndType(exDmTransaction.getCustomerId(),
              exDmTransaction.getCurrencyType(), exDmTransaction.getWebsite());
      remoteAppAccountService
          .unfreezeAccountSelf(appAccount.getId(), exDmTransaction.getTransactionMoney(),
              exDmTransaction.getTransactionNum(), "驳回解冻", null, null);

      AppTransaction appTransaction = remoteAppTransactionService
          .getByTransactionNum(exDmTransaction.getTransactionNum());
      appTransaction.setStatus(3);
      remoteAppTransactionService.update(appTransaction);
      ;
      // 修改订单
      exDmTransaction.setStatus(3);
      exDmTransaction.setUserId(exDigitalmoneyAccount.getCustomerId());
      exDmTransactionService.update(exDmTransaction);

      return "OK";
    } else {
      return "NO";
    }

  }

  @Override
  public String rechargeCoin(ExDmTransaction exTxs) {
    System.out.println("充币记录保存完成，开始处理热钱包流水记录及修改账户");
    StringBuffer ret = new StringBuffer("{\"success\":\"true\",\"msg\":");
    try {
      Integer i = exTxs.getStatus();
      if (i == 2) {
        ret.append("\"该订单已通过\"");
      }

      ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService
          .getByCustomerIdAndType(
              exTxs.getCustomerId(), exTxs.getCoinCode(), exTxs.getCurrencyType(),
              exTxs.getWebsite());
      if (exDigitalmoneyAccount == null) {
        System.out.println("未查询到钱包账户");
        return null;
      }

      BigDecimal hotMoney = exDigitalmoneyAccount.getHotMoney();
      BigDecimal transactionMoney = exTxs.getTransactionMoney();
      BigDecimal k = hotMoney.add(transactionMoney);
      exDigitalmoneyAccount.setHotMoney(k);
      // 修改可用余额
      exDigitalmoneyAccountService.update(exDigitalmoneyAccount);

      // 保存可用余额流水
      ExDmHotAccountRecord exDmHotAccountRecord = new ExDmHotAccountRecord();
      exDmHotAccountRecord.setAccountId(exDigitalmoneyAccount.getId());
      exDmHotAccountRecord.setCustomerId(exTxs.getCustomerId());
      exDmHotAccountRecord.setUserName(exDigitalmoneyAccount.getUserName());
      exDmHotAccountRecord.setRecordType(1);
      exDmHotAccountRecord.setTransactionMoney(exTxs.getTransactionMoney());
      exDmHotAccountRecord.setStatus(5);
      exDmHotAccountRecord.setRemark("可用余额流水已记录成功 ");
      exDmHotAccountRecord.setCurrencyType(exTxs.getCurrencyType());
      exDmHotAccountRecord.setCoinCode(exTxs.getCoinCode());
      exDmHotAccountRecord.setWebsite(exTxs.getWebsite());
      exDmHotAccountRecord.setTransactionNum(exTxs.getTransactionNum());
      exDmHotAccountRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));
      exDmHotAccountRecordService.save(exDmHotAccountRecord);

      // 修改订单
      exTxs.setStatus(2);
      exTxs.setUserId(exDigitalmoneyAccount.getCustomerId());
      exDmTransactionService.update(exTxs);
      ret.append("\"记账成功\"");
    } catch (Exception e) {
      e.printStackTrace();
      ret.append("\"异常 :" + e.getMessage() + "  \"");
    } finally {
      ret.append("}");
    }
    return ret.toString();
  }

  /**
   * 驳回一个订单(ICO)
   */
  @Override
  public String paseRefuseOrder(Long id) {
    AppIcoCoinTransaction transaction = appIcoCoinTransactionService.get(id);

    QueryFilter filter = new QueryFilter(AppIcoCoinAccount.class);
    filter.addFilter("accountId=", id);
    AppIcoCoinAccount coinAccount = appIcoCoinAccountService.get(filter);
    Integer i = transaction.getStatus();
    if (i != 1) {
      return "NO";
    }
    Integer tp = transaction.getTransactionType();
    if (tp == 1) {
      transaction.setStatus(3);
      transaction.setUserId(coinAccount.getCustomerId());
      appIcoCoinTransactionService.update(transaction);
      return "OK";
    }
    if (tp == 2) {

      BigDecimal hotMoney = coinAccount.getHotMoney();
      BigDecimal transactionMoney = transaction.getTransactionMoney();
      BigDecimal k = hotMoney.add(transactionMoney);
      BigDecimal coldMoney = coinAccount.getColdMoney();
      BigDecimal l = coldMoney.subtract(transactionMoney);
      coinAccount.setHotMoney(k);
      coinAccount.setColdMoney(l);
      // 修改可用余额
      appIcoCoinAccountService.update(coinAccount);

      // 保存可用余额流水
      AppIcoCoinAccountHotRecord hotRecord = new AppIcoCoinAccountHotRecord();
      hotRecord.setAccountId(coinAccount.getId());
      hotRecord.setCustomerId(transaction.getCustomerId());
      hotRecord.setUserName(coinAccount.getUserName());
      hotRecord.setRecordType(1);
      hotRecord.setTransactionMoney(transaction.getTransactionMoney());
      hotRecord.setStatus(10);
      hotRecord.setRemark("订单审核未通过 ");
      hotRecord.setCurrencyType(transaction.getCurrencyType());
      hotRecord.setCoinCode(transaction.getCoinCode());
      hotRecord.setWebsite(transaction.getCoinCode());
      hotRecord.setTransactionNum(transaction.getTransactionNum());
      hotRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));
      appIcoCoinAccountHotRecordService.save(hotRecord);

      // 保存冻结流水明细
      AppIcoCoinAccountColdRecord coldRecord = new AppIcoCoinAccountColdRecord();
      coldRecord.setAccountId(coinAccount.getId());
      coldRecord.setCustomerId(transaction.getCustomerId());
      coldRecord.setUserName(coinAccount.getUserName());
      coldRecord.setRecordType(2);
      coldRecord.setTransactionMoney(transaction.getTransactionMoney());
      coldRecord.setStatus(10);
      coldRecord.setRemark("订单审核未通过");
      coldRecord.setCurrencyType(transaction.getCurrencyType());
      coldRecord.setCoinCode(transaction.getCoinCode());
      coldRecord.setWebsite(transaction.getWebsite());
      coldRecord.setTransactionNum(transaction.getTransactionNum());
      coldRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));
      appIcoCoinAccountColdRecordService.save(coldRecord);

      // 修改订单
      transaction.setStatus(3);
      transaction.setUserId(coinAccount.getCustomerId());
      appIcoCoinTransactionService.update(transaction);

      return "OK";
    } else {
      return "NO";
    }
  }


}
