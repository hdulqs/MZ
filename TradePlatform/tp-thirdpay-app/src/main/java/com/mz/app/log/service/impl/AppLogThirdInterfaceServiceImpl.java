/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-28 15:16:24 
 */
package com.mz.app.log.service.impl;

import com.mz.app.log.model.AppLogThirdInterface;
import com.mz.app.log.service.AppLogThirdInterfaceService;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p> AppLogThirdInterfaceService </p>
 * @author:         shangxl
 * @Date :          2017-07-28 15:16:24  
 */
@Service("appLogThirdInterfaceService")
public class AppLogThirdInterfaceServiceImpl extends BaseServiceImpl<AppLogThirdInterface, Long> implements AppLogThirdInterfaceService{
	
	@Resource(name="appLogThirdInterfaceDao")
	@Override
	public void setDao(BaseDao<AppLogThirdInterface, Long> dao) {
		super.dao = dao;
	}
	
	@Override
	public void saveThirdInterfaceLog(AppLogThirdInterface appLogThirdInterface){
		this.save(appLogThirdInterface);
	}

}
