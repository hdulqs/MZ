/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.trade.entrust.service.impl;

import com.alibaba.fastjson.JSON;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.github.pagehelper.util.StringUtil;
import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.service.log.AppExceptionService;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.redis.common.utils.impl.RedisServiceImpl;
import com.mz.trade.account.service.AppAccountService;
import com.mz.trade.account.service.ExDigitalmoneyAccountService;
import com.mz.trade.comparator.AscBigDecimalComparator;
import com.mz.trade.comparator.DescBigDecimalComparator;
import com.mz.trade.entrust.dao.CommonDao;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.front.redis.model.UserRedis;
import com.mz.trade.comparator.AccountaddComparator;
import com.mz.trade.entrust.dao.ExEntrustDao;
import com.mz.trade.entrust.service.AppPersonInfoService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.entrust.service.TradeService;
import com.mz.trade.model.TradeRedis;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.AppAccountRedis;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Gao Mimi
 * @Date : 2016年4月12日 下午4:45:50
 */
@Import(RedisServiceImpl.class)
@Service("tradeService")
public class TradeServiceImpl implements TradeService {

	private final Logger log = Logger.getLogger(TradeServiceImpl.class);

	@Resource
	public ExDigitalmoneyAccountService exDigitalmoneyAccountService;
	@Resource
	public AppAccountService appAccountService;

	@Resource
	public ExOrderInfoService exOrderInfoService;
	@Resource
	public RedisService redisService;
	@Resource
	private AppPersonInfoService appPersonInfoService;

	@Resource
    private CommonDao commonDao;

	@Resource
	public ExEntrustDao exEntrustDao;
	public void canceltype(EntrustTrade entrustTrade1) {

		if (null == entrustTrade1.getEntrustNum()) { // 委托单号为空，表示全部撤销某个用户的某个交易对的所有委托
			long start = System.currentTimeMillis();
			if (null != entrustTrade1.getCoinCode()) {
				   if(null!=entrustTrade1.getCancelKeepN()){
					    Integer i=0;
						Map<String,Object> map =new  HashMap<String,Object>();
						map.put("customerId", entrustTrade1.getCustomerId().toString());
						map.put("fixPriceCoinCode", entrustTrade1.getFixPriceCoinCode());
						map.put("coinCode", entrustTrade1.getCoinCode());
						map.put("type", entrustTrade1.getType());
					    List<ExEntrust>	listex=exEntrustDao.getEntrustingByCustomerId(map);
					    if(null==listex||listex.size()==0){
					    	return ;
					    }
						for(ExEntrust l:listex){
	                        if(i+entrustTrade1.getCancelKeepN()>=listex.size()){
	                        	break;
	                        }
							EntrustTrade entrustTrade = new EntrustTrade();
							entrustTrade.setEntrustNum(l.getEntrustNum());
							entrustTrade.setCoinCode(l.getCoinCode());
							entrustTrade.setType(l.getType());
							entrustTrade.setFixPriceCoinCode(l.getFixPriceCoinCode());
							entrustTrade.setEntrustPrice(BigDecimal.valueOf(Double.parseDouble(l.getEntrustPrice().toString())).stripTrailingZeros());
							entrustTrade.setEntrustPrice(l.getEntrustPrice().setScale(10, BigDecimal.ROUND_HALF_EVEN));
							cancelExEntrust(entrustTrade);
		                    i++;
						
						}
						
					  long end = System.currentTimeMillis();
					  LogFactory.info("("+(i.toString())+")全部单一币种mq撤销总耗时：" + (end - start) + "毫秒");
				   
					   
					   
				   }else{
					    Integer i=0;
			            //如果光判断这个也是有问题的，如果满了，但是单个撤销了几个，个数就少于满的，但实际上还有不在缓存里的		
				        //	if(list.size()>=EntrustByUser.ingMAXsize-1){  //说明已经满了，需要从数据那全部的未成交委托单
				        //		LogFactory.info("list.size()>=EntrustByUser.ingMAXsize");
						
						Map<String,Object> map =new  HashMap<String,Object>(); 
						map.put("customerId", entrustTrade1.getCustomerId().toString());
						map.put("fixPriceCoinCode", entrustTrade1.getFixPriceCoinCode());
						map.put("coinCode", entrustTrade1.getCoinCode());
					    List<ExEntrust>	listex=exEntrustDao.getEntrustingByCustomerId(map);
					    if(null==listex||listex.size()==0){
					    	
					    	return ;
					    }
						for(ExEntrust l:listex){
	
							EntrustTrade entrustTrade = new EntrustTrade();
							entrustTrade.setEntrustNum(l.getEntrustNum());
							entrustTrade.setCoinCode(l.getCoinCode());
							entrustTrade.setType(l.getType());
							entrustTrade.setFixPriceCoinCode(l.getFixPriceCoinCode());
							entrustTrade.setEntrustPrice(BigDecimal.valueOf(Double.parseDouble(l.getEntrustPrice().toString())).stripTrailingZeros());
							entrustTrade.setEntrustPrice(l.getEntrustPrice().setScale(10, BigDecimal.ROUND_HALF_EVEN));
							cancelExEntrust(entrustTrade);
		                    i++;
						
						}
						
					  long end = System.currentTimeMillis();
					  LogFactory.info("("+(i.toString())+")全部单一币种mq撤销总耗时：" + (end - start) + "毫秒");
				   }
			} else {
				Integer i=0;
				Map<String,Object> map =new  HashMap<String,Object>(); 
				map.put("customerId", entrustTrade1.getCustomerId().toString());
			    List<ExEntrust>	listex=exEntrustDao.getEntrustingByCustomerId(map);
			    if(null==listex||listex.size()==0){
			    	return ;
			    }
				for(ExEntrust l:listex){

					EntrustTrade entrustTrade = new EntrustTrade();
					entrustTrade.setEntrustNum(l.getEntrustNum());
					entrustTrade.setCoinCode(l.getCoinCode());
					entrustTrade.setType(l.getType());
					entrustTrade.setFixPriceCoinCode(l.getFixPriceCoinCode());
					entrustTrade.setEntrustPrice(BigDecimal.valueOf(Double.parseDouble(l.getEntrustPrice().toString())).stripTrailingZeros());
					entrustTrade.setEntrustPrice(l.getEntrustPrice().setScale(10, BigDecimal.ROUND_HALF_EVEN));
					cancelExEntrust(entrustTrade);
                    i++;
				
				}
				long end = System.currentTimeMillis();
				LogFactory.info("("+(i.toString())+")全部撤销总耗时：" + (end - start) + "毫秒");
			}

		} else {

			long start = System.currentTimeMillis();
			entrustTrade1.setEntrustPrice(entrustTrade1.getEntrustPrice().setScale(10, BigDecimal.ROUND_HALF_EVEN));
			cancelExEntrust(entrustTrade1);
			long end = System.currentTimeMillis();
			LogFactory.info("单个mq撤销总耗时：" + (end - start) + "毫秒");

		}

	}

