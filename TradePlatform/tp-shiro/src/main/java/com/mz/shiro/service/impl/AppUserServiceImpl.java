/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月18日 上午10:29:49
 */
package com.mz.shiro.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mz.shiro.PasswordHelper;
import com.mz.shiro.dao.AppOrganizationDao;
import com.mz.shiro.dao.AppUserDao;
import com.mz.shiro.dao.AppUserOrganizationDao;
import com.mz.shiro.dao.AppUserRoleDao;
import com.mz.shiro.service.AppOrganizationService;
import com.mz.shiro.service.AppRoleService;
import com.mz.shiro.service.AppUserOrganizationService;
import com.mz.shiro.service.AppUserRoleService;
import com.mz.shiro.service.AppUserService;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.oauth.user.model.AppOrganization;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRole;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.user.model.AppUserOrganization;
import com.mz.oauth.user.model.AppUserRole;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月18日 上午10:29:49 
 */
@Service("appUserService")
public class AppUserServiceImpl extends BaseServiceImpl<AppUser, Long>  implements AppUserService {
	
	@Resource(name = "appUserDao")
	@Override
	public void setDao(BaseDao<AppUser, Long> dao) {
		super.dao = dao;
	}
	
	@Resource
	private AppUserRoleDao appUserRoleDao;
	@Resource
	private AppUserRoleService appUserRoleService;
	@Resource
	private AppUserOrganizationDao appUserOrganizationDao;
	@Resource
	private AppUserOrganizationService appUserOrganizationService;
	@Resource
	private AppOrganizationDao appOrganizationDao;
	@Resource
	private AppRoleService appRoleService;
	@Resource
	private AppOrganizationService appOrganizationService;
	
	@Override
	public JsonResult add(HttpServletRequest request, AppUser appUser) {
		
		JsonResult jsonResult  = new JsonResult();
		
		QueryFilter filter = new QueryFilter(AppUser.class);
		filter.addFilter("username=", appUser.getUsername());
		
		List<AppUser> find = find(filter);
		
		if(find!=null&&find.size()>0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("账号不能重复");
			return jsonResult;
		}
		
		if(appUser.getCompanySet()==null||appUser.getCompanySet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("所属公司不能为空");
			return jsonResult;
		}
		if(appUser.getDepartmentSet()==null||appUser.getDepartmentSet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("所属部门不能为空");
			return jsonResult;
		}
		if(appUser.getAppRoleSet()==null||appUser.getAppRoleSet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("角色不能为空");
			return jsonResult;
		}
		
		//设置appuser账号前缀
		appUser.setAppuserprefix(ContextUtil.getCurrentUser().getAppuserprefix());
		PasswordHelper passwordHelper = new PasswordHelper();
		passwordHelper.encryptPassword(appUser);
		//保存appuser
		this.save(appUser);
		
		
		//设置公司关联关系
		Iterator<AppOrganization> iteratorCompanySet = appUser.getCompanySet().iterator();
		while (iteratorCompanySet.hasNext()) {
			AppUserOrganization appUserOrganization = new AppUserOrganization();
			appUserOrganization.setUserId(appUser.getId());
			appUserOrganization.setOrganizationId(iteratorCompanySet.next().getId());
			appUserOrganization.setType("company");
			appUserOrganizationService.save(appUserOrganization);
		}
		
		//设置门店关联关系
		if(appUser.getShopSet()!=null&&appUser.getShopSet().size()!=0){
			Iterator<AppOrganization> iteratorShopSet = appUser.getShopSet().iterator();
			while (iteratorShopSet.hasNext()) {
				AppUserOrganization appUserOrganization = new AppUserOrganization();
				appUserOrganization.setUserId(appUser.getId());
				appUserOrganization.setOrganizationId(iteratorShopSet.next().getId());
				appUserOrganization.setType("shop");
				appUserOrganizationService.save(appUserOrganization);
				
			}
		}
		
		//设置部门关联关系
		Iterator<AppOrganization> iteratorDepartmentSet = appUser.getDepartmentSet().iterator();
		while (iteratorDepartmentSet.hasNext()) {
			AppUserOrganization appUserOrganization = new AppUserOrganization();
			appUserOrganization.setUserId(appUser.getId());
			appUserOrganization.setOrganizationId(iteratorDepartmentSet.next().getId());
			appUserOrganization.setType("department");
			appUserOrganizationService.save(appUserOrganization);
			
		}
		
		//设置角色关联关系
		Iterator<AppRole> iteratorRoleSet = appUser.getAppRoleSet().iterator();
		while (iteratorRoleSet.hasNext()) {
			AppUserRole appUserRole = new AppUserRole();
			appUserRole.setUserId(appUser.getId());
			appUserRole.setRoleId(iteratorRoleSet.next().getId());
			appUserRoleService.save(appUserRole);
		}
		jsonResult.setSuccess(true);
		jsonResult.setMsg("success");
		return jsonResult;
	}
	
	
	
	
	
