/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.mz.account.fund.model.AppBankCard;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.businessman.model.AppBusinessman;
import com.mz.customer.businessman.model.AppBusinessmanBank;
import com.mz.customer.businessman.model.OtcCoin;
import com.mz.customer.businessman.model.OtcOrderTransaction;
import com.mz.customer.businessman.model.OtcTransaction;
import com.mz.customer.user.model.AppCustomer;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.account.fund.service.AppBankCardService;
import com.mz.customer.businessman.dao.OtcTransactionDao;
import com.mz.customer.businessman.service.AppBusinessmanBankService;
import com.mz.customer.businessman.service.AppBusinessmanService;
import com.mz.customer.businessman.service.OtcAccountRecordService;
import com.mz.customer.businessman.service.OtcOrderTransactionService;
import com.mz.customer.businessman.service.OtcTransactionService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.otc.OtcTransactionOrder;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.mz.util.UserRedisUtils;

/**
 * <p>
 * OtcTransactionService
 * </p>
 *
 * @author: liushilei
 * @Date : 2017-12-07 14:06:38
 */
@Service("otcTransactionService")
public class OtcTransactionServiceImpl extends BaseServiceImpl<OtcTransaction, Long> implements OtcTransactionService {

    private static Object lock = new Object();
    private static final Logger log = Logger.getLogger(OtcTransactionServiceImpl.class);

    @Resource(name = "otcTransactionDao")
    @Override
    public void setDao(BaseDao<OtcTransaction, Long> dao) {
        super.dao = dao;
    }

    @Resource
    private AppBusinessmanService appBusinessmanService;

    @Resource
    private AppBusinessmanBankService appBusinessmanBankService;

    @Resource
    private AppBankCardService appBankCardService;

    @Resource
    private ExDigitalmoneyAccountService exDigitalmoneyAccountService;

    @Resource
    private MessageProducer messageProducer;

    @Resource
    private ExDmTransactionService exDmTransactionService;

    @Resource
    private OtcOrderTransactionService otcOrderTransactionService;

    @Resource
    private OtcAccountRecordService otcAccountRecordService;

    @Resource
    private AppCustomerService appCustomerService;

