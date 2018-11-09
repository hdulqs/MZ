/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:52:11
 */
package com.mz.account.fund.service.impl;


import com.mz.account.fund.model.AppTransaction;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.account.fund.service.AppTransactionService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月31日 下午6:52:11 
 */
@Service("appTransactionService")
public class AppTransactionServiceImpl extends BaseServiceImpl<AppTransaction, Long> implements AppTransactionService{

	@Resource(name="appTransactionDao")
	@Override
	public void setDao(BaseDao<AppTransaction, Long> dao) {
		super.dao = dao;
	}
	
	@Override
	public List<AppTransaction> record(Long id, String type,String status,String beginDate,String endDate,String currencyType,String website) {
		
		QueryFilter filter = new QueryFilter(AppTransaction.class);
		filter.addFilter("customerId=",id);
		
		if(null!=website){
			filter.addFilter("website=",website);
		}
		if(null!=currencyType){
			filter.addFilter("currencyType=",currencyType);
		}
		if(null!=status){
			filter.addFilter("status_in", status);
		}else{
			filter.addFilter("status_in", "2");  //默认查成功状态
		}
		if(null!=type){
			filter.addFilter("transactionType_in",type);
		}
		
		if(null!=beginDate){
			filter.addFilter("modified>",beginDate);
		}
		if(null!=endDate){
			filter.addFilter("modified<",endDate);
		}
		
		
		List<AppTransaction> list = this.find(filter);
		return list;
	}
	

}
