
package com.mz.manage.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.mz.account.fund.model.AppAccount;
import com.mz.account.fund.model.AppAccountRecord;
import com.mz.account.fund.model.AppBankCard;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.fund.model.AppTransaction;
import com.mz.account.remote.RemoteAppAccountService;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.customer.agents.model.AppAgentsCustromer;
import com.mz.customer.businessman.model.C2cTransaction;
import com.mz.customer.businessman.model.OtcOrderTransaction;
import com.mz.customer.businessman.model.OtcTransaction;
import com.mz.customer.commend.model.AppCommendUser;
import com.mz.customer.money.model.AppCommendMoney;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.rank.model.AppCommendRank;
import com.mz.customer.trade.model.AppCommendTrade;
import com.mz.customer.user.model.AppCustomer;
import com.mz.customer.user.model.AppMemberPoint;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.product.model.ProductCommon;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.remote.RemoteThirdPayInterfaceService;
import com.mz.shiro.PasswordHelper;
import com.mz.sms.JuheSendUtils;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.trade.redis.model.*;
import com.mz.util.QueryFilter;
import com.mz.util.UUIDUtil;
import com.mz.util.UniqueRecord;
import com.mz.util.date.DateUtil;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.md5.Md5Encrypt;
import com.mz.util.serialize.Mapper;
import com.mz.util.sys.ContextUtil;
import com.mz.web.dictionary.model.AppDicOnelevel;
import com.mz.account.fund.service.AppAccountService;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.account.fund.service.AppOurAccountService;
import com.mz.account.fund.service.AppTransactionService;
import com.mz.core.constant.CodeConstant;
import com.mz.customer.agents.service.AppAgentsCustromerService;
import com.mz.customer.businessman.service.C2cTransactionService;
import com.mz.customer.businessman.service.OtcOrderTransactionService;
import com.mz.customer.businessman.service.OtcTransactionService;
import com.mz.customer.commend.service.AppCommendUserService;
import com.mz.customer.money.service.AppCommendMoneyService;
import com.mz.customer.person.dao.AppPersonInfoDao;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.customer.rank.service.AppCommendRankService;
import com.mz.customer.rebat.service.AppCommendRebatService;
import com.mz.customer.remote.RemoteAppAgentsService;
import com.mz.customer.trade.service.AppCommendTradeService;
import com.mz.customer.user.dao.AppCustomerDao;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.customer.user.service.AppMemberPointService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.lend.service.ExDmPingService;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.exchange.product.service.ProductCommonService;
import com.mz.exchange.purse.CoinInterfaceUtil;
import com.mz.exchange.remote.account.RemoteExProductService;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.model.AppAccountManage;
import com.mz.manage.remote.model.AppOurAccountManage;
import com.mz.manage.remote.model.AppPersonInfoManage;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.CoinAccount;
import com.mz.manage.remote.model.MyAccountTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.TradeAccount;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.manage.remote.model.bakc.AppCommendBackMoney;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.c2c.C2cOrder;
import com.mz.manage.remote.model.commendCode;
import com.mz.manage.remote.model.otc.OtcOrderTransactionMange;
import com.mz.manage.remote.model.otc.OtcTransactionOrder;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.entrust.dao.ExEntrustDao;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.util.UniqueRecordService;
import com.mz.web.dictionary.service.AppDicOnelevelService;
import com.mz.web.remote.RemoteAppConfigService;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.mz.util.UserRedisUtils;

import static org.nutz.dao.util.Pojos.log;

public class RemoteManageServiceImpl implements RemoteManageService {

  @Resource
  private AppCommendMoneyService appCommendMoneyService;

  @Resource
  private AppCustomerService appCustomerService;
  @Resource
  private AppPersonInfoService appPersonInfoService;

  @Resource
  private ExOrderInfoService exOrderInfoService;

  @Resource
  private ExEntrustService exEntrustService;
  @Resource
  private ExEntrustDao exEntrustDao;
  // @Resource(name = "customerAsAgentsService")
  // private CustomerAsAgentsService customerAsAgentsService;

  @Resource
  private AppAccountService appAccountService;

  @Resource
  private AppPersonInfoDao appPersonInfoDao;
  @Resource
  private AppOurAccountService appOurAccountService;

  @Resource
  private ExProductService exProductService;

  @Resource
  private ExCointoCoinService exCointoCoinService;

  @Resource
  private ExDigitalmoneyAccountService exDigitalmoneyAccountService;
  @Resource
  private ProductCommonService productCommonService;
  @Resource
  private AppTransactionService appTransactionService;
  @Resource
  private AppCustomerDao appCustomerDao;

  @Resource
  private MessageProducer messageProducer;

  @Resource
  private AppAgentsCustromerService appAgentsCustromerService;

  @Autowired
  private RedisService redisService;

  @Resource
  private C2cTransactionService c2cTransactionService;
  @Resource
  private ExDmPingService exDmPingService;
  @Resource
  private AppCommendTradeService appCommendTradeService;

  @Resource
  private AppCommendRankService appCommendRankService;

  @Resource
  private AppCommendUserService appCommendUserService;

  @Resource
  private ExDmTransactionService exDmTransactionService;

  @Resource
  private OtcTransactionService otcTransactionService;

  @Resource
  private AppDicOnelevelService appDicOnelevelService;

  @Resource
  private OtcOrderTransactionService otcOrderTransactionService;

  @Resource
  private AppCommendRebatService appCommendRebatService;

  @Resource
  private AppBankCardService appBankCardService;

  @Resource
  private AppMemberPointService appMemberPointService;

  @Autowired
  private UniqueRecordService uniqueRecordService;

  @Autowired
  private RemoteThirdPayInterfaceService remoteThirdPayInterfaceService;

  @Override
  public void cancelAllExEntrust(EntrustTrade entrustTrade) {
    exEntrustService.cancelAllExEntrust(entrustTrade);
  }

  @Override
  public RemoteResult createC2cOrder(C2cOrder c2cOrder) {

    // add by zongwei 根据数据字典检验未付款订单 20180512 begin
    AppDicOnelevel appDicOnelevel = appDicOnelevelService.getParent("c2climit");
    if (appDicOnelevel != null && ("1").equals(c2cOrder.getTransactionType().toString())) {
      try {
        QueryFilter filter = new QueryFilter(C2cTransaction.class);
        filter.addFilter("customerId=", c2cOrder.getCustomerId());
        filter.addFilter("transactionType=", "1");
        filter.addFilter("status=", "1");
        filter.setPage(1);
        filter.setPageSize(10);
        Page<C2cTransaction> findPage = c2cTransactionService.findPage(filter);
        List<C2cTransaction> list = findPage.getResult();
        if (list.size() >= Integer.valueOf(appDicOnelevel.getItemValue())) {
          return new RemoteResult().setSuccess(false).setMsg("unpaid_order_have_5");
        }
      } catch (Exception e) {
      }
    }
    // add by zongwei 根据数据字典检验未付款订单 20180512 end
    JsonResult jr = c2cTransactionService.createC2cOrder(c2cOrder);
    if (jr != null) {
      if (jr.getSuccess()) {
        return new RemoteResult().setSuccess(true).setObj(jr.getObj());
      } else {
        return new RemoteResult().setMsg(jr.getMsg());
      }
    }
    return new RemoteResult();
  }

  /*
   * add by zongwei Otc创建
   */
  @Override
  public RemoteResult createOtcOrder(OtcTransactionOrder otcTransactionOrder) {

    JsonResult jr = otcTransactionService.createOtcOrder(otcTransactionOrder);
    if (jr != null) {
      if (jr.getSuccess()) {
        return new RemoteResult().setSuccess(true).setObj(jr.getObj());
      } else {
        return new RemoteResult().setMsg(jr.getMsg());
      }
    }
    return new RemoteResult();
  }

  @Override
  public Integer getKeepDecimalForCoin(String CoinCode) {
    ProductCommon productCommon = productCommonService.getProductCommon(CoinCode);
    return productCommon.getKeepDecimalForCoin();
  }

  @Override
  public String getC2cOrderDetail(String transactionNum) {
    return c2cTransactionService.getC2cOrderDetail(transactionNum);
  }

  @Override
  public FrontPage findAllEntrust(Map<String, String> params) {

    return exEntrustService.findPage(params);
  }

  @Override
  public RemoteResult appgetAccountInfo(String coinCode, String fixCode) {
    JSONObject result = new JSONObject();
    List<ExCointoCoin> find = exCointoCoinService
        .find(new QueryFilter(ExCointoCoin.class).addFilter("state=", 1)
            .addFilter("coinCode=", coinCode).addFilter("fixPriceCoinCode=", fixCode));
    if (find.size() > 0) {
      ExCointoCoin e = find.get(0);
      result.put("buyFeeRate", e.getBuyFeeRate());
      result.put("sellFeeRate", e.getSellFeeRate());
    }

    ArrayList<Coin> list = new ArrayList<Coin>();
    for (ExCointoCoin ex : find) {
      Coin coin = new Coin();
      coin.setCoinCode(ex.getCoinCode());
      coin.setFixPriceCoinCode(ex.getFixPriceCoinCode());
      list.add(coin);
    }

    return new RemoteResult().setSuccess(true).setObj(result);
  }

  @Override
  public List<C2cOrder> c2c10Order(Long customerId, String coinCode) {

    QueryFilter filter = new QueryFilter(C2cTransaction.class);
    filter.addFilter("coinCode=", coinCode);
    filter.addFilter("customerId=", customerId);
    filter.setOrderby("id desc");
    filter.setPage(1);
    filter.setPageSize(10);
    Page<C2cTransaction> findPage = c2cTransactionService.findPage(filter);
    List<C2cTransaction> list = findPage.getResult();

    ArrayList<C2cOrder> arrayList = new ArrayList<C2cOrder>();
    if (list != null && list.size() > 0) {
      for (C2cTransaction c2cTransaction : list) {
        C2cOrder c2cOrder = new C2cOrder();
        c2cOrder.setTransactionTime(c2cTransaction.getCreated());
        c2cOrder.setCreateTime(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
            .format(c2cTransaction.getCreated())); // modify
        // by
        // zongwei
        // 改时间格式YYYY-MM-dd
        // HH:mm:ss
        c2cOrder.setCoinCode(c2cTransaction.getCoinCode());
        c2cOrder.setTransactionNum(c2cTransaction.getTransactionNum());
        c2cOrder.setTransactionPrice(c2cTransaction.getTransactionPrice());
        c2cOrder.setTransactionCount(c2cTransaction.getTransactionCount());
        c2cOrder.setTransactionMoney(c2cTransaction.getTransactionMoney());
        c2cOrder.setTransactionType(c2cTransaction.getTransactionType());
        c2cOrder.setStatus(c2cTransaction.getStatus());
        c2cOrder.setStatus2(c2cTransaction.getStatus2());
        arrayList.add(c2cOrder);
      }
    }

    return arrayList;

  }

  @Override
  public RemoteResult createPublicKey(Long exdmaccountId) {

    RemoteResult remoteResult = new RemoteResult();
    remoteResult.setMsg("createerror");

    ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(exdmaccountId);
    if (!StringUtils.isEmpty(exDigitalmoneyAccount.getPublicKey())) {
      return remoteResult.setMsg("qianbaodizhicunzai");// 钱包地址已存在!
    }
    try {
      String ss = CoinInterfaceUtil.create(exDigitalmoneyAccount.getUserName(),
          exDigitalmoneyAccount.getAccountNum().toLowerCase(), exDigitalmoneyAccount.getCoinCode());
      JSONObject parseObject = null;
      if (!StringUtils.isEmpty(ss)) {
        parseObject = JSONObject.parseObject(ss);
      }
      String address = "";
      if (parseObject != null) {
        address = parseObject.get("address").toString();
      }

      if (!StringUtils.isEmpty(address)) {
        remoteResult.setSuccess(true);
        remoteResult.setObj(address);
        remoteResult.setMsg("success");
        exDigitalmoneyAccount.setPublicKey(address);
        exDigitalmoneyAccountService.update(exDigitalmoneyAccount);
        System.out.println("手机号为：" + exDigitalmoneyAccount.getUserName() + ",币地址：" + address);
      } else {
        remoteResult.setMsg("createerror");// 生成失败
        System.out.println("开通" + exDigitalmoneyAccount.getCoinCode() + "钱包出错");
      }

    } catch (Exception e) {
      System.out.println("远程调用开通钱包失败");
      e.printStackTrace();
    }

    return remoteResult;
  }

  @Override
  public RemoteResult getPublicKey(Long exdmaccountId) {

    RemoteResult remoteResult = new RemoteResult();

    ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(exdmaccountId);
    if (exDigitalmoneyAccount != null) {
      return remoteResult.setSuccess(true).setObj(exDigitalmoneyAccount.getPublicKey());
    }

    return remoteResult;
  }

  @Override
  public User selectByustomerId(Long customerId) {

    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("id=", customerId);
    AppCustomer appCustomer = appCustomerService.get(qf);

    User user = new User();
    if (appCustomer != null) {
      QueryFilter qfw = new QueryFilter(AppPersonInfo.class);
      qfw.addFilter("customerId=", appCustomer.getId());
      AppPersonInfo apppersonInfo = appPersonInfoService.get(qfw);

      user.setUsername(appCustomer.getUserName());
      user.setUserCode(appCustomer.getUserCode());
      user.setAccountPassWord(appCustomer.getAccountPassWord());
      user.setIsReal(appCustomer.getIsReal() == null ? 0 : appCustomer.getIsReal().intValue());
      user.setIsDelete(appCustomer.getIsDelete());
      user.setIsChange(appCustomer.getIsChange());
      user.setCustomerId(appCustomer.getId());
      user.setMobile(appCustomer.getUserName());
      user.setSurname(apppersonInfo.getSurname());
      user.setTruename(apppersonInfo.getTrueName());
      user.setIsLock(appCustomer.getIsLock());
      user.setCustomerType(apppersonInfo.getCustomerType());
      user.setSalt(appCustomer.getSalt());
      user.setSaasId(appCustomer.getSaasId());
      user.setGoogleKey(appCustomer.getGoogleKey());
      user.setGoogleState(appCustomer.getGoogleState());
      user.setMessIp(appCustomer.getMessIp());
      user.setPassDate(appCustomer.getPassDate());
      user.setPhone(appCustomer.getPhone());
      user.setPhoneState(appCustomer.getPhoneState());
      user.setStates(appCustomer.getStates());
      user.setPassword(appCustomer.getPassWord());
      user.setUuid(appCustomer.getUuid());
      user.setOpenOtcStates(appCustomer.getOpenOtcStates()); // add by
      // zongwei
      // 20180628
      // 加开通otc功能字段
      user.setMail(appCustomer.getMail());
      user.setMailStates(appCustomer.getMailStates());

      // 是否展示推荐返佣
      String commend = getCnfigValue("commend_switch");
      if (commend != null && !"".equals(commend)) {
        user.setCommend(Integer.valueOf(commend));
      }

      String cardCurrency = getCnfigValue("card_Currency");
      // 未实名每日提币量
      String uncardCurrency = getCnfigValue("uncard_Currency");
      if (cardCurrency != null && uncardCurrency != null) {
        user.setCardCurrency(cardCurrency);
        user.setUncardCurrency(uncardCurrency);
      }

    }
    return user;
  }

  @Override
  public RemoteResult login(String username, String password, String uuid) {

    QueryFilter f = new QueryFilter(AppCustomer.class);
    username = username.toLowerCase();
    f.addFilter("userName=", username);
    AppCustomer appCustomer = appCustomerService.get(f);

    if (appCustomer != null) {
      PasswordHelper passwordHelper = new PasswordHelper();
      String encryString = passwordHelper.encryString(password, appCustomer.getSalt());
      if (!appCustomer.getPassWord().equals(encryString)) {
        return new RemoteResult().setSuccess(false).setMsg("mimacuowu");
      }
      if (appCustomer.getIsDelete().equals(1)) {
        return new RemoteResult().setSuccess(false).setMsg("user_forbidden");
      }
      if (appCustomer.getHasEmail() == null || appCustomer.getHasEmail()
          .equals(0)) {
        return new RemoteResult().setSuccess(false).setMsg("zhanghaoweijihuo");
      }
      /*
       * QueryFilter qf = new QueryFilter(AppPersonInfo.class);
       * qf.addFilter("customerId=", appCustomer.getId());
       */
      AppPersonInfo apppersonInfo = appPersonInfoDao
          .getAppPersonInfoByCustomerId(appCustomer.getId());

      User user = new User();
      user.setUsername(appCustomer.getUserName());
      user.setUserCode(appCustomer.getUserCode());
      user.setAccountPassWord(appCustomer.getAccountPassWord());
      user.setIsReal(appCustomer.getIsReal() == null ? 0 : appCustomer.getIsReal().intValue());
      user.setIsDelete(appCustomer.getIsDelete());
      user.setIsChange(appCustomer.getIsChange());
      user.setCustomerId(appCustomer.getId());
      user.setCountry(apppersonInfo.getCountry());
      user.setMobile(appCustomer.getUserName());
      user.setSurname(apppersonInfo.getSurname());
      user.setTruename(apppersonInfo.getTrueName());
      user.setIsLock(appCustomer.getIsLock());
      user.setCustomerType(apppersonInfo.getCustomerType());
      user.setSalt(appCustomer.getSalt());
      user.setSaasId(appCustomer.getSaasId());
      user.setGoogleKey(appCustomer.getGoogleKey());
      user.setGoogleState(appCustomer.getGoogleState());
      user.setMessIp(appCustomer.getMessIp());
      user.setPassDate(appCustomer.getPassDate());
      user.setPhone(appCustomer.getPhone());
      user.setPhoneState(appCustomer.getPhoneState());
      user.setStates(appCustomer.getStates());
      user.setPassword(appCustomer.getPassWord());
      user.setUuid(uuid);
      user.setOpenOtcStates(appCustomer.getOpenOtcStates()); // add by
      // zongwei
      // 20180628
      // 加开通otc功能字段
      user.setMail(appCustomer.getMail());
      user.setMailStates(appCustomer.getMailStates());

      // 新增判断
      if ((appCustomer.getCheckStates() == null || appCustomer.getCheckStates() == 1)
          && appCustomer.getPhoneState() == 1) {
        user.setCheckStates(1);
      } else {
        user.setCheckStates(0);
      }

      // 是否展示推荐返佣
      String commend = getCnfigValue("commend_switch");
      if (commend != null && !"".equals(commend)) {
        user.setCommend(Integer.valueOf(commend));
      }

      appCustomer.setUuid(uuid);
      appCustomerService.update(appCustomer);

      String cardCurrency = getCnfigValue("card_Currency");
      // 未实名每日提币量
      String uncardCurrency = getCnfigValue("uncard_Currency");
      if (cardCurrency != null && uncardCurrency != null) {
        user.setCardCurrency(cardCurrency);
        user.setUncardCurrency(uncardCurrency);
      }
      return new RemoteResult().setSuccess(true).setObj(user).setMsg("login_success");
    }
    return new RemoteResult().setSuccess(false).setMsg("login_nameorpwd_erro");

  }

