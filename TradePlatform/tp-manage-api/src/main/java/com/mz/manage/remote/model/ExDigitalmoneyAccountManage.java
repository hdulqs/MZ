package com.mz.manage.remote.model;


import java.math.BigDecimal;

import com.mz.core.mvc.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;


public class ExDigitalmoneyAccountManage extends BaseModel {

	// 主键	
	private Long id;
	// 版本字段
	@ApiModelProperty(value = "版本字段", required = false)
	private Integer version;
	// 用户id
	@ApiModelProperty(value = "用户id", required = false)
	private Long customerId;
	// 可用总额
	@ApiModelProperty(value = "可用总额", required = false)
	private BigDecimal hotMoney;
	// 冻结总额
	@ApiModelProperty(value = "冻结总额", required = false)
	private BigDecimal coldMoney;
	// 用户登录名
	@ApiModelProperty(value = "用户登录名", required = false)
	private String userName;
	
	@ApiModelProperty(value = "持卡人", required = false)
	private String trueName; //持卡人
	
	//交易账户类型 cny,usd
	@ApiModelProperty(value = "交易账户类型 cny,usd", required = false)
	private String currencyType;
	
	//虚拟币种类型 btc ,ltc ,eth
	@ApiModelProperty(value = "虚拟币种类型 btc ,ltc ,eth", required = false)
	private String coinCode;
	
	//站点类别 en ,cn
	@ApiModelProperty(value = "站点类别 en ,cn", required = false)
	private String website;
	
	// 虚拟币种名称（比特币  莱特币 ...）
	@ApiModelProperty(value = "虚拟币种名称（比特币  莱特币 ...）", required = false)
	private String coinName;
	
	// 虚拟账号（证件号+账户类型 0 线上注册用户 1线下注册用户）
	@ApiModelProperty(value = "虚拟账号（证件号+账户类型 0 线上注册用户 1线下注册用户）", required = false)
	private String accountNum;
	// 账户状态（0 不可用1可用）
	@ApiModelProperty(value = "账户状态（0 不可用1可用）", required = false)
	private Integer status;
	// 公钥
	@ApiModelProperty(value = "公钥", required = false)
	private String publicKey;
	// 私钥
	@ApiModelProperty(value = "私钥", required = false)
	private String privateKey;
	// 借款总额
	@ApiModelProperty(value = "借款总额", required = false)
	private BigDecimal lendMoney;
	// 禁用
	@ApiModelProperty(value = "禁用", required = false)
	private BigDecimal disableMoney;
	
	
	private BigDecimal psitioNaveragePrice;
	private BigDecimal psitioProtectPrice;
	private BigDecimal sumCost;
	
	@ApiModelProperty(value = "持有均价", required = false)
	private BigDecimal positionAvePrice;  //持有均价
	
	@ApiModelProperty(value = "浮动盈亏", required = false)
	private  BigDecimal floatprofitandlossMoney;//浮动盈亏
	
	@ApiModelProperty(value = "浮动盈亏率", required = false)
	private  BigDecimal floatprofitandlossMoneyRate;//浮动盈亏
	
	@ApiModelProperty(value = "提币手续费率", required = false)
	private BigDecimal paceFeeRate;//提币手续费率
	
	@ApiModelProperty(value = "单次提币最小数量", required = false)
	private BigDecimal leastPaceNum;//单次提币最小数量
	
	@ApiModelProperty(value = "一天提币最大数量", required = false)
	private BigDecimal oneDayPaceNum;//一天提币最大数量
	
	
	private String paceType;
	
	private String paceCyrrcey;
	
	
	
	
	
	public String getPaceCyrrcey() {
		return paceCyrrcey;
	}

	public void setPaceCyrrcey(String paceCyrrcey) {
		this.paceCyrrcey = paceCyrrcey;
	}

	public String getPaceType() {
		return paceType;
	}

	public void setPaceType(String paceType) {
		this.paceType = paceType;
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
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getFloatprofitandlossMoney() {
		return floatprofitandlossMoney;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setFloatprofitandlossMoney(BigDecimal floatprofitandlossMoney) {
		this.floatprofitandlossMoney = floatprofitandlossMoney;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getFloatprofitandlossMoneyRate() {
		return floatprofitandlossMoneyRate;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setFloatprofitandlossMoneyRate(
			BigDecimal floatprofitandlossMoneyRate) {
		this.floatprofitandlossMoneyRate = floatprofitandlossMoneyRate;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getPositionAvePrice() {
		return positionAvePrice;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setPositionAvePrice(BigDecimal positionAvePrice) {
		this.positionAvePrice = positionAvePrice;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getSumCost() {
		return sumCost;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setSumCost(BigDecimal sumCost) {
		this.sumCost = sumCost;
	}


	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getPsitioNaveragePrice() {
		return psitioNaveragePrice;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setPsitioNaveragePrice(BigDecimal psitioNaveragePrice) {
		this.psitioNaveragePrice = psitioNaveragePrice;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getPsitioProtectPrice() {
		return psitioProtectPrice;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setPsitioProtectPrice(BigDecimal psitioProtectPrice) {
		this.psitioProtectPrice = psitioProtectPrice;
	}

	public BigDecimal getLendMoney() {
		return lendMoney;
	}

	public void setLendMoney(BigDecimal lendMoney) {
		this.lendMoney = lendMoney;
	}

	


	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getHotMoney() {
		return hotMoney;
	}

	public void setHotMoney(BigDecimal hotMoney) {
		this.hotMoney = hotMoney;
	}

	public BigDecimal getColdMoney() {
		return coldMoney;
	}

	public void setColdMoney(BigDecimal coldMoney) {
		this.coldMoney = coldMoney;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public BigDecimal getDisableMoney() {
		return disableMoney;
	}

	public void setDisableMoney(BigDecimal disableMoney) {
		this.disableMoney = disableMoney;
	}

	public ExDigitalmoneyAccountManage(Long id, Integer version, Long customerId,
			BigDecimal hotMoney, BigDecimal coldMoney, String userName,
			String currencyType, String accountNum, Integer status,
			String publicKey, String privateKey) {
		super();
		this.id = id;
		this.version = version;
		this.customerId = customerId;
		this.hotMoney = hotMoney;
		this.coldMoney = coldMoney;
		this.userName = userName;
		this.currencyType = currencyType;
		this.accountNum = accountNum;
		this.status = status;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public ExDigitalmoneyAccountManage() {
		super();
	}

	public BigDecimal getPaceFeeRate() {
		return paceFeeRate;
	}

	public void setPaceFeeRate(BigDecimal paceFeeRate) {
		this.paceFeeRate = paceFeeRate;
	}
	
	
}
