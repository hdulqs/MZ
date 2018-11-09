/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年4月29日 下午5:02:03
 */
package com.mz.web.app.controller;


import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppApi;
import com.mz.web.app.model.AppApiParam;
import com.mz.web.app.service.AppApiParamService;
import com.mz.web.app.service.AppApiService;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年8月22日 下午4:49:57
 */
@Controller
@RequestMapping("/app/appapi")
public class AppApiController extends BaseController<AppApi, Long> {

	@Resource(name = "appApiService")
	@Override
	public void setService(BaseService<AppApi, Long> service) {
		super.service = service;
	}

	@Resource(name = "appApiParamService")
	private AppApiParamService appApiParamService;

	@MethodName(name = "修改api")
	@RequestMapping("/modify")
	@ResponseBody
	// BindingResult result
	public JsonResult modify(AppApi appApi){
		JsonResult jsoreult = super.update(appApi);
		return jsoreult;
	}

	@MethodName(name = "添加api")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(AppApi appApi) {

	

		JsonResult jsonResult = super.save(appApi);

		return jsonResult;
	}

	@MethodName(name = "删除banner")
	@RequestMapping("/remove")
	@ResponseBody
	public JsonResult remove(@RequestParam(value = "ids[]") Long[] ids) {
		AppApiService aservice = (AppApiService) service;
		JsonResult jsonResult = new JsonResult();
		boolean s = aservice.delete(ids[0]);
		if(s){
			jsonResult.setSuccess(true);
			jsonResult.setMsg("删除成功");
		}else{
			jsonResult.setSuccess(false);
			jsonResult.setMsg("删除失败");
		}
		return jsonResult;
	}



	@MethodName(name = "加载api接口列表")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppApi.class, request);
		filter.setOrderby("sort asc");
		
		return super.findPage(filter);
	}
	

	@MethodName(name = "查看一个api接口")
	@RequestMapping("/see/{id}")
	@ResponseBody
	public AppApi see(@PathVariable Long id) {
		return service.get(id);
	}

	
	@MethodName(name = "查看一个api接口详情")
	@RequestMapping("/seeinfo/{id}")
	@ResponseBody
	public AppApi seeinfo(@PathVariable Long id) {
		QueryFilter filter = new QueryFilter(AppApiParam.class);
		AppApi appApi=service.get(id);
		filter.addFilter("apiId=", appApi.getId());
		List<AppApiParam>  list=appApiParamService.find(filter);
		appApi.setList(list);
		return appApi;
	}
}
