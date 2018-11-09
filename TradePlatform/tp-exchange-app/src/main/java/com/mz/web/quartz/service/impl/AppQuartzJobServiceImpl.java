/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2016-11-10 20:31:53 
 */
package com.mz.web.quartz.service.impl;

import com.mz.web.quartz.model.AppQuartzJob;
import com.mz.web.quartz.service.AppQuartzJobService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppQuartzJobService </p>
 * @author:         liushilei
 * @Date :          2016-11-10 20:31:53  
 */
@Service("appQuartzJobService")
public class AppQuartzJobServiceImpl extends BaseServiceImpl<AppQuartzJob, Long> implements AppQuartzJobService{
	
	@Resource(name="appQuartzJobDao")
	@Override
	public void setDao(BaseDao<AppQuartzJob, Long> dao) {
		super.dao = dao;
	}
	

}
