/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.check.listener;
import com.mz.core.quartz.QuartzJob;
import com.mz.core.quartz.QuartzManager;
import com.mz.core.quartz.ScheduleJob;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.AppUtils;

import javax.servlet.ServletContextEvent;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import com.github.pagehelper.util.StringUtil;

/**
 * 
 * @author Administrator
 *
 */
@Component
public class StartupListener implements CommandLineRunner {

	@Override
	public void run(String... strings) {
		
		//加载每个应用的启动方法
		/*
		ScheduleJob checkExEntrustExEntrust = new ScheduleJob();
		checkExEntrustExEntrust.setSpringId("exEntrustService");
		checkExEntrustExEntrust.setMethodName("checkExEntrust");
		QuartzManager.addJob("checkExEntrustExEntrust", checkExEntrustExEntrust, QuartzJob.class, "0 12 04 * * ?");// 两秒
		*/
		String isStartCheck=PropertiesUtils.APP.getProperty("app.isStartCheck");
		if(!StringUtil.isEmpty(isStartCheck)&&isStartCheck.equals("true")){
			ScheduleJob checkculSureOldAccountAllCustomerErrorInfo= new ScheduleJob();
			checkculSureOldAccountAllCustomerErrorInfo.setSpringId("appReportSettlementNoTrService");
			checkculSureOldAccountAllCustomerErrorInfo.setMethodName("timeingCulSureOldAccountAllCustomerErrorInfo");
			QuartzManager.addJob("checkculSureOldAccountAllCustomerErrorInfo", checkculSureOldAccountAllCustomerErrorInfo, QuartzJob.class, "0 21 01 * * ?");// 晚上2点12核算
			
			ScheduleJob timeingCulRedisScheduleJob= new ScheduleJob();
			timeingCulRedisScheduleJob.setSpringId("appReportSettlementNoTrService");
			timeingCulRedisScheduleJob.setMethodName("timeingCulRedis");
			QuartzManager.addJob("timeingCulRedisScheduleJob", timeingCulRedisScheduleJob, QuartzJob.class, "0 31 03 * * ?");// 晚上2点12核算
			}
		
		
		String isStartRemoveDataRobot=PropertiesUtils.APP.getProperty("app.isStartRemoveDataRobot");
		if(!StringUtil.isEmpty(isStartRemoveDataRobot)&&isStartCheck.equals("true")){
			ScheduleJob removeEntrustRobt= new ScheduleJob();
			removeEntrustRobt.setSpringId("exOrderInfoService");
			removeEntrustRobt.setMethodName("removeEntrustRobt");
			QuartzManager.addJob("removeEntrustRobt", removeEntrustRobt, QuartzJob.class, "0 01 01 * * ?");// 晚上删掉机器人产生的数据
			}
	}

}
