package com.mz.trade.mq.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.trade.mq.service.MessageProducer;

@Controller
@RequestMapping("/test")
public class TestController {

	/**
	 * 注册类型属性编辑器
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {

		// 系统注入的只能是基本类型，如int，char，String

		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
	}



	
	
	@Resource 
	private MessageProducer messageProducer;
	
	
	
	@RequestMapping("sell")
	@ResponseBody
	public JsonResult sell(HttpServletRequest request, HttpServletResponse response) {
		
		
		return new JsonResult();
		
	}

	

}
