/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月12日 上午10:09:31
 */
package com.mz.exapi.api.trade.controller;

import com.mz.core.annotation.RequestLimit;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.exapi.constant.APICodeConstant;
import com.mz.exapi.util.EncryptUtil;
import com.mz.manage.remote.RemoteManageService;
import com.mz.util.IpUtil;
import com.mz.util.sys.SpringContextUtil;
import com.mz.util.common.Constant;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteApiService;
import com.mz.manage.remote.model.ApiExApiApply;
import com.mz.manage.remote.model.User;
import com.mz.trade.redis.model.EntrustTrade;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/trade")
public class TradeController {

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

	/**
	 * 买卖委托
	 * 
	 * @param request
	 * @return
	 */

    @RequestLimit(count=100 ,time=1)//拦截请求time秒，count次数
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult appadd(HttpServletRequest request,String entrustPrice,String type,
		String coinCode,String entrustCount,String sign1,String accesskey) {
		JsonResult jsonResult = new JsonResult();
		EntrustTrade exEntrust = new EntrustTrade();
		RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
		ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
		// 校验k值
		String sign2 = EncryptUtil.hmacSign(entrustPrice + type + coinCode + entrustCount, exApiApply.getAccessKey());
		if (sign1 == null || !sign2.equals(sign1)) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("sign错误");
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			return jsonResult;
		}

		exEntrust.setCoinCode(coinCode);
		exEntrust.setEntrustCount(new BigDecimal(entrustCount));
		exEntrust.setEntrustPrice(new BigDecimal(entrustPrice));
		exEntrust.setType(Integer.parseInt(type));
		exEntrust.setSource(8);
		exEntrust.setEntrustWay(1);

		if (exApiApply == null || exApiApply.equals("")) {
			jsonResult.setSuccess(false);
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			jsonResult.setMsg("key无效");
			return jsonResult;
		}
		Long customerId = exApiApply.getCustomerId();
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByustomerId(customerId);
		if (user != null) {

			exEntrust.setCustomerId(user.getCustomerId());

			// 委托之前判断
			String[] rtd = exEntrust.getCoinCode().split("_");
			if (rtd.length == 1) {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("定价币种传空了");
				jsonResult.setCode(APICodeConstant.CODE_FAILED);
				return jsonResult;
			} else {
				exEntrust.setFixPriceCoinCode(rtd[1]);
				exEntrust.setCoinCode(rtd[0]);
			}

			exEntrust.setCustomerId(user.getCustomerId());
			String[] rt = remoteManageService.addEntrustCheck(exEntrust);
			if (rt[0].equals(Constant.CODE_FAILED)) {
				String[] str = rt[1].split("::");
				if(str.length>1){
					jsonResult.setSuccess(false);
					String str1 = SpringContextUtil.diff(str[1],"zh_CN");
					String str0 = str[0];
					jsonResult.setMsg(str0+str1);
				}else{
					jsonResult.setSuccess(false);
					jsonResult.setMsg( SpringContextUtil.diff(rt[1],"zh_CN"));
				}
				return jsonResult;
			}
			exEntrust.setCustomerIp(IpUtil.getIp(request));
			exEntrust.setUserName(user.getMobile());

			String requestHeader = request.getHeader("user-agent");
			boolean isMobile = IpUtil.isMobileDevice(requestHeader);
			// 委托业务
			String[] relt = remoteManageService.addEntrust(exEntrust);
			if (relt[0].equals(Constant.CODE_SUCCESS)) {
				jsonResult.setSuccess(true);
				jsonResult.setMsg(SpringContextUtil.diff("delegate_success","zh_CN"));
			} else {
				String Msg=relt[1];
				if(relt[1].equals("jinzhijiaoyi")){
					Msg=SpringContextUtil.diff("jinzhijiaoyi","zh_CN");
				}
				jsonResult.setSuccess(false);
				jsonResult.setMsg(Msg);
			}
			return jsonResult;
		}
		return new JsonResult().setSuccess(false).setMsg("key值无效");
	}

	/**
	 * 全部撤销
	 * 
	 * @return
	 */
    @RequestLimit(count=100 ,time=1)//拦截请求time秒，count次数
	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JsonResult allcancelKK(HttpServletRequest request,String accesskey,String sign1,String coinCode) {
		JsonResult jsonResult = new JsonResult();
		// 获取key值表对象数据
		RemoteApiService remoteApiService = SpringContextUtil.getBean("remoteApiService");
		ApiExApiApply exApiApply = remoteApiService.getExApiApply(accesskey);
		
		if (coinCode == null || "".equals(coinCode)) {
			jsonResult.setSuccess(false);
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			jsonResult.setMsg("币代码为空");
			return jsonResult;
		}
		//校验k值
		String sign2=EncryptUtil.hmacSign(coinCode,exApiApply.getAccessKey());
		if(sign1==null||!sign2.equals(sign1)){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("sign错误");
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			return jsonResult;
		}
		if (coinCode == null || "".equals(coinCode)) {
			jsonResult.setSuccess(false);
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			jsonResult.setMsg("币代码为空");
			return jsonResult;
		}

		EntrustTrade entrustTrade = new EntrustTrade();
		try {
			// 根据_分割币代码
			String[] split = coinCode.split("_");
			entrustTrade.setCoinCode(split[0]);
			entrustTrade.setFixPriceCoinCode(split[1]);
			entrustTrade.setCustomerId(exApiApply.getCustomerId());
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setSuccess(false);
			jsonResult.setCode(APICodeConstant.CODE_FAILED);
			jsonResult.setMsg("币代码格式错误");
			return jsonResult;
		}
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		remoteManageService.cancelCustAllExEntrust(entrustTrade);
		jsonResult.setSuccess(true);
		jsonResult.setMsg("撤销成功");
		jsonResult.setCode("1001");

		return jsonResult;
	}
}
