/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi 
 * @version:      V1.0 
 * @Date:       2016年5月23日 下午6:28:57
 */
package com.mz.manage.remote.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;


/**
 * 
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月23日 下午6:28:57
 */
public class LendIntent implements Serializable{

	// 主键
	private Long id;
	// 借款单号
	@ApiModelProperty(value = "借款单号", required = false)
	private String intentNum;
	// 借款人的币账号id
	@ApiModelProperty(value = "借款人的币账号id", required = false)
	private Long lendId;
	// 借款人
	@ApiModelProperty(value = "借款人", required = false)
	private Long customerId;
	// 借款人的币账号id
	@ApiModelProperty(value = "借款人的币账号id", required = false)
	private Long accountId;
	// 借的币种可以是money还是币种virtualCoin
	@ApiModelProperty(value = "借的币种可以是money还是币种virtualCoin", required = false)
	private String lendCoinType;
	// 借的币种可以是cny，可以是bit
	@ApiModelProperty(value = "借的币种可以是cny，可以是bit", required = false)
	private String lendCoin;
	// 应还币时间
	@ApiModelProperty(value = "应还币时间", required = false)
	private Date intentTime;
	// 实际还币时间
	@ApiModelProperty(value = "实际还币时间", required = false)
	private Date factTime;
	// 还币的数量
	@ApiModelProperty(value = "还币的数量", required = false)
	private BigDecimal repayCount;
	// interest利息principal本金
	@ApiModelProperty(value = "interest利息principal本金", required = false)
	private String intentType;
	
	private String userName;
	
	private String trueName;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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
	public String getLendCoin() {
		return lendCoin;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setLendCoin(String lendCoin) {
		this.lendCoin = lendCoin;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getLendCoinType() {
		return lendCoinType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setLendCoinType(String lendCoinType) {
		this.lendCoinType = lendCoinType;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getId() {
		return id;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIntentNum() {
		return intentNum;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIntentNum(String intentNum) {
		this.intentNum = intentNum;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getLendId() {
		return lendId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setLendId(Long lendId) {
		this.lendId = lendId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getAccountId() {
		return accountId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	


	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getIntentTime() {
		return intentTime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setIntentTime(Date intentTime) {
		this.intentTime = intentTime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getFactTime() {
		return factTime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setFactTime(Date factTime) {
		this.factTime = factTime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getRepayCount() {
		return repayCount;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setRepayCount(BigDecimal repayCount) {
		this.repayCount = repayCount;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIntentType() {
		return intentType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIntentType(String intentType) {
		this.intentType = intentType;
	}

	public LendIntent() {
		super();
	}

	@Override
	public String toString() {
		return "EcEntrust [id=" + id + ", intentNum=" + intentNum
				+ ", customerId=" + customerId + ", accountId=" + accountId
				+  ", repayCount=" + repayCount + ", intentType="
				+ intentType + ", factTime=" + factTime + ", intentTime=" + intentTime 
			+ "]";
	}

}
