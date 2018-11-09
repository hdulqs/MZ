/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-09-18 15:00:44 
 */
package com.mz.coin.coin.controller;

import com.mz.coin.coin.model.AppCoinTransaction;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
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
 * @Date:        2017-09-18 15:00:44 
 */
@Controller
@RequestMapping("/coin/appcointransaction")
public class AppCoinTransactionController extends BaseController<AppCoinTransaction, Long> {
	
	@Resource(name = "appCoinTransactionService")
	@Override
	public void setService(BaseService<AppCoinTransaction, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppCoinTransaction")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCoinTransaction see(@PathVariable Long id){
		AppCoinTransaction appCoinTransaction = service.get(id);
		return appCoinTransaction;
	}
	
	@MethodName(name="增加AppCoinTransaction")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppCoinTransaction appCoinTransaction){
		return super.save(appCoinTransaction);
	}
	
	@MethodName(name="修改AppCoinTransaction")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppCoinTransaction appCoinTransaction){
		return super.update(appCoinTransaction);
	}
	
	@MethodName(name="删除AppCoinTransaction")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppCoinTransaction")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCoinTransaction.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
