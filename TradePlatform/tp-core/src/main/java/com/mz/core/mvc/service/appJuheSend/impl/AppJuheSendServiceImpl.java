/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-06-20 16:54:45 
 */
package com.mz.core.mvc.service.appJuheSend.impl;


import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.appJuheSend.AppJuheSendService;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.sms.send.model.AppJuheSend;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppJuheSendService </p>
 * @author:         shangxl
 * @Date :          2017-06-20 16:54:45  
 */
@Service
public class AppJuheSendServiceImpl extends BaseServiceImpl<AppJuheSend, Long> implements AppJuheSendService{
	
	@Resource(name="appJuheSendDao")
	@Override
	public void setDao(BaseDao<AppJuheSend, Long> dao) {
		super.dao = dao;
	}
	

}
