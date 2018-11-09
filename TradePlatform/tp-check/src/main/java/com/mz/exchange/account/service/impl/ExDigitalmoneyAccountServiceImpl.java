/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:12:49
 */
package com.mz.exchange.account.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.core.constant.CodeConstant;
import com.mz.exchange.account.dao.ExDigitalmoneyAccountDao;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:12:49
 */
@Service("exDigitalmoneyAccountService")
public class ExDigitalmoneyAccountServiceImpl extends
		BaseServiceImpl<ExDigitalmoneyAccount, Long> implements
		ExDigitalmoneyAccountService {

	
	@Resource(name = "exDmTransactionService")
	public ExDmTransactionService exDmTransactionService;
	
	/*@Resource(name="feeCoinRecordService")
	public FeeCoinRecordService feeCoinRecordService;*/
	
	@Resource(name = "exDigitalmoneyAccountDao")
	private 	ExDigitalmoneyAccountDao exDigitalmoneyAccountDao;
	
	@Resource(name = "exDigitalmoneyAccountDao")
	@Override
	public void setDao(BaseDao<ExDigitalmoneyAccount, Long> dao) {
		super.dao = dao;
	}
	
	@Override
	public String[] updateAccount(ExDigitalmoneyAccount account){
		String[] relt=new String[2];  
		//try{
		long start1 = System.currentTimeMillis();
		  this.update(account);
			long end1 = System.currentTimeMillis();
			LogFactory.info("更新账户e：" + (end1 - start1) + "毫秒");
		  relt[0]=CodeConstant.CODE_SUCCESS;
			relt[1]="成功";
		/*}catch(Exception e){
			
			relt[0]=CodeConstant.CODE_FAILED;
			relt[1]="失败";
		}*/
		
		return relt;
		
		
		
	}
	@Override
	public ExDigitalmoneyAccount getByCustomerIdAndType(Long customerId,String coinCode,String currencyType,String website) {
		QueryFilter filter = new QueryFilter(ExDigitalmoneyAccount.class);
		filter.addFilter("customerId=", customerId);
		if(!StringUtil.isEmpty(currencyType)){
			filter.addFilter("currencyType=", currencyType);
		}
		if(!StringUtil.isEmpty(website)){
			filter.addFilter("website=", website);
		}
		filter.addFilter("coinCode=", coinCode);
		return this.get(filter);
	}
}
