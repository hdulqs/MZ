package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.otc.OtcOrderTransactionMange;
import com.mz.manage.remote.model.otc.OtcTransactionOrder;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.FileType;
import com.mz.util.FileUpload;
import com.mz.util.SessionUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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


@Controller
@RequestMapping("/mobile")
@Api(value= "OTC操作类", description ="OTC所有功能")
public class AppOtcController {
	private final static Logger log = Logger.getLogger(AppOtcController.class);


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
		String[] pathImg = new String[3];
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



	@Autowired
	private RedisService redisService;

	/**
	 * c2c入口进到c2c默认的币种页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/otc/query")
	@ResponseBody
    @ApiOperation(value = "OTC币查询", httpMethod = "POST", response = JsonResult.class, notes = "")
	public JsonResult otc(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult =  new JsonResult();
		String str = redisService.get("cn:Otclist");
		if(!StringUtils.isEmpty(str)){
			List<String> list = JSON.parseArray(str, String.class);
			jsonResult.setSuccess(true).setObj(list);
		}
		return jsonResult;
	}


	/**
	 * 跳到c2c页面
	 * @param request
	 * @param coinCode
	 * @return
	 */
	@RequestMapping("otc/{coinCode}")
	@ResponseBody
    @ApiOperation(value = "OTC配置信息查询", httpMethod = "POST", response = JsonResult.class, notes = "")
	public JsonResult otcinfo(HttpServletRequest request,@PathVariable String coinCode) {
		JsonResult jsonResult =  new JsonResult();
		JSONObject view =  new JSONObject();
		//查询所有开通Otc的币
		String str = redisService.get("cn:Otclist");
		if(!StringUtils.isEmpty(str)){
			List<String> list = JSON.parseArray(str, String.class);
			view.put("coinList", list);
		}
		//当前激活的节点
		view.put("activeCoin",coinCode);

		view.put("showColor",7);

		//获得OtcCoin表配置信息
		String otcCoinList = redisService.get("cn:OtcCoinList");
		if(!StringUtils.isEmpty(otcCoinList)){
			JSONArray parseArray = JSON.parseArray(otcCoinList);
			if(parseArray!=null){
				for(int i = 0 ; i < parseArray.size(); i++ ){
					JSONObject jsonObject = parseArray.getJSONObject(i);
					if(coinCode.equals(jsonObject.getString("coinCode"))){
						view.put("otcpoundage_type", jsonObject.get("poundage_type"));
						view.put("otcpoundage", jsonObject.getBigDecimal("poundage"));
						view.put("otcBuyPrice", jsonObject.getBigDecimal("buyPrice"));
						view.put("otcSellPrice", jsonObject.getBigDecimal("sellPrice"));
                        view.put("otcminBuyPrice", jsonObject.getBigDecimal("minbuyPrice"));
                        view.put("otcmaxBuyPrice", jsonObject.getBigDecimal("maxbuyPrice"));
                        view.put("otcminSellPrice", jsonObject.getBigDecimal("minsellPrice"));
                        view.put("otcmaxSellPrice", jsonObject.getBigDecimal("maxsellPrice"));
						view.put("minCount",jsonObject.getBigDecimal("minCount"));
						view.put("maxCount",jsonObject.getBigDecimal("maxCount"));
					}
				}
			}
		}

		jsonResult.setSuccess(true).setObj(view);
		return jsonResult;
	}


	/**
	 * OTC下单
	 * @param request
	 * @return
	 */
	@RequestMapping("user/otcCreateTransaction")
	@ResponseBody
    @ApiOperation(value = "OTC委托下单", httpMethod = "POST", response = JsonResult.class, notes = "coinCode：币种;transactionType:1买2卖;transactionCount:数量；transactionPrice 单价；tokenId")
	public JsonResult createTransaction(HttpServletRequest request) {

		String coinCode = request.getParameter("coinCode");
		String transactionType = request.getParameter("transactionType");
		String transactionCount = request.getParameter("transactionCount");
		String transactionPrice = request.getParameter("transactionPrice");

		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  new JsonResult().setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);
		if(user!=null){

			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setMsg("请到个人中心进行实名!");
			}

			if(user.getPhoneState()!=1){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心绑定手机!");
			}

