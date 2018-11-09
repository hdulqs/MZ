/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月24日 下午3:35:55
 */
package com.mz.web.schedule.controller;

import com.mz.core.annotation.NoLogin;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年5月24日 下午3:35:55 
 */
@Controller
@RequestMapping("/schedule/scheduleJob")
public class ScheduleJobController  {
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class,
				new DateTimePropertyEditorSupport());

		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class,
				new StringPropertyEditorSupport(true, false));
	}
	
	
	@MethodName(name = "查询定时器记录")
	@RequestMapping(value = "/list")
	@NoLogin
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		return null;/*
		QuartzManager.findAllJobs();
		MongoUtil<ScheduleJob, Long> mongoUtil = new MongoUtil<ScheduleJob, Long>(ScheduleJob.class);
		MongoQueryFilter mongoQueryFilter = new MongoQueryFilter(request);
		mongoQueryFilter.setNosaas();
		return mongoUtil.findPage(mongoQueryFilter);
	*/}
	
}
