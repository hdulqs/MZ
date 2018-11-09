/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月13日 下午5:27:31
 */
package com.mz.trade.websoketContext.model;

import com.mz.trade.entrust.model.ExOrder;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月13日 下午5:27:31 
 */
public class MarketTradesSelf {
	public List<ExOrder> trades;

	/**
	 * <p> TODO</p>
	 * @return:     List<ExOrder>
	 */
	public List<ExOrder> getTrades() {
		return trades;
	}

	/** 
	 * <p> TODO</p>
	 * @return: List<ExOrder>
	 */
	public void setTrades(List<ExOrder> trades) {
		this.trades = trades;
	}


	
	

	

	

}
