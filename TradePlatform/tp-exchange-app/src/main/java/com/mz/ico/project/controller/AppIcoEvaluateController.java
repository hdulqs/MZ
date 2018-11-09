/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      denghf
 * @version:     V1.0 
 * @Date:        2017-08-16 20:24:46 
 */
package com.mz.ico.project.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.project.model.AppIcoEvaluate;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      denghf
 * @version:     V1.0 
 * @Date:        2017-08-16 20:24:46 
 */
@Controller
@RequestMapping("/project/appicoevaluate")
public class AppIcoEvaluateController extends BaseController<AppIcoEvaluate, Long> {
	
	@Resource(name = "appIcoEvaluateService")
	@Override
	public void setService(BaseService<AppIcoEvaluate, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoEvaluate")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoEvaluate see(@PathVariable Long id){
		AppIcoEvaluate appIcoEvaluate = service.get(id);
		return appIcoEvaluate;
	}
	
	@MethodName(name="增加AppIcoEvaluate")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoEvaluate appIcoEvaluate){
		return super.save(appIcoEvaluate);
	}
	
	@MethodName(name="修改AppIcoEvaluate")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoEvaluate appIcoEvaluate){
		return super.update(appIcoEvaluate);
	}
	
	@MethodName(name="删除AppIcoEvaluate")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoEvaluate")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoEvaluate.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