	@Override
    public void matchExtrustToOrderQueue(String exentrust) {
        LogFactory.info("exentrust : " + exentrust);
        EntrustTrade entrust = JSON.parseObject(exentrust, EntrustTrade.class);
        if (null == entrust.getEntrustTime()) { // 撤销
            canceltype(entrust);
            return;
        }
        // 匹配
        //
        RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
        UserRedis userRedis = redisUtil.get(entrust.getCustomerId().toString());
        String code = entrust.getCoinCode();// 交易币
        String priceCode = entrust.getFixPriceCoinCode(); // 定价币
        //
        if (entrust.getType() == 1) {// 如果买 判断定价币
            if (entrust.getFixPriceType().compareTo(0)==0) {
                AppAccountRedis accountRedis = appAccountService.getAppAccountByRedis(userRedis.getAccountId().toString());
                if (accountRedis.getHotMoney().compareTo(entrust.getEntrustPrice().multiply(entrust.getEntrustCount())) < 0) {
                    LogFactory.info("钱余额不足");
                    return;
                }
            } else {
                ExDigitalmoneyAccountRedis ear = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(userRedis.getDmAccountId(priceCode).toString());
                if (ear.getHotMoney().compareTo(entrust.getEntrustPrice().multiply(entrust.getEntrustCount())) < 0) {
                    LogFactory.info("币余额不足");
                    return;
                }
            }
        }
        if (entrust.getType() == 2) {// 如果卖 判断交易币
            ExDigitalmoneyAccountRedis ear = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(userRedis.getDmAccountId(code).toString());
            if (ear.getHotMoney().compareTo(entrust.getEntrustCount()) < 0) {
                return;
            }
        }
        if(entrust.getEntrustWay().compareTo(1)==0){
             if(entrust.getEntrustPrice().compareTo(new BigDecimal(0))<=0||entrust.getEntrustCount().compareTo(new BigDecimal(0))<=0){
                    return ;
             }
        }
        entrust.setEntrustTime(new Date());
        entrust.setEntrustNum(transactionNum(entrust.getEntrustTime()));
        if(entrust.getEntrustPrice().compareTo(new BigDecimal("9999999999")) > 0
                || entrust.getEntrustCount().compareTo(new BigDecimal("9999999999")) > 0
                || entrust.getEntrustSum().compareTo(new BigDecimal("9999999999")) > 0){
            LogFactory.info("超过了数据库的长度，总价或者价格或者量");
            return ;
        }
        matchExtrustToOrder(entrust);
	}

	public String transactionNum(Date date) {
		String randomStr = RandomStringUtils.random(3, false, true);
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		String time = format.format(date);
		String v = redisService.get(time);
		if (!StringUtil.isEmpty(v)) {
			Integer aa = Integer.valueOf(v) + 1;
			String bb = aa.toString();
			if (aa.compareTo(10) == -1) {
				bb = "00" + aa.toString();
			} else if (aa.compareTo(100) == -1) {
				bb = "0" + aa.toString();
			}
			redisService.save(time, aa.toString(), 1);
			return time + bb + randomStr;
		} else {
			redisService.save(time, "1", 1);
			return time + "001" + randomStr;
		}

	}

	public EntrustTrade getById(List<EntrustTrade> list, Integer start, Integer end, Long id) {
		Integer k = (start + end) / 2;
		EntrustTrade et = list.get(k);
		if (et.getId().compareTo(id) == 0) {
			list.remove(k);
			return et;
		} else if (et.getId().compareTo(id) == 1) {
			getById(list, start, k, id);
		} else if (et.getId().compareTo(id) == -1) {
			getById(list, k, end, id);
		}
		return null;
	}

	public void cancelExEntrust(EntrustTrade exEntrust) {
		String key = TradeRedis.getHeader(exEntrust.getCoinCode(), exEntrust.getFixPriceCoinCode(), exEntrust.getType()) + ":" + exEntrust.getEntrustPrice();
		String entrustredis = TradeRedis.getTradeStringData(key);
		if (StringUtil.isEmpty(entrustredis) || entrustredis.equals("[]")) {
			LogFactory.info("撤销失败，keylist为空" + key);
			return;
		}
		List<EntrustTrade> list = JSON.parseArray(entrustredis, EntrustTrade.class);
		int k = 0;
		for (EntrustTrade l : list) {
			if (exEntrust.getEntrustNum().equals(l.getEntrustNum())) {
				exEntrust = l;
				list.remove(l);
				k = 1;
				break;
			}
		}
		if (k == 0) {
			LogFactory.info("撤销失败，因为已经没找到这个委托");
			return;
		}
		if (null == exEntrust) {
			LogFactory.info("撤销失败，因为已经没找到这个委托");
			return;
		}
		if (exEntrust.getStatus() >= 2) {
			LogFactory.info("撤销失败，因为已经之前已经撤销过");
			return;
		}
		if (exEntrust.getStatus().equals(1)) {
			exEntrust.setStatus(3);
		} else if (exEntrust.getStatus().equals(0)) {
			exEntrust.setStatus(4);
		}
		List<Accountadd> aaddlists = new ArrayList<Accountadd>();
		String transactionNum = exEntrust.getEntrustNum();
		if (exEntrust.getType().equals(1)) {
			// 如果是真实货币
			if (exEntrust.getFixPriceType().equals(0)) {
				BigDecimal unfreezeMoney = exEntrust.getEntrustSum().subtract(exEntrust.getTransactionSum());
				Accountadd accountadd1 = getAccountadd(0, exEntrust.getAccountId(), unfreezeMoney, 1, 12, transactionNum);
				aaddlists.add(accountadd1);
				Accountadd accountadd2 = getAccountadd(0, exEntrust.getAccountId(), unfreezeMoney.negate(), 2, 12, transactionNum);
				aaddlists.add(accountadd2);

			} else {
				BigDecimal unfreezeMoney = exEntrust.getEntrustSum().subtract(exEntrust.getTransactionSum());
				Accountadd accountadd1 = getAccountadd(1, exEntrust.getAccountId(), unfreezeMoney, 1, 12, transactionNum);
				aaddlists.add(accountadd1);
				Accountadd accountadd2 = getAccountadd(1, exEntrust.getAccountId(), unfreezeMoney.negate(), 2, 12, transactionNum);
				aaddlists.add(accountadd2);

			}
		}
		// 卖币都一样
		if (exEntrust.getType().equals(2)) {
			BigDecimal unfreezeMoney = exEntrust.getSurplusEntrustCount();
			Accountadd accountadd1 = getAccountadd(1, exEntrust.getCoinAccountId(), unfreezeMoney, 1, 12, transactionNum);
			aaddlists.add(accountadd1);
			Accountadd accountadd2 = getAccountadd(1, exEntrust.getCoinAccountId(), unfreezeMoney.negate(), 2, 12, transactionNum);
			aaddlists.add(accountadd2);
		}
		TradeRedis.cancelEntrust(exEntrust, list, key, aaddlists);

	}

	public void matchExtrustToOrder(EntrustTrade exEntrust) {
		long start = System.currentTimeMillis();
		// type类型 1 ： 买 2 ： 卖
		if (exEntrust.getType().equals(1)) {
			buyExchange(exEntrust);
		} else if (exEntrust.getType().equals(2)) {
			sellExchange(exEntrust);
		}

		long time = System.currentTimeMillis() - start;
		if (time > 20) {
			LogFactory.info("匹配总耗时：" + (time) + "毫秒");
		}
	}

