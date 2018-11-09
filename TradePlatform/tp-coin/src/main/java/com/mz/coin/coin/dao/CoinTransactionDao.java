/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年5月17日 上午11:02:21
 */
package com.mz.coin.coin.dao;

import java.math.BigDecimal;
import java.util.List;

import com.mz.coin.coin.model.CoinTransaction;
import com.mz.core.mvc.dao.base.BaseDao;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Yuan Zhicheng
 * @Date : 2016年5月17日 上午11:02:21
 */
public interface CoinTransactionDao extends BaseDao<CoinTransaction, Long> {
	/**
	 * 
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             txid
	 * @param: @return
	 * @return: int
	 * @Date : 2017年3月13日 下午8:11:11
	 * @throws:
	 */
	public int isExists(String txid);

	/**
	 * 
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             txidType
	 * @param: @param
	 *             account
	 * @param: @return
	 * @return: int
	 * @Date : 2017年3月13日 下午8:11:19
	 * @throws:
	 */
	public int countByParams(String txidType, String account);

	/**
	 * 查询是否生成过订单数据
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             coinType
	 * @param: @param
	 *             isCreateOrder
	 * @param: @param
	 *             category
	 * @param: @return
	 * @return: List<Transaction>
	 * @Date : 2017年3月13日 下午8:11:26
	 * @throws:
	 */
	public List<CoinTransaction> findTransactionListByconfirm(String coinType, int isCreateOrder, String category);

	/**
	 * 查询是否已经转币
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: shangxl
	 * @param: @param
	 *             coinType
	 * @param: @param
	 *             istIsRollOut
	 * @param: @param
	 *             category
	 * @param: @return
	 * @return: List<Transaction>
	 * @Date : 2017年3月13日 下午8:11:37
	 * @throws:
	 */
	public List<CoinTransaction> findTransactionListIsRollOut(String coinType, int istIsRollOut, String category);

	/**
	 * 询coin_transaction表获取昨天0点到24点的所有充值订单（group by 手机号）
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Lei
	 * @param: @param
	 *             type
	 * @param: @return
	 * @return: List<Transaction>
	 * @Date : 2017年3月27日 下午5:00:25
	 * @throws:
	 */
	public List<CoinTransaction> getAllRechargeTransactionsYesterday(String type);

	/**
	 * 判断hash是否存在
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Shangxl
	 * @param: @param
	 *             hash
	 * @param: @return
	 * @return: BigDecimal
	 * @Date : 2017年9月12日 下午5:24:07
	 * @throws:
	 */
	public BigDecimal isexistHash(String hash);

	/**
	 * list未确认交易记录by coinCode
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Shangxl
	 * @param: @param
	 *             coinCode
	 * @param: @return
	 * @return: List<Transaction>
	 * @Date : 2017年9月13日 上午9:35:53
	 * @throws:
	 */
	public List<CoinTransaction> listUnconfirmed(String coinCode);
	
	/**
	 * 查询所有未处理记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @return
	 * @return: List<Transaction> 
	 * @Date :          2017年3月15日 下午5:24:39   
	 * @throws:
	 */
	public List<CoinTransaction> listUnCreateOrder();
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年4月12日 上午11:34:23   
	 * @throws:
	 */
	List<String> listAddressGroupByCoinType(String coinType);
}
