/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午6:37:38
 */
package com.mz.trade.account.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.util.QueryFilter;
import com.mz.trade.account.service.ExDigitalmoneyAccountService;
import com.mz.trade.account.service.ExDmHotAccountRecordService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午6:37:38
 */
@Service("exDmHotAccountRecordService")
public class ExDmHotAccountRecordServiceImpl extends
		BaseServiceImpl<ExDmHotAccountRecord, Long> implements
		ExDmHotAccountRecordService {

	@Resource(name = "exDigitalmoneyAccountService")
	public ExDigitalmoneyAccountService exDigitalmoneyAccountService;

	@Resource(name = "exDmHotAccountRecordDao")
	@Override
	public void setDao(BaseDao<ExDmHotAccountRecord, Long> dao) {
		super.dao = dao;
	}

	@Override
	public ExDmHotAccountRecord findByAccountId(Long id) {
		ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService
				.get(id);
		String accountNum = exDigitalmoneyAccount.getAccountNum();

		return null;
	}

	@Override
	public List<ExDmHotAccountRecord> findHotAccountRecordBytransactionNum(
			String transactionNum) {
		QueryFilter filter = new QueryFilter(ExDmHotAccountRecord.class);
		filter.addFilter("transactionNum=", transactionNum);
		List<ExDmHotAccountRecord> list = super.find(filter);
		
		return list;
	}

}
