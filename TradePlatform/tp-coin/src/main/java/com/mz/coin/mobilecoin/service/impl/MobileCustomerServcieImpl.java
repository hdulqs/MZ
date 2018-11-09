package com.mz.coin.mobilecoin.service.impl;

import com.mz.coin.mobilecoin.model.MobileCustomer;
import com.mz.coin.mobilecoin.service.MobileCustomerServcie;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
@Service("mobileCustomerServcie")
public class MobileCustomerServcieImpl extends BaseServiceImpl<MobileCustomer, Long> implements MobileCustomerServcie{

	@Resource(name = "mobileCustomerDao")
	@Override
	public void setDao(BaseDao<MobileCustomer, Long> dao) {
		super.dao = dao;
	}

}
