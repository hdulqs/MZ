package com.mz.mq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huobi.api.ApiClient;
import com.huobi.api.ApiException;
import com.huobi.response.Kline;
import com.huobi.response.KlineResponse;
import com.mz.exchange.kline.model.TransactionOrder;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.date.DateUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.trade.kline.KlineEngine;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * 充币监听
 *
 * @author CHINA_LSL
 */
public class MessageSyncKline implements MessageListener {
    private static final Logger logger = Logger.getLogger(MessageSyncKline.class);

    private ApiClient huobiApiClient = null;

    // k线中的时间周期,1min, 5min, 15min, 30min, 60min, 1day, 1week, 1mon, 1year
    private static final HashMap<Integer, String> periods = new HashMap<>();

    private static final int HUOBI_RETRY_GAP = 3 * 1000;

    static {
        periods.put(1, "1min");
        periods.put(5, "5min");
        periods.put(15, "15min");
        periods.put(30, "30min");
        periods.put(60, "60min");
        periods.put(1440, "1day");
        periods.put(10080, "1week");
        periods.put(30000, "1mon");
        periods.put(525600, "1year");
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ExecutorService executors = Executors.newFixedThreadPool(1); // 使用一个，尽量减少系统开销

    @Override
    public void onMessage(final Message message) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String coinCode = new String(message.getBody());
                    updateAllKline(coinCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 导入所有的火币的k线数据，1min，5min等等
     *
     * @param coinCode 交易币的代号，例如BTC_USDT
     */
    public void updateAllKline(String coinCode) {
        for (int time : periods.keySet()) {
            String symbol = getSymbol(coinCode);
            RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
            String table = coinCode + ":klinedata:TransactionOrder_" + coinCode + "_" + time;
            inner_for:
            for (int i = 0; i < 20; i++) {
                try {
                    List<TransactionOrder> orderList = getHuobiKline(time, symbol, KlineEngine.HOLD_COUNT + 1);
                    if (orderList != null && orderList.size() > 1) {
                        orderList = orderList.subList(1, orderList.size() - 1);
                        redisService.save(table, objectMapper.writeValueAsString(orderList));
                        break inner_for;
                    } else {
                        logger.info("import " + symbol + " kline error!!!");
                    }
                } catch (ApiException e) {
                    logger.error("", e);
                } catch (JsonProcessingException e) {
                    logger.error("import " + KlineEngine.HOLD_COUNT + " data error from huobi", e);
                } catch (Exception e) {
                    logger.error("update kline", e);
                }
                try {
                    Thread.sleep(HUOBI_RETRY_GAP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static String getSymbol(String coinCode) {
        if (coinCode == null || !coinCode.contains("_")) {
            throw new NullPointerException("coinCode is null");
        }

        return coinCode.replace("_", "").toLowerCase();
    }

    /**
     * 获取火币的k线数据
     *
     * @param period 用于表示多少分钟
     * @param symbol 用于请求火币网的k线标签，比如BTC_USDT在火币网为btcusdt
     * @param size   获取多少条k线信息
     * @return
     */
    private synchronized List<TransactionOrder> getHuobiKline(int period, String symbol, int size) {
        String periodStr = periods.get(period);
        if (periodStr == null) {
            return null;
        }

        List<TransactionOrder> transactionOrderList = null;
        if (null == symbol) {
            return transactionOrderList;
        }

        if (huobiApiClient == null) {
            huobiApiClient = (ApiClient) ContextUtil.getBean("huobiApiClient");
        }

        if (size > 2000) { // 火币最多只能请求2000条
            size = 2000;
        }
        KlineResponse huobiKline = huobiApiClient.kline(symbol, periodStr, String.valueOf(size));
        if (!huobiKline.getStatus().equals("ok")) {
            huobiApiClient = new ApiClient("fff-xxx-ssss-kkk", "xxxxxx");
            huobiKline = huobiApiClient.kline(symbol, periodStr, String.valueOf(size));
        }
        if (huobiKline.getStatus().equals("ok")) {
            if (huobiKline.data instanceof List) {
                List<Kline> klineList = (List<Kline>) huobiKline.data;
                transactionOrderList = new ArrayList<>(klineList.size());
                for (Kline kline : klineList) {
                    TransactionOrder transactionOrder = new TransactionOrder();
                    Date date = new Date(kline.getId() * 1000L);
                    Date nowDate = getEndDate(date, period);
                    transactionOrder.setId(DateUtil.dateToString(nowDate, "yyyyMMddHHmm"));
                    transactionOrder.setStartPrice(kline.getOpen());
                    transactionOrder.setEndPrice(kline.getClose());
                    transactionOrder.setMinPrice(kline.getLow());
                    transactionOrder.setMaxPrice(kline.getHigh());
                    transactionOrder.setTransactionCount(kline.getAmount());
                    transactionOrder.setTransactionEndTime(DateUtil.dateToString(nowDate, "yyyy-MM-dd HH:mm"));
                    transactionOrder.setTransactionTime(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm"));
                    transactionOrder.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
                    transactionOrderList.add(transactionOrder);
                }
            }
        }
        return transactionOrderList;
    }

    private static Date getEndDate(Date startDate, int period) {
        if (startDate == null) {
            return null;
        }
        switch (period) {
            case 1:
                return DateUtil.addMinToDate(startDate, 1);
            case 5:
                return DateUtil.addMinToDate(startDate, 5);
            case 15:
                return DateUtil.addMinToDate(startDate, 15);
            case 30:
                return DateUtil.addMinToDate(startDate, 30);
            case 60:
                return DateUtil.addMinToDate(startDate, 60);
            case 1440:
                return DateUtil.addDaysToDate(startDate, 1);
            case 10080:
                return DateUtil.addDaysToDate(startDate, 7);
            case 30000:
                return DateUtil.addMonthsToDate(startDate, 1);
            case 525600:
                return DateUtil.addMonthsToDate(startDate, 12);
        }
        return null;
    }
}
