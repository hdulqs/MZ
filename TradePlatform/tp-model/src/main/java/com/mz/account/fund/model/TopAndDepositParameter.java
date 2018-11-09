/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年6月21日 上午10:24:17
 */
package com.mz.account.fund.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mz.core.mvc.model.BaseModel;

/**
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年6月21日 上午10:24:17 
 */
@SuppressWarnings("serial")
@Table(name="topAndDeposit_parameter")
public class TopAndDepositParameter extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name="currencyCode")
	private String currencyCode;
	
	// 具体指哪个充值还是提现
	@Column(name="type")
	private Integer type;
	
	// 费率值
	@Column(name="feeRate")
	private BigDecimal feeRate;
	
	// 状态值
	@Column(name="sates")
	private Integer sates;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}
	public Integer getSates() {
		return sates;
	}
	public void setSates(Integer sates) {
		this.sates = sates;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public TopAndDepositParameter(Long id, Integer type, BigDecimal feeRate,
			Integer sates) {
		super();
		this.id = id;
		this.type = type;
		this.feeRate = feeRate;
		this.sates = sates;
	}
	
	public TopAndDepositParameter() {
		super();
	}
	
	@Override
	public String toString() {
		return "topAndDepositParameter [id=" + id + ", type=" + type
				+ ", feeRate=" + feeRate + ", sates=" + sates + "]";
	}

	
}
