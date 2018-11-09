/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:10:02
 */
package com.mz.exchange.account.service;

import com.mz.account.fund.model.FeeWithdrawalsRecord;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;

public interface FeeWithdrawalsRecordService extends BaseService<FeeWithdrawalsRecord, Long> {

	PageResult findPageBySql(QueryFilter filter);
	
}
