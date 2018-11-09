/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-06-20 16:08:28 
 */
package com.mz.core.mvc.controller.AppSmsSend;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.sms.send.model.AppSmsSend;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.service.AppSmsSend.AppSmsSendService;
import javax.annotation.Resource;
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
 * @Date:        2017-06-20 16:08:28 
 */
@Controller
@RequestMapping("/AppSmsSend/appsmssend")
public class AppSmsSendController extends BaseController<AppSmsSend, Long> {
	
	@Resource(name = "appSmsSendService")
	@Override
	public void setService(BaseService<AppSmsSend, Long> service) {
		super.service = service;
	}

	@Autowired
	AppSmsSendService appSmsSendService;
	
	@MethodName(name = "查看AppSmsSend")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppSmsSend see(@PathVariable Long id){
		AppSmsSend appSmsSend = service.get(id);
		return appSmsSend;
	}
	
	@MethodName(name="增加AppSmsSend")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppSmsSend appSmsSend){
		return super.save(appSmsSend);
	}
	
	@MethodName(name="修改AppSmsSend")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppSmsSend appSmsSend){
		return super.update(appSmsSend);
	}
	
	@MethodName(name="删除AppSmsSend")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppSmsSend")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppSmsSend.class,request);
		filter.setOrderby("id desc");
		return super.findPage(filter);
	}
	
}
