/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.exchange.entrust.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.trade.entrust.model.ExEntrustPlan;
import com.mz.exchange.entrust.service.ExExEntrustPlanService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
@Service("exExEntrustPlanService")
public class ExExEntrustPlanServiceImpl extends BaseServiceImpl<ExEntrustPlan, Long>
		implements ExExEntrustPlanService {

	@Resource(name = "exExEntrustPlanDao")
	@Override
	public void setDao(BaseDao<ExEntrustPlan, Long> dao) {
		super.dao = dao;
	}

}
