/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月2日 下午4:33:16
 */
package com.mz.shiro.service.impl;

import com.mz.shiro.PasswordHelper;
import com.mz.shiro.dao.AppOrganizationDao;
import com.mz.shiro.service.AppOrganizationService;
import com.mz.shiro.service.AppRoleResourceService;
import com.mz.shiro.service.AppRoleService;
import com.mz.shiro.service.AppUserOrganizationService;
import com.mz.shiro.service.AppUserRoleService;
import com.mz.shiro.service.AppUserService;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.oauth.user.model.AppOrganization;
import com.mz.oauth.user.model.AppRole;
import com.mz.oauth.user.model.AppRoleResource;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.user.model.AppUserOrganization;
import com.mz.oauth.user.model.AppUserRole;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月2日 下午4:33:16
 */
@Service("appOrganizationService")
public class AppOrganizationServiceImpl extends BaseServiceImpl<AppOrganization, Long>  implements
		AppOrganizationService {
	
	@Resource(name = "appOrganizationDao")
	@Override
	public void setDao(BaseDao<AppOrganization, Long> dao) {
		super.dao = dao;
	}
	
	@Resource
	private AppUserRoleService appUserRoleService;

	@Resource
	private AppRoleResourceService appRoleResourceService;
	
	@Resource
	private AppRoleService appRoleService;
	
	@Resource
	private AppUserService appUserService;
	
	@Resource
	private AppUserOrganizationService appUserOrganizationService;
	
	@Override
	public JsonResult remove(String[] ids) {
		JsonResult jsonResult = new JsonResult();
		//判空
		if(StringUtils.isEmpty(ids)){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("id不能为空");
			return jsonResult;
		}else{
			String[] strArr = ids;
			//保存关联信息
			for(String str : strArr){
			//	AppOrganization appOrganization = dao.get(Long.valueOf(str));
				
				AppOrganization appOrganization = dao.selectByPrimaryKey(Long.valueOf(str));
				
				if("root".equals(appOrganization.getType())||"company".equals(appOrganization.getType())){
					jsonResult.setSuccess(false);
					jsonResult.setMsg("不能删除根节点/子公司");
					return jsonResult;
				}
				
				QueryFilter filter = new QueryFilter(AppUserOrganization.class);
				filter.addFilter("organizationId=", appOrganization.getId());
				List<AppUserOrganization> findByAppOrganization =  appUserOrganizationService.find(filter);
				
				if(findByAppOrganization!=null&&findByAppOrganization.size()>0){
					jsonResult.setSuccess(false);
					jsonResult.setMsg("该组织下存在用户,请先删除用户");
					return jsonResult;
				}
				
				if(hasSonOrganization(appOrganization)){
					jsonResult.setSuccess(false);
					jsonResult.setMsg("该组织下存在子组织，请先删除子组织");
					return jsonResult;
				}
				
				dao.delete(appOrganization);
			}
			jsonResult.setSuccess(true);
			return jsonResult;
		}
	}

	@Override
	public JsonResult removeSubCompanty(String[] ids) {
		return null;
	}

	@Override
	public JsonResult addSubCompany(HttpServletRequest request, AppUser appuser,AppOrganization appOrganization) {
		JsonResult jsonResult = new JsonResult();
		
		try {
			String permissions = request.getParameter("permissions");
			if(StringUtils.isEmpty(permissions)){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("添加非法，权限列表为空");
				return jsonResult;
			}
			
			String[] permissionsArr = permissions.split(",");
			
			//----------------------为分公司增加分公司管理员账号的角色--------------------------------------------
			AppRole appRole = new AppRole();
			appRole.setName("【分公司管理角色】_"+appOrganization.getName());
			appRole.setRemark("【分公司管理角色】_"+appOrganization.getName());
			appRole.setType("subcompany");   //标记些角色为分公司角色
			appRoleService.save(appRole);
			
			
			//--------------------------增加角色权限关联表--------------------------------------------------
			for(String str : permissionsArr){
				AppRoleResource appRoleResource = new AppRoleResource();
				appRoleResource.setRoleId(appRole.getId());
				appRoleResource.setResourceId(Long.valueOf(str));
				appRoleResourceService.save(appRoleResource);
			}
			
			//--------------------------分公司增加管理员账号------------------------------------------------------
			//设置appuser账号前缀
			appuser.setAppuserprefix(ContextUtil.getCurrentUser().getAppuserprefix());
			//appuser密码加密
			PasswordHelper passwordHelper = new PasswordHelper();
			passwordHelper.encryptPassword(appuser);
			appUserService.save(appuser);
			
			
			//--------------------------增加用户角色关联表------------------------------------------------------
			AppUserRole appUserRole = new AppUserRole();
			appUserRole.setUserId(appuser.getId());
			appUserRole.setRoleId(appRole.getId());
			appUserRoleService.save(appUserRole);
			
			//-------------------------增加分公司---------------------------------------------------
			super.save(appOrganization);
			
			//-------------------------增加用户公司关联表---------------------------------------------------
			AppUserOrganization appUserOrganization  = new AppUserOrganization();
			appUserOrganization.setUserId(appuser.getId());
			appUserOrganization.setOrganizationId(appOrganization.getId());
			appUserOrganization.setType("subcompany");
			appUserOrganizationService.save(appUserOrganization);
			
			//清空session
			ContextUtil.getSession().removeAttribute("appUser");
			ContextUtil.getSession().removeAttribute("appOrganization");
			jsonResult.setSuccess(true);
			
		} catch (Exception e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("增加异常");
		}
		
		return jsonResult;
		
	}

	@Override
	public List<AppOrganization> findByCompanyId(String companyId, String type) {
		return findRecursive(companyId,type,new ArrayList<AppOrganization>());
	}
		
	public  List<AppOrganization> findRecursive(String companyId,String type,List<AppOrganization> allList){
		
		QueryFilter filter = new QueryFilter(AppOrganization.class);
		
	//	QueryFilter filter = new QueryFilter();
		if(!StringUtils.isEmpty(companyId)){
	//		filter.addFilter("Q_t.pid_=_Long", companyId);
			
			filter.addFilter("pid=", companyId);
			
		}
		if(!StringUtils.isEmpty(type)){
	//		filter.addFilter("Q_t.type_=_String", type);
			filter.addFilter("type=", type);
		}
		List<AppOrganization> dataList = dao.selectByExample(filter.getExample());
		
		
		//allList.addAll(dataList);
		if(dataList!=null&&dataList.size()>0){
			for(AppOrganization appOrganization : dataList){
				allList.add(appOrganization);
				findRecursive(appOrganization.getId().toString(), type, allList);
			}
		}
		return allList;
	}

	@Override
	public List<AppOrganization> findSons(AppOrganization appOrganization) {
		return findRecursive(appOrganization.getId().toString(),null,new ArrayList<AppOrganization>());
	}

	@Override
	public String findSonsToIds(AppOrganization appOrganization) {
		List<AppOrganization> findSons = findSons(appOrganization);
		String ids ="";
		if(findSons!=null){
			for(int i = 0 ; i < findSons.size() ; i++ ){
				ids += findSons.get(i).getId().toString();
				if(i!=findSons.size()-1){
					ids += ",";
				}
			}
			ids = ids + "," + appOrganization.getId();
		}
		if(ids.length()==0){
			ids = appOrganization.getId()+"";
		}
		return ids;
	}

	@Override
	public boolean hasSonOrganization(AppOrganization appOrganization) {
		List<AppOrganization> list = ((AppOrganizationDao)dao).findSonOrganization(appOrganization.getId());
		if(list!=null&&list.size()>0){
			return true;
		}
		return false;
	
	}
	
}
