/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:52:11
 */
package com.mz.trade.account.service;

import com.mz.account.fund.model.AppAccount;
import com.mz.account.fund.model.AppColdAccountRecord;
import com.mz.account.fund.model.AppHotAccountRecord;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.trade.redis.model.AppAccountRedis;
import java.math.BigDecimal;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:52:11 
 */
public interface AppAccountService extends BaseService<AppAccount, Long>{
	public AppColdAccountRecord createColdRecord(Integer recordType,AppAccount account, BigDecimal freezeMoney,String transactionNum,Integer remark);
	public AppHotAccountRecord createHotRecord(Integer recordType,AppAccount account, BigDecimal freezeMoney,String transactionNum,Integer remark);

	public AppAccountRedis getAppAccountByRedis(String id);
	
	public void setAppAccounttoRedis(AppAccountRedis appAccount);
}
