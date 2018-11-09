package com.mz.customer.agents.service.impl;

import com.mz.account.fund.model.AppTransaction;
import com.mz.account.remote.RemoteAppTransactionService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.agents.model.AppAgentsCustromer;
import com.mz.customer.agents.model.CommissionDetail;
import com.mz.customer.agents.model.vo.CommissionForAgents;
import com.mz.customer.user.model.AppCustomer;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.agents.dao.CommissionDetailDao;
import com.mz.customer.agents.service.AppAgentsCustromerService;
import com.mz.customer.agents.service.CommissionDeployService;
import com.mz.customer.agents.service.CommissionDetailService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
          
@Service("commissionDetailService")
public class CommissionDetailServiceImpl extends BaseServiceImpl<CommissionDetail,Long> implements CommissionDetailService {

	@Resource(name="commissionDetailDao")
	@Override
	public void setDao(BaseDao<CommissionDetail, Long> dao) {
		super.dao = dao;
	}
		
	@Resource(name="commissionDetailService")
	public CommissionDetailService commissionDetailService;

	@Resource(name="commissionDeployService")
	public CommissionDeployService commissionDeployService;
	
	@Resource(name="appAgentsCustromerService")
	public AppAgentsCustromerService appAgentsCustromerService;

	/**
	 * 通过订单号保存佣金明细
	 */
	@Override
	public Boolean commissionDetailByOrder(String orderNum) {
		
		RemoteAppTransactionService remoteAppTransactionService = (RemoteAppTransactionService)ContextUtil.getBean("remoteAppTransactionService");
		AppTransaction transaction = remoteAppTransactionService.createTranctonByOrderNum(orderNum);
		
		if(null != transaction){
		//	BigDecimal decimal = commissionDeployService.selectMoneyByOrder(transaction, 1);
			List<AppAgentsCustromer> list = commissionDeployService.findByTransaction(transaction.getUserName());
			if(list.size()>0){
				for(int i=1;i<=list.size();i++){
					AppAgentsCustromer agentCustromer = list.get(i);
					if(null != agentCustromer){
						this.saveCommissionDetail(transaction,agentCustromer,i);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 保存一个佣金流水    这个方法是供上面的方法调用  不是接口方法
	 * 
	 * @author:    Wu Shuiming
	 * @version:   V1.0 
	 * @date:      2016年7月18日 下午9:38:48
	 */
	@Override
	public void saveCommissionDetail(AppTransaction transaction,AppAgentsCustromer agentsCustromer,Integer rank){
		
		// 返回提现的费率 rank 表示几级父
		BigDecimal rate = commissionDeployService.selectRateByOrder(1, rank);
		// 查询所 rank 级父所产生的佣金 
		BigDecimal money = commissionDeployService.selectMoneyByOrder(transaction, rank);
		CommissionDetail commissionDetail = new CommissionDetail();
		commissionDetail.setAgentsId(agentsCustromer.getId());
		commissionDetail.setAgentsName(agentsCustromer.getCustomerName());
		commissionDetail.setOrderNum(transaction.getTransactionNum());
		commissionDetail.setDeliveryName(transaction.getUserName());
		commissionDetail.setDeliveryId(transaction.getUserId());
		commissionDetail.setDeliveryMoney(money);
		commissionDetail.setAgentsRank(rank);
		commissionDetail.setCategory(1);
		commissionDetail.setStates(1);
		commissionDetail.setRate(rate);
		commissionDetail.setTotalFee(transaction.getFee());
		//提現默認存cny
		commissionDetail.setFixPriceCoinCode("CNY");
		commissionDetail.setFixPriceType(0);
		super.save(commissionDetail);
		
	}
	/**
	 * 认购保存佣金明细
	 */
	@Override
	public void saveCommissionDetailSubscription(AppCustomer customer,
			AppAgentsCustromer agentsCustromer, BigDecimal srationMoney,
			String transactionNum,Integer agentsRank,BigDecimal sratio) {
		CommissionDetail commissionDetail = new CommissionDetail();
		commissionDetail.setAgentsId(agentsCustromer.getId());
		commissionDetail.setAgentsName(agentsCustromer.getCustomerName());
		commissionDetail.setOrderNum(transactionNum);
		commissionDetail.setDeliveryName(customer.getUserName());
		commissionDetail.setDeliveryId(customer.getId());
		commissionDetail.setDeliveryMoney(srationMoney);
		commissionDetail.setAgentsRank(agentsRank);
		commissionDetail.setCategory(4);
		commissionDetail.setStates(1);
		commissionDetail.setRate(sratio);
		commissionDetail.setTotalFee(srationMoney);
		super.save(commissionDetail);
	}
	/**
	 * 通过成交单订单保存佣金名细
	 * 
	 */
	@Override
	public void saveCommissionDetailForOrder(ExOrderInfo exOrderInfo,AppAgentsCustromer agentsCustromer,Integer rank){
		if(null!=agentsCustromer){
			// 返回提现的费率 rank 表示几级父
			BigDecimal rate = commissionDeployService.selectRateByOrder(2, rank);
			// 查询所 rank 级父所产生的佣金
			BigDecimal money=commissionDeployService.selectCommissionByMoney(exOrderInfo.getTransactionBuyFee(),2, rank);
			/*if(exOrderInfo.getType()==1){
				money = commissionDeployService.selectCommissionByMoney(exOrderInfo.getTransactionBuyFee(),2, rank);
			}else if(exOrderInfo.getType()==2){
				money = commissionDeployService.selectCommissionByMoney(exOrderInfo.getTransactionSellFee(),2, rank);
			}else {
				money = null;
			}*/
		
			CommissionDetail commissionDetail = new CommissionDetail();
			commissionDetail.setAgentsId(agentsCustromer.getId());
			commissionDetail.setAgentsName(agentsCustromer.getCustomerName());
			commissionDetail.setFixPriceType(exOrderInfo.getFixPriceType());
			commissionDetail.setFixPriceCoinCode(exOrderInfo.getCoinCode());
			commissionDetail.setOrderNum(exOrderInfo.getOrderNum());
			// 成交买方 订单
			//if(exOrderInfo.getType()==1){
				commissionDetail.setDeliveryName(exOrderInfo.getBuyUserName());
				commissionDetail.setCategory(21);
				// 设置买方总交的手续费 
				commissionDetail.setTotalFee(exOrderInfo.getTransactionBuyFee());
			//}
			// 成交卖方 订单
			/*if(exOrderInfo.getType()==2){
				commissionDetail.setDeliveryName(exOrderInfo.getSellUserName());
				commissionDetail.setCategory(22);
				// 设置卖方总交的手续费
				commissionDetail.setTotalFee(exOrderInfo.getTransactionSellFee());
			}*/
			commissionDetail.setDeliveryMoney(money);
			commissionDetail.setAgentsRank(rank);
			commissionDetail.setStates(1);
			
			
			commissionDetail.setRate(rate);
			super.save(commissionDetail);
		}else{
			
			System.out.println("代理商为空");
		}
		
		
	}
	


	/**
	 * 
	 * 通过用户的名字查询用户的佣金 
	 * 
	 * 包括提现订单  以及交易订单的佣金
	 * 
	 */
	@Override
	public BigDecimal findMoneyByCustromerName(String custromerName) {
		BigDecimal f_amoney = BigDecimal.ZERO;
		BigDecimal f_bmoney = BigDecimal.ZERO;
		BigDecimal f_cmoney = BigDecimal.ZERO;
		
		QueryFilter filter = new QueryFilter(AppAgentsCustromer.class);
		filter.addFilter("customerName=", custromerName);
		AppAgentsCustromer custromer = appAgentsCustromerService.get(filter);
		if(null == custromer){
			return null;
		}else{
			CommissionDetailDao commissionDetailDao = (CommissionDetailDao) dao;
			CommissionForAgents commissionForAgents = commissionDetailDao.findMoneyByCustromerName(custromerName);
			if(null != commissionForAgents){
				BigDecimal f_amoney1 = commissionForAgents.getF_amoney();
				if(null != f_amoney1){
					f_amoney = f_amoney1;
				}
				BigDecimal f_bmoney1 = commissionForAgents.getF_bmoney();
				if(null != f_bmoney1){
					f_bmoney = f_bmoney1;
				}
				BigDecimal f_cmoney1 = commissionForAgents.getF_cmoney();
				if(null != f_cmoney1){
					f_cmoney = f_cmoney1;
				}
				BigDecimal money = f_amoney.add(f_bmoney).add(f_cmoney);
				
				return money;
			}else{
				return BigDecimal.ZERO;
			}
			
		}
		
	}


	
}