    /**
     * 生成与商户匹配的订单
     *
     * @param otcTransactionOrder
     */
    @Override
    public synchronized JsonResult createOtcOrder(OtcTransactionOrder otcTransactionOrder) {
        synchronized (lock) {

            JsonResult jsonResult = new JsonResult();
            AppCustomer appCustomer = appCustomerService.get(otcTransactionOrder.getCustomerId());
            if (appCustomer.getOtcFrozenDate() != null) {
                String otcFrozenString = DateUtil.dateToString(appCustomer.getOtcFrozenDate(), "yyyy-MM-dd HH:mm");
                Date date = new Date();
                //Date startDate = DateUtil.addMinToDate(date, 2880);
                String startDateString = DateUtil.dateToString(date, "yyyy-MM-dd HH:mm");
                String otcFrozenStringto = DateUtil.dateToString(DateUtil.addMinToDate(appCustomer.getOtcFrozenDate(), 2880), "yyyy-MM-dd HH:mm");
                if (otcFrozenStringto.compareTo(startDateString) > 0) {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg("你已被冻结到：" + otcFrozenStringto);
                    return jsonResult;
                }
                if (appCustomer.getOtcFrozenCout().compareTo(new BigDecimal(3)) >= 0) {
                    appCustomer.setOtcFrozenDate(null);
                    appCustomer.setOtcFrozenCout(new BigDecimal(0));
                    appCustomerService.updateNull(appCustomer);
                }
            }


            jsonResult.setSuccess(false);
            String poundage_type = null;
            BigDecimal poundage = new BigDecimal(0);
            RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
            String OtcCoinList = redisService.get("cn:OtcCoinList");
            BigDecimal fee = new BigDecimal(0);
            if (!StringUtils.isEmpty(OtcCoinList)) {
                List<OtcCoin> parseArray = JSON.parseArray(OtcCoinList, OtcCoin.class);
                BigDecimal min = new BigDecimal(1);
                BigDecimal max = new BigDecimal(100);
                BigDecimal minbuyPrice = new BigDecimal(0);
                BigDecimal maxbuyPrice = new BigDecimal(100000);
                BigDecimal minsellPrice = new BigDecimal(0);
                BigDecimal maxsellPrice = new BigDecimal(100000);
                boolean   CoinIsOpen = false ;
                for (OtcCoin OtcCoin : parseArray) {
                    if (otcTransactionOrder.getCoinCode().equals(OtcCoin.getCoinCode())) {
                        if(OtcCoin.getIsOpen() == 0){
                            jsonResult.setSuccess(false);
                            jsonResult.setMsg(OtcCoin.getCoinCode() + "未开启！");
                            return jsonResult;
                        }
                        CoinIsOpen = true;
                        min = OtcCoin.getMinCount();
                        max = OtcCoin.getMaxCount();
                        minbuyPrice = OtcCoin.getMinbuyPrice();
                        maxbuyPrice = OtcCoin.getMaxbuyPrice();
                        minsellPrice = OtcCoin.getMinsellPrice();
                        maxsellPrice = OtcCoin.getMaxsellPrice();
                        poundage_type = OtcCoin.getPoundage_type();
                        poundage = OtcCoin.getPoundage();
                        if (("Definite").equals(OtcCoin.getPoundage_type())) {
                            fee = OtcCoin.getPoundage();
                        } else {
                            fee = OtcCoin.getPoundage().multiply(otcTransactionOrder.getTransactionCount());
                        }
                    }
                }
                if(CoinIsOpen == false){
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(otcTransactionOrder.getCoinCode() + "未开启！");
                    return jsonResult;
                }
                if (otcTransactionOrder.getTransactionType().intValue() == 1) {
                    if (minbuyPrice != null) {
                        if (otcTransactionOrder.getTransactionPrice().compareTo(minbuyPrice) < 0) {
                            jsonResult.setMsg("买入最小下单单价为:" + minbuyPrice.setScale(4, BigDecimal.ROUND_HALF_DOWN));
                            return jsonResult;
                        }
                    }
                    if (maxbuyPrice != null) {
                        if (otcTransactionOrder.getTransactionPrice().compareTo(maxbuyPrice) > 0) {
                            jsonResult.setMsg("买入最大下单单价为:" + maxbuyPrice.setScale(4, BigDecimal.ROUND_HALF_DOWN));
                            return jsonResult;
                        }
                    }
                }
                if (otcTransactionOrder.getTransactionType().intValue() == 2) {
                    if (minsellPrice != null) {
                        if (otcTransactionOrder.getTransactionPrice().compareTo(minsellPrice) < 0) {
                            jsonResult.setMsg("卖出最小下单单价为:" + minsellPrice.setScale(4, BigDecimal.ROUND_HALF_DOWN));
                            return jsonResult;
                        }
                    }
                    if (maxsellPrice != null) {
                        if (otcTransactionOrder.getTransactionPrice().compareTo(maxsellPrice) > 0) {
                            jsonResult.setMsg("卖出最大下单单价为:" + maxsellPrice.setScale(4, BigDecimal.ROUND_HALF_DOWN));
                            return jsonResult;
                        }
                    }
                }
                if(min != null) {
                    if (otcTransactionOrder.getTransactionCount().compareTo(min) < 0) {
                        jsonResult.setMsg("最小下单数为:" + min.setScale(3, BigDecimal.ROUND_HALF_DOWN));
                        return jsonResult;
                    }
                }
                if(max != null) {
                    if (otcTransactionOrder.getTransactionCount().compareTo(max) > 0) {
                        jsonResult.setMsg("最大下单数为:" + max.setScale(3, BigDecimal.ROUND_HALF_DOWN));
                        return jsonResult;
                    }
                }

            } else {
                jsonResult.setMsg("otc配置缓存错误");
                return jsonResult;
            }

            // 判断是否添加银行卡
            AppBankCard appBankCard = null;
            if (otcTransactionOrder.getTransactionType().intValue() == 2) {
                QueryFilter filter = new QueryFilter(AppBankCard.class);
                filter.addFilter("customerId=", otcTransactionOrder.getCustomerId());
                filter.addFilter("isDelete=", 0);
                appBankCard = appBankCardService.get(filter);
                if (appBankCard == null) {
                    jsonResult.setMsg("请到个人中心添加银行卡后再操作");
                    return jsonResult;
                }

                // 判断账户是否大于要卖的币
                RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
                UserRedis userRedis = redisUtil.get(otcTransactionOrder.getCustomerId().toString());
                Long dmAccountId = userRedis.getDmAccountId(otcTransactionOrder.getCoinCode());
                ExDigitalmoneyAccountRedis dmAccount = UserRedisUtils.getAccount(dmAccountId.toString(), otcTransactionOrder.getCoinCode());
                if (dmAccount.getHotMoney().compareTo(otcTransactionOrder.getTransactionCount().add(fee)) < 0) {
                    jsonResult.setMsg(otcTransactionOrder.getCoinCode() + "不足!");
                    return jsonResult;
                }

                otcTransactionOrder.setFee(fee);

            }

            // 订单
            OtcTransaction otcTransaction = new OtcTransaction();

            otcTransaction = createOtccTransaction(otcTransaction, otcTransactionOrder, appBankCard);

            otcTransaction.setPoundage(poundage);

            otcTransaction.setPoundage_type(poundage_type);

            otcTransaction.setCancelFlag("N");

            otcTransaction.setCreateBy(otcTransactionOrder.getCustomerId());

            //成交量0，
            otcTransaction.setDealQuantity(new BigDecimal("0"));
            //交易量0
            otcTransaction.setBusinessQuantity(new BigDecimal("0"));
            //冻结数量
            otcTransaction.setFrozenQuantity(new BigDecimal("0"));
            //设置退还数
            otcTransaction.setReturnQuantity(new BigDecimal("0"));
            //剩余量=数量
            otcTransaction.setSurplusQuantity(otcTransaction.getTransactionCount());

            //交易标识
            otcTransaction.setBusinessFlag("Y");


            otcTransaction.setRandomNum(RandomStringUtils.randomNumeric(4));
            // 保存订单
            super.save(otcTransaction);
            jsonResult.setObj(otcTransaction.getTransactionNum());
            jsonResult.setSuccess(true);

            if (jsonResult.getSuccess() && otcTransaction.getTransactionType().intValue() == 2) {
                sellCoin(otcTransaction);
            }

            return jsonResult;
        }
    }


