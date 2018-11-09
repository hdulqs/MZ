/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年7月6日 下午6:09:23
 */
package com.mz.customer.agents.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.agents.model.AppAgentsCustromer;
import com.mz.customer.user.model.AppCustomer;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.agents.service.AppAgentsCustromerService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年7月6日 下午6:09:23 
 */

@Controller
@RequestMapping("/agents/appAgentscustromer")
public class AppAgentsCustromerController extends BaseController<AppAgentsCustromer, Long> {

	@Resource(name="appAgentsCustromerService")
	@Override
	public void setService(BaseService<AppAgentsCustromer, Long> service) {
		super.service = service;
	}
	
	@Resource(name="appCustomerService")
	public AppCustomerService appCustomerService;
	
	@MethodName(name = "分页查询代理商用户 ")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppAgentsCustromer.class,
				request);
		String states = (String)filter.getRequest().getParameter("states");
		if (states == null) {
			filter.addFilter("states!=", 1);
		} else {
			filter.addFilter("states=", states);
		}
		System.out.println(states);
		PageResult page = super.findPage(filter);
		return page;
	}
	
	
	@MethodName(name = "通过id返回用户的代理商信息 ")
	@RequestMapping("/getById")
	@ResponseBody
	public AppAgentsCustromer getById(HttpServletRequest request) {
		String s = request.getParameter("id");
		if(null != s && !"".equals(s)){
			AppAgentsCustromerService appAgentsCustromerService = (AppAgentsCustromerService)service;
			AppAgentsCustromer appAgentsCustromer = appAgentsCustromerService.get(Long.valueOf(s));
			return appAgentsCustromer;
		}else{
			return null;
		}
	}
	
	@MethodName(name = "分页查询代理商用户 ")
	@RequestMapping("/see")
	@ResponseBody
	public PageResult see(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppAgentsCustromer.class,
				request);
		filter.addFilter("states=", 1);
		PageResult page = super.findPage(filter);
		return page;
	}
	
	
	@MethodName(name = "多选审核代理订单 ")
	@RequestMapping("/paseUser")
	@ResponseBody
	public JsonResult paseUser(@RequestParam(value = "ids")Long[] ids) {
		
		if(ids.length>0){
			AppAgentsCustromerService appAgentsCustromerService = (AppAgentsCustromerService)service;
			JsonResult result = appAgentsCustromerService.pasetUser(ids, 2);
			if(result.getSuccess()){
				result.setMsg("全部审核成功");
				return result;
			}
		}
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setMsg("处理失败");
		return jsonResult ;
	}
	
	
	
	@MethodName(name = "多选阻止代理订单 ")
	@RequestMapping("/storpUser")
	@ResponseBody
	public JsonResult storpUser(@RequestParam(value = "ids")Long[] ids) {
		
		if(ids.length>0){
			AppAgentsCustromerService appAgentsCustromerService = (AppAgentsCustromerService)service;
			JsonResult result = appAgentsCustromerService.pasetUser(ids, 3);
			if(result.getSuccess()){
				result.setMsg("全部删除成功");
				return result;
			}
		}
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setMsg("处理失败");
		return jsonResult ;
	}
	

	
	
	@MethodName(name = "根据某个用户查询其所对应的一级或二级、三级 用户 ")
	@RequestMapping("/findPersonInfo")
	@ResponseBody
	public PageResult findPersonInfo(HttpServletRequest req){
		String c=req.getParameter("userName");;
		String num = req.getParameter("num");
		String userName = req.getParameter("userName");
		PageResult page=new PageResult();
		if (userName!=null) {
			if(num.equals("1")){
				req.setAttribute("referralCode", userName);
				QueryFilter filter = new QueryFilter(AppCustomer.class,req);
				page = appCustomerService.findPageBySql(filter);
			}else if(num.equals("2")){
				for(int i=0;i<1;i++){
					QueryFilter filter = new QueryFilter(AppCustomer.class,req);
					filter.addFilter("referralCode=", c);
			    	List<AppCustomer> find = appCustomerService.find(filter);
			    		if(find.size()>0){
			    			String username2 = find.get(0).getUserName();
			    			c=username2;
			    		}
				 }
			 if(c!=null&&!c.equals("")){
						req.setAttribute("referralCode", c);
						QueryFilter filter = new QueryFilter(AppCustomer.class,req);
						filter.addFilter("referralCode", c);
						page = appCustomerService.findPageBySql(filter);
				 }
			}
			else if(num.equals("3")){
					for(int i=0;i<2;i++){
					QueryFilter filter = new QueryFilter(AppCustomer.class,req);
					filter.addFilter("referralCode=", c);
			    	List<AppCustomer> find = appCustomerService.find(filter);
			    		if(find.size()>0){
			    			String username2 = find.get(0).getUserName();
			    			c=username2;
			    		}
				 }
				 if(c!=null&&!c.equals("")){
					req.setAttribute("referralCode", c);
					QueryFilter filter = new QueryFilter(AppCustomer.class,req);
					filter.addFilter("referralCode", c);
					page = appCustomerService.findPageBySql(filter);
				 }
			}
		}else{
			req.setAttribute("referralCode", "qweqweqwrq");
			QueryFilter filter1 = new QueryFilter(AppCustomer.class,req);
			page = appCustomerService.findPageBySql(filter1);
		}
		return page;
	}
	
	

	
	@MethodName(name = "查询所有的代理商实际收了多少钱")
	@ResponseBody
	@RequestMapping("/findAgentsForMoney")
	public PageResult findAgentsForMoney(HttpServletRequest req){
		
		String agentName = req.getParameter("agentName");
		String fixPriceCoinCode = req.getParameter("fixPriceCoinCode");

	
		QueryFilter filter = new QueryFilter(AppAgentsCustromer.class,req);
		
		AppAgentsCustromerService appAgentsCustromerService = (AppAgentsCustromerService)service;
		
		PageResult result = appAgentsCustromerService.findAgentsForMoney(filter,agentName,fixPriceCoinCode);
		
		return result ;
		
	}
	
	
	@MethodName(name = "审核认证")
	@RequestMapping(value="/audit/{id}")
	@ResponseBody
	public JsonResult audit(@PathVariable Long id){
		JsonResult jsonResult = new JsonResult();
		AppAgentsCustromer appAgentsCustromer = service.get(id);
		if(appAgentsCustromer!=null){
			((AppAgentsCustromerService) service).audit(appAgentsCustromer);
			jsonResult.setSuccess(true);
			return jsonResult;
		}else{
			jsonResult.setMsg("不存在该用户");
		}
		return jsonResult;
	}
	
	@MethodName(name = "代理客户管理查看")
	@RequestMapping("/look/{id}")
	@ResponseBody
	public AppAgentsCustromer look(@PathVariable Long id) {
		return service.get(id);
	}

}
