package com.mz.coin.mobilecoin.model;



import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Table(name = "mobile_customer")
public class MobileCustomer extends BaseModel {
	// 主键
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name="accountName")
	private String accountName;  //生成币的用户名（唯一）
	
	@Column(name="coinAddr")
	private String coinAddr;  //币地址
	
	@Column(name="passWord")
	private String passWord;  //密码
	
	@Column(name="mobileId")
	private String mobileId;  //手机唯一标识
	
	@Column(name="coinType")
	private String coinType;  //币种类型
	
	@Column(name="accountPassWord")
	private String accountPassWord;  //交易密码
	
	@Column(name="hotCurrency")
	private BigDecimal hotCurrency;//可用币
	
	@Column(name="coldCurrency")
	private BigDecimal coldCurrency;//冻结币

	
	
	
	
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCoinType() {
		return coinType;
	}

	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoinAddr() {
		return coinAddr;
	}

	public void setCoinAddr(String coinAddr) {
		this.coinAddr = coinAddr;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getAccountPassWord() {
		return accountPassWord;
	}

	public void setAccountPassWord(String accountPassWord) {
		this.accountPassWord = accountPassWord;
	}

	public BigDecimal getHotCurrency() {
		return hotCurrency;
	}

	public void setHotCurrency(BigDecimal hotCurrency) {
		this.hotCurrency = hotCurrency;
	}

	public BigDecimal getColdCurrency() {
		return coldCurrency;
	}

	public void setColdCurrency(BigDecimal coldCurrency) {
		this.coldCurrency = coldCurrency;
	}
	

}
