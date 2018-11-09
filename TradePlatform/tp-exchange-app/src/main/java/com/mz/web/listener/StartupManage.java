package com.mz.web.listener;

import com.mz.web.app.service.AppHolidayConfigService;
import com.mz.web.cache.CacheManageCallBack;
import com.mz.web.cache.CacheManageService;
import org.apache.log4j.Logger;

import com.mz.core.constant.StartInitConstant;
import com.mz.core.listener.StartLoad;
import com.mz.core.listener.StartupService;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.web.quartz.QuartzEngine;

public class StartupManage implements StartupService {
	
	public final static String AppName = "hurong_web";
	public final static String AppKey = "web";

	private final static Logger log = Logger.getLogger(StartupManage.class);
	@Override
	public void start() {
		//读menu.xml加载菜单和权限
		StartLoad.loadBase(AppName, AppKey,PropertiesUtils.APP.getProperty("app.saasId"));
		//加载noLogin注解方法
		StartLoad.loadNoLoginAnnotations(StartInitConstant.noLoginSet,AppKey);
	
		CacheManageCallBack callback=(CacheManageCallBack)ContextUtil.getBean("cacheManageCallBack");
		//初始化系统配置信息缓存
		CacheManageService appConfigService=(CacheManageService)ContextUtil.getBean("appConfigService");
		appConfigService.initCache(callback);
		//初始化杠杆配置信息缓存
		CacheManageService appLendConfigService=(CacheManageService)ContextUtil.getBean("appLendConfigService");
		appLendConfigService.initCache(callback);
		//初始化 网站配置信息缓存
		CacheManageService appCaheAreaService=(CacheManageService)ContextUtil.getBean("appCaheAreaServiceImpl");
		//初始化地区数字字典缓
		appCaheAreaService.initCache(callback);
		//初始化银行网站配置信息缓存
		CacheManageService appDicMultilevelService=(CacheManageService) ContextUtil.getBean("appDicMultilevelService");
		appDicMultilevelService.initCache(callback);
		//初始化banner缓存
		CacheManageService appBannerService=(CacheManageService) ContextUtil.getBean("appBannerService");
		appBannerService.initCache(callback);
		//加载定时器
		QuartzEngine.startUp();
		
		log.info("初始华节假日配置信息..................");
		AppHolidayConfigService appHolidayConfigService = (AppHolidayConfigService) ContextUtil.getBean("appHolidayConfigService");
		appHolidayConfigService.initCache();
		
		
	}
	
	
	
	
}
