/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-08 11:13:05 
 */
package com.mz.ex.dmTransaction.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ex.dmTransaction.model.ExDmTransaction;
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
 * @Date:        2017-11-08 11:13:05 
 */
@Controller
@RequestMapping("/dmTransaction/exdmtransaction")
public class ExDmTransactionController extends BaseController<ExDmTransaction, Long> {
	
	@Resource(name = "exDmTransactionService")
	@Override
	public void setService(BaseService<ExDmTransaction, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看ExDmTransaction")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExDmTransaction see(@PathVariable Long id){
		ExDmTransaction exDmTransaction = service.get(id);
		return exDmTransaction;
	}
	
	@MethodName(name="增加ExDmTransaction")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExDmTransaction exDmTransaction){
		return super.save(exDmTransaction);
	}
	
	@MethodName(name="修改ExDmTransaction")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExDmTransaction exDmTransaction){
		return super.update(exDmTransaction);
	}
	
	@MethodName(name="删除ExDmTransaction")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表ExDmTransaction")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExDmTransaction.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
