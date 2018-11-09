package com.mz.front.user.exdmcustomerpublickey.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.util.shiro.PasswordHelper;
import com.mz.manage.remote.model.ExDmCustomerPublicKeyManage;
import com.mz.manage.remote.model.ExProductManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/user/publickeylist")
public class PublickeylistController {
	
	
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
		User user = SessionUtils.getUser(request);
		String coinCode = request.getParameter("coinCode");
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		User selectByTel = remoteManageService.selectByTel(user.getUsername());
		
		//查询后台参数配置
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String config = redisService.get("configCache:all");
		JSONObject parseObject = JSONObject.parseObject(config);
		String isTibi = parseObject.get("isTibi").toString();
		if(isTibi!=null && "1".equals(isTibi)){
			if(user!=null){
				RemoteAppTransactionManageService remoteAppTransactionManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				List<ExDmCustomerPublicKeyManage> listPublic = remoteAppTransactionManageService.listPublic(user.getCustomerId());
				if(listPublic!=null && listPublic.size()>0){
					mav.addObject("listPublic", listPublic);
				}
				List<ExProductManage> listProduct = remoteAppTransactionManageService.listProduct();
				if(listProduct!=null && listProduct.size()>0){
					if(coinCode!=null && !"".equals(coinCode)){
						for(int i=0;i<listProduct.size();i++){
							if(listProduct.get(i).getCoinCode().equals(coinCode)){
								String coinCodeOld = listProduct.get(0).getCoinCode();
								listProduct.get(0).setCoinCode(coinCode);
								listProduct.get(i).setCoinCode(coinCodeOld);
							}
						}
					}
					
					mav.addObject("listProduct", listProduct);
					mav.addObject("listProductFirst", listProduct.get(0).getCoinCode());
				}
			}
            mav.setViewName("front/user/publickeylist");
			return mav;
		}else{
			//未实名，往实名页跳
			if(selectByTel!=null){
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
					if(user!=null){
						RemoteAppTransactionManageService remoteAppTransactionManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
						List<ExDmCustomerPublicKeyManage> listPublic = remoteAppTransactionManageService.listPublic(user.getCustomerId());
						if(listPublic!=null && listPublic.size()>0){
							mav.addObject("listPublic", listPublic);
						}
						List<ExProductManage> listProduct = remoteAppTransactionManageService.listProduct();
						if(listProduct!=null && listProduct.size()>0){
							if(coinCode!=null && !"".equals(coinCode)){
								for(int i=0;i<listProduct.size();i++){
									if(listProduct.get(i).getCoinCode().equals(coinCode)){
										String coinCodeOld = listProduct.get(0).getCoinCode();
										listProduct.get(0).setCoinCode(coinCode);
										listProduct.get(i).setCoinCode(coinCodeOld);
									}
								}
							}
							
							mav.addObject("listProduct", listProduct);
							mav.addObject("listProductFirst", listProduct.get(0).getCoinCode());
						}
					}
					mav.setViewName("front/user/publickeylist");
					return mav;
				}else if(selectByTel.getStates()==3){
					mav.setViewName("front/user/realinfono");
					return mav;
				}
			}
		}
		return mav;
	}
	

	/**
	 * 保存币账户
	 * @param exDmCustomerPublicKeyManage
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public JsonResult save(ExDmCustomerPublicKeyManage exDmCustomerPublicKeyManage,HttpServletRequest request){
		String language = (String) request.getAttribute("language");
		
		JsonResult jsonresult = new JsonResult();
		User user = SessionUtils.getUser(request);
		if(user!=null){
			try {
				RemoteAppTransactionManageService remoteAppTransactionManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				List<ExDmCustomerPublicKeyManage> listPublic = remoteAppTransactionManageService.listPublic(user.getCustomerId());
				if(listPublic!=null && listPublic.size()>0){
					for (int i = 0; i < listPublic.size(); i++) {
						String publicKey = request.getParameter("publicKey");
						String currencyType = request.getParameter("currencyType");
						String listpublicKey = listPublic.get(i).getPublicKey();
						String listcurrencyType = listPublic.get(i).getCurrencyType();
						if(listpublicKey.equals(publicKey)&&listcurrencyType.equals(currencyType)){
							
							return jsonresult.setSuccess(false).setMsg(SpringContextUtil.diff("waltbunengchongfu"));
						};
					}
				}
				RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				remoteAppBankCardManageService.save(exDmCustomerPublicKeyManage,user);
				return jsonresult.setSuccess(true).setObj(exDmCustomerPublicKeyManage).setMsg(SpringContextUtil.diff("save_success"));
			} catch (Exception e) {
				e.printStackTrace();
				return jsonresult.setSuccess(false).setMsg(SpringContextUtil.diff("yichang"));
			}
		}else{
			return jsonresult.setSuccess(false).setMsg(SpringContextUtil.diff("before_login"));
		}
	}
	
	/**
	 * 删除
	 * @param request
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult delete(HttpServletRequest request){
		String id = request.getParameter("id");
		if(id!=null && !id.equals("")){
			try {
				RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				remoteAppBankCardManageService.deletePublieKey(Long.valueOf(id));
				return new JsonResult().setSuccess(true);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return new JsonResult().setSuccess(false);
			}
		}else{
			return new JsonResult().setSuccess(false);
		}
	}
	
	/**
	 * 用户提交提现订单
	 * @param
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getOrder")
	@ResponseBody
	public JsonResult getOrder(HttpServletRequest request) throws Exception {
		User user = SessionUtils.getUser(request);
		String sessionAccountpwSmsCode = (String) request.getSession().getAttribute("accounCoinSmsCode");
		String code = request.getParameter("coinType");
		String accountpwSmsCode = request.getParameter("withdrawCode");
		String passWord = request.getParameter("accountPassWord");
		String type = request.getParameter("currencyType");//HTC
		String btcNum = request.getParameter("btcNum");//jine
		String btcKey = request.getParameter("btcKey");//钱包地址
		String pacecerrecy = request.getParameter("shouxufei");
	/*	if("".equals(pacecerrecy)||pacecerrecy==null){
			return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("qingshezhitibishouxufei"));
		}*/
		//Date passDate = user.getPassDate();
	/*	if(passDate!=null&&!passDate.equals("")){
			String passDate1	 = formatter.format(passDate);
			if(checkDate(passDate1)){
		}else{
			return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("24h_jinzhitibi"));
			}
		}*/
		if(user!=null){
			try {
				//add by zongwei 提币必须绑定手机 20180628
				if(user.getPhoneState()!=1){
					return new JsonResult().setSuccess(false).setMsg("请到个人中心绑定手机!");
				}

				if (user.getAccountPassWord() != null &&  !"".equals(user.getAccountPassWord())) {
					PasswordHelper passwordHelper = new PasswordHelper();
					String pw = passwordHelper.encryString(passWord, user.getSalt());
					if (!pw.equals(user.getAccountPassWord())) {
						return new JsonResult().setMsg(SpringContextUtil.diff("mimacuowu"));
					}
				}

				RemoteAppTransactionManageService transactionManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				RemoteResult remoteResult = transactionManageService.getOrder(user, code, accountpwSmsCode, sessionAccountpwSmsCode, btcNum, type,user.getUsername(),btcKey,pacecerrecy);

				if(remoteResult.getSuccess()){
					return new JsonResult().setSuccess(true).setObj(remoteResult.getObj());
				}else{
					return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff(remoteResult.getMsg()));
				}
			} catch (Exception e) {
				// TODO: handle exception
				return new JsonResult().setSuccess(false);
			}
		}
		return new JsonResult();
	}
	
	
	/**
	 * 查询提现币的记录
	 * 交易类型(1充币 ，2提币)'
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public FrontPage list(HttpServletRequest request) {
		User user = SessionUtils.getUser(request);
		String status = request.getParameter("status");
		
		RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		params.put("customerId", user.getCustomerId().toString());
		params.put("transactionType", "2");
		params.put("status", status);
		return remoteAppTransactionManageService.findExdmtransaction(params);
	}
	
	
	
	
	
	/**
	 * 判断是否超过24小时
	 * @param date1
	 * @return
	 * @throws Exception
	 */
	
	 public static boolean checkDate(String date1) throws Exception { 
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date start = formatter.parse(date1); 
	        String format = formatter.format(new Date());
	        Date end = formatter.parse(format); 
	        long cha = end.getTime() - start.getTime(); 
	        double result = cha * 1.0 / (1000 * 60 * 60); 
	        if(result<=24){ 
	             //System.out.println("不可用");   
	             return false; 
	        }else{ 
	             //System.out.println("可用");  
	             return true; 
	        } 
	    } 
	
	
	 
}
