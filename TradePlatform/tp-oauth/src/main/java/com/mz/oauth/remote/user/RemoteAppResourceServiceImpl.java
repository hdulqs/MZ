/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年11月25日 下午4:48:47
 */
package com.mz.oauth.remote.user;

import com.mz.shiro.dao.AppResourceDao;
import com.mz.shiro.dao.AppRoleResourceDao;
import com.mz.shiro.dao.AppUserRoleDao;
import com.mz.shiro.service.AppResourceService;
import com.mz.shiro.service.AppRoleResourceService;
import com.mz.shiro.service.AppUserRoleService;
import com.mz.util.QueryFilter;
import com.mz.manage.init.model.MgrAppMenu;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRoleResource;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.user.model.AppUserRole;
import com.mz.tenant.user.model.SaasUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;

/**
 * 
 * <p>
 * TODO
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2015年11月25日 下午4:48:47
 */
public class RemoteAppResourceServiceImpl implements RemoteAppResourceService {

	@Resource
	private AppResourceDao appResourceDao;

	@Resource
	private AppResourceService appResourceService;

	@Resource
	private AppUserRoleDao appUserRoleDao;
	@Resource
	private AppUserRoleService appUserRoleService;
	@Resource
	private AppRoleResourceDao appRoleResourceDao;
	@Resource
	private AppRoleResourceService appRoleResourceService;

	@Override
	public Serializable save_NOSAAS(AppResource appResource) {
		// return appResourceDao.save_NOSAAS(appResource);

		return appResourceDao.insertSelective(appResource);
	}

	@Override
	public List<AppResource> findBySaasId(String saasId) {
		// QueryFilter filter = new QueryFilter();
		// filter.addFilter("Q_t.saasId_=_String", saasId);
		// return appResourceDao.find_NOSAAS(filter);
		QueryFilter filter = new QueryFilter(AppResource.class);
		filter.addFilter("saasId=", saasId);

		return appResourceDao.selectByExample(filter.setNosaas().getExample());
	}

	@Override
	public Set<String> getAllResource(AppUser user) {

		Set<String> set = new HashSet<String>();

		QueryFilter filter = new QueryFilter(AppUserRole.class);
		filter.setSaasId(user.getSaasId());
		filter.addFilter("userId=", user.getId());

		// 查用户角色关联表
		List<AppUserRole> listRoles = appUserRoleService.find(filter);

		if (listRoles != null && listRoles.size() > 0) {
			// 遍历用户角色关联表
			for (AppUserRole appUserRole : listRoles) {

				QueryFilter filter2 = new QueryFilter(AppUserRole.class);
				filter2.setSaasId(user.getSaasId());
				filter2.addFilter("roleId=", appUserRole.getRoleId());

				// 查角色权限关联表
				List<AppRoleResource> listResources = appRoleResourceService.find(filter2);

				if (listResources != null && listResources.size() > 0) {
					// 遍历用户角色关联表
					for (AppRoleResource appRoleResource : listResources) {
						AppResource appResource = appResourceService.get(appRoleResource.getResourceId());
						filter.addFilter("isVisible!=", "1");
						// set装入
						set.add(appResource.getMkey());

					}
				}

			}

		}

		return set;

	}

	@Override
	public void init(SaasUser saasUser, List<MgrAppMenu> list) {
		for (MgrAppMenu mgrAppMenu : list) {
			// 创建权限
			AppResource appResource = new AppResource();
			// copy属性
			appResource.setAppName(mgrAppMenu.getAppName());
			appResource.setName(mgrAppMenu.getName());
			appResource.setMkey(mgrAppMenu.getMkey());
			appResource.setPkey(mgrAppMenu.getPkey());
			appResource.setShiroUrl(mgrAppMenu.getShiroUrl());
			appResource.setUrl(mgrAppMenu.getUrl());
			appResource.setType(mgrAppMenu.getType());
			// saasId
			appResource.setSaasId(saasUser.getSaasId());
			// appResourceDao.save_NOSAAS(appResource);

			appResourceDao.insertSelective(appResource);

		}

	}

