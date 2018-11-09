package com.mz.shiro.authorization.service.impl;

import com.mz.shiro.authorization.model.Authorization;
import com.mz.shiro.authorization.service.AuthorizationService;
import com.mz.shiro.dao.AppRoleDao;
import com.mz.shiro.dao.AppUserDao;
import com.mz.shiro.dao.AppUserRoleDao;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.oauth.user.model.AppRole;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.user.model.AppUserRole;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * client 端访问调用权限查询service
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年11月4日 上午10:11:35
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
	
	@Resource
	private AppRoleDao appRoleDao;
	@Resource
	private AppUserRoleDao appUserRoleDao;
	@Resource
	private AppUserDao appUserDao;
	
	
	@Override
	public Authorization createAuthorization(Authorization authorization) {
		return null;
	}

	@Override
	public Authorization updateAuthorization(Authorization authorization) {
		return null;
	}

	@Override
	public void deleteAuthorization(Long authorizationId) {
		
	}

	@Override
	public Authorization findOne(Long authorizationId) {
		return null;
	}

	@Override
	public List<Authorization> findAll() {
		return null;
	}

	@Override
	public Set<String> findRoles(String saasId,String appKey, AppUser user) {
		/*
		 * appKey  保留日后扩展
		 */
		if(PropertiesUtils.APP.getProperty("app.admin").equals(user.getUsername())){
			
			Set<String> set = new HashSet<String>();
			
			QueryFilter filter = new QueryFilter(AppRole.class);
			filter.addFilter("saasId=", saasId);
			List<AppRole> list =    appRoleDao.selectByExample(filter.setNosaas().getExample());
			
			
			for(AppRole appRole : list){
				set.add(appRole.getName());
			}
			
			return set;
			
		}else{
			Set<String> set = new HashSet<String>();
			
			QueryFilter filter = new QueryFilter(AppUserRole.class);
			filter.addFilter("saasId=", saasId);
			filter.addFilter("userId=", user.getId()+"");
			
			
			//查用户角色关联表
			
			List<AppUserRole> listAppUserRole = appUserRoleDao.selectByExample(filter.setNosaas().getExample());
			
			if(listAppUserRole!=null&&listAppUserRole.size()>0){
				for(AppUserRole appUserRole : listAppUserRole){
					AppRole appRole = appRoleDao.selectByPrimaryKey(appUserRole.getRoleId());
					set.add(appRole.getName());
				}
			}
			return set;
		}
		
	}

	@Override
	public Set<String> findPermissions(String saasId,String appKey, AppUser user) {
		/*
		 * appKey  保留日后扩展
		 */
		if(PropertiesUtils.APP.getProperty("app.admin").equals(user.getUsername())){
			return  appUserDao.findAllShiroUrl();
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			return appUserDao.findUserShiroUrl(map);
		}
		
	}




}
