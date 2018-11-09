/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zheng lei
 * @version:      V1.0 
 * @Date:        2016年11月28日 下午3:29:32
 */
package com.mz.ipspay;

import com.itrus.util.sign.RSAWithSoftware;
import com.mz.util.properties.PropertiesUtils;
import com.mz.utils.CommonRequest;
import com.mz.ipspay.util.CommonUtil;
import com.mz.ipspay.util.IpsXmlUtil;
import com.mz.ipspay.util.Verify;
import com.mz.ipspay.util.WebClient;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 环迅接口
 * <p>
 * TODO
 * </p>
 * @author: Zhang Lei
 * @Date : 2016年11月28日 下午3:29:32
 */
public class IpspayInterfaceUtil {

	public static Properties THIRDAPP = null;

	public static String merchantID = "";
	public static String virCardNoIn = "";
	public static String identificationNum = "";
	public static String rechargeURL = "";
	public static String directStr = "";
	public static String accountNo = "";

	static {
		// 商户的信息比如商户号公钥私钥之类的 都在thirdPayConfig.properties里面
		THIRDAPP = new Properties();
		try {
			THIRDAPP.load(new FileReader(PropertiesUtils.class.getClassLoader()
					.getResource("/thirdpayConfig/thirdPayConfig.properties")
					.getPath()));
			String typeString = "";
			if (THIRDAPP.containsKey("environmentType_ipspay")) {
				typeString = THIRDAPP.getProperty("environmentType_ipspay");
			}
			if (typeString.equals("normal")) {
				//商户号
				if (THIRDAPP.containsKey("MerchantID_ipspay")) {
					merchantID = THIRDAPP.getProperty("MerchantID_ipspay");
				}
				//交易账户号，环迅商户后台可查询
				if (THIRDAPP.containsKey("MerchantID_accountNo")) {
					accountNo = THIRDAPP.getProperty("MerchantID_accountNo");
				}
				//充值URL
				if (THIRDAPP.containsKey("rechargeURL_ipspay")) {
					rechargeURL = THIRDAPP.getProperty("rechargeURL_ipspay");
				}
				//MD5密钥（暂没用上）
				if (THIRDAPP.containsKey("directStr_ipspay")) {
					directStr = THIRDAPP.getProperty("directStr_ipspay");
				}
			} else {
				if (THIRDAPP.containsKey("test.MerchantID_ipspay")) {
					merchantID = THIRDAPP.getProperty("test.MerchantID_ipspay");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取请求头的信息
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param response
	 * @param:    @param request
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年12月1日 下午3:13:36   
	 * @throws:
	 */
	public static IpspayHead getCommonHead(CommonRequest request) {
		IpspayHead ips=new IpspayHead();
		ips.setVersion("v1.0.0");
		ips.setMerCode(merchantID);//商户号
		ips.setMerName("com.mz");
		ips.setAccount(accountNo);//交易账户号
		ips.setMsgId(request.getRequestNo());//消息标识，交易必输
		ips.setReqDate(request.getTransactionDateTime());//交易时间
		return ips;
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
		CommonRequest result = new CommonRequest();
		// 设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息
		result.setRequestUser(request.getRequestUser());
		result.setRequestUrl(request.getRequestUrl());
		result.setRequestThirdPay("ipspay");
		result.setAmount(request.getAmount());

		
		
		IpspayBody ips = new IpspayBody();//getCommonHead(request);
		ips.setMerBillNo(request.getRequestNo());//商户订单号
		ips.setGatewayType(IpspayBody.GATEWAYTYPE_JJ);//这里默认借记卡
		ips.setDate(CommonUtil.df7.format(new Date()));//订单日期
		ips.setCurrencyType(IpspayBody.CURRENCYTYPE);//人民币
		ips.setAmount(request.getAmount());//金额
		ips.setLang(IpspayBody.LONG);//语言
		ips.setMerchanturl(request.getBaseUrl() + "/pay/thirdpayconfig/recharge_ipspay");//成功回执url
		ips.setFailUrl(request.getBaseUrl() + "/pay/thirdpayconfig/recharge_ipspay");//失败的回执
		ips.setAttach("recharge");
		ips.setOrderEncodeType(IpspayBody.ORDERENCODETYPE);
		ips.setRetEncodeType(IpspayBody.RETENCODETYPE_MD5);
		ips.setRetType(IpspayBody.RETTYPE);
		ips.setServerUrl(request.getBaseUrl() + "/pay/thirdpayconfig/recharge_ipspay");
		ips.setGoodsName("recharge");
		ips.setBillEXP("1000");//订单有效期
		ips.setIsCredit("");//是否直连   直连为：1
		ips.setBankCode("");
		ips.setProductType("");
		
		//签名加密
		String bodyXml=IpsXmlUtil.bodyBeanToXML(ips);

		//System.out.println("环迅MD5加密明文："+bodyXml + merchantID + directStr);
		String sign = DigestUtils.md5Hex(Verify.getBytes(bodyXml + merchantID + directStr,"UTF-8"));
		
		//获取报文头信息
		IpspayHead head=getCommonHead(request);
		head.setSignature(sign);
		String headXml=IpsXmlUtil.headBeanToXML(head);
		try {
			WebClient.operateParameter(response,IpspayHead.RECHARGEURL,headXml,bodyXml,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// 请求发送成功并不代表最终结果，最终结果需等待异步通知
		result.setResponseCode("success");
		result.setResponseMsg("闪付充值申请成功");
		result.setRequestNo(request.getRequestNo());

		return result;
	}

	/**
	 * 充值回调
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param map
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年11月29日 上午10:23:37   
	 * @throws:
	 */
	public static CommonRequest rechargeCallBack(Map<String, Object> map) {
		CommonRequest commonRequest = new CommonRequest();
		
		commonRequest.setRequestThirdPay("ipspay");
		//根据环迅第三方具体的参数返回进行解析
		String resMsg=(String) map.get("paymentResult");
		System.out.println("环迅支付回调："+resMsg);
		if(CommonUtil.isNotNull(resMsg)){
			Map<String, Object> resmap=IpsXmlUtil.xml2map(resMsg,false);
			String bodyXml = IpsXmlUtil.getBodyXml(resMsg);
			String newsign = DigestUtils.md5Hex(Verify.getBytes(bodyXml + merchantID + directStr,"UTF-8"));
			
			commonRequest.setRequestNo(resmap.get("MerBillNo").toString());//商户订单号
			commonRequest.setResponseObj(map.toString());
			if (resmap.containsKey("Signature")) {
				//获取响应报文里的签名
				String signString = resmap.get("Signature").toString();
				if (CommonUtil.isNotNull(signString) && signString.equals(newsign)) {
					System.out.println("环迅支付回调==签名验证通过");
					String respCode = resmap.get("Status").toString();// 响应码
					//Status    Y#交易成功;N#交易失败;P#交易处理中
					if ("Y".equals(respCode)) {
						System.out.println("环迅支付回调==充值成功");
						commonRequest.setResponseCode("success");
						commonRequest.setResponseMsg("充值成功");
						commonRequest.setResponseObj("充值成功");
					} else {
						System.out.println("环迅支付回调==充值失败");
						commonRequest.setResponseCode("faile");
						commonRequest.setResponseMsg("充值失败");
					}
				} else {
					commonRequest.setResponseCode("signfaile");
					commonRequest.setResponseMsg("验签失败");
				}
			}
		}else{
			commonRequest.setResponseCode("faile");
			commonRequest.setResponseMsg("回调信息参数有误！");
		}
		return commonRequest;
	}

	/**
	 * 
	 * <p>
	 * 
	 * 把实体类转换为map
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param gopay
	 * @param: @return
	 * @return: Map<String,String>
	 * @Date : 2016年7月19日 下午7:30:08
	 * @throws:
	 */
	public static Map<String, String> objectToMap(IpspayHead gopay) {

		Map<String, String> map = new HashMap<String, String>();
		Class<?> c = gopay.getClass();

		Method[] method = c.getMethods();

		for (Method m : method) {
			try {
				if (m.getName().startsWith("get")) {

					if (!m.getName().contains("Class")) {
						Object valueString = m.invoke(gopay, null);
						if (null != valueString && !"".equals(valueString)
								&& !"null".equals(valueString)) {
							map.put(getPropertiesName(m.getName()),
									valueString.toString());
							// System.out.println(getPropertiesName(m.getName())+"="+valueString.toString());
						}
					}

				}

			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return map;

	}

	/**
	 * 
	 * <p>
	 * 验签数据
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param gopay
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月7日 下午7:51:31
	 * @throws:
	 */
	public static String signData(Map map) {

		if (!map.isEmpty()) {
			Object[] obj = map.keySet().toArray();
			Arrays.sort(obj);
			String plain, sign, str, value, valueEncoder = null;
			StringBuffer signString = new StringBuffer();
			for (int i = 0; i < obj.length; i++) {
				str = obj[i].toString();
				value = map.get(str).toString();
				if (!str.equals("sign") && !str.equals("sign_type")) {

					if (value == null || "".equals(value)) {
						continue;
					}
					signString.append(str).append("=").append(value)
							.append("&");
				}

			}
			// System.out.println("---"+signString);
			sign = signString.substring(0, signString.length() - 1).toString();
			return sign;
		}

		return "";

	}

	/**
	 * 加密 加密用的是商户的私钥
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param data
	 * @param: @return
	 * @return: String
	 * @Date : 2016年9月2日 下午4:17:41
	 * @throws:
	 */
	public static String RSAsign(String data) {
		String dString;
		try {

			dString = RSAWithSoftware.signByPrivateKey(data, "");//privateKey
		} catch (Exception e) {
			dString = "";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dString;

	}

	/**
	 * 解析验证签名
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param data
	 * @param:    @param reciveData
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2016年12月1日 下午2:21:16   
	 * @throws:
	 */
	public static boolean RSADecryption(String data, String reciveData) {
		boolean result = false;
		
		return result;

	}

	

	/**
	 * 
	 * <p>
	 * 根据方法 名字获取属性值 比如方法名为:getVersion 那么返回的值为：version
	 * 
	 * 
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param name
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月5日 下午2:47:43
	 * @throws:
	 */
	public static String getPropertiesName(String name) {
		String nString = name.substring(3, name.length());
		String firString = String.valueOf(nString.toCharArray()[0])
				.toLowerCase();
		String str = nString.substring(1, nString.length());
		return firString + str;
	}
	
	
	
	

	
	
	
}
