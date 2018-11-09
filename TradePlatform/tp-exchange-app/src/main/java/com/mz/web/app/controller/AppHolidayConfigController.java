/**
 * 
 */
package com.mz.web.app.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.model.AppHolidayConfig;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lvna
 *
 */
@Controller
@RequestMapping("/app/appHolidayConfig")
public class AppHolidayConfigController extends BaseController<AppHolidayConfig, Long> {

	@Resource(name="appHolidayConfigService")
	@Override
	public void setService(BaseService<AppHolidayConfig, Long> service) {
		super.service = service;
	}


	@MethodName(name = "添加节日")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(AppHolidayConfig appHolidayConfig,HttpServletRequest req) {
		
		Date beginDate2 = new Date();
		Date endDate2 = new Date();
		String beginDate = req.getParameter("beginDate");
		String endDate = req.getParameter("endDate");
		if(null != beginDate){
			beginDate2 = DateUtil.stringToDate(beginDate);
		}
		if(null != endDate){
			endDate2 = DateUtil.stringToDate(endDate);
		}
		
		appHolidayConfig.setBeginDate(beginDate2);
		appHolidayConfig.setEndDate(endDate2);
		
		JsonResult jsonResult = super.save(appHolidayConfig);
		
		//存入缓存
		List<AppHolidayConfig> findAll = super.findAll();
		if(findAll!=null&&findAll.size()>0){
			RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
			redisService.save("appholidayConfig", JSON.toJSONString(findAll));
		}

		
		return jsonResult;
	}

	@MethodName(name = "删除节日")
	@RequestMapping("/remove/{id}")
	@ResponseBody
	public JsonResult remove(@PathVariable Long id) {
		JsonResult jsonResult = super.delete(id);
		return jsonResult;
	}

	@MethodName(name = "加载节日管理列表")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppHolidayConfig.class, request);
		
		return super.findPage(filter);
	}
	

	@MethodName(name = "查看一个指定的日期")
	@RequestMapping("/see/{id}")
	@ResponseBody
	public AppHolidayConfig see(@PathVariable Long id) {
		return service.get(id);
	}

	
	@MethodName(name = "修改一个指定的日期")
	@RequestMapping("/modify")
	@ResponseBody
	public JsonResult modify(AppHolidayConfig appHolidayConfig,HttpServletRequest req) {
	
		Date beginDate2 = new Date();
		Date endDate2 = new Date();
		String beginDate = req.getParameter("beginDate");
		String endDate = req.getParameter("endDate");
		if(null != beginDate){
			beginDate2 = DateUtil.stringToDate(beginDate);
		}
		if(null != endDate){
			endDate2 = DateUtil.stringToDate(endDate);
		}
		
		appHolidayConfig.setBeginDate(beginDate2);
		appHolidayConfig.setEndDate(endDate2);
		
		JsonResult result = super.update(appHolidayConfig);
		
		//存入缓存
		List<AppHolidayConfig> findAll = super.findAll();
		if(findAll!=null&&findAll.size()>0){
			RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
			redisService.save("appholidayConfig", JSON.toJSONString(findAll));
		}
		
		return result;
	}
}
