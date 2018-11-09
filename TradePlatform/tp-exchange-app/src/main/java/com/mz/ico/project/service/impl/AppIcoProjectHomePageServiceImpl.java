/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-21 16:51:55 
 */
package com.mz.ico.project.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.project.model.AppIcoProjectHomePage;
import com.mz.ico.project.service.AppIcoProjectHomePageService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoProjectHomePageService </p>
 * @author:         shangxl
 * @Date :          2017-07-21 16:51:55  
 */
@Service("appIcoProjectHomePageService")
public class AppIcoProjectHomePageServiceImpl extends BaseServiceImpl<AppIcoProjectHomePage, Long> implements AppIcoProjectHomePageService{
	
	@Resource(name="appIcoProjectHomePageDao")
	@Override
	public void setDao(BaseDao<AppIcoProjectHomePage, Long> dao) {
		super.dao = dao;
	}
	

}
