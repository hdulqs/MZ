package com.mz.xinsheng;

import com.alibaba.fastjson.JSON;
import com.mz.util.properties.PropertiesUtils;
import com.mz.utils.CommonRequest;
import com.mz.jinkongpay.util.rsaUtil.RSASignUtil;
import com.mz.xinsheng.util.MyUtils;
import com.mz.xinsheng.util.PublicUtil;
import com.mz.xinsheng.util.WebClient;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;

/**
 * 新生支付接口
 * <p>
 * TODO
 * </p>
 *
 * @author: Zhang Lei
 * @Date : 2017年2月3日 下午3:29:32
 */
@SuppressWarnings("all")
public class XinshengpayInterfaceUtil {

  public static Properties THIRDAPP = null;
  public static String merchantID = "";
  public static String privateKey = "";
  public static String rechargeurl = "";//有收银台充值URL
  public static String rechargeurl_weixin = "";//微信的充值URL
  public static String rechargeurl_zhifubao = "";//支付宝的充值URL
  public static String rechargeurl_wangyin = "";//网银的充值URL
  public static String signtype = "";//加密方式，正式只能用rsa，测试可以用md5
  public static String certPath = "";//rsa证书路径
  public static String certPass = "";//rsa密码
  public static String qccodepath = "";//二维码路径
  public static String md5key = "";//MD5密钥


