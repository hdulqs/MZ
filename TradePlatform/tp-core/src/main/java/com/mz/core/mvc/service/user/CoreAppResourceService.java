/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午11:41:33
 */
package com.mz.core.mvc.service.user;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.user.model.AppResource;
import com.mz.web.menu.model.AppMenu;
import java.util.List;

/**
 * <p> TODO</p>     应用系统初始化权限
 * @author:         Liu Shilei 
 * @Date :          2015年9月21日 上午11:41:33 
 */
public interface CoreAppResourceService extends BaseService<AppResource, Long> {
	
	/**
	 * 初始化权限列表
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appName   应用系统名称
	 * @return: void 
	 * @Date :          2015年11月4日 下午1:40:23   
	 * @throws:
	 */
	void init(String appName,List<AppMenu> list);



	
}
