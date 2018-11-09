/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2016-11-10 20:31:53 
 */
package com.mz.web.quartz.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.quartz.model.AppQuartzJob;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.quartz.QuartzEngine;
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
 * @Date:        2016-11-10 20:31:53 
 */
@Controller
@RequestMapping("/quartz/appquartzjob")
public class AppQuartzJobController extends BaseController<AppQuartzJob, Long> {
	
	@Resource(name = "appQuartzJobService")
	@Override
	public void setService(BaseService<AppQuartzJob, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppQuartzJob")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppQuartzJob see(@PathVariable Long id){
		AppQuartzJob appQuartzJob = service.get(id);
		return appQuartzJob;
	}
	
	@MethodName(name="增加AppQuartzJob")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppQuartzJob appQuartzJob){
		return super.save(appQuartzJob);
	}
	
	@MethodName(name="修改AppQuartzJob")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppQuartzJob appQuartzJob){
		return super.update(appQuartzJob);
	}
	
	@MethodName(name="启动AppQuartzJob")
	@RequestMapping(value="/start")
	@ResponseBody
	public JsonResult start(HttpServletRequest request,Long id){
		AppQuartzJob appQuartzJob = service.get(id);
		//启动
		QuartzEngine.startJob(appQuartzJob);
		appQuartzJob.setStart(1);
		return super.update(appQuartzJob);
	}
	
	@MethodName(name="停止AppQuartzJob")
	@RequestMapping(value="/stop")
	@ResponseBody
	public JsonResult stop(HttpServletRequest request,Long id){
		AppQuartzJob appQuartzJob = service.get(id);
		//停止
		QuartzEngine.endJob(appQuartzJob);
		appQuartzJob.setStart(0);
		return super.update(appQuartzJob);
	}
	
	@MethodName(name="删除AppQuartzJob")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		AppQuartzJob appQuartzJob = service.get(Long.valueOf(ids));
		//停止
		QuartzEngine.endJob(appQuartzJob);
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppQuartzJob")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppQuartzJob.class,request);
		return super.findPage(filter);
	}
	
	
	
	
}
