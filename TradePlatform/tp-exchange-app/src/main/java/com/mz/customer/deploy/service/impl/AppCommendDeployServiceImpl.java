/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 16:07:54 
 */
package com.mz.customer.deploy.service.impl;

import com.mz.customer.deploy.model.AppCommendDeploy;
import com.mz.customer.deploy.service.AppCommendDeployService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppCommendDeployService </p>
 * @author:         menwei
 * @Date :          2017-11-28 16:07:54  
 */
@Service("appCommendDeployService")
public class AppCommendDeployServiceImpl extends BaseServiceImpl<AppCommendDeploy, Long> implements AppCommendDeployService{
	
	@Resource(name="appCommendDeployDao")
	@Override
	public void setDao(BaseDao<AppCommendDeploy, Long> dao) {
		super.dao = dao;
	}
	

}
