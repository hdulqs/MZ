/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午1:45:20
 */
package com.mz.trade.entrust.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.trade.entrust.model.ExOrderInfo;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年4月12日 下午4:45:50 
 */
public interface ExOrderInfoService extends BaseService<ExOrderInfo, Long> {
	 public void removeEntrustRobt();
}
