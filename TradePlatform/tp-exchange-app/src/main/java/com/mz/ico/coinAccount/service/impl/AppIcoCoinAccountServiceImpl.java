/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-17 18:26:08 
 */
package com.mz.ico.coinAccount.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;
import com.mz.util.QueryFilter;
import com.mz.ico.coinAccount.service.AppIcoCoinAccountService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

/**
 * <p> AppIcoCoinAccountService </p>
 * @author:         shangxl
 * @Date :          2017-08-17 18:26:08  
 */
@Service("appIcoCoinAccountService")
public class AppIcoCoinAccountServiceImpl extends BaseServiceImpl<AppIcoCoinAccount, Long> implements AppIcoCoinAccountService{
	
	@Resource(name="appIcoCoinAccountDao")
	@Override
	public void setDao(BaseDao<AppIcoCoinAccount, Long> dao) {
		super.dao = dao;
	}
	
	@Override
	public AppIcoCoinAccount getByCustomerIdAndType(Long customerId,String coinCode,String currencyType,String website) {
		QueryFilter filter = new QueryFilter(AppIcoCoinAccount.class);
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