  @Override
  public RemoteResult login(String mobile, String password, String country, String uuid) {
    AppCustomer appCustomer = null;
    QueryFilter f = new QueryFilter(AppPersonInfo.class);
    f.addFilter("mobilePhone=", mobile);
    f.addFilter("country=", country);
    AppPersonInfo _appPersonInfo = appPersonInfoService.get(f);
    if (_appPersonInfo != null) {
      appCustomer = appCustomerService.get(_appPersonInfo.getCustomerId());
    }
    if (appCustomer != null) {
      PasswordHelper passwordHelper = new PasswordHelper();
      String encryString = passwordHelper.encryString(password, appCustomer.getSalt());
      if (!appCustomer.getPassWord().equals(encryString)) {
        return new RemoteResult().setSuccess(false).setMsg("mimacuowu");
      }
      if (appCustomer.getIsDelete().equals(Integer.valueOf(1))) {
        return new RemoteResult().setSuccess(false).setMsg("user_forbidden");
      }
      if (appCustomer.getHasEmail() == null || appCustomer.getHasEmail()
          .equals(Integer.valueOf(0))) {
        return new RemoteResult().setSuccess(false).setMsg("zhanghaoweijihuo");
      }
      /*
       * QueryFilter qf = new QueryFilter(AppPersonInfo.class);
       * qf.addFilter("customerId=", appCustomer.getId());
       */
      AppPersonInfo apppersonInfo = appPersonInfoDao
          .getAppPersonInfoByCustomerId(appCustomer.getId());

      User user = new User();
      user.setUsername(appCustomer.getUserName());
      user.setUserCode(appCustomer.getUserCode());
      user.setAccountPassWord(appCustomer.getAccountPassWord());
      user.setIsReal(appCustomer.getIsReal() == null ? 0 : appCustomer.getIsReal().intValue());
      user.setIsDelete(appCustomer.getIsDelete());
      user.setIsChange(appCustomer.getIsChange());
      user.setCustomerId(appCustomer.getId());
      user.setMobile(appCustomer.getUserName());
      user.setSurname(apppersonInfo.getSurname());
      user.setCountry(apppersonInfo.getCountry());
      user.setTruename(apppersonInfo.getTrueName());
      user.setIsLock(appCustomer.getIsLock());
      user.setCustomerType(apppersonInfo.getCustomerType());
      user.setSalt(appCustomer.getSalt());
      user.setSaasId(appCustomer.getSaasId());
      user.setGoogleKey(appCustomer.getGoogleKey());
      user.setGoogleState(appCustomer.getGoogleState());
      user.setMessIp(appCustomer.getMessIp());
      user.setPassDate(appCustomer.getPassDate());
      user.setPhone(appCustomer.getPhone());
      user.setPhoneState(appCustomer.getPhoneState());
      user.setStates(appCustomer.getStates());
      user.setPassword(appCustomer.getPassWord());
      user.setUuid(uuid);
      user.setOpenOtcStates(appCustomer.getOpenOtcStates()); // add by
      // zongwei
      // 20180628
      // 加开通otc功能字段
      user.setMail(appCustomer.getMail());
      user.setMailStates(appCustomer.getMailStates());

      // 是否展示推荐返佣
      String commend = getCnfigValue("commend_switch");
      if (commend != null && !"".equals(commend)) {
        user.setCommend(Integer.valueOf(commend));
      }

      appCustomer.setUuid(uuid);
      appCustomerService.update(appCustomer);

      String cardCurrency = getCnfigValue("card_Currency");
      // 未实名每日提币量
      String uncardCurrency = getCnfigValue("uncard_Currency");
      if (cardCurrency != null && uncardCurrency != null) {
        user.setCardCurrency(cardCurrency);
        user.setUncardCurrency(uncardCurrency);
      }
      return new RemoteResult().setSuccess(true).setObj(user).setMsg("login_success");
    }
    return new RemoteResult().setSuccess(false).setMsg("login_nameorpwd_erro");

  }

  public RemoteResult logout(User user) {
    QueryFilter f = new QueryFilter(AppCustomer.class);
    f.addFilter("userName=", user.getUsername());
    AppCustomer appCustomer = appCustomerService.get(f);
    if (appCustomer != null) {
      appCustomer.setUuid("");
      appCustomerService.update(appCustomer);
      return new RemoteResult().setSuccess(true);
    }
    return new RemoteResult().setSuccess(false);
  }

  public BigDecimal getOldMoney(Long customerId, String created) {
    BigDecimal oldMoney = BigDecimal.valueOf(0);
    QueryFilter f = new QueryFilter(AppTransaction.class);
    f.addFilter("customerId=", customerId);
    // f.addFilter("status=", 1);
    f.addFilter("status=", 2);
    f.addFilter("created>", created + " 00:00:00");
    List<AppTransaction> appTransaction = appTransactionService.find(f);

    QueryFilter f1 = new QueryFilter(AppTransaction.class);
    f1.addFilter("customerId=", customerId);
    f1.addFilter("status=", 1);
    // f1.addFilter("status=", 2);
    f1.addFilter("created>", created + " 00:00:00");
    List<AppTransaction> app = appTransactionService.find(f1);
    if (appTransaction != null) {
      for (AppTransaction a : appTransaction) {
        oldMoney = oldMoney.add(a.getTransactionMoney());
      }

    }
    if (app != null) {
      for (AppTransaction a : app) {
        oldMoney = oldMoney.add(a.getTransactionMoney());
      }

    }
    return oldMoney;
  }

  public static String getCnfigValue(String type) {
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    return remoteAppConfigService.getValueByKey(type);
  }

