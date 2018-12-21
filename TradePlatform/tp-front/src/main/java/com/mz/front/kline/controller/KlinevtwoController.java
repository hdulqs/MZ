package com.mz.front.kline.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.web.app.model.AppHolidayConfig;
import com.mz.core.constant.StringConstant;
import com.mz.front.kline.model.MarketDepths;
import com.mz.front.kline.model.MarketTrades;
import com.mz.front.kline.model.MarketTradesSub;
import com.mz.manage.remote.RemoteBaseInfoService;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.Coin2;
import com.mz.manage.remote.model.User;
import com.mz.util.HttpConnectionUtil;
import com.mz.util.SortList;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

;

/**
 * 
 * <p>
 * TODO
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2016年7月27日 下午6:24:24
 */
@Controller
@RequestMapping("/klinevtwo")
public class KlinevtwoController {

	private final static Logger log = Logger.getLogger(KlinevtwoController.class);

	// 数据库中保留k线的数据条数
	public static final int HOLD_COUNT = 800;
	// 默认查询1分钟K线周期
	private final int PERIOD = 1;

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

	@Autowired
	private RedisService redisService;

	/**
	 * 第一次加载KLINE
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/con")
	@ResponseBody
	public HashMap<String, Object> con(HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ReqMsgSubscribe reqMsgSubscribe = reqMsgSubscribe(request);
		ReqKLine reqKLine = reqKLine(request);
		map.put("reqMsgSubscribe", reqMsgSubscribe);
		map.put("reqKLine", reqKLine);
		return map;
	}

	/**
	 * 定时推送消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/message")
	@ResponseBody
	public HashMap<String, Object> message(HttpServletRequest request) {
		
	
		
		// 闭盘就不推送数据了
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

			Map<String, List<MarketDetail>> marketDetail = marketDetail(request, productList);
			Map<String, List<LastKLine>> lastKLine = lastKLine(request, productList);
			map.put("lastKLine", lastKLine);
			map.put("marketDetail", marketDetail);
		}

		return map;
	}

	/**
	 * 
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
	public static boolean isOpenTrade(Date date) {
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String str = redisService.get("appholidayConfig");
		if (!StringUtils.isEmpty(str)) {
			// 判断否是节假日
			List<AppHolidayConfig> list = JSON.parseArray(str, AppHolidayConfig.class);
			if (list != null && list.size() > 0) {
				for (AppHolidayConfig ahc : list) {
					if (date.getTime() > ahc.getBeginDate().getTime() && date.getTime() < ahc.getEndDate().getTime()) {
						return false;
					}
				}
			}
		}

		// 计算是否是开闭盘
		String financeByKey = "";
		String string = redisService.get(StringConstant.CONFIG_CACHE + ":financeConfig");
		JSONArray obj = JSON.parseArray(string);
		if (obj != null) {
			for (Object o : obj) {
				JSONObject oo = JSON.parseObject(o.toString());
				if (oo.getString("configkey").equals("openAndclosePlateTime")) {
					financeByKey = oo.getString("value");
				}
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
	 * 
	 * @param request
	 * @return
	 */
	public ReqMsgSubscribe reqMsgSubscribe(HttpServletRequest request) {
		ReqMsgSubscribe reqMsgSubscribe = new ReqMsgSubscribe();
		// 设置请求时间
		reqMsgSubscribe.setRequestIndex(new Date().getTime() + "");
		return reqMsgSubscribe;
	}

	/**
	 * 从redis中获得 exProduct
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @return
	 * @return: ExProduct
	 * @Date : 2016年10月11日 下午6:17:59
	 * @throws:
	 */
	public Coin getRedisExProduct(String coinCode) {

		return null;
	}

	/**
	 * 获得钱的格式
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param
	 *             coinCode
	 * @param: @return
	 * @return: int
	 * @Date : 2016年10月11日 下午6:25:23
	 * @throws:
	 */
	public int getMoneyFormat(String coinCode) {
		// ExProduct ex = getRedisExProduct(coinCode);
		// if (ex != null) {
		// return ex.getKeepDecimalForCurrency();
		// }
		return 2;
	}

