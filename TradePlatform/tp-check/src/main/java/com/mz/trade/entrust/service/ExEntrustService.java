/**

 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
package com.mz.trade.entrust.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.trade.entrust.model.ExEntrust;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
public interface ExEntrustService extends BaseService<ExEntrust, Long> {
	  public  void  checkExEntrust();
	
}
