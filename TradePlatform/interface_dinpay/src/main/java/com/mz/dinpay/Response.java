/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年9月19日 上午10:51:49
 */
package com.mz.dinpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年9月19日 上午10:51:49 
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {

	
	 //状态  0 通过  1 不通过  2 处理中
    private String  status;
    
    //签名
  	private String  sign;
  	
  	
  	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSign() {
		return sign;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getInformation() {
		return information;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getReal_name() {
		return real_name;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setInformation(String information) {
		this.information = information;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}


	private String 	information;
  	
  	
  	private String real_name;
}
