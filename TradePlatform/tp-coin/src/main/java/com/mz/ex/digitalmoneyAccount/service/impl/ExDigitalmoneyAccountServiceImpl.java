/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 10:22:22 
 */
package com.mz.ex.digitalmoneyAccount.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ex.digitalmoneyAccount.dao.ExDigitalmoneyAccountDao;
import com.mz.ex.digitalmoneyAccount.model.ExDigitalmoneyAccount;
import com.mz.ex.digitalmoneyAccount.service.ExDigitalmoneyAccountService;
import com.mz.util.QueryFilter;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> ExDigitalmoneyAccountService </p>
 * @author:         shangxl
 * @Date :          2017-11-08 10:22:22  
 */
@Service("exDigitalmoneyAccountService")
public class ExDigitalmoneyAccountServiceImpl extends BaseServiceImpl<ExDigitalmoneyAccount, Long> implements ExDigitalmoneyAccountService{
	
	@Resource(name="exDigitalmoneyAccountDao")
	@Override
	public void setDao(BaseDao<ExDigitalmoneyAccount, Long> dao) {
		super.dao = dao;
	}

	@Override
	public ExDigitalmoneyAccount getExDigitalmoneyAccountByPublicKey(String publicKey) {
		QueryFilter filter=new QueryFilter(ExDigitalmoneyAccount.class);
		filter.addFilter("publicKey=", publicKey);
		ExDigitalmoneyAccount coinAccount=this.get(filter);
		if(coinAccount!=null){
			return coinAccount;
		}
		return null;
	}

	@Override
	public boolean isexist(String accountNum, String coinType) {
		return ((ExDigitalmoneyAccountDao)dao).count(accountNum, coinType)>0;
	}

	@Override
	public List<String> listAccountNumByCoinCode(String coinCode) {
		// TODO Auto-generated method stub
		return ((ExDigitalmoneyAccountDao)dao).listAccountNumByCoinCode(coinCode);
	}

	@Override
	public List<String> listPublicKeyByCoinCode(String coinCode) {
		// TODO Auto-generated method stub
		return ((ExDigitalmoneyAccountDao)dao).listPublicKeyByCoinCode(coinCode);
	}

	@Override
	public String getEthPublicKeyByparams(String userName) {
		// TODO Auto-generated method stub
		return ((ExDigitalmoneyAccountDao)dao).getEthPublicKeyByparams(userName);
	}

	@Override
	public List<String> listEtherpublickey() {
		return ((ExDigitalmoneyAccountDao)dao).listEtherpublickey();
	}

	@Override
	public ExDigitalmoneyAccount getAccountByAccountNumber(String accountNumber) {
		// TODO Auto-generated method stub
		return ((ExDigitalmoneyAccountDao)dao).getAccountByAccountNumber(accountNumber);
	}
	
}
