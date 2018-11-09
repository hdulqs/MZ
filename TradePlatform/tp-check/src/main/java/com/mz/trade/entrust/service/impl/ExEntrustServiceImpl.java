/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午2:04:29
 */
package com.mz.trade.entrust.service.impl;

import com.mz.account.fund.model.AppColdAccountRecord;
import com.mz.account.fund.model.AppHotAccountRecord;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.impl.RedisServiceImpl;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.account.fund.dao.AppColdAccountRecordDao;
import com.mz.account.fund.dao.AppHotAccountRecordDao;
import com.mz.account.fund.service.AppAccountService;
import com.mz.exchange.account.dao.ExDmColdAccountRecordDao;
import com.mz.exchange.account.dao.ExDmHotAccountRecordDao;
import com.mz.trade.entrust.dao.ExEntrustDao;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
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
@Import({RedisServiceImpl.class})
@Service("exEntrustService")
public class ExEntrustServiceImpl extends BaseServiceImpl<ExEntrust, Long> implements ExEntrustService {

    @Resource(name = "exEntrustDao")
    @Override
    public void setDao(BaseDao<ExEntrust, Long> dao) {
        super.dao = dao;
    }

    @Resource
    private ExOrderInfoService exOrderInfoService;
    @Resource
    public RedisService redisService;
    @Resource
    public ExEntrustDao exEntrustDao;
    @Resource
    public AppColdAccountRecordDao appColdAccountRecordDao;
    @Resource
    public AppHotAccountRecordDao appHotAccountRecordDao;
    @Resource
    public ExDmColdAccountRecordDao exDmColdAccountRecordDao;
    @Resource
    public ExDmHotAccountRecordDao exDmHotAccountRecordDao;
    @Resource
    public AppAccountService appAccountService;


