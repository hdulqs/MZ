/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-08-18 14:08:35
 */
package com.mz.ico.coinTransaction.controller;

import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.remote.RemoteAppOurAccountService;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.customer.user.model.AppCustomer;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;
import com.mz.ico.coinAccountColdRecord.model.AppIcoCoinAccountColdRecord;
import com.mz.ico.coinAccountHotRecord.model.AppIcoCoinAccountHotRecord;
import com.mz.ico.coinTransaction.model.AppIcoCoinTransaction;
import com.mz.util.QueryFilter;
import com.mz.util.StringUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.exchange.account.service.ExAmineOrderService;
import com.mz.exchange.purse.IcoCoinInterfaceUtil;
import com.mz.ico.coinAccount.service.AppIcoCoinAccountService;
import com.mz.ico.coinAccountColdRecord.service.AppIcoCoinAccountColdRecordService;
import com.mz.ico.coinAccountHotRecord.service.AppIcoCoinAccountHotRecordService;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-08-18 14:08:35
 */
@Controller
@RequestMapping("/coinTransaction/appicocointransaction")
public class AppIcoCoinTransactionController extends BaseController<AppIcoCoinTransaction, Long> {

  @Resource
  public ExAmineOrderService examineOrderService;
  @Resource
  private AppIcoCoinAccountService appIcoCoinAccountService;
  @Resource
  private AppIcoCoinAccountHotRecordService appIcoCoinAccountHotRecordService;
  @Resource
  private AppIcoCoinAccountColdRecordService appIcoCoinAccountColdRecordService;

  @Resource(name = "appIcoCoinTransactionService")
  @Override
  public void setService(BaseService<AppIcoCoinTransaction, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看AppIcoCoinTransaction")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public AppIcoCoinTransaction see(@PathVariable Long id) {
    AppIcoCoinTransaction appIcoCoinTransaction = service.get(id);
    return appIcoCoinTransaction;
  }

