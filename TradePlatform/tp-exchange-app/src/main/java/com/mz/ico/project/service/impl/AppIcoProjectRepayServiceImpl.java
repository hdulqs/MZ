/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-21 17:52:29 
 */
package com.mz.ico.project.service.impl;

import com.mz.ico.project.model.AppIcoProjectRepay;
import com.mz.ico.project.service.AppIcoProjectRepayService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoProjectRepayService </p>
 * @author:         shangxl
 * @Date :          2017-07-21 17:52:29  
 */
@Service("appIcoProjectRepayService")
public class AppIcoProjectRepayServiceImpl extends BaseServiceImpl<AppIcoProjectRepay, Long> implements
    AppIcoProjectRepayService {
	
	@Resource(name="appIcoProjectRepayDao")
	@Override
	public void setDao(BaseDao<AppIcoProjectRepay, Long> dao) {
		super.dao = dao;
	}
	

}