  static {
    // 商户的信息比如商户号公钥私钥之类的 都在thirdPayConfig.properties里面
    THIRDAPP = new Properties();
    try {
      THIRDAPP.load(new FileReader(PropertiesUtils.class.getClassLoader()
          .getResource("/thirdpayConfig/thirdPayConfig.properties")
          .getPath()));
      String typeString = "";
      if (THIRDAPP.containsKey("environmentType_jinkongpay")) {
        typeString = THIRDAPP.getProperty("environmentType_jinkongpay");
      }
      if (typeString.equals("normal")) {
        //商户号
        if (THIRDAPP.containsKey("MerchantID_jinkongpay")) {
          merchantID = THIRDAPP.getProperty("MerchantID_jinkongpay");
        }
        if (THIRDAPP.containsKey("privateKey_jinkongpay")) {
          privateKey = THIRDAPP.getProperty("privateKey_jinkongpay");
        }
        if (THIRDAPP.containsKey("rechargeurl_jinkongpay")) {
          rechargeurl = THIRDAPP.getProperty("rechargeurl_jinkongpay");
        }
        if (THIRDAPP.containsKey("signtype_jinkongpay")) {
          signtype = THIRDAPP.getProperty("signtype_jinkongpay");
        }
        if (THIRDAPP.containsKey("certPath_jinkongpay")) {
          certPath = THIRDAPP.getProperty("certPath_jinkongpay");
        }
        if (THIRDAPP.containsKey("certPass_jinkongpay")) {
          certPass = THIRDAPP.getProperty("certPass_jinkongpay");
        }
        if (THIRDAPP.containsKey("rechargeurl_weixin_jinkongpay")) {
          rechargeurl_weixin = THIRDAPP.getProperty("rechargeurl_weixin_jinkongpay");
        }
        if (THIRDAPP.containsKey("rechargeurl_zhifubao_jinkongpay")) {
          rechargeurl_zhifubao = THIRDAPP.getProperty("rechargeurl_zhifubao_jinkongpay");
        }
        if (THIRDAPP.containsKey("rechargeurl_wangyin_jinkongpay")) {
          rechargeurl_wangyin = THIRDAPP.getProperty("rechargeurl_wangyin_jinkongpay");
        }
        if (THIRDAPP.containsKey("qccodepath_jinkongpay")) {
          qccodepath = THIRDAPP.getProperty("qccodepath_jinkongpay");
        }
        if (THIRDAPP.containsKey("md5key_jinkongpay")) {
          md5key = THIRDAPP.getProperty("md5key_jinkongpay");
        }
      } else {
        if (THIRDAPP.containsKey("test.MerchantID_jinkongpay")) {
          merchantID = THIRDAPP.getProperty("test.MerchantID_jinkongpay");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 充值方法(有收银台模式)
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @param response
   * @param: @param request
   * @param: @return
   * @return: CommonRequest
   * @Date :         2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest recharge(HttpServletResponse response, CommonRequest request) {
    // 请求发送成功并不代表最终结果，最终结果需等待异步通知
    CommonRequest result = new CommonRequest();

    // 设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息
    result.setRequestUser(request.getRequestUser());
    result.setRequestUrl(request.getRequestUrl());
    result.setRequestThirdPay("xinshengpay");
    result.setAmount(request.getAmount());

    //初始化请求对象信息
    LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();

    sortedMap.put("version", "2.6");//标识接口版本信息，当前版本 2.6
    sortedMap.put("serialID", request.getRequestNo());//请求序列号(用流水号代替)
    sortedMap.put("submitTime", MyUtils.getTime("yyyyMMddHHmmss"));
    sortedMap.put("failureTime", "");
    sortedMap.put("customerIP", request.getUserBrowerIP());
    BigDecimal amount = new BigDecimal(request.getAmount());
    String postamount = amount.multiply(new BigDecimal(100)).toString();
    //orderDetails=2016061405055932409,100,,在线充值
    sortedMap
        .put("orderDetails", request.getRequestNo() + "," + postamount + ",,onlinepay,1");//订单明细信息

    sortedMap.put("totalAmount", postamount);//订单总金额（单位分）
    sortedMap.put("type", "1000");//交易类型 固定值：1000（默认）
		/*  ALL：商户签约后开通的所有付款方式（非直连时：默认值； 直连时：不可填 ALL）
			BANK_B2C：网银 B2C
			BANK_B2B：网银 B2BBANK_B2C
			LARGE_CREDIT_CARD：信用卡大额
			BANK_B2C_LARGE：网银 B2C 大额
			ACCT_RMB：新生 RMB 账户支付
			LARGE_DEBIT_CARD：借记卡大额
			CHINA_E_CARD：易卡支付
			CONSUME_CARD：充值卡
			QRCODE_B2C:二维码 B2C
		 */
    sortedMap.put("buyerMarked", "");
    sortedMap.put("payType", "ALL");
    sortedMap.put("orgCode", "");
    sortedMap.put("currencyCode", "1");
    sortedMap.put("directFlag", "0");
    sortedMap.put("borrowingMarked", "0");
    sortedMap.put("couponFlag", "");
    sortedMap.put("platformID", "");
    //异步通知
    sortedMap.put("returnUrl", request.getBaseUrl() + "/pay/thirdpayconfig/recharge_jinkongpay");
    //支付完的跳转页面
    sortedMap
        .put("noticeUrl", request.getBaseUrl() + "/pay/thirdpayconfig/recharge_jinkongpay_tbhd");
    sortedMap.put("partnerID", merchantID);//商户 ID
    sortedMap.put("remark", request.getRequestNo());//備注
    sortedMap.put("charset", "1");//报文编码格式1：UTF-8
    sortedMap.put("signType", "2");//选择报文签名类型	1：RSA 方式	2：MD5 方式

    String signData = MyUtils.mapToStringAndTrim1(sortedMap);

    if ("MD5".equals(signtype)) {
      signData += "&pkey=" + md5key;
      System.out.println("签名串为" + signData);
      sortedMap.put("signMsg", signData);
      try {
        //对验签串进行MD5加密
        String sign = MyUtils.getMD5Str(signData);
        sortedMap.put("signMsg", sign);//商户签名
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      //RSA验签方式
      sortedMap.put("signMsg", signData);
      String path = PropertiesUtils.class.getClassLoader().getResource(certPath).getPath();
      RSASignUtil util = new RSASignUtil(path, certPass);
      String merchantSign = util.sign(signData, "GBK");
      String merchantCert = util.getCertInfo();
      sortedMap.put("merchantSign", merchantSign);
      sortedMap.put("merchantCert", merchantCert);
    }

    try {
      WebClient.operateParameter(response, rechargeurl, sortedMap, "UTF-8");
    } catch (IOException e) {
      System.out.println("请求第三方异常！！！！！！");
      e.printStackTrace();
    }

    result.setResponseCode("success");
    result.setResponseMsg("充值申请成功");
    result.setRequestNo(request.getRequestNo());
    return result;
  }


  /**
   * 充值方法(微信模式)
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @param response
   * @param: @param request
   * @param: @return
   * @return: CommonRequest
   * @Date :         2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest recharge_weixin(HttpServletResponse response, CommonRequest request) {
    // 请求发送成功并不代表最终结果，最终结果需等待异步通知
    CommonRequest result = new CommonRequest();

    // 设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息
    result.setRequestUser(request.getRequestUser());
    result.setRequestUrl(request.getRequestUrl());
    result.setRequestThirdPay("jinkongpay");
    result.setAmount(request.getAmount());

    //初始化请求对象信息
    SortedMap<String, String> sortedMap = new TreeMap<String, String>();
    sortedMap.put("body", request.getRequestNo());//所购买商品的描述，不可空
    sortedMap.put("charset", "UTF-8");//字符集
    sortedMap.put("mch_create_ip", request.getUserBrowerIP());
    sortedMap.put("mch_id", merchantID);//商户号
    sortedMap.put("nonce_str", "qwedxfasdfsvzxcrgtdr");//随机字符串
    sortedMap
        .put("notify_url", request.getBaseUrl() + "pay/thirdpayconfig/recharge_jinkongpay_weixin");
    sortedMap.put("out_trade_no", request.getRequestNo());
    //客户端ip(用户在创建交易时，该用户当前所使用机器的IP)
    sortedMap.put("pay_type", "pc");
    sortedMap
        .put("service", "pay.weixin.native");//接口类型Pc扫码：pay.weixin.native	App扫码: unified.trade.pay
    sortedMap.put("sign_type", "RSA");//签名方式，现只支持MD5
    BigDecimal amount = new BigDecimal(request.getAmount());
    sortedMap.put("total_fee", amount.multiply(new BigDecimal(100)).toString());//订单金额，单位分
    sortedMap.put("version", Xinshengpay.VER);

    //sortedMap.put("attach", "hrypay");//扩展参数，可空

    String signData = MyUtils.mapToStringAndTrim(sortedMap);
    System.out.println("签名串为" + signData);
    if ("MD5".equals(signtype)) {
      signData += "&key=123456";
      sortedMap.put("signData", signData);
      try {
        //对验签串进行MD5加密
        String sign = MyUtils.md5Encode(signData, "GBK").toUpperCase();
        sortedMap.put("merchantSign", sign);//商户签名
        sortedMap.put("merchantCert", sign);//商户证书
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      //RSA验签方式
      //sortedMap.put("signData", signData);
      String path = PropertiesUtils.class.getClassLoader().getResource(certPath).getPath();
      RSASignUtil util = new RSASignUtil(path, certPass);
      String merchantSign = util.sign(signData, "GBK");
      String merchantCert = util.getCertInfo();
      sortedMap.put("sign", merchantSign);
      sortedMap.put("cert", merchantCert);
    }
    try {
      Map<String, String> resultMap = WebClient.httpPost(sortedMap, rechargeurl_weixin);
      //System.out.println("微信支付充值提交返回:"+resData.toString());
      if (resultMap != null) {
        String status = resultMap.get("status");
        if (status.equals("0")) {
          result.setResponseCode("success");
          result.setResponseMsg(resultMap.get("code_img_url"));
          result.setRequestNo(request.getRequestNo());
          //map.get("out_trade_no");
          //map.get("total_fee");
          //URLDecoder.decode(map.get("body"),"UTF-8");
        } else {
          result.setResponseCode("fail");
          result.setResponseMsg("充值申请失败，返回state!=0");
          result.setRequestNo(request.getRequestNo());
        }
      } else {
        result.setResponseCode("fail");
        result.setResponseMsg("充值申请失败，返回为空！");
        result.setRequestNo(request.getRequestNo());
      }
    } catch (IOException e) {
      System.out.println("请求第三方异常！！！！！！");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 充值方法(支付宝模式)
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @param response
   * @param: @param request
   * @param: @return
   * @return: CommonRequest
   * @Date :         2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest recharge_zhifubao(HttpServletResponse response,
      CommonRequest request) {
    // 请求发送成功并不代表最终结果，最终结果需等待异步通知
    CommonRequest result = new CommonRequest();

    // 设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息
    result.setRequestUser(request.getRequestUser());
    result.setRequestUrl(request.getRequestUrl());
    result.setRequestThirdPay("jinkongpay");
    result.setAmount(request.getAmount());

    //初始化请求对象信息
    SortedMap<String, String> sortedMap = new TreeMap<String, String>();

    sortedMap
        .put("callBack", request.getBaseUrl() + "pay/thirdpayconfig/recharge_jinkongpay_zhifubao");
    sortedMap.put("charset", Xinshengpay.CHARSET_UTF8);//字符集
    sortedMap.put("merchantId", merchantID);//商户号
    sortedMap.put("msgType", "01");
    sortedMap.put("orderId", request.getRequestNo());

    String format = "yyyyMMddHHmmss";
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    sortedMap.put("orderTime", sdf.format(new Date()));

    sortedMap.put("payWay", "ZFBZF");
    sortedMap.put("productDesc", request.getRequestNo());//所购买商品的描述，不可空
    sortedMap.put("productName", request.getRequestNo());//所购买商品的描述，不可空

    sortedMap.put("signType", "RSA");//签名方式，现只支持MD5
    BigDecimal amount = new BigDecimal(request.getAmount());
    sortedMap.put("terminalIp", request.getUserBrowerIP());
    sortedMap.put("totalAmount", amount.multiply(new BigDecimal(100)).toString());//订单金额，单位分
    sortedMap.put("version", "1.0.0");

    String signData = MyUtils.mapToStringAndTrim(sortedMap);
    System.out.println("签名串为" + signData);
    if ("MD5".equals(signtype)) {
      signData += "&key=123456";
      sortedMap.put("signData", signData);
      try {
        //对验签串进行MD5加密
        String sign = MyUtils.md5Encode(signData, "GBK").toUpperCase();
        sortedMap.put("merchantSign", sign);//商户签名
        sortedMap.put("merchantCert", sign);//商户证书
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      //RSA验签方式
      //sortedMap.put("signData", signData);
      String path = PropertiesUtils.class.getClassLoader().getResource(certPath).getPath();
      RSASignUtil util = new RSASignUtil(path, certPass);
      String merchantSign = util.sign(signData, "GBK");
      String merchantCert = util.getCertInfo();
      sortedMap.put("merchantSign", merchantSign);
      sortedMap.put("merchantCert", merchantCert);
    }
    try {
      //Map<String,String> resultMap=WebClient.httpPost1(sortedMap, rechargeurl_zhifubao);
      String submitData = PublicUtil.mapToStringAndTrim(sortedMap);//请求数据
      sortedMap.put("serverUrl", rechargeurl_zhifubao);
      String requestData = (String) sortedMap.get("serverUrl") + "?" + submitData;

      String resultData = PublicUtil
          .sendPost(requestData, (String) sortedMap.get("serverUrl"), "GBK");
      System.out.println("支付宝支付请求返回:" + resultData);
      Map<String, String> resultMap = (Map) JSON.parse(resultData);

      if (resultMap != null) {
        String status = resultMap.get("respType");
        String qrCode = resultMap.get("qrCode");
        if ("R".equals(status) && qrCode != null && !"".equals(qrCode)) {
          result.setResponseCode("success");

          //获取tomcat的绝对路径
          String tomcatPath = System.getProperty("catalina.home");

          //这里需要将URL转成二维码保存
          String qcpath = tomcatPath + qccodepath + "/";
          String qcFileName = request.getRequestNo() + "_" + sdf.format(new Date()) + ".png";
          String truePath = qcpath + qcFileName;
          //PropertiesUtils.class.getClassLoader().getResource(qcpath).getPath();
          System.out.println(truePath);
          File f = new File(truePath);
          if (!f.exists()) {
            f.createNewFile();
          }

          PublicUtil.encoderQRCode(qrCode, truePath);

          //返回绝对路径
          result.setResponseMsg(qcFileName);
          result.setRequestNo(request.getRequestNo());
          //map.get("out_trade_no");
          //map.get("total_fee");
          //URLDecoder.decode(map.get("body"),"UTF-8");
        } else {
          result.setResponseCode("fail");
          result.setResponseMsg("支付宝充值申请失败，返回state!=0");
          result.setRequestNo(request.getRequestNo());
        }
      } else {
        result.setResponseCode("fail");
        result.setResponseMsg("支付宝充值申请失败，返回为空！");
        result.setRequestNo(request.getRequestNo());
      }
    } catch (Exception e) {
      System.out.println("请求第三方异常！！！！！！");
      e.printStackTrace();
    }
    return result;
  }


  /**
   * 充值方法(网银模式（无收银台页面）)
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @param response
   * @param: @param request
   * @param: @return
   * @return: CommonRequest
   * @Date :         2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest recharge_wangyin(HttpServletResponse response,
      CommonRequest request) {
    // 请求发送成功并不代表最终结果，最终结果需等待异步通知
    CommonRequest result = new CommonRequest();

    // 设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息
    result.setRequestUser(request.getRequestUser());
    result.setRequestUrl(request.getRequestUrl());
    result.setRequestThirdPay("jinkongpay");
    result.setAmount(request.getAmount());

    //初始化请求对象信息
    SortedMap<String, String> sortedMap = new TreeMap<String, String>();

    sortedMap.put("merchantId", merchantID);
    sortedMap.put("charset", Xinshengpay.CHARSET_UTF8);
    sortedMap.put("clientIP", request.getUserBrowerIP());
    BigDecimal amount = new BigDecimal(request.getAmount());
    sortedMap.put("totalAmount", amount.multiply(new BigDecimal(100)).toString());//订单金额，单位分
    //sortedMap.put("showUrl", model.getShowUrl());
    sortedMap.put("purchaserId", request.getRequestUser());
    sortedMap.put("productName", request.getRequestNo());
    //sortedMap.put("productId", model.getProductId());
    sortedMap.put("productDesc", request.getRequestNo());
    sortedMap.put("backParam", "zbwbbzka");
    sortedMap.put("validNum", "999");
    sortedMap.put("validUnit", Xinshengpay.VALIDUNIT_MONTH);
    sortedMap.put("currency", Xinshengpay.CUR);

    sortedMap.put("bankAbbr", request.getBankAccNum());
    sortedMap.put("cardType", request.getBankAcctName());
    sortedMap.put("requestId", request.getRequestNo());
    //sortedMap.put("merchantName", model.getMerchantName());
    sortedMap.put("version", "1.0");
    sortedMap.put("orderId", request.getRequestNo());
    sortedMap.put("orderTime", MyUtils.getTime("yyyyMMddHHmmss"));
    sortedMap.put("service", "NpDirectPayment");

    sortedMap.put("offlineNotifyUrl",
        request.getBaseUrl() + "pay/thirdpayconfig/recharge_jinkongpay_wangyin");
    sortedMap
        .put("pageReturnUrl", request.getBaseUrl() + "pay/thirdpayconfig/recharge_jinkongpay_tbhd");

    sortedMap.put("signType", "RSA");//签名方式，现只支持MD5
    String signData = MyUtils.mapToStringAndTrim(sortedMap);
    System.out.println(sortedMap);

    if ("MD5".equals(signtype)) {
      signData += "&key=123456";
      sortedMap.put("signData", signData);
      try {
        //对验签串进行MD5加密
        String sign = MyUtils.md5Encode(signData, "GBK").toUpperCase();
        sortedMap.put("merchantSign", sign);
        sortedMap.put("merchantCert", sign);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {//RSA验签方式
      String path = PropertiesUtils.class.getClassLoader().getResource(certPath).getPath();
      RSASignUtil util = new RSASignUtil(path, certPass);
      String merchantSign = util.sign(signData, "GBK");
      String merchantCert = util.getCertInfo();
      sortedMap.put("merchantSign", merchantSign);
      sortedMap.put("merchantCert", merchantCert);
    }

    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      //获取tomcat的绝对路径
      String tomcatPath = System.getProperty("catalina.home");
      //将返回的页面写到支付宝二维码地址
      //这里需要将URL转成二维码保存
      String qcpath = tomcatPath + qccodepath + "/";
      String qcFileName = request.getRequestNo() + "_" + sdf.format(new Date()) + ".html";
      String truePath = qcpath + qcFileName;
      System.out.println(truePath);
      File f = new File(truePath);
      if (!f.exists()) {
        f.createNewFile();
      }
      WebClient.writePage(truePath, rechargeurl_wangyin, sortedMap, "UTF-8");

      //返回绝对路径
      result.setResponseCode("success");
      result.setResponseMsg(qcFileName);
      result.setRequestNo(request.getRequestNo());
      //WebClient.operateParameter(response, rechargeurl_wangyin,sortedMap, "UTF-8");
    } catch (IOException e) {
      System.out.println("请求第三方异常！！！！！！");
      e.printStackTrace();
    }

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
   * @Date : 2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest rechargeCallBack(Map<String, Object> map) {
    System.out.println("新生充值回调。。。。。。。。。");
    System.out.println(map.toString());

    CommonRequest commonRequest = new CommonRequest();
    commonRequest.setRequestThirdPay("jinkongpay");
    String merOrderNum = map.get("orderID").toString();// 订单号系统
    commonRequest.setRequestNo(merOrderNum);
    commonRequest.setResponseObj(map.toString());

    if (map.containsKey("stateCode")) {
      String status = map.get("stateCode").toString();

      if ("2".equals(status)) {
        System.out.println("订单号为:" + merOrderNum + " 充值成功");
        commonRequest.setResponseCode("success");
        commonRequest.setResponseMsg("充值成功");
        commonRequest.setResponseObj(map.toString());
      } else {
        System.out.println("订单号为:" + merOrderNum + " 充值失败");
        commonRequest.setResponseCode("faile");
        commonRequest.setResponseMsg("充值失败");
      }
    }
    return commonRequest;
  }

  /**
   * 充值回调(微信端)
   * <p>
   * TODO
   * </p>
   *
   * @author: Zhang Lei
   * @param: @param map
   * @param: @return
   * @return: CommonRequest
   * @Date : 2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest rechargeCallBack_weixin(Map<String, Object> map) {
    System.out.println("金控微信端充值回调。。。。。。。。。" + map.toString());
    System.out.println(map.toString());

    CommonRequest commonRequest = new CommonRequest();
    commonRequest.setRequestThirdPay("jinkongpay");
    String merOrderNum = map.get("ppd_mord_no").toString();// 订单号系统
    commonRequest.setRequestNo(merOrderNum);
    commonRequest.setResponseObj(map.toString());

    if (map.containsKey("ord_sts")) {
      String status = map.get("ord_sts").toString();

      if ("S1".equals(status)) {
        System.out.println("订单号为:" + merOrderNum + " 充值成功");
        commonRequest.setResponseCode("success");
        commonRequest.setResponseMsg("充值成功");
        commonRequest.setResponseObj(map.toString());
      } else {
        System.out.println("订单号为:" + merOrderNum + " 充值失败");
        commonRequest.setResponseCode("faile");
        commonRequest.setResponseMsg("充值失败");
      }
    }
    return commonRequest;
  }


  /**
   * 充值回调(支付宝端)
   * <p>
   * TODO
   * </p>
   *
   * @author: Zhang Lei
   * @param: @param map
   * @param: @return
   * @return: CommonRequest
   * @Date : 2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest rechargeCallBack_zhifubao(Map<String, Object> map) {
    System.out.println("金控支付宝端充值回调。。。。。。。。。" + map.toString());
    System.out.println(map.toString());

    CommonRequest commonRequest = new CommonRequest();
    commonRequest.setRequestThirdPay("jinkongpay");
    String merOrderNum = map.get("orderId").toString();// 订单号系统
    commonRequest.setRequestNo(merOrderNum);
    commonRequest.setResponseObj(map.toString());

    if (map.containsKey("respCode")) {
      String status = map.get("respCode").toString();

      if ("000000".equals(status)) {
        System.out.println("订单号为:" + merOrderNum + " 充值成功");
        commonRequest.setResponseCode("success");
        commonRequest.setResponseMsg("充值成功");
        commonRequest.setResponseObj(map.toString());
      } else {
        System.out.println("订单号为:" + merOrderNum + " 充值失败");
        commonRequest.setResponseCode("faile");
        commonRequest.setResponseMsg("充值失败");
      }
    }
    return commonRequest;
  }


  /**
   * 充值回调(网银端)
   * <p>
   * TODO
   * </p>
   *
   * @author: Zhang Lei
   * @param: @param map
   * @param: @return
   * @return: CommonRequest
   * @Date : 2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest rechargeCallBack_wangyin(Map<String, Object> map) {
    System.out.println("金控网银端充值回调。。。。。。。。。" + map.toString());
    System.out.println(map.toString());

    CommonRequest commonRequest = new CommonRequest();
    commonRequest.setRequestThirdPay("jinkongpay");
    String merOrderNum = map.get("orderId").toString();// 订单号系统
    commonRequest.setRequestNo(merOrderNum);
    commonRequest.setResponseObj(map.toString());

    if (map.containsKey("status")) {
      String status = map.get("status").toString();

      if ("SUCCESS".equals(status)) {
        System.out.println("订单号为:" + merOrderNum + " 充值成功");
        commonRequest.setResponseCode("success");
        commonRequest.setResponseMsg("充值成功");
        commonRequest.setResponseObj(map.toString());
      } else {
        System.out.println("订单号为:" + merOrderNum + " 充值失败");
        commonRequest.setResponseCode("faile");
        commonRequest.setResponseMsg("充值失败");
      }
    }
    return commonRequest;
  }


  /**
   * 提现接口
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @param request
   * @param: @return
   * @return: CommonRequest
   * @Date :          2017年2月3日 下午3:29:32
   * @throws:
   */
  public static CommonRequest withdraw(CommonRequest request) {
    CommonRequest resRequest = new CommonRequest();
    return resRequest;
  }


  public static void main(String[] args) {

  }

}
