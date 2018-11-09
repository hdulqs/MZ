/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2016-11-29 17:13:33 
 */
package com.mz.spotchange.entrust.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> SpPositionPing </p>
 * @author:         liushilei
 * @Date :          2016-11-29 17:13:33  
 */
@Table(name="sp_position_ping")
public class SpPositionPing extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "pingNum")
	private String pingNum;  //平仓单号
	
	@Column(name= "customerId")
	private Long customerId;  //用户id
	
	@Column(name= "userCode")
	private String userCode;  //userCode
	
	@Column(name= "userName")
	private String userName;  //userName
	
	@Column(name= "trueName")
	private String trueName;  //trueName
	
	@Column(name= "vipNumber")
	private String vipNumber;  //会员编号
	
	@Column(name= "status")
	private Integer status;  //1平仓中2平仓完成
	
	@Column(name= "currencyType")
	private String currencyType;  //currencyType
	
	@Column(name= "website")
	private String website;  //website
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>平仓单号</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public String getPingNum() {
		return pingNum;
	}
	
	/**
	 * <p>平仓单号</p>
	 * @author:  liushilei
	 * @param:   @param pingNum
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setPingNum(String pingNum) {
		this.pingNum = pingNum;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  liushilei
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>userCode</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public String getUserCode() {
		return userCode;
	}
	
	/**
	 * <p>userCode</p>
	 * @author:  liushilei
	 * @param:   @param userCode
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	/**
	 * <p>userName</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>userName</p>
	 * @author:  liushilei
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>trueName</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>trueName</p>
	 * @author:  liushilei
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	/**
	 * <p>会员编号</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public String getVipNumber() {
		return vipNumber;
	}
	
	/**
	 * <p>会员编号</p>
	 * @author:  liushilei
	 * @param:   @param vipNumber
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setVipNumber(String vipNumber) {
		this.vipNumber = vipNumber;
	}
	
	
	/**
	 * <p>1平仓中2平仓完成</p>
	 * @author:  liushilei
	 * @return:  Integer 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * <p>1平仓中2平仓完成</p>
	 * @author:  liushilei
	 * @param:   @param status
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	/**
	 * <p>currencyType</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public String getCurrencyType() {
		return currencyType;
	}
	
	/**
	 * <p>currencyType</p>
	 * @author:  liushilei
	 * @param:   @param currencyType
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-11-29 17:13:33    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  liushilei
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2016-11-29 17:13:33   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	

}
