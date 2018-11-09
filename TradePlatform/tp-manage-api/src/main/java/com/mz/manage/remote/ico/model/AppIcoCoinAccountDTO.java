/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-17 18:26:08 
 */
package com.mz.manage.remote.ico.model;

import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

/**
 * <p> AppIcoCoinAccount </p>
 * @author:         shangxl
 * @Date :          2017-08-17 18:26:08  
 */
public class AppIcoCoinAccountDTO extends BaseModel {
	
	
	private Long id;  //id
	
	private Integer version;  //版本号
	
	private Long customerId;  //用户id
	
	private BigDecimal hotMoney;  //可用总额
	
	private BigDecimal coldMoney;  //冻结总额
	
	private String userName;  //用户登录名
	
	private String accountNum;  //虚拟账号（证件号+账户类型 0 线上注册用户 1线下注册用户）
	
	private String currencyType;  //虚拟币种（ BTC  LTC ...）
	
	private Integer status;  //账户状态（0 不可用1可用）
	
	private String publicKey;  //公钥
	
	private String privateKey;  //公钥
	
	private BigDecimal lendMoney;  //已借金额
	
	private String coinName;  //虚拟币种汉语名称（ 比特币 莱特币...）
	
	private String coinCode;  //币种（CNY）
	
	private String website;  //中国站标识默认中国站
	
	private BigDecimal psitioNaveragePrice;  //psitioNaveragePrice
	
	private BigDecimal psitioProtectPrice;  //psitioProtectPrice
	
	private BigDecimal sumCost;  //sumCost
	
	private String trueName;  //trueName
	
	private BigDecimal disableMoney;  //禁用
	
	
	private BigDecimal paceFeeRate;//提币手续费率
	
	private BigDecimal leastPaceNum;//单次提币最小数量
	
	private BigDecimal oneDayPaceNum;//一天提币最大数量
	
	
	public BigDecimal getPaceFeeRate() {
		return paceFeeRate;
	}

	public void setPaceFeeRate(BigDecimal paceFeeRate) {
		this.paceFeeRate = paceFeeRate;
	}

	public BigDecimal getLeastPaceNum() {
		return leastPaceNum;
	}

	public void setLeastPaceNum(BigDecimal leastPaceNum) {
		this.leastPaceNum = leastPaceNum;
	}

	public BigDecimal getOneDayPaceNum() {
		return oneDayPaceNum;
	}

	public void setOneDayPaceNum(BigDecimal oneDayPaceNum) {
		this.oneDayPaceNum = oneDayPaceNum;
	}

	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>版本号</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public Integer getVersion() {
		return version;
	}
	
	/**
	 * <p>版本号</p>
	 * @author:  shangxl
	 * @param:   @param version
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>可用总额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public BigDecimal getHotMoney() {
		return hotMoney;
	}
	
	/**
	 * <p>可用总额</p>
	 * @author:  shangxl
	 * @param:   @param hotMoney
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setHotMoney(BigDecimal hotMoney) {
		this.hotMoney = hotMoney;
	}
	
	
	/**
	 * <p>冻结总额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public BigDecimal getColdMoney() {
		return coldMoney;
	}
	
	/**
	 * <p>冻结总额</p>
	 * @author:  shangxl
	 * @param:   @param coldMoney
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setColdMoney(BigDecimal coldMoney) {
		this.coldMoney = coldMoney;
	}
	
	
	/**
	 * <p>用户登录名</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>用户登录名</p>
	 * @author:  shangxl
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>虚拟账号（证件号+账户类型 0 线上注册用户 1线下注册用户）</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getAccountNum() {
		return accountNum;
	}
	
	/**
	 * <p>虚拟账号（证件号+账户类型 0 线上注册用户 1线下注册用户）</p>
	 * @author:  shangxl
	 * @param:   @param accountNum
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	
	
	/**
	 * <p>虚拟币种（ BTC  LTC ...）</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getCurrencyType() {
		return currencyType;
	}
	
	/**
	 * <p>虚拟币种（ BTC  LTC ...）</p>
	 * @author:  shangxl
	 * @param:   @param currencyType
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	
	/**
	 * <p>账户状态（0 不可用1可用）</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * <p>账户状态（0 不可用1可用）</p>
	 * @author:  shangxl
	 * @param:   @param status
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	/**
	 * <p>公钥</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getPublicKey() {
		return publicKey;
	}
	
	/**
	 * <p>公钥</p>
	 * @author:  shangxl
	 * @param:   @param publicKey
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	
	/**
	 * <p>公钥</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getPrivateKey() {
		return privateKey;
	}
	
	/**
	 * <p>公钥</p>
	 * @author:  shangxl
	 * @param:   @param privateKey
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
	/**
	 * <p>已借金额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public BigDecimal getLendMoney() {
		return lendMoney;
	}
	
	/**
	 * <p>已借金额</p>
	 * @author:  shangxl
	 * @param:   @param lendMoney
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setLendMoney(BigDecimal lendMoney) {
		this.lendMoney = lendMoney;
	}
	
	
	/**
	 * <p>虚拟币种汉语名称（ 比特币 莱特币...）</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getCoinName() {
		return coinName;
	}
	
	/**
	 * <p>虚拟币种汉语名称（ 比特币 莱特币...）</p>
	 * @author:  shangxl
	 * @param:   @param coinName
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	
	
	/**
	 * <p>币种（CNY）</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>币种（CNY）</p>
	 * @author:  shangxl
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>中国站标识默认中国站</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>中国站标识默认中国站</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>psitioNaveragePrice</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public BigDecimal getPsitioNaveragePrice() {
		return psitioNaveragePrice;
	}
	
	/**
	 * <p>psitioNaveragePrice</p>
	 * @author:  shangxl
	 * @param:   @param psitioNaveragePrice
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setPsitioNaveragePrice(BigDecimal psitioNaveragePrice) {
		this.psitioNaveragePrice = psitioNaveragePrice;
	}
	
	
	/**
	 * <p>psitioProtectPrice</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public BigDecimal getPsitioProtectPrice() {
		return psitioProtectPrice;
	}
	
	/**
	 * <p>psitioProtectPrice</p>
	 * @author:  shangxl
	 * @param:   @param psitioProtectPrice
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setPsitioProtectPrice(BigDecimal psitioProtectPrice) {
		this.psitioProtectPrice = psitioProtectPrice;
	}
	
	
	/**
	 * <p>sumCost</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public BigDecimal getSumCost() {
		return sumCost;
	}
	
	/**
	 * <p>sumCost</p>
	 * @author:  shangxl
	 * @param:   @param sumCost
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setSumCost(BigDecimal sumCost) {
		this.sumCost = sumCost;
	}
	
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	/**
	 * <p>禁用</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-17 18:26:08    
	 */
	public BigDecimal getDisableMoney() {
		return disableMoney;
	}
	
	/**
	 * <p>禁用</p>
	 * @author:  shangxl
	 * @param:   @param disableMoney
	 * @return:  void 
	 * @Date :   2017-08-17 18:26:08   
	 */
	public void setDisableMoney(BigDecimal disableMoney) {
		this.disableMoney = disableMoney;
	}
	
	

}
