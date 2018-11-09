/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-18 14:08:35 
 */
package com.mz.manage.remote.ico.model;


import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

/**
 * <p> AppIcoCoinTransaction </p>
 * @author:         shangxl
 * @Date :          2017-08-18 14:08:35  
 */
public class AppIcoCoinTransactionDTO extends BaseModel {
	
	
	private Long id;  //id
	
	private String transactionNum;  //transactionNum
	
	private Long customerId;  //用户id
	
	private String customerName;  //用户名
	
	private Long accountId;  //数字货币账户id
	
	private Integer transactionType;  //交易类型(1充币 ，2提币)
	
	private BigDecimal transactionMoney;  //交易金额
	
	private Integer status;  //1待审核 2已完成 3以否决
	
	private Long userId;  //操作人id
	
	private String currencyType;  //currencyType
	
	private String coinCode;  //币种（CNY）
	
	private String website;  //website
	
	private BigDecimal fee;  //fee
	
	private String inAddress;  //inAddress
	
	private String outAddress;  //outAddress
	
	private String time;  //time
	
	private String confirmations;  //confirmations
	
	private String timereceived;  //timereceived
	
	private String blocktime;  //blocktime
	
	private String rejectionReason;  //rejectionReason
	
	private String ourAccountNumber;  //ourAccountNumber
	
	private String orderNo;  //orderNo
	
	private String trueName;  //trueName
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>transactionNum</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getTransactionNum() {
		return transactionNum;
	}
	
	/**
	 * <p>transactionNum</p>
	 * @author:  shangxl
	 * @param:   @param transactionNum
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>用户名</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getCustomerName() {
		return customerName;
	}
	
	/**
	 * <p>用户名</p>
	 * @author:  shangxl
	 * @param:   @param customerName
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	
	/**
	 * <p>数字货币账户id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public Long getAccountId() {
		return accountId;
	}
	
	/**
	 * <p>数字货币账户id</p>
	 * @author:  shangxl
	 * @param:   @param accountId
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	
	/**
	 * <p>交易类型(1充币 ，2提币)</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public Integer getTransactionType() {
		return transactionType;
	}
	
	/**
	 * <p>交易类型(1充币 ，2提币)</p>
	 * @author:  shangxl
	 * @param:   @param transactionType
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	
	
	/**
	 * <p>交易金额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public BigDecimal getTransactionMoney() {
		return transactionMoney;
	}
	
	/**
	 * <p>交易金额</p>
	 * @author:  shangxl
	 * @param:   @param transactionMoney
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setTransactionMoney(BigDecimal transactionMoney) {
		this.transactionMoney = transactionMoney;
	}
	
	
	/**
	 * <p>1待审核 2已完成 3以否决</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * <p>1待审核 2已完成 3以否决</p>
	 * @author:  shangxl
	 * @param:   @param status
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	/**
	 * <p>操作人id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * <p>操作人id</p>
	 * @author:  shangxl
	 * @param:   @param userId
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	/**
	 * <p>currencyType</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getCurrencyType() {
		return currencyType;
	}
	
	/**
	 * <p>currencyType</p>
	 * @author:  shangxl
	 * @param:   @param currencyType
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	
	/**
	 * <p>币种（CNY）</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>币种（CNY）</p>
	 * @author:  shangxl
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>fee</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public BigDecimal getFee() {
		return fee;
	}
	
	/**
	 * <p>fee</p>
	 * @author:  shangxl
	 * @param:   @param fee
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	
	/**
	 * <p>inAddress</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getInAddress() {
		return inAddress;
	}
	
	/**
	 * <p>inAddress</p>
	 * @author:  shangxl
	 * @param:   @param inAddress
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setInAddress(String inAddress) {
		this.inAddress = inAddress;
	}
	
	
	/**
	 * <p>outAddress</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getOutAddress() {
		return outAddress;
	}
	
	/**
	 * <p>outAddress</p>
	 * @author:  shangxl
	 * @param:   @param outAddress
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setOutAddress(String outAddress) {
		this.outAddress = outAddress;
	}
	
	
	/**
	 * <p>time</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getTime() {
		return time;
	}
	
	/**
	 * <p>time</p>
	 * @author:  shangxl
	 * @param:   @param time
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	
	/**
	 * <p>confirmations</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getConfirmations() {
		return confirmations;
	}
	
	/**
	 * <p>confirmations</p>
	 * @author:  shangxl
	 * @param:   @param confirmations
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setConfirmations(String confirmations) {
		this.confirmations = confirmations;
	}
	
	
	/**
	 * <p>timereceived</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getTimereceived() {
		return timereceived;
	}
	
	/**
	 * <p>timereceived</p>
	 * @author:  shangxl
	 * @param:   @param timereceived
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setTimereceived(String timereceived) {
		this.timereceived = timereceived;
	}
	
	
	/**
	 * <p>blocktime</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getBlocktime() {
		return blocktime;
	}
	
	/**
	 * <p>blocktime</p>
	 * @author:  shangxl
	 * @param:   @param blocktime
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setBlocktime(String blocktime) {
		this.blocktime = blocktime;
	}
	
	
	/**
	 * <p>rejectionReason</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getRejectionReason() {
		return rejectionReason;
	}
	
	/**
	 * <p>rejectionReason</p>
	 * @author:  shangxl
	 * @param:   @param rejectionReason
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	
	
	/**
	 * <p>ourAccountNumber</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getOurAccountNumber() {
		return ourAccountNumber;
	}
	
	/**
	 * <p>ourAccountNumber</p>
	 * @author:  shangxl
	 * @param:   @param ourAccountNumber
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setOurAccountNumber(String ourAccountNumber) {
		this.ourAccountNumber = ourAccountNumber;
	}
	
	
	/**
	 * <p>orderNo</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getOrderNo() {
		return orderNo;
	}
	
	/**
	 * <p>orderNo</p>
	 * @author:  shangxl
	 * @param:   @param orderNo
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:08:35    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2017-08-18 14:08:35   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	

}
