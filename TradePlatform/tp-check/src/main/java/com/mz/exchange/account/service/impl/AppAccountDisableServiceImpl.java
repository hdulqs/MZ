/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao mimi
 * @version:     V1.0 
 * @Date:        2016-12-12 19:39:38 
 */
package com.mz.exchange.account.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.account.model.AppAccountDisable;
import com.mz.exchange.account.service.AppAccountDisableService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppAccountDisableService </p>
 * @author:         Gao mimi
 * @Date :          2016-12-12 19:39:38  
 */
@Service("appAccountDisableService")
public class AppAccountDisableServiceImpl extends BaseServiceImpl<AppAccountDisable, Long> implements AppAccountDisableService{
	
	@Resource(name="appAccountDisableDao")
	@Override
	public void setDao(BaseDao<AppAccountDisable, Long> dao) {
		super.dao = dao;
	}


}
