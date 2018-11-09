/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月12日 下午5:39:55
 */
package com.mz.thirdpay.biz.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import java.util.List;


import com.mz.thirdpay.AppThirdPayConfig;


/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月12日 下午5:39:55 
 */             
public interface AppThirdPayConfigDao extends BaseDao<AppThirdPayConfig, Long> {
	
	
	List<AppThirdPayConfig> findType();
		
		
}
