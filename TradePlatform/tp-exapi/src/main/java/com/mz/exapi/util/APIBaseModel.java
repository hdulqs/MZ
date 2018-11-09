/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月16日 上午11:24:10
 */
package com.mz.exapi.util;

import javax.validation.constraints.NotNull;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月16日 上午11:24:10 
 */
public class APIBaseModel {
	  //签名顺序就按正常字段先后顺序，private不参加签名
	  @NotNull
	  public String method;
	  @NotNull 
	  public String accesskey;
	  @NotNull 
	  public String reqTime;
	  @NotNull
	  private String sign;  //不参加签名
	  
	  
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getAccesskey() {
		return accesskey;
	}
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
	  
	  
}
