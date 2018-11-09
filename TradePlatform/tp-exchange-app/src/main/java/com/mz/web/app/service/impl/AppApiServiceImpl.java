/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年10月10日  18:41:55
 */
package com.mz.web.app.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppApi;
import com.mz.web.app.service.AppApiService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年8月22日 下午4:23:55
 */
@Service("appApiService")
public class AppApiServiceImpl extends BaseServiceImpl<AppApi, Long> implements AppApiService {
	
	
	@Resource(name = "appApiDao")
	@Override
	public void setDao(BaseDao<AppApi, Long> dao) {
		super.dao = dao;
	}

	
	
	@Override
	public PageResult findPageBySql(QueryFilter filter) {
		
		return null;
	}

	
	

}