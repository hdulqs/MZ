/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-06-20 16:08:28 
 */
package com.mz.core.mvc.service.AppSmsSend.impl;


import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.AppSmsSend.AppSmsSendService;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.sms.send.model.AppSmsSend;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppSmsSendService </p>
 * @author:         shangxl
 * @Date :          2017-06-20 16:08:28  
 */
@Service("appSmsSendService")
public class AppSmsSendServiceImpl  extends BaseServiceImpl<AppSmsSend, Long> implements AppSmsSendService{

	@Resource(name="appSmsSendDao")
	@Override
	public void setDao(BaseDao<AppSmsSend, Long> dao) {
		super.dao = dao;
	}


}