    /**
     * 封装参数
     *
     * @param otcTransactionOrder
     * @return
     */
    private OtcTransaction createOtccTransaction(OtcTransaction otcTransaction, OtcTransactionOrder otcTransactionOrder, AppBankCard appBankCard) {
        OtcTransaction ct = otcTransaction;
        if (appBankCard != null) {
            ct.setCustomerBankId(appBankCard.getId());
        }
        // 币种
        ct.setCoinCode(otcTransactionOrder.getCoinCode());
        // 订单号
        ct.setTransactionNum(otcTransactionOrder.getTransactionNum());
        // 交易类型
        ct.setTransactionType(otcTransactionOrder.getTransactionType());
        // 交易数量
        ct.setTransactionCount(otcTransactionOrder.getTransactionCount());
        // 交易价格
        ct.setTransactionPrice(otcTransactionOrder.getTransactionPrice());
        // 交易金额
        ct.setTransactionMoney(otcTransactionOrder.getTransactionCount().multiply(otcTransactionOrder.getTransactionPrice()).setScale(2, BigDecimal.ROUND_DOWN));
        // 待审核
        ct.setStatus(Integer.valueOf(1));
        //手续费
        ct.setFee(otcTransactionOrder.getFee());

        // 未支付
        ct.setStatus2(Integer.valueOf(1));
        // 用户名
        ct.setUserName(otcTransactionOrder.getUserName());
        // 用户id
        ct.setCustomerId(otcTransactionOrder.getCustomerId());
        // accountId
        ct.setAccountId(otcTransactionOrder.getAccountId());
        return ct;
    }


