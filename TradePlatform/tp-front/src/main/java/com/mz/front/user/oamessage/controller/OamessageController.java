package com.mz.front.user.oamessage.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteOamessageService;
import com.mz.manage.remote.model.Oamessage;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;

/**
 * 站内信操作类
 * @author tzw
 *
 * 2017年7月19日
 */
@Controller
@RequestMapping("user/oamessage")
public class OamessageController {
	
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
	
	/**
	 * 获取用户消息列表
	 * @param request
	 * @return
	 * 2017年7月19日
	 * tzw
	 */
	@ResponseBody
	@RequestMapping("/list")
	public FrontPage list(HttpServletRequest request) {
		User user=SessionUtils.getUser(request);
		RemoteOamessageService service  = SpringContextUtil.getBean("remoteOamessageService");
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		params.put("customerName", user.getMobile());
		return service.findOamessage(params);
	}
	
	
	/**
	 * 用户点击消息返回具体消息
	 * 并且将消息设置为已读
	 * @param request
	 * @return
	 * 2017年7月21日
	 * tzw
	 */
	@ResponseBody
	@RequestMapping("/read/{sid}")
	public ModelAndView read(@PathVariable Long sid,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("front/user/read");
		User user = SessionUtils.getUser(request);
		if (user != null) {//如果登录了
			if (sid != null) {
				//获取远程调用service
				RemoteOamessageService service  = SpringContextUtil.getBean("remoteOamessageService");
				Oamessage oamessage = service.get(sid);
				//是否本人操作
				if (oamessage.getCustomerId().longValue() == user.getCustomerId().longValue()) {
					Oamessage read = service.read(sid);
					mav.getModel().put("content", read.getContent());
				}else {
					mav.getModel().put("content", "系统错误请重试！");
				}
			}
		}
		return mav;
	}
	

}
