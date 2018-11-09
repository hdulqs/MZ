/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      gaomimi
 * @version:     V1.0 
 * @Date:        2017-07-06 19:40:34 
 */
package com.mz.exchange.product.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.exchange.product.service.ExCointoCoinService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> ExCointoCoinService </p>
 * @author:         gaomimi
 * @Date :          2017-07-06 19:40:34  
 */
@Service("exCointoCoinService")
public class ExCointoCoinServiceImpl extends BaseServiceImpl<ExCointoCoin, Long> implements ExCointoCoinService{
	
	
	@Resource(name="exCointoCoinDao")
	@Override
	public void setDao(BaseDao<ExCointoCoin, Long> dao) {
		super.dao = dao;
	}
	
	
}
