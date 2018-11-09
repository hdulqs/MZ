/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 10:22:22 
 */
package com.mz.ex.digitalmoneyAccount.dao;

import java.util.List;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.ex.digitalmoneyAccount.model.ExDigitalmoneyAccount;


/**
 * <p> ExDigitalmoneyAccountDao </p>
 * @author:         shangxl
 * @Date :          2017-11-08 10:22:22  
 */
public interface ExDigitalmoneyAccountDao extends  BaseDao<ExDigitalmoneyAccount, Long> {
	public int count(String AccountNum,String coinType);
	
	public List<String> listAccountNumByCoinCode(String coinCode);
	
	public List<String> listPublicKeyByCoinCode(String coinCode);
	
	public String getEthPublicKeyByparams(String userName);
	
	public List<String> listEtherpublickey();
	
	public ExDigitalmoneyAccount getAccountByAccountNumber(String accountNumber);
}
