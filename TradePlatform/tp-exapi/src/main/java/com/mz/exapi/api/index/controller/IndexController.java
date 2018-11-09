package com.mz.exapi.api.index.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.thread.ThreadPool;
import com.mz.exapi.constant.APICodeConstant;
import com.mz.exapi.util.EmailRunnable;
import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.PropertiesUtils;
import com.mz.util.Validators;
import com.mz.util.sys.SpringContextUtil;
import com.mz.manage.remote.RemoteApiService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/api/index")
public class IndexController {

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JsonResult appreg(HttpServletRequest request, HttpServletResponse response, @RequestParam String username,
			@RequestParam String password,@RequestParam Locale locale) {
		String referralCode = request.getParameter("referralCode");
		
		
		
		if (StringUtils.isEmpty(username)) {
			return new JsonResult().setMsg("邮箱不能为空").setSuccess(false).setCode(APICodeConstant.CODE_Error_Tips);
		}
		if(!Validators.isEmail(username)){
			return new JsonResult().setMsg("邮箱格式不正确").setSuccess(false).setCode(APICodeConstant.CODE_Error_Tips);
		}
		if (StringUtils.isEmpty(password)) {
			return new JsonResult().setMsg("密码不能为空").setSuccess(false).setCode(APICodeConstant.CODE_Error_Tips);
		}
		if(!Validators.isPassword(password)){
			return new JsonResult().setMsg("密码格式不正确").setSuccess(false).setCode(APICodeConstant.CODE_Error_Tips);
		}
		if (StringUtils.isEmpty(locale)) {
			return new JsonResult().setMsg("语言不能为空").setSuccess(false).setCode(APICodeConstant.CODE_Error_Tips);
		}
		if (!"".equals(referralCode) && referralCode != null) {
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult selectPhone = remoteManageService.selectAgent(referralCode);
			if (!selectPhone.getSuccess()) {
				return new JsonResult().setSuccess(false).setMsg("推荐码不存在").setCode(APICodeConstant.CODE_Error_Tips);
			}
		}
		try {
			RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");

			// 后台配置的法币类型
			String language_code = null;
			RedisService redisService = SpringContextUtil.getBean("redisService");
			String config = redisService.get("configCache:all");
			if (!StringUtils.isEmpty(config)) {
				JSONObject parseObject = JSONObject.parseObject(config);
				language_code = parseObject.get("language_code").toString();
			}

			RemoteResult regist = manageService.regist(username, password,referralCode, language_code);
			if (regist != null) {
				if (regist.getSuccess()) {
					// 发送邮件
					StringBuffer sb = new StringBuffer();
					sb.append(SpringContextUtil.diff("dear") + " " + username + "<br><br>" + SpringContextUtil.diff("regestone")
							+ "<br><br>" + SpringContextUtil.diff("regesttwo") + "<br><br>" + SpringContextUtil.diff("browseropen")
							+ "<br><br>");
					sb.append(PropertiesUtils.APP.getProperty("app.url"));
					sb.append("/activation/" + username + "/" + regist.getObj() + "<br><br>");
					String type = "1";
					ThreadPool.exe(new EmailRunnable(username,sb.toString(),type,locale));
					return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success","zh_CN")).setCode(APICodeConstant.CODE_SUCCESS);
				} else {
					return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg(),"zh_CN")).setCode(APICodeConstant.CODE_Error_Tips).setSuccess(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult().setSuccess(false).setMsg("注册方法远程调用出错").setCode(APICodeConstant.CODE_Internal_Error);
		}
		return new JsonResult();
	}
	
	
	
	@RequestMapping(value = "/applogin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JsonResult applogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String username,
			@RequestParam String password) {
		
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new JsonResult().setMsg("用户或密码不能为空!");
		}

		try {
			UUID uuid = UUID.randomUUID();
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult login = remoteManageService.login(username, password, uuid.toString());
			if (login != null && login.getSuccess()) {
				User user = (User) login.getObj();
					return new JsonResult().setSuccess(true);
				} else {
					return new JsonResult().setSuccess(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new JsonResult().setSuccess(false);
	}
	
	
	@RequestMapping(value = "/appSendFrom", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JsonResult appSendFrom(String userName,String toAddress, String amount, String coinCode,String transactionNum) {
		RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
		return null;
	}
	
}
