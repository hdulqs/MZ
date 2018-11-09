/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gaomm
 * @version:     V1.0 
 * @Date:        2017-11-29 18:36:30 
 */
package com.mz.exchange.lend.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.lend.model.ExDmLendTimes;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.lend.service.ExDmLendTimesService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gaomm
 * @version:     V1.0 
 * @Date:        2017-11-29 18:36:30 
 */
@Controller
@RequestMapping("/lend/exdmlendtimes")
public class ExDmLendTimesController extends BaseController<ExDmLendTimes, Long> {
	
	@Resource(name = "exDmLendTimesService")
	@Override
	public void setService(BaseService<ExDmLendTimes, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看ExDmLendTimes")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public ExDmLendTimes see(@PathVariable Long id){
		ExDmLendTimes exDmLendTimes = service.get(id);
		return exDmLendTimes;
	}
	
	@MethodName(name="增加ExDmLendTimes")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,ExDmLendTimes exDmLendTimes){
		return super.save(exDmLendTimes);
	}
	
	@MethodName(name="修改ExDmLendTimes")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,ExDmLendTimes exDmLendTimes){
		return super.update(exDmLendTimes);
	}
	
	@MethodName(name="删除ExDmLendTimes")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表ExDmLendTimes")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(ExDmLendTimes.class,request);
		filter.setOrderby(" applyTime desc ");
		return super.findPage(filter);
	}
	
	
	  
    @MethodName(name = "确认通过") 
	@RequestMapping("/confirm")
	@ResponseBody
    public JsonResult confirm(HttpServletRequest request){
    	
    	String id = request.getParameter("id");
    	String lengPing = request.getParameter("lengPing");
    	String lengRiskRate = request.getParameter("lengRiskRate");
    	JsonResult jsonResult = new JsonResult();
    	if(!StringUtils.isEmpty(id)){
    		((ExDmLendTimesService)service).confirm(id,lengRiskRate,lengPing);
    			jsonResult.setSuccess(true);
        		jsonResult.setMsg("确认成功");
    	}else{
    		jsonResult.setSuccess(false);
    		jsonResult.setMsg("请选择确认数据");
    	}
    	return jsonResult;
    }
    @MethodName(name = "确认驳回") 
	@RequestMapping("/invalid")
	@ResponseBody
    public JsonResult invalid(HttpServletRequest request){
    	
    	String id = request.getParameter("id");
		String description = request.getParameter("description");
    	JsonResult jsonResult = new JsonResult();
    	if(!StringUtils.isEmpty(id)){
    		((ExDmLendTimesService)service).invalid(id,description);
    		jsonResult.setSuccess(true);
    		jsonResult.setMsg("确认成功");
    	}else{
    		jsonResult.setSuccess(false);
    		jsonResult.setMsg("请选择确认数据");
    	}
    	return jsonResult;
    }
    @MethodName(name = "充值,提现无效处理")
    @RequestMapping("/invalids")
    @ResponseBody
    public JsonResult invalids(HttpServletRequest request){
    	String post_id = request.getParameter("id");
    	JsonResult jsonResult = new JsonResult();
    	if(!StringUtils.isEmpty(post_id)){
        		((ExDmLendTimesService)service).invalids(post_id);
    	}
    	jsonResult.setSuccess(true);
		jsonResult.setMsg("确认成功");
		return jsonResult;
    }
}