	@Override
	public JsonResult modify(HttpServletRequest request, AppUser appUser) {


		JsonResult jsonResult  = new JsonResult();
		
		if(appUser.getCompanySet()==null||appUser.getCompanySet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("所属公司不能为空");
			return jsonResult;
		}
		if(appUser.getDepartmentSet()==null||appUser.getDepartmentSet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("所属部门不能为空");
			return jsonResult;
		}
		if(appUser.getAppRoleSet()==null||appUser.getAppRoleSet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("角色不能为空");
			return jsonResult;
		}
		
		AppUser _appUser = this.get(appUser.getId());
		
		String newpassword = appUser.getPassword();
		String oldpassword = _appUser.getPassword();
		BeanUtil.copyNotNullProperties(appUser, _appUser);
		//保存appuser、密码加密
		if(!newpassword.equals(oldpassword)){
			appUser.setAppuserprefix(ContextUtil.getCurrentUser().getAppuserprefix());
			PasswordHelper  passwordHelper = new PasswordHelper();
			_appUser.setPassword(newpassword);
			passwordHelper.encryptPassword(_appUser);
		}
		this.update(_appUser);
		
		 Long userId = appUser.getId();
		 appUserOrganizationDao.deleteOrganId(userId);
		//删除关系
		QueryFilter filter = new QueryFilter(AppUserRole.class);
		filter.addFilter("userId=", appUser.getId());
		appUserRoleDao.deleteUserId(userId);
		
		//设置公司关联关系
		Iterator<AppOrganization> iteratorCompanySet = appUser.getCompanySet().iterator();
		while (iteratorCompanySet.hasNext()) {
			AppUserOrganization appUserOrganization = new AppUserOrganization();
			appUserOrganization.setUserId(appUser.getId());
			appUserOrganization.setOrganizationId(iteratorCompanySet.next().getId());
			appUserOrganization.setType("company");
			appUserOrganizationService.save(appUserOrganization);
			
		}
		
		//设置门店关联关系
		if(appUser.getShopSet()!=null&&appUser.getShopSet().size()!=0){
			Iterator<AppOrganization> iteratorShopSet = appUser.getShopSet().iterator();
			while (iteratorShopSet.hasNext()) {
				AppUserOrganization appUserOrganization = new AppUserOrganization();
				appUserOrganization.setUserId(appUser.getId());
				appUserOrganization.setOrganizationId(iteratorShopSet.next().getId());
				appUserOrganization.setType("shop");
				appUserOrganizationService.save(appUserOrganization);
				
			}
		}
		
		//设置部门关联关系
		Iterator<AppOrganization> iteratorDepartmentSet = appUser.getDepartmentSet().iterator();
		while (iteratorDepartmentSet.hasNext()) {
			AppUserOrganization appUserOrganization = new AppUserOrganization();
			appUserOrganization.setUserId(appUser.getId());
			appUserOrganization.setOrganizationId(iteratorDepartmentSet.next().getId());
			appUserOrganization.setType("department");
			appUserOrganizationService.save(appUserOrganization);
			
		}
		
		//设置角色关联关系
		Iterator<AppRole> iteratorRoleSet = appUser.getAppRoleSet().iterator();
		while (iteratorRoleSet.hasNext()) {
			AppUserRole appUserRole = new AppUserRole();
			appUserRole.setUserId(appUser.getId());
			appUserRole.setRoleId(iteratorRoleSet.next().getId());
			appUserRoleService.save(appUserRole);
		}
		
		
		jsonResult.setSuccess(true);
		jsonResult.setMsg("success");
		return jsonResult;
	
	
	}
	

