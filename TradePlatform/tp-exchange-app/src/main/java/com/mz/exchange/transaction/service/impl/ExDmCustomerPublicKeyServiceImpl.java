/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午7:05:06
 */
package com.mz.exchange.transaction.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.transaction.model.ExDmCustomerPublicKey;
import com.mz.exchange.transaction.service.ExDmCustomerPublicKeyService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午7:05:06
 */
@Service("exDmCustomerPublicKeyService")
public class ExDmCustomerPublicKeyServiceImpl extends
		BaseServiceImpl<ExDmCustomerPublicKey, Long> implements
		ExDmCustomerPublicKeyService {

	@Resource(name = "exDmCustomerPublicKeyDao")
	@Override
	public void setDao(BaseDao<ExDmCustomerPublicKey, Long> dao) {
		super.dao = dao;
	}

}
