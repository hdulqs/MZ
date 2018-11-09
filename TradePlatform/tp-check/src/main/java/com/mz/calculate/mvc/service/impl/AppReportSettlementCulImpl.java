/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年7月5日 上午10:42:10
 */
package com.mz.calculate.mvc.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.account.fund.model.AppAccount;
import com.mz.account.fund.model.AppAccountSureold;
import com.mz.account.fund.model.AppTransaction;
import com.mz.calculate.mvc.service.AppReportSettlementCulService;
import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.agents.model.AngestAsMoney;
import com.mz.customer.rebat.model.AppCommendRebat;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.account.model.AppAccountDisable;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.lend.model.ExDmLend;
import com.mz.exchange.lend.model.ExDmLendIntent;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.account.fund.service.AppAccountService;
import com.mz.account.fund.service.AppAccountSureoldService;
import com.mz.account.fund.service.AppTransactionService;
import com.mz.calculate.mvc.po.AccountFundInfo;
import com.mz.calculate.mvc.po.HistoryAccountFundInfo;
import com.mz.customer.agents.dao.AngestAsMoneyDao;
import com.mz.customer.agents.dao.AppCommendRebatDao;
import com.mz.customer.agents.service.AngestAsMoneyService;
import com.mz.customer.businessman.service.OtcAccountRecordService;
import com.mz.customer.businessman.service.RepairAccountRecordService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.account.service.AppAccountDisableService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.lend.service.ExDmLendIntentService;
import com.mz.exchange.lend.service.ExDmLendService;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.trade.entrust.dao.ExEntrustDao;
import com.mz.trade.entrust.model.EradeCheck;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.redis.model.AppAccountRedis;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import com.mz.web.app.service.AppConfigService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年7月5日 上午10:42:10
 */
