/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-21 17:52:29 
 */
package com.mz.ico.project.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.project.model.AppIcoProjectRepay;
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
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-21 17:52:29 
 */
@Controller
@RequestMapping("/project/appicoprojectrepay")
public class AppIcoProjectRepayController extends BaseController<AppIcoProjectRepay, Long> {
	
	@Resource(name = "appIcoProjectRepayService")
	@Override
	public void setService(BaseService<AppIcoProjectRepay, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoProjectRepay")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoProjectRepay see(@PathVariable Long id){
		AppIcoProjectRepay appIcoProjectRepay = service.get(id);
		return appIcoProjectRepay;
	}
	
	@MethodName(name="增加AppIcoProjectRepay")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoProjectRepay appIcoProjectRepay){
		return super.save(appIcoProjectRepay);
	}
	
	@MethodName(name="修改AppIcoProjectRepay")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoProjectRepay appIcoProjectRepay){
		return super.update(appIcoProjectRepay);
	}
	
	@MethodName(name="删除AppIcoProjectRepay")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoProjectRepay")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoProjectRepay.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
