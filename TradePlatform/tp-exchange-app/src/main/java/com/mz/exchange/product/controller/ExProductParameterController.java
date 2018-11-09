/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:09:25
 */
package com.mz.exchange.product.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ExProductParameter;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.product.service.ExProductParameterService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:09:25
 */
@Controller
@RequestMapping("/product/exProductparameter")
public class ExProductParameterController extends BaseController<ExProductParameter, Long> {

	@Resource(name = "exProductParameterService")
	@Override
	public void setService(BaseService<ExProductParameter, Long> service) {
		super.service = service;
	}

	@MethodName(name = "查询产品参数的信息")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExProductParameter.class, request);
		PageResult findPage = super.findPage(filter);
		return findPage;
	}
	
	
	@MethodName(name = "修改数据")
	@RequestMapping("/modify")
	@ResponseBody
	public JsonResult modify(ExProductParameter exProductParameter) {
	
		ExProductParameterService exProductParameterService = (ExProductParameterService)service;
		JsonResult result = exProductParameterService.saveExProductParameter(exProductParameter ,2);
		return result;
		
	}
	
	@MethodName(name = "添加数据")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(ExProductParameter exProductParameter) {
		ExProductParameterService exProductParameterService = (ExProductParameterService)service;
		JsonResult result = exProductParameterService.saveExProductParameter(exProductParameter ,1);
		return result;
	}
	
	@MethodName(name = "添加数据")
	@RequestMapping("/see/{parameterId}")
	@ResponseBody
	public ExProductParameter see(@PathVariable Long parameterId) {
		ExProductParameterService exProductParameterService = (ExProductParameterService)service;
		ExProductParameter parameter = exProductParameterService.get(parameterId);
		return parameter;
	}
	
	
	@MethodName(name = "查询所有的参数类型")
	@RequestMapping("/selectPeoductParameter")
	@ResponseBody
	public List<ExProductParameter> selectPeoductParameter(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExProductParameter.class, request);
		filter.addFilter("state=", 1);
		List<ExProductParameter> list = super.find(filter);
		return list;
	}
	
	
	
}