    /**
     * 判断订单是否是对冲订单，是否是机器人帐号
     * @param entrustTrade
     * @param exCointoCoin
     * @return
     */
	private boolean isHedgeEnTrustTrade(EntrustTrade entrustTrade, ExCointoCoin exCointoCoin) {
		if (exCointoCoin.getIsHedge()!= null && exCointoCoin.getIsHedge().equals(1)) {
			String[] autoUsernameArr = null;
			String autoUsernames = exCointoCoin.getAutoUsername();


			Long customerId = exCointoCoin.getCustomerId();
			if (null == customerId) {
				autoUsernameArr = autoUsernames.split(",");
			}
			if (null == autoUsernameArr) {
				return false;
			}
			for (String autoUsername : autoUsernameArr) {
				// TODO: 2018/8/28 0028 设置了对冲后，需要把机器人的用户信息缓存到redis中，
				AppCustomer customer = commonDao.getAppUserByuserName(autoUsername);
				if (customer != null) {
					if (customer.getId().equals(entrustTrade.getCustomerId())) {
						return true;
					}
				}
			}
		} else {
			return false;
		}

		return false;
	}

	private ExCointoCoin getExCointoCoin(EntrustTrade entrustTrade) {
		List<ExCointoCoin> exCointoCoinList = commonDao.getExCointoCoinByCoinCode(entrustTrade.getCoinCode(), entrustTrade.getFixPriceCoinCode());
		if (exCointoCoinList != null && exCointoCoinList.size() > 0) {
			return exCointoCoinList.get(0);
		}
		return null;
	}

    /**
     * 判断两个委托是否可以进行匹配交易
     * @param buyExEntrust
     * @param sellExEntrust
     * @return
     */
	private boolean canMatchTrade(EntrustTrade buyExEntrust, EntrustTrade sellExEntrust) {
	    ExCointoCoin cointoCoin = getExCointoCoin(buyExEntrust);
	    if (isHedgeEnTrustTrade(buyExEntrust, cointoCoin) == isHedgeEnTrustTrade(sellExEntrust, cointoCoin)) {
	        return true;
        }
	    return false;
    }

	/**
	 * 
	 * <p>
	 * (1)买家限价，卖家限价 (2)买家限价，卖家市价 (3)买家市价，卖家限价 (4)买家市价，卖家市价 暂不考虑
	 * </p>
	 * 
	 * @author: Gao Mimi
	 * @param: @param
	 *             exEntrust
	 * @param: @param
	 *             saasId
	 * @return: void
	 * @Date : 2016年4月19日 下午5:21:18
	 * @throws:
	 */
	public void buyExchange(EntrustTrade buyEntrust) {
		if (buyEntrust.getEntrustWay() == 1) {// 买家限价
			// 获取能够匹配的委托单
			long start = System.currentTimeMillis();
            /*
             * BigDecimal sellonePriceold =
             * TradeRedis.getMatchOnePrice(buyEntrust); if (null ==
             * sellonePriceold ||
             * sellonePriceold.compareTo(buyEntrust.getEntrustPrice()) == 1) {
             * // 比卖一价还小，那就没必要去差keys dealFundNoMatch(buyEntrust); return; }
             */
            // 当前是买委托，则查询所有卖委托的价格
            List<BigDecimal> sellKeyList = TradeRedis.getMatchkeys(buyEntrust);// 查所有的keys
            long time = System.currentTimeMillis() - start;
            if (time > 3) {
                LogFactory.info("买委托，取key并排序：" + (time) + "毫秒");
            }

            // 获取能够匹配的委托单
            if (null == sellKeyList || sellKeyList.size() <= 0) {
                dealFundNoMatch(buyEntrust);
                return;
            }
            Collections.sort(sellKeyList, new AscBigDecimalComparator());
            try {
                BigDecimal sellonePrice = null; // 最新成交价
                List<EntrustTrade> listed = new ArrayList<EntrustTrade>();
                // 部分成交的委托单需要更新交易数量和状态
                Map<String, List<EntrustTrade>> maping = new HashMap<String, List<EntrustTrade>>();
                String sellKeyHeader = TradeRedis.getHeaderMatch(buyEntrust);
                sellPriceLoop:
                for (BigDecimal sellPrice : sellKeyList) {
                    // 限价交易，且价格比当前最低的卖单的价格还低，就不再去匹配卖单
                    if (buyEntrust.getEntrustPrice().compareTo(sellPrice) < 0) {
                        sellonePrice = sellPrice;
                        break;
                    }
                    String sellKey = sellKeyHeader + ":" + sellPrice.toString();
                    List<EntrustTrade> sellEntrustList = TradeRedis.getMatchEntrustTradeBykey(sellKey);
                    maping.put(sellKey, sellEntrustList);
                    if (sellEntrustList == null || sellEntrustList.size() == 0) {
                        continue;
                    }
                    for (Iterator<EntrustTrade> it = sellEntrustList.iterator(); it.hasNext(); ) {
                        EntrustTrade sellEntrust = it.next();
                        if (buyEntrust.getEntrustPrice().compareTo(sellEntrust.getEntrustPrice()) < 0) {
                            sellonePrice = sellEntrust.getEntrustPrice();
                            break sellPriceLoop;
                        }

                        if (!canMatchTrade(buyEntrust, sellEntrust)) {
                            continue;
                        }
                        matching(buyEntrust, sellEntrust, "buy");
                        if (sellEntrust.getStatus().equals(2)) {
                            it.remove();
                        }
                        listed.add(sellEntrust);
                        // 如果匹配完了走出循环
                        if (buyEntrust.getStatus().equals(2)) {
                            if (!sellEntrust.getStatus().equals(2)) {
                                sellonePrice = sellEntrust.getEntrustPrice();
                            } else {
                                int index = sellEntrustList.indexOf(sellEntrust);
                                if (index+1 < sellEntrustList.size()) {
                                    EntrustTrade nextSellEntrust = sellEntrustList.get(index+1);
                                    sellonePrice = nextSellEntrust.getEntrustPrice();
                                } else {
                                    sellonePrice = null;
                                }
                            }
                            break sellPriceLoop;
                        }
                    }
                }

                if (buyEntrust.getStatus().equals(0)) { // 当前买单全部未成交
                    dealFundNoMatch(buyEntrust);
                // } else if (buyEntrust.getStatus().equals(1)) { // 当前买委托单部分成交
                } else {
                    TradeRedis.matchOneEnd(dealFundEntrust(buyEntrust), buyEntrust, maping, listed, sellonePrice);
                }
            } catch (Exception e) {
                LogFactory.error(e);
                throw e;
            } finally {
                TradeRedis.eoinfolists.clear();
                TradeRedis.aaddlists.clear();
            }
		} else if (buyEntrust.getEntrustWay().equals(2)) {
			/*
			 * // 买家市价 List<EntrustTrade> list =
			 * TradeRedis.getMatch(buyexEntrust); if (null != list &&
			 * list.size() > 0) { int size = list.size(); List<EntrustTrade>
			 * listed = new ArrayList<EntrustTrade>(); int i = 0; while (i <
			 * size) { EntrustTrade sellentrust = list.get(i);
			 * matching(buyexEntrust, sellentrust, "buy"); if
			 * (sellentrust.getStatus().equals(2)) { list.remove(i); i--;
			 * size--; } listed.add(sellentrust); // 如果匹配完了走出循环 if
			 * (buyexEntrust.getStatus().equals(2)) { return; } i++; } if
			 * (buyexEntrust.getStatus().equals(0)) {
			 * dealFundNoMatch(buyexEntrust); } else {
			 * TradeRedis.matchOneEnd(dealFundEntrust(buyexEntrust),
			 * buyexEntrust, null, listed); } } else { // 保存
			 * dealFundNoMatch(buyexEntrust); }
			 * 
			 */}

	}

