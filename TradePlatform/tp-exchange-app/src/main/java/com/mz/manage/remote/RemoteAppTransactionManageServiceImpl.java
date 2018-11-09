/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年4月1日 上午11:18:14
 */
package com.mz.manage.remote;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mz.account.fund.model.AppBankCard;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.fund.model.AppTransaction;
import com.mz.core.annotation.ThirdPayControllerLog;
import com.mz.core.mvc.model.AppConfig;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.lend.model.ExDmPing;
import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.transaction.model.ExDmCustomerPublicKey;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.pakgclass.OrderParameter;
import com.mz.redis.common.utils.RedisService;
import com.mz.remote.RemoteThirdInterfaceService;
import com.mz.util.QueryFilter;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.serialize.ObjectUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.account.fund.service.AppAccountService;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.account.fund.service.AppOurAccountService;
import com.mz.account.fund.service.AppTransactionService;
import com.mz.change.remote.account.RemoteExAmineOrderService;
import com.mz.change.remote.account.RemoteExDigitalmoneyAccountService;
import com.mz.change.remote.dmtransaction.RemoteExDmTransactionService;
import com.mz.core.constant.CodeConstant;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.lend.service.ExDmLendService;
import com.mz.exchange.lend.service.ExDmPingService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.exchange.remote.account.RemoteExProductService;
import com.mz.exchange.transaction.service.ExDmCustomerPublicKeyService;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.manage.remote.model.AppAccountManage;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.AppOurAccountManage;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.ExDigitalmoneyAccountManage;
import com.mz.manage.remote.model.ExDmCustomerPublicKeyManage;
import com.mz.manage.remote.model.ExProductManage;
import com.mz.manage.remote.model.LmcTransfer;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.entrust.DifCustomer;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.redis.model.Accountadd;
import com.mz.web.remote.RemoteAppConfigService;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;

public class RemoteAppTransactionManageServiceImpl implements RemoteAppTransactionManageService {

  @Resource
  private ExDmPingService exDmPingService;

  @Resource
  private AppTransactionService appTransactionService;

  @Resource
  private AppAccountService appAccountService;

  @Resource
  private RemoteManageService remoteManageService;

  @Resource
  private ExDmTransactionService exDmTransactionService;

  @Resource
  private ExDmCustomerPublicKeyService exDmCustomerPublicKeyService;

  @Resource
  private ExProductService exProductService;

  @Resource
  private ExDigitalmoneyAccountService exDigitalmoneyAccountService;

  @Resource
  private RemoteExProductService remoteExProductService;

  @Resource
  private RemoteExDigitalmoneyAccountService remoteExDigitalmoneyAccountService;
  @Resource
  private AppPersonInfoService appPersonInfoService;
  @Resource
  private AppCustomerService appCustomerService;

  @Resource
  private AppOurAccountService appOurAccountService;
  @Resource
  private RemoteThirdInterfaceService remoteThirdInterfaceService;

  @Resource
  private MessageProducer messageProducer;

  @Resource
  private ExOrderInfoService exOrderInfoService;

  @Resource
  private ExDmLendService exDmLendService;

  public FrontPage findTransaction(Map<String, String> params) {
    return appTransactionService.findTransaction(params);
  }

  public void save(AppTransactionManage appTransactionManage) {
    AppTransaction appTransaction = common(appTransactionManage);
    appTransactionService.save(appTransaction);
  }


  //*********************************************银行卡*********************************************
  @Resource
  private AppBankCardService appBankCardService;

  @Override
  public void save(AppBankCardManage appBankCardManage) {
    AppBankCard appBankCard = common(appBankCardManage);
    //add by zongwei 20180512 只能保存一直银行卡信息 begin
    QueryFilter queryFilter = new QueryFilter(AppBankCard.class);
    queryFilter.addFilter("customerId=", appBankCardManage.getCustomerId());
    List<AppBankCard> find = appBankCardService.find(queryFilter);
    //add by zongwei 20180512 只能保存一直银行卡信息 end
    if (!find.isEmpty() && find != null) {
      appBankCard.setId(find.get(0).getId());
      appBankCardService.update(appBankCard);
    } else {
      appBankCardService.save(appBankCard);
    }
  }

  @Override
  public List<AppBankCardManage> findByCustomerId(Long id) {
    String saasId = (String) RpcContext.getContext().getAttachment("saasId");
    QueryFilter queryFilter = new QueryFilter(AppBankCard.class);
    queryFilter.setSaasId(saasId);
    queryFilter.addFilter("customerId=", id);
    queryFilter.addFilter("isDelete=", 0);
		/*queryFilter.addFilter("currencyType=", ContextUtil.getRemoteCurrencyType());
		queryFilter.addFilter("website=", ContextUtil.getRemoteWebsite());*/
    List<AppBankCard> find = appBankCardService.find(queryFilter);
    List<AppBankCardManage> list = new ArrayList<AppBankCardManage>();
    for (int i = 0; i < find.size(); i++) {
      AppBankCardManage appBankCardManage = common(find.get(i));
      list.add(appBankCardManage);
    }
    return list;
  }

  @Override
  public RemoteResult delete(Long id) {
    try {
      AppBankCard appBankCard = appBankCardService.get(id);
      QueryFilter queryFilter = new QueryFilter(AppTransaction.class);
      QueryFilter addFilter = queryFilter
          .addFilter("custromerAccountNumber=", appBankCard.getCardNumber());
      List<AppTransaction> listAppTransaction = appTransactionService.find(addFilter);
      for (int i = 0; i < listAppTransaction.size(); i++) {
        Integer status = listAppTransaction.get(i).getStatus();
        if (status == 1) {
          RemoteResult remoteResult = new RemoteResult();

          return remoteResult.setSuccess(false).setMsg("inUsing_card_no_delete");
        }
      }
      appBankCard.setIsDelete(1);
      appBankCardService.update(appBankCard);
      return new RemoteResult().setSuccess(true);
    } catch (Exception e) {
      e.printStackTrace();
      return new RemoteResult().setSuccess(false);
    }
  }


  @Override
  public AppBankCardManage get(String cardname) {
    AppBankCardManage appBankCardManage = null;
    QueryFilter qf = new QueryFilter(AppBankCard.class);
    qf.addFilter("cardNumber=", cardname);
    if (ContextUtil.EN.equals(ContextUtil.getWebsite())) {
      qf.addFilter("website=", ContextUtil.EN);
    } else {
      qf.addFilter("website=", ContextUtil.CN);
    }
    AppBankCard _appBankCard = appBankCardService.get(qf);
    if (_appBankCard != null) {
      appBankCardManage = common(_appBankCard);
    }

    return appBankCardManage;
  }

  @Override
  public AppBankCardManage get(Long cardId) {
    AppBankCardManage appBankCardManage = null;
    QueryFilter qf = new QueryFilter(AppBankCard.class);
    qf.addFilter("id=", cardId);
		/*if(ContextUtil.EN.equals(ContextUtil.getWebsite())){
			qf.addFilter("website=", ContextUtil.EN);
		}else{
			qf.addFilter("website=", ContextUtil.CN);
		}*/
    AppBankCard _appBankCard = appBankCardService.get(qf);
    if (_appBankCard != null) {
      appBankCardManage = common(_appBankCard);
    }

    return appBankCardManage;
  }

