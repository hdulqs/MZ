/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月4日 下午7:02:19
 */
package com.mz.utils;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p> TODO</p>
 * @author: Zhang Xiaofang
 * @Date :          2016年7月4日 下午7:02:19 
 */
@Getter
@Setter
public class CommonResponse {

  private Boolean success;

  private String msg;

  private Object obj = null;// 返回其他对象信息


  //商户号
  private String merchantId;
  //平台标示
  private String platform;
  //返回码
  private String retCode;
  //返回消息
  private String retMsg;
  //商户交易订单号
  private String merOrderId;
  //交易金额
  private BigDecimal tranAmt;
  //商户号
  private Integer tradeStatus;
  //创新支付交易号
  private String tranNo;
  //交易创建时间
  private String tranTime;
  //渠道第三方订单号
  private String chnOutOrderNo;
  //渠道订单号
  private String chnOrderNo;
  //渠道处理日期
  private String chnOrderDate;
  //支付方式
  private String payType;
}
