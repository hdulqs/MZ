/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2018-01-19 10:07:55 
 */
package com.mz.customer.user.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.user.model.AppCustomerCollection;
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
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2018-01-19 10:07:55 
 */
@Controller
@RequestMapping("/user/appcustomercollection")
public class AppCustomerCollectionController extends BaseController<AppCustomerCollection, Long> {
	
	@Resource(name = "appCustomerCollectionService")
	@Override
	public void setService(BaseService<AppCustomerCollection, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppCustomerCollection")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppCustomerCollection see(@PathVariable Long id){
		AppCustomerCollection appCustomerCollection = service.get(id);
		return appCustomerCollection;
	}
	
	@MethodName(name="增加AppCustomerCollection")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppCustomerCollection appCustomerCollection){
		return super.save(appCustomerCollection);
	}
	
	@MethodName(name="修改AppCustomerCollection")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppCustomerCollection appCustomerCollection){
		return super.update(appCustomerCollection);
	}
	
	@MethodName(name="删除AppCustomerCollection")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppCustomerCollection")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppCustomerCollection.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
