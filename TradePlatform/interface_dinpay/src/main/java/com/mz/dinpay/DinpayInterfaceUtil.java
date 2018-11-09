/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午9:39:45
 */
package com.mz.dinpay;

import com.itrus.util.sign.RSAWithSoftware;
import com.mz.util.idgenerate.IdGenerate;
import com.mz.util.idgenerate.NumConstant;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.utils.CommonRequest;
import com.mz.dinpay.utils.HttpClientUtils;
import com.mz.dinpay.utils.WebClient;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * <p>
 * 智付接口
 * </p>
 * 
 * @author: Zhang Xiaofang
 * @Date : 2016年7月5日 上午9:39:45
 */
public class DinpayInterfaceUtil {
	
	
	public static Properties THIRDAPP = null;
	
	public static String merchantID = "";
	public static String virCardNoIn = "";
	public static String identificationNum = "";
	public static String privateKey = "";
	public static String publicKey = "";
	public static String dinPublicKey = "";
	
	
	static {
		
		//商户的信息比如商户号公钥私钥之类的 都在thirdPayConfig.properties里面
		THIRDAPP = new Properties();
		try {
			THIRDAPP.load(new FileReader(PropertiesUtils.class
					.getClassLoader()
					.getResource("/thirdpayConfig/thirdPayConfig.properties")
					.getPath()));
			String typeString="";
			//智付第三方没有测试环境
			if (THIRDAPP.containsKey("environmentType_dinpay")) {
				typeString = THIRDAPP.getProperty("environmentType_dinpay");
			}
			if(typeString.equals("normal")){

				if (THIRDAPP.containsKey("MerchantID_dinpay")) {
					merchantID = THIRDAPP.getProperty("MerchantID_dinpay");
				}
				
				if (THIRDAPP.containsKey("privateKey_dinpay")) {
					privateKey = THIRDAPP.getProperty("privateKey_dinpay");
				}
				if (THIRDAPP.containsKey("publicKey_dinpay")) {
					publicKey = THIRDAPP.getProperty("publicKey_dinpay");
				}
				if (THIRDAPP.containsKey("dinPublicKey_dinpay")) {
					dinPublicKey = THIRDAPP.getProperty("dinPublicKey_dinpay");
				}
			}else{
				if (THIRDAPP.containsKey("test.MerchantID_dinpay")) {
					merchantID = THIRDAPP.getProperty("test.MerchantID_dinpay");
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * <p>
	 * 充值接口
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param response
	 * @param: @param request
	 * @param: @return
	 * @return: String[]
	 * @Date : 2016年7月19日 下午7:33:58
	 * @throws:
	 */
	public static CommonRequest recharge(HttpServletResponse response,
			CommonRequest request) {
		CommonRequest result=new CommonRequest();
		//设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息
		result.setRequestUser(request.getRequestUser());
		result.setRequestUrl(request.getRequestUrl());
		result.setRequestThirdPay("dinpay");
		result.setAmount(request.getAmount());
		
		Dinpay dinpay = new Dinpay();
		
		dinpay.setMerchant_code(merchantID);//商户号
		dinpay.setService_type("direct_pay");//业务类型
		dinpay.setInterface_version("V3.0");
		dinpay.setInput_charset("UTF-8");
		dinpay.setNotify_url(request.getBaseUrl()
				+ "/pay/thirdpayconfig/recharge_dinpay");// 商户后台通知地址
		dinpay.setSign_type("RSA-S");
		
		dinpay.setOrder_no(request.getRequestNo());
		//时间格式为：2016-09-01 12:34:58
		String date=request.getTransactionDateTime();
		String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
		date = date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
		dinpay.setOrder_time(date);
		dinpay.setOrder_amount(request.getAmount());
		dinpay.setProduct_name("we");
		Map m=objectToMap(dinpay);
		String signData = signData(m);

		System.out.println("第一次签名数据=" + signData);
		dinpay.setSign(RSAsign(signData));
		
		Map<String, String> map = objectToMap(dinpay);
	
		try {
			WebClient.operateParameter(response, Dinpay.RECHARGEURL+"UTF-8", map, "UTF-8");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
        //请求发送成功并不代表最终结果，最终结果需等待异步通知
		result.setResponseCode("success");
		result.setResponseMsg("充值申请成功");
		result.setRequestNo(request.getRequestNo());
	
		return result;
	}

	/**
	 * 
	 * <p>
	 * 提现接口(直付到银行卡)
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param request
	 * @param: @return
	 * @return: String[]
	 * @Date : 2016年7月19日 下午7:34:14
	 * @throws:
	 */
	public static CommonRequest withdraw(CommonRequest request) {
		
		return null;
	}
	
	
	
	/**
	 * 公民身份信息验证接口
	 * <p> TODO</p>
	 * @author:         Zhang Xiaofang
	 * @param:    @param request
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年9月18日 下午12:00:03   
	 * @throws:
	 */
    public static CommonRequest checkIdentity(CommonRequest request) {
    	CommonRequest result=new CommonRequest();
		result.setRequestUser(request.getRequestUser());
		result.setRequestThirdPay("dinpay");
	    Dinpay dinpay = new Dinpay();
		dinpay.setService_type(Dinpay.CHECKIDENTITY);
		dinpay.setMerchant_code(merchantID);//商户号
		dinpay.setMerchant_serial_no(request.getRequestNo());//流水号
		dinpay.setId_no(request.getIdCard());//身份证号码
		dinpay.setReal_name(request.getTrueName());//姓名
		dinpay.setInterface_version("V3.1");
		
		dinpay.setSign_type("RSA-S");
		
		Map m=objectToMap(dinpay);
		String signData = signData(m);

		
		String rsAsign = RSAsign(signData);
		dinpay.setSign(rsAsign);
		System.out.println("签名数据=" + rsAsign);
		
		Map<String, String> map = objectToMap(dinpay);
		String rett = HttpClientUtils.post(Dinpay.IDENTITYRL, map,"UTF-8");
		//String rett = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><dinpay><response><sign>jtfNboUfefwrhRvUzo4gVczbDj60oNbP0cQbrrH9dE/mgkD9dMp0AO13IGQXQig6W/S0kW23jSE69T+KjcnKD3YYOiUusZLE1bhzXfaEG8+3J9HMMqvp21Zdflymt06wVhQGuUly54VnK4YK3J8VwNGjXFG1FKukM+xILQhOX2U=</sign><merchant_serial_no>14160922171057333735</merchant_serial_no><information>身份核查通过</information><status>0</status><sign_type>RSA-S</sign_type><real_name>张小芳</real_name><id_picture>null</id_picture><merchant_code>2001210006</merchant_code><serial_no>26073</serial_no><id_no>410581199410189022</id_no></response></dinpay>";
		try {
			rett = new String(rett.getBytes("UTF-8"), "UTF-8");
			LogFactory.info("第一次验证结果："+rett);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Dinpay.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Dinpay din = (Dinpay) unmarshaller.unmarshal(new StringReader(rett));
			if("0".equals(din.getResponse().getStatus())){
				LogFactory.info("一次成功");
				result.setResponseCode("success") ;
				result.setResponseMsg(din.getResponse().getInformation());
			}else if("2".equals(din.getResponse().getStatus())){//如果返回结果为2则继续调用查询接口
				LogFactory.info("返回为2直接成功");
				result.setResponseCode("success") ;
				result.setResponseMsg(din.getResponse().getInformation());
				//重新签名
			    /*Dinpay dinpay2 = new Dinpay();
			    dinpay2.setService_type(Dinpay.IDENTITY_QUERY);
			    dinpay2.setMerchant_code(merchantID);//商户号
			    dinpay2.setMerchant_serial_no(request.getRequestNo());//流水号
			    dinpay2.setInterface_version("V3.1");
			    dinpay2.setSign_type("RSA-S");
				
				Map m2=objectToMap(dinpay2);
				String signData2 = signData(m2);
				String rsAsign2 = RSAsign(signData2);
				System.out.println("第二次签名数据=" + rsAsign2);
				dinpay2.setSign(rsAsign2);
				
				Map<String, String> map2 = objectToMap(dinpay2);
				String queryResult = HttpClientUtils.post(Dinpay.IDENTITYQUERY, map2,"UTF-8");
				
				LogFactory.info("第二次查询结果："+queryResult);
				System.out.println("第二次查询结果："+queryResult);
				JAXBContext newInstance = JAXBContext.newInstance(Dinpay.class);
				Unmarshaller unmarshaller2 = newInstance.createUnmarshaller();
				Dinpay din2 = (Dinpay) unmarshaller2.unmarshal(new StringReader(queryResult));
				if("0".equals(din2.getResponse().getStatus())){
					LogFactory.info("二次成功");
					result.setResponseCode("success") ;
					result.setResponseMsg(din2.getResponse().getInformation());
				}*/
			}else{
				result.setResponseCode("fail") ;
				result.setResponseMsg("校验失败");
			}
		
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result.setResponseCode("fail") ;
			result.setResponseMsg("校验失败");
		}
		return result;
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
	public static Map<String, String> objectToMap(Dinpay gopay) {

		Map<String, String> map = new HashMap<String, String>();
		Class<?> c = gopay.getClass();

		Method[] method = c.getMethods();

		for (Method m : method) {
			try {
				if (m.getName().startsWith("get")
						) {
					
					if(!m.getName().contains("Class")
						){
						Object valueString = m.invoke(gopay, null);
						if (null != valueString && !"".equals(valueString)
								&& !"null".equals(valueString)) {
							map.put(getPropertiesName(m.getName()),
									valueString.toString());
							//System.out.println(getPropertiesName(m.getName())+"="+valueString.toString());
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
		
		   if(!map.isEmpty()){
	        	Object[] obj = map.keySet().toArray();
				Arrays.sort(obj);
				String plain,sign,str,value,valueEncoder= null;
				StringBuffer signString = new StringBuffer();
				for(int i = 0 ; i < obj.length ; i++){
					str = obj[i].toString();
					value = map.get(str).toString();
					if(!str.equals("sign")
						&& !str.equals("sign_type")){
						
						if(value==null||"".equals(value)){
							continue;
						}
						signString.append(str).append("=").append(value).append("&");
					}
					
				}
				//System.out.println("---"+signString);
				sign = signString.substring(0,signString.length()-1).toString();
				return sign;
		   }
	
		return "";

	}



	
	/**
	 * 加密
	 * 加密用的是商户的私钥
	 * <p> TODO</p>
	 * @author:         Zhang Xiaofang
	 * @param:    @param data
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年9月2日 下午4:17:41   
	 * @throws:
	 */
	public static String RSAsign(String data) {
	String dString;
	try {

		dString = RSAWithSoftware.signByPrivateKey(data, privateKey);
	} catch (Exception e) {
		dString="";
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return dString;
		
	}


	/**
	 * 解密
	 * 解密用的是智付的公钥(公钥在商户控台)
	 * <p> TODO</p>
	 * @author:         Zhang Xiaofang
	 * @param:    @param data
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年9月2日 下午4:17:31   
	 * @throws:
	 */
	public static boolean RSADecryption(String data,String reciveData) {
		boolean result=false;
		try {
			result=RSAWithSoftware.validateSignByPublicKey(data, dinPublicKey, reciveData);
			
		} catch (Exception e) {
			result=false;
		
		}
			return result;
			
		}
	

	



	/**
	 * <p> TODO</p>
	 * @author:         Zhang Xiaofang
	 * @param:    @param map
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年7月22日 下午5:15:18   
	 * @throws:
	 */
	public static CommonRequest rechargeCallBack(Map<String, Object> map) {
		CommonRequest commonRequest=new CommonRequest();
		commonRequest.setRequestThirdPay("dinpay");
		String merOrderNum = map.get("order_no").toString();// 订单号
		commonRequest.setRequestNo(merOrderNum);
		commonRequest.setResponseObj(map.toString());
	    String string=	signData(map);
	
		if(map.containsKey("sign")){
		String signString=	map.get("sign").toString();
		
		if (RSADecryption(string, signString)) {
			
		
		String respCode = map.get("trade_status").toString();// 响应码
		
		if (respCode.equals("SUCCESS")) {
			commonRequest.setResponseCode("success");
			commonRequest.setResponseMsg("充值成功");
			commonRequest.setResponseObj("充值成功");
		} else {
			commonRequest.setResponseCode("faile");
			commonRequest.setResponseMsg("提现失败");
		}
	} else {
		commonRequest.setResponseCode("signfaile");
		commonRequest.setResponseMsg("验签失败");
	}
		}
	
	

		return commonRequest;
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
	
public static void main(String[] args) {
	
   	CommonRequest commonRequest=new CommonRequest();
	commonRequest.setRequestUser("13799969968");
	commonRequest.setTrueName("徐赛花");
	commonRequest.setIdCard("350124196409191003");
	commonRequest.setRequestNo(IdGenerate.transactionNum(NumConstant.Third_Interface));
	checkIdentity(commonRequest);
}
}