	@Override
	public JsonResult remove(String[] ids) {
		
		JsonResult jsonResult = new JsonResult();
		if(ids==null||ids.length==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("id不能为空");
			return jsonResult;
		}
		
		for(String id :ids){
			AppUser appUser = this.get(Long.valueOf(id));
			
			QueryFilter filter = new QueryFilter(AppUserRole.class);
			filter.addFilter("userId=", appUser.getId());
			boolean removeRole = appUserRoleService.delete(filter);
			
			if(!removeRole){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("删除角色出错");
				return jsonResult;
			}
			
			
			QueryFilter filter2 = new QueryFilter(AppUserOrganization.class);
			filter2.addFilter("userId=", appUser.getId());
			boolean removeOrganization = appUserOrganizationService.delete(filter2);
			if(!removeOrganization){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("删除部门出错");
				return jsonResult;
			}
			
			dao.delete(appUser);
		}
		
		jsonResult.setSuccess(true);
		jsonResult.setMsg("删除成功");
		return jsonResult;
	}

	@Override
	public Set<AppRole> getAppRoleSet(AppUser appUser) {
		List<AppUserRole> list = appUserRoleDao.findByAppUser(appUser.getId());
		Set<AppRole> set = new HashSet<AppRole>();
		if(list!=null&&list.size()>0){
			for(AppUserRole appUserRole : list){
				set.add(appRoleService.get(appUserRole.getRoleId()));
			}
		}
		return set;
		
	}
	
	@Override
	public Set<AppResource> getAppResourceSet(AppUser appuser) {
		Set<AppResource>  appResourceSet = new HashSet<AppResource>();
		Set<AppRole> appRoleSet = getAppRoleSet(appuser);
		Iterator<AppRole> iterator = appRoleSet.iterator();
		while (iterator.hasNext()) {
			appResourceSet.addAll(appRoleService.getAppResourceSet(iterator.next()));
		}
		return appResourceSet;
		
	}

	
	
	@Override
	public Set<AppOrganization> getCompanySet(AppUser appUser) {
		List<AppUserOrganization> list = appUserOrganizationDao.findByAppUser(appUser.getId(),"company");
		Set<AppOrganization> set = new HashSet<AppOrganization>();
		if(list!=null&&list.size()>0){
			for(AppUserOrganization appUserOrganization : list){
				set.add(appOrganizationService.get(appUserOrganization.getOrganizationId()));
			}
		}
		return set;
	}

	@Override
	public Set<AppOrganization> getShopSet(AppUser appUser) {
		List<AppUserOrganization> list = appUserOrganizationDao.findByAppUser(appUser.getId(),"shop");
		Set<AppOrganization> set = new HashSet<AppOrganization>();
		if(list!=null&&list.size()>0){
			for(AppUserOrganization appUserOrganization : list){
				set.add(appOrganizationService.get(appUserOrganization.getOrganizationId()));
			}
		}
		return set;
	}

	@Override
	public Set<AppOrganization> getDepartmentSet(AppUser appUser) {
		List<AppUserOrganization> list = appUserOrganizationDao.findByAppUser(appUser.getId(),"department");
		Set<AppOrganization> set = new HashSet<AppOrganization>();
		if(list!=null&&list.size()>0){
			for(AppUserOrganization appUserOrganization : list){
		//		set.add(appOrganizationDao.get(appUserOrganization.getOrganizationId()));
				
				set.add(appOrganizationDao.selectByPrimaryKey(appUserOrganization.getOrganizationId()));
				
			}
		}
		return set;
	}

	@Override
	public PageResult findPage(HttpServletRequest request) {
		
		String organizationId = request.getParameter("organizationId");
		
		AppOrganization appOrganization = null;
		if(StringUtils.isEmpty(organizationId)||"0".equals(organizationId)){
			QueryFilter queryFilter = new QueryFilter(AppOrganization.class);
			queryFilter.addFilter("type=", "root");
			appOrganization = appOrganizationService.get(queryFilter);
			
		}else{
			appOrganization = appOrganizationService.get(Long.valueOf(organizationId));
			
		}
		if(appOrganization!=null){
			//递归查找当前组织下的所有组织 返回 ids 1,2,3
			String ids = appOrganizationService.findSonsToIds(appOrganization);
			
			
			String username = request.getParameter("username_like");
			String mobilePhone = request.getParameter("mobilePhone_like");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids", ids.split(","));
			if(!StringUtils.isEmpty(username)){
				map.put("username", "%"+username+"%");
			}
			if(!StringUtils.isEmpty(mobilePhone)){
				map.put("mobilePhone", "%"+mobilePhone+"%");
			}
			
			
			//封装必要参数
			QueryFilter filter = new QueryFilter(AppUser.class, request);
			
			//分页插件
			Page<AppUser> page = PageHelper.startPage(filter.getPage(), filter.getPageSize());
			
			//查询方法
			List<AppUser> list = ((AppUserDao)dao).findPage(map);
			
			//封装页面数据
			PageResult p = new PageResult();
			p.setDraw(filter.getDraw());
			p.setRows(list);
			p.setRecordsTotal(page.getTotal());
			p.setRecordsFiltered(page.getTotal());
			p.setPage(filter.getPage());
			p.setPageSize(filter.getPageSize());
			return p;
		}
		return null;
		
	}

