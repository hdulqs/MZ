/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-18 14:07:27 
 */
package com.mz.ico.coinAccountColdRecord.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.coinAccountColdRecord.model.AppIcoCoinAccountColdRecord;
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
 * @Date:        2017-08-18 14:07:27 
 */
@Controller
@RequestMapping("/coinAccountColdRecord/appicocoinaccountcoldrecord")
public class AppIcoCoinAccountColdRecordController extends BaseController<AppIcoCoinAccountColdRecord, Long> {
	
	@Resource(name = "appIcoCoinAccountColdRecordService")
	@Override
	public void setService(BaseService<AppIcoCoinAccountColdRecord, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoCoinAccountColdRecord")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoCoinAccountColdRecord see(@PathVariable Long id){
		AppIcoCoinAccountColdRecord appIcoCoinAccountColdRecord = service.get(id);
		return appIcoCoinAccountColdRecord;
	}
	
	@MethodName(name="增加AppIcoCoinAccountColdRecord")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoCoinAccountColdRecord appIcoCoinAccountColdRecord){
		return super.save(appIcoCoinAccountColdRecord);
	}
	
	@MethodName(name="修改AppIcoCoinAccountColdRecord")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoCoinAccountColdRecord appIcoCoinAccountColdRecord){
		return super.update(appIcoCoinAccountColdRecord);
	}
	
	@MethodName(name="删除AppIcoCoinAccountColdRecord")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoCoinAccountColdRecord")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoCoinAccountColdRecord.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
