package com.mz.trade.model;

import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderService;
import com.mz.trade.websoketContext.PushData;
import com.mz.util.sys.ContextUtil;

public class ExEntrutKlineRunable implements Runnable {
	
	
	private String coinCode;
	private String fixPriceCoinCode;
	private String header;
	public ExEntrutKlineRunable(String coinCode,String fixPriceCoinCode,String header){
		this.coinCode=coinCode;
		this.fixPriceCoinCode=fixPriceCoinCode;
		this.header=header;
	}
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		ExEntrustService entrustService=(ExEntrustService) ContextUtil.getBean("exEntrustService");
		ExOrderService exOrderService=(ExOrderService) ContextUtil.getBean("exOrderService");
		
		// 委托-----行情板块
		PushData.pushEntrusMarket(entrustService.getExEntrustChangeMarket(coinCode,fixPriceCoinCode, 18),header);
		long end1 = System.currentTimeMillis();
	//	LogFactory.info("定时委单深度：" + (end1 - start) + "毫秒");
		// 成交订单
		PushData.pushNewListRecordMarketDesc(exOrderService.getNewListMarket(header, "desc"), header);
		long end2 = System.currentTimeMillis();
	//	LogFactory.info("定时成交：" + (end2 - end1) + "毫秒");
	
	}
	
}
