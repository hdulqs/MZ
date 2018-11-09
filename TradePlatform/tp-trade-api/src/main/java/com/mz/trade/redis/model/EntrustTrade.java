package com.mz.trade.redis.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class EntrustTrade implements Serializable {
	private Long id;
	private String entrustNum;
	private Long customerId;
	private Long accountId;
	private Long coinAccountId;
	private String coinCode;
	private Integer fixPriceType; // 0真实货币1虚拟币
	private String fixPriceCoinCode; // 定价币种
	private Integer type;
	private Integer status;
	private BigDecimal entrustPrice;
	private BigDecimal entrustCount;
	private BigDecimal entrustSum;
	private BigDecimal surplusEntrustCount;
	private BigDecimal transactionSum;
	private Date entrustTime;

	private Long entrustTime_long;

	private BigDecimal transactionFeeRate;
	private BigDecimal transactionFee;
	private BigDecimal floatUpPrice;
	private BigDecimal floatDownPrice;
	private Integer source;
	private String customerIp; // 下单用户的IP
	private Integer entrustWay;
	private String userName;
	private String trueName; // 名字
	private String surName;// 姓氏
	private BigDecimal processedPrice;
	private Integer cancelKeepN; // 撤销保留最新n条

	public Integer getCancelKeepN() {
		return cancelKeepN;
	}

	public void setCancelKeepN(Integer cancelKeepN) {
		this.cancelKeepN = cancelKeepN;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public BigDecimal getProcessedPrice() {
		return processedPrice;
	}

	public void setProcessedPrice(BigDecimal processedPrice) {
		this.processedPrice = processedPrice;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getTransactionFee() {
		return transactionFee;
	}

	public void setTransactionFee(BigDecimal transactionFee) {
		this.transactionFee = transactionFee;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEntrustWay() {
		return entrustWay;
	}

	public void setEntrustWay(Integer entrustWay) {
		this.entrustWay = entrustWay;
	}

	public BigDecimal getSurplusEntrustCount() {
		return surplusEntrustCount;
	}

	public void setSurplusEntrustCount(BigDecimal surplusEntrustCount) {
		this.surplusEntrustCount = surplusEntrustCount;
	}

	public BigDecimal getTransactionSum() {
		return transactionSum;
	}

	public void setTransactionSum(BigDecimal transactionSum) {
		this.transactionSum = transactionSum;
	}

	public String getEntrustNum() {
		return entrustNum;
	}

	public void setEntrustNum(String entrustNum) {
		this.entrustNum = entrustNum;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getCoinAccountId() {
		return coinAccountId;
	}

	public void setCoinAccountId(Long coinAccountId) {
		this.coinAccountId = coinAccountId;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public Integer getFixPriceType() {
		return fixPriceType;
	}

	public void setFixPriceType(Integer fixPriceType) {
		this.fixPriceType = fixPriceType;
	}

	public String getFixPriceCoinCode() {
		return fixPriceCoinCode;
	}

	public void setFixPriceCoinCode(String fixPriceCoinCode) {
		this.fixPriceCoinCode = fixPriceCoinCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}

	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}

	public BigDecimal getEntrustCount() {
		return entrustCount;
	}

	public void setEntrustCount(BigDecimal entrustCount) {
		this.entrustCount = entrustCount;
	}

	public BigDecimal getEntrustSum() {
		return entrustSum;
	}

	public void setEntrustSum(BigDecimal entrustSum) {
		this.entrustSum = entrustSum;
	}

	public Date getEntrustTime() {
		return entrustTime;
	}

	public void setEntrustTime(Date entrustTime) {
		this.entrustTime = entrustTime;
	}

	public BigDecimal getTransactionFeeRate() {
		return transactionFeeRate;
	}

	public void setTransactionFeeRate(BigDecimal transactionFeeRate) {
		this.transactionFeeRate = transactionFeeRate;
	}

	public BigDecimal getFloatUpPrice() {
		return floatUpPrice;
	}

	public void setFloatUpPrice(BigDecimal floatUpPrice) {
		this.floatUpPrice = floatUpPrice;
	}

	public BigDecimal getFloatDownPrice() {
		return floatDownPrice;
	}

	public void setFloatDownPrice(BigDecimal floatDownPrice) {
		this.floatDownPrice = floatDownPrice;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public Long getEntrustTime_long() {
		return entrustTime_long;
	}

	public void setEntrustTime_long(Long entrustTime_long) {
		this.entrustTime_long = entrustTime_long;
	}

}
