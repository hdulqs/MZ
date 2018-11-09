/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-12-04 17:19:36 
 */
package com.mz.coin.transaction.service.impl;

import com.mz.coin.transaction.dao.AppCoinTransactionTvDao;
import com.mz.coin.transaction.model.AppCoinTransactionTv;
import com.mz.coin.transaction.service.AppCoinTransactionTvService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppCoinTransactionTvService </p>
 * @author:         shangxl
 * @Date :          2017-12-04 17:19:36  
 */
@Service("appCoinTransactionTvService")
public class AppCoinTransactionTvServiceImpl extends BaseServiceImpl<AppCoinTransactionTv, Long> implements AppCoinTransactionTvService{
	
	@Resource(name="appCoinTransactionTvDao")
	@Override
	public void setDao(BaseDao<AppCoinTransactionTv, Long> dao) {
		super.dao = dao;
	}

	@Override
	public boolean notload(String trxid) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionTvDao)dao).getcountBytrxid(trxid)==0;
	}

	@Override
	public List<String> listYesterdayRechargeRecord(String type) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionTvDao)dao).listYesterdayRechargeRecord(type);
	}

	@Override
	public List<String> listTxIdByIsUse(int isUse) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionTvDao)dao).listTxIdByIsUse(isUse);
	}

	@Override
	public List<AppCoinTransactionTv> listTxByIsUse(int isUse) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionTvDao)dao).listTxByIsUse(isUse);
	}
	

}
