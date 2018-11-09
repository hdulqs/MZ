/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月8日 下午5:33:54
 */
package com.mz.thirdpay;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月8日 下午5:33:54 
 */
@Table(name="app_thirdpay_config")
public class AppThirdPayConfig  extends BaseModel{

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id",unique=true,nullable=false)
	private Long id;
	
	
	@Column(name="thirdPayConfig")
	private String thirdPayConfig;
	
	@Column(name="thirdPayName")
	private String thirdPayName;
	
	@Column(name="thirdPayType")
	private String thirdPayType;
	
	@Column(name="thirdPayEnvironment")
	private String thirdPayEnvironment;
	
	@Column(name="platformId")
	private String platformId;
	
	@Column(name="platAcctId")
	private String platAcctId;
	
	@Column(name="privKey")
	private String privKey;
	
	@Column(name="pubKey")
	private  String pubKey;
	
	@Column(name="password")
	private String password;
	
	@Column(name="currentThird")
	private String currentThird;
	
	@Column(name="propertiesURL")
	private String propertiesURL;
	
	@Column(name="testPropertiesURL")
	private String testPropertiesURL;
	
	@Column(name="isOpen")
	private String isOpen;
	
	
	@Column(name="remark1")
	private String remark1;
	
	@Column(name="remark2")
	private  String remark2;
	
	@Column(name="remark3")
	private  String  remark3;
	
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getThirdPayName() {
		return thirdPayName;
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
	public String getThirdPayEnvironment() {
		return thirdPayEnvironment;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPlatformId() {
		return platformId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPlatAcctId() {
		return platAcctId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPrivKey() {
		return privKey;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPubKey() {
		return pubKey;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCurrentThird() {
		return currentThird;
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
	 * @return: String
	 */
	public void setThirdPayName(String thirdPayName) {
		this.thirdPayName = thirdPayName;
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
	public void setThirdPayEnvironment(String thirdPayEnvironment) {
		this.thirdPayEnvironment = thirdPayEnvironment;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPlatAcctId(String platAcctId) {
		this.platAcctId = platAcctId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPrivKey(String privKey) {
		this.privKey = privKey;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCurrentThird(String currentThird) {
		this.currentThird = currentThird;
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


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getThirdPayConfig() {
		return thirdPayConfig;
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
	 * @return:     String
	 */
	public String getPropertiesURL() {
		return propertiesURL;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTestPropertiesURL() {
		return testPropertiesURL;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPropertiesURL(String propertiesURL) {
		this.propertiesURL = propertiesURL;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTestPropertiesURL(String testPropertiesURL) {
		this.testPropertiesURL = testPropertiesURL;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsOpen() {
		return isOpen;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

}
