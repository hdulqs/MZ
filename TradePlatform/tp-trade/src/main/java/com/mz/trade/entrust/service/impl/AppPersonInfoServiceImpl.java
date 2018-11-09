/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.trade.entrust.service.impl;



import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.trade.entrust.service.AppPersonInfoService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Gao Mimi
 * @Date : 2016年4月12日 下午4:45:50
 */
@Service("appPersonInfoService")
public class AppPersonInfoServiceImpl extends BaseServiceImpl<AppPersonInfo, Long> implements AppPersonInfoService {

	@Resource(name = "appPersonInfoDao")
	@Override
	public void setDao(BaseDao<AppPersonInfo, Long> dao) {
		super.dao = dao;
	}

}
