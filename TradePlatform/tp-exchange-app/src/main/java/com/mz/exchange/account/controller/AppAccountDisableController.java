/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao mimi
 * @version:     V1.0 
 * @Date:        2016-12-12 19:39:38 
 */
package com.mz.exchange.account.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.account.model.AppAccountDisable;
import com.mz.exchange.account.service.AppAccountDisableService;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao mimi
 * @version:     V1.0 
 * @Date:        2016-12-12 19:39:38 
 */
@Controller
@RequestMapping("/account/appaccountdisable")
public class AppAccountDisableController extends BaseController<AppAccountDisable, Long> {
	
	@Resource(name = "appAccountDisableService")
	@Override
	public void setService(BaseService<AppAccountDisable, Long> service) {
		super.service = service;
	}
	@Resource
	public AppAccountDisableService appAccountDisableService;

	@MethodName(name="禁用币")
	@RequestMapping(value="/coindisable")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult coindisable(HttpServletRequest request){
		JsonResult jr = new JsonResult();
	   String disableMoney=	request.getParameter("disableMoney");
	   String id=	request.getParameter("id");
	   String remark=	request.getParameter("remark");
	   
	   if(null!=disableMoney&&new BigDecimal(disableMoney).compareTo(new BigDecimal("0"))<0){
		   jr.setMsg("禁用币数量不能为0");
			jr.setSuccess(false);
			return jr;
	   }
	   return  appAccountDisableService.coindisable(disableMoney, id, remark);
		
	}
	@MethodName(name="解禁币")
	@RequestMapping(value="/encoindisable/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult encoindisable(@PathVariable String ids){
	
		
		return appAccountDisableService.encoindisable(ids);
	}
	
	@MethodName(name = "列表AppAccountDisable")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppAccountDisable.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
