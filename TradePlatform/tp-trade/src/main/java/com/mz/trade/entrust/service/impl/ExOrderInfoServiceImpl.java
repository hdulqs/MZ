/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.trade.entrust.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.account.fund.model.AppAccount;
import com.mz.account.fund.model.AppColdAccountRecord;
import com.mz.account.fund.model.AppHotAccountRecord;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.redis.common.utils.RedisService;
import com.mz.trade.account.service.AppAccountService;
import com.mz.trade.account.service.AppColdAccountRecordService;
import com.mz.trade.account.service.AppHotAccountRecordService;
import com.mz.trade.account.service.ExDigitalmoneyAccountService;
import com.mz.trade.account.service.ExDmColdAccountRecordService;
import com.mz.trade.account.service.ExDmHotAccountRecordService;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrder;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.sys.ContextUtil;
import com.mz.trade.account.dao.AppAccountDao;
import com.mz.trade.account.dao.AppColdAccountRecordDao;
import com.mz.trade.account.dao.AppHotAccountRecordDao;
import com.mz.trade.account.dao.ExDigitalmoneyAccountDao;
import com.mz.trade.account.dao.ExDmColdAccountRecordDao;
import com.mz.trade.account.dao.ExDmHotAccountRecordDao;
import com.mz.trade.entrust.dao.ExEntrustDao;
import com.mz.trade.entrust.dao.ExOrderInfoDao;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.entrust.service.ExOrderService;
import com.mz.trade.mq.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.AppAccountRedis;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import com.mz.trade.redis.model.ExchangeDataCacheRedis;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Gao Mimi
 * @Date : 2016年4月12日 下午4:45:50
 */
@Service("exOrderInfoService")
public class ExOrderInfoServiceImpl extends BaseServiceImpl<ExOrderInfo, Long> implements ExOrderInfoService {

    Logger logger = Logger.getLogger(ExOrderInfoServiceImpl.class);

	@Resource(name = "exOrderInfoDao")
	@Override
	public void setDao(BaseDao<ExOrderInfo, Long> dao) {
		super.dao = dao;
	}

	@Autowired
	private RedisService redisService;
	@Resource
	public ExDigitalmoneyAccountService exDigitalmoneyAccountService;
	@Resource
	public AppAccountService appAccountService;
	@Resource
	public ExDigitalmoneyAccountDao exDigitalmoneyAccountDao;
	@Resource
	public AppAccountDao appAccountDao;
	@Resource
	private ExEntrustDao exEntrustDao;
	@Resource
	private ExOrderInfoDao exOrderInfoDao;
	@Resource
	private ExDmColdAccountRecordDao exDmColdAccountRecordDao;
	@Resource
	private ExDmHotAccountRecordDao exDmHotAccountRecordDao;
	@Resource
	private AppColdAccountRecordDao appColdAccountRecordDao;
	@Resource
	private AppHotAccountRecordDao appHotAccountRecordDao;

	public ExOrderInfo createExOrderInfo(Integer type, EntrustTrade buyExEntrust, EntrustTrade sellentrust, BigDecimal tradeCount, BigDecimal tradePrice) {
		// 订单开始详细
		ExOrderInfo exOrderInfo = new ExOrderInfo();
		exOrderInfo.setType(type);
        // TODO: 10/24/18 这里的id会产生重复的交易流水号
		String transactionNum = IdGenerate.transactionNum(NumConstant.Ex_Order);
		exOrderInfo.setOrderNum("T" + transactionNum.substring(2));
		exOrderInfo.setTransactionCount(tradeCount);
		exOrderInfo.setTransactionPrice(tradePrice);
		exOrderInfo.setTransactionSum(tradePrice.multiply(tradeCount));
		exOrderInfo.setTransactionTime(new Date());
		exOrderInfo.setOrderTimestapm(exOrderInfo.getTransactionTime().getTime());
		exOrderInfo.setTransactionBuyFeeRate(buyExEntrust.getTransactionFeeRate());
		exOrderInfo.setTransactionSellFeeRate(sellentrust.getTransactionFeeRate());
		exOrderInfo.setTransactionBuyFee(exOrderInfo.getTransactionCount().multiply(exOrderInfo.getTransactionBuyFeeRate()).divide(BigDecimal.valueOf(100L)));
		exOrderInfo.setTransactionSellFee(exOrderInfo.getTransactionSum().multiply(exOrderInfo.getTransactionSellFeeRate()).divide(BigDecimal.valueOf(100L)));
		exOrderInfo.setBuyCustomId(buyExEntrust.getCustomerId());
		exOrderInfo.setSellCustomId(sellentrust.getCustomerId());
		exOrderInfo.setWebsite("cn");
		exOrderInfo.setCurrencyType("cny");
		exOrderInfo.setFixPriceCoinCode(buyExEntrust.getFixPriceCoinCode());
		exOrderInfo.setFixPriceType(buyExEntrust.getFixPriceType());
		exOrderInfo.setCoinCode(buyExEntrust.getCoinCode());
		// 订单开始详细
		exOrderInfo.setBuyEntrustNum(buyExEntrust.getEntrustNum());
		exOrderInfo.setSellEntrustNum(sellentrust.getEntrustNum());
		exOrderInfo.setBuyUserName(buyExEntrust.getUserName());
		exOrderInfo.setSellUserName(sellentrust.getUserName());
		exOrderInfo.setTransactionTime(new Date());
		exOrderInfo.setOrderTimestapm(exOrderInfo.getTransactionTime().getTime());
		// 订单结束详细
		return exOrderInfo;
	}

