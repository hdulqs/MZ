/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2016-11-28 16:50:29 
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
 * <p> SpVipAccountRecord </p>
 * @author:         liushilei
 * @Date :          2016-11-28 16:50:29  
 */
@Table(name="sp_vip_account_record")
public class SpVipAccountRecord extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "vipAccountId")
	private Long vipAccountId;  //会员账户ID
	
	@Column(name= "vipUserId")
	private Long vipUserId;  //会员ID
	
	@Column(name= "vipName")
	private String vipName;  //会员名称
	
	@Column(name= "vipNumber")
	private String vipNumber;  //会员编号
	
	@Column(name= "money")
	private BigDecimal money;  //交易金额
	
	@Column(name= "recordType")
	private String recordType;  //流水类型:0入金  1：出金
	
	
	
	
	
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getVipAccountId() {
		return vipAccountId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setVipAccountId(Long vipAccountId) {
		this.vipAccountId = vipAccountId;
	}

	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2016-11-28 16:50:29    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-28 16:50:29   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>会员ID</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2016-11-28 16:50:29    
	 */
	public Long getVipUserId() {
		return vipUserId;
	}
	
	/**
	 * <p>会员ID</p>
	 * @author:  liushilei
	 * @param:   @param vipUserId
	 * @return:  void 
	 * @Date :   2016-11-28 16:50:29   
	 */
	public void setVipUserId(Long vipUserId) {
		this.vipUserId = vipUserId;
	}
	
	
	/**
	 * <p>会员名称</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-28 16:50:29    
	 */
	public String getVipName() {
		return vipName;
	}
	
	/**
	 * <p>会员名称</p>
	 * @author:  liushilei
	 * @param:   @param vipName
	 * @return:  void 
	 * @Date :   2016-11-28 16:50:29   
	 */
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	
	
	/**
	 * <p>会员编号</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-28 16:50:29    
	 */
	public String getVipNumber() {
		return vipNumber;
	}
	
	/**
	 * <p>会员编号</p>
	 * @author:  liushilei
	 * @param:   @param vipNumber
	 * @return:  void 
	 * @Date :   2016-11-28 16:50:29   
	 */
	public void setVipNumber(String vipNumber) {
		this.vipNumber = vipNumber;
	}
	
	
	/**
	 * <p>交易金额</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2016-11-28 16:50:29    
	 */
	public BigDecimal getMoney() {
		return money;
	}
	
	/**
	 * <p>交易金额</p>
	 * @author:  liushilei
	 * @param:   @param money
	 * @return:  void 
	 * @Date :   2016-11-28 16:50:29   
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	
	/**
	 * <p>流水类型:0入金  1：出金</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-28 16:50:29    
	 */
	public String getRecordType() {
		return recordType;
	}
	
	/**
	 * <p>流水类型:0入金  1：出金</p>
	 * @author:  liushilei
	 * @param:   @param recordType
	 * @return:  void 
	 * @Date :   2016-11-28 16:50:29   
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	

}
