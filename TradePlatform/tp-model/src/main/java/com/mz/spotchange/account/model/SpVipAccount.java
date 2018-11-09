/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2016-11-27 21:38:03 
 */
package com.mz.spotchange.account.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> SpVipAccount </p>
 * @author:         liushilei
 * @Date :          2016-11-27 21:38:03  
 */
@Table(name="sp_vip_account")
public class SpVipAccount extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "vipUserId")
	private Long vipUserId;  //会员ID
	
	@Column(name= "vipName")
	private String vipName;  //会员名称
	
	@Column(name= "vipNumber")
	private String vipNumber;  //会员编号
	
	@Column(name= "ownMoneny")
	private BigDecimal ownMoneny;  //自有资金
	
	@Column(name= "vipMoney")
	private BigDecimal vipMoney;  //用户资金
	
	@Column(name= "netCashMoney")
	private BigDecimal netCashMoney;  //账户净资金
	
	
	
	


	/**
	 * <p>会员ID</p>
	 * @return:     Long
	 */
	public Long getVipUserId() {
		return vipUserId;
	}

	/** 
	 * <p>会员ID</p>
	 * @return: Long
	 */
	public void setVipUserId(Long vipUserId) {
		this.vipUserId = vipUserId;
	}

	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2016-11-27 21:38:03    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-27 21:38:03   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>会员名称</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-27 21:38:03    
	 */
	public String getVipName() {
		return vipName;
	}
	
	/**
	 * <p>会员名称</p>
	 * @author:  liushilei
	 * @param:   @param vipName
	 * @return:  void 
	 * @Date :   2016-11-27 21:38:03   
	 */
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	
	
	/**
	 * <p>会员编号</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-27 21:38:03    
	 */
	public String getVipNumber() {
		return vipNumber;
	}
	
	/**
	 * <p>会员编号</p>
	 * @author:  liushilei
	 * @param:   @param vipNumber
	 * @return:  void 
	 * @Date :   2016-11-27 21:38:03   
	 */
	public void setVipNumber(String vipNumber) {
		this.vipNumber = vipNumber;
	}
	
	
	/**
	 * <p>自有资金</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2016-11-27 21:38:03    
	 */
	public BigDecimal getOwnMoneny() {
		return ownMoneny;
	}
	
	/**
	 * <p>自有资金</p>
	 * @author:  liushilei
	 * @param:   @param ownMoneny
	 * @return:  void 
	 * @Date :   2016-11-27 21:38:03   
	 */
	public void setOwnMoneny(BigDecimal ownMoneny) {
		this.ownMoneny = ownMoneny;
	}
	
	
	/**
	 * <p>用户资金</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2016-11-27 21:38:03    
	 */
	public BigDecimal getVipMoney() {
		return vipMoney;
	}
	
	/**
	 * <p>用户资金</p>
	 * @author:  liushilei
	 * @param:   @param vipMoney
	 * @return:  void 
	 * @Date :   2016-11-27 21:38:03   
	 */
	public void setVipMoney(BigDecimal vipMoney) {
		this.vipMoney = vipMoney;
	}
	
	
	/**
	 * <p>账户净资金</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2016-11-27 21:38:03    
	 */
	public BigDecimal getNetCashMoney() {
		return netCashMoney;
	}
	
	/**
	 * <p>账户净资金</p>
	 * @author:  liushilei
	 * @param:   @param netCashMoney
	 * @return:  void 
	 * @Date :   2016-11-27 21:38:03   
	 */
	public void setNetCashMoney(BigDecimal netCashMoney) {
		this.netCashMoney = netCashMoney;
	}
	
	

}
