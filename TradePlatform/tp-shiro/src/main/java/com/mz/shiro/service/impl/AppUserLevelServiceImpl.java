/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年2月17日 下午3:03:58
 */
package com.mz.shiro.service.impl;

import com.mz.shiro.dao.AppUserDao;
import com.mz.shiro.service.AppUserLevelService;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.util.properties.PropertiesUtils;
import com.mz.oauth.user.model.AppUser;
import com.mz.oauth.user.model.AppUserLevel;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年2月17日 下午3:03:58 
 */
@Service("appUserLevelService")
public class AppUserLevelServiceImpl extends BaseServiceImpl<AppUserLevel, Long> implements
		AppUserLevelService {

	@Resource(name = "appUserLevelDao")
	@Override
	public void setDao(BaseDao<AppUserLevel, Long> dao) {
		super.dao = dao;
	}
	
	@Resource
	private AppUserDao appUserDao;

	@Override
	public JsonResult addBatch(List<AppUserLevel> users) {
		JsonResult jsonResult = new JsonResult();
		try {
			//删除所有的上下级关系
	//		List<AppUserLevel> findAll = dao.findAll();
			
			List<AppUserLevel> findAll = dao.selectAll();
			
			for (AppUserLevel appUserLevel : findAll) {
				dao.delete(appUserLevel);
			}
			//增加一个root
			AppUserLevel level = new AppUserLevel();
			level.setUserId(Long.valueOf(0));
			level.setSuperiorId(null);
			level.setName("root");
		//	dao.save(level);
			
			dao.insertSelective(level);
			
			//重新保存
			if(users!=null){
				for(AppUserLevel appUserLevel : users){
				//	dao.save(appUserLevel);
					
					dao.insertSelective(appUserLevel);
				}
			}
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}

	@Override
	public List<AppUserLevel> saveForlist() {
		/**
		 * 2、先查询用户表和上下级表进行比较，上下级中没有的从用户表信息中复制到上下级表中，最终目的使用户表和上下级表数据相同，方便页面直接操作上下级表数据
		 */
		
//		QueryFilter filter = new QueryFilter();
		QueryFilter filter = new QueryFilter(AppUserLevel.class);
		
		//过滤admin账户
//		filter.addFilter("Q_t.username_!=_String", "admin");
		
		filter.addFilter("username=", PropertiesUtils.APP.getProperty("app.admin"));
		
//		List<AppUser> appUserList = appUserDao.find(filter);
		
		List<AppUser> appUserList  = appUserDao.selectByExample(filter.getExample());
		
		
//		List<AppUserLevel> appUserLevelList = dao.findAll();
		
		List<AppUserLevel> appUserLevelList =  dao.selectAll();
		
		//比较两表
		for(AppUser appUser : appUserList){
			boolean flag = false; //标记appUser是否存在appUserLevel中
			for(AppUserLevel appUserLevel : appUserLevelList){
				if(appUserLevel.getUserId().compareTo(appUser.getId())==0){
					//如果存在，标记为true
					flag = true;
				}
			}
			
			if(!flag){//如果不存在，增加一条appUserlevel记录
				AppUserLevel appUserLevel = new AppUserLevel();
				appUserLevel.setUserId(appUser.getId());
				appUserLevel.setSuperiorId(Long.valueOf(0));//上级ID默认设置为0
				appUserLevel.setName(appUser.getName());
			//	dao.save(appUserLevel);
				
				dao.insertSelective(appUserLevel);
				
			}
		}
		
		QueryFilter filter2 = new QueryFilter(AppUserLevel.class);
		
	//	QueryFilter filter2 = new QueryFilter();
		//过滤root账户
	//	filter2.addFilter("Q_t.name_!=_String", "root");
		
		filter2.addFilter("name!=", "root");
		return dao.selectAll();
	//	return dao.find(filter2);
	}

}