    private synchronized void sellCoin(OtcTransaction OtcTransaction) {


        // 热账户减少
        Accountadd accountadd = new Accountadd();
        accountadd.setAccountId(OtcTransaction.getAccountId());
        accountadd.setMoney((OtcTransaction.getTransactionCount().add(OtcTransaction.getFee())).multiply(new BigDecimal(-1)));
        accountadd.setMonteyType(1);
        accountadd.setAcccountType(1);
        accountadd.setRemarks(52);
        accountadd.setTransactionNum(OtcTransaction.getTransactionNum());

        accountadd.setTransactionNum(OtcTransaction.getTransactionNum());
        //保存币账户操作记录
        otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "OTC提交委托");

        // 冷账户增加
        Accountadd accountadd2 = new Accountadd();
        accountadd2.setAccountId(OtcTransaction.getAccountId());
        accountadd2.setMoney((OtcTransaction.getTransactionCount().add(OtcTransaction.getFee())));
        accountadd2.setMonteyType(2);
        accountadd2.setAcccountType(1);
        accountadd2.setRemarks(52);
        accountadd2.setTransactionNum(OtcTransaction.getTransactionNum());

        List<Accountadd> list = new ArrayList<Accountadd>();
        list.add(accountadd);
        list.add(accountadd2);

