/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月9日 下午7:27:20
 */
package com.mz.shiro.service.impl;

import com.mz.shiro.service.AppRoleMenuTreeService;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.oauth.user.model.AppRoleMenuTree;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月9日 下午7:27:20 
 */
@Service("appRoleMenuTreeService")
public class AppRoleMenuTreeServiceImpl extends BaseServiceImpl<AppRoleMenuTree, Long>  implements
		AppRoleMenuTreeService {
	
	@Resource(name = "appRoleMenuTreeDao")
	@Override
	public void setDao(BaseDao<AppRoleMenuTree, Long> dao) {
		super.dao = dao;
	}

}

	