  public RemoteResult saveBankCard(User user, AppBankCardManage appBankCardManage) {
    RemoteResult remoteResult = new RemoteResult();
    if (user != null) {
      appBankCardManage.setSaasId(ContextUtil.getSaasId());
      appBankCardManage.setCustomerId(user.getCustomerId());
      appBankCardManage.setCardName(appBankCardManage.getCardName());
      appBankCardManage.setSurName(appBankCardManage.getSurName());
      appBankCardManage.setTrueName(appBankCardManage.getTrueName());

      AppBankCardManage _appBankCard = this.get(appBankCardManage.getCardNumber());
      if (_appBankCard != null && _appBankCard.getId() != null) {
        remoteResult.setSuccess(false);
        remoteResult.setMsg("bankcode_no_repeat");
        return remoteResult;
      }

      AppAccountManage appAccount = remoteManageService
          .getByCustomerIdAndType(user.getCustomerId(), ContextUtil.getCurrencyType(),
              ContextUtil.getWebsite());
      if (null != appAccount) {
        appBankCardManage.setAccountId(appAccount.getId());
        appBankCardManage.setUserName(user.getUsername());
        appBankCardManage.setCurrencyType(appAccount.getCurrencyType());
        appBankCardManage.setWebsite(appAccount.getWebsite());

        this.save(appBankCardManage);
        remoteResult.setSuccess(true);
        remoteResult.setObj(appBankCardManage);
        remoteResult.setMsg("success");
      } else {
        remoteResult.setSuccess(false);
        remoteResult.setMsg("xnzh_no_exist");
      }
    } else {
      remoteResult.setSuccess(false);
      remoteResult.setMsg("user_no_exist");
    }
    return remoteResult;
  }

  //************************************************人民币提现*************************************************************

  @Override
  public BigDecimal countByDate(String[] type, String beginDate,
      String endDate, String[] status, String userName) {
    BigDecimal money = new BigDecimal(0);
    money = appTransactionService.countByDate(type, beginDate, endDate, status, userName);
    return money;
  }

  @Override
  public void rmbwithdraw(AppAccountManage appAccount, AppTransactionManage appTransactionManage) {
    AppTransaction appTransaction = common(appTransactionManage);
    appTransactionService.save(appTransaction);
    appAccountService.freezeAccountSelf(appAccount.getId(),
        appTransaction.getTransactionMoney().subtract(appTransaction.getFee()),
        appTransaction.getTransactionNum(), "人民币提现", null, null);
    appAccountService.freezeAccountSelf(appAccount.getId(), appTransaction.getFee(),
        appTransaction.getTransactionNum() + "-fee", "人民币提现手续费", null, null);

  }

  //提现 手续费
  public BigDecimal withdrawFee(BigDecimal amount, String type) {

    BigDecimal withdrawFeeRate = new BigDecimal(0);
    BigDecimal withdrawAmount = new BigDecimal(0);
    BigDecimal fee = new BigDecimal(0);
    RemoteAppConfigService service = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");

    String id2 = ContextUtil.getSaasId();
    RpcContext.getContext().setAttachment("saasId", id2);

    if (null != type && !"".equals(type)) {
      if (type.equals("1")) {
        type = "onlineWithdrawFeeRate";
      }
      List<AppConfig> list = service.getConfigInfo(type);
      if (list.size() > 0) {
        withdrawFeeRate = new BigDecimal(list.get(0).getValue());
      }
      if (null != amount && !"".equals(amount)) {
        withdrawAmount = amount;
      }
      fee = withdrawAmount.multiply(withdrawFeeRate).divide(new BigDecimal(100))
          .setScale(2, BigDecimal.ROUND_HALF_EVEN);

    } else {

    }

    return fee;
  }

