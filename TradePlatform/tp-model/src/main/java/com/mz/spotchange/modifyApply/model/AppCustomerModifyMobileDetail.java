/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2016-11-30 17:21:18 
 */
package com.mz.spotchange.modifyApply.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;



import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p> AppCustomerModifyMobileDetail </p>
 * @author:         shangxl
 * @Date :          2016-11-30 17:21:18  
 */
@Table(name="app_customer_modifyMobile_detail")
public class AppCustomerModifyMobileDetail extends BaseModel {
	
	
	/**  
	 * @Fields : TODO   
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //ID
	
	@Column(name= "customerId")
	private Long customerId;  //客户ID
	
	@Column(name= "trueName")
	private String trueName;  //真实姓名
	
	@Column(name= "mobilePhone")
	private String mobilePhone;  //用户编号
	
	@Column(name= "currentValue")
	private String currentValue;  //当前值
	
	@Column(name= "changeValue")
	private String changeValue;  //修改的值
	
	@Transient   //不与数据库映射的字段
	private Long appType;//修改类型
	
	
	
	/**
	 * <p>ID</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 17:21:18    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>ID</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-30 17:21:18   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public Long getAppType() {
		return appType;
	}

	public void setAppType(Long appType) {
		this.appType = appType;
	}

	/**
	 * <p>客户ID</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 17:21:18    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>客户ID</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2016-11-30 17:21:18   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>真实姓名</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:21:18    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>真实姓名</p>
	 * @author:  shangxl
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2016-11-30 17:21:18   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	/**
	 * <p>用户编号</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:21:18    
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}
	
	/**
	 * <p>用户编号</p>
	 * @author:  shangxl
	 * @param:   @param mobilePhone
	 * @return:  void 
	 * @Date :   2016-11-30 17:21:18   
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	
	/**
	 * <p>当前值</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:21:18    
	 */
	public String getCurrentValue() {
		return currentValue;
	}
	
	/**
	 * <p>当前值</p>
	 * @author:  shangxl
	 * @param:   @param currentValue
	 * @return:  void 
	 * @Date :   2016-11-30 17:21:18   
	 */
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	
	
	/**
	 * <p>修改的值</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:21:18    
	 */
	public String getChangeValue() {
		return changeValue;
	}
	
	/**
	 * <p>修改的值</p>
	 * @author:  shangxl
	 * @param:   @param changeValue
	 * @return:  void 
	 * @Date :   2016-11-30 17:21:18   
	 */
	public void setChangeValue(String changeValue) {
		this.changeValue = changeValue;
	}
	
	

}
