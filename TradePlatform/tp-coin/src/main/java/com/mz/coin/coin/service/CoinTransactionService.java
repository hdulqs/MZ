/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年5月17日 上午11:21:35
 */
package com.mz.coin.coin.service;

import com.mz.coin.coin.model.CoinTransaction;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Yuan Zhicheng
 * @Date :          2016年5月17日 上午11:21:35 
 */                                                       
public interface CoinTransactionService extends BaseService<CoinTransaction, Long>{
	/**
	 * 
	 * <p> 交易订单是否存在</p>
	 * @author:         Yuan Zhicheng
	 * @param:    @param txid
	 * @param:    @param account(会出现txid一样的交易记录 add by shangxl)
	 * @param:    @return
	 * @return: int 
	 * @Date :          2016年11月15日 上午12:31:24   
	 * @throws:
	 */
	public int isExists(String txid);
	
	/**
	 * 根据参数查询匹配数据条数
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param txidType
	 * @param:    @param account
	 * @param:    @return
	 * @return: int 
	 * @Date :          2018年2月27日 下午4:06:09   
	 * @throws:
	 */
	public int countByParams(String txidType,String account);
	
	/**
	 * 
	 * <p> btc/ltc/qtum 刷新交易记录</p>
	 * @author:         Yuan Zhicheng
	 * @param:    @param type 币种code
	 * @param:    @param account 帐号
	 * @param:    @param count 数量
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年11月21日 下午2:40:04   
	 * @throws:
	 */
	public JsonResult recordTransaction(String type,String account,Integer count);
	
	/**
	 * 
      <p> 查询确认节点数大于 confirmations 的数据 并且 还未生成过订单 </p>
	 * @author:         Yuan Zhicheng
	 * @param:    @param coinType
	 * @param:    @param isCreateOrder 0 未生成过 1 已经生成
	 * @param:    @return
	 * @return: List<Transaction> 
	 * @Date :          2016年11月16日 上午1:02:27   
	 * @throws:
	 */
	public List<CoinTransaction> findTransactionListByconfirm(String coinType,int isCreateOrder, String category);

	
	/**
	 * 
	 * <p> 查询转币记录</p>
	 * @author:         Yuan Zhicheng
	 * @param:    @param coinType
	 * @param:    @param istIsRollOut
	 * @param:    @param category
	 * @param:    @return
	 * @return: List<Transaction> 
	 * @Date :          2016年11月21日 下午10:06:42   
	 * @throws:
	 */
	public List<CoinTransaction> findTransactionListIsRollOut(String coinType,int istIsRollOut,String category);
	

	
	/**
	 * 判断hash是否保存
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param hash
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年9月12日 下午5:21:20   
	 * @throws:
	 */
	public boolean isexistHash(String hash);
	
	/**
	 * list未确认充值记录by coinCode
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param coinCode
	 * @param:    @return
	 * @return: List<Transaction> 
	 * @Date :          2017年9月13日 上午9:33:36   
	 * @throws:
	 */
	public List<CoinTransaction> listUnconfirmed(String coinCode);

	/**
	 * 充币处理平台逻辑
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param transatcion
	 * @param:    @param upperCase
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年12月5日 下午5:20:17   
	 * @throws:
	 *//*
	public boolean updateOrder(Transaction transatcion, String upperCase);*/
	
	/**
	 * 充币
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param tx
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2017年12月18日 下午3:52:07   
	 * @throws:
	 */
	public JsonResult rechargecoin(CoinTransaction tx);
	
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType 
	 * @param:    @param address (string, optional) address filter (default: "*")
	 * @param:    @param count (number, optional) show at most n transactions (default: 10)
	 * @param:    @param skip	(number, optional) skip the first n transactions (default: 0)
	 * @param:    @param startblock (number, optional) first block to begin the search (default: 0)
	 * @param:    @param endblock (number, optional) last block to include in the search (default: 999999)
	 * @param:    @return
	 * @return: JsonResult 
	 * @Date :          2017年12月25日 下午7:14:20   
	 * @throws:
	 */
	public JsonResult omniRecordTransaction(String type,String address,int count,int skip,int startblock,int endblock);

	/**
	 * 刷新USDT区块记录
	 * @param coinType
	 * @param address
	 * @param count
	 * @param skip
	 * @param startblock
	 * @param endblock
	 * @return
	 */
	public JsonResult omniRecordTransactionByOneBlock(String coinType,String address, int count, int skip, int startblock, int endblock);
	/**
	 * 消费coin_transaction未消费的记录
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    
	 * @return: void 
	 * @Date :          2017年3月15日 下午5:09:00   
	 * @throws:
	 */
	public void consumeTransaction();
	
	/**
	 * 根据coinType分组获取address
	 * <p> TODO</p>
	 * @author:         shangxl
	 * @param:    @param coinType
	 * @param:    @return
	 * @return: List<String> 
	 * @Date :          2018年4月12日 上午11:31:45   
	 * @throws:
	 */
	List<String> listAddressGroupByCoinType(String coinType);

	void listWalletByredis();
	
}
