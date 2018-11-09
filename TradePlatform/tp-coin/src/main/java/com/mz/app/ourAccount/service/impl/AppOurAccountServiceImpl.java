package com.mz.app.ourAccount.service.impl;

import com.mz.app.ourAccount.model.AppOurAccount;
import com.mz.app.ourAccount.service.AppOurAccountService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * <p> TODO</p>
 * @author:         shangxlS
 * @Date :          2017年11月7日 下午6:14:34
 */
@Service("appOurAccountService")
public class AppOurAccountServiceImpl extends BaseServiceImpl<AppOurAccount, Long> implements AppOurAccountService{

	
	@Resource(name="appOurAccountDao")
	@Override
	public void setDao(BaseDao<AppOurAccount, Long> dao) {
		super.dao = dao;
	}

	@Override
	public AppOurAccount getAccountByfilter(String accountType,String currencyType,String openAccountType) {
		QueryFilter filter=new QueryFilter(AppOurAccount.class);
		filter.addFilter("accountType=", accountType);
		filter.addFilter("currencyType=", currencyType);
		filter.addFilter("openAccountType=",openAccountType);
		filter.addFilter("isShow=", "1");
		AppOurAccount ourAccount=this.get(filter);
		if(ourAccount!=null){
			return ourAccount;
		}
		return null;
	}
	
}