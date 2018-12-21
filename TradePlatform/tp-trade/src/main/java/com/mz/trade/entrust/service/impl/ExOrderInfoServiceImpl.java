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
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.trade.account.dao.AppAccountDao;
import com.mz.trade.account.dao.AppColdAccountRecordDao;
import com.mz.trade.account.dao.AppHotAccountRecordDao;
import com.mz.trade.account.dao.ExDigitalmoneyAccountDao;
import com.mz.trade.account.dao.ExDmColdAccountRecordDao;
import com.mz.trade.account.dao.ExDmHotAccountRecordDao;
import com.mz.trade.comparator.AscBigDecimalComparator;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
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

    @Resource(name = "exOrderInfoDao")
    @Override
    public void setDao(BaseDao<ExOrderInfo, Long> dao) {
        super.dao = dao;
    }

    @Resource(name = "exOrderService")
    public ExOrderService exOrderService;

    @Resource
    public ExEntrustService exEntrustService;
    @Autowired
    private RedisService redisService;
    @Resource
    private ExOrderInfoService exOrderInfoService;
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
    private ExDmColdAccountRecordService exDmColdAccountRecordService;
    @Resource
    private ExDmHotAccountRecordService exDmHotAccountRecordService;
    @Resource
    private AppColdAccountRecordService appColdAccountRecordService;
    @Resource
    private AppHotAccountRecordService appHotAccountRecordService;
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
        String transactionNum = IdGenerate.transactionNum(NumConstant.Ex_Order);
        exOrderInfo.setOrderNum("T" + transactionNum.substring(2, transactionNum.length()));
        exOrderInfo.setTransactionCount(tradeCount);
        exOrderInfo.setTransactionPrice(tradePrice);
        exOrderInfo.setTransactionSum(tradePrice.multiply(tradeCount));
        exOrderInfo.setTransactionTime(new Date());
        exOrderInfo.setOrderTimestapm(exOrderInfo.getTransactionTime().getTime());
        exOrderInfo.setTransactionBuyFeeRate(buyExEntrust.getTransactionFeeRate());
        exOrderInfo.setTransactionSellFeeRate(sellentrust.getTransactionFeeRate());
        exOrderInfo.setTransactionBuyFee(exOrderInfo.getTransactionCount().multiply(exOrderInfo.getTransactionBuyFeeRate()).divide(new BigDecimal("100")));
        exOrderInfo.setTransactionSellFee(exOrderInfo.getTransactionSum().multiply(exOrderInfo.getTransactionSellFeeRate()).divide(new BigDecimal("100")));
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
    public ExOrder createExOrder(ExOrderInfo exOrderInfo) {

        // 订单开始
        ExOrder exOrder = new ExOrder();
        exOrder.setOrderNum(exOrderInfo.getOrderNum());
        exOrder.setTransactionTime(exOrderInfo.getTransactionTime());
        exOrder.setOrderTimestapm(exOrderInfo.getOrderTimestapm());
        exOrder.setSaasId(exOrderInfo.getSaasId());
        exOrder.setCurrencyType(exOrderInfo.getCurrencyType());
        exOrder.setWebsite(exOrderInfo.getWebsite());
        exOrder.setTransactionCount(exOrderInfo.getTransactionCount());
        exOrder.setTransactionPrice(exOrderInfo.getTransactionPrice());
        exOrder.setTransactionSum(exOrderInfo.getTransactionSum());
        exOrder.setCoinCode(exOrderInfo.getCoinCode());
        exOrder.setWebsite(exOrderInfo.getWebsite());
        exOrder.setCurrencyType(exOrderInfo.getCurrencyType());
        exOrder.setProductName(exOrderInfo.getProductName());
        exOrder.setInOrOutTransaction(exOrderInfo.getInOrOutTransaction());
        exOrder.setFixPriceCoinCode(exOrderInfo.getFixPriceCoinCode());
        exOrder.setFixPriceType(exOrderInfo.getFixPriceType());

        // 订单结束
        return exOrder;

    }

    @Override
    public void redisToMysql() {
        long start = System.currentTimeMillis();
        // 委托信息入库
        Map<String, EntrustTrade> map = new HashMap<String, EntrustTrade>();
        long start4 = System.currentTimeMillis();
        Set<String> keysTradeDealEntrustChange = redisService.noPerkeys(ExchangeDataCacheRedis.TradeDealEntrustChange + ":");
        long end4 = System.currentTimeMillis();
        //	LogFactory.info("TradeDealEntrustChange入库总耗时：" + (end4 - start4) + "ms");
        List<BigDecimal> list2 = new ArrayList<BigDecimal>();
        Iterator<String> iterator1 = keysTradeDealEntrustChange.iterator();
        while (iterator1.hasNext()) {
            String keystr = iterator1.next();
            BigDecimal ks = new BigDecimal(keystr.split(":")[2]);
            list2.add(ks);
        }
        Collections.sort(list2, new AscBigDecimalComparator());
        long start5 = System.currentTimeMillis();
        for (BigDecimal l : list2) {
            String keystr = ExchangeDataCacheRedis.TradeDealEntrustChange + ":" + l;

            List<EntrustTrade> entrustTradeSlist = JSON.parseArray(redisService.get(keystr), EntrustTrade.class);
            if (null != entrustTradeSlist) {
                for (EntrustTrade es : entrustTradeSlist) {
                    map.put(es.getEntrustNum(), es);
                }
            }
            //	redisService.delete(keystr);
        }
        long end5 = System.currentTimeMillis();
//		LogFactory.info("TradeDealEntrustChange入库总耗时：" + (end5 - start5) + "ms");
        List<EntrustTrade> entrustlisted = new ArrayList<EntrustTrade>(map.values());
        if (null != entrustlisted && entrustlisted.size() > 0) {
            List<ExEntrust> entrustupdatelist = exEntrustDao.getExEntrustListByNumstoMysql(entrustlisted);
            List<EntrustTrade> entrustnewlist = new ArrayList<EntrustTrade>();
            if(null != entrustupdatelist && entrustupdatelist.size()>0){
                int k = 0;
                int size = entrustlisted.size();
                while (k < size) {
                    int i = 0;
                    EntrustTrade entrusted = entrustlisted.get(k);
                    for (ExEntrust entrustUpdate : entrustupdatelist) {
                        if (entrusted.getEntrustNum().equals(entrustUpdate.getEntrustNum())) {
                            entrustUpdate.setStatus(entrusted.getStatus());
                            entrustUpdate.setSurplusEntrustCount(entrusted.getSurplusEntrustCount());
                            entrustUpdate.setTransactionSum(entrusted.getTransactionSum());
                            entrustUpdate.setTransactionFee(entrusted.getTransactionFee());
                            if(null==entrusted.getProcessedPrice()){
                                entrustUpdate.setProcessedPrice(BigDecimal.ZERO);
                            }else{
                                entrustUpdate.setProcessedPrice(entrusted.getProcessedPrice());
                            }

                            entrustUpdate.setModified(new Date());
                            i++;
                            break;
                        }
                    }
                    if (i == 0) {
                        if(null==entrusted.getProcessedPrice()){
                            entrusted.setProcessedPrice(new BigDecimal("0"));
                        }
                        entrustnewlist.add(entrusted);
                    }
                    k++;
                }
            }else{
                entrustnewlist=entrustlisted;
            }

            if (null != entrustupdatelist && entrustupdatelist.size() > 0) {//
                long start1 = System.currentTimeMillis();

                exEntrustDao.updateExEntrust(entrustupdatelist);//
                long end1 = System.currentTimeMillis();
                //LogFactory.info("entrustupdatelist入库总耗时：" + (end1 - start1) + "ms");
            }
            //	LogFactory.info("entrustupdatelist.size=" + entrustupdatelist.size());
            if (null != entrustnewlist && entrustnewlist.size() > 0) {//
                long start2 = System.currentTimeMillis();
                // LogFactory.info("entrustnewlist.size()=="+entrustnewlist.size());
                exEntrustDao.insertEnEntrustTrade(entrustnewlist);
                long end2 = System.currentTimeMillis();
                //	LogFactory.info("entrustnewlist入库总耗时：" + (end2 - start2) + "ms");
            }
            //	LogFactory.info("entrustnewlist.size=" + entrustupdatelist.size());
        }


/*
		for (Map.Entry<String, EntrustTrade> entry : map.entrySet()) {
			EntrustTrade e = entry.getValue();
			ExEntrust exEntrust = exEntrustDao.getExEntrustByentrustNum(e.getEntrustNum());
			if (null == exEntrust) {
				ExEntrust exEntrust1 = JSON.parseObject(JSON.toJSONString(e), ExEntrust.class);
				exEntrust1.setWebsite("cn");
				exEntrust1.setCurrencyType("cny");
				exEntrustService.save(exEntrust1);
			} else {
				exEntrust.setSurplusEntrustCount(e.getSurplusEntrustCount());
				exEntrust.setStatus(e.getStatus());
				exEntrust.setTransactionSum(e.getTransactionSum());
				 exEntrust.setTransactionFee(e.getTransactionFee());
				 exEntrust.setProcessedPrice(e.getProcessedPrice());
				// exEntrust.setTransactionTime(transactionTime);
				exEntrustService.update(exEntrust);
			}

		}*/


        // 成交信息入库
        List<ExOrderInfo> eExOrderInfolistss = new ArrayList<ExOrderInfo>();
        Set<String> keysTradeDealOrderInfoChange = redisService.noPerkeys(ExchangeDataCacheRedis.TradeDealOrderInfoChange + ":");
        Iterator<String> iteratorTradeDealOrderInfoChange = keysTradeDealOrderInfoChange.iterator();
        while (iteratorTradeDealOrderInfoChange.hasNext()) {
            String keystr = iteratorTradeDealOrderInfoChange.next();
            List<ExOrderInfo> accountaddSlist = JSON.parseArray(redisService.get(keystr), ExOrderInfo.class);
            if (null != accountaddSlist) {
                eExOrderInfolistss.addAll(accountaddSlist);
            }
            //redisService.delete(keystr);
        }
        if (null != eExOrderInfolistss && eExOrderInfolistss.size() > 0) {
            long start3 = System.currentTimeMillis();
            exOrderInfoDao.insertExorderInfos(eExOrderInfolistss);
            long end3 = System.currentTimeMillis();
            //	LogFactory.info("eExOrderInfolistss入库总耗时：" + (end3 - start3) + "ms");
        }

        //	LogFactory.info("eExOrderInfolistss.size=" + eExOrderInfolistss.size());


        //最后才删
        Iterator<String> keysTradeDealEntrustChangedelete = keysTradeDealEntrustChange.iterator();
        while (keysTradeDealEntrustChangedelete.hasNext()) {
            String keystr = keysTradeDealEntrustChangedelete.next();
            redisService.delete(keystr);
        }

        Iterator<String> keysTradeDealOrderInfoChangedelete = keysTradeDealOrderInfoChange.iterator();
        while (keysTradeDealOrderInfoChangedelete.hasNext()) {
            String keystr = keysTradeDealOrderInfoChangedelete.next();
            redisService.delete(keystr);
        }

        long end = System.currentTimeMillis();
        long time=end - start;
        if(time>800){
            LogFactory.info("redis(委托单和成交单)入库总耗时：" + (time) + "ms");
        }
    }

    @Override
    public void redisToredisLog() {	// 资金流水入库
        long start = System.currentTimeMillis();
        Set<Long> setaccount = new HashSet<Long>();
        Set<Long> setaccountcoin = new HashSet<Long>();
        List<Accountadd> accountaddSlistss = new ArrayList<Accountadd>();
        Set<String> keysTradeDealAccountChange = redisService.noPerkeys(ExchangeDataCacheRedis.TradeDealAccountChange + ":");
        Iterator<String> iteratorTradeDealAccountChange = keysTradeDealAccountChange.iterator();
        while (iteratorTradeDealAccountChange.hasNext()) {
            String keystr = iteratorTradeDealAccountChange.next();
            List<Accountadd> accountaddSlist = JSON.parseArray(redisService.get(keystr), Accountadd.class);
            if (null != accountaddSlist) {
                accountaddSlistss.addAll(accountaddSlist);
            }
            //redisService.delete(keystr);
        }
        List<AppHotAccountRecord> listahar=new ArrayList<AppHotAccountRecord>();
        List<AppColdAccountRecord> listacar=new ArrayList<AppColdAccountRecord>();
        List<ExDmHotAccountRecord> listehar=new ArrayList<ExDmHotAccountRecord>();
        List<ExDmColdAccountRecord> listedcar=new ArrayList<ExDmColdAccountRecord>();
        if (null != accountaddSlistss) {
            for (Accountadd accountadd : accountaddSlistss) {
                if (accountadd.getAcccountType().equals(0)) { // 资金账户
                    AppAccount appAccount = appAccountService.get(accountadd.getAccountId());
                    if(null!=appAccount){
                        if (accountadd.getMonteyType().equals(1)) { // 热账户
                            if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == -1) {
                                AppHotAccountRecord appHotAccountRecord = appAccountService.createHotRecord(2, appAccount, BigDecimal.ZERO.subtract(accountadd.getMoney()), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listahar.add(appHotAccountRecord);
                            } else if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == 1) {
                                AppHotAccountRecord appHotAccountRecord = appAccountService.createHotRecord(1, appAccount, accountadd.getMoney(), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listahar.add(appHotAccountRecord);
                            }

                        } else { // 冷账户
                            if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == -1) {
                                AppColdAccountRecord AppColdAccountRecord = appAccountService.createColdRecord(2, appAccount, BigDecimal.ZERO.subtract(accountadd.getMoney()), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listacar.add(AppColdAccountRecord);
                            } else if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == 1) {
                                AppColdAccountRecord AppColdAccountRecord = appAccountService.createColdRecord(1, appAccount, accountadd.getMoney(), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listacar.add(AppColdAccountRecord);
                            }
                        }
                        setaccount.add(appAccount.getId());
                    }

                } else {// 币账户
                    ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService.get(accountadd.getAccountId());
                    if(null!=exDigitalmoneyAccount){
                        if (accountadd.getMonteyType().equals(1)) { // 热账户
                            if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == -1) {
                                ExDmHotAccountRecord exDmHotAccountRecord = exDigitalmoneyAccountService.createHotRecord(2, exDigitalmoneyAccount, BigDecimal.ZERO.subtract(accountadd.getMoney()), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listehar.add(exDmHotAccountRecord);
                            } else if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == 1) {
                                ExDmHotAccountRecord exDmHotAccountRecord = exDigitalmoneyAccountService.createHotRecord(1, exDigitalmoneyAccount, accountadd.getMoney(), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listehar.add(exDmHotAccountRecord);
                            }
                        } else { // 冷账户
                            if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == -1) {
                                ExDmColdAccountRecord exDmColdAccountRecord = exDigitalmoneyAccountService.createColdRecord(2, exDigitalmoneyAccount, BigDecimal.ZERO.subtract(accountadd.getMoney()), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listedcar.add(exDmColdAccountRecord);
                            } else if (accountadd.getMoney().compareTo(BigDecimal.ZERO) == 1) {
                                ExDmColdAccountRecord exDmColdAccountRecord = exDigitalmoneyAccountService.createColdRecord(1, exDigitalmoneyAccount, accountadd.getMoney(), accountadd.getTransactionNum(), accountadd.getRemarks());
                                listedcar.add(exDmColdAccountRecord);
                            }
                        }

                        setaccountcoin.add(exDigitalmoneyAccount.getId());
                    }

                }
            }
        }
        //批量入库流水记录
        if (null != listahar && listahar.size() > 0) {
            appHotAccountRecordDao.insertRecord(listahar);
        }
        if (null != listacar && listacar.size() > 0) {
            appColdAccountRecordDao.insertRecord(listacar);
        }
        if (null != listehar && listehar.size() > 0) {
            exDmHotAccountRecordDao.insertRecord(listehar);
        }
        if (null != listedcar && listedcar.size() > 0) {
            exDmColdAccountRecordDao.insertRecord(listedcar);
        }



        // 账户批量入库
        Iterator<Long> iteratora = setaccount.iterator();
        List<AppAccountRedis> lista=new ArrayList<AppAccountRedis>();
        while (iteratora.hasNext()) {
            Long id = iteratora.next();
            AppAccountRedis appAccountredis = appAccountService.getAppAccountByRedis(id.toString());
            appAccountredis.setHotMoney(appAccountredis.getHotMoney());
            appAccountredis.setColdMoney(appAccountredis.getColdMoney());
            lista.add(appAccountredis);
        }
        if(null!=lista&&lista.size()>0){
            appAccountDao.updateAppAccount(lista);
        }
        Iterator<Long> iteratorc = setaccountcoin.iterator();
        List<ExDigitalmoneyAccountRedis> listd=new ArrayList<ExDigitalmoneyAccountRedis>();
        while (iteratorc.hasNext()) {
            Long id = iteratorc.next();
            ExDigitalmoneyAccountRedis exDigitalmoneyAccountredis = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(id.toString());
            exDigitalmoneyAccountredis.setHotMoney(exDigitalmoneyAccountredis.getHotMoney());
            exDigitalmoneyAccountredis.setColdMoney(exDigitalmoneyAccountredis.getColdMoney());
            listd.add(exDigitalmoneyAccountredis);
        }
        if(null!=listd&&listd.size()>0){
            exDigitalmoneyAccountDao.updateExDigitalmoneyAccount(listd);
        }




        Iterator<String> iteratorTradeDealAccountChangedelete = keysTradeDealAccountChange.iterator();
        while (iteratorTradeDealAccountChangedelete.hasNext()) {
            String keystr = iteratorTradeDealAccountChangedelete.next();
            redisService.delete(keystr);
        }
        long end = System.currentTimeMillis();
        long time=end - start;
        if(time>800){
            LogFactory.info("accountredis（账户和资金流水）入库总耗时：" + (time) + "ms");
        }

    }

    @Override
    public void redisToMysqlmq() {
        MessageProducer messageProducer = (MessageProducer) ContextUtil.getBean("messageProducer");
        messageProducer.redisToMysql("111");
        //System.out.println("发送定时消息");
    }

    @Override
    public void redisToredisLogmq() {
        MessageProducer messageProducer = (MessageProducer) ContextUtil.getBean("messageProducer");
        messageProducer.redisToRedisLog("333");

    }
}