	@Override
    public void redisToMysql() {
        long start = System.currentTimeMillis();
        this.exEntrustToMysql();
        long enTrustEndTime = System.currentTimeMillis() - start;
        this.exOrderInfoToMysql();
        long time = System.currentTimeMillis() - start;
        if (time > 800) {
            logger.info("redis(委托单和成交单)入库总耗时：" + (time) + "ms\n" +
                    "委托单耗时：" + enTrustEndTime +
                    "成交单耗时：" + (time - enTrustEndTime));
        }
    }

    /**
     * 更新委托单信息
     * 将{@link ExchangeDataCacheRedis#TradeDealEntrustChange}中的信息，录入数据库
     */
    private synchronized void exEntrustToMysql() {
        // 获取需要入库的订单编号
        Set<String> tradeDealEntrustChangeKeys = redisService.noPerkeys(ExchangeDataCacheRedis.TradeDealEntrustChange + ":");
        if (tradeDealEntrustChangeKeys == null || tradeDealEntrustChangeKeys.size() < 1) {
            return;
        }
        Set<String> needDeleteKeys = new HashSet<>();
        // 在入库的间隔内，a委托未全部交易，b委托又和a委托撮合，导致a委托在deal:tradeDealEntrustChange：*中有过多的记录，此处需要过滤
        // 当前的Map用于过滤，指存储deal:tradeDealEntrustChange：*中数字大的委托记录，上面排序就是为了在map中只存储一笔委托最新的信息
        Map<String, EntrustTrade> dealEntrustMap = new HashMap<>();
        for (String key : tradeDealEntrustChangeKeys) { // 不要在此处删除，录入数据库后再删除
            List<EntrustTrade> entrustTradeSlist = JSON.parseArray(redisService.get(key), EntrustTrade.class);
            if (null != entrustTradeSlist && entrustTradeSlist.size() > 0) {
                for (EntrustTrade entrustTrade : entrustTradeSlist) {
                    EntrustTrade mapEnTrustTrade = dealEntrustMap.get(entrustTrade.getEntrustNum());
                    if (mapEnTrustTrade == null) {
                        dealEntrustMap.put(entrustTrade.getEntrustNum(), entrustTrade);
                        needDeleteKeys.add(key);
                    } else {
                        if (mapEnTrustTrade.getStatus() < entrustTrade.getStatus()  // 状态改变
                                || mapEnTrustTrade.getSurplusEntrustCount().compareTo(entrustTrade.getSurplusEntrustCount()) > 0) { // 资金发生改变
                            dealEntrustMap.put(entrustTrade.getEntrustNum(), entrustTrade);
                            needDeleteKeys.add(key);
                        } else {
                            logger.info("有可能有错误的订单: " + entrustTrade.getEntrustNum() + " status: " + entrustTrade.getStatus());
                        }
                    }
                }
            }
        }

        List<EntrustTrade> entrustTradeListed = new ArrayList<>(dealEntrustMap.values());
        if (entrustTradeListed.size() == 0) {
            return;
        }

        // 获取需要更新状态的委托单信息，只有id和entrustNum
        List<ExEntrust> entrustUpdateList = exEntrustDao.getExEntrustListByNumstoMysql(entrustTradeListed);
        if (entrustUpdateList == null || entrustUpdateList.size() == 0) {
            for (EntrustTrade entrustTrade: entrustTradeListed) {
                if (null == entrustTrade.getProcessedPrice()) {
                    entrustTrade.setProcessedPrice(BigDecimal.ZERO);
                }
            }

            exEntrustDao.insertEnEntrustTrade(entrustTradeListed);
            redisService.delete(needDeleteKeys.toArray(new String[0]));
            return;
        }

        // 根据已有的委托单信息，然后更新已有的委托单信息
        List<EntrustTrade> newEntrustTradeList = new ArrayList<>();
        outLoop:
        for (EntrustTrade entrustTrade: entrustTradeListed) {
            if (null == entrustTrade.getProcessedPrice()) {
                entrustTrade.setProcessedPrice(BigDecimal.ZERO);
            }

            for (ExEntrust exEntrust : entrustUpdateList) {
                if (entrustTrade.getEntrustNum().equals(exEntrust.getEntrustNum())) {
                    exEntrust.setModified(new Date());
                    exEntrust.setStatus(entrustTrade.getStatus());
                    exEntrust.setTransactionSum(entrustTrade.getTransactionSum());
                    exEntrust.setTransactionFee(entrustTrade.getTransactionFee());
                    exEntrust.setProcessedPrice(entrustTrade.getProcessedPrice());
                    exEntrust.setSurplusEntrustCount(entrustTrade.getSurplusEntrustCount());
                    continue outLoop;
                }
            }
            newEntrustTradeList.add(entrustTrade);
        }

        // 查询重复的订单的id，并删除重复的订单
        for (Iterator<ExEntrust> it = entrustUpdateList.iterator(); it.hasNext();) {
            ExEntrust exEntrust = it.next();
            if (exEntrust.getStatus() == null) {
                exEntrustDao.deleteByPrimaryKey(exEntrust.getId());
                // TODO: 10/22/18 这里需要某种通知管理员的机制
                logger.info("`，委托单号为：" + exEntrust.getEntrustNum());
                it.remove();
            }
        }
        // 更新数据库中已有的数据
        exEntrustDao.updateExEntrust(entrustUpdateList);

        if (newEntrustTradeList.size() > 0) {   // 插入新有的数据
            exEntrustDao.insertEnEntrustTrade(newEntrustTradeList);
        }
        // 删除redis中委托订单
        redisService.delete(needDeleteKeys.toArray(new String[0]));
    }

