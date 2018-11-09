/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月18日 上午10:32:03
 */
package com.mz.oauth.company.controller;

import com.mz.oauth.user.model.AppOrganization;
import com.mz.shiro.service.AppOrganizationService;
import com.mz.shiro.service.AppResourceService;
import com.mz.shiro.service.AppRoleService;
import com.mz.shiro.service.AppUserService;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu   Shilei   
 * @Date :          2015年9月18日 上午10:32:03 
 */
@Controller
@RequestMapping("/company/apporganization")
public class AppOrganizationController  extends BaseController<AppOrganization, Long>{
	
	@Resource(name = "appOrganizationService")
	@Override
	public void setService(BaseService<AppOrganization, Long> service) {
		super.service = service;
	}
	
	@Resource
	private AppRoleService appRoleService;
	@Resource
	private AppResourceService appResourceService;
	@Resource
	private AppUserService appUserService;
	
	//---------------------------------------------部--------------------------------------------------------
	//---------------------------------------------门--------------------------------------------------------
	@MethodName(name = "加载部门树")
	@RequestMapping("/loadTree")
	@ResponseBody
	public List<AppOrganization>  loadTree(){
		QueryFilter filter = new QueryFilter(AppOrganization.class);
		filter.setOrderby("orderNo asc");
		return service.find(filter);
	}
	
	@MethodName(name = "加载自己的公司")
	@RequestMapping("/findMyCompany")
	@ResponseBody
	public List<AppOrganization>  findMyCompany(){
		if(PropertiesUtils.APP.getProperty("app.admin").equals(ContextUtil.getCurrentUserName())){//如果是admin账户查所有的公司包括根集团
			QueryFilter filter = new QueryFilter(AppOrganization.class);
			filter.addFilter("type_in ", "root ,company");
			
			
			return service.find(filter);
		}else{//如果不是admin只有查自己所在的公司
			Set<AppOrganization> companySet = appUserService.getCompanySet(ContextUtil.getCurrentUser());
			return new ArrayList<AppOrganization>(companySet);
		}
	}
	
	@MethodName(name = "加载自己的部门")
	@RequestMapping("/findMyDepartment")
	@ResponseBody
	public List<AppOrganization>  findMyDepartment(){
		if(PropertiesUtils.APP.getProperty("app.admin").equals(ContextUtil.getCurrentUserName())){//如果是admin账户查所有的公司包括根集团
			QueryFilter filter = new QueryFilter(AppOrganization.class);
			filter.addFilter("type_in", "root,company");
			return service.find(filter);
		}else{//如果不是admin只有查自己所在的公司
			Set<AppOrganization> companySet = appUserService.getCompanySet(ContextUtil.getCurrentUser());
			return new ArrayList<AppOrganization>(companySet);
		}
	}
	
	
	
	@MethodName(name="增加部门")
	@RequestMapping("/add")
	@MyRequiresPermissions({"/add","/addshop"})
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppOrganization appOrganization){
		return save(appOrganization);
	}
	
	
	@MethodName(name="查看部门")
	@RequestMapping(value="/see/{id}",method=RequestMethod.GET)
	@ResponseBody
	public AppOrganization see(@PathVariable Long id){
		return service.get(id);
	}
	
	
	@MethodName(name="修改部门")
	@RequestMapping("/modify")
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppOrganization appOrganization){
		AppOrganization _appOrganization = service.get(appOrganization.getId());
		BeanUtil.copyNotNullProperties(appOrganization, _appOrganization);
		return super.update(_appOrganization);
	}
	
	
	
	@MethodName(name="删除部门")
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	@ResponseBody
	public JsonResult remove(@PathVariable String[] ids){
		return ((AppOrganizationService) service).remove(ids);
	}
	
	
	@MethodName(name="递归查找公司下的所有部门或门店")
	@RequestMapping("/findByCompanyId")
	@ResponseBody
	public List<AppOrganization> findByCompanyId(HttpServletRequest request){
		String companyId = request.getParameter("companySet");
		String type = request.getParameter("type");
		return ((AppOrganizationService) service).findByCompanyId(companyId,type);
	}
	
	@MethodName(name="加载root")
	@RequestMapping("/loadRoot")
	@ResponseBody
	public List<AppOrganization> loadRoot(){
		QueryFilter  filter = new QueryFilter(AppOrganization.class);
		filter.addFilter("type=", "root");
		return super.find(filter);
	}
	

}
