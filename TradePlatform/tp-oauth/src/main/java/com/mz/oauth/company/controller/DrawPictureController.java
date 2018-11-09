/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年1月13日 上午10:30:07
 */
package com.mz.oauth.company.controller;

import com.mz.oauth.user.model.AppOrganization;
import com.mz.shiro.service.AppOrganizationService;
import com.mz.core.annotation.base.MethodName;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年1月13日 上午10:30:07
 */
@Controller
@RequestMapping("/company/drawpicture")
public class DrawPictureController {
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
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
	private AppOrganizationService appOrganizationService;
	
	@MethodName(name = "查看")
	@RequestMapping(value="/rjgxt")
	public void rjgxt(HttpServletResponse response,HttpServletRequest request){
		List<AppOrganization> list = appOrganizationService.findAll();
	}
	
	
	public synchronized void test1(String name ){
		
		System.out.println(name);
		
	}
	
	
	
	
	
}
