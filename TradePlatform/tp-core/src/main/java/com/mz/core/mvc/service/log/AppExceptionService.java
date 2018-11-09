/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月17日 上午11:31:08
 */
package com.mz.core.mvc.service.log;

import com.mz.core.mvc.model.log.AppException;
import com.mz.core.mvc.service.base.BaseService;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月17日 上午11:31:08 
 */
public interface AppExceptionService extends BaseService<AppException, Long>  {
	
	
	public AppException selectOne1();

}