	@Override
	public JsonResult openVip(AppUser appUser) {

		
		JsonResult jsonResult  = new JsonResult();
		
		QueryFilter filter = new QueryFilter(AppUser.class);
		filter.addFilter("username=", appUser.getUsername());
		
	//	List<AppUser> find = dao.find(filter);
		List<AppUser> find = find(filter);
		
		if(find!=null&&find.size()>0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("账号不能重复");
			return jsonResult;
		}
		
		if(appUser.getCompanySet()==null||appUser.getCompanySet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("所属公司不能为空");
			return jsonResult;
		}
//		if(appUser.getDepartmentSet()==null||appUser.getDepartmentSet().size()==0){
//			jsonResult.setSuccess(false);
//			jsonResult.setMsg("所属部门不能为空");
//			return jsonResult;
//		}
		if(appUser.getAppRoleSet()==null||appUser.getAppRoleSet().size()==0){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("角色不能为空");
			return jsonResult;
		}
		
		//设置appuser账号前缀
		PasswordHelper  passwordHelper = new PasswordHelper();
		passwordHelper.encryptPassword(appUser);
		//保存appuser
		this.save(appUser);
		
		
		//设置公司关联关系
		Iterator<AppOrganization> iteratorCompanySet = appUser.getCompanySet().iterator();
		while (iteratorCompanySet.hasNext()) {
			AppUserOrganization appUserOrganization = new AppUserOrganization();
			appUserOrganization.setUserId(appUser.getId());
			appUserOrganization.setOrganizationId(iteratorCompanySet.next().getId());
			appUserOrganization.setType("company");
			appUserOrganizationService.save(appUserOrganization);
		}
		
		//设置门店关联关系
		if(appUser.getShopSet()!=null&&appUser.getShopSet().size()!=0){
			Iterator<AppOrganization> iteratorShopSet = appUser.getShopSet().iterator();
			while (iteratorShopSet.hasNext()) {
				AppUserOrganization appUserOrganization = new AppUserOrganization();
				appUserOrganization.setUserId(appUser.getId());
				appUserOrganization.setOrganizationId(iteratorShopSet.next().getId());
				appUserOrganization.setType("shop");
//				appUserOrganizationDao.save(appUserOrganization);
				appUserOrganizationService.save(appUserOrganization);
				
			}
		}
		
		//设置部门关联关系
		if(appUser.getDepartmentSet()!=null){
			Iterator<AppOrganization> iteratorDepartmentSet = appUser.getDepartmentSet().iterator();
			while (iteratorDepartmentSet.hasNext()) {
				AppUserOrganization appUserOrganization = new AppUserOrganization();
				appUserOrganization.setUserId(appUser.getId());
				appUserOrganization.setOrganizationId(iteratorDepartmentSet.next().getId());
				appUserOrganization.setType("department");
				appUserOrganizationService.save(appUserOrganization);
				
			}
		}
		
		//设置角色关联关系
		Iterator<AppRole> iteratorRoleSet = appUser.getAppRoleSet().iterator();
		while (iteratorRoleSet.hasNext()) {
			AppUserRole appUserRole = new AppUserRole();
			appUserRole.setUserId(appUser.getId());
			appUserRole.setRoleId(iteratorRoleSet.next().getId());
//			appUserRoleDao.save(appUserRole);
			appUserRoleService.save(appUserRole);
			
		}
		
		jsonResult.setObj(appUser);
		jsonResult.setSuccess(true);
		jsonResult.setMsg("success");
		return jsonResult;
	
	}



	


	
}
