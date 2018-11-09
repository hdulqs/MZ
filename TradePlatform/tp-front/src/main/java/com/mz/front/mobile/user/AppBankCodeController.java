package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.constant.StringConstant;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.FileUpload;
import com.mz.util.sys.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(value="/mobile/user/appbankcode")
@Api(value= "App操作类", description ="银行卡管理")
public class AppBankCodeController {

	@RequestMapping(value = "/findBankCard")
	@ApiOperation(value = "查询当前账户下的银行卡", httpMethod = "POST", response = AppBankCardManage.class, notes = "查询当前账户下的银行卡(JsonResult+Obj)")
	@ResponseBody
	@ApiImplicitParams({@ApiImplicitParam(name = "tokenId", value = "tokenId", required = true, dataType = "string", paramType = "query")})
	public JsonResult findBankCard(HttpServletRequest request) {
		String tokenId = request.getParameter("tokenId");
		
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			if (user != null) {
				RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				System.out.println("***********tokenId:"+tokenId);
				System.out.println("***********Username:"+user.getUsername());
				System.out.println("***********CustomerId"+user.getCustomerId());
				List<AppBankCardManage> list = remoteAppBankCardManageService.findByCustomerId(user.getCustomerId());
				return new JsonResult().setSuccess(true).setObj(list);
			}
		}
		return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
	}
	
	@RequestMapping(value = "/removeBankCard")
	@ApiOperation(value = "删除银行卡", httpMethod = "POST", response = JsonResult.class, notes = "删除银行卡")
	@ResponseBody
	public JsonResult removeBankCard(HttpServletRequest request,Long id){
		JsonResult jsonResult = new JsonResult();
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			if (user != null) {
				RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
				RemoteResult remoteresult = remoteAppBankCardManageService.delete(id);
				String diff = SpringContextUtil.diff(remoteresult.getMsg().toString(),"zh_CN");
				jsonResult.setSuccess(remoteresult.getSuccess()).setMsg(SpringContextUtil.diff(remoteresult.getMsg().toString(),"zh_CN"));
				return jsonResult;
			}
		}
		return new JsonResult().setSuccess(false).setMsg("登录已超时");
	}
	
	public static void main(String[] args) {
		String aa = "{\"mobile\":\"13522221111\"}";
		JSONObject json = JSONObject.parseObject(aa);
		System.out.println(json.get("mobile"));
	}
	
	/**
	 * 查询省
	 * @return
	 */
	@RequestMapping("/findArea")
	@ApiOperation(value = "查询省", httpMethod = "POST", response = JsonResult.class, notes = "查询省")
	@ResponseBody
	public JsonResult findArea(HttpServletRequest request) {
		JsonResult result=new JsonResult(); 
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			if (user != null) {
				result.setObj(redisService.get(StringConstant.AREA_CACHE));
				result.setSuccess(true);
				
				return  result;
			}
		}
		return new JsonResult().setSuccess(false).setMsg("登录已超时");
	}
	
	/**
	 * 查询市
	 * @return
	 */
	@RequestMapping("/appcity/{key}")
	@ApiOperation(value = "查询市", httpMethod = "POST", response = JsonResult.class, notes = "key为省的key")
	@ResponseBody
	public JsonResult appcity(HttpServletRequest request,@PathVariable String key) {
		System.out.println("---"+"DIC:"+key);
		JsonResult result=new JsonResult(); 
		
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			if (user != null) {
				String value1 = redisService.get(StringConstant.AREA_CACHE);
				JSONArray jsona = JSONArray.parseArray(value1);
				String json_n = "";
				for(int i=0;i<jsona.size();i++){
					String jsonvalur = (String) jsona.getString(i);
					if(jsonvalur.contains(key)){
						JSONObject jsono = jsona.getJSONObject(i);
						json_n = jsono.get("cities").toString().replace("[", "").replace("]", "");
					}
				}
				result.setObj(json_n);
				result.setSuccess(true);
				return result;
			}
		}
		return new JsonResult().setSuccess(false).setMsg("登录已超时");
	}
	
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
			    	  pathImg[i] = "";
			      }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathImg;
	}
	
	/**
	 * 保存银行卡
	 * @param trueName
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveBankCard")
	@ApiOperation(value = "保存银行卡", httpMethod = "POST", response = JsonResult.class, notes = "bankname银行,p1省,c1市,subBank开户支行,subBankNum银行机构代码,img1 微信,img2 支付宝"
			+ ",trueName持卡人,account银行卡号")
	@ResponseBody
	public JsonResult saveBankCard(HttpServletRequest request,@RequestParam String bankname,@RequestParam String p1,@RequestParam String c1,@RequestParam String subBank,
			@RequestParam  String subBankNum,@RequestParam  String trueName,@RequestParam  String surName,@RequestParam String account) {
		JsonResult jsonResult = new JsonResult();
		
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		
		String alipay = request.getParameter("alipay");
		String weChat = request.getParameter("wechat");
		String bankProvinceKey = request.getParameter("bankProvinceKey");
		//获取短信验证码
		String verifyCode = request.getParameter("verifyCode");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
		
		MultipartFile img1 = multipartRequest.getFile("img1");  
		MultipartFile img2 = multipartRequest.getFile("img2");
		MultipartFile[] files = {img1,img2};
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
								
			if (user != null) {
				String mobile = user.getPhone();
				String session_pwSmsCode = redisService.get("SMS:smsphone:"+mobile);
				//如果验证码不正确
				if (!verifyCode.equals(session_pwSmsCode)){
					return jsonResult.setMsg(SpringContextUtil.diff("tel_code_error"));
				}
				AppBankCardManage appBankCardManage = new AppBankCardManage();
				appBankCardManage.setUserName(user.getMobile());
				appBankCardManage.setCardBank(bankname);
				appBankCardManage.setBankProvince(p1);
				appBankCardManage.setBankAddress(c1);
				appBankCardManage.setSubBank(subBank);
				appBankCardManage.setSubBankNum(subBankNum);
				//名
				appBankCardManage.setTrueName(trueName);
				//姓
				appBankCardManage.setSurName(surName);
				
				appBankCardManage.setCardNumber(account);
				
				//String[] pathImg = this.upload(files); 更换上传方式
				String[] pathImg = FileUpload.POSTFileQiniu(request,files);
				appBankCardManage.setAlipay(alipay);
				appBankCardManage.setWeChat(weChat);
				appBankCardManage.setWeChatPicture(pathImg[0]);
				appBankCardManage.setAlipayPicture(pathImg[1]);
				appBankCardManage.setBankProvinceKey(bankProvinceKey);
				
				try {
					RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
					RemoteResult remoteResult = remoteAppBankCardManageService.saveBankCard(user,appBankCardManage);
					return jsonResult.setSuccess(remoteResult.getSuccess()).setMsg(SpringContextUtil.diff(remoteResult.getMsg().toString(),"zh_CN")).setObj(remoteResult.getObj());
				} catch (Exception e) {
					return jsonResult.setMsg("远程调用出错");
				}
			}
		}
		return new JsonResult().setSuccess(false).setMsg("登录已超时");
	}



}
