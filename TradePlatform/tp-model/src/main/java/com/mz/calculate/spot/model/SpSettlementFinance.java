/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao
 * @version:     V1.0 
 * @Date:        2016-11-28 14:23:27 
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
 * <p> SpSettlementFinance </p>
 * @author:         Gao
 * @Date :          2016-11-28 14:23:27  
 */
@Table(name="sp_settlement_finance")
public class SpSettlementFinance extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "createDate")
	private String createDate;  //createDate
	
	@Column(name= "customerId")
	private Long customerId;  //customerId
	
	@Column(name= "userCode")
	private String userCode;  //userCode
	@Column(name= "userName")
	private String userName;  //userCode
	
	@Column(name= "trueName")
	private String trueName;  //trueName
	@Column(name= "vipNumber")
	private String vipNumber;  //会员编号
	
	@Column(name= "vipName")
	private String vipName;  //会员名称
	@Column(name= "settleDate")
	private Date settleDate;  //结算时间
	
	@Column(name= "startSettleDate")
	private Date startSettleDate;  //开始结算时间
	
	@Column(name= "endSettleDate")
	private Date endSettleDate;  //结束时间
	
	@Column(name= "beginMoney")
	private BigDecimal beginMoney;  //期初金额
	
	@Column(name= "endMoney")
	private BigDecimal endMoney;  //期末金额
	
	@Column(name= "hotMoney")
	private BigDecimal hotMoney;  //可用金额
	
	@Column(name= "coldMoney")
	private BigDecimal coldMoney;  //冻结保证金
	
	@Column(name= "lendMoney")
	private BigDecimal lendMoney;  //持仓保证金
	
	@Column(name= "floatprofitandlossMoney")
	private BigDecimal floatprofitandlossMoney;  //浮动盈亏
	
	@Column(name= "rechargeMoney")
	private BigDecimal rechargeMoney;  //入金
	
	@Column(name= "rechargeFee")
	private BigDecimal rechargeFee;  //入金收费
	
	@Column(name= "withdrawMoney")
	private BigDecimal withdrawMoney;  //出金
	
	@Column(name= "withdrawFee")
	private BigDecimal withdrawFee;  //出金收费
	
	@Column(name= "createPositionsFee")
	private BigDecimal createPositionsFee;  //建仓手续费
	
	@Column(name= "pingPositionsFee")
	private BigDecimal pingPositionsFee;  //平仓手续费
	
	@Column(name= "pingProfitandlossMoney")
	private BigDecimal pingProfitandlossMoney;  //平仓盈亏
	
	@Column(name= "delayMoney")
	private BigDecimal delayMoney;  //延期费
	
	@Column(name= "riskRate")
	private BigDecimal riskRate;  //分限比
	
	@Column(name= "stutus")
	private Integer stutus;  //-1,0,1
	
	
	
	
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
	 * @Date :   2016-11-28 14:23:27    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  Gao
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>createDate</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public String getCreateDate() {
		return createDate;
	}
	
	/**
	 * <p>createDate</p>
	 * @author:  Gao
	 * @param:   @param createDate
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
	/**
	 * <p>customerId</p>
	 * @author:  Gao
	 * @return:  Long 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>customerId</p>
	 * @author:  Gao
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>userCode</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public String getUserCode() {
		return userCode;
	}
	
	/**
	 * <p>userCode</p>
	 * @author:  Gao
	 * @param:   @param userCode
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	/**
	 * <p>trueName</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>trueName</p>
	 * @author:  Gao
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	/**
	 * <p>结算时间</p>
	 * @author:  Gao
	 * @return:  Date 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public Date getSettleDate() {
		return settleDate;
	}
	
	/**
	 * <p>结算时间</p>
	 * @author:  Gao
	 * @param:   @param settleDate
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
	
	
	/**
	 * <p>开始结算时间</p>
	 * @author:  Gao
	 * @return:  Date 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public Date getStartSettleDate() {
		return startSettleDate;
	}
	
	/**
	 * <p>开始结算时间</p>
	 * @author:  Gao
	 * @param:   @param startSettleDate
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setStartSettleDate(Date startSettleDate) {
		this.startSettleDate = startSettleDate;
	}
	
	
	/**
	 * <p>结束时间</p>
	 * @author:  Gao
	 * @return:  Date 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public Date getEndSettleDate() {
		return endSettleDate;
	}
	
	/**
	 * <p>结束时间</p>
	 * @author:  Gao
	 * @param:   @param endSettleDate
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setEndSettleDate(Date endSettleDate) {
		this.endSettleDate = endSettleDate;
	}
	
	
	/**
	 * <p>期初金额</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getBeginMoney() {
		return beginMoney;
	}
	
	/**
	 * <p>期初金额</p>
	 * @author:  Gao
	 * @param:   @param beginMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setBeginMoney(BigDecimal beginMoney) {
		this.beginMoney = beginMoney;
	}
	
	
	/**
	 * <p>期末金额</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getEndMoney() {
		return endMoney;
	}
	
	/**
	 * <p>期末金额</p>
	 * @author:  Gao
	 * @param:   @param endMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setEndMoney(BigDecimal endMoney) {
		this.endMoney = endMoney;
	}
	
	
	/**
	 * <p>可用金额</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getHotMoney() {
		return hotMoney;
	}
	
	/**
	 * <p>可用金额</p>
	 * @author:  Gao
	 * @param:   @param hotMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setHotMoney(BigDecimal hotMoney) {
		this.hotMoney = hotMoney;
	}
	
	
	/**
	 * <p>冻结保证金</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getColdMoney() {
		return coldMoney;
	}
	
	/**
	 * <p>冻结保证金</p>
	 * @author:  Gao
	 * @param:   @param coldMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setColdMoney(BigDecimal coldMoney) {
		this.coldMoney = coldMoney;
	}
	

	
	
	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getLendMoney() {
		return lendMoney;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setLendMoney(BigDecimal lendMoney) {
		this.lendMoney = lendMoney;
	}

	/**
	 * <p>浮动盈亏</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getFloatprofitandlossMoney() {
		return floatprofitandlossMoney;
	}
	
	/**
	 * <p>浮动盈亏</p>
	 * @author:  Gao
	 * @param:   @param floatprofitandlossMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setFloatprofitandlossMoney(BigDecimal floatprofitandlossMoney) {
		this.floatprofitandlossMoney = floatprofitandlossMoney;
	}
	
	
	/**
	 * <p>入金</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getRechargeMoney() {
		return rechargeMoney;
	}
	
	/**
	 * <p>入金</p>
	 * @author:  Gao
	 * @param:   @param rechargeMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setRechargeMoney(BigDecimal rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}
	
	
	/**
	 * <p>入金收费</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getRechargeFee() {
		return rechargeFee;
	}
	
	/**
	 * <p>入金收费</p>
	 * @author:  Gao
	 * @param:   @param rechargeFee
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setRechargeFee(BigDecimal rechargeFee) {
		this.rechargeFee = rechargeFee;
	}
	
	
	/**
	 * <p>出金</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getWithdrawMoney() {
		return withdrawMoney;
	}
	
	/**
	 * <p>出金</p>
	 * @author:  Gao
	 * @param:   @param withdrawMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setWithdrawMoney(BigDecimal withdrawMoney) {
		this.withdrawMoney = withdrawMoney;
	}
	
	
	/**
	 * <p>出金收费</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getWithdrawFee() {
		return withdrawFee;
	}
	
	/**
	 * <p>出金收费</p>
	 * @author:  Gao
	 * @param:   @param withdrawFee
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setWithdrawFee(BigDecimal withdrawFee) {
		this.withdrawFee = withdrawFee;
	}
	
	
	/**
	 * <p>建仓手续费</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getCreatePositionsFee() {
		return createPositionsFee;
	}
	
	/**
	 * <p>建仓手续费</p>
	 * @author:  Gao
	 * @param:   @param createPositionsFee
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setCreatePositionsFee(BigDecimal createPositionsFee) {
		this.createPositionsFee = createPositionsFee;
	}
	
	
	/**
	 * <p>平仓手续费</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getPingPositionsFee() {
		return pingPositionsFee;
	}
	
	/**
	 * <p>平仓手续费</p>
	 * @author:  Gao
	 * @param:   @param pingPositionsFee
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setPingPositionsFee(BigDecimal pingPositionsFee) {
		this.pingPositionsFee = pingPositionsFee;
	}
	
	
	/**
	 * <p>平仓盈亏</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getPingProfitandlossMoney() {
		return pingProfitandlossMoney;
	}
	
	/**
	 * <p>平仓盈亏</p>
	 * @author:  Gao
	 * @param:   @param pingProfitandlossMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setPingProfitandlossMoney(BigDecimal pingProfitandlossMoney) {
		this.pingProfitandlossMoney = pingProfitandlossMoney;
	}
	
	
	/**
	 * <p>延期费</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getDelayMoney() {
		return delayMoney;
	}
	
	/**
	 * <p>延期费</p>
	 * @author:  Gao
	 * @param:   @param delayMoney
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setDelayMoney(BigDecimal delayMoney) {
		this.delayMoney = delayMoney;
	}
	
	
	/**
	 * <p>分限比</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public BigDecimal getRiskRate() {
		return riskRate;
	}
	
	/**
	 * <p>分限比</p>
	 * @author:  Gao
	 * @param:   @param riskRate
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setRiskRate(BigDecimal riskRate) {
		this.riskRate = riskRate;
	}
	
	
	/**
	 * <p>-1,0,1</p>
	 * @author:  Gao
	 * @return:  Integer 
	 * @Date :   2016-11-28 14:23:27    
	 */
	public Integer getStutus() {
		return stutus;
	}
	
	/**
	 * <p>-1,0,1</p>
	 * @author:  Gao
	 * @param:   @param stutus
	 * @return:  void 
	 * @Date :   2016-11-28 14:23:27   
	 */
	public void setStutus(Integer stutus) {
		this.stutus = stutus;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getUserName() {
		return userName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
