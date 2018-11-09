package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.exchange.kline.model.TransactionOrder;
import com.mz.exchange.kline2.model.LastKLine;
import com.mz.exchange.kline2.model.LastKLinePayload;
import com.mz.exchange.kline2.model.MarketDetail;
import com.mz.exchange.kline2.model.MarketPayload;
import com.mz.exchange.kline2.model.MarketPayloadAsks;
import com.mz.exchange.kline2.model.MarketPayloadBids;
import com.mz.exchange.kline2.model.MarketPayloadTrades;
import com.mz.exchange.kline2.model.ReqKLine;
import com.mz.exchange.kline2.model.ReqKLinePayload;
import com.mz.exchange.kline2.model.ReqMsgSubscribe;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.date.DateUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.web.app.model.AppHolidayConfig;
import com.mz.core.constant.StringConstant;
import com.mz.front.kline.model.MarketDepths;
import com.mz.front.kline.model.MarketTrades;
import com.mz.front.kline.model.MarketTradesSub;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.Entrust;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.util.HttpConnectionUtil;
import com.mz.util.IpUtil;
import com.mz.util.SessionUtils;
import com.mz.util.SortList;
import com.mz.util.common.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/mobile/nouser/trades")
@Api(value = "App操作类", description = "交易大厅")
public class AppTradesController {

  private final static Logger log = Logger.getLogger(AppTradesController.class);

  // 默认查询1分钟K线周期
  private final int PERIOD = 1;
  //数据库中保留k线的数据条数
  public static final int HOLD_COUNT = 800;


