/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月4日 下午7:02:02
 */
package com.mz.utils;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p> TODO</p>
 * @author: Zhang Xiaofang
 * @Date :          2016年7月4日 下午7:02:02 
 */
@Getter
@Setter
public class CommonRequest implements Serializable {


  /*
   * 流水号
   */
  public String requestNo;


  /*
   * 查询订单流水号
   */
  public String queryOrderNo;

  /*
   * 查询订单日期
   */
  public String queryOrderDate;


  /*
   * 金额
   */
  public String amount;

  /*
   * 手续费
   */

  public String fee;


  public String baseUrl;

  /*
   * 开户人姓名
   */
  public String bankAcctName;


  /*
   * 开户省份
   */
  public String bankProvince;


  /*
   * 开户城市
   */
  public String bankCity;


  /*
   * 开户行
   */
  public String bankName;

  /*
   * 开户支行
   */
  public String bankBranchName;

  /*
   * 银行卡号
   */
  public String bankAccNum;


  /*
   * 用户浏览器IP
   */
  public String userBrowerIP;

  /*
   * 用户交易时间
   */
  public String transactionDateTime;


  /*
   *响应码
   */
  public String responseCode;

  /*
   *响应码描述
   */
  public String responseMsg;

  /*
   *响应信息
   */
  public String responseObj;

  /*
   *用户
   */
  public String requestUser;

  /*
   *请求地址
   */
  public String requestUrl;

  /*
   *第三方类型
   */
  public String requestThirdPay;

  /*
   * 真实姓名
   */
  public String trueName;


  /*
   *身份证
   */
  public String idCard;

  //服务名称
  private String serviceName;
  //版本号
  private String version = "V1";
  //平台标示
  private String platform;
  //商户号
  private String merchantId;
  //支付类型
  private String payType;
  //参数编码字符集
  private String charset = "UTF-8";
  //商户订单号
  private String merOrderId;
  //币种
  private String currency;
  //通知URL
  private String notifyUrl;
  //返回URL
  private String returnUrl;
  //产品名称
  private String productName;
  //产品描述
  private String productDesc;
  //交易金额
  private String tranAmt;
  //交易时间
  private String tranTime;
  //银行卡类标识
  private String bankCardType;
  //银行编码
  private String bankCode;
  //客户端IP
  private String clientIp;
  //付款人标识
  private String payerId;

  //****************************************微信/支付宝参数*********************************************
  private String type;//1微信 2支付宝

  //请求渠道
  private String reqChannel;
  //用户标识
  private String openid;
  //子用户标识
  private String subOpenid;
  //交易类型
  private String mergeType;
  //消费金额
  private String tradeAmt;
  //商户订单编号
  private String merchOrderNo;
  //商品的标题
  private String subject;
  //商品描述
  private String body;
  //用户备注
  private String userNote;
  //否使用信用卡
  private String limitCreditPay;

  //************************8快捷（*****************************
  //银行卡号
  private String bankCardNo;
  //证件类型
  private String idType;
  //身份证号
  private String idNo;
  //手机号
  private String mobileNo;
  //短信验证码
  private String verifyCode;


}
