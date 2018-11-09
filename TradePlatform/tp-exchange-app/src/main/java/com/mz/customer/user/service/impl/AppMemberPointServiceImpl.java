
package com.mz.customer.user.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.user.model.AppMemberPoint;
import com.mz.customer.user.service.AppMemberPointService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("appMemberPointService")
public class AppMemberPointServiceImpl extends BaseServiceImpl<AppMemberPoint, Long> implements AppMemberPointService {
	
	@Resource(name = "appMemberPointDao")
	@Override
	public void setDao(BaseDao<AppMemberPoint, Long> dao) {
		super.dao = dao;
	}


}
