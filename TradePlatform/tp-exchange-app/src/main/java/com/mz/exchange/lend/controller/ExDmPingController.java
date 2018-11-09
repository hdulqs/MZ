/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:09:25
 */
package com.mz.exchange.lend.controller;


import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.lend.model.ExDmPing;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.remote.RemoteAppCustomerService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/lend/exDmPing")
public class ExDmPingController extends BaseController<ExDmPing, Long> {

	@Resource(name = "exDmPingService")
	@Override
	public void setService(BaseService<ExDmPing, Long> service) {
		super.service = service;
	}
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExDmPing.class,request);
		String entrustType=request.getParameter("entrustType");
		if(!StringUtil.isEmpty(entrustType)){
			filter.addFilter("type=",entrustType);
		}
		PageResult findPage = super.findPage(filter);
		List<ExDmPing> list=(List<ExDmPing>)findPage.getRows();
		
		for(ExDmPing l:list){
			RpcContext.getContext().setAttachment("saasId",ContextUtil.getSaasId());
			RemoteAppCustomerService remoteExEntrustService = (RemoteAppCustomerService) ContextUtil.getBean("remoteAppCustomerService");
			AppCustomer ac=remoteExEntrustService.getById(l.getCustomerId());
			l.setUserName(ac.getUserName());
			
		}
		// System.out.println(findPage);
		return findPage;
	}

}
