/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2016年9月6日 下午6:52:40
 */
package com.mz.calculate.mvc.controller;

import com.mz.calculate.mvc.po.AccountFundInfo;
import com.mz.calculate.settlement.model.AppReportSettlement;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.remote.settlement.RemoteAppReportSettlementCheckService;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @author Wu shuiming
 * @date 2016年9月6日 下午6:52:40
 */
@Controller
@RequestMapping(value="/redisandsqlcheck")
public class RedisAndSqlCheckController extends BaseController<AppReportSettlement, Long>
{

	@Resource(name="appReportSettlementService")
	@Override
	public void setService(BaseService<AppReportSettlement, Long> service) {
		super.service = service;
	}
	@Resource
	private  RedisService redisService;
	@Resource
	public RemoteAppReportSettlementCheckService remoteAppReportSettlementCheckService;
	
	
	//====================start核算 用redis======================
	@MethodName(name="基于sureold全部余额核算有错误的客户信息ToRedis")
	@RequestMapping(value="/culSureOldAccountAllCustomerErrorInfoToRedis")
	@ResponseBody	
	public JsonResult culSureOldAccountAllCustomerErrorInfoToRedis(HttpServletRequest request) {
		String daysstr = request.getParameter("days");
		List<Map<String, Object>> list = remoteAppReportSettlementCheckService
				.culRedisAndSqlSureOldAccountAllCustomerErrorInfo(StringUtil.isEmpty(daysstr) ? null : Integer.valueOf(daysstr));
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		jsonResult.setMsg("计算完成已经保存到Redis，一共" + list.size() + "条");
		return jsonResult;
	}
	@MethodName(name="全部余额核算展示有错误的客户信息Redis")
	@RequestMapping(value="/culAccountAllCustomerErrorInfo")
	@ResponseBody	
	public List<Map<String,List<AccountFundInfo>>> culAccountAllCustomerErrorInfo(HttpServletRequest request){
		  List<Map<String,List<AccountFundInfo>>> list=redisService.getList1("user_fund_check_all_redisansql");
	   
		    return list;
	}
	
	
	@MethodName(name="基于sureold对单个客户余额核算展示有错误的客户信息")
	@RequestMapping(value="/culAccountByCustomerErrorInfosureold/{ids}")
	@ResponseBody	
	public List<Map<String,Object>> culAccountByCustomerErrorInfosureold(@PathVariable String[] ids){
		List<Map<String,Object>> list=remoteAppReportSettlementCheckService.culRedisAndSqlAccountByCustomer(ids, false);
		return list;
		
	}
	@MethodName(name="基于sureold对单个客户余额核算展示有错误正确的的客户信息")
	@RequestMapping(value="/culAccountByCustomerErrorAndRightInfosureold/{ids}")
	@ResponseBody	
	public List<Map<String,Object>> culAccountByCustomerErrorAndRightInfosureold(@PathVariable String[] ids){
		RemoteAppReportSettlementCheckService remoteAppReportSettlementCheckService  = (RemoteAppReportSettlementCheckService) ContextUtil.getBean("remoteAppReportSettlementCheckService");
		List<Map<String,Object>> list=remoteAppReportSettlementCheckService.culRedisAndSqlAccountByCustomer(ids,true);
		
		return list;
		
	}
	@MethodName(name="基于sureold对单个客户余额核算并保存到数据库")
	@RequestMapping(value="/culAccountByCustomersureold/{ids}")
	@ResponseBody	
	public JsonResult culAccountByCustomersureold(@PathVariable String[] ids){
		JsonResult jsonResult = new JsonResult();
		remoteAppReportSettlementCheckService.culRedisAndSqlToSqlAccountByCustomer(ids);
		
		jsonResult.setSuccess(true);
		
		return jsonResult;
		
	}
	//====================end核算=================================
	
}
