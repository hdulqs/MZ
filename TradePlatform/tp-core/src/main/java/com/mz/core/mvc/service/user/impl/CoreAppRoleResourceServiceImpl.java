/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午11:42:06
 */
package com.mz.core.mvc.service.user.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.user.CoreAppRoleResourceService;
import com.mz.oauth.user.model.AppRoleResource;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月21日 上午11:42:06 
 */
@Service
public class CoreAppRoleResourceServiceImpl extends BaseServiceImpl<AppRoleResource, Long> implements CoreAppRoleResourceService{
	
	@Resource(name="coreAppRoleResourceDao")
	@Override
	public void setDao(BaseDao<AppRoleResource, Long> dao) {
		super.dao = dao;
		
	}
	
}
