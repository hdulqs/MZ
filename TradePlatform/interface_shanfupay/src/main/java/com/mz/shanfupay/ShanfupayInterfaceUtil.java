package com.mz.shanfupay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.shanfupay.util.BeanMapUtils;
import com.mz.shanfupay.util.RsaUtil;
import com.mz.shanfupay.util.WebClient;
import com.mz.util.properties.PropertiesUtils;
import com.mz.utils.CommonRequest;
import com.mz.shanfupay.util.SignUtils;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import sun.misc.BASE64Decoder;

/**
 * 闪付接口
 * <p>
 * TODO
 * </p>
 * @author: Zhang Lei
 * @Date : 2016年11月28日 下午3:29:32
 */
@SuppressWarnings("all")
public class ShanfupayInterfaceUtil {

	public static Properties THIRDAPP = null;

	public static String merchantID = "";
	public static String privateKey = "";

	static {
		// 商户的信息比如商户号公钥私钥之类的 都在thirdPayConfig.properties里面
		THIRDAPP = new Properties();
		try {
			THIRDAPP.load(new FileReader(PropertiesUtils.class.getClassLoader()
					.getResource("/thirdpayConfig/thirdPayConfig.properties")
					.getPath()));
			String typeString = "";
			if (THIRDAPP.containsKey("environmentType_shanfupay")) {
				typeString = THIRDAPP.getProperty("environmentType_shanfupay");
			}
			if (typeString.equals("normal")) {
				//商户号
				if (THIRDAPP.containsKey("MerchantID_shanfupay")) {
					merchantID = THIRDAPP.getProperty("MerchantID_shanfupay");
				}
				if (THIRDAPP.containsKey("privateKey_shanfupay")) {
					privateKey = THIRDAPP.getProperty("privateKey_shanfupay");
				}
			} else {
				if (THIRDAPP.containsKey("test.MerchantID_shanfupay")) {
					merchantID = THIRDAPP.getProperty("test.MerchantID_shanfupay");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 充值方法
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param response
	 * @param:    @param request
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年11月29日 上午10:25:12   
	 * @throws:
	 */
	public static CommonRequest recharge(HttpServletResponse response,CommonRequest request) {
		// 请求发送成功并不代表最终结果，最终结果需等待异步通知
		CommonRequest result = new CommonRequest();
		
		Map<String, String> map = new HashMap<String, String>();
		// 设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息
		result.setRequestUser(request.getRequestUser());
		result.setRequestUrl(request.getRequestUrl());
		result.setRequestThirdPay("shanfupay");
		result.setAmount(request.getAmount());

		//初始化请求对象信息
		Shanfupay shanfu=new Shanfupay();
		shanfu.setP0_Cmd(Shanfupay.CMD);//业务类型
		shanfu.setP1_MerId(merchantID);//商户编号
		shanfu.setP2_Order(request.getRequestNo());//商户订单号
		shanfu.setP3_Amt(request.getAmount());//支付金额 单位：元  精确到分
		shanfu.setP4_Cur(Shanfupay.CUR);//币种
		shanfu.setP5_Pid("recharge");//商品名称
		shanfu.setP8_Url(request.getBaseUrl() + "/pay/thirdpayconfig/recharge_shanfupay");//回调URL
		
		//shanfu.setPd_FrpId("ABC");//银行
		//生成签名信息
		String sign=SignUtils.getReqMd5Hmac(shanfu, privateKey);
		shanfu.setHmac(sign);
		//将对象转成请求参数map
		map= BeanMapUtils.beanToMap(shanfu);
		try {
			WebClient.operateParameter(response, Shanfupay.RECHARGEURL,map, "UTF-8");
		} catch (IOException e) {
			System.out.println("请求第三方异常！！！！！！");
			e.printStackTrace();
		} 
		
		result.setResponseCode("success");
		result.setResponseMsg("闪付充值申请成功");
		result.setRequestNo(request.getRequestNo());
		return result;
	}

	/**
	 * 充值回调
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Lei
	 * @param: @param map
	 * @param: @return
	 * @return: CommonRequest
	 * @Date : 2016年11月29日 上午10:23:37
	 * @throws:
	 */
	public static CommonRequest rechargeCallBack(Map<String, Object> map) {
		CommonRequest commonRequest = new CommonRequest();
		commonRequest.setRequestThirdPay("shanfupay");
		String merOrderNum = map.get("r6_Order").toString();// 订单号
		commonRequest.setRequestNo(merOrderNum);
		commonRequest.setResponseObj(map.toString());
		

		if (map.containsKey("hmac")) {
			String signString = map.get("hmac").toString();
			//验证签名
			String sNewString = SignUtils.getResMd5Hmac(map, privateKey);

			if (signString!=null && !"".equals(signString) && sNewString.equals(sNewString)) {
				String respCode = map.get("r1_Code").toString();// 响应码

				if (ShanfuResponse.CODE_SUCCESS.equals(respCode)) {
					System.out.println("订单号为:"+merOrderNum+" 充值成功");
					commonRequest.setResponseCode("success");
					commonRequest.setResponseMsg("充值成功");
					commonRequest.setResponseObj(map.toString());
				} else {
					System.out.println("订单号为:"+merOrderNum+" 充值失败");
					commonRequest.setResponseCode("faile");
					commonRequest.setResponseMsg("充值失败");
				}
			} else {
				System.out.println("签名验证失败");
				commonRequest.setResponseCode("signfaile");
				commonRequest.setResponseMsg("验签失败");
			}
		}
		return commonRequest;
	}

	
	/**
	 * 提现接口
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param request
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年12月7日 下午8:48:24   
	 * @throws:
	 */
	public static CommonRequest withdraw(CommonRequest request) {
		CommonRequest resRequest = new CommonRequest();
		Map map = new HashMap();
		map.put("merchantId", merchantID);// 
		map.put("orderId", request.getRequestNo());  //商户提现号 不允许重复
		map.put("bankNum", request.getBankAccNum()); //银行卡号
		map.put("openName", request.getRequestUser()); //开户姓名
		map.put("openBank", request.getBankName()); //开户银行
		map.put("openBranch",request.getBankBranchName()); //开户分行
		map.put("openSubranch", request.getBankBranchName()); //开户支行
		map.put("amount", request.getAmount()); //提现金额
		map.put("province", request.getBankProvince()); //省名称
		map.put("city", request.getBankCity()); //城市名称
		map.put("idcard", request.getIdCard()); //身份证号
		map.put("remark", "测试"); //备注信息
		//map.put("bankCode", "000000000000"); //行号
		
		Map newmap = RsaUtil.ParaFilter(map);
		String mySign = RsaUtil.BuildMysign(newmap, privateKey);
		System.out.println("map:"+map);	
		System.out.println("nmap:"+newmap);	
		JSONObject obj = JSONObject.parseObject(JSON.toJSONString(map));
		
		String paydata = null;
		try {
			paydata = RsaUtil.encrypt(obj.toString(),"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 发送信息
		HttpClient client = new HttpClient();
		HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
		
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(1110000);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(3011000);
		
		PostMethod postMethod = null;
		postMethod = new PostMethod(Shanfupay.WITHDRAWURL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		//设置参数
		NameValuePair[] nvp = {		 
				new NameValuePair("paydata",paydata),
				new NameValuePair("sign", mySign)
		};
		postMethod.setRequestBody(nvp);
		postMethod.setRequestHeader("Connection", "close");
		String returnStr="";
		try {
			client.executeMethod(postMethod);
			returnStr = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("return:"+returnStr);
		String result = returnStr.substring(1, returnStr.length()-1);
		
		JSONObject object = JSONObject.parseObject(result.replaceAll("\\\\", ""));//去除字符串中的反斜杠
		
		String resCode = object.getString("resCode");
		String message = object.getString("message");
		
		if("00".equals(resCode)){
			String wid = object.getString("withdrawalId");
			resRequest.setRequestNo(wid);
			System.out.println("提现流水号:"+wid);
			resRequest.setResponseCode("success");
			resRequest.setResponseMsg("闪付提现申请成功");
		}else{
			System.out.println("错误信息:"+message);
			resRequest.setResponseCode("fail");
			resRequest.setResponseMsg("闪付提现申请失败,失败原因:"+message);
		}
		
		
		return resRequest;
	}
	
	/**
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param request
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年12月7日 下午8:48:24   
	 * @throws:
	 */
	public static CommonRequest queryOrder(CommonRequest request) {
		CommonRequest resRequest = new CommonRequest();
		// 发送信息
		HttpClient client = new HttpClient();
		HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(1110000);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(3011000);
		
		PostMethod postMethod = null;
		postMethod = new PostMethod(Shanfupay.QUERYORDERURL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		//设置参数81810959
		Map map = new HashMap();
		map.put("merchantId", merchantID);// 
		map.put("orderId", request.getRequestNo());  //商户提现号 不允许重复
		String sign=RsaUtil.BuildMysign(map, privateKey);
		
		NameValuePair[] nvp = {	
				new NameValuePair("merchantId",merchantID),
				new NameValuePair("orderId",request.getRequestNo()),
				new NameValuePair("sign", sign)
		};
		postMethod.setRequestBody(nvp);
		postMethod.setRequestHeader("Connection", "close");
		String returnStr="";
		try {
			client.executeMethod(postMethod);
			returnStr = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("return:"+returnStr);
		String result = returnStr.substring(1, returnStr.length()-1);
		JSONObject object = null;
		if(result.contains("message")){
			result = result.replaceAll("\\\\", "");//包含message 时  需要去除反斜杠
			object=JSONObject.parseObject(result); 
		}else{
			object = JSONObject.parseObject(result); 
		}
		
		
		String resCode = object.getString("resCode");
		String message = object.getString("message");
		
		if("00".equals(resCode)){
			String detail = object.getString("detail");
			System.out.println(detail);
			//进行解密
			String paydata;
			try {
				BASE64Decoder decoder = new BASE64Decoder();
	  			byte[] re = decoder.decodeBuffer(detail);//new String(detail.getBytes(),"utf-8")
				paydata = RsaUtil.decrypt(re,"");
				JSONObject res = JSONObject.parseObject(paydata);
				System.out.println("流水号为["+request.getRequestNo()+"]提现流水信息:"+res.toString());
				String status=res.getString("status");
				//订单状态 0 待处理  2 银行处理中 3 提现成功  1和 4 提现失败
				if("3".equals(status)){//提现成功
					resRequest.setResponseCode("success");
					resRequest.setResponseMsg("闪付提现申请成功");
				}else if("1".equals(status) || "4".equals(status)){
					resRequest.setResponseCode("fail");
					resRequest.setResponseMsg("闪付处理失败，原因:"+res.getString("reason"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			System.out.println("错误信息:"+message);
			resRequest.setResponseMsg("闪付查询代付结果失败");
		}
		
		resRequest.setRequestNo(request.getRequestNo());
		return resRequest;
	}
	
	
	
	
	
	public static void main(String[] args) {
		/*CommonRequest request=new CommonRequest();
		request.setRequestNo("123123123123");
		request.setBankAccNum("1234567890");
		request.setBankAcctName("张三");
		request.setBankName("中国建设银行");
		request.setBankBranchName("海淀分行");
		request.setBankBranchName("海淀分行");
		request.setAmount("1.00");
		request.setBankProvince("北京");
		request.setBankCity("北京");
		request.setIdCard("1235345646757");
		withdraw(request);*/
		
		
		CommonRequest request=new CommonRequest();
		request.setRequestNo("14813504623993030");
		queryOrder(request);
	}
	
}