	/**
	 * 获得量(币)的格式
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param
	 *             coinCode
	 * @param: @return
	 * @return: int
	 * @Date : 2016年10月11日 下午6:26:18
	 * @throws:
	 */
	public int getCountFormat(String coinCode) {
		// ExProduct ex = getRedisExProduct(coinCode);
		// if (ex != null) {
		// return ex.getKeepDecimalForCoin();
		// }
		return 4;
	}

	/**
	 * 增加当前时间的最新成交节点
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Liu Shilei
	 * @param: @param
	 *             periodKData
	 * @return: void
	 * @Date : 2016年10月25日 下午4:47:52
	 * @throws:
	 */
	public void addKlineLastData(List<TransactionOrder> periodKData, Map<String, Date> periodDate, String period, String symbol) {
		
		
		TransactionOrder lastKline = new TransactionOrder();
		String periodLastKLineListStr = redisService.get(symbol + ":PeriodLastKLineList");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(periodLastKLineListStr)) {
			List<LastKLinePayload> periodList = com.alibaba.fastjson.JSON.parseArray(periodLastKLineListStr, LastKLinePayload.class);
			if (periodList != null) {
				for (LastKLinePayload l : periodList) {
					if (l.getPeriod().equals(period)) {
						
						Long nowTime = periodDate.get(l.getPeriod()).getTime() / 1000;
						if (nowTime.compareTo(l.getTime()) != 0) {//当前时间和最新缓存不在同一区间
							lastKline.setTransactionTime(DateUtil.dateToString(periodDate.get(period)));
							lastKline.setStartPrice(l.getPriceLast());
							lastKline.setMaxPrice(l.getPriceLast());
							lastKline.setMinPrice(l.getPriceLast());
							lastKline.setEndPrice(l.getPriceLast());
							lastKline.setTransactionCount(new BigDecimal(0));
							break;
						}else{//当前时间和最新缓存在同一区间

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

	/**
	 * 返回con 中的reqKLine对象
	 * 
	 * @param request
	 * @return 获取k线推送数据 第一次加载获取1分钟k线数据 默认分时和1分钟一样
	 */
	public ReqKLine reqKLine(HttpServletRequest request) {
		ReqKLine reqKLine = new ReqKLine();
		// 设置请求时间
		reqKLine.setRequestIndex(new Date().getTime() + "");
		ReqKLinePayload reqKLinePayload = new ReqKLinePayload();

		// 获得币种
		String symbol = request.getParameter("symbol");
		reqKLinePayload.setSymbolId(symbol);

		int t = PERIOD;
		String period = request.getParameter("period");
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

		// 最多查询最新的800条数据
		String coinCode = symbol;
		String time = String.valueOf(t);
		String table = coinCode+":klinedata:TransactionOrder_" + coinCode + "_" + time;
		log.info(table);
		List<TransactionOrder> periodKData = JSON.parseArray(redisService.get(table), TransactionOrder.class);
		if(periodKData==null){
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

	public Map<String, Date> getPeriodDate(Date date) {
		Map<String, Date> map = new HashMap<String, Date>();
		// "1min","5min","15min","30min","60min","1day","1week","1mon"
		map.put("1min", DateUtil.stringToDate(DateUtil.getFormatDateTime(date, "yyyy-MM-dd HH:mm")));
		// 当前时间的所在5分区间
		map.put("5min", DateUtil.getPeriodMin(date, 5));
		// 当前时间的所在15分区间
		map.put("15min", DateUtil.getPeriodMin(date, 15));
		// 当前时间的所在30分区间
		map.put("30min", DateUtil.getPeriodMin(date, 30));
		// 当前时间的所在的小时
		map.put("60min", DateUtil.stringToDate(DateUtil.getFormatDateTime(date, "yyyy-MM-dd HH")));
		// 当前时间的所在天
		map.put("1day", DateUtil.stringToDate(DateUtil.getFormatDateTime(date, "yyyy-MM-dd")));
		// 当前时间区间所在的星期一
		Calendar cweek = Calendar.getInstance(Locale.CHINA);
		cweek.setTime(date);
		cweek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cweek.set(Calendar.HOUR_OF_DAY, 0);
		cweek.set(Calendar.MINUTE, 0);
		map.put("1week", cweek.getTime());
		// 当前时间所在月的第一天
		Calendar cmon = Calendar.getInstance(Locale.CHINA);
		cmon.setTime(date);
		cmon.set(Calendar.DAY_OF_MONTH, 1);
		cmon.set(Calendar.HOUR_OF_DAY, 0);
		cmon.set(Calendar.MINUTE, 0);
		map.put("1mon", cmon.getTime());
		return map;
	}

	/**
	 * 每秒推最后一条KLINE数据,8种 时间区间一次全推 查全部币种的每个时间区间 总条数为币种数*8个时间区间
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, List<LastKLine>> lastKLine(HttpServletRequest request, List<String> productList) {


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

					/*
					 * ExOrderInfo exOrderInfo = new ExOrderInfo(); Long time =
					 * Long.valueOf((String) data.get("date"));
					 * exOrderInfo.setTransactionTime(new Date(time * 1000));
					 * exOrderInfo.setTransactionPrice(new BigDecimal(last));
					 * exOrderInfo.setTransactionCount(BigDecimal.ONE);
					 * exOrderInfo.setWebsite(ContextUtil.getWebsite());
					 * exOrderInfo.setCurrencyType(ContextUtil.getCurrencyType()
					 * ); exOrderInfo.setCoinCode(coinCode);
					 * remoteExOrderService.savePeriodLastKLineList(exOrderInfo)
					 * ;
					 */

				}
			}
			ArrayList<LastKLine> list = new ArrayList<LastKLine>();
			String periodLastKLineListStr = redisService.get(coinCode + ":PeriodLastKLineList");
			if (!org.apache.commons.lang3.StringUtils.isEmpty(periodLastKLineListStr)) {
				List<LastKLinePayload> periodList = com.alibaba.fastjson.JSON.parseArray(periodLastKLineListStr, LastKLinePayload.class);
				if (periodList != null) {
					for (LastKLinePayload l : periodList) {
						l.setSymbolId(coinCode);
						LastKLine lastKLine = new LastKLine();
						lastKLine.setSymbolId(coinCode);
						// 以当前时间做最新时间区间
						// 价格可以用历史价格
						Long nowTime = periodDate.get(l.getPeriod()).getTime() / 1000;
						if (nowTime.compareTo(l.getTime()) != 0) {// 如果redis中的最新成交时间和当前时间不相同，说明当前时间没有成交单，则当前成交数据用历史成交的收盘价
							l.set_id(nowTime);
							l.setTime(nowTime);
							l.setPriceHigh(l.getPriceLast());
							l.setPriceOpen(l.getPriceLast());
							l.setPriceLow(l.getPriceLast());
							l.setPriceLast(l.getPriceLast());
							l.setAmount(new BigDecimal(0));


						} else {
							l.setTime(periodDate.get(l.getPeriod()).getTime() / 1000);
						}


						list.add(lastKLine);
						lastKLine.setPayload(l);

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
	 * @param request
	 * @return
	 */
	public Map<String, List<MarketDetail>> marketDetail(HttpServletRequest request, List<String> productList) {


		/*获得usdt对人民币价格*/
		BigDecimal usdttormb = new BigDecimal(6.6);
		String financeConfig = redisService.get("configCache:financeConfig");
		if(!StringUtils.isEmpty(financeConfig)){
			JSONArray parseArray = JSON.parseArray(financeConfig);

			if(parseArray!=null){
				for(int i = 0 ; i<parseArray.size(); i++){
					JSONObject jsonObject = parseArray.getJSONObject(i);
					if("usdttormb".equals(jsonObject.getString("configkey"))){
						String value = jsonObject.getString("value");
						if(!StringUtils.isEmpty(value)){
							usdttormb = new BigDecimal(value);
						}
					}
				}
			}

		}

		// 做市字段
		String theSeat = request.getParameter("theSeat");
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


				// payload.setYestdayPriceLast(yestdayPriceLast);

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
					pushEntrusMarket = redisService.get(coinCode + ":" + key);
				} else {
					pushEntrusMarket = redisService.get( coinCode + ":" + key + dep);
				}

				if (!StringUtils.isEmpty(pushEntrusMarket)) {
					// 获得委托数据
					MarketDepths marketDepths = com.alibaba.fastjson.JSON.parseObject(pushEntrusMarket, MarketDepths.class);

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
				String pushNewListRecordMarket = redisService.get( coinCode + ":pushNewListRecordMarketDesc");
				BigDecimal priceNew = BigDecimal.ZERO;
				if (!StringUtils.isEmpty(pushNewListRecordMarket)) {
					MarketTrades marketTrades = com.alibaba.fastjson.JSON.parseObject(pushNewListRecordMarket, MarketTrades.class);
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
									directions[i] = new BigDecimal(2);// 绿色
								} else {
									directions[i] = new BigDecimal(1);// 红色
								}
							}

						}
					}
				}

				marketPayloadTrades.setPrice(prices);
				marketPayloadTrades.setAmount(amounts);
				marketPayloadTrades.setDirection(directions);
				marketPayloadTrades.setTime(times);

				// 获得每个时间段的最新价 最高价，开盘价，收盘价
				String periodLastKLineListStr = redisService.get( coinCode + ":PeriodLastKLineList");
				// 获得最新价
				String index = redisService.get(ContextUtil.getWebsite() + ":" + ContextUtil.getCurrencyType() + ":" + coinCode + ":pushIndex");
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
					List<LastKLinePayload> periodList = com.alibaba.fastjson.JSON.parseArray(periodLastKLineListStr, LastKLinePayload.class);
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

				/*转成人民币价格*/
				payload.setUsdtToRmb(usdttormb);
				String fixPriceCoinCode = coinCode.split("_")[1];
				if("USDT".equals(fixPriceCoinCode)){
					payload.setUsdtCount(payload.getPriceNew());
				}else{
					//如果当前币对usdt有价格
					String usdtprice = redisService.get(fixPriceCoinCode + "_USDT" +":currentExchangPrice");
					if(!StringUtils.isEmpty(usdtprice)){
						payload.setUsdtCount(payload.getPriceNew().multiply(new BigDecimal(usdtprice)));
					}else{
						payload.setUsdtCount(new BigDecimal(0));
					}
				}

				// 昨日收盘价
				String table = coinCode+":klinedata:TransactionOrder_" + coinCode + "_1440";
				List<TransactionOrder> orders = JSON.parseArray(redisService.get(table), TransactionOrder.class);
				if (orders != null && orders.size() > 0) {
					if(orders.get(0).getEndPrice().compareTo(BigDecimal.ZERO)==0){
						payload.setYestdayPriceLast(new BigDecimal(1));
						
						String coinStr = redisService.get("cn:coinInfoList2");
						if(!StringUtils.isEmpty(coinStr)){
							List<Coin2> coins = JSON.parseArray(coinStr, Coin2.class);
							for(Coin2 c :coins){
								if(coinCode.equals(c.getCoinCode()+"_"+c.getFixPriceCoinCode())){
									if(!StringUtils.isEmpty(c.getYesterdayPrice())){
										payload.setYestdayPriceLast(new BigDecimal(c.getYesterdayPrice()));
									}else{
										payload.setYestdayPriceLast(new BigDecimal(1));
									}
								}
							}
						}
						
					}else{
						payload.setYestdayPriceLast(orders.get(0).getEndPrice());
					}
				}else{
					
					String coinStr = redisService.get("cn:coinInfoList2");
					if(!StringUtils.isEmpty(coinStr)){
						List<Coin> coins = JSON.parseArray(coinStr, Coin.class);
						for(Coin c :coins){
							if(coinCode.equals(c.getCoinCode()+"_"+c.getFixPriceCoinCode())){
								if(!StringUtils.isEmpty(c.getYesterdayPrice())){
									payload.setYestdayPriceLast(new BigDecimal(c.getYesterdayPrice()));
								}else{
									payload.setYestdayPriceLast(new BigDecimal(1));
								}
							}
						}
					}
					
				}
				marketDetail.setPayload(payload);
				list.add(marketDetail);

			}
			map.put(coinCode, list);
		}
		return map;

	}

	// 首页数据推送
	@RequestMapping(value = "/index")
	@ResponseBody
	public List<JSONObject> index(HttpServletRequest request) {
		Locale locale = LocaleContextHolder.getLocale();
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		// 去redis查询产品数量
		String productListStr = redisService.get("cn:coinInfoList");
		if (!StringUtils.isEmpty(productListStr)) {
			List<Coin> productList = JSON.parseArray(productListStr, Coin.class);

			for (Coin coin : productList) {
				JSONObject data = new JSONObject();

				//交易币的位数
				Integer keepCoin =  coin.getKeepDecimalForCoin();
				//定价币的位数
				Integer keepCurrency = coin.getKeepDecimalForCurrency();
				int  zeroLength = 2;
				//交易币的格式化
				String keepCoinFormat = "0.00";
				//定价币的格式化
				String keepCurrencyFormat = "0.00";
				if(keepCoin>zeroLength){
					keepCoinFormat = "0.";
					for(int i =1 ;i<=keepCoin ;i++){
						keepCoinFormat = keepCoinFormat += "0";
					}
				}
				if(keepCurrency>zeroLength){
					keepCurrencyFormat = "0.";
					for(int i =1 ;i<=keepCurrency ;i++){
						keepCurrencyFormat = keepCurrencyFormat += "0";
					}
				}
				DecimalFormat decimalFormatCoin = new DecimalFormat(keepCoinFormat);
				DecimalFormat decimalFormatCurrency = new DecimalFormat(keepCurrencyFormat);

				data.put("coinCode", coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
				if(locale.toString()==null){
					data.put("name", coin.getName()+coin.getCoinCode());
				}else{
					if("zh_CN".equals(locale.toString())){
						data.put("name",coin.getCoinCode());
					}else{
						data.put("name", coin.getCoinCode());
					}
				}
				data.put("picturePath", coin.getPicturePath());

				String currentExchangPrice_str = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()+":currentExchangPrice");
				if(!StringUtils.isEmpty(currentExchangPrice_str)){
					data.put("currentExchangPrice",decimalFormatCurrency.format(new BigDecimal(currentExchangPrice_str)));
				}else{
					data.put("currentExchangPrice",0);
				}

				// 昨日收盘价
				String coinStr = redisService.get("cn:coinInfoList2");
				String coinCode = coin.getCoinCode() + "_" + coin.getFixPriceCoinCode();
				BigDecimal yesterdayPrice = new BigDecimal(0);
				if(!StringUtils.isEmpty(coinStr)){
					List<Coin2> coins = JSON.parseArray(coinStr, Coin2.class);
					for(Coin2 c :coins){
						if(coinCode.equals(c.getCoinCode()+"_"+c.getFixPriceCoinCode())){
							if(!StringUtils.isEmpty(c.getYesterdayPrice())){
								yesterdayPrice = new BigDecimal(c.getYesterdayPrice());
							}
						}
					}
				}
				data.put("yesterdayPrice", decimalFormatCurrency.format(yesterdayPrice));

				String str = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()+":PeriodLastKLineList");
				if(str!=null){
					JSONArray jsonv = JSON.parseArray(str);
					//System.out.println(jsonv.getString(5));
					if(jsonv.getString(5)!=null){
						JSONObject jsonv_ = JSON.parseObject(jsonv.getString(5));
						if("1day".equals(jsonv_.getString("period"))){

							BigDecimal currentExchangPrice = new BigDecimal(0);
							//上一笔交易价格
							String orders = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()+":pushNewListRecordMarketDesc");
							if(!StringUtils.isEmpty(orders)){
								MarketTrades marketTrades = com.alibaba.fastjson.JSON.parseObject(orders, MarketTrades.class);
								// 最新价格
								if (marketTrades != null) {
									List<MarketTradesSub> trades = marketTrades.getTrades();
									if(trades!=null){
										if(trades.size()>1){
											MarketTradesSub marketTradesSub0 = trades.get(0);
											data.put("currentExchangPrice", decimalFormatCurrency.format(marketTradesSub0.getPrice()));
											currentExchangPrice = marketTradesSub0.getPrice();

											MarketTradesSub marketTradesSub1 = trades.get(1);
											data.put("lastExchangPrice",decimalFormatCurrency.format(marketTradesSub1.getPrice()));
										}else{

											MarketTradesSub marketTradesSub0 = trades.get(0);
											data.put("currentExchangPrice", decimalFormatCurrency.format(marketTradesSub0.getPrice()));
											currentExchangPrice = marketTradesSub0.getPrice();

											data.put("lastExchangPrice",decimalFormatCurrency.format(marketTradesSub0.getPrice()));
										}
									}else{
										data.put("lastExchangPrice","1");
									}
								}else{
									data.put("lastExchangPrice","1");
								}
							}else{
								data.put("lastExchangPrice","1");
							}
							//当日成交总量
							data.put("transactionSum",decimalFormatCoin.format(new BigDecimal(jsonv_.getString("amount"))));
//							//当日成交总额
//							data.put("transactionSum",jsonv_.getString("dayTotalDealAmount"));

							//24小时成交总额
//							String volstr =  redisService.get( coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()+":new24hoursvol");
//							BigDecimal transactionSum = new BigDecimal(0);
//							if(!StringUtils.isEmpty(volstr)){
//								transactionSum = new BigDecimal(volstr);
//							}
//							data.put("transactionSum",transactionSum.setScale(4, BigDecimal.ROUND_HALF_DOWN));



							data.put("maxPrice",decimalFormatCurrency.format(new BigDecimal(jsonv_.getString("priceHigh"))));
							data.put("minPrice",decimalFormatCurrency.format(new BigDecimal(jsonv_.getString("priceLow"))));
							// 开盘价
							data.put("openPrice", decimalFormatCurrency.format(new BigDecimal(jsonv_.getString("priceOpen"))));

							if(BigDecimal.ZERO.compareTo(yesterdayPrice)!=0){
								if(BigDecimal.ZERO.compareTo(currentExchangPrice)!=0){
									BigDecimal divide = (currentExchangPrice.subtract(yesterdayPrice)).divide(yesterdayPrice,5,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
									data.put("RiseAndFall",divide);
								}else{
									data.put("RiseAndFall",0);
								}
							}else{
								data.put("RiseAndFall",0);
							}


						}else{
							data.put("lastExchangPrice",0);
							data.put("transactionSum",0);
							data.put("maxPrice",0);
							data.put("minPrice",0);
							// 开盘价
							data.put("openPrice", new BigDecimal(0));
							data.put("lastEndPrice", 0);
							data.put("RiseAndFall", 0);
						}
					}else{
						data.put("lastExchangPrice",0);
						data.put("transactionSum",0);
						data.put("maxPrice",0);
						data.put("minPrice",0);
						// 开盘价
						data.put("openPrice", new BigDecimal(0));

						data.put("lastEndPrice", 0);
						data.put("RiseAndFall", 0);
					}
				}else{
					data.put("lastExchangPrice",0);
					data.put("transactionSum",0);
					data.put("maxPrice",0);
					data.put("minPrice",0);
					// 开盘价
					data.put("openPrice", new BigDecimal(0));

					data.put("lastEndPrice", 0);
					data.put("RiseAndFall", 0);
				}
				list.add(data);

			}
		}
		return list;
	}
	public static void main(String[] args) {
		BigDecimal b = new BigDecimal("1.02");
		BigDecimal a = new BigDecimal("1.01");
		System.out.println(b.divide(a,4,BigDecimal.ROUND_HALF_DOWN));
	}


	/**
	 * 新首页数据推送  view_v1版本
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/indexv1")
	@ResponseBody
	public JSONArray indexv1(HttpServletRequest request) {

		/*获得usdt对人民币价格*/
		BigDecimal usdttormb = new BigDecimal(1);
		String financeConfig = redisService.get("configCache:financeConfig");
		if(!StringUtils.isEmpty(financeConfig)){
			JSONArray parseArray = JSON.parseArray(financeConfig);

			if(parseArray!=null){
				for(int i = 0 ; i<parseArray.size(); i++){
					JSONObject jsonObject = parseArray.getJSONObject(i);
					if("usdttormb".equals(jsonObject.getString("configkey"))){
						String value = jsonObject.getString("value");
						if(!StringUtils.isEmpty(value)){
							usdttormb = new BigDecimal(value);
						}
					}
				}
			}

		}

		JSONArray jsonArray = new JSONArray();

		// 去redis查询产品数量
		String productListStr = redisService.get("cn:coinInfoList");
		if (!StringUtils.isEmpty(productListStr)) {
			List<Coin> productList = JSON.parseArray(productListStr, Coin.class);

			//统计交易区数量
			Set<String> coinarea = new LinkedHashSet<String>();
			//查询用户自选区
			User user = (User) request.getSession().getAttribute("user");
			//用户自选交易对
			List<String> mycoins = null;
			if(user!=null){
				RemoteBaseInfoService remoteBaseInfoService = SpringContextUtil.getBean("remoteBaseInfoService");
				mycoins = remoteBaseInfoService.findCustomerCollection(user.getCustomerId());
				JSONObject obj = new JSONObject();
				obj.put("areaname", "mycollection");
				obj.put("areanameview", SpringContextUtil.diff("zixuan"));
				
				//创建list
				ArrayList<JSONObject> list = new ArrayList<JSONObject>();
				if(mycoins!=null&&mycoins.size()>0){
					for (String coinCode : mycoins) {
						//遍历
						for (Coin coin : productList) {
							if(coinCode.equals(coin.getCoinCode()+"_"+coin.getFixPriceCoinCode())){
								JSONObject data = createData(coin);
								data.put("usdttormb", usdttormb);
								data.put("isShoucang", true);
								list.add(data);
							}
						}
					}
				}
				
				obj.put("data", list);
				//加入map中
				jsonArray.add(obj);
			}
			//增加交易区
			for (Coin coin : productList) {
				coinarea.add(coin.getFixPriceCoinCode());
			}
			
			if(coinarea.size()>0){
				Iterator<String> it = coinarea.iterator();
				while (it.hasNext()) {
					String areaname = it.next();

					JSONObject obj = new JSONObject();
					obj.put("areaname", areaname);
					obj.put("areanameview", areaname+"  "+SpringContextUtil.diff("jiaoyiqu"));
					
					//创建list
					ArrayList<JSONObject> list = new ArrayList<JSONObject>();
					//遍历
					for (Coin coin : productList) {
						if(areaname.equals(coin.getFixPriceCoinCode())){
							JSONObject data = createData(coin);
                            data.put("usdttormb", usdttormb);
							if(mycoins!=null&&mycoins.size()>0){
								for(String coinCode : mycoins){
									if(coinCode.equals(coin.getCoinCode()+"_"+coin.getFixPriceCoinCode())){
										data.put("isShoucang", true);
									}
								}
							}
							
							list.add(data);
						}
					}
					obj.put("data", list);
					//加入map中
					jsonArray.add(obj);
				}
			}
				
			
		}
		return jsonArray;
	}
	
