package com.mz.trade.model;

import com.alibaba.fastjson.JSON;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisTradeService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.trade.MQmanager.MQEnter;
import com.mz.trade.comparator.AscBigDecimalComparator;
import com.mz.trade.comparator.DescBigDecimalComparator;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.trade.redis.model.EntrustByUser;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.core.thread.ThreadPool;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.AppAccountRedis;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import com.mz.trade.redis.model.ExchangeDataCacheRedis;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tk.mybatis.mapper.util.StringUtil;

public class TradeRedis {

  public static RedisUtil redisUtilAppAccount = new RedisUtil(AppAccountRedis.class);
  public static RedisUtil redisUtilExDigitalmoneyAccount = new RedisUtil(
      ExDigitalmoneyAccountRedis.class);
  public static RedisUtil redisUtilExEntrust = new RedisUtil(EntrustTrade.class);
  public static RedisUtil redisUtilEntrustByUser = new RedisUtil(EntrustByUser.class);
  public static List<Accountadd> aaddlists = new ArrayList<Accountadd>(); // 一次匹配增量记录
  public static List<ExOrderInfo> eoinfolists = new ArrayList<ExOrderInfo>(); // 一次匹配成交记录
  public static String noSaveEntrustByUser = null;
  public static Integer isNoSaveEntrustByUser = null;
  // public static HashMap<String,List<Entrust>> exEntrustsMemoryMap=new
  // HashMap<String,List<Entrust>>();

  public static String getHeader(EntrustTrade exEntrust) {
    if (exEntrust.getType().equals(2)) {
      String header = exEntrust.getCoinCode() + "_" + exEntrust.getFixPriceCoinCode() + ":sell";
      return header;
    } else {
      String header = exEntrust.getCoinCode() + "_" + exEntrust.getFixPriceCoinCode() + ":buy";
      return header;
    }
  }

  public static String getHeader(String coinCode, String fixPriceCoinCode, Integer type) {
    if (null == type) {
      String header = coinCode + "_" + fixPriceCoinCode;
      return header;
    } else if (type.equals(2)) {
      String header = coinCode + "_" + fixPriceCoinCode + ":sell";
      return header;
    } else {
      String header = coinCode + "_" + fixPriceCoinCode + ":buy";
      return header;
    }
  }

  public static String getHeaderMatch(String coinCode, String fixPriceCoinCode, Integer type) {
    if (null == type) {
      String header = coinCode + "_" + fixPriceCoinCode;
      return header;
    } else if (type.equals(1)) {
      String header = coinCode + "_" + fixPriceCoinCode + ":sell";
      return header;
    } else {
      String header = coinCode + "_" + fixPriceCoinCode + ":buy";
      return header;
    }
  }

  public static String getEntrustTimeFlag(String coinCode, String fixPriceCoinCode) {
    String header = coinCode + "_" + fixPriceCoinCode + ":isTimgEntrsutFlag";
    return header;

  }

  public static String getHeaderMatch(EntrustTrade exEntrust) {
    if (exEntrust.getType().equals(1)) {
      String header = exEntrust.getCoinCode() + "_" + exEntrust.getFixPriceCoinCode() + ":sell";
      return header;
    } else {
      String header = exEntrust.getCoinCode() + "_" + exEntrust.getFixPriceCoinCode() + ":buy";
      return header;
    }
  }

  public static String getHeaderFront(EntrustTrade exEntrust) {
    String header = exEntrust.getCoinCode() + "_" + exEntrust.getFixPriceCoinCode();
    return header;

  }


