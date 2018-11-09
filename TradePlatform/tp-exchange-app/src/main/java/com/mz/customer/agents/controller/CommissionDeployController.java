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
import com.mz.customer.agents.model.CommissionDeploy;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.agents.service.CommissionDeployService;
import java.util.List;
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
@RequestMapping("/agents/commissionDeploy")
public class CommissionDeployController extends BaseController<CommissionDeploy, Long> {

	@Resource(name="commissionDeployService")
	@Override
	public void setService(BaseService<CommissionDeploy, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查询佣金比例配置的信息")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {

		QueryFilter filter = new QueryFilter(CommissionDeploy.class,
				request);
		filter.addFilter("states=", 1);
	
		PageResult page = super.findPage(filter);
		return page;
	}
	
	@MethodName(name = "添加一条佣金信息")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(CommissionDeploy commissionDeploy){
		
		CommissionDeployService commissionDeployService = (CommissionDeployService)service;
		JsonResult result = commissionDeployService.SaveCommissionDeployByCostId(commissionDeploy);
		return result;
	}
	
	@MethodName(name="修改代理参数表")
	@RequestMapping("/modified")
	@ResponseBody
	public JsonResult modified(CommissionDeploy commissionDeploy){
		
		CommissionDeployService commissionDeployService = (CommissionDeployService)service;
		JsonResult result = commissionDeployService.SaveCommissionDeployByCostId(commissionDeploy);
		return result;
		
	}
	
	@MethodName(name="删除某个代理参数")
	@RequestMapping("/remove/{id}")
	@ResponseBody
	public JsonResult remove(@PathVariable Long id){
		
		CommissionDeployService commissionDeployService = (CommissionDeployService)service;
		CommissionDeploy deploy = commissionDeployService.get(id);
		deploy.setStates(0);
		
		return super.update(deploy);

	}
	
	@MethodName(name="根据id查看某个对象的方法")
	@RequestMapping(value="/see/{id}",method = RequestMethod.GET)
	@ResponseBody
	public CommissionDeploy see(@PathVariable Long id){
		CommissionDeployService commissionDeployService = (CommissionDeployService)service;
		CommissionDeploy deploy = commissionDeployService.get(id);
		return deploy;
	}
	
	
	@MethodName(name="根据类别id查询代理商参数配置数据")
	@RequestMapping(value="/getCommissionDeploy",method = RequestMethod.GET)
	@ResponseBody
	public CommissionDeploy getCommissionDeploy(Integer costId){
		
		QueryFilter filter = new QueryFilter(CommissionDeploy.class);
		filter.addFilter("costId=", costId);
		filter.addFilter("states=", 1);
		List<CommissionDeploy> list = super.find(filter);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
