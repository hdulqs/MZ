/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2016-11-30 16:55:04 
 */
package com.mz.spotchange.modifyApply.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppCustomerModifyApply </p>
 * @author:         shangxl
 * @Date :          2016-11-30 16:55:04  
 */
@Table(name="app_customer_modifyApply")
public class AppCustomerModifyApply extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //ID
	
	@Column(name= "website")
	private String website;  //站点类型
	
	@Column(name= "applyId")
	private Long applyId;  //修改申请详情表ID
	
	@Column(name= "customerId")
	private Long customerId;  //客户ID
	
	@Column(name= "mobilePhone")
	private String mobilePhone;  //用户编号、手机号码
	
	@Column(name= "trueName")
	private String trueName;  //真实姓名
	
	@Column(name= "orderNumber")
	private String orderNumber;  //订单号
	
	@Column(name= "appType")
	private Long appType;  //处理类型  1.修改手机 2.修改邮箱 3.修改银行卡
	
	@Column(name= "dealState")
	private Long dealState;  //处理状态0.待处理 1.已处理 2.已拒绝
	
	@Column(name= "dealtime")
	private Date dealtime;  //处理时间
	
	@Column(name= "refuseReason")
	private String refuseReason;  //拒绝原因
	
	@Column(name= "userName")
	private String userName;  //操作人
	
	@Column(name= "userId")
	private Long userId;  //操作人ID
	
	
	
	
	/**
	 * <p>ID</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>ID</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>站点类型</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>站点类型</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>修改申请详情表ID</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public Long getApplyId() {
		return applyId;
	}
	
	/**
	 * <p>修改申请详情表ID</p>
	 * @author:  shangxl
	 * @param:   @param applyId
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
	
	
	/**
	 * <p>客户ID</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>客户ID</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>用户编号、手机号码</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}
	
	/**
	 * <p>用户编号、手机号码</p>
	 * @author:  shangxl
	 * @param:   @param mobilePhone
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	
	/**
	 * <p>真实姓名</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>真实姓名</p>
	 * @author:  shangxl
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	/**
	 * <p>订单号</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	
	/**
	 * <p>订单号</p>
	 * @author:  shangxl
	 * @param:   @param orderNumber
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	
	/**
	 * <p>处理类型</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public Long getAppType() {
		return appType;
	}
	
	/**
	 * <p>处理类型</p>
	 * @author:  shangxl
	 * @param:   @param appType
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setAppType(Long appType) {
		this.appType = appType;
	}
	
	
	/**
	 * <p>处理状态</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public Long getDealState() {
		return dealState;
	}
	
	/**
	 * <p>处理状态</p>
	 * @author:  shangxl
	 * @param:   @param dealState
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setDealState(Long dealState) {
		this.dealState = dealState;
	}
	
	
	/**
	 * <p>处理时间</p>
	 * @author:  shangxl
	 * @return:  Date 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public Date getDealtime() {
		return dealtime;
	}
	
	/**
	 * <p>处理时间</p>
	 * @author:  shangxl
	 * @param:   @param dealtime
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setDealtime(Date dealtime) {
		this.dealtime = dealtime;
	}
	
	
	/**
	 * <p>拒绝原因</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public String getRefuseReason() {
		return refuseReason;
	}
	
	/**
	 * <p>拒绝原因</p>
	 * @author:  shangxl
	 * @param:   @param refuseReason
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	
	
	/**
	 * <p>操作人</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>操作人</p>
	 * @author:  shangxl
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>操作人ID</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 16:55:04    
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * <p>操作人ID</p>
	 * @author:  shangxl
	 * @param:   @param userId
	 * @return:  void 
	 * @Date :   2016-11-30 16:55:04   
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

}
