package com.mz.spotchange.account.model;

import com.mz.core.mvc.model.BaseModel;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@SuppressWarnings("serial")
@Table(name = "sp_position_account")
public class SpPositionAccount extends BaseModel {

	// 主键
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	// 版本字段
	@Version()
	private Integer version;
	// 用户id
	@Column(name = "customerId")
	private Long customerId;
	// 持仓数量负数是卖仓，正数是买仓
	@Column(name = "positionCount")
	private BigDecimal positionCount;
	//预冻结保证金
	@Column(name = "coldMoney")
	private BigDecimal coldMoney;
	// 持仓保证金
	@Column(name = "posiDepositMoney")
	private BigDecimal posiDepositMoney;
	// 用户登录名
	@Column(name = "userName")
	private String userName;
	// 用户登录名
	@Column(name = "trueName")
	private String trueName;
	//交易账户类型 cny,usd
	@Column(name = "currencyType")
	private String currencyType;
	
	//虚拟币种类型 btc ,ltc ,eth
	@Column(name = "coinCode")
	private String coinCode;
	
	//站点类别 en ,cn
	@Column(name = "website")
	private String website;
	

	// 账户状态（0 不可用1可用）
	@Column(name = "status")
	private Integer status;

	
	
	
	@Column(name = "psitioNaveragePrice")
	private BigDecimal psitioNaveragePrice;
	@Column(name = "psitioProtectPrice")
	private BigDecimal psitioProtectPrice;
	@Column(name = "sumCost")
	private BigDecimal sumCost;
	@Transient
	private AppPersonInfo appPersonInfo;
	
	
	
	
	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getPositionCount() {
		return positionCount;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setPositionCount(BigDecimal positionCount) {
		this.positionCount = positionCount;
	}


	

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTrueName() {
		return trueName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
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
	 * @return:     AppPersonInfo
	 */
	public AppPersonInfo getAppPersonInfo() {
		return appPersonInfo;
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

	/** 
	 * <p> TODO</p>
	 * @return: AppPersonInfo
	 */
	public void setAppPersonInfo(AppPersonInfo appPersonInfo) {
		this.appPersonInfo = appPersonInfo;
	}



	


	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getPosiDepositMoney() {
		return posiDepositMoney;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setPosiDepositMoney(BigDecimal posiDepositMoney) {
		this.posiDepositMoney = posiDepositMoney;
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

	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public SpPositionAccount(Long id, Integer version, Long customerId,
			BigDecimal hotMoney, BigDecimal coldMoney, String userName,
			String currencyType, String accountNum, Integer status,
			String publicKey, String privateKey) {
		super();
		this.id = id;
		this.version = version;
		this.customerId = customerId;
		this.coldMoney = coldMoney;
		this.userName = userName;
		this.currencyType = currencyType;
		this.status = status;
	}

	public SpPositionAccount() {
		super();
	}

	@Override
	public String toString() {
		return "SpPositionAccount [id=" + id + ", version=" + version
				+ ", customerId=" + customerId 
				+ ", coldMoney=" + coldMoney + ", userName=" + userName
				+ ", currencyType=" + currencyType + ", status=" + status  + "]";
	}

}