	@Override
	public void restore(SaasUser saasUser, List<MgrAppMenu> list) {
		// QueryFilter filter = new QueryFilter();

		QueryFilter filter = new QueryFilter(SaasUser.class);

		// filter.addFilter("Q_t.saasId_=_String", saasUser.getSaasId());

		filter.addFilter("saasId=", saasUser.getSaasId());

		// List<AppResource> listAppResource =
		// appResourceDao.find_NOSAAS(filter);
		List<AppResource> listAppResource = appResourceDao.selectByExample(filter.setNosaas().getExample());

		if (listAppResource != null) {
			for (AppResource appResource : listAppResource) {
				appResourceDao.delete(appResource);
			}
		}
	}

	@Override
	public void updateinit(SaasUser saasUser, List<MgrAppMenu> list) {

		try {

			QueryFilter filter = new QueryFilter(AppResource.class);

			filter.addFilter("saasId=", saasUser.getSaasId());

			// 业务平台AppMenu
			// List<AppResource> listAll = appResourceDao.find_NOSAAS(filter);

			List<AppResource> listAll = appResourceDao.selectByExample(filter.setNosaas().getExample());

			// 更新过的key值
			List<String> updateKey = new ArrayList<String>();
			// --------------------------------比较第一步 业务平台的AppMenu 和管理平台的menu
			for (AppResource appResource : listAll) {
				boolean isHas = false;
				for (int i = 0; i < list.size(); i++) {
					// 业务平台的menu存在管理平台的menu中key值匹配上 ---------更新
					MgrAppMenu mgrAppMenu = list.get(i);
					if (appResource.getMkey().equals(mgrAppMenu.getMkey())) {
						isHas = true;

						// copy属性
						appResource.setAppName(mgrAppMenu.getAppName());
						appResource.setName(mgrAppMenu.getName());
						appResource.setMkey(mgrAppMenu.getMkey());
						appResource.setPkey(mgrAppMenu.getPkey());
						appResource.setShiroUrl(mgrAppMenu.getShiroUrl());
						appResource.setUrl(mgrAppMenu.getUrl());
						appResource.setType(mgrAppMenu.getType());

						// appResourceDao.update(appResource);

						appResourceDao.updateByPrimaryKeySelective(appResource);

						// 被更新过的key值
						updateKey.add(list.get(i).getMkey());

						break;
					}
				}
				// 业务平台的menu不存在管理平台的menu中key值匹配不上 ----------- 执行删除
				if (!isHas) {
					// 先删除外键关系
					// QueryFilter filterRoleResource = new QueryFilter();
					// filterRoleResource.addFilter("Q_t.saasId_=_String",
					// saasUser.getSaasId());
					// filterRoleResource.addFilter("Q_t.resourceId_=_Long",
					// appResource.getId()+"");

					QueryFilter filterRoleResource = new QueryFilter(AppRoleResource.class);
					filterRoleResource.addFilter("saasId=", saasUser.getSaasId());
					filterRoleResource.addFilter("resourceId=", appResource.getId());

					// List<AppRoleResource> listAppRoleResource =
					// appRoleResourceDao.find_NOSAAS(filterRoleResource);

					List<AppRoleResource> listAppRoleResource = appRoleResourceDao.selectByExample(filterRoleResource.setNosaas().getExample());

					if (listAppRoleResource != null) {
						for (AppRoleResource appRoleResource : listAppRoleResource) {
							appRoleResourceDao.delete(appRoleResource);
						}
					}
					// 再删除对象
					appResourceDao.delete(appResource);
				}
			}

			// --------------------------------比较第二步 menu.xml中的数据 没有被更新过的执行新增
			for (MgrAppMenu mgrAppMenu : list) {
				// 是否被更新过
				boolean isUpdate = false;
				for (String str : updateKey) {
					if (mgrAppMenu.getMkey().equals(str)) {
						isUpdate = true;// 表示更新过
						break;
					}
				}

				if (!isUpdate) {// 如果没有更新过-------执行新增
					// 创建权限
					AppResource appResource = new AppResource();
					// copy属性
					appResource.setAppName(mgrAppMenu.getAppName());
					appResource.setName(mgrAppMenu.getName());
					appResource.setMkey(mgrAppMenu.getMkey());
					appResource.setPkey(mgrAppMenu.getPkey());
					appResource.setShiroUrl(mgrAppMenu.getShiroUrl());
					appResource.setUrl(mgrAppMenu.getUrl());
					appResource.setType(mgrAppMenu.getType());
					// saasId
					appResource.setSaasId(saasUser.getSaasId());

					appResourceDao.insertSelective(appResource);

					// appResourceDao.save_NOSAAS(appResource);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
