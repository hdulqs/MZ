package com.mz.manage.remote.model.c2c;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class C2cOrder implements Serializable {
	
	
	private String transactionNum;  //交易订单号
	
	private String userName;  //用户登录名
	
	private Long customerId;  //用户id
	
	private Long accountId;  //虚拟账户id
	
	private Integer transactionType;  //交易类型  1买，2卖
	
	private BigDecimal transactionMoney;  //交易金额
	private BigDecimal transactionCount;  //交易数量
	private BigDecimal transactionPrice;  //交易价格
	
	private Date transactionTime ;//下单时间
	private String createTime;
	
	private Long transactionLong;//下单时间戳
	
	private Integer status;  //1待审核 2已完成 3以否决
	private Integer status2;  //1未支付 2已支付
	
	private String remark;  //备注
	
	private String coinCode;  //币的类型
	
	private BigDecimal fee;  //手续费

	public Long getTransationLong() {
		if(transactionTime!=null) {
			return this.transactionTime.getTime();
		}
		return 0L;
	}
	public void setTransactionLong(Long transactionLong) {
		this.transactionLong = transactionLong;
	}
	/**
	 * <p>交易订单号</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getTransactionNum() {
		return transactionNum;
	}
	
	/**
	 * <p>交易订单号</p>
	 * @author:  liushilei
	 * @param:   @param transactionNum
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	
	
	/**
	 * <p>用户登录名</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>用户登录名</p>
	 * @author:  liushilei
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  liushilei
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>虚拟账户id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Long getAccountId() {
		return accountId;
	}
	
	/**
	 * <p>虚拟账户id</p>
	 * @author:  liushilei
	 * @param:   @param accountId
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	
	/**
	 * <p>交易类型(1线上充值,2线上提现 3线下充值 4线下取现...)</p>
	 * @author:  liushilei
	 * @return:  Integer 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Integer getTransactionType() {
		return transactionType;
	}
	
	/**
	 * <p>交易类型(1线上充值,2线上提现 3线下充值 4线下取现...)</p>
	 * @author:  liushilei
	 * @param:   @param transactionType
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	
	
	/**
	 * <p>交易金额</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public BigDecimal getTransactionMoney() {
		return transactionMoney;
	}
	
	/**
	 * <p>交易金额</p>
	 * @author:  liushilei
	 * @param:   @param transactionMoney
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setTransactionMoney(BigDecimal transactionMoney) {
		this.transactionMoney = transactionMoney;
	}
	
	
	/**
	 * <p>1待审核 2已完成 3以否决</p>
	 * @author:  liushilei
	 * @return:  Integer 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * <p>1待审核 2已完成 3以否决</p>
	 * @author:  liushilei
	 * @param:   @param status
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	/**
	 * <p>备注</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * <p>备注</p>
	 * @author:  liushilei
	 * @param:   @param remark
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	/**
	 * <p>币的类型(RMB USD)</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>币的类型(RMB USD)</p>
	 * @author:  liushilei
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>手续费</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public BigDecimal getFee() {
		return fee;
	}
	
	/**
	 * <p>手续费</p>
	 * @author:  liushilei
	 * @param:   @param fee
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(BigDecimal transactionCount) {
		this.transactionCount = transactionCount;
	}

	public BigDecimal getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(BigDecimal transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Integer getStatus2() {
		return status2;
	}

	public void setStatus2(Integer status2) {
		this.status2 = status2;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
	
	


}
