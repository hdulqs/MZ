/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月5日 上午9:39:45
 */
package com.mz.huicaopay;

import com.itrus.util.sign.RSAWithSoftware;
import com.mz.util.date.DateUtil;
import com.mz.util.file.Md5Util;
import com.mz.util.properties.PropertiesUtils;
import com.mz.utils.CommonRequest;
import com.mz.huicaopay.utils.WebClient;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 智付接口
 * </p>
 *
 * @author: Zhang Xiaofang
 * @Date : 2016年7月5日 上午9:39:45
 */
public class HuicaopayInterfaceUtil {


  public static Properties THIRDAPP = null;

  public static String merchantID = "";
  public static String virCardNoIn = "";
  public static String identificationNum = "";
  public static String privateKey = "";
  public static String publicKey = "";
  public static String dinPublicKey = "";
  public static String md5key = "";


  static {

    //商户的信息比如商户号公钥私钥之类的 都在thirdPayConfig.properties里面
    THIRDAPP = new Properties();
    try {
      THIRDAPP.load(new FileReader(PropertiesUtils.class
          .getClassLoader()
          .getResource("/thirdpayConfig/thirdPayConfig.properties")
          .getPath()));
      String typeString = "";
      //environmentType 来判断当前是正式环境还是测试环境
      //智付第三方没有测试环境
      if (THIRDAPP.containsKey("environmentType")) {
        typeString = THIRDAPP.getProperty("environmentType");
      }
      if (typeString.equals("normal")) {

        if (THIRDAPP.containsKey("MerchantID_huicaopay")) {
          merchantID = THIRDAPP.getProperty("MerchantID_huicaopay");
        }
        if (THIRDAPP.containsKey("md5key_huicaopay")) {
          md5key = THIRDAPP.getProperty("md5key_huicaopay");
        }

        if (THIRDAPP.containsKey("privateKey_huicaopay")) {
          privateKey = THIRDAPP.getProperty("privateKey_huicaopay");
        }
        if (THIRDAPP.containsKey("publicKey_huicaopay")) {
          publicKey = THIRDAPP.getProperty("publicKey_huicaopay");
        }
        if (THIRDAPP.containsKey("dinPublicKey_huicaopay")) {
          dinPublicKey = THIRDAPP.getProperty("dinPublicKey_huicaopay");
        }
      } else {
        if (THIRDAPP.containsKey("test.MerchantID_huicaopay")) {
          merchantID = THIRDAPP.getProperty("test.MerchantID_huicaopay");
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
    CommonRequest result = new CommonRequest();
    //设置请求的地址 以及用户包括金额 是为了在请求发送之后保存日志信息

    result.setRequestUser(request.getRequestUser());
    result.setRequestUrl(request.getRequestUrl());
    result.setRequestThirdPay("huicaopay");
    result.setAmount(request.getAmount());

    Huicaopay huicaopay = new Huicaopay();

    huicaopay.setMerNo(merchantID);//商户号
    huicaopay.setAmount(request.getAmount());

    huicaopay.setBillNo(request.getRequestNo()); // 订单号

    System.out.println("request.getBaseUrl()===" + request.getBaseUrl());
    huicaopay.setReturnURL(request.getBaseUrl() + "/pay/thirdpayconfig/html"); // 前台的返回地址

    huicaopay.setAdviceURL(request.getBaseUrl()
        + "/pay/thirdpayconfig/recharge_huicaopay"); // 后台的返回地址

    String md5src;  //加密字符串
    md5src = huicaopay.getMerNo() + "&" + huicaopay.getBillNo() + "&" + huicaopay.getAmount() + "&"
        + huicaopay.getReturnURL() + "&" + md5key;
    Md5Util md5 = new Md5Util();
    String SignInfo; //MD5加密后的字符串
    SignInfo = md5.MD5(md5src);//MD5检验结果

    huicaopay.setSignInfo(SignInfo); // 签名信息

    huicaopay.setOrderTime(
        DateUtil.getFormatDateTime(new Date(), "yyyyMMddHHmmss")); // 请求时间(YYYYMMDDHHMMSS)

    huicaopay.setRemark("用户充值：" + request.getAmount()); // 备注
    huicaopay.setDefaultBankNumber("");
    huicaopay.setProducts("");

    Map<String, String> map = objectToMap(huicaopay);

    try {
      WebClient.operateParameter(response, Huicaopay.RECHARGEURL, map, "UTF-8");
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
   * @author: Zhang Xiaofang
   * @param:    @param request
   * @param:    @return
   * @return: CommonRequest
   * @Date :          2016年9月18日 下午12:00:03
   * @throws:
   */
  public static CommonRequest checkIdentity(CommonRequest request) {
    return null;
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
   * @return: Map<String , String>
   * @Date : 2016年7月19日 下午7:30:08
   * @throws:
   */
  public static Map<String, String> objectToMap(Huicaopay gopay) {

    Map<String, String> map = new HashMap<String, String>();
    Class<?> c = gopay.getClass();

    Method[] method = c.getMethods();

    for (Method m : method) {
      try {
        if (m.getName().startsWith("get")
        ) {

          if (!m.getName().contains("Class")
          ) {
            Object valueString = m.invoke(gopay, null);
            if (null != valueString && !"".equals(valueString)
                && !"null".equals(valueString)) {
              map.put(getPropertiesName(m.getName()),
                  valueString.toString());
              System.out.println(getPropertiesName(m.getName()) + "=" + valueString.toString());
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
        if (!str.equals("sign")
            && !str.equals("sign_type")) {

          if (value == null || "".equals(value)) {
            continue;
          }
          signString.append(str).append("=").append(value).append("&");
        }

      }
      System.out.println("---" + signString);
      sign = signString.substring(0, signString.length() - 1).toString();
      return sign;
    }

    return "";

  }


  /**
   * 加密
   * 加密用的是商户的私钥
   * <p> TODO</p>
   * @author: Zhang Xiaofang
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
      dString = "";
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return dString;

  }


  /**
   * 解密
   * 解密用的是智付的公钥(公钥在商户控台)
   * <p> TODO</p>
   * @author: Zhang Xiaofang
   * @param:    @param data
   * @param:    @return
   * @return: String
   * @Date :          2016年9月2日 下午4:17:31
   * @throws:
   */
  public static boolean RSADecryption(String data, String reciveData) {
    boolean result = false;
    try {
      result = RSAWithSoftware.validateSignByPublicKey(data, dinPublicKey, reciveData);

    } catch (Exception e) {
      result = false;

    }
    return result;

  }


  /**
   * <p> TODO</p>
   * @author: Zhang Xiaofang
   * @param:    @param map
   * @param:    @return
   * @return: CommonRequest
   * @Date :          2016年7月22日 下午5:15:18
   * @throws:
   */
  public static CommonRequest rechargeCallBack(Map<String, Object> map) {
    CommonRequest commonRequest = new CommonRequest();
    commonRequest.setRequestThirdPay("huicaopay");
    String merOrderNum = map.get("BillNo").toString();// 订单号
    commonRequest.setRequestNo(merOrderNum);
    commonRequest.setResponseObj(map.toString());
    //commonRequest.setAmount(map.get("Amount").toString());
    System.out.println("====进入" + map.toString());
    if (map.containsKey("SignMD5info")) {
      String signString = map.get("SignMD5info").toString();

      Md5Util md5 = new Md5Util();
      String md5src = map.get("BillNo").toString() + "&" + map.get("Amount").toString() + "&" + map
          .get("Succeed").toString() + "&" + md5key;
      String md5sign; //MD5加密后的字符串
      md5sign = md5.MD5(md5src);//MD5检验结果

      if (md5sign.equals(signString)) {
        System.out.println("====校验通过");

        String respCode = map.get("Succeed").toString();// 响应码

        if (respCode.equals("88")) {
          commonRequest.setResponseCode("success");
          commonRequest.setResponseMsg("成功");
          commonRequest.setResponseObj("成功");
        } else {
          commonRequest.setResponseCode("faile:" + respCode);
          commonRequest.setResponseMsg("失败:" + map.get("Result").toString());
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

    checkIdentity(null);
  }
}
