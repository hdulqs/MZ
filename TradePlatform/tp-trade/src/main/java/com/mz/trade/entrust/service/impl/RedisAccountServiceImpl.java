package com.mz.trade.entrust.service.impl;

import com.alibaba.fastjson.JSON;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.mz.account.fund.model.AppAccount;
import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.trade.account.service.AppAccountService;
import com.mz.trade.account.service.ExDigitalmoneyAccountService;
import com.mz.trade.comparator.AccountaddComparator;
import com.mz.trade.entrust.service.RedisAccountService;
import com.mz.trade.model.AccountRemarkEnum;
import com.mz.trade.model.AccountResultEnum;
import com.mz.trade.redis.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By lingyang on 11/17/18
 * Project: tp-trade
 */
public class RedisAccountServiceImpl implements RedisAccountService {

    private final Logger logger = LoggerFactory.getLogger(RedisAccountServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ExDigitalmoneyAccountService exDigitalmoneyAccountService;

    @Autowired
    private AppAccountService appAccountService;

    @Autowired
    private AppExceptionService appExceptionService;

    private String getTradeDealAccountChangeNum() {
        try (Jedis jedis = jedisPool.getResource()) {
            Long num = jedis.incr(ExchangeDataCacheRedis.TradeDealAccountChangeNum);
            return ExchangeDataCacheRedis.TradeDealAccountChange + ":" + num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public AccountResultEnum accountChange(List<Accountadd> accountaddList, final boolean negateCheck) {
        if (!negateCheck) {
            logger.warn("账户资金存在资金操作为负的风险");
        }
        if (accountaddList == null && accountaddList.size() == 0) {
            return AccountResultEnum.SUCCESS;
        }
        accountaddList.sort(new AccountaddComparator());
        try (Jedis jedis = jedisPool.getResource()) {
            // 开启一个事务
            Transaction transaction = jedis.multi();
            // todo lingyang transaction.watch()
            // 将入库的流水保存在redis中
            transaction.set(getTradeDealAccountChangeNum(), JSON.toJSONString(accountaddList));

            Long coinAccountId = null;
            ExDigitalmoneyAccountRedis coinAccount = null;
            Long appAcountId = null;
            AppAccountRedis appAccount = null;

            for (Accountadd accountadd : accountaddList) {
                if (accountadd.getAcccountType().equals(1)) {   // 数字货币账户
                    if (null == coinAccountId || accountadd.getAccountId().compareTo(coinAccountId) != 0) {
                        coinAccountTransaction(transaction, coinAccount);
                        coinAccount = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(accountadd.getAccountId().toString());
                        coinAccountId = accountadd.getAccountId();
                    }
                    if (null != coinAccount) {
                        if (accountadd.getMonteyType().equals(1)) {
                            BigDecimal finalCoinHotMoney = coinAccount.getHotMoney().add(accountadd.getMoney());
                            if (negateCheck && finalCoinHotMoney.compareTo(BigDecimal.ZERO) < 0) {
                                return AccountResultEnum.COIN_ACCOUNT_HOT_MONEY_NEGATE;
                            }
                            coinAccount.setHotMoney(finalCoinHotMoney);
                        } else {
                            BigDecimal finalCoinColdMoney = coinAccount.getColdMoney().add(accountadd.getMoney());
                            if (negateCheck && finalCoinColdMoney.compareTo(BigDecimal.ZERO) < 0) {
                                return AccountResultEnum.COIN_ACCOUNT_COLD_MONEY_NEGATE;
                            }
                            coinAccount.setColdMoney(finalCoinColdMoney);
                        }
                    } else {
                        return AccountResultEnum.COIN_ACCOUNT_NOT_ALIVE;
                    }
                } else {    // 法币账户
                    if (null == appAcountId || accountadd.getAccountId().compareTo(appAcountId) != 0) {
                        appAccountTransaction(transaction, appAccount);
                        appAccount = appAccountService.getAppAccountByRedis(accountadd.getAccountId().toString());
                        appAcountId = accountadd.getAccountId();
                    }
                    if (null != appAccount) {
                        if (accountadd.getMonteyType().equals(1)) {
                            BigDecimal finalAppAccountHotMoney = appAccount.getHotMoney().add(accountadd.getMoney());
                            if (negateCheck && finalAppAccountHotMoney.compareTo(BigDecimal.ZERO) < 0) {
                                return AccountResultEnum.ACCOUNT_HOT_MONEY_NEGATE;
                            }
                            appAccount.setHotMoney(finalAppAccountHotMoney);
                        } else {
                            BigDecimal finalAppAccountColdMoney = appAccount.getColdMoney().add(accountadd.getMoney());
                            if (negateCheck && finalAppAccountColdMoney.compareTo(BigDecimal.ZERO) < 0) {
                                return AccountResultEnum.ACCOUNT_COLD_MONEY_NEGATE;
                            }
                            appAccount.setColdMoney(appAccount.getColdMoney().add(accountadd.getMoney()));
                        }
                    } else {
                        return AccountResultEnum.ACCOUNT_NO_ALIVE;
                    }
                }
            }
            coinAccountTransaction(transaction, coinAccount);
            appAccountTransaction(transaction, appAccount);

            List<Object> list = transaction.exec();
            if (null == list || list.size() == 0) {
                return AccountResultEnum.SAVE_FAULT;
            }
            return AccountResultEnum.SUCCESS;
        } catch (Exception e) {
            logger.error("accountChange", e);
            AppException accountException = new AppException();
            accountException.setName("资金操作错误");
            accountException.setName("操作redis，修改账户资金时出错" + JSON.toJSONString(accountaddList));
            appExceptionService.save(accountException);
            e.printStackTrace();
        }
        return AccountResultEnum.FAULT;
    }

    @Override
    public boolean forzenByEntrust(EntrustTrade entrustTrade) {
        List<Accountadd> accountChangeList = new ArrayList<>();
        String transactionNum = entrustTrade.getEntrustNum();
        if (entrustTrade.getType().equals(1)) {// 买单
            if (entrustTrade.getFixPriceType().equals(0)) { // 定价真实货币
                BigDecimal freezeMoney = entrustTrade.getEntrustSum();
                // 重新计算冷热钱包的总额
                accountChangeList.add(getAccountadd(0, entrustTrade.getAccountId(), freezeMoney.negate(), 1, AccountRemarkEnum.TYPE1, transactionNum));
                accountChangeList.add(getAccountadd(0, entrustTrade.getAccountId(), freezeMoney, 2, AccountRemarkEnum.TYPE1, transactionNum));
            } else if (entrustTrade.getFixPriceType().equals(1)){// 定价虚拟货币
                BigDecimal freezeMoney = entrustTrade.getEntrustSum();
                accountChangeList.add(getAccountadd(1, entrustTrade.getAccountId(), freezeMoney.negate(), 1, AccountRemarkEnum.TYPE1, transactionNum));
                accountChangeList.add(getAccountadd(1, entrustTrade.getAccountId(), freezeMoney, 2, AccountRemarkEnum.TYPE1, transactionNum));
            }
        } else if (entrustTrade.getType().equals(2)) {//
            BigDecimal freezeMoney = entrustTrade.getEntrustCount();
            accountChangeList.add(getAccountadd(1, entrustTrade.getCoinAccountId(), freezeMoney.negate(), 1, AccountRemarkEnum.TYPE1, transactionNum));
            accountChangeList.add(getAccountadd(1, entrustTrade.getCoinAccountId(), freezeMoney, 2, AccountRemarkEnum.TYPE1, transactionNum));
        }
        return accountChange(accountChangeList, true) == AccountResultEnum.SUCCESS;
    }

    private void coinAccountTransaction(Transaction transaction, ExDigitalmoneyAccountRedis coinAccount) {
        // 数字货币的资金账户redis
        final String exDigitalmoneyAccountRedisHeader
                = "RedisDB:" + ExDigitalmoneyAccountRedis.class.getName().replace(".", ":");
        final RuntimeSchema<ExDigitalmoneyAccountRedis> coinAccountRSchema
                = RuntimeSchema.createFrom(ExDigitalmoneyAccountRedis.class);
        if (null != coinAccount) {
            byte[] bytes = ProtostuffIOUtil.toByteArray(coinAccount, coinAccountRSchema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            String key = exDigitalmoneyAccountRedisHeader + ":" + coinAccount.getId();
            transaction.watch(key.getBytes());
            transaction.set(key.getBytes(), bytes);
            logger.info("coin account id: " + coinAccount.getId()
                    + " hot money: <" + coinAccount.getHotMoney()
                    + ">, cold money: <" + coinAccount.getColdMoney() + ">");
        }
    }

    private void appAccountTransaction(Transaction transaction, AppAccountRedis appAccount) {
        // 法币的资金账户redis
        final String appAccountRedisHeader
                = "RedisDB:" + AppAccountRedis.class.getName().replace(".", ":");
        final RuntimeSchema<AppAccountRedis> appAccountRSchema
                = RuntimeSchema.createFrom(AppAccountRedis.class);
        if (null != appAccount) {
            byte[] appAccountBytes = ProtostuffIOUtil.toByteArray(appAccount, appAccountRSchema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            String key1 = appAccountRedisHeader + ":" + appAccount.getId();
            transaction.watch(key1.getBytes());
            transaction.set(key1.getBytes(), appAccountBytes);
            logger.info("app account id: " + appAccount.getId()
                    + " hot money: <" + appAccount.getHotMoney()
                    + ">, cold money: <" + appAccount.getColdMoney() + ">");
        }
    }

    /**
     * 获得redis法币账户
     * @return
     */
    private AppAccountRedis getAppAccount(long accountId) {
        RedisUtil<AppAccountRedis> appAccountRedisRedis = new RedisUtil<>(AppAccountRedis.class);
        AppAccountRedis appAccountRedis = appAccountRedisRedis.get(String.valueOf(accountId));
        return appAccountRedis;
    }

    /**
     * 获得redis数字货币账户
     * @return
     */
    private ExDigitalmoneyAccountRedis getExDigitalAccount(long digitalAccountId) {
        RedisUtil<ExDigitalmoneyAccountRedis> digitalmoneyAccountRedis = new RedisUtil<>(ExDigitalmoneyAccountRedis.class);
        ExDigitalmoneyAccountRedis exDigitalmoneyAccountRedis = digitalmoneyAccountRedis.get(String.valueOf(digitalAccountId));
        return exDigitalmoneyAccountRedis;
    }

    /**
     *
     * @param acccountType 0资金账号，1币账户
     * @param accountId 资金账户的id，{@link ExDigitalmoneyAccount#getId()}和 {@link AppAccount#getId()}
     * @param money
     * @param monteyType 1热账户，2，冷账号
     * @param remarks {@link AccountRemarkEnum}
     * @param transactionNum
     * @return
     */
    private Accountadd getAccountadd(Integer acccountType, Long accountId, BigDecimal money, Integer monteyType, AccountRemarkEnum remarks, String transactionNum) {
        Accountadd accountadd = new Accountadd();
        accountadd.setAcccountType(acccountType);
        accountadd.setMoney(money);
        accountadd.setAccountId(accountId);
        accountadd.setMonteyType(monteyType);
        accountadd.setTransactionNum(transactionNum);
        accountadd.setRemarks(remarks.getIndex());
        return accountadd;
    }

}
