package com.mz.front.user.rmbWithdraw.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import com.mz.core.util.shiro.PasswordHelper;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.model.AppAccountManage;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.util.SessionUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/rmbWithdraw")
public class RmbWithdrawController {
	private final static Logger log = Logger.getLogger(RmbWithdrawController.class);
	
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
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		Locale locale = LocaleContextHolder.getLocale();
		ModelAndView mav = new ModelAndView();
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		User user = SessionUtils.getUser(request);
		User selectByTel = remoteManageService.selectByTel(user.getUsername());
		
		//查询后台参数配置
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String config = redisService.get("configCache:all");
		JSONObject parseObject = JSONObject.parseObject(config);

		//判断是否开启强制手机认证
		String isOpenVerify = "";
		if(parseObject.get("isOpenVerify") !=null){
			isOpenVerify = parseObject.get("isOpenVerify").toString();
		}
		if(null != isOpenVerify && (!"".equals(isOpenVerify)) && isOpenVerify.equals("0")){
			if(user.getGoogleState() == 0 && user.getPhoneState() ==0){
				mav.setViewName("front/user/setphone");
				return mav;
			}
		}


		String isTibi = parseObject.get("isTibi").toString();
		if(isTibi!=null && "1".equals(isTibi)){
			mav.addObject("languageCode", parseObject.get("language_code"));//当前币种
			RemoteManageService remoteAppAccountManageService = SpringContextUtil.getBean("remoteManageService");
			BigDecimal oldMoney = remoteAppAccountManageService.getOldMoney(user.getCustomerId(), new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()));
			AppAccountManage appAccount = null;
			if(user!=null){
				String username = (String)request.getSession().getAttribute("username");
				//查询虚拟账户
				appAccount = remoteAppAccountManageService.getAppAccountManage(user.getCustomerId());
				mav.addObject("appAccount", appAccount);
				
				//查询银行卡列表
				RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				List<AppBankCardManage> list = remoteAppBankCardManageService.findByCustomerId(user.getCustomerId());
				mav.addObject("list", list);
				mav.addObject("username", username);
				
				//查询后台参数配置
				if(!StringUtils.isEmpty(config)){
					mav.addObject("oldMoney", oldMoney.toString());//当天已经提现的金额
					mav.addObject("onlineWithdrawFeeRate", parseObject.get("onlineWithdrawFeeRate"));//提现手续费率
					mav.addObject("maxWithdrawMoney", parseObject.get("maxWithdrawMoney"));//当天最多提现金额
					mav.addObject("maxWithdrawMoneyOneTime", parseObject.get("maxWithdrawMoneyOneTime"));//单笔最多提现金额(元)
				}
				
				
			}
			mav.setViewName("front/user/rmbwithdraw");
			return mav;
		}else{
			if(selectByTel!=null){
				if(selectByTel.getStates()==0){
					mav.setViewName("front/user/identity");
					return mav;
				}else if(selectByTel.getStates()==1){
					RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
					if(result!=null&&result.getSuccess()){
						UserInfo userInfo = (UserInfo)result.getObj();
						if("1".equals(userInfo.getType())){
							if("en".equals(locale.toString())){
								userInfo.setPapersType("ID Card");
							}
						}else if("2".equals(userInfo.getType())){
							if("en".equals(locale.toString())){
								userInfo.setPapersType("Passport");
							}
						}
						mav.addObject("info", result.getObj());
					}
					mav.setViewName("front/user/realinfo");
					return mav;
				}else if(selectByTel.getStates()==2){
					RemoteManageService remoteAppAccountManageService = SpringContextUtil.getBean("remoteManageService");
					BigDecimal oldMoney = remoteAppAccountManageService.getOldMoney(user.getCustomerId(), new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()));
					AppAccountManage appAccount = null;
					if(user!=null){
						String username = (String)request.getSession().getAttribute("username");
						//查询虚拟账户
						appAccount = remoteAppAccountManageService.getAppAccountManage(user.getCustomerId());
						mav.addObject("appAccount", appAccount);
						
						//查询银行卡列表
						RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
						List<AppBankCardManage> list = remoteAppBankCardManageService.findByCustomerId(user.getCustomerId());
						mav.addObject("list", list);
						mav.addObject("username", username);
						
						//查询后台参数配置
						if(!StringUtils.isEmpty(config)){
							mav.addObject("oldMoney", oldMoney.toString());//当天已经提现的金额
							mav.addObject("onlineWithdrawFeeRate", parseObject.get("onlineWithdrawFeeRate"));//提现手续费率
							mav.addObject("maxWithdrawMoney", parseObject.get("maxWithdrawMoney"));//当天最多提现金额
							mav.addObject("maxWithdrawMoneyOneTime", parseObject.get("maxWithdrawMoneyOneTime"));//单笔最多提现金额(元)
						}
						
						
					}
					mav.setViewName("front/user/rmbwithdraw");
					return mav;
				}else if(selectByTel.getStates()==3){
					mav.setViewName("front/user/realinfono");
					return mav;
				}
				
			}
		}
		return mav;
	}
	
	//提现
	@RequestMapping(value = "/rmbwithdraw")
	@ResponseBody
	public JsonResult rmbwithdraw(AppTransactionManage appTransaction, HttpServletRequest request) {


		//String accountPassWord = request.getParameter("accountPassWord");//交易密码
		String bankCardId = request.getParameter("custromerAccountNumber");//选中的自己的银行卡对象ID
		//String withdrawCode = request.getParameter("withdrawCode");//短信验证码
		String accountpwSmsCode = (String) request.getSession().getAttribute("accountpwSmsCode");
		String language = (String) request.getAttribute("language");
		String withdrawCode=null;
		String accountPassWord=null;
		String encryString=null;
		/*if(StringUtils.isEmpty(accountPassWord)){
			return new JsonResult().setMsg(SpringContextUtil.diff("jy_pwd_error"));
		}*/
		
		User user = SessionUtils.getUser(request);

		PasswordHelper passwordHelper = new PasswordHelper();
		/*String encryString = passwordHelper.encryString(accountPassWord, user.getSalt());
		
		if(!user.getAccountPassWord().equals(encryString)){
			return new JsonResult().setMsg(SpringContextUtil.diff("jy_pwd_error"));
		}*/
		/*if(StringUtils.isEmpty(withdrawCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
		}
		if(!withdrawCode.equals(accountpwSmsCode)){
			return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
		}*/
		
		
		RemoteResult remoteResult = null;
		try {
			if (user != null) {
				RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
				remoteResult = remoteAppTransactionManageService.rmbwithdraw(accountPassWord, bankCardId, withdrawCode, accountpwSmsCode, user, encryString,appTransaction);
				return new JsonResult().setSuccess(remoteResult.getSuccess()).setMsg(SpringContextUtil.diff(remoteResult.getMsg()));
			}
		} catch (Exception e) {
			log.error("提现远程调用出错");
			log.error(e);
			e.printStackTrace();
			return new JsonResult().setMsg(SpringContextUtil.diff("yichang"));
		}
		return new JsonResult();
	}
	
}
