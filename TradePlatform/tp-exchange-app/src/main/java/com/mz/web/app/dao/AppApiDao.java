/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年8月20日 下午5:05:21
 */
package com.mz.web.app.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.web.app.model.AppApi;
import java.util.List;
import java.util.Map;



/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年8月20日 下午5:05:21 
 */
public interface AppApiDao extends BaseDao<AppApi, Long>{

	
	List<AppApi> findPageBySql(Map<String,Object> map);
}