			if(user.getOpenOtcStates()!=1){
				return new JsonResult().setSuccess(false).setMsg("请联系客服开通委托下单功能!");
			}

			OtcTransactionOrder otcOrder = new OtcTransactionOrder();
			//交易币种
			otcOrder.setCoinCode(coinCode);
			//交易单号
			otcOrder.setTransactionNum(UUID.randomUUID().toString().replace("-", ""));
			//交易数量
			otcOrder.setTransactionCount(new BigDecimal(transactionCount));
			//交易价格
			otcOrder.setTransactionPrice(new BigDecimal(transactionPrice));
			//用户名
			otcOrder.setUserName(user.getUsername());
			//交易类型1买，2卖
			otcOrder.setTransactionType(Integer.valueOf(transactionType));
			//customerId
			otcOrder.setCustomerId(user.getCustomerId());
			RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
			UserRedis userRedis = redisUtil.get(user.getCustomerId().toString());
			Long coinAccountId = userRedis.getDmAccountId(coinCode);
			//虚拟币账户id,要买的币，或者要卖的币
			otcOrder.setAccountId(coinAccountId);

			RemoteResult remoteResult = null;
			try {
				remoteResult = remoteManageService.createOtcOrder(otcOrder);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(remoteResult!=null){
				if(remoteResult.getSuccess()){
					return new JsonResult().setSuccess(true).setObj(remoteResult.getObj()).setMsg(SpringContextUtil.diff("success"));
				}else{
					return new JsonResult().setMsg(SpringContextUtil.diff(remoteResult.getMsg()));
				}
			}else{
				return new JsonResult().setSuccess(false).setMsg("remote错误");
			}

		}else{
			return new JsonResult().setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}

	}





	@RequestMapping("/user/otcPayInfor")
	@ResponseBody
    @ApiOperation(value = "付款信息", httpMethod = "POST", response = JsonResult.class, notes = "id；tokenId")
	public JsonResult otcPayInfor(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		JSONObject view =  new JSONObject();
        String transactionorderid = request.getParameter("id");

        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
        String tel = JSONObject.parseObject(value).getString("mobile");
        RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
        User selectuser = remoteManageService.selectByTel(tel);

		OtcOrderTransactionMange otcOrderTransactionMange  =	remoteManageService.selectOtcOrderbyid(Integer.valueOf(transactionorderid).longValue());
        JSONObject userinfo =  new JSONObject();
        if(selectuser != null) {
			User user = new User();
			if (selectuser.getCustomerId().equals(otcOrderTransactionMange.getBuyCustomId())) {
				 user = remoteManageService.selectByustomerId(otcOrderTransactionMange.getSellCustomId());
			} else {
				 user = remoteManageService.selectByustomerId(otcOrderTransactionMange.getBuyCustomId());
			}

			RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
			List<AppBankCardManage> list ;
			if (selectuser.getCustomerId().equals(otcOrderTransactionMange.getBuyCustomId())) {
				list = remoteAppBankCardManageService.findByCustomerId(otcOrderTransactionMange.getSellCustomId());
			} else {
				list = remoteAppBankCardManageService.findByCustomerId(otcOrderTransactionMange.getBuyCustomId());
			}
			userinfo.put("username", user.getUsername());
			userinfo.put("name", user.getSurname() + user.getTruename());
			userinfo.put("phone", user.getPhone());

			if (list != null && !list.isEmpty()) {
				view.put("Bankinfo", list.get(0));
			}

			view.put("userinfo", userinfo);
			view.put("orderinfo", otcOrderTransactionMange);
			return jsonResult.setSuccess(true).setObj(view);
		}else{
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}

	}



	@RequestMapping("/user/otcUndo")
	@ResponseBody
    @ApiOperation(value = "订单信息", httpMethod = "POST", response = JsonResult.class, notes = "id；tokenId")
	public JsonResult otcUndo(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		JSONObject view =  new JSONObject();
		String transactionorderid = request.getParameter("id");
		String transactionNum = request.getParameter("transactionNum");

		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		RemoteResult  remoteResult =	remoteManageService.getOtcbyid(Integer.valueOf(transactionorderid).longValue());

		view.put("transactionorderid", transactionorderid);
		view.put("orderinfo", remoteResult.getObj());
		return jsonResult.setSuccess(true).setObj(view);
	}

	@RequestMapping("/user/otcApplyArbitration")
	@ResponseBody
    @ApiOperation(value = "我要申诉信息", httpMethod = "POST", response = JsonResult.class, notes = "id；tokenId")
	public JsonResult otcApplyArbitration(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		JSONObject view =  new JSONObject();
		String transactionorderid = request.getParameter("id");
		String transactionNum = request.getParameter("transactionNum");
		view.put("transactionorderid", transactionorderid);
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		RemoteResult  remoteResult =	remoteManageService.getOtcbyid(Integer.valueOf(transactionorderid).longValue());

		view.put("transactionorderid", transactionorderid);
		view.put("orderinfo", remoteResult.getObj());
		return jsonResult.setSuccess(true).setObj(view);
	}

	@RequestMapping("/user/otcApplyArbitrationInfo")
	@ResponseBody
    @ApiOperation(value = "申诉信息", httpMethod = "POST", response = JsonResult.class, notes = "id；tokenId")
	public JsonResult otcApplyArbitrationInfo(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		JSONObject view =  new JSONObject();
		String transactionorderid = request.getParameter("id");
		String transactionNum = request.getParameter("transactionNum");
		RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
		RemoteResult  remoteResult =	remoteManageService.getOtcbyid(Integer.valueOf(transactionorderid).longValue());

		view.put("transactionorderid", transactionorderid);
		view.put("orderinfo", remoteResult.getObj());
		return jsonResult.setSuccess(true).setObj(view);
	}

	@RequestMapping("/otc/getOtcTransactionAll")
	@ResponseBody
    @ApiOperation(value = "otc委托列表", httpMethod = "POST", response = JsonResult.class, notes = "id；tokenId")
	public JsonResult getOtcTransactionAll(HttpServletRequest request) {
		JsonResult jsonResult =  new JsonResult();
//		String tokenId = request.getParameter("tokenId");
//		RedisService redisService = SpringContextUtil.getBean("redisService");
//		String value = redisService.get("mobile:"+tokenId);
//		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		//User user = remoteManageService.selectByTel(tel);
		//if(user!=null){

			RemoteResult  remoteResult =	remoteManageService.getOtclist(null,null,null);
			if(remoteResult.getSuccess()) {
				jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
			}else{
				jsonResult.setSuccess(false).setMsg("失败!");
			}
		//}else{
        //    return new JsonResult().setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
       // }
		return jsonResult;
	}

	/**
	 * @Description:    买单挂单列表
	 * @Author:         zongwei
	 * @CreateDate:     2018/6/22 10:05
	 * @UpdateUser:    zongwei
	 * @UpdateDate:     2018/6/22 10:05
	 * @UpdateRemark:   创建
	 * @Version:        1.0
	 */
	@RequestMapping("/otc/getOtcTransactionForBuy")
	@ResponseBody
	@ApiOperation(value = "买单挂单列表", httpMethod = "POST", response = JsonResult.class, notes = "")
	public JsonResult getOtcTransactionForBuy(HttpServletRequest request) {
		JsonResult jsonResult =  new JsonResult();
		User user = SessionUtils.getUser(request);
		String coinCode = null;
		coinCode = request.getParameter("coinCode");
		//if(user!=null){
		try {
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult  remoteResult =	remoteManageService.getOtclist("1",coinCode,"desc");
			if(remoteResult.getSuccess()) {
				jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
			}else{
				jsonResult.setSuccess(false).setMsg("失败!");
			}
		}catch (Exception e){
			jsonResult.setSuccess(false).setMsg("失败!");
		}
		//}
		return jsonResult;
	}

	/**
	 * @Description:    卖单挂单列表
	 * @Author:         zongwei
	 * @CreateDate:     2018/6/22 10:05
	 * @UpdateUser:    zongwei
	 * @UpdateDate:     2018/6/22 10:05
	 * @UpdateRemark:   创建
	 * @Version:        1.0
	 */
	@RequestMapping("/otc/getOtcTransactionForSell")
	@ResponseBody
	@ApiOperation(value = "卖单挂单列表", httpMethod = "POST", response = JsonResult.class, notes = "")
	public JsonResult getOtcTransactionForSell(HttpServletRequest request) {
		JsonResult jsonResult =  new JsonResult();
		User user = SessionUtils.getUser(request);
		String coinCode = null;
		coinCode = request.getParameter("coinCode");
		//if(user!=null){
		try {
			RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
			RemoteResult  remoteResult =	remoteManageService.getOtclist("2",coinCode ,null);
			if(remoteResult.getSuccess()) {
				jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
			}else{
				jsonResult.setSuccess(false).setMsg("失败!");
			}
		}catch (Exception e){
			jsonResult.setSuccess(false).setMsg("失败!");
		}
		//}
		return jsonResult;
	}

	@RequestMapping("/otc/createOrder")
	@ResponseBody
    @ApiOperation(value = "创建交易订单", httpMethod = "POST", response = JsonResult.class, notes = "id；tokenId；transactioncount：数量")
	public JsonResult createOrderTransaction(HttpServletRequest request) {
		JsonResult jsonResult =  new JsonResult();
		Long id = new Long(request.getParameter("id"));
		BigDecimal transactioncount = new BigDecimal(request.getParameter("transactioncount"));

		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);
		if(user!=null){
			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
			}

			if(user.getPhoneState()!=1){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心绑定手机!");
			}

			RemoteResult  remoteResult =	remoteManageService.createOrderTransaction( user.getCustomerId(), id, transactioncount);
			if(remoteResult.getSuccess()) {
				jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
			}else{
				jsonResult.setSuccess(false).setMsg(remoteResult.getMsg());
			}
		}else{
			return new JsonResult().setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		return jsonResult;
	}


    @RequestMapping("/otc/otcorderlistall")
    @ResponseBody
    @ApiOperation(value = "交易订单列表", httpMethod = "POST", response = JsonResult.class, notes = "tokenId;transactionType")
    public FrontPage otcorderlistall(HttpServletRequest request) {
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if( value != null) {
			String tel = JSONObject.parseObject(value).getString("mobile");
			RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
			User user = remoteManageService.selectByTel(tel);
			String type = request.getParameter("transactionType");
			Map<String, String> params = HttpServletRequestUtils.getParams(request);
			if (user != null) {
				if ("0".equals(type)) {
					params.put("customId", user.getCustomerId().toString());
					FrontPage frontPage = remoteManageService.otcorderlistall(params);
					return frontPage;

				} else if ("1".equals(type)) {
					params.put("buyCustomId", user.getCustomerId().toString());
					FrontPage frontPage = remoteManageService.otcorderbuylist(params);
					return frontPage;

				} else if ("2".equals(type)) {
					params.put("sellCustomId", user.getCustomerId().toString());
					FrontPage frontPage = remoteManageService.otcorderselllist(params);
					return frontPage;
				}
			}
		}
        return new FrontPage(null, 0, 1, 10);
    }

    @RequestMapping("/otc/otcorderselllist")
    @ResponseBody
    @ApiOperation(value = "卖交易订单列表", httpMethod = "POST", response = JsonResult.class, notes = "tokenId")
    public FrontPage otcorderselllist(HttpServletRequest request) {
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  new  FrontPage(null, 0, 1, 10);
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        if(user!=null){
            params.put("sellCustomId", user.getCustomerId().toString());
            FrontPage frontPage=	remoteManageService.otcorderselllist(params);
            return frontPage;

        }
        return new FrontPage(null, 0, 1, 10);
    }

    @RequestMapping("/otc/otcorderbuylist")
    @ResponseBody
    @ApiOperation(value = "买交易订单列表", httpMethod = "POST", response = JsonResult.class, notes = "tokenId")
    public FrontPage otcorderbuylist(HttpServletRequest request) {
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  new  FrontPage(null, 0, 1, 10);
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        if(user!=null){
            params.put("buyCustomId", user.getCustomerId().toString());
            FrontPage frontPage =	remoteManageService.otcorderbuylist(params);
            return frontPage;
        }

        return new FrontPage(null, 0, 1, 10);

    }


    @RequestMapping("/otc/otcPayment")
    @ResponseBody
    @ApiOperation(value = "付款完成", httpMethod = "POST", response = JsonResult.class, notes = "tokenId；payment：付款类型(Alipay 支付宝,WeChat 微信，Bank 银行转账)；transactionorderid；img1；img2；img3")
    public JsonResult otcPayment(HttpServletRequest request) {

        JsonResult jsonResult =  new JsonResult();

		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String paymenttype = request.getParameter("payment");
		String transactionorderid = request.getParameter("transactionorderid");
		MultipartFile img1 = multipartRequest.getFile("img1");
		MultipartFile img2 = multipartRequest.getFile("img2");
		MultipartFile img3 = multipartRequest.getFile("img3");
		if(img1.isEmpty() && img2.isEmpty() && img3.isEmpty()){
			return jsonResult.setMsg("凭证必须上传！").setSuccess(false);
		}
        MultipartFile[] files = {img1,img2,img3};
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty() && file.getSize() >0) {
                try {
                    InputStream inputStream = file.getInputStream();
                    String fileType = FileType.getFileType(inputStream);
                    if (fileType != null && (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif") || fileType.equals("bmp"))) {
                    } else {
                        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                    }
                }catch (Exception E){
                    return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                }

            }
        }
		if(user!=null){
			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
			}
			//String[] pathImg = this.upload(files);
			String[] pathImg = FileUpload.POSTFileQiniu(request,files);
			OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
			otcOrderTransactionMange.setImg1(pathImg[0]);
			otcOrderTransactionMange.setImg2(pathImg[1]);
			otcOrderTransactionMange.setImg3(pathImg[2]);
			otcOrderTransactionMange.setPaymentType(paymenttype);
			otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());
			otcOrderTransactionMange.setPaymentTime(new Date());

			RemoteResult  remoteResult =	remoteManageService.otcPayment(otcOrderTransactionMange);
            if(remoteResult.getSuccess()){
				return jsonResult.setMsg("操作成功！").setSuccess(true);
			}else{
				return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
			}

		}else {
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}


    }


	@RequestMapping("/otc/otcApplyArbitration")
	@ResponseBody
    @ApiOperation(value = "申诉", httpMethod = "POST", response = JsonResult.class, notes = "tokenId；appealReason：申诉描述；transactionorderid；img4；img5；img6")
	public JsonResult confirmotcApplyArbitration(HttpServletRequest request) {

		JsonResult jsonResult =  new JsonResult();

		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String appealReason = request.getParameter("appealReason");
		String transactionorderid = request.getParameter("transactionorderid");
		MultipartFile img4 = multipartRequest.getFile("img4");
		MultipartFile img5 = multipartRequest.getFile("img5");
		MultipartFile img6 = multipartRequest.getFile("img6");

		if(appealReason == null || appealReason.isEmpty()){
			return jsonResult.setMsg("申诉原因不能为空！").setSuccess(false);
		}

		if(img4.isEmpty() && img5.isEmpty() && img6.isEmpty()){
			return jsonResult.setMsg("凭证必须上传！").setSuccess(false);
		}

        MultipartFile[] files = {img4,img5,img6};

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty() && file.getSize() >0) {
                try {
                    InputStream inputStream = file.getInputStream();
                    String fileType = FileType.getFileType(inputStream);
                    if (fileType != null && (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif") || fileType.equals("bmp"))) {
                    } else {
                        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                    }
                }catch (Exception E){
                    return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                }

            }
        }
		if(user!=null){
			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
			}
			//String[] pathImg = this.upload(files);
			String[] pathImg = FileUpload.POSTFileQiniu(request,files);
			OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
			otcOrderTransactionMange.setImg4(pathImg[0]);
			otcOrderTransactionMange.setImg5(pathImg[1]);
			otcOrderTransactionMange.setImg6(pathImg[2]);
			otcOrderTransactionMange.setAppealReason(appealReason);
			otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());
			otcOrderTransactionMange.setPaymentTime(new Date());
            otcOrderTransactionMange.setAppealCustomId(user.getCustomerId());
            otcOrderTransactionMange.setAppealCustomName(user.getUsername());

			RemoteResult  remoteResult =	remoteManageService.confirmotcApplyArbitration(otcOrderTransactionMange);
			if(remoteResult.getSuccess()){
				return jsonResult.setMsg("操作成功！").setSuccess(true);
			}else{
				return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
			}

		}else {
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}

	}

	@RequestMapping("/otc/finishOtcOrder")
	@ResponseBody
    @ApiOperation(value = "确认收款", httpMethod = "POST", response = JsonResult.class, notes = "tokenId；transactionorderid")
	public JsonResult finishOtcOrder(HttpServletRequest request) {

		JsonResult jsonResult =  new JsonResult();

		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);

		String transactionorderid = request.getParameter("transactionorderid");

		if(user!=null){
			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
			}
			OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
			otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());

			RemoteResult  remoteResult =	remoteManageService.finishOtcOrder(otcOrderTransactionMange);
			if(remoteResult.getSuccess()){
				return jsonResult.setMsg("操作成功！").setSuccess(true);
			}else{
				return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
			}

		}else {
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}

	}

	@RequestMapping("/otc/otcUndo")
	@ResponseBody
    @ApiOperation(value = "交易订单取消", httpMethod = "POST", response = JsonResult.class, notes = "tokenId；transactionorderid；confirmcancelflag 确认标识")
	public JsonResult otcUndopro(HttpServletRequest request) {

		JsonResult jsonResult =  new JsonResult();

		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);

		String transactionorderid = request.getParameter("transactionorderid");
		String confirmcancelflag = request.getParameter("confirmcancelflag");

		if(!("true").equals(confirmcancelflag) ){
			return jsonResult.setMsg("请勾选确认！").setSuccess(false);
		}

		if(user!=null){
			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
			}
			OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
			otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());
			otcOrderTransactionMange.setCancelBy(user.getCustomerId());
			RemoteResult  remoteResult =	remoteManageService.otcUndo(otcOrderTransactionMange);
			if(remoteResult.getSuccess()){
				return jsonResult.setMsg("操作成功！").setSuccess(true);
			}else{
				return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
			}

		}else {
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}

	}


	@RequestMapping("/otc/getOtclists")
	@ResponseBody
    @ApiOperation(value = "委托单列表", httpMethod = "POST", response = JsonResult.class, notes = "tokenId；transactionorderid；transactionType：1买2卖")
	public FrontPage getOtclists(HttpServletRequest request) {

		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return  new FrontPage(null, 0, 1, 10);
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);

		String type = request.getParameter("transactionType");
		Map<String, String> params = HttpServletRequestUtils.getParams(request);
		if(user!=null){

			}if("0".equals(type)) {
				params.put("customerId", user.getCustomerId().toString());
				params.put("transactionType", null);
				FrontPage frontPage = remoteManageService.getOtclists(params);
				return frontPage;

			}else if("1".equals(type)){
			    params.put("customerId", user.getCustomerId().toString());
			    params.put("transactionType", type);
				FrontPage frontPage =	remoteManageService.getOtclists(params);
				return frontPage;

			}else if("2".equals(type)){
			    params.put("customerId", user.getCustomerId().toString());
			    params.put("transactionType", type);
				FrontPage frontPage =	remoteManageService.getOtclists(params);
				return frontPage;
		}else{

		}
		return new FrontPage(null, 0, 1, 10);

	}
    /*
    * zongwei
    * 委托单撤销
    * */

	@RequestMapping("/otc/OtcListclose")
	@ResponseBody
    @ApiOperation(value = "委托单撤销", httpMethod = "POST", response = JsonResult.class, notes = "tokenId；transactionorderid；")
	public JsonResult OtcListclose(HttpServletRequest request) {

		JsonResult jsonResult =  new JsonResult();
		String tokenId = request.getParameter("tokenId");
		RedisService redisService = SpringContextUtil.getBean("redisService");
		String value = redisService.get("mobile:"+tokenId);
		if(value == null){
			return jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}
		String tel = JSONObject.parseObject(value).getString("mobile");
		RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
		User user = remoteManageService.selectByTel(tel);
		String transactionid = request.getParameter("transactionorderid");

		if(user!=null){
			//判断是否实名
			if(user.getStates()!=2){
				return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
			}
			OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
			otcOrderTransactionMange.setId(Integer.valueOf(transactionid).longValue());

			RemoteResult  remoteResult =	remoteManageService.OtcListclose(Integer.valueOf(transactionid).longValue());
			if(remoteResult.getSuccess()){
				return jsonResult.setMsg("操作成功！").setSuccess(true);
			}else{
				return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
			}

		}else {
			return  jsonResult.setSuccess(false).setCode("600").setMsg("请登录或重新登录！");
		}

	}







}