    @Override
    public void checkExEntrust() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newdate = new Date();
        String checkExEntrustTime = redisService.get("checkExEntrustTime");
        String newdatestr = formatter.format(newdate);
        if (StringUtil.isEmpty(checkExEntrustTime)) {
            redisService.save("checkExEntrustTime", newdatestr);
            return;
        }
        LogFactory.info("checkExEntrustTime=" + checkExEntrustTime);
        QueryFilter filter = new QueryFilter(ExEntrust.class);
        // filter.addFilter("entrustNum=", entrustNum);
        filter.addFilter("isCheck=", 0);//完成
        filter.addFilter("status>", 1);//完成
        filter.addFilter("entrustTime>=", formatter.format(checkExEntrustTime));//完成
        List<ExEntrust> list = this.find(filter);
        int k = 0;
        for (ExEntrust l : list) {

            int i = 0;
            Boolean flag = false;
            if (l.getType() == 1) { //买
                //1，先得有一条冻结定价币
                //app_cold_account_record  收入，app_cold_account_record支出
                //

                if (l.getFixPriceType() == 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<AppColdAccountRecord> listacar = appColdAccountRecordDao.find(map);
                    BigDecimal acarPay = new BigDecimal("0");
                    BigDecimal acarIncome = new BigDecimal("0");
                    for (AppColdAccountRecord acar : listacar) {
                        i++;
                        if (acar.getRecordType() == 1) {
                            acarIncome = acarIncome.add(acar.getTransactionMoney());
                        } else {
                            acarPay = acarPay.add(acar.getTransactionMoney());
                        }
                    }
                    if (acarIncome.setScale(8, BigDecimal.ROUND_DOWN).compareTo(acarPay.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                    } else {
                        flag = true;
                        LogFactory.info("acarIncome=" + acarIncome + "acarPay=" + acarPay + "==" + "1,1");
                    }

                    //

                    List<AppHotAccountRecord> listahar = appHotAccountRecordDao.find(map);
                    BigDecimal hcarPay = new BigDecimal("0");
                    BigDecimal hcarIncome = new BigDecimal("0");
                    for (AppHotAccountRecord acar : listahar) {
                        i++;

                    }
                    //2得到币
                    Map<String, Object> mapedha = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<ExDmHotAccountRecord> listedha = exDmHotAccountRecordDao.find(map);
                    BigDecimal edhaPay = new BigDecimal("0");
                    BigDecimal edhaIncome = new BigDecimal("0");
                    for (ExDmHotAccountRecord edha : listedha) {
                        i++;
                        if (edha.getRecordType() == 1) {
                            edhaIncome = edhaIncome.add(edha.getTransactionMoney());
                        } else {
                            edhaPay = edhaPay.add(edha.getTransactionMoney());
                        }
                    }
                    BigDecimal edhaliu = edhaIncome.subtract(edhaPay);
                    BigDecimal edhaentru = l.getEntrustCount().subtract(l.getSurplusEntrustCount()).subtract(l.getTransactionFee());
                    if (edhaliu.setScale(8, BigDecimal.ROUND_DOWN).compareTo(edhaentru.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                        //没错
                    } else {
                        //有错
                        LogFactory.info("edhaliu=" + edhaliu + "edhaentru=" + edhaentru + "1,1");
                        flag = true;
                    }
                } else {

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<ExDmColdAccountRecord> listacar = exDmColdAccountRecordDao.find(map);
                    BigDecimal acarPay = new BigDecimal("0");
                    BigDecimal acarIncome = new BigDecimal("0");
                    for (ExDmColdAccountRecord acar : listacar) {
                        i++;
                        if (acar.getRecordType() == 1) {
                            acarIncome = acarIncome.add(acar.getTransactionMoney());
                        } else {
                            acarPay = acarPay.add(acar.getTransactionMoney());
                        }
                    }
                    if (acarIncome.setScale(8, BigDecimal.ROUND_DOWN).compareTo(acarPay.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                        //没错	//没错 定价币:冻结=解冻
                    } else {
                        LogFactory.info("acarIncome=" + acarIncome + "acarPay=" + acarPay + "1,2---1");
                        flag = true;
                    }
                    //2得到币
                    Map<String, Object> mapedha = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<ExDmHotAccountRecord> listedha = exDmHotAccountRecordDao.find(map);
                    BigDecimal edhaPay = new BigDecimal("0");
                    BigDecimal edhaIncome = new BigDecimal("0");

                    BigDecimal edhafixPay = new BigDecimal("0");
                    BigDecimal edhafixIncome = new BigDecimal("0");
                    for (ExDmHotAccountRecord acar : listedha) {
                        i++;
                        if (acar.getCoinCode().equals(l.getCoinCode())) {
                            if (acar.getRecordType() == 1) {
                                edhaIncome = edhaIncome.add(acar.getTransactionMoney());
                            } else {
                                edhaPay = edhaPay.add(acar.getTransactionMoney());
                            }
                        } else {
                            if (acar.getRecordType() == 1) {
                                edhafixIncome = edhafixIncome.add(acar.getTransactionMoney());
                            } else {

                                edhafixPay = edhafixPay.add(acar.getTransactionMoney());
                            }
                        }

                    }
                    BigDecimal sur = l.getEntrustSum().subtract(l.getTransactionSum());
                    if ((edhafixIncome.setScale(8, BigDecimal.ROUND_DOWN)).compareTo(sur.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {

                        //定价币：下单时热账户支出==成交的总额
                    } else {
                        LogFactory.info("edhafixIncome=" + edhafixIncome + "sur=" + sur + "1,2---2");
                        flag = true;
                    }

                    BigDecimal edhaliu = edhaIncome.subtract(edhaPay);
                    BigDecimal edhaentru = l.getEntrustCount().subtract(l.getSurplusEntrustCount()).subtract(l.getTransactionFee());
                    if (edhaliu.setScale(8, BigDecimal.ROUND_DOWN).compareTo(edhaentru.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                        ////交易币 ：热账户收入==成交的币
                    } else {
                        LogFactory.info("edhaliu=" + edhaliu + "edhaentru=" + edhaentru + "1,2---3");
                        flag = true;
                    }


                }

            } else { //卖

                if (l.getFixPriceType() == 0) {

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<ExDmColdAccountRecord> listacar = exDmColdAccountRecordDao.find(map);
                    BigDecimal acarPay = new BigDecimal("0");
                    BigDecimal acarIncome = new BigDecimal("0");
                    for (ExDmColdAccountRecord acar : listacar) {
                        i++;
                        if (acar.getRecordType() == 1) {
                            acarIncome = acarIncome.add(acar.getTransactionMoney());
                        } else {
                            acarPay = acarPay.add(acar.getTransactionMoney());
                        }
                    }
                    if (acarIncome.setScale(8, BigDecimal.ROUND_DOWN).compareTo(acarPay.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                        //没错
                    } else {
                        LogFactory.info("acarIncome=" + acarIncome + "acarPay=" + acarPay + "2,1");
                        flag = true;
                    }
                    //

                    List<ExDmHotAccountRecord> listahar = exDmHotAccountRecordDao.find(map);
                    BigDecimal hcarPay = new BigDecimal("0");
                    BigDecimal hcarIncome = new BigDecimal("0");
                    for (ExDmHotAccountRecord acar : listahar) {
                        i++;

                    }
                    //2得到币
                    Map<String, Object> mapedha = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<AppHotAccountRecord> listedha = appHotAccountRecordDao.find(map);
                    BigDecimal edhaPay = new BigDecimal("0");
                    BigDecimal edhaIncome = new BigDecimal("0");
                    for (AppHotAccountRecord acar : listedha) {
                        i++;
                        if (acar.getRecordType() == 1) {
                            edhaIncome = edhaIncome.add(acar.getTransactionMoney());
                        } else {
                            edhaPay = edhaPay.add(acar.getTransactionMoney());
                        }
                    }
                    BigDecimal edhaliu = edhaIncome.subtract(edhaPay);
                    BigDecimal edhaentru = l.getTransactionSum().subtract(l.getTransactionFee());
                    if (edhaliu.setScale(8, BigDecimal.ROUND_DOWN).compareTo(edhaentru.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                        //没错
                    } else {
                        LogFactory.info("edhaliu==" + edhaliu + "edhaentru==" + edhaentru + "==" + "2,1");
                        flag = true;
                    }


                } else {

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<ExDmColdAccountRecord> listacar = exDmColdAccountRecordDao.find(map);
                    BigDecimal acarPay = new BigDecimal("0");
                    BigDecimal acarIncome = new BigDecimal("0");
                    for (ExDmColdAccountRecord acar : listacar) {
                        i++;
                        if (acar.getRecordType() == 1) {
                            acarIncome = acarIncome.add(acar.getTransactionMoney());
                        } else {
                            acarPay = acarPay.add(acar.getTransactionMoney());
                        }
                    }
                    if (acarIncome.setScale(8, BigDecimal.ROUND_DOWN).compareTo(acarPay.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                        // 交易币:冻结=解冻
                    } else {
                        LogFactory.info("acarIncome=" + acarIncome + "acarPay=" + acarPay + "2,2---1");
                        flag = true;
                    }
                    //2得到定价币
                    Map<String, Object> mapedha = new HashMap<String, Object>();
                    map.put("transactionNum", l.getEntrustNum());
                    List<ExDmHotAccountRecord> listedha = exDmHotAccountRecordDao.find(map);
                    BigDecimal edhaPay = new BigDecimal("0");
                    BigDecimal edhaIncome = new BigDecimal("0");
                    BigDecimal edhaPayCoin = new BigDecimal("0");
                    BigDecimal edhaIncomeCoin = new BigDecimal("0");
                    for (ExDmHotAccountRecord acar : listedha) {
                        i++;
                        if (acar.getCoinCode().equals(l.getCoinCode())) { //交易币
                            if (acar.getRecordType() == 1) {
                                edhaIncomeCoin = edhaIncomeCoin.add(acar.getTransactionMoney());
                            } else {

                                edhaPayCoin = edhaPayCoin.add(acar.getTransactionMoney());
                            }
                        } else {
                            //定价币
                            if (acar.getRecordType() == 1) {
                                edhaIncome = edhaIncome.add(acar.getTransactionMoney());
                            } else {
                                edhaPay = edhaPay.add(acar.getTransactionMoney());
                            }
                        }

                    }
                    if (edhaPayCoin.setScale(8, BigDecimal.ROUND_DOWN).compareTo(acarIncome.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {

                        //下单冷热账号收支平衡
                    } else {
                        LogFactory.info("edhaPayCoin=" + edhaPayCoin + "acarIncome=" + acarIncome + "2,2---2");
                        flag = true;

                    }
                    BigDecimal edhaliu = edhaIncome.subtract(edhaPay);
                    BigDecimal edhaentru = l.getTransactionSum().subtract(l.getTransactionFee());
                    if (edhaliu.setScale(8, BigDecimal.ROUND_DOWN).compareTo(edhaentru.setScale(8, BigDecimal.ROUND_DOWN)) == 0) {
                        //没错
                    } else {
                        LogFactory.info("edhaliu=" + edhaliu + "edhaentru=" + edhaentru + "2,2---3");
                        flag = true;
                    }
                }


            }

            if (flag) {
                LogFactory.info("l.getEntrustNum()==" + l.getEntrustNum());
            }
            // LogFactory.info("l.getEntrustNum()=="+l.getEntrustNum());
            // System.out.println("i=="+i);
            k = k + i;
        }
        redisService.save("checkExEntrustTime", newdatestr);
        System.out.println("k==" + k);
    }
}
