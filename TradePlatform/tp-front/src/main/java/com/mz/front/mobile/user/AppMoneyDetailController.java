package com.mz.front.mobile.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.ExDmTransactionManage;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value="/mobile/user/appmoneydetail")
@Api(value= "App操作类", description ="人民币资金流水以及币的充值提现流水")
public class AppMoneyDetailController {

	@RequestMapping(value = "/rmbSelect")
	@ApiOperation(value = "人民币流水查询(JsonResult + frontPage + rows)", httpMethod = "POST", response = AppTransactionManage.class, notes = "人民币流水查询,transactionType传all,查询全部，status传0，查询全部")
	@ResponseBody
	public JsonResult rmbSelect(HttpServletRequest request,@RequestParam String transactionType,@RequestParam String status){
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			if(user != null){
				RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
				Map<String, String> params = HttpServletRequestUtils.getParams(request);
				params.put("customerId", user.getCustomerId().toString());
				if("all".equals(transactionType)){//all查全部
					params.put("transactionType", null);
				}
				if("0".equals(status)){//0查全部
					params.put("status",null);
				}
				return new JsonResult().setSuccess(true).setObj(remoteAppTransactionManageService.findTransaction(params));
			}
		}
		return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
	}
	
	
	/**
	 * 查询充值币的记录
	 * 交易类型(1充币 ，2提币)'
	 * @return
	 */
	@RequestMapping("/list")
	@ApiOperation(value = "币的流水查询(JsonResult + frontPage + rows)", httpMethod = "POST", response = ExDmTransactionManage.class, notes = "transactionType 1充币 ，2提币")
	@ResponseBody
	public JsonResult list(HttpServletRequest request,@RequestParam String transactionType) {
		String tokenId = request.getParameter("tokenId");
		String coinCode = request.getParameter("coinCode");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			if(user != null){
				RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
				Map<String, String> params = HttpServletRequestUtils.getParams(request);
				params.put("customerId", user.getCustomerId().toString());
				params.put("transactionType", transactionType);
				//params.put("status", status);
				FrontPage page = remoteAppTransactionManageService.findExdmtransaction(params);
				List<ExDmTransactionManage> list = page.getRows();
				List<ExDmTransactionManage> list2 = new ArrayList<ExDmTransactionManage>();
				if(coinCode==null){
					list2 = list;
				}else{
					for (ExDmTransactionManage object : list) {
						if(object.getCoinCode().equals(coinCode)){
							list2.add(object);
						}
					}
				}
				page.setRows(list2);
				return new JsonResult().setSuccess(true).setObj(page);
			}
		}
		return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
	}
}
