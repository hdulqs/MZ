/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午10:58:48
 */
package com.mz.calculate.mvc.controller;

import com.mz.calculate.mvc.po.AppCalculate;
import com.mz.calculate.mvc.po.AppCalculateAllCustomer;
import com.mz.calculate.mvc.po.AppStatisticalToIndexVo;
import com.mz.calculate.mvc.po.CalculateParameter;
import com.mz.calculate.mvc.po.CalculatePo;
import com.mz.calculate.mvc.po.CustromerRegisterPo;
import com.mz.calculate.mvc.po.FirstCoin;
import com.mz.calculate.mvc.po.PendingOrders;
import com.mz.calculate.mvc.po.TotalCustomerForReport;
import com.mz.calculate.settlement.model.AppOrderDistributionVo;
import com.mz.core.annotation.NoLogin;
import com.mz.core.annotation.base.MethodName;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.calculate.mvc.service.AppCalculateService;
import com.mz.calculate.mvc.service.CalculateAppTransactionService;
import com.mz.trade.entrust.service.ExEntrustService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年7月5日 上午10:58:48 
 */

@Controller   
@RequestMapping("/calculateapptransaction/apptransaction")
public class CalculateAppTransactionController {

	@Resource(name="calculateAppTransactionService")
	public CalculateAppTransactionService calculateAppTransactionService;

	//	@Resource(name="appFinanceService")
	//	public AppFinanceService appFinanceService;
	//	
	@Resource(name="appCalculateService")
	public AppCalculateService appCalculateService;
	
	@Resource(name="exEntrustService")
	public ExEntrustService exEntrustService;

	@RequestMapping("/getFirstCoin")
	@ResponseBody
	public FirstCoin getFirstCoin(HttpServletRequest request) {
		List<ExEntrust> a = exEntrustService.findFirstCoin();
		FirstCoin firstCoin = new FirstCoin();
		firstCoin.setCoinCode(a.get(0).getCoinCode());
		firstCoin.setEntrustCount(a.get(0).getEntrustCount());
		return firstCoin;
	}


	@MethodName(name = "充值订单总数量")
	@RequestMapping("/getTransactionPost")
	@ResponseBody
	public PendingOrders getTransactionPost(){
		PendingOrders pendingOrders = calculateAppTransactionService.selectMoneyByFactor(null);
		return pendingOrders ;
	}

	@MethodName(name = "提现订单总数量")
	@RequestMapping("/getTransactionGet")
	@ResponseBody
	public PendingOrders getTransactionGet(HttpServletRequest req){
		PendingOrders pendingOrders = calculateAppTransactionService.selectMoneyByFactor("get");
		return pendingOrders ;
	}


	@MethodName(name = "查询一天前的各个币种交易的量数据")
	@RequestMapping("/findOrderMoney")
	@ResponseBody
	public CalculatePo findCalculatePoByDayAgo(HttpServletRequest req){

		String s = req.getParameter("timeago");
		List<CalculatePo> list = calculateAppTransactionService.selectMoneyByOrder(Integer.valueOf(s));

		if(list.size()>0){
			List<CalculatePo> moneyList = new ArrayList<CalculatePo>();
			for(int i=0;i<list.size();i++){
				if(moneyList.size()>0){
					CalculatePo po = moneyList.get(0);
					CalculatePo po2 = list.get(i);
					int j = (po.getTrancateMoney()).compareTo(po2.getTrancateMoney());
					if(j==1){
						moneyList.remove(0);
						moneyList.add(po2);
					}
				}else{
					moneyList.add(list.get(i));
				}
			}
			return moneyList.get(0) ;
		}
		return null;
	}


	@MethodName(name = "查询用户当日注册跟系统总用户的一个比例")
	@RequestMapping("/findCustromerRegister")
	@ResponseBody
	public CustromerRegisterPo custromerRegisterPoByDayAgo(HttpServletRequest req){

		String s = req.getParameter("timeago");
		CustromerRegisterPo po = calculateAppTransactionService.selectCustromerByTime(Integer.valueOf(s), 0);
		System.out.println(po);
		return po ;
	}

	@NoLogin
	@MethodName(name = "查询用户当日注册跟系统总用户的一个比例")
	@RequestMapping("/queryCalculate")
	@ResponseBody
	public AppCalculate queryCalculate(CalculateParameter calculateParameter){

		AppCalculate findCalculate = appCalculateService.findCalculate(calculateParameter.getStartDate(),calculateParameter.getEndDate(),calculateParameter.getCustomerType(),calculateParameter.getCode());
		return findCalculate;

	}


	@NoLogin
	@MethodName(name = "查询用户当日注册跟系统总用户的一个比例")
	@RequestMapping("/queryTotalCustomerForReport")
	@ResponseBody
	public AppCalculateAllCustomer queryTotalCustomerForReport(
      TotalCustomerForReport totalCustomerForReport){

		//	AppCalculateAllCustomer findCalculateAll = appCalculateService.findCalculateAll(calculateParameter.getStartDate(),calculateParameter.getEndDate());
		return null;

	}


	@MethodName(name = "查询用户当日注册跟系统总用户的一个比例")
	@RequestMapping("/queryCalculateAll")
	@ResponseBody
	public AppCalculateAllCustomer queryCalculateAll(CalculateParameter calculateParameter){
		AppCalculateAllCustomer findCalculateAll = appCalculateService.findCalculateAll(calculateParameter.getStartDate(),calculateParameter.getEndDate());
		return findCalculateAll;

	}

	@MethodName(name = "查询首页用户分布图数据")
	@RequestMapping("/findUserDistribution")
	@ResponseBody
	public AppStatisticalToIndexVo findUserDistribution(HttpServletRequest req){
		AppStatisticalToIndexVo findUserDistribution = calculateAppTransactionService.findUserDistribution();
		return findUserDistribution;
	}

	@MethodName(name = "查询所有币的成交数据图")
	@RequestMapping("/findOrderDistribution")
	@ResponseBody
	public List<AppOrderDistributionVo> findOrderDistribution(HttpServletRequest req){
		List<AppOrderDistributionVo> appOrderDistributionVo = calculateAppTransactionService.findOrderDistribution();
		return appOrderDistributionVo;
	}


}




