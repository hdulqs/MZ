/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-06-14 17:35:14 
 */
package com.mz.trade.exEntrustOneDay.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.trade.exEntrustOneDay.model.ExentrustOneday;

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
 * @Date:        2017-06-14 17:35:14 
 */
@Controller
@RequestMapping("/exEntrustOneDay/exentrustoneday")
public class ExentrustOnedayController extends BaseController<ExentrustOneday, Long> {
	
	@Resource(name = "exentrustOnedayService")
	@Override
	public void setService(BaseService<ExentrustOneday, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看ExentrustOneday")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExentrustOneday see(@PathVariable Long id){
		ExentrustOneday exentrustOneday = service.get(id);
		return exentrustOneday;
	}
	
	@MethodName(name="增加ExentrustOneday")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExentrustOneday exentrustOneday){
		return super.save(exentrustOneday);
	}
	
	@MethodName(name="修改ExentrustOneday")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExentrustOneday exentrustOneday){
		return super.update(exentrustOneday);
	}
	
	@MethodName(name="删除ExentrustOneday")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表ExentrustOneday")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExentrustOneday.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
