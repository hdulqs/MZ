package com.mz.manage.remote.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 委托记录
 * @author CHINA_LSL
 *
 */
public class Entrust implements Serializable{
	
	//委托
	@ApiModelProperty(value = "委托", required = false)
	private String entrustNum;
	//委托时间
	@ApiModelProperty(value = "委托时间", required = false)
	private Date entrustTime;
	//时间戳
	@ApiModelProperty(value = "时间戳", required = false)
	private Long entrustTime_long;
    // 委托的来源 (1. 表示人工pc 2. 表示 机器人3.表示人工移动端 4.强制平仓系统生成,5计划委托，6，止盈平仓系统生成，7止损平仓系统生成)
	@ApiModelProperty(value = "委托的来源 (1. 表示人工pc 2. 表示 机器人3.表示人工移动端 4.强制平仓系统生成,5计划委托，6，止盈平仓系统生成，7止损平仓系统生成)", required = false)
	private int source;
	// 委托价格
	@ApiModelProperty(value = "委托价格", required = false)
	private BigDecimal entrustPrice;
	// 委托数量
	@ApiModelProperty(value = "委托数量", required = false)
	private BigDecimal entrustCount;
	// TYPE 类型 1 ： 买 2 ： 卖
	@ApiModelProperty(value = "TYPE 类型 1 ： 买 2 ： 卖", required = false)
	private int type;
	// 委托剩余数量
	@ApiModelProperty(value = "委托剩余数量", required = false)
	private BigDecimal surplusEntrustCount;
	// STATUS 状态 ---> 0未成交　1部分成交　2已完成　 3部分成交已撤销 4已撤销 
	@ApiModelProperty(value = "STATUS 状态 ---> 0未成交　1部分成交　2已完成　 3部分成交已撤销 4已撤销 ", required = false)
	private int status;
	//成交金额
	@ApiModelProperty(value = "成交金额 ", required = false)
	private BigDecimal transactionSum;
	//委托金额
	@ApiModelProperty(value = "委托金额 ", required = false)
	private BigDecimal entrustSum;
	//成交平台价格
	@ApiModelProperty(value = "成交平台价格", required = false)
	private BigDecimal processedPrice;
	
	private String coinCode;
	
	private String fixPriceCoinCode;
	
	private String coin;
	
	
	
	public String getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = coin;
	}
	public String getCoinCode() {
		return coinCode;
	}
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	public String getFixPriceCoinCode() {
		return fixPriceCoinCode;
	}
	public void setFixPriceCoinCode(String fixPriceCoinCode) {
		this.fixPriceCoinCode = fixPriceCoinCode;
	}
	public BigDecimal getProcessedPrice() {
		return processedPrice;
	}
	public void setProcessedPrice(BigDecimal processedPrice) {
		this.processedPrice = processedPrice;
	}
	public String getEntrustNum() {
		return entrustNum;
	}
	public void setEntrustNum(String entrustNum) {
		this.entrustNum = entrustNum;
	}
	public Date getEntrustTime() {
		return entrustTime;
	}
	public void setEntrustTime(Date entrustTime) {
		this.entrustTime = entrustTime;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getSurplusEntrustCount() {
		return surplusEntrustCount;
	}
	public void setSurplusEntrustCount(BigDecimal surplusEntrustCount) {
		this.surplusEntrustCount = surplusEntrustCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigDecimal getTransactionSum() {
		return transactionSum;
	}
	public void setTransactionSum(BigDecimal transactionSum) {
		this.transactionSum = transactionSum;
	}
	public BigDecimal getEntrustSum() {
		return entrustSum;
	}
	public void setEntrustSum(BigDecimal entrustSum) {
		this.entrustSum = entrustSum;
	}
	public Long getEntrustTime_long() {
		return entrustTime_long;
	}
	public void setEntrustTime_long(Long entrustTime_long) {
		this.entrustTime_long = entrustTime_long;
	}
	
	
	
	
	
}
