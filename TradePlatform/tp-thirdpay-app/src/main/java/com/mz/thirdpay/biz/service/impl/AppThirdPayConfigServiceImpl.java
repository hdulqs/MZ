/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月12日 下午4:27:13
 */
package com.mz.thirdpay.biz.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.thirdpay.AppThirdPayConfig;
import com.mz.thirdpay.biz.dao.AppThirdPayConfigDao;
import com.mz.thirdpay.biz.service.AppThirdPayConfigService;


/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月12日 下午4:27:13 
 */
@Service("appThirdPayConfigService")
public class AppThirdPayConfigServiceImpl extends BaseServiceImpl<AppThirdPayConfig, Long> implements AppThirdPayConfigService{

	@Resource(name="appThirdPayConfigDao")
	
	@Override
	public void setDao(BaseDao<AppThirdPayConfig, Long> dao) {
		super.dao=dao;
		
	}

	
	@Override
	public List<AppThirdPayConfig> findType() {
	
		return ((AppThirdPayConfigDao)dao).findType();
	}


	
	@Override
	public AppThirdPayConfig findCurrentType() {
		// TODO Auto-generated method stub
	    return null;
	}

}
