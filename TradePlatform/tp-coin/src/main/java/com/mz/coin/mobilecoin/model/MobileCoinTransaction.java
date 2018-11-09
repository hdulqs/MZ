package com.mz.coin.mobilecoin.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import com.mz.customer.person.model.AppPersonInfo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@SuppressWarnings("serial")
@Table(name = "mobile_coin_transaction")
public class MobileCoinTransaction extends BaseModel {

	// 主键
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	// 交易订单号
	@Column(name = "transactionNum")
	private String transactionNum;
	
	// 数字货币账户id
	@Column(name = "accountId")
	private Long accountId;
	
	// 生成币用户名
	@Column(name = "accountName")
	private String accountName;
	
	// 交易类型(1充币 ，2提币)
	@Column(name = "transactionType")
	private Integer transactionType;
	
	// 交易金额
	@Column(name = "transactionMoney")
	private BigDecimal transactionMoney;
	
	// 状态 1待审核 --2已完成 -- 3以否决
	@Column(name = "status")
	private Integer status;

	// 币的类型
	@Column(name = "coinCode")
	private String coinCode;
	
	//手续费
	@Column(name = "fee")
	private BigDecimal fee;

	//转入钱包地址
	@Column(name = "inAddress")
	private String inAddress;
	
	//转出钱包地址
	@Column(name = "outAddress")
	private String  outAddress;
	
	//确认节点数
	@Column(name = "confirmations")
	private String confirmations; 	
	
	//区块时间
	@Column(name = "blocktime")
	private String blocktime;
	
	//时间
	@Column(name = "time")
	private String time;		
		
	//时间
	@Column(name = "timereceived")
	private String timereceived;
	
	// 我方币种账号     
	@Column(name="ourAccountNumber")
	private String ourAccountNumber ;	
	
	//订单号  
	@Column(name="orderNo")
	private String orderNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransactionNum() {
		return transactionNum;
	}

	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getTransactionMoney() {
		return transactionMoney;
	}

	public void setTransactionMoney(BigDecimal transactionMoney) {
		this.transactionMoney = transactionMoney;
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

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getInAddress() {
		return inAddress;
	}

	public void setInAddress(String inAddress) {
		this.inAddress = inAddress;
	}

	public String getOutAddress() {
		return outAddress;
	}

	public void setOutAddress(String outAddress) {
		this.outAddress = outAddress;
	}

	public String getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(String confirmations) {
		this.confirmations = confirmations;
	}

	public String getBlocktime() {
		return blocktime;
	}

	public void setBlocktime(String blocktime) {
		this.blocktime = blocktime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimereceived() {
		return timereceived;
	}

	public void setTimereceived(String timereceived) {
		this.timereceived = timereceived;
	}

	public String getOurAccountNumber() {
		return ourAccountNumber;
	}

	public void setOurAccountNumber(String ourAccountNumber) {
		this.ourAccountNumber = ourAccountNumber;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