        //保存操作记录
        otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "OTC提交委托");


        messageProducer.toAccount(JSON.toJSONString(list));
    }

    /**
     * 查询成交订单最少的商户
     *
     * @param coinCode
     * @param businessmanType
     * @return
     */
    private AppBusinessman minOrderBusinessman(String coinCode, int businessmanType) {

        // 一个币种所有商户
        QueryFilter filter = new QueryFilter(AppBusinessman.class);
        filter.addFilter("changeCoin=", coinCode);
        filter.addFilter("type=", businessmanType);
        List<AppBusinessman> list1 = appBusinessmanService.find(filter);
        if (list1 == null || list1.size() < 1) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("coinCode", coinCode);
        // 有成交的商户列表，成交数量从小到大排序
        List<OtcTransaction> list2 = ((OtcTransactionDao) dao).groupByBusinessmanId(map);

        List<AppBusinessman> hasOrder = new ArrayList<AppBusinessman>();

        for (AppBusinessman appBusinessman : list1) {
            for (OtcTransaction OtcTransaction : list2) {
                if (appBusinessman.getId().compareTo(OtcTransaction.getBusinessmanId()) == 0) {
                    hasOrder.add(appBusinessman);
                }
            }
        }

        // 得到差集
        list1.removeAll(hasOrder);
        // 如果有商户没有订单的，则随机取一个
        if (list1.size() > 0) {
            Collections.shuffle(list1);
            for (AppBusinessman appBusinessman : list1) {
                if (appBusinessman.getType() == businessmanType) {
                    return appBusinessman;
                }
            }
            return list1.get(0);
        } else {// 所有商户都有订单,返回订单数最小的一个商户，订单数相同返回前一个
            for (OtcTransaction OtcTransaction : list2) {
                AppBusinessman appBusinessman = appBusinessmanService.get(OtcTransaction.getBusinessmanId());
                if (appBusinessman.getType() == businessmanType) {
                    return appBusinessman;
                }
            }
            return null;
        }

    }


    @Override
    public String getOtcOrderDetail(String transactionNum) {

        QueryFilter filter = new QueryFilter(OtcTransaction.class);
        filter.addFilter("transactionNum=", transactionNum);
        OtcTransaction OtcTransaction = super.get(filter);

        JSONObject obj = new JSONObject();
        // 汇款随机码
        obj.put("randomNum", OtcTransaction.getRandomNum());
        // 订单状态
        obj.put("status", OtcTransaction.getStatus());
        // 转账金额
        obj.put("transactionMoney", OtcTransaction.getTransactionMoney());
        // 交易币种
        obj.put("coinCode", OtcTransaction.getCoinCode());
        // 交易状态
        obj.put("status2", OtcTransaction.getStatus2());
        // 订单类型
        obj.put("transactionType", OtcTransaction.getTransactionType());
        // 订单号
        obj.put("transactionNum", OtcTransaction.getTransactionNum());
        // 用户登录名
        obj.put("userName", OtcTransaction.getUserName());
        // 交易数量
        obj.put("transactionCount", OtcTransaction.getTransactionCount());
        // 交易单价
        obj.put("transactionPrice", OtcTransaction.getTransactionPrice());

        if (OtcTransaction.getTransactionType() == 1) {// 买
            AppBusinessman appBusinessman = appBusinessmanService.get(OtcTransaction.getBusinessmanId());
            if (appBusinessman != null) {
                obj.put("businessmanTrueName", appBusinessman.getTrueName());
            }

            AppBusinessmanBank appBusinessmanBank = appBusinessmanBankService.get(OtcTransaction.getBusinessmanBankId());
            if (appBusinessmanBank != null) {
                // 开户行名称
                obj.put("bankname", appBusinessmanBank.getBankname());
                // 卡号
                obj.put("bankcard", appBusinessmanBank.getBankcard());
                // 持卡人
                obj.put("bankowner", appBusinessmanBank.getBankowner());
            }
        } else if (OtcTransaction.getTransactionType() == 2) {// 卖
            AppBankCard appBankCard = appBankCardService.get(OtcTransaction.getCustomerBankId());
            if (appBankCard != null) {

                // 开户行名称
                obj.put("bankname", appBankCard.getCardBank() + appBankCard.getSubBank());
                // 卡号
                obj.put("bankcard", appBankCard.getCardNumber());
                // 银行卡所在地
                obj.put("bankAddress", appBankCard.getBankProvince() + appBankCard.getBankAddress());
                // 持卡人
                obj.put("bankowner", appBankCard.getCardName());

            }
        }

        return obj.toJSONString();
    }

    @Override
    public synchronized JsonResult confirm(Long id) {

        JsonResult jsonResult = new JsonResult();

        synchronized (lock) {
            OtcTransaction OtcTransaction = super.get(id);

            if (OtcTransaction.getStatus().intValue() == 3) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg("交易已取消");
                return jsonResult;
            }
            if (OtcTransaction.getStatus().intValue() == 2) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg("交易已成功");
                return jsonResult;
            }

            if (OtcTransaction.getTransactionType() == 1) {// 买确认,充币,可用增加


                // 发送mq消息
                Accountadd accountadd = new Accountadd();
                accountadd.setAccountId(OtcTransaction.getAccountId());
                accountadd.setMoney(OtcTransaction.getTransactionCount().subtract(OtcTransaction.getFee()));
                accountadd.setMonteyType(1);
                accountadd.setAcccountType(1);
                accountadd.setRemarks(21);
                accountadd.setTransactionNum(OtcTransaction.getTransactionNum());

                //保存币账户操作记录
                otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "确认");


                List<Accountadd> list = new ArrayList<Accountadd>();
                list.add(accountadd);
                messageProducer.toAccount(JSON.toJSONString(list));

            } else if (OtcTransaction.getTransactionType() == 2) {// 卖确认,扣币,冻结减少

                // 币订单状态改为成功

                Accountadd accountadd = new Accountadd();
                accountadd.setAccountId(OtcTransaction.getAccountId());
                accountadd.setMoney(OtcTransaction.getTransactionCount().multiply(new BigDecimal(-1)));
                accountadd.setMonteyType(2);
                accountadd.setAcccountType(1);
                accountadd.setRemarks(33);
                accountadd.setTransactionNum(OtcTransaction.getTransactionNum());

                //保存币账户操作记录
                otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "确认");


                List<Accountadd> list = new ArrayList<Accountadd>();
                list.add(accountadd);
                messageProducer.toAccount(JSON.toJSONString(list));

            }

            OtcTransaction.setStatus(2);
            super.update(OtcTransaction);
        }
        jsonResult.setSuccess(true);

        return jsonResult;
    }

    /*
  /  获取手续费
   */
    private BigDecimal getFee(String CoinCode, BigDecimal transactioncount, String Deal) {
        BigDecimal fee = new BigDecimal(0);
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String OtcCoinList = redisService.get("cn:OtcCoinList");
        if (!StringUtils.isEmpty(OtcCoinList)) {
            List<OtcCoin> parseArray = JSON.parseArray(OtcCoinList, OtcCoin.class);
            for (OtcCoin OtcCoin : parseArray) {
                if (CoinCode.equals(OtcCoin.getCoinCode())) {
                    if (("Definite").equals(OtcCoin.getPoundage_type())) {
                        if (("Y").equals(Deal)) {
                            fee = new BigDecimal(0);
                        } else {
                            fee = OtcCoin.getPoundage();
                        }
                    } else {
                        fee = OtcCoin.getPoundage().multiply(transactioncount);
                    }
                }
            }

        }
        return fee;
    }

    @Override
    public synchronized JsonResult OtcListclose(Long id) {

        JsonResult jsonResult = new JsonResult();

        synchronized (lock) {
            OtcTransaction OtcTransaction = super.get(id);

            if (OtcTransaction.getStatus().intValue() == 3) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg("交易已取消");
                return jsonResult;
            }
            if (OtcTransaction.getStatus().intValue() == 2) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg("交易已成功");
                return jsonResult;
            }
            QueryFilter filter = new QueryFilter(OtcOrderTransaction.class);
            filter.addFilter("transactionId=", OtcTransaction.getId());
            filter.addFilter("status_in", "1,2,6");
            List<OtcOrderTransaction> orderlist = otcOrderTransactionService.find(filter);
            if (orderlist != null && !orderlist.isEmpty()) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg("存在待成交交易订单，不可撤销!");
                return jsonResult;
            }

            if (OtcTransaction.getTransactionType() == 1) {// 买取消
                this.otcOrderUndo(OtcTransaction);
                jsonResult.setSuccess(true);

            } else if (OtcTransaction.getTransactionType() == 2) {// 卖取消

                List<Accountadd> list = new ArrayList<Accountadd>();
                // 减少冻结，
                Accountadd accountadd = new Accountadd();
                accountadd.setAccountId(OtcTransaction.getAccountId());

                if (("Definite").equals(OtcTransaction.getPoundage_type())) {
                    if (OtcTransaction.getDealQuantity().intValue() != 0) {
                        accountadd.setMoney(OtcTransaction.getSurplusQuantity().multiply(new BigDecimal(-1)));
                    } else {
                        accountadd.setMoney(OtcTransaction.getSurplusQuantity().add(OtcTransaction.getFee()).multiply(new BigDecimal(-1)));
                    }
                } else {
                    accountadd.setMoney(OtcTransaction.getSurplusQuantity().multiply(new BigDecimal(1).add(OtcTransaction.getPoundage())).multiply(new BigDecimal(-1)));
                }

                accountadd.setMonteyType(2);
                accountadd.setAcccountType(1);
                accountadd.setRemarks(52);
                accountadd.setTransactionNum(OtcTransaction.getTransactionNum());

                //保存币账户操作记录
                otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "OTC委托取消");


                // 增加可用
                Accountadd accountadd2 = new Accountadd();
                accountadd2.setAccountId(OtcTransaction.getAccountId());
                if (("Definite").equals(OtcTransaction.getPoundage_type())) {
                    if (OtcTransaction.getDealQuantity().intValue() != 0) {
                        accountadd2.setMoney(OtcTransaction.getSurplusQuantity());
                    } else {
                        accountadd2.setMoney(OtcTransaction.getSurplusQuantity().add(OtcTransaction.getFee()));
                    }
                } else {
                    accountadd2.setMoney(OtcTransaction.getSurplusQuantity().multiply(new BigDecimal(1).add(OtcTransaction.getPoundage())));
                }
                accountadd2.setMonteyType(1);
                accountadd2.setAcccountType(1);
                accountadd2.setRemarks(52);
                accountadd2.setTransactionNum(OtcTransaction.getTransactionNum());

                //保存币账户操作记录
                otcAccountRecordService.saveRecord(OtcTransaction.getCustomerId(), accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "OTC委托取消");

                list.add(accountadd);
                list.add(accountadd2);
                messageProducer.toAccount(JSON.toJSONString(list));
                OtcTransaction.setReturnQuantity(OtcTransaction.getFrozenQuantity());
                this.otcOrderUndo(OtcTransaction);

                jsonResult.setSuccess(true);


            }
        }

        return jsonResult;
    }


    private OtcTransactionOrder common(OtcTransaction otcTransaction) {
        OtcTransactionOrder otcTransactionOrder = new OtcTransactionOrder();
        otcTransactionOrder.setId(otcTransaction.getId());
        otcTransactionOrder.setCoinCode(otcTransaction.getCoinCode());
        otcTransactionOrder.setFee(otcTransaction.getFee());
        otcTransactionOrder.setStatus(otcTransaction.getStatus());
        otcTransactionOrder.setRandomNum(otcTransaction.getRandomNum());
        otcTransactionOrder.setTransactionType(otcTransaction.getTransactionType());
        otcTransactionOrder.setTransactionMoney(otcTransaction.getTransactionMoney());
        otcTransactionOrder.setTransactionCount(otcTransaction.getTransactionCount());
        otcTransactionOrder.setTransactionPrice(otcTransaction.getTransactionPrice());
        otcTransactionOrder.setSurplusQuantity(otcTransaction.getSurplusQuantity());
        otcTransactionOrder.setFrozenQuantity(otcTransaction.getFrozenQuantity());
        otcTransactionOrder.setBusinessQuantity(otcTransaction.getBusinessQuantity());
        otcTransactionOrder.setBusinessFlag(otcTransaction.getBusinessFlag());
        otcTransactionOrder.setTransactionNum(otcTransaction.getTransactionNum());
        otcTransactionOrder.setCreated(otcTransaction.getCreated());
        otcTransactionOrder.setModified(otcTransaction.getModified());
        otcTransactionOrder.setSaasId(otcTransaction.getSaasId());
        otcTransactionOrder.setStatus2(otcTransaction.getStatus2());
        otcTransactionOrder.setUserName(otcTransaction.getUserName());
        otcTransactionOrder.setPoundage(otcTransaction.getPoundage());
        otcTransactionOrder.setCancelBy(otcTransaction.getCancelBy());
        otcTransactionOrder.setCancelFlag(otcTransaction.getCancelFlag());
        otcTransactionOrder.setCustomerId(otcTransaction.getCustomerId());
        otcTransactionOrder.setAccountId(otcTransaction.getAccountId());
        otcTransactionOrder.setCreateBy(otcTransaction.getCreateBy());
        otcTransactionOrder.setPoundage_type(otcTransaction.getPoundage_type());
        otcTransactionOrder.setCancelDate(otcTransaction.getCancelDate());
        otcTransactionOrder.setTransactionId(otcTransaction.getTransactionId());
        otcTransactionOrder.setDealQuantity(otcTransaction.getDealQuantity());
        return otcTransactionOrder;
    }


    /**
     * 获得委托订单
     *
     * @param transactionType
     * @return
     */
    public List<OtcTransaction> getOtclist(String transactionType,String coinCode,String OrderByClause) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("transactionType", transactionType);
        map.put("coinCode", coinCode);
        map.put("OrderByClause", OrderByClause);
        List<OtcTransaction> list2 = ((OtcTransactionDao) dao).otclist(map);