    /**
     * 更新交易记录信息
     * 将{@link ExchangeDataCacheRedis#TradeDealOrderInfoChange}中的信息，录入数据库
     */
    private synchronized void exOrderInfoToMysql() {
        // 成交信息入库
        List<ExOrderInfo> eExOrderInfolist = new ArrayList<ExOrderInfo>();
        Set<String> keysTradeDealOrderInfoChange = redisService.noPerkeys(ExchangeDataCacheRedis.TradeDealOrderInfoChange + ":");
        if (keysTradeDealOrderInfoChange == null || keysTradeDealOrderInfoChange.size() < 1) {
            return;
        }
        for (String keyStr : keysTradeDealOrderInfoChange) {
            List<ExOrderInfo> accountaddSlist = JSON.parseArray(redisService.get(keyStr), ExOrderInfo.class);
            if (null != accountaddSlist) {
                eExOrderInfolist.addAll(accountaddSlist);
            }
        }
        if (eExOrderInfolist.size() > 0) {
            exOrderInfoDao.insertExorderInfos(eExOrderInfolist);
        }

        redisService.delete(keysTradeDealOrderInfoChange.toArray(new String[0]));
    }

	@Override
	public synchronized void redisToredisLog() {	// 资金流水入库
		long start = System.currentTimeMillis();
		Set<Long> accountIds = new HashSet<>(); // 资金账户Id
		Set<Long> coinAccountIds = new HashSet<>(); // 币账户Id
		List<Accountadd> accountaddSlistss = new ArrayList<>(); // 所有需要入库的币流水记录
		Set<String> keysTradeDealAccountChange = redisService.noPerkeys(ExchangeDataCacheRedis.TradeDealAccountChange + ":");
		if (keysTradeDealAccountChange == null || keysTradeDealAccountChange.size() == 0) {
		    return;
        }
        for (String key : keysTradeDealAccountChange) { // 不要在此处删除，录入数据库后再删除
            List<Accountadd> accountaddSlist = JSON.parseArray(redisService.get(key), Accountadd.class);
            if (null != accountaddSlist) {
                accountaddSlistss.addAll(accountaddSlist);
            }
        }

		List<AppHotAccountRecord> appHotAccountRecordArrayList= new ArrayList<>();      // 资金热钱记录
		List<AppColdAccountRecord> appColdAccountRecordArrayList= new ArrayList<>();    // 资金冷钱记录
		List<ExDmHotAccountRecord> exDmHotAccountRecordArrayList= new ArrayList<>();    // 币热钱记录
		List<ExDmColdAccountRecord> exDmColdAccountRecordArrayList= new ArrayList<>();  // 币冷钱记录
        for (Accountadd accountadd : accountaddSlistss) {
            if (accountadd.getMoney() == null || BigDecimal.ZERO.compareTo(accountadd.getMoney()) == 0) {
                continue;
            }
            int recordType = 1;
            if (accountadd.getMoney().compareTo(BigDecimal.ZERO) < 0) {
                recordType = 2;
            }
            if (accountadd.getAcccountType().equals(0)) { // 资金账户
                AppAccount appAccount = appAccountService.get(accountadd.getAccountId());
                if (appAccount == null) {
                    continue;
                }
                if (accountadd.getMonteyType().equals(1)) { // 热账户
                    AppHotAccountRecord appHotAccountRecord = appAccountService.createHotRecord(recordType, appAccount, accountadd.getMoney(), accountadd.getTransactionNum(), accountadd.getRemarks());
                    appHotAccountRecordArrayList.add(appHotAccountRecord);
                } else { // 冷账户
                    AppColdAccountRecord AppColdAccountRecord = appAccountService.createColdRecord(recordType, appAccount, accountadd.getMoney(), accountadd.getTransactionNum(), accountadd.getRemarks());
                    appColdAccountRecordArrayList.add(AppColdAccountRecord);
                }
                accountIds.add(appAccount.getId());
            } else {// 币账户
                ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(accountadd.getAccountId());
                if (exDigitalmoneyAccount == null) {
                    continue;
                }
                if (accountadd.getMonteyType().equals(1)) { // 热账户
                    ExDmHotAccountRecord exDmHotAccountRecord= exDigitalmoneyAccountService.createHotRecord(recordType, exDigitalmoneyAccount, accountadd.getMoney().abs(), accountadd.getTransactionNum(), accountadd.getRemarks());
                    exDmHotAccountRecordArrayList.add(exDmHotAccountRecord);
                } else { // 冷账户
                    ExDmColdAccountRecord exDmColdAccountRecord = exDigitalmoneyAccountService.createColdRecord(recordType, exDigitalmoneyAccount, accountadd.getMoney().abs(), accountadd.getTransactionNum(), accountadd.getRemarks());
                    exDmColdAccountRecordArrayList.add(exDmColdAccountRecord);
                }
                coinAccountIds.add(exDigitalmoneyAccount.getId());
            }
        }
        //批量入库流水记录
		if (appHotAccountRecordArrayList.size() > 0) {
			appHotAccountRecordDao.insertRecord(appHotAccountRecordArrayList);
		}
		if (appColdAccountRecordArrayList.size() > 0) {
			appColdAccountRecordDao.insertRecord(appColdAccountRecordArrayList);
		}
		if (exDmHotAccountRecordArrayList.size() > 0) {
			exDmHotAccountRecordDao.insertRecord(exDmHotAccountRecordArrayList);
		}
		if (exDmColdAccountRecordArrayList.size() > 0) {
			exDmColdAccountRecordDao.insertRecord(exDmColdAccountRecordArrayList);
		}
		// 账户批量入库
        List<AppAccountRedis> lista=new ArrayList<AppAccountRedis>();
        for (Long accontId : accountIds) {
            AppAccountRedis appAccountredis = appAccountService.getAppAccountByRedis(accontId.toString());
            lista.add(appAccountredis);
        }
		if(lista.size() > 0){
			appAccountDao.updateAppAccount(lista);
		}
        List<ExDigitalmoneyAccountRedis> listd=new ArrayList<ExDigitalmoneyAccountRedis>();
        for (Long coinAccountId : coinAccountIds) {
            ExDigitalmoneyAccountRedis exDigitalmoneyAccountredis = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(coinAccountId.toString());
            listd.add(exDigitalmoneyAccountredis);
        }
		if(listd.size() > 0){
			exDigitalmoneyAccountDao.updateExDigitalmoneyAccount(listd);
		}
        redisService.delete(keysTradeDealAccountChange.toArray(new String[0]));
        long time = System.currentTimeMillis() - start;
        if (time > 800) {
            logger.info("accountredis（账户和资金流水）入库总耗时：" + (time) + "ms");
        }
	}

    @Override
    public void redisToMysqlmq() {
        try {
            MessageProducer messageProducer = (MessageProducer) ContextUtil.getBean("messageProducer");
            messageProducer.redisToMysql("111");
        } catch (Exception e) {
            logger.info("redisToMysqlmq error.....");
        }
    }

    @Override
    public void redisToredisLogmq() {
        try {
            MessageProducer messageProducer = (MessageProducer) ContextUtil.getBean("messageProducer");
            messageProducer.redisToRedisLog("333");
        } catch (Exception e) {
            logger.info("redisToredisLogmq error.............");
        }
    }
}
