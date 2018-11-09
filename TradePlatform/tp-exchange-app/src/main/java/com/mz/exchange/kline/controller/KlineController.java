/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年9月14日 下午3:56:56
 */
package com.mz.exchange.kline.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.exchange.kline.model.TransactionOrder;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.date.DateUtil;
import com.mz.util.http.HttpConnectionUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.trade.entrust.ExchangeDataCache;
import com.mz.trade.kline.KlineEngine;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 手动修复K线
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2016年9月14日 下午3:56:56
 */
@Controller
@RequestMapping("/kline")
public class KlineController {

  //清理K线数据，限制最多保留的数据条数
  public static final int LIMIT_COUNT = 2000;

  @InitBinder
  public void initBinder(ServletRequestDataBinder binder) {
    /**
     * 自动转换日期类型的字段格式
     */
    binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

    /**
     * 防止XSS攻击，并且带去左右空格功能
     */
    binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
  }


  @MethodName(name = "手动修复一段时间的K线")
  @RequestMapping(value = "/kline")
  @ResponseBody
  public JsonResult setaccountpw(HttpServletRequest request) {

    JsonResult jsonResult = new JsonResult();
    String startDateStr = request.getParameter("startDate");
    String coinCode = request.getParameter("coinCode");
    Date startDate = DateUtil.stringToDate(startDateStr);
    Date endDate = DateUtil.addDaysToDate(startDate, 1);
    int handRepairKline = KlineEngine.handRepairKline(coinCode, startDate, endDate);
    jsonResult.setSuccess(true);
    jsonResult.setMsg("成功修改" + handRepairKline + "条记录");
    return jsonResult;
  }

  @MethodName(name = "清空交易数据")
  @RequestMapping(value = "/clearinit")
  @ResponseBody
  public JsonResult clearinit(HttpServletRequest request) {

    JsonResult jsonResult = new JsonResult();

    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String str = redisService.get("cn:productFixList");
    if (!org.springframework.util.StringUtils.isEmpty(str)) {
      List<String> list = com.alibaba.fastjson.JSONArray.parseArray(str, String.class);
      if (list != null && list.size() > 0) {
        for (String coincode : list) {
          Set<String> keys = redisService.keys(coincode + ":");
          if (keys != null && keys.size() > 0) {
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
              redisService.delete(it.next());
            }
          }
        }
      }
    }

    jsonResult.setSuccess(true);
    return jsonResult;
  }


  @MethodName(name = "清理k线数据")
  @RequestMapping(value = "/clear")
  @ResponseBody
  public JsonResult clear(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();

    int[] times = {1, 5, 15, 30, 60, 1440, 10080};
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String table = "";
    Map<String, String> loadWeb = PropertiesUtils.getLoadWeb();// 站点数
    Set<Entry<String, String>> entrySet = loadWeb.entrySet();
    Iterator<Entry<String, String>> iterator = entrySet.iterator();
    while (iterator.hasNext()) {
      Entry<String, String> next = iterator.next();
      String website = next.getKey();
      String currencyType = next.getValue();
      String productListStr = ExchangeDataCache.getStringData(website + ":productList");
      if (!StringUtils.isEmpty(productListStr)) {
        List<String> productList = JSON.parseArray(productListStr, String.class);
        for (String coinCode : productList) {
          for (Integer time : times) {
            table =
                "TransactionOrder_" + website + "_" + currencyType + "_" + coinCode + "_" + time;
            List<Object> list = JSON.parseArray(redisService.get(table));
            //如果K线数据超过限制条数，删除多余的旧数据，保留最新的
            if (list != null && list.size() > LIMIT_COUNT) {
              list = list.subList(0, LIMIT_COUNT);
            }
            redisService.save(table, JSON.toJSONString(list));
          }
        }
      }
    }

    jsonResult.setSuccess(true);

    return jsonResult;

  }

  @MethodName(name = "重新加载okcoin数据")
  @RequestMapping(value = "/okcoin")
  @ResponseBody
  public JsonResult okcoin(HttpServletRequest request) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String table = "";
    JsonResult jsonResult = new JsonResult();
    if ("true".equals(PropertiesUtils.APP.getProperty("app.okcoin"))) {
      int[] times = {1, 5, 15, 30, 60, 1440, 10080};
      String[] websites = {"cn", "en"};
      String[] coins = {"BTC", "LTC"};
      String url = "https://www.okcoin.cn/api/v1/kline.do";
      for (String website : websites) {
        String currencyType = "cny";
        if ("en".equals(website)) {
          currencyType = "usd";
          url = "https://www.okcoin.com/api/v1/kline.do";
        }
        for (String coinCode : coins) {
          for (int time : times) {
            List<TransactionOrder> orders = new ArrayList<TransactionOrder>();
            table =
                "TransactionOrder_" + website + "_" + currencyType + "_" + coinCode + "_" + time;
            //第一步清空对应的mongoDB表
            redisService.save(table, "");
            //第二步初始化数据
            String param = "symbol=" + coinCode.toLowerCase() + "_" + currencyType;
            param += "&";
            String type = "";
            switch (time) {
              case 1:
                type = "1min";
                break;
              case 5:
                type = "5min";
                break;
              case 15:
                type = "15min";
                break;
              case 30:
                type = "30min";
                break;
              case 60:
                type = "1hour";
                break;
              case 1440:
                type = "1day";
                break;
              case 10080:
                type = "1week";
                break;
              default:
                break;
            }
            param += "type=" + type;
            param += "&size=500";
            String send = HttpConnectionUtil.getSend(url, param);

            ArrayList<String[]> list = JSON
                .parseObject(send, new TypeReference<ArrayList<String[]>>() {
                });
            if (list.size() > 0) {
              for (String[] arr : list) {
                //时间 格式化到分钟数
                Date nowDate = new Date(Long.valueOf(arr[0]));

                // 获得时间区间的前区间
                Date minDate = DateUtil.dateAddMinute(nowDate, -time);
                if (time == 30000) {// 如果传的值为30000，表示是月周期，则前区间以当前时间向前推一个月
                  minDate = DateUtil.addMonth(new Date(), -1);
                }
                // 如果数据库中没有当前结点,创建结点
                TransactionOrder transactionOrder = new TransactionOrder();
                transactionOrder.setId(DateUtil.dateToString(nowDate, "yyyyMMddHHmm"));
                transactionOrder
                    .setTransactionTime(DateUtil.dateToString(minDate, "yyyy-MM-dd HH:mm"));
                transactionOrder
                    .setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));
                transactionOrder.setTransactionCount(new BigDecimal(arr[5]));
                transactionOrder.setMinPrice(new BigDecimal(arr[3]));
                transactionOrder.setMaxPrice(new BigDecimal(arr[2]));
                transactionOrder.setStartPrice(new BigDecimal(arr[1]));
                transactionOrder.setEndPrice(new BigDecimal(arr[4]));
                orders.add(0, transactionOrder);
              }

            }
            redisService.save(table, JSON.toJSONString(orders));
          }
        }
      }
      jsonResult.setSuccess(true);
    }
    return jsonResult;
  }

}
