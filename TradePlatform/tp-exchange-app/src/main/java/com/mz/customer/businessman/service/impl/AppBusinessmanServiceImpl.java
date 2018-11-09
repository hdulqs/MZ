/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-06 18:43:30 
 */
package com.mz.customer.businessman.service.impl;

import com.mz.customer.businessman.model.AppBusinessman;
import com.mz.customer.businessman.service.AppBusinessmanService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppBusinessmanService </p>
 * @author:         liushilei
 * @Date :          2017-12-06 18:43:30  
 */
@Service("appBusinessmanService")
public class AppBusinessmanServiceImpl extends BaseServiceImpl<AppBusinessman, Long> implements AppBusinessmanService{
	
	@Resource(name="appBusinessmanDao")
	@Override
	public void setDao(BaseDao<AppBusinessman, Long> dao) {
		super.dao = dao;
	}
	

}
