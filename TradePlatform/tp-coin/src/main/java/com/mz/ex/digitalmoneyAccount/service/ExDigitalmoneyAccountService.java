/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 10:22:22 
 */
package com.mz.ex.digitalmoneyAccount.service;

import java.util.List;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.ex.digitalmoneyAccount.model.ExDigitalmoneyAccount;



/**
 * <p> ExDigitalmoneyAccountService </p>
 * @author:         shangxl
 * @Date :          2017-11-08 10:22:22  
 */
public interface ExDigitalmoneyAccountService  extends BaseService<ExDigitalmoneyAccount, Long>{
	/**
	 * 根据publickey（address）获取币账户
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param publickKey
	 * @param:    @return
	 * @return: ExDigitalmoneyAccount 
	 * @Date :          2017年11月8日 上午10:50:39   
	 * @throws:
	 */
	public ExDigitalmoneyAccount getExDigitalmoneyAccountByPublicKey(String publicKey);
	
	/**
	 * 根据AccountNum、coinType查询账号是否存在
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param accountNum
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年12月5日 下午2:23:32   
	 * @throws:
	 */
	public boolean isexist(String accountNum,String coinType);
	
	/**
	 * 根据coinCode查询AccountNum
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinCode
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2017年12月6日 上午9:54:19   
	 * @throws:
	 */
	public List<String> listAccountNumByCoinCode(String coinCode);
	/**
	 * 根据coinCode查询publicKey
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinCode
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2017年12月6日 上午9:55:01   
	 * @throws:
	 */
	public List<String> listPublicKeyByCoinCode(String coinCode);
	
	/**
	 * 根据用户名返回以太坊地址
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param userName
	 * @param:    @return
	 * @return: String 
	 * @Date :          2018年1月3日 下午6:32:51   
	 * @throws:
	 */
	public String  getEthPublicKeyByparams(String userName);
	
	/**
	 * 查询用户以太坊地址
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年1月29日 下午6:03:18   
	 * @throws:
	 */
	public List<String> listEtherpublickey();
	
	/**
	 * 根据account获取publickey
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param accountNumber
	 * @param:    @return
	 * @return: ExDigitalmoneyAccount 
	 * @Date :          2018年1月31日 上午9:43:39   
	 * @throws:
	 */
	public ExDigitalmoneyAccount getAccountByAccountNumber(String accountNumber);
}
