/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-12-04 17:19:36 
 */
package com.mz.coin.transaction.service;

import java.util.List;

import com.mz.coin.transaction.model.AppCoinTransactionTv;
import com.mz.core.mvc.service.base.BaseService;



/**
 * <p> AppCoinTransactionTvService </p>
 * @author:         shangxl
 * @Date :          2017-12-04 17:19:36  
 */
public interface AppCoinTransactionTvService  extends BaseService<AppCoinTransactionTv, Long>{
	
	/**
	 * 判断没有保存数据库
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param trxid
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年12月4日 下午6:45:04   
	 * @throws:
	 */
	public boolean notload(String trxid);
	
	/**
	 * 获得昨日充币记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param type
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2017年12月5日 下午7:00:21   
	 * @throws:
	 */
	public List<String> listYesterdayRechargeRecord(String type);
	
	
	/**
	 * 根据消费状态获取记录的交易txid list
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param isconfirmed
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年4月11日 下午1:53:01   
	 * @throws:
	 */
	List<String> listTxIdByIsUse(int isUse);
	
	/**
	 * 根据消费状态获取记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param isconfirmed
	 * @param:    @return
	 * @return: List<AppCoinTransactionTv> 
	 * @Date :          2018年4月11日 下午3:42:31   
	 * @throws:
	 */
	List<AppCoinTransactionTv> listTxByIsUse(int isUse);
	
}
