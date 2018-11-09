package com.mz.calculate.mvc.dao;

import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.calculate.mvc.po.CalculatePo;
import com.mz.calculate.mvc.po.TotalAccountForReport;
import com.mz.calculate.mvc.po.TotalCurrencyForReport;
import com.mz.calculate.mvc.po.TotalCustomerForReport;
import com.mz.calculate.mvc.po.TotalEarningsForReport;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;


public interface AppReportDao extends BaseDao<CalculatePo,Long> {

	// 会员管理报表
	public TotalCustomerForReport findTotalCustomer(@Param(value="beginTime") String beginTime,@Param(value="endTime") String endTime);

	// 平台资金收益日结算报表
	public TotalEarningsForReport findTotalMoney(@Param(value="beginTime") String beginTime,@Param(value="endTime") String endTime);
	
	
	public List<TotalCurrencyForReport> findTotalCurrency(@Param(value="beginTime") String beginTime, @Param(value="endTime") String endTime );
	
	// 平台资金单日结算报表   custromerType 1 2 3  ---甲 乙 丙 用户类型
	public TotalAccountForReport findTotalAccount(@Param(value="beginTime") String beginTime, @Param(value="endTime") String endTime,@Param(value="customerType")Integer custromerType);
 
	// 平台用户盈亏结算报表  
	public List<AppReportSettlement> findTotalCustomerProfitForReport(@Param(value="date")String date,@Param(value="customerType") Integer customerType);

	public BigDecimal getTotalCoinNum(String coinCode);
	
}





