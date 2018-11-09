/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-19 13:40:56 
 */
package com.mz.ico.project.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.ico.project.model.AppIcoProject;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-19 13:40:56 
 */
@Controller
@RequestMapping("/project/appicoproject")
public class AppIcoProjectController extends BaseController<AppIcoProject, Long> {
	
	@Resource(name = "appIcoProjectService")
	@Override
	public void setService(BaseService<AppIcoProject, Long> service) {
		super.service = service;
	}
	
	@MethodName(name = "查看AppIcoProject")
	@RequestMapping(value="/see/{id}")
	@MyRequiresPermissions
	@ResponseBody
	public AppIcoProject see(@PathVariable Long id){
		AppIcoProject appIcoProject = service.get(id);
		return appIcoProject;
	}
	
	@MethodName(name="增加AppIcoProject")
	@RequestMapping(value="/add")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult add(HttpServletRequest request,AppIcoProject appIcoProject){
		return super.save(appIcoProject);
	}
	
	@MethodName(name="修改AppIcoProject")
	@RequestMapping(value="/modify")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult modify(HttpServletRequest request,AppIcoProject appIcoProject){
		return super.update(appIcoProject);
	}
	
	@MethodName(name="删除AppIcoProject")
	@RequestMapping(value="/remove/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(@PathVariable String ids){
		return super.deleteBatch(ids);
	}
	
	@MethodName(name = "列表AppIcoProject")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request){
		QueryFilter filter = new QueryFilter(AppIcoProject.class,request);
		return super.findPage(filter);
	}
	
	@MethodName(name="项目审核")
	@RequestMapping(value="/audit/{ids}")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult audit(@PathVariable String ids,HttpServletRequest request){
		JsonResult jsonResult=new JsonResult();
		jsonResult.setSuccess(false);
		String isAgree=request.getParameter("isAgree");
		Integer status=null;
		if(StringUtils.isNotEmpty(isAgree)&&StringUtils.isNotEmpty(ids)){
			if(Boolean.parseBoolean(isAgree)){
				status=Integer.valueOf(3);//通过
			}else{
				status=Integer.valueOf(2);//未通过
			}
			//审核通过可能还伴有其他业务，因此使用for循环
			String[] arrId=ids.split(",");
			for(String id:arrId){
				AppIcoProject appIcoProject=service.get(Long.valueOf(id));
				appIcoProject.setStatus(status);
				Date starttime=new Date();
				appIcoProject.setStartTime(starttime);
				appIcoProject.setEndTime(DateUtil.addDaysToDate(starttime,appIcoProject.getIcoDays()));
				service.update(appIcoProject);
			}
			jsonResult.setSuccess(true);
		}
		return jsonResult;
	}
	
}
