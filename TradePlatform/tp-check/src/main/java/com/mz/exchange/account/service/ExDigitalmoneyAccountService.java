/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:10:02
 */
package com.mz.exchange.account.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:10:02
 */
public interface ExDigitalmoneyAccountService extends
		BaseService<ExDigitalmoneyAccount, Long> {
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
	public String[] updateAccount(ExDigitalmoneyAccount account);
	
	public ExDigitalmoneyAccount getByCustomerIdAndType(Long customerId,String coinCode,String currencyType,String website); 
	
}
