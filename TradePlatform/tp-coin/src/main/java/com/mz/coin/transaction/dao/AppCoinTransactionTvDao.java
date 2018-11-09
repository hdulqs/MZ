/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-12-04 17:19:36 
 */
package com.mz.coin.transaction.dao;

import java.util.List;

import com.mz.coin.transaction.model.AppCoinTransactionTv;
import com.mz.core.mvc.dao.base.BaseDao;


/**
 * <p> AppCoinTransactionTvDao </p>
 * @author:         shangxl
 * @Date :          2017-12-04 17:19:36  
 */
public interface AppCoinTransactionTvDao extends BaseDao<AppCoinTransactionTv, Long> {
	/**
	 * 根据trxid查询记录条数
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param trxid
	 * @param:    @return
	 * @return: int 
	 * @Date :          2017年3月13日 下午8:12:44   
	 * @throws:
	 */
	public int getcountBytrxid(String trxid);

	/**
	 * 查询昨日充币所有账户
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2017年3月13日 下午8:13:00   
	 * @throws:
	 */
	public List<String> listYesterdayRechargeRecord(String coinType);
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param isconfirmed
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年4月11日 下午1:55:06   
	 * @throws:
	 */
	List<String> listTxIdByIsUse(int isUse);
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param isconfirmed
	 * @param:    @return
	 * @return: List<AppCoinTransactionTv> 
	 * @Date :          2018年4月11日 下午3:43:19   
	 * @throws:
	 */
	List<AppCoinTransactionTv> listTxByIsUse(int isUse);
}
