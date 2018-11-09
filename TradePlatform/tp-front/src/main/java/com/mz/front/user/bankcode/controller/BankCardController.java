package com.mz.front.user.bankcode.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.ContextUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.core.constant.StringConstant;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.util.FileUpload;
import com.mz.util.SessionUtils;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


/**
 * BankCardController.java
 * @author denghf
 * 2017年7月11日 上午18:27:24
 */
@Controller
@RequestMapping(value ="/user/bankcard")
public class BankCardController {
	
	private final static Logger log = Logger.getLogger(BankCardController.class);
	
	
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
	
	@RequestMapping("index")
	public ModelAndView index(HttpServletRequest request) {
		Locale locale = LocaleContextHolder.getLocale();
		ModelAndView mav = new ModelAndView();
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		User user = SessionUtils.getUser(request);
		User selectByTel = remoteManageService.selectByTel(user.getUsername());
		
		//查询后台参数配置
		/*RedisService redisService = SpringContextUtil.getBean("redisService");
		String config = redisService.get("configCache:all");
		JSONObject parseObject = JSONObject.parseObject(config);
		String isTibi = parseObject.get("isTibi").toString();
		if(isTibi!=null && "1".equals(isTibi)){
			RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
			
			List<AppBankCardManage> list = remoteAppBankCardManageService.findByCustomerId(user.getCustomerId());
			mav.addObject("list", list);
			mav.addObject("ischeckName",user.getStates());

			mav.setViewName("front/user/bankcard");
			return mav;
		}else{*/
			if(selectByTel!=null&&user!=null){
				if(user.getStates()==0){
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
					RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
					
					List<AppBankCardManage> list = remoteAppBankCardManageService.findByCustomerId(user.getCustomerId());
					mav.addObject("list", list);
					
					mav.setViewName("front/user/bankcard");
					return mav;
				}else if(selectByTel.getStates()==3){
					mav.setViewName("front/user/realinfono");
					return mav;
				}
			}
		//}
		return mav;
	}
	
	@RequestMapping(value = "/jumpbank")
	public String jumpbank(Model m,HttpServletRequest request){
		User user = SessionUtils.getUser(request);
		if(user!=null){
			m.addAttribute("user", user.getTruename());
			
		}
		return "front/user/bankcard";
	}

	@RequestMapping(value = "/findBankCard")
	@ResponseBody
	public JsonResult findBankCard(HttpServletRequest request) {
		User user = SessionUtils.getUser(request);
		if (user != null) {
			//System.out.println(ContextUtil.getSaasId());
			//RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
			RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
			ContextUtil.setRemoteCurrencyType();
			ContextUtil.setRemoteWebsite();
			List<AppBankCardManage> list = remoteAppBankCardManageService.findByCustomerId(user.getCustomerId());
			return new JsonResult().setSuccess(true).setObj(list);
		}
		return new JsonResult().setSuccess(false);
	}
	
	/**
	 * 查询银行
	 * @return
	 */
	@RequestMapping("/bank")
	@ResponseBody
	public JsonResult findBank() {
		JsonResult result=new JsonResult(); 
		RedisService redisService = SpringContextUtil.getBean("redisService");
		
		result.setObj(redisService.get("DIC:"+"bank"));
		result.setSuccess(true);
		return  result;
	}
	
	/**
	 * 查询省
	 * @return
	 */
	@RequestMapping("/area")
	@ResponseBody
	public JsonResult findArea() {
		JsonResult result=new JsonResult(); 
		RedisService redisService = SpringContextUtil.getBean("redisService");
		 
		result.setObj(redisService.get(StringConstant.AREA_CACHE));
		result.setSuccess(true);
		
		return  result;
	}
	
