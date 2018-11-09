/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月13日 上午10:23:31
 */
package com.mz.core.mvc.service.menu.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.core.mvc.service.menu.CoreAppRoleMenuTreeService;
import com.mz.oauth.user.model.AppRoleMenuTree;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月13日 上午10:23:31 
 */
@Service
public class CoreAppRoleMenuTreeServiceImpl extends BaseServiceImpl<AppRoleMenuTree, Long>  implements CoreAppRoleMenuTreeService{

	@Resource(name = "coreAppRoleMenuTreeDao")
	@Override
	public void setDao(BaseDao<AppRoleMenuTree, Long> dao) {
		super.dao = dao;
	}
	
	
	@Override
	public void init(String appName,String appKey) {}



}
