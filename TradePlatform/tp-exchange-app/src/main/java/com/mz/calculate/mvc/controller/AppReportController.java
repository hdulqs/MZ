/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2016年9月5日 下午4:03:44
 */
package com.mz.calculate.mvc.controller;

import com.mz.calculate.mvc.ExchangeDataCache;
import com.mz.calculate.mvc.po.TotalAccountForReport;
import com.mz.calculate.mvc.po.TotalCurrencyForReport;
import com.mz.calculate.mvc.po.TotalCustomerForReport;
import com.mz.calculate.mvc.po.TotalEarningsForReport;
import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.calculate.util.DateUtil;
import com.mz.calculate.mvc.service.AppReportService;
import com.mz.core.annotation.base.MethodName;
import com.mz.util.sys.ContextUtil;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.web.remote.RemoteAppConfigService;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * @author Wu shuiming
 * @date 2016年9月5日 下午4:03:44
 */
@Controller
@RequestMapping("/appReportController")
public class AppReportController {
	
	@Resource(name="appReportService")
	public AppReportService appReportService;

	@MethodName(name = "查询平台用户的详细信息")
	@RequestMapping("/findTotalCustomerForReport")
	@ResponseBody
	public TotalCustomerForReport findTotalCustomerForReport(HttpServletRequest req){
		String beginTime = req.getParameter("beginTime")+" "+"00:00:01";
		String endTime = req.getParameter("endTime")+" "+"00:00:01";
		TotalCustomerForReport totalCustomerForReport = appReportService.findTotalCustomerForReport(beginTime, endTime);
		return totalCustomerForReport;
	}
	
	@MethodName(name = "查询平台用户的详细信息")
	@RequestMapping("/findTotalAccountForReport")
	@ResponseBody
	public TotalAccountForReport findTotalAccountForReport(HttpServletRequest req){
		Integer i = null;
		
		String beginTime = req.getParameter("beginTime")+" "+"00:00:01";
		String endTime = req.getParameter("endTime")+" "+"00:00:01";
		String customerType = req.getParameter("customerType");
		if(null != customerType && "" != customerType){
			try {
				  i = Integer.valueOf(customerType);
			} catch (Exception e) {
				i= null;
				e.printStackTrace();
			}
		}
		TotalAccountForReport totalAccountForReport = appReportService.findTotalAccountForReport(beginTime, endTime,i);
		
		//查询用户总资产
		//所有用户资金可用加冻结+三种币的币量（可用加冻结）*时价
		BigDecimal totalMoney=totalAccountForReport.getAvailableTotalMoney().add(totalAccountForReport.getFrozenTotalMoney());
		BigDecimal totalCoinNum=BigDecimal.ZERO;

		System.out.println("金科取当前最后一笔成交价计算用户总额！");
		String productListStr = ExchangeDataCache.getStringData("cn:productList");
		if (!StringUtils.isEmpty(productListStr)) {
			System.out.println("产品的str:"+productListStr);
				List<String> productList = JSON.parseArray(productListStr, String.class);
				for (String coinCode : productList) {
					ExOrderInfoService exOrderService = (ExOrderInfoService) ContextUtil.getBean("exOrderInfoService");
				//	ExOrderInfo exOrderInfo =  exOrderService.exAveragePrice(coinCode);
				//	System.out.println("币的种类："+coinCode+",查询到的最后一笔流水为："+exOrderInfo);
					//保存成交价
					/*if(null!=exOrderInfo&&null!=exOrderInfo.getTransactionPrice()){
						//totalCoinNum=totalCoinNum.add(exOrderInfo.getTransactionPrice());
						//获取该币的总量
						BigDecimal CoinNum=appReportService.getTotalCoinNum(coinCode);
						System.out.println("查询"+coinCode+"币的总量为："+CoinNum);
						totalCoinNum=(CoinNum.multiply(exOrderInfo.getTransactionPrice())).add(totalCoinNum);
						
					}else{
						totalCoinNum=totalCoinNum.add(BigDecimal.ZERO);
					}*/
			}
		}
		
		totalAccountForReport.setUserTotalMoney(totalMoney.add(totalCoinNum));
		return totalAccountForReport;
	}
	
	@MethodName(name = "查询平台币种结算报表信息")
	@RequestMapping("/findTotalCurrencyForReport")
	@ResponseBody
	public List<TotalCurrencyForReport> findTotalCurrencyForReport(HttpServletRequest req){
		RemoteAppConfigService remoteAppConfigService = (RemoteAppConfigService) ContextUtil.getBean("remoteAppConfigService");
		String closeTime=remoteAppConfigService.getFinanceByKey("closeTime");
		String beginTime = req.getParameter("beginTime")+" "+"00:00:01";
		String endTime = req.getParameter("endTime")+" "+"00:00:01";
		List<TotalCurrencyForReport> totalCurrencyForReport = appReportService.findTotalCurrencyForReport(beginTime, endTime);
		return totalCurrencyForReport;
	}
	
	
	
	
	@MethodName(name = "查询平台资金日结算报表信息")
	@RequestMapping("/findTotalEarningsForReport")
	@ResponseBody
	public TotalEarningsForReport findTotalEarningsForReport(HttpServletRequest req){
		
		String beginTime = req.getParameter("beginTime")+" "+"00:00:01";
		String endTime = req.getParameter("endTime")+" "+"00:00:01";
		TotalEarningsForReport totalEarningsForReport = appReportService.findTotalEarningsForReport(beginTime, endTime);
		return totalEarningsForReport;
	}
	
	
	@MethodName(name = "查询平台币种结算报表信息")
	@RequestMapping("/findTotalCustomerProfitForReport")
	@ResponseBody
	public List<AppReportSettlement> findTotalCustomerProfitForReport(HttpServletRequest req){
		String date;
		Integer i = null;
		date = req.getParameter("date");
		String customerTpe = req.getParameter("customerType");
		if(null == date | "" == date){
			date = DateUtil.getNowDate();
		}
		if(null != customerTpe & "" != customerTpe){
			i = Integer.valueOf(customerTpe);
			}
			List<AppReportSettlement> totalCustomerProfitForReport = appReportService.findTotalCustomerProfitForReport(date,i);
			return totalCustomerProfitForReport ;
		}
	
}




