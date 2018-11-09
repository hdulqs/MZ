 /**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年11月04日  18:23:33
 */
package com.mz.web.app.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.log.AppLogLogin;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录日志
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年5月23日 下午1:45:33
 */
@Controller
@RequestMapping("/log/apploglogin")
public class AppLogLoginController extends BaseController<AppLogLogin, Long>{
	
	@Resource(name="appLogLoginService")
	@Override
	public void setService(BaseService<AppLogLogin, Long> service) {
			// TODO Auto-generated method stub
			super.service = service;
	}

	/**
	 *AppLogController自己的initBinder
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinderDemoUser(ServletRequestDataBinder binder) {
		
	}
	
    @MethodName(name = "登录日志分页查询")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
    	QueryFilter filter = new QueryFilter(AppLogLogin.class, request);
		filter.setOrderby("loginTime desc");
    	PageResult findPage = super.findPage(filter);
    	
		return findPage;
//    	MongoUtil<AppLogLogin, Long> mongoUtil = new MongoUtil<AppLogLogin, Long>(AppLogLogin.class);
//    	MongoQueryFilter mongoQueryFilter = new MongoQueryFilter(request);
//    	mongoQueryFilter.setNosaas();
//    	mongoQueryFilter.setOrderby("id","desc");
//    	return mongoUtil.findPage(mongoQueryFilter);
    }


	
  
 
}
