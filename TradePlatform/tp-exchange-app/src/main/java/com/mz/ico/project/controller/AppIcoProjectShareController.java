/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-26 20:15:54 
 */
package com.mz.ico.project.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.project.model.AppIcoProjectShare;
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
 * @Date:        2017-07-26 20:15:54 
 */
@Controller
@RequestMapping("/project/appicoprojectshare")
public class AppIcoProjectShareController extends BaseController<AppIcoProjectShare, Long> {
	
	@Resource(name = "appIcoProjectShareService")
	@Override
	public void setService(BaseService<AppIcoProjectShare, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoProjectShare")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoProjectShare see(@PathVariable Long id){
		AppIcoProjectShare appIcoProjectShare = service.get(id);
		return appIcoProjectShare;
	}
	
	@MethodName(name="增加AppIcoProjectShare")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoProjectShare appIcoProjectShare){
		return super.save(appIcoProjectShare);
	}
	
	@MethodName(name="修改AppIcoProjectShare")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoProjectShare appIcoProjectShare){
		return super.update(appIcoProjectShare);
	}
	
	@MethodName(name="删除AppIcoProjectShare")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoProjectShare")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoProjectShare.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
