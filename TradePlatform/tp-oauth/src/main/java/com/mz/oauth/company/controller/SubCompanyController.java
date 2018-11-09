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
import com.mz.shiro.service.AppUserOrganizationService;
import com.mz.shiro.service.AppUserRoleService;
import com.mz.shiro.service.AppUserService;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRole;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.user.model.AppUserOrganization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/company/subcompany")
public class SubCompanyController  extends BaseController<AppOrganization, Long>{
	
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
	@Resource
	private AppUserOrganizationService appUserOrganizationService;
	@Resource
	private AppUserRoleService appUserRoleService;
	
	
	
	
	@MethodName(name="增加分公司基本信息")
	@RequestMapping("/addbase")
	@MyRequiresPermissions("/addbase")
	@ResponseBody
	public JsonResult addbase(AppOrganization appOrganization){
		ContextUtil.getSession().setAttribute("appOrganization", appOrganization);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	@MethodName(name="增加分公司管理员")
	@RequestMapping("/addadmin")
	@MyRequiresPermissions("/addbase")
	@ResponseBody
	public JsonResult addadmin(HttpServletRequest request, AppUser appuser){
		JsonResult jsonResult = new JsonResult();
		AppOrganization appOrganization  = (AppOrganization) ContextUtil.getSession().getAttribute("appOrganization");
		if(appOrganization==null){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("请补填基本信息");
		}else{
			
			QueryFilter filter = new QueryFilter(AppUser.class);
			filter.addFilter("username=", appuser.getUsername());
			
			List<AppUser> find = appUserService.find(filter);
			if(find!=null&&find.size()>0){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("账号重复请修改");
				return jsonResult;
			}
			String repassword = request.getParameter("repassword");
			if(repassword==null||appuser.getPassword()==null||!repassword.equals(appuser.getPassword())){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("密码不一致请修改");
				return jsonResult;
			}
			
			
			ContextUtil.getSession().setAttribute("appUser", appuser);
			jsonResult.setSuccess(true);
			
		}
		return jsonResult;
	}
	
	@MethodName(name="增加分公司_管理员权限")
	@RequestMapping("/addpermissions")
	@MyRequiresPermissions("/addbase")
	@ResponseBody
	public JsonResult addpermissions(HttpServletRequest request){
		JsonResult jsonResult = new JsonResult(); 
		AppUser appuser = (AppUser)ContextUtil.getSession().getAttribute("appUser");
		AppOrganization appOrganization = (AppOrganization)ContextUtil.getSession().getAttribute("appOrganization");
		
		if(appOrganization!=null&&appuser!=null){
			return ((AppOrganizationService)service).addSubCompany(request,appuser,appOrganization);
		}else{
			jsonResult.setSuccess(false);
			jsonResult.setMsg("页面超时");
		}
		
		return jsonResult;
	}
	
	
	
	@MethodName(name = "分公司列表")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppResource.class, request);
		filter.addFilter("type=", "company");
		return super.findPage(filter);
	}
	
	@MethodName(name = "查看")
	@RequestMapping(value="/see/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  see(@PathVariable Long id){
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("appOrganization", service.get(id));
		
		//查管理员
//		QueryFilter queryFilter = new QueryFilter();
//		queryFilter.addFilter("Q_t.type_=_String", "subcompany");
//		queryFilter.addFilter("Q_t.organizationId_=_Long", id+"");
		
		QueryFilter queryFilter = new QueryFilter(AppUserOrganization.class);
		queryFilter.addFilter("type=", "subcompany");
		queryFilter.addFilter("organizationId=", id);
		
		List<AppUserOrganization> find = appUserOrganizationService.find(queryFilter);
		if(find!=null){
			map.put("appUser", appUserService.get(find.get(0).getUserId())); 
		}
		
		//查管理员权限
//		QueryFilter queryFilter2 = new QueryFilter();
//		queryFilter2.addFilter("Q_t.type_=_String", "subcompany");
//		queryFilter2.addFilter("Q_t.organizationId_=_Long", id+"");
		
		QueryFilter queryFilter2 = new QueryFilter(AppUserOrganization.class);
		queryFilter2.addFilter("type=", "subcompany");
		queryFilter2.addFilter("organizationId=", id);
		
		List<AppUserOrganization> findAppUserOrganization = appUserOrganizationService.find(queryFilter2);
		if(find!=null){
			//查出管理员
			AppUser appUser = appUserService.get(findAppUserOrganization.get(0).getUserId());
			
			//查出管理员角色
			Set<AppRole> appRoleSet = appUserService.getAppRoleSet(appUser);
			//得到唯一的一个角色
			List<AppRole> listRole = new ArrayList<AppRole>(appRoleSet);
			//返回角色对应的权限列表
			 map.put("appResourceSet", appRoleService.getAppResourceSet(listRole.get(0)));
		}
		
		return map;
	}
	
	
	@MethodName(name="删除")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String[] ids){
		return ((AppOrganizationService) service).removeSubCompanty(ids);
	}
	
	
	
	//-------------------------------------------------集团配置-------------------------------------------------
	@MethodName(name = "加载集团根节点")
	@RequestMapping("/loadRoot")
	@ResponseBody
	public AppOrganization loadRoot(){
		QueryFilter filter = new QueryFilter(AppOrganization.class);
		filter.addFilter("type=", "root");
		return service.get(filter);
	}
	
	
	@MethodName(name = "配置集团根节点")
	@RequestMapping("/saveRoot")
	@ResponseBody
	public JsonResult saveRoot(AppOrganization appOrganization){
		JsonResult jsonResult = new JsonResult();
		appOrganization.setType("root");
		if(appOrganization.getId()!=null){
			service.update(appOrganization);
			jsonResult.setSuccess(true);
		}else{
			QueryFilter filter = new QueryFilter(AppOrganization.class);
			filter.addFilter("type=", "root");
			AppOrganization ao = service.get(filter);
			if(ao!=null){
				com.mz.util.BeanUtil.copyNotNullProperties(appOrganization, ao);
				service.update(ao);
			}else{
				service.save(appOrganization);
			}
			jsonResult.setSuccess(true);
		}
		return jsonResult;
	}

	
	@MethodName(name = "加载公司下拉框")
	@RequestMapping("/findCompanyList")
	@ResponseBody
	public List<AppOrganization> findCompanyList(){
//		QueryFilter filter = new QueryFilter();
//		filter.addFilter("Q_t.type_in_String", "company,root");
		
		QueryFilter filter = new QueryFilter(AppOrganization.class);
		List<String> list = new ArrayList<String>();
		list.add("company");
		list.add("root");
		filter.addFilter("type_in",list );
		
		return service.find(filter);
	}
	

}
