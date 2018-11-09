package com.mz.trade.kline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huobi.api.ApiClient;
import com.huobi.api.ApiException;
import com.huobi.response.Kline;
import com.huobi.response.KlineResponse;
import com.mz.core.quartz.QuartzJob;
import com.mz.core.quartz.QuartzManager;
import com.mz.core.quartz.ScheduleJob;
import com.mz.exchange.kline.model.TransactionOrder;
import com.mz.exchange.kline2.model.LastKLinePayload;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.redis.common.utils.RedisService;
import com.mz.trade.entrust.ExchangeDataCache;
import com.mz.trade.entrust.dao.ExOrderDao;
import com.mz.trade.entrust.model.ExOrder;
import com.mz.trade.entrust.service.ExOrderService;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.http.HttpConnectionUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.model.AppHolidayConfig;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.manage.remote.model.Coin2;
import com.mz.web.remote.RemoteAppConfigService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * K线生成方法
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2016年7月26日 上午10:05:04
 */
public class KlineEngine {

  private static final Logger log = Logger.getLogger(KlineEngine.class);

  // 数据库中保留k线的数据条数
  public static final int HOLD_COUNT = 800;

  /**
   * 时间分钟数校正
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param date
   * @param: @param space
   * @param: @return 返回时间区间开始值
   * @return: Date
   * @Date : 2016年5月3日 下午5:15:55
   * @throws:
   */
  public static Date timeRevise(Date date, int space) {
    String mm = DateUtil.dateToString(date, "mm");
    int i = Integer.valueOf(mm).intValue() % space;
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MINUTE, -i);
    return cal.getTime();
  }

  public static void addKlineNode(RedisService redisService, String coinCode,
      TransactionOrder order, JSONObject rootObj, String period) {
    if (rootObj != null) {
      JSONObject obj = rootObj.getJSONObject(period);
      if (obj != null) {
        if (obj.getString("startTime").equals(order.getTransactionTime())) {//如果和备份节点时间区间相同
          order.setMinPrice(obj.getBigDecimal("priceLow"));
          order.setMaxPrice(obj.getBigDecimal("priceHigh"));
          order.setStartPrice(obj.getBigDecimal("priceOpen"));
          order.setEndPrice(obj.getBigDecimal("priceLast"));
          order.setTransactionCount(obj.getBigDecimal("amount"));
        } else {
          String currencyStr = redisService.get(coinCode + ":PeriodLastKLineList");
          if (!StringUtils.isEmpty(currencyStr)) {
            JSONArray parseArray = JSON.parseArray(currencyStr);
            for (int i = 0; i < parseArray.size(); i++) {
              JSONObject jsonObject = parseArray.getJSONObject(i);
              if (period.equals(jsonObject.getString("period"))) {
                if (jsonObject.getString("startTime").equals(order.getTransactionTime())) {//如果在一个区间
                  order.setMinPrice(jsonObject.getBigDecimal("priceLow"));
                  order.setMaxPrice(jsonObject.getBigDecimal("priceHigh"));
                  order.setStartPrice(jsonObject.getBigDecimal("priceOpen"));
                  order.setEndPrice(jsonObject.getBigDecimal("priceLast"));
                  order.setTransactionCount(jsonObject.getBigDecimal("amount"));
                } else {
                  order.setMinPrice(jsonObject.getBigDecimal("priceLast"));
                  order.setMaxPrice(jsonObject.getBigDecimal("priceLast"));
                  order.setStartPrice(jsonObject.getBigDecimal("priceLast"));
                  order.setEndPrice(jsonObject.getBigDecimal("priceLast"));
                  //order.setTransactionCount(obj.getBigDecimal("amount"));
                }
              }
            }
          }


        }
      } else {
        String currencyStr = redisService.get(coinCode + ":PeriodLastKLineList");
        if (!StringUtils.isEmpty(currencyStr)) {
          JSONArray parseArray = JSON.parseArray(currencyStr);
          for (int i = 0; i < parseArray.size(); i++) {
            JSONObject jsonObject = parseArray.getJSONObject(i);
            if (period.equals(jsonObject.getString("period"))) {
              if (jsonObject.getString("startTime").equals(order.getTransactionTime())) {//如果在一个区间
                order.setMinPrice(jsonObject.getBigDecimal("priceLow"));
                order.setMaxPrice(jsonObject.getBigDecimal("priceHigh"));
                order.setStartPrice(jsonObject.getBigDecimal("priceOpen"));
                order.setEndPrice(jsonObject.getBigDecimal("priceLast"));
                order.setTransactionCount(jsonObject.getBigDecimal("amount"));
              } else {
                order.setMinPrice(jsonObject.getBigDecimal("priceLast"));
                order.setMaxPrice(jsonObject.getBigDecimal("priceLast"));
                order.setStartPrice(jsonObject.getBigDecimal("priceLast"));
                order.setEndPrice(jsonObject.getBigDecimal("priceLast"));
                //order.setTransactionCount(jsonObject.getBigDecimal("amount"));
              }
            }
          }
        }
      }
    } else {

      String currencyStr = redisService.get(coinCode + ":PeriodLastKLineList");
      if (!StringUtils.isEmpty(currencyStr)) {
        JSONArray parseArray = JSON.parseArray(currencyStr);
        for (int i = 0; i < parseArray.size(); i++) {
          JSONObject jsonObject = parseArray.getJSONObject(i);
          if (period.equals(jsonObject.getString("period"))) {
            if (jsonObject.getString("startTime").equals(order.getTransactionTime())) {//如果在一个区间
              order.setMinPrice(jsonObject.getBigDecimal("priceLow"));
              order.setMaxPrice(jsonObject.getBigDecimal("priceHigh"));
              order.setStartPrice(jsonObject.getBigDecimal("priceOpen"));
              order.setEndPrice(jsonObject.getBigDecimal("priceLast"));
              order.setTransactionCount(jsonObject.getBigDecimal("amount"));
            } else {
              order.setMinPrice(jsonObject.getBigDecimal("priceLast"));
              order.setMaxPrice(jsonObject.getBigDecimal("priceLast"));
              order.setStartPrice(jsonObject.getBigDecimal("priceLast"));
              order.setEndPrice(jsonObject.getBigDecimal("priceLast"));
              //order.setTransactionCount(jsonObject.getBigDecimal("amount"));
            }
          }
        }
      }


    }

  }

  /**
   * 计算方法
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param time
   * @param: @param website
   * @param: @param currencyType
   * @param: @param coinCode
   * @return: void
   * @Date : 2016年7月28日 下午2:49:14
   * @throws:
   */
  public static void compute2(Integer time, String website, String currencyType, String coinCode) {

    Date nowDate = new Date();

    log.info("触发" + time + "分钟" + coinCode + "k线数据生成器.............................." + DateUtil
        .dateToString(nowDate));
    // 如果是做市模式，就开启判断当前时间 在不在 开市时间
    boolean openTrade = isOpenTrade(time, nowDate);
    if (!openTrade) {
      log.info(DateUtil.dateToString(nowDate) + "还没开市不生成K线");
      return;
    }

    String table = coinCode + ":klinedata:TransactionOrder_" + coinCode + "_" + time;

    // k线数据超过800条就将老的数据删除
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    List<TransactionOrder> orderList = JSON
        .parseArray(redisService.get(table), TransactionOrder.class);
    if (orderList == null) {
      orderList = new ArrayList<TransactionOrder>();
    }
    // 如果redis中老数据超过800条，只去最新的800条，其他的舍弃
    // TODO: 2018/8/6 0006 这里舍弃有必要进行排序吗？
    if (orderList != null && orderList.size() > HOLD_COUNT) {
      orderList = orderList.subList(0, HOLD_COUNT);
    }

    // 获得时间区间的前区间
    Date minDate = DateUtil.dateAddMinute(nowDate, -time);
    if (time == 30000) {// 如果传的值为30000，表示是月周期，则前区间以当前时间向前推一个月
      minDate = DateUtil.addMonth(nowDate, -1);
    }

    //创建结点
    TransactionOrder order = new TransactionOrder();
    order.setId(DateUtil.dateToString(nowDate, "yyyyMMddHHmm"));
    order.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
    order.setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
    order.setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));

    System.out.println(order.getTransactionTime() + "---------" + order.getTransactionEndTime());

    order.setTransactionCount(new BigDecimal(0));
    // 由于当前时间段没有交易数据，所以全部设置为历史数据的收盘价
    order.setMinPrice(new BigDecimal(0));
    order.setMaxPrice(new BigDecimal(0));
    order.setStartPrice(new BigDecimal(0));
    order.setEndPrice(new BigDecimal(0));

    orderList.add(0, order);

    String backups = redisService.get(coinCode + ":PeriodLastKLineList_backups");
    // TODO: 2018/8/14 0014 获取备份节点对应火币的数据？
    JSONObject rootObj = JSON.parseObject(backups);
    if (time == 1) {
      addKlineNode(redisService, coinCode, order, rootObj, "1min");
    } else if (time == 5) {
      addKlineNode(redisService, coinCode, order, rootObj, "5min");
    } else if (time == 15) {
      addKlineNode(redisService, coinCode, order, rootObj, "15min");
    } else if (time == 30) {
      addKlineNode(redisService, coinCode, order, rootObj, "30min");
    } else if (time == 60) {
      addKlineNode(redisService, coinCode, order, rootObj, "60min");
    } else if (time == 1440) {
      addKlineNode(redisService, coinCode, order, rootObj, "1day");

      //更新昨日收盘价到cn:coinInfoList中
      String coinStr = redisService.get("cn:coinInfoList2");
      if (!StringUtils.isEmpty(coinStr)) {
        List<Coin2> coins = JSON.parseArray(coinStr, Coin2.class);
        //判断coins是否有交易对信息
        boolean has = false;
        for (Coin2 c : coins) {
          if (coinCode.equals(c.getCoinCode() + "_" + c.getFixPriceCoinCode())) {
            c.setYesterdayPrice(order.getEndPrice().toString());
            redisService.save("cn:coinInfoList2", JSON.toJSONString(coins));
            has = true;
          }
        }
        //如果没有交易对信息
        if (!has) {
          String[] split = coinCode.split("_");
          Coin2 coin2 = new Coin2();
          coin2.setCoinCode(split[0]);
          coin2.setFixPriceCoinCode(split[1]);
          coin2.setYesterdayPrice(order.getEndPrice().toString());
          coins.add(coin2);
          redisService.save("cn:coinInfoList2", JSON.toJSONString(coins));
        }
      } else {
        String[] split = coinCode.split("_");
        Coin2 coin2 = new Coin2();
        coin2.setCoinCode(split[0]);
        coin2.setFixPriceCoinCode(split[1]);
        coin2.setYesterdayPrice(order.getEndPrice().toString());
        List<Coin2> coins = new ArrayList<Coin2>();
        coins.add(coin2);
        redisService.save("cn:coinInfoList2", JSON.toJSONString(coins));
      }

    } else if (time == 10080) {
      addKlineNode(redisService, coinCode, order, rootObj, "1week");
    } else if (time == 30000) {
      addKlineNode(redisService, coinCode, order, rootObj, "1mon");
    }

    // 最后保存数据
    redisService.save(table, JSON.toJSONString(orderList));
  }

  /**
   * 计算方法
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param time
   * @param: @param website
   * @param: @param currencyType
   * @param: @param coinCode
   * @return: void
   * @Date : 2016年7月28日 下午2:49:14
   * @throws:
   */
  public static void compute(Integer time, String website, String currencyType, String coinCode) {

    Date nowDate = new Date();

    log.info(
        "触发" + time + "分钟k线数据生成器.............................." + DateUtil.dateToString(nowDate));
    // 如果是做市模式，就开启判断当前时间 在不在 开市时间
    boolean openTrade = isOpenTrade(time, nowDate);
    if (!openTrade) {
      log.info(DateUtil.dateToString(nowDate) + "还没开市不生成K线");
      return;
    }

    /**
     * 算法介绍 一、获得当前时间,记录当前时间到上一个时间的K线结点 如果当前此节点没有数据，就查最后一天交易记录所在的K线时间段内的结点记录
     *
     * 比如现在12:00，查11.59——12:00之间的数据，算出结点，
     * 如果11.59——12:00之间没有得出结点数据,则查询最后一条交易记录， 比如最后一条交易记录在10：45
     * 那么就查10:45所在的时间得出结点数据
     *
     * 二、时间结点定位 1分钟就是按每1分钟间隔 3分钟从0.00开始 3,6,9,12.......60 5分钟
     * 5,10,15......60 15分钟 15,30......60
     *
     *
     */
    // 获得当前时间 格式化到分钟数

    String table = "TransactionOrder_" + website + "_" + currencyType + "_" + coinCode + "_" + time;

    // k线数据超过800条就将老的数据删除
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    List<TransactionOrder> orderList = JSON
        .parseArray(redisService.get(table), TransactionOrder.class);
    if (orderList == null) {
      orderList = new ArrayList<TransactionOrder>();
    }
    // 如果redis中老数据超过800条，只去最新的800条，其他的舍弃
    if (orderList != null && orderList.size() > HOLD_COUNT) {
      orderList = orderList.subList(0, HOLD_COUNT);
    }

    // 获得时间区间的前区间
    Date minDate = DateUtil.dateAddMinute(nowDate, -time);
    if (time == 30000) {// 如果传的值为30000，表示是月周期，则前区间以当前时间向前推一个月
      minDate = DateUtil.addMonth(nowDate, -1);
    }

    ExOrderDao exOrderDao = (ExOrderDao) ContextUtil.getBean("exOrderDao");
    Map<String, Object> query = new HashMap<String, Object>();
    query.put("minDate", DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
    query.put("maxDate", DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));
    query.put("website", website);
    query.put("currencyType", currencyType);
    String[] split = coinCode.split("_");
    query.put("coinCode", split[0]);
    query.put("fixPriceCoinCode", split[1]);
    List<TransactionOrder> list = exOrderDao.findOrder(query);
    // 如果两个时间差之间的数据不等于空
    if (list != null && list.size() > 0) {
      TransactionOrder exOrder = list.get(0);
      if (exOrder != null) {// 如果当前时间结点数据不为空
        // 如果数据库中没有当前结点,创建结点
        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.setId(DateUtil.dateToString(nowDate, "yyyyMMddHHmm"));
        transactionOrder.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
        transactionOrder.setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
        transactionOrder.setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));

        transactionOrder.setTransactionCount(exOrder.getTransactionCount());
        transactionOrder.setMinPrice(exOrder.getMinPrice());
        transactionOrder.setMaxPrice(exOrder.getMaxPrice());

        transactionOrder.setStartPrice(exOrder.getStartPrice());
        transactionOrder.setEndPrice(exOrder.getEndPrice());
        // 将新元素放到list的第一个位置
        orderList.add(0, transactionOrder);
      } else {// 如果当前时间结点数据为空,查询最近的一条交易记录,计算出所在的时间结点
        QueryFilter filter = new QueryFilter(ExOrder.class);
        filter.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
        filter.addFilter("website=", website);
        filter.addFilter("currencyType=", currencyType);
        filter.addFilter("coinCode=", split[0]);
        filter.addFilter("fixPriceCoinCode=", split[1]);
        filter.setOrderby("transactionTime desc");
        Page<ExOrder> page = PageHelper.startPage(0, 1);

        ExOrderService exOrderService = (ExOrderService) ContextUtil.getBean("exOrderService");
        List<ExOrder> find = exOrderService.find(filter);
        List<ExOrder> result = page.getResult();
        if (find != null && find.size() > 0) {
          ExOrder exOrder2 = find.get(0);
          // 二次查询，查找最近一条交易订单的时间，计算出开始时间和结束时间区间值
          Date minDate2 = timeRevise(exOrder2.getTransactionTime(), time);
          Date maxDate2 = DateUtil.dateAddMinute(minDate2, time);

          Map<String, Object> query2 = new HashMap<String, Object>();
          query2.put("minDate", DateUtil.dateToString(minDate2, "yyyy-MM-dd HH:mm"));
          query2.put("maxDate", DateUtil.dateToString(maxDate2, "yyyy-MM-dd HH:mm"));
          query2.put("website", website);
          query2.put("currencyType", currencyType);
          query2.put("coinCode", split[0]);
          query2.put("fixPriceCoinCode", split[1]);
          List<TransactionOrder> list2 = exOrderDao.findOrder(query2);

          if (list2 != null && list2.size() > 0 && list2.get(0) != null) {
            TransactionOrder _transactionOrder2 = list2.get(0);
            // 判断这个前一个结点不为空,正常情况百分百不为空
            if (_transactionOrder2 != null) {// 如果数据库中没有当前结点,创建结点
              TransactionOrder transactionOrder2 = new TransactionOrder();
              transactionOrder2.setId(DateUtil.dateToString(nowDate, "yyyyMMddHHmm"));
              transactionOrder2.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
              transactionOrder2
                  .setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
              transactionOrder2
                  .setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));

              transactionOrder2.setTransactionCount(new BigDecimal(0));
              // 由于当前时间段没有交易数据，所以全部设置为历史数据的收盘价
              transactionOrder2.setMinPrice(_transactionOrder2.getEndPrice());
              transactionOrder2.setMaxPrice(_transactionOrder2.getEndPrice());
              transactionOrder2.setStartPrice(_transactionOrder2.getEndPrice());
              transactionOrder2.setEndPrice(_transactionOrder2.getEndPrice());

              orderList.add(0, transactionOrder2);
            }
          }
        } else {// 历史节点为空
          // 如果数据库中没有当前结点,创建结点
          TransactionOrder transactionOrder0 = new TransactionOrder();
          transactionOrder0.setId(DateUtil.dateToString(nowDate, "yyyyMMddHHmm"));
          transactionOrder0.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
          transactionOrder0.setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
          transactionOrder0
              .setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));

          BigDecimal bigDecimal = new BigDecimal(0);
          transactionOrder0.setTransactionCount(bigDecimal);
          transactionOrder0.setMinPrice(bigDecimal);
          transactionOrder0.setMaxPrice(bigDecimal);
          transactionOrder0.setStartPrice(bigDecimal);
          transactionOrder0.setEndPrice(bigDecimal);
          orderList.add(0, transactionOrder0);
        }
      }
    }

    // 最后保存数据
    redisService.save(table, JSON.toJSONString(orderList));
  }

  /**
   * <p>
   * 对接okcoin Kline数据
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param time
   * @param: @param website
   * @param: @param currencyType
   * @param: @param coinCode
   * @return: void
   * @Date : 2016年10月25日 下午2:22:21
   * @throws: 1min : 1分钟 3min : 3分钟 5min : 5分钟 15min : 15分钟 30min : 30分钟 1day : 1日 3day : 3日 1week :
   * 1周 1hour : 1小时 2hour : 2小时 4hour : 4小时 6hour : 6小时 12hour : 12小时
   */
  public static void okcoinKline(String type, Date date, Integer time, String website,
      String currencyType, String coinCode) {
    String url = "https://www.okcoin.cn/api/v1/kline.do";
    if ("en".equals(website)) {
      url = "https://www.okcoin.com/api/v1/kline.do";
    }
    String param = "symbol=";
    if ("BTC".equals(coinCode)) {
      param += "btc_" + currencyType;
    }
    if ("LTC".equals(coinCode)) {
      param += "ltc_" + currencyType;
    }
    if (time.intValue() == 1) {
      param += "&type=1min";
    }
    if (time.intValue() == 5) {
      param += "&type=5min";
    }
    if (time.intValue() == 15) {
      param += "&type=15min";
    }
    if (time.intValue() == 30) {
      param += "&type=30min";
    }
    if (time.intValue() == 60) {
      param += "&type=1hour";
    }
    if (time.intValue() == 1440) {
      param += "&type=1day";
    }
    if (time.intValue() == 10080) {
      param += "&type=1week";
    }
    if (time.intValue() == 30000) {
      return;
    }
    if ("handcompute".equals(type)) {
      param += "&since=" + date.getTime();
    } else {
      param += "&size=1";
    }

    String send = HttpConnectionUtil.getSend(url, param);
    log.info(send);
    ArrayList<String[]> list = JSON.parseObject(send, new TypeReference<ArrayList<String[]>>() {
    });
    if (list.size() > 0) {
      String[] arr = list.get(0);

      // 获得当前时间 格式化到分钟数
      Date nowDate = date;
      String table =
          "TransactionOrder_" + website + "_" + currencyType + "_" + coinCode + "_" + time;
      RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
      List<TransactionOrder> orders = JSON
          .parseArray(redisService.get(table), TransactionOrder.class);
      if (orders == null) {
        orders = new ArrayList<TransactionOrder>();
      }
      // 获得时间区间的前区间
      Date minDate = DateUtil.dateAddMinute(nowDate, -time);
      if (time == 30000) {// 如果传的值为30000，表示是月周期，则前区间以当前时间向前推一个月
        minDate = DateUtil.addMonth(new Date(), -1);
      }
      // 如果数据库中没有当前结点,创建结点
      TransactionOrder transactionOrder = new TransactionOrder();
      transactionOrder.setId(DateUtil.dateToString(nowDate, "yyyyMMddHHmm"));
      transactionOrder.setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
      transactionOrder.setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));
      transactionOrder.setTransactionCount(new BigDecimal(arr[5]));
      transactionOrder.setMinPrice(new BigDecimal(arr[3]));
      transactionOrder.setMaxPrice(new BigDecimal(arr[2]));
      transactionOrder.setStartPrice(new BigDecimal(arr[1]));
      transactionOrder.setEndPrice(new BigDecimal(arr[4]));
      orders.add(0, transactionOrder);
    }
  }

  /**
   * 定时入口
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param time
   * @return: void
   * @Date : 2016年7月28日 下午2:49:44
   * @throws:
   */
  public static void generateKlineData(Integer time) {
    log.info("进入定时器-----" + time);
    String productListStr = ExchangeDataCache.getStringData("cn:productFixList");
    List<String> productList = null;
    if (!StringUtils.isEmpty(productListStr)) {
      productList = JSON.parseArray(productListStr, String.class);
    }

    if (productList != null) {
      for (String coinCode : productList) {
        compute2(time, "cn", "cny", coinCode);
      }
    }
  }

  /**
   * 用于同步火币最新的k线
   */
  private static void syncHuobiKline(int time) {
    ExCointoCoinService exCointoCoinService = (ExCointoCoinService) ContextUtil
        .getBean("exCointoCoinService");
    QueryFilter filter = new QueryFilter(ExCointoCoin.class);
    filter.addFilter("isSyncKline=", 1);
    List<ExCointoCoin> cointoCoins = exCointoCoinService.find(filter);
    // 这里从火币同步k线，不用采用算法计算
    if (cointoCoins != null) {
      for (ExCointoCoin cointoCoin : cointoCoins) {
        String coinCode = cointoCoin.getCoinCode() + "_" + cointoCoin.getFixPriceCoinCode();
        try {
          updateLastKLineByHuobi(coinCode, getPeriodStr(time));
        } catch (ApiException e) {
          log.error("从火币导入" + coinCode + "的k线失败", e);
        }
      }
    }
  }

  private static String getPeriodStr(int time) {
    switch (time) {
      case 1:
        return "1min";
      case 5:
        return "5min";
      case 15:
        return "15min";
      case 30:
        return "30min";
      case 60:
        return "60min";
      case 1440:
        return "1day";
      case 10080:
        return "1week";
      case 30000:
        return "1mon";
      case 525600:
        return "1year";
    }
    return "1min";
  }

  private static void updatePeriodLastKLineListBackups(String coinCode, String period,
      Kline huobiKline) {
    if (coinCode == null || period == null || huobiKline == null) {
      return;
    }
    String periodBackupTable = coinCode + ":PeriodLastKLineList_backups";
    Map<String, LastKLinePayload> periodMap = null;
    String periodBackupStr = ExchangeDataCache.getStringData(periodBackupTable);
    if (periodBackupStr != null) {
      periodMap = JSON
          .parseObject(periodBackupStr, new TypeReference<HashMap<String, LastKLinePayload>>() {
          });
    }

    if (periodMap != null) {
      if (periodMap.get(period) != null) {
        updateLastKLinePayloadByHuobi(periodMap.get(period), huobiKline);
      } else {
        LastKLinePayload lastKLinePayload = new LastKLinePayload();
        lastKLinePayload.setPeriod(period);
        updateLastKLinePayloadByHuobi(lastKLinePayload, huobiKline);
        periodMap.put(period, lastKLinePayload);
      }
    } else {
      periodMap = new HashMap<>();
      LastKLinePayload lastKLinePayload = new LastKLinePayload();
      lastKLinePayload.setPeriod(period);
      updateLastKLinePayloadByHuobi(lastKLinePayload, huobiKline);
      periodMap.put(period, lastKLinePayload);

    }
    ExchangeDataCache.setStringData(periodBackupTable, JSON.toJSONString(periodMap));
  }

  private static void updateLastKLineByHuobi(String coinCode, String period) {
    if (coinCode == null || period == null) {
      return;
    }

    Map<String, Date> periodDate = DateUtil.getPeriodDate2(new Date());
    String symbol = (coinCode.replace("_", "")).toLowerCase();
    // 获取最近的两条记录，最新的更新：PeriodLastKlineList，第二条用于更新：PeriodLastKLineList_backup
    String periodTable = coinCode + ":PeriodLastKLineList";
    List<LastKLinePayload> periodList = null;
    // 如果:PeriodLastKlineList没有更新，那么:PeriodLastKLineList_backup也不会被更新
    String periodListStr = ExchangeDataCache.getStringData(periodTable);
    if (periodListStr != null) {
      LastKLinePayload updateKlinePayload = null;
      periodList = JSON.parseArray(periodListStr, LastKLinePayload.class);
      if (periodList != null) {
        for (int i = 0; i < periodList.size(); i++) {
          LastKLinePayload kLinePayload = periodList.get(i);

          if (kLinePayload.getPeriod().equals(period)
              // 没有交易订单来刷新k线，就更新点
              && kLinePayload.getTime() * 100l < periodDate.get(period).getTime()) {
            updateKlinePayload = kLinePayload;
            break;
          }
        }
        if (updateKlinePayload != null) {
          List<Kline> huobiLastKline = getHuobiLastKline(symbol, period);
          updateLastKLinePayloadByHuobi(updateKlinePayload, huobiLastKline.get(0));
          ExchangeDataCache.setStringData(periodTable, JSON.toJSONString(periodList));
          updatePeriodLastKLineListBackups(coinCode, period, huobiLastKline.get(1));
        }
      }
    } else {
      periodList = new ArrayList<>();
      List<Kline> huobiLastKline = getHuobiLastKline(symbol, period);
      LastKLinePayload lastKLinePayload = new LastKLinePayload();
      lastKLinePayload.setPeriod(period);
      updateLastKLinePayloadByHuobi(lastKLinePayload, huobiLastKline.get(0));
      periodList.add(lastKLinePayload);
      ExchangeDataCache.setStringData(periodTable, JSON.toJSONString(periodList));
      updatePeriodLastKLineListBackups(coinCode, period, huobiLastKline.get(1));
    }

  }

  private static List<Kline> getHuobiLastKline(String symbol, String period) {
    ApiClient apiClient = (ApiClient) ContextUtil.getBean("huobiApiClient");
    KlineResponse huobiKline = null;
    List<Kline> klineList = null;
    try {
      huobiKline = apiClient.kline(symbol, period, String.valueOf(2));
      if (huobiKline == null || !(huobiKline.getStatus().equals("ok"))) {
        // status == error
        return null;
      }
      if (huobiKline.data instanceof List) {
        klineList = (List<Kline>) huobiKline.data;
      }
    } catch (ApiException e) {
      return null;
    }

    return klineList;
  }

  private static void updateLastKLinePayloadByHuobi(LastKLinePayload kLinePayload, Kline kline) {
    if (kLinePayload == null || kline == null) {
      return;
    }
    kLinePayload.setPriceOpen(kline.getOpen());
    kLinePayload.setPriceLast(kline.getClose());
    kLinePayload.setPriceHigh(kline.getHigh());
    kLinePayload.setPriceLow(kline.getLow());
    kLinePayload.setAmount(kline.getAmount());
    kLinePayload.setTime(kline.getId());
    kLinePayload.set_id(Long.valueOf(kline.getId()));
    kLinePayload.setCount(kline.getVol());
    Date date = new Date(kline.getId() * 1000L);
    Date endDate = getEndDate(date, kLinePayload.getPeriod());
    kLinePayload.setStartTime(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm "));
    kLinePayload.setEndTime(DateUtil.dateToString(endDate, "yyyy-MM-dd HH:mm "));
    if (kLinePayload.getPeriod().equals("1day")) {
      kLinePayload.setDayTotalDealAmount(kline.getAmount());
    }
  }

  private static Date getEndDate(Date startDate, String period) {
    if (startDate == null) {
      return null;
    }
    switch (period) {
      case "1min":
        return DateUtil.addMinToDate(startDate, 1);
      case "5min":
        return DateUtil.addMinToDate(startDate, 5);
      case "15min":
        return DateUtil.addMinToDate(startDate, 15);
      case "30min":
        return DateUtil.addMinToDate(startDate, 30);
      case "60min":
        return DateUtil.addMinToDate(startDate, 60);
      case "1day":
        return DateUtil.addDaysToDate(startDate, 1);
      case "1week":
        return DateUtil.addDaysToDate(startDate, 7);
      case "1mon":
        return DateUtil.addMonthsToDate(startDate, 1);
      case "1year":
        return DateUtil.addMonthsToDate(startDate, 12);
    }
    return null;
  }

  public static void main(String[] args) {

    okcoinKline("h", new Date(), 1, "cn", "cny", "BTC");

  }

  /**
   * 获取app_config配置
   * <p>
   * TODO
   * </p>
   *
   * @author: Zhang Lei
   * @param: @param type
   * @param: @return
   * @return: String
   * @Date : 2016年12月8日 上午10:50:01
   * @throws:
   */
  public static String getCnfigValue(String type) {
    RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
        .getBean("remoteAppConfigService");
    String value = remoteAppConfigService.getValueByKey(type);
    return value;
  }

  /**
   * 判断是否是周末
   */
  public static boolean isWeekend(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * <p>
   * 是否是开闭盘时间
   * </p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: boolean
   * @Date : 2016年9月21日 下午6:15:31
   * @throws:
   */
  public static boolean isOpenTrade(int time, Date date) {
    // 判断周末是否开盘
    String isWeekend = getCnfigValue("isWeekend");// 0 开启 1关闭
    if ("1".equals(isWeekend)) {// 关闭
      // 继续判断今天是否是周六或周日
      boolean flag = isWeekend(new Date());
      if (flag) {
        return false;
      }
    }

    // 计算是否是节假日
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String str = redisService.get("appholidayConfig");
    if (!StringUtils.isEmpty(str)) {
      // 判断否是节假日
      List<AppHolidayConfig> list = JSON.parseArray(str, AppHolidayConfig.class);
      if (list != null && list.size() > 0) {
        for (AppHolidayConfig ahc : list) {
          if (time == 1440) {// 日线
            // 5分钟=300秒(s)=300000毫秒(ms)
            Long t = date.getTime() - 300000l;
            // 因为是每天凌晨生成K线，所以判断日线生成的时候需注意：例如：生成24日数据，生成k线时已经为25日，所以需要往后退一段时间（5分钟）
            if (t > ahc.getBeginDate().getTime() && t < ahc.getEndDate().getTime()) {
              return false;
            }
          } else {
            if (date.getTime() > ahc.getBeginDate().getTime() && date.getTime() < ahc.getEndDate()
                .getTime()) {
              return false;
            }
          }
        }
      }
    }

    // 这样的计算时分，其他只计算节假日
    if (time == 1 || time == 5 || time == 15 || time == 30 || time == 60) {
      // 计算是否是开闭盘
      RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
          .getBean("remoteAppConfigService");
      String financeByKey = remoteAppConfigService.getFinanceByKey("openAndclosePlateTime");
      if (!org.springframework.util.StringUtils.isEmpty(financeByKey)) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String[] split = financeByKey.split(",");
        boolean flag = true;
        for (int i = 0; i < split.length; i++) {
          if (i % 2 == 0) {
            int h = new Integer(split[i].split(":")[0]).intValue();
            int m = new Integer(split[i].split(":")[1]).intValue();
            if (hours == h) {
              if (minutes < m) {
                flag = false;
              }
            }
            if (hours < h) {
              flag = false;
            }
          }
          if (i % 2 == 1) {
            int h = new Integer(split[i].split(":")[0]).intValue();
            int m = new Integer(split[i].split(":")[1]).intValue();

            if (hours == h) {
              if (minutes > m) {
                flag = false;
              }
            }
            if (hours > h) {
              flag = false;
            }
          }

          if (!flag) {
            return flag;
          }
        }

        return flag;
      } else {// 如果缓存为空 直接返回true 让K线正常执行
        return true;
      }
    } else {
      return true;
    }
  }

  /**
   * klineJob 定时器
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param:
   * @return: void
   * @Date : 2016年7月28日 下午2:40:26
   * @throws:
   */
  public static void klineJob() {
    ScheduleJob job1 = new ScheduleJob();
    job1.setBeanClass("com.mz.trade.kline.KlineEngine");
    job1.setMethodName("generateKlineData");
    Object[] object1 = {1};
    job1.setMethodArgs(object1);
    QuartzManager.addJob("klineData1", job1, QuartzJob.class, "0 0/1 * * * ?");

    ScheduleJob job5 = new ScheduleJob();
    job5.setBeanClass("com.mz.trade.kline.KlineEngine");
    job5.setMethodName("generateKlineData");
    Object[] object5 = {5};
    job5.setMethodArgs(object5);
    QuartzManager.addJob("klineData5", job5, QuartzJob.class, "0 0/5 * * * ?");

    ScheduleJob job15 = new ScheduleJob();
    job15.setBeanClass("com.mz.trade.kline.KlineEngine");
    job15.setMethodName("generateKlineData");
    Object[] object15 = {15};
    job15.setMethodArgs(object15);
    QuartzManager.addJob("klineData15", job15, QuartzJob.class, "0 0/15 * * * ?");

    ScheduleJob job30 = new ScheduleJob();
    job30.setBeanClass("com.mz.trade.kline.KlineEngine");
    job30.setMethodName("generateKlineData");
    Object[] object30 = {30};
    job30.setMethodArgs(object30);
    QuartzManager.addJob("klineData30", job30, QuartzJob.class, "0 0/30 * * * ?");

    ScheduleJob job60 = new ScheduleJob();
    job60.setBeanClass("com.mz.trade.kline.KlineEngine");
    job60.setMethodName("generateKlineData");
    Object[] object60 = {60};
    job60.setMethodArgs(object60);
    QuartzManager.addJob("klineData60", job60, QuartzJob.class, "0 0 0/1 * * ?");

    // 每天0点触发
    ScheduleJob job1440 = new ScheduleJob();
    job1440.setBeanClass("com.mz.trade.kline.KlineEngine");
    job1440.setMethodName("generateKlineData");
    Object[] object1440 = {1440};
    job1440.setMethodArgs(object1440);
    QuartzManager.addJob("klineData1440", job1440, QuartzJob.class, "0 0 0 * * ?");

    // 每周一触发
    ScheduleJob job10080 = new ScheduleJob();
    job10080.setBeanClass("com.mz.trade.kline.KlineEngine");
    job10080.setMethodName("generateKlineData");
    Object[] object10080 = {10080};
    job10080.setMethodArgs(object10080);
    QuartzManager.addJob("klineData10080", job10080, QuartzJob.class, "0 0 0 ? * MON");

    //每月1号0点触发
    ScheduleJob jobMon = new ScheduleJob();
    jobMon.setBeanClass("com.mz.trade.kline.KlineEngine");
    jobMon.setMethodName("generateKlineData");
    Object[] object30000 = {30000};
    jobMon.setMethodArgs(object30000);
    QuartzManager.addJob("klineData30000", jobMon, QuartzJob.class, "0 0 0 1 * ?");


  }

  /**
   * <p>
   * 手动修复数据
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param coinCode
   * @param: @param startDate
   * @param: @param endDate
   * @return: int 返回一共修复了多少条数据
   * @Date : 2016年9月14日 下午4:07:33
   * @throws:
   */
  public static int handRepairKline(String coinCode, Date startDate, Date endDate) {
    int total = 0;
    // 去properties抓取配置的站点数量
    Map<String, String> loadWeb = PropertiesUtils.getLoadWeb();// 站点数
    Set<Entry<String, String>> entrySet = loadWeb.entrySet();
    Iterator<Entry<String, String>> iterator = entrySet.iterator();
    while (iterator.hasNext()) {
      Entry<String, String> next = iterator.next();
      String website = next.getKey();
      String currencyType = next.getValue();

      // 获得所有的时间节点
      Map<String, List<String[]>> map = DateUtil.GetTimeInterval(startDate, endDate);
      Set<Entry<String, List<String[]>>> timeEntrySet = map.entrySet();
      Iterator<Entry<String, List<String[]>>> timeIterator = timeEntrySet.iterator();
      while (timeIterator.hasNext()) {
        Entry<String, List<String[]>> timeNext = timeIterator.next();
        List<String[]> list = timeNext.getValue();
        String key = timeNext.getKey();
        if ("1min".equals(key)) {
          int i = 1;
          for (String[] arr : list) {
            log.info("手动修复" + key + "————" + i + "-------" + arr[0] + "————" + arr[1]);
            i++;
            handcompute(DateUtil.stringToDate(arr[1]), 1, website, currencyType, coinCode);
          }
          total += i;
        } else if ("5min".equals(key)) {
          int i = 1;
          for (String[] arr : list) {
            log.info("手动修复" + key + "————" + i + "-------" + arr[0] + "————" + arr[1]);
            i++;
            handcompute(DateUtil.stringToDate(arr[1]), 5, website, currencyType, coinCode);
          }
          total += i;
        } else if ("15min".equals(key)) {
          int i = 1;
          for (String[] arr : list) {
            log.info("手动修复" + key + "————" + i + "-------" + arr[0] + "————" + arr[1]);
            i++;
            handcompute(DateUtil.stringToDate(arr[1]), 15, website, currencyType, coinCode);
          }
          total += i;
        } else if ("30min".equals(key)) {
          int i = 1;
          for (String[] arr : list) {
            log.info("手动修复" + key + "————" + i + "-------" + arr[0] + "————" + arr[1]);
            i++;
            handcompute(DateUtil.stringToDate(arr[1]), 30, website, currencyType, coinCode);
          }
          total += i;
        } else if ("60min".equals(key)) {
          int i = 1;
          for (String[] arr : list) {
            log.info("手动修复" + key + "————" + i + "-------" + arr[0] + "————" + arr[1]);
            i++;
            handcompute(DateUtil.stringToDate(arr[1]), 60, website, currencyType, coinCode);
          }
          total += i;
        }
      }

    }

    return total;
  }

  /**
   * 手动修复K线方法
   * <p>
   * TODO
   * </p>
   *
   * @author: Liu Shilei
   * @param: @param date
   * @param: @param time
   * @param: @param website
   * @param: @param currencyType
   * @param: @param coinCode
   * @return: void
   * @Date : 2016年9月14日 下午4:18:34
   * @throws:
   */
  public static void handcompute(Date date, Integer time, String website, String currencyType,
      String coinCode) {
    log.info(
        "触发手动" + time + "分钟k线数据生成器.............................." + DateUtil.dateToString(date));

    if ("theSeat"
        .equals(PropertiesUtils.APP.getProperty("app.differetCustomer"))) {// 如果是做市模式，就开启判断当前时间
      // 在不在
      // 开市时间
      boolean openTrade = isOpenTrade(time, date);
      if (!openTrade) {
        log.info("手动修复，不在开市时间，不修复" + DateUtil.dateToString(date));
        return;
      }
    }

    /**
     * 算法介绍 一、获得当前时间,记录当前时间到上一个时间的K线结点 如果当前此节点没有数据，就查最后一天交易记录所在的K线时间段内的结点记录
     *
     * 比如现在12:00，查11.59——12:00之间的数据，算出结点，
     * 如果11.59——12:00之间没有得出结点数据,则查询最后一条交易记录， 比如最后一条交易记录在10：45
     * 那么就查10:45所在的时间得出结点数据
     *
     * 二、时间结点定位 1分钟就是按每1分钟间隔 3分钟从0.00开始 3,6,9,12.......60 5分钟
     * 5,10,15......60 15分钟 15,30......60
     *
     *
     */
    // 获得当前时间 格式化到分钟数
    Date nowDate = date;
    String table = "TransactionOrder_" + website + "_" + currencyType + "_" + coinCode + "_" + time;
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    List<TransactionOrder> orders = JSON
        .parseArray(redisService.get(table), TransactionOrder.class);
    if (orders == null) {
      orders = new ArrayList<TransactionOrder>();
    }

    Date minDate = DateUtil.dateAddMinute(nowDate, -time);
    if (time == 30000) {// 如果传的值为30000，表示是月周期，则前区间以当前时间向前推一个月
      minDate = DateUtil.addMonth(new Date(), -1);
    }

    ExOrderDao exOrderDao = (ExOrderDao) ContextUtil.getBean("exOrderDao");
    Map<String, Object> query = new HashMap<String, Object>();
    query.put("minDate", DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
    query.put("maxDate", DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));
    query.put("website", website);
    query.put("currencyType", currencyType);
    String[] split = coinCode.split("_");
    query.put("coinCode", split[0]);
    query.put("fixPriceCoinCode", split[1]);
    List<TransactionOrder> list = exOrderDao.findOrder(query);
    // 如果两个时间差之间的数据不等于空
    if (list != null && list.size() > 0) {
      TransactionOrder exOrder = list.get(0);
      if (exOrder != null) {
        // 如果数据库中没有当前结点,创建结点
        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.setId(DateUtil.dateToString(date, "yyyyMMddHHmm"));
        transactionOrder.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
        transactionOrder.setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
        transactionOrder.setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));

        transactionOrder.setTransactionCount(exOrder.getTransactionCount());
        transactionOrder.setMinPrice(exOrder.getMinPrice());
        transactionOrder.setMaxPrice(exOrder.getMaxPrice());
        transactionOrder.setStartPrice(exOrder.getStartPrice());
        transactionOrder.setEndPrice(exOrder.getEndPrice());
        orders.add(0, transactionOrder);

      } else {// 如果当前时间结点数据为空,查询最近的一条交易记录,计算出所在的时间结点
        QueryFilter filter = new QueryFilter(ExOrder.class);
        filter.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
        filter.addFilter("website=", website);
        filter.addFilter("currencyType=", currencyType);
        filter.addFilter("coinCode=", coinCode);
        filter.setOrderby("transactionTime desc");
        Page<ExOrder> page = PageHelper.startPage(0, 1);

        ExOrderService exOrderService = (ExOrderService) ContextUtil.getBean("exOrderService");
        exOrderService.find(filter);
        List<ExOrder> result = page.getResult();
        if (result != null && result.size() > 0) {
          ExOrder exOrder2 = result.get(0);
          // 二次查询，查找最近一条交易订单的时间，计算出开始时间和结束时间区间值
          Date minDate2 = timeRevise(exOrder2.getTransactionTime(), time);
          Date maxDate2 = DateUtil.dateAddMinute(minDate2, time);

          Map<String, Object> query2 = new HashMap<String, Object>();
          query2.put("minDate", DateUtil.dateToString(minDate2, "yyyy-MM-dd HH:mm"));
          query2.put("maxDate", DateUtil.dateToString(maxDate2, "yyyy-MM-dd HH:mm"));
          query2.put("website", website);
          query2.put("currencyType", currencyType);
          query2.put("coinCode", split[0]);
          query2.put("fixPriceCoinCode", split[1]);
          List<TransactionOrder> list2 = exOrderDao.findOrder(query2);

          if (list2 != null && list2.size() > 0 && list2.get(0) != null) {
            TransactionOrder _transactionOrder2 = list2.get(0);
            // 判断这个前一个结点不为空,正常情况百分百不为空
            if (_transactionOrder2 != null) {
              // 如果数据库中没有当前结点,创建结点
              TransactionOrder transactionOrder2 = new TransactionOrder();
              transactionOrder2.setId(DateUtil.dateToString(date, "yyyyMMddHHmm"));
              transactionOrder2.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
              transactionOrder2
                  .setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
              transactionOrder2
                  .setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));

              transactionOrder2.setTransactionCount(new BigDecimal(0));
              // 由于当前时间段没有交易数据，所以全部设置为历史数据的收盘价
              transactionOrder2.setMinPrice(_transactionOrder2.getEndPrice());
              transactionOrder2.setMaxPrice(_transactionOrder2.getEndPrice());
              transactionOrder2.setStartPrice(_transactionOrder2.getEndPrice());
              transactionOrder2.setEndPrice(_transactionOrder2.getEndPrice());
              orders.add(0, transactionOrder2);
            }

          }
        } else {
          // 如果数据库中没有当前结点,创建结点
          TransactionOrder transactionOrder0 = new TransactionOrder();
          transactionOrder0.setId(DateUtil.dateToString(date, "yyyyMMddHHmm"));
          transactionOrder0.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
          transactionOrder0.setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
          transactionOrder0
              .setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));

          BigDecimal bigDecimal = new BigDecimal(0);
          transactionOrder0.setTransactionCount(bigDecimal);
          transactionOrder0.setMinPrice(bigDecimal);
          transactionOrder0.setMaxPrice(bigDecimal);
          transactionOrder0.setStartPrice(bigDecimal);
          transactionOrder0.setEndPrice(bigDecimal);
          orders.add(0, transactionOrder0);
        }

      }
    }
    redisService.save(table, JSON.toJSONString(orders));
  }

}
