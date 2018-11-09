/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:35:56
 */
package com.mz.trade.account.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.util.QueryFilter;
import com.mz.trade.account.service.ExDmColdAccountRecordService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:35:56
 */
@Service("exDmColdAccountRecordService")
public class ExDmColdAccountRecordServiceImpl extends
		BaseServiceImpl<ExDmColdAccountRecord, Long> implements
		ExDmColdAccountRecordService {

	@Resource(name = "exDmColdAccountRecordDao")
	@Override
	public void setDao(BaseDao<ExDmColdAccountRecord, Long> dao) {
		super.dao = dao;
	}

	@Override
	public List<ExDmColdAccountRecord> findColdAccountRecordBytransactionNum(
			String transactionNum) {
		QueryFilter filter = new QueryFilter(ExDmHotAccountRecord.class);
		filter.addFilter("transactionNum=", transactionNum);
		List<ExDmColdAccountRecord> list = super.find(filter);
		return list;
	}

	

}
