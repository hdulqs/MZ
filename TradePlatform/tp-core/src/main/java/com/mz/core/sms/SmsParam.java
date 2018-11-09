/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年9月6日 下午4:01:38
 */
package com.mz.core.sms;

import com.alibaba.fastjson.JSON;
import com.mz.util.properties.PropertiesUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>短信参数</p>
 *
 * @author: Liu Shilei
 * @Date :          2016年9月6日 下午4:01:38
 */
@Getter
@Setter
public class SmsParam {

  private Long sendId;//发送ID
  private String smsKey;//内部系统调用认证Key;
  private String hryCode;//短信验证码
  private String hryMobilephone;//接收人的手机号
  private String hrySmstype;//短信类型     注册，找回密码.....

  public SmsParam() {//初始化smsKey
    this.smsKey = PropertiesUtils.APP.getProperty("app.smsKey");
  }

  /**
   * 转JSON
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @return
   * @return: String
   * @Date :          2016年9月6日 下午4:18:25
   * @throws:
   */
  public String toJson() {
    return JSON.toJSONString(this);
  }
}