  /*	public static List<EntrustTrade> getMatch(EntrustTrade exEntrust) {

      String key = getHeaderMatch(exEntrust);
      RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
      String v = redisService.get(key);
      List<EntrustTrade> list = JSON.parseArray(v, EntrustTrade.class);
      return list;
    }
  */
  public static void setSelfonePrice(EntrustTrade entrust) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String key = getHeaderFront(entrust);
    if (entrust.getType().equals(1)) { // 买
      String buyonePricekey = key + ":" + ExchangeDataCacheRedis.BuyOnePrice; // 自己单的买一
      String buyonePrices = redisService.get(buyonePricekey);
      if (StringUtil.isEmpty(buyonePrices)) {
        redisService.save(buyonePricekey, JSON.toJSONString(entrust.getEntrustPrice()));
      } else {
        if (new BigDecimal(buyonePrices).compareTo(entrust.getEntrustPrice()) == -1) {
          redisService.save(buyonePricekey, JSON.toJSONString(entrust.getEntrustPrice()));
        }
      }

    } else {
      String sellonePricekey = key + ":" + ExchangeDataCacheRedis.SellOnePrice; // 自己单的卖一
      String sellonePrices = redisService.get(sellonePricekey);
      if (StringUtil.isEmpty(sellonePrices)) {
        redisService.save(sellonePricekey, JSON.toJSONString(entrust.getEntrustPrice()));
      } else {
        if (new BigDecimal(sellonePrices).compareTo(entrust.getEntrustPrice()) == 1) {
          redisService.save(sellonePricekey, JSON.toJSONString(entrust.getEntrustPrice()));
        }
      }
    }
  }

  public static String getTradeDealEntrustChangeNum() {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(ExchangeDataCacheRedis.TradeDealEntrustChangeNum);
    if (!StringUtil.isEmpty(v) && !"null".equals(v)) {
      String num = String.valueOf((Integer.valueOf(v) + 1));
      redisService.save(ExchangeDataCacheRedis.TradeDealEntrustChangeNum, num);
      return ExchangeDataCacheRedis.TradeDealEntrustChange + ":" + num;
    } else {
      redisService.save(ExchangeDataCacheRedis.TradeDealEntrustChangeNum, "0");
      return ExchangeDataCacheRedis.TradeDealEntrustChange + ":" + 0;
    }
  }

  public static String getTradeDealAccountChangeNum() {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(ExchangeDataCacheRedis.TradeDealAccountChangeNum);
    if (!StringUtil.isEmpty(v) && !"null".equals(v)) {
      String num = String.valueOf((Integer.valueOf(v) + 1));
      redisService.save(ExchangeDataCacheRedis.TradeDealAccountChangeNum, num);
      return ExchangeDataCacheRedis.TradeDealAccountChange + ":" + num;
    } else {
      redisService.save(ExchangeDataCacheRedis.TradeDealAccountChangeNum, "0");
      return ExchangeDataCacheRedis.TradeDealAccountChange + ":" + 0;
    }
  }

  public static String getTradeDealOrderInfoChangeNum() {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(ExchangeDataCacheRedis.TradeDealOrderInfoChangeNum);
    if (!StringUtil.isEmpty(v) && !"null".equals(v)) {
      String num = String.valueOf((Integer.valueOf(v) + 1));
      redisService.save(ExchangeDataCacheRedis.TradeDealOrderInfoChangeNum, num);
      return ExchangeDataCacheRedis.TradeDealOrderInfoChange + ":" + num;
    } else {
      redisService.save(ExchangeDataCacheRedis.TradeDealOrderInfoChangeNum, "0");
      return ExchangeDataCacheRedis.TradeDealOrderInfoChange + ":" + 0;
    }
  }

  public static void NoMatchEnd(EntrustTrade entrust, List<Accountadd> aadds) {
    //System.out.println("匹配失败==" + entrust.getEntrustNum());
    setSelfonePrice(entrust);
    putSelfEntrustTradelist(entrust);// 进自己的委托单列表// 进匹配委托
    //	putchange(entrust); // 已经改变了的委托单；
    MQEnter.pushDealFundMQ(aadds); //发出mq给缓存里的账号改变金额,添加资金的增量记录
    putEntrustByUser(entrust);

    // 已经改变了的委托单；
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    List<EntrustTrade> listchange = new ArrayList<EntrustTrade>();
    listchange.add(entrust);
    redisService.save(getTradeDealEntrustChangeNum(), JSON.toJSONString(listchange));

    //通知定时器需要标记需要深度计算
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    redisTradeService
        .save(getEntrustTimeFlag(entrust.getCoinCode(), entrust.getFixPriceCoinCode()), "1");


  }

  public static void matchOneEnd(List<Accountadd> exEntrustAccountadd, EntrustTrade exEntrust,
      Map<String, List<EntrustTrade>> maping, List<EntrustTrade> listed, BigDecimal matchonePrice) {
    aaddlists.addAll(0, exEntrustAccountadd);
    listed.add(exEntrust);
    if (!exEntrust.getStatus().equals(2)) {
      putSelfEntrustTradelist(exEntrust);// 进自己的委托单列表
      setSelfonePrice(exEntrust);// 设置自己队列的委一价
    }
    // 改变的对手单
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    for (Map.Entry<String, List<EntrustTrade>> entry : maping.entrySet()) {
      List<EntrustTrade> listing = entry.getValue();
      if (null == listing || listing.size() == 0) {
        redisTradeService.delete(entry.getKey());
      } else {
        redisTradeService.save(entry.getKey(), JSON.toJSONString(listing));
      }

    }
    //	put(listed, "change"); // 已经改变了的委托单；
    MQEnter.pushDealFundMQ(aaddlists); //发出mq给缓存里的账号改变金额,添加资金的增量记录
    //	putAccountaddlist(aaddlists);// 添加资金的增量记录
    //	putExOrderInfolist(eoinfolists);// 成交信息
    for (EntrustTrade entrust : listed) {
      putEntrustByUser(entrust);
    }
    setMatchOnePrice(matchonePrice, exEntrust); //对手单一价
    String keyFront = getHeaderFront(exEntrust);
    setExchangeDataCache(eoinfolists, keyFront); //设置成交列表，最新成交价

    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save(getTradeDealEntrustChangeNum(), JSON.toJSONString(listed));// 已经改变了的委托单；
    redisService.save(getTradeDealOrderInfoChangeNum(), JSON.toJSONString(eoinfolists));// 成交信息

    //刷新时间分区的最新价，最高价，最低价，收盘价
    List<ExOrderInfo> arr = new ArrayList<ExOrderInfo>();
    arr.addAll(eoinfolists);
    PeriodLastKLineListRunable periodLastKLineListRunable = new PeriodLastKLineListRunable(arr);
    ThreadPool.exe(periodLastKLineListRunable);
    aaddlists.clear();
    eoinfolists.clear();
    //通知定时器需要标记需要深度计算
    redisTradeService
        .save(getEntrustTimeFlag(exEntrust.getCoinCode(), exEntrust.getFixPriceCoinCode()), "1");

  }

  public static void cancelEntrust(EntrustTrade exEntrust, List<EntrustTrade> entrustlist,
      String key,
      List<Accountadd> aadds) {
    putIngExEntrust(entrustlist, key); // 还没完成的委托单；
    //	putchange(exEntrust); // 已经改变了的委托单；
    MQEnter.pushDealFundMQ(aadds); //发出mq给缓存里的账号改变金额,添加资金的增量记录
    //	putAccountaddlist(aadds);
    putEntrustByUser(exEntrust);
    setcancelSelfOnePrice(exEntrust);// 必须最后一步

    // 已经改变了的委托单；
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    List<EntrustTrade> listchange = new ArrayList<EntrustTrade>();
    listchange.add(exEntrust);
    redisService.save(getTradeDealEntrustChangeNum(), JSON.toJSONString(listchange));
    //通知定时器需要标记需要深度计算
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    redisTradeService
        .save(getEntrustTimeFlag(exEntrust.getCoinCode(), exEntrust.getFixPriceCoinCode()), "1");

  }

  public static void setcancelSelfOnePrice(EntrustTrade exEntrust) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    List<BigDecimal> keyslist = TradeRedis.getSelfkeys(exEntrust);// 查所有的keys
    String keyFront = getHeaderFront(exEntrust);
    if (null != keyslist && keyslist.size() > 0) {
      BigDecimal onePrice = keyslist.get(0);
      if (null != onePrice) {
        if (exEntrust.getType().equals(1)) { //
          String buyonePricekey = keyFront + ":" + ExchangeDataCacheRedis.BuyOnePrice; // 买一
          redisService.save(buyonePricekey, JSON.toJSONString(onePrice));
        } else {
          String sellonePricekey = keyFront + ":" + ExchangeDataCacheRedis.SellOnePrice; // 卖一
          redisService.save(sellonePricekey, JSON.toJSONString(onePrice));
        }
      }

    } else {
      if (exEntrust.getType().equals(1)) { //
        String buyonePricekey = keyFront + ":" + ExchangeDataCacheRedis.BuyOnePrice; // 买一
        redisService.delete(buyonePricekey);
      } else {
        String sellonePricekey = keyFront + ":" + ExchangeDataCacheRedis.SellOnePrice; // 卖一
        redisService.delete(sellonePricekey);
      }

    }
  }

  public static void setMatchOnePrice(BigDecimal matchonePrice, EntrustTrade exEntrust) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String keyFront = getHeaderFront(exEntrust);
    if (null != matchonePrice) {
      if (exEntrust.getType().equals(2)) { // 卖
        String buyonePricekey = keyFront + ":" + ExchangeDataCacheRedis.BuyOnePrice; // 对手单的买一
        redisService.save(buyonePricekey, JSON.toJSONString(matchonePrice));
      } else {
        String sellonePricekey = keyFront + ":" + ExchangeDataCacheRedis.SellOnePrice; // 对手单的卖一
        redisService.save(sellonePricekey, JSON.toJSONString(matchonePrice));
      }
    } else {

      if (exEntrust.getType().equals(2)) { // 卖
        String buyonePricekey = keyFront + ":" + ExchangeDataCacheRedis.BuyOnePrice; // 对手单的买一
        redisService.delete(buyonePricekey);
      } else {
        String sellonePricekey = keyFront + ":" + ExchangeDataCacheRedis.SellOnePrice; // 对手单的卖一
        redisService.save(sellonePricekey, JSON.toJSONString(matchonePrice));
        redisService.delete(sellonePricekey);
      }

    }

  }

  public static void setExchangeDataCache(List<ExOrderInfo> listExOrderInfo, String header) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    ExOrderInfo exOrderInfo = listExOrderInfo.get(listExOrderInfo.size() - 1); //最新成交
    // 设置当前最新成交价
    TradeRedis.setStringData(header + ":" + ExchangeDataCacheRedis.CurrentExchangPrice,
        exOrderInfo.getTransactionPrice().setScale(10, BigDecimal.ROUND_HALF_UP).toString());
    // 保存这条成交记录到re
    String v = redisService.get(header + ":" + ExchangeDataCacheRedis.LastOrderRecords);
    List<ExOrderInfo> list = null;
    if (!StringUtil.isEmpty(v)) {
      list = JSON.parseArray(v, ExOrderInfo.class);
    } else {
      list = new ArrayList<ExOrderInfo>();
    }
    list.addAll(listExOrderInfo);
    if (list.size() > ExchangeDataCacheRedis.LastOrderRecordsLmit) {
      List<ExOrderInfo> sublist = list
          .subList(list.size() - ExchangeDataCacheRedis.LastOrderRecordsLmit, list.size());
      redisService
          .save(header + ":" + ExchangeDataCacheRedis.LastOrderRecords, JSON.toJSONString(sublist));
    } else {
      redisService
          .save(header + ":" + ExchangeDataCacheRedis.LastOrderRecords, JSON.toJSONString(list));

    }

  }

  public static void putEntrustByUser(EntrustTrade entrust) {
    Boolean flag = isSaveEntrustByUser(entrust.getUserName());
    if (!flag) {
      EntrustByUser ebu = (EntrustByUser) redisUtilEntrustByUser
          .get(entrust.getCustomerId().toString());
      if (null == ebu) {
        ebu = new EntrustByUser();
        ebu.addEntrust(entrust);
        ebu.setCustomerId(entrust.getCustomerId());
      } else {
        ebu.addEntrust(entrust);
      }
      if (ebu.getEntrustedmap().size() == 0 && ebu.getEntrustingmap().size() == 0) {
        TradeRedis.redisUtilEntrustByUser.delete(ebu.getCustomerId().toString());
      } else {
        TradeRedis.redisUtilEntrustByUser.put(ebu, ebu.getCustomerId().toString());
      }
    }


  }

  public static Boolean isSaveEntrustByUser(String userName) {
    if (null != isNoSaveEntrustByUser) { //第二次开始就走这
      if (null != noSaveEntrustByUser) {
        int i = 0;
        String[] rt = noSaveEntrustByUser.split(",");
        while (i < rt.length) {
          if (rt[i].equals(userName)) {
            return true;
          }
          i++;
        }

      }
      return false;
    } else {
      isNoSaveEntrustByUser = new Integer("1"); //启动后的第一次就走这
      String appisNoSaveEntrustByUser = PropertiesUtils.APP.getProperty("app.noSaveEntrustByUser");
      if (!StringUtil.isEmpty(appisNoSaveEntrustByUser)) {
        noSaveEntrustByUser = appisNoSaveEntrustByUser;
        int i = 0;
        String[] rt = appisNoSaveEntrustByUser.split(",");
        while (i < rt.length) {
          if (rt[i].equals(userName)) {

            return true;
          }
          i++;
        }

      }
      return false;
    }
  }

  public static void putchange(EntrustTrade entrust) {

    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(ExchangeDataCacheRedis.ChangeEntrust);
    List<EntrustTrade> list = JSON.parseArray(v, EntrustTrade.class);
    if (null == list) {
      list = new ArrayList<EntrustTrade>();
    }
    //	if (entrust.equals(2)) {
    list.add(entrust);
    //	}

    redisService.save(ExchangeDataCacheRedis.ChangeEntrust, JSON.toJSONString(list));
  }

  public static void matchOneAndOneEnd(ExOrderInfo exOrderInfo, List<Accountadd> aadds) {
    eoinfolists.add(exOrderInfo);
    for (Accountadd aadd : aadds) {
      aaddlists.add(aadd);
    }
  }