  @Override
  public RemoteResult regist(String username, String password, String referralCode,
      String language) {

    try {

      // 全部转换小写
      username = username.toLowerCase();
      // 查询此用户有没有被注册
      AppCustomer _appCustomer = appCustomerDao.getAppCustomerSingleByUserName(username);
      if (_appCustomer != null) {
        return new RemoteResult().setMsg("user_reg");

      } else {
        // add by zongwei 注册加验证邮箱是否被绑定
        QueryFilter filter2 = new QueryFilter(AppCustomer.class);
        filter2.addFilter("mail=", username);
        List<AppCustomer> appCustomers2 = appCustomerService.find(filter2);
        if (!appCustomers2.isEmpty() && appCustomers2 != null) {
          return new RemoteResult().setSuccess(false).setMsg("邮箱已经被绑定！");
        }

        AppCustomer customer = new AppCustomer();

        customer.setUserName(username);
        customer.setPassWord(password);
        // 设置谷歌认证和手机认证初始化为0
        customer.setGoogleState(0);
        customer.setPhoneState(0);
        customer.setStates(0);
        customer.setIsReal(0);
        customer.setReferralCode(UUIDUtil.getUUID());
        if (referralCode != null) {
          customer.setCommendCode(referralCode);
        }
        // 设置uid
        customer.setUserCode(UUIDUtil.getUUID());
        PasswordHelper passwordHelper = new PasswordHelper();
        // 密码加密与加盐
        passwordHelper.encryptPassword(customer);

        // add by zongwei 邮箱注册默认绑定邮箱
        customer.setMailStates(1);
        customer.setMail(username);

        appCustomerService.save(customer);

        // 初始化数据AppPersonInfo
        AppPersonInfo appPersonInfo = new AppPersonInfo();
        appPersonInfo.setCountry("+86");
        appPersonInfo.setCustomerSource(0);
        appPersonInfo.setMobilePhone(username);
        appPersonInfo.setCustomerId(customer.getId());
        appPersonInfo.setCustomerType(1);
        appPersonInfo.setEmailCode(UUIDUtil.getUUID());
        appPersonInfoService.save(appPersonInfo);

        // 开通人民币账户
        RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil
            .getBean("remoteAppAccountService");
        remoteAppAccountService
            .openAccount(customer, appPersonInfo, language, ContextUtil.getWebsite());

        // 开通虚拟币账户
        RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil
            .getBean("remoteExProductService");
        remoteExProductService
            .openDmAccount(customer, appPersonInfo, null, ContextUtil.getWebsite(), language);

        appCustomerService.update(customer);
        appPersonInfoService.update(appPersonInfo);

        // 注册送币(联盟网移动到实名)
        giveCoin(customer.getId(), language);
        // 推荐人
        appCommendUserService.saveObj(customer);

        // add by zongwei 注册送积分
        givePoit(customer.getId(), referralCode);

        // 推荐送币(联盟网移动到实名)
        if (!StringUtils.isEmpty(referralCode)) {
          AppCommendUser appCommendUser = appCommendUserService
              .get(new QueryFilter(AppCommendUser.class).addFilter("receCode=", referralCode));
          AppCustomer appCustomer = appCustomerService.get(
              new QueryFilter(AppCustomer.class)
                  .addFilter("username=", appCommendUser.getUsername()));
          commendCoin(appCustomer.getId());
        }
        // 注册成功
        return new RemoteResult().setSuccess(true).setMsg(customer.getUserCode())
            .setObj(appPersonInfo.getEmailCode());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new RemoteResult().setSuccess(false).setMsg("error");

  }

  /**
   * 送币 2017-10-31 09:57:43 -- denghf
   */
  private void giveCoin(Long id, String language) {
    // 查出全部发行的产品例表
    ExProductService exProductService = (ExProductService) ContextUtil.getBean("exProductService");
    ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
        .getBean("exDigitalmoneyAccountService");
    QueryFilter filter = new QueryFilter(ExProduct.class);
    filter.addFilter("issueState=", Integer.valueOf(1));
    filter.setOrderby("isRecommend DESC");
    List<ExProduct> list = exProductService.find(filter);

    for (ExProduct exProduct : list) {
      QueryFilter filterAppAccount = new QueryFilter(AppAccount.class);
      filterAppAccount.addFilter("customerId=", id);
      filterAppAccount.addFilter("coinCode=", exProduct.getCoinCode());
      ExDigitalmoneyAccount _digitalmoneyAccount = exDigitalmoneyAccountService
          .get(filterAppAccount);
      if (_digitalmoneyAccount != null) {
        if (exProduct.getGiveCoin() != null
            && exProduct.getGiveCoin().compareTo(new BigDecimal(0)) > 0) {
          if (_digitalmoneyAccount.getHotMoney() != null) {
            _digitalmoneyAccount
                .setHotMoney(
                    _digitalmoneyAccount.getHotMoney().add(exProduct.getGiveCoin()));// 注册送币
          } else {
            _digitalmoneyAccount.setHotMoney(exProduct.getGiveCoin());
          }
          exDigitalmoneyAccountService.update(_digitalmoneyAccount);

          // 记录流水
          exDigitalmoneyAccountService.saveRecord(_digitalmoneyAccount, 1);
        }
      }
    }
  }

  /**
   * 邀请送币
   *
   * @param id 邀请人
   */
  private void commendCoin(Long id) {
    // 查出全部发行的产品例表
    ExProductService exProductService = (ExProductService) ContextUtil.getBean("exProductService");
    ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
        .getBean("exDigitalmoneyAccountService");
    QueryFilter filter = new QueryFilter(ExProduct.class);
    filter.addFilter("issueState=", Integer.valueOf(1));
    filter.setOrderby("isRecommend DESC");
    List<ExProduct> list = exProductService.find(filter);

    for (ExProduct exProduct : list) {
      QueryFilter filterAppAccount = new QueryFilter(AppAccount.class);
      filterAppAccount.addFilter("customerId=", id);
      filterAppAccount.addFilter("coinCode=", exProduct.getCoinCode());
      ExDigitalmoneyAccount _digitalmoneyAccount = exDigitalmoneyAccountService
          .get(filterAppAccount);
      if (_digitalmoneyAccount != null) {
        if (exProduct.getCommendCoin() != null
            && exProduct.getCommendCoin().compareTo(new BigDecimal(0)) > 0) {

          // 记录订单
          ExDmTransaction exDmTransaction = new ExDmTransaction();
          exDmTransaction.setAccountId(_digitalmoneyAccount.getId());
          exDmTransaction.setCoinCode(_digitalmoneyAccount.getCoinCode());
          exDmTransaction.setCreated(new Date());
          exDmTransaction.setCustomerId(_digitalmoneyAccount.getCustomerId());
          exDmTransaction.setCustomerName(_digitalmoneyAccount.getUserName());
          exDmTransaction.setTime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
          exDmTransaction.setTimereceived(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
          exDmTransaction.setBlocktime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
          exDmTransaction.setFee(new BigDecimal(0));
          // exDmTransaction.setTransactionMoney(_digitalmoneyAccount.getHotMoney());
          // modifyby zongwei 20180607取送币的个数
          exDmTransaction.setTransactionMoney(exProduct.getCommendCoin());
          exDmTransaction.setStatus(2);
          exDmTransaction
              .setTransactionNum(IdGenerate.transactionNum(NumConstant.Ex_Dm_Transaction));
          // 充币
          exDmTransaction.setTransactionType(1);
          exDmTransaction.setUserId(_digitalmoneyAccount.getCustomerId());
          exDmTransaction.setWebsite(_digitalmoneyAccount.getWebsite());
          exDmTransaction.setCurrencyType(_digitalmoneyAccount.getCurrencyType());
          exDmTransaction.setRemark("邀请送" + exProduct.getCommendCoin() + "个币!");
          exDmTransactionService.save(exDmTransaction);

          // 发送消息
          addCoin(_digitalmoneyAccount.getId(), exProduct.getCommendCoin(),
              exDmTransaction.getTransactionNum());

        }
      }
    }
  }

  /**
   * 充币方法
   *
   * @param exdigaccountId 充币账户
   * @param count 充币数量
   * @param transactionNum 订单号
   */
  public boolean addCoin(Long exdigaccountId, BigDecimal count, String transactionNum) {
    try {
      Accountadd accountadd = new Accountadd();
      accountadd.setAccountId(exdigaccountId);
      accountadd.setMoney(count);
      accountadd.setMonteyType(1);
      accountadd.setAcccountType(1);
      accountadd.setRemarks(31);
      accountadd.setTransactionNum(transactionNum);

      List<Accountadd> list = new ArrayList<Accountadd>();
      list.add(accountadd);
      MessageProducer messageProducer =(MessageProducer)ContextUtil.getBean("messageProducer");
      messageProducer.toAccount(Mapper.objectToJson(list));
      return true;
    } catch (Exception e) {
      log.error("充币失败");
    }
    return false;
  }

  @Override
  public FrontPage findTrades(Map<String, String> params) {
    return exOrderInfoService.findFrontPage(params);
  }

  @Override
  public FrontPage findEntrust(Map<String, String> params) {

    return exEntrustService.findFrontPage(params);
  }

  @Override
  public RemoteResult realname(String userName, String trueName, String surName, String country,
      String cardType,
      String cardId) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("userName=", userName));

    if (appCustomer != null && appCustomer.getIsReal() == 0) {
      AppPersonInfo appPersonInfo = appPersonInfoService
          .get(new QueryFilter(AppPersonInfo.class).addFilter("customerId=", appCustomer.getId()));
      // 小X替换大X
      appPersonInfo.setCardId(cardId.replace("x", "X"));
      appPersonInfo.setTrueName(trueName);
      appPersonInfo.setSurname(surName);
      appPersonInfo.setCountry(country);
      appPersonInfo.setCardType(Integer.valueOf(cardType));

      // 验证证件号是否重复
      AppPersonInfo appPersonInfo2 = appPersonInfoDao
          .getAppPersonInfoByCardId(appPersonInfo.getCardId());
      if (appPersonInfo2 != null) {
        return new RemoteResult().setMsg("card_id_chongfu");
      }

      try {
        String checkIdentityIsOpen = remoteThirdPayInterfaceService
            .getIsOpen("checkIdentityInterface");

        if ("0".equals(checkIdentityIsOpen)) {
          RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
              .getBean("remoteAppConfigService");
          String juhe_cardOpen = remoteAppConfigService.getValueByKey("juhe_cardOpen");
          String juhe_cardKey = remoteAppConfigService.getValueByKey("juhe_cardKey");
          String juhe_cardUrl = remoteAppConfigService.getValueByKey("juhe_cardUrl");
          // 聚合接口实名认证,并且国家为中国 如果为0则为开启
          if ("0".equals(juhe_cardOpen)) {
            boolean checkCard = JuheSendUtils
                .auth(appPersonInfo.getTrueName(), appPersonInfo.getCardId(),
                    juhe_cardUrl, juhe_cardKey);
            if (!checkCard) {
              return new RemoteResult().setMsg("failrealname");
            }
          }
        }

        // 开通人民币账户
        // RemoteAppAccountService remoteAppAccountService =
        // (RemoteAppAccountService)
        // ContextUtil.getBean("remoteAppAccountService");
        // remoteAppAccountService.openAccount(appCustomer,appPersonInfo,ContextUtil.getCurrencyType(),ContextUtil.getWebsite());

        // 开通虚拟币账户
        /*
         * RemoteExProductService remoteExProductService =
         * (RemoteExProductService)
         * ContextUtil.getBean("remoteExProductService");
         * remoteExProductService.openDmAccount(appCustomer,
         * appPersonInfo,null,ContextUtil.getWebsite(),ContextUtil.
         * getCurrencyType());
         */

        // 保存代理商
        AppAgentsCustromer appAgentsCustromer = new AppAgentsCustromer();
        appAgentsCustromer.setAddress("中国");
        appAgentsCustromer.setAgentName(appPersonInfo.getTrueName());
        appAgentsCustromer.setSurname(appPersonInfo.getSurname());
        appAgentsCustromer.setCustomerName(appPersonInfo.getMobilePhone());
        appAgentsCustromer.setCustomerId(appPersonInfo.getCustomerId());
        appAgentsCustromer.setPapersType("0");
        appAgentsCustromer.setPapersNo(appPersonInfo.getCardId());
        appAgentsCustromer.setRecommendCode(appPersonInfo.getMobilePhone());
        appAgentsCustromer.setStates(2);
        RemoteAppAgentsService remoteAppAgentsService = (RemoteAppAgentsService) ContextUtil
            .getBean("remoteAppAgentsService");
        JsonResult result = remoteAppAgentsService.saveAgents(appAgentsCustromer);

        appCustomer.setIsReal(1);
        appCustomerService.update(appCustomer);
        appPersonInfoService.update(appPersonInfo);

        return new RemoteResult().setSuccess(true);
      } catch (Exception e) {
        return new RemoteResult().setMsg("身份证号码重复");
      }
    }
    return new RemoteResult();

  }

  @Override
  public RemoteResult getPersonInfo(String userCode) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("userCode=", userCode));
    if (appCustomer != null) {
      AppPersonInfo appPersonInfo = appPersonInfoService
          .get(new QueryFilter(AppPersonInfo.class).addFilter("customerId=", appCustomer.getId()));
      UserInfo info = new UserInfo();
      info.setTrueName(appPersonInfo.getTrueName());
      info.setSurname(appPersonInfo.getSurname());
      info.setCountry(appPersonInfo.getCountry());
      info.setCardType(appPersonInfo.getCardType().toString());
      info.setCardId(appPersonInfo.getCardId());
      String papersType = appPersonInfo.getPapersType();

      if ("1".equals(papersType)) {
        info.setPapersType("身份证");
      } else if ("2".equals(papersType)) {
        info.setPapersType("护照");

      }
      info.setType(papersType);
      return new RemoteResult().setSuccess(true).setObj(info);
    }
    return new RemoteResult();
  }

  @Override
  public RemoteResult setpw(String username, String oldPassWord, String newPassWord) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));
    // 数据库密码
    String passWord = appCustomer.getPassWord();

    PasswordHelper passwordHelper = new PasswordHelper();

    String encryString = passwordHelper.encryString(oldPassWord, appCustomer.getSalt());
    if (!passWord.equals(encryString)) {
      return new RemoteResult().setMsg("yuanshimimabuzhengq");
    }

    appCustomer.setPassDate(new Date());
    String encryString2 = passwordHelper.encryString(newPassWord, appCustomer.getSalt());
    // 密码加密与加盐
    appCustomer.setPassWord(passwordHelper.encryString(newPassWord, appCustomer.getSalt()));
    appCustomerService.update(appCustomer);
    return new RemoteResult().setSuccess(true).setObj(encryString2);

  }

  @Override
  public RemoteResult setapw(String username, String accountPassWord) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));

    PasswordHelper passwordHelper = new PasswordHelper();

    String apw = passwordHelper.encryString(accountPassWord, appCustomer.getSalt());
    if (apw.equals(appCustomer.getPassWord())) {
      return new RemoteResult().setMsg("交易密码不能和登录密码相同!");
    }

    // 密码加密与加盐
    appCustomer.setAccountPassWord(apw);
    appCustomerService.update(appCustomer);

    // 返回user对象更新session

    return new RemoteResult().setSuccess(true).setObj(appCustomer.getAccountPassWord());

  }

  @Override
  public RemoteResult resetapw(String username, String passWord, String accountPassWord) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));

    PasswordHelper passwordHelper = new PasswordHelper();

    String pw = passwordHelper.encryString(passWord, appCustomer.getSalt());
    if (!pw.equals(appCustomer.getPassWord())) {
      return new RemoteResult().setMsg("登陆密码错误");
    }

    // 密码加密与加盐
    appCustomer
        .setAccountPassWord(passwordHelper.encryString(accountPassWord, appCustomer.getSalt()));
    appCustomerService.update(appCustomer);

    return new RemoteResult().setSuccess(true).setObj(appCustomer.getAccountPassWord());

  }

  @Override
  public RemoteResult getAccountInfo(String coinCode, Long customerId) {

    String[] split = coinCode.split("_");
    // 交易币的代码
    String code = split[0];
    // 定价币的代码
    String priceCode = split[1];

    // 返回对象
    TradeAccount tradeAccount = new TradeAccount();
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");

    // 获得当前用户redis对象
    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    UserRedis userRedis = redisUtil.get(customerId.toString());
    if (userRedis == null || StringUtils.isEmpty(userRedis.getDmAccountId(code))) {
      return new RemoteResult();
    }
    ExDigitalmoneyAccountRedis digitAccount = UserRedisUtils
        .getAccount(userRedis.getDmAccountId(code).toString(),
            code);

    // product调用redis中的数据
    ExProduct product = null;
    String productJson = redisService.getMap("HRY:EXCHANGE:PRODUCT", code);
    if (null != productJson) {
      product = (ExProduct) Mapper.JSONToObj(productJson, ExProduct.class);
    } else {
      RemoteExProductService rmoteExProductService = (RemoteExProductService) ContextUtil
          .getBean("remoteExProductService");
      product = rmoteExProductService.findByCoinCode(code, "");
    }

    ProductCommonService productCommonService = (ProductCommonService) ContextUtil
        .getBean("productCommonService");
    Coin productCommon = productCommonService.getProductCommon(code, priceCode);

    String currencyType = ContextUtil.getCurrencyType();
    String Website = ContextUtil.getWebsite();
    String header = exEntrustService.getHeader(null, null, code, priceCode);
    String currentExchangPrices = redisService
        .get(header + ":" + ExchangeDataCacheRedis.CurrentExchangPrice);
    BigDecimal currentExchangPrice = new BigDecimal("0.00");
    if (null != currentExchangPrices) {
      currentExchangPrice = new BigDecimal(currentExchangPrices);
      // 能卖多少钱
      tradeAccount.setCanSellMoney(
          digitAccount.getHotMoney().multiply(currentExchangPrice)
              .setScale(2, BigDecimal.ROUND_HALF_DOWN));

    }
    BigDecimal buyOnePrice = new BigDecimal("0.00");
    String buyOnePrices = redisService.get(header + ":" + ExchangeDataCacheRedis.BuyOnePrice);
    if (null != buyOnePrices) {
      buyOnePrice = new BigDecimal(buyOnePrices);
    }
    String sellOnePrices = redisService.get(header + ":" + ExchangeDataCacheRedis.SellOnePrice);
    BigDecimal sellOnePrice = new BigDecimal("0.00");
    if (null != sellOnePrices) {
      sellOnePrice = new BigDecimal(sellOnePrices);
    }

    // 如果用人民币交易，查人民币账户
    if (priceCode.equalsIgnoreCase("cny") || priceCode.equalsIgnoreCase("usd")
        || priceCode.equalsIgnoreCase("sgd")) {
      // AppAccount appAccount = appAccountService.get(new
      // QueryFilter(AppAccount.class).addFilter("userName=", username));
      AppAccountRedis appAccount = UserRedisUtils.getAccount(userRedis.getAccountId().toString());

      tradeAccount.setRmb(appAccount != null ? appAccount.getHotMoney() : BigDecimal.ZERO);
      tradeAccount.setColdrmb(appAccount != null ? appAccount.getColdMoney() : BigDecimal.ZERO);
      if (currentExchangPrice.compareTo(new BigDecimal(0)) > 0) {
        // 最大可买币量
        tradeAccount.setCanBuyCoin(
            appAccount.getHotMoney().divide(currentExchangPrice, 10, BigDecimal.ROUND_HALF_DOWN));
      }
      // 格式化
      Integer keepDecimalForRmb = productCommon.getKeepDecimalForCurrency();
      if (null == keepDecimalForRmb) {
        keepDecimalForRmb = 4;
      }
      tradeAccount
          .setRmb(tradeAccount.getRmb().setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN));
      tradeAccount.setColdrmb(
          tradeAccount.getColdrmb().setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN));
    } else {// 用币交易，就查币账户
      // ExDigitalmoneyAccount appAccount =
      // exDigitalmoneyAccountService.get(new
      // QueryFilter(ExDigitalmoneyAccount.class).addFilter("userName=",
      // username).addFilter("coinCode=", priceCode));
      ExDigitalmoneyAccountRedis appAccount = UserRedisUtils
          .getAccount(userRedis.getDmAccountId(priceCode).toString(), priceCode);

      tradeAccount.setRmb(appAccount != null ? (appAccount.getHotMoney()
          .setScale(productCommon.getKeepDecimalForCoin(), BigDecimal.ROUND_HALF_DOWN))
          : BigDecimal.ZERO);
      tradeAccount.setColdrmb(appAccount != null ? appAccount.getColdMoney()
          .setScale(productCommon.getKeepDecimalForCoin(), BigDecimal.ROUND_HALF_DOWN)
          : BigDecimal.ZERO);
      if (currentExchangPrice.compareTo(new BigDecimal(0)) > 0) {
        tradeAccount.setCanBuyCoin(appAccount.getHotMoney().divide(currentExchangPrice,
            productCommon.getKeepDecimalForCoin(), BigDecimal.ROUND_HALF_DOWN));
      }
    }

    // 买市价下单最小个数
    tradeAccount.setBuyMinMoney(
        productCommon.getBuyMinMoney() == null ? new BigDecimal(0.00)
            : productCommon.getBuyMinMoney());
    // 每次下单的最小个数
    tradeAccount.setSellMinCoin(productCommon.getOneTimeOrderNumMin() == null ? new BigDecimal(0.00)
        : productCommon.getOneTimeOrderNumMin());
    // 每次下单的最大个数
    tradeAccount.setOneTimeOrderNum(productCommon.getOneTimeOrderNum());

    // 交易币的个数，热，冷个数
    tradeAccount.setCoinHotMoney(digitAccount != null
        ? digitAccount.getHotMoney()
        .setScale(productCommon.getKeepDecimalForCoin(), BigDecimal.ROUND_HALF_DOWN)
        : BigDecimal.ZERO);
    tradeAccount.setCurrentCoinColdMoney(
        digitAccount.getColdMoney().setScale(productCommon.getKeepDecimalForCoin(),
            BigDecimal.ROUND_HALF_DOWN));

    tradeAccount.setKeepDecimalForCoin(null != productCommon.getKeepDecimalForCoin()
        ? new BigDecimal(productCommon.getKeepDecimalForCoin()) : new BigDecimal(4));
    tradeAccount.setKeepDecimalForCurrency(null != productCommon.getKeepDecimalForCurrency()
        ? new BigDecimal(productCommon.getKeepDecimalForCurrency()) : new BigDecimal(2));
    tradeAccount.setCurrentExchangPrice(currentExchangPrice);
    tradeAccount.setBuyOnePrice(buyOnePrice);
    tradeAccount.setSellOnePrice(sellOnePrice);
    tradeAccount.setBuyFeeRate(productCommon.getBuyFeeRate() == null ? new BigDecimal(0.00)
        : productCommon.getBuyFeeRate().setScale(4, BigDecimal.ROUND_HALF_DOWN));
    tradeAccount.setSellFeeRate(productCommon.getSellFeeRate() == null ? new BigDecimal(0.00)
        : productCommon.getSellFeeRate().setScale(4, BigDecimal.ROUND_HALF_DOWN));
    // tradeAccount.setCirculation(productCommon.getCirculation()==null?new
    // BigDecimal(0.00):productCommon.getCirculation().setScale(4,BigDecimal.ROUND_HALF_DOWN));
    return new RemoteResult().setSuccess(true).setObj(JSON.toJSONString(tradeAccount));
  }

  @Override
  public String[] addEntrust(EntrustTrade exEntrust) {
    String[] rt = new String[2];
    rt = exDmPingService.checkPing(exEntrust.getCustomerId());
    if (rt[0].equals(CodeConstant.CODE_FAILED)) {
      return rt;
    }

    // 获得redis缓存
    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    UserRedis userRedis = redisUtil.get(exEntrust.getCustomerId().toString());

    String code = exEntrust.getCoinCode();// 定价币
    String priceCode = exEntrust.getFixPriceCoinCode(); // 交易币
    // 获取后台配置的法币
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    String languageCode = (String) parseObject.get("language_code");// 法币类型

    // ExEntrust exEntruste=JSON.parseObject(JSON.toJSONString(exEntrust),
    // ExEntrust.class);
    String[] check = exEntrustService.saveExEntrustCheck(exEntrust);
    if (check != null && CodeConstant.CODE_SUCCESS.equals(check[0])) {
      EntrustTrade et = new EntrustTrade();
      et.setCoinCode(exEntrust.getCoinCode());
      et.setFixPriceCoinCode(exEntrust.getFixPriceCoinCode());
      if ("cny".equalsIgnoreCase(exEntrust.getFixPriceCoinCode())
          || "usd".equalsIgnoreCase(exEntrust.getFixPriceCoinCode())
          || languageCode.equalsIgnoreCase(priceCode)) {
        et.setFixPriceType(0);
      } else {
        et.setFixPriceType(1);
      }
      et.setCustomerId(exEntrust.getCustomerId());
      et.setEntrustPrice(exEntrust.getEntrustPrice());
      et.setEntrustCount(exEntrust.getEntrustCount());
      et.setEntrustSum(exEntrust.getEntrustSum());
      et.setType(exEntrust.getType());
      et.setSource(exEntrust.getSource());
      et.setEntrustWay(exEntrust.getEntrustWay());
      et.setUserName(exEntrust.getUserName());
      et.setTrueName(exEntrust.getTrueName());
      et.setSurName(exEntrust.getSurName());
      if (et.getFixPriceType().equals(0)) { // 真实货币
        et.setAccountId(userRedis.getAccountId());
      } else {
        et.setAccountId(userRedis.getDmAccountId(exEntrust.getFixPriceCoinCode()));
      }
      et.setCoinAccountId(userRedis.getDmAccountId(exEntrust.getCoinCode()));
      // 初始化一些参数(返回订单号)
      String[] saveExEntrustTrade = exEntrustService.saveExEntrustTrade(et);

      if (exEntrust.getType() == 1) {// 如果买 判断定价币
        if ("cny".equalsIgnoreCase(priceCode) || "usd".equalsIgnoreCase(priceCode)
            || languageCode.equalsIgnoreCase(priceCode)) {
          AppAccountRedis accountRedis = UserRedisUtils
              .getAccount(userRedis.getAccountId().toString());
          if (accountRedis.getHotMoney().compareTo(et.getEntrustSum()) < 0) {
            rt[0] = CodeConstant.CODE_FAILED;
            rt[1] = priceCode + "不足";
            return rt;
          }
        } else {
          // add by zongwei 20180509 获取币信息 异常处理 begin
          if (userRedis.getDmAccountId(priceCode) == null) {
            rt[0] = CodeConstant.CODE_FAILED;
            rt[1] = priceCode + "不足";
            return rt;
          }
          // add by zongwei 20180509 获取币信息 异常处理 end
          ExDigitalmoneyAccountRedis ear = UserRedisUtils
              .getAccount(userRedis.getDmAccountId(priceCode).toString(), priceCode);
          if (ear == null) {
            UserRedis userRedis1 = new UserRedis();
            userRedis1.setId(exEntrust.getCustomerId().toString());
            Map<String, Long> map = this.findAllAccountId(exEntrust.getCustomerId().toString());
            userRedis1.setAccountId(map.get("accountId") == null ? null : map.get("accountId"));
            // 获取完后，移除
            map.remove("accountId");
            userRedis1.setDmAccountId(map);
            redisUtil.put(userRedis1, userRedis1.getId());
          } else {
            if (ear.getHotMoney().compareTo(et.getEntrustSum()) < 0) {
              rt[0] = CodeConstant.CODE_FAILED;
              rt[1] = priceCode + "不足";
              return rt;
            }
          }
        }
      }

      if (et.getType() == 2) {// 如果卖 判断交易币
        ExDigitalmoneyAccountRedis ear = UserRedisUtils
            .getAccount(userRedis.getDmAccountId(code).toString(),
                code);
        if (ear.getHotMoney().compareTo(et.getEntrustCount()) < 0) {
          rt[0] = CodeConstant.CODE_FAILED;
          rt[1] = code + "不足";
          return rt;
        }
      }

      if (et.getEntrustPrice().compareTo(new BigDecimal("0")) == 0
          && et.getEntrustCount().compareTo(new BigDecimal("0")) == 0
          && et.getEntrustSum().compareTo(new BigDecimal("0")) == 0) {
        rt[0] = CodeConstant.CODE_FAILED;
        rt[1] = code + "不足";
        return rt;
      }
      if (et.getEntrustWay() == 1 && et.getEntrustCount().compareTo(new BigDecimal("0")) == 0
          && et.getEntrustSum().compareTo(new BigDecimal("0")) == 0) {
        rt[0] = CodeConstant.CODE_FAILED;
        rt[1] = code + "不足";
        return rt;
      }
      // 序列化
      String str = JSON.toJSONString(et);
      System.out.println(str);
      // 发送消息
      messageProducer.toTrade(str);
      String string = saveExEntrustTrade[0];
      String[] arr = {CodeConstant.CODE_SUCCESS, string};
      return arr;
    } else {
      return check;
    }

  }

  public AppCustomer selectCustomerByName(String username) {
    QueryFilter f = new QueryFilter(AppCustomer.class);
    f.addFilter("userName=", username);
    AppCustomer appCustomer = appCustomerService.get(f);
    if (appCustomer != null) {
      AppCustomer cust = new AppCustomer();
      cust.setId(appCustomer.getId());
      cust.setUserName(appCustomer.getUserName());
      cust.setSaasId(appCustomer.getSaasId());
      return cust;
    }
    return null;
  }

  @Override
  public AppAccountManage getByCustomerIdAndType(Long customerId, String currencyType,
      String website) {

    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    UserRedis userRedis = redisUtil.get(customerId.toString());
    AppAccountRedis account = UserRedisUtils.getAccount(userRedis.getAccountId().toString());

    AppAccountManage appAccountManage = common(account);

    return appAccountManage;
  }

  public AppAccountManage getAppAccountManage(Long customerId) {
    AppAccountManage appAccountManage = new AppAccountManage();
    AppAccount account = appAccountService
        .get(new QueryFilter(AppAccount.class).addFilter("customerId=", customerId));
    if (account != null) {
      appAccountManage.setId(account.getId());
      appAccountManage.setUserName(account.getUserName());
      appAccountManage.setHotMoney(account.getHotMoney());
    }
    return appAccountManage;
  }

  /**
   * 需要字段 自行添加 denghf
   */
  public AppAccountManage common(AppAccountRedis appAccount) {
    AppAccountManage appAccountManage = new AppAccountManage();
    appAccountManage.setId(appAccount.getId());
    appAccountManage.setUserName(appAccount.getUserName());
    appAccountManage.setHotMoney(appAccount.getHotMoney());
    return appAccountManage;
  }

  /**
   * 需要字段 自行添加 denghf
   */
  public AppPersonInfoManage common(AppPersonInfo appPersonInfo) {
    AppPersonInfoManage appPersonInfoManage = new AppPersonInfoManage();
    appPersonInfoManage.setTrueName(appPersonInfo.getTrueName());
    appPersonInfoManage.setSurName(appPersonInfo.getSurname());
    return appPersonInfoManage;
  }

  @Override
  public boolean isAgentExist(String agentLevel, String provinceId, String cityId,
      String countyId) {
    return appPersonInfoService.isAgentExist(agentLevel, provinceId, cityId, countyId);
  }

  /**
   * 我方账户
   */
  public AppOurAccountManage getOurAccount(String website, String currencyType,
      String accountType) {
    QueryFilter filter = new QueryFilter(AppAccountRecord.class);
    filter.addFilter("website=", website);
    filter.addFilter("currencyType=", currencyType);
    filter.addFilter("isShow=", "1");
    filter.addFilter("accountType=", accountType);
    AppOurAccount ourAccount = appOurAccountService.get(filter);
    AppOurAccountManage appOurAccountManage = common(ourAccount);
    return appOurAccountManage;
  }

  public AppOurAccountManage common(AppOurAccount ourAccount) {
    AppOurAccountManage appOurAccountManage = new AppOurAccountManage();
    appOurAccountManage.setAccountMoney(ourAccount.getAccountMoney());
    appOurAccountManage.setAccountName(ourAccount.getAccountName());
    appOurAccountManage.setAccountNumber(ourAccount.getAccountNumber());
    appOurAccountManage.setAccountType(ourAccount.getAccountType());
    appOurAccountManage.setBankAddress(ourAccount.getBankAddress());
    appOurAccountManage.setBankLogo(ourAccount.getBankLogo());
    appOurAccountManage.setBankName(ourAccount.getBankName());
    appOurAccountManage.setCoinTotalMoney(ourAccount.getCoinTotalMoney());
    appOurAccountManage.setCreated(ourAccount.getCreated());
    appOurAccountManage.setCurrencyType(ourAccount.getCurrencyType());
    appOurAccountManage.setDicBankName(ourAccount.getDicBankName());
    appOurAccountManage.setId(ourAccount.getId());
    appOurAccountManage.setIsShow(ourAccount.getIsShow());
    appOurAccountManage.setModified(ourAccount.getModified());
    appOurAccountManage.setOpenAccountType(ourAccount.getOpenAccountType());
    appOurAccountManage.setOpenTime(ourAccount.getOpenTime());
    appOurAccountManage.setRemark(ourAccount.getRemark());
    appOurAccountManage.setRetainsMoney(ourAccount.getRetainsMoney());
    appOurAccountManage.setSaasId(ourAccount.getSaasId());
    appOurAccountManage.setWebsite(ourAccount.getWebsite());
    appOurAccountManage.setWithdrawMoney(ourAccount.getWithdrawMoney());
    return appOurAccountManage;
  }

  @Override
  public String[] addEntrustCheck(EntrustTrade exEntrust) {
    return exEntrustService.saveExEntrustCheck(exEntrust);
  }

  @Override
  public RemoteResult finaCoins() {

    List<ExCointoCoin> find = exCointoCoinService
        .find(new QueryFilter(ExCointoCoin.class).addFilter("state=", 1));

    ArrayList<Coin> list = new ArrayList<Coin>();
    for (ExCointoCoin ex : find) {
      Coin coin = new Coin();
      // add by begin zongwei 20180428 加照片地址
      ExProduct exProduct = exProductService.findByallCoinCode(ex.getCoinCode());
      if (exProduct != null) {
        coin.setPicturePath(exProduct.getPicturePath());
      }
      // add by end zongwei 20180428 加照片地址
      coin.setCoinCode(ex.getCoinCode());
      coin.setFixPriceCoinCode(ex.getFixPriceCoinCode());
      list.add(coin);
    }

    return new RemoteResult().setSuccess(true).setObj(list);
  }

  @Override
  public String[] cancelExEntrust(EntrustTrade entrustTrade) {
    String[] rt = new String[2];
    rt = exDmPingService.checkPing(entrustTrade.getCustomerId());
    if (rt[0].equals(CodeConstant.CODE_FAILED)) {
      return rt;
    }
    return exEntrustService.cancelExEntrust(entrustTrade, "手动撤单");
  }

  public static double div(double v1, double v2) {
    return div(v1, v2);
  }

  /**
   * 传币代码换算成BTC，换的成就返回BTC数量 ，换不成就返回NULL
   */
  public BigDecimal computeBTC(UserRedis userRedis, String code) {

    // 比特币总数
    BigDecimal btcSum = new BigDecimal(0);

    String string = redisService.get("cn:coinInfoList");
    List<Coin> coins = JSONArray.parseArray(string, Coin.class);

    Map<String, Long> dmMap = userRedis.getDmAccountId();
    ExDigitalmoneyAccountRedis ear = UserRedisUtils.getAccount(dmMap.get(code).toString(), code);
    boolean flag = false;
    for (Coin c : coins) {
      if (ear.getCoinCode().equals(c.getCoinCode())) {
        if ("BTC".equals(c.getFixPriceCoinCode())) {
          // 最新成交价
          String changePrice = redisService.get(code + "_BTC:currentExchangPrice");
          btcSum = new BigDecimal(changePrice);
          /*
           * if(!StringUtils.isEmpty(changePrice)){ flag = true;
           * BigDecimal coinBtc =
           * (ear.getHotMoney().add(ear.getColdMoney())).multiply(new
           * BigDecimal(changePrice)); btcSum = btcSum.add(coinBtc); }
           */
          break;
        }

      }
    }

    if (!StringUtils.isEmpty(btcSum)) {
      return btcSum;
    }
    return null;
  }

  /**
   * 换算成BTC共有多少个，换算成ETH共有多少个
   *
   * @return BigDecimal[0] btc ,BigDecimal[1] eth
   */
  public BigDecimal[] computeBTCETH(UserRedis userRedis) {

    String string = redisService.get("cn:coinInfoList");
    List<Coin> coins = JSONArray.parseArray(string, Coin.class);

    // 比特币总数
    BigDecimal btcSum = new BigDecimal(0);

    // 计算总资产
    Map<String, Long> dmMap = userRedis.getDmAccountId();
    if (dmMap != null) {
      Set<String> keySet = dmMap.keySet();
      Iterator<String> it = keySet.iterator();
      while (it.hasNext()) {
        String code = it.next();
        if ("BTC".equals(code)) {// 如果是比特币，直接加上比特币
          ExDigitalmoneyAccountRedis ear = UserRedisUtils
              .getAccount(dmMap.get(code).toString(), code);
          if (ear != null) {
            btcSum = btcSum.add(ear.getHotMoney()).add(ear.getColdMoney());
          }
        } else {// 如果是其它的币。换算成比特币
          ExDigitalmoneyAccountRedis ear = UserRedisUtils
              .getAccount(dmMap.get(code).toString(), code);
          if (ear != null) {
            for (Coin c : coins) {
              if (ear.getCoinCode().equals(c.getCoinCode())) {
                if ("BTC".equals(c.getFixPriceCoinCode())) {
                  // 最新成交价
                  String changePrice = redisService.get(code + "_BTC:currentExchangPrice");
                  if (!StringUtils.isEmpty(changePrice)) {
                    BigDecimal coinBtc = (ear.getHotMoney().add(ear.getColdMoney()))
                        .multiply(new BigDecimal(changePrice));
                    btcSum = btcSum.add(coinBtc);
                  }
                  break;
                }

              }
            }
          }
        }
      }
    }

    BigDecimal[] arr = new BigDecimal[2];
    arr[0] = btcSum;

    return arr;
  }

  @Override
  public MyAccountTO myAccount(Long customerId) {

    // 查redis缓存
    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    UserRedis userRedis = redisUtil.get(customerId.toString());

    BigDecimal[] computeBTCETH = computeBTCETH(userRedis);

    // 比特币总数
    BigDecimal btcSum = computeBTCETH[0];

    // 美元金额
    BigDecimal $money = new BigDecimal(0);

    // B网比特币价格
    BigDecimal bprice = new BigDecimal(1);
    String bweb_price = redisService.get("bitstamp_btc_price");
    if (!StringUtils.isEmpty(bweb_price)) {
      bprice = new BigDecimal(bweb_price);
    }

    // B网ETH价格
    BigDecimal eprice = new BigDecimal(1);
    String eweb_price = redisService.get("bitstamp_eth_price");
    if (!StringUtils.isEmpty(eweb_price)) {
      bprice = new BigDecimal(eweb_price);
    }

    $money = btcSum.multiply(bprice);

    // 封装对象返回
    MyAccountTO myAccountTO = new MyAccountTO();
    Integer keepDecimalForRmb = exProductService.getkeepDecimalForRmb();
    myAccountTO.setSumMoney($money.setScale(keepDecimalForRmb, BigDecimal.ROUND_HALF_DOWN));
    myAccountTO.setSumBtcMoney(btcSum.setScale(8, BigDecimal.ROUND_HALF_DOWN));

    AppAccount account = appAccountService
        .get(new QueryFilter(AppAccount.class).addFilter("customerId=", customerId));
    if (account != null) {
      myAccountTO.setHotMoney(account.getHotMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
      myAccountTO.setColdMoney(account.getColdMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
    }

    return myAccountTO;

  }

  @Override
  public List<CoinAccount> findCoinAccount(Long customerId) {

    ArrayList<CoinAccount> list = new ArrayList<CoinAccount>();

    // 查redis缓存
    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    UserRedis userRedis = redisUtil.get(customerId.toString());
    if (userRedis == null || "".equals(userRedis)) {
      return list;
    }

    // 获得缓存中所有的币账户id
    Map<String, Long> map = userRedis.getDmAccountId();
    Set<String> keySet = map.keySet();
    for (String key : keySet) {
      // 获得币账户
      ExDigitalmoneyAccountRedis exaccount = UserRedisUtils
          .getAccount(userRedis.getDmAccountId(key).toString(),
              key);
      if (exaccount != null) {
        ProductCommon productCommon = productCommonService
            .getProductCommon(exaccount.getCoinCode());
        if (productCommon != null && null != productCommon.getKeepDecimalForCoin()) {
          CoinAccount coinAccount = new CoinAccount();
          coinAccount.setCoinCode(exaccount.getCoinCode());
          coinAccount
              .setColdMoney(exaccount.getColdMoney().setScale(8, BigDecimal.ROUND_HALF_DOWN));
          coinAccount.setHotMoney(exaccount.getHotMoney().setScale(8, BigDecimal.ROUND_HALF_DOWN));

          if (null != productCommon && null != productCommon.getKeepDecimalForCoin()) {
            coinAccount.getColdMoney().setScale(productCommon.getKeepDecimalForCoin(),
                BigDecimal.ROUND_HALF_DOWN);
            coinAccount.getHotMoney().setScale(productCommon.getKeepDecimalForCoin(),
                BigDecimal.ROUND_HALF_DOWN);
            coinAccount.setName(productCommon.getName());
            coinAccount.setKeepDecimalForCoin(productCommon.getKeepDecimalForCoin());
          }
          list.add(coinAccount);
        }
      }
    }

    // 从redis查图片路径
    if (list != null && list.size() > 0) {
      RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
      String string = redisService.get("cn:productinfoListall");
      List<Coin> coins = JSONArray.parseArray(string, Coin.class);
      for (Coin coin : coins) {
        for (CoinAccount coinAccount : list) {
          if (coin.getCoinCode().equals(coinAccount.getCoinCode())) {
            coinAccount.setPicturePath(coin.getPicturePath());
          }
        }
      }
    }

    return list;
  }

  public RemoteResult selectPhone(String telphone) {
    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("userName=", telphone);
    AppCustomer appcustomer = appCustomerService.get(qf);
    if (appcustomer != null) {
      return new RemoteResult().setSuccess(true);
    } else {
      return new RemoteResult().setSuccess(false);
    }
  }

  public RemoteResult selectAgent(String username) {
    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("referralCode=", username);
    // qf.addFilter("states=", 2);
    List<AppCustomer> find = appCustomerService.find(qf);
    if (find.size() > 0) {
      return new RemoteResult().setSuccess(true);
    } else {
      return new RemoteResult().setSuccess(false);
    }
  }

  public RemoteResult updatepwd(String pwd, String tel) {
    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("userName=", tel);
    AppCustomer appcustomer = appCustomerService.get(qf);

    PasswordHelper passwordHelper = new PasswordHelper();
    String encryString = passwordHelper.encryString(pwd, appcustomer.getSalt());
    appcustomer.setPassWord(encryString);
    appCustomerService.update(appcustomer);
    return new RemoteResult().setSuccess(true);
  }

  public User selectByTel(String tel) {
    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("userName=", tel);
    AppCustomer appCustomer = appCustomerService.get(qf);
    User user = new User();
    if (appCustomer != null) {
      QueryFilter qfw = new QueryFilter(AppPersonInfo.class);
      qfw.addFilter("customerId=", appCustomer.getId());
      AppPersonInfo apppersonInfo = appPersonInfoService.get(qfw);

      /*
       * user.setUsername(appCustomer.getUserName());
       * user.setUserCode(appCustomer.getUserCode());
       * user.setAccountPassWord(appCustomer.getAccountPassWord());
       * user.setIsReal(appCustomer.getIsReal());
       * user.setIsDelete(appCustomer.getIsDelete());
       * user.setIsChange(appCustomer.getIsChange());
       * user.setCustomerId(appCustomer.getId());
       * user.setMobile(appCustomer.getUserName());
       * user.setTruename(apppersonInfo.getTrueName());
       * user.setIsLock(appCustomer.getIsLock());
       * user.setCustomerType(apppersonInfo.getCustomerType());
       * user.setSalt(appCustomer.getSalt());
       * user.setSaasId(apppersonInfo.getSaasId());
       * user.setCardcode(apppersonInfo.getCardId());
       * user.setEmail(apppersonInfo.getEmail());
       * user.setSex(apppersonInfo.getSex());
       * user.setPostalAddress(apppersonInfo.getPostalAddress());
       */
      user.setCardcode(apppersonInfo.getCardId());
      user.setUsername(appCustomer.getUserName());
      user.setUserCode(appCustomer.getUserCode());
      user.setAccountPassWord(appCustomer.getAccountPassWord());
      user.setIsReal(appCustomer.getIsReal() == null ? 0 : appCustomer.getIsReal().intValue());
      user.setIsDelete(appCustomer.getIsDelete());
      user.setIsChange(appCustomer.getIsChange());
      user.setCustomerId(appCustomer.getId());
      user.setMobile(appCustomer.getUserName());
      user.setTruename(apppersonInfo.getTrueName());
      user.setSurname(apppersonInfo.getSurname());
      user.setIsLock(appCustomer.getIsLock());
      user.setCustomerType(apppersonInfo.getCustomerType());
      user.setSalt(appCustomer.getSalt());
      user.setSaasId(appCustomer.getSaasId());
      user.setGoogleKey(appCustomer.getGoogleKey());
      user.setGoogleState(appCustomer.getGoogleState());
      user.setMessIp(appCustomer.getMessIp());
      user.setPassDate(appCustomer.getPassDate());
      user.setPhone(appCustomer.getPhone());
      user.setPhoneState(appCustomer.getPhoneState());
      user.setStates(appCustomer.getStates());
      user.setPassword(appCustomer.getPassWord());
      user.setUuid(appCustomer.getUuid());
      user.setCountry(apppersonInfo.getCountry());
      user.setThirdUserName(appCustomer.getThirdUserName());
      user.setOpenOtcStates(appCustomer.getOpenOtcStates());
      user.setMail(appCustomer.getMail());
      user.setMailStates(appCustomer.getMailStates());
      String ThirdUserPw;
      if (appCustomer.getThirdUserPw() != null) {
        ThirdUserPw = Md5Encrypt.md5(appCustomer.getThirdUserPw());
      } else {
        ThirdUserPw = appCustomer.getThirdUserPw();
      }
      user.setThirdUserPw(ThirdUserPw); // 加密再返回
      user.setPlatform(appCustomer.getPlatform());
    }
    return user;
  }

  @Override
  public RemoteResult appresetapw(String username, String accountPassWord) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));

    PasswordHelper passwordHelper = new PasswordHelper();

    String apw = passwordHelper.encryString(accountPassWord, appCustomer.getSalt());
    if (apw.equals(appCustomer.getPassWord())) {
      return new RemoteResult().setMsg("交易密码不能和登录密码相同!");
    }

    // 密码加密与加盐
    appCustomer
        .setAccountPassWord(passwordHelper.encryString(accountPassWord, appCustomer.getSalt()));
    appCustomerService.update(appCustomer);

    return new RemoteResult().setSuccess(true);

  }

  public CoinAccount getAppaccount(Long id) {
    AppAccount account = appAccountService
        .get(new QueryFilter(AppAccount.class).addFilter("customerId=", id));
    CoinAccount c = new CoinAccount();
    if (account != null) {
      c.setHotMoney(account.getHotMoney());
      c.setColdMoney(account.getColdMoney());
      c.setAccountNum(account.getAccountNum());
    }
    return c;
  }

  // 查詢用戶修改密碼時間
  /*
   * public AppCustomer getAppMoblie(String mobile){ AppCustomer account =
   * appCustomerService.get(new
   * QueryFilter(AppCustomer.class).addFilter("username=", mobile)); return
   * account; }
   */

  @Override
  public RemoteResult testAppCustomer(String username) {
    AppCustomer appCustomer = appCustomerDao.getAppCustomerSingleByUserName(username);
    if (appCustomer != null) {
      return new RemoteResult().setSuccess(true);
    } else {
      return new RemoteResult().setSuccess(false);
    }

  }

  @Override
  public RemoteResult sendgoogle(String mobile, String savedSecret) {
    // TODO Auto-generated method stub
    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", mobile));
    // 存谷歌key
    if (savedSecret != null && !savedSecret.equals("")) {
      System.out.println("savedSecret not null");
      appCustomer.setGoogleState(1);
      appCustomer.setIsProving(1);
      appCustomer.setGoogleKey(savedSecret);
      appCustomerService.update(appCustomer);
      return new RemoteResult().setSuccess(true);
    } else {
      return new RemoteResult().setSuccess(false).setCode("sendgoogleerror");
    }
  }

  @Override
  public RemoteResult jcgoogle(String mobile, String savedSecret) {
    // TODO Auto-generated method stub
    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", mobile));
    // 存谷歌key
    appCustomer.setGoogleState(0);
    appCustomer.setIsProving(0);
    appCustomer.setGoogleDate(new Date());
    appCustomer.setPassDate(new Date());
    appCustomerService.update(appCustomer);
    return new RemoteResult().setSuccess(true);
  }

  // 存ip第一次登錄
  @Override
  public RemoteResult savaIp(String mobile, String messIp) {
    // TODO Auto-generated method stub
    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("userName=", mobile));

    // 存谷歌key
    appCustomer.setMessIp(messIp);
    appCustomerService.update(appCustomer);
    return new RemoteResult().setSuccess(true);
  }

  @Override
  public RemoteResult activation(String code) {

    QueryFilter filter = new QueryFilter(AppPersonInfo.class);
    filter.addFilter("emailCode=", code);
    AppPersonInfo appPersonInfo = appPersonInfoService.get(filter);
    if (appPersonInfo != null) {
      AppCustomer appCustomer = appCustomerService.get(appPersonInfo.getCustomerId());
      appCustomer.setHasEmail(1);
      appCustomerService.update(appCustomer);
      return new RemoteResult().setSuccess(true);
    }
    return new RemoteResult();

  }

  @Override
  public Map<String, Long> findAllAccountId(String id) {
    Map<String, Long> map = new HashMap<String, Long>();

    AppAccount appAccount = appAccountService
        .get(new QueryFilter(AppAccount.class).addFilter("customerId=", Long.valueOf(id)));
    if (appAccount != null) {

      RedisUtil<AppAccountRedis> redisUtil = new RedisUtil<AppAccountRedis>(AppAccountRedis.class);
      AppAccountRedis appAccountRedis = redisUtil.get(appAccount.getId().toString());
      // 如果redis中的accout为空则重置
      if (appAccountRedis == null || appAccountRedis.getHotMoney() == null
          || appAccountRedis.getHotMoney().compareTo(new BigDecimal(0)) == 0) {
        AppAccountRedis ar = new AppAccountRedis();
        ar.setColdMoney(appAccount.getColdMoney());
        ar.setCustomerId(appAccount.getCustomerId());
        ar.setHotMoney(appAccount.getHotMoney());
        ar.setId(appAccount.getId());
        ar.setUserName(appAccount.getUserName());
        redisUtil.put(ar, ar.getId().toString());
      }
      map.put("accountId", appAccount.getId());
    }

    List<ExDigitalmoneyAccount> list = exDigitalmoneyAccountService
        .find(new QueryFilter(ExDigitalmoneyAccount.class)
            .addFilter("customerId=", Long.valueOf(id)));
    if (list != null && list.size() > 0) {
      for (ExDigitalmoneyAccount exDigitalmoneyAccount : list) {

        RedisUtil<ExDigitalmoneyAccountRedis> redisUtil = new RedisUtil<ExDigitalmoneyAccountRedis>(
            ExDigitalmoneyAccountRedis.class);
        ExDigitalmoneyAccountRedis exDigitalmoneyAccountRedis = redisUtil
            .get(exDigitalmoneyAccount.getId().toString());
        // 如果redis中的accout为空则重置
        if (exDigitalmoneyAccountRedis == null || exDigitalmoneyAccountRedis.getHotMoney() == null
            || exDigitalmoneyAccountRedis.getHotMoney().compareTo(new BigDecimal(0)) == 0) {
          ExDigitalmoneyAccountRedis exar = new ExDigitalmoneyAccountRedis();
          exar.setCoinCode(exDigitalmoneyAccount.getCoinCode());
          exar.setColdMoney(exDigitalmoneyAccount.getColdMoney());
          exar.setHotMoney(exDigitalmoneyAccount.getHotMoney());
          exar.setCustomerId(exDigitalmoneyAccount.getCustomerId());
          exar.setId(exDigitalmoneyAccount.getId());
          redisUtil.put(exar, exDigitalmoneyAccount.getId().toString());
        }
        map.put(exDigitalmoneyAccount.getCoinCode(), exDigitalmoneyAccount.getId());
      }
    }

    return map;
  }

  @Override
  public RemoteResult setPhone(String mobile, String username) {
    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("userName=", username));
    // 存谷歌key
    appCustomer.setPhone(mobile);
    // 谷歌认证状态
    appCustomer.setPhoneState(1);
    appCustomer.setCheckStates(1);
    appCustomerService.update(appCustomer);

    return new RemoteResult().setSuccess(true);
  }

  @Override
  public RemoteResult xstar(String userName, String trueName, String sex, String suranme,
      String country,
      String cardType, String cardId, String[] pathImg, String type, String language) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("userName=", userName));

    if (appCustomer != null) {
      // AppPersonInfo appPersonInfo = appPersonInfoService.get(new
      // QueryFilter(AppPersonInfo.class).addFilter("customerId=",
      // appCustomer.getId()));
      AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(appCustomer.getId());
      // 验证证件号是否重复
      AppPersonInfo appPersonInfo2 = appPersonInfoDao.getAppPersonInfoByCardId(cardId);
      if (appPersonInfo2 != null) {
        return new RemoteResult().setSuccess(false).setMsg("card_id_chongfu");
      }
      // 小X替换大X
      appPersonInfo.setCardId(cardId.replace("x", "X"));
      if (sex != null && sex.equals("男")) {
        appPersonInfo.setSex(0);
      } else if (sex != null && sex.equals("女")) {
        appPersonInfo.setSex(1);
      }
      if (type.equals("1")) {
        appPersonInfo.setCountry("86");

      } else {
        country = country.substring(country.lastIndexOf("+") + 1);
        appPersonInfo.setCountry(country);
      }
      appPersonInfo.setPapersType(type);
      appPersonInfo.setSurname(suranme);
      appPersonInfo.setTrueName(trueName);
      appPersonInfo.setCardType(Integer.valueOf(cardType));
      appPersonInfo.setPersonBank(pathImg[0]);
      appPersonInfo.setPersonCard(pathImg[1]);
      appPersonInfo.setFrontpersonCard(pathImg[2]);
      appPersonInfoService.update(appPersonInfo);

      try {
        // 是否开启实名认证接口
        String checkIdentityIsOpen = remoteThirdPayInterfaceService
            .getIsOpen("checkIdentityInterface");

        if ("0".equals(checkIdentityIsOpen)) {
          RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
              .getBean("remoteAppConfigService");
          String juhe_cardOpen = remoteAppConfigService.getValueByKey("juhe_cardOpen");
          String juhe_cardKey = remoteAppConfigService.getValueByKey("juhe_cardKey");
          String juhe_cardUrl = remoteAppConfigService.getValueByKey("juhe_cardUrl");
          // 聚合接口实名认证,并且国家为中国 如果为0则为开启
          if ("0".equals(juhe_cardOpen)) {
            boolean checkCard = JuheSendUtils
                .auth(appPersonInfo.getTrueName(), appPersonInfo.getCardId(),
                    juhe_cardUrl, juhe_cardKey);
            if (!checkCard) {
              return new RemoteResult().setMsg("failrealname");
            }
          }
        }

        // 开通人民币账户
        // RemoteAppAccountService remoteAppAccountService =
        // (RemoteAppAccountService)
        // ContextUtil.getBean("remoteAppAccountService");
        // remoteAppAccountService.openAccount(appCustomer,appPersonInfo,ContextUtil.getCurrencyType(),ContextUtil.getWebsite());
        // remoteAppAccountService.openAccount(appCustomer,appPersonInfo,language,ContextUtil.getWebsite());

        // 开通虚拟币账户
        // RemoteExProductService remoteExProductService =
        // (RemoteExProductService)
        // ContextUtil.getBean("remoteExProductService");
        // remoteExProductService.openDmAccount(appCustomer,appPersonInfo,null,ContextUtil.getWebsite(),ContextUtil.getCurrencyType());

        // 保存代理商
        AppAgentsCustromer appAgentsCustromer = new AppAgentsCustromer();
        appAgentsCustromer.setAddress(country);
        if (Integer.valueOf(type) == 1) {
          appAgentsCustromer.setSex(sex);
        }
        appAgentsCustromer.setStates(1);
        appAgentsCustromer.setSurname(suranme);
        appAgentsCustromer.setAgentName(appPersonInfo.getTrueName());
        appAgentsCustromer.setCustomerName(appPersonInfo.getMobilePhone());
        appAgentsCustromer.setCustomerId(appPersonInfo.getCustomerId());
        appAgentsCustromer.setPapersType(type);
        appAgentsCustromer.setPapersNo(appPersonInfo.getCardId());
        appAgentsCustromer.setRecommendCode(appPersonInfo.getMobilePhone());
        RemoteAppAgentsService remoteAppAgentsService = (RemoteAppAgentsService) ContextUtil
            .getBean("remoteAppAgentsService");
        JsonResult result = remoteAppAgentsService.saveAgents(appAgentsCustromer);
        appCustomer.setStates(1);
        appCustomer.setIsReal(1);
        /*
         * appCustomer.setFrontBank(pathImg[0]);
         * appCustomer.setFrontIDCard(pathImg[1]);
         * appCustomer.setHandIDCard(pathImg[2]);
         */
        appCustomerService.update(appCustomer);
        appPersonInfoService.update(appPersonInfo);
        // 币帐号写入姓和名
        QueryFilter filter = new QueryFilter(ExDigitalmoneyAccount.class);
        filter.addFilter("userName=", appCustomer.getUserName());
        List<ExDigitalmoneyAccount> list = exDigitalmoneyAccountService.find(filter);
        for (ExDigitalmoneyAccount exDigitalmoneyAccount : list) {
          exDigitalmoneyAccount.setTrueName(trueName);
          exDigitalmoneyAccount.setSurname(suranme);
          exDigitalmoneyAccountService.update(exDigitalmoneyAccount);
        }

        // 将注册送币改到实名(联盟网)
        // giveCoin(appCustomer.getId(), language);

        // String referralCode = appCustomer.getCommendCode();
        // 推荐送币将推荐送币改到实名
        /*
         * if(!StringUtils.isEmpty(referralCode)) { AppCommendUser
         * appCommendUser = appCommendUserService.get(new
         * QueryFilter(AppCommendUser.class).addFilter("receCode=",
         * referralCode)); AppCustomer appCustomer1 =
         * appCustomerService.get(new
         * QueryFilter(AppCustomer.class).addFilter("username=",
         * appCommendUser.getUsername()));
         * commendCoin(appCustomer1.getId()); }
         */
        return new RemoteResult().setSuccess(true);
      } catch (Exception e) {
        e.printStackTrace();
        return new RemoteResult().setMsg("card_id_chongfu");
      }
    }
    return new RemoteResult().setSuccess(false).setMsg("yishixianhuoshenhezhong");

  }

  @Override
  public RemoteResult offPhone(String mobile, String username) {
    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));
    // 存谷歌key
    // 谷歌认证状态
    appCustomer.setPhoneState(0);
    appCustomer.setOpenPhone(1);
    appCustomer.setPassDate(new Date());
    appCustomer.setPhone(null);
    appCustomer.setCheckStates(0);
    // appCustomerService.update(appCustomer);
    appCustomerService.updateNull(appCustomer);
    return new RemoteResult().setSuccess(true);
  }

  // 新增
  @Override
  public RemoteResult offLoginPhone(String mobile, String username) {
    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));
    // 存谷歌key
    // 谷歌认证状态
    appCustomer.setCheckStates(0);
    // appCustomer.setOpenPhone(1);
    appCustomer.setPassDate(new Date());
    // appCustomer.setPhone(null);
    // appCustomerService.update(appCustomer);
    appCustomerService.updateNull(appCustomer);
    return new RemoteResult().setSuccess(true);
  }

  // 新增
  @Override
  public RemoteResult setLoginPhone(String mobile, String username) {
    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));
    // 存谷歌key
    // 谷歌认证状态
    appCustomer.setCheckStates(1);
    // appCustomer.setOpenPhone(0);
    appCustomer.setPassDate(new Date());
    // appCustomer.setPhone(null);
    // appCustomerService.update(appCustomer);
    appCustomerService.updateNull(appCustomer);
    return new RemoteResult().setSuccess(true);
  }

  @Override
  public RemoteResult setvail(String username, String oldPassWord) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));
    // 数据库密码
    String passWord = appCustomer.getPassWord();

    PasswordHelper passwordHelper = new PasswordHelper();

    String encryString = passwordHelper.encryString(oldPassWord, appCustomer.getSalt());

    if (!passWord.equals(encryString)) {
      return new RemoteResult().setSuccess(false).setMsg("yuanshimimabuzhengq");
    }
    return new RemoteResult().setSuccess(true);

  }

  @Override
  public RemoteResult setpwvail(String username, String oldPassWord, String newPassWord) {

    AppCustomer appCustomer = appCustomerService
        .get(new QueryFilter(AppCustomer.class).addFilter("username=", username));
    // 数据库密码
    String passWord = appCustomer.getPassWord();

    PasswordHelper passwordHelper = new PasswordHelper();

    String encryString = passwordHelper.encryString(oldPassWord, appCustomer.getSalt());
    if (!passWord.equals(encryString)) {
      return new RemoteResult().setMsg("yuanshimimabuzhengq");
    }

    return new RemoteResult().setSuccess(true);

  }

  @Override
  public RemoteResult regphone(String mobile) {
    // AppCustomer appCustomer = appCustomerService.get(new
    // QueryFilter(AppCustomer.class).addFilter("phone=", mobile));
    List<AppCustomer> appCustomer = appCustomerService
        .find(new QueryFilter(AppCustomer.class).addFilter("phone=", mobile));
    // appCustomerService.getByPhone(mobile)
    if (appCustomer.size() >= 1) {
      return new RemoteResult().setSuccess(false).setMsg("card_id_chongfu");
    } else {
      return new RemoteResult().setSuccess(true);

    }
  }

  @Override
  public void cancelCustAllExEntrust(EntrustTrade entrustTrade) {
    Boolean isPinging = exDmPingService.isPinging(entrustTrade.getCustomerId(), null, null, null);
    String[] rt = new String[2];
    if (isPinging) {

      rt[0] = CodeConstant.CODE_FAILED;
      rt[1] = "正在平仓中，不能撤销";
    } else {
      exEntrustService.cancelCustAllExEntrust(entrustTrade);
    }

  }

  /**
   * 重置密码
   */
  @Override
  public RemoteResult updatepwdemail(String passwd, String username) {
    String encryString = null;
    try {
      QueryFilter qf = new QueryFilter(AppCustomer.class);
      qf.addFilter("userName=", username);
      AppCustomer appcustomer = appCustomerService.get(qf);
      PasswordHelper passwordHelper = new PasswordHelper();
      encryString = passwordHelper.encryString(passwd, appcustomer.getSalt());
      appcustomer.setPassWord(encryString);
      appCustomerService.update(appcustomer);

      /*
       * QueryFilter pe = new QueryFilter(AppCustomer.class);
       * pe.addFilter("mobilePhone", username); AppPersonInfo
       * appPersonInfo = appPersonInfoService.get(pe);
       * appPersonInfo.setEmailCode(UUIDUtil.getUUID());
       * appPersonInfoService.update(appPersonInfo);
       */
    } catch (Exception e) {
      // TODO: handle exception
      new RemoteResult().setSuccess(false);
    }
    return new RemoteResult().setSuccess(true).setObj(encryString);
  }

  @Override
  public RemoteResult forget(String email, String password) {
    // 初始化数据AppPersonInfo
    QueryFilter qf = new QueryFilter(AppPersonInfo.class);
    qf.addFilter("mobilePhone=", email);
    AppPersonInfo appPersonInfo = appPersonInfoService.get(qf);
    if (appPersonInfo == null || "".equals(appPersonInfo)) {
      return new RemoteResult().setSuccess(false);
    }
    appPersonInfo.setEmail(UUIDUtil.getUUID());
    appPersonInfoService.update(appPersonInfo);
    return new RemoteResult().setSuccess(true).setObj(appPersonInfo.getEmail());
  }

  @Override
  public RemoteResult emailvail(String code) {
    // TODO Auto-generated method stub
    QueryFilter filter = new QueryFilter(AppPersonInfo.class);
    filter.addFilter("email=", code);
    AppPersonInfo appPersonInfo = appPersonInfoService.get(filter);
    if (appPersonInfo != null) {
      return new RemoteResult().setSuccess(true);
    } else {
      return new RemoteResult().setSuccess(false);
    }
  }

  @Override
  public BigDecimal myBtcCount(Long customerId, String code) {

    // TODO Auto-generated method stub
    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    UserRedis userRedis = redisUtil.get(customerId.toString());

    BigDecimal computeBTC = computeBTC(userRedis, code);
    return computeBTC;
  }

  @Override
  public List<Map<String, List<EntrustTrade>>> findExEntrustBycust(Long customerId) {
    QueryFilter queryFilter = new QueryFilter(ExCointoCoin.class);
    queryFilter.addFilter("state=", 1);
    List<ExCointoCoin> list = exCointoCoinService.find(queryFilter);
    List<Map<String, List<EntrustTrade>>> listml = new ArrayList<Map<String, List<EntrustTrade>>>();
    Map<String, List<EntrustTrade>> entrustedmap = new HashMap<String, List<EntrustTrade>>();
    Map<String, List<EntrustTrade>> entrustingmap = new HashMap<String, List<EntrustTrade>>();
    for (ExCointoCoin ec : list) {
      Map<String, Object> maping = new HashMap<String, Object>();
      maping.put("customerId", customerId);
      maping.put("coinCode", ec.getCoinCode());
      maping.put("fixPriceCoinCode", ec.getFixPriceCoinCode());
      maping.put("counting", EntrustByUser.ingMAXsize);
      List<ExEntrust> listing = exEntrustDao.getExIngBycustomerId(maping);
      String listings = Mapper.objectToJson(listing);
      List<EntrustTrade> entrustTradinglist = JSON.parseArray(listings, EntrustTrade.class);
      entrustingmap.put(ec.getCoinCode() + "_" + ec.getFixPriceCoinCode(), entrustTradinglist);

      Map<String, Object> map = new HashMap<String, Object>();
      map.put("customerId", customerId);
      map.put("coinCode", ec.getCoinCode());
      map.put("fixPriceCoinCode", ec.getFixPriceCoinCode());
      map.put("counted", EntrustByUser.edMAXsize);
      List<ExEntrust> listied = exEntrustDao.getExEdBycustomerId(map);
      String listeds = Mapper.objectToJson(listied);
      List<EntrustTrade> entrustTradedlist = JSON.parseArray(listeds, EntrustTrade.class);
      entrustedmap.put(ec.getCoinCode() + "_" + ec.getFixPriceCoinCode(), entrustTradedlist);

    }
    listml.add(entrustedmap);
    listml.add(entrustingmap);
    return listml;
  }

  @Override
  public RemoteResult selectCommend(String username, String property) {
    // TODO Auto-generated method stub
    RemoteResult RemoteResult = new RemoteResult();
    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("userName=", username);
    AppCustomer appcustomer = appCustomerService.get(qf);
    int commendCount = appCustomerService.commendCount(appcustomer.getReferralCode());
    // appAgentsCustromerService.findAgentsForMoneyToList(appcustomer.getUserName());
    commendCode ac = new commendCode();
    ac.setCommendCount(commendCount);
    ac.setCommendCode(appcustomer.getReferralCode());
    // ac.setCommendAmount(agentsForMoney.getSurplusMoney());
    ac.setCommendLink(property + "/regcode/" + appcustomer.getReferralCode());
    ac.setUserName(appcustomer.getUserName());
    // ac.setCommendNoAmount(agentsForMoney.getDeawalMoney());
    return RemoteResult.setSuccess(true).setObj(ac);
  }

  /*
   * @Override public List<commendCode> selectCommendfind(String username) {
   *
   * List<commendCode> list = new ArrayList<commendCode>();
   * List<AgentsForMoney> lists =
   * appAgentsCustromerService.findAgentsForMoneyToListOne(username);
   * for(AgentsForMoney agentsForMoney :lists){ commendCode commendCode=new
   * commendCode(); // map.put("key1",agentsForMoney.getDeawalMoney());
   * commendCode.setDeawalMoney(agentsForMoney.getDeawalMoney());
   * commendCode.setFixPriceCoinCode(agentsForMoney.getFixPriceCoinCode());
   * commendCode.setSurplusMoney(agentsForMoney.getSurplusMoney());
   * list.add(commendCode); } return list;
   *
   * }
   */

  /*
   * @Override public List<commendCode> selectCommendfind(String username) {
   * List<commendCode> deptVosList = new ArrayList<commendCode>(); //清算奖励金额
   * List<AppCommendTrade> appCommendTrades =
   * appCommendTradeService.findByUsername(username);
   *
   * //清算奖励金额 BigDecimal totalMoney = new BigDecimal(0); for(AppCommendTrade
   * commendTrade:appCommendTrades){ commendCode commendCode=new
   * commendCode(); String deliveryName = commendTrade.getDeliveryName();
   * String[] split = deliveryName.split("@"); String uname=null;
   * if(split[0].length()==1){ uname="*@"+split[1]; }else{ StringBuilder sb =
   * new StringBuilder(); for(int i = 0;i<split[0].length()-1;i++){
   * sb.append("*"); }
   * uname=split[0].substring(0,1)+sb.toString()+"@"+split[1]; }
   * commendCode.setName(uname);
   * if(commendTrade.getCoinCode().equals("USDT")){ totalMoney =
   * commendTrade.getFeemoney(); }else{ String currentMoney;
   * if(commendTrade.getCurrentMoney()!=null) { currentMoney =
   * commendTrade.getCurrentMoney().toString(); }else{ currentMoney="0"; }
   * if(currentMoney!=null){ totalMoney = totalMoney.add(new
   * BigDecimal(currentMoney).multiply(commendTrade.getFeemoney())); } }
   * commendCode.setCommendMoney(totalMoney);
   * commendCode.setFixPriceCoinCode("USDT"); deptVosList.add(commendCode);
   * totalMoney = new BigDecimal(0); } Map<String,commendCode> commends = new
   * HashMap<>(); for(commendCode codef:deptVosList){
   * if(commends.containsKey(codef.getName())){ commendCode commendCode =
   * commends.get(codef.getName());
   * commendCode.setCommendMoney(commendCode.getCommendMoney().add(codef.
   * getCommendMoney())); commends.put(commendCode.getName(),commendCode);
   * }else { commends.put(codef.getName(), codef); } } List<commendCode> codes
   * = new ArrayList<>(); for(Map.Entry<String, commendCode>
   * code:commends.entrySet()){ commendCode commendCode = code.getValue();
   * codes.add(commendCode); } return codes; }
   */

  /**
   * @Description: 返回已派发佣金
   * @Author: zongwei
   * @CreateDate: 2018/6/7 20:28
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/6/7 20:28
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
	/*@Override
	public List<commendCode> selectCommendfind(String username) {
		List<commendCode> deptVosList = new ArrayList<commendCode>();
		// 清算奖励金额
		QueryFilter qf = new QueryFilter(AppCommendRebat.class);
		qf.addFilter("trueName=", username);
		List<AppCommendRebat> appCommendRebats = appCommendRebatService.find(qf);
		// 清算奖励金额
		BigDecimal totalMoney = new BigDecimal(0);
		for (AppCommendRebat appCommendRebat : appCommendRebats) {
			commendCode commendCode = new commendCode();
			String deliveryName = appCommendRebat.getTrueName();
			String[] split = deliveryName.split("@");
			String uname = null;
			if (split[0].length() == 1) {
				uname = "*@" + split[1];
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < split[0].length() - 1; i++) {
					sb.append("*");
				}
				uname = split[0].substring(0, 1) + sb.toString() + "@" + split[1];
			}
			commendCode.setName(uname);
			commendCode.setCommendMoney(appCommendRebat.getRebatMoney());
			commendCode.setFixPriceCoinCode(appCommendRebat.getCoinCode());
			deptVosList.add(commendCode);
			totalMoney = new BigDecimal(0);
		}
		Map<String, commendCode> commends = new HashMap<>();
		for (commendCode codef : deptVosList) {
			if (commends.containsKey(codef.getFixPriceCoinCode())) {
				commendCode commendCode = commends.get(codef.getFixPriceCoinCode());
				commendCode.setCommendMoney(commendCode.getCommendMoney().add(codef.getCommendMoney()));
				commends.put(codef.getFixPriceCoinCode(), commendCode);
			} else {
				commends.put(codef.getFixPriceCoinCode(), codef);
			}
		}
		List<commendCode> codes = new ArrayList<>();
		for (Map.Entry<String, commendCode> code : commends.entrySet()) {
			commendCode commendCode = code.getValue();
			codes.add(commendCode);
		}
		return codes;
	}*/

  //修改APP端的返佣状态
  @Override
  public List<commendCode> selectCommendfind(String username) {
    List<commendCode> deptVosList = new ArrayList<commendCode>();

    List<AppCommendMoney> selectCommend = appCommendMoneyService.selectMoneyCommend(username);
    if (selectCommend != null) {
      for (AppCommendMoney appCommendMoney : selectCommend) {
        commendCode commendcode = new commendCode();
        String name = appCommendMoney.getCustromerName();

        commendcode.setMoneyNum(appCommendMoney.getMoneyNum());
        commendcode.setFixPriceCoinCode(appCommendMoney.getFixPriceCoinCode());
        commendcode.setCommendMoney(appCommendMoney.getPaidMoney());
        deptVosList.add(commendcode);
      }
      return deptVosList;
    }
    return null;
  }


  int lent = 1;
  // 递归文本路径sgh
  List<AppCommendUser> deptVosList2 = new ArrayList<AppCommendUser>();

  public List<AppCommendUser> findNumber(Long uid, String username, int num) {
    num++;
    AppCommendUser deptVo2 = new AppCommendUser();
    List<AppCommendUser> find2 = null;
    String coinCode = null;
    if (num != 1) {
      QueryFilter AppCommendUser = new QueryFilter(AppCommendUser.class);
      AppCommendUser.addFilter("pid=", uid);
      find2 = appCommendUserService.find(AppCommendUser);
    } else if (num == 1) {
      deptVosList2.clear();
      QueryFilter AppCommendUser = new QueryFilter(AppCommendUser.class);
      AppCommendUser.addFilter("uid=", uid);
      find2 = appCommendUserService.find(AppCommendUser);
    }

    BigDecimal moneyNum = new BigDecimal("0");
    if  (find2.size() > 0) {
      for (AppCommendUser deptVo : find2) {
        deptVo2 = new AppCommendUser();
        BigDecimal moneyNumone = new BigDecimal("0");
        // 佣金金额转为USDT
        List<AppCommendTrade> selectCommendTrade = appCommendTradeService
            .selectCommendTrade(deptVo.getUsername());
        for (AppCommendTrade appCommendTrade : selectCommendTrade) {
          coinCode = appCommendTrade.getFixPriceCoinCode() + "_" + "USDT";
          String price = newTransactionPrice(coinCode);
          System.out.println(coinCode + "当前价" + price);
          BigDecimal money = appCommendTrade.getRewardmoney().multiply(new BigDecimal(price));
          moneyNumone = moneyNum.add(new BigDecimal(money.toString()));
        }
        // 交易数量
        int count = 0;
        if (deptVo.getUsername() != null && !"".equals(deptVo.getUsername())) {
          count = exOrderInfoService.selectTransactionCount(deptVo.getUsername());
          System.out.println("交易数量" + count);
        }
        // 计算交易总量并转为USDT
        List<ExOrderInfo> selectTransaction = exOrderInfoService
            .selectTransaction(deptVo.getUsername());
        if (selectTransaction.size() > 0) {
          for (ExOrderInfo exOrderInfo : selectTransaction) {
            coinCode = exOrderInfo.getCoinCode() + "_" + exOrderInfo.getFixPriceCoinCode();
            String price = newTransactionPrice(coinCode);
            BigDecimal money = exOrderInfo.getTransactionSum().multiply(new BigDecimal(price));
            moneyNum = moneyNum.add(new BigDecimal(money.toString()));
          }
        }
        deptVo2.setComLeven(num);
        deptVo2.setMoneyNum(moneyNum);
        deptVo2.setUsername(deptVo.getUsername());
        deptVo2.setCommendMoney(moneyNumone);
        deptVo2.setExorderCount(count);
        deptVo2.setCoinCode(coinCode);
        deptVo2.setUid(deptVo.getUid());
        deptVosList2.add(deptVo2);
        findNumber(deptVo2.getUid(), username, num);
      }
    }
    return deptVosList2;

  }

  @Override
  public List<commendCode> selectCommendRanking() {
    List<commendCode> deptVosList = new ArrayList<commendCode>();
    List<AppCommendRank> findAll = appCommendRankService.findAll();
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    Integer RankingNum = Integer.valueOf(parseObject.get("RankingNum").toString());
    if (RankingNum > findAll.size()) {
      RankingNum = findAll.size();
    }
    for (int i = 0; i < RankingNum; i++) {
      commendCode commendCode = new commendCode();
      commendCode.setName(findAll.get(i).getUserName());
      commendCode
          .setFixPriceCoinCode(findAll.get(i).getFixMoney() + "" + findAll.get(i).getFixCoin());
      deptVosList.add(commendCode);
    }
    return deptVosList;

  }

  public String newTransactionPrice(String coinCode) {

    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String price = redisService.get(coinCode + ":currentExchangPrice");
    if (price == null || "".equals(price)) {
      price = "1";
    }

    return price;

  }

  @Override
  public List<C2cOrder> c2cNewBuyOrder() {

    QueryFilter filter = new QueryFilter(C2cTransaction.class);
    filter.addFilter("transactionType=", 1);
    filter.addFilter("status=", 2);
    filter.setOrderby("id desc");
    filter.setPage(1);
    filter.setPageSize(2);
    Page<C2cTransaction> findPage = c2cTransactionService.findPage(filter);
    List<C2cTransaction> list = findPage.getResult();

    ArrayList<C2cOrder> arrayList = new ArrayList<C2cOrder>();
    if (list != null && list.size() > 0) {
      for (C2cTransaction c2cTransaction : list) {
        C2cOrder c2cOrder = new C2cOrder();
        c2cOrder.setTransactionTime(c2cTransaction.getCreated());
        c2cOrder.setCreateTime(
            new SimpleDateFormat("YYYY:MM:DD HH:mm:ss").format(c2cTransaction.getCreated()));
        c2cOrder.setCoinCode(c2cTransaction.getCoinCode());
        c2cOrder.setTransactionNum(c2cTransaction.getTransactionNum());
        c2cOrder.setTransactionPrice(c2cTransaction.getTransactionPrice());
        c2cOrder.setTransactionCount(c2cTransaction.getTransactionCount());
        c2cOrder.setTransactionMoney(c2cTransaction.getTransactionMoney());
        c2cOrder.setTransactionType(c2cTransaction.getTransactionType());
        c2cOrder.setStatus(c2cTransaction.getStatus());
        c2cOrder.setUserName(
            c2cTransaction.getUserName().substring(0, 2) + "****" + c2cTransaction.getUserName()
                .substring(
                    c2cTransaction.getUserName().length() - 3,
                    c2cTransaction.getUserName().length()));
        arrayList.add(c2cOrder);
      }
    }

    return arrayList;

  }

  public Map<String, Object> selectRechargeCoin(String username, String coinCode) {
    Map<String, Object> map = new HashMap<String, Object>();

    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("userName=", username);
    AppCustomer appCustomer = appCustomerService.get(qf);
    if (appCustomer != null) {
      map.put("customerId", appCustomer.getId());

      QueryFilter qf1 = new QueryFilter(ExDigitalmoneyAccount.class);
      qf1.addFilter("coinCode=", coinCode);
      qf1.addFilter("customerId=", appCustomer.getId());
      ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(qf1);

      map.put("accountId", exDigitalmoneyAccount.getId());
    }
    return map;
  }

  @Override
  public List<C2cOrder> c2cNewSellOrder() {

    QueryFilter filter = new QueryFilter(C2cTransaction.class);
    filter.addFilter("transactionType=", 2);
    filter.addFilter("status=", 2);
    filter.setOrderby("id desc");
    filter.setPage(1);
    filter.setPageSize(2);
    Page<C2cTransaction> findPage = c2cTransactionService.findPage(filter);
    List<C2cTransaction> list = findPage.getResult();

    ArrayList<C2cOrder> arrayList = new ArrayList<C2cOrder>();
    if (list != null && list.size() > 0) {
      for (C2cTransaction c2cTransaction : list) {
        C2cOrder c2cOrder = new C2cOrder();
        c2cOrder.setTransactionTime(c2cTransaction.getCreated());
        c2cOrder.setCreateTime(
            new SimpleDateFormat("YYYY:MM:DD HH:mm:ss").format(c2cTransaction.getCreated()));
        c2cOrder.setCoinCode(c2cTransaction.getCoinCode());
        c2cOrder.setTransactionNum(c2cTransaction.getTransactionNum());
        c2cOrder.setTransactionPrice(c2cTransaction.getTransactionPrice());
        c2cOrder.setTransactionCount(c2cTransaction.getTransactionCount());
        c2cOrder.setTransactionMoney(c2cTransaction.getTransactionMoney());
        c2cOrder.setTransactionType(c2cTransaction.getTransactionType());
        c2cOrder.setStatus(c2cTransaction.getStatus());
        c2cOrder.setUserName(
            c2cTransaction.getUserName().substring(0, 2) + "****" + c2cTransaction.getUserName()
                .substring(
                    c2cTransaction.getUserName().length() - 3,
                    c2cTransaction.getUserName().length()));
        arrayList.add(c2cOrder);
      }
    }

    return arrayList;

  }

  public User selectByCustomerId(Long customerId) {
    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("id=", customerId);
    AppCustomer appCustomer = appCustomerService.get(qf);
    User user = new User();
    if (appCustomer != null) {
      QueryFilter qfw = new QueryFilter(AppPersonInfo.class);
      qfw.addFilter("customerId=", appCustomer.getId());
      AppPersonInfo apppersonInfo = appPersonInfoService.get(qfw);

      /*
       * user.setUsername(appCustomer.getUserName());
       * user.setUserCode(appCustomer.getUserCode());
       * user.setAccountPassWord(appCustomer.getAccountPassWord());
       * user.setIsReal(appCustomer.getIsReal());
       * user.setIsDelete(appCustomer.getIsDelete());
       * user.setIsChange(appCustomer.getIsChange());
       * user.setCustomerId(appCustomer.getId());
       * user.setMobile(appCustomer.getUserName());
       * user.setTruename(apppersonInfo.getTrueName());
       * user.setIsLock(appCustomer.getIsLock());
       * user.setCustomerType(apppersonInfo.getCustomerType());
       * user.setSalt(appCustomer.getSalt());
       * user.setSaasId(apppersonInfo.getSaasId());
       * user.setCardcode(apppersonInfo.getCardId());
       * user.setEmail(apppersonInfo.getEmail());
       * user.setSex(apppersonInfo.getSex());
       * user.setPostalAddress(apppersonInfo.getPostalAddress());
       */
      user.setCardcode(apppersonInfo.getCardId());
      user.setUsername(appCustomer.getUserName());
      user.setUserCode(appCustomer.getUserCode());
      user.setAccountPassWord(appCustomer.getAccountPassWord());
      user.setIsReal(appCustomer.getIsReal() == null ? 0 : appCustomer.getIsReal().intValue());
      user.setIsDelete(appCustomer.getIsDelete());
      user.setIsChange(appCustomer.getIsChange());
      user.setCustomerId(appCustomer.getId());
      user.setMobile(appCustomer.getUserName());
      user.setTruename(apppersonInfo.getTrueName());
      user.setSurname(apppersonInfo.getSurname());
      user.setIsLock(appCustomer.getIsLock());
      user.setCustomerType(apppersonInfo.getCustomerType());
      user.setSalt(appCustomer.getSalt());
      user.setSaasId(appCustomer.getSaasId());
      user.setGoogleKey(appCustomer.getGoogleKey());
      user.setGoogleState(appCustomer.getGoogleState());
      user.setMessIp(appCustomer.getMessIp());
      user.setPassDate(appCustomer.getPassDate());
      user.setPhone(appCustomer.getPhone());
      user.setPhoneState(appCustomer.getPhoneState());
      user.setStates(appCustomer.getStates());
      user.setPassword(appCustomer.getPassWord());
      user.setUuid(appCustomer.getUuid());
      // 新增类添加
      user.setCheckStates(appCustomer.getCheckStates());
      user.setMail(appCustomer.getMail());
      user.setMailStates(appCustomer.getMailStates());

    }
    return user;
  }

  @Override
  public boolean setc2cTransactionStatus2(String transactionNum, int status2, String remark) {
    QueryFilter filter = new QueryFilter(C2cTransaction.class);
    filter.addFilter("transactionNum=", transactionNum);
    C2cTransaction c2cTransaction = c2cTransactionService.get(filter);

    // 订单不等于完成状态
    if (c2cTransaction != null) {

      if (c2cTransaction.getStatus() != 2) {
        c2cTransaction.setStatus2(status2);

        if (!StringUtils.isEmpty(remark)) {
          c2cTransaction.setRemark(remark);
        }

        // 如果是交易失败，和交易关闭,直接交易订单否决
        if (status2 == 3 || status2 == 4) {
          c2cTransaction.setStatus(3);
          ;

          // 如果是卖单交易关闭,进行资金撤消
          if (status2 == 4 && c2cTransaction.getTransactionType() == 2) {
            c2cTransactionService.close(c2cTransaction.getId(), null);
          }

        }
        c2cTransactionService.update(c2cTransaction);
      }
      return true;

    }
    return false;
  }

  @Override
  public boolean setc2cTransactionStatus(String transactionNum, int status2, String remark) {
    QueryFilter filter = new QueryFilter(C2cTransaction.class);
    filter.addFilter("transactionNum=", transactionNum);
    filter.addFilter("status2=", 2);
    C2cTransaction c2cTransaction = c2cTransactionService.get(filter);

    // 订单不等于完成状态
    if (c2cTransaction != null) {

      if (c2cTransaction.getStatus() != 2) {
        c2cTransaction.setStatus(status2);

        if (!StringUtils.isEmpty(remark)) {
          c2cTransaction.setRemark(remark);
        }

        // 如果是交易失败，和交易关闭,直接交易订单否决
        if (status2 == 3 || status2 == 4) {
          c2cTransaction.setStatus(3);
          ;

          // 如果是卖单交易关闭,进行资金撤消
          if (status2 == 4 && c2cTransaction.getTransactionType() == 2) {
            c2cTransactionService.close(c2cTransaction.getId(), null);
          }

        }
        c2cTransactionService.update(c2cTransaction);
      }
      return true;

    }
    return false;
  }

  @Override
  public String[] checkPing(Long customerId) {
    return exDmPingService.checkPing(customerId);
  }

  @Override
  public FrontPage c2clist(Map<String, String> params) {
    return c2cTransactionService.c2clist(params);
  }

  @Override
  public List<String> findOpenTibi() {
    QueryFilter filter = new QueryFilter(ExProduct.class);
    filter.addFilter("openTibi=", "1");
    List<ExProduct> list = exProductService.find(filter);

    List<String> coinList = new ArrayList<String>();
    if (list != null) {
      for (ExProduct exProduct : list) {
        coinList.add(exProduct.getCoinCode());
      }
      return coinList;
    }
    return Collections.emptyList();

  }

  @Override
  public RemoteResult registMobile(String mobile, String password, String referralCode,
      String country,
      String language) {

    try {

      // 查询此用户有没有被注册
      QueryFilter f = new QueryFilter(AppPersonInfo.class);
      f.addFilter("mobilePhone=", mobile);
      f.addFilter("country=", country);
      AppPersonInfo _appPersonInfo = appPersonInfoService.get(f);
      if (_appPersonInfo != null) {
        return new RemoteResult().setMsg("user_reg");
      }

      QueryFilter f1 = new QueryFilter(AppCustomer.class);
      f1.addFilter("phone_like", "%" + mobile + "%");
      AppCustomer _appCustomer2 = appCustomerService.get(f1);
      if (_appCustomer2 != null) {
        return new RemoteResult().setMsg("user_reg");
      }

      AppCustomer customer = new AppCustomer();

      customer.setUserName(mobile);
      customer.setPassWord(password);
      // 设置谷歌认证0
      customer.setGoogleState(0);
      // 手机认证初始化为1
      customer.setPhoneState(1);
      String Phone = country + ' ' + mobile;
      customer.setPhone(Phone);
      customer.setHasEmail(1);
      customer.setStates(0);
      customer.setIsReal(0);
      // 设置uid
      customer.setUserCode(UUIDUtil.getUUID());
      customer.setReferralCode(UUIDUtil.getUUID());
      if (referralCode != null) {
        customer.setCommendCode(referralCode);
      }
      // 设置uid
      customer.setUserCode(UUIDUtil.getUUID());

      PasswordHelper passwordHelper = new PasswordHelper();
      // 密码加密与加盐
      passwordHelper.encryptPassword(customer);
      appCustomerService.save(customer);

      // 初始化数据AppPersonInfo
      AppPersonInfo appPersonInfo = new AppPersonInfo();
      appPersonInfo.setCountry(country);
      appPersonInfo.setCustomerSource(0);
      appPersonInfo.setMobilePhone(mobile);
      appPersonInfo.setCustomerId(customer.getId());
      appPersonInfo.setCustomerType(1);
      appPersonInfo.setEmailCode(UUIDUtil.getUUID());
      appPersonInfoService.save(appPersonInfo);

      // 开通人民币账户
      RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil
          .getBean("remoteAppAccountService");
      remoteAppAccountService
          .openAccount(customer, appPersonInfo, language, ContextUtil.getWebsite());

      // 开通虚拟币账户
      RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil
          .getBean("remoteExProductService");
      remoteExProductService
          .openDmAccount(customer, appPersonInfo, null, ContextUtil.getWebsite(), language);

      appCustomerService.update(customer);
      appPersonInfoService.update(appPersonInfo);

      // 注册送币(联盟网需要实名认证后才送币)
      giveCoin(customer.getId(), language);

      appCommendUserService.saveObj(customer);

      // add by zongwei 注册送积分
      givePoit(customer.getId(), referralCode);

      // 推荐送币(联盟网需要实名认证后才给推荐人送币)
      if (!StringUtils.isEmpty(referralCode)) {
        AppCommendUser appCommendUser = appCommendUserService
            .get(new QueryFilter(AppCommendUser.class).addFilter("receCode=", referralCode));
        AppCustomer appCustomer = appCustomerService
            .get(new QueryFilter(AppCustomer.class)
                .addFilter("username=", appCommendUser.getUsername()));
        commendCoin(appCustomer.getId());
      }

      // 注册成功
      return new RemoteResult().setSuccess(true).setMsg(customer.getUserCode())
          .setObj(appPersonInfo.getEmailCode());

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new RemoteResult().setSuccess(false).setMsg("error");

  }

  @Override
  public RemoteResult registMobile1(String mobile, String password, String referralCode,
      String country,
      String language) {

    try {

      // 查询此用户有没有被注册
      QueryFilter f = new QueryFilter(AppPersonInfo.class);
      f.addFilter("mobilePhone=", mobile);
      f.addFilter("country=", country);
      AppPersonInfo _appPersonInfo = appPersonInfoService.get(f);
      if (_appPersonInfo != null) {
        return new RemoteResult().setMsg("user_reg");
      }

      QueryFilter f1 = new QueryFilter(AppCustomer.class);
      f1.addFilter("phone_like", "%" + mobile + "%");
      AppCustomer _appCustomer2 = appCustomerService.get(f1);
      if (_appCustomer2 != null) {
        return new RemoteResult().setMsg("user_reg");
      }

      AppCustomer customer = new AppCustomer();

      customer.setUserName(mobile);
      customer.setPassWord(password);
      // 设置谷歌认证0
      customer.setGoogleState(0);
      // 手机认证初始化为1
      customer.setPhoneState(1);
      String Phone = country + ' ' + mobile;
      customer.setPhone(Phone);
      customer.setHasEmail(1);
      customer.setStates(0);
      customer.setIsReal(0);
      // 设置平台标识
      customer.setPlatform("花链");
      // 设置uid
      customer.setUserCode(UUIDUtil.getUUID());
      customer.setReferralCode(UUIDUtil.getUUID());
      if (referralCode != null) {
        customer.setCommendCode(referralCode);
      }
      // 设置uid
      customer.setUserCode(UUIDUtil.getUUID());

      PasswordHelper passwordHelper = new PasswordHelper();
      // 密码加密与加盐
      passwordHelper.encryptPassword(customer);
      appCustomerService.save(customer);

      // 初始化数据AppPersonInfo
      AppPersonInfo appPersonInfo = new AppPersonInfo();
      appPersonInfo.setCountry(country);
      appPersonInfo.setCustomerSource(0);
      appPersonInfo.setMobilePhone(mobile);
      appPersonInfo.setCustomerId(customer.getId());
      appPersonInfo.setCustomerType(1);
      appPersonInfo.setEmailCode(UUIDUtil.getUUID());
      appPersonInfoService.save(appPersonInfo);

      // 开通人民币账户
      RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil
          .getBean("remoteAppAccountService");
      remoteAppAccountService
          .openAccount(customer, appPersonInfo, language, ContextUtil.getWebsite());

      // 开通虚拟币账户
      RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil
          .getBean("remoteExProductService");
      remoteExProductService
          .openDmAccount(customer, appPersonInfo, null, ContextUtil.getWebsite(), language);

      appCustomerService.update(customer);
      appPersonInfoService.update(appPersonInfo);

      // 注册送币
      giveCoin(customer.getId(), language);
      // 推荐人
      appCommendUserService.saveObj(customer);
      // add by zongwei 注册送积分
      givePoit(customer.getId(), referralCode);

      // 推荐送币
      if (!StringUtils.isEmpty(referralCode)) {
        AppCommendUser appCommendUser = appCommendUserService
            .get(new QueryFilter(AppCommendUser.class).addFilter("receCode=", referralCode));
        AppCustomer appCustomer = appCustomerService
            .get(new QueryFilter(AppCustomer.class)
                .addFilter("username=", appCommendUser.getUsername()));
        commendCoin(appCustomer.getId());
      }

      // 注册成功
      return new RemoteResult().setSuccess(true).setMsg(customer.getUserCode())
          .setObj(appPersonInfo.getEmailCode());

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new RemoteResult().setSuccess(false).setMsg("error");

  }

  @Override
  public RemoteResult canTakeMoney(String customerId) {
    RemoteResult jsonResult = new RemoteResult();
    // 获得redis缓存
    RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
    UserRedis userRedis = redisUtil.get(customerId);

    // 获取资金账户，判断资金账户余额
    AppAccountRedis accountRedis = UserRedisUtils.getAccount(userRedis.getAccountId().toString());
    if (null != accountRedis) {
      if (accountRedis.getHotMoney().compareTo(new BigDecimal("0")) < 0
          || accountRedis.getColdMoney().compareTo(new BigDecimal("0")) < 0) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("资金账户余额不足");
        return jsonResult;
      }
    }
    // 获取币账户
    Map<String, Long> dmAccountId = userRedis.getDmAccountId();
    for (Map.Entry<String, Long> entry : dmAccountId.entrySet()) {
      if (entry.getKey() != null && entry.getValue() != null) {
        ExDigitalmoneyAccountRedis ear = UserRedisUtils
            .getAccount(entry.getValue().toString(), entry.getKey());
        if (ear.getHotMoney().compareTo(new BigDecimal("0")) < 0
            || ear.getColdMoney().compareTo(new BigDecimal("0")) < 0) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg(ear.getCoinCode() + "余额不足");
          return jsonResult;
        }
      }
    }

    return jsonResult.setSuccess(true);
  }

  /**
   * 查询手机号是否存在
   *
   * @author zongwei
   * @time 2018-04-27
   */
  public RemoteResult checknoPhone(String telphone, String country) {
    QueryFilter qf = new QueryFilter(AppCustomer.class);
    qf.addFilter("userName=", telphone);
    AppCustomer appcustomer = appCustomerService.get(qf);

    QueryFilter ph = new QueryFilter(AppCustomer.class);
    ph.addFilter("phone=", country + ' ' + telphone);
    AppCustomer phappcustomer = appCustomerService.get(ph);

    if (appcustomer == null && phappcustomer == null) {
      return new RemoteResult().setSuccess(true);
    } else {
      return new RemoteResult().setSuccess(false);
    }
  }

  public RemoteResult getOtcTransactionAll(String transactionType, String status, String status2) {
    QueryFilter filter = new QueryFilter(OtcTransaction.class);
    if (transactionType != null) {
      filter.addFilter("transactionType=", transactionType);
    }
    if (status != null) {
      filter.addFilter("status=", status);
    }
    if (status2 != null) {
      filter.addFilter("status2=", status2);
    }
    filter.setOrderby("transactionPrice asc");
    List<OtcTransaction> list = otcTransactionService.find(filter);
    return new RemoteResult().setSuccess(true).setObj(list);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.mz.manage.remote.RemoteManageService#getappDictionary(java.lang.String)
   * 查询字典表
   */
  public Map<String, String> getappDictionary(String key) {
    Map<String, String> map = new HashMap<>();
    List<AppDicOnelevel> findListBypDicKey = appDicOnelevelService.findListBypDicKey2(key);
    for (AppDicOnelevel appDicOnelevel2 : findListBypDicKey) {
      map.put(appDicOnelevel2.getItemName(), appDicOnelevel2.getItemValue());

    }

    return map;

  }

  /*
   * 保存第三方用户信息 zongwei 20180517
   */
  public RemoteResult updateThird(User user) {
    AppCustomer appcustomer = new AppCustomer();
    try {

      QueryFilter qf = new QueryFilter(AppCustomer.class);
      qf.addFilter("userName=", user.getUsername());
      appcustomer = appCustomerService.get(qf);
      // if(appcustomer.getThirdUserPw() == null) {
      appcustomer.setThirdUserPw(user.getThirdUserPw());
      // }
      appcustomer.setThirdUserName(user.getThirdUserName());
      appcustomer.setPlatform(user.getPlatform());
      appCustomerService.update(appcustomer);
    } catch (Exception e) {
      return new RemoteResult().setMsg(e.getMessage()).setSuccess(false);
    }
    return new RemoteResult().setSuccess(true).setObj(appcustomer);
  }

  /**
   * 查询otc从清单中购买
   *
   * @param id 清单id
   * @param customerId 操作者id
   * @author zongwei
   * @time 2018-05-08
   */
  public RemoteResult createOrderTransaction(Long customerId, Long id,
      BigDecimal transactioncount) {
    JsonResult jr = otcOrderTransactionService
        .createOrderTransaction(customerId, id, transactioncount);
    if (jr != null) {
      if (jr.getSuccess()) {
        return new RemoteResult().setSuccess(true).setObj(jr.getObj());
      } else {
        return new RemoteResult().setMsg(jr.getMsg());
      }
    }
    return new RemoteResult();
  }

  /*
   *
   *
   * 查询otc委托
   */
  public RemoteResult getOtcTransaction(String transactionType) {
    QueryFilter filter = new QueryFilter(OtcTransaction.class);

    List<OtcTransaction> list = otcTransactionService.find(filter);
    return new RemoteResult().setSuccess(true).setObj(list);
  }

  public RemoteResult getOtclist(String transactionType, String coinCode, String OrderByClause) {
    List<OtcTransaction> list = otcTransactionService
        .getOtclist(transactionType, coinCode, OrderByClause);
    ArrayList<JSONObject> otclinsts = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      Iterator iterator = list.iterator();
      if (iterator.hasNext()) {
        JSONObject JSON = new JSONObject();
        AppPersonInfo appPersonInfo = appPersonInfoService
            .getByCustomerId(list.get(i).getCustomerId());
        if (appPersonInfo.getOtcFinishCount() == null) {
          appPersonInfo.setOtcFinishCount(new BigDecimal(0));
        }
        QueryFilter queryFilter = new QueryFilter(AppBankCard.class);
        queryFilter.addFilter("customerId=", list.get(i).getCustomerId());
        queryFilter.addFilter("isDelete=", 0);
        List<AppBankCard> bankCardsfind = appBankCardService.find(queryFilter);

        String online = "true";
        String value = redisService.get("online:username:" + appPersonInfo.getMobilePhone());

        if (value == null) {
          online = "false";
        }

        String cardNumberFlag = "false";
        String weChatFlag = "false";
        String alipayFlag = "false";
        if (bankCardsfind != null && !bankCardsfind.isEmpty()) {
          if (bankCardsfind.get(0).getCardNumber() != null
              && bankCardsfind.get(0).getCardNumber() != ""
              && !bankCardsfind.get(0).getCardNumber().isEmpty()) {
            cardNumberFlag = "true";
          }
          if (bankCardsfind.get(0).getWeChat() != null && bankCardsfind.get(0).getWeChat() != ""
              && !bankCardsfind.get(0).getWeChat().isEmpty()) {
            weChatFlag = "true";
          }
          if (bankCardsfind.get(0).getWeChatPicture() != null
              && bankCardsfind.get(0).getWeChatPicture() != ""
              && !bankCardsfind.get(0).getWeChatPicture().isEmpty()) {
            weChatFlag = "true";
          }
          if (bankCardsfind.get(0).getAlipay() != null && bankCardsfind.get(0).getAlipay() != ""
              && !bankCardsfind.get(0).getAlipay().isEmpty()) {
            alipayFlag = "true";
          }
          if (bankCardsfind.get(0).getAlipayPicture() != null
              && bankCardsfind.get(0).getAlipayPicture() != ""
              && !bankCardsfind.get(0).getAlipayPicture().isEmpty()) {
            alipayFlag = "true";
          }
        }
        JSON.put("transactionMoney", list.get(i).getTransactionMoney());
        JSON.put("transactionType", list.get(i).getTransactionType());
        JSON.put("coinCode", list.get(i).getCoinCode());
        JSON.put("transactionPrice", list.get(i).getTransactionPrice());
        JSON.put("transactionCount", list.get(i).getTransactionCount());
        JSON.put("id", list.get(i).getId());
        JSON.put("businessQuantity", list.get(i).getBusinessQuantity());
        JSON.put("otcFinishCount", appPersonInfo.getOtcFinishCount());
        JSON.put("customerName", appPersonInfo.getSurname() + "**");
        JSON.put("cardNumberFlag", cardNumberFlag);
        JSON.put("weChatFlag", weChatFlag);
        JSON.put("alipayFlag", alipayFlag);
        JSON.put("online", online);
        otclinsts.add(JSON);
      }
    }
    return new RemoteResult().setSuccess(true).setObj(otclinsts);
  }

  public FrontPage otcorderlistall(Map<String, String> params) {
    FrontPage frontPage = otcOrderTransactionService.otcorderlistall(params);
    return frontPage;
  }

  /**
   * 获卖交易订单列表
   */
  public FrontPage otcorderselllist(Map<String, String> params) {
    FrontPage frontPage = otcOrderTransactionService.otcorderselllist(params);
    return frontPage;
  }

  /**
   * 获买交易订单列表
   */
  public FrontPage otcorderbuylist(Map<String, String> params) {
    FrontPage frontPage = otcOrderTransactionService.otcorderbuylist(params);
    return frontPage;
  }

  /**
   * 获买OTC交易订单列表
   */
  public RemoteResult getOtcbyid(Long id) {
    RemoteResult remoteResult = new RemoteResult();
    QueryFilter filter = new QueryFilter(OtcOrderTransaction.class);
    OtcOrderTransaction otcOrderTransaction = otcOrderTransactionService.get(id);
    remoteResult.setSuccess(false).setObj(otcOrderTransaction);
    return remoteResult;
  }

  /**
   * OTC完成付款
   */
  public RemoteResult otcPayment(OtcOrderTransactionMange otcOrderTransactionMange) {
    RemoteResult remoteResult = new RemoteResult();
    JsonResult jsonResult = otcOrderTransactionService.otcPayment(otcOrderTransactionMange);
    if (jsonResult.getSuccess()) {
      return remoteResult.setSuccess(true).setObj(jsonResult.getObj());
    } else {
      return remoteResult.setSuccess(false).setMsg(jsonResult.getMsg());
    }
  }

  /**
   * OTC申诉
   */
  public RemoteResult confirmotcApplyArbitration(
      OtcOrderTransactionMange otcOrderTransactionMange) {
    RemoteResult remoteResult = new RemoteResult();
    JsonResult jsonResult = otcOrderTransactionService
        .confirmotcApplyArbitration(otcOrderTransactionMange);
    if (jsonResult.getSuccess()) {
      return remoteResult.setSuccess(true).setObj(jsonResult.getObj());
    } else {
      return remoteResult.setSuccess(false).setMsg(jsonResult.getMsg());
    }
  }

  /**
   * OTC确认到账
   */
  public RemoteResult finishOtcOrder(OtcOrderTransactionMange otcOrderTransactionMange) {
    RemoteResult remoteResult = new RemoteResult();
    JsonResult jsonResult = otcOrderTransactionService.finishOtcOrder(otcOrderTransactionMange);
    if (jsonResult.getSuccess()) {
      return remoteResult.setSuccess(true).setObj(jsonResult.getObj());
    } else {
      return remoteResult.setSuccess(false).setMsg(jsonResult.getMsg());
    }
  }

  /**
   * OTC撤销
   */
  public RemoteResult otcUndo(OtcOrderTransactionMange otcOrderTransactionMange) {
    RemoteResult remoteResult = new RemoteResult();
    JsonResult jsonResult = otcOrderTransactionService.otcUndo(otcOrderTransactionMange);
    if (jsonResult.getSuccess()) {
      return remoteResult.setSuccess(true).setObj(jsonResult.getObj());
    } else {
      return remoteResult.setSuccess(false).setMsg(jsonResult.getMsg());
    }
  }

  public FrontPage getOtclists(Map<String, String> params) {
    FrontPage frontPage = otcTransactionService.getOtclists(params);
    return frontPage;
  }

  public RemoteResult OtcListclose(Long id) {
    RemoteResult remoteResult = new RemoteResult();
    JsonResult jsonResult = otcTransactionService.OtcListclose(id);
    if (jsonResult.getSuccess()) {
      return remoteResult.setSuccess(true).setObj(jsonResult.getObj());
    } else {
      return remoteResult.setSuccess(false).setMsg(jsonResult.getMsg());
    }
  }

  private OtcOrderTransactionMange common(OtcOrderTransactionMange otcOrderTransactionMange,
      OtcOrderTransaction otcOrderTransaction) {
    otcOrderTransactionMange.setId(otcOrderTransaction.getId());
    otcOrderTransactionMange.setBuyCustomId(otcOrderTransaction.getBuyCustomId());
    otcOrderTransactionMange.setSellCustomId(otcOrderTransaction.getSellCustomId());
    otcOrderTransactionMange.setBuyAccountId(otcOrderTransaction.getBuyAccountId());
    otcOrderTransactionMange.setSellAaccountId(otcOrderTransaction.getSellAaccountId());
    otcOrderTransactionMange.setImg1(otcOrderTransaction.getImg1());
    otcOrderTransactionMange.setImg2(otcOrderTransaction.getImg2());
    otcOrderTransactionMange.setImg3(otcOrderTransaction.getImg3());
    otcOrderTransactionMange.setImg4(otcOrderTransaction.getImg4());
    otcOrderTransactionMange.setImg5(otcOrderTransaction.getImg5());
    otcOrderTransactionMange.setImg6(otcOrderTransaction.getImg6());
    otcOrderTransactionMange.setAppealReason(otcOrderTransaction.getAppealReason());
    otcOrderTransactionMange.setPaymentType(otcOrderTransaction.getPaymentType());
    otcOrderTransactionMange.setAppealFlag(otcOrderTransaction.getAppealFlag());
    otcOrderTransactionMange.setPaymentTime(otcOrderTransaction.getPaymentTime());
    otcOrderTransactionMange.setCoinCode(otcOrderTransaction.getCoinCode());
    otcOrderTransactionMange.setFee(otcOrderTransaction.getFee());
    otcOrderTransactionMange.setStatus(otcOrderTransaction.getStatus());
    otcOrderTransactionMange.setRandomNum(otcOrderTransaction.getRandomNum());
    otcOrderTransactionMange.setTransactionType(otcOrderTransaction.getTransactionType());
    otcOrderTransactionMange.setTransactionMoney(otcOrderTransaction.getTransactionMoney());
    otcOrderTransactionMange.setTransactionCount(otcOrderTransaction.getTransactionCount());
    otcOrderTransactionMange.setTransactionPrice(otcOrderTransaction.getTransactionPrice());
    otcOrderTransactionMange.setAppealCustomId(otcOrderTransaction.getAppealCustomId());
    otcOrderTransactionMange.setAppealCustomName(otcOrderTransaction.getAppealCustomName());
    otcOrderTransactionMange.setAppealHandle(otcOrderTransaction.getAppealHandle());

    return otcOrderTransactionMange;
  }

  public OtcOrderTransactionMange selectOtcOrderbyid(Long id) {
    OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
    RemoteResult remoteResult = new RemoteResult();
    QueryFilter filter = new QueryFilter(OtcOrderTransaction.class);
    OtcOrderTransaction otcOrderTransaction = otcOrderTransactionService.get(id);
    otcOrderTransactionMange = this.common(otcOrderTransactionMange, otcOrderTransaction);
    return otcOrderTransactionMange;
  }

  @Override
  public Boolean checkUnique(UniqueRecord unique) {
    UniqueRecord uniqueRecord = new UniqueRecord();
    uniqueRecord.setId(unique.getId());
    boolean result = uniqueRecordService.add(uniqueRecord);
    return result;
  }

  // 获取用户单个钱包地址
  @Override
  public RemoteResult getAddress(Long customerId, String coinCode) {
    // TODO Auto-generated method stub
    ExDigitalmoneyAccount digitalmoneyAccount = new ExDigitalmoneyAccount();
    QueryFilter filter = new QueryFilter(AppAccount.class);
    filter.addFilter("customerId=", customerId);
    filter.addFilter("coinCode=", coinCode);
    ExDigitalmoneyAccount _digitalmoneyAccount = exDigitalmoneyAccountService.get(filter);
    RemoteResult remoteResult = new RemoteResult();
    return remoteResult.setObj(_digitalmoneyAccount);
  }

  /**
   * @Description: 绑定邮箱
   * @Author: zongwei
   * @CreateDate: 2018/7/7 10:57
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/7/7 10:57
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  public RemoteResult setMail(Long customerId, String mail) {
    RemoteResult remoteResult = new RemoteResult();
    QueryFilter filter = new QueryFilter(AppCustomer.class);
    filter.addFilter("id_NEQ", customerId);
    filter.addFilter("username=", mail);
    List<AppCustomer> appCustomers = appCustomerService.find(filter);

    if (!appCustomers.isEmpty()) {
      return remoteResult.setSuccess(false).setMsg("邮箱已经存在！");
    }

    QueryFilter filter2 = new QueryFilter(AppCustomer.class);
    filter2.addFilter("mail=", mail);

    List<AppCustomer> appCustomers2 = appCustomerService.find(filter2);

    if (!appCustomers2.isEmpty()) {
      return remoteResult.setSuccess(false).setMsg("邮箱已经存在！");
    }

    AppCustomer appCustomer = appCustomerService.get(customerId);
    appCustomer.setMail(mail);
    appCustomer.setMailStates(1);

    appCustomerService.update(appCustomer);
    return remoteResult.setSuccess(true).setMsg("绑定成功！");
  }

  /**
   * @Description: 邮箱解绑
   * @Author: zongwei
   * @CreateDate: 2018/7/7 10:57
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/7/7 10:57
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  public RemoteResult cancelMail(Long customerId, String mail) {
    RemoteResult remoteResult = new RemoteResult();
    AppCustomer appCustomer = appCustomerService.get(customerId);
    appCustomer.setMail(null);
    appCustomer.setMailStates(0);
    appCustomerService.updateNull(appCustomer);
    return remoteResult.setSuccess(true).setMsg("解绑成功！");
  }

  /**
   * @Description: 送积分
   * @Author: zongwei
   * @CreateDate: 2018/7/11 16:33
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/7/11 16:33
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  private void givePoit(Long id, String referralCode) {
    AppCommendUser appCommendUser = appCommendUserService
        .get(new QueryFilter(AppCommendUser.class).addFilter("receCode=", referralCode));

    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String appMemberPointConfigs = redisService.get("cn:AppMemberPointConfig");
    BigDecimal recomm_point = new BigDecimal(0);
    BigDecimal regist_point = new BigDecimal(0);
    if (!StringUtils.isEmpty(appMemberPointConfigs)) {
      List<com.mz.spotchange.model.AppMemberPointConfig> parseArray = JSON
          .parseArray(appMemberPointConfigs, com.mz.spotchange.model.AppMemberPointConfig.class);
      for (com.mz.spotchange.model.AppMemberPointConfig appMemberPointConfig : parseArray) {
        if (appMemberPointConfig.getIsopen() == 1) {
          if (appMemberPointConfig.getRecomm_isopen() == 1) {
            recomm_point = appMemberPointConfig.getRecomm_point();
          }
          if (appMemberPointConfig.getRegist_isopen() == 1) {
            regist_point = appMemberPointConfig.getRegist_point();
          }
        }
      }
      // 注册者
      AppMemberPoint appMemberPoint = new AppMemberPoint();
      AppCustomer RegistappCustomer = appCustomerService.get(id);
      RegistappCustomer.setMemberPoint(RegistappCustomer.getMemberPoint().add(regist_point));
      if (regist_point.compareTo(new BigDecimal(0)) != 0) {
        appMemberPoint.setCustomerId(id);
        appMemberPoint.setMember_Point(regist_point);
      }
      appCustomerService.update(RegistappCustomer);
      // 推荐者
      if (appCommendUser != null && !StringUtils.isEmpty(appCommendUser)) {
        if (appCommendUser.getIsFrozen() == 0) {
          AppCustomer recommappCustomer = appCustomerService.get(appCommendUser.getUid());
          recommappCustomer.setMemberPoint(recommappCustomer.getMemberPoint().add(recomm_point));
          appCustomerService.update(recommappCustomer);

          if (recomm_point.compareTo(new BigDecimal(0)) != 0) {
            appMemberPoint.setRecomm_customerId(appCommendUser.getUid());
            appMemberPoint.setRecomm_Point(recomm_point);
          }
        }
        if (appMemberPoint != null && !StringUtils.isEmpty(appMemberPoint)) {
          appMemberPointService.save(appMemberPoint);
        }
      }

    }
  }

  // 修改用户返佣状态显示
  @Override
  public FrontPage rakebake(Map<String, String> params) {

    Page page = PageFactory.getPage(params);
    String username = params.get("username");
    ArrayList<AppCommendBackMoney> resultList = new ArrayList<AppCommendBackMoney>();

    // 查询方法
    List<AppCommendMoney> selectCommend = appCommendMoneyService.selectMoneyCommend(username);
    if (selectCommend != null && selectCommend.size() > 0) {
      for (int i = 0; i < selectCommend.size(); i++) {
        AppCommendMoney appCommendMoney = selectCommend.get(i);
        AppCommendBackMoney appCommendBackMoney = new AppCommendBackMoney();
        appCommendBackMoney.setCustromerName(appCommendMoney.getCustromerName());
        appCommendBackMoney.setFixPriceCoinCode(appCommendMoney.getFixPriceCoinCode());
        appCommendBackMoney.setMoneyNum(appCommendMoney.getMoneyNum());
        appCommendBackMoney.setPaidMoney(appCommendMoney.getPaidMoney());
        appCommendBackMoney.setTransactionNum(appCommendMoney.getTransactionNum());
        resultList.add(appCommendBackMoney);
      }

    }

    return new FrontPage(resultList, page.getTotal(), page.getPages(), page.getPageSize());
  }

}
