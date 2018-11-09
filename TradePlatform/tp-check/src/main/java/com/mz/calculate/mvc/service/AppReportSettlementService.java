/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2016年8月23日 下午9:13:09
 */
package com.mz.calculate.mvc.service;

import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.core.mvc.service.base.BaseService;
import java.util.List;
import java.util.Map;

/**
 * @author Wu shuiming
 * @date 2016年8月23日 下午9:13:09
 */
public interface AppReportSettlementService extends BaseService<AppReportSettlement, Long>{
	//start核算
	 public List<Map<String,Object>>  culAccountByCustomersErrorInfosureold(String[] ids,Boolean iserrorright);
	 public void culAccountByCustomerssureold(String[] ids);
	 
	 public List<Map<String,Object>>   culRedisAndSqlAccountByCustomer(String[] ids,Boolean iserrorright);
	public void  culRedisAndSqlToSqlAccountByCustomer(String[] ids);

	//start核算
	
}
