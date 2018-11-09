/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-28 15:16:24 
 */
package com.mz.app.log.service;

import com.mz.app.log.model.AppLogThirdInterface;
import com.mz.core.mvc.service.base.BaseService;



/**
 * <p> AppLogThirdInterfaceService </p>
 * @author:         shangxl
 * @Date :          2017-07-28 15:16:24  
 */
public interface AppLogThirdInterfaceService extends BaseService<AppLogThirdInterface, Long> {
	
	/**
	 * 保存第三方调用接口
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Shangxl
	 * @param: @param appLogThirdInterface
	 * @return: void
	 * @Date : 2017年7月28日 下午3:56:28
	 * @throws:
	 */
	public void saveThirdInterfaceLog(AppLogThirdInterface appLogThirdInterface);

}
