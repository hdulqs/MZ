package com.mz.trade.model;

import com.mz.trade.entrust.model.ExOrderInfo;
import java.util.List;

public class PeriodLastKLineListRunable implements Runnable {
	
	
	private List<ExOrderInfo> exorderlist;
	public PeriodLastKLineListRunable(List<ExOrderInfo> exorderlist){
		this.exorderlist=exorderlist;
	}
	@Override
	public void run() {
		RedisLastKLine.savePeriodLastKLineList(this.exorderlist);
	}
	
}
