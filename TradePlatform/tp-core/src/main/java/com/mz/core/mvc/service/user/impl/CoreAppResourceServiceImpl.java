/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午11:42:06
 */
package com.mz.core.mvc.service.user.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.menu.CoreAppMenuService;
import com.mz.web.menu.model.AppMenu;
import com.mz.core.mvc.service.user.CoreAppResourceService;
import com.mz.core.mvc.service.user.CoreAppRoleResourceService;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.oauth.user.model.AppResource;
import com.mz.oauth.user.model.AppRoleResource;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月21日 上午11:42:06 
 */
@Service
public class CoreAppResourceServiceImpl extends BaseServiceImpl<AppResource, Long> implements CoreAppResourceService{
	
	@Resource(name="coreAppResourceDao")
	@Override
	public void setDao(BaseDao<AppResource, Long> dao) {
		super.dao = dao;
		
	}
	@Resource 
	private CoreAppMenuService coreAppMenuService;
	@Resource
	private CoreAppRoleResourceService coreAppRoleResourceService;
	

	
	@Override
	public void init(String appName,List<AppMenu> list) {

		try {
			
			 QueryFilter filter = new QueryFilter(AppResource.class);
			 filter.addFilter("saasId=", PropertiesUtils.APP.getProperty("app.saasId"));
			 //查询当前业务平台的所有AppMenu
			 List<AppResource> listAll = super.find(filter.setNosaas());
			 
			 
			 //更新过的key值
			 List<String> updateKey = new ArrayList<String>();
			 //--------------------------------比较第一步    业务平台的AppMenu 和管理平台的menu
			 //--------------------------------比较第一步    业务平台的AppMenu 和管理平台的menu
			 for(AppResource  appResource: listAll){
				 boolean isHas = false;
				 for(int i = 0 ; i < list.size() ; i++ ){
					 //业务平台的menu存在管理平台的menu中key值匹配上 ---------更新
					 AppMenu mgrAppMenu = list.get(i);
					 if(appResource.getMkey().equals(mgrAppMenu.getMkey())){
						 isHas = true;
						 
						//copy属性
						 appResource.setAppName(mgrAppMenu.getAppName());
						 appResource.setName(mgrAppMenu.getName());
						 appResource.setMkey(mgrAppMenu.getMkey());
						 appResource.setPkey(mgrAppMenu.getPkey());
						 appResource.setShiroUrl(mgrAppMenu.getShiroUrl());
						 appResource.setUrl(mgrAppMenu.getUrl());
						 appResource.setType(mgrAppMenu.getType());
						 
					//	 appResourceDao.update(appResource);
						 super.update(appResource);
						 
						 //被更新过的key值
						 updateKey.add(list.get(i).getMkey());
						 
						 break;
					 }
				 }
				 //业务平台的menu不存在管理平台的menu中key值匹配不上 ----------- 执行删除
				 if(!isHas){
					 //先删除外键关系
					 	
					 QueryFilter filterRoleResource = new QueryFilter(AppRoleResource.class);
					 filterRoleResource.addFilter("saasId=", PropertiesUtils.APP.getProperty("app.saasId"));
					 filterRoleResource.addFilter("resourceId=", appResource.getId());
					 List<AppRoleResource> listAppRoleResource =  coreAppRoleResourceService.find(filterRoleResource.setNosaas());
					 
					 if(listAppRoleResource!=null){
						 for(AppRoleResource appRoleResource :  listAppRoleResource){
							 coreAppRoleResourceService.delete(appRoleResource.getId());
						 }
					 }
					 //再删除对象
					// appResourceDao.delete(appResource);
					 super.delete(appResource.getId());
				 }
			 }
			 
			//--------------------------------比较第二步    menu.xml中的数据      没有被更新过的执行新增
			 //--------------------------------比较第二步    menu.xml中的数据      没有被更新过的执行新增
			 for(AppMenu mgrAppMenu : list){
				 //是否被更新过
				 boolean isUpdate = false;
				 for(String str :updateKey){
					 if(mgrAppMenu.getMkey().equals(str)){
						 isUpdate = true;//表示更新过
						 break;
					 }
				 }
				 
				 if(!isUpdate){//如果没有更新过-------执行新增
					//创建权限
					AppResource appResource = new AppResource();
					//copy属性
					appResource.setAppName(mgrAppMenu.getAppName());
					appResource.setName(mgrAppMenu.getName());
					appResource.setMkey(mgrAppMenu.getMkey());
					appResource.setPkey(mgrAppMenu.getPkey());
					appResource.setShiroUrl(mgrAppMenu.getShiroUrl());
					appResource.setUrl(mgrAppMenu.getUrl());
					appResource.setType(mgrAppMenu.getType());
					//saasId
					appResource.setSaasId(PropertiesUtils.APP.getProperty("app.saasId"));
					
					//appResourceDao.insertSelective(appResource);
					super.save(appResource);
					
				//	appResourceDao.save_NOSAAS(appResource);
				 }
				 
			 }

			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	}

	
}
