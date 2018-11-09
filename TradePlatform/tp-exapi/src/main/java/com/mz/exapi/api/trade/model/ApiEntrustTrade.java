package com.mz.exapi.api.trade.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ApiEntrustTrade  implements Serializable{
	//价格
	private BigDecimal entrustPrice;
	//1买2卖
	private Integer type;
	//交易对
	private String coinCode;
	//数量
	private BigDecimal entrustCount;
	//api 来源默认为8
	private Integer  source=8;
	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}
	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCoinCode() {
		return coinCode;
	}
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	public BigDecimal getEntrustCount() {
		return entrustCount;
	}
	public void setEntrustCount(BigDecimal entrustCount) {
		this.entrustCount = entrustCount;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}

	
	
	
}
