package com.mz.customer.listener;

import com.mz.core.constant.StartInitConstant;
import com.mz.core.listener.StartLoad;
import com.mz.core.listener.StartupService;
import com.mz.core.quartz.QuartzJob;
import com.mz.core.quartz.QuartzManager;
import com.mz.core.quartz.ScheduleJob;
import com.mz.util.properties.PropertiesUtils;

public class StartupManage implements StartupService {
	
	public final static String AppName = "hurong_customer";
	public final static String AppKey = "customer";
	
	
	@Override
	public void start() {
		//读menu.xml加载菜单和权限
		StartLoad.loadBase(AppName, AppKey,PropertiesUtils.APP.getProperty("app.saasId"));
		//加载noLogin注解方法
		StartLoad.loadNoLoginAnnotations(StartInitConstant.noLoginSet,AppKey);
		
		ScheduleJob job1 = new ScheduleJob();
		job1.setSpringId("c2cTransactionService");
		job1.setMethodName("timeout");
		QuartzManager.addJob("c2cTransactionServiceTimeOut", job1, QuartzJob.class, "0 0/1 * * * ?");

        //otc超时 add by zongwei 20180601
		ScheduleJob job2 = new ScheduleJob();
		job2.setSpringId("otcOrderTransactionService");
//		job2.setBeanClass("OtcOrderTransactionServiceImpl");
		job2.setMethodName("timeout");
		QuartzManager.addJob("otcOrderTransactionServiceTimeOut", job2, QuartzJob.class, "0 0/1 * * * ?");
	}
	
}
