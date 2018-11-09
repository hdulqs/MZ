/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-28 15:16:24 
 */
package com.mz.app.log.controller;

import com.mz.app.log.model.AppLogThirdInterface;
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
 * @Date:        2017-07-28 15:16:24 
 */
@Controller
@RequestMapping("/app/applogthirdinterface")
public class AppLogThirdInterfaceController extends BaseController<AppLogThirdInterface, Long> {
	
	@Resource(name = "appLogThirdInterfaceService")
	@Override
	public void setService(BaseService<AppLogThirdInterface, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppLogThirdInterface")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppLogThirdInterface see(@PathVariable Long id){
		AppLogThirdInterface appLogThirdInterface = service.get(id);
		return appLogThirdInterface;
	}
	
	@MethodName(name="增加AppLogThirdInterface")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppLogThirdInterface appLogThirdInterface){
		return super.save(appLogThirdInterface);
	}
	
	@MethodName(name="修改AppLogThirdInterface")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppLogThirdInterface appLogThirdInterface){
		return super.update(appLogThirdInterface);
	}
	
	@MethodName(name="删除AppLogThirdInterface")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppLogThirdInterface")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppLogThirdInterface.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
