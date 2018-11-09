/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.exchange.lend.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.lend.model.ExDmLendIntent;
import com.mz.exchange.lend.service.ExDmLendIntentService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
@Service("exDmLendIntentService")
public class ExDmLendIntentServiceImpl extends BaseServiceImpl<ExDmLendIntent, Long>
		implements ExDmLendIntentService {

	@Resource(name = "exDmLendIntentDao")
	@Override
	public void setDao(BaseDao<ExDmLendIntent, Long> dao) {
		super.dao = dao;
	}


	
}