//		QueryFilter filter = new QueryFilter(OtcTransaction.class);
//		if (transactionType != null) {
//			filter.addFilter("transactionType=", transactionType);
//		}
//		filter.addFilter("status_in", "1,4,6");
//		filter.addFilter("businessFlag=", "Y");
//		List<OtcTransaction> list2 = super.find(filter);
        return list2;
    }

    public FrontPage getOtclists(Map<String, String> params) {
        Page page = PageFactory.getPage(params);
        List<OtcTransaction> list2 = ((OtcTransactionDao) dao).otclists(params);
        ArrayList<OtcTransactionOrder> resultList = new ArrayList<OtcTransactionOrder>();
        for (int i = 0; i < list2.size(); i++) {
            resultList.add(common(list2.get(i)));
        }
        return new FrontPage(resultList, page.getTotal(), page.getPages(), page.getPageSize());

    }

    private synchronized void otcOrderUndo(OtcTransaction otcTransaction) {
//        QueryFilter filter = new QueryFilter(OtcOrderTransaction.class);
//        filter.addFilter("transactionId=", otcTransaction.getId());
//        filter.addFilter("status_in", "1,6");
//        List<OtcOrderTransaction> list = otcOrderTransactionService.find(filter);
//        for (int i = 0; i < list.size(); i++) {
//
//            OtcOrderTransaction OrderTransaction = otcOrderTransactionService.get(list.get(i).getId());
//            OrderTransaction.setStatus(3);
//            otcOrderTransactionService.update(OrderTransaction);
//            if (otcTransaction.getTransactionType() == 1) {
//                // 卖方冷账号减少
//                Accountadd accountadd = new Accountadd();
//                accountadd.setAccountId(list.get(i).getSellAaccountId());
//                accountadd.setMoney(list.get(i).getTransactionCount().add(list.get(i).getFee()).multiply(new BigDecimal(-1)));
//                accountadd.setMonteyType(2);
//                accountadd.setAcccountType(1);
//                accountadd.setRemarks(52);
//                accountadd.setTransactionNum(list.get(i).getTransactionNum());
//
//                //保存币账户操作记录
//                otcAccountRecordService.saveRecord(otcTransaction.getCustomerId(), accountadd.getAccountId(), accountadd.getMoney(),accountadd.getMonteyType(), accountadd.getAcccountType(), accountadd.getTransactionNum(), "OTC委托取消");
//
//
//                // 卖方热账号增加
//                Accountadd accountadd2 = new Accountadd();
//                accountadd2.setAccountId(list.get(i).getSellAaccountId());
//                accountadd2.setMoney(list.get(i).getTransactionCount().add(list.get(i).getFee()));
//                accountadd2.setMonteyType(1);
//                accountadd2.setAcccountType(1);
//                accountadd2.setRemarks(52);
//                accountadd2.setTransactionNum(list.get(i).getTransactionNum());
//
//                //保存币账户操作记录
//                otcAccountRecordService.saveRecord(otcTransaction.getCustomerId(), accountadd2.getAccountId(), accountadd2.getMoney(),accountadd2.getMonteyType(), accountadd2.getAcccountType(), accountadd2.getTransactionNum(), "OTC委托取消");
//
//
//                List<Accountadd> Accountaddlist = new ArrayList<Accountadd>();
//                Accountaddlist.add(accountadd);
//                Accountaddlist.add(accountadd2);
//                messageProducer.toAccount(JSON.toJSONString(Accountaddlist));
//            }
//        }
        //回写委托订单
        otcTransaction.setStatus(3);
        otcTransaction.setBusinessFlag("N");
        otcTransaction.setCancelFlag("Y");
        otcTransaction.setFrozenQuantity(new BigDecimal(0));
        otcTransaction.setCancelBy(ContextUtil.getCurrentUserId());
        otcTransaction.setCancelDate(new Date());
        //如果成交量大于0 状态改为部分成交
//        if (otcTransaction.getDealQuantity().compareTo(new BigDecimal(0)) > 0) {
//            otcTransaction.setStatus(6);
//        } else {
//            if (otcTransaction.getBusinessQuantity().compareTo(new BigDecimal(0)) > 0) {
//                otcTransaction.setStatus(4);
//            }
//        }
        super.update(otcTransaction);

    }

}
