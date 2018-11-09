/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2016-11-30 17:22:48 
 */
package com.mz.spotchange.modifyApply.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppCustomerModifyBankCardDetail </p>
 * @author:         shangxl
 * @Date :          2016-11-30 17:22:48  
 */
@Table(name="app_customer_modifyBankCard_detail")
public class AppCustomerModifyBankCardDetail extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "customerId")
	private Long customerId;  //customerId
	
	@Column(name= "trueName")
	private String trueName;  //trueName
	
	@Column(name= "mobilePhone")
	private String mobilePhone;  //mobilePhone
	
	@Column(name= "currentCardBank")
	private String currentCardBank;  //当前银行名称
	
	@Column(name= "currentBankProvince")
	private String currentBankProvince;  //当前银行所在省
	
	@Column(name= "currentBankAddress")
	private String currentBankAddress;  //当前银行所在市
	
	@Column(name= "currentSubBank")
	private String currentSubBank;  //当前开户支行
	
	@Column(name= "currentCardNumber")
	private String currentCardNumber;  //当前银行账号
	
	@Column(name= "changeCardBank")
	private String changeCardBank;  //新银行名称
	
	@Column(name= "changeBankProvince")
	private String changeBankProvince;  //新银行所在省
	
	@Column(name= "changeBankAddress")
	private String changeBankAddress;  //新银行所在市
	
	@Column(name= "changeSubBank")
	private String changeSubBank;  //新开户支行
	
	@Column(name= "changeCardNumber")
	private String changeCardNumber;  //新银行账号
	
	
	@Column(name= "changeSignBank")
	private String changeSignBank;  //新签约银行
	
	
	@Column(name= "currentSignBank")
	private String currentSignBank;  //当前签约银行
	
	
	
	
	public String getChangeSignBank() {
		return changeSignBank;
	}

	public void setChangeSignBank(String changeSignBank) {
		this.changeSignBank = changeSignBank;
	}

	public String getCurrentSignBank() {
		return currentSignBank;
	}

	public void setCurrentSignBank(String currentSignBank) {
		this.currentSignBank = currentSignBank;
	}

	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>customerId</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>customerId</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>trueName</p>
	 * @author:  shangxl
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	/**
	 * <p>mobilePhone</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}
	
	/**
	 * <p>mobilePhone</p>
	 * @author:  shangxl
	 * @param:   @param mobilePhone
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	
	/**
	 * <p>当前银行名称</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getCurrentCardBank() {
		return currentCardBank;
	}
	
	/**
	 * <p>当前银行名称</p>
	 * @author:  shangxl
	 * @param:   @param currentCardBank
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setCurrentCardBank(String currentCardBank) {
		this.currentCardBank = currentCardBank;
	}
	
	
	/**
	 * <p>当前银行所在省</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getCurrentBankProvince() {
		return currentBankProvince;
	}
	
	/**
	 * <p>当前银行所在省</p>
	 * @author:  shangxl
	 * @param:   @param currentBankProvince
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setCurrentBankProvince(String currentBankProvince) {
		this.currentBankProvince = currentBankProvince;
	}
	
	
	/**
	 * <p>当前银行所在市</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getCurrentBankAddress() {
		return currentBankAddress;
	}
	
	/**
	 * <p>当前银行所在市</p>
	 * @author:  shangxl
	 * @param:   @param currentBankAddress
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setCurrentBankAddress(String currentBankAddress) {
		this.currentBankAddress = currentBankAddress;
	}
	
	
	/**
	 * <p>当前开户支行</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getCurrentSubBank() {
		return currentSubBank;
	}
	
	/**
	 * <p>当前开户支行</p>
	 * @author:  shangxl
	 * @param:   @param currentSubBank
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setCurrentSubBank(String currentSubBank) {
		this.currentSubBank = currentSubBank;
	}
	
	
	/**
	 * <p>当前银行账号</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getCurrentCardNumber() {
		return currentCardNumber;
	}
	
	/**
	 * <p>当前银行账号</p>
	 * @author:  shangxl
	 * @param:   @param currentCardNumber
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setCurrentCardNumber(String currentCardNumber) {
		this.currentCardNumber = currentCardNumber;
	}
	
	
	/**
	 * <p>新银行名称</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getChangeCardBank() {
		return changeCardBank;
	}
	
	/**
	 * <p>新银行名称</p>
	 * @author:  shangxl
	 * @param:   @param changeCardBank
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setChangeCardBank(String changeCardBank) {
		this.changeCardBank = changeCardBank;
	}
	
	
	/**
	 * <p>新银行所在省</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getChangeBankProvince() {
		return changeBankProvince;
	}
	
	/**
	 * <p>新银行所在省</p>
	 * @author:  shangxl
	 * @param:   @param changeBankProvince
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setChangeBankProvince(String changeBankProvince) {
		this.changeBankProvince = changeBankProvince;
	}
	
	
	/**
	 * <p>新银行所在市</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getChangeBankAddress() {
		return changeBankAddress;
	}
	
	/**
	 * <p>新银行所在市</p>
	 * @author:  shangxl
	 * @param:   @param changeBankAddress
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setChangeBankAddress(String changeBankAddress) {
		this.changeBankAddress = changeBankAddress;
	}
	
	
	/**
	 * <p>新开户支行</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getChangeSubBank() {
		return changeSubBank;
	}
	
	/**
	 * <p>新开户支行</p>
	 * @author:  shangxl
	 * @param:   @param changeSubBank
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setChangeSubBank(String changeSubBank) {
		this.changeSubBank = changeSubBank;
	}
	
	
	/**
	 * <p>新银行账号</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-30 17:22:48    
	 */
	public String getChangeCardNumber() {
		return changeCardNumber;
	}
	
	/**
	 * <p>新银行账号</p>
	 * @author:  shangxl
	 * @param:   @param changeCardNumber
	 * @return:  void 
	 * @Date :   2016-11-30 17:22:48   
	 */
	public void setChangeCardNumber(String changeCardNumber) {
		this.changeCardNumber = changeCardNumber;
	}
	
	

}
