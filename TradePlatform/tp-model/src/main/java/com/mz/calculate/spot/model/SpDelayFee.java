/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao
 * @version:     V1.0 
 * @Date:        2016-11-28 18:34:39 
 */
package com.mz.calculate.spot.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.util.Date;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> SpDelayFee </p>
 * @author:         Gao
 * @Date :          2016-11-28 18:34:39  
 */
@Table(name="sp_delay_fee")
public class SpDelayFee extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "customerId")
	private Long customerId;  //customerId
	
	@Column(name= "userCode")
	private String userCode;  //userCode
	
	@Column(name= "trueName")
	private String trueName;  //trueName
	
	@Column(name= "userName")
	private String userName;  //userName
	@Column(name= "vipNumber")
	private String vipNumber;  //会员编号
	
	@Column(name= "vipName")
	private String vipName;  //会员名称
	@Column(name= "coinCode")
	private String coinCode;  //商品代碼
	
	@Column(name= "settleDate")
	private Date settleDate;  //结算时间
	
	@Column(name= "positionType")
	private Integer positionType;  //持仓類型
	
	@Column(name= "positionCount")
	private BigDecimal positionCount;  //持仓筆數
	
	@Column(name= "delayMoney")
	private BigDecimal delayMoney;  //延期费
	
	
	
	// 持仓id
	@Column(name = "entrustId")
	private Long entrustId;
	// 持仓单号
	@Column(name = "entrustNum")
	private String entrustNum;
	
	@Column(name= "positionPrcie")
	private BigDecimal positionPrcie;  //持仓价
	
	@Column(name= "depositRransactionSum")
	private BigDecimal depositRransactionSum;  
	
	@Column(name= "profitandlossMoney")
	private BigDecimal profitandlossMoney;  
	
	
	
	
	

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getEntrustId() {
		return entrustId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setEntrustId(Long entrustId) {
		this.entrustId = entrustId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getEntrustNum() {
		return entrustNum;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setEntrustNum(String entrustNum) {
		this.entrustNum = entrustNum;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getPositionPrcie() {
		return positionPrcie;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setPositionPrcie(BigDecimal positionPrcie) {
		this.positionPrcie = positionPrcie;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getDepositRransactionSum() {
		return depositRransactionSum;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setDepositRransactionSum(BigDecimal depositRransactionSum) {
		this.depositRransactionSum = depositRransactionSum;
	}

	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getProfitandlossMoney() {
		return profitandlossMoney;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setProfitandlossMoney(BigDecimal profitandlossMoney) {
		this.profitandlossMoney = profitandlossMoney;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getVipNumber() {
		return vipNumber;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setVipNumber(String vipNumber) {
		this.vipNumber = vipNumber;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getVipName() {
		return vipName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	/**
	 * <p>id</p>
	 * @author:  Gao
	 * @return:  Long 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  Gao
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * <p>customerId</p>
	 * @author:  Gao
	 * @return:  Long 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>customerId</p>
	 * @author:  Gao
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>userCode</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public String getUserCode() {
		return userCode;
	}
	
	/**
	 * <p>userCode</p>
	 * @author:  Gao
	 * @param:   @param userCode
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	/**
	 * <p>trueName</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>trueName</p>
	 * @author:  Gao
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	/**
	 * <p>userName</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>userName</p>
	 * @author:  Gao
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>商品代碼</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>商品代碼</p>
	 * @author:  Gao
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>结算时间</p>
	 * @author:  Gao
	 * @return:  Date 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public Date getSettleDate() {
		return settleDate;
	}
	
	/**
	 * <p>结算时间</p>
	 * @author:  Gao
	 * @param:   @param settleDate
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
	
	

	
	
	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getPositionType() {
		return positionType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setPositionType(Integer positionType) {
		this.positionType = positionType;
	}

	/**
	 * <p>持仓筆數</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public BigDecimal getPositionCount() {
		return positionCount;
	}
	
	/**
	 * <p>持仓筆數</p>
	 * @author:  Gao
	 * @param:   @param positionCount
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setPositionCount(BigDecimal positionCount) {
		this.positionCount = positionCount;
	}
	
	
	/**
	 * <p>延期费</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 18:34:39    
	 */
	public BigDecimal getDelayMoney() {
		return delayMoney;
	}
	
	/**
	 * <p>延期费</p>
	 * @author:  Gao
	 * @param:   @param delayMoney
	 * @return:  void 
	 * @Date :   2016-11-28 18:34:39   
	 */
	public void setDelayMoney(BigDecimal delayMoney) {
		this.delayMoney = delayMoney;
	}
	
	

}
