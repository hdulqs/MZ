/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月13日 上午10:22:53
 */
package com.mz.web.menu.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.menu.model.AppMenuCust;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月13日 上午10:22:53 
 */
public interface AppMenuCustService extends BaseService<AppMenuCust, Long> {

	/**
	 * <p>批量添加,并添加菜单下的权限</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年6月17日 上午10:22:02   
	 * @throws:
	 */
	JsonResult add(String ids);

	/**
	 * <p>批量删除，并删除菜单下的权限</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年6月17日 上午10:24:12   
	 * @throws:
	 */
	JsonResult remove(String ids);
	
	
	/**
	 * <p>批量设置菜单不显示</p>
	 * @author:         Liu Shilei
	 * @param:    @param ids
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2016年6月17日 上午10:24:12   
	 * @throws:
	 */
	JsonResult isVisible(String ids);
	
	
	
}
