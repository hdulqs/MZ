/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-09-18 15:00:44 
 */
package com.mz.coin.coin.dao;

import java.math.BigInteger;
import java.util.List;

import com.mz.coin.coin.model.AppCoinTransaction;
import com.mz.core.mvc.dao.base.BaseDao;


/**
 * <p> AppCoinTransactionDao </p>
 * @author:         shangxl
 * @Date :          2017-09-18 15:00:44  
 */
public interface AppCoinTransactionDao extends  BaseDao<AppCoinTransaction, Long> {

	/**
	 * 消费充币记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: List<AppCoinTransaction> 
	 * @Date :          2017年3月13日 下午8:09:14   
	 * @throws:
	 */
	public List<AppCoinTransaction> consumeTx();
	
	/**
	 * blockhash是否保存数据库
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param blockhash
	 * @param:    @return
	 * @return: int 
	 * @Date :          2017年3月13日 下午8:09:48   
	 * @throws:
	 */
	public int  existNumber(String blockhash);
	/**
	 * 获取数据库最新的blockNumber
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: BigInteger 
	 * @Date :          2017年3月13日 下午8:10:18   
	 * @throws:
	 */
	public BigInteger getLastestBlock();
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinCode
	 * @param:    @return
	 * @return: BigInteger 
	 * @Date :          2017年3月13日 下午8:10:40   
	 * @throws:
	 */
	public BigInteger getLastestBlockByCoinCode(String coinCode);
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<AppCoinTransaction> 
	 * @Date :          2017年3月13日 下午8:10:58   
	 * @throws:
	 */
	public List<AppCoinTransaction> listYesterdayRechargeRecord(String coinType);
	
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param hour
	 * @param:    @param amount
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年3月26日 下午3:30:12   
	 * @throws:
	 */
	public List<String> listTokenAddressByCondition(int hour, int amount);
	
	/**
	 *
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<String>
	 * @Date :          2018年3月29日 上午9:49:23
	 * @throws:
	 */
	List<String> listAddressGroupByAddress(String coinType);
}
