/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-26 20:15:54 
 */
package com.mz.ico.project.service.impl;

import com.mz.ico.project.model.AppIcoProjectShare;
import com.mz.ico.project.service.AppIcoProjectShareService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoProjectShareService </p>
 * @author:         shangxl
 * @Date :          2017-07-26 20:15:54  
 */
@Service("appIcoProjectShareService")
public class AppIcoProjectShareServiceImpl extends BaseServiceImpl<AppIcoProjectShare, Long> implements AppIcoProjectShareService{
	
	@Resource(name="appIcoProjectShareDao")
	@Override
	public void setDao(BaseDao<AppIcoProjectShare, Long> dao) {
		super.dao = dao;
	}
	

}
