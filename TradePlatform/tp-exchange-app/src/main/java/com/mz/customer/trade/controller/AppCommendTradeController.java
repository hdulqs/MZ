/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
package com.mz.customer.trade.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.trade.model.AppCommendTrade;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.trade.service.AppCommendTradeService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
@Controller
@RequestMapping("/trade/appcommendtrade")
public class AppCommendTradeController extends BaseController<AppCommendTrade, Long> {
	
	@Resource
	private AppCommendTradeService AppCommendTradeServiceImpl;
	
	
	@Resource(name = "appCommendTradeService")
	@Override
	public void setService(BaseService<AppCommendTrade, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppCommendTrade")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCommendTrade see(@PathVariable Long id){
		AppCommendTrade appCommendTrade = service.get(id);
		return appCommendTrade;
	}
	
	@MethodName(name="增加AppCommendTrade")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppCommendTrade appCommendTrade){
		return super.save(appCommendTrade);
	}
	
	@MethodName(name="修改AppCommendTrade")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppCommendTrade appCommendTrade){
		return super.update(appCommendTrade);
	}
	
	@MethodName(name="删除AppCommendTrade")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppCommendTrade")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCommendTrade.class,request);
		filter.setOrderby("created desc");
		PageResult page = super.findPage(filter);
		//List<AppCommendTrade> appCommendTrade =AppCommendTradeServiceImpl.findList();
	    //page.setRows(appCommendTrade);
		return page;
	}
	
	@MethodName(name = "列表AppCommendTrade")
	@RequestMapping(value="/total/{id}")
	@ResponseBody
	public AppCommendTrade total(HttpServletRequest request,@PathVariable String id){
		QueryFilter filter = new QueryFilter(AppCommendTrade.class,request);
		//filter.setOrderby("created desc");
		//PageResult page = super.findPage(filter);
		AppCommendTrade appCommendTrade =AppCommendTradeServiceImpl.findList(id);
	    //page.setRows(appCommendTrade);
		return appCommendTrade;
	}
	
}
