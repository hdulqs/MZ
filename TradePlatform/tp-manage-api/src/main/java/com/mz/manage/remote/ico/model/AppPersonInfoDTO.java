/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zhangcb
 * @version:     V1.0 
 * @Date:        2016-11-22 18:25:51 
 */
package com.mz.manage.remote.ico.model;

import com.mz.core.mvc.model.BaseModel;

import java.util.Date;

/**
 * 
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年7月20日 下午2:39:38
 */
public class AppPersonInfoDTO extends BaseModel {
	
	/**  
	 * @Fields : TODO   
	 */
	private static final long serialVersionUID = 1L;

	private Long id;  //id
	
	private Long customerId;  //用户ID
	
	private Integer customerType;  //客户类型customerType甲类账户1(普通的，默认)，乙类账号2，丙类账户3
	
	private String mobilePhone;  //手机号
	
	private String email;  //邮箱
	
	private String trueName;  //真实姓名
	
	private Integer sex;  //性别  0男  1女
	
	private String birthday;  //生日
	
	private String country;  //国家
	
	private Integer cardType;  //证件类型
	
	private String cardId;  //身份证号
	
	private Integer customerSource;  //客户来源   0线上注册   1业务员注册
	
	private Date realTime;  //realTime
	
	private String cardIdUsd;  //cardIdUsd
	
	private String cardIdValidPeriod;  //身份证有效期
	
	private String postalAddress;  //通讯地址
	
	private String contacts;  //紧急联系人
	
	private Integer postCode;  //邮编
	
	private Integer stage;  //基础信息完善状态
	
	private String contactsTel;  //紧急联系人电话
	
	private String handIdCardUrl;  //手持身份证照片
	
	private String idCardFrontUrl;  //身份证正面照片
	
	private String idCardBackUrl;  //身份证背面照片
	
	private String handDealUrl;  //手持协议照片
	
	private Integer isExamine;  //审核状态 0=未审核 1=审核通过、2=审核不通过

	private String eamineRefusalReason;  //审核拒绝原因
	
	private Integer isCancle;  //是否注销 0、未注销  1、注销
	
	private String cancleReason;  //注销原因

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Integer getCustomerSource() {
		return customerSource;
	}

	public void setCustomerSource(Integer customerSource) {
		this.customerSource = customerSource;
	}

	public Date getRealTime() {
		return realTime;
	}

	public void setRealTime(Date realTime) {
		this.realTime = realTime;
	}

	public String getCardIdUsd() {
		return cardIdUsd;
	}

	public void setCardIdUsd(String cardIdUsd) {
		this.cardIdUsd = cardIdUsd;
	}

	public String getCardIdValidPeriod() {
		return cardIdValidPeriod;
	}

	public void setCardIdValidPeriod(String cardIdValidPeriod) {
		this.cardIdValidPeriod = cardIdValidPeriod;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Integer getPostCode() {
		return postCode;
	}

	public void setPostCode(Integer postCode) {
		this.postCode = postCode;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public String getContactsTel() {
		return contactsTel;
	}

	public void setContactsTel(String contactsTel) {
		this.contactsTel = contactsTel;
	}

	public String getHandIdCardUrl() {
		return handIdCardUrl;
	}

	public void setHandIdCardUrl(String handIdCardUrl) {
		this.handIdCardUrl = handIdCardUrl;
	}

	public String getIdCardFrontUrl() {
		return idCardFrontUrl;
	}

	public void setIdCardFrontUrl(String idCardFrontUrl) {
		this.idCardFrontUrl = idCardFrontUrl;
	}

	public String getIdCardBackUrl() {
		return idCardBackUrl;
	}

	public void setIdCardBackUrl(String idCardBackUrl) {
		this.idCardBackUrl = idCardBackUrl;
	}

	public String getHandDealUrl() {
		return handDealUrl;
	}

	public void setHandDealUrl(String handDealUrl) {
		this.handDealUrl = handDealUrl;
	}

	public Integer getIsExamine() {
		return isExamine;
	}

	public void setIsExamine(Integer isExamine) {
		this.isExamine = isExamine;
	}

	public String getEamineRefusalReason() {
		return eamineRefusalReason;
	}

	public void setEamineRefusalReason(String eamineRefusalReason) {
		this.eamineRefusalReason = eamineRefusalReason;
	}

	public Integer getIsCancle() {
		return isCancle;
	}

	public void setIsCancle(Integer isCancle) {
		this.isCancle = isCancle;
	}

	public String getCancleReason() {
		return cancleReason;
	}

	public void setCancleReason(String cancleReason) {
		this.cancleReason = cancleReason;
	}


}
