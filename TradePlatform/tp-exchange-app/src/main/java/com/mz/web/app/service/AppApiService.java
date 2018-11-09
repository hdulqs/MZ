/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年10月10日  18:41:55
 */
package com.mz.web.app.service;

import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppApi;

/**
 * 
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年8月22日 下午4:23:37
 */
public interface AppApiService extends BaseService<AppApi, Long> {
	

	PageResult findPageBySql(QueryFilter filter);
	
}


