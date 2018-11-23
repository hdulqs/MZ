/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.trade.entrust.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huobi.api.ApiClient;
import com.huobi.api.ApiException;
import com.huobi.response.HistoryTrade;
import com.huobi.response.TradeResponse;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisTradeService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.websoketContext.model.MarketDepths;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.front.redis.model.UserRedis;
import com.mz.trade.MQmanager.MQEnter;
import com.mz.trade.entrust.dao.CommonDao;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.model.CoinKeepDecimal;
import com.mz.trade.model.TradeRedis;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.trade.redis.model.ExchangeDataCacheRedis;
import java.math.BigDecimal;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Gao Mimi
 * @Date : 2016年4月12日 下午4:45:50
 */
@Service("exEntrustService")
public class ExEntrustServiceImpl extends BaseServiceImpl<ExEntrust, Long> implements ExEntrustService {

    private Logger logger = Logger.getLogger(ExEntrustServiceImpl.class);

	@Resource(name = "exEntrustDao")
	@Override
	public void setDao(BaseDao<ExEntrust, Long> dao) {
		super.dao = dao;
	}

	@Resource
	public RedisService redisService;
	@Resource
	public CommonDao commonDao;
	@Autowired
	private ApiClient huobiApiClient;

	private static int entrustType=1;

    @Override
    public void tradeInit() {
        RedisTradeService redisTradeService = (RedisTradeService) ContextUtil.getBean("redisTradeService");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        redisTradeService.delkeys(":buy:");
        redisTradeService.delkeys(":sell:");
        // 初始化交易数据==========================================================================================
        List<ExCointoCoin> listExCointoCoin = commonDao.getExCointoCoinValid();
        for (ExCointoCoin exCointoCoin : listExCointoCoin) {
            String header = exCointoCoin.getCoinCode() + "_" + exCointoCoin.getFixPriceCoinCode();
            // 买
            QueryFilter filterbuy = new QueryFilter(ExEntrust.class);
            filterbuy.addFilter("status<", 2)
                    .addFilter("type=", 1)
                    .addFilter("coinCode=", exCointoCoin.getCoinCode())
                    .addFilter("fixPriceCoinCode=", exCointoCoin.getFixPriceCoinCode())
                    .setOrderby("entrustPrice desc");
            List<ExEntrust> buyList = this.find(filterbuy);
            String buyonePricekey = header + ":" + ExchangeDataCacheRedis.BuyOnePrice; // 买一
            filterEntrust(buyList); // 过滤重复的委托
            if (null != buyList && buyList.size() > 0) {
                List<EntrustTrade> sameBuyPriceList = new ArrayList<>();
                BigDecimal sameprice = buyList.get(0).getEntrustPrice();
                for (ExEntrust buyExEntrust : buyList) {
                    if (buyExEntrust.getSurplusEntrustCount() == null
                            || buyExEntrust.getSurplusEntrustCount().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    EntrustTrade entrustTrade = objectMapper.convertValue(buyExEntrust, EntrustTrade.class);
                    if (buyExEntrust.getEntrustPrice().compareTo(sameprice) == 0) {
                        sameBuyPriceList.add(entrustTrade);
                    } else {
                        String key = header + ":buy:" + sameprice.setScale(10, BigDecimal.ROUND_HALF_EVEN).toString();
                        redisTradeService.save(key, JSON.toJSONString(sameBuyPriceList));
                        sameBuyPriceList.clear();
                        sameBuyPriceList.add(entrustTrade);
                        sameprice = buyExEntrust.getEntrustPrice();
                    }
                }
                String key = header + ":buy:" + sameprice.setScale(10, BigDecimal.ROUND_HALF_EVEN).toString();
                redisTradeService.save(key, JSON.toJSONString(sameBuyPriceList));
                redisService.save(buyonePricekey, JSON.toJSONString(buyList.get(0).getEntrustPrice()));
            } else {
                redisService.delete(buyonePricekey);
            }
            // 卖
            QueryFilter filtersell = new QueryFilter(ExEntrust.class);
            filtersell.addFilter("status<", 2)
                    .addFilter("type=", 2)
                    .addFilter("coinCode=", exCointoCoin.getCoinCode())
                    .addFilter("fixPriceCoinCode=", exCointoCoin.getFixPriceCoinCode())
                    .setOrderby("entrustPrice asc");
            List<ExEntrust> sellList = this.find(filtersell);
            String sellonePricekey = header + ":" + ExchangeDataCacheRedis.SellOnePrice; // 对手单的卖一
            filterEntrust(sellList); // 过滤重复的委托
            if (null != sellList && sellList.size() > 0) {
                List<EntrustTrade> listsellpricesame = new ArrayList<>();
                BigDecimal entrustPrice = sellList.get(0).getEntrustPrice();
                for (ExEntrust sellExEntrust : sellList) {
                    if (sellExEntrust.getSurplusEntrustCount() == null
                            || sellExEntrust.getSurplusEntrustCount().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    EntrustTrade entrustTrade = objectMapper.convertValue(sellExEntrust, EntrustTrade.class);
                    if (sellExEntrust.getEntrustPrice().compareTo(entrustPrice) == 0) {
                        listsellpricesame.add(entrustTrade);
                    } else {
                        String key = header + ":sell:" + entrustPrice.setScale(10, BigDecimal.ROUND_HALF_EVEN).toString();
                        redisTradeService.save(key, JSON.toJSONString(listsellpricesame));
                        listsellpricesame.clear();
                        listsellpricesame.add(entrustTrade);
                        entrustPrice = sellExEntrust.getEntrustPrice();
                    }
                }
                String key = header + ":sell:" + entrustPrice.setScale(10, BigDecimal.ROUND_HALF_EVEN).toString();
                redisTradeService.save(key, JSON.toJSONString(listsellpricesame));
                redisService.save(sellonePricekey, JSON.toJSONString(sellList.get(0).getEntrustPrice()));
            } else {
                redisService.delete(sellonePricekey);
            }
        }
    }

    private void filterEntrust(List<ExEntrust> exEntrustList) {
        if (exEntrustList == null) {
            return;
        }
        List<ExEntrust> filteredList = new ArrayList<>();
        Set<String> entrustNums = new HashSet<>();
        for (Iterator<ExEntrust> it = exEntrustList.iterator(); it.hasNext(); ) {
            ExEntrust exEntrust = it.next();
            if (!entrustNums.contains(exEntrust.getEntrustNum())) {
                entrustNums.add(exEntrust.getEntrustNum());
                filteredList.add(exEntrust);
            }
        }
        exEntrustList.clear();
        exEntrustList.addAll(filteredList);
    }

    @Override
    public void cancelAutoAddExEntrust() {
        Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
        for (String Website : mapLoadWeb.keySet()) {
            List<ExCointoCoin> listExCointoCoin = commonDao.getSratAutoExCointoCoin();
            for (ExCointoCoin exCointoCoin : listExCointoCoin) {
                Integer isSratAuto = exCointoCoin.getIsSratAuto();
                if (isSratAuto.equals(1)) {
                    String autoUsernames = exCointoCoin.getAutoUsername();
                    String[] autoUsernameArr = autoUsernames.split(",");
                    if (null == autoUsernameArr) {
                        continue;
                    }
                    for (String autoUsername : autoUsernameArr) {
                        if (null == autoUsername) {
                            System.out.println("填写的手机号有误，请检查重试！");
                            continue;
                        }
                        AppCustomer customer = commonDao.getAppUserByuserName(autoUsername);
                        Long customerId = customer.getId();
                        exCointoCoin.setCustomerId(customerId);

                        EntrustTrade entrustTrade = new EntrustTrade();
                        entrustTrade.setCustomerId(customerId);
                        entrustTrade.setCoinCode(exCointoCoin.getCoinCode());
                        entrustTrade.setFixPriceCoinCode(exCointoCoin.getFixPriceCoinCode());
                        entrustTrade.setCancelKeepN(10);
                        MQEnter.pushExEntrustMQ(entrustTrade);
                    }
                }
            }
        }
    }

    public BigDecimal getTime() {
        return getFloatNum(new BigDecimal("1500"), new BigDecimal("90"), null);
    }

	public Integer[] getCoinToCoinKeep(String coinCode,String fixPriceCoinCode){

		int keepDecimalForCoin = 4;
		int keepDecimalForCurrency = 4;

		CoinKeepDecimal coinKeepDecimal = null;
		String str = redisService.get("cn:coinInfoList");
		if (!StringUtils.isEmpty(str)) {
			List<CoinKeepDecimal> coins = JSON.parseArray(str, CoinKeepDecimal.class);
			if (coins != null && coins.size() > 0) {
				for (CoinKeepDecimal coin : coins) {
					if (coinCode.equals(coin.getCoinCode()) && fixPriceCoinCode.equals(coin.getFixPriceCoinCode())) {
						coinKeepDecimal = coin;
					}
				}
			}
		}
		if (null != coinKeepDecimal) {
			keepDecimalForCoin = coinKeepDecimal.getKeepDecimalForCoin();
			keepDecimalForCurrency = coinKeepDecimal.getKeepDecimalForCurrency();
		}
		Integer[] keepDec=new Integer[2];
		keepDec[0]=keepDecimalForCurrency;
		keepDec[1]=keepDecimalForCoin;

		return keepDec;
	}

	/**
	 * 自动刷单
	 */
	@Override
	public void autoAddExEntrust() {
        if(entrustType==1){
            // 生成刷单价格1
            entrustType=2;
        }else{
            entrustType=1;
        }
        long start = System.currentTimeMillis();
        // 获取允许交易的交易对
        List<ExCointoCoin> exCointoCoinList = commonDao.getSratAutoExCointoCoin();
        if (exCointoCoinList == null) { // 存在 可以交易 开启了自动交易 有自动下委托的帐号 的交易对
            return;
        }

        List<ExCointoCoin> hedgeCointoCoinList = new ArrayList<>();      // 对冲交易对
        List<ExCointoCoin> normalCointoCoinList = new ArrayList<>();    // 非对冲交易对
        for (ExCointoCoin exCointoCoin : exCointoCoinList) {
            if (exCointoCoin.getIsSratAuto()== null || exCointoCoin.getIsSratAuto().equals(0)) {
                continue;
            }
            if (exCointoCoin.getIsHedge() != null && exCointoCoin.getIsHedge().equals(1)) {
                hedgeCointoCoinList.add(exCointoCoin);
            } else {
                normalCointoCoinList.add(exCointoCoin);
            }
        }
        Map<String, String> mapLoadWeb = PropertiesUtils.getLoadWeb();
        for (String website : mapLoadWeb.keySet()) {
            for (ExCointoCoin exCointoCoin : hedgeCointoCoinList) {
                addHedgeEntrustByAutoAccount(exCointoCoin, entrustType, mapLoadWeb.get(website), website);
            }
        }
        long speedTime = System.currentTimeMillis() - start;
        if (speedTime > 300) {
            logger.info("对冲委托耗时: " + speedTime);
        }
        // 未开启对冲交易的交易对，最好睡眠后进行自动委托
        long sleepTime = getTime().longValue();
        if (sleepTime > speedTime) {
            sleepTime -= speedTime;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (String website : mapLoadWeb.keySet()) {
            for (ExCointoCoin exCointoCoin : normalCointoCoinList) {
                addEntrustByAutoAccount(exCointoCoin, entrustType, mapLoadWeb.get(website), website);
            }
        }
    }


    /**
     * @param exCointoCoin
     * @param entrustType  1：获取匹配的
     * @return
     */
    private static List<EntrustTrade> getMatchEntrustTradebyType(ExCointoCoin exCointoCoin, Long customerId, int entrustType) {
        List<EntrustTrade> matchEnTrustTradeList = new ArrayList<>();
        if (exCointoCoin != null && entrustType != 0) {
            RedisTradeService redisTradeService = (RedisTradeService) ContextUtil.getBean("redisTradeService");
            Set<String> keys = null;
            if (entrustType == 1) {
                String buyKey = exCointoCoin.getCoinCode() + "_" + exCointoCoin.getFixPriceCoinCode() + ":buy";
                keys = redisTradeService.noPerkeys(buyKey + ":");
            } else if (entrustType == 2) {
                String sellKey = exCointoCoin.getCoinCode() + "_" + exCointoCoin.getFixPriceCoinCode() + ":sell";
                keys = redisTradeService.noPerkeys(sellKey + ":");
            }
            if (keys == null) {
                return null;
            }
            for (String key : keys) {
                String v = redisTradeService.get(key);
                List<EntrustTrade> entrustTradeList = JSON.parseArray(v, EntrustTrade.class);
                if (entrustTradeList == null) {
                    continue;
                }
                for (EntrustTrade entrustTrade : entrustTradeList) {
                    if (entrustTrade.getCustomerId().equals(customerId)) {
                        matchEnTrustTradeList.add(entrustTrade);
                    }
                }
            }
        }

        return matchEnTrustTradeList;
    }

    /**
     * 从火币获取最新的一条成交记录，并以该成交记录作为火币的参考价格
     * @param coinCouple
     * @return
     */
    private BigDecimal getHuobiReferencePriceByApi(String coinCouple) throws ApiException {
        if (huobiApiClient == null) {
            huobiApiClient = (ApiClient) ContextUtil.getBean("huobiApiClient");
        }

        TradeResponse tradeResponse = huobiApiClient.trade(coinCouple.replace("_", "").toLowerCase());
        if (!tradeResponse.getStatus().equals("ok")) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<HistoryTrade> historyTradeList = objectMapper.convertValue(tradeResponse.getTick().getData(),
                new TypeReference<List<HistoryTrade>>() {
                });
        if (historyTradeList != null) {
            for (HistoryTrade historyTrade : historyTradeList) {
                return new BigDecimal(historyTrade.getPrice());
            }
        }
        return null;
    }

    /**
     * 定时器刷新参考价格
     */
    public void refreshReferencePrice() {
        List<ExCointoCoin> exCointoCoinList = commonDao.getSratAutoExCointoCoin();
        if (exCointoCoinList == null || exCointoCoinList.size() == 0) {
            return;
        }
        for (ExCointoCoin exCointoCoin : exCointoCoinList) {
            String coinCouple = exCointoCoin.getCoinCode() + "_" + exCointoCoin.getFixPriceCoinCode();
            String referencePriceKey = coinCouple + ":" + ExchangeDataCacheRedis.ReferenceExchangPrice;
            BigDecimal referencePrice = null;
            if (exCointoCoin.getAtuoPriceType() == 3) { // 参考火币的价格
                for (int i = 0; i < 3; i++) {
                    try {
                        referencePrice = getHuobiReferencePriceByApi(coinCouple);
                        if (referencePrice != null && BigDecimal.ZERO.compareTo(referencePrice) > 0) {
                            break;
                        }
                    } catch (ApiException e) {
                        logger.error("refresh <" + i + "> "+ referencePriceKey + " error!");
                    }
                }
            }
            if (referencePrice == null || BigDecimal.ZERO.compareTo(referencePrice) == 0) {
                continue;
            }

            RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
            redisService.save(referencePriceKey, referencePrice.toString());
        }
    }

    /**
     * 调用该方法
     * <p>
     * TODO
     * </p>
     *
     * @author: Zhang Lei
     * @param: @param
     * type 买还是卖类型 1 买 2 卖
     * @param: @param
     * customerId 用户ID
     * @param: @param
     * price 价格（机器人随机浮动范围内生成）
     * @param: @param
     * userCode 用户code
     * @param: @param
     * coinCode 币种
     * @param: @param
     * entrustCount 数量
     * @param: @param
     * currencyType 固定cny
     * @param: @param
     * website 固定cn
     * @return: void
     * @Date : 2017年2月9日 下午4:25:01
     * @throws:
     */
    @Override
    public boolean addExEntrust(String coinCode, String fixPriceCoinCode, Integer fixPriceType, Integer type, Integer entrustWay, Long customerId, BigDecimal price, String autoUsername, BigDecimal entrustCount, String currencyType, String website) {
        EntrustTrade entrustTrade = new EntrustTrade();
        if (coinCode == null || fixPriceCoinCode == null) {
            return false;
        }
        entrustTrade.setFixPriceCoinCode(fixPriceCoinCode);
        entrustTrade.setCoinCode(coinCode);
        entrustTrade.setFixPriceType(fixPriceType);
        entrustTrade.setType(type);
        if (price.compareTo(BigDecimal.ZERO) == 0 || entrustCount.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }
        entrustTrade.setEntrustSum(BigDecimal.ZERO);

        Integer[] keepDec = getCoinToCoinKeep(coinCode, fixPriceCoinCode);
        int keepDecimalForCoin = keepDec[1];
        int keepDecimalForCurrency = keepDec[0];

        if (entrustWay != null) {
            if (entrustWay == 1) {
                entrustTrade.setEntrustWay(entrustWay);// 1.限价--> 表示以固定的价格 , 2.市价--->
                entrustTrade.setEntrustPrice(price.setScale(keepDecimalForCurrency, BigDecimal.ROUND_HALF_DOWN));
                entrustTrade.setEntrustCount(entrustCount.setScale(keepDecimalForCoin, BigDecimal.ROUND_HALF_DOWN));
            } else {
                entrustTrade.setEntrustWay(entrustWay);// 1.限价--> 表示以固定的价格 , 2.市价--->
                if (type == 1) {
                    entrustTrade.setEntrustSum(price.multiply(entrustCount).setScale(keepDecimalForCurrency, BigDecimal.ROUND_HALF_DOWN));
                } else {
                    entrustTrade.setEntrustCount(entrustCount.setScale(keepDecimalForCoin, BigDecimal.ROUND_HALF_DOWN));
                }
            }
        }

        entrustTrade.setCustomerId(customerId);
        entrustTrade.setSource(2);
        entrustTrade.setUserName(autoUsername);
        entrustTrade.setSurName("");
        entrustTrade.setTrueName("机器人");
        initExEntrust(entrustTrade);
        MQEnter.pushExEntrustMQ(entrustTrade);
        return true;
    }

    public void addHedgeEntrustByAutoAccount(ExCointoCoin exCointoCoin, Integer entrustType, String currencyType, String website) {
        if (exCointoCoin == null || exCointoCoin.getIsHedge() == null || exCointoCoin.getIsHedge() != 1) {
            return;
        }
        String coinCouple = exCointoCoin.getCoinCode() + "_" + exCointoCoin.getFixPriceCoinCode();
        String autoUsernames = exCointoCoin.getAutoUsername();
        if (autoUsernames == null) {
            return;
        }
        String[] autoUsernameArr = autoUsernames.split(",");
        for (String autoUsername : autoUsernameArr) {
            if (null == autoUsername) {
                System.out.println("填写的手机号有误，请检查重试！");
                continue;
            }
            AppCustomer customer = commonDao.getAppUserByuserName(autoUsername);
            if (customer == null) {
                System.out.println("填写的手机号有误，请检查重试！======no account");
                continue;
            }
            exCointoCoin.setCustomerId(customer.getId());

            BigDecimal autoPrice = BigDecimal.ZERO;
            BigDecimal entrustNum = BigDecimal.ZERO;

            Integer fixPriceType = exCointoCoin.getFixPriceType();
            for (int i = 0; i < 2; i++) {
                List<EntrustTrade> entrustTradeList = getMatchEntrustTradebyType(exCointoCoin, exCointoCoin.getCustomerId(), entrustType);
                if (entrustTradeList != null && entrustTradeList.size() > 0) {  // 不能再下这个类型的委托了，必须换类型
                    // 切换委托类型，卖切换为买，买切换为卖
                    if (entrustType == 1) {
                        entrustType = 2;
                    } else {
                        entrustType = 1;
                    }
                    // 如果是卖找到卖中的最大价格，如果是买找到最小价格，并获取所有委托数量
                    for (EntrustTrade entrustTrade : entrustTradeList) {
                        if (autoPrice == null || autoPrice.compareTo(BigDecimal.ZERO) <= 0) {
                            autoPrice = entrustTrade.getEntrustPrice();
                        }
                        if (entrustType == 1) { // 买,找到卖单中最大的价格
                            autoPrice = autoPrice.max(entrustTrade.getEntrustPrice());
                        } else { // 卖，找到买单中的最小的价格
                            autoPrice = autoPrice.min(entrustTrade.getEntrustPrice());
                        }
                        entrustNum = entrustNum.add(entrustTrade.getSurplusEntrustCount());
                    }
                    // 根据价格浮动一下，并保证卖价格下浮，买价格上浮，数量不能采用浮动，如果采用浮动则无法参考火币的价格
                    if (autoPrice != null && autoPrice.compareTo(BigDecimal.ZERO) > 0) {
                        if (entrustType == 1) { // 买
                            autoPrice = getFloatNum(autoPrice, exCointoCoin.getAutoPriceFloat(), true);
                        } else { // 卖
                            autoPrice = getFloatNum(autoPrice, exCointoCoin.getAutoPriceFloat(), false);
                        }
                    }
                    if (autoPrice != null && autoPrice.compareTo(BigDecimal.ZERO) > 0
                            && entrustNum != null && entrustNum.compareTo(BigDecimal.ZERO) > 0) {
                        break;
                    }
                }
            }

            if (autoPrice == null || autoPrice.compareTo(BigDecimal.ZERO) <= 0) {
                autoPrice = getPrcie(exCointoCoin.getAtuoPriceType(), exCointoCoin.getUpFloatPer(),
                        coinCouple, exCointoCoin.getAutoPrice(), exCointoCoin.getAutoPriceFloat());
            }
            if (entrustNum == null || entrustNum.compareTo(BigDecimal.ZERO) <= 0) {
                entrustNum = getFloatNum(exCointoCoin.getAutoCount(), exCointoCoin.getAutoCountFloat(), null);
            }
            if (!addExEntrust(exCointoCoin.getCoinCode(), exCointoCoin.getFixPriceCoinCode(),
                    fixPriceType, entrustType, 1, exCointoCoin.getCustomerId(),
                    autoPrice, autoUsername, entrustNum, currencyType, website)) {
                logger.info("委托失败");
            }
        }
    }


    /**
     * 根据ex_cointo_coin中的自动交易进行下单
     * @param exCointoCoin
     * @param entrustType 1为买，2为卖
     * @param currencyType
     * @param website
     * @return
     */
    public void addEntrustByAutoAccount(ExCointoCoin exCointoCoin, Integer entrustType, String currencyType, String website) {
        String autoUsernames = exCointoCoin.getAutoUsername();
        if (autoUsernames == null) {
            return ;
        }
        String[] autoUsernameArr = autoUsernames.split(",");
        for (String autoUsername : autoUsernameArr) {
            if (null == autoUsername) {
                System.out.println("填写的手机号有误，请检查重试！");
                continue;
            }
            AppCustomer customer = commonDao.getAppUserByuserName(autoUsername);
            if (customer == null) {
                System.out.println("填写的手机号有误，请检查重试！======no account");
                continue;
            }
            exCointoCoin.setCustomerId(customer.getId());
            String coinCouple = exCointoCoin.getCoinCode() + "_" + exCointoCoin.getFixPriceCoinCode();
            BigDecimal autoPrice = getPrcie(exCointoCoin.getAtuoPriceType(), exCointoCoin.getUpFloatPer(),
                    coinCouple, exCointoCoin.getAutoPrice(), exCointoCoin.getAutoPriceFloat());
            BigDecimal entrustNum = getFloatNum(exCointoCoin.getAutoCount(), exCointoCoin.getAutoCountFloat(), null);
            if (!addExEntrust(exCointoCoin.getCoinCode(), exCointoCoin.getFixPriceCoinCode(), exCointoCoin.getFixPriceType(),
                    entrustType, 1, exCointoCoin.getCustomerId(),
                    autoPrice, autoUsername, entrustNum, currencyType, website)) {
                logger.info("委托失败");
            }
        }
    }

	public void initExEntrust(EntrustTrade exEntrust) {
		String saasId = PropertiesUtils.APP.getProperty("app.saasId");
		if (null == exEntrust.getEntrustTime()) {
			exEntrust.setEntrustTime(new Date());
		}

		// String transactionNum =
		// IdGenerate.transactionNum(NumConstant.Ex_Entrust);
		if (exEntrust.getType() == 1) {
			exEntrust.setEntrustNum("WB" + UUID.randomUUID());
		} else {
			exEntrust.setEntrustNum("WS" + UUID.randomUUID());
		}
		exEntrust.setCustomerId(exEntrust.getCustomerId());
		// 查redis缓存
		RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
		UserRedis userRedis = redisUtil.get(exEntrust.getCustomerId().toString());
		if (userRedis == null) {
		    logger.warn("请前台机器人账户：" + exEntrust.getUserName());
        }
		// 获得缓存中所有的币账户id
		if (exEntrust.getFixPriceType().equals(0)) { // 真实货币
			exEntrust.setAccountId(userRedis.getAccountId());
		} else {
			exEntrust.setAccountId(userRedis.getDmAccountId(exEntrust.getFixPriceCoinCode()));
		}
		exEntrust.setCoinAccountId(userRedis.getDmAccountId(exEntrust.getCoinCode()));
		/*
		 * exEntrust.setAccountId(Long.valueOf("22434"));
		 * exEntrust.setCoinAccountId(Long.valueOf("52687"));
		 */
		exEntrust.setStatus(0);
		exEntrust.setTransactionSum(null == exEntrust.getTransactionSum() ? new BigDecimal("0") : exEntrust.getTransactionSum());
		exEntrust.setEntrustSum((null != exEntrust.getEntrustPrice() && null != exEntrust.getEntrustCount()) && !new BigDecimal("0").equals(exEntrust.getEntrustPrice()) && !new BigDecimal("0").equals(exEntrust.getEntrustCount()) ? exEntrust.getEntrustPrice().multiply(exEntrust.getEntrustCount()) : exEntrust.getEntrustSum());
		exEntrust.setEntrustCount(null == exEntrust.getEntrustCount() ? new BigDecimal("0") : exEntrust.getEntrustCount());
		exEntrust.setSurplusEntrustCount(exEntrust.getEntrustCount());
		if (null == exEntrust.getEntrustCount()) {
			exEntrust.setEntrustCount(new BigDecimal("0"));
		}
		if (null == exEntrust.getEntrustPrice()) {
			exEntrust.setEntrustPrice(new BigDecimal("0"));
		}
		exEntrust.setTransactionFeeRate(new BigDecimal("0"));
		exEntrust.setTransactionFee(new BigDecimal("0"));

		if (null == exEntrust.getEntrustWay()) {

			exEntrust.setEntrustWay(1);// 默认限价
		}
		if (null == exEntrust.getFloatDownPrice()) {
			exEntrust.setFloatDownPrice(new BigDecimal("0"));
			if (exEntrust.getEntrustWay() == 1 && exEntrust.getType() == 1) {
				exEntrust.setFloatDownPrice(exEntrust.getEntrustPrice());
			}
			if (exEntrust.getEntrustWay() == 1 && exEntrust.getType() == 2) {
				exEntrust.setFloatUpPrice((new BigDecimal("999999")));
			}
		}
		if (null == exEntrust.getFloatUpPrice()) {

			exEntrust.setFloatUpPrice(new BigDecimal("0"));
		}

	}

    /**
     * 根据浮动比例获取浮动值
     * 当@param autoPriceType大于3的时候，就是获取参考价格{@link #refreshReferencePrice}
     * @param autoPriceType 价格类型，1定价下单，2市价下单，3参考火币价格
     * @param upFloatPer
     * @param coinCouple
     * @param basePrice
     * @param floatPrice
     * @return
     */
    public BigDecimal getPrcie(Integer autoPriceType, BigDecimal upFloatPer, String coinCouple, BigDecimal basePrice, BigDecimal floatPrice) {
        if (autoPriceType == null) {
            return BigDecimal.ZERO;
        }
        switch (autoPriceType) {
            case 1:     // 按定价浮动
                break;
            case 2: {   // 按市价浮动
                String currentPrice = TradeRedis.getStringData(coinCouple + ":" + ExchangeDataCacheRedis.CurrentExchangPrice);
                if (!StringUtil.isEmpty(currentPrice)) {
                    Boolean upDown = (Math.random() > 0.5);
                    BigDecimal price = getFloatNum(new BigDecimal(currentPrice), floatPrice, upDown);
                    if (upDown && upFloatPer != null) {
                        return price.multiply(upFloatPer);
                    }
                    return price;
                }
                break;
            }
            case 3: { // 获取参考价格
                String currentPrice = TradeRedis.getStringData(coinCouple + ":" + ExchangeDataCacheRedis.ReferenceExchangPrice);
                if (!StringUtil.isEmpty(currentPrice)) {
                    return getFloatNum(new BigDecimal(currentPrice), floatPrice, null);
                }
                break;
            }
        }

        return getFloatNum(basePrice,floatPrice, null);
    }

    /**
     * 根据基准价格和浮动比率获取一个浮动价格
     * 获取浮动值 刷币价格 * (浮动比例 * 随机小数 )
     * @param base
     * @param floatRange
     * @param isUp null的时候，随机，true上浮，false下浮
     * @return
     */
    private BigDecimal getFloatNum(BigDecimal base, BigDecimal floatRange, Boolean isUp) {
        BigDecimal singleFloatPrice = base
                .multiply(floatRange.divide(new BigDecimal("100")))
                .multiply(BigDecimal.valueOf(Math.random()));

        if (isUp == null) {
            isUp = (Math.random() > 0.5);
        }

        if (isUp) {
            return base.add(singleFloatPrice);
        } else {
            return base.subtract(singleFloatPrice);
        }
    }

	@Override
	public ExEntrust getExEntrustByentrustNum(String entrustNum) {
		QueryFilter filter = new QueryFilter(ExEntrust.class);
		filter.addFilter("entrustNum=", entrustNum);
		filter.setSaasId("hurong_system");
		return this.get(filter);
	}

	@Override
	public String getExEntrustChangeMarket(String coinCode, String fixPriceCoinCode, Integer n) {
		MarketDepths marketDepths = new MarketDepths();
		Map<String, List<BigDecimal[]>> map = new HashMap<String, List<BigDecimal[]>>();
		int keepDecimalForCoin = 4;
		int keepDecimalForCurrency = 4;

		CoinKeepDecimal coinKeepDecimal = null;
		String str = redisService.get("cn:coinInfoList");
		if (!StringUtils.isEmpty(str)) {
			List<CoinKeepDecimal> coins = JSON.parseArray(str, CoinKeepDecimal.class);
			if (coins != null && coins.size() > 0) {
				for (CoinKeepDecimal coin : coins) {
					if (coinCode.equals(coin.getCoinCode()) && fixPriceCoinCode.equals(coin.getFixPriceCoinCode())) {
						coinKeepDecimal = coin;
					}
				}
			}
		}
		if (null != coinKeepDecimal) {
			keepDecimalForCoin = coinKeepDecimal.getKeepDecimalForCoin();
			keepDecimalForCurrency = coinKeepDecimal.getKeepDecimalForCurrency();
		}

		List<BigDecimal[]> bids = new ArrayList<BigDecimal[]>();
		EntrustTrade sellexEntrust = new EntrustTrade();
		sellexEntrust.setCoinCode(coinCode);
		sellexEntrust.setFixPriceCoinCode(fixPriceCoinCode);
		sellexEntrust.setType(2);
		List<BigDecimal> keyslistbuy = TradeRedis.getMatchkeys(sellexEntrust);// 查所有的keys
		int i = 0;
		while (i < keyslistbuy.size()) {



			BigDecimal keybig = keyslistbuy.get(i);
			String keyall = TradeRedis.getHeaderMatch(sellexEntrust) + ":" + keybig.toString();
			List<EntrustTrade> list = TradeRedis.getMatchEntrustTradeBykey(keyall);
			BigDecimal entrustPrice = new BigDecimal("0");
			BigDecimal surplusEntrustCount = new BigDecimal("0");
			if (null != list) {
				for (EntrustTrade entrustTrade : list) {
					entrustPrice = entrustTrade.getEntrustPrice();
					surplusEntrustCount = surplusEntrustCount.add(entrustTrade.getSurplusEntrustCount());
				}
				if (surplusEntrustCount.compareTo(BigDecimal.ZERO) <= 0) {
					continue;
				}
				BigDecimal[] array = new BigDecimal[2];
				array[0] = entrustPrice.setScale(keepDecimalForCurrency, BigDecimal.ROUND_HALF_EVEN);
				array[1] = surplusEntrustCount.setScale(keepDecimalForCoin, BigDecimal.ROUND_HALF_EVEN);
				bids.add(array);
			}

			if (i == n)
				break;
			i++;
		}

		map.put("bids", bids);
		if (keyslistbuy.size() > 0) {
			String keybuy = TradeRedis.getHeaderFront(sellexEntrust) + ":" + ExchangeDataCacheRedis.BuyOnePrice;
			String v = redisService.get(keybuy);
			if (StringUtil.isEmpty(v)) {
				redisService.save(keybuy, JSON.toJSONString(bids.get(0)[0]));
			}
		}
		// 卖单
		List<BigDecimal[]> asks = new ArrayList<BigDecimal[]>();
		EntrustTrade byllexEntrust = new EntrustTrade();
		byllexEntrust.setCoinCode(coinCode);
		byllexEntrust.setFixPriceCoinCode(fixPriceCoinCode);
		byllexEntrust.setType(1);
		List<BigDecimal> keyslistsell = TradeRedis.getMatchkeys(byllexEntrust);// 查所有的keys
		int k = 0;
		while (k < keyslistsell.size()) {
			BigDecimal keybig = keyslistsell.get(k);
			String keyall = TradeRedis.getHeaderMatch(byllexEntrust) + ":" + keybig.toString();
			List<EntrustTrade> list = TradeRedis.getMatchEntrustTradeBykey(keyall);
			BigDecimal entrustPrice = new BigDecimal("0");
			BigDecimal surplusEntrustCount = new BigDecimal("0");
			if (null != list) {
				for (EntrustTrade entrustTrade : list) {
					entrustPrice = entrustTrade.getEntrustPrice();
					surplusEntrustCount = surplusEntrustCount.add(entrustTrade.getSurplusEntrustCount());
				}
				if (surplusEntrustCount.compareTo(BigDecimal.ZERO) <= 0) {
					continue;
				}
				BigDecimal[] array = new BigDecimal[2];
				array[0] = entrustPrice.setScale(keepDecimalForCurrency, BigDecimal.ROUND_HALF_EVEN);
				array[1] = surplusEntrustCount.setScale(keepDecimalForCoin, BigDecimal.ROUND_HALF_EVEN);
				asks.add(array);
			}
			if (k == n)
				break;
			k++;
		}
		map.put("asks", asks);
		if (keyslistsell.size() > 0) {
			String keysell = TradeRedis.getHeaderFront(byllexEntrust) + ":" + ExchangeDataCacheRedis.SellOnePrice;
			String v = redisService.get(keysell);
			if (StringUtil.isEmpty(v)) {
				redisService.save(keysell, JSON.toJSONString(asks.get(0)[0]));
			}

		}
		marketDepths.setDepths(map);
		return JSON.toJSONString(marketDepths);
	}

	public BigDecimal getByn(int n) {
		BigDecimal bd = new BigDecimal(1);
		for (int i = 0; i < n; i++) {
			bd = bd.multiply(new BigDecimal(10));
		}
		return bd;
	}

	@Override
	public String getExEntrustChangeDephMarket(String coinCode, String fixPriceCoinCode, Integer n, BigDecimal jj) {
		MarketDepths marketDepths = new MarketDepths();
		Map<String, List<BigDecimal[]>> map = new HashMap<String, List<BigDecimal[]>>();
		int keepDecimalForCoin = 4;
		int keepDecimalForCurrency = 4;

		CoinKeepDecimal coinKeepDecimal = null;
		String str = redisService.get("cn:coinInfoList");
		if (!StringUtils.isEmpty(str)) {
			List<CoinKeepDecimal> coins = JSON.parseArray(str, CoinKeepDecimal.class);
			if (coins != null && coins.size() > 0) {
				for (CoinKeepDecimal coin : coins) {
					if (coinCode.equals(coin.getCoinCode()) && fixPriceCoinCode.equals(coin.getFixPriceCoinCode())) {
						coinKeepDecimal = coin;
					}
				}
			}
		}

		if (null != coinKeepDecimal) {
			keepDecimalForCoin = coinKeepDecimal.getKeepDecimalForCoin();
			keepDecimalForCurrency = coinKeepDecimal.getKeepDecimalForCurrency();
		}
		int keepDecimalForCurrencysubone = keepDecimalForCurrency - 1;
		if (keepDecimalForCurrencysubone < 0) {
			keepDecimalForCurrencysubone = 0;
		}
		BigDecimal depth = new BigDecimal(1).divide(getByn(keepDecimalForCurrencysubone), keepDecimalForCurrencysubone, BigDecimal.ROUND_DOWN);
		depth = depth.multiply(jj);
		List<BigDecimal[]> bids = new ArrayList<BigDecimal[]>();
		EntrustTrade sellexEntrust = new EntrustTrade();
		sellexEntrust.setCoinCode(coinCode);
		sellexEntrust.setFixPriceCoinCode(fixPriceCoinCode);
		sellexEntrust.setType(2);
		List<BigDecimal> keyslistbuy = TradeRedis.getMatchkeys(sellexEntrust);// 查所有的keys
		if (null != keyslistbuy && keyslistbuy.size() > 0) {
			BigDecimal maxPrice = new BigDecimal(keyslistbuy.get(0).toString());
			if (maxPrice.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal startPrice = maxPrice.setScale(depth.scale(), BigDecimal.ROUND_DOWN);
				startPrice = startPrice.subtract(depth);
				int i = 0;
				int flag = 0;
				for (int j = 0; j < 5; j++) {
					if (j > 0) {
						startPrice = startPrice.subtract(depth);
					}
					if (flag == 1) {
						break;
					}
					if (startPrice.compareTo(new BigDecimal(0)) <= 0) {
						flag = 1;
						startPrice = new BigDecimal(0);
					}
					BigDecimal surplusEntrustCount = new BigDecimal("0");
					while (i < keyslistbuy.size()) {
						BigDecimal keybig = keyslistbuy.get(i);
						if (keybig.compareTo(startPrice) >= 0) {
							String keyall = TradeRedis.getHeaderMatch(sellexEntrust) + ":" + keybig.toString();
							List<EntrustTrade> list = TradeRedis.getMatchEntrustTradeBykey(keyall);
							if (null != list) {
								for (EntrustTrade entrustTrade : list) {
									surplusEntrustCount = surplusEntrustCount.add(entrustTrade.getSurplusEntrustCount());
								}
							}
							i++;
						} else {
							break;
						}

					}

					BigDecimal[] array = new BigDecimal[2];
					array[0] = startPrice.setScale(keepDecimalForCurrency, BigDecimal.ROUND_HALF_EVEN);
					array[1] = surplusEntrustCount.setScale(keepDecimalForCoin, BigDecimal.ROUND_HALF_EVEN);
					bids.add(array);
				}
			}
		}
		map.put("bids", bids);

		// 卖单
		List<BigDecimal[]> asks = new ArrayList<BigDecimal[]>();
		EntrustTrade byllexEntrust = new EntrustTrade();
		byllexEntrust.setCoinCode(coinCode);
		byllexEntrust.setFixPriceCoinCode(fixPriceCoinCode);
		byllexEntrust.setType(1);
		List<BigDecimal> keyslistsell = TradeRedis.getMatchkeys(byllexEntrust);// 查所有的keys
		if (null != keyslistsell && keyslistsell.size() > 0) {
			BigDecimal minPrice = new BigDecimal(keyslistsell.get(0).toString());
			if (minPrice.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal startPrice = minPrice.setScale(depth.scale(), BigDecimal.ROUND_DOWN);
				startPrice = startPrice.add(depth);
				int k = 0;
				for (int j = 0; j < 5; j++) {
					if (j > 0) {
						startPrice = startPrice.add(depth);
					}
					BigDecimal surplusEntrustCount = new BigDecimal("0");
					while (k < keyslistsell.size()) {
						BigDecimal keybig = keyslistsell.get(k);
						if (keybig.compareTo(startPrice) < 1) {
							String keyall = TradeRedis.getHeaderMatch(byllexEntrust) + ":" + keybig.toString();
							List<EntrustTrade> list = TradeRedis.getMatchEntrustTradeBykey(keyall);
							if (null != list) {
								for (EntrustTrade entrustTrade : list) {
									surplusEntrustCount = surplusEntrustCount.add(entrustTrade.getSurplusEntrustCount());
								}
							}
							k++;
						} else {
							break;
						}

					}

					BigDecimal[] array = new BigDecimal[2];
					array[0] = startPrice.setScale(keepDecimalForCurrency, BigDecimal.ROUND_HALF_EVEN);
					array[1] = surplusEntrustCount.setScale(keepDecimalForCoin, BigDecimal.ROUND_HALF_EVEN);
					asks.add(array);
				}
			}

		}
		map.put("asks", asks);
		marketDepths.setDepths(map);
		return JSON.toJSONString(marketDepths);
	}

}
