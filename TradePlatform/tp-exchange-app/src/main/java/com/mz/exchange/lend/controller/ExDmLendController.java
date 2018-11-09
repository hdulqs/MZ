/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:09:25
 */
package com.mz.exchange.lend.controller;


import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.lend.model.ExDmLend;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.customer.remote.RemoteAppPersonInfoService;
import com.mz.exchange.lend.service.ExDmLendService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:09:25
 */
@Controller
@RequestMapping("/lend/exdmLend")
public class ExDmLendController extends BaseController<ExDmLend, Long> {

	@Resource(name = "exDmLendService")
	@Override
	public void setService(BaseService<ExDmLend, Long> service) {
		super.service = service;
	}

	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExDmLend.class,request);
		String entrustType=request.getParameter("entrustType");
		if(!StringUtil.isEmpty(entrustType)){
			filter.addFilter("type=",entrustType);
		}
		PageResult findPage = super.findPage(filter);
		List<ExDmLend> list=(List<ExDmLend>)findPage.getRows();
		
		for(ExDmLend l:list){
			RpcContext.getContext().setAttachment("saasId",ContextUtil.getSaasId());
			RemoteAppCustomerService remoteExEntrustService = (RemoteAppCustomerService) ContextUtil.getBean("remoteAppCustomerService");
			AppCustomer ac=remoteExEntrustService.getById(l.getCustomerId());
			RemoteAppPersonInfoService remoteAppPersonInfoService = (RemoteAppPersonInfoService)ContextUtil.getBean("remoteAppPersonInfoService");
			AppPersonInfo appPersonInfo = remoteAppPersonInfoService.getByCustomerId(l.getId());
			l.setTrueName(appPersonInfo.getTrueName());
		}
		return findPage;
	}
	
	@MyRequiresPermissions
	@RequestMapping("/find")
	@MethodName(name = "分页查询ExDmLend")
	@ResponseBody
	public PageResult find(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExDmLend.class,request);
		filter.setOrderby("created desc");
		PageResult page = super.findPage(filter);
	    List<ExDmLend> list=(List<ExDmLend>)page.getRows();
		for(ExDmLend l:list){
			RpcContext.getContext().setAttachment("saasId",ContextUtil.getSaasId());
			RemoteAppCustomerService remoteExEntrustService = (RemoteAppCustomerService) ContextUtil.getBean("remoteAppCustomerService");
			AppCustomer ac=remoteExEntrustService.getById(l.getCustomerId());
			l.setUserName(ac.getUserName());
		}
		return page;
	}
	
	
	
	
	
	@RequestMapping("/see")
	@MethodName(name = "see分页查ExDmLend")
	@ResponseBody
	public PageResult see(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExDmLend.class,request);
		filter.setOrderby("created desc");
		PageResult page = ((ExDmLendService)service).see(filter);
		return page;
	}
	@RequestMapping("/getLending")
	@MethodName(name = "得到在借金额")
	@ResponseBody
	public String getLending(HttpServletRequest request) {
	
	//	BigDecimal Lending = ((ExDmLendService)service).getLending();
		BigDecimal Lending =new BigDecimal("1");
		return Lending.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	
}
