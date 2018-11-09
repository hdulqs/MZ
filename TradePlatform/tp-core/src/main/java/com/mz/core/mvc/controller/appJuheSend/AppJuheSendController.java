/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-06-20 16:54:45 
 */
package com.mz.core.mvc.controller.appJuheSend;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.sms.send.model.AppJuheSend;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.appJuheSend.AppJuheSendService;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-06-20 16:54:45 
 */
@Controller
@RequestMapping("/appJuheSend/appjuhesend")
public class AppJuheSendController extends BaseController<AppJuheSend, Long> {
	
	/*@Resource(name = "appJuheSendService")*/
	@Override
	public void setService(BaseService<AppJuheSend, Long> service) {
		super.service = service;
	}

	@Autowired
	AppJuheSendService appJuheSendService;
	
	@MethodName(name = "查看AppJuheSend")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppJuheSend see(@PathVariable Long id){
		AppJuheSend appJuheSend = service.get(id);
		return appJuheSend;
	}
	
	@MethodName(name="增加AppJuheSend")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppJuheSend appJuheSend){
		return super.save(appJuheSend);
	}
	
	@MethodName(name="修改AppJuheSend")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppJuheSend appJuheSend){
		return super.update(appJuheSend);
	}
	
	@MethodName(name="删除AppJuheSend")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppJuheSend")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppJuheSend.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
