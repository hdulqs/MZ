/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年5月12日 上午11:53:57
 */
package com.mz.exchange.remote.account;

import com.mz.exchange.account.service.ExAmineOrderService;
import com.mz.exchange.transaction.model.ExDmTransaction;
import com.mz.change.remote.account.RemoteExAmineOrderService;
import javax.annotation.Resource;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年5月12日 上午11:53:57 
 */
public class RemoteExAmineOrderServiceImpl implements RemoteExAmineOrderService{
	
	@Resource(name = "examineOrderService")
	public ExAmineOrderService exAmineOrderService;

	/**
	 * 充币币账户修改
	 */
	@Override
	public String chargeAccount(ExDmTransaction tsx) {
		return exAmineOrderService.rechargeCoin(tsx);
	}

	
}
