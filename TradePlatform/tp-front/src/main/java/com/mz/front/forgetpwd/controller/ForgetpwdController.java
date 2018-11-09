package com.mz.front.forgetpwd.controller;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.util.sys.SpringContextUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/forgetpwd")
public class ForgetpwdController {

	@RequestMapping("/forgetpwd/{type}")
	public String forgetpwd1(@PathVariable String type, HttpServletRequest request) {
		if ("1".equals(type)) {
			return "forgetpwd1";
		} else if ("2".equals(type)) {
			String backTel = (String) request.getSession().getAttribute("backTel");
			if (backTel == null) {
				return "forgetpwd1";
			} else {
				return "forgetpwd2";
			}
		} else if ("3".equals(type)) {
			String backTel = (String) request.getSession().getAttribute("backTel");
			if (backTel == null) {
				return "forgetpwd1";
			} else {
				return "forgetpwd3";
			}
		}
		return null;
	}
	
	@RequestMapping("/forgetpwdphone/{type}")
	public String forgetpwdPhone(@PathVariable String type, HttpServletRequest request) {
		if ("1".equals(type)) {
			return "forgetpwdPhone1";
		} else if ("2".equals(type)) {			
				return "forgetpwdPhone2";			
		}
		return null;
	}

	/**
	 * 找回密码第一步
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/firststep")
	@ResponseBody
	public JsonResult firststep(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String code = request.getParameter("code");
		String phoneNum = request.getParameter("phoneNum");
		String smsfindloginpw = (String) request.getSession().getAttribute("smsfindloginpw" + phoneNum);
		if (!code.equals(smsfindloginpw)) {
			return jsonResult.setSuccess(false).setMsg("验证码不正确");
		} else {
			request.getSession().removeAttribute("smsfindloginpw" + phoneNum);
			request.getSession().setAttribute("backTel", phoneNum);
			return jsonResult.setSuccess(true).setObj(phoneNum);
		}
	}

	/**
	 * 找回密码第二步
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/secondstep")
	@ResponseBody
	public JsonResult secondstep(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String passwd = request.getParameter("passwd");
		// 从session中取邮箱账号
		String email = (String) request.getSession().getAttribute("forgetEmail");
		// 如果session过期
		if (StringUtils.isEmpty(email)) {
			jsonResult.setSuccess(false).setMsg(SpringContextUtil.diff("chongzhimimashibai"));
		}
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		RemoteResult result = remoteManageService.updatepwdemail(passwd, email);
		if (!result.getSuccess()) {
			return jsonResult.setSuccess(false).setMsg(SpringContextUtil.diff("chongzhimimashibai"));
		}
		return jsonResult.setSuccess(true).setMsg(SpringContextUtil.diff("chongzhimimachenggong"));
	}
	
	/**
	 * 手机找回密码第二步
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/secondstep2")
	@ResponseBody
	public JsonResult secondstep2(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String passwd = request.getParameter("passwd");	
		// 从session中取邮箱账号
		String username = (String) request.getSession().getAttribute("backTel");
		// 如果session过期
		if (StringUtils.isEmpty(username)) {
			jsonResult.setSuccess(false).setMsg(SpringContextUtil.diff("chongzhimimashibai"));
		}
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		RemoteResult result = remoteManageService.updatepwdemail(passwd, username);
		if (!result.getSuccess()) {
			return jsonResult.setSuccess(false).setMsg(SpringContextUtil.diff("chongzhimimashibai"));
		}
		return jsonResult.setSuccess(true).setMsg(SpringContextUtil.diff("chongzhimimachenggong"));
	}

	/**
	 * 找回密码第二步
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/codevail")
	@ResponseBody
	public JsonResult codevail(HttpServletRequest request) {
		// session图形验证码
		// 图形验证码
		String registCode = request.getParameter("registCode");
		if (StringUtils.isEmpty(registCode)) {
			return new JsonResult().setMsg(SpringContextUtil.diff("tx_is_not_null"));
		}
		String session_registcode = (String) request.getSession().getAttribute("registCode");
		if (!registCode.equalsIgnoreCase(session_registcode)) {
			return new JsonResult().setMsg(SpringContextUtil.diff("tx_error"));
		}
		return new JsonResult().setSuccess(true);
	}
}
