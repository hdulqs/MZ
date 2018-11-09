/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月24日 上午9:36:36
 */
package com.mz.exchange.entrust.service.impl;

import com.mz.customer.person.model.AppPersonInfo;
import com.mz.redis.common.utils.RedisService;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrder;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.date.DateUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.core.constant.StringConstant;
import com.mz.customer.remote.RemoteAppPersonInfoService;
import com.mz.trade.entrust.ExchangeDataCache;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.entrust.service.ExOrderService;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 
 * @author gaomm
 *
 */
public class OrderRunable implements Runnable {
	
	private final static Logger log = Logger.getLogger(OrderRunable.class);
	
	public ExOrderInfo exOrderInfobuy;
	public ExOrderInfo exOrderInfosell;
	public ExOrder exOrder;
	public ExEntrust buyexEntrust;
	public ExEntrust sellentrust;
	public OrderRunable(ExOrderInfo exOrderInfobuy,ExOrderInfo exOrderInfosell,ExOrder exOrder,ExEntrust buyexEntrust,ExEntrust sellentrust){
		this.exOrderInfobuy = exOrderInfobuy;
		this.exOrderInfosell = exOrderInfosell;
		this.exOrder = exOrder;
		this.buyexEntrust = buyexEntrust;
		this.sellentrust = sellentrust;
	}
	
	@Override
	public void run() {
		long end = System.currentTimeMillis();
		ExOrderInfoService exOrderInfoService =(ExOrderInfoService)ContextUtil.getBean("exOrderInfoService");
		ExOrderService exOrderService =(ExOrderService)ContextUtil.getBean("exOrderService");
		updateExOrderInfo(exOrderInfobuy,buyexEntrust,sellentrust,exOrderInfobuy.getType());
		updateExOrderInfo(exOrderInfosell,buyexEntrust,sellentrust,exOrderInfosell.getType());
		
		exOrderInfoService.save(exOrderInfobuy);
		exOrderInfoService.save(exOrderInfosell);
		exOrderService.save(exOrder);
		long start = System.currentTimeMillis();
		setExchangeDataCache( exOrderInfobuy,  exOrder);
		LogFactory.info("保存exOrderInfo：" + (end - start) + "毫秒");
	}
	public void updateExOrderInfo(ExOrderInfo exOrderInfo ,ExEntrust buyExEntrust,ExEntrust sellentrust,Integer type){

		RemoteAppPersonInfoService remoteAppPersonInfoService = (RemoteAppPersonInfoService)ContextUtil.getBean("remoteAppPersonInfoService");
		//订单开始详细
		exOrderInfo.setSaasId(buyExEntrust.getSaasId());
	
		
		exOrderInfo.setBuyEntrustNum(buyExEntrust.getEntrustNum());
		
		exOrderInfo.setSellEntrustNum(sellentrust.getEntrustNum());
		
		exOrderInfo.setTransactionBuyFeeRate(buyExEntrust.getTransactionFeeRate());
		exOrderInfo.setTransactionSellFeeRate(sellentrust.getTransactionFeeRate());
		exOrderInfo.setSellUserCode(sellentrust.getUserCode());
		exOrderInfo.setBuyUserCode(buyExEntrust.getUserCode());
		exOrderInfo.setBuyUserName(buyExEntrust.getUserName());
		exOrderInfo.setSellUserName(sellentrust.getUserName());
		
		
		AppPersonInfo appPersonInfo1 = remoteAppPersonInfoService.getByCustomerId(exOrderInfo.getBuyCustomId());
		exOrderInfo.setBuyTrueName(appPersonInfo1.getTrueName());
	
		AppPersonInfo appPersonInfo = remoteAppPersonInfoService.getByCustomerId(exOrderInfo.getSellCustomId());
		exOrderInfo.setSellTrueName(appPersonInfo.getTrueName());


		exOrderInfo.setTransactionTime(new Date());
		exOrderInfo.setOrderTimestapm(exOrderInfo.getTransactionTime().getTime());
		
		
		if(type==1){
			exOrderInfo.setCustomerId(exOrderInfo.getBuyCustomId());
			exOrderInfo.setUserCode(exOrderInfo.getBuyUserCode());
			exOrderInfo.setUserName(exOrderInfo.getBuyUserName());
    		exOrderInfo.setTrueName(exOrderInfo.getBuyTrueName());
			exOrderInfo.setTransactionFee(exOrderInfo.getTransactionBuyFee());
			exOrderInfo.setTransactionFeeRate(exOrderInfo.getTransactionBuyFeeRate());
			exOrderInfo.setEntrustNum(exOrderInfo.getBuyEntrustNum());
		}else{
			exOrderInfo.setCustomerId(exOrderInfo.getSellCustomId());
			exOrderInfo.setUserCode(exOrderInfo.getSellUserCode());
			exOrderInfo.setUserName(exOrderInfo.getSellUserName());
    		exOrderInfo.setTrueName(exOrderInfo.getSellTrueName());
			exOrderInfo.setTransactionFee(exOrderInfo.getTransactionSellFee());
			exOrderInfo.setTransactionFeeRate(exOrderInfo.getTransactionSellFeeRate());
			exOrderInfo.setEntrustNum(exOrderInfo.getSellEntrustNum());
		}
		

		//订单结束详细
	
	}
	
