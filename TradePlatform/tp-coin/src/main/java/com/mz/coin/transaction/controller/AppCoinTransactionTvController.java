/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-12-04 17:19:36 
 */
package com.mz.coin.transaction.controller;

import com.mz.coin.transaction.model.AppCoinTransactionTv;
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
 * @Date:        2017-12-04 17:19:36 
 */
@Controller
@RequestMapping("/transaction/appcointransactiontv")
public class AppCoinTransactionTvController extends BaseController<AppCoinTransactionTv, Long> {
	
	@Resource(name = "appCoinTransactionTvService")
	@Override
	public void setService(BaseService<AppCoinTransactionTv, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppCoinTransactionTv")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCoinTransactionTv see(@PathVariable Long id){
		AppCoinTransactionTv appCoinTransactionTv = service.get(id);
		return appCoinTransactionTv;
	}
	
	@MethodName(name="增加AppCoinTransactionTv")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppCoinTransactionTv appCoinTransactionTv){
		return super.save(appCoinTransactionTv);
	}
	
	@MethodName(name="修改AppCoinTransactionTv")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppCoinTransactionTv appCoinTransactionTv){
		return super.update(appCoinTransactionTv);
	}
	
	@MethodName(name="删除AppCoinTransactionTv")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppCoinTransactionTv")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCoinTransactionTv.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
