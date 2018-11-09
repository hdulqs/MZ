package com.mz.manage.remote.model;

import java.util.Date;


import com.mz.core.mvc.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

public class User extends BaseModel{
	
	//用户名/手机号
	@ApiModelProperty(value = "用户名/手机号", required = false)
	private String username;
	
	//密码af
	@ApiModelProperty(value = "密码af", required = false)
	private String password;
	
	//用户唯一标识
	@ApiModelProperty(value = "用户唯一标识", required = false)
	private String userCode;
	
	//是否实名0没有实名，1实名
	@ApiModelProperty(value = "是否实名0没有实名，1实名", required = false)
	private  int isReal;  
	
	//是否能交易  0可以交易  1不能交易
	@ApiModelProperty(value = "是否能交易  0可以交易  1不能交易", required = false)
	private  int isChange;  
	
	 //是否禁用  0没有禁用 1禁用
	@ApiModelProperty(value = "是否禁用  0没有禁用 1禁用", required = false)
	private int isDelete; 
	
	 //是否锁定   0没锁定  1锁定
	@ApiModelProperty(value = "是否锁定   0没锁定  1锁定", required = false)
	private int isLock;
	
	//交易密码
	@ApiModelProperty(value = "交易密码", required = false)
	private String accountPassWord;  //交易密码
	
	//前台用户id
	@ApiModelProperty(value = "前台用户id", required = false)
	private Long customerId;
	
	
	//手机号
	@ApiModelProperty(value = "手机号", required = false)
	private String mobile;
	
	//真实名
	@ApiModelProperty(value = "真实名", required = false)
	private String truename;
	//真实姓
	@ApiModelProperty(value = "真实姓", required = false)
	private String surname;
	//`customerType` int(5) DEFAULT '1' COMMENT '客户类型customerType甲类账户1(普通的，默认)，乙类账号2，丙类账户3'
	@ApiModelProperty(value = "客户类型customerType甲类账户1(普通的，默认)，乙类账号2，丙类账户3", required = false)
	private int customerType;
	
	//盐
	@ApiModelProperty(value = "盐", required = false)
	private String salt;
	
	//身份证
	@ApiModelProperty(value = "身份证", required = false)
	private String cardcode;
	
	//邮箱
	@ApiModelProperty(value = "邮箱", required = false)
	private String email;
	
	//性别
	@ApiModelProperty(value = "性别", required = false)
	private Integer sex;
	
	//详细地址
	@ApiModelProperty(value = "详细地址", required = false)
	private String postalAddress;
	
	private String googleKey;
	
	@ApiModelProperty(value = "谷歌认证状态(0否，1是)", required = false)
	private Integer googleState;//谷歌认证状态(0否，1是)
	
	
	private String messIp;
	
	private Date passDate;
	
	
	private String phone;
	
	@ApiModelProperty(value = "手机认证状态(0否，1是)", required = false)
	private Integer phoneState;//手机认证状态(0否，1是)
	
	private Integer states;
	
	private String tokenId;
	
	private String cardCurrency;
	
	private String uncardCurrency;
	
	private String company;
	
	private String uuid;
	
	//新增字段（2018.4.28）
	private Integer checkStates;
	public Integer getCheckStates() {
		return checkStates;
	}

	public void setCheckStates(Integer checkStates) {
		this.checkStates = checkStates;
	}

	//是否展示推荐返佣
	@ApiModelProperty(value = "是否展示推荐返佣", required = false)
	private Integer commend;
	
	//国家地址字段
	@ApiModelProperty(value = "国家地址字段", required = false)
	private String country;


	//第三方用户名
	@ApiModelProperty(value = "第三方用户名", required = false)
	private String thirdUserName;

	//第三方平台
	@ApiModelProperty(value = "第三方平台", required = false)
	private String platform;

	//第三方用户密码
	@ApiModelProperty(value = "第三方用户密码", required = false)
	private String thirdUserPw;

	private Integer openOtcStates;


	private Integer mailStates; //绑定邮箱标识 0 未绑定 1 绑定


	private String mail; //绑定的邮箱

	public Integer getMailStates() {
		return mailStates;
	}

	public void setMailStates(Integer mailStates) {
		this.mailStates = mailStates;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getOpenOtcStates() {
		return openOtcStates;
	}

	public void setOpenOtcStates(Integer openOtcStates) {
		this.openOtcStates = openOtcStates;
	}

	public String getThirdUserName() {
		return thirdUserName;
	}

	public void setThirdUserName(String thirdUserName) {
		this.thirdUserName = thirdUserName;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getThirdUserPw() {
		return thirdUserPw;
	}

	public void setThirdUserPw(String thirdUserPw) {
		this.thirdUserPw = thirdUserPw;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getPhone_googleState(){
		
		if(this.phoneState ==1 && this.googleState == 1){
			return 1;
		}
		return 0;
	}

	public Integer getCommend() {
		return commend;
	}

	public void setCommend(Integer commend) {
		this.commend = commend;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCardCurrency() {
		return cardCurrency;
	}

	public void setCardCurrency(String cardCurrency) {
		this.cardCurrency = cardCurrency;
	}

	public String getUncardCurrency() {
		return uncardCurrency;
	}

	public void setUncardCurrency(String uncardCurrency) {
		this.uncardCurrency = uncardCurrency;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getPhoneState() {
		return phoneState;
	}

	public void setPhoneState(Integer phoneState) {
		this.phoneState = phoneState;
	}

	public Date getPassDate() {
		return passDate;
	}

	public void setPassDate(Date passDate) {
		this.passDate = passDate;
	}

	public String getMessIp() {
		return messIp;
	}

	public void setMessIp(String messIp) {
		this.messIp = messIp;
	}

	public Integer getGoogleState() {
		return googleState;
	}

	public void setGoogleState(Integer googleState) {
		this.googleState = googleState;
	}

	public String getGoogleKey() {
		return googleKey;
	}

	public void setGoogleKey(String googleKey) {
		this.googleKey = googleKey;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCardcode() {
		return cardcode;
	}

	public void setCardcode(String cardcode) {
		this.cardcode = cardcode;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public int getCustomerType() {
		return customerType;
	}

	public void setCustomerType(int customerType) {
		this.customerType = customerType;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getUsername() {
//		return username.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getIsReal() {
		return isReal;
	}

	public void setIsReal(int isReal) {
		this.isReal = isReal;
	}

	public int getIsChange() {
		return isChange;
	}

	public void setIsChange(int isChange) {
		this.isChange = isChange;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getAccountPassWord() {
		return accountPassWord;
	}

	public void setAccountPassWord(String accountPassWord) {
		this.accountPassWord = accountPassWord;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}
	
	
	
	
	
	

}