	public void setExchangeDataCache(ExOrderInfo exOrderInfo, ExOrder exOrder) {
		RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
		ExEntrustService exEntrustService = (ExEntrustService) ContextUtil.getBean("exEntrustService");
		ExOrderInfoService exOrderInfoService = (ExOrderInfoService) ContextUtil.getBean("exOrderInfoService");
		
		String header=exEntrustService.getHeader(exOrderInfo);
		// 设置最新成交上一个成交价格
		  
		ExchangeDataCache.setStringData(header + ":" + ExchangeDataCache.LastExchangPrice,
				ExchangeDataCache.getStringData(header + ":" + ExchangeDataCache.CurrentExchangPrice));
	/*	// 设置当前最新成交价
		ExchangeDataCache.setStringData(header + ":" + ExchangeDataCache.CurrentExchangPrice, exOrderInfo
				.getTransactionPrice().setScale(5, BigDecimal.ROUND_HALF_UP).toString());
	*/	String transactionTime = DateUtil
				.dateToString(exOrderInfo.getTransactionTime(), StringConstant.DATE_FORMAT_YMD).toString();
		// 设置开盘价格，如果当前成交日期与之前保存的成交日期不是一样的说明这个是开盘价，从凌晨到开盘这一短时间的开盘价就用最后一个成交价
		ExchangeDataCache.setStringData(header + ":" + ExchangeDataCache.CurrentExchangDate, transactionTime);
		ExchangeDataCache.setStringData(header + ":" + ExchangeDataCache.CurrentExchangTime,
				DateUtil.dateToString(exOrderInfo.getTransactionTime(), StringConstant.DATE_FORMAT_FULL).toString());

		// 保存这条成交记录到re

		List<ExOrder> list = redisService.getList1(header + ":" + ExchangeDataCache.LastOrderRecords);
	/*	if (list.size() > 100) {
			list.remove(0);
		}
		list.add(exOrder);
		redisService.setList(header + ":" + ExchangeDataCache.LastOrderRecords, list);
		// 保存增量记录
*/		List<ExOrder> listadd = redisService.getList1(header + ":" + ExchangeDataCache.LastOrderRecordAdds);
		list.add(exOrder);
		redisService.setList(header + ":" + ExchangeDataCache.LastOrderRecordAdds, listadd);
		long start1 = System.currentTimeMillis();
		// 保存分期数据
		exOrderInfoService.savePeriodLastKLineList(exOrderInfo);
		long end1 = System.currentTimeMillis();
		LogFactory.info("成交之后设置缓存保存分期数据：" + (end1 - start1) + "毫秒");
	}
}
