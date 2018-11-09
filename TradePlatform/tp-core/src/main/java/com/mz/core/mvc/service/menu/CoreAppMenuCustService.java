/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月13日 上午10:22:53
 */
package com.mz.core.mvc.service.menu;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.menu.model.AppMenu;
import com.mz.web.menu.model.AppMenuCust;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月13日 上午10:22:53 
 */
public interface CoreAppMenuCustService extends BaseService<AppMenuCust, Long> {

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param appMenuList
	 * @return: void 
	 * @Date :          2016年8月15日 下午6:21:20   
	 * @throws:
	 */
	void init(List<AppMenu> appMenuList,String appName);


}
