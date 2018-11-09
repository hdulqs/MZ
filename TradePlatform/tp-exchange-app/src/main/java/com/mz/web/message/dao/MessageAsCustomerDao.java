/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年09月28日  18:10:04
 */
package com.mz.web.message.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.web.app.model.MessageAsCustomer;
import com.mz.manage.remote.model.Oamessage;
import java.util.List;
import java.util.Map;

/**
 * <p> TODO</p>
 * @author:  Gao Mimi        
 * @Date :   2015年09月28日  18:10:04     
 */
public interface MessageAsCustomerDao extends BaseDao<MessageAsCustomer,Long>{
	

	/**
	 * 前台分页查询
	 * @param params
	 * @return
	 */
	List<Oamessage> findFrontPageBySql(Map<String, String> params);
	
}