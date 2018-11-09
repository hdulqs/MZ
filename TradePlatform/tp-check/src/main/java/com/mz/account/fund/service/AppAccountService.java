/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:52:11
 */
package com.mz.account.fund.service;

import com.mz.account.fund.model.AppAccount;
import com.mz.core.mvc.service.base.BaseService;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:52:11 
 */
public interface AppAccountService extends BaseService<AppAccount, Long>{
	/**
	 * 
	 * <p> 并发保存</p>
	 * @author:         Gao Mimi
	 * @param:    @param account
	 * @param:    @return
	 * @return: String[] 
	 * @Date :          2016年5月12日 下午7:00:48   
	 * @throws:
	 */
	public String[] updateAccount(AppAccount appAccount);
	
	public AppAccount getByCustomerIdAndType(Long customerId,String currencyType,String website);
}
