/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午9:39:45
 */
package com.mz.gopay;

import com.mz.util.properties.PropertiesUtils;
import com.mz.utils.CommonRequest;
import com.mz.gopay.utils.HttpClientUtils;
import com.mz.gopay.utils.UrlUtils;
import com.mz.gopay.utils.WebClient;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * <p>
 * 国付宝接口
 * </p>
 * 
 * @author: Zhang Xiaofang
 * @Date : 2016年7月5日 上午9:39:45
 */
public class GopayInterfaceUtil {
	
	
	public static Properties THIRDAPP = null;
	public static String merchantID = "";
	public static String virCardNoIn = "";
	public static String identificationNum = "";
	public static String URL = "";
	static {
		THIRDAPP = new Properties();
		try {
			THIRDAPP.load(new FileReader(PropertiesUtils.class
					.getClassLoader()
					.getResource("/thirdpayConfig/thirdPayConfig.properties")
					.getPath()));
			System.out.println("======================"+THIRDAPP);
			if (THIRDAPP.containsKey("platFormId_gopay")) {
				merchantID = THIRDAPP.getProperty("platFormId_gopay");
			}
			if (THIRDAPP.containsKey("platFormNo_gopay")) {
				virCardNoIn = THIRDAPP.getProperty("platFormNo_gopay");
			}
			if (THIRDAPP.containsKey("identificationNum_gopay")) {
				identificationNum = THIRDAPP.getProperty("identificationNum_gopay");
			}
			if (THIRDAPP.containsKey("thirdpayURL_gopay")) {
				URL = THIRDAPP.getProperty("thirdpayURL_gopay");
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
		result.setRequestUser(request.getRequestUser());
		result.setRequestUrl(URL);
		result.setRequestThirdPay("gopay");
		result.setAmount(request.getAmount());
		Gopay gopay = new Gopay();
		gopay.setVersion(Gopay.VERSION);
		gopay.setCharset(Gopay.CHARSET_UTF8);
		gopay.setLanguage(Gopay.Language_CH);
		gopay.setSignType(Gopay.MD5);
		gopay.setTranCode(Gopay.GATEWAY_CODE);
		gopay.setMerchantID(merchantID);

		gopay.setMerOrderNum(request.getRequestNo());
		gopay.setTranAmt(request.getAmount());
		if (null != request.getFee()) {
			gopay.setFeeAmt(request.getFee());
		}
		gopay.setVerficationCode(identificationNum);
		gopay.setCurrencyType(Gopay.COIN_RMB);
		gopay.setFrontMerUrl("");
		gopay.setBackgroundMerUrl(request.getBaseUrl()
				+ "pay/thirdpayconfig/recharge_gopay");// 商户后台通知地址
		gopay.setTranDateTime(request.getTransactionDateTime());
		gopay.setVirCardNoIn(virCardNoIn);
		gopay.setTranIP(request.getUserBrowerIP());
		gopay.setGopayOutOrderId("");
		gopay.setOrderId("");
		gopay.setRespCode("");
		String dateString = GopayUtils.getGopayServerTime();
		gopay.setGopayServerTime(dateString);// 防钓鱼字段 国付宝服务器的时间
		String signData = rechargeSignData(gopay);

		System.out.println("签名数据=" + signData);
		gopay.setSignValue(GopayUtils.md5(signData));
		Map<String, String> map = objectToMap(gopay);
		String urlString = "";
		try {
			urlString = UrlUtils.generateUrl(map, URL, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		WebClient.SendByUrl(response, urlString, "GBK");
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
		CommonRequest req=new CommonRequest();
		try {
		req.setRequestThirdPay("gopay");
		req.setRequestUrl(URL);
		req.setRequestUser(request.getRequestUser());
		GopayWithdraw gopay = new GopayWithdraw();
		gopay.setVersion("1.1");
		gopay.setTranCode(Gopay.PAY_CODE);
		gopay.setCustomerId(merchantID);
		gopay.setPayAcctId(virCardNoIn);
		gopay.setMerOrderNum(request.getRequestNo());
		gopay.setVerficationCode(identificationNum);
		// gopay.setMerURL("http://zhangxf.ngrok.cc/manage/pay/thirdpayconfig/withdraw");
		gopay.setMerURL(request.getBaseUrl()
				+ "/pay/thirdpayconfig/withdraw");
		gopay.setTranAmt(request.getAmount());
		gopay.setRecvBankAcctName(request.getBankAcctName());// 收款人银行开户名
		gopay.setRecvBankProvince(request.getBankProvince());//
		gopay.setRecvBankCity(request.getBankCity());
		gopay.setRecvBankName(request.getBankName());
		gopay.setRecvBankBranchName(request.getBankBranchName());
		gopay.setRecvBankAcctNum(request.getBankAccNum());
		gopay.setCorpPersonFlag("2");
		gopay.setTranDateTime(request.getTransactionDateTime());
		gopay.setMerchantEncode(Gopay.CHARSET_GBK);
		gopay.setApprove("1");

		String signData = withdrawSignData(gopay);
		//System.out.println("签名数据=" + signData);
		gopay.setSignValue(signData);
		Map<String, String> map = objectToMap(gopay);
		String rett = HttpClientUtils.post(URL, map,"GBK");

		req.setResponseMsg("提现申请成功");
        req.setRequestNo(request.getRequestNo());
		String rrString = "";
	
			rrString = new String(rett.getBytes("gbk"), "UTF-8");
	
		System.out.println("rrString==="+rrString);
		req.setResponseObj(rrString);
		if (rett.contains("GopayAPIResp")) {
			String successString = "";
			try {
				successString = new String(rett.getBytes("gbk"), "gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			req.setResponseObj(successString);
			System.out.println("successString=" + successString);
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(GopayWithdraw.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				GopayWithdraw go = (GopayWithdraw) unmarshaller
						.unmarshal(new StringReader(rett));
				req.setResponseCode("success");
				
				
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}

		else if (rett.contains("GopayTranRes")) {
			String rString = "";
			try {
				rString = new String(rett.getBytes("gbk"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println("---"+rString);
			req.setResponseObj(rString);
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(Gopay.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				Gopay go = (Gopay) unmarshaller.unmarshal(new StringReader(
						rString));
				req.setResponseCode("fail") ;
				req.setResponseMsg(go.getErrMessage());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
			
			req.setResponseCode("fail") ;
			req.setResponseMsg("提现申请失败");
		}
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			req.setResponseCode("fail") ;
			req.setResponseMsg("提现申请失败");
		}
		return req;
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
	public static Map<String, String> objectToMap(Gopay gopay) {

		Map<String, String> map = new HashMap<String, String>();
		Class<?> c = gopay.getClass();

		Method[] method = c.getMethods();

		for (Method m : method) {
			try {
				if (m.getName().startsWith("get")
						&& !m.getName().contains("getClass")
						&& !m.getName().contains("getVerficationCode")) {
					Object valueString = m.invoke(gopay, null);
					if (null != valueString && !"".equals(valueString)
							&& !"null".equals(valueString)) {
						map.put(getPropertiesName(m.getName()),
								valueString.toString());
						System.out.println(getPropertiesName(m.getName()) + "="
								+ valueString);
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
	 * 把实体类转换为map
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param gopay
	 * @param: @return
	 * @return: Map<String,String>
	 * @Date : 2016年7月19日 下午7:29:36
	 * @throws:
	 */
	public static Map<String, String> objectToMap(GopayWithdraw gopay) {

		Map<String, String> map = new HashMap<String, String>();
		Class<?> c = gopay.getClass();

		Method[] method = c.getMethods();

		for (Method m : method) {
			try {
				if (m.getName().startsWith("get")
						&& !m.getName().contains("getClass")
						&& !m.getName().contains("getVerficationCode")) {
					Object valueString = m.invoke(gopay, null);
					if (null != valueString && !"".equals(valueString)
							&& !"null".equals(valueString)) {
						map.put(getPropertiesName(m.getName()),
								valueString.toString());
						System.out.println(getPropertiesName(m.getName()) + "="
								+ valueString);
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
	 * 充值验签数据 参数格式需要按照国付宝文档上的格式
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param gopay
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月7日 下午7:51:31
	 * @throws:
	 */
	public static String rechargeSignData(Gopay gopay) {

		String time = null == gopay.getGopayServerTime() ? "" : gopay
				.getGopayServerTime();
		String feeAmt = null == gopay.getFeeAmt() ? "" : gopay.getFeeAmt();
		String gopayOutOrderId = null == gopay.getGopayOutOrderId() ? ""
				: gopay.getGopayOutOrderId();
		String respCode = null == gopay.getRespCode() ? "" : gopay
				.getRespCode();
		String verficationCode = null == gopay.getVerficationCode() ? ""
				: gopay.getVerficationCode();
		String signData = "version=[" + gopay.getVersion() + "]tranCode=["
				+ gopay.getTranCode() + "]merchantID=[" + gopay.getMerchantID()
				+ "]merOrderNum=[" + gopay.getMerOrderNum() + "]" + "tranAmt=["
				+ gopay.getTranAmt() + "]feeAmt=[" + feeAmt + "]tranDateTime=["
				+ gopay.getTranDateTime() + "]frontMerUrl=[]"
				+ "backgroundMerUrl=[" + gopay.getBackgroundMerUrl()
				+ "]orderId=[" + gopay.getOrderId() + "]gopayOutOrderId=["
				+ gopayOutOrderId + "]tranIP=[" + gopay.getTranIP() + "]"
				+ "respCode=[" + respCode + "]gopayServerTime=[" + time
				+ "]VerficationCode=[" + verficationCode + "]";

		return signData;

	}
	
	
	

	/**
	 * 
	 * <p>
	 * 取现验签数据 参数格式需要按照国付宝文档上的格式
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param gopay
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月19日 下午7:27:03
	 * @throws:
	 */
	public static String withdrawSignData(GopayWithdraw gopay) {

		String feeAmt = null == gopay.getFeeAmt() ? "" : gopay.getFeeAmt();
		String orderId = null == gopay.getOrderId() ? "" : gopay.getOrderId();
		String respCode = null == gopay.getRespCode() ? "" : gopay.getRespCode();
		String verficationCode = null == gopay.getVerficationCode() ? "": gopay.getVerficationCode();
		String payAcctId = null == gopay.getPayAcctId() ? "" : gopay.getPayAcctId();
		String approve = null == gopay.getApprove() ? "" : gopay.getApprove();
		String totalAmount = null == gopay.getTotalAmount() ? "" : gopay.getTotalAmount();
		String signData = "version=[" + gopay.getVersion() + "]tranCode=["
				+ gopay.getTranCode() + "]customerId=[" + gopay.getCustomerId()
				+ "]merOrderNum=[" + gopay.getMerOrderNum() + "]" + "tranAmt=["
				+ gopay.getTranAmt() + "]feeAmt=[" + feeAmt
				+ "]totalAmount=["+totalAmount+"]merURL=[" + gopay.getMerURL()
				+ "]recvBankAcctNum=[" + gopay.getRecvBankAcctNum()
				+ "]tranDateTime=[" + gopay.getTranDateTime() + "]"
				+ "orderId=[" + orderId + "]respCode=[" + respCode
				+ "]payAcctId=[" +payAcctId+ "]approve=["
				+ approve+ "]" + "VerficationCode=["
				+ verficationCode + "]";
           System.out.println("----"+signData);
		return GopayUtils.md5(signData);

	}

	

	/**
	 * 
	 * <p>
	 * 取现回调验签数据 参数格式需要按照国付宝文档上的格式
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param gopay
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月19日 下午7:27:03
	 * @throws:
	 */
	public static String withdrawBackSignData(GopayWithdraw gopay) {

		String feeAmt = null == gopay.getFeeAmt() ? "" : gopay.getFeeAmt();
		String orderId = null == gopay.getOrderId() ? "" : gopay.getOrderId();
		String respCode = null == gopay.getRespCode() ? "" : gopay.getRespCode();
		String verficationCode = null == gopay.getVerficationCode() ? "": gopay.getVerficationCode();
		String totalAmount = null == gopay.getTotalAmount() ? "" : gopay.getTotalAmount();
		String signData = "version=[" + gopay.getVersion() + "]tranCode=["
				+ gopay.getTranCode() + "]customerId=[" + gopay.getCustomerId()
				+ "]merOrderNum=[" + gopay.getMerOrderNum() + "]" + "tranAmt=["
				+ gopay.getTranAmt() + "]feeAmt=[" + feeAmt
				+ "]totalAmount=["+totalAmount+"]merURL=[" + gopay.getMerURL()
				+ "]recvBankAcctNum=[" + gopay.getRecvBankAcctNum()
				+ "]tranDateTime=[" + gopay.getTranDateTime() + "]"
				+ "orderId=[" + orderId + "]respCode=[" + respCode
				+  "]" + "VerficationCode=["
				+ verficationCode + "]";
           System.out.println("----"+signData);
		return GopayUtils.md5(signData);

	}
	/**
	 * 
	 * <p>
	 * 查询订单验签数据 参数格式需要按照国付宝文档上的格式
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param gopay
	 * @param: @return
	 * @return: String
	 * @Date : 2016年7月19日 下午7:28:40
	 * @throws:
	 */
	public static String queryOrderSignData(Gopay gopay) {

		String signData = "tranCode=["
				+ gopay.getTranCode()
				+ "]"
				+ "merchantID=["
				+ gopay.getMerchantID()
				+ "]"
				+ "merOrderNum=["
				+ gopay.getMerOrderNum()
				+ "]"
				+ "tranAmt=[]ticketAmt=[]tranDateTime=["
				+ gopay.getTranDateTime()
				+ "]currencyType=[]merURL=[]"
				+ "customerEMail=[]authID=[]orgOrderNum=["
				+ gopay.getOrgOrderNum()
				+ "]"
				+ "orgtranDateTime=["
				+ gopay.getOrgtranDateTime()
				+ "]orgtranAmt=[]orgTxnType=[]orgTxnStat=[]msgExt=[]virCardNo=[]virCardNoIn=[]"
				+ "tranIP=[" + gopay.getTranIP() + "]isLocked=[]feeAmt=[]"
				+ "respCode=[]gopayOrderId=[]" + "VerficationCode=["
				+ gopay.getVerficationCode() + "]";

		return signData;

	}

	public boolean validateSign() {

		return true;
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

	/**
	 * <p>
	 * 订单查询接口
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param response
	 * @param: @param request
	 * @return: void
	 * @Date : 2016年7月8日 下午12:01:55
	 * @throws:
	 */
	public static String[] queryOrder(CommonRequest request) {
		String[] str = new String[2];

		Properties p = new Properties();
		String merchantID = "";
		String virCardNoIn = "";
		String identificationNum = "";
		InputStream in = GopayInterfaceUtil.class
				.getResourceAsStream("/thirdpayConfig/gopayConfig.properties");
		try {
			p.load(in);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (p.containsKey("platFormId")) {
			merchantID = p.getProperty("platFormId");
		}
		if (p.containsKey("platFormNo")) {
			virCardNoIn = p.getProperty("platFormNo");
		}
		if (p.containsKey("identificationNum")) {
			identificationNum = p.getProperty("identificationNum");
		}

		Gopay gopay = new Gopay();
		gopay.setVersion("1.1");
		gopay.setTranCode("4020");
		gopay.setMerchantID(merchantID);
		gopay.setMerOrderNum(request.getRequestNo());
		gopay.setTranDateTime(request.getTransactionDateTime());
		gopay.setTranIP(request.getUserBrowerIP());
		gopay.setOrgOrderNum(request.getQueryOrderNo());
		gopay.setOrgtranDateTime(request.getQueryOrderDate());
		gopay.setGopayOutOrderId("");
		gopay.setOrderId("");
		gopay.setRespCode("");
		String dateString = GopayUtils.getGopayServerTime();
		gopay.setGopayServerTime(dateString);// 防钓鱼字段 国付宝服务器的时间
		String signData = queryOrderSignData(gopay);
		gopay.setSignValue(GopayUtils.md5(signData));
		Map<String, String> map = objectToMap(gopay);
		String ret = HttpClientUtils.httpPost(Gopay.GOPAY_GATEWAY, map);
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(gopay.getClass());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Gopay go = (Gopay) unmarshaller.unmarshal(new StringReader(ret));
			str[0] = go.getRespCode();
		} catch (JAXBException e) {
			
			//e.printStackTrace();
		}

		return str;
	}

	/**
	 * 
	 * <p>
	 * 充值回调验签方法
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param map
	 * @param: @return
	 * @return: boolean
	 * @Date : 2016年7月22日 下午12:54:57
	 * @throws:
	 */
	public static boolean rechargeSignValidate(Map<String, Object> map) {

		Gopay gopay = new Gopay();
		Class<?> c = gopay.getClass();

		Method[] method = c.getMethods();

		for (Method m : method) {
			try {
				if (m.getName().startsWith("set")
						&& !m.getName().contains("setClass")
						&& !m.getName().contains("setVerficationCode")) {
					Object value = map.get(getPropertiesName(m.getName()));
					String  val="";
					if (null != value ) {
						val=value.toString();
						Class[] parameterC = m.getParameterTypes();
						if (parameterC[0] == int.class) {
							m.invoke(gopay, Integer.valueOf(val).intValue());

						}else {
							m.invoke(gopay, parameterC[0].cast(val));

						}
                   }

				}

			} catch (IllegalAccessException e) {
				
				//e.printStackTrace();
			} catch (IllegalArgumentException e) {
				
				//e.printStackTrace();
			} catch (InvocationTargetException e) {
				
				//e.printStackTrace();
			}

		}

		gopay.setVerficationCode(identificationNum);
		String sign = rechargeSignData(gopay);
		String s = GopayUtils.md5(sign);
		System.out.println(gopay.getSignValue());
		System.out.println("sign==" + sign);
		System.out.println("sign==" + GopayUtils.md5(sign));
		if (s.equals(gopay.getSignValue())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * <p>
	 * 提现回调验签
	 * </p>
	 * 
	 * @author: Zhang Xiaofang
	 * @param: @param map
	 * @param: @return
	 * @return: boolean
	 * @Date : 2016年7月22日 下午12:57:11
	 * @throws:
	 */
	public static boolean withdrawSignValidate(GopayWithdraw gopay) {
	
		gopay.setVerficationCode(identificationNum);
		
	   String sign=	withdrawBackSignData(gopay);
		String backValue=gopay.getSignValue();
		
		
		
		System.out.println("sign==" + sign);
		System.out.println("backValue==" + backValue);
		if (sign.equals(backValue)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p> TODO</p>
	 * @author:         Zhang Xiaofang
	 * @param:    @param map
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2016年7月22日 下午5:15:09   
	 * @throws:
	 */
	public static CommonRequest withdrawCallBack(Map<String, Object> map) {
		CommonRequest commonRequest=new CommonRequest();
		commonRequest.setRequestThirdPay("gopay");
		if(map.containsKey("notifyMsg")){
			
			if(null!=map.get("notifyMsg")){
				Object obj =map.get("notifyMsg");
				String msg=obj.toString();
				commonRequest.setResponseObj(msg);
				String orderId = "";
				boolean signValue=false;
				String responseCode="";
				
				if (msg.contains("GopayAPIResp")) {
					JAXBContext context;
					try {
						context = JAXBContext.newInstance(GopayWithdraw.class);
						Unmarshaller unmarshaller = context.createUnmarshaller();
						GopayWithdraw go = (GopayWithdraw) unmarshaller
								.unmarshal(new StringReader(msg));
						orderId = go.getMerOrderNum();
						responseCode=go.getRespCode();
						signValue=GopayInterfaceUtil.withdrawSignValidate(go);
						System.out.println("signvalue==="+go.getSignValue());
					} catch (JAXBException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						commonRequest.setResponseCode("exception");
						commonRequest.setResponseMsg("回调业务处理失败");
					}
				}
				commonRequest.setRequestNo(orderId);
				
				if (signValue) {
					
					if(responseCode.equals("8")){
						commonRequest.setResponseCode("success");
						commonRequest.setResponseMsg("提现成功");
						commonRequest.setResponseObj("提现成功");
					}else{
						
						commonRequest.setResponseCode("faile");
						commonRequest.setResponseMsg("提现失败");
					}
					
				}else{
					commonRequest.setResponseCode("signfaile");
					commonRequest.setResponseMsg("验签失败");
				}
			}
		}else{
			commonRequest.setResponseCode("incorrect");
			commonRequest.setResponseMsg("第三方回调参数不正确");
		}
		
		
		
		return commonRequest;
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
		commonRequest.setRequestThirdPay("gopay");
		String merOrderNum = map.get("merOrderNum").toString();// 订单号
		commonRequest.setRequestNo(merOrderNum);
		commonRequest.setResponseObj(map.toString());
			if (GopayInterfaceUtil.rechargeSignValidate(map)) {
		
		// String tranFinishTime = map.get("tranFinishTime");// 交易完成时间 
		String respCode = map.get("respCode").toString();// 响应码
		
		if (respCode.equals("0000")) {
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

		return commonRequest;
	}
	

	
	//====================================================================================
	
	
	/**
	 * 微信扫码
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param response
	 * @param:    @param request
	 * @param:    @return
	 * @return: CommonRequest 
	 * @Date :          2017年3月24日 上午10:03:02   
	 * @throws:
	 */
	public static CommonRequest recharge_weixin(HttpServletResponse response,CommonRequest request) {
		CommonRequest result=new CommonRequest();
		result.setRequestUser(request.getRequestUser());
		result.setRequestUrl(URL);
		result.setRequestThirdPay("gopay_weixin");
		result.setAmount(request.getAmount());
		
		//初始化请求对象信息
		SortedMap<String, String> sortedMap = new TreeMap<String, String>();
		sortedMap.put("version", "2.1");
		sortedMap.put("charset", "2");//字符集
		sortedMap.put("language", "2");
		sortedMap.put("signType", "1");
		sortedMap.put("tranCode", "SC01");
		sortedMap.put("callType", "WX_WEB");
		sortedMap.put("merchantID", merchantID);
		sortedMap.put("merOrderNum", request.getRequestNo());
		sortedMap.put("tranAmt", request.getAmount());
		sortedMap.put("currencyType", "156");
		sortedMap.put("backgroundMerUrl", request.getBaseUrl() + "/pay/thirdpayconfig/recharge_gopay_weiixn");
		sortedMap.put("tranDateTime", request.getTransactionDateTime());
		
		String dateString = GopayUtils.getGopayServerTime();
		sortedMap.put("gopayServerTime", dateString);
		sortedMap.put("virCardNoIn", virCardNoIn);
		sortedMap.put("tranIP", request.getUserBrowerIP());
		sortedMap.put("goodsDetail", request.getRequestNo());
		//获取签名串原文
		String signData = rechargeSignData_weixin(sortedMap);
		System.out.println("签名数据=" + signData);
		sortedMap.put("signValue", GopayUtils.md5(signData));
		
		try {
			
			String resultMap="";//WebClient.getWebContentByPost(URL, WebClient.generateParams(sortedMap, "UTF-8"), "UTF-8", 60000);
			System.out.println("国付宝微信支付充值提交返回:"+resultMap.toString());
			if(resultMap!=null){
				/*String status = resultMap.get("respCode");
                if(status.equals("0000")){
                   result.setResponseCode("success");
                   result.setResponseMsg(resultMap.get("result"));
                   result.setRequestNo(request.getRequestNo());
                }else{
                	result.setResponseCode("fail");
    				result.setResponseMsg("充值申请失败，返回state!=0")
    				result.setRequestNo(request.getRequestNo());
                }*/
			}else{
				result.setResponseCode("fail");
				result.setResponseMsg("充值申请失败，返回为空！");
				result.setRequestNo(request.getRequestNo());
			}
		} catch (Exception e) {
			System.out.println("请求第三方异常！！！！！！");
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	/**
	 * 微信的签名串原文
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param gopay
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年3月24日 下午2:03:20   
	 * @throws:
	 */
	public static String rechargeSignData_weixin(SortedMap<String, String> sortedMap) {
		String signData = "version=[" + sortedMap.get("version")
				+ "]tranCode=["+ sortedMap.get("tranCode")
				+ "]merchantID=[" + sortedMap.get("merchantID")
				+ "]merOrderNum=[" + sortedMap.get("merOrderNum")
				+ "]tranAmt=["+ sortedMap.get("tranAmt")
				+ "]tranDateTime=["	+ sortedMap.get("tranDateTime")
				+ "]backgroundMerUrl=[" + sortedMap.get("backgroundMerUrl")
				+ "]gopayServerTime=[" + sortedMap.get("gopayServerTime")
				+ "]tranIP=[" + sortedMap.get("tranIP")
				+ "]callType=[WX_WEB"
				+ "]goodsTag=["
				+ "]productId=["
				+ "]VerficationCode=[" + identificationNum + "]";
		
		return signData;
		
	}
}
