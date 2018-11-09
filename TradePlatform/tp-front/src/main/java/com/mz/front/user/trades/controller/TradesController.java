package com.mz.front.user.trades.controller;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.model.Order;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.trade.redis.model.EntrustTrade;
import com.mz.util.SessionUtils;
import com.mz.util.common.Constant;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/trades")
public class TradesController {

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
	
	@Resource
	private RemoteManageService remoteManageService;
	
	/**
	 * 查询交易记录
	 * 
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public FrontPage list(HttpServletRequest request) {
		RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
		String type = request.getParameter("type");
		User user = SessionUtils.getUser(request);
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

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult add(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String config = redisService.get("configCache:all");
		JSONObject parseObject = JSONObject.parseObject(config);
		
		User user = SessionUtils.getUser(request);
		
		String isTrade = parseObject.get("isTrade").toString();
		if(isTrade!=null && "1".equals(isTrade)){
			
			if(user.getIsChange()!=1){
				//交易币代码
				String coinCode = request.getParameter("coinCode");
				//来源
				String source = request.getParameter("source");
				//类型
				String type = request.getParameter("type");
				
				String entrustWay = request.getParameter("entrustWay");
				//委托价格
				String entrustPrice = request.getParameter("entrustPrice");
				//委托 数量
				String entrustCount = request.getParameter("entrustCount");
				String entrustSum = request.getParameter("entrustSum");
				String[] split = coinCode.split("_");
				
				EntrustTrade exEntrust = new EntrustTrade();
				//用户id
				exEntrust.setCustomerId(user.getCustomerId());
				exEntrust.setTrueName(user.getTruename());
				exEntrust.setCoinCode(split[0]);
				exEntrust.setFixPriceCoinCode(split[1]);
				exEntrust.setEntrustWay(Integer.valueOf(entrustWay));
				exEntrust.setType(Integer.valueOf(type));
				exEntrust.setSource(Integer.valueOf(source));
				exEntrust.setUserName(user.getUsername());
				exEntrust.setSurName(user.getSurname());
				if(entrustWay.equals("1")){
					exEntrust.setEntrustPrice(new BigDecimal(entrustPrice));
					exEntrust.setEntrustCount(new BigDecimal(entrustCount));
				}else if(entrustWay.equals("2")){
					if(exEntrust.getType().intValue()==1){
						exEntrust.setEntrustSum(new BigDecimal(entrustSum));
					}else{
						exEntrust.setEntrustCount(new BigDecimal(entrustCount));
					}
					
				}
				
				
				// 委托业务
				String[] relt = remoteManageService.addEntrust(exEntrust);
				if (relt[0].equals(Constant.CODE_SUCCESS)) {
					jsonResult.setSuccess(true);
					jsonResult.setMsg(SpringContextUtil.diff("delegate_success"));
				} else {
					jsonResult.setSuccess(false);
					if(relt[1].contains("不足")){
						jsonResult.setMsg(relt[1].replace("不足", "")+SpringContextUtil.diff("buzhu"));
					}else{
						jsonResult.setMsg(SpringContextUtil.diff(relt[1]));
					}
				}
				}else{
				jsonResult.setMsg(SpringContextUtil.diff("jinzhijiaoyi"));
			}
		}else{
			User selectByTel = remoteManageService.selectByTel(user.getUsername());
			if(selectByTel!=null && selectByTel.getStates()==2){
				if(user.getIsReal()==1){
					if(user.getIsChange()!=1){
						//交易币代码
						String coinCode = request.getParameter("coinCode");
						//来源
						String source = request.getParameter("source");
						//类型
						String type = request.getParameter("type");
						
						String entrustWay = request.getParameter("entrustWay");
						//委托价格
						String entrustPrice = request.getParameter("entrustPrice");
						//委托 数量
						String entrustCount = request.getParameter("entrustCount");
						String entrustSum = request.getParameter("entrustSum");
						String[] split = coinCode.split("_");
						
						EntrustTrade exEntrust = new EntrustTrade();
						//用户id
						exEntrust.setCustomerId(user.getCustomerId());
						exEntrust.setTrueName(user.getTruename());
						exEntrust.setCoinCode(split[0]);
						exEntrust.setFixPriceCoinCode(split[1]);
						exEntrust.setEntrustWay(Integer.valueOf(entrustWay));
						exEntrust.setType(Integer.valueOf(type));
						exEntrust.setSource(Integer.valueOf(source));
						exEntrust.setUserName(user.getUsername());
						exEntrust.setSurName(user.getSurname());
						if(entrustWay.equals("1")){
							exEntrust.setEntrustPrice(new BigDecimal(entrustPrice));
							exEntrust.setEntrustCount(new BigDecimal(entrustCount));
						}else if(entrustWay.equals("2")){
							if(exEntrust.getType().intValue()==1){
								exEntrust.setEntrustSum(new BigDecimal(entrustSum));
							}else{
								exEntrust.setEntrustCount(new BigDecimal(entrustCount));
							}
							
						}
						
						
						// 委托业务
						String[] relt = remoteManageService.addEntrust(exEntrust);
						if (relt[0].equals(Constant.CODE_SUCCESS)) {
							jsonResult.setSuccess(true);
							jsonResult.setMsg(SpringContextUtil.diff("delegate_success"));
						} else {
							jsonResult.setSuccess(false);
							if(relt[1].contains("不足")){
								jsonResult.setMsg(relt[1].replace("不足", "")+SpringContextUtil.diff("buzhu"));
							}else{
								jsonResult.setMsg(SpringContextUtil.diff(relt[1]));
							}
						}
						}else{
							jsonResult.setMsg(SpringContextUtil.diff("jinzhijiaoyi"));
						}
					return jsonResult;
				}
			}
			jsonResult.setMsg(SpringContextUtil.diff("qingxianshimingrenzheng"));
		}
		return jsonResult;
	}

	@RequestMapping(value = "/cancelExEntrust")
	@ResponseBody
	public JsonResult cancelExEntrust(HttpServletRequest request) {
		String language = (String) request.getAttribute("language");

		JsonResult jsonResult = new JsonResult();
		User user = SessionUtils.getUser(request);
		//RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());

		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		String[] re = remoteManageService.checkPing(user.getCustomerId());
		if(re[0].equals("0000")){
			jsonResult.setSuccess(false);
			jsonResult.setMsg(SpringContextUtil.diff(re[1]));
			return jsonResult;
		}

		String entrustNums = request.getParameter("entrustNums").toString();
		String entrustPrice = request.getParameter("entrustPrice").toString();
		String coinCode = request.getParameter("coinCode");
		String[] split = coinCode.split("_");
		String type = request.getParameter("type");
		EntrustTrade entrustTrade=new EntrustTrade();
		entrustTrade.setEntrustNum(entrustNums);
		if(split[0].contains("-")){
			String[] splitt = split[0].split("-");
			split[0] = splitt[0];
		}
		entrustTrade.setCoinCode(split[0]);
		entrustTrade.setType(Integer.valueOf(type));
		entrustTrade.setFixPriceCoinCode(split[1]);
		if(entrustPrice.equals("市价")||entrustPrice.equals("Market Price")){
			entrustTrade.setEntrustPrice(new BigDecimal("0"));
		}else{
			entrustTrade.setEntrustPrice(new BigDecimal(entrustPrice));
		}
		remoteManageService.cancelExEntrust(entrustTrade);
		jsonResult.setSuccess(true);
		jsonResult.setMsg(SpringContextUtil.diff("revoke_success"));
		return jsonResult;

	}
	@RequestMapping(value = "/cancelCustAllExEntrust")
	@ResponseBody
	public JsonResult cancelCustAllExEntrust(HttpServletRequest request) {
		String language = (String) request.getAttribute("language");
		JsonResult jsonResult = new JsonResult();
		User user = SessionUtils.getUser(request);
		String coinCode = request.getParameter("coinCode");
		String[] split = coinCode.split("_");
		EntrustTrade entrustTrade=new EntrustTrade();
		entrustTrade.setCoinCode(split[0]);
		entrustTrade.setFixPriceCoinCode(split[1]);
		entrustTrade.setCustomerId(user.getCustomerId());
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		remoteManageService.cancelCustAllExEntrust(entrustTrade);
		jsonResult.setSuccess(true);
		jsonResult.setMsg(SpringContextUtil.diff("revoke_success"));
		return jsonResult;

	}
	@RequestMapping(value = "/cancelCustAllAllExEntrust")
	@ResponseBody
	public JsonResult cancelCustAllAllExEntrust(HttpServletRequest request) {
		/*String language = (String) request.getAttribute("language");
		
		User user = SessionUtils.getUser(request);
		//RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		remoteManageService.cancelCustAllExEntrust(user.getCustomerId());*/
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		jsonResult.setMsg(SpringContextUtil.diff("revoke_success"));
		return jsonResult;

	}
}
