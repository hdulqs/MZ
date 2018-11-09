package com.mz.manage.remote.model.bakc;

import java.io.Serializable;
import java.math.BigDecimal;

public class AppCommendBackMoney implements Serializable{

	private String custromerName; // 推荐人姓名

	private String fixPriceCoinCode; // 返佣币种

	private BigDecimal moneyNum; // 返佣金额
	
	private BigDecimal paidMoney; // 已派发金额
	
	private String transactionNum; // 返佣生成单号

	public String getCustromerName() {
		return custromerName;
	}

	public void setCustromerName(String custromerName) {
		this.custromerName = custromerName;
	}

	public String getFixPriceCoinCode() {
		return fixPriceCoinCode;
	}

	public void setFixPriceCoinCode(String fixPriceCoinCode) {
		this.fixPriceCoinCode = fixPriceCoinCode;
	}

	public BigDecimal getMoneyNum() {
		return moneyNum;
	}

	public void setMoneyNum(BigDecimal moneyNum) {
		this.moneyNum = moneyNum;
	}

	public BigDecimal getPaidMoney() {
		return paidMoney;
	}

	public void setPaidMoney(BigDecimal paidMoney) {
		this.paidMoney = paidMoney;
	}

	public String getTransactionNum() {
		return transactionNum;
	}

	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	
}
