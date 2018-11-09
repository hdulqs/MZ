/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月28日 下午4:25:12
 */
package com.mz.manage.remote.model;


import java.math.BigDecimal;

import com.mz.core.mvc.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;


@SuppressWarnings("serial")
public class ExDmTransactionManage extends BaseModel {

	// 主键
	private Long id;
	// 交易金额
	@ApiModelProperty(value = "交易金额", required = false)
	private BigDecimal transactionMoney;
	// 交易订单号
	@ApiModelProperty(value = "交易订单号", required = false)
	private String transactionNum;
	//手续费
	@ApiModelProperty(value = "手续费", required = false)
	private BigDecimal fee;
	// 状态 1待审核 --2已完成 -- 3以否决
	@ApiModelProperty(value = "状态 1待审核 --2已完成 -- 3以否决", required = false)
	private Integer status;
	// 用户id
	@ApiModelProperty(value = "用户id", required = false)
	private Long customerId;
	private String customerName;
	@ApiModelProperty(value = "持卡人", required = false)
	private String trueName;
	// 数字货币账户id
	@ApiModelProperty(value = "数字货币账户id", required = false)
	private Long accountId;
	// 交易类型(1充币 ，2提币)
	@ApiModelProperty(value = "交易类型(1充币 ，2提币)", required = false)
	private Integer transactionType;
	// 操作人id
	@ApiModelProperty(value = "操作人id", required = false)
	private Long userId;
	
	
	private String currencyType;
	// 币的类型
	@ApiModelProperty(value = "币的类型", required = false)
	private String coinCode;
	
	//站点类别 en ,cn
	@ApiModelProperty(value = "站点类别 en ,cn", required = false)
	private String website;
	
	private Long created_long;
	
	
	
	
	public Long getCreated_long() {
		if(super.getCreated()!=null){
			return super.getCreated().getTime();
		}
		return 0L;
	}

	public void setCreated_long(Long created_long) {
		this.created_long = created_long;
	}

	//驳回理由
	@ApiModelProperty(value = "驳回理由", required = false)
	private String rejectionReason ;

	
	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOurAccountNumber() {
		return ourAccountNumber;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOurAccountNumber(String ourAccountNumber) {
		this.ourAccountNumber = ourAccountNumber;
	}

	//转入钱包地址
	@ApiModelProperty(value = "转入钱包地址", required = false)
	private String inAddress;

	
	//转出钱包地址
	@ApiModelProperty(value = "转出钱包地址", required = false)
	private String  outAddress;
	
	//确认节点数
	@ApiModelProperty(value = "确认节点数", required = false)
	private String confirmations; 
	
	
	//区块时间
	@ApiModelProperty(value = "区块时间", required = false)
	private String blocktime;
	
	//时间
	@ApiModelProperty(value = "时间", required = false)
	private String time;
		
		
	//时间
	@ApiModelProperty(value = "接收时间", required = false)
	private String timereceived;
	
	// 我方币种账号    
	@ApiModelProperty(value = "我方币种账号", required = false)
	private String ourAccountNumber ;
	
	
	//订单号  
	@ApiModelProperty(value = "订单号", required = false)
	private String orderNo;
	
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getInAddress() {
		return inAddress;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOutAddress() {
		return outAddress;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getConfirmations() {
		return confirmations;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBlocktime() {
		return blocktime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTime() {
		return time;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTimereceived() {
		return timereceived;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setInAddress(String inAddress) {
		this.inAddress = inAddress;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOutAddress(String outAddress) {
		this.outAddress = outAddress;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setConfirmations(String confirmations) {
		this.confirmations = confirmations;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBlocktime(String blocktime) {
		this.blocktime = blocktime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTimereceived(String timereceived) {
		this.timereceived = timereceived;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRejectionReason() {
		return rejectionReason;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getFee() {
		return fee;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public ExDmTransactionManage(Long id, String transactionNum, Long customerId,
			String customerName, Long accountId, Integer transactionType,
			BigDecimal transactionMoney, Integer status, Long userId,
			String currencyType) {
		super();
		this.id = id;
		this.transactionNum = transactionNum;
		this.customerId = customerId;
		this.customerName = customerName;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.transactionMoney = transactionMoney;
		this.status = status;
		this.userId = userId;
		this.currencyType = currencyType;
	}

	public ExDmTransactionManage() {
		super();
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	
}
