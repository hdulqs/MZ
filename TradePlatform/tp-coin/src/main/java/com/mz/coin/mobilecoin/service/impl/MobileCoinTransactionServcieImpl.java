package com.mz.coin.mobilecoin.service.impl;

import com.mz.coin.mobilecoin.model.MobileCoinTransaction;
import com.mz.coin.mobilecoin.model.MobileCustomer;
import com.mz.coin.mobilecoin.service.MobileCoinTransactionServcie;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
@Service("mobileCoinTransactionServcie")
public class MobileCoinTransactionServcieImpl extends BaseServiceImpl<MobileCoinTransaction, Long> implements MobileCoinTransactionServcie{

	@Resource(name = "mobileCoinTransactionDao")
	@Override
	public void setDao(BaseDao<MobileCoinTransaction, Long> dao) {
		super.dao = dao;
	}

}
