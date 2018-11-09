package com.mz.front.user.trades.controller;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import com.mz.front.index.controller.IndexController;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.util.IpUtil;
import com.mz.util.common.Constant;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/trades")
public class TradesTestController {
	
	private final static Logger log = Logger.getLogger(IndexController.class);
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

	//coinCode:MGB_CNY,entrustWay：1现价2市价，entrustPrice（价格）entrustCount（量）
	//两种情况1， coinCode:MGB_CNY,entrustWay：1现价，entrustPrice（价格）entrustCount（量）（默认情况为限价）
	//     1， coinCode:MGB_CNY,entrustWay：2市价，entrustSum总价
	@RequestMapping(value = "/testBuy",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult testBuy(EntrustTrade exEntrust, HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
	//    User user = SessionUtils.getUser(request);
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		if(StringUtils.isEmpty(userName)){
			return new JsonResult().setMsg(SpringContextUtil.diff("login_nameorpwd_erro"));
		}
		if(StringUtils.isEmpty(password)){
			return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
		}
		User user =null;
		if(!StringUtils.isEmpty(userName)){
			UUID uuid = UUID.randomUUID();
			RemoteResult login = remoteManageService.login(userName,password,uuid.toString());
			if(login!=null&&login.getSuccess()){
				user = (User)login.getObj();
			}else{
				jsonResult.setSuccess(false);
	        	jsonResult.setMsg(SpringContextUtil.diff("yichang"));
	        	return jsonResult;
			}	
		
		}else{
			UUID uuid = UUID.randomUUID();
			RemoteResult login = remoteManageService.login("15101017743", "36190a648140c2321551bcf430d533b5",uuid.toString());
			user = (User)login.getObj();
		}
	
		
		// 委托之前判断
	   String[] rtd= exEntrust.getCoinCode().split("_");
	   if(rtd.length==1){
			jsonResult.setSuccess(false);
        	jsonResult.setMsg(SpringContextUtil.diff("djb_is_null"));
        	return jsonResult;
	   }else{
			exEntrust.setFixPriceCoinCode(rtd[1]);
			exEntrust.setCoinCode(rtd[0]); 
	   }
	   
	   if(null==exEntrust.getEntrustWay()){
		   exEntrust.setEntrustWay(1); //默认为现价
	   }
	   if(exEntrust.getEntrustWay().equals(1)){
		   if(null==exEntrust.getEntrustCount()){
			   jsonResult.setSuccess(false);
	        	jsonResult.setMsg(SpringContextUtil.diff("wt_no_null"));
	        	return jsonResult;
		   }
           if(null==exEntrust.getEntrustPrice()){
        	   jsonResult.setSuccess(false);
	        	jsonResult.setMsg(SpringContextUtil.diff("wtprice_no_null"));
	        	return jsonResult;
		   }
	   }
	   if(exEntrust.getEntrustWay().equals(2)){
		   if(exEntrust.getType()==1&&null==exEntrust.getEntrustSum()){
			   jsonResult.setSuccess(false);
	        	jsonResult.setMsg(SpringContextUtil.diff("wtallmoney_no_null"));
	        	return jsonResult;
		   }
           if(exEntrust.getType()==2&&null==exEntrust.getEntrustCount()){
        	   jsonResult.setSuccess(false);
	        	jsonResult.setMsg(SpringContextUtil.diff("wt_no_null"));
	        	return jsonResult;
		   }
	   }
		exEntrust.setCustomerId(user.getCustomerId());
		String[] rt = remoteManageService.addEntrustCheck(exEntrust);
        if(rt[0].equals(Constant.CODE_FAILED)){
        	String[] str = rt[1].split("::");
        	if(str.length>1){
        		jsonResult.setSuccess(false);
        		String message = SpringContextUtil.diff(str[1]);
            	jsonResult.setMsg(str[0]+message);
        	}else{
        		jsonResult.setSuccess(false);
            	jsonResult.setMsg(rt[1]);
        	}
        	return jsonResult;
        }  
        exEntrust.setSource(2);
	/*	exEntrust.setCurrencyType(ContextUtil.getCurrencyType());
		exEntrust.setWebsite(ContextUtil.getWebsite());*/
		exEntrust.setCustomerIp(IpUtil.getIp(request));
		exEntrust.setUserName(user.getMobile());
	/*	exEntrust.setTrueName(user.getTruename());
		
		exEntrust.setUserCode(user.getUserCode());
		exEntrust.setMatchPriority(1);
		exEntrust.setCustomerType(1);*/
		exEntrust.setType(1);
		String requestHeader = request.getHeader("user-agent");
		boolean isMobile = IpUtil.isMobileDevice(requestHeader);
		if (isMobile) {
			exEntrust.setSource(3);
		} else {
			exEntrust.setSource(1);
		}
		//委托业务
		String[] relt = remoteManageService.addEntrust(exEntrust);
		if (relt[0].equals(Constant.CODE_SUCCESS)) {
			jsonResult.setSuccess(true);
			jsonResult.setMsg(SpringContextUtil.diff("wt_success"));
		} else {
			String[] str = relt[1].split("::");
        	if(str.length>1){
        		jsonResult.setSuccess(false);
        		String message = SpringContextUtil.diff(str[1]);
            	jsonResult.setMsg(str[0]+message);
        	}else{
        		jsonResult.setSuccess(false);
            	jsonResult.setMsg(relt[1]);
        	}
		}
		return jsonResult;
	}
	//coinCode:MGB_CNY,entrustWay：1现价2市价，entrustPrice（价格）entrustCount（量）
		//两种情况1， coinCode:MGB_CNY,entrustWay：1现价，entrustPrice（价格）entrustCount（量）（默认情况为限价）
		//     2， coinCode:MGB_CNY,entrustWay：2市价，entrustCount（量）
		@RequestMapping(value = "/testSell", method = RequestMethod.GET)
		@ResponseBody
		public JsonResult testSell(EntrustTrade exEntrust, HttpServletRequest request) {
			JsonResult jsonResult=new JsonResult();
		//    User user = SessionUtils.getUser(request);
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			String userName=request.getParameter("username");
			String password=request.getParameter("password");
			if(StringUtils.isEmpty(userName)){
				return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
			}
			if(StringUtils.isEmpty(password)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}
			User user =null;
			if(!StringUtils.isEmpty(userName)){
				UUID uuid = UUID.randomUUID();
				RemoteResult login = remoteManageService.login(userName,password,uuid.toString());
				if(login!=null&&login.getSuccess()){
					user = (User)login.getObj();
				}else{
					jsonResult.setSuccess(false);
		        	jsonResult.setMsg(SpringContextUtil.diff("yichang"));
		        	return jsonResult;
				}	
			
			}else{
				UUID uuid = UUID.randomUUID();
				RemoteResult login = remoteManageService.login("13051580072", "36190a648140c2321551bcf430d533b5",uuid.toString());
				user = (User)login.getObj();
			}
		
			exEntrust.setEntrustWay(1);
			// 委托之前判断
		   String[] rtd= exEntrust.getCoinCode().split("_");
		   if(rtd.length==1){
				jsonResult.setSuccess(false);
	        	jsonResult.setMsg(SpringContextUtil.diff("djb_is_null"));
	        	return jsonResult;
		   }else{
				exEntrust.setFixPriceCoinCode(rtd[1]);
				exEntrust.setCoinCode(rtd[0]); 
		   }
		   
		   if(null==exEntrust.getEntrustWay()){
			   exEntrust.setEntrustWay(1); //默认为现价
		   }
		   if(exEntrust.getEntrustWay().equals(1)){
			   if(null==exEntrust.getEntrustCount()){
				   jsonResult.setSuccess(false);
		        	jsonResult.setMsg(SpringContextUtil.diff("wt_no_null"));
		        	return jsonResult;
			   }
	           if(null==exEntrust.getEntrustPrice()){
	        	   jsonResult.setSuccess(false);
		        	jsonResult.setMsg(SpringContextUtil.diff("wtprice_no_null"));
		        	return jsonResult;
			   }
		   }
		   if(exEntrust.getEntrustWay().equals(2)){
			   if(exEntrust.getType()==1&&null==exEntrust.getEntrustSum()){
				   jsonResult.setSuccess(false);
		        	jsonResult.setMsg(SpringContextUtil.diff("wtallmoney_no_null"));
		        	return jsonResult;
			   }
	           if(exEntrust.getType()==2&&null==exEntrust.getEntrustCount()){
	        	   jsonResult.setSuccess(false);
		        	jsonResult.setMsg(SpringContextUtil.diff("wt_no_null"));
		        	return jsonResult;
			   }
		   }
			exEntrust.setCustomerId(user.getCustomerId());
			String[] rt = remoteManageService.addEntrustCheck(exEntrust);
	        if(rt[0].equals(Constant.CODE_FAILED)){
	        	String[] str = rt[1].split("::");
	        	if(str.length>1){
	        		jsonResult.setSuccess(false);
	        		String message = SpringContextUtil.diff(str[1]);
	            	jsonResult.setMsg(str[0]+message);
	        	}else{
	        		jsonResult.setSuccess(false);
	            	jsonResult.setMsg(rt[1]);
	        	}
	        	return jsonResult;
	        }  
	        exEntrust.setSource(2);
			/*exEntrust.setCurrencyType(ContextUtil.getCurrencyType());
			exEntrust.setWebsite(ContextUtil.getWebsite());*/
			exEntrust.setCustomerIp(IpUtil.getIp(request));
			exEntrust.setUserName(user.getMobile());
		/*	exEntrust.setTrueName(user.getTruename());
			exEntrust.setUserCode(user.getUserCode());
			exEntrust.setMatchPriority(1);
			exEntrust.setCustomerType(1);*/
			exEntrust.setType(2);
			String requestHeader = request.getHeader("user-agent");
			boolean isMobile = IpUtil.isMobileDevice(requestHeader);
			if (isMobile) {
				exEntrust.setSource(3);
			} else {
				exEntrust.setSource(1);
			}
			//委托业务
			String[] relt = remoteManageService.addEntrust(exEntrust);
			if (relt[0].equals(Constant.CODE_SUCCESS)) {
				jsonResult.setSuccess(true);
				jsonResult.setMsg("委托成功");
			} else {
				String[] str = relt[1].split("::");
	        	if(str.length>1){
	        		jsonResult.setSuccess(false);
	        		String message = SpringContextUtil.diff(str[1]);
	            	jsonResult.setMsg(str[0]+message);
	        	}else{
	        		jsonResult.setSuccess(false);
	            	jsonResult.setMsg(relt[1]);
	        	}
			}
			return jsonResult;
		}
		

		/**
		 * 登录ajax方法
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping("loginService")
		@ResponseBody
		public JsonResult loginService(HttpServletRequest request, HttpServletResponse response) {
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}
			
			try{
				UUID uuid = UUID.randomUUID();
				RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
				RemoteResult login = remoteManageService.login(username, password,uuid.toString());
				if(login!=null&&login.getSuccess()){
					User user = (User) login.getObj();
					request.getSession().setAttribute("user", login.getObj());
					request.getSession().setAttribute("userName", username);
					request.getSession().setAttribute("trueName", user.getTruename());
					log.info(username+"|登录成功!");
					return new JsonResult().setSuccess(true);
				}	
			} catch (Exception e) {
				e.printStackTrace();
				log.error("登录方法远程调用出错");
			}
			
			return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
		}
		
		/**
		 * 注册ajax方法
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping("registSingleService")
		@ResponseBody
		public JsonResult registSingleService(HttpServletRequest request, HttpServletResponse response) {

			//用户名
			String username = request.getParameter("username");
			//密码
			String password = request.getParameter("password");
	/*		//图形验证码
			String registCode = request.getParameter("registCode");
			//手机验证码
			String registSmsCode = request.getParameter("registSmsCode");*/
			//邀请码
			String referralCode = request.getParameter("referralCode");
			
			if(StringUtils.isEmpty(username)){
				return new JsonResult().setMsg(SpringContextUtil.diff("telphone_is_not_null"));
			}
			if(StringUtils.isEmpty(password)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}

			
			try {
				RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
				RemoteResult regist = manageService.regist(username,password,referralCode,"cny");
				if(regist!=null){
					if(regist.getSuccess()){	
						return new JsonResult().setSuccess(true);
						
					}else{
						return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg()));
					}
				}
			} catch (Exception e) {
				log.error("注册方法远程调用出错");
			}
			
			return new JsonResult();
		
		}
		/**
		 * 注册ajax方法
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping("realnameService")
		@ResponseBody
		public JsonResult realnameService(HttpServletRequest request, HttpServletResponse response) {

			//用户名
			String username = request.getParameter("username");
			//密码
			String password = request.getParameter("password");
		
			if(StringUtils.isEmpty(username)){
				return new JsonResult().setMsg(SpringContextUtil.diff("telphone_is_not_null"));
			}
			if(StringUtils.isEmpty(password)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}
			try {
				RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
				RemoteResult realname = manageService.realname(username,"test","","test","1",username);
				if(realname!=null){
					if(realname.getSuccess()){
						return new JsonResult().setSuccess(true);
					}else{
						return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(realname.getMsg()));
					}
				}else{
					return new JsonResult().setSuccess(false);
				}
						
			} catch (Exception e) {
				log.error("注册方法远程调用出错");
			}
			
			return new JsonResult();
		
		}
		//生成银行汇款单
		@RequestMapping(value = "/testRmbRecharge")
		@ResponseBody
		public JsonResult testRmbRecharge(String surname,String remitter,String bankCode,String bankAmount,String bankName,HttpServletRequest request){
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			if(StringUtils.isEmpty(username)){
				return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
			}
			if(StringUtils.isEmpty(password)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}
			if(StringUtils.isEmpty(bankAmount)){
				return new JsonResult().setMsg(SpringContextUtil.diff("czmoney_no_null"));
			}
			AppTransactionManage appTransaction = new AppTransactionManage();
			UUID uuid = UUID.randomUUID();
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult login = remoteManageService.login(username, password,uuid.toString());
			User user = (User)login.getObj();
			RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
			if(user!=null){
					//确认汇款单
					String[] rt=remoteAppTransactionManageService.testconfirmRmbRecharge(surname,remitter, bankCode, bankAmount, bankName, appTransaction, user);
					if(rt[0].equals("true")){
						return new JsonResult().setSuccess(true);
					}else{
						return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(rt[1]));
					}
			}else{
				return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("login_nameorpwd_erro"));
			}
				
		}
		//充值虚拟币
		@RequestMapping(value = "/testCoinRecharge")
		@ResponseBody
		public JsonResult testCoinRecharge(String coinCode,String amount,String bankName,HttpServletRequest request){
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			if(StringUtils.isEmpty(username)){
				return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
			}
			if(StringUtils.isEmpty(password)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}
			if(StringUtils.isEmpty(amount)){
				return new JsonResult().setMsg(SpringContextUtil.diff("chongbi_no_null"));
			}
			if(StringUtils.isEmpty(coinCode)){
				return new JsonResult().setMsg(SpringContextUtil.diff("chongbitype_no_null"));
			}
			AppTransactionManage appTransaction = new AppTransactionManage();
			UUID uuid = UUID.randomUUID();
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult login = remoteManageService.login(username, password,uuid.toString());
			User user = (User)login.getObj();
           RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
			if(user!=null){
				String[] rt= remoteAppTransactionManageService.testCoinRecharge(coinCode, amount, user);
				if(rt[0].equals("true")){
					return new JsonResult().setSuccess(true);
				}else{
					return new JsonResult().setSuccess(false).setMsg(rt[1]);
				}
			}
			return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("error"));
		}
		

		/**
		 * 登录ajax方法
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping("testService")
		@ResponseBody
		public JsonResult testService(HttpServletRequest request, HttpServletResponse response) {
			
			String username = request.getParameter("username");
			if(StringUtils.isEmpty(username)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}
			try{
				RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
				RemoteResult login = remoteManageService.testAppCustomer(username);
				if(login!=null&&login.getSuccess()){
					return new JsonResult().setSuccess(true);
				}	
			} catch (Exception e) {
				e.printStackTrace();
				log.error("登录方法远程调用出错");
			}
			
			return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
		}
		/**
		 * 登录ajax方法
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping("testServiceNoSql")
		@ResponseBody
		public JsonResult testServiceNoSql(HttpServletRequest request, HttpServletResponse response) {
			
			try{
					return new JsonResult().setSuccess(true);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("登录方法远程调用出错");
			}
			
			return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
		}
		/**
		 * 注册ajax方法
		 * 
		 * @param request
		 * @param response
		 */
		@RequestMapping("registService")
		@ResponseBody
		public JsonResult registService(HttpServletRequest request, HttpServletResponse response) {

			//用户名
			String username = request.getParameter("username");
			//密码
			String password = request.getParameter("password");
	/*		//图形验证码
			String registCode = request.getParameter("registCode");
			//手机验证码
			String registSmsCode = request.getParameter("registSmsCode");*/
			//邀请码
			String referralCode = request.getParameter("referralCode");
			
			if(StringUtils.isEmpty(username)){
				return new JsonResult().setMsg(SpringContextUtil.diff("telphone_is_not_null"));
			}
			if(StringUtils.isEmpty(password)){
				return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
			}

			
			try {
				RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
				RemoteResult regist = manageService.regist(username,password,referralCode,"cny");
				if(regist!=null){
					if(regist.getSuccess()){
						String usercode=regist.getMsg();
						RemoteResult realname = manageService.realname(username,"test","","test","1",username);
						if(realname!=null){
							if(realname.getSuccess()){
								return new JsonResult().setSuccess(true);
							}else{
								return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(realname.getMsg()));
							}
						}else{
							return new JsonResult().setSuccess(false);
						}
						
						
						
					}else{
						return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg()));
					}
				}
			} catch (Exception e) {
				log.error("注册方法远程调用出错");
			}
			
			return new JsonResult();
		
		}
}
