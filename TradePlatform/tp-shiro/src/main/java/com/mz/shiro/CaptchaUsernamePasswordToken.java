/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年10月13日 下午4:56:26
 */
package com.mz.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2015年10月13日 下午4:56:26 
 */

public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {  
  //验证码字符串  
  private String captcha;  
  
  //租户账户前缀
  private String appuserprefix;
  
  
  public CaptchaUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha,String appuserprefix) {  
      super(username, password, rememberMe, host);  
      this.captcha = captcha;  
      this.appuserprefix = appuserprefix;
  }  

  public String getCaptcha() {  
      return captcha;  
  }  

  public void setCaptcha(String captcha) {  
      this.captcha = captcha;  
  }

/**
 * <p> TODO</p>
 * @return:     String
 */
public String getAppuserprefix() {
	return appuserprefix;
}

/** 
 * <p> TODO</p>
 * @return: String
 */
public void setAppuserprefix(String appuserprefix) {
	this.appuserprefix = appuserprefix;
}  
  
  
  
}  
