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
import com.mz.customer.agents.model.CommissionDetail;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.agents.service.CommissionDetailService;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年7月6日 下午6:09:23 
 */

@Controller
@RequestMapping("/agents/commissionDetail")
public class CommissionDetailController extends BaseController<CommissionDetail, Long> {

	@Resource(name="commissionDetailService")
	@Override
	public void setService(BaseService<CommissionDetail, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查询佣金比例配置的信息")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(CommissionDetail.class,
				request);
		filter.setOrderby("created desc");
		PageResult page = super.findPage(filter);
		return page;
	}
	
	@MethodName(name = "添加一条佣金信息")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(CommissionDetail commissionDetail){
		JsonResult result = super.save(commissionDetail);
		return result;
	}
	
	@MethodName(name="修改代理参数表")
	@RequestMapping("/modified")
	@ResponseBody
	public JsonResult modified(CommissionDetail commissionDetail){
		
		return super.update(commissionDetail);
		
	}
	
	@MethodName(name="删除某个代理参数")
	@RequestMapping("/remove/{id}")
	@ResponseBody
	public JsonResult remove(@PathVariable Long id){
		
		CommissionDetailService commissionDeployService = (CommissionDetailService)service;
		CommissionDetail detail = commissionDeployService.get(id);
		detail.setStates(0);
		
		return super.update(detail);

	}
	
	@MethodName(name="根据id查看某个对象的方法")
	@RequestMapping(value="/see/{id}",method = RequestMethod.GET)
	@ResponseBody
	public CommissionDetail see(@PathVariable Long id){

		CommissionDetailService commissionDetailService = (CommissionDetailService)service;
		
		CommissionDetail detail = commissionDetailService.get(id);
		
		return detail;

	}
	
	@MethodName(name="根据id查看某个对象的方法")
	@RequestMapping(value="/getCommissionByCustrommerName/{custromerName}",method = RequestMethod.GET)
	@ResponseBody
	public BigDecimal getCommissionByCustrommerName(@PathVariable String custromerName){
		
		CommissionDetailService commissionDetailService = (CommissionDetailService)service;
		
		BigDecimal m = commissionDetailService.findMoneyByCustromerName(custromerName);
		return m;
		
	}
	
	
	
	
	
	
    
}