  @RequestMapping(value = "/cancelExEntrust")
  @ApiOperation(value = "取消单个委托", httpMethod = "POST", response = JsonResult.class, notes =
      "entrustNums委托单号, entrustPrice 委托价格,   coinCode币的代码,"
          + "fixPriceCoinCode定价币  type1 买 ,2卖")
  @ResponseBody
  public JsonResult cancelExEntrust(HttpServletRequest request, @RequestParam String type,
      @RequestParam String entrustNums, @RequestParam String entrustPrice,
      @RequestParam String coinCode
      , @RequestParam String fixPriceCoinCode) {

    JsonResult jsonResult = new JsonResult();
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        //RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());

        EntrustTrade entrustTrade = new EntrustTrade();
        entrustTrade.setEntrustNum(entrustNums);
        if (coinCode.contains("-")) {
          entrustTrade.setCoinCode(coinCode.split("-")[0]);
        } else {
          entrustTrade.setCoinCode(coinCode);
        }

        entrustTrade.setType(Integer.valueOf(type));
        entrustTrade.setFixPriceCoinCode(fixPriceCoinCode);
        entrustTrade.setEntrustPrice(new BigDecimal(entrustPrice));
        remoteManageService.cancelExEntrust(entrustTrade);
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("revoke_success", "zh_CN"));
        return jsonResult;
      }
      {
        return new JsonResult().setSuccess(false).setMsg("用户不存在");
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/appCancelExEntrust")
  @ApiOperation(value = "取消单个委托", httpMethod = "POST", response = JsonResult.class, notes =
      "entrustNums委托单号, entrustPrice 委托价格,   coinCode币的代码,"
          + "fixPriceCoinCode定价币  type1 买 ,2卖")
  @ResponseBody
  public JsonResult cancelExEntrust(HttpServletRequest request) {
    String language = (String) request.getAttribute("language");

    JsonResult jsonResult = new JsonResult();
    User user = SessionUtils.getUser(request);
    //RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
    RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
        .getBean("remoteManageService");
    String entrustNums = request.getParameter("entrustNums").toString();
    String entrustPrice = request.getParameter("entrustPrice").toString();
    String coinCode = request.getParameter("coinCode");
    String[] split = coinCode.split("_");
    String type = request.getParameter("type");
    EntrustTrade entrustTrade = new EntrustTrade();
    entrustTrade.setEntrustNum(entrustNums);
    if (split[0].contains("-")) {
      String[] splitt = split[0].split("-");
      split[0] = splitt[0];
    }
    entrustTrade.setCoinCode(split[0]);
    entrustTrade.setType(Integer.valueOf(type));
    entrustTrade.setFixPriceCoinCode(split[1]);
    entrustTrade.setEntrustPrice(new BigDecimal(entrustPrice));
    remoteManageService.cancelExEntrust(entrustTrade);
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("revoke_success", "zh_CN"));
    return jsonResult;

  }

  @RequestMapping(value = "/cancelAllExEntrust")
  @ApiOperation(value = "取消用户所有的委托", httpMethod = "POST", response = JsonResult.class, notes = "登陆后即可调用")
  @ResponseBody
  public JsonResult cancelCustAllExEntrust(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        EntrustTrade entrustTrade = new EntrustTrade();
        entrustTrade.setCustomerId(user.getCustomerId());
        remoteManageService.cancelAllExEntrust(entrustTrade);
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("revoke_success", "zh_CN"));

      }
    } else {
      jsonResult.setSuccess(true);
      jsonResult.setMsg("请先登录");
    }

    return jsonResult;

  }


  @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "委托记录查询", httpMethod = "POST", response = JsonResult.class, notes =
      "type 0:全部 ,1 ：买 ,2 ：卖 "
          + "status 1: 2: 历史委托 ")
  @ResponseBody
  public JsonResult list(HttpServletRequest request) {

    String querypath = request.getParameter("querypath");
    // 交易类型
    String typeone = request.getParameter("typeone");

    String tokenId = request.getParameter("tokenId");

    String coinCode = request.getParameter("coinCode");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        RemoteManageService service = SpringContextUtil.getBean("remoteManageService");
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        if ("0".equals(typeone)) {
          params.put("typeone", null);
        }
        params.put("customerId", user.getCustomerId().toString());

        FrontPage findTrades = service.findEntrust(params);
        List<Entrust> list = findTrades.getRows();
        for (int i = 0; i < list.size(); i++) {
          if (coinCode != null) {
            if (coinCode.equals(list.get(i).getCoinCode())) {
              Entrust entrust = list.get(i);
              entrust.setCoin(list.get(i).getCoin());
              entrust.setEntrustTime_long(entrust.getEntrustTime().getTime());
              entrust
                  .setCoinCode(list.get(i).getCoinCode() + "-" + list.get(i).getFixPriceCoinCode());
            }
          } else {
            Entrust entrust = list.get(i);
            entrust.setCoin(list.get(i).getCoin());
            entrust.setEntrustTime_long(entrust.getEntrustTime().getTime());
            entrust
                .setCoinCode(list.get(i).getCoinCode() + "-" + list.get(i).getFixPriceCoinCode());
          }
        }
        return new JsonResult().setSuccess(true).setObj(findTrades);
      } else {
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
      }
    } else {
      return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }
  }


  @RequestMapping(value = "/appfindAllCoin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "发行的币种(JsonResult + obj)", httpMethod = "POST", response = Coin.class, notes = "")
  @ResponseBody
  public JsonResult appfindAllCoin() {
    List<Coin> list = new ArrayList<Coin>();
    try {
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult result = manageService.finaCoins();
      if (result.getSuccess()) {
        return new JsonResult().setSuccess(true).setObj(result.getObj());
      }
    } catch (Exception e) {
    }
    return new JsonResult().setSuccess(true).setObj(list);
  }


  @RequestMapping(value = "/appkLine", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "第一次加载K线", httpMethod = "POST", response = JsonResult.class, notes =
      "symbol 币的类型,例如：HRC_CNY，"
          + "period 时间段，例如：1min 5min")
  @ResponseBody
  public JsonResult appkLine(HttpServletRequest request, @RequestParam String symbol,
      @RequestParam String period) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    JsonResult js = new JsonResult();

    ReqMsgSubscribe reqMsgSubscribe = reqMsgSubscribe(request);
    ReqKLine reqKLine = reqKLine(request, symbol, period);
    map.put("reqMsgSubscribe", reqMsgSubscribe);
    map.put("reqKLine", reqKLine);

    js.setSuccess(true).setObj(map);
    return js;
  }

  /**
   * 定时推送消息
   */
  @RequestMapping(value = "/appmessage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "定时推送消息", httpMethod = "POST", response = JsonResult.class, notes = "theSeat 我也不知道干嘛用的")
  @ResponseBody
  public JsonResult appmessage(HttpServletRequest request, String theSeat) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    //闭盘就不推送数据了
    boolean openTrade = isOpenTrade(new Date());
    if (!openTrade) {
      return null;
    }
    HashMap<String, Object> map = new HashMap<String, Object>();
    // 去redis查询产品数量
    String productListStr = redisService.get(ContextUtil.getWebsite() + ":productFixList");
    if (!StringUtils.isEmpty(productListStr)) {
      List<String> productList = JSON.parseArray(productListStr, String.class);
      map.put("productList", productList);// put所有的产品数量

      Map<String, List<MarketDetail>> marketDetail = marketDetail(request, productList, theSeat);
      Map<String, List<LastKLine>> lastKLine = lastKLine(request, productList);
      map.put("lastKLine", lastKLine);
      map.put("marketDetail", marketDetail);
    }
    return new JsonResult().setSuccess(true).setObj(map);
  }


  @RequestMapping(value = "/appadd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "买/卖", httpMethod = "POST", response = JsonResult.class, notes =
      "entrustPrice委托价格,type 1 ：买 2 ：卖,coinCode币的代码,"
          + "customerId客户id,entrustCount委托数量")
  @ResponseBody
  public JsonResult appadd(EntrustTrade exEntrust, HttpServletRequest request) {

    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    String isTrade = parseObject.get("isTrade").toString();
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        if (isTrade == null || !"1".equals(isTrade)) {
          if (user.getStates() == 0) {
            return new JsonResult().setSuccess(false).setMsg("实名还未认证，请先实名");
          }
          if (user.getStates() == 1) {
            return new JsonResult().setSuccess(false).setMsg("实名正在审核，请等待！");
          }
          if (user.getStates() == 3) {
            return new JsonResult().setSuccess(false).setMsg("实名已拒绝请重新提交审核");
          }
        }
        if (user.getIsChange() == 1) {//判断是否禁止交易
          return new JsonResult().setSuccess(false).setMsg("您已被禁止交易");
        }
        exEntrust.setCustomerId(user.getCustomerId());

        JsonResult jsonResult = new JsonResult();
        // 委托之前判断
        String[] rtd = exEntrust.getCoinCode().split("_");
        if (rtd.length == 1) {
          jsonResult.setSuccess(false);
          jsonResult.setMsg(SpringContextUtil.diff("djb_is_null", "zh_CN"));
          return jsonResult;
        } else {
          exEntrust.setFixPriceCoinCode(rtd[1]);
          exEntrust.setCoinCode(rtd[0]);
        }

        exEntrust.setCustomerId(user.getCustomerId());
        String[] rt = remoteManageService.addEntrustCheck(exEntrust);
        if (rt[0].equals(Constant.CODE_FAILED)) {
          String[] str = rt[1].split("::");
          if (str.length > 1) {
            jsonResult.setSuccess(false);
            String str1 = SpringContextUtil.diff(str[1], "zh_CN");
            String str0 = SpringContextUtil.diff(str[0], "zh_CN");
            jsonResult.setMsg(str0 + str1);
          } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(SpringContextUtil.diff(rt[1], "zh_CN"));
          }
          return jsonResult;
        }
        exEntrust.setCustomerIp(IpUtil.getIp(request));
        exEntrust.setUserName(user.getMobile());
        exEntrust.setSource(3);
        //委托业务
        System.out.println(exEntrust);
        String[] relt = remoteManageService.addEntrust(exEntrust);
        if (relt[0].equals(Constant.CODE_SUCCESS)) {
          jsonResult.setSuccess(true);
          jsonResult.setMsg(SpringContextUtil.diff("delegate_success", "zh_CN"));
        } else {
          jsonResult.setSuccess(false);
          jsonResult.setMsg(SpringContextUtil.diff(relt[1], "zh_CN"));
        }
        return jsonResult;
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/appcancelExEntrust", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "撤销委托", httpMethod = "POST", response = JsonResult.class, notes =
      "entrustNums委托单号, entrustPrice 委托价格,   coinCode币的代码,"
          + "fixPriceCoinCode定价币")
  @ResponseBody
  public JsonResult appcancelExEntrust(HttpServletRequest request) {

    JsonResult jsonResult = new JsonResult();
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        //RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
        String entrustNums = request.getParameter("entrustNums").toString();
        String entrustPrice = request.getParameter("entrustPrice").toString();
        String coinCode = request.getParameter("coinCode");
        String fixPriceCoinCode = request.getParameter("fixPriceCoinCode");
        String type = request.getParameter("type");
        EntrustTrade entrustTrade = new EntrustTrade();
        entrustTrade.setEntrustNum(entrustNums);
        entrustTrade.setCoinCode(coinCode);
        entrustTrade.setType(Integer.valueOf(type));
        entrustTrade.setFixPriceCoinCode(fixPriceCoinCode);
        entrustTrade.setEntrustPrice(new BigDecimal(entrustPrice));
        remoteManageService.cancelExEntrust(entrustTrade);
        jsonResult.setSuccess(true);
        jsonResult.setMsg(SpringContextUtil.diff("revoke_success", "zh_CN"));
        return jsonResult;
      }
      {
        return new JsonResult().setSuccess(false).setMsg("用户不存在");
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  //**************************************************************************************************************************************//

  /**
   * 每秒推最后一条KLINE数据,8种 时间区间一次全推 查全部币种的每个时间区间 总条数为币种数*8个时间区间
   */
  public Map<String, List<LastKLine>> lastKLine(HttpServletRequest request,
      List<String> productList) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    // 获得当前时间所在的时间区间
    Map<String, Date> periodDate = DateUtil.getPeriodDate2(new Date());

    Map<String, List<LastKLine>> map = new HashMap<String, List<LastKLine>>();
    for (String coinCode : productList) {
      if ("true".equals(PropertiesUtils.APP.getProperty("app.okcoin"))) {
        if ("BTC".equals(coinCode) || "LTC".equals(coinCode)) {
          String url = "https://www.okcoin.cn/api/v1/ticker.do";
          if (ContextUtil.EN.equals(ContextUtil.getWebsite())) {
            url = "https://www.okcoin.com/api/v1/ticker.do";
          }
          String param = "symbol=";
          if ("BTC".equals(coinCode)) {
            param += "btc_" + ContextUtil.getCurrencyType();
          }
          if ("LTC".equals(coinCode)) {
            param += "ltc_" + ContextUtil.getCurrencyType();
          }
          String send = HttpConnectionUtil.getSend(url, param);
          JSONObject data = (JSONObject) JSON.parse(send);
          JSONObject ticker = (JSONObject) data.get("ticker");

          String last = (String) ticker.get("last");

					/*ExOrderInfo exOrderInfo = new ExOrderInfo();
					Long time = Long.valueOf((String) data.get("date"));
					exOrderInfo.setTransactionTime(new Date(time * 1000));
					exOrderInfo.setTransactionPrice(new BigDecimal(last));
					exOrderInfo.setTransactionCount(BigDecimal.ONE);
					exOrderInfo.setWebsite(ContextUtil.getWebsite());
					exOrderInfo.setCurrencyType(ContextUtil.getCurrencyType());
					exOrderInfo.setCoinCode(coinCode);
					remoteExOrderService.savePeriodLastKLineList(exOrderInfo);*/

        }
      }
      ArrayList<LastKLine> list = new ArrayList<LastKLine>();
      String periodLastKLineListStr = redisService.get(
          ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + coinCode
              + ":periodLastKLineList");
      if (!org.apache.commons.lang3.StringUtils.isEmpty(periodLastKLineListStr)) {
        List<LastKLinePayload> periodList = com.alibaba.fastjson.JSON
            .parseArray(periodLastKLineListStr, LastKLinePayload.class);
        if (periodList != null) {
          for (LastKLinePayload l : periodList) {
            l.setSymbolId(coinCode);
            LastKLine lastKLine = new LastKLine();
            lastKLine.setSymbolId(coinCode);
            // 以当前时间做最新时间区间
            // 价格可以用历史价格
            Long nowTime = periodDate.get(l.getPeriod()).getTime() / 1000;
            if (nowTime.compareTo(l.getTime())
                != 0) {// 如果redis中的最新成交时间和当前时间不相同，说明当前时间没有成交单，则当前成交数据用历史成交的收盘价
              l.set_id(nowTime);
              l.setTime(nowTime);
              l.setPriceHigh(l.getPriceLast());
              l.setPriceOpen(l.getPriceLast());
              l.setPriceLow(l.getPriceLast());
              l.setPriceLast(l.getPriceLast());
              l.setAmount(new BigDecimal(0));

              lastKLine.setPayload(l);
              list.add(lastKLine);
            } else {
              l.setTime(periodDate.get(l.getPeriod()).getTime() / 1000);
              lastKLine.setPayload(l);
              list.add(lastKLine);
            }
          }
        }
      }
      map.put(coinCode, list);
    }
    return map;
  }


  /**
   * 每秒推最新成交的数据, 推出所有币的成交记录
   *
   * @param theSeat 做市字段
   */
  public Map<String, List<MarketDetail>> marketDetail(HttpServletRequest request,
      List<String> productList, String theSeat) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String key = "pushEntrusMarket";
    // 如果是做市
    if ("theSeat".equals(theSeat)) {
      key = "pushtheSeatEntrustCenter";
    }

    Map<String, List<MarketDetail>> map = new HashMap<String, List<MarketDetail>>();
    for (String coinCode : productList) {
      ArrayList<MarketDetail> list = new ArrayList<MarketDetail>();
      for (int dep = 0; dep <= 5; dep++) {
        MarketDetail marketDetail = new MarketDetail();
        marketDetail.setMsgType("marketDetail" + dep);
        marketDetail.setSymbolId(coinCode);
        Random random = new Random();
        Date nowDate = new Date();
        String nowDateStr = DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm:ss");
        int intValue = Long.valueOf(DateUtil.stringToDate(nowDateStr).getTime() / 1000).intValue();
        marketDetail.set_id(intValue);
        marketDetail.setIdCur(intValue);

        MarketPayload payload = new MarketPayload();
        //payload.setYestdayPriceLast(yestdayPriceLast);

        // -----------------------------委托数据start------------------------------------
        // 委托卖
        MarketPayloadAsks marketPayloadAsks = new MarketPayloadAsks();
        BigDecimal[] aprices = null;
        BigDecimal[] aamounts = null;
        BigDecimal[] alevels = null;

        // 委托买
        MarketPayloadBids marketPayloadBids = new MarketPayloadBids();
        BigDecimal[] bprices = null;
        BigDecimal[] bamounts = null;
        BigDecimal[] blevels = null;

        String pushEntrusMarket;
        if (dep == 0) {
          pushEntrusMarket = redisService.get(
              ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + coinCode + ":"
                  + key);
        } else {
          pushEntrusMarket = redisService.get(
              ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + coinCode + ":"
                  + key + dep);
        }

        if (!StringUtils.isEmpty(pushEntrusMarket)) {
          // 获得委托数据
          MarketDepths marketDepths = com.alibaba.fastjson.JSON
              .parseObject(pushEntrusMarket, MarketDepths.class);

          Map<String, List<BigDecimal[]>> depths = marketDepths.getDepths();
          if (depths != null && !depths.isEmpty()) {
            List<BigDecimal[]> askslist = depths.get("asks");
            SortList<BigDecimal[]> sortList = new SortList<BigDecimal[]>();
            sortList.SortBigDecimalArray(askslist, 0, null);

            if (askslist != null && !askslist.isEmpty()) {

              aprices = new BigDecimal[askslist.size()];
              aamounts = new BigDecimal[askslist.size()];
              alevels = new BigDecimal[askslist.size()];

              for (int i = 0; i < askslist.size(); i++) {
                BigDecimal[] b = askslist.get(i);
                aprices[i] = b[0];
                aamounts[i] = b[1];
                alevels[i] = BigDecimal.ONE;
              }
            }

            List<BigDecimal[]> bidslist = depths.get("bids");
            if (bidslist != null && !bidslist.isEmpty()) {
              bprices = new BigDecimal[bidslist.size()];
              bamounts = new BigDecimal[bidslist.size()];
              blevels = new BigDecimal[bidslist.size()];

              for (int i = 0; i < bidslist.size(); i++) {
                BigDecimal[] b = bidslist.get(i);
                bprices[i] = b[0];
                bamounts[i] = b[1];
                blevels[i] = new BigDecimal(1);
              }

            }

          }
        }
        marketPayloadAsks.setPrice(aprices);
        marketPayloadAsks.setLevel(alevels);
        marketPayloadAsks.setAmount(aamounts);

        marketPayloadBids.setPrice(bprices);
        marketPayloadBids.setLevel(blevels);
        marketPayloadBids.setAmount(bamounts);
        // -----------------------------委托数据end------------------------------------

        MarketPayloadTrades marketPayloadTrades = new MarketPayloadTrades();
        BigDecimal[] prices = null;
        BigDecimal[] amounts = null;
        // 交易类型
        BigDecimal[] directions = null;
        Long[] times = null;

        // 获得交易数据
        String pushNewListRecordMarket = redisService.get(
            ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + coinCode
                + ":pushNewListRecordMarketDesc");
        BigDecimal priceNew = BigDecimal.ZERO;
        if (!StringUtils.isEmpty(pushNewListRecordMarket)) {
          MarketTrades marketTrades = com.alibaba.fastjson.JSON
              .parseObject(pushNewListRecordMarket, MarketTrades.class);
          // 最新价格
          if (marketTrades != null) {
            List<MarketTradesSub> trades = marketTrades.getTrades();
            if (trades != null && trades.size() > 0) {
              // 第一条为最新价格
              priceNew = trades.get(0).getPrice();

              prices = new BigDecimal[trades.size()];
              amounts = new BigDecimal[trades.size()];
              directions = new BigDecimal[trades.size()];
              times = new Long[trades.size()];

              for (int i = 0; i < trades.size(); i++) {
                MarketTradesSub marketTradesSub = trades.get(i);
                prices[i] = marketTradesSub.getPrice();
                amounts[i] = marketTradesSub.getAmount();
                times[i] = marketTradesSub.getTime();
                if ("buy".equals(marketTradesSub.getType())) {
                  directions[i] = new BigDecimal(2);//绿色
                } else {
                  directions[i] = new BigDecimal(1);//红色
                }
              }

            }
          }
        }

        marketPayloadTrades.setPrice(prices);
        marketPayloadTrades.setAmount(amounts);
        marketPayloadTrades.setDirection(directions);
        marketPayloadTrades.setTime(times);

        //获得每个时间段的最新价  最高价，开盘价，收盘价
        String periodLastKLineListStr = redisService.get(
            ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + coinCode
                + ":periodLastKLineList");
        //获得最新价
        String index = redisService.get(
            ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + coinCode
                + ":pushIndex");
        BigDecimal ts = null;
        if (!StringUtils.isEmpty(index)) {
          try {
            JSONObject jb = JSON.parseObject(index);
            JSONObject data = (JSONObject) jb.get("data");
            String transactionSum = (String) data.get("transactionCount");
            ts = new BigDecimal(transactionSum);
          } catch (Exception e) {
          }
        }
        if (!StringUtils.isEmpty(periodLastKLineListStr)) {
          List<LastKLinePayload> periodList = com.alibaba.fastjson.JSON
              .parseArray(periodLastKLineListStr, LastKLinePayload.class);
          if (periodList != null) {
            for (LastKLinePayload l : periodList) {
              if ("1day".equals(l.getPeriod())) {
                payload.setPriceOpen(l.getPriceOpen());
                payload.setPriceHigh(l.getPriceHigh());
                payload.setPriceLow(l.getPriceLow());
                payload.setPriceLast(l.getPriceLast());
                payload.setLevel(l.getPriceLast());
                payload.setAmount(ts);
                payload.setTotalAmount(l.getDayTotalDealAmount());
                break;
              }
            }
          }
        }
        payload.setPriceNew(priceNew);

        payload.setTotalVolume(new BigDecimal(random.nextInt(5000000)));
        payload.setAmp(null);

        payload.setAsks(marketPayloadAsks);
        payload.setBids(marketPayloadBids);
        payload.setTrades(marketPayloadTrades);

        //昨日收盘价
        String table =
            "TransactionOrder_" + ContextUtil.getWebsite() + "_" + ContextUtil.getCurrencyType()
                + "_" + coinCode + "_1440";
        List<TransactionOrder> orders = JSON
            .parseArray(redisService.get(table), TransactionOrder.class);
        if (orders != null && orders.size() > 0) {
          payload.setYestdayPriceLast(orders.get(0).getEndPrice());
        }
        marketDetail.setPayload(payload);
        list.add(marketDetail);

      }
      map.put(coinCode, list);
    }
    return map;

  }


  /**
   * <p>
   * 是否是开闭盘时间
   * </p>
   */
  public static boolean isOpenTrade(Date date) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String str = redisService.get("appholidayConfig");
    if (!StringUtils.isEmpty(str)) {
      // 判断否是节假日
      List<AppHolidayConfig> list = JSON.parseArray(str, AppHolidayConfig.class);
      if (list != null && list.size() > 0) {
        for (AppHolidayConfig ahc : list) {
          if (date.getTime() > ahc.getBeginDate().getTime() && date.getTime() < ahc.getEndDate()
              .getTime()) {
            return false;
          }
        }
      }
    }

    // 计算是否是开闭盘
    String financeByKey = "";
    String string = redisService.get(StringConstant.CONFIG_CACHE + ":financeConfig");
    JSONArray obj = JSON.parseArray(string);
    for (Object o : obj) {
      JSONObject oo = JSON.parseObject(o.toString());
      if (oo.getString("configkey").equals("openAndclosePlateTime")) {
        financeByKey = oo.getString("value");
      }
    }

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

  }


  /**
   * 返回con 中的一个对象
   */
  public ReqMsgSubscribe reqMsgSubscribe(HttpServletRequest request) {
    ReqMsgSubscribe reqMsgSubscribe = new ReqMsgSubscribe();
    // 设置请求时间
    reqMsgSubscribe.setRequestIndex(new Date().getTime() + "");
    return reqMsgSubscribe;
  }

  /**
   * @param symbol 获得币种
   * @param period 1分钟 5分钟 时间段
   */
  public ReqKLine reqKLine(HttpServletRequest request, String symbol, String period) {
    RedisService redisService = SpringContextUtil.getBean("redisService");

    ReqKLine reqKLine = new ReqKLine();
    // 设置请求时间
    reqKLine.setRequestIndex(new Date().getTime() + "");
    ReqKLinePayload reqKLinePayload = new ReqKLinePayload();

    reqKLinePayload.setSymbolId(symbol);

    int t = PERIOD;
    if (!StringUtils.isEmpty(period) && !"undefined".equals(period)) {
      reqKLinePayload.setPeriod(period);
    }
    if (!StringUtils.isEmpty(period)) {
      switch (period) {
        case "1min":
          t = 1;
          break;
        case "5min":
          t = 5;
          break;
        case "15min":
          t = 15;
          break;
        case "30min":
          t = 30;
          break;
        case "60min":
          t = 60;
          break;
        case "1day":
          t = 1440;
          break;
        case "1week":
          t = 10080;
          break; // 10080为分钟
        case "1mon":
          t = 30000;
          break; // 30000 只做为一个月的代号，没有实际意义
        default:
          t = PERIOD;
          break;
      }
    } else {
      period = "1min";
    }

    //最多查询最新的800条数据
    String website = ContextUtil.getWebsite();
    String currencyType = ContextUtil.getCurrencyType();
    String coinCode = symbol;
    String time = String.valueOf(t);
    //String  table="TransactionOrder_" + website + "_" + currencyType + "_" + coinCode + "_" + time;
    String table = coinCode + ":klinedata:TransactionOrder_" + coinCode + "_" + time;
    log.info(table);
    List<TransactionOrder> periodKData = JSON
        .parseArray(redisService.get(table), TransactionOrder.class);
    if (periodKData == null) {
      periodKData = new ArrayList<TransactionOrder>();
    }
    if (periodKData != null && periodKData.size() > HOLD_COUNT) {
      periodKData = periodKData.subList(0, HOLD_COUNT);
    }
    // 对800条数据按时间正序排序
    SortList<TransactionOrder> sort = new SortList<TransactionOrder>();
    if (periodKData != null && periodKData.size() > 0) {
      sort.Sort(periodKData, "getTransactionTime", "asc");
    }

    // 获得当前时间所在的时间区间
    Map<String, Date> periodDate = DateUtil.getPeriodDate(new Date());
    // 增加当前lastKline节点
    addKlineLastData(periodKData, periodDate, period, symbol);

    // 拼接时间参数
    Random random = new Random();
    // 时间
    Long[] times = new Long[periodKData.size()];
    // 开盘价
    BigDecimal[] priceOpen = new BigDecimal[periodKData.size()];
    // 最高价
    BigDecimal[] priceHigh = new BigDecimal[periodKData.size()];
    // 最低价
    BigDecimal[] priceLow = new BigDecimal[periodKData.size()];
    // 收盘价
    BigDecimal[] priceLast = new BigDecimal[periodKData.size()];
    // 成交量
    BigDecimal[] amount = new BigDecimal[periodKData.size()];
    BigDecimal[] volume = new BigDecimal[periodKData.size()];
    BigDecimal[] count = new BigDecimal[periodKData.size()];

    for (int i = 0; i < periodKData.size(); i++) {
      TransactionOrder transactionOrder = periodKData.get(i);
      if (transactionOrder != null && transactionOrder.getTransactionTime() != null) {
        times[i] = DateUtil.stringToDate(transactionOrder.getTransactionTime()).getTime() / 1000;
      } else {
        log.info("序列化" + transactionOrder.toString());
        log.info("时间" + transactionOrder.getTransactionTime());
        log.info("ID" + transactionOrder.getId());

        continue;
      }

      priceOpen[i] = transactionOrder.getStartPrice();
      priceHigh[i] = transactionOrder.getMaxPrice();
      priceLow[i] = transactionOrder.getMinPrice();
      priceLast[i] = transactionOrder.getEndPrice();
      amount[i] = transactionOrder.getTransactionCount();

      volume[i] = new BigDecimal(random.nextInt(6000000));
      count[i] = new BigDecimal(random.nextInt(1000));
    }

    reqKLinePayload.setTime(times);
    reqKLinePayload.setPriceOpen(priceOpen);
    reqKLinePayload.setPriceHigh(priceHigh);
    reqKLinePayload.setPriceLow(priceLow);
    reqKLinePayload.setPriceLast(priceLast);
    reqKLinePayload.setAmount(amount);
    reqKLinePayload.setVolume(volume);
    reqKLinePayload.setCount(count);

    reqKLine.setPayload(reqKLinePayload);
    return reqKLine;
  }


  /**
   * 增加当前时间的最新成交节点
   * <p>
   * TODO
   * </p>
   */
  public void addKlineLastData(List<TransactionOrder> periodKData, Map<String, Date> periodDate,
      String period, String symbol) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    TransactionOrder lastKline = new TransactionOrder();
    String periodLastKLineListStr = redisService.get(
        ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + symbol
            + ":periodLastKLineList");
    if (!org.apache.commons.lang3.StringUtils.isEmpty(periodLastKLineListStr)) {
      List<LastKLinePayload> periodList = com.alibaba.fastjson.JSON
          .parseArray(periodLastKLineListStr, LastKLinePayload.class);
      if (periodList != null) {
        for (LastKLinePayload l : periodList) {
          if (l.getPeriod().equals(period)) {
            lastKline.setTransactionTime(DateUtil.dateToString(periodDate.get(period)));
            lastKline.setStartPrice(l.getPriceOpen());
            lastKline.setMaxPrice(l.getPriceHigh());
            lastKline.setMinPrice(l.getPriceLow());
            lastKline.setEndPrice(l.getPriceLast());
            lastKline.setTransactionCount(l.getAmount());
            break;
          }
        }
      }
    } else {
      lastKline.setTransactionTime(DateUtil.dateToString(periodDate.get(period)));
      if (periodKData != null && periodKData.size() > 0) {
        TransactionOrder first = periodKData.get(0);
        lastKline.setStartPrice(first.getStartPrice());
        lastKline.setMaxPrice(first.getMaxPrice());
        lastKline.setMinPrice(first.getMinPrice());
        lastKline.setEndPrice(first.getEndPrice());
        lastKline.setTransactionCount(first.getTransactionCount());
      } else {
        BigDecimal zero = BigDecimal.ZERO;
        lastKline.setStartPrice(zero);
        lastKline.setMaxPrice(zero);
        lastKline.setMinPrice(zero);
        lastKline.setEndPrice(zero);
        lastKline.setTransactionCount(zero);
      }
    }
    if (periodKData != null) {
      periodKData.add(lastKline);
    }
  }

}
