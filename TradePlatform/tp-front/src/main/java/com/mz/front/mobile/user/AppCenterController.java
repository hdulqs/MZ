package com.mz.front.mobile.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.RemoteOamessageService;
import com.mz.manage.remote.model.Entrust;
import com.mz.manage.remote.model.MyAccountTO;
import com.mz.manage.remote.model.Oamessage;
import com.mz.manage.remote.model.Order;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/mobile/user/appCenter")
@Api(value= "App操作类", description ="个人中心和委托管理和我的消息")
public class AppCenterController {


	@RequestMapping(value="/cancelAllExEntrust", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ApiOperation(value = "个人中心-我要委托", httpMethod = "POST", response = JsonResult.class, notes = "type传current位当前委托，传history为历史委托")
	@ResponseBody
	public JsonResult cancelAllExEntrust(HttpServletRequest request) {
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			
			
			
			RemoteManageService service  = SpringContextUtil.getBean("remoteManageService");
			Map<String, String> params = HttpServletRequestUtils.getParams(request);
			params.put("userName",user.getMobile() );
			
			return new JsonResult().setSuccess(true).setObj(service.findEntrust(params));
		}
		return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
	}
	
	
	
	@RequestMapping(value="/myAccount", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ApiOperation(value = "个人中心-资产(JsonResult + obj)", httpMethod = "POST", response = MyAccountTO.class, notes = "总资产，净资产等")
	@ResponseBody
	public JsonResult myAccount(HttpServletRequest request){
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			if(user != null){
				MyAccountTO myAccount = remoteManageService.myAccount(user.getCustomerId());
				return new JsonResult().setSuccess(true).setObj(myAccount);
			}
		}
		return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
	}
	
	/*
	@RequestMapping(value="/appentrustlist", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ApiOperation(value = "个人中心-我要委托", httpMethod = "POST", response = JsonResult.class, notes = "type传current位当前委托，传history为历史委托")
	@ResponseBody
	public JsonResult appentrustlist(HttpServletRequest request,@RequestParam String type,@RequestParam Integer offset,@RequestParam Integer limit) {
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			
			String coinCode = request.getParameter("coinCode");
			if(user.getIsReal()!=1){
				return new JsonResult().setSuccess(true).setObj(new FrontPage(null, 0, 1, 5));
			}
			if(limit!=null){
				limit = Integer.valueOf(10);
			}
			
			RemoteManageService service  = SpringContextUtil.getBean("remoteManageService");
			Map<String, String> params = HttpServletRequestUtils.getParams(request);
			params.put("userName",user.getMobile() );
			if(!StringUtils.isEmpty(coinCode)){
				String[] split = coinCode.split("_");
				params.put("coinCode",split[0] );
				params.put("fixPriceCoinCode",split[1] );
			}
			return new JsonResult().setSuccess(true).setObj(service.findEntrust(params));
		}
		return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
	}
	*/
	
	/**
	 * 查询交易记录
	 * 
	 * @return
	 */
	@RequestMapping(value="/apptradeslist", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ApiOperation(value = "个人中心-我要交易(FrontPage + Rows)", httpMethod = "POST", response = Order.class, notes = "limit:'4',offset:'0',sortOrder:'asc',type:type,transactionType:'chongzhi'")
	@ResponseBody
	public FrontPage apptradeslist(HttpServletRequest request) {
		RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
		String type = request.getParameter("type");
		User user = SessionUtils.getUser(request);
		//add by zongwei 20180509 如果用户取不到通过 tokenId取   begin
		if(user == null){
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value!=null){
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			 user = remoteManageService.selectByTel(tel);
		 }
		}
		//add by zongwei 20180509 如果用户取不到通过 tokenId取   end
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		params.put("userName", user.getMobile());
		if ("0".equals(type)) {// 0查全部
			params.put("type", null);
		}
		params.put("customerId", user.getCustomerId().toString());
		FrontPage findTrades = remoteAppTransactionManageService.frontselectFee(params);
		List<Order> list = findTrades.getRows();
		for(int i=0;i<list.size();i++){
			Order order = list.get(i);
			if(list.get(i).getType()==1){
				
				list.get(i).setCoin(list.get(i).getCoinCode());
			}else if(list.get(i).getType()==2){
				list.get(i).setCoin(list.get(i).getFixPriceCoinCode());
			}
			order.setTransactionTime_long(order.getTransactionTime().getTime());
			list.get(i).setCoinCode(list.get(i).getCoinCode() + "-" + list.get(i).getFixPriceCoinCode());
		}
		return findTrades;

	}

	/**
	 * 委托记录
	 * 
	 * @return
	 */
	@RequestMapping(value="/appentrustList", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ApiOperation(value = "委托记录(FrontPage + Rows)", httpMethod = "POST", response = Entrust.class, notes = "limit:'4',offset:'0',sortOrder:'asc',typeone:'0',querypath:'center',type:'current 当前委托，history 历史委托'")
	@ResponseBody
	public FrontPage appentrustList(HttpServletRequest request) {

		String querypath = request.getParameter("querypath");

		// 交易类型
		String typeone = request.getParameter("typeone");
		User user = SessionUtils.getUser(request);
		//add by zongwei 如果user 取不到，通过 tokenId 取 begin 20180504
		if (user == null ){
			String tokenId = request.getParameter("tokenId");
			RedisService redisService = SpringContextUtil.getBean("redisService");
			String value = redisService.get("mobile:"+tokenId);
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			 user = remoteManageService.selectByTel(tel);
		}
		//add by zongwei 如果user 取不到，通过 tokenId 取 end 20180504
//		if (user.getIsReal() != 1) {
//			return new FrontPage(null, 0, 1, 5);
//		}
		RemoteManageService service = SpringContextUtil.getBean("remoteManageService");
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		// 全部
		if ("0".equals(typeone)) {
			params.put("typeone", null);
		}
		params.put("customerId", user.getCustomerId().toString());
		if (!"center".equals(querypath)) {// 如果是个人中心查询，加上coinCode查询
			String coinCode = request.getParameter("coinCode");
			if (StringUtils.isEmpty(coinCode) || !coinCode.contains("_")) {
				return null;
			}
			if (!StringUtils.isEmpty(coinCode)) {
				String[] split = coinCode.split("_");
				params.put("coinCode", split[0]);
				params.put("fixPriceCoinCode", split[1]);
			}
		}

		FrontPage findTrades = service.findEntrust(params);
		List<Entrust> list = findTrades.getRows();
		for(int i=0;i<list.size();i++){
			Entrust entrust = list.get(i);
			entrust.setCoin(list.get(i).getCoin());
			entrust.setEntrustTime_long(entrust.getEntrustTime().getTime());
			entrust.setCoinCode(list.get(i).getCoinCode() + "-" + list.get(i).getFixPriceCoinCode());
		}
		return findTrades;
	}

	/**
	 * 获取用户消息列表
	 * @param request
	 * @return
	 * 2017年7月19日
	 * tzw
	 */
	@ResponseBody
	@ApiOperation(value = "获取用户消息列表(FrontPage + rows)", httpMethod = "POST", response = Oamessage.class, notes = "tokenId")
	@RequestMapping("/list")
	public FrontPage list(HttpServletRequest request) {
		User user=SessionUtils.getUser(request);
		RemoteOamessageService service  = SpringContextUtil.getBean("remoteOamessageService");
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		params.put("customerName", user.getMobile());
		return service.findOamessage(params);
	}
	
}