/*	public static void putAccountaddlist(List<Accountadd> aadds) {

		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		String v = redisService.get(ExchangeDataCacheRedis.AccountAddS);
		List<Accountadd> list = JSON.parseArray(v, Accountadd.class);
		if (null == list) {
			list = new ArrayList<Accountadd>();
		}
		for (Accountadd accountadd : aadds) {
			list.add(accountadd);
		}
		redisService.save(ExchangeDataCacheRedis.AccountAddS, JSON.toJSONString(list));

		
	}*/

  public static void putExOrderInfolist(List<ExOrderInfo> oinfolist) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(ExchangeDataCacheRedis.ExorderInfoS);
    List<ExOrderInfo> list = JSON.parseArray(v, ExOrderInfo.class);
    if (null == list) {
      list = new ArrayList<ExOrderInfo>();
    }
    for (ExOrderInfo info : oinfolist) {
      list.add(info);
    }
    redisService.save(ExchangeDataCacheRedis.ExorderInfoS, JSON.toJSONString(list));
  }

  public static void putSelfEntrustTradelist(EntrustTrade exEntrust) {
    String key = getHeader(exEntrust) + ":" + exEntrust.getEntrustPrice()
        .setScale(10, BigDecimal.ROUND_HALF_EVEN).toString();
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    String v = redisTradeService.get(key);
    List<EntrustTrade> list = JSON.parseArray(v, EntrustTrade.class);
    if (null == list) {
      list = new ArrayList<EntrustTrade>();
    }
    list.add(exEntrust);
    redisTradeService.save(key, JSON.toJSONString(list));
  }


  public static BigDecimal getMatchOnePrice(EntrustTrade exEntrust) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String key = "";
    if (exEntrust.getType().equals(1)) {
      key = getHeaderFront(exEntrust) + ":" + ExchangeDataCacheRedis.SellOnePrice;
    } else {
      key = getHeaderFront(exEntrust) + ":" + ExchangeDataCacheRedis.BuyOnePrice;
    }
    String onePrice = redisService.get(key);
    if (StringUtil.isEmpty(onePrice)) {
      return null;
    } else {
      return new BigDecimal(onePrice);
    }

  }

  /*
   * public static BigDecimal getOnePrice(EntrustTrade exEntrust){
   * RedisService redisService = (RedisService)
   * ContextUtil.getBean("redisService"); String key = "";
   * if(exEntrust.getType().equals(1)){ key =
   * getHeaderMatch(exEntrust)+":"+SellOnePrice; }else{ key =
   * getHeaderMatch(exEntrust)+":"+BuyOnePrice; } String onePrice=
   * redisService.get(key); if(StringUtil.isEmpty(onePrice)){ return null;
   * }else{ return new BigDecimal(onePrice); }
   *
   *
   * }
   */
  public static void putIngExEntrust(List<EntrustTrade> exEntrustlist, String key) {
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
	/*	if (null == exEntrustlist) {
			exEntrustlist = new ArrayList<EntrustTrade>();
		} else {
		   //时间优先
		}*/
    if (null == exEntrustlist || exEntrustlist.size() == 0) {
      redisTradeService.delete(key);
    } else {
      redisTradeService.save(key, JSON.toJSONString(exEntrustlist));
    }


  }

  public static void put(List<EntrustTrade> exEntrustlist, String type) {
    if (null == exEntrustlist || exEntrustlist.size() == 0) {
      return;
    }
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    if (type.equals("change")) {
      String v = redisService.get(ExchangeDataCacheRedis.ChangeEntrust);
      List<EntrustTrade> list = JSON.parseArray(v, EntrustTrade.class);
      if (null == list) {
        redisService.save(ExchangeDataCacheRedis.ChangeEntrust, JSON.toJSONString(exEntrustlist));
      } else {
        for (EntrustTrade entrust : exEntrustlist) {
          list.add(entrust);
          putEntrustByUser(entrust);
        }
        redisService.save(ExchangeDataCacheRedis.ChangeEntrust, JSON.toJSONString(list));
      }

    }

  }

  public static void put(ExOrderInfo exOrderInfo) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(ExchangeDataCacheRedis.ExorderInfoS);
    List<ExOrderInfo> list = JSON.parseArray(v, ExOrderInfo.class);
    list.add(exOrderInfo);
    redisService.save(ExchangeDataCacheRedis.ExorderInfoS, JSON.toJSONString(list));
  }

  public static void put(Accountadd accountadd) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(ExchangeDataCacheRedis.AccountAddS);
    List<Accountadd> list = JSON.parseArray(v, Accountadd.class);
    list.add(accountadd);
    redisService.save(ExchangeDataCacheRedis.AccountAddS, JSON.toJSONString(list));
  }

  public static String getStringData(String key) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String v = redisService.get(key);
    return v;

  }

  public static String getTradeStringData(String key) {
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");

    String v = redisTradeService.get(key);
    return v;

  }

  public static void setStringData(String key, String val) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String preData = redisService.save(key, val);

  }

  public static List<BigDecimal> getSelfkeys(EntrustTrade exEntrust) {
    String key = getHeader(exEntrust);
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    Set<String> keys = redisTradeService.noPerkeys(key + ":");
    if (null == keys) {
      return null;
    } else {
      List<BigDecimal> list2 = new ArrayList<BigDecimal>();
      Iterator<String> iterator = keys.iterator();
      while (iterator.hasNext()) {
        String keystr = iterator.next();
        BigDecimal ks = new BigDecimal(keystr.split(":")[2]);
        list2.add(ks);
      }
      if (exEntrust.getType().equals(2)) {
        Collections.sort(list2, new AscBigDecimalComparator());
      } else {
        Collections.sort(list2, new DescBigDecimalComparator());
      }
      return list2;
    }
  }

  public static List<BigDecimal> getMatchkeys(EntrustTrade exEntrust) {
    String key = getHeaderMatch(exEntrust);
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    Set<String> keys = redisTradeService.noPerkeys(key + ":");
    if (null == keys) {
      return null;
    } else {
      List<BigDecimal> list2 = new ArrayList<BigDecimal>();
      Iterator<String> iterator = keys.iterator();
      while (iterator.hasNext()) {
        String keystr = iterator.next();
        BigDecimal ks = new BigDecimal(keystr.split(":")[2]);
        list2.add(ks);
      }
      if (exEntrust.getType().equals(1)) {
        Collections.sort(list2, new AscBigDecimalComparator());
      } else {
        Collections.sort(list2, new DescBigDecimalComparator());
      }
      return list2;
    }
  }

  public static List<EntrustTrade> getMatchEntrustTradeBykey(String keys) {
    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    String v = redisTradeService.get(keys);
    List<EntrustTrade> list = JSON.parseArray(v, EntrustTrade.class);
    return list;
  }
  /*
   * public static void put(Entrust exEntrust){ String
   * key=getHeader(exEntrust); List<Entrust> exEntrustlist
   * =exEntrustsMemoryMap.get(key); exEntrustlist.add(exEntrust);
   * Collections.sort(exEntrustlist, new DescEntrustComparator());
   * exEntrustsMemoryMap.put(key, exEntrustlist); } public static
   * List<Entrust> get(Entrust exEntrust){ String key=getHeader(exEntrust);
   * List<Entrust> exEntrustlist =exEntrustsMemoryMap.get(key); return
   * exEntrustlist; }
   */
}
