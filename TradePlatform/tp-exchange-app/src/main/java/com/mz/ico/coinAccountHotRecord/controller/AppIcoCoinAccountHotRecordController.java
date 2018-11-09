/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-18 14:06:56 
 */
package com.mz.ico.coinAccountHotRecord.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.coinAccountHotRecord.model.AppIcoCoinAccountHotRecord;
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
 * @Date:        2017-08-18 14:06:56 
 */
@Controller
@RequestMapping("/coinAccountHotRecord/appicocoinaccounthotrecord")
public class AppIcoCoinAccountHotRecordController extends BaseController<AppIcoCoinAccountHotRecord, Long> {
	
	@Resource(name = "appIcoCoinAccountHotRecordService")
	@Override
	public void setService(BaseService<AppIcoCoinAccountHotRecord, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoCoinAccountHotRecord")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoCoinAccountHotRecord see(@PathVariable Long id){
		AppIcoCoinAccountHotRecord appIcoCoinAccountHotRecord = service.get(id);
		return appIcoCoinAccountHotRecord;
	}
	
	@MethodName(name="增加AppIcoCoinAccountHotRecord")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoCoinAccountHotRecord appIcoCoinAccountHotRecord){
		return super.save(appIcoCoinAccountHotRecord);
	}
	
	@MethodName(name="修改AppIcoCoinAccountHotRecord")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoCoinAccountHotRecord appIcoCoinAccountHotRecord){
		return super.update(appIcoCoinAccountHotRecord);
	}
	
	@MethodName(name="删除AppIcoCoinAccountHotRecord")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoCoinAccountHotRecord")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoCoinAccountHotRecord.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
