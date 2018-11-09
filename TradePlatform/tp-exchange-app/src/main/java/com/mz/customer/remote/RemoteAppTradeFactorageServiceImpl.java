/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午12:19:16
 */
package com.mz.customer.remote;

import com.mz.customer.trade.service.AppCommendTradeService;
import com.mz.exchange.subscription.model.ExSubscriptionPlan;
import com.mz.customer.agents.service.AppTradeFactorageService;
import com.mz.customer.agents.service.CommissionDetailService;
import java.math.BigDecimal;
import javax.annotation.Resource;


/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月28日 下午12:19:16 
 */
public class RemoteAppTradeFactorageServiceImpl implements RemoteAppTradeFactorageService {
	
	@Resource(name="appTradeFactorageService")
	public AppTradeFactorageService appTradeFactorageService;
	
	@Resource(name="commissionDetailService")
	public CommissionDetailService commissionDetailService;
	
	@Resource(name="appCommendTradeService")
	public AppCommendTradeService appCommendTradeService;
	/**
	 * 
	 * 通过订单号 保存订单产生的手续费所产生的费率  type 参数表示以第二个费率类型来保存数据
	 * 
	 */
	@Override
	public Boolean saveTradeFactoryge(String orderNum,Integer type){
		Boolean boolean1 = appTradeFactorageService.dealCommission(orderNum,type);
		return boolean1;
	}
	
	
	public Boolean saveTrade(String orderNum){
		Boolean boolean1 = appCommendTradeService.dealCommission(orderNum);
		return boolean1;
	}


	/**
	 * 保存订单的明细  type 1 提现  2 是交易 
	 */
	@Override
	public Boolean saveTrackOrder(String orderNum,int type){
		Boolean boolean1 = appTradeFactorageService.dealCommissionByTraction(orderNum,type);
		return boolean1;
	}
	
	/**
	 * 保存认购返佣金额
	 */
	@Override
	public Boolean dealCommissionBySubscription(ExSubscriptionPlan plan,Long customerId,
			BigDecimal buyTotalNum,String transactionNum){ 
		return appTradeFactorageService.dealCommissionBySubscription(plan, customerId, buyTotalNum,transactionNum);
	}
	
	
}   
