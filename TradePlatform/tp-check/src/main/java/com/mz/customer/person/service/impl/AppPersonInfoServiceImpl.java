/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zhangcb
 * @version:     V1.0 
 * @Date:        2016-11-22 18:25:52 
 */
package com.mz.customer.person.service.impl;


import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.person.service.AppPersonInfoService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p> AppPersonInfoService </p>
 * @author:         zhangcb
 * @Date :          2016-11-22 18:25:52  
 */
@Service("appPersonInfoService")
public class AppPersonInfoServiceImpl extends BaseServiceImpl<AppPersonInfo, Long> implements AppPersonInfoService{
	
	@Resource(name="appPersonInfoDao")
	@Override
	public void setDao(BaseDao<AppPersonInfo, Long> dao) {
		super.dao = dao;
	}

	
}