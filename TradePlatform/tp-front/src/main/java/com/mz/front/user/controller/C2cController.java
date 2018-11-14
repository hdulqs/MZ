package com.mz.front.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.util.shiro.PasswordHelper;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.c2c.C2cOrder;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.SessionUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public class C2cController {
	private final static Logger log = Logger.getLogger(C2cController.class);
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

	
	@Autowired
	private RedisService redisService;
	
	/**
	 * c2c入口进到c2c默认的币种页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("c2c")
	public String c2c(HttpServletRequest request, HttpServletResponse response) {
		String str = redisService.get("cn:c2clist");
		String coinCode = "BTC";
		if(!StringUtils.isEmpty(str)){
			List<String> list = JSON.parseArray(str, String.class);
			if(list!=null&&list.size()>0){
				coinCode = list.get(0);
			}
		}
		return "redirect:/c2c/"+coinCode+".do?activeId=c2c";
	}
	
	
	/**
	 * 跳到c2c页面
	 * @param request
	 * @param coinCode
	 * @return
	 */
	@RequestMapping("c2c/{coinCode}")
	public ModelAndView c2cinfo(HttpServletRequest request,@PathVariable String coinCode) {
		ModelAndView view = new ModelAndView("c2c");
		
		//查询所有开通c2c的币
		String str = redisService.get("cn:c2clist");
		if(!StringUtils.isEmpty(str)){
			List<String> list = JSON.parseArray(str, String.class);
			view.addObject("coinList", list);
		}
		//当前激活的节点
		view.addObject("activeCoin",coinCode);

		view.addObject("showColor",6);
		
		//获得c2cCoin表配置信息
		String c2cCoinList = redisService.get("cn:c2cCoinList");
		if(!StringUtils.isEmpty(c2cCoinList)){
			JSONArray parseArray = JSON.parseArray(c2cCoinList);
			if(parseArray!=null){
				for(int i = 0 ; i < parseArray.size(); i++ ){
					JSONObject jsonObject = parseArray.getJSONObject(i);
					if(coinCode.equals(jsonObject.getString("coinCode"))){
						view.addObject("c2cBuyPrice", jsonObject.getBigDecimal("buyPrice"));
						view.addObject("c2cSellPrice", jsonObject.getBigDecimal("sellPrice"));
					}
				}
			}
		}
		
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		
		List<C2cOrder> buyList  = remoteManageService.c2cNewBuyOrder();
		view.addObject("buyList",buyList);
		List<C2cOrder> sellList = remoteManageService.c2cNewSellOrder();
		view.addObject("sellList",sellList);
		
		//如果登录了，查询最近10条c2c记录
		User user = SessionUtils.getUser(request);
		if(user!=null){
			view.addObject("user", user);
			List<C2cOrder> listOrder = remoteManageService.c2c10Order(user.getCustomerId(),coinCode);
			if(listOrder!=null){
				view.addObject("orderList",listOrder);
			}
		}
		
		return view;
	}
	
	/**
	 * 查看c2c订单详情
	 * @param request
	 * @param transactionNum
	 * @return
	 */
	@RequestMapping("user/getc2cTransaction/{transactionNum}")
	public ModelAndView getc2cTransaction(HttpServletRequest request,@PathVariable String transactionNum) {
		
		ModelAndView view = new ModelAndView("c2cdetail");
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		String c2cOrderDetail = remoteManageService.getC2cOrderDetail(transactionNum);
		if(!StringUtils.isEmpty(c2cOrderDetail)){
			JSONObject obj = JSON.parseObject(c2cOrderDetail);
			view.addObject("obj", obj);
		}
		
		return view;
	}
	
	
	/**
	 * 支付完成
	 * @param request
	 * @param transactionNum
	 * @return
	 */
	@RequestMapping("user/payc2cTransaction/{transactionNum}")
	@ResponseBody
	public JsonResult payc2cTransaction(HttpServletRequest request,@PathVariable String transactionNum) {
		
		JsonResult jsonResult = new JsonResult();
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		boolean flag = remoteManageService.setc2cTransactionStatus2(transactionNum,2,null);
		if(flag){
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("订单号错误");
		}
		return jsonResult;
	}
	
	/**
	 * 确认到账
	 * @param request
	 * @param transactionNum
	 * @return
	 */
	@RequestMapping("user/confirm/{transactionNum}")
	@ResponseBody
	public JsonResult Confirm(HttpServletRequest request,@PathVariable String transactionNum) {
		
		JsonResult jsonResult = new JsonResult();
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		boolean flag = remoteManageService.setc2cTransactionStatus(transactionNum,2,null);
		if(flag){
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("订单号错误");
		}
		return jsonResult;
	}
	
	
	/**
	 * 交易关闭
	 * @param request
	 * @param transactionNum
	 * @return
	 */
	@RequestMapping("user/closec2cTransaction/{transactionNum}")
	@ResponseBody
	public JsonResult closec2cTransaction(HttpServletRequest request,@PathVariable String transactionNum) {
		
		String remark = request.getParameter("remark");
		JsonResult jsonResult = new JsonResult();
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		boolean flag = remoteManageService.setc2cTransactionStatus2(transactionNum,4,remark);
		if(flag){
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("订单号错误");
		}
		return jsonResult;
	}
	
	
	/**
	 * 交易失败
	 * @param request
	 * @param transactionNum
	 * @return
	 */
	@RequestMapping("user/failc2cTransaction/{transactionNum}")
	@ResponseBody
	public JsonResult failc2cTransaction(HttpServletRequest request,@PathVariable String transactionNum) {
		
		String remark = request.getParameter("remark");
		JsonResult jsonResult = new JsonResult();
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		boolean flag = remoteManageService.setc2cTransactionStatus2(transactionNum,3,remark);
		if(flag){
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("订单号错误");
		}
		return jsonResult;
	}
	
	
	/**
	 * c2c下单
	 * @param request
	 * @return
	 */
	@RequestMapping("user/createTransaction")
	@ResponseBody
	public JsonResult createTransaction(HttpServletRequest request) {
		
		String coinCode = request.getParameter("coinCode");
		String transactionType = request.getParameter("transactionType");
		String transactionCount = request.getParameter("transactionCount");
		String transactionPrice = request.getParameter("transactionPrice");
		String accountPassWord = request.getParameter("accountPassWord");
		String verifyCode = request.getParameter("verifyCode");
		
		User user = SessionUtils.getUser(request);
		if(user!=null){

			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setMsg("请到个人中心进行实名!");
			}
            if(("2").equals(transactionType)) {
				//判断是否手机认证
				if (user.getPhoneState() != 1) {
					return new JsonResult().setMsg("请到个人中心进行绑定手机!");
				}

				String session_verifyCode = redisService.get("SMS:smsphone:" + user.getPhone());
				if (verifyCode == null || !verifyCode.equals(session_verifyCode)) {
					return new JsonResult().setMsg(SpringContextUtil.diff("短信验证错误或已失效！"));
				}
				if (user.getAccountPassWord() != null && !"".equals(user.getAccountPassWord())) {
					PasswordHelper passwordHelper = new PasswordHelper();
					String pw = passwordHelper.encryString(accountPassWord, user.getSalt());
					if (!pw.equals(user.getAccountPassWord())) {
						return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("mimacuowu"));
					}
				}
			}

			
			C2cOrder c2cOrder = new C2cOrder();
			//交易币种
			c2cOrder.setCoinCode(coinCode);
			//交易单号
			c2cOrder.setTransactionNum(UUID.randomUUID().toString().replace("-", ""));
			//交易数量
			c2cOrder.setTransactionCount(new BigDecimal(transactionCount));
			//交易价格
			c2cOrder.setTransactionPrice(new BigDecimal(transactionPrice));
			//用户名
			c2cOrder.setUserName(user.getUsername());
			//交易类型1买，2卖
			c2cOrder.setTransactionType(Integer.valueOf(transactionType));
			//customerId
			c2cOrder.setCustomerId(user.getCustomerId());
			RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
			UserRedis userRedis = redisUtil.get(user.getCustomerId().toString());
			Long coinAccountId = userRedis.getDmAccountId(coinCode);
			//虚拟币账户id,要买的币，或者要卖的币
			c2cOrder.setAccountId(coinAccountId);
			
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult remoteResult = null;
			try {
				remoteResult = remoteManageService.createC2cOrder(c2cOrder);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(remoteResult!=null){
				if(remoteResult.getSuccess()){
					return new JsonResult().setSuccess(true).setObj(remoteResult.getObj()).setMsg(SpringContextUtil.diff("success"));
				}else{
					return new JsonResult().setMsg(remoteResult.getMsg());
				}
			}else{
				return new JsonResult().setMsg("remote错误");
			}
			
		}
		
		return new JsonResult();
	}
	
	@RequestMapping("/user/c2clist")
	@ResponseBody
	public FrontPage list(HttpServletRequest request) {
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		String type = request.getParameter("transactionType");
		User user = SessionUtils.getUser(request);
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		if ("0".equals(type)) {// 0查全部
			params.put("transactionType", null);
		}
		params.put("customerId", user.getCustomerId().toString());
		FrontPage findTrades = remoteManageService.c2clist(params);
		return findTrades;

	}

}
