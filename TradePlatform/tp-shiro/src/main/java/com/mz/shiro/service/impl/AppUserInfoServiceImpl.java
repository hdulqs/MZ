/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月3日 下午2:27:21
 */
package com.mz.shiro.service.impl;

import com.mz.shiro.service.AppUserInfoService;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.oauth.user.model.AppUserInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月3日 下午2:27:21 
 */
@Service("appUserInfoService")
public class AppUserInfoServiceImpl  extends BaseServiceImpl<AppUserInfo, Long>  implements
    AppUserInfoService {

	@Resource(name = "appUserInfoDao")
	@Override
	public void setDao(BaseDao<AppUserInfo, Long> dao) {
		super.dao = dao;
	}

}