	/**
	 * 创建交易区的一个交易对的data
	 * @return
	 */
	private JSONObject createData(Coin coin ){


		//交易币的位数
		Integer keepCoin =  coin.getKeepDecimalForCoin();
		//定价币的位数
		Integer keepCurrency = coin.getKeepDecimalForCurrency();
		int  zeroLength = 2;
		//交易币的格式化
		String keepCoinFormat = "0.00";
		//定价币的格式化
		String keepCurrencyFormat = "0.00";
		if(keepCoin>zeroLength){
			keepCoinFormat = "0.";
			for(int i =1 ;i<=keepCoin ;i++){
				keepCoinFormat = keepCoinFormat += "0";
			}
		}
		if(keepCurrency>zeroLength){
			keepCurrencyFormat = "0.";
			for(int i =1 ;i<=keepCurrency ;i++){
				keepCurrencyFormat = keepCurrencyFormat += "0";
			}
		}
		DecimalFormat decimalFormatCoin = new DecimalFormat(keepCoinFormat);
		DecimalFormat decimalFormatCurrency = new DecimalFormat(keepCurrencyFormat);
		
		JSONObject data = new JSONObject();
		data.put("coinCode", coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
		data.put("name",  coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
		data.put("picturePath", coin.getPicturePath());
		
		String currentExchangPrice_str = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()+":currentExchangPrice");
		if(!StringUtils.isEmpty(currentExchangPrice_str)){
			data.put("currentExchangPrice",decimalFormatCurrency.format(new BigDecimal(currentExchangPrice_str)));
			if("USDT".equals(coin.getFixPriceCoinCode())){
				data.put("usdtcount", new BigDecimal(currentExchangPrice_str));
			}else{
				//如果当前币对usdt有价格
				String usdtprice = redisService.get(coin.getFixPriceCoinCode() + "_USDT" +":currentExchangPrice");
				if(!StringUtils.isEmpty(usdtprice)){
					data.put("usdtcount", new BigDecimal(currentExchangPrice_str).multiply(new BigDecimal(usdtprice)));
				}else{
					data.put("usdtcount", 0);
				}
			}
		}else{
			data.put("usdtcount", 0);
			data.put("currentExchangPrice",0);
		}
		
		// 昨日收盘价
		String coinStr = redisService.get("cn:coinInfoList2");
		String coinCode = coin.getCoinCode() + "_" + coin.getFixPriceCoinCode();
		BigDecimal yesterdayPrice = new BigDecimal(0);
		if(!StringUtils.isEmpty(coinStr)){
			List<Coin2> coins = JSON.parseArray(coinStr, Coin2.class);
			for(Coin2 c :coins){
				if(coinCode.equals(c.getCoinCode()+"_"+c.getFixPriceCoinCode())){
					if(!StringUtils.isEmpty(c.getYesterdayPrice())){
						yesterdayPrice = new BigDecimal(c.getYesterdayPrice());
					}
				}
			}
		}
		data.put("yesterdayPrice", decimalFormatCurrency.format(yesterdayPrice));
		
		String str = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()+":PeriodLastKLineList");
		if(str!=null){
			JSONArray jsonv = JSON.parseArray(str);
			//System.out.println(jsonv.getString(5));
			if(jsonv.getString(5)!=null){
				JSONObject jsonv_ = JSON.parseObject(jsonv.getString(5));
				if("1day".equals(jsonv_.getString("period"))){
					
					BigDecimal currentExchangPrice = new BigDecimal(0);
					//上一笔交易价格
					String orders = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()+":pushNewListRecordMarketDesc");
					if(!StringUtils.isEmpty(orders)){
						MarketTrades marketTrades = com.alibaba.fastjson.JSON.parseObject(orders, MarketTrades.class);
						// 最新价格
						if (marketTrades != null) {
							List<MarketTradesSub> trades = marketTrades.getTrades();
							if(trades!=null && trades.size() > 0){
								if(trades.size()>1){
									MarketTradesSub marketTradesSub0 = trades.get(0);
									data.put("currentExchangPrice", decimalFormatCurrency.format(marketTradesSub0.getPrice()));
									currentExchangPrice = marketTradesSub0.getPrice();
									
									MarketTradesSub marketTradesSub1 = trades.get(1);
									data.put("lastExchangPrice",decimalFormatCurrency.format(marketTradesSub1.getPrice()));
								}else{

									MarketTradesSub marketTradesSub0 = trades.get(0);
									data.put("currentExchangPrice", decimalFormatCurrency.format(marketTradesSub0.getPrice()));
									currentExchangPrice = marketTradesSub0.getPrice();
									
									data.put("lastExchangPrice",decimalFormatCurrency.format(marketTradesSub0.getPrice()));
								}
							}else{
								data.put("lastExchangPrice","1");
							}
						}else{
							data.put("lastExchangPrice","1");
						}
					}else{
						data.put("lastExchangPrice","1");
					}
					//当日成交总量
					data.put("transactionSum",decimalFormatCoin.format(new BigDecimal(jsonv_.getString("amount"))));
					
					data.put("maxPrice",jsonv_.getString("priceHigh"));
					data.put("minPrice",jsonv_.getString("priceLow"));
					// 开盘价
					data.put("openPrice", decimalFormatCurrency.format(new BigDecimal(jsonv_.getString("priceOpen"))));
					
					if(BigDecimal.ZERO.compareTo(yesterdayPrice)!=0){
						if(BigDecimal.ZERO.compareTo(currentExchangPrice)!=0){
							BigDecimal divide = (currentExchangPrice.subtract(yesterdayPrice)).divide(yesterdayPrice,5,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
							data.put("RiseAndFall",divide);
						}else{
							data.put("RiseAndFall",0);
						}
					}else{
						data.put("RiseAndFall",0);
					}
						
					
				}else{
					data.put("lastExchangPrice",0);
					data.put("transactionSum",0);
					data.put("maxPrice",0);
					data.put("minPrice",0);
					// 开盘价
					data.put("openPrice", new BigDecimal(0));
					data.put("lastEndPrice", 0);
					data.put("RiseAndFall", 0);
				}
			}else{
				data.put("lastExchangPrice",0);
				data.put("transactionSum",0);
				data.put("maxPrice",0);
				data.put("minPrice",0);
				// 开盘价
				data.put("openPrice", new BigDecimal(0));

				data.put("lastEndPrice", 0);
				data.put("RiseAndFall", 0);
			}
		}else{
			data.put("lastExchangPrice",0);
			data.put("transactionSum",0);
			data.put("maxPrice",0);
			data.put("minPrice",0);
			// 开盘价
			data.put("openPrice", new BigDecimal(0));

			data.put("lastEndPrice", 0);
			data.put("RiseAndFall", 0);
		}

		return data;
		
	}
	
	
}