	/**
	 * 查询市
	 * @return
	 */
	@RequestMapping("/city/{key}")
	@ResponseBody
	public JsonResult findCity(@PathVariable String key) {
		System.out.println("---"+"DIC:"+key);
		JsonResult result=new JsonResult(); 
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get(StringConstant.AREA_CACHE);
		JSONArray jsona = JSONArray.parseArray(value);
		String json_n = "";
		for(int i=0;i<jsona.size();i++){
			String jsonvalur = (String) jsona.getString(i);
			if(jsonvalur.contains(key)){
				JSONObject jsono = jsona.getJSONObject(i);
				json_n = jsono.get("cities").toString().replace("[", "").replace("]", "");
			}
		}
		//result.setObj(redisService.get("DIC:"+key));
		result.setObj(json_n);
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * 保存银行卡
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveBankCard")
	@ApiOperation(value = "保存银行卡", httpMethod = "POST", response = JsonResult.class, notes = "cardBank银行,bankProvince省,bankAddress市,subBank开户支行,subBankNum银行机构代码,weChatPicture 微信,alipayPicture 支付宝"
			+ ",trueName持卡人,cardNumber银行卡号")
	@ResponseBody
	public JsonResult saveBankCard(HttpServletRequest request,@RequestParam String cardBank,@RequestParam String bankProvince,@RequestParam String bankAddress,@RequestParam String subBank,
			@RequestParam  String trueName,@RequestParam  String surName,@RequestParam String cardNumber,@RequestParam String verifyCode) {
		JsonResult jsonResult = new JsonResult();
		User user = SessionUtils.getUser(request);
		String mobile = "";
		if (user!=null) {
			 mobile = user.getPhone();
		}
		//获取保存的验证码
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String session_pwSmsCode = redisService.get("SMS:smsphone:"+mobile);
		//如果验证码不正确
		if (!verifyCode.equals(session_pwSmsCode)){
			return jsonResult.setMsg(SpringContextUtil.diff("tel_code_error"));
		}
		String alipay = request.getParameter("alipay");
		String weChat = request.getParameter("wechat");
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
		
		MultipartFile img1 = multipartRequest.getFile("alipayPicture");  
		MultipartFile img2 = multipartRequest.getFile("weChatPicture");
		MultipartFile[] files = {img1,img2};

			if (user != null) {
				AppBankCardManage appBankCardManage = new AppBankCardManage();
				appBankCardManage.setUserName(user.getMobile());
				appBankCardManage.setCardName(trueName);
				appBankCardManage.setCardBank(cardBank);
				appBankCardManage.setBankProvince(bankProvince);
				appBankCardManage.setBankAddress(bankAddress);
				appBankCardManage.setSubBank(subBank);
				//名
				appBankCardManage.setTrueName(trueName);
				//姓
				appBankCardManage.setSurName(surName);
				
				appBankCardManage.setCardNumber(cardNumber);
				
				//String[] pathImg = this.upload(files);
				String[] pathImg = FileUpload.POSTFileQiniu(request,files);
				
				appBankCardManage.setAlipay(alipay);
				appBankCardManage.setWeChat(weChat);
				appBankCardManage.setAlipayPicture(pathImg[0]);
				appBankCardManage.setWeChatPicture(pathImg[1]);
				
				try {
					RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
					RemoteResult remoteResult = remoteAppBankCardManageService.saveBankCard(user,appBankCardManage);
					return jsonResult.setSuccess(remoteResult.getSuccess()).setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(),"zh_CN")).setObj(remoteResult.getObj());
				} catch (Exception e) {
					return jsonResult.setMsg("远程调用出错");
				}
			}
	
		return new JsonResult().setSuccess(false).setMsg("登录已超时");
	}
	/*public JsonResult saveBankCard(AppBankCardManage appBankCardManage,HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		User user = SessionUtils.getUser(request);
		appBankCardManage.setUserName(user.getMobile());
		appBankCardManage.setCardName(appBankCardManage.getTrueName());
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
		MultipartFile img1 = multipartRequest.getFile("alipayPicture");  
		MultipartFile img2 = multipartRequest.getFile("img2");
		MultipartFile[] files = {img1};
		String[] pathImg = this.upload(files);
		appBankCardManage.setAlipayPicture(pathImg[1]);
		try {
			RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
			RemoteResult remoteResult = remoteAppBankCardManageService.saveBankCard(user,appBankCardManage);
			return jsonResult.setSuccess(remoteResult.getSuccess()).setMsg(SpringContextUtil.diff(remoteResult.getMsg())).setObj(remoteResult.getObj());
		} catch (Exception e) {
			log.error("远程调用出错");
			return jsonResult.setMsg(SpringContextUtil.diff("yichang"));
		}
	}*/
	
	public static HttpServletRequest getRequest() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			return request;
		} catch (Exception e) {
		}
		return null;

	}
	/**
	 * 上传图片
	 *
	 * @param files
	 * @return
	 */
	public String[] upload(@RequestParam("file") MultipartFile[] files) {
		String[] pathImg = new String[2];
		try {
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					// 获取文件名
					if (file != null){
					String filename = file.getOriginalFilename();
					// 上传图片
					if (file != null && filename != null && filename.length() > 0) {
						// 上传路径

						String realPath = this.getRequest().getRealPath("/");
						// 生成hryfile路径
						String rootPath = realPath.substring(0,
								org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2)
								+ 1);
						System.out.println("rootPath" + rootPath);
						// 新图片名称
						String newFileName = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
						pathImg[i] = "hryfilefront" + File.separator + newFileName;
						File secondFolder = new File(rootPath + "hryfilefront");
						// 存入本地
						if (!secondFolder.exists()) {
							secondFolder.mkdirs();
						}
						File newFile = new File(rootPath + "hryfilefront" + File.separator + newFileName);
						file.transferTo(newFile);
					}
			      }else{
			    	  pathImg[i] = null;
			      }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathImg;
	}
	@RequestMapping(value = "/removeBankCard")
	@ResponseBody
	public JsonResult removeBankCard(Long id){
		JsonResult jsonResult = new JsonResult();
		RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
		RemoteResult remoteresult = remoteAppBankCardManageService.delete(id);
		return jsonResult.setSuccess(remoteresult.getSuccess()).setMsg(SpringContextUtil.diff(remoteresult.getMsg()));
	}
	/**
	 * 取消微信和支付宝支付
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/detetePicture")
	@ResponseBody
	public JsonResult detetePicture(@RequestParam Long id,@RequestParam String type){
		JsonResult jsonResult = new JsonResult();
		RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
		RemoteResult remoteresult = remoteAppBankCardManageService.detetePicture(id,type);
		return jsonResult.setSuccess(remoteresult.getSuccess());
	}


	
}
