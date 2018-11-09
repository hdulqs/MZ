/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 10:22:22 
 */
package com.mz.ex.digitalmoneyAccount.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ex.digitalmoneyAccount.model.ExDigitalmoneyAccount;
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
 * @Date:        2017-11-08 10:22:22 S
 */
@Controller
@RequestMapping("/digitalmoneyAccount/exdigitalmoneyaccount")
public class ExDigitalmoneyAccountController extends BaseController<ExDigitalmoneyAccount, Long> {
	
	@Resource(name = "exDigitalmoneyAccountService")
	@Override
	public void setService(BaseService<ExDigitalmoneyAccount, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看ExDigitalmoneyAccount")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExDigitalmoneyAccount see(@PathVariable Long id){
		ExDigitalmoneyAccount exDigitalmoneyAccount = service.get(id);
		return exDigitalmoneyAccount;
	}
	
	@MethodName(name="增加ExDigitalmoneyAccount")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExDigitalmoneyAccount exDigitalmoneyAccount){
		return super.save(exDigitalmoneyAccount);
	}
	
	@MethodName(name="修改ExDigitalmoneyAccount")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExDigitalmoneyAccount exDigitalmoneyAccount){
		return super.update(exDigitalmoneyAccount);
	}
	
	@MethodName(name="删除ExDigitalmoneyAccount")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表ExDigitalmoneyAccount")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExDigitalmoneyAccount.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
