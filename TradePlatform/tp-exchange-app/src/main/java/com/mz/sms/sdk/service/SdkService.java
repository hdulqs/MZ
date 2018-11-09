/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年5月18日 下午2:24:46
 */
package com.mz.sms.sdk.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.sms.send.model.AppSmsSend;
import java.io.IOException;
import java.util.Map;


/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2016年5月18日 下午2:24:46 
 */
public interface SdkService {

  /**
   *
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param appSmsSend  短信记录
   * @param:    @param mobilePhone 手机号
   * @param:    @param code        验证码
   * @param:    @param sendType    短信模板
   * @param:    @return
   * @return: boolean
   * @Date :          2016年5月18日 下午2:32:40
   * @throws:
   */
  public boolean sendSms(AppSmsSend appSmsSend, SmsParam smsParam, String phone);


  /**
   * <p>验证身份证</p>
   * @author: Liu Shilei
   * @param:    @param name
   * @param:    @param idCard
   * @param:    @return
   * @return: boolean
   * @Date :          2016年9月7日 下午9:47:44
   * @throws:
   */
  public JsonResult checkCard(String name, String idCard);


  public boolean sendSmsHai(AppSmsSend appSmsSend, SmsParam smsParam, String Phone)
      throws IOException;

  /**
   * @Description: 根据系统代码获取短信模板发送短信
   * @Author: zongwei
   * @CreateDate: 2018/6/11 11:31
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/6/11 11:31
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  public boolean sendSmsInfo(AppSmsSend appSmsSend, SmsParam smsParam, String phone, String syscode,
      Map<String, Object> map);


}
