/**
* Copyright:   风云科技
 * @author:         zongwei
 * @Date :          20180502
 * @version:     V1.0 
 */
package com.mz.customer.businessman.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * Copyright:   风云科技
 * @author:         zongwei
 * @Date :          20180502
 */
@Table(name="otc_coin")
public class OtcCoin extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "coinCode")
	private String coinCode;  //币种代码

	

	@Column(name= "isOpen")
	private int isOpen;  //是否开启Otc
	
	@Column(name= "buyPrice")
	private BigDecimal buyPrice;  //买入价
	
	@Column(name= "sellPrice")
	private BigDecimal sellPrice;  //卖出价
	
	@Column(name= "poundage_type")
	private String poundage_type;  //手续费类型, Definite 固定的，Proportions  比例的   
	
	@Column(name= "poundage")
	private BigDecimal poundage;  //手续费
	
	@Column(name= "minCount")
	private BigDecimal minCount;  //最小交易数量
	
	@Column(name= "maxCount")
	private BigDecimal maxCount;  //最大交易数量
	
	@Column(name= "maxTradeTime")
	private BigDecimal maxTradeTime ;  //允许最大未付款时间  默认30 分钟

	@Column(name= "minbuyPrice")
	private BigDecimal minbuyPrice;  //最小买入价

	@Column(name= "maxbuyPrice")
	private BigDecimal maxbuyPrice;  //最大买入价

	@Column(name= "minsellPrice")
	private BigDecimal minsellPrice;  //最小卖出价

	public BigDecimal getMinbuyPrice() {
		return minbuyPrice;
	}

	public void setMinbuyPrice(BigDecimal minbuyPrice) {
		this.minbuyPrice = minbuyPrice;
	}

	public BigDecimal getMaxbuyPrice() {
		return maxbuyPrice;
	}

	public void setMaxbuyPrice(BigDecimal maxbuyPrice) {
		this.maxbuyPrice = maxbuyPrice;
	}

	public BigDecimal getMinsellPrice() {
		return minsellPrice;
	}

	public void setMinsellPrice(BigDecimal minsellPrice) {
		this.minsellPrice = minsellPrice;
	}

	public BigDecimal getMaxsellPrice() {
		return maxsellPrice;
	}

	public void setMaxsellPrice(BigDecimal maxsellPrice) {
		this.maxsellPrice = maxsellPrice;
	}

	@Column(name= "maxsellPrice")
	private BigDecimal maxsellPrice;  //最大卖出价

	public BigDecimal getMaxTradeTime() {
		return maxTradeTime;
	}

	public void setMaxTradeTime(BigDecimal maxTradeTime) {
		this.maxTradeTime = maxTradeTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getMinCount() {
		return minCount;
	}

	public void setMinCount(BigDecimal minCount) {
		this.minCount = minCount;
	}

	public BigDecimal getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(BigDecimal maxCount) {
		this.maxCount = maxCount;
	}
	
	public String getPoundage_type() {
		return poundage_type;
	}

	public void setPoundage_type(String poundage_type) {
		this.poundage_type = poundage_type;
	}

	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

}
