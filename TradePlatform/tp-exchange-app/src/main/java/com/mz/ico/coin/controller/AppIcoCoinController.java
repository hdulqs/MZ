/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-17 18:22:21 
 */
package com.mz.ico.coin.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.coin.model.AppIcoCoin;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.ico.coin.service.AppIcoCoinService;
import java.util.List;
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
 * @Date:        2017-08-17 18:22:21 
 */
@Controller
@RequestMapping("/coin/appicocoin")
public class AppIcoCoinController extends BaseController<AppIcoCoin, Long> {
	
	@Resource(name = "appIcoCoinService")
	@Override
	public void setService(BaseService<AppIcoCoin, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoCoin")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoCoin see(@PathVariable Long id){
		AppIcoCoin appIcoCoin = service.get(id);
		return appIcoCoin;
	}
	
	@MethodName(name="增加AppIcoCoin")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoCoin appIcoCoin){
		return super.save(appIcoCoin);
	}
	
	@MethodName(name="修改AppIcoCoin")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoCoin appIcoCoin){
		return super.update(appIcoCoin);
	}
	
	@MethodName(name="删除AppIcoCoin")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoCoin")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		((AppIcoCoinService)service).initIcoRedisCode();
		QueryFilter filter = new QueryFilter(AppIcoCoin.class,request);
		String issueState=request.getParameter("states");
		if(issueState!=null&&!"".equals(issueState)){
			filter.addFilter("issueState=", issueState);
		}
		return super.findPage(filter);
	}
	
	
	@MethodName(name = "下拉菜单")
	@RequestMapping("/selects")
	@ResponseBody
	public List<AppIcoCoin> selects(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoCoin.class,request);
		String issueState=request.getParameter("states");
		if(issueState!=null&&!"".equals(issueState)){
			filter.addFilter("issueState=", issueState);
		}
		return service.find(filter);
	}
}