  /**
   * 提现记录保存
   *
   * @param accountPassWord 交易密码
   * @param bankCardId 银行卡ID
   * @param withdrawCode 短信验证码
   */
  public RemoteResult rmbwithdraw(String accountPassWord, String bankCardId, String withdrawCode,
      String accountpwSmsCode, User user, String encryString, AppTransactionManage appTransaction) {

    if (DifCustomer.isInClosePlateAndClose()) {
      return new RemoteResult().setMsg("js_no_recharge");
    }

    String[] rt = exDmPingService.checkLending(user.getCustomerId());
    if (rt[0].equals(CodeConstant.CODE_FAILED)) {
      return new RemoteResult().setMsg("warning_money_ping");
    }

    AppBankCardManage bankCard = this.get(Long.valueOf(bankCardId));
    appTransaction.setCustromerAccountNumber(bankCard.getCardNumber());
    appTransaction.setBankNum(bankCard.getCardBank());
    RemoteResult remoteResult = new RemoteResult();

    String currencyType = ContextUtil.getCurrencyType();
    String website = ContextUtil.getWebsite();
    AppOurAccountManage ourAccount = remoteManageService.getOurAccount(website, currencyType, "0");
    if (user != null) {
      //判断用户是否低于平仓线
      QueryFilter PFilter = new QueryFilter(ExDmPing.class);
      PFilter.addFilter("customerId=", user.getCustomerId());
      PFilter.addFilter("status=", 1);
      List<ExDmPing> exDmPings = exDmPingService.find(PFilter);
      if (exDmPings != null && exDmPings.size() > 0) {
        remoteResult.setMsg("warning_ping");
        return remoteResult;
      }

      Integer customerType = user.getCustomerType();
      if (customerType.compareTo(Integer.valueOf(2)) != 0) {
        ContextUtil.setRemoteCurrencyType();
        ContextUtil.setRemoteWebsite();
				
				/*if (!accountpwSmsCode.equals(encryString)) {
					remoteResult.setSuccess(false);
					remoteResult.setMsg("交易密码不正确");
					return remoteResult;
				}*/

        AppAccountManage appAccount = remoteManageService
            .getByCustomerIdAndType(user.getCustomerId(), ContextUtil.getCurrencyType(),
                ContextUtil.getWebsite());
        if (appAccount == null) {
          remoteResult.setMsg("xlzh_is_null");
          return remoteResult;
        }
        if (appTransaction.getTransactionMoney() == null) {
          remoteResult.setSuccess(false);
          remoteResult.setMsg("jymoney_is_null");
          return remoteResult;
        }
        if (appTransaction.getTransactionMoney().compareTo(appAccount.getHotMoney()) > 0) {
          remoteResult.setSuccess(false);
          remoteResult.setMsg("jym_gt_xnzhm");
          return remoteResult;
        }

        //判断最大提现额度
        String beginDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd 00:00:00");
        String endDate = DateUtil
            .dateToString(DateUtil.addDay(new Date(), 1), "yyyy-MM-dd 00:00:00");
        BigDecimal allMoney = countByDate(new String[]{"4", "2"}, beginDate, endDate,
            new String[]{"1", "2"}, user.getMobile());
        BigDecimal maxMoney = null;

        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);
        maxMoney = new BigDecimal(parseObject.get("maxWithdrawMoney").toString());
        System.out.println("today_max" + ":" + allMoney + "      " + maxMoney);
        if (allMoney.add(appTransaction.getTransactionMoney()).compareTo(maxMoney) > 0) {
          return new RemoteResult().setMsg("tx_gt_today_max");
        }

        BigDecimal maxWithdrawMoneyOneTime = new BigDecimal(
            parseObject.get("maxWithdrawMoneyOneTime").toString());
        if (appTransaction.getTransactionMoney().compareTo(maxWithdrawMoneyOneTime) > 0) {
          return new RemoteResult().setMsg("tx_gt_one!");
        }

        // saasId
        appTransaction.setSaasId(user.getSaasId());
        // 交易类型 线下提现
        appTransaction.setTransactionType(4);
        // 设置备注
        appTransaction.setRemark(RandomStringUtils.random(8, false, true));
        //设置我方账号
        appTransaction.setOurAccountNumber(ourAccount.getAccountNumber());
        // 设置用户id
        appTransaction.setCustomerId(user.getCustomerId());
        // 用户登录名
        appTransaction.setUserName(user.getMobile());
        // 真实姓名
        appTransaction.setTrueName(user.getTruename());
        //姓氏
        appTransaction.setSurname(user.getSurname());
        // 交易订单号
        appTransaction.setTransactionNum(transactionNum("01"));
        //手续费
        BigDecimal fee = withdrawFee(appTransaction.getTransactionMoney(), "1");
        appTransaction.setFee(fee);
        //实际到账金额
        appTransaction.setTransactionMoney(appTransaction.getTransactionMoney());
        // 设置账户ID
        appTransaction.setAccountId(appAccount.getId());
        appTransaction.setCurrencyType(appAccount.getCurrencyType());
        appTransaction.setWebsite(appAccount.getWebsite());
        appTransaction.setStyle(0);

        //判断提现金额是否超过该用户的提现审核额度
        //当提现接口没有开启或者接口开启但提现超过审核额度都进入后台审核
        //进行审核 状态 待审核
        appTransaction.setStatus(1);

        //两次调用改成一次调用(保存流水，冻结金额)
//				RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
//				this.rmbwithdraw(appAccount,appTransaction);

        //保存提现记录
        AppTransaction at = common(appTransaction);
        appTransactionService.save(at);

        //----发送mq消息----start
        //热账户减少
        Accountadd accountadd = new Accountadd();
        accountadd.setAccountId(appTransaction.getAccountId());
        accountadd.setMoney(appTransaction.getTransactionMoney().multiply(new BigDecimal(-1)));
        accountadd.setMonteyType(1);
        accountadd.setRemarks(32);
        accountadd.setAcccountType(0);
        accountadd.setTransactionNum(appTransaction.getTransactionNum());

        //冷账户增加
        Accountadd accountadd2 = new Accountadd();
        accountadd2.setAccountId(appTransaction.getAccountId());
        accountadd2.setMoney(appTransaction.getTransactionMoney());
        accountadd2.setMonteyType(2);
        accountadd2.setAcccountType(0);
        accountadd2.setRemarks(32);
        accountadd2.setTransactionNum(appTransaction.getTransactionNum());
        List<Accountadd> list = new ArrayList<Accountadd>();
        list.add(accountadd);
        list.add(accountadd2);
        messageProducer.toAccount(JSON.toJSONString(list));
        //----发送mq消息----end

        remoteResult.setSuccess(true);
        remoteResult.setObj(appTransaction);
        remoteResult.setMsg("tx_success");

      } else {
        remoteResult.setSuccess(false);
        remoteResult.setMsg("yi_no_tx");
      }
      return remoteResult;
    } else {
      remoteResult.setSuccess(false);
      remoteResult.setMsg("before_login");
    }
    return remoteResult;
  }

  //********************************************************虚拟货币*******************************************************************

  public FrontPage findExdmtransaction(Map<String, String> map) {
    return exDmTransactionService.findExdmtransaction(map);
  }

  public List<ExDmCustomerPublicKeyManage> listPublic(Long id) {
    QueryFilter qf = new QueryFilter(ExDmCustomerPublicKey.class);
    qf.addFilter("customerId=", id);
    List<ExDmCustomerPublicKey> list = exDmCustomerPublicKeyService.find(qf);
    List<ExDmCustomerPublicKeyManage> listmanage = new ArrayList<ExDmCustomerPublicKeyManage>();
    for (int i = 0; i < list.size(); i++) {
      ExDmCustomerPublicKeyManage ex = common(list.get(i));
      listmanage.add(ex);
    }
    return listmanage;
  }

  public List<ExProductManage> listProduct() {
    List<ExProduct> list = exProductService.findAll();
    List<ExProductManage> listmanage = new ArrayList<ExProductManage>();
    for (int i = 0; i < list.size(); i++) {
      ExProductManage ex = common(list.get(i));
      listmanage.add(ex);
    }
    return listmanage;
  }


  public void deletePublieKey(Long id) {
    exDmCustomerPublicKeyService.delete(id);
  }


  //币账户查询
  public List<ExDigitalmoneyAccountManage> listexd(Long id, String language) {

    AppCustomer appCustomer = appCustomerService.get(id);
    AppPersonInfo appPersonInfo = appPersonInfoService
        .get(new QueryFilter(AppPersonInfo.class).addFilter("customerId=", appCustomer.getId()));
    //开通虚拟币账户
//		RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil.getBean("remoteExProductService");
//		remoteExProductService.openDmAccount(appCustomer,appPersonInfo,null,ContextUtil.getWebsite(),language);

    QueryFilter qf = new QueryFilter(ExDigitalmoneyAccount.class);
    qf.addFilter("customerId=", id);
    List<ExDigitalmoneyAccount> list = exDigitalmoneyAccountService.find(qf);
    List<ExDigitalmoneyAccountManage> listex = new ArrayList<ExDigitalmoneyAccountManage>();
    for (int i = 0; i < list.size(); i++) {
      listex.add(common(list.get(i)));
    }

    for (ExDigitalmoneyAccountManage obj : listex) {
      ExProduct exProduct = exProductService
          .get(new QueryFilter(ExProduct.class).addFilter("coinCode=", obj.getCoinCode()));
      //提币手续费
      obj.setPaceFeeRate(
          exProduct.getPaceFeeRate().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_DOWN));
      //单次最小提币
      obj.setLeastPaceNum(
          exProduct.getLeastPaceNum() == null ? new BigDecimal(0) : exProduct.getLeastPaceNum());
      //一日最大提币
      obj.setOneDayPaceNum(
          exProduct.getOneDayPaceNum() == null ? new BigDecimal(0) : exProduct.getOneDayPaceNum());
    }

    return listex;
  }


  //币账户查询
  public List<ExDigitalmoneyAccountManage> listcoin(String coinCode) {

    List<ExDigitalmoneyAccountManage> listex = new ArrayList<ExDigitalmoneyAccountManage>();

    //提币手续费
    for (ExDigitalmoneyAccountManage obj : listex) {
      ExProduct exProduct = exProductService
          .get(new QueryFilter(ExProduct.class).addFilter("coinCode=", coinCode));
      //提币手续费
      obj.setPaceFeeRate(exProduct.getPaceFeeRate());
      //单次最小提币
      obj.setLeastPaceNum(
          exProduct.getLeastPaceNum() == null ? new BigDecimal(0) : exProduct.getLeastPaceNum());
      //一日最大提币
      obj.setOneDayPaceNum(
          exProduct.getOneDayPaceNum() == null ? new BigDecimal(0) : exProduct.getOneDayPaceNum());
    }
    return listex;
  }

  public List<ExDmCustomerPublicKeyManage> getList(Long id) {
    QueryFilter qf = new QueryFilter(ExDmCustomerPublicKey.class);
    qf.addFilter("customerId=", id);
    List<ExDmCustomerPublicKey> list = exDmCustomerPublicKeyService.find(qf);
    List<ExDmCustomerPublicKeyManage> listex = new ArrayList<ExDmCustomerPublicKeyManage>();
    for (int i = 0; i < list.size(); i++) {
      listex.add(common(list.get(i)));
    }
    return listex;
  }

  public void save(ExDmCustomerPublicKeyManage exDmCustomerPublicKeyManage, User user) {
    exDmCustomerPublicKeyManage.setCreated(new Date());
    exDmCustomerPublicKeyManage.setPublicKeyName(user.getUsername());
    exDmCustomerPublicKeyManage.setCustomerId(user.getCustomerId());
    exDmCustomerPublicKeyManage.setModified(new Date());
    exDmCustomerPublicKeyManage.setSaasId("hurong_system");
    exDmCustomerPublicKeyManage.setTrueName(user.getTruename());
    exDmCustomerPublicKeyManage.setSurname(user.getSurname());
    ExDmCustomerPublicKey e = common(exDmCustomerPublicKeyManage);
    exDmCustomerPublicKeyService.save(e);
  }

  public BigDecimal productWithdrawFeeRate(String coinCode) {
    BigDecimal rate = new BigDecimal(0);
    RemoteQueryFilter filter = new RemoteQueryFilter(ExProduct.class);
    filter.addFilter("coinCode=", coinCode);
    filter.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
    ExProduct product = remoteExProductService.findByCoinCode(filter);
    rate = product.getPaceFeeRate();
    return rate;
  }


  //*************************************************************生成充值汇款单*********************************************************
  public RemoteResult generateRmbdeposit(String surname, String remitter, String bankCode,
      String bankAmount, String bankName, AppTransactionManage appTransaction, User user) {

    if (DifCustomer.isInClosePlateAndClose()) {
      return new RemoteResult().setMsg("cz_no_allow");
    }

    appTransaction.setUserName(user.getUsername());
    // saasId
    //appTransaction.setSaasId(appCustomer.getSaasId());
    // 产生一个8位随机数
    appTransaction.setRemark(RandomStringUtils.random(4, false, true));
    // 设置用户id
    appTransaction.setCustomerId(user.getCustomerId());
    // 用户登录名
    appTransaction.setUserName(user.getUsername());
    appTransaction.setTransactionNum(transactionNum("01"));//成功
    appTransaction.setTransactionType(3);//线下充值
    //金额
    appTransaction.setTransactionMoney(new BigDecimal(bankAmount));
    appTransaction.setStatus(1);//待审核
    appTransaction.setBankNum(bankName);
    appTransaction.setStyle(0);//银行卡充值
    appTransaction.setSurname(remitter);
    appTransaction.setTrueName(surname);
    appTransaction.setCustromerAccountNumber(bankCode);//卡号

    appTransaction.setCardHolder(user.getTruename());
    appTransaction.setFee(new BigDecimal(0));//手续费
    AppAccountManage appAccount = remoteManageService
        .getByCustomerIdAndType(user.getCustomerId(), ContextUtil.getCurrencyType(),
            ContextUtil.getWebsite());
    QueryFilter filter = new QueryFilter(AppOurAccount.class);
    filter.addFilter("isShow=", "1");
    filter.addFilter("currencyType=", "cny");
    List<AppOurAccount> listout = appOurAccountService.find(filter);
    if (listout != null && listout.size() > 0) {
      if (appAccount != null) {

        // 设置账户ID -- 虚拟账户ID
        appTransaction.setAccountId(appAccount.getId());
        Date date = new Date();
        appTransaction.setCreated(date);
        //设置默认的账户类型
        appTransaction.setCurrencyType(appAccount.getCurrencyType());
        //设置默认站点
        appTransaction.setWebsite(appAccount.getWebsite());

        appTransaction.setBankAddress(listout.get(0).getBankAddress());
        appTransaction.setAccountName(listout.get(0).getAccountName());
        appTransaction.setBankName(listout.get(0).getBankName());
        appTransaction.setAccountNumber(listout.get(0).getAccountNumber());
        appTransaction.setOurAccountNumber(listout.get(0).getAccountNumber());

        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String config = redisService.get("configCache:all");
        if (!StringUtils.isEmpty(config)) {
          JSONObject parseObject = JSONObject.parseObject(config);
          String object = (String) parseObject.get("rechargeFeeRate");//线下充值手续费
          BigDecimal fee = new BigDecimal(object).multiply(appTransaction.getTransactionMoney())
              .divide(new BigDecimal(100), 2);
          appTransaction.setFee(fee);
        }
        this.save(appTransaction);

        return new RemoteResult().setSuccess(true).setObj(appTransaction)
            .setMsg(appTransaction.getTransactionNum() + "," + appTransaction.getUserName());
      }
      return new RemoteResult().setSuccess(false).setMsg("xnzh_no_exist");
    }
    return new RemoteResult().setSuccess(false).setMsg("our_no_exist");

  }

  //****************************************************************************************************************************

  public ExDigitalmoneyAccountManage common(ExDigitalmoneyAccount e) {
    ExDigitalmoneyAccountManage ex = new ExDigitalmoneyAccountManage();
    ex.setId(e.getId());
    ex.setHotMoney(e.getHotMoney());
    ex.setColdMoney(e.getColdMoney());
    ex.setPublicKey(e.getPublicKey());
    ex.setCreated(e.getCreated());
    ex.setCoinName(e.getCoinName());
    ex.setCoinCode(e.getCoinCode());
    ex.setCurrencyType(e.getCurrencyType());
    return ex;
  }

  public ExDmCustomerPublicKey common(ExDmCustomerPublicKeyManage e) {
    ExDmCustomerPublicKey ex = new ExDmCustomerPublicKey();
    ex.setCreated(e.getCreated());
    ex.setCurrencyType(e.getCurrencyType());
    ex.setCustomerId(e.getCustomerId());
    ex.setId(e.getId());
    ex.setModified(e.getModified());
    ex.setPublicKey(e.getPublicKey());
    ex.setPublicKeyName(e.getPublicKeyName());
    ex.setRemark(e.getRemark());
    ex.setSaasId(e.getSaasId());
    ex.setTrueName(e.getTrueName());
    ex.setSurname(e.getSurname());
    return ex;
  }

  public ExProductManage common(ExProduct e) {
    ExProductManage ex = new ExProductManage();
    ex.setCoinCode(e.getCoinCode());
    return ex;
  }

  public ExDmCustomerPublicKeyManage common(ExDmCustomerPublicKey e) {
    ExDmCustomerPublicKeyManage ex = new ExDmCustomerPublicKeyManage();
    ex.setCreated(e.getCreated());
    ex.setCurrencyType(e.getCurrencyType());
    ex.setCustomerId(e.getCustomerId());
    ex.setId(e.getId());
    ex.setModified(e.getModified());
    ex.setPublicKey(e.getPublicKey());
    ex.setPublicKeyName(e.getPublicKeyName());
    ex.setRemark(e.getRemark());
    ex.setSaasId(e.getSaasId());
    ex.setTrueName(e.getTrueName());
    return ex;
  }

  public AppTransaction common(AppTransactionManage appTransactionManage) {
    AppTransaction appTransaction = new AppTransaction();
    appTransaction.setAccountId(appTransactionManage.getAccountId());
    appTransaction.setBankAddress(appTransactionManage.getBankAddress());
    appTransaction.setBankName(appTransactionManage.getBankName());
    appTransaction.setBankNum(appTransactionManage.getBankNum());
    appTransaction.setBankProvince(appTransactionManage.getBankProvince());
    appTransaction.setCardHolder(appTransactionManage.getCardHolder());
    appTransaction.setCreated(appTransactionManage.getCreated());
    appTransaction.setCurrencyType(appTransactionManage.getCurrencyType());
    appTransaction.setCustomerId(appTransactionManage.getCustomerId());
    appTransaction.setCustromerAccountNumber(appTransactionManage.getCustromerAccountNumber());
    appTransaction.setFee(appTransactionManage.getFee());
    appTransaction.setId(appTransactionManage.getId());
    appTransaction.setModified(appTransactionManage.getModified());
    appTransaction.setOurAccountNumber(appTransactionManage.getOurAccountNumber());
    appTransaction.setReadyStates(appTransactionManage.getReadyStates());
    appTransaction.setRejectionReason(appTransactionManage.getRejectionReason());
    appTransaction.setRemark(appTransactionManage.getRemark());
    appTransaction.setSaasId(appTransactionManage.getSaasId());
    appTransaction.setStatus(appTransactionManage.getStatus());
    appTransaction.setStyle(appTransactionManage.getStyle());
    appTransaction.setSubBank(appTransactionManage.getSubBank());
    appTransaction.setSubBankNum(appTransactionManage.getSubBankNum());
    appTransaction.setThirdPayName(appTransactionManage.getThirdPayName());
    appTransaction.setTransactionMoney(appTransactionManage.getTransactionMoney());
    appTransaction.setTransactionNum(appTransactionManage.getTransactionNum());
    appTransaction.setTransactionType(appTransactionManage.getTransactionType());
    appTransaction.setTrueName(appTransactionManage.getTrueName());
    appTransaction.setUserId(appTransactionManage.getUserId());
    appTransaction.setUserName(appTransactionManage.getUserName());
    appTransaction.setWebsite(appTransactionManage.getWebsite());
    appTransaction.setSurname(appTransactionManage.getSurname());
    return appTransaction;
  }


  public AppBankCardManage common(AppBankCard _appBankCard) {
    AppBankCardManage appBankCardManage = new AppBankCardManage();
    appBankCardManage.setId(_appBankCard.getId());
    appBankCardManage.setAccountId(_appBankCard.getAccountId());
    appBankCardManage.setBankAddress(_appBankCard.getBankAddress());
    appBankCardManage.setBankProvince(_appBankCard.getBankProvince());
    appBankCardManage.setCardBank(_appBankCard.getCardBank());
    appBankCardManage.setCardName(_appBankCard.getCardName());
    appBankCardManage.setCardNumber(_appBankCard.getCardNumber());
    appBankCardManage.setCreated(_appBankCard.getCreated());
    appBankCardManage.setCurrencyType(_appBankCard.getCurrencyType());
    appBankCardManage.setCustomerId(_appBankCard.getCustomerId());
    appBankCardManage.setModified(_appBankCard.getModified());
    appBankCardManage.setSignBank(_appBankCard.getSignBank());
    appBankCardManage.setSaasId(_appBankCard.getSaasId());
    appBankCardManage.setSubBank(_appBankCard.getSubBank());
    appBankCardManage.setSubBankNum(_appBankCard.getSubBankNum());
    appBankCardManage.setTrueName(_appBankCard.getTrueName());
    appBankCardManage.setUserName(_appBankCard.getUserName());
    appBankCardManage.setWebsite(_appBankCard.getWebsite());
    appBankCardManage.setSurName(_appBankCard.getSurname());
    //add by zongwei 20180511 加支付宝微信等信息 beign
    appBankCardManage.setAlipay(_appBankCard.getAlipay());
    appBankCardManage.setAlipayPicture(_appBankCard.getAlipayPicture());
    appBankCardManage.setWeChat(_appBankCard.getWeChat());
    appBankCardManage.setWeChatPicture(_appBankCard.getWeChatPicture());
    appBankCardManage.setBankProvinceKey(_appBankCard.getBankProvinceKey());
    //add by zongwei 20180511 加支付宝微信等信息 end
    return appBankCardManage;
  }

  public AppBankCard common(AppBankCardManage appBankCardManage) {
    AppBankCard appBankCard = new AppBankCard();
    appBankCard.setId(appBankCardManage.getId());
    appBankCard.setAccountId(appBankCardManage.getAccountId());
    appBankCard.setBankAddress(appBankCardManage.getBankAddress());
    appBankCard.setBankProvince(appBankCardManage.getBankProvince());
    appBankCard.setCardBank(appBankCardManage.getCardBank());
    appBankCard.setCardName(appBankCardManage.getCardName());
    appBankCard.setCardNumber(appBankCardManage.getCardNumber());
    appBankCard.setCreated(appBankCardManage.getCreated());
    appBankCard.setCurrencyType(appBankCardManage.getCurrencyType());
    appBankCard.setCustomerId(appBankCardManage.getCustomerId());
    appBankCard.setModified(appBankCardManage.getModified());
    appBankCard.setSignBank(appBankCardManage.getSignBank());
    appBankCard.setSaasId(appBankCardManage.getSaasId());
    appBankCard.setSubBank(appBankCardManage.getSubBank());
    appBankCard.setSubBankNum(appBankCardManage.getSubBankNum());
    appBankCard.setTrueName(appBankCardManage.getTrueName());
    appBankCard.setSurname(appBankCardManage.getSurName());
    appBankCard.setUserName(appBankCardManage.getUserName());
    appBankCard.setWebsite(appBankCardManage.getWebsite());

    //add by zongwei 20180511 加支付宝微信等信息 beign
    appBankCard.setAlipay(appBankCardManage.getAlipay());
    appBankCard.setAlipayPicture(appBankCardManage.getAlipayPicture());
    appBankCard.setWeChat(appBankCardManage.getWeChat());
    appBankCard.setWeChatPicture(appBankCardManage.getWeChatPicture());
    appBankCard.setBankProvinceKey(appBankCardManage.getBankProvinceKey());
    //add by zongwei 20180511 加支付宝微信等信息 end
    return appBankCard;
  }

  public static String transactionNum(String bussType) {
    SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
    String time = format.format(new Date(System.currentTimeMillis()));
    String randomStr = RandomStringUtils.random(4, false, true);
    if (null == bussType) {
      bussType = "00";
    }
    return bussType + time + randomStr;
  }


  @Override
  public String[] testconfirmRmbRecharge(String surname, String remitter, String bankCode,
      String bankAmount, String bankName, AppTransactionManage appTransaction, User user) {
    String[] rt = new String[2];
    if (StringUtil.isEmpty(bankCode)) {
      bankCode = "testcode";
    }
    if (StringUtil.isEmpty(bankName)) {
      bankCode = "testbankName";
    }
    RemoteResult remoteResult = this
        .generateRmbdeposit(surname, remitter, bankCode, bankAmount, bankName, appTransaction,
            user);
    if (remoteResult.getObj() != null) {
      String[] orderuse = remoteResult.getMsg().split(",");
      QueryFilter fil = new QueryFilter(AppTransaction.class);
      fil.addFilter("transactionNum=", orderuse[0]);
      fil.addFilter("userName=", orderuse[1]);
      List<AppTransaction> list = appTransactionService.find(fil);
      if (null != list && list.size() > 0) {
        AppTransaction a = list.get(0);
        boolean confirm = appTransactionService.confirm(a.getId(), user.getUsername());
        if (confirm) {
          rt[0] = "true";
          rt[1] = "success";
        } else {
          rt[0] = "false";
          rt[1] = "error";
        }
      } else {
        rt[0] = "false";
        rt[1] = "error";
      }

    } else {
      rt[0] = "false";
      rt[1] = "error";
    }

    return rt;
  }

  @Override
  public String[] testCoinRecharge(String coinCode, String amount, User user) {
    String[] rt = new String[2];
    String account = "";
    try {
      account = user.getUsername();
      RemoteExAmineOrderService remoteExAmineOrderService = (RemoteExAmineOrderService) ContextUtil
          .getBean("remoteExAmineOrderService");
      RemoteExDmTransactionService remoteExDmTxService = (RemoteExDmTransactionService) ContextUtil
          .getBean("remoteExDmTransactionService");
      RemoteExDigitalmoneyAccountService remoteExDigService = (RemoteExDigitalmoneyAccountService) ContextUtil
          .getBean("remoteExDigitalmoneyAccountService");
      ExDigitalmoneyAccount exDigitalmoneyAccount = remoteExDigService
          .findByCustomerType(user.getCustomerId(), coinCode, "cny", "cn");

      if (null != exDigitalmoneyAccount) {
        ExDmTransaction exDmTransaction = new ExDmTransaction();
        exDmTransaction.setAccountId(exDigitalmoneyAccount.getId());
        exDmTransaction.setCoinCode(coinCode);
        exDmTransaction.setCreated(new Date());
        exDmTransaction.setCurrencyType(exDigitalmoneyAccount.getCurrencyType());
        exDmTransaction.setCustomerId(exDigitalmoneyAccount.getCustomerId());
        exDmTransaction.setCustomerName(exDigitalmoneyAccount.getUserName());
        exDmTransaction.setTrueName(exDigitalmoneyAccount.getTrueName());
        exDmTransaction.setTime("test");
        exDmTransaction.setTimereceived("test");
        exDmTransaction.setInAddress(exDigitalmoneyAccount.getPublicKey());
        exDmTransaction.setConfirmations("test");
        exDmTransaction.setBlocktime("test");
        exDmTransaction.setFee(new BigDecimal("0"));
        exDmTransaction.setTransactionMoney(new BigDecimal(amount));
        exDmTransaction.setStatus(2);
        exDmTransaction
            .setOrderNo("test" + IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
        exDmTransaction.setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
        // 充币
        exDmTransaction.setTransactionType(1);
        exDmTransaction.setUserId(exDigitalmoneyAccount.getCustomerId());
        exDmTransaction.setWebsite(exDigitalmoneyAccount.getWebsite());

        ExDmTransaction txs = null;

        txs = remoteExDmTxService
            .getTransaction(exDmTransaction.getCustomerName(), exDmTransaction.getOrderNo());
        if (null == txs) {
          System.out.println("保存交易订单！！！");
          //保存交易订单
          remoteExDmTxService.saveTransaction(exDmTransaction);
          txs = remoteExDmTxService
              .getTransaction(exDmTransaction.getCustomerName(), exDmTransaction.getOrderNo());
          //修改资产信息
          remoteExAmineOrderService.chargeAccount(txs);

          txs = null;
        }
        rt[0] = "true";
        return rt;
      } else {
        rt[0] = "false";
        rt[1] = "虚拟币账户为空";
        return rt;
      }
    } catch (Exception e) {
      System.out.println("异常:" + e.getMessage());
      e.printStackTrace();
    }
    rt[0] = "false";
    return rt;
  }

  public BigDecimal witfee() {
    RemoteAppConfigService service = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    List<AppConfig> list = service.getConfigInfo("onlineWithdrawFeeRate");
    if (list.size() > 0) {
      return new BigDecimal(list.get(0).getValue());
    }
    return new BigDecimal(0);
  }

  @Override
  @ThirdPayControllerLog
  public String[] lmcTransfer(LmcTransfer lmcTransfer) {
    return remoteThirdInterfaceService.lmcTransfer(lmcTransfer);
  }

  @Override
  public String[] walletTransferSum(LmcTransfer transfer) {
    return remoteThirdInterfaceService.walletTransferSum(transfer);
  }

  @Override
  public String[] listwalletTransfer(LmcTransfer transfer) {
    return remoteThirdInterfaceService.listwalletTransfer(transfer);
  }

  @Override
  public List<ExDmCustomerPublicKeyManage> getList(Long customerId, String coinCode) {

    QueryFilter qf = new QueryFilter(ExDmCustomerPublicKey.class);
    qf.addFilter("customerId=", customerId);
    qf.addFilter("currencyType=", coinCode);
    List<ExDmCustomerPublicKey> list = exDmCustomerPublicKeyService.find(qf);
    List<ExDmCustomerPublicKeyManage> listex = new ArrayList<ExDmCustomerPublicKeyManage>();
    for (int i = 0; i < list.size(); i++) {
      listex.add(common(list.get(i)));
    }
    return listex;

  }

  @Override
  public RemoteResult getOrdervail(User user, String code, String accountpwSmsCode,
      String sessionAccountpwSmsCode, String btcNum, String type, String username, String btcKey) {
    RemoteResult remoteResult = new RemoteResult();
    ExProduct product = remoteExProductService.findByCoinCode(code, "");
    ContextUtil.setRemoteCurrencyType();
    ContextUtil.setRemoteWebsite();
    remoteResult.setSuccess(false);
    // 判断当前用户是否已经登录
    if (user == null) {
      remoteResult.setMsg("before_login");
      return remoteResult;
    }

    String[] rt = exDmPingService.checkLending(user.getCustomerId());
    if (rt[0].equals(CodeConstant.CODE_FAILED)) {
      return new RemoteResult().setMsg("warning_money_ping");
    }

    //AppPersonInfo appPersonInfo = appPersonInfoService.get(user.getCustomerId());
    AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(user.getCustomerId());
    int t = appPersonInfo.getCustomerType();
    if (t != 2) {
      RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
      AppAccountManage appAccount = remoteManageService
          .getByCustomerIdAndType(user.getCustomerId(), ContextUtil.getCurrencyType(),
              ContextUtil.getWebsite());
      if (appAccount.getHotMoney().compareTo(new BigDecimal("0.00")) >= 0) {
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);//ContextUtil.getCurrencyType()
        ExDigitalmoneyAccount digAccount = remoteExDigitalmoneyAccountService
            .findByCustomerType(user.getCustomerId(), code,
                parseObject.get("language_code").toString(), ContextUtil.getWebsite());
        if (digAccount == null) {
          remoteResult.setMsg("xnzh_no_exist");// 虚拟账户不存在
          return remoteResult;
        }
        // 获取前台传的num数值 小数精确到小数点后5位
        // 判断可用余额是否大于转账的金额
        BigDecimal bigBtcNum = new BigDecimal(btcNum);
        // 可用的总数量
        BigDecimal hotMoney = digAccount.getHotMoney();
        // 每次提币的最小数量
        BigDecimal leastPaceNum = product.getLeastPaceNum();
        // 每日最大提币数量
        BigDecimal oneDayPaceNum = product.getOneDayPaceNum();
        BigDecimal todayGetNum = exDmTransactionService
            .findTransactionByCustomer(user.getMobile(), code, "2");
        // 判断是否小于可用余额
        int i2 = hotMoney.compareTo(bigBtcNum);
        // 判断是否大于单笔最小提现数量
        int i3 = leastPaceNum.compareTo(bigBtcNum);
        // 判断是否小于单日提现数量
        int i4 = (todayGetNum.add(bigBtcNum)).compareTo(oneDayPaceNum);

        if (i2 == -1) {
          remoteResult.setMsg("tb_gt_maxavailable");// 提现的金额超出了最大可用余额
          return remoteResult;
        }
        if (i3 == 1) {//提币小于单笔提币数量
          remoteResult.setMsg("tb_nlt_timemin");
          return remoteResult;
        }
        if (i4 == 1) {
          remoteResult.setMsg("tb_ngt_todaymax");
          return remoteResult;
        }
        ContextUtil.setRemoteCurrencyType();
        ContextUtil.setRemoteWebsite();
				
				/*QueryFilter filter = new QueryFilter(AppOurAccount.class);
				filter.addFilter("isShow=", "1");
				filter.addFilter("accountType=",1);
				filter.addFilter("currencyType=",code);
				filter.addFilter("openAccountType=",1);
				AppOurAccount account = appOurAccountService.get(filter);
				if(account == null){
					remoteResult.setMsg("wofangzhangbaonull");
					return remoteResult;
				}*/
				
				
			/*	
				BigDecimal btcPrice=new BigDecimal(0);
				BigDecimal mu =new BigDecimal(0);
				if(!code.equals("BTC")){
					btcPrice = remoteManageService.myBtcCount(user.getCustomerId(),code);
					mu = btcPrice.multiply(new BigDecimal(btcNum));
				}else{
					btcPrice=new BigDecimal(1);
					mu = btcPrice.multiply(new BigDecimal(btcNum));
				}
				SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
				String format = matter.format(new Date());
				RemoteQueryFilter q=new RemoteQueryFilter(ExDmTransaction.class);
			    q.addFilter("customerId=", user.getCustomerId());
				q.addFilter("btcDate=", format);
				List<ExDmTransaction> find = remoteExDmTransactionService.find(q);*/
        //提取btc总数
        //实名每日提币量
				/*String cardCurrency=getCnfigValue("card_Currency");
				//未实名每日提币量
				String uncardCurrency=getCnfigValue("uncard_Currency");


				BigDecimal num =BigDecimal.ZERO;
				BigDecimal count = new BigDecimal(cardCurrency);
				BigDecimal uncount = new BigDecimal(uncardCurrency);
				for (ExDmTransaction exDmTransaction : find) {
					 num=num.add(exDmTransaction.getBtcCount());
				}
			    Integer states = user.getStates();
			    //已实名
				BigDecimal bc=new BigDecimal(0);
				if(states==2){
					//当前提币数量与历史之和
					bc=num.add(mu);
					int newpri = bc.compareTo(count);
					if (newpri != -1) {
						remoteResult.setMsg("jinritibiliangyiman");
						return remoteResult;
					}
					//未实名
				}else if(states==1||states==0||states==3){
					bc=num.add(mu);
					int newpri = bc.compareTo(uncount);
					if (newpri!=-1) {

						remoteResult.setMsg("jinritibiliangyiman");// 提现的金额超出了最大可用余额
						return remoteResult;
					}
				}*/

      } else {
        remoteResult.setMsg("提现数量不能为空");
        return remoteResult;
      }
    } else {
      // 返回false
      remoteResult.setMsg("乙类账户不允许提币");
      return remoteResult;
    }
    return remoteResult.setSuccess(true);
  }


  public FrontPage selectFee(Map<String, String> params) {
    return exOrderInfoService.selectFee(params);
  }

  public FrontPage frontselectFee(Map<String, String> params) {
    return exOrderInfoService.frontselectFee(params);
  }

  public static String getCnfigValue(String type) {
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    return remoteAppConfigService.getValueByKey(type);
  }

  /**
   * 提币
   */
  @Override
  public RemoteResult getOrder(User user, String code, String accountpwSmsCode,
      String sessionAccountpwSmsCode,
      String btcNum, String type, String username, String btcKey, String pacecurrecy) {

    // TODO Auto-generated method stub

    //PasswordHelper passwordHelper = new PasswordHelper();
    RemoteResult remoteResult = new RemoteResult();

    ExProduct product = remoteExProductService.findByCoinCode(code, "");

    ContextUtil.setRemoteCurrencyType();
    ContextUtil.setRemoteWebsite();

    remoteResult.setSuccess(false);
    // 判断当前用户是否已经登录
    if (user == null) {
      remoteResult.setMsg("before_login");
      return remoteResult;
    }

    //AppPersonInfo appPersonInfo = appPersonInfoService.get(user.getCustomerId());
    AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(user.getCustomerId());

    int t = appPersonInfo.getCustomerType();
    if (t != 2) {

      // 获得人民币账户
      RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
      AppAccountManage appAccount = remoteManageService
          .getByCustomerIdAndType(user.getCustomerId(), ContextUtil.getCurrencyType(),
              ContextUtil.getWebsite());
      if (appAccount.getHotMoney().compareTo(new BigDecimal("0.00")) >= 0) {

        //校验短信验证码
				/*if (!accountpwSmsCode.equals(sessionAccountpwSmsCode)) {
						remoteResult.setSuccess(false);
						remoteResult.setMsg("tel_code_error");
						return remoteResult;
					}*/

        // 判断密码是否正确
				/*String mdpwd = passwordHelper.encryString(pwd, user.getSalt());
					String passWord = user.getAccountPassWord();
				    if (!mdpwd.equals(passWord)) {
						remoteResult.setMsg("jy_pwd_error");
						return remoteResult;
					}*/

        // 判断该用户是否已经有虚拟账户
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);//ContextUtil.getCurrencyType()
        ExDigitalmoneyAccount digAccount = remoteExDigitalmoneyAccountService
            .findByCustomerType(user.getCustomerId(), code,
                parseObject.get("language_code").toString(), ContextUtil.getWebsite());
        //ExDigitalmoneyAccount digAccount = remoteExDigitalmoneyAccountService.findByCustomerType(user.getCustomerId(), code,ContextUtil.getCurrencyType(),ContextUtil.getWebsite());
        if (digAccount == null) {
          remoteResult.setMsg("xnzh_no_exist");// 虚拟账户不存在
          return remoteResult;
        }

        // 获取前台传的num数值 小数精确到小数点后5位
        // 判断可用余额是否大于转账的金额
        //if(btcNum != null && !"".equals(btcNum) ){
        BigDecimal bigBtcNum = new BigDecimal(btcNum);
        // 可用的总数量
        BigDecimal hotMoney = digAccount.getHotMoney();
        //	BigDecimal money = product.getOneTimePaceNum();
        // 每次提币的最小数量
        BigDecimal leastPaceNum = product.getLeastPaceNum();
        // 每日最大提币数量
        BigDecimal oneDayPaceNum = product.getOneDayPaceNum();

        BigDecimal todayGetNum = exDmTransactionService
            .findTransactionByCustomer(user.getMobile(), code, "2");

        // 判断是否小于可用余额
        int i2 = hotMoney.compareTo(bigBtcNum);
        // 判断是否大于单笔最小提现数量
        int i3 = leastPaceNum.compareTo(bigBtcNum);
        // 判断是否小于单日提现数量
        int i4 = (todayGetNum.add(bigBtcNum)).compareTo(oneDayPaceNum);

        if (i2 == -1) {
          remoteResult.setMsg("tb_gt_maxavailable");// 提现的金额超出了最大可用余额
          return remoteResult;
        }
        if (i3 == 1) {//提币小于单笔提币数量
          remoteResult.setMsg("tb_nlt_timemin");
          return remoteResult;
        }
        if (i4 == 1) {
          remoteResult.setMsg("tb_ngt_todaymax");
          return remoteResult;
        }

        ContextUtil.setRemoteCurrencyType();
        ContextUtil.setRemoteWebsite();

        //
        //				QueryFilter filter = new QueryFilter(AppOurAccount.class);
        //				filter.addFilter("isShow=", "1");
        //				filter.addFilter("accountType=",1);
        //				filter.addFilter("currencyType=",code);
        //				filter.addFilter("openAccountType=",1);
        //				AppOurAccount account = appOurAccountService.get(filter);

        //if(null!=account){
        // 创建包装类
        OrderParameter orderParameter = new OrderParameter();

        ExProduct exProduct = exProductService
            .get(new QueryFilter(ExProduct.class).addFilter("coinCode=", code));
        //提币手续费
        //BigDecimal  fee=bigBtcNum.multiply(exProduct.getPaceFeeRate()).divide(new BigDecimal(100).setScale(4, BigDecimal.ROUND_HALF_DOWN));
        //按百分比提币
        BigDecimal fee = new BigDecimal(0);
        if (exProduct.getPaceType().equals("1")) {

          BigDecimal bd = new BigDecimal(pacecurrecy);
          fee = bd;
          //fee=bigBtcNum.multiply(bd.divide(new BigDecimal(100).setScale(4, BigDecimal.ROUND_HALF_DOWN)));
          bigBtcNum = bigBtcNum.subtract(fee);
        } else {
          BigDecimal bd = new BigDecimal(pacecurrecy);
          bigBtcNum = bigBtcNum.subtract(bd);
          fee = bd;
          //fee=bigBtcNum.multiply(bd.divide(new BigDecimal(100).setScale(4, BigDecimal.ROUND_HALF_DOWN)));
        }
        // String btcKey = req.getParameter("btcKey");
        orderParameter.setCurrencyId(digAccount.getId());
        orderParameter.setCustomerId(user.getCustomerId());
        orderParameter.setTrueName(appPersonInfo.getTrueName());
        orderParameter.setSurname(appPersonInfo.getSurname());
        orderParameter.setCurrencyNum(
            bigBtcNum.setScale(exProduct.getKeepDecimalForCoin(), BigDecimal.ROUND_DOWN));
        orderParameter.setType(type);
        orderParameter.setCustomerName(username);
        //orderParameter.setOurAccountNumber(account.getAccountNumber());
        orderParameter
            .setFee(fee.setScale(exProduct.getKeepDecimalForCoin(), BigDecimal.ROUND_DOWN));

        //提币地址
        orderParameter.setCurrencyKey(type);
        orderParameter.setCurrencyType(code);
        orderParameter.setWebsite(ContextUtil.getWebsite());
        //orderParameter.setOutAddress(account.getAccountNumber());
        orderParameter.setInAddress(btcKey);
				/*BigDecimal btcPrice=new BigDecimal(0);
					BigDecimal mu =new BigDecimal(0);
					if(!code.equals("BTC")){
						btcPrice = remoteManageService.myBtcCount(user.getCustomerId(),code);
						mu = btcPrice.multiply(new BigDecimal(btcNum));
					}else{
						btcPrice=new BigDecimal(1);
						mu = btcPrice.multiply(new BigDecimal(btcNum));
					}*/
        //等价比特币数量
				/*	if(btcPrice!=null){
					//BigDecimal multiply = bigBtcNum.multiply(btcPrice);
					orderParameter.setBtcCount(btcPrice);
					}else{
					orderParameter.setBtcCount(new BigDecimal(0));
					}*/
				/*	SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
					String format = matter.format(new Date());
					RemoteQueryFilter q=new RemoteQueryFilter(ExDmTransaction.class);
				    q.addFilter("customerId=", user.getCustomerId());
					q.addFilter("btcDate=", format);
					List<ExDmTransaction> find = remoteExDmTransactionService.find(q);*/
        //提取btc总数
        //实名每日提币量
				/*			String cardCurrency=getCnfigValue("card_Currency");
					//未实名每日提币量
					String uncardCurrency=getCnfigValue("uncard_Currency");


					BigDecimal num =BigDecimal.ZERO;
					BigDecimal count = new BigDecimal(cardCurrency);
					BigDecimal uncount = new BigDecimal(uncardCurrency);
					for (ExDmTransaction exDmTransaction : find) {
						 num=num.add(exDmTransaction.getBtcCount());
					}
				    Integer states = user.getStates();
				    //已实名
					BigDecimal bc=new BigDecimal(0);
					if(states==2){
						//当前提币数量与历史之和
						bc=num.add(mu);
						int newpri = bc.compareTo(count);
						if (newpri != -1) {
							remoteResult.setMsg("jinritibiliangyiman");
							return remoteResult;
						}
						//未实名
					}else if(states==1||states==0||states==3){
						bc=num.add(mu);
						int newpri = bc.compareTo(uncount);
						if (newpri!=-1) {

							remoteResult.setMsg("jinritibiliangyiman");// 提现的金额超出了最大可用余额
							return remoteResult;
						}
					}*/
        try {
          //orderParameter.setBtcDate(matter.parse(format));
          String ss = remoteExDigitalmoneyAccountService.setOrder(orderParameter);
					/*if (ss.equals("OK")) {
							remoteResult.setMsg(MessageUtil.getText(MessageConstant.WAIT_ORDER));

							//发送提币短信通知
							SmsParam smsParam = new SmsParam();
							smsParam.setHryMobilephone(appCustomer2.getUserName());
							smsParam.setHrySmstype("sms_withdraw_rmbOrCoin_front");
							smsParam.setHryCode(code);
							SmsSendUtil.sendSmsCode(smsParam);

							remoteResult.setSuccess(true);
							return remoteResult;
						}*/
          remoteResult.setSuccess(true);
        } catch (Exception e) {
          remoteResult.setSuccess(false).setMsg("saveorder_exception");
          System.out.println("保存订单时抛了异常。。。");
        }
        //				}
        //				else{
        //					remoteResult.setMsg("wofangzhangbaonull");
        //					return remoteResult;
        //
        //				}

      } else {
        remoteResult.setMsg("提现数量不能为空");
      }

    } else {
      // 返回false
      remoteResult.setMsg("乙类账户不允许提币");
      return remoteResult;
    }
    remoteResult.setSuccess(true);
    return remoteResult;

  }

  /**
   * 金如悦 -- 是否开启第三方充值接口
   */
  public List<AppConfig> getConfigInfo(String type) {
    RemoteAppConfigService service = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    List<AppConfig> configInfo = service.getConfigInfo(type);
    List<AppConfig> beanList = ObjectUtil
        .beanList(configInfo, AppConfig.class);
    return beanList;
  }

  //币账户查询
  public List<ExDigitalmoneyAccountManage> listexdbycoinCode(Long id, String coinCode) {

    AppCustomer appCustomer = appCustomerService.get(id);
    AppPersonInfo appPersonInfo = appPersonInfoService
        .get(new QueryFilter(AppPersonInfo.class).addFilter("customerId=", appCustomer.getId()));
    //开通虚拟币账户
//		RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil.getBean("remoteExProductService");
//		remoteExProductService.openDmAccount(appCustomer,appPersonInfo,null,ContextUtil.getWebsite(),language);

    QueryFilter qf = new QueryFilter(ExDigitalmoneyAccount.class);
    qf.addFilter("customerId=", id);
    qf.addFilter("coinCode=", coinCode);
    List<ExDigitalmoneyAccount> list = exDigitalmoneyAccountService.find(qf);
    List<ExDigitalmoneyAccountManage> listex = new ArrayList<ExDigitalmoneyAccountManage>();
    for (int i = 0; i < list.size(); i++) {
      listex.add(common(list.get(i)));
    }

    for (ExDigitalmoneyAccountManage obj : listex) {
      ExProduct exProduct = exProductService
          .get(new QueryFilter(ExProduct.class).addFilter("coinCode=", obj.getCoinCode()));
      //提币手续费
      obj.setPaceFeeRate(
          exProduct.getPaceFeeRate().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_DOWN));
      //单次最小提币
      obj.setLeastPaceNum(
          exProduct.getLeastPaceNum() == null ? new BigDecimal(0) : exProduct.getLeastPaceNum());
      //一日最大提币
      obj.setOneDayPaceNum(
          exProduct.getOneDayPaceNum() == null ? new BigDecimal(0) : exProduct.getOneDayPaceNum());
    }

    return listex;
  }

  @Override
  public RemoteResult detetePicture(Long id, String type) {
    try {
      AppBankCard appBankCard = appBankCardService.get(id);
      //如果是取消支付宝支付
      if ("1".equals(type)) {
        appBankCard.setAlipay("");
        appBankCard.setAlipayPicture("");
      }
      if ("2".equals(type)) {
        appBankCard.setWeChat("");
        appBankCard.setWeChatPicture("");
      }
      appBankCardService.update(appBankCard);
      return new RemoteResult().setSuccess(true);
    } catch (Exception e) {
      e.printStackTrace();
      return new RemoteResult().setSuccess(false);
    }
  }


}
