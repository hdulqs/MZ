/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月5日 下午5:39:18
 */
package com.mz.thirdpay;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Zhang Xiaofang
 * @Date : 2016年7月5日 下午5:39:18
 */

@Table(name="app_log_thirdpay")
public class AppLogThirdPay extends BaseModel{
     
	
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id",unique=true,nullable=false)
	private Long id;
    
    @Column(name="userId")
	private Long userId;
    
    @Column(name="thirdPayType")
	private String thirdPayType;
    
    @Column(name="thirdPayConfig")
    private String thirdPayConfig;
    
    @Column(name="money")
	private String money;
    
    @Column(name="recordNum")
	private String recordNum;
    
    @Column(name="requestTime")
	private String requestTime;
    
    @Column(name="requestNum")
	private String requestNum;
    
    @Column(name="returnTime")
	private String returnTime;
    
    @Column(name="responseCode")
	private String responseCode;
    
    @Column(name="responseMsg")
	private String responseMsg;
    
    @Column(name="remark1")
	private String remark1;
    
    @Column(name="remark2")
	private String remark2;
    
    @Column(name="remark3")
	private String remark3;
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getId() {
		return id;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getThirdPayType() {
		return thirdPayType;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getThirdPayConfig() {
		return thirdPayConfig;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMoney() {
		return money;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRecordNum() {
		return recordNum;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRequestTime() {
		return requestTime;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRequestNum() {
		return requestNum;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getReturnTime() {
		return returnTime;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getResponseCode() {
		return responseCode;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getResponseMsg() {
		return responseMsg;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRemark1() {
		return remark1;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRemark2() {
		return remark2;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRemark3() {
		return remark3;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setThirdPayType(String thirdPayType) {
		this.thirdPayType = thirdPayType;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setThirdPayConfig(String thirdPayConfig) {
		this.thirdPayConfig = thirdPayConfig;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMoney(String money) {
		this.money = money;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRequestNum(String requestNum) {
		this.requestNum = requestNum;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

}
