package com.mz.exchange.listener;

import com.mz.core.constant.StartInitConstant;
import com.mz.core.listener.StartLoad;
import com.mz.core.listener.StartupService;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.exchange.entrust.StartupEntrust;
import com.mz.exchange.lend.StartupLend;
import com.mz.trade.kline.KlineEngine;

public class StartupManage implements StartupService {

	public final static String AppName = "hurong_exchange";
	public final static String AppKey = "exchange";

	@Override
	public void start() {
		String saasId = PropertiesUtils.APP.getProperty("app.saasId");
		try {
			// 读menu.xml加载菜单和权限
			StartLoad.loadBase(AppName, AppKey, saasId);
			// 加载noLogin注解方法
			StartLoad.loadNoLoginAnnotations(StartInitConstant.noLoginSet, AppKey);
			
			LogFactory.info("exchange启动定时器中................................................................");
			StartupEntrust.Entrustinit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//启动平仓功能
		StartupLend.LendJob();
		StartupEntrust.EntrustJob();
		//加载Kline定时器
		LogFactory.info("exchange启动K线定时器中................................................................");
		KlineEngine.klineJob();
		
	}

}
