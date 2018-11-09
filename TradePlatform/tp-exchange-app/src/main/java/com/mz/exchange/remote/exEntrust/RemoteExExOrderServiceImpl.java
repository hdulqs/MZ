/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.exchange.remote.exEntrust;

import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrder;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.change.remote.exEntrust.RemoteExExOrderService;
import com.mz.exchange.entrust.service.ExExOrderInfoService;
import com.mz.exchange.entrust.service.ExExOrderService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.entrust.service.ExOrderService;
import java.util.List;
import javax.annotation.Resource;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:04:29
 */

public class RemoteExExOrderServiceImpl implements RemoteExExOrderService {

	@Resource
	private ExOrderInfoService exOrderInfoService;

	@Resource
	private ExExOrderInfoService exExOrderInfoService;
	@Resource
	private ExOrderService exOrderService;
	@Resource
	private ExExOrderService exExOrderService;
	@Override
	public List<ExOrderInfo> findByOrderNum(String orderNum) {
		
		QueryFilter filter = new QueryFilter(ExOrderInfo.class);
		filter.addFilter("orderNum=", orderNum);
		List<ExOrderInfo> list = exOrderInfoService.find(filter);
		return list;
	}


	@Override
	public String[] deductMoney(ExOrderInfo exOrderInfo, ExEntrust buyexEntrust,ExEntrust sellentrust,ExOrderInfo exOrderInfosell,
			ExOrder exOrder) {
		// TODO Auto-generated method stub
		return exExOrderInfoService.deductMoney(exOrderInfo, buyexEntrust, sellentrust,exOrderInfosell,exOrder);
	}


	/**
	 * 获得用户的订单量
	 * @param buyId
	 * @return
	 */
	@Override
	public Long ListCount(Long buyId) {
		// TODO Auto-generated method stub
		QueryFilter filter = new QueryFilter(ExOrderInfo.class);
		filter.addFilter("buyCustomId=", buyId);
		return exExOrderInfoService.count(filter);
	}

}
