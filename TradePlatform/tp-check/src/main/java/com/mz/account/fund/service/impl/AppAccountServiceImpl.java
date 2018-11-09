/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:52:11
 */
package com.mz.account.fund.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.mz.account.fund.model.AppAccount;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.util.log.LogFactory;
import com.mz.account.fund.dao.AppAccountDao;
import com.mz.account.fund.service.AppAccountService;
import com.mz.core.constant.CodeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:52:11 
 */
@Service("appAccountService")
public class AppAccountServiceImpl extends BaseServiceImpl<AppAccount, Long> implements AppAccountService{
	
	/*@Resource(name="appAccountDao")*/
	@Override
	public void setDao(BaseDao<AppAccount, Long> dao) {
		super.dao = dao;
	}

	@Autowired
	AppAccountDao appAccountDao;

	
	@Override
	public String[] updateAccount(AppAccount account){
		String[] relt=new String[2];  
		//try{
			long start1 = System.currentTimeMillis();
			this.update(account);
			long end1 = System.currentTimeMillis();
			LogFactory.info("更新账户a：" + (end1 - start1) + "毫秒");
		    relt[0]=CodeConstant.CODE_SUCCESS;
			relt[1]="成功";
	//	}catch(Exception e){
			
		//	relt[0]=CodeConstant.CODE_FAILED;
		//	relt[1]="失败";
		//}
		return relt;
	}
	
	@Override
	public AppAccount getByCustomerIdAndType(Long customerId,String currencyType,String website) {
		QueryFilter filter = new QueryFilter(AppAccount.class);
		filter.addFilter("customerId=", customerId);
		if(!StringUtil.isEmpty(currencyType)){
			filter.addFilter("currencyType=", currencyType);
		}
		if(!StringUtil.isEmpty(website)){
			filter.addFilter("website=", website);
		}
	
		
		AppAccount account = this.get(filter);
		return account;
	}



}
