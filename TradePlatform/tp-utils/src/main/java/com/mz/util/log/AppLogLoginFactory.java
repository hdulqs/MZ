/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月23日 上午11:58:37
 */
package com.mz.util.log;

import com.mz.util.date.DateUtil;
import com.mz.util.httpRequest.IpUtil;
import com.mz.core.mvc.model.log.AppLogLogin;
import com.mz.customer.user.model.AppCustomer;
import com.mz.oauth.user.model.AppUser;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年5月23日 上午11:58:37 
 */
public class AppLogLoginFactory {
	
	/**
	 * 生成appLogLogin
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param obj       登录的主体对象
	 * @param:    @param isLogin   是否登录成功    true成功，false失败
	 * @param:    @return
	 * @return: AppLogLogin 
	 * @Date :          2016年5月23日 下午12:18:15   
	 * @throws:
	 */
	public static AppLogLogin getAppLogLogin(Object obj,boolean isLogin,HttpServletRequest request){
		
		AppLogLogin appLogLogin = new AppLogLogin();
		if(obj instanceof AppUser){
			AppUser appUser = (AppUser) obj;
			appLogLogin.setType(appUser.getClass().getSimpleName());
			appLogLogin.setUserId(appUser.getId());
			appLogLogin.setUserName(appUser.getUsername());
		}else if(obj instanceof AppCustomer){
			AppCustomer appCustomer = (AppCustomer) obj;
			appLogLogin.setType(appCustomer.getClass().getSimpleName());
			appLogLogin.setUserId(appCustomer.getId());
			appLogLogin.setUserName(appCustomer.getUserName());
		}
		
		appLogLogin.setLoginTime(DateUtil.dateToString(new Date()));
		appLogLogin.setIp(IpUtil.getIp(request));
		if(isLogin){
			appLogLogin.setStatus("1");
		}else{
			appLogLogin.setStatus("0");
		}
		
		return appLogLogin;
		
	}
	
	
}
