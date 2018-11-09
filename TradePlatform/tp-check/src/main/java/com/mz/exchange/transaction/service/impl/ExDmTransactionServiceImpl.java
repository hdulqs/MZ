/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午7:00:10
 */
package com.mz.exchange.transaction.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午7:00:10
 */
@Service("exDmTransactionService")
public class ExDmTransactionServiceImpl extends
		BaseServiceImpl<ExDmTransaction, Long> implements
		ExDmTransactionService {
	
	@Resource(name = "exDmTransactionDao")
	@Override
	public void setDao(BaseDao<ExDmTransaction, Long> dao) {
		super.dao = dao;
	}

	
		
		
}




