/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0
 * @Date:        2017-06-20 17:38:04
 */
package com.mz.shiro.service.impl;

import com.mz.shiro.service.AppLogLoginService;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.log.AppLogLogin;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppLogLoginService </p>
 * @author:         shangxl
 * @Date :          2017-06-20 17:38:04
 */
@Service("appLogLoginService")
public class AppLogLoginServiceImpl extends BaseServiceImpl<AppLogLogin, Long> implements
		AppLogLoginService {

	@Resource(name="appLogLoginDao")
	@Override
	public void setDao(BaseDao<AppLogLogin, Long> dao) {
		super.dao = dao;
	}

}
