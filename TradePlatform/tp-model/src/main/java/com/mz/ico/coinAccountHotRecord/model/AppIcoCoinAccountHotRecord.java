/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-08-18 14:06:56 
 */
package com.mz.ico.coinAccountHotRecord.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppIcoCoinAccountHotRecord </p>
 * @author:         shangxl
 * @Date :          2017-08-18 14:06:56  
 */
@Table(name="app_ico_coinAccount_hot_record")
public class AppIcoCoinAccountHotRecord extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "accountId")
	private Long accountId;  //数字货币账户id（dm_account）
	
	@Column(name= "customerId")
	private Long customerId;  //用户id
	
	@Column(name= "userName")
	private String userName;  //用户登录名
	
	@Column(name= "recordType")
	private Integer recordType;  //流水类型 （ 1增加 2减少）
	
	@Column(name= "transactionMoney")
	private BigDecimal transactionMoney;  //交易金额
	
	@Column(name= "transactionNum")
	private String transactionNum;  //transactionNum
	
	@Column(name= "status")
	private Integer status;  //流水状态( 0处理中 5成功 10失败 )
	
	@Column(name= "remark")
	private String remark;  //交易备注
	
	@Column(name= "currencyType")
	private String currencyType;  //货币类型（BTC, LTC ）
	
	@Column(name= "coinCode")
	private String coinCode;  //币种（CNY）
	
	@Column(name= "website")
	private String website;  //website
	
	@Column(name= "trueName")
	private String trueName;  //trueName
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>数字货币账户id（dm_account）</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public Long getAccountId() {
		return accountId;
	}
	
	/**
	 * <p>数字货币账户id（dm_account）</p>
	 * @author:  shangxl
	 * @param:   @param accountId
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>用户登录名</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>用户登录名</p>
	 * @author:  shangxl
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>流水类型 （ 1增加 2减少）</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public Integer getRecordType() {
		return recordType;
	}
	
	/**
	 * <p>流水类型 （ 1增加 2减少）</p>
	 * @author:  shangxl
	 * @param:   @param recordType
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}
	
	
	/**
	 * <p>交易金额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public BigDecimal getTransactionMoney() {
		return transactionMoney;
	}
	
	/**
	 * <p>交易金额</p>
	 * @author:  shangxl
	 * @param:   @param transactionMoney
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setTransactionMoney(BigDecimal transactionMoney) {
		this.transactionMoney = transactionMoney;
	}
	
	
	/**
	 * <p>transactionNum</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public String getTransactionNum() {
		return transactionNum;
	}
	
	/**
	 * <p>transactionNum</p>
	 * @author:  shangxl
	 * @param:   @param transactionNum
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	
	
	/**
	 * <p>流水状态( 0处理中 5成功 10失败 )</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * <p>流水状态( 0处理中 5成功 10失败 )</p>
	 * @author:  shangxl
	 * @param:   @param status
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	/**
	 * <p>交易备注</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * <p>交易备注</p>
	 * @author:  shangxl
	 * @param:   @param remark
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	/**
	 * <p>货币类型（BTC, LTC ）</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public String getCurrencyType() {
		return currencyType;
	}
	
	/**
	 * <p>货币类型（BTC, LTC ）</p>
	 * @author:  shangxl
	 * @param:   @param currencyType
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	
	/**
	 * <p>币种（CNY）</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>币种（CNY）</p>
	 * @author:  shangxl
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-08-18 14:06:56    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2017-08-18 14:06:56   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	

}
