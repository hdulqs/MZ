/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月1日 上午11:17:16
 */
package com.mz.remote.settlement;

import java.util.List;
import java.util.Map;


/**
 * <p> TODO</p>
 */
public interface RemoteAppReportSettlementCheckService {
	
	//全部核算到redis
	public List<Map<String,Object>>  culSureOldAccountAllCustomerErrorInfo(Integer days);
	//有错的全部核算到数据库
	public void culAccountAllCustomer();
	//单个客户核算查看
	public List<Map<String, Object>> culAccountByCustomersErrorInfosureold(String[] ids,Boolean iserrorright);
	//单个客户核算到数据库
	public void culAccountByCustomerssureold(String[] ids);
	
	//全部缓存和数据库核算到redis
	public List<Map<String,Object>>  culRedisAndSqlSureOldAccountAllCustomerErrorInfo(Integer days);
	//单个客户缓存和数据库查看
	public List<Map<String, Object>> culRedisAndSqlAccountByCustomer(String[] ids,Boolean iserrorright);
	//单个客户缓存和数据库  更新数据库
	public void culRedisAndSqlToSqlAccountByCustomer(String[] ids);
	//主动移动机器人的数据
	public void  removeEntrustRobt();

}
