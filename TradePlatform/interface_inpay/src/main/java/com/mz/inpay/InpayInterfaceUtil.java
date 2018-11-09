package com.mz.inpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.utils.CommonRequest;
import com.mz.utils.CommonResponse;
import com.mz.inpay.utils.AesUtils;
import com.mz.inpay.utils.Common;
import com.mz.inpay.utils.InpayUtil;
import com.mz.inpay.utils.RSA;
import com.mz.inpay.utils.WebClient;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InpayInterfaceUtil {

  public static Properties config;


  /**
   * 加载正式或者测试的配置文件
   */
  public static void gain() {
    String environmental = Common.Environmental;
    InputStream ins = null;

    config = new Properties();
    try {
      if ("normal".equals(environmental)) {
        ins = InpayInterfaceUtil.class.getClassLoader()
            .getResourceAsStream("customer/normal/inpayNormal.properties");
      } else {
        ins = InpayInterfaceUtil.class.getClassLoader()
            .getResourceAsStream("customer/test/inpayTest.properties");
      }
      config.load(ins);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 网银充值
   */
  public static CommonResponse paywy(CommonRequest commonRequest, HttpServletRequest request,
      HttpServletResponse response) {
    System.out.println("-----------------进入网银充值---------------------");
    SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
    //加载配置文件
    gain();

    CommonResponse commonResponse = new CommonResponse();
    String ip = InpayUtil.getIp(request);

    commonRequest.setServiceName("ebankPayApi");
    commonRequest.setMerchantId(config.getProperty("merchantId"));
    commonRequest.setVersion("V1");
    commonRequest.setMerOrderId(commonRequest.getMerOrderId());
    commonRequest.setNotifyUrl(config.getProperty("notifyUrlwy"));
    commonRequest.setReturnUrl(config.getProperty("returnUrlwy"));
    commonRequest.setProductName(commonRequest.getProductName());
    commonRequest.setProductDesc(commonRequest.getProductDesc());
    commonRequest.setTranAmt(commonRequest.getTranAmt());
    commonRequest.setCurrency("CNY");
    commonRequest.setBankCardType(commonRequest.getBankCardType());
    commonRequest.setBankCode(commonRequest.getBankCode());
    commonRequest.setClientIp(ip);
    //commonRequest.setPayerId(commonRequest.getPayerId());
    commonRequest.setTranTime(sim.format(new Date()));
    commonRequest.setPayType("01");
		
		/*commonRequest.setServiceName("ebankPayApi");
		commonRequest.setMerchantId(config.getProperty("merchantId"));
		commonRequest.setPlatform(config.getProperty("platform"));
		commonRequest.setMerOrderId(InpayUtil.num());
		commonRequest.setNotifyUrl(config.getProperty("returnUrlwy"));
		commonRequest.setReturnUrl(config.getProperty("notifyUrlwy"));
		commonRequest.setProductName(commonRequest.getProductName());
		commonRequest.setProductDesc(commonRequest.getProductDesc());
		commonRequest.setTranAmt(new BigDecimal("0.01"));
		commonRequest.setCurrency("CNY");
		commonRequest.setBankCardType("01");
		commonRequest.setBankCode("CMB");
		commonRequest.setClientIp(ip);
		//commonRequest.setPayerId("15926553111");
		commonRequest.setTranTime(sim.format(new Date()));
		commonRequest.setPayType("01");*/

    //拼接参数
    String str = InpayUtil.common(commonRequest);
    //读取秘钥内容
    String key = config.getProperty("myPrivate");
    String pemContent = InpayUtil.method2(key);
    //加密
    String sign = "";
    try {
      sign = RSA.sign(str, pemContent, "UTF-8");
      System.out.println(sign);
      Map<String, String> createMap = InpayUtil.createMap(CommonRequest.class, commonRequest);
      Map<String, String> params = InpayUtil.params(createMap, sign);

      String[] strParam = WebClient
          .operateParameter(config.getProperty("paywy"), params, "UTF-8", response);
      if ("8888".equals(strParam[0])) {
        commonResponse.setSuccess(true);
        commonResponse.setMsg("充值成功");
        commonResponse.setObj(strParam[1]);
      } else {
        commonResponse.setSuccess(false);
        commonResponse.setMsg("充值失败");
        commonResponse.setObj(strParam[1]);
      }
    } catch (Exception e) {
      e.printStackTrace();
      commonResponse.setSuccess(false);
      commonResponse.setMsg("充值失败");
    }
    return commonResponse;
  }

  /**
   * 订单查询
   */
  public static CommonResponse querywy(CommonRequest commonRequest, HttpServletRequest request,
      HttpServletResponse response) {
    //加载配置文件
    gain();
    String ip = InpayUtil.getIp(request);

    CommonResponse commonResponse = new CommonResponse();
    commonRequest.setServiceName("queryResult");
    commonRequest.setMerchantId(config.getProperty("merchantId"));
    commonRequest.setPlatform(config.getProperty("platform"));
    //commonRequest.setMerOrderId(commonRequest.getMerOrderId());
    commonRequest.setClientIp(ip);
    commonRequest.setPayType("01");

    //拼接参数
    String str = InpayUtil.common(commonRequest);
    //读取秘钥内容
    String key = config.getProperty("myPrivate");
    String pemContent = InpayUtil.method2(key);
    //加密
    String sign = "";
    try {
      sign = RSA.sign(str, pemContent, "UTF-8");
      System.out.println(sign);
      Map<String, String> createMap = InpayUtil.createMap(CommonRequest.class, commonRequest);
      Map<String, String> params = InpayUtil.params(createMap, sign);

      String strParam = WebClient
          .getWebContentByPost(config.getProperty("querywy"), InpayUtil.getSignatureContent(params),
              "UTF-8", 10000);
      System.out.println(strParam);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return commonResponse;
  }

  //********************************************************weixin******************************************************************
	
	/*public static CommonResponse paywx(){
		
	}*/


  public static CommonResponse prepay(CommonRequest commonRequest, HttpServletRequest request,
      HttpServletResponse response, String type) {
    CommonResponse commonResponse = new CommonResponse();

    SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
    //加载配置文件
    gain();

    String ip = InpayUtil.getIp(request);

    if ("1".equals(type)) {//微信
      System.out.println("-----------------进入微信充值---------------------");
      commonRequest.setServiceName("wcprepay");
      commonRequest.setPayType("10");
      commonRequest.setReqChannel("WECHAT");
      commonRequest.setNotifyUrl(config.getProperty("notifyUrlwx"));
    } else if ("2".equals(type)) {//支付宝
      System.out.println("-----------------进入支付宝充值---------------------");
      commonRequest.setServiceName("aliprepay");
      commonRequest.setPayType("20");
      commonRequest.setReqChannel("ALIPAY");
      commonRequest.setNotifyUrl(config.getProperty("notifyUrlzfb"));
    }
    commonRequest.setMergeType("NATIVE");
    commonRequest.setMerchantId(config.getProperty("merchantId"));
    commonRequest.setCurrency("CNY");
    commonRequest.setClientIp(ip);
    commonRequest.setLimitCreditPay("01");

    commonRequest.setTradeAmt(commonRequest.getTradeAmt());
    commonRequest.setMerchOrderNo(commonRequest.getMerchOrderNo());
    commonRequest.setSubject(commonRequest.getSubject());

    //拼接参数
    String str = InpayUtil.common(commonRequest);
    //读取秘钥内容
    String key = config.getProperty("myPrivate");
    String pemContent = InpayUtil.method2(key);
    //加密
    String sign = "";
    try {
      sign = RSA.sign(str, pemContent, "UTF-8");
      sign = URLEncoder.encode(sign);
      System.out.println(sign);
      Map<String, String> createMap = InpayUtil.createMap(CommonRequest.class, commonRequest);
      Map<String, String> params = InpayUtil.params(createMap, sign);

      String strParam = WebClient
          .getWebContentByPost(config.getProperty("paywa"), InpayUtil.getSignatureContent(params),
              "UTF-8", 20000);
      System.out.println(strParam);

      if (strParam == null) {
        //Json转换
        commonResponse.setSuccess(false);
        commonResponse.setMsg("第三方异常，请联系管理员");
      } else {
        JSONObject json = JSON.parseObject(strParam);//json字符串转换成jsonobject对象
        String retCode = json.get("retCode").toString();
        if (!"0000".equals(retCode)) {
          String retMsg = json.get("retMsg").toString();
          commonResponse.setSuccess(false);
          commonResponse.setMsg(retMsg);
        } else {
          //Json转换
          String value = json.get("codeUrl").toString();
          commonResponse.setSuccess(true);
          commonResponse.setObj(value);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      commonResponse.setSuccess(false);
      commonResponse.setMsg("充值异常");
    }
    return commonResponse;
  }

  //**************************************************快捷支付*****************************************************

  /**
   * 支付接口
   */
  public static CommonResponse placeQuickPay(CommonRequest commonRequest,
      HttpServletRequest request, HttpServletResponse response) {
    System.out.println("-----------------进入快捷支付充值---------------------");
    CommonResponse commonResponse = new CommonResponse();

    SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
    String date = sim.format(new Date());
    //加载配置文件
    gain();

    String ip = InpayUtil.getIp(request);

    //明文加密
    commonRequest.setServiceName("quickPayApi");
    commonRequest.setMerchantId(config.getProperty("merchantId"));
    commonRequest.setNotifyUrl(config.getProperty("notifyUrlquick"));
    commonRequest.setCurrency("CNY");
    commonRequest.setPayType("03");
    commonRequest.setMerOrderId(commonRequest.getMerOrderId());
    commonRequest.setTranAmt(commonRequest.getTranAmt());
    commonRequest.setTranTime(date);
    commonRequest.setBankCardNo(commonRequest.getBankCardNo());
    commonRequest.setIdNo(commonRequest.getIdNo());
    commonRequest.setBankAcctName(commonRequest.getBankAcctName());
    commonRequest.setMobileNo(commonRequest.getMobileNo());
    commonRequest.setClientIp(ip);

    //拼接参数
    String str = InpayUtil.common(commonRequest);
    //读取秘钥内容
    String key = config.getProperty("myPrivate");
    String pemContent = InpayUtil.method2(key);
    //加密
    String sign = "";

    try {
      sign = RSA.sign(str, pemContent, "UTF-8");
      sign = URLEncoder.encode(sign);
      System.out.println(sign);

      //密文传输
      commonRequest.setServiceName("quickPayApi");
      commonRequest.setMerchantId(config.getProperty("merchantId"));
      commonRequest.setNotifyUrl(config.getProperty("notifyUrlquick"));
      commonRequest.setCurrency("CNY");
      commonRequest.setPayType("03");
      commonRequest.setMerOrderId(URLEncoder
          .encode(AesUtils.encrypt(commonRequest.getMerOrderId(), config.getProperty("aes"))));
      commonRequest.setTranAmt(URLEncoder
          .encode(AesUtils.encrypt(commonRequest.getTranAmt(), config.getProperty("aes"))));
      commonRequest.setTranTime(date);
      commonRequest.setBankCardNo(URLEncoder
          .encode(AesUtils.encrypt(commonRequest.getBankCardNo(), config.getProperty("aes"))));
      commonRequest.setIdNo(
          URLEncoder.encode(AesUtils.encrypt(commonRequest.getIdNo(), config.getProperty("aes"))));
      commonRequest.setBankAcctName(URLEncoder
          .encode(AesUtils.encrypt(commonRequest.getBankAcctName(), config.getProperty("aes"))));
      commonRequest.setMobileNo(URLEncoder
          .encode(AesUtils.encrypt(commonRequest.getMobileNo(), config.getProperty("aes"))));
      commonRequest.setClientIp(ip);

      Map<String, String> createMap = InpayUtil.createMap(CommonRequest.class, commonRequest);
      Map<String, String> params = InpayUtil.params(createMap, sign);

      String strParam = WebClient.getWebContentByPost(config.getProperty("placeQuickPay"),
          InpayUtil.getSignatureContent(params), "UTF-8", 20000);
      System.out.println(strParam);
      if (strParam == null) {
        commonResponse.setSuccess(false);
        commonResponse.setMsg("充值异常");
      } else {
        JSONObject json = JSON.parseObject(strParam);//json字符串转换成jsonobject对象
        String retCode = json.get("retCode").toString();
        if (!"0000".equals(retCode)) {
          String retMsg = json.get("retMsg").toString();
          commonResponse.setSuccess(false);
          commonResponse.setMsg(retMsg);
        } else {
          //Json转换
          String value = json.get("merOrderId").toString();
          commonResponse.setSuccess(true);
          commonResponse.setObj(value);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      commonResponse.setSuccess(false);
      commonResponse.setMsg("充值异常");
    }
    return commonResponse;
  }

  /**
   * 确认支付
   */
  public static CommonResponse confirmPay(CommonRequest commonRequest, HttpServletRequest request,
      HttpServletResponse response) {
    System.out.println("-----------------进入快捷支付充值确认---------------------");
    CommonResponse commonResponse = new CommonResponse();

    //加载配置文件
    gain();

    //明文加密
    commonRequest.setServiceName("confirmPay");
    commonRequest.setMerchantId(config.getProperty("merchantId"));
    commonRequest.setPayType("03");
    commonRequest.setMerOrderId(commonRequest.getMerOrderId());
    //拼接参数
    String str = InpayUtil.common(commonRequest);
    //读取秘钥内容
    String key = config.getProperty("myPrivate");
    String pemContent = InpayUtil.method2(key);
    //加密
    String sign = "";
    try {
      sign = RSA.sign(str, pemContent, "UTF-8");
      sign = URLEncoder.encode(sign);
      System.out.println(sign);

      //密文验签
      commonRequest.setServiceName("confirmPay");
      commonRequest.setMerchantId(config.getProperty("merchantId"));
      commonRequest.setPayType("03");
      commonRequest.setMerOrderId(URLEncoder
          .encode(AesUtils.encrypt(commonRequest.getMerOrderId(), config.getProperty("aes"))));

      Map<String, String> createMap = InpayUtil.createMap(CommonRequest.class, commonRequest);
      Map<String, String> params = InpayUtil.params(createMap, sign);

      String strParam = WebClient.getWebContentByPost(config.getProperty("confirmPay"),
          InpayUtil.getSignatureContent(params), "UTF-8", 20000);

      System.out.println(strParam);
      if (strParam == null) {
        commonResponse.setSuccess(false);
        commonResponse.setMsg("充值异常");
      } else {
        JSONObject json = JSON.parseObject(strParam);//json字符串转换成jsonobject对象
        String retCode = json.get("retCode").toString();
        if (!"0000".equals(retCode)) {
          String retMsg = json.get("retMsg").toString();
          commonResponse.setSuccess(false);
          commonResponse.setMsg(retMsg);
        } else {
          //Json转换
          commonResponse.setSuccess(true);
          commonResponse.setMsg("充值成功");
        }
      }
			/*String[] strParam = WebClient.operateParameter(config.getProperty("confirmPay"), params, "UTF-8", response);
			if("8888".equals(strParam[0])){
				commonResponse.setSuccess(true);
				commonResponse.setMsg("充值成功");
				commonResponse.setObj(strParam[1]);
			}else{
				commonResponse.setSuccess(false);
				commonResponse.setMsg("充值失败");
				commonResponse.setObj(strParam[1]);
			}*/
    } catch (Exception e) {
      e.printStackTrace();
      commonResponse.setSuccess(false);
      commonResponse.setMsg("充值失败");
    }
    return commonResponse;
  }


  public static void main(String[] args) throws Exception {
    CommonRequest commonRequest = new CommonRequest();
    //pay(commonRequest);
    gain();
    String key = config.getProperty("myPrivate");

    String aa = "bankAcctName=王美晨&bankCardNo=6214830161611776&bankCardType=01&bankCode=CMB&charset=UTF-8&clientIp=47.75.200.109&currency=CNY&idNo=420881199511248915&idType=01&merOrderId=170828144105435&merchantId=000290048160188&mobileNo=18701526287&notifyUrl=http://www.200.com/manage/pay/thirdpayconfig/recharge_inpay_quick.do&payType=03&payerId=15926553111&productDesc=15926553111用户充值&productName=15926553111用户充值&serviceName=quickPayApi&tranAmt=1.19&tranTime=20170828144109&version=V1";
    String bb = "yZxGH8LPZkJH4Dwns4iq6cWzAsYusXsPGj80CZsoMUl91i3Bc%2FSLea0qXVFdbVRguXGkzrKNlSVElPDcbsv6u03pCICHYfRZmkIAsbg7%2B7rLWux%2FIp5gy6N%2FcDnaFNxwVchAyXXNqSaR7D1tIq22ZFBcQ3uVXfM9Nfaa0g%2Faq70%3D";
    String method2 = InpayUtil.method2("my/rsa_public_key.pem");
    boolean verifySign = RSA.verifySign(aa, bb, method2, "UTF-8");
    System.out.println(verifySign);
    //{"sign":"O2zOzlNIuiE/AXXnHZLDGnIbel9wpvsR1a9aHy7uoYj8wrE3F+oEfg61F17sRhliw94fLAC/JJ9z+5QBxeMD1Ah1LKh4fVj2D8fqM9ZfoF6ZxFSNXlm2wrljLtohagSCrJMvDy8a0AXL6vw292B41eMp7RiWVyNZowhb6EqjRXDG0AMTeNmw0VHtbeMjNNRkA4OAHBS/ofrxIQHQmzC4xGKay+pYNF2hpkuSljqhgEh7KBYY79rEAR/Dq5S6CHd9GIyCBMlKVRSCKrbl8JITfWW3hpu4+L8lIZoEUWPLZrQ3ItYRB6yCKEsqnQLesNbDHvleIh5X0Fq9fj1/0C0iqw==","tranStatus":"01","retCode":"0000","retMsg":"交易成功","codeUrl":"https://qr.alipay.com/bax07729i8nrz04notk60045","tranNo":"20170825162814089071","merchantId":"000290048160188","merOrderId":"170825163355752","appParams":"null"}
    String abc = "{\"codeUrl\":\"https://qr.alipay.com/bax07729i8nrz04notk60045\",\"tranNo\":\"20170825162814089071\",\"merchantId\":\"000290048160188\",\"merOrderId\":\"170825163355752\",\"appParams\":\"null\"}";
    JSONObject jso = JSON.parseObject(abc);//json字符串转换成jsonobject对象
    //System.out.println(jso.get("codeUrl"));
		
		/*String sign = "vmHKzRnb8lF3QETZWwG8M9WJCo/itglNMduioc70qtsIXUqoEZx616iruYIdY1klNVrRaLJ4ODaHljUtRynrOgbtAlhPgcETgwWmvEHM2mF2Deocv8chy9TbpN+aFUXckMpLdRg3m8tu5W9oOYs2HMYt5NT95KnLbIlJmOl/YVktEez4FwvgCDEcVYe98ukJe08A0G9DdYN01XHPcYDT2LIb7+u8kttZXwXbfCReJxhBK0ge685zqAZYhk+cFPfcFj7tsT92Ynpj4W0c+ENiDvpTK/O9fSHinlB+qzjL/JgFDUJFme4HVMV27yCd4v6+pDgjcOX3BNCoQOB3lYk5Xg==";
		String method3 = InpayUtil.method2("customer/test/chinainpay_public_key-test.pem");
		String signInpay = RSA.encrypt(sign, method3, "UTF-8");
		System.out.println(signInpay);*/
  }
}
