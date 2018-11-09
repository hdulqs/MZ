package com.mz.front.user.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.mz.front.notice.Notice;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/phone")
public class PhoneController {

	/**
	 * 
	 * @param codes
	 * @param 手机认证
	 * @param request
	 * @return authcode
	 */
	
    @RequestMapping("/setPhone")
	@ResponseBody
    public JsonResult setPhone(String codes, String savedSecret,HttpServletRequest request) {  
		String pwSmsCode = request.getParameter("verifyCode");//短信验证码
		String mobile="";
		mobile = request.getParameter("mobile");
		String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);
		User user = SessionUtils.getUser(request);
		//地区截取
		if(mobile==null&&mobile.equals("")){
			 mobile = user.getPhone();
		}
		//request.getSession().setAttribute("phone", mobile);
		if(StringUtils.isEmpty(pwSmsCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
		}
			RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult regist = manageService.regphone(mobile);
    
		if(!regist.getSuccess()){
			return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("phonechongfu"));

		}else{
			if(regist.getSuccess()){
			RedisService redisService = SpringContextUtil.getBean("redisService");
			String session_pwSmsCode = redisService.get("SMS:smsphone:"+mobile);
		 if(!pwSmsCode.equals(session_pwSmsCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
		  }
          RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        
		 RemoteResult setPhone = remoteManageService.setPhone(mobile,user.getUsername());
		//通知邮箱和旧手机号 add by zongwei 20180703
		if(setPhone!=null&&setPhone.getSuccess()) {
					boolean natice = Notice.setphoneNotice(user);
		}
		 if(setPhone!=null&&setPhone.getSuccess()){
		 user.setPhoneState(1);
		 user.setPhone(mobile);
		 //北京处理跳号问题 -- 2018.4.21
		 //SessionUtils.updateRedis(user);
		 request.getSession().setAttribute("user",user);

		 //北京处理跳号问题 -- 2018.4.21
		  }
		}
	 }
		 return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("phonesuccess"));
    }  
    
    
    
	
    @RequestMapping("/secondPhone")
	@ResponseBody
    public JsonResult secondPhone(String codes, String savedSecret,HttpServletRequest request) {  
		String mobile = request.getParameter("mobile");
		//地区截取
		User user = SessionUtils.getUser(request);
		//request.getSession().setAttribute("phone", mobile);
			/* request.getSession().setAttribute("phone", mobile);
			 request.getSession().setAttribute("phoneState", 1);
*/
          RemoteManageService remoteManageService = 	SpringContextUtil.getBean("remoteManageService");
        
		 RemoteResult setPhone = remoteManageService.setPhone(mobile,user.getUsername());
		 if(setPhone.getSuccess()){
			 user.setPhone(mobile);
			 //北京处理跳号问题 -- 2018.4.21
			 //SessionUtils.updateRedis(user);
			 request.getSession().setAttribute("user",user);
			 //北京处理跳号问题 -- 2018.4.21
			 request.getSession().setAttribute("isAuthentication", "1");
		 	return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("phonesuccess"));
		 }else{
		 	return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("phoneerror"));

		 }
    }  
    
    

	/**
	 * 
	 * @param codes
	 * @param 关闭手机认证
	 * @param request
	 * @return authcode
	 */
	
    @RequestMapping("/offPhone")
	@ResponseBody
    public JsonResult offPhone(String codes, String savedSecret,HttpServletRequest request) {  
		String pwSmsCode = request.getParameter("verifyCode");//短信验证码
		String mobile = request.getParameter("mobile");//短信验证码

		//地区截取
		User user = SessionUtils.getUser(request);
		if(StringUtils.isEmpty(pwSmsCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
		}
		//String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String session_pwSmsCode = redisService.get("SMS:smsphone:"+user.getPhone());
		if(!pwSmsCode.equals(session_pwSmsCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
		}
        RemoteManageService remoteManageService = 	SpringContextUtil.getBean("remoteManageService");
		RemoteResult offPhone = remoteManageService.offPhone(mobile,user.getUsername());
		 if(offPhone.getSuccess()){
			 user.setPhoneState(0);
			 user.setPassDate(new Date());
			 //北京处理跳号问题 -- 2018.4.21
			 //SessionUtils.updateRedis(user);
			 request.getSession().setAttribute("user",user);
			 //北京处理跳号问题 -- 2018.4.21
			return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("offphonesu"));

		 }else{
			return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("offphoneer"));

		 }
    }  
    
    /**
	 * 
	 * @param codes
	 * @param 关闭手机认证(新增功能2018.4.27)
	 * @param request
	 * @return authcode
	 */
	
    @RequestMapping("/offLoginPhone")
	@ResponseBody
    public JsonResult offLoginPhone(HttpServletRequest request) {  
		//String pwSmsCode = request.getParameter("verifyCode");//短信验证码
		String mobile = request.getParameter("mobile");//短信验证码

		//地区截取
		User user = SessionUtils.getUser(request);

		//if(StringUtils.isEmpty(pwSmsCode)){
			//return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
		//}
		//String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
		//RedisService redisService = SpringContextUtil.getBean("redisService");
		//String session_pwSmsCode = redisService.get("SMS:smsphone:"+user.getPhone());
		//if(!pwSmsCode.equals(session_pwSmsCode)){
			//return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
		//}
        RemoteManageService remoteManageService = 	SpringContextUtil.getBean("remoteManageService");
		RemoteResult offPhone = remoteManageService.offLoginPhone(mobile,user.getUsername());
		 if(offPhone.getSuccess()){
			 user.setCheckStates(0);
			 //user.setPassDate(new Date());
			 //北京处理跳号问题 -- 2018.4.21
			 SessionUtils.updateRedis(user);
			 request.getSession().setAttribute("user",user);
			 //北京处理跳号问题 -- 2018.4.21
			return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("colseSMSsuccess"));

		 }else{
			return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("offphoneer"));

		 }
    }
    
    /**
	 * 
	 * @param codes
	 * @param 开启手机认证(新增功能2018.4.27)
	 * @param request
	 * @return authcode
	 */
	
    @RequestMapping("/setLoginPhone")
	@ResponseBody
    public JsonResult setLoginPhone(HttpServletRequest request) {  
		//String pwSmsCode = request.getParameter("verifyCode");//短信验证码
		String mobile = request.getParameter("mobile");//短信验证码

		//地区截取
		User user = SessionUtils.getUser(request);

		//if(StringUtils.isEmpty(pwSmsCode)){
			//return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
		//}
		//String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
		//RedisService redisService = SpringContextUtil.getBean("redisService");
		//String session_pwSmsCode = redisService.get("SMS:smsphone:"+user.getPhone());
		//if(!pwSmsCode.equals(session_pwSmsCode)){
			//return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
		//}
        RemoteManageService remoteManageService = 	SpringContextUtil.getBean("remoteManageService");
		RemoteResult offPhone = remoteManageService.setLoginPhone(mobile,user.getUsername());
		 if(offPhone.getSuccess()){
			 user.setCheckStates(1);
			 //user.setPassDate(new Date());
			 //北京处理跳号问题 -- 2018.4.21
			 SessionUtils.updateRedis(user);
			 request.getSession().setAttribute("user",user);
			 //北京处理跳号问题 -- 2018.4.21
			return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("openSMSsuccess"));

		 }else{
			return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("offphoneer"));

		 }
    }
	
    /**
	 * 
	 * @param codes
	 * @param 手机二次认证
	 * @param request
	 * @return
	 */
	
    @RequestMapping("/PhoneAuth")
	@ResponseBody
    public JsonResult PhoneAuth(String codes, String savedSecret,HttpServletRequest request) {  
		String pwSmsCode = request.getParameter("verifyCode");//短信验证码
		String username = request.getParameter("username");
		User user =null;
		if(username!=null){
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		 user = remoteManageService.selectByTel(username);
		}else{
			 user = SessionUtils.getUser(request);
		}
		if(StringUtils.isEmpty(pwSmsCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_is_not_null"));
		}
		
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String session_pwSmsCode = redisService.get("SMS:smsphone:"+user.getPhone());
		//String session_pwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
		if(!pwSmsCode.equals(session_pwSmsCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
		}
		request.getSession().setAttribute("isAuthentication", "1");
		return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("phonesuccess"));
		

    }


	@RequestMapping("setphonetwo")
	public ModelAndView setphonetwo(HttpServletRequest request) {
		String verifyCode = request.getParameter("verifyCode");
	    User user = SessionUtils.getUser(request);
		ModelAndView mav = new ModelAndView();

		RedisService redisService = SpringContextUtil.getBean("redisService");
		String session_verifyCode = redisService.get("SMS:smsphone:" + user.getPhone());
		if (verifyCode.equals(session_verifyCode)) {
			mav.setViewName("front/user/setphone");
			// 默认生成html静态页，配置false不生成
			mav.addObject("CREATE_HTML", false);

			mav.addObject("a", "3");

			mav.addObject("verifyCode", "verifyCode");
			;
		} else {
			mav.setViewName("front/user/setphoneone");
			// 默认生成html静态页，配置false不生成
			mav.addObject("CREATE_HTML", false);

			mav.addObject("a", "3");
			if(user.getPhone() != null){
				String  phone = user.getPhone();
				phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
				mav.addObject("phoneHide", phone);
			}
			mav.addObject("meg", "短信验证码不对");
		}

		return mav;
	}
    
    
  
}
