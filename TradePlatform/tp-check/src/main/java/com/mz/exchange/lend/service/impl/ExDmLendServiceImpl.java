/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.exchange.lend.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.lend.model.ExDmLend;
import com.mz.exchange.lend.service.ExDmLendService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
@Service("exDmLendService")
public class ExDmLendServiceImpl extends BaseServiceImpl<ExDmLend, Long>
		implements ExDmLendService {

	@Resource(name = "exDmLendDao")
	@Override
	public void setDao(BaseDao<ExDmLend, Long> dao) {
		super.dao = dao;
	}
}	
