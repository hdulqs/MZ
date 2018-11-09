/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.exchange.product.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.product.service.ExProductService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:04:29
 */
@Service("exProductService")
public class ExProductServiceImpl extends BaseServiceImpl<ExProduct, Long> implements ExProductService {


	@Resource(name = "exProductDao")
	@Override
	public void setDao(BaseDao<ExProduct, Long> dao) {
		super.dao = dao;
	}

	

}