@Service("appReportSettlementCulService")
public class AppReportSettlementCulImpl extends
    BaseServiceImpl<AppReportSettlement, Long> implements AppReportSettlementCulService {

  @Resource(name = "appReportSettlementDao")
  @Override
  public void setDao(BaseDao<AppReportSettlement, Long> dao) {
    super.dao = dao;
  }

  @Resource
  public AppCustomerService appCustomerService;
  @Resource
  public ExEntrustService exEntrustService;
  @Resource
  public ExDigitalmoneyAccountService remoteExDigitalmoneyAccountService;
  @Resource
  public ExDmTransactionService remoteExDmTransactionService;
  @Resource
  public ExEntrustService remoteExEntrustService;

  @Resource
  public ExDmLendService remoteExDmLendService;
  @Autowired
  private RedisService redisService;
  @Resource
  private AppTransactionService appTransactionService;
  @Resource
  public AppAccountSureoldService remoteAppAccountSureoldService;
  @Resource
  public AppReportSettlementCulService appReportSettlementCulService;
  @Resource
  public AngestAsMoneyService angestAsMoneyService;
  @Resource
  public AngestAsMoneyDao angestAsMoneyDao;
  @Resource
  public ExOrderInfoService exOrderInfoService;
  @Resource
  public ExDmLendIntentService exDmLendIntentService;
  @Resource
  public AppAccountDisableService appAccountDisableService;
  @Resource
  public ExEntrustDao exEntrustDao;
  @Resource
  public AppCommendRebatDao appCommendRebatDao;

  @Resource
  private OtcAccountRecordService otcAccountRecordService;

  @Resource
  private RepairAccountRecordService repairAccountRecordService;

  @Override
  public Map<String, BigDecimal> getTransactionByExEntrust(Long customerId, String currencyType,
      String website, String beginDateString, String endDateString) {
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    BigDecimal buyTransactionMoney = new BigDecimal("0");
    BigDecimal sellTransactionMoney = new BigDecimal("0");
    BigDecimal transactionFee = new BigDecimal("0");
    BigDecimal coldEntrustMoney = new BigDecimal("0"); // 委托冻结

    Map<String, Object> mapColdEntrustMoney = new HashMap<String, Object>();
    mapColdEntrustMoney.put("customerId", customerId);
    // 买成交额,卖成交额，成交手续费
    if (null != beginDateString) {
      mapColdEntrustMoney.put("entrustTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapColdEntrustMoney.put("entrustTimel", endDateString);
    }
    EradeCheck mapColdEntrustMoneyeradeCheck = exEntrustDao
        .getcoldEntrustMoney(mapColdEntrustMoney);
    if (null != mapColdEntrustMoneyeradeCheck && null != mapColdEntrustMoneyeradeCheck
        .getColdEntrustMoney()) {
      coldEntrustMoney = mapColdEntrustMoneyeradeCheck.getColdEntrustMoney();
    }
    // 计算币的买成交量，卖成交量

    Map<String, Object> mapbuyTransactionMoney = new HashMap<String, Object>();
    mapbuyTransactionMoney.put("customerId", customerId);
    // 买成交额,卖成交额，成交手续费
    if (null != beginDateString) {
      mapbuyTransactionMoney.put("transactionTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapbuyTransactionMoney.put("transactionTimel", endDateString);
    }
    EradeCheck eradeCheckmapbuyTransactionMoney = exEntrustDao
        .getbuyTransactionMoney(mapbuyTransactionMoney);
    if (null != eradeCheckmapbuyTransactionMoney && null != eradeCheckmapbuyTransactionMoney
        .getBuyTransactionMoney()) {

      buyTransactionMoney = eradeCheckmapbuyTransactionMoney.getBuyTransactionMoney();
    }
    // 计算币的买成交量，卖成交量
    Map<String, Object> mapsellTransactionMoney = new HashMap<String, Object>();
    mapsellTransactionMoney.put("customerId", customerId);
    // 买成交额,卖成交额，成交手续费
    if (null != beginDateString) {
      mapsellTransactionMoney.put("transactionTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapsellTransactionMoney.put("transactionTimel", endDateString);
    }
    EradeCheck eradeCheckmapmapsellTransactionMoney = exEntrustDao
        .getsellTransactionMoney(mapsellTransactionMoney);
    if (null != eradeCheckmapmapsellTransactionMoney && null != eradeCheckmapmapsellTransactionMoney
        .getSellTransactionMoney()) {

      sellTransactionMoney = eradeCheckmapmapsellTransactionMoney.getSellTransactionMoney();
    }
    if (null != eradeCheckmapmapsellTransactionMoney && null != eradeCheckmapmapsellTransactionMoney
        .getTransactionFee()) {

      transactionFee = eradeCheckmapmapsellTransactionMoney.getTransactionFee();
    }
    map.put("buyTransactionMoney", buyTransactionMoney);
    map.put("sellTransactionMoney", sellTransactionMoney);
    map.put("transactionFee", transactionFee);
    map.put("coldEntrustMoney", coldEntrustMoney);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getTranCoinByExEntrust(Long customerId, String coinCode,
      String currencyType, String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    BigDecimal buyTransactioncount = new BigDecimal("0");
    BigDecimal sellTransactioncount = new BigDecimal("0");
    BigDecimal transactionFee = new BigDecimal("0");
    BigDecimal edcoldEntrustCount = new BigDecimal("0"); // 委托冻结

    Map<String, Object> mapedcoldEntrustCount = new HashMap<String, Object>();
    mapedcoldEntrustCount.put("customerId", customerId);
    mapedcoldEntrustCount.put("coinCode", coinCode);
    // 买成交额,卖成交额，成交手续费
    if (null != beginDateString) {
      mapedcoldEntrustCount.put("entrustTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapedcoldEntrustCount.put("entrustTimel", endDateString);
    }
    EradeCheck mapedcoldEntrustCounteradeCheck = exEntrustDao
        .getedcoldEntrustCount(mapedcoldEntrustCount);
    if (null != mapedcoldEntrustCounteradeCheck && null != mapedcoldEntrustCounteradeCheck
        .getEdcoldEntrustCount()) {

      edcoldEntrustCount = mapedcoldEntrustCounteradeCheck.getEdcoldEntrustCount();
    }
    // 计算币的买成交量，卖成交量

    Map<String, Object> mapbuyTransactioncount = new HashMap<String, Object>();
    mapbuyTransactioncount.put("customerId", customerId);
    mapbuyTransactioncount.put("coinCode", coinCode);
    // 买成交额,卖成交额，成交手续费
    if (null != beginDateString) {
      mapbuyTransactioncount.put("transactionTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapbuyTransactioncount.put("transactionTimel", endDateString);
    }
    EradeCheck mapbuyTransactioncountCheck = exEntrustDao
        .getbuyTransactioncount(mapbuyTransactioncount);
    if (null != mapbuyTransactioncountCheck && null != mapbuyTransactioncountCheck
        .getBuyTransactioncount()) {

      buyTransactioncount = mapbuyTransactioncountCheck.getBuyTransactioncount();
    }
    if (null != mapbuyTransactioncountCheck && null != mapbuyTransactioncountCheck
        .getTransactionFeecoin()) {

      transactionFee = mapbuyTransactioncountCheck.getTransactionFeecoin();
    }

    // 计算币的买成交量，卖成交量
    Map<String, Object> mapsellTransactioncount = new HashMap<String, Object>();
    mapsellTransactioncount.put("customerId", customerId);
    mapsellTransactioncount.put("coinCode", coinCode);
    // 买成交额,卖成交额，成交手续费
    if (null != beginDateString) {
      mapsellTransactioncount.put("transactionTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapsellTransactioncount.put("transactionTimel", endDateString);
    }
    EradeCheck mapsellTransactioncountCheck = exEntrustDao
        .getsellTransactioncount(mapsellTransactioncount);
    if (null != mapsellTransactioncountCheck && null != mapsellTransactioncountCheck
        .getSellTransactioncount()) {
      sellTransactioncount = mapsellTransactioncountCheck.getSellTransactioncount();
    }
    map.put("buyTransactioncount", buyTransactioncount);
    map.put("sellTransactioncount", sellTransactioncount);
    map.put("edcoldEntrustCount", edcoldEntrustCount);
    map.put("transactionFeecoin", transactionFee);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getTranfixPriceCoinCodeByExEntrust(Long customerId,
      String fixPriceCoinCode, String currencyType, String website, String beginDateString,
      String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    BigDecimal buyTransactionMoney = new BigDecimal("0");
    BigDecimal sellTransactionMoney = new BigDecimal("0");
    BigDecimal transactionFee = new BigDecimal("0");
    BigDecimal coldEntrustMoney = new BigDecimal("0"); // 委托冻结

    // 买成交额,卖成交额，成交手续费

    Map<String, Object> mapcoldEntrustFixPrice = new HashMap<String, Object>();
    mapcoldEntrustFixPrice.put("customerId", customerId);
    mapcoldEntrustFixPrice.put("fixPriceCoinCode", fixPriceCoinCode);
    // 买成交额,卖成交额，成交手续费
    if (null != beginDateString) {
      mapcoldEntrustFixPrice.put("entrustTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapcoldEntrustFixPrice.put("entrustTimel", endDateString);
    }
    EradeCheck mapedcoldEntrustCounteradeCheck = exEntrustDao
        .getcoldEntrustFixPrice(mapcoldEntrustFixPrice);
    if (null != mapedcoldEntrustCounteradeCheck && null != mapedcoldEntrustCounteradeCheck
        .getColdEntrustFixPrice()) {
      coldEntrustMoney = mapedcoldEntrustCounteradeCheck.getColdEntrustFixPrice();
    }

    // 计算币的买成交量，卖成交量
    Map<String, Object> mapbuyTransactionFixPrice = new HashMap<String, Object>();
    mapbuyTransactionFixPrice.put("customerId", customerId);
    mapbuyTransactionFixPrice.put("fixPriceCoinCode", fixPriceCoinCode);
    if (null != beginDateString) {
      mapbuyTransactionFixPrice.put("transactionTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapbuyTransactionFixPrice.put("transactionTimel", endDateString);
    }
    EradeCheck mapbuyTransactionFixPriceCheck = exEntrustDao
        .getbuyTransactionFixPrice(mapbuyTransactionFixPrice);
    if (null != mapbuyTransactionFixPriceCheck && null != mapbuyTransactionFixPriceCheck
        .getBuyTransactionFixPrice()) {
      buyTransactionMoney = mapbuyTransactionFixPriceCheck.getBuyTransactionFixPrice();
    }

    // 计算币的买成交量，卖成交量

    Map<String, Object> mapsellTransactionFixPrice = new HashMap<String, Object>();
    mapsellTransactionFixPrice.put("customerId", customerId);
    mapsellTransactionFixPrice.put("fixPriceCoinCode", fixPriceCoinCode);
    if (null != beginDateString) {
      mapsellTransactionFixPrice.put("transactionTimeg", beginDateString);
    }
    if (null != endDateString) {
      mapsellTransactionFixPrice.put("transactionTimel", endDateString);
    }
    EradeCheck mapsellTransactionFixPriceCheck = exEntrustDao
        .getsellTransactionFixPrice(mapsellTransactionFixPrice);

    if (null != mapsellTransactionFixPriceCheck && null != mapsellTransactionFixPriceCheck
        .getSellTransactionFixPrice()) {

      sellTransactionMoney = mapsellTransactionFixPriceCheck.getSellTransactionFixPrice();
    }
    if (null != mapsellTransactionFixPriceCheck && null != mapsellTransactionFixPriceCheck
        .getTransactionFeeFixPrice()) {

      transactionFee = mapsellTransactionFixPriceCheck.getTransactionFeeFixPrice();
    }

    map.put("buyTransactionFixPrice", buyTransactionMoney);
    map.put("sellTransactionFixPrice", sellTransactionMoney);
    map.put("transactionFeeFixPrice", transactionFee);
    map.put("coldEntrustFixPrice", coldEntrustMoney);
    return map;
  }

  public Map<String, BigDecimal> getCoinByAccountDisable(Long customerId, String coinCode,
      String currencyType, String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    QueryFilter transactionMoneyfilter1 = new QueryFilter(AppAccountDisable.class);
    transactionMoneyfilter1.addFilter("customerId=", customerId);
    transactionMoneyfilter1.addFilter("currencyType=", currencyType);
    transactionMoneyfilter1.addFilter("website=", website);
    transactionMoneyfilter1.addFilter("coinCode=", coinCode);
    transactionMoneyfilter1.addFilter("status=", 1);
    List<AppAccountDisable> appAccountDisablelist = appAccountDisableService
        .find(transactionMoneyfilter1);
    BigDecimal disabletCount = new BigDecimal("0");
    for (AppAccountDisable e : appAccountDisablelist) {
      disabletCount = disabletCount.add(e.getTransactionCount());
    }

    map.put("disabletCount", disabletCount);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getReWiByAppTransaction(Long customerId, String currencyType,
      String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();

    // 充值额,提现额，提现手续费
    // 查出所以充值提现的成功数据
    List<AppTransaction> rechargeList = appTransactionService
        .record(customerId, null, "2", beginDateString, endDateString, null, null);
    BigDecimal rechargeMoney = new BigDecimal("0");
    BigDecimal rechargeFee = new BigDecimal("0");
    BigDecimal withdrawMoney = new BigDecimal("0");
    BigDecimal withdrawFee = new BigDecimal("0");
    for (AppTransaction at : rechargeList) {
      if (at.getTransactionType() == 1 || at.getTransactionType() == 3
          || at.getTransactionType() == 5) {
        rechargeMoney = rechargeMoney.add(at.getTransactionMoney());
        rechargeFee = rechargeFee.add(at.getFee());
      } else {
        withdrawMoney = withdrawMoney.add(at.getTransactionMoney());
        withdrawFee = withdrawFee.add(at.getFee());
      }

    }

    map.put("rechargeMoney", rechargeMoney);
    map.put("withdrawMoney", withdrawMoney);
    map.put("withdrawFee", withdrawFee);
    map.put("rechargeFee", rechargeFee);
    return map;
  }

  public Map<String, BigDecimal> getTransactionByExEntrust1(Long customerId, String currencyType,
      String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();

    // 买成交额,卖成交额，成交手续费
    QueryFilter transactionMoneyfilter1 = new QueryFilter(ExEntrust.class);
    transactionMoneyfilter1.addFilter("customerId=", customerId);
    // transactionMoneyfilter1.addFilter("currencyType=",currencyType);
    // transactionMoneyfilter1.addFilter("website=",website);
    transactionMoneyfilter1.addFilter("type=", 1);
    transactionMoneyfilter1.addFilter("fixPriceType=", 0);// 真实货币
    transactionMoneyfilter1.addFilter("status<", 2);
    if (null != beginDateString) {
      transactionMoneyfilter1.addFilter("entrustTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfilter1.addFilter("entrustTime<", endDateString);
    }
    List<ExEntrust> buyTransactionMoneylist = remoteExEntrustService.find(transactionMoneyfilter1);
    BigDecimal buyTransactionMoney = new BigDecimal("0");
    BigDecimal sellTransactionMoney = new BigDecimal("0");
    BigDecimal transactionFee = new BigDecimal("0");
    BigDecimal coldEntrustMoney = new BigDecimal("0"); // 委托冻结
    for (ExEntrust e : buyTransactionMoneylist) {
      coldEntrustMoney = coldEntrustMoney.add(e.getEntrustSum().subtract(e.getTransactionSum()));
    }

    // 计算币的买成交量，卖成交量
    QueryFilter transactionMoneyfilterbuy = new QueryFilter(ExOrderInfo.class);
    transactionMoneyfilterbuy.addFilter("buyCustomId=", customerId);
    // transactionMoneyfilterbuy.addFilter("currencyType=",currencyType);
    // transactionMoneyfilterbuy.addFilter("website=",website);
    transactionMoneyfilterbuy.addFilter("fixPriceType=", 0);// 真实货币
    if (null != beginDateString) {
      transactionMoneyfilterbuy.addFilter("transactionTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfilterbuy.addFilter("transactionTime<", endDateString);
    }
    List<ExOrderInfo> Transactioncountlistbuy = exOrderInfoService.find(transactionMoneyfilterbuy);
    for (ExOrderInfo e : Transactioncountlistbuy) {
      buyTransactionMoney = buyTransactionMoney.add(e.getTransactionSum());
    }

    // 计算币的买成交量，卖成交量
    QueryFilter transactionMoneyfiltersell = new QueryFilter(ExOrderInfo.class);
    transactionMoneyfiltersell.addFilter("sellCustomId=", customerId);
    // transactionMoneyfilter.addFilter("currencyType=",currencyType);
    // transactionMoneyfilter.addFilter("website=",website);
    transactionMoneyfiltersell.addFilter("fixPriceType=", 0);// 真实货币
    if (null != beginDateString) {
      transactionMoneyfiltersell.addFilter("transactionTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfiltersell.addFilter("transactionTime<", endDateString);
    }
    List<ExOrderInfo> Transactioncountlistsell = exOrderInfoService
        .find(transactionMoneyfiltersell);
    for (ExOrderInfo a : Transactioncountlistsell) {
      sellTransactionMoney = sellTransactionMoney.add(a.getTransactionSum());
      transactionFee = transactionFee.add(a.getTransactionSellFee());
    }
    map.put("buyTransactionMoney", buyTransactionMoney);
    map.put("sellTransactionMoney", sellTransactionMoney);
    map.put("transactionFee", transactionFee);
    map.put("coldEntrustMoney", coldEntrustMoney);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getLendByExDmLend(Long customerId, String type,
      String currencyType, String website, String beginDateString, String endDateString) {
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 融资
    QueryFilter remoteQueryFilter = new QueryFilter(ExDmLend.class);
    remoteQueryFilter.addFilter("customerId=", customerId);
    if (null != beginDateString) {
      remoteQueryFilter.addFilter("lendTime>=", beginDateString);
    }
    if (null != endDateString) {
      remoteQueryFilter.addFilter("lendTime<", endDateString);
    }

    remoteQueryFilter.addFilter("lendCoin=", type);
    // remoteQueryFilter.addFilter("currencyType=",currencyType);
    // remoteQueryFilter.addFilter("website=",website);
    List<ExDmLend> listedl = remoteExDmLendService.find(remoteQueryFilter);
    BigDecimal lendMoney = new BigDecimal("0");
    BigDecimal notInterestMoney = new BigDecimal("0");
    BigDecimal lendRate = new BigDecimal("0");
    for (ExDmLend e : listedl) {
      notInterestMoney = notInterestMoney
          .add(e.getInterestCount().subtract(e.getRepayInterestCount()));
      lendMoney = lendMoney.add(e.getLendCount());
      lendRate = e.getLendRate();
    }
    if (null == endDateString && null == beginDateString) {

      map.put("notInterestMoney", notInterestMoney);
    }
    map.put("lendMoney", lendMoney);
    map.put("lendRate", lendRate);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getRepayByExDmLendIntent(Long customerId, String currencyType,
      String type, String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 已还融资
    QueryFilter edliFilter = new QueryFilter(ExDmLendIntent.class);
    if (null != beginDateString) {
      edliFilter.addFilter("factTime>=", beginDateString);
    }
    if (null != endDateString) {
      edliFilter.addFilter("factTime<", endDateString);
    }
    edliFilter.addFilter("customerId=", customerId);
    edliFilter.addFilter("lendCoin=", currencyType);
    // edliFilter.addFilter("currencyType=",currencyType);
    // edliFilter.addFilter("website=",website);
    if (null != type) {
      edliFilter.addFilter("intentType=", type);// "interest"
    }

    List<ExDmLendIntent> listedli = exDmLendIntentService.find(edliFilter);
    BigDecimal repaylendMoney = new BigDecimal("0");
    for (ExDmLendIntent e : listedli) {
      repaylendMoney = repaylendMoney.add(e.getRepayCount());
    }

    map.put("repaylendMoney", repaylendMoney);
    return map;
  }

  public Map<String, BigDecimal> getTranCoinByExEntrust1(Long customerId, String coinCode,
      String currencyType, String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    QueryFilter transactionMoneyfilter1 = new QueryFilter(ExEntrust.class);
    transactionMoneyfilter1.addFilter("customerId=", customerId);
    // transactionMoneyfilter1.addFilter("currencyType=",currencyType);
    // transactionMoneyfilter1.addFilter("website=",website);
    transactionMoneyfilter1.addFilter("coinCode=", coinCode);
    transactionMoneyfilter1.addFilter("type=", 2);
    transactionMoneyfilter1.addFilter("status<", 2);
    if (null != beginDateString) {
      transactionMoneyfilter1.addFilter("entrustTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfilter1.addFilter("entrustTime<", endDateString);
    }
    List<ExEntrust> buyTransactionMoneylist = remoteExEntrustService.find(transactionMoneyfilter1);
    BigDecimal edcoldEntrustCount = new BigDecimal("0"); // 委托冻结
    for (ExEntrust e : buyTransactionMoneylist) {
      edcoldEntrustCount = edcoldEntrustCount.add(e.getSurplusEntrustCount());
    }
    // 计算币的买成交量，卖成交量
    QueryFilter transactionMoneyfilterbuy = new QueryFilter(ExOrderInfo.class);
    transactionMoneyfilterbuy.addFilter("buyCustomId=", customerId);
    // transactionMoneyfilter.addFilter("currencyType=",currencyType);
    // transactionMoneyfilter.addFilter("website=",website);
    if (null != beginDateString) {
      transactionMoneyfilterbuy.addFilter("transactionTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfilterbuy.addFilter("transactionTime<", endDateString);
    }
    transactionMoneyfilterbuy.addFilter("coinCode=", coinCode);
    List<ExOrderInfo> Transactioncountlistbuy = exOrderInfoService.find(transactionMoneyfilterbuy);
    BigDecimal buyTransactioncount = new BigDecimal("0");
    BigDecimal sellTransactioncount = new BigDecimal("0");
    BigDecimal transactionFee = new BigDecimal("0");
    for (ExOrderInfo e : Transactioncountlistbuy) {
      buyTransactioncount = buyTransactioncount.add(e.getTransactionCount());
      transactionFee = transactionFee.add(e.getTransactionBuyFee());
    }

    // 计算币的买成交量，卖成交量
    QueryFilter transactionMoneyfiltersell = new QueryFilter(ExOrderInfo.class);
    transactionMoneyfiltersell.addFilter("sellCustomId=", customerId);
    // transactionMoneyfilter.addFilter("currencyType=",currencyType);
    // transactionMoneyfilter.addFilter("website=",website);
    if (null != beginDateString) {
      transactionMoneyfiltersell.addFilter("transactionTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfiltersell.addFilter("transactionTime<", endDateString);
    }
    transactionMoneyfiltersell.addFilter("coinCode=", coinCode);
    List<ExOrderInfo> Transactioncountlistsell = exOrderInfoService
        .find(transactionMoneyfiltersell);
    for (ExOrderInfo e : Transactioncountlistsell) {
      sellTransactioncount = sellTransactioncount.add(e.getTransactionCount());
    }
    map.put("buyTransactioncount", buyTransactioncount);
    map.put("sellTransactioncount", sellTransactioncount);
    map.put("edcoldEntrustCount", edcoldEntrustCount);
    map.put("transactionFeecoin", transactionFee);
    return map;
  }

  public Map<String, BigDecimal> getTranfixPriceCoinCodeByExEntrust1(Long customerId,
      String fixPriceCoinCode, String currencyType, String website, String beginDateString,
      String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();

    // 买成交额,卖成交额，成交手续费
    QueryFilter transactionMoneyfilter1 = new QueryFilter(ExEntrust.class);
    transactionMoneyfilter1.addFilter("customerId=", customerId);
    // transactionMoneyfilter1.addFilter("currencyType=",currencyType);
    // transactionMoneyfilter1.addFilter("website=",website);
    transactionMoneyfilter1.addFilter("type=", 1);
    transactionMoneyfilter1.addFilter("fixPriceType=", 1);// 虚拟货币
    transactionMoneyfilter1.addFilter("fixPriceCoinCode=", fixPriceCoinCode);
    transactionMoneyfilter1.addFilter("status<", 2);
    if (null != beginDateString) {
      transactionMoneyfilter1.addFilter("entrustTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfilter1.addFilter("entrustTime<", endDateString);
    }
    List<ExEntrust> buyTransactionMoneylist = remoteExEntrustService.find(transactionMoneyfilter1);
    BigDecimal buyTransactionMoney = new BigDecimal("0");
    BigDecimal sellTransactionMoney = new BigDecimal("0");
    BigDecimal transactionFee = new BigDecimal("0");
    BigDecimal coldEntrustMoney = new BigDecimal("0"); // 委托冻结
    for (ExEntrust e : buyTransactionMoneylist) {
      coldEntrustMoney = coldEntrustMoney.add(e.getEntrustSum().subtract(e.getTransactionSum()));
    }

    // 计算币的买成交量，卖成交量
    QueryFilter transactionMoneyfilterbuy = new QueryFilter(ExOrderInfo.class);
    transactionMoneyfilterbuy.addFilter("buyCustomId=", customerId);
    // transactionMoneyfilter.addFilter("currencyType=",currencyType);
    transactionMoneyfilterbuy.addFilter("fixPriceType=", 1);// 虚拟货币
    transactionMoneyfilterbuy.addFilter("fixPriceCoinCode=", fixPriceCoinCode);
    // transactionMoneyfilter.addFilter("website=",website);
    if (null != beginDateString) {
      transactionMoneyfilterbuy.addFilter("transactionTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfilterbuy.addFilter("transactionTime<", endDateString);
    }
    List<ExOrderInfo> Transactioncountlistbuy = exOrderInfoService.find(transactionMoneyfilterbuy);
    for (ExOrderInfo e : Transactioncountlistbuy) {
      buyTransactionMoney = buyTransactionMoney.add(e.getTransactionSum());

    }

    // 计算币的买成交量，卖成交量
    QueryFilter transactionMoneyfiltersell = new QueryFilter(ExOrderInfo.class);
    transactionMoneyfiltersell.addFilter("sellCustomId=", customerId);
    // transactionMoneyfilter.addFilter("currencyType=",currencyType);
    transactionMoneyfiltersell.addFilter("fixPriceType=", 1);// 虚拟货币
    transactionMoneyfiltersell.addFilter("fixPriceCoinCode=", fixPriceCoinCode);
    // transactionMoneyfilter.addFilter("website=",website);
    if (null != beginDateString) {
      transactionMoneyfiltersell.addFilter("transactionTime>=", beginDateString);
    }
    if (null != endDateString) {
      transactionMoneyfiltersell.addFilter("transactionTime<", endDateString);
    }
    List<ExOrderInfo> Transactioncountlist = exOrderInfoService.find(transactionMoneyfiltersell);
    for (ExOrderInfo e : Transactioncountlist) {
      sellTransactionMoney = sellTransactionMoney.add(e.getTransactionSum());
      transactionFee = transactionFee.add(e.getTransactionSellFee());

    }
    map.put("buyTransactionFixPrice", buyTransactionMoney);
    map.put("sellTransactionFixPrice", sellTransactionMoney);
    map.put("transactionFeeFixPrice", transactionFee);
    map.put("coldEntrustFixPrice", coldEntrustMoney);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getInOutByExDmTransaction(Long customerId, String coinCode,
      String currencyType, String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 币的转入转出量
    QueryFilter edtfilter = new QueryFilter(ExDmTransaction.class);
    edtfilter.addFilter("customerId=", customerId);
    edtfilter.addFilter("status=", 2);
    edtfilter.addFilter("coinCode=", coinCode);
    if (null != beginDateString) {
      edtfilter.addFilter("modified>=", beginDateString);
    }
    if (null != endDateString) {
      edtfilter.addFilter("modified<", endDateString);
    }
    // edtfilter.addFilter("currencyType=",currencyType);
    // edtfilter.addFilter("website=",website);
    List<ExDmTransaction> edtlist = remoteExDmTransactionService.find(edtfilter);
    BigDecimal inCoinCount = new BigDecimal("0");
    BigDecimal outCoinCount = new BigDecimal("0");
    BigDecimal inCoinFee = new BigDecimal("0");
    BigDecimal outCoinFee = new BigDecimal("0");
    for (ExDmTransaction edt : edtlist) {
      if (edt.getTransactionType() == 1) {
        inCoinCount = inCoinCount.add(edt.getTransactionMoney());
        inCoinFee = inCoinFee.add(null == edt.getFee() ? new BigDecimal("0") : edt.getFee());
      } else {
        outCoinCount = outCoinCount.add(edt.getTransactionMoney());
        outCoinFee = outCoinFee.add(null == edt.getFee() ? new BigDecimal("0") : edt.getFee());
      }
    }
    map.put("inCoinFee", inCoinFee);
    map.put("outCoinFee", outCoinFee);
    map.put("inCoinCount", inCoinCount);
    map.put("outCoinCount", outCoinCount);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getLendcoinByExDmLend(Long customerId, String coinCode,
      String currencyType, String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 融币
    QueryFilter listedlFilter = new QueryFilter(ExDmLend.class);
    listedlFilter.addFilter("customerId=", customerId);
    if (null != beginDateString) {
      listedlFilter.addFilter("lendTime>=", beginDateString);
    }
    if (null != endDateString) {
      listedlFilter.addFilter("lendTime<", endDateString);
    }
    listedlFilter.addFilter("lendCoin=", currencyType);
    // listedlFilter.addFilter("currencyType=",currencyType);
    // listedlFilter.addFilter("website=",website);
    listedlFilter.addFilter("intentType=", "interest");
    List<ExDmLend> listedlcoin = remoteExDmLendService.find(listedlFilter);
    BigDecimal lendCoin = new BigDecimal("0");
    for (ExDmLend e : listedlcoin) {
      lendCoin = lendCoin.add(e.getLendCount());
    }
    map.put("inCoinFee", lendCoin);
    return map;
  }

  @Override
  public Map<String, BigDecimal> getRepaycoinExDmLendIntent(Long customerId, String coinCode,
      String currencyType, String website, String beginDateString, String endDateString) {

    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 已还融币
    QueryFilter edlicoinFilter = new QueryFilter(ExDmLendIntent.class);
    edlicoinFilter.addFilter("customerId=", customerId);
    if (null != beginDateString) {
      edlicoinFilter.addFilter("factTime>=", beginDateString);
    }
    if (null != endDateString) {
      edlicoinFilter.addFilter("factTime<", endDateString);
    }
    edlicoinFilter.addFilter("lendCoin=", coinCode);
    // edlicoinFilter.addFilter("currencyType=",currencyType);
    // edlicoinFilter.addFilter("website=",website);
    List<ExDmLendIntent> listedlicoin = exDmLendIntentService.find(edlicoinFilter);
    BigDecimal repaylendcoin = new BigDecimal("0");
    for (ExDmLendIntent e : listedlicoin) {
      repaylendcoin = repaylendcoin.add(e.getRepayCount());
    }
    map.put("repaylendcoin", repaylendcoin);
    return map;
  }


  @Override
  public Map<String, BigDecimal> getWithdrawcoldcount(Long customerId, String coinCode,
      String currencyType, String website, String beginDateString, String endDateString) {
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 提币2审核中1，4所有记录
    QueryFilter withdrawedtfilter = new QueryFilter(ExDmTransaction.class);
    withdrawedtfilter.addFilter("customerId=", customerId);
    withdrawedtfilter.addFilter("status_in", "1,4");
    withdrawedtfilter.addFilter("transactionType_in", "2");
    withdrawedtfilter.addFilter("coinCode=", coinCode);
    // withdrawedtfilter.addFilter("currencyType=", currencyType);
    if (null != beginDateString) {
      withdrawedtfilter.addFilter("modified>=", beginDateString);
    }

    // withdrawedtfilter.addFilter("website=", website);
    List<ExDmTransaction> widthedtlist = remoteExDmTransactionService.find(withdrawedtfilter);
    BigDecimal withdrawcoldcount = new BigDecimal("0");
    for (ExDmTransaction at : widthedtlist) {
      withdrawcoldcount = withdrawcoldcount.add(at.getTransactionMoney());
    }
    map.put("withdrawcoldcount", withdrawcoldcount);
    return map;
  }

  public Map<String, BigDecimal> getAngestAsMoney(Long customerId, String fixPriceCoinCode,
      String userName, String website, String beginDateString, String endDateString) {
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    // 佣金派发记录
    QueryFilter angestAsMoneyfilter = new QueryFilter(AngestAsMoney.class);
    angestAsMoneyfilter.addFilter("customerId=", customerId);
    angestAsMoneyfilter.addFilter("fixPriceCoinCode=", fixPriceCoinCode);
    if (null != beginDateString) {
      angestAsMoneyfilter.addFilter("modified>=", beginDateString);
    }
    List<AngestAsMoney> angestAsMoneylist = angestAsMoneyService.find(angestAsMoneyfilter);
    /*
     * Map<String, Object> map1 = new HashMap<String, Object>();
     * map1.put("agentName", userName); map1.put("fixPriceCoinCode",
     * fixPriceCoinCode); List<AngestAsMoney>
     * angestAsMoneylist=angestAsMoneyDao.getAngestAsMoney(map1);
     */
    BigDecimal drawalMoney = new BigDecimal("0");
    for (AngestAsMoney at : angestAsMoneylist) {
      drawalMoney = drawalMoney.add(at.getDrawalMoney());
    }

    // 佣金派发记录3.1

    Map<String, Object> mapappCommendRebat = new HashMap<String, Object>();
    mapappCommendRebat.put("customerId", customerId);
    mapappCommendRebat.put("coinCode", fixPriceCoinCode);
    if (null != beginDateString) {
      mapappCommendRebat.put("modified", beginDateString);
    }
    List<AppCommendRebat> appCommendRebatlist = appCommendRebatDao
        .culApoCmmendRebat(mapappCommendRebat);
    if (null != appCommendRebatlist && appCommendRebatlist.size() > 0) {
      if (null != appCommendRebatlist.get(0) && null != appCommendRebatlist.get(0)
          .getRebatMoney()) {
        drawalMoney = drawalMoney.add(appCommendRebatlist.get(0).getRebatMoney());
      }

    }
    map.put("drawalMoney", drawalMoney);
    return map;
  }

  public String productListsb(String website) {

    String productListStr = redisService.get(website + ":productList");
    List<String> productList = JSON.parseArray(productListStr, String.class);
    StringBuffer productListsb = new StringBuffer();
    if (null != productList && productList.size() > 0) {
      int a = 0;
      while (a < productList.size()) {
        productListsb.append(productList.get(a));
        if (a < productList.size() - 1) {
          productListsb.append(",");
        }
        a++;
      }
    }
    return productListsb.toString();
  }

  public String getRMBCode() {
    AppConfigService remoteAppConfigService = (AppConfigService) ContextUtil
        .getBean("appConfigService");
    String language_code = remoteAppConfigService.getBykey("language_code");
    return language_code;
  }

  @Override
  public Map<String, Object> culAccountByCustomer(Long customerId, String currencyType,
      String website, Boolean isSave, Boolean iserrorright) {
    long start = System.currentTimeMillis();
    AppAccountService appAccountService = (AppAccountService) ContextUtil
        .getBean("appAccountService");
    int rmbsacle = AccountFundInfo.sacle;
    int sacle = AccountFundInfo.sacle;
    ;
    if (null == isSave) {
      isSave = false;
    }
    if (null == iserrorright) {
      iserrorright = false;
    }
    AppCustomer appCustomer = appCustomerService.get(customerId);
    String productListsb = productListsb(website);
    Map<String, Object> map = new HashMap<String, Object>();
    List<AccountFundInfo> newlist = new ArrayList<AccountFundInfo>();
    List<AccountFundInfo> oldlist = new ArrayList<AccountFundInfo>();

    if (null == appCustomer) {
      return null;
    }
    AppAccount appAccount = appAccountService.getByCustomerIdAndType(customerId, null, null);
    if (null == appAccount) {
      return null;
    }
    AccountFundInfo afundrmb = new AccountFundInfo();
    afundrmb.setCoinCode(currencyType);
    afundrmb.setUserName(appCustomer.getUserName());
    afundrmb.setHotMoney(appAccount.getHotMoney());
    afundrmb.setColdMoney(appAccount.getColdMoney());
    afundrmb.setLendMoney(appAccount.getLendMoney());

    afundrmb.setWebsite(website);
    afundrmb.setCurrencyType(currencyType);
    oldlist.add(afundrmb);

    QueryFilter edafilter = new QueryFilter(ExDigitalmoneyAccount.class);
    edafilter.addFilter("customerId=", customerId);
    // edafilter.addFilter("currencyType=", currencyType);
    // edafilter.addFilter("website=", website);
    // edafilter.addFilter("coinCode_in", productListsb);
    List<ExDigitalmoneyAccount> listeda = remoteExDigitalmoneyAccountService.find(edafilter);

    for (ExDigitalmoneyAccount eda : listeda) {
      AccountFundInfo afundcoin = new AccountFundInfo();
      afundcoin.setCoinCode(eda.getCoinCode());
      afundcoin.setUserName(appCustomer.getUserName());
      afundcoin.setHotMoney(eda.getHotMoney());
      afundcoin.setColdMoney(eda.getColdMoney());
      afundcoin.setLendMoney(eda.getLendMoney());

      afundcoin.setWebsite(website);
      afundcoin.setCurrencyType(currencyType);
      oldlist.add(afundcoin);
    }
    String beginDateString = null;
    List<AppAccountSureold> sureoldlist = new ArrayList<AppAccountSureold>();
    // AppAccountSureold aasaccount =
    // remoteAppAccountSureoldService.getBycustomerIdAndcoinCode(customerId,
    // appCustomer.getUserName(), currencyType, currencyType, website);
    BigDecimal sureoldHotMoney = new BigDecimal("0");
    BigDecimal sureoldColdMoney = new BigDecimal("0");
    BigDecimal sureoldLendMony = new BigDecimal("0");
    /*
     * if (null != aasaccount) { beginDateString =
     * DateUtil.dateToString(aasaccount.getSureTime(),
     * StringConstant.DATE_FORMAT_FULL); sureoldHotMoney =
     * aasaccount.getHotMoney(); sureoldColdMoney =
     * aasaccount.getColdMoney(); sureoldLendMony =
     * aasaccount.getLendMoney(); sureoldlist.add(aasaccount); } else {
     */
    AppAccountSureold appAccountSureoldinit = new AppAccountSureold(null, currencyType,
        new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"));
    sureoldlist.add(appAccountSureoldinit);

    // }

    // 查出所以充值提现的成功数据,充值额,提现额，提现手续费
    Map<String, BigDecimal> getReWiByAppTransaction = getReWiByAppTransaction(customerId,
        currencyType, website, beginDateString, null);
    BigDecimal rechargeMoney = getReWiByAppTransaction.get("rechargeMoney");
    BigDecimal withdrawMoney = getReWiByAppTransaction.get("withdrawMoney");
    BigDecimal withdrawFee = getReWiByAppTransaction.get("withdrawFee");
    BigDecimal rechargeFee = getReWiByAppTransaction.get("rechargeFee");
    // 买成交额,卖成交额，成交手续费
    Map<String, BigDecimal> getTransactionByExEntrust = getTransactionByExEntrust(customerId,
        currencyType, website, beginDateString, null);
    BigDecimal buyTransactionMoney = getTransactionByExEntrust.get("buyTransactionMoney");
    BigDecimal sellTransactionMoney = getTransactionByExEntrust.get("sellTransactionMoney");
    BigDecimal transactionFee = getTransactionByExEntrust.get("transactionFee");
    BigDecimal coldEntrustMoney = getTransactionByExEntrust.get("coldEntrustMoney");

    // 佣金收入
    Map<String, BigDecimal> getAngestAsMoney = getAngestAsMoney(customerId, getRMBCode(),
        appCustomer.getUserName(), null, null, null);
    BigDecimal drawalMoney = getAngestAsMoney.get("drawalMoney");

    Map<String, BigDecimal> getRepayByExDmLendIntent = appReportSettlementCulService
        .getRepayByExDmLendIntent(customerId, currencyType, "interest", website, beginDateString,
            null);
    BigDecimal repaylendMoney = getRepayByExDmLendIntent.get("repaylendMoney");
    BigDecimal LendMoney = appAccount.getLendMoney();

    BigDecimal coldMoney = coldEntrustMoney;
    BigDecimal hotMoney = sureoldHotMoney.subtract(sureoldLendMony).add(rechargeMoney)
        .subtract(withdrawMoney).subtract(rechargeFee).add(sellTransactionMoney)
        .subtract(buyTransactionMoney).subtract(transactionFee).subtract(coldEntrustMoney)
        .add(sureoldColdMoney).add(LendMoney).subtract(repaylendMoney).add(drawalMoney);

    // 提现2,4审核1,4中的记录
    List<AppTransaction> withdrawListcold = appTransactionService
        .record(customerId, "2,4", "1,4", null, null, null, null);
    BigDecimal withdrawcoldMoney = new BigDecimal("0");
    for (AppTransaction at : withdrawListcold) {
      withdrawcoldMoney = withdrawcoldMoney.add(at.getTransactionMoney());
    }
    coldMoney = coldMoney.add(withdrawcoldMoney);// 总冻结=委托冻结+提现冻结

    hotMoney = hotMoney.subtract(withdrawcoldMoney);
    AccountFundInfo newafundrmb = new AccountFundInfo();
    newafundrmb.setCoinCode(currencyType);
    newafundrmb.setUserName(appCustomer.getUserName());
    newafundrmb.setHotMoney(hotMoney);
    newafundrmb.setColdMoney(coldMoney);
    newafundrmb.setLendMoney(LendMoney);
    newafundrmb.setWebsite(website);
    newafundrmb.setCurrencyType(currencyType);
    newlist.add(newafundrmb);

    List<HistoryAccountFundInfo> hislist = new ArrayList<HistoryAccountFundInfo>();
    HistoryAccountFundInfo cnyHistoryAccountFundInfo = new HistoryAccountFundInfo();

    // 充值提现
    cnyHistoryAccountFundInfo
        .setRechargeMoney(rechargeMoney.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo
        .setRechargeMoneyFee(rechargeFee.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo
        .setWithdrawMoney(withdrawMoney.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo
        .setWithdrawcoldMoney(withdrawcoldMoney.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo
        .setWithdrawFee(withdrawFee.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));

    // 作为定价币:
    cnyHistoryAccountFundInfo.setSellTransactionFixPrice(
        sellTransactionMoney.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo.setBuyTransactionFixPrice(
        buyTransactionMoney.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo
        .setTransactionFeeFixPrice(transactionFee.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo
        .setColdEntrustFixPrice(coldEntrustMoney.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    // 佣金
    cnyHistoryAccountFundInfo
        .setDrawalMoney(drawalMoney.setScale(rmbsacle, BigDecimal.ROUND_HALF_EVEN));
    cnyHistoryAccountFundInfo.setLendMoney(LendMoney);
    cnyHistoryAccountFundInfo.setRepaylendMoney(repaylendMoney);
    cnyHistoryAccountFundInfo.setCurrencyType(currencyType);
    cnyHistoryAccountFundInfo.setCoinCode(currencyType);
    cnyHistoryAccountFundInfo.setUserName(appCustomer.getUserName());
    cnyHistoryAccountFundInfo.setWebsite(website);
    hislist.add(cnyHistoryAccountFundInfo);

    // 计算币

    for (ExDigitalmoneyAccount eda : listeda) {

      // AppAccountSureold aasaccountcoin =
      // remoteAppAccountSureoldService.getBycustomerIdAndcoinCode(customerId,
      // appCustomer.getUserName(), eda.getCoinCode(), currencyType,
      // website);

      String coinbeginDateString = null;
      BigDecimal coinsureoldHotCount = new BigDecimal("0");
      BigDecimal coinsureoldColdCount = new BigDecimal("0");
      BigDecimal coinsureoldLendCount = new BigDecimal("0");
      /*
       * if (null != aasaccountcoin) { coinbeginDateString =
       * DateUtil.dateToString(aasaccountcoin.getSureTime(),
       * StringConstant.DATE_FORMAT_FULL); coinsureoldHotCount =
       * aasaccountcoin.getHotMoney(); coinsureoldColdCount =
       * aasaccountcoin.getColdMoney(); coinsureoldLendCount =
       * aasaccountcoin.getLendMoney(); sureoldlist.add(aasaccountcoin); }
       * else {
       */
      AppAccountSureold appAccountSureoldinitcoin = new AppAccountSureold(null, eda.getCoinCode(),
          new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"));
      sureoldlist.add(appAccountSureoldinitcoin);

      // }
      // 作为交易币：计算币的买成交量，卖成交量
      Map<String, BigDecimal> getTranCoinByExEntrust = getTranCoinByExEntrust(customerId,
          eda.getCoinCode(), currencyType, website, coinbeginDateString, null);
      BigDecimal buyTransactioncount = getTranCoinByExEntrust.get("buyTransactioncount");
      BigDecimal sellTransactioncount = getTranCoinByExEntrust.get("sellTransactioncount");
      BigDecimal edcoldEntrustCount = getTranCoinByExEntrust
          .get("edcoldEntrustCount");// 委托冻结币?????????
      BigDecimal transactionFeecoin = getTranCoinByExEntrust.get("transactionFeecoin");

      // 作为定价币：买成交额,卖成交额，成交手续费
      Map<String, BigDecimal> getTranfixPriceCoinCodeByExEntrust = getTranfixPriceCoinCodeByExEntrust(
          customerId, eda.getCoinCode(), currencyType, website, beginDateString, null);
      BigDecimal buyTransactionFixPrice = getTranfixPriceCoinCodeByExEntrust
          .get("buyTransactionFixPrice");
      BigDecimal sellTransactionFixPrice = getTranfixPriceCoinCodeByExEntrust
          .get("sellTransactionFixPrice");
      BigDecimal transactionFeeFixPrice = getTranfixPriceCoinCodeByExEntrust
          .get("transactionFeeFixPrice");
      BigDecimal coldEntrustFixPrice = getTranfixPriceCoinCodeByExEntrust
          .get("coldEntrustFixPrice");

      // 佣金收入
      Map<String, BigDecimal> getAngestAsMoneycoin = getAngestAsMoney(customerId, eda.getCoinCode(),
          appCustomer.getUserName(), null, null, null);
      BigDecimal coindrawalMoney = getAngestAsMoneycoin.get("drawalMoney");

      // 币的转入转出量
      Map<String, BigDecimal> getInOutByExDmTransaction = getInOutByExDmTransaction(customerId,
          eda.getCoinCode(), currencyType, website, coinbeginDateString, null);
      BigDecimal inCoinCount = getInOutByExDmTransaction.get("inCoinCount");
      BigDecimal outCoinCount = getInOutByExDmTransaction.get("outCoinCount");
      BigDecimal inCoinFee = getInOutByExDmTransaction.get("inCoinFee");
      BigDecimal outCoinFee = getInOutByExDmTransaction.get("outCoinFee");
      // 币禁用
      Map<String, BigDecimal> getCoinByAccountDisable = getCoinByAccountDisable(customerId,
          eda.getCoinCode(), currencyType, website, coinbeginDateString, null);
      BigDecimal disabletCount = getCoinByAccountDisable.get("disabletCount");

      ExDigitalmoneyAccount edappAccount = remoteExDigitalmoneyAccountService
          .getByCustomerIdAndType(customerId, eda.getCoinCode(), null, null);
      BigDecimal edLendMoney = edappAccount.getLendMoney();
      Map<String, BigDecimal> getRepaycoinExDmLendIntent = appReportSettlementCulService
          .getRepaycoinExDmLendIntent(customerId, eda.getCoinCode(), null, null,
              coinbeginDateString, null);
      BigDecimal repaylendcoin = getRepaycoinExDmLendIntent.get("repaylendcoin");

      BigDecimal edcoldCount = edcoldEntrustCount.add(coldEntrustFixPrice)
          .add(disabletCount);// 总冻结金额

      BigDecimal edhotMoney = coinsureoldHotCount.subtract(coinsureoldLendCount).add(inCoinCount)
          .subtract(inCoinFee).subtract(outCoinCount).add(buyTransactioncount)
          .subtract(sellTransactioncount).subtract(edcoldEntrustCount).add(coinsureoldColdCount)
          .add(edLendMoney).subtract(repaylendcoin).add(sellTransactionFixPrice)
          .subtract(buyTransactionFixPrice).subtract(transactionFeeFixPrice)
          .subtract(transactionFeecoin).subtract(coldEntrustFixPrice).add(coindrawalMoney);

      // 提币2审核中1，4所有记录
      Map<String, BigDecimal> getWithdrawcoldcount = getWithdrawcoldcount(customerId,
          eda.getCoinCode(), currencyType, website, null, null);
      BigDecimal withdrawcoldcount = getWithdrawcoldcount.get("withdrawcoldcount");
      if (withdrawcoldcount.compareTo(edhotMoney.add(edcoldEntrustCount).subtract(edLendMoney))
          > 0) {
        System.out.println(
            customerId + "币提现大于资产需要驳回:(withdrawcoldcount:" + withdrawcoldcount + "," + edhotMoney
                .add(edcoldEntrustCount).subtract(edLendMoney));
      }

      edcoldCount = edcoldCount.add(withdrawcoldcount);
      edhotMoney = edhotMoney.subtract(withdrawcoldcount);
      //add by zongwwei 20180613 加OTC币操作  begin
      Map<String, BigDecimal> getotccount = otcAccountRecordService
          .getOtcAccountRecordcount(eda.getId(), null);
      BigDecimal otccoldcount = getotccount.get("otcoldcount");
      BigDecimal otchotcount = getotccount.get("otchotcount");

      edcoldCount = edcoldCount.add(otccoldcount);
      edhotMoney = edhotMoney.add(otchotcount);
      //修复记录
      Map<String, BigDecimal> getRepairAccount = repairAccountRecordService
          .getRepairAccountRecordcount(eda.getId(), null);
      BigDecimal Repaircoldcount = getRepairAccount.get("Repairoldcount");
      BigDecimal Repairhotcount = getRepairAccount.get("Repairhotcount");

      edcoldCount = edcoldCount.add(Repaircoldcount);
      edhotMoney = edhotMoney.add(Repairhotcount);

      //add by zongwwei 20180613 加OTC币操作  end
      AccountFundInfo newafundcoin = new AccountFundInfo();
      newafundcoin.setCoinCode(eda.getCoinCode());
      newafundcoin.setUserName(appCustomer.getUserName());
      newafundcoin.setHotMoney(edhotMoney);
      newafundcoin.setColdMoney(edcoldCount);
      newafundcoin.setLendMoney(edLendMoney);

      newafundcoin.setWebsite(website);
      newafundcoin.setCurrencyType(currencyType);
      newlist.add(newafundcoin);

      HistoryAccountFundInfo coinHistoryAccountFundInfo = new HistoryAccountFundInfo();
      // 充值提现
      coinHistoryAccountFundInfo
          .setRechargeMoney(inCoinCount.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setRechargeMoneyFee(inCoinFee.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setWithdrawMoney(outCoinCount.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setWithdrawcoldMoney(withdrawcoldcount.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setRechargeMoneyFee(inCoinFee.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setWithdrawFee(outCoinFee.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      // 作为交易币
      coinHistoryAccountFundInfo.setSellTransactionMoney(
          sellTransactioncount.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setBuyTransactionMoney(buyTransactioncount.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setColdEntrustMoney(edcoldEntrustCount.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setTransactionFee(transactionFeecoin.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));

      // 作为定价币
      coinHistoryAccountFundInfo.setSellTransactionFixPrice(
          sellTransactionFixPrice.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo.setBuyTransactionFixPrice(
          buyTransactionFixPrice.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo.setTransactionFeeFixPrice(
          transactionFeeFixPrice.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo
          .setColdEntrustFixPrice(coldEntrustFixPrice.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      // 佣金
      coinHistoryAccountFundInfo
          .setDrawalMoney(coindrawalMoney.setScale(sacle, BigDecimal.ROUND_HALF_DOWN));
      coinHistoryAccountFundInfo.setLendMoney(edLendMoney);
      coinHistoryAccountFundInfo.setRepaylendMoney(repaylendcoin);
      coinHistoryAccountFundInfo.setDisableMoney(disabletCount);
      coinHistoryAccountFundInfo.setCurrencyType(currencyType);
      coinHistoryAccountFundInfo.setCoinCode(eda.getCoinCode());
      coinHistoryAccountFundInfo.setUserName(appCustomer.getUserName());
      coinHistoryAccountFundInfo.setWebsite(website);
      hislist.add(coinHistoryAccountFundInfo);
    }
    if (iserrorright) { // true全部查出

      map.put("oldAccountFundInfolist", oldlist);
      map.put("newAccountFundInfolist", newlist);
      map.put("hisorylist", hislist);
      map.put("sureoldlist", sureoldlist);
      long end = System.currentTimeMillis();
      System.out.println("单个余额核算时间==" + (end - start));
      return map;
    }
    int i = 0;
    Boolean flag = true;
    while (i < newlist.size()) {
      if (!oldlist.get(i).toString().equals(newlist.get(i).toString())) {
        flag = false;
        break;
      }
      /*
       * if (oldlist.get(i).toString().contains("-")) { flag = false;
       * break; }
       */
      i++;
    }
    if (!flag) {
      map.put("oldAccountFundInfolist", oldlist);
      map.put("newAccountFundInfolist", newlist);
      map.put("hisorylist", hislist);
      map.put("sureoldlist", sureoldlist);

      if (isSave) {
        // 保存资金账户
        appAccount.setHotMoney(hotMoney);
        appAccount.setColdMoney(coldMoney);
        appAccount.setLendMoney(LendMoney);
        appAccountService.updateAccount(appAccount);

        AppAccountRedis appAccountRedis = getAppAccountByRedis(appAccount.getId().toString());
        if (null != appAccountRedis) {
          appAccountRedis.setHotMoney(hotMoney);
          appAccountRedis.setColdMoney(coldMoney);
          this.setAppAccounttoRedis(appAccountRedis);
        }

        // 保存币账户
        for (ExDigitalmoneyAccount eda : listeda) {
          for (AccountFundInfo newafundcoin : newlist) {
            if (eda.getCoinCode().equals(newafundcoin.getCoinCode())) {
              eda.setHotMoney(newafundcoin.getHotMoney());
              eda.setColdMoney(newafundcoin.getColdMoney());
              eda.setLendMoney(newafundcoin.getLendMoney());
              remoteExDigitalmoneyAccountService.updateAccount(eda);

              ExDigitalmoneyAccountRedis exDigitalmoneyAccountRedis = getExDigitalmoneyAccountByRedis(
                  eda.getId().toString());
              if (null != exDigitalmoneyAccountRedis) {
                exDigitalmoneyAccountRedis.setHotMoney(newafundcoin.getHotMoney());
                exDigitalmoneyAccountRedis.setColdMoney(newafundcoin.getColdMoney());
                this.setExDigitalmoneyAccounttoRedis(exDigitalmoneyAccountRedis);
              }
            }

          }

        }

        // 如果有数据改变并且保存到数据库的话就要保存日志
        /*
         * MongoUtil<UpdateAccountFundInfoLog, Long> mongoUtil = new
         * MongoUtil<UpdateAccountFundInfoLog, Long>(
         * UpdateAccountFundInfoLog.class,"update_accountfundinfo_log");
         * UpdateAccountFundInfoLog updateAccountFundInfoLog=new
         * UpdateAccountFundInfoLog();
         * updateAccountFundInfoLog.setWebsite(website);
         * updateAccountFundInfoLog.setCurrencyType(currencyType);
         * updateAccountFundInfoLog.setOperatorName(ContextUtil.
         * getCurrentUser().getUsername());
         * updateAccountFundInfoLog.setUserName(appCustomer.getUserName(
         * )); updateAccountFundInfoLog.setNewAccountFundInfo(Mapper.
         * objectToJson(newlist));
         * updateAccountFundInfoLog.setOldAccountFundInfo(Mapper.
         * objectToJson(oldlist));
         * updateAccountFundInfoLog.setCreatDate(new Date());
         * mongoUtil.save(updateAccountFundInfoLog);
         */

      }
      long end = System.currentTimeMillis();
      System.out.println("单个余额核算时间==" + (end - start));
      return map;
    } else {
      long end = System.currentTimeMillis();
      System.out.println("单个余额核算时间==" + (end - start));
      return null;
      /*
       * // 资金正确 // 保存币账户 for (AccountFundInfo newafundcoin : newlist) {
       * AppAccountSureold eda =
       * remoteAppAccountSureoldService.getBycustomerIdAndcoinCode(
       * customerId, appCustomer.getUserName(),
       * newafundcoin.getCoinCode(), currencyType, website); if (null !=
       * eda) { eda.setHotMoney(newafundcoin.getHotMoney());
       * eda.setColdMoney(newafundcoin.getColdMoney());
       * eda.setLendMoney(newafundcoin.getLendMoney());
       * eda.setSureTime(new Date());
       * remoteAppAccountSureoldService.updateAccount(eda); } else { eda =
       * new AppAccountSureold(); eda.setCustomerId(customerId);
       * eda.setUserName(appCustomer.getUserName());
       * eda.setCoinCode(newafundcoin.getCoinCode());
       * eda.setCurrencyType(currencyType); eda.setWebsite(website);
       * eda.setAccountId(Long.valueOf("1"));
       * eda.setHotMoney(newafundcoin.getHotMoney());
       * eda.setColdMoney(newafundcoin.getColdMoney());
       * eda.setLendMoney(newafundcoin.getLendMoney());
       * eda.setSureTime(new Date());
       * remoteAppAccountSureoldService.saveAccount(eda);
       *
       * } }
       *
       * return null;
       */
    }

  }

  public ExDigitalmoneyAccountRedis getExDigitalmoneyAccountByRedis(String id) {
    RedisUtil redisUtilExDigitalmoneyAccount = new RedisUtil(ExDigitalmoneyAccountRedis.class);
    Object obj = redisUtilExDigitalmoneyAccount.get(id);
    if (null == obj) {
      return null;
    } else {
      ExDigitalmoneyAccountRedis accountr = (ExDigitalmoneyAccountRedis) obj;
      return accountr;
    }

  }

  public void setExDigitalmoneyAccounttoRedis(ExDigitalmoneyAccountRedis exDigitalmoneyAccount) {
    RedisUtil redisUtilExDigitalmoneyAccount = new RedisUtil(ExDigitalmoneyAccountRedis.class);
    redisUtilExDigitalmoneyAccount
        .put(exDigitalmoneyAccount, exDigitalmoneyAccount.getId().toString());
  }

  public AppAccountRedis getAppAccountByRedis(String id) {
    RedisUtil redisUtilAppAccount = new RedisUtil(AppAccountRedis.class);
    Object obj = redisUtilAppAccount.get(id);
    if (null == obj) {
      return null;
    } else {
      AppAccountRedis accountr = (AppAccountRedis) obj;
      return accountr;
    }

  }

  public void setAppAccounttoRedis(AppAccountRedis appAccount) {
    RedisUtil redisUtilAppAccount = new RedisUtil(AppAccountRedis.class);
    redisUtilAppAccount.put(appAccount, appAccount.getId().toString());
  }

  @Override
  public Map<String, Object> culRedisAndSqlAccountByCustomer(Long customerId, String currencyType,
      String website, Boolean isSave, Boolean iserrorright) {
    AppAccountService appAccountService = (AppAccountService) ContextUtil
        .getBean("appAccountService");

    List<AccountFundInfo> newlist = new ArrayList<AccountFundInfo>();
    List<AccountFundInfo> oldlist = new ArrayList<AccountFundInfo>();
    AppCustomer appCustomer = appCustomerService.get(customerId);
    AppAccount appAccount = appAccountService.getByCustomerIdAndType(customerId, null, null);
    if (null == appAccount) {
      return null;
    }
    if (null == appCustomer) {
      return null;
    }
    AccountFundInfo afundrmb = new AccountFundInfo();
    afundrmb.setCoinCode(currencyType);
    afundrmb.setUserName(appCustomer.getUserName());
    afundrmb.setHotMoney(appAccount.getHotMoney());
    afundrmb.setColdMoney(appAccount.getColdMoney());
    afundrmb.setLendMoney(appAccount.getLendMoney());

    afundrmb.setWebsite(website);
    afundrmb.setCurrencyType(currencyType);
    oldlist.add(afundrmb);

    QueryFilter edafilter = new QueryFilter(ExDigitalmoneyAccount.class);
    edafilter.addFilter("customerId=", customerId);
    List<ExDigitalmoneyAccount> listeda = remoteExDigitalmoneyAccountService.find(edafilter);

    for (ExDigitalmoneyAccount eda : listeda) {
      AccountFundInfo afundcoin = new AccountFundInfo();
      afundcoin.setCoinCode(eda.getCoinCode());
      afundcoin.setUserName(appCustomer.getUserName());
      afundcoin.setHotMoney(eda.getHotMoney());
      afundcoin.setColdMoney(eda.getColdMoney());
      afundcoin.setLendMoney(eda.getLendMoney());

      afundcoin.setWebsite(website);
      afundcoin.setCurrencyType(currencyType);
      oldlist.add(afundcoin);
    }

    AppAccountRedis appAccountRedis = getAppAccountByRedis(appAccount.getId().toString());
    if (null == appAccountRedis) {
      return null;
    }

    AccountFundInfo afundrmbnew = new AccountFundInfo();
    afundrmbnew.setCoinCode(currencyType);
    afundrmbnew.setUserName(appCustomer.getUserName());
    afundrmbnew.setHotMoney(appAccountRedis.getHotMoney());
    afundrmbnew.setColdMoney(appAccountRedis.getColdMoney());
    afundrmbnew.setLendMoney(appAccount.getLendMoney()); //??
    afundrmbnew.setWebsite(website);
    afundrmbnew.setCurrencyType(currencyType);
    newlist.add(afundrmbnew);

    // 保存币账户
    for (ExDigitalmoneyAccount eda : listeda) {
      ExDigitalmoneyAccountRedis exDigitalmoneyAccountRedis = getExDigitalmoneyAccountByRedis(
          eda.getId().toString());
      if (null == exDigitalmoneyAccountRedis) {
        return null;
      }
      AccountFundInfo afundcoinnew = new AccountFundInfo();
      afundcoinnew.setCoinCode(eda.getCoinCode());
      afundcoinnew.setUserName(appCustomer.getUserName());
      afundcoinnew.setHotMoney(exDigitalmoneyAccountRedis.getHotMoney());
      afundcoinnew.setColdMoney(exDigitalmoneyAccountRedis.getColdMoney());
      afundcoinnew.setLendMoney(eda.getLendMoney());//???
      afundcoinnew.setWebsite(website);
      afundcoinnew.setCurrencyType(currencyType);
      newlist.add(afundcoinnew);


    }
    if (iserrorright) { // true全部查出
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("oldAccountFundInfolist", oldlist);
      map.put("newAccountFundInfolist", newlist);
      return map;
    }
    int i = 0;
    Boolean flag = true;
    while (i < newlist.size()) {
      if (!oldlist.get(i).toString().equals(newlist.get(i).toString())) {
        flag = false;
        break;
      }
      i++;
    }
    if (!flag) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("oldAccountFundInfolist", oldlist);
      map.put("newAccountFundInfolist", newlist);
      return map;
    }
    return null;
  }

  @Override
  public void culRedisAndSqlToSqlAccountByCustomer(Long customerId, String currencyType,
      String website) {
    AppAccountService appAccountService = (AppAccountService) ContextUtil
        .getBean("appAccountService");
    AppAccount appAccount = appAccountService.getByCustomerIdAndType(customerId, null, null);
    if (null == appAccount) {
      return;
    }
    // 保存资金账户

    AppAccountRedis appAccountRedis = getAppAccountByRedis(appAccount.getId().toString());
    if (null != appAccountRedis) {
      appAccount.setHotMoney(appAccountRedis.getHotMoney());
      appAccount.setColdMoney(appAccountRedis.getColdMoney());
      appAccountService.updateAccount(appAccount);
    }
    // 保存币账户
    QueryFilter edafilter = new QueryFilter(ExDigitalmoneyAccount.class);
    edafilter.addFilter("customerId=", customerId);
    List<ExDigitalmoneyAccount> listeda = remoteExDigitalmoneyAccountService.find(edafilter);
    for (ExDigitalmoneyAccount eda : listeda) {
      ExDigitalmoneyAccountRedis exDigitalmoneyAccountRedis = getExDigitalmoneyAccountByRedis(
          eda.getId().toString());
      eda.setHotMoney(exDigitalmoneyAccountRedis.getHotMoney());
      eda.setColdMoney(exDigitalmoneyAccountRedis.getColdMoney());
      remoteExDigitalmoneyAccountService.updateAccount(eda);
    }


  }

}
