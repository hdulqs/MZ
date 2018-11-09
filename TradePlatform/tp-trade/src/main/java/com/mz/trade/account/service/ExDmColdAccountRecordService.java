/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:20:35
 */
package com.mz.trade.account.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:20:35
 */
public interface ExDmColdAccountRecordService extends
		BaseService<ExDmColdAccountRecord, Long> {
	
	public List<ExDmColdAccountRecord> findColdAccountRecordBytransactionNum(String transactionNum);
	
	

}
