/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:52:11
 */
package com.mz.account.fund.service;

import com.mz.account.fund.model.AppTransaction;
import com.mz.core.mvc.service.base.BaseService;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:52:11 
 */
public interface AppTransactionService extends BaseService<AppTransaction, Long>{
	/**
	 * 
	 * <p>
	 * 根据用户id 交易类型 获取交易记录
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @return
	 * @return: List<AppTransaction>
	 * @Date : 2016年8月11日 上午11:16:04
	 * @throws:
	 */
	public List<AppTransaction> record(Long id, String type, String status, String beginDate, String endDate,
			String currencyType, String website);
}