  @MethodName(name = "增加AppIcoCoinTransaction")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, AppIcoCoinTransaction appIcoCoinTransaction) {
    return super.save(appIcoCoinTransaction);
  }

  @MethodName(name = "修改AppIcoCoinTransaction")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request,
      AppIcoCoinTransaction appIcoCoinTransaction) {
    return super.update(appIcoCoinTransaction);
  }

  @MethodName(name = "删除AppIcoCoinTransaction")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表AppIcoCoinTransaction")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppIcoCoinTransaction.class, request);
    PageResult page = null;
    String type = request.getParameter("transactionType_EQ");
    if (type.equals("1")) {
      filter.setOrderby("created desc");
      page = super.findPage(filter);
    } else {
      filter.setOrderby("created desc");
      page = super.findPage(filter);
    }

    return page;
  }


  @RequestMapping(value = "/withdrawals/{ids}", method = RequestMethod.GET)
  @MethodName(name = "提币")
  @ResponseBody
  public JsonResult withdrawals(@PathVariable String ids) {
    JsonResult jsonResult = new JsonResult();
    if (!"".equals(ids) && ids != null) {
      String[] list = ids.split(",");
      Long id = null;
      for (String l : list) {
        id = Long.valueOf(l);
        try {
          JsonResult result = super.get(id);
          AppIcoCoinTransaction transaction = (AppIcoCoinTransaction) result.getObj();
          // transactionType=2提币、status=1待审核
          if (transaction.getTransactionType() == 2 && transaction.getStatus() == 1) {
            //转入钱包地址
            BigDecimal money = transaction.getTransactionMoney().subtract(transaction.getFee());
            String coinCode = transaction.getCoinCode();
            Long customerId = transaction.getCustomerId();

            RemoteAppCustomerService appCustomerService = (RemoteAppCustomerService) ContextUtil
                .getBean("remoteAppCustomerService");
            AppCustomer customer = appCustomerService.getById(customerId);

            RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
                .getBean("remoteAppOurAccountService");
            //获取我方ico提币账户
            //暂时不确定放到后台哪里？
            AppOurAccount ourAccount = remoteAppOurAccountService.findAppOurAccount(
                transaction.getWebsite(), coinCode, Integer.valueOf("3"));

            if (ourAccount == null) {
              jsonResult.setMsg("提币账户不能为空");
              jsonResult.setSuccess(false);
              return jsonResult;
            }

            String islmc = PropertiesUtils.APP.getProperty("app.coinInterFace");
            //lmc充币接口
            if ("LMC".equals(islmc)) {/*
							jsonResult = sendTo(transaction);
							if(jsonResult.getSuccess()){
								// 发送提币短信通知
								SmsParam smsParam = new SmsParam();
								smsParam.setHryMobilephone(customer.getUserName());
								smsParam.setHrySmstype("sms_withdraw_rmbOrCoin");
								smsParam.setHryCode(coinCode);
								SmsSendUtil.sendSmsCode(smsParam);
							}else{
								return jsonResult;
							}
						*/
            } else {//默认充币接口
              // 查询Ico钱包余额
              String balance = IcoCoinInterfaceUtil.balance("", transaction.getCoinCode());
              if (null != balance && !"".equals(balance)) {
                BigDecimal purseBalance = new BigDecimal(balance);
                // 查询提币账户余额
                String withdrawBalance = IcoCoinInterfaceUtil.balance(ourAccount.getAccountName(),
                    transaction.getCoinCode());
                BigDecimal pursewithdrawBalance = new BigDecimal(withdrawBalance);
                // 提币账号余额比较提币金额
                if (pursewithdrawBalance.compareTo(money.setScale(4, BigDecimal.ROUND_HALF_UP))
                    > 0) {
                  // 钱包总余额比较提币金额
                  if (purseBalance.compareTo(money.setScale(4, BigDecimal.ROUND_HALF_UP)) > 0) {
                    jsonResult = sendTo(transaction);
                    if (jsonResult.getSuccess()) {
                      // 发送提币短信通知
                      SmsParam smsParam = new SmsParam();
                      smsParam.setHryMobilephone(customer.getUserName());
                      smsParam.setHrySmstype("sms_withdraw_rmbOrCoin");
                      smsParam.setHryCode(coinCode);
                      SmsSendUtil.sendSmsCode(smsParam,null,null);
                    } else {
                      return jsonResult;
                    }

                  } else {
                    jsonResult.setMsg("钱包总账户余额不足");
                    jsonResult.setSuccess(false);
                    return jsonResult;
                  }

                } else {
                  jsonResult.setMsg("提币账户余额不足");
                  jsonResult.setSuccess(false);
                  return jsonResult;
                }

              } else {
                jsonResult.setMsg("服务器钱包未查到数据");
                jsonResult.setSuccess(false);
                return jsonResult;
              }
            }
          }

        } catch (Exception e) {
          e.printStackTrace();
          jsonResult.setSuccess(false);
          jsonResult.setMsg("后台处理异常   失败");
          return jsonResult;
        }
      }
    }
    jsonResult.setSuccess(true);
    return jsonResult;
  }


  @RequestMapping(value = "/refuseWith/{ids}")
  @MethodName(name = "驳回一个订单")
  @ResponseBody
  public JsonResult stop(@PathVariable String ids, HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    if (!"".equals(ids) && ids != null) {
      String[] list = ids.split(",");
      String reason = "批量驳回";
      String str = request.getParameter("reason");
      if (list.length == 1 && str != null && !"".equals(str)) {//确认不是通过批量操作的
        reason = str;
      }
      Long id = null;
      for (String l : list) {
        id = Long.valueOf(l);
        try {
          String s = examineOrderService.paseRefuseOrder(id);
          if (s.equals("OK")) {
            AppIcoCoinTransaction coinTransaction = service.get(id);
            coinTransaction.setRejectionReason(reason);
            service.update(coinTransaction);

            Long customerId = coinTransaction.getCustomerId();
            RemoteAppCustomerService appCustomerService = (RemoteAppCustomerService) ContextUtil
                .getBean("remoteAppCustomerService");
            AppCustomer customer = appCustomerService.getById(customerId);
            //发送提现驳回短信通知(提币驳回)
            SmsParam smsParam = new SmsParam();
            smsParam.setHryMobilephone(customer.getUserName());
            smsParam.setHrySmstype("sms_coinwithdraw_invalid");
            smsParam.setHryCode(coinTransaction.getCoinCode());
            SmsSendUtil.sendSmsCode(smsParam, null, null);
          } else {
            jsonResult.setSuccess(false);
            return jsonResult;
          }
        } catch (Exception e) {
          jsonResult.setSuccess(false);
          return jsonResult;
        }
      }
    }
    jsonResult.setSuccess(true);
    return jsonResult;
  }


  /**
   * * 调用钱包接口转出币
   *
   * @param: @param account  我方币种账户(转出币的账户)
   * @param: @param address  提币账户(转入币的地址)
   * @param: @param amount   数量
   * @param: @param coinCode 币种类型
   * <p> TODO</p>
   * @author: Shangxl
   * @param: @param t
   * @param: @return
   * @return: JsonResult
   * @Date :          2017年8月19日 下午5:33:43
   * @throws:
   */
  public JsonResult sendTo(AppIcoCoinTransaction t) {
    String ourAccount = t.getOurAccountNumber();
    String address = t.getInAddress();
    String amount = t.getTransactionMoney().subtract(t.getFee()).toString();
    String coinCode = t.getCoinCode();
    String transactionNum = t.getTransactionNum();
    String userName = t.getCustomerName();
    JsonResult jsonResult = new JsonResult();
    try {
      String coinInterFace = PropertiesUtils.APP.getProperty("app.coinInterFace");
      String txStr = "";
      //调用钱包接口转出币
      if ("LMC".equals(coinInterFace)) {/*
				//邻萌宝提币接口
				txStr=IcoCoinInterfaceUtil.lmcSendTo(ourAccount, address, amount, coinCode, transactionNum, userName);
			*/
      } else {
        //默认提币接口
        txStr = IcoCoinInterfaceUtil.sendTo(ourAccount, address, amount, coinCode, transactionNum);
      }
      txStr = txStr.replaceAll("\"", "");
      if (!"".equals(txStr) && null != txStr) {
        System.out.println("-------钱包返回信息:" + txStr);
        Map<String, Object> tx2map = StringUtil.str2map(txStr);
        String order = tx2map.get("msg").toString();
        String code = tx2map.get("code").toString().replaceAll("", "");
        if ("8".equals(code)) {
          t.setOrderNo(order + "_send");
          service.update(t);
          //提币成功处理平台业务
          this.pasePutOrder(t.getId());
          jsonResult.setSuccess(true);
          jsonResult.setMsg("提币申请成功");
        } else if ("6".equals(code)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("资金账户余额不足");
        } else if ("0".equals(code)) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("参数有误！");
        } else {
          jsonResult.setSuccess(false);
          jsonResult.setMsg("未知错误code:" + code + ",msg:" + order);
        }
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("接口调用错误");
      }

    } catch (Exception e) {
      e.printStackTrace();
      jsonResult.setSuccess(false);
      jsonResult.setMsg(e.getMessage());
      return jsonResult;
    }
    return jsonResult;
  }


  public String pasePutOrder(Long id) {
    AppIcoCoinTransaction coinTransaction = service.get(id);
    Integer i = coinTransaction.getStatus();
    if (i == 2) {
      return "NO";
    }

    AppIcoCoinAccount coinAccount = appIcoCoinAccountService.get(coinTransaction.getAccountId());

    Integer tp = coinTransaction.getTransactionType();
    //充币
    if (tp == 1) {

      BigDecimal hotMoney = coinAccount.getHotMoney();
      BigDecimal transactionMoney = coinTransaction.getTransactionMoney();
      BigDecimal k = hotMoney.add(transactionMoney);
      coinAccount.setHotMoney(k);
      // 修改可用余额
      appIcoCoinAccountService.update(coinAccount);

      // 保存可用余额流水
      AppIcoCoinAccountHotRecord hotRecord = new AppIcoCoinAccountHotRecord();
      hotRecord.setAccountId(coinAccount.getId());
      hotRecord.setCustomerId(coinTransaction.getCustomerId());
      hotRecord.setUserName(coinAccount.getUserName());
      hotRecord.setRecordType(1);
      hotRecord.setTransactionMoney(coinTransaction.getTransactionMoney());
      hotRecord.setStatus(5);
      hotRecord.setRemark("可用余额流水已记录成功 ");
      hotRecord.setCurrencyType(coinTransaction.getCurrencyType());
      hotRecord.setCoinCode(coinTransaction.getCoinCode());
      hotRecord.setWebsite(coinTransaction.getWebsite());
      hotRecord.setTransactionNum(coinTransaction.getTransactionNum());
      hotRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));
      appIcoCoinAccountHotRecordService.save(hotRecord);

      // 修改订单
      coinTransaction.setStatus(2);
      coinTransaction.setUserId(coinAccount.getCustomerId());
      service.update(coinTransaction);

      return "OK";
    }
    //提币
    if (tp == 2) {

      BigDecimal transactionMoney = coinTransaction.getTransactionMoney();
      BigDecimal coldMoney = coinAccount.getColdMoney();
      BigDecimal l = coldMoney.subtract(transactionMoney);
      coinAccount.setColdMoney(l);
      // 修改可用余额
      appIcoCoinAccountService.update(coinAccount);

      // 保存可用余额流水
      AppIcoCoinAccountHotRecord hotRecord = new AppIcoCoinAccountHotRecord();
      hotRecord.setAccountId(coinAccount.getId());
      hotRecord.setCustomerId(coinTransaction.getCustomerId());
      hotRecord.setUserName(coinAccount.getUserName());
      hotRecord.setRecordType(2);
      hotRecord.setTransactionMoney(
          coinTransaction.getTransactionMoney().subtract(coinTransaction.getFee()));
      hotRecord.setStatus(5);
      hotRecord.setRemark("可用余额明细已记录 ");
      hotRecord.setCurrencyType(coinTransaction.getCurrencyType());
      hotRecord.setCoinCode(coinTransaction.getCoinCode());
      hotRecord.setWebsite(coinTransaction.getWebsite());

      hotRecord.setTransactionNum(coinTransaction.getTransactionNum());
      hotRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));
      appIcoCoinAccountHotRecordService.save(hotRecord);

      // 保存可用余额流水--手续费
      AppIcoCoinAccountHotRecord fee = new AppIcoCoinAccountHotRecord();
      fee.setAccountId(coinAccount.getId());
      fee.setCustomerId(coinTransaction.getCustomerId());
      fee.setUserName(coinAccount.getUserName());
      fee.setRecordType(1);
      fee.setTransactionMoney(coinTransaction.getFee());
      fee.setStatus(5);
      fee.setRemark("可用余额明细已记录 ");
      fee.setCurrencyType(coinTransaction.getCurrencyType());
      fee.setCoinCode(coinTransaction.getCoinCode());
      fee.setWebsite(coinTransaction.getWebsite());

      fee.setTransactionNum(coinTransaction.getTransactionNum());
      fee.setSaasId(RpcContext.getContext().getAttachment("saasId"));
      appIcoCoinAccountHotRecordService.save(fee);

      // 保存冻结流水明细
      AppIcoCoinAccountColdRecord coldRecord = new AppIcoCoinAccountColdRecord();
      coldRecord.setAccountId(coinAccount.getId());
      coldRecord.setCustomerId(coinTransaction.getCustomerId());
      coldRecord.setUserName(coinAccount.getUserName());
      coldRecord.setRecordType(2);
      coldRecord.setTransactionMoney(
          coinTransaction.getTransactionMoney().subtract(coinTransaction.getFee()));
      coldRecord.setStatus(5);
      coldRecord.setRemark("冻结余额明细已记录");
      coldRecord.setCurrencyType(coinTransaction.getCurrencyType());
      coldRecord.setCoinCode(coinTransaction.getCoinCode());
      coldRecord.setWebsite(coinTransaction.getWebsite());
      coldRecord.setTransactionNum(coinTransaction.getTransactionNum());
      coldRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));

      appIcoCoinAccountColdRecordService.save(coldRecord);

      // 保存冻结流水明细--手续费
      AppIcoCoinAccountColdRecord fee2 = new AppIcoCoinAccountColdRecord();
      fee2.setAccountId(coinAccount.getId());
      fee2.setCustomerId(coinTransaction.getCustomerId());
      fee2.setUserName(coinAccount.getUserName());
      fee2.setRecordType(2);
      fee2.setTransactionMoney(coinTransaction.getFee());
      fee2.setStatus(5);
      fee2.setRemark("冻结余额明细已记录");
      fee2.setCurrencyType(coinTransaction.getCurrencyType());
      fee2.setCoinCode(coinTransaction.getCoinCode());
      fee2.setWebsite(coinTransaction.getWebsite());
      fee2.setTransactionNum(coinTransaction.getTransactionNum());
      fee2.setSaasId(RpcContext.getContext().getAttachment("saasId"));

      appIcoCoinAccountColdRecordService.save(fee2);
      // 修改订单
      coinTransaction.setStatus(2);
      coinTransaction.setUserId(coinAccount.getCustomerId());
      service.update(coinTransaction);
      RemoteAppOurAccountService remoteAppOurAccountService = (RemoteAppOurAccountService) ContextUtil
          .getBean("remoteAppOurAccountService");
      //我方提币账户 ico提币账户type 3
      AppOurAccount ourAccount = remoteAppOurAccountService
          .findAppOurAccount(ContextUtil.getWebsite(), coinTransaction.getCoinCode(),
              Integer.valueOf("3"));
      //提币
      remoteAppOurAccountService
          .ourAccountChange(ourAccount, coinTransaction, coinTransaction.getOutAddress(), "提币记录",
              "");
      //提币手续费
      remoteAppOurAccountService
          .ourAccountChange(ourAccount, coinTransaction, coinTransaction.getOutAddress(), "提币手续费记录",
              "fee");

      return "OK";
    } else {
      return "NO";
    }

  }


}
