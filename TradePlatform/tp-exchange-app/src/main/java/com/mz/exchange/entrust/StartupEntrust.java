/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年8月10日 下午8:06:46
 */
package com.mz.exchange.entrust;

import com.alibaba.fastjson.JSON;
import com.mz.exchange.product.service.ExCointoCoinService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.core.quartz.QuartzJob;
import com.mz.core.quartz.QuartzManager;
import com.mz.core.quartz.ScheduleJob;
import com.mz.ico.coin.service.AppIcoCoinService;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.exEntrustOneDay.model.ExentrustOneday;
import com.mz.trade.exEntrustOneDay.service.ExentrustOnedayService;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Gao Mimi
 * @Date : 2016年8月10日 下午8:06:46
 */
public class StartupEntrust {
	public static void Entrustinit() {
		try {
			// 初始化,产品K值
			ExProductService exProductService = (ExProductService) ContextUtil.getBean("exProductService");
			AppIcoCoinService appIcoCoinService = (AppIcoCoinService) ContextUtil.getBean("appIcoCoinService");
			// 初始化redis中cn:productList
			exProductService.initRedisCode();
			if ("true".equals(PropertiesUtils.APP.getProperty("app.hasico"))) {
				// 初始化redis中cn:coinList
				appIcoCoinService.initIcoRedisCode();
			}
			// 初始化交易数据==========================================================================================
			ExCointoCoinService exCointoCoinService = (ExCointoCoinService) ContextUtil.getBean("exCointoCoinService");
			exCointoCoinService.initRedisCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void initExentrustRedis() {

	}

	public static void initExentrustOnedayAndRadis() {
		ExEntrustService exEntrustService = (ExEntrustService) ContextUtil.getBean("exEntrustService");
		ExentrustOnedayService exentrustOnedayService = (ExentrustOnedayService) ContextUtil.getBean("exentrustOnedayService");
		// 先清空所有的挂单数据
		QueryFilter delfilter = new QueryFilter(ExentrustOneday.class);
		exentrustOnedayService.delete(delfilter);
		// 查询出所有的未成交的委托单status<2
		QueryFilter filter = new QueryFilter(ExEntrust.class);
		filter.addFilter("status<", 2);

		List<ExEntrust> list = exEntrustService.find(filter);
		ExentrustOneday exentrustOneday = null;
		for (ExEntrust l : list) {
			String exentrust = JSON.toJSONString(l);
			exentrustOneday = JSON.parseObject(exentrust, ExentrustOneday.class);
			exentrustOnedayService.save(exentrustOneday);
			// exEntrustService.saveExtrustToRedis(l);
		}


	}

	public static void EntrustJob() {
	
		ScheduleJob jobRunTimetimingCulAtferMoney = new ScheduleJob();
		jobRunTimetimingCulAtferMoney.setSpringId("exOrderInfoServiceNoTr");
		jobRunTimetimingCulAtferMoney.setMethodName("timingCulAtferMoney");
		QuartzManager.addJob("jobRunTimetimingCulAtferMoney", jobRunTimetimingCulAtferMoney, QuartzJob.class, "0 0 1 * * ?");// 10分钟
//		QuartzManager.addJob("jobRunTimetimingCulAtferMoney", jobRunTimetimingCulAtferMoney, QuartzJob.class, "0 0 0 * * ?");// 两秒0 0 24 * * ?0 0/5 * * * ?"0/60 * * * * ?
	}
}
