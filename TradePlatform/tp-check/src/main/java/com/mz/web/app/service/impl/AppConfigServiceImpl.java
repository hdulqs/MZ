/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年10月10日  18:41:55
 */
package com.mz.web.app.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.AppConfig;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.web.app.dao.AppConfigDao;
import com.mz.web.app.service.AppConfigService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:   Gao Mimi        
 * @Date :    2015年10月10日  18:41:55     
 */
@Service("appConfigService")
public class AppConfigServiceImpl extends BaseServiceImpl<AppConfig, Long> implements AppConfigService{

	@Resource(name = "appConfigDao")
	@Override
	public void setDao(BaseDao<AppConfig, Long> dao) {
		super.dao = dao;
	}

	@Override
	public String getBykey(String key) {
		QueryFilter filter = new QueryFilter(AppConfig.class);
		if (key != null) {
			filter.addFilter("configkey=", key);
		}
		
		
		List<AppConfig> list = super.dao.selectByExample(filter.setNosaas().getExample());
		return list==null?null:list.get(0).getValue();
	}


	
	@Override
	public 	List<AppConfig> findKey() {
		
		return ((AppConfigDao)dao).findKey();
	}
	
	

	@Override
	public List<AppConfig> getConfig(Map<String, String> map) {
		List<AppConfig> list=((AppConfigDao)dao).getConfig(map);
		return list;
	}

	@Override
	public void setBykeyToDB(String key,String value) {
		QueryFilter filter = new QueryFilter(AppConfig.class);
		if (key != null) {
			filter.addFilter("configkey=", key);
		}
		AppConfig appConfig=this.get(filter);
		if(null!=appConfig){
			appConfig.setValue(value);
			this.update(appConfig);
		}
	}


	/**
	 * 0 开启，1 不开启
	 */
	@Override
	public String getBykeyfromDB(String key) {
		QueryFilter filter = new QueryFilter(AppConfig.class);
		if (key != null) {
			filter.addFilter("configkey=", key);
		}
		AppConfig appConfig=this.get(filter);
		if(null!=appConfig){
			return appConfig.getValue();
		}
		return null;
	}


	

}