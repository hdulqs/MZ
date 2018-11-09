/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.mvc.service.log.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.log.AppLog;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.log.AppLogService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * appLogServiceImpl
 * 
 * 
 *
 * 由代码生成器生成
 *
 * @author Yuan Zhicheng
 *
 */
@Service("appLogService")
public class AppLogServiceImpl extends BaseServiceImpl<AppLog, Long> implements AppLogService {

	@Resource(name = "appLogDao")
	@Override
	public void setDao(BaseDao<AppLog, Long> dao) {
		super.dao = dao;
	}

}
