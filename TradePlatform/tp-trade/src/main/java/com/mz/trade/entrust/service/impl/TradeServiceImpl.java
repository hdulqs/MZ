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
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.redis.common.utils.impl.RedisServiceImpl;
import com.mz.trade.account.service.AppAccountService;
import com.mz.trade.account.service.ExDigitalmoneyAccountService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public ExEntrustDao exEntrustDao;
	public void canceltype(EntrustTrade entrustTrade1) {

		if (null == entrustTrade1.getEntrustNum()) {
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
		 LogFactory.info("exentrust"+exentrust);
		EntrustTrade entrust = JSON.parseObject(exentrust, EntrustTrade.class);
	
		if (null == entrust.getEntrustTime()) { // 撤销
			canceltype(entrust);
		} else { // 匹配

			RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
			UserRedis userRedis = redisUtil.get(entrust.getCustomerId().toString());

			String code = entrust.getCoinCode();// 交易币
			String priceCode = entrust.getFixPriceCoinCode(); // 定价币
			//
			if (entrust.getType() == 1) {// 如果买 判断定价币
				if (entrust.getFixPriceType().compareTo(0)==0) {
					AppAccountRedis accountRedis = appAccountService.getAppAccountByRedis(userRedis.getAccountId().toString());

					if (accountRedis.getHotMoney().compareTo(entrust.getEntrustPrice().multiply(entrust.getEntrustCount())) < 0) {
						/*
						 * rt[0] = CodeConstant.CODE_FAILED; rt[1] = priceCode +
						 * "不足"; return rt;
						 */
						LogFactory.info("钱余额不足");
						return;
					}
				} else {

					ExDigitalmoneyAccountRedis ear = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(userRedis.getDmAccountId(priceCode).toString());

					if (ear.getHotMoney().compareTo(entrust.getEntrustPrice().multiply(entrust.getEntrustCount())) < 0) {
						/*
						 * rt[0] = CodeConstant.CODE_FAILED; rt[1] = priceCode +
						 * "不足"; return rt;
						 */
						LogFactory.info("币余额不足");
						return;
					}
				}
			}

			if (entrust.getType() == 2) {// 如果卖 判断交易币
				ExDigitalmoneyAccountRedis ear = exDigitalmoneyAccountService.getExDigitalmoneyAccountByRedis(userRedis.getDmAccountId(code).toString());

				if (ear.getHotMoney().compareTo(entrust.getEntrustCount()) < 0) {
					/*
					 * rt[0] = CodeConstant.CODE_FAILED; rt[1] = code + "不足";
					 * return rt;
					 */
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
			if(entrust.getEntrustPrice().compareTo(new BigDecimal("9999999999"))==1 || 
					entrust.getEntrustCount().compareTo(new BigDecimal("9999999999"))==1 ||
					entrust.getEntrustSum().compareTo(new BigDecimal("9999999999"))==1 ){
				LogFactory.info("超过了数据库的长度，总价或者价格或者量");
				return ;
			}
			matchExtrustToOrder(entrust);

		}

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
				Accountadd accountadd2 = getAccountadd(0, exEntrust.getAccountId(), fu(unfreezeMoney), 2, 12, transactionNum);
				aaddlists.add(accountadd2);

			} else {
				BigDecimal unfreezeMoney = exEntrust.getEntrustSum().subtract(exEntrust.getTransactionSum());
				Accountadd accountadd1 = getAccountadd(1, exEntrust.getAccountId(), unfreezeMoney, 1, 12, transactionNum);
				aaddlists.add(accountadd1);
				Accountadd accountadd2 = getAccountadd(1, exEntrust.getAccountId(), fu(unfreezeMoney), 2, 12, transactionNum);
				aaddlists.add(accountadd2);

			}
		}
		// 卖币都一样
		if (exEntrust.getType().equals(2)) {
			BigDecimal unfreezeMoney = exEntrust.getSurplusEntrustCount();
			Accountadd accountadd1 = getAccountadd(1, exEntrust.getCoinAccountId(), unfreezeMoney, 1, 12, transactionNum);
			aaddlists.add(accountadd1);
			Accountadd accountadd2 = getAccountadd(1, exEntrust.getCoinAccountId(), fu(unfreezeMoney), 2, 12, transactionNum);
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

		long end = System.currentTimeMillis();
		long time=end - start;
		if(time>20){
		  LogFactory.info("匹配总耗时：" + (time) + "毫秒");
		}
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
	public void buyExchange(EntrustTrade buyexEntrust) {
		// System.out.println("buyExchange==" + buyexEntrust.getEntrustNum());
		if (buyexEntrust.getEntrustWay().equals(1)) {// 买家限价
			// 获取能够匹配的委托单
			long start = System.currentTimeMillis();
			/*
			 * BigDecimal sellonePriceold =
			 * TradeRedis.getMatchOnePrice(buyexEntrust); if (null ==
			 * sellonePriceold ||
			 * sellonePriceold.compareTo(buyexEntrust.getEntrustPrice()) == 1) {
			 * // 比卖一价还小，那就没必要去差keys dealFundNoMatch(buyexEntrust); return; }
			 */
			List<BigDecimal> keyslist = TradeRedis.getMatchkeys(buyexEntrust);// 查所有的keys
			long end = System.currentTimeMillis();
			long time=end - start;
			if(time>3){
				 LogFactory.info("取key并排序：" + (time) + "毫秒");
			}
			
			// 获取能够匹配的委托单
			BigDecimal sellonePrice = null;
			long start1 = System.currentTimeMillis();
			if (null != keyslist && keyslist.size() > 0) {
				try {
					List<EntrustTrade> listed = new ArrayList<EntrustTrade>();
					Map<String, List<EntrustTrade>> maping = new HashMap<String, List<EntrustTrade>>();

					outterLoop: for (BigDecimal keybig : keyslist) {
						String keyall = TradeRedis.getHeaderMatch(buyexEntrust) + ":" + keybig.toString();
						List<EntrustTrade> list = TradeRedis.getMatchEntrustTradeBykey(keyall);
						if (null != list && list.size() > 0) {
							maping.put(keyall, list);
							int size = list.size();
							int i = 0;
							while (i < size) {
								EntrustTrade sellentrust = list.get(i);
								if (sellentrust.getEntrustPrice().compareTo(buyexEntrust.getEntrustPrice()) == 1) {
									sellonePrice = sellentrust.getEntrustPrice();
									break outterLoop;
								}
								matching(buyexEntrust, sellentrust, "buy");
								if (sellentrust.getStatus().equals(2)) {
									list.remove(i);
									i--;
									size--;
								}
								listed.add(sellentrust); // 完成的
								// 如果匹配完了走出循环
								if (buyexEntrust.getStatus().equals(2)) {
									if (!sellentrust.getStatus().equals(2)) {
										sellonePrice = sellentrust.getEntrustPrice();
									} else {
										if (i + 1 < size) {
											EntrustTrade sellentrustsellone = list.get(i + 1);
											sellonePrice = sellentrustsellone.getEntrustPrice();
										} else {
											sellonePrice = null;
										}

									}
									break outterLoop;
								}
								i++;
							}
						}
					}

					long end1 = System.currentTimeMillis();
					// LogFactory.info("业务逻辑：" + (end1 - start1) + "毫秒");
					if (buyexEntrust.getStatus().equals(0)) {
						long start2 = System.currentTimeMillis();
						dealFundNoMatch(buyexEntrust);
						long end2 = System.currentTimeMillis();
						/* LogFactory.info("匹配失败业务逻辑end：" + (end2 - start2) +
						 "毫秒");*/
					} else {
						long start2 = System.currentTimeMillis();
						TradeRedis.matchOneEnd(dealFundEntrust(buyexEntrust), buyexEntrust, maping, listed, sellonePrice);
						long end2 = System.currentTimeMillis();
					/*	 LogFactory.info("匹配成功业务逻辑end：" + (end2 - start2) +
						 "毫秒");*/
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					TradeRedis.eoinfolists = new ArrayList<ExOrderInfo>();
					TradeRedis.aaddlists = new ArrayList<Accountadd>();
				}

			} else {
				// 保存
				long start3 = System.currentTimeMillis();
				dealFundNoMatch(buyexEntrust);
				long end3 = System.currentTimeMillis();
			//	LogFactory.info("匹配失败业务逻辑end：" + (end3 - start3) + "毫秒");
			}
		} else if (buyexEntrust.getEntrustWay().equals(2)) {
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
	public void sellExchange(EntrustTrade sellentrust) {
		// System.out.println("sellExchange==" + sellentrust.getEntrustNum());
		if (sellentrust.getEntrustWay().equals(1)) {// 卖家限价 //必须相等才匹配
			/*
			 * BigDecimal onePrice = TradeRedis.getMatchOnePrice(sellentrust);
			 * if (null == onePrice ||
			 * onePrice.compareTo(sellentrust.getEntrustPrice()) == -1) { //
			 * 比买一价还大，那就没必要去差keys dealFundNoMatch(sellentrust); return; }
			 */
			List<BigDecimal> keyslist = TradeRedis.getMatchkeys(sellentrust);
			if (null != keyslist && keyslist.size() > 0) {
				try {
					List<EntrustTrade> listed = new ArrayList<EntrustTrade>();
					Map<String, List<EntrustTrade>> maping = new HashMap<String, List<EntrustTrade>>();
					BigDecimal buyonePrice = null;
					outterLoop: for (BigDecimal keybig : keyslist) {
						String keyall = TradeRedis.getHeaderMatch(sellentrust) + ":" + keybig.toString();
						List<EntrustTrade> list = TradeRedis.getMatchEntrustTradeBykey(keyall);
						if (null != list && list.size() > 0) {
							maping.put(keyall, list);
							int size = list.size();
							int i = 0;
							while (i < size) {
								EntrustTrade buyexEntrust = list.get(i);
								if (buyexEntrust.getEntrustPrice().compareTo(sellentrust.getEntrustPrice()) == -1) {
									buyonePrice = buyexEntrust.getEntrustPrice();
									break outterLoop;
								}
								matching(buyexEntrust, sellentrust, "sell");
								if (buyexEntrust.getStatus().equals(2)) {
									list.remove(i);
									i--;
									size--;
								}
								listed.add(buyexEntrust);
								// 如果匹配完了走出循环
								if (sellentrust.getStatus().equals(2)) {
									if (!buyexEntrust.getStatus().equals(2)) {
										buyonePrice = buyexEntrust.getEntrustPrice();
									} else {
										if (i + 1 < size) {
											EntrustTrade buyexEntrustone = list.get(i + 1);
											buyonePrice = buyexEntrustone.getEntrustPrice();
										} else {
											buyonePrice = null;
										}
									}
									break outterLoop;
								}
								i++;
							}
						}
					}
					if (sellentrust.getStatus().equals(0)) {
						dealFundNoMatch(sellentrust);
					} else {
						TradeRedis.matchOneEnd(dealFundEntrust(sellentrust), sellentrust, maping, listed, buyonePrice);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					TradeRedis.eoinfolists = new ArrayList<ExOrderInfo>();
					TradeRedis.aaddlists = new ArrayList<Accountadd>();
				}
				// 处理资金问题，进入资金处理队列todo
			} else {
				// 保存
				dealFundNoMatch(sellentrust);

			}
		} else if (sellentrust.getEntrustWay().equals(2)) {
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
	public void oneCase(EntrustTrade buyexEntrust, EntrustTrade sellentrust, String initiative) {
		// 谁小取谁，获取本次交易币的个数
		BigDecimal tradeCount = buyexEntrust.getSurplusEntrustCount().compareTo(sellentrust.getSurplusEntrustCount()) <= 0 ? buyexEntrust.getSurplusEntrustCount() : sellentrust.getSurplusEntrustCount();
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

	// (3)买家市价，卖家限价
	public void threeCase(EntrustTrade buyexEntrust, EntrustTrade sellentrust) {

		// 买家剩余委托金额
		BigDecimal buysurplusEntrusMoney = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
		// 卖家剩余委托总金额
		BigDecimal sellsurplusEntrusMoney = sellentrust.getSurplusEntrustCount().multiply(sellentrust.getEntrustPrice());

		BigDecimal tradeCount = new BigDecimal("0");
		if (buysurplusEntrusMoney.compareTo(sellsurplusEntrusMoney) <= 0) {
			tradeCount = buysurplusEntrusMoney.divide(sellentrust.getEntrustPrice(), 4, BigDecimal.ROUND_DOWN);
			buyexEntrust.setStatus(2);
		}
		if (buysurplusEntrusMoney.compareTo(sellsurplusEntrusMoney) == 1) {
			tradeCount = sellentrust.getSurplusEntrustCount();
		}
		if (tradeCount.compareTo(new BigDecimal(0)) == 0) {
			return;
		}
		BigDecimal tradePrice = sellentrust.getEntrustPrice();

		if (tradePrice.compareTo(new BigDecimal(0)) == 0) {
			return;
		}
		dealmatchend(buyexEntrust, sellentrust, tradeCount, tradePrice, "buy");
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
		updatebuyExEntrust(buyexEntrust, sellentrust, exOrderInfo);
		updatesellExEntrust(buyexEntrust, sellentrust, exOrderInfo);
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
		Accountadd accountadd5 = getAccountadd(0, buyexEntrust.getAccountId(), fu(exOrderInfo.getTransactionSum()), 2, 2, transactionNumbuy);
		aaddlists.add(accountadd5);
		// 买家是市价
		if (buyexEntrust.getEntrustWay().equals(2) && buyexEntrust.getStatus().equals(2)) { // 买家是市价
			BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
			// 剩余委托金额
			if (surpSum.compareTo(BigDecimal.ZERO) > 0) {
				Accountadd accountadd2 = getAccountadd(0, buyexEntrust.getAccountId(), fu(surpSum), 2, 3, transactionNumbuy);
				aaddlists.add(accountadd2);
				Accountadd accountadd1 = getAccountadd(0, buyexEntrust.getAccountId(), surpSum, 1, 4, transactionNumbuy);
				aaddlists.add(accountadd1);
			}
			// 买家是限价
		} else if ((buyexEntrust.getEntrustWay().equals(1)) && buyexEntrust.getStatus().equals(2)) {
			if (buyexEntrust.getEntrustSum().compareTo(buyexEntrust.getTransactionSum()) == 1) {
				BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
				Accountadd accountadd3 = getAccountadd(0, buyexEntrust.getAccountId(), fu(surpSum), 2, 5, transactionNumbuy);
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
			Accountadd accountadd8 = getAccountadd(0, sellentrust.getAccountId(), fu(exOrderInfo.getTransactionSellFee()), 1, 8, transactionNumsell);
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
			Accountadd accountadd6 = getAccountadd(1, buyexEntrust.getCoinAccountId(), fu(exOrderInfo.getTransactionBuyFee()), 1, 10, transactionNumbuy);
			aaddlists.add(accountadd6);
		}
		// 卖家支出币
		Accountadd coinaccountadd2 = getAccountadd(1, sellentrust.getCoinAccountId(), fu(incomecoin), 2, 11, transactionNumsell);
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
		Accountadd accountadd5 = getAccountadd(1, buyexEntrust.getAccountId(), fu(exOrderInfo.getTransactionSum()), 2, 2, transactionNumbuy);
		aaddlists.add(accountadd5);
		// 买家是市价
		if (buyexEntrust.getEntrustWay().equals(2) && buyexEntrust.getStatus().equals(2)) { // 买家是市价
			BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
			// 剩余委托金额
			if (surpSum.compareTo(BigDecimal.ZERO) > 0) {
				Accountadd accountadd2 = getAccountadd(1, buyexEntrust.getAccountId(), fu(surpSum), 2, 3, transactionNumbuy);
				aaddlists.add(accountadd2);
				Accountadd accountadd1 = getAccountadd(1, buyexEntrust.getAccountId(), surpSum, 1, 4, transactionNumbuy);
				aaddlists.add(accountadd1);
			}
			// 买家是限价
		} else if ((buyexEntrust.getEntrustWay().equals(1)) && buyexEntrust.getStatus().equals(2)) {
			if (buyexEntrust.getEntrustSum().compareTo(buyexEntrust.getTransactionSum()) == 1) {
				BigDecimal surpSum = buyexEntrust.getEntrustSum().subtract(buyexEntrust.getTransactionSum());
				Accountadd accountadd3 = getAccountadd(1, buyexEntrust.getAccountId(), fu(surpSum), 2, 5, transactionNumbuy);
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

	public void updatebuyExEntrust(EntrustTrade buyExEntrust, EntrustTrade sellentrust, ExOrderInfo exOrderInfo) {

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

	public void updatesellExEntrust(EntrustTrade buyExEntrust, EntrustTrade sellentrust, ExOrderInfo exOrderInfo) {
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
				Accountadd accountadd1 = getAccountadd(0, exEntrust.getAccountId(), fu(freezeMoney), 1, 1, transactionNum);
				aaddlists.add(accountadd1);
				Accountadd accountadd2 = getAccountadd(0, exEntrust.getAccountId(), freezeMoney, 2, 1, transactionNum);
				aaddlists.add(accountadd2);
			} else {// 定价虚拟货币
				BigDecimal freezeMoney = exEntrust.getEntrustSum();
				Accountadd accountadd1 = getAccountadd(1, exEntrust.getAccountId(), fu(freezeMoney), 1, 1, transactionNum);
				aaddlists.add(accountadd1);
				Accountadd accountadd2 = getAccountadd(1, exEntrust.getAccountId(), freezeMoney, 2, 1, transactionNum);
				aaddlists.add(accountadd2);
			}

		} else if (exEntrust.getType().equals(2)) {//
			BigDecimal freezeMoney = exEntrust.getEntrustCount();
			Accountadd accountadd1 = getAccountadd(1, exEntrust.getCoinAccountId(), fu(freezeMoney), 1, 1, transactionNum);
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
