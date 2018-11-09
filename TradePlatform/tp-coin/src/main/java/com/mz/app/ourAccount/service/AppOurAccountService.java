package com.mz.app.ourAccount.service;

import com.mz.app.ourAccount.model.AppOurAccount;
import com.mz.core.mvc.service.base.BaseService;

/**
 * 
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年11月7日 下午6:09:37
 */
public interface AppOurAccountService extends BaseService<AppOurAccount, Long>{
	
	/**
	 * <p>
	 * ps:根据查询条件获取对象<br>
	 * accountType 0:人民币账户 1:币账户<br>
	 * coinCode 币编码<br>
	 * openAccountType 0:充币、1：提币
	 * </p>
	 * @author:         shangxl
	 * @param:    @param accountType
	 * @param:    @param coinCode
	 * @param:    @param openAccountType
	 * @param:    @param website
	 * @param:    @return
	 * @return: AppOurAccount 
	 * @Date :          2017年11月8日 上午9:31:08   
	 * @throws:
	 */
	public AppOurAccount getAccountByfilter(String accountType,String coinCode,String openAccountType);
}