	/**
	 * 
	 * <p>
	 * (1)卖家限价，买家限价 (3)卖家限价，买家市价 (2)卖家市价，买家限价 (4)卖家市价，买家市价 暂不考虑
	 * </p>
	 * 
	 * @author: Gao Mimi
	 * @param: @param
	 *             exEntrust
	 * @param: @param
	 *             saasId
	 * @return: void
	 * @Date : 2016年4月19日 下午5:21:18
	 * @throws:
	 */
	public void sellExchange(EntrustTrade sellEntrust) {
		if (sellEntrust.getEntrustWay() == 1) {// 卖家限价 //必须相等才匹配
            long start = System.currentTimeMillis();
            /*
			 * BigDecimal onePrice = TradeRedis.getMatchOnePrice(sellEntrust);
			 * if (null == onePrice ||
			 * onePrice.compareTo(sellentrust.getEntrustPrice()) == -1) { //
			 * 比买一价还大，那就没必要去差keys dealFundNoMatch(sellentrust); return; }
			 */
			List<BigDecimal> buyKeyList = TradeRedis.getMatchkeys(sellEntrust);
            long time = System.currentTimeMillis() - start;
            if (time > 3) {
                LogFactory.info("卖委托取key并排序：" + (time) + "毫秒");
            }
            // 获取能够匹配的委托单
            if (null == buyKeyList || buyKeyList.size() <= 0) {   // 可用
             dealFundNoMatch(sellEntrust);
                return;
            }
            Collections.sort(buyKeyList, new DescBigDecimalComparator());
            try {
                BigDecimal buyOnePrice = null; // 最新成交价
                List<EntrustTrade> listed = new ArrayList<EntrustTrade>();
                Map<String, List<EntrustTrade>> maping = new HashMap<String, List<EntrustTrade>>();
                String buyKeyHeader = TradeRedis.getHeaderMatch(sellEntrust);
                buyPriceLoop:
                for (BigDecimal buyPrice : buyKeyList) {
                    // 限价交易，且价格比最高的买入价格高，就不再去匹配买单
                    if (sellEntrust.getEntrustPrice().compareTo(buyPrice) > 0) {
                        buyOnePrice = buyPrice;
                        break;
                    }
                    String buyKey = buyKeyHeader + ":" + buyPrice.toString();
                    List<EntrustTrade> buyEntrustList = TradeRedis.getMatchEntrustTradeBykey(buyKey);
                    maping.put(buyKey, buyEntrustList);
                    if (buyEntrustList == null || buyEntrustList.size() == 0) {
                        continue;
                    }
                    for (Iterator<EntrustTrade> it = buyEntrustList.iterator(); it.hasNext(); ) {
                        EntrustTrade buyEntrust = it.next();
                        if (sellEntrust.getEntrustPrice().compareTo(buyEntrust.getEntrustPrice()) > 0) {
                            buyOnePrice = buyEntrust.getEntrustPrice();
                            break buyPriceLoop;
                        }
                        if (!canMatchTrade(buyEntrust, sellEntrust)) {
                            continue;
                        }
                        matching(buyEntrust, sellEntrust, "sell");
                        if (buyEntrust.getStatus().equals(2)) {
                            listed.add(buyEntrust);
                            // TODO: 10/24/18 此处需要优化，不使用it.remove()
                            it.remove();
                        }
                        if (sellEntrust.getStatus().equals(2)) {
                            if (!buyEntrust.getStatus().equals(2)) {
                                buyOnePrice = buyEntrust.getEntrustPrice();
                            } else {
                                int index = buyEntrustList.indexOf(buyEntrust);
                                if (index+1 < buyEntrustList.size()) {
                                    EntrustTrade nextBuyEntrust = buyEntrustList.get(index+1);
                                    buyOnePrice = nextBuyEntrust.getEntrustPrice();
                                } else {
                                    buyOnePrice = null;
                                }
                            }
                            break buyPriceLoop;
                        }
                    }
                }

                if (sellEntrust.getStatus().equals(0)) { // 当前买单全部未成交
                    dealFundNoMatch(sellEntrust);
                } else {
                    TradeRedis.matchOneEnd(dealFundEntrust(sellEntrust), sellEntrust, maping, listed, buyOnePrice);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                TradeRedis.eoinfolists.clear();
                TradeRedis.aaddlists.clear();
            }
		} else if (sellEntrust.getEntrustWay().equals(2)) {
			/*
			 * // 卖家市价 // //只要未完成的卖家都可以 List<EntrustTrade> list =
			 * TradeRedis.getMatch(sellentrust); if (null != list && list.size()
			 * > 0) { int size = list.size(); List<EntrustTrade> listed = new
			 * ArrayList<EntrustTrade>(); int i = 0; while (i < size) {
			 * EntrustTrade buyexEntrust = list.get(i); matching(buyexEntrust,
			 * sellentrust, "sell"); // 如果匹配完了走出循环 if
			 * (sellentrust.getStatus().equals(2)) { // break; return; } i++; }
			 * if (sellentrust.getStatus().equals(0)) {
			 * dealFundNoMatch(sellentrust); } else {
			 * TradeRedis.matchOneEnd(dealFundEntrust(sellentrust), sellentrust,
			 * null, listed); } } else { // 保存 dealFundNoMatch(sellentrust);
			 * 
			 * }
			 */}
	}

	/**
	 * 交易所计算机自动撮合系统将买卖申报指令以价格优先、时间优先的原则进行排序，
	 * 当买入价大于、等于卖出价则自动撮合成交。撮合成交价等于买入价（bp）、卖出价（sp）和前一成交价（cp）三者中居中的一个价格。即：
	 * 当 bp≥sp≥cp，则：最新成交价=sp
	 * bp≥cp≥sp，最新成交价=cp
	 * cp≥bp≥sp，最新成交价=bp
	 * @param buyexEntrust
	 * @param sellentrust
	 * @param initiative
	 */
	public void matching(EntrustTrade buyexEntrust, EntrustTrade sellentrust, String initiative) {
		// 买家限价（必须相等才匹配）
		if (buyexEntrust.getEntrustWay().equals(1)) {
			// (1)买家限价，卖家限价
			if (sellentrust.getEntrustWay().equals(1)) {
				oneCase(buyexEntrust, sellentrust, initiative);
				// (2)买家限价，卖家市价
			} else if (sellentrust.getEntrustWay().equals(2)) {
				twoCase(buyexEntrust, sellentrust, initiative);
			}
			// 买家市价 只要未完成的卖家都可以
		} else if (buyexEntrust.getEntrustWay().equals(2)) {
			// (3)买家市价，卖家限价
			if (sellentrust.getEntrustWay().equals(1)) {
				threeCase(buyexEntrust, sellentrust);
				// (4)买家市价，卖家市价
			} else if (sellentrust.getEntrustWay().equals(2)) {
				fourCase(buyexEntrust, sellentrust);
			}
		}
	}

	// (1)买家限价，卖家限价

	/**
	 * 能成交
	 *
	 * 3.6.3 连续竞价时，成交价格的确定原则为：
	 * （一）最高买入申报价格与最低卖出申报价格相同，以该价格为成交价格；
	 * （二）买入申报价格高于即时揭示的最低卖出申报价格的，以即时揭示的最低卖出申报价格为成交价格；
	 * （三）卖出申报价格低于即时揭示的最高买入申报价格的，以即时揭示的最高买入申报价格为成交价格。
	 * 即时揭示的xxx，表示最新的委托价
	 * @param buyexEntrust
	 * @param sellentrust
	 * @param initiative
	 */
	public void oneCase(EntrustTrade buyexEntrust, EntrustTrade sellentrust, String initiative) {
		// 谁小取谁，获取本次交易币的个数
		BigDecimal tradeCount = buyexEntrust.getSurplusEntrustCount().min(sellentrust.getSurplusEntrustCount());
		// 本次交易数量不能为0
		if (tradeCount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		// 交易单价为买单的交易价
		BigDecimal tradePrice = buyexEntrust.getEntrustPrice();
		// 本次交易单价不能为0
		if (tradePrice.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		if (sellentrust.getEntrustPrice().compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		
		// 不相等的情况说明是有浮动的，得求最优成交价（卖家限价，并且价格不相等）
		if (sellentrust.getEntrustWay().equals(1) && buyexEntrust.getEntrustPrice().compareTo(sellentrust.getEntrustPrice()) != 0) {
			BigDecimal[] array = new BigDecimal[4];
			// 买家上限浮动的价格
			array[0] = buyexEntrust.getEntrustPrice().add(buyexEntrust.getFloatUpPrice());
			// 买家下限浮动的价格
			array[1] = buyexEntrust.getEntrustPrice().subtract(buyexEntrust.getFloatDownPrice());
			// 卖家上限浮动的价格
			array[2] = sellentrust.getEntrustPrice().add(sellentrust.getFloatUpPrice());
			// 卖家下限浮动的价格
			array[3] = sellentrust.getEntrustPrice().subtract(sellentrust.getFloatDownPrice());
			java.util.Arrays.sort(array);
			if (initiative.equals("buy")) {// 买主动
				tradePrice = array[1];// 买家当然是价格越低越好
			} else if (initiative.equals("sell")) {// 卖主动
				tradePrice = array[2];// 卖家当然然是价格越低高越好
			}

		}

		dealmatchend(buyexEntrust, sellentrust, tradeCount, tradePrice, initiative);
	}

	// (2)买家限价，卖家市价
	public void twoCase(EntrustTrade buyexEntrust, EntrustTrade sellentrust, String initiative) {
		oneCase(buyexEntrust, sellentrust, initiative);
	}

	/**
	 * (3)买家市价，卖家限价
	 * @param buyExEntrust 买委托，卖委托
	 * @param sellExEntrust
	 */
	public void threeCase(EntrustTrade buyExEntrust, EntrustTrade sellExEntrust) {
		if (buyExEntrust.getEntrustWay() != 2
				&& sellExEntrust.getEntrustWay() != 1) {
			return;
		}
		// 买家剩余委托金额
		BigDecimal buySurplusEntrusMoney = buyExEntrust.getEntrustSum().subtract(buyExEntrust.getTransactionSum());
		// 卖家剩余委托总金额
		BigDecimal sellSurplusEntrusMoney = sellExEntrust.getSurplusEntrustCount().multiply(sellExEntrust.getEntrustPrice());

		BigDecimal tradeCount;
		if (buySurplusEntrusMoney.compareTo(sellSurplusEntrusMoney) <= 0) { // 买家委托全部成交，卖
			tradeCount = buySurplusEntrusMoney.divide(sellExEntrust.getEntrustPrice(), 4, BigDecimal.ROUND_DOWN);
			buyExEntrust.setStatus(2);
		} else {
			tradeCount = sellExEntrust.getSurplusEntrustCount();
		}
		if (tradeCount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		dealmatchend(buyExEntrust, sellExEntrust, tradeCount, sellExEntrust.getEntrustPrice(), "buy");
	}

	// (4)买家市价，卖家市价
	public void fourCase(EntrustTrade buyexEntrust, EntrustTrade sellentrust) {
		/*
		 * 
		 * String tradePricestring =
		 * ExchangeDataCache.getStringData(buyexEntrust.getWebsite() + ":" +
		 * buyexEntrust.getCurrencyType() + ":" + buyexEntrust.getCoinCode() +
		 * ":" + ExchangeDataCache.CurrentExchangPrice);
		 * 
		 * if (null != tradePricestring && new
		 * BigDecimal(tradePricestring).compareTo(new BigDecimal("0")) != 0) {
		 * BigDecimal tradePrice = new BigDecimal(tradePricestring); if
		 * (tradePrice.compareTo(new BigDecimal(0)) == 0) { return; } //
		 * 买家剩余委托金额 BigDecimal buysurplusEntrusMoney =
		 * buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum(
		 * )); // 卖家剩余委托总金额 BigDecimal sellsurplusEntrusMoney =
		 * sellentrust.getSurplusEntrustCount().multiply(tradePrice); BigDecimal
		 * tradeCount = new BigDecimal("0"); if
		 * (buysurplusEntrusMoney.compareTo(sellsurplusEntrusMoney) <= 0) {
		 * tradeCount = buysurplusEntrusMoney.divide(tradePrice, 4,
		 * BigDecimal.ROUND_DOWN); buyexEntrust.setStatus(2); } if
		 * (buysurplusEntrusMoney.compareTo(sellsurplusEntrusMoney) == 1) {
		 * tradeCount = sellentrust.getSurplusEntrustCount();
		 * 
		 * } if (tradeCount.compareTo(new BigDecimal(0)) == 0) { return; }
		 * dealmatchend( buyexEntrust, sellentrust, tradeCount, tradePrice,
		 * "buy");
		 * 
		 * }
		 */}

	public void dealmatchend(EntrustTrade buyexEntrust, EntrustTrade sellentrust, BigDecimal tradeCount, BigDecimal tradePrice, String initiative) {

		ExOrderInfo exOrderInfo = exOrderInfoService.createExOrderInfo(1, buyexEntrust, sellentrust, tradeCount, tradePrice);
		exOrderInfo.setInOrOutTransaction(initiative.equals("buy") ? "sell" : "buy");
		updateBuyExEntrust(buyexEntrust, exOrderInfo);
		updateSellExEntrust(sellentrust, exOrderInfo);
		deductMoney(exOrderInfo, buyexEntrust, sellentrust);
	}

	public void deductMoney(ExOrderInfo exOrderInfo, EntrustTrade buyexEntrust, EntrustTrade sellentrust) {
		if (buyexEntrust.getFixPriceType() == 0) {// 如果是定价是真实货币
			this.deductMoneyByaccount(exOrderInfo, buyexEntrust, sellentrust);
		} else { // 如果定价是虚拟货币
			this.deductMoneyByExDigita(exOrderInfo, buyexEntrust, sellentrust);
		}
	}

	public Accountadd getAccountadd(Integer acccountType, Long accountId, BigDecimal money, Integer monteyType, Integer remarks, String transactionNum) {
		Accountadd accountadd = new Accountadd();
		accountadd.setAcccountType(acccountType);
		accountadd.setMoney(money);
		accountadd.setAccountId(accountId);
		accountadd.setMonteyType(monteyType);
		accountadd.setTransactionNum(transactionNum);
		accountadd.setRemarks(remarks);
		// accountadd.setRemarks(remarks);
		return accountadd;
	}
// todo meld
	public BigDecimal fu(BigDecimal money) {
		return new BigDecimal("0").subtract(money);
	}

	public void deductMoneyByaccount(ExOrderInfo exOrderInfo, EntrustTrade buyexEntrust, EntrustTrade sellentrust) {
		List<Accountadd> aaddlists = new ArrayList<Accountadd>();
		BigDecimal buychangHotMoney = BigDecimal.ZERO;
		BigDecimal buychangColdMoney = BigDecimal.ZERO;
		// 买家人民币账户变动，添加一条冷钱包记录 unfreezeAccountThemBuyTranstion.
		String transactionNumbuy = buyexEntrust.getEntrustNum()+","+exOrderInfo.getOrderNum();
		String transactionNumsell =sellentrust.getEntrustNum() +","+exOrderInfo.getOrderNum();
		Accountadd accountadd5 = getAccountadd(0, buyexEntrust.getAccountId(), exOrderInfo.getTransactionSum().negate(), 2, 2, transactionNumbuy);
		aaddlists.add(accountadd5);
		// 买家是市价
		if (buyexEntrust.getEntrustWay().equals(2) && buyexEntrust.getStatus().equals(2)) { // 买家是市价
			BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
			// 剩余委托金额
			if (surpSum.compareTo(BigDecimal.ZERO) > 0) {
				Accountadd accountadd2 = getAccountadd(0, buyexEntrust.getAccountId(), surpSum.negate(), 2, 3, transactionNumbuy);
				aaddlists.add(accountadd2);
				Accountadd accountadd1 = getAccountadd(0, buyexEntrust.getAccountId(), surpSum, 1, 4, transactionNumbuy);
				aaddlists.add(accountadd1);
			}
			// 买家是限价
		} else if ((buyexEntrust.getEntrustWay().equals(1)) && buyexEntrust.getStatus().equals(2)) {
			if (buyexEntrust.getEntrustSum().compareTo(buyexEntrust.getTransactionSum()) == 1) {
				BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
				Accountadd accountadd3 = getAccountadd(0, buyexEntrust.getAccountId(), surpSum.negate(), 2, 5, transactionNumbuy);
				aaddlists.add(accountadd3);
				Accountadd accountadd4 = getAccountadd(0, buyexEntrust.getAccountId(), surpSum, 1, 6, transactionNumbuy);
				aaddlists.add(accountadd4);
			}
		}

		BigDecimal sellchangHotMoney = BigDecimal.ZERO;
		// 卖家资金变化
		BigDecimal incomeMoney = exOrderInfo.getTransactionSum();
		sellchangHotMoney = sellchangHotMoney.add(incomeMoney);
		Accountadd accountadd7 = getAccountadd(0, sellentrust.getAccountId(), incomeMoney, 1, 7, transactionNumsell);
		aaddlists.add(accountadd7);
		// 卖家手续费
		if (exOrderInfo.getTransactionSellFee().compareTo(new BigDecimal("0")) == 1) {
			Accountadd accountadd8 = getAccountadd(0, sellentrust.getAccountId(), exOrderInfo.getTransactionSellFee().negate(), 1, 8, transactionNumsell);
			aaddlists.add(accountadd8);
		}

		BigDecimal buycoinchangHotMoney = BigDecimal.ZERO;
		BigDecimal sellcoinchangColdMoney = BigDecimal.ZERO;
		// 买家获得币

		BigDecimal incomecoin = exOrderInfo.getTransactionCount();
		Accountadd coinaccountadd1 = getAccountadd(1, buyexEntrust.getCoinAccountId(), incomecoin, 1, 9, transactionNumbuy);
		aaddlists.add(coinaccountadd1);
		// "交易成功，买家手续费"
		if (exOrderInfo.getTransactionBuyFee().compareTo(new BigDecimal("0")) == 1) {
			Accountadd accountadd6 = getAccountadd(1, buyexEntrust.getCoinAccountId(), exOrderInfo.getTransactionBuyFee().negate(), 1, 10, transactionNumbuy);
			aaddlists.add(accountadd6);
		}
		// 卖家支出币
		Accountadd coinaccountadd2 = getAccountadd(1, sellentrust.getCoinAccountId(), incomecoin.negate(), 2, 11, transactionNumsell);
		aaddlists.add(coinaccountadd2);

		// 缓存成交信息
		TradeRedis.matchOneAndOneEnd(exOrderInfo, aaddlists);

	}

	public void deductMoneyByExDigita(ExOrderInfo exOrderInfo, EntrustTrade buyexEntrust, EntrustTrade sellentrust) {
		List<Accountadd> aaddlists = new ArrayList<Accountadd>();
		BigDecimal buychangHotMoney = BigDecimal.ZERO;
		BigDecimal buychangColdMoney = BigDecimal.ZERO;
		// 买家人民币账户变动，添加一条冷钱包记录 unfreezeAccountThemBuyTranstion.
		String transactionNumbuy = buyexEntrust.getEntrustNum()+","+exOrderInfo.getOrderNum();
		String transactionNumsell =sellentrust.getEntrustNum() +","+exOrderInfo.getOrderNum();
		Accountadd accountadd5 = getAccountadd(1, buyexEntrust.getAccountId(), exOrderInfo.getTransactionSum().negate(), 2, 2, transactionNumbuy);
		aaddlists.add(accountadd5);
		// 买家是市价
		if (buyexEntrust.getEntrustWay().equals(2) && buyexEntrust.getStatus().equals(2)) { // 买家是市价
			BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
			// 剩余委托金额
			if (surpSum.compareTo(BigDecimal.ZERO) > 0) {
				Accountadd accountadd2 = getAccountadd(1, buyexEntrust.getAccountId(), surpSum.negate(), 2, 3, transactionNumbuy);
				aaddlists.add(accountadd2);
				Accountadd accountadd1 = getAccountadd(1, buyexEntrust.getAccountId(), surpSum, 1, 4, transactionNumbuy);
				aaddlists.add(accountadd1);
			}
			// 买家是限价
		} else if ((buyexEntrust.getEntrustWay().equals(1)) && buyexEntrust.getStatus().equals(2)) {
			if (buyexEntrust.getEntrustSum().compareTo(buyexEntrust.getTransactionSum()) == 1) {
				BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
				Accountadd accountadd3 = getAccountadd(1, buyexEntrust.getAccountId(), surpSum.negate(), 2, 5, transactionNumbuy);
				aaddlists.add(accountadd3);
				Accountadd accountadd4 = getAccountadd(1, buyexEntrust.getAccountId(), surpSum, 1, 6, transactionNumbuy);
				aaddlists.add(accountadd4);
			}
		}
		//start 新增操盘账户买家免手续费 2018.5.15
		QueryFilter filter = new QueryFilter(AppPersonInfo.class);
		filter.addFilter("customerId=", exOrderInfo.getBuyCustomId());
		AppPersonInfo appPersonInfo = appPersonInfoService.get(filter);
		if(appPersonInfo.getCustomerType()==2){
			exOrderInfo.setTransactionBuyFee(BigDecimal.ZERO);
			exOrderInfo.setTransactionSellFeeRate(BigDecimal.ZERO);
		}
		//end 新增操盘账户买家免手续费 2018.5.15

		//start 新增操盘账户卖家免手续费 2018.5.15
		QueryFilter f = new QueryFilter(AppPersonInfo.class);
		f.addFilter("customerId=", exOrderInfo.getSellCustomId());
		AppPersonInfo seller = appPersonInfoService.get(f);
		if(seller.getCustomerType()==2){
			exOrderInfo.setTransactionSellFee(BigDecimal.ZERO);
			exOrderInfo.setTransactionSellFeeRate(BigDecimal.ZERO);
		}
		//end 新增操盘账户卖家免手续费 2018.5.15

		BigDecimal sellchangHotMoney = BigDecimal.ZERO;
		// 卖家资金变化
		BigDecimal incomeMoney = exOrderInfo.getTransactionSum();
		sellchangHotMoney = sellchangHotMoney.add(incomeMoney);
		Accountadd accountadd7 = getAccountadd(1, sellentrust.getAccountId(), incomeMoney, 1, 7, transactionNumsell);
		aaddlists.add(accountadd7);
		// 卖家手续费
		if (exOrderInfo.getTransactionSellFee().compareTo(new BigDecimal("0")) == 1 && seller.getCustomerType()!=2) {
			Accountadd accountadd8 = getAccountadd(1, sellentrust.getAccountId(), fu(exOrderInfo.getTransactionSellFee()), 1, 8, transactionNumsell);
			aaddlists.add(accountadd8);
		}

		BigDecimal buycoinchangHotMoney = BigDecimal.ZERO;
		BigDecimal sellcoinchangColdMoney = BigDecimal.ZERO;
		// 买家获得币
		BigDecimal incomecoin = exOrderInfo.getTransactionCount();
		Accountadd coinaccountadd1 = getAccountadd(1, buyexEntrust.getCoinAccountId(), incomecoin, 1, 9, transactionNumbuy);
		aaddlists.add(coinaccountadd1);
		// 买家支出手续费
		if (exOrderInfo.getTransactionBuyFee().compareTo(new BigDecimal("0")) == 1 && appPersonInfo.getCustomerType()!=2) {
			Accountadd accountadd6 = getAccountadd(1, buyexEntrust.getCoinAccountId(), fu(exOrderInfo.getTransactionBuyFee()), 1, 10, transactionNumbuy);
			aaddlists.add(accountadd6);
		}
		// 卖家支出币
		Accountadd coinaccountadd2 = getAccountadd(1, sellentrust.getCoinAccountId(), fu(incomecoin), 2, 11, transactionNumsell);
		aaddlists.add(coinaccountadd2);



		// 缓存成交信息
		TradeRedis.matchOneAndOneEnd(exOrderInfo, aaddlists);

	}

	/**
	 * 根据生产的交易订单更新买委托的状态
	 * @param buyExEntrust
	 * @param exOrderInfo
	 */
	public void updateBuyExEntrust(EntrustTrade buyExEntrust, ExOrderInfo exOrderInfo) {
	    if (!buyExEntrust.getEntrustNum().equals(exOrderInfo.getBuyEntrustNum())) {
	        return;
        }
		buyExEntrust.setSurplusEntrustCount(buyExEntrust.getSurplusEntrustCount().subtract(exOrderInfo.getTransactionCount()));
		buyExEntrust.setTransactionFee(buyExEntrust.getTransactionFee().add(exOrderInfo.getTransactionBuyFee()));
		buyExEntrust.setTransactionSum(buyExEntrust.getTransactionSum().add(exOrderInfo.getTransactionSum()));
		// 平均价格
		buyExEntrust.setProcessedPrice(buyExEntrust.getTransactionSum().divide(buyExEntrust.getEntrustCount().subtract(buyExEntrust.getSurplusEntrustCount()), 10, BigDecimal.ROUND_HALF_UP));

		// 如果是市价
		if (buyExEntrust.getEntrustWay().equals(2)) {

			if (!buyExEntrust.getStatus().equals(2)) {
				buyExEntrust.setStatus(1);
			}

			// }
		} else {// 是限价，还是普通价格优先都有剩余个数
			// 剩余个数为0，说明已完成
			if (buyExEntrust.getSurplusEntrustCount().compareTo(BigDecimal.ZERO) <= 0) {
				buyExEntrust.setStatus(2);
			} else {
				buyExEntrust.setStatus(1);

			}
		}
	}

	public void updateSellExEntrust(EntrustTrade sellentrust, ExOrderInfo exOrderInfo) {
		sellentrust.setSurplusEntrustCount(sellentrust.getSurplusEntrustCount().subtract(exOrderInfo.getTransactionCount()));
		sellentrust.setTransactionFee(sellentrust.getTransactionFee().add(exOrderInfo.getTransactionSellFee()));
		sellentrust.setTransactionSum(sellentrust.getTransactionSum().add(exOrderInfo.getTransactionSum()));

		// 平均价格
		sellentrust.setProcessedPrice(sellentrust.getTransactionSum().divide(sellentrust.getEntrustCount().subtract(sellentrust.getSurplusEntrustCount()), 8, BigDecimal.ROUND_HALF_UP));

		// 剩余个数为0，说明已完成（卖家不管是限价还是市价就会有余额这个值）
		sellentrust.setStatus(1);
		if (sellentrust.getSurplusEntrustCount().compareTo(new BigDecimal(0)) <= 0) {
			sellentrust.setStatus(2);
		}
	}

	public void dealFundNoMatch(EntrustTrade exEntrust) {
		long start3 = System.currentTimeMillis();
		TradeRedis.NoMatchEnd(exEntrust, dealFundEntrust(exEntrust));
		long end3 = System.currentTimeMillis();
		// LogFactory.info("匹配失败业务逻辑end：" + (end3 - start3) + "毫秒");
	}

	public List<Accountadd> dealFundEntrust(EntrustTrade exEntrust) {
		List<Accountadd> aaddlists = new ArrayList<Accountadd>();
		String transactionNum = exEntrust.getEntrustNum();
		if (exEntrust.getType().equals(1)) {// 买单
			if (exEntrust.getFixPriceType().equals(0)) { // 定价真实货币
				BigDecimal freezeMoney = exEntrust.getEntrustSum();
				// 重新计算冷热钱包的总额
				Accountadd accountadd1 = getAccountadd(0, exEntrust.getAccountId(), freezeMoney.negate(), 1, 1, transactionNum);
				aaddlists.add(accountadd1);
				Accountadd accountadd2 = getAccountadd(0, exEntrust.getAccountId(), freezeMoney, 2, 1, transactionNum);
				aaddlists.add(accountadd2);
			} else {// 定价虚拟货币
				BigDecimal freezeMoney = exEntrust.getEntrustSum();
				Accountadd accountadd1 = getAccountadd(1, exEntrust.getAccountId(), freezeMoney.negate(), 1, 1, transactionNum);
				aaddlists.add(accountadd1);
				Accountadd accountadd2 = getAccountadd(1, exEntrust.getAccountId(), freezeMoney, 2, 1, transactionNum);
				aaddlists.add(accountadd2);
			}

		} else if (exEntrust.getType().equals(2)) {//
			BigDecimal freezeMoney = exEntrust.getEntrustCount();
			Accountadd accountadd1 = getAccountadd(1, exEntrust.getCoinAccountId(), freezeMoney.negate(), 1, 1, transactionNum);
			aaddlists.add(accountadd1);
			Accountadd accountadd2 = getAccountadd(1, exEntrust.getCoinAccountId(), freezeMoney, 2, 1, transactionNum);
			aaddlists.add(accountadd2);
		}

		return aaddlists;
	}

	@Override
	public Boolean accountaddQueue(String accoutadds) {
		Boolean  flag=true;
		JedisPool jedisPool = (JedisPool) ContextUtil.getBean("jedisPool");
		Jedis jedis=jedisPool.getResource();
		try {
		
		Transaction transaction = jedis.multi();
		List<Accountadd> accountaddlist = JSON.parseArray(accoutadds, Accountadd.class);
		transaction.set(TradeRedis.getTradeDealAccountChangeNum(), JSON.toJSONString(accountaddlist));
	
		
		AccountaddComparator accountaddComparator=new AccountaddComparator();
		Collections.sort(accountaddlist,accountaddComparator);
		Long coinaccountId=null;
    	ExDigitalmoneyAccountRedis coinaccount =null;
    	Long accountId=null;
    	AppAccountRedis appAccount =null;
		for (Accountadd accountadd : accountaddlist) {
			if (accountadd.getAcccountType().equals(1)) {
				if(null==coinaccountId||accountadd.getAccountId().compareTo(coinaccountId)!=0){
					if(null!=coinaccount){
						RuntimeSchema<ExDigitalmoneyAccountRedis>  schema = RuntimeSchema.createFrom(ExDigitalmoneyAccountRedis.class);
						byte[] bytes = ProtostuffIOUtil.toByteArray(coinaccount, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
						String key = "RedisDB:" +ExDigitalmoneyAccountRedis.class.getName().replace(".", ":")+ ":" + coinaccount.getId();
						transaction.del(key.getBytes());
						transaction.set(key.getBytes(), bytes);
						}
					
					 coinaccount = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(accountadd.getAccountId().toString());
					 coinaccountId=accountadd.getAccountId();
				}
				if (null != coinaccount) {
					if (accountadd.getMonteyType().equals(1)) {
						coinaccount.setHotMoney(coinaccount.getHotMoney().add(accountadd.getMoney()));
					} else {
						coinaccount.setColdMoney(coinaccount.getColdMoney().add(accountadd.getMoney()));
					}
				
				//	exDigitalmoneyAccountService.setExDigitalmoneyAccounttoRedis(coinaccount);
				}else{
					 LogFactory.info("mq:redis资金账户没有查到=="+accountadd.getAccountId());
				}

			} else {
				if(null==accountId||accountadd.getAccountId().compareTo(accountId)!=0){
					if(null!=appAccount){
						RuntimeSchema<AppAccountRedis>  schema = RuntimeSchema.createFrom(AppAccountRedis.class);
						byte[] bytes = ProtostuffIOUtil.toByteArray(appAccount, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
						String key = "RedisDB:" +AppAccountRedis.class.getName().replace(".", ":")+ ":" + appAccount.getId();
						transaction.del(key.getBytes());
						transaction.set(key.getBytes(), bytes);
					
					}
					 appAccount = appAccountService.getAppAccountByRedis(accountadd.getAccountId().toString());
					
					 accountId=accountadd.getAccountId();
				}
				
				if (null != appAccount) {
					if (accountadd.getMonteyType().equals(1)) {
						appAccount.setHotMoney(appAccount.getHotMoney().add(accountadd.getMoney()));
					} else {
						appAccount.setColdMoney(appAccount.getColdMoney().add(accountadd.getMoney()));
					}
					
					
				}else{
					
					 LogFactory.info("mq:redis虚拟账户没有查到=="+accountadd.getAccountId());
				}

			}
			
		
		}	
		if(null!=coinaccount){
			RuntimeSchema<ExDigitalmoneyAccountRedis>  schema = RuntimeSchema.createFrom(ExDigitalmoneyAccountRedis.class);
			byte[] bytes = ProtostuffIOUtil.toByteArray(coinaccount, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			String key = "RedisDB:" +ExDigitalmoneyAccountRedis.class.getName().replace(".", ":")+ ":" + coinaccount.getId();
			transaction.del(key.getBytes());
			transaction.set(key.getBytes(), bytes);
			 LogFactory.info("coinaccount.getHotMoney()=="+coinaccount.getHotMoney()+"=="+coinaccount.getUserName());
		}
	
		if(null!=appAccount){
			RuntimeSchema<AppAccountRedis>  schema1 = RuntimeSchema.createFrom(AppAccountRedis.class);
			byte[] bytes1 = ProtostuffIOUtil.toByteArray(appAccount, schema1, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			String key1 = "RedisDB:" +AppAccountRedis.class.getName().replace(".", ":")+ ":" + appAccount.getId();
			transaction.del(key1.getBytes());
			transaction.set(key1.getBytes(), bytes1);
			 LogFactory.info("appAccount.getHotMoney()"+appAccount.getHotMoney()+"=="+appAccount.getUserName());
		}
		
		
		
		
		List<Object> list =transaction.exec();
		if(null==list||list.size()==0){
		    flag=false;
		}
		
		
		 LogFactory.info("accoutadds==="+accoutadds);
		
		}catch(Exception e	) {
			 AppException exceptionLog = new AppException();
			 exceptionLog.setName("mq==accountaddQueue==");
	         AppExceptionService appExceptionService=(AppExceptionService) ContextUtil.getBean("appExceptionService");
			 appExceptionService.save(exceptionLog);
			 System.out.println("mq==accountaddQueue=="+accoutadds);
			e.printStackTrace();
			throw e;
		}finally {
			jedis.close();
		}
		
		return flag;
	//	return flag;
		
		/*
		 * // 添加资金记录 RedisService redisService = (RedisService)
		 * ContextUtil.getBean("redisService"); String v =
		 * redisService.get(ExchangeDataCacheRedis.AccountAddS);
		 * List<Accountadd> list = JSON.parseArray(v, Accountadd.class); if
		 * (null == list) { list = new ArrayList<Accountadd>(); }
		 * list.addAll(accountaddlist);
		 * redisService.save(ExchangeDataCacheRedis.AccountAddS,
		 * JSON.toJSONString(list));
		 */
     
	}
}
