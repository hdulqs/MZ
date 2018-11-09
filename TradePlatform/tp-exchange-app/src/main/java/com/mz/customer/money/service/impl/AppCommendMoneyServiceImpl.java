/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menwei
 * @version: V1.0
 * @Date: 2017-11-29 10:05:55
 */
package com.mz.customer.money.service.impl;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.money.model.AppCommendMoney;
import com.mz.customer.rebat.model.AppCommendRebat;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.money.dao.AppCommendMoneyDao;
import com.mz.customer.money.service.AppCommendMoneyService;
import com.mz.customer.rebat.service.AppCommendRebatService;
import com.mz.customer.trade.service.AppCommendTradeService;
import com.mz.front.redis.model.UserRedis;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import com.mz.web.remote.RemoteAppConfigService;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p> AppCommendMoneyService </p>
 * @author: menwei
 * @Date :          2017-11-29 10:05:55  
 */
@Service("appCommendMoneyService")
public class AppCommendMoneyServiceImpl extends BaseServiceImpl<AppCommendMoney, Long> implements AppCommendMoneyService {

    @Resource(name = "appCommendMoneyDao")
    @Override
    public void setDao(BaseDao<AppCommendMoney, Long> dao) {
        super.dao = dao;
    }
    
    @Resource(name = "appCommendMoneyDao")
    public AppCommendMoneyDao appCommendMoneyDao;

    private static final Logger log = LoggerFactory.getLogger(AppCommendMoneyServiceImpl.class);
    @Resource
    private MessageProducer messageProducer;

    @Resource(name = "appCommendTradeService")
    public AppCommendTradeService appCommendTradeService;

    @Resource(name = "appCommendMoneyService")
    public AppCommendMoneyService appCommendMoneyService;

    @Resource
    private AppCommendRebatService appCommendRebatService;

    @Override
    public List<AppCommendMoney> selectCommend() {
        // TODO Auto-generated method stub
		/*
		QueryFilter filter = new QueryFilter(AppCodeMirror.class,request);
		filter.addFilter("Id=", id);
		List<AppCodeMirror> find = service.find(filter);*/

        return null;
    }

    @Override
    public BigDecimal findOne(String userName, String fixPriceCoinCode) {
        // TODO Auto-generated method stub
        BigDecimal findOne = appCommendTradeService.findOne(userName, fixPriceCoinCode);
        return findOne;
    }

    @Override
    public BigDecimal findTwo(String userName, String fixPriceCoinCode) {
        // TODO Auto-generated method stub
        BigDecimal findTwo = appCommendTradeService.findTwo(userName, fixPriceCoinCode);
        return findTwo;
    }

    @Override
    public BigDecimal findThree(String userName, String fixPriceCoinCode) {
        // TODO Auto-generated method stub
        BigDecimal findThree = appCommendTradeService.findThree(userName, fixPriceCoinCode);
        return findThree;
    }

    @Override
    public BigDecimal findLater(String userName, String fixPriceCoinCode) {
        // TODO Auto-generated method stub
        BigDecimal findLater = appCommendTradeService.findLater(userName, fixPriceCoinCode);
        return findLater;
    }

    public JsonResult postMoneyById(Long id, BigDecimal money, String fixPriceCoinCode) {
        JsonResult result = new JsonResult();
        result.setSuccess(false);
        QueryFilter AppCommendMoney = new QueryFilter(AppCommendMoney.class);
        AppCommendMoney.addFilter("id=", id);
        AppCommendMoney appCommendMoney = appCommendMoneyService.get(AppCommendMoney);

        if (null != appCommendMoney) {
            BigDecimal moneyNum = appCommendMoney.getMoneyNum();
            appCommendMoney.setPaidMoney(moneyNum);
            appCommendMoney.setTransactionNum(transactionNum("01"));

            RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
            UserRedis userRedis = redisUtil.get(appCommendMoney.getCustromerId().toString());
            if (null == userRedis) {
                result.setMsg("此用户的缓存账号为空，登陆会生成缓存账号");
                result.setSuccess(false);
                return result;
            }
            super.update(appCommendMoney);

            //存明细
            AppCommendRebat appCommendRebat = new AppCommendRebat();
            try {
                appCommendRebat.setCoinCode(fixPriceCoinCode);
                appCommendRebat.setRebatMoney(money);
                appCommendRebat.setTrueName(appCommendMoney.getCustromerName());
                appCommendRebat.setCustomerId(appCommendMoney.getCustromerId().intValue());
                String coin = getCnfigValue("language_code");
                if (fixPriceCoinCode.equals(coin)) {
                    //----发送mq消息----start
                    //热账户增加
                    Accountadd accountadd = new Accountadd();
                    Long accountId = userRedis.getAccountId();
                    accountadd.setAccountId(accountId);
                    accountadd.setMoney(money);
                    accountadd.setMonteyType(1);
                    accountadd.setRemarks(32);
                    accountadd.setAcccountType(0);
                    accountadd.setTransactionNum(appCommendMoney.getTransactionNum());

                    List<Accountadd> list2 = new ArrayList<Accountadd>();
                    list2.add(accountadd);
                    messageProducer.toAccount(JSON.toJSONString(list2));
                } else {
                    //----发送mq消息----start
                    //热账户增加

                    Accountadd accountadd = new Accountadd();
                    Long accountId = userRedis.getDmAccountId(fixPriceCoinCode);
                    accountadd.setAccountId(accountId);
                    accountadd.setMoney(money);
                    accountadd.setMonteyType(1);
                    accountadd.setRemarks(32);
                    accountadd.setAcccountType(1);
                    accountadd.setTransactionNum(appCommendMoney.getTransactionNum());

                    List<Accountadd> list3 = new ArrayList<Accountadd>();
                    list3.add(accountadd);
                    messageProducer.toAccount(JSON.toJSONString(list3));
                }
                appCommendRebat.setStatus(1);
                result.setMsg("派送成功");
                result.setSuccess(true);
            } catch (Exception e) {
                appCommendRebat.setStatus(0);
                result.setMsg("派送失败");
                log.error("消息发送失败：{}", appCommendMoney.getCustromerName(), e);
            }
            appCommendRebatService.save(appCommendRebat);
        }
        return result;
    }

    public static String transactionNum(String bussType) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
        String time = format.format(new Date(System.currentTimeMillis()));
        String randomStr = RandomStringUtils.random(4, false, true);
        if (null == bussType) {
            bussType = "00";
        }
        return bussType + time + randomStr;
    }


    public static String getCnfigValue(String type) {
        RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil
                .getBean("remoteAppConfigService");
        return remoteAppConfigService.getValueByKey(type);
    }

    /*
     * 改变前台用户佣金状态
     */
    @Override
    public List<AppCommendMoney> selectMoneyCommend(String username) {
        // TODO Auto-generated method stub
    	List<AppCommendMoney> appCommendMoney=appCommendMoneyDao.selectMoneyCommendAll(username);

        return appCommendMoney;
    }

    /*
     * 一键派发
     */
	@Override
	public List<AppCommendMoney> selectAllCommend() {
		// TODO Auto-generated method stub
		List<AppCommendMoney> all=appCommendMoneyDao.getAll();
		return all;
	}

}
