/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-31 10:05:11 
 */
package com.mz.web.open.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.web.open.model.SysOpenUser;
import com.mz.web.open.service.SysOpenUserService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> SysOpenUserService </p>
 * @author:         shangxl
 * @Date :          2017-07-31 10:05:11  
 */
@Service("sysOpenUserService")
public class SysOpenUserServiceImpl extends BaseServiceImpl<SysOpenUser, Long> implements SysOpenUserService{
	
	@Resource(name="sysOpenUserDao")
	@Override
	public void setDao(BaseDao<SysOpenUser, Long> dao) {
		super.dao = dao;
	}
	

}
