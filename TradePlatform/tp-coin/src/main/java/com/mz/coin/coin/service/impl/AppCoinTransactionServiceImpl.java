/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-09-18 15:00:44 
 */
package com.mz.coin.coin.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mz.coin.coin.dao.AppCoinTransactionDao;
import com.mz.coin.coin.model.AppCoinTransaction;
import com.mz.coin.coin.service.AppCoinTransactionService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;

/**
 * <p> AppCoinTransactionService </p>
 * @author:         shangxl
 * @Date :          2017-09-18 15:00:44  
 */
@Service("appCoinTransactionService")
public class AppCoinTransactionServiceImpl extends BaseServiceImpl<AppCoinTransaction, Long> implements AppCoinTransactionService{
	
	@Resource(name="appCoinTransactionDao")
	@Override
	public void setDao(BaseDao<AppCoinTransaction, Long> dao) {
		super.dao = dao;
	}

	@Override
	public List<AppCoinTransaction> consumeTx() {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionDao)dao).consumeTx();
	}

	@Override
	public int existNumber(String hash) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionDao)dao).existNumber(hash);
	}
	
	@Override
	public BigInteger getLastestBlock(){
		return ((AppCoinTransactionDao)dao).getLastestBlock();
	}
	
	
	@Override
	public BigInteger getLastestBlockByCoinCode(String coinCode){
		return ((AppCoinTransactionDao)dao).getLastestBlockByCoinCode(coinCode);
	}

	@Override
	public List<AppCoinTransaction> listYesterdayRechargeRecord(String coinType) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionDao)dao).listYesterdayRechargeRecord(coinType);
	}

	@Override
	public List<String> listTokenAddressByCondition(int hour, int amount) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionDao)dao).listTokenAddressByCondition(hour,amount);
	}

	@Override
	public List<String> listAddressGroupByAddress(String coinType) {
		// TODO Auto-generated method stub
		return ((AppCoinTransactionDao)dao).listAddressGroupByAddress(coinType);
	}
}