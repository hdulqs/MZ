/**

 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月25日 下午4:09:31
 */
package com.mz.trade.entrust.model;

import java.math.BigDecimal;

public class EradeCheck  {
	
	private BigDecimal coldEntrustMoney;
	private BigDecimal buyTransactionMoney;
	private BigDecimal sellTransactionMoney;
	private BigDecimal transactionFee;
	
	private BigDecimal edcoldEntrustCount;
	private BigDecimal buyTransactioncount;
	private BigDecimal transactionFeecoin;
	private BigDecimal sellTransactioncount;
	
	private BigDecimal coldEntrustFixPrice;
	private BigDecimal buyTransactionFixPrice;
	private BigDecimal sellTransactionFixPrice;
	private BigDecimal transactionFeeFixPrice;
	public BigDecimal getColdEntrustMoney() {
		return coldEntrustMoney;
	}
	public void setColdEntrustMoney(BigDecimal coldEntrustMoney) {
		this.coldEntrustMoney = coldEntrustMoney;
	}
	public BigDecimal getBuyTransactionMoney() {
		return buyTransactionMoney;
	}
	public void setBuyTransactionMoney(BigDecimal buyTransactionMoney) {
		this.buyTransactionMoney = buyTransactionMoney;
	}
	public BigDecimal getSellTransactionMoney() {
		return sellTransactionMoney;
	}
	public void setSellTransactionMoney(BigDecimal sellTransactionMoney) {
		this.sellTransactionMoney = sellTransactionMoney;
	}
	public BigDecimal getTransactionFee() {
		return transactionFee;
	}
	public void setTransactionFee(BigDecimal transactionFee) {
		this.transactionFee = transactionFee;
	}
	public BigDecimal getEdcoldEntrustCount() {
		return edcoldEntrustCount;
	}
	public void setEdcoldEntrustCount(BigDecimal edcoldEntrustCount) {
		this.edcoldEntrustCount = edcoldEntrustCount;
	}
	public BigDecimal getBuyTransactioncount() {
		return buyTransactioncount;
	}
	public void setBuyTransactioncount(BigDecimal buyTransactioncount) {
		this.buyTransactioncount = buyTransactioncount;
	}
	public BigDecimal getTransactionFeecoin() {
		return transactionFeecoin;
	}
	public void setTransactionFeecoin(BigDecimal transactionFeecoin) {
		this.transactionFeecoin = transactionFeecoin;
	}
	public BigDecimal getSellTransactioncount() {
		return sellTransactioncount;
	}
	public void setSellTransactioncount(BigDecimal sellTransactioncount) {
		this.sellTransactioncount = sellTransactioncount;
	}
	public BigDecimal getColdEntrustFixPrice() {
		return coldEntrustFixPrice;
	}
	public void setColdEntrustFixPrice(BigDecimal coldEntrustFixPrice) {
		this.coldEntrustFixPrice = coldEntrustFixPrice;
	}
	public BigDecimal getBuyTransactionFixPrice() {
		return buyTransactionFixPrice;
	}
	public void setBuyTransactionFixPrice(BigDecimal buyTransactionFixPrice) {
		this.buyTransactionFixPrice = buyTransactionFixPrice;
	}
	public BigDecimal getSellTransactionFixPrice() {
		return sellTransactionFixPrice;
	}
	public void setSellTransactionFixPrice(BigDecimal sellTransactionFixPrice) {
		this.sellTransactionFixPrice = sellTransactionFixPrice;
	}
	public BigDecimal getTransactionFeeFixPrice() {
		return transactionFeeFixPrice;
	}
	public void setTransactionFeeFixPrice(BigDecimal transactionFeeFixPrice) {
		this.transactionFeeFixPrice = transactionFeeFixPrice;
	}
	
	
	
	
}
