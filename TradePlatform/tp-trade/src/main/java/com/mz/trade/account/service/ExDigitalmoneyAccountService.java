/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:10:02
 */
package com.mz.trade.account.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.trade.redis.model.ExDigitalmoneyAccountRedis;
import java.math.BigDecimal;

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
	
		
		public ExDigitalmoneyAccountRedis getExDigitalmoneyAccountByRedis(String id);
		
		public void setExDigitalmoneyAccounttoRedis(ExDigitalmoneyAccountRedis exDigitalmoneyAccount);
		
		public ExDmColdAccountRecord createColdRecord(Integer recordType,ExDigitalmoneyAccount exDigitalmoneyAccount, BigDecimal freezeMoney,String transactionNum,Integer remark);
		
		public ExDmHotAccountRecord createHotRecord(Integer recordType,ExDigitalmoneyAccount account, BigDecimal freezeMoney,String transactionNum,Integer remark);
}
