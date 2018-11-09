package com.mz.oauth.listener;

import com.mz.shiro.PasswordHelper;
import com.mz.shiro.service.AppUserService;
import com.mz.core.constant.StartInitConstant;
import com.mz.core.listener.StartLoad;
import com.mz.core.listener.StartupService;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.oauth.user.model.AppUser;

public class StartupManage implements StartupService {
	
	public final static String AppName = "hurong_oauth";
	public final static String AppKey = "oauth";
	
	@Override
	public void start() {
		//读menu.xml加载菜单和权限
		StartLoad.loadBase(AppName, AppKey,PropertiesUtils.APP.getProperty("app.saasId"));
		//加载noLogin注解方法
		StartLoad.loadNoLoginAnnotations(StartInitConstant.noLoginSet,AppKey);
		
		//初始化超级管理员账户
		AppUserService appUserService = (AppUserService) ContextUtil.getBean("appUserService");
		QueryFilter filter = new QueryFilter(AppUser.class);
		filter.addFilter("username=", PropertiesUtils.APP.getProperty("app.admin"));
		AppUser appUser = appUserService.get(filter);
		if(appUser==null){
			AppUser _appUser = new AppUser();
			_appUser.setUsername(PropertiesUtils.APP.getProperty("admin"));
			_appUser.setPassword("admin");
			PasswordHelper passwordHelper = new PasswordHelper();
			passwordHelper.encryptPassword(_appUser);
			appUserService.save(_appUser);
		}
		
	}
	
	
	
	
}
