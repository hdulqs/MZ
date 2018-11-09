/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-09-18 15:00:44 
 */
package com.mz.coin.coin.service;

import java.math.BigInteger;
import java.util.List;

import com.mz.coin.coin.model.AppCoinTransaction;
import com.mz.core.mvc.service.base.BaseService;



/**
 * <p> AppCoinTransactionService </p>
 * @author:         shangxl
 * @Date :          2017-09-18 15:00:44  
 */
public interface AppCoinTransactionService extends BaseService<AppCoinTransaction, Long> {
	/**
	 * 根据coinType查询没有确认的交易记录
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<AppCoinTransaction> 
	 * @Date :          2017年9月18日 下午3:40:33   
	 * @throws:
	 */
	public List<AppCoinTransaction> consumeTx();
	
	/**
	 * 判断记录是否存在数据库
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param blockhash
	 * @param:    @return
	 * @return: int 
	 * @Date :          2017年9月18日 下午4:48:18   
	 * @throws:
	 */
	public int existNumber(String hash);
	
	/**
	 * （ETH及代币区块链相同）查询数据库保存的最新的交易记录blockNumber
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: BigInteger 
	 * @Date :          2017年11月3日 下午5:29:50   
	 * @throws:
	 */
	public BigInteger getLastestBlock();
	
	
	/**
	 * 查询数据库保存的最新的交易记录blockNumber
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: BigInteger 
	 * @Date :          2017年11月3日 下午5:29:50   
	 * @throws:
	 */
	public BigInteger getLastestBlockByCoinCode(String coinCode);
	
	/**
	 * 查询所有昨天的充值记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<AppCoinTransaction> 
	 * @Date :          2017年11月3日 下午6:11:09   
	 * @throws:
	 */
	public List<AppCoinTransaction> listYesterdayRechargeRecord(String coinType);
	
	
	/**
	 * <p>根据时间范围、充币数量获取list</p>
	 * @author:         shangxl
	 * @param:    @param hour
	 * @param:    @param amount
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年3月26日 下午3:28:05   
	 * @throws:
	 */
	public List<String> listTokenAddressByCondition(int hour,int amount);
	/**
	 * <p> 根据币种类型获取数据分组集合</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年3月29日 上午9:44:29   
	 * @throws:
	 */
	public List<String> listAddressGroupByAddress(String coinType);

}
