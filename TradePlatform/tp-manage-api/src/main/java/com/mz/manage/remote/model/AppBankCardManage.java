/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月31日 下午6:17:33
 */
package com.mz.manage.remote.model;

import java.io.Serializable;
import java.util.Date;



import com.mz.core.mvc.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "银行卡信息", description = "银行卡信息")
public class AppBankCardManage extends BaseModel{
	
	private Long id;
    @ApiModelProperty(value = "账户ID", required = false)
	private Long accountId;  //账户ID
    @ApiModelProperty(value = "用户id", required = false)
	private Long customerId;   //用户id
    @ApiModelProperty(value = "用户名", required = false)
	private String userName;  //用户名
	//名
    @ApiModelProperty(value = "名", required = false)
	private String trueName;
	//姓
    @ApiModelProperty(value = "姓", required = false)
	private String surName;
  //姓
    @ApiModelProperty(value = "货币类型", required = false)
	private String currencyType;  //货币类型
    @ApiModelProperty(value = "银行卡持有人", required = false)
	private String cardName; // 银行卡持有人

    @ApiModelProperty(value = "银行卡号", required = false)   
	private String cardNumber; //银行卡号
    
    @ApiModelProperty(value = "开户银行", required = false)
	private String cardBank;  //开户银行
	
    @ApiModelProperty(value = "开户行所在地  开户市", required = false)
	private String bankAddress;  //开户行所在地  开户市
	
    @ApiModelProperty(value = "开户支行", required = false)
	private String subBank;  //开户支行
	
    @ApiModelProperty(value = "开户支行银行机构代码", required = false)
	private String subBankNum;  //开户支行银行机构代码
	
    @ApiModelProperty(value = "站点类别默认cn", required = false)
	private String website;//站点类别默认cn

    @ApiModelProperty(value = "开户省份", required = false)
	private String bankProvince;  //开户省份
	
    @ApiModelProperty(value = "签约银行通道", required = false)
	private String signBank;  //签约银行通道
	
    @ApiModelProperty(value = "微信照片", required = false)
   	private String weChatPicture;  //微信照片     add by zongwei  20180511
	
    @ApiModelProperty(value = "支付宝照片", required = false)
   	private String alipayPicture;  //支付宝照片     add by zongwei 20180511
	
    @ApiModelProperty(value = "微信", required = false)
   	private String weChat;  //微信照片     add by zongwei 20180511
    
    @ApiModelProperty(value = "支付宝", required = false)
   	private String alipay;  //支付宝照片     add by zongwei 20180511
    
    @ApiModelProperty(value = "开户省份key", required = false)
	private String bankProvinceKey;  //开户省份key  add by zongwei 20180514
	
    public String getBankProvinceKey() {
		return bankProvinceKey;
	}

	public void setBankProvinceKey(String bankProvinceKey) {
		this.bankProvinceKey = bankProvinceKey;
	}

	public String getWeChatPicture() {
		return weChatPicture;
	}

	public void setWeChatPicture(String weChatPicture) {
		this.weChatPicture = weChatPicture;
	}

	public String getAlipayPicture() {
		return alipayPicture;
	}

	public void setAlipayPicture(String alipayPicture) {
		this.alipayPicture = alipayPicture;
	}

	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	
	
	

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getSubBankNum() {
		return subBankNum;
	}

	public void setSubBankNum(String subBankNum) {
		this.subBankNum = subBankNum;
	}

	public String getSignBank() {
		return signBank;
	}

	public void setSignBank(String signBank) {
		this.signBank = signBank;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBankProvince() {
		return bankProvince;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getId() {
		return id;
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
	 * @return:     Long
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCurrencyType() {
		return currencyType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getAccountId() {
		return accountId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCardName() {
		return cardName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCardBank() {
		return cardBank;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBankAddress() {
		return bankAddress;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSubBank() {
		return subBank;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getUserName() {
		return userName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}


}
