/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao
 * @version:     V1.0 
 * @Date:        2016-11-28 18:42:05 
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
 * <p> SpVipuserCommission </p>
 * @author:         Gao
 * @Date :          2016-11-28 18:42:05  
 */
@Table(name="sp_vipuser_commission")
public class SpVipuserCommission extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "createDate")
	private String createDate;  //createDate
	
	@Column(name= "vipNumber")
	private String vipNumber;  //会员编号
	
	@Column(name= "vipName")
	private String vipName;  //会员名称
	
	@Column(name= "coinCode")
	private String coinCode;  //商品代碼
	
	@Column(name= "settleDate")
	private Date settleDate;  //结算时间

	@Column(name = "startSettleDate")
	private Date  startSettleDate;
	@Column(name = "endSettleDate")
	private Date  endSettleDate;
	@Column(name= "fee")
	private BigDecimal fee;  //反拥金额
	
	@Column(name= "feeRate")
	private BigDecimal feeRate;  //返佣比例
	
	@Column(name= "SumMoney")
	private BigDecimal SumMoney;  //收費費縂金額
	
	
	
	
	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getStartSettleDate() {
		return startSettleDate;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setStartSettleDate(Date startSettleDate) {
		this.startSettleDate = startSettleDate;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getEndSettleDate() {
		return endSettleDate;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setEndSettleDate(Date endSettleDate) {
		this.endSettleDate = endSettleDate;
	}

	/**
	 * <p>id</p>
	 * @author:  Gao
	 * @return:  Long 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  Gao
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>createDate</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public String getCreateDate() {
		return createDate;
	}
	
	/**
	 * <p>createDate</p>
	 * @author:  Gao
	 * @param:   @param createDate
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
	/**
	 * <p>会员编号</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public String getVipNumber() {
		return vipNumber;
	}
	
	/**
	 * <p>会员编号</p>
	 * @author:  Gao
	 * @param:   @param vipNumber
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setVipNumber(String vipNumber) {
		this.vipNumber = vipNumber;
	}
	
	
	/**
	 * <p>会员名称</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public String getVipName() {
		return vipName;
	}
	
	/**
	 * <p>会员名称</p>
	 * @author:  Gao
	 * @param:   @param vipName
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	
	
	/**
	 * <p>商品代碼</p>
	 * @author:  Gao
	 * @return:  String 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>商品代碼</p>
	 * @author:  Gao
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>结算时间</p>
	 * @author:  Gao
	 * @return:  Date 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public Date getSettleDate() {
		return settleDate;
	}
	
	/**
	 * <p>结算时间</p>
	 * @author:  Gao
	 * @param:   @param settleDate
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
	
	
	/**
	 * <p>反拥金额</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public BigDecimal getFee() {
		return fee;
	}
	
	/**
	 * <p>反拥金额</p>
	 * @author:  Gao
	 * @param:   @param fee
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	
	/**
	 * <p>返佣比例</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public BigDecimal getFeeRate() {
		return feeRate;
	}
	
	/**
	 * <p>返佣比例</p>
	 * @author:  Gao
	 * @param:   @param feeRate
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}
	
	
	/**
	 * <p>收費費縂金額</p>
	 * @author:  Gao
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 18:42:05    
	 */
	public BigDecimal getSumMoney() {
		return SumMoney;
	}
	
	/**
	 * <p>收費費縂金額</p>
	 * @author:  Gao
	 * @param:   @param SumMoney
	 * @return:  void 
	 * @Date :   2016-11-28 18:42:05   
	 */
	public void setSumMoney(BigDecimal SumMoney) {
		this.SumMoney = SumMoney;
	}
	
	

}
