/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 11:44:10 
 */
package com.mz.ex.dmHotAccountRecord.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ex.dmHotAccountRecord.model.ExDmHotAccountRecord;
import com.mz.util.QueryFilter;
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
 * @Date:        2017-11-08 11:44:10 
 */
@Controller
@RequestMapping("/dmHotAccountRecord/exdmhotaccountrecord")
public class ExDmHotAccountRecordController extends BaseController<ExDmHotAccountRecord, Long> {
	
	@Resource(name = "exDmHotAccountRecordService")
	@Override
	public void setService(BaseService<ExDmHotAccountRecord, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看ExDmHotAccountRecord")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExDmHotAccountRecord see(@PathVariable Long id){
		ExDmHotAccountRecord exDmHotAccountRecord = service.get(id);
		return exDmHotAccountRecord;
	}
	
	@MethodName(name="增加ExDmHotAccountRecord")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExDmHotAccountRecord exDmHotAccountRecord){
		return super.save(exDmHotAccountRecord);
	}
	
	@MethodName(name="修改ExDmHotAccountRecord")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExDmHotAccountRecord exDmHotAccountRecord){
		return super.update(exDmHotAccountRecord);
	}
	
	@MethodName(name="删除ExDmHotAccountRecord")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表ExDmHotAccountRecord")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExDmHotAccountRecord.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
