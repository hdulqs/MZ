/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午11:43:39
 */
package com.mz.oauth.user.controller;

import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRole;
import com.mz.shiro.service.AppRoleService;
import com.mz.shiro.service.AppUserService;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>     AppResource 
 * 					存在用意，因为hibernate多态查询  子类找不了父类，而父类可以查子类，所以这里直接用父类baseApp来查询
 * @author:         Liu Shilei 
 * @Date :          2015年10月12日16:19:21
 */
@Controller
@RequestMapping("/user/appresource")
public class AppResourceController extends BaseController<AppResource, Long> {
	
	@Resource(name="appResourceService")
	@Override
	public void setService(BaseService<AppResource, Long> service) {
		super.service = service;
		
	}
	
	@Resource
	private AppUserService appUserService;
	
	@Resource
	private AppRoleService appRoleService;
	
	
	@MethodName(name = "查找resource列表,返回json,用于rolesAdd.jsp页面")
	@RequestMapping("/findToJsonOnRolesAdd")
	@ResponseBody
	public List<AppResource>	findToJsonOnRolesAdd(HttpServletRequest request){
		if(PropertiesUtils.APP.getProperty("app.admin").equals(ContextUtil.getCurrentUserName())){
		//	QueryFilter filter = new QueryFilter();
			
			QueryFilter filter = new QueryFilter(AppResource.class);
			
			return super.service.find(filter);
		}else{
			Set<AppResource> set = new HashSet<AppResource>();
			Set<AppRole> appRoleSet = appUserService.getAppRoleSet(ContextUtil.getCurrentUser());
			Iterator<AppRole> iterator = appRoleSet.iterator();
			while (iterator.hasNext()) {
				set.addAll(appRoleService.getAppResourceSet(iterator.next()));
			}
			return new ArrayList<AppResource>(set);
		}
		
	}
	
	

}
