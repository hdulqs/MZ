/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年7月6日 下午6:02:18
 */
package com.mz.customer.agents.service;

import com.mz.account.fund.model.AppTransaction;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.agents.model.AppAgentsCustromer;
import com.mz.customer.agents.model.CommissionDeploy;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年7月6日 下午6:02:18 
 */
public interface CommissionDeployService extends BaseService<CommissionDeploy, Long> {
	
	// 通过订单查出应该交给父佣金的多少钱  (rank 表示查询的几级父)
	public BigDecimal selectMoneyByOrder(AppTransaction transaction,Integer rank);
	
	// 通过订单类型对应的费率是是多少
	public BigDecimal selectRateByOrder(Integer deploy,Integer rank);

	public List<AppAgentsCustromer> findByTransaction(String userNmae);
	
	public BigDecimal selectCommissionByMoney(BigDecimal money,Integer commissionType, Integer rank);
	
	public JsonResult SaveCommissionDeployByCostId(CommissionDeploy commissionDeploy);
	
	// 通过什么类型  和几级父返回一个佣金配置数据
	public CommissionDeploy findCommissionDeploy(Integer costId);


	/**
	 * 返回派發的佣金数
	 * 
	 * 两种类型中取最大值
	 * 
	 * @author:    Wu Shuiming
	 * @version:   V1.0 
	 * @date:      2016年9月1日 下午4:56:20
	 */
	public BigDecimal getStandardMoney(); 
	
	
}                                                                                             
