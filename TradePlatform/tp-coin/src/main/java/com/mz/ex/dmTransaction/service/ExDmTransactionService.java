/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 11:13:05 
 */
package com.mz.ex.dmTransaction.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.ex.dmTransaction.model.ExDmTransaction;



/**
 * <p> ExDmTransactionService </p>
 * @author:         shangxl
 * @Date :          2017-11-08 11:13:05  
 */
public interface ExDmTransactionService  extends BaseService<ExDmTransaction, Long>{

	/**
	 * 根据单号查询交易记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param orderNo
	 * @param:    @return
	 * @return: ExDmTransaction 
	 * @Date :          2017年11月8日 上午11:27:03   
	 * @throws:
	 */
	public ExDmTransaction getExDmTransactionByOrderNo(String orderNo);
	
	/**
	 * 根据充币记录处理资产数据
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param exTxs
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年11月8日 上午11:36:26   
	 * @throws:
	 *//*
	public String rechargeCoin(ExDmTransaction exTxs);*/
}
