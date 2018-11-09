/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-26 18:09:22 
 */
package com.mz.ico.project.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.project.model.AppIcoProjectSuport;
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
 * @Date:        2017-07-26 18:09:22 
 */
@Controller
@RequestMapping("/project/appicoprojectsuport")
public class AppIcoProjectSuportController extends BaseController<AppIcoProjectSuport, Long> {
	
	@Resource(name = "appIcoProjectSuportService")
	@Override
	public void setService(BaseService<AppIcoProjectSuport, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoProjectSuport")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoProjectSuport see(@PathVariable Long id){
		AppIcoProjectSuport appIcoProjectSuport = service.get(id);
		return appIcoProjectSuport;
	}
	
	@MethodName(name="增加AppIcoProjectSuport")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoProjectSuport appIcoProjectSuport){
		return super.save(appIcoProjectSuport);
	}
	
	@MethodName(name="修改AppIcoProjectSuport")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoProjectSuport appIcoProjectSuport){
		return super.update(appIcoProjectSuport);
	}
	
	@MethodName(name="删除AppIcoProjectSuport")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoProjectSuport")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoProjectSuport.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
