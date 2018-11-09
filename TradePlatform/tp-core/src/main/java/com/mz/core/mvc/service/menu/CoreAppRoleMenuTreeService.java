/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月13日 上午10:22:53
 */
package com.mz.core.mvc.service.menu;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppRoleMenuTree;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月13日 上午10:22:53 
 */
public interface CoreAppRoleMenuTreeService extends BaseService<AppRoleMenuTree, Long> {
	
	/**
	 * <p> TODO</p>     初始化菜单
	 * @author:         Liu Shilei
	 * @param:    appName   应用系统名称
	 * @return: void 
	 * @Date :          2015年10月14日 下午5:19:46   
	 * @throws:
	 */
	void init(String appName,String appKey);


}
