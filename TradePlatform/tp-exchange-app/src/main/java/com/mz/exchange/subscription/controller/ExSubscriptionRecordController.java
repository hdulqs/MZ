/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zenghao
 * @version:     V1.0 
 * @Date:        2016-11-22 18:36:28 
 */
package com.mz.exchange.subscription.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.subscription.model.ExSubscriptionRecord;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.subscription.service.ExSubscriptionRecordService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zenghao
 * @version:     V1.0 
 * @Date:        2016-11-22 18:36:28 
 */
@Controller
@RequestMapping("/subscription/exsubscriptionrecord")
public class ExSubscriptionRecordController extends BaseController<ExSubscriptionRecord, Long> {
	
	@Resource(name = "exSubscriptionRecordService")
	@Override
	public void setService(BaseService<ExSubscriptionRecord, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看ExSubscriptionRecord")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExSubscriptionRecord see(@PathVariable Long id){
		ExSubscriptionRecord exSubscriptionRecord = service.get(id);
		return exSubscriptionRecord;
	}
	
	@MethodName(name="增加ExSubscriptionRecord")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExSubscriptionRecord exSubscriptionRecord){
		return super.save(exSubscriptionRecord);
	}
	
	@MethodName(name="修改ExSubscriptionRecord")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExSubscriptionRecord exSubscriptionRecord){
		return super.update(exSubscriptionRecord);
	}
	
	@MethodName(name="删除ExSubscriptionRecord")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表ExSubscriptionRecord")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		System.out.println(request.getParameter("state_EQ"));
		QueryFilter filter = new QueryFilter(ExSubscriptionRecord.class,request);
		filter.setOrderby("time desc");
		return super.findPage(filter);
	}
	@MethodName(name = "回购ExSubscriptionRecord")
	@RequestMapping(value="/buyBack", method = RequestMethod.POST)
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult buyBack(HttpServletRequest req){
		JsonResult jr = new JsonResult();
		String id = req.getParameter("id");
		String buyBackNum = req.getParameter("buyBackNum");
		if(null!=id&&buyBackNum!=null){
			ExSubscriptionRecordService exSubscriptionRecordService = (ExSubscriptionRecordService)service;
			jr = exSubscriptionRecordService.saveBuyBack(id,buyBackNum);
		}else{
			jr.setMsg("回购失败");
			jr.setSuccess(false);
		}
		return jr;
	}
}
