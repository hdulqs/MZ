package com.mz.calculate.listener;

import com.mz.calculate.mvc.StartupSettlement;
import com.mz.core.constant.StartInitConstant;
import com.mz.core.listener.StartLoad;
import com.mz.core.listener.StartupService;
import com.mz.util.properties.PropertiesUtils;

public class StartupManage implements StartupService {
	
	public final static String AppName = "hurong_calculate";
	public final static String AppKey = "calculate";
	
	@Override
	public void start() {
		//读menu.xml加载菜单和权限
		StartLoad.loadBase(AppName, AppKey,PropertiesUtils.APP.getProperty("app.saasId"));
		//加载noLogin注解方法
		StartLoad.loadNoLoginAnnotations(StartInitConstant.noLoginSet,AppKey);
		
		StartupSettlement.SettlementJob();
	}
	
	
	
	
}
