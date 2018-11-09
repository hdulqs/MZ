/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-17 18:26:08 
 */
package com.mz.ico.coinAccount.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;
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
 * @Date:        2017-08-17 18:26:08 
 */
@Controller
@RequestMapping("/coinAccount/appicocoinaccount")
public class AppIcoCoinAccountController extends BaseController<AppIcoCoinAccount, Long> {
	
	@Resource(name = "appIcoCoinAccountService")
	@Override
	public void setService(BaseService<AppIcoCoinAccount, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoCoinAccount")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoCoinAccount see(@PathVariable Long id){
		AppIcoCoinAccount appIcoCoinAccount = service.get(id);
		return appIcoCoinAccount;
	}
	
	@MethodName(name="增加AppIcoCoinAccount")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoCoinAccount appIcoCoinAccount){
		return super.save(appIcoCoinAccount);
	}
	
	@MethodName(name="修改AppIcoCoinAccount")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoCoinAccount appIcoCoinAccount){
		return super.update(appIcoCoinAccount);
	}
	
	@MethodName(name="删除AppIcoCoinAccount")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoCoinAccount")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoCoinAccount.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
