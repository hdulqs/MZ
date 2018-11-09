/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      denghf
 * @version:     V1.0 
 * @Date:        2017-08-18 09:58:28 
 */
package com.mz.manage.remote.ico.model;


import java.math.BigDecimal;

import com.mz.core.mvc.model.BaseModel;


/**
 * <p> AppIcoPay </p>
 * @author:         denghf
 * @Date :          2017-08-18 09:58:28  
 */
public class AppIcoPayDTO extends BaseModel {
	
	
	private Long id;  //id
	
	private String website;  //website
	
	private Integer step;  //订单资料填写到第几步 1.填写支持金额,2.确认投资信息,3.在线支付
	
	private Long customerId;  //用户id
	
	private Long projectId;  //项目id
	
	private String projectName;  //项目名
	
	private String coinType;  //币种
	
	private BigDecimal getMoney;  //支持金额
	
	private BigDecimal payMoney;  //已支付金额
	
	private String payType;  //支付方式
	
	private String proportion;  //所占比例
	
	private String Remarks1;  //备注一
	
	private String Remarks2;  //备注二
	
	private String Remarks3;  //备注三
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  denghf
	 * @return:  Long 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  denghf
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  denghf
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>订单资料填写到第几步 1.填写支持金额,2.确认投资信息,3.在线支付</p>
	 * @author:  denghf
	 * @return:  Integer 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public Integer getStep() {
		return step;
	}
	
	/**
	 * <p>订单资料填写到第几步 1.填写支持金额,2.确认投资信息,3.在线支付</p>
	 * @author:  denghf
	 * @param:   @param step
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setStep(Integer step) {
		this.step = step;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  denghf
	 * @return:  Long 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  denghf
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>项目id</p>
	 * @author:  denghf
	 * @return:  Long 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public Long getProjectId() {
		return projectId;
	}
	
	/**
	 * <p>项目id</p>
	 * @author:  denghf
	 * @param:   @param projectId
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	
	/**
	 * <p>项目名</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * <p>项目名</p>
	 * @author:  denghf
	 * @param:   @param projectName
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	/**
	 * <p>币种</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getCoinType() {
		return coinType;
	}
	
	/**
	 * <p>币种</p>
	 * @author:  denghf
	 * @param:   @param coinType
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}
	
	
	/**
	 * <p>支持金额</p>
	 * @author:  denghf
	 * @return:  BigDecimal 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public BigDecimal getGetMoney() {
		return getMoney;
	}
	
	/**
	 * <p>支持金额</p>
	 * @author:  denghf
	 * @param:   @param getMoney
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setGetMoney(BigDecimal getMoney) {
		this.getMoney = getMoney;
	}
	
	
	/**
	 * <p>已支付金额</p>
	 * @author:  denghf
	 * @return:  BigDecimal 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	
	/**
	 * <p>已支付金额</p>
	 * @author:  denghf
	 * @param:   @param payMoney
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	
	
	/**
	 * <p>支付方式</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getPayType() {
		return payType;
	}
	
	/**
	 * <p>支付方式</p>
	 * @author:  denghf
	 * @param:   @param payType
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	
	/**
	 * <p>所占比例</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getProportion() {
		return proportion;
	}
	
	/**
	 * <p>所占比例</p>
	 * @author:  denghf
	 * @param:   @param proportion
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	
	
	/**
	 * <p>备注一</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getRemarks1() {
		return Remarks1;
	}
	
	/**
	 * <p>备注一</p>
	 * @author:  denghf
	 * @param:   @param Remarks1
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setRemarks1(String Remarks1) {
		this.Remarks1 = Remarks1;
	}
	
	
	/**
	 * <p>备注二</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getRemarks2() {
		return Remarks2;
	}
	
	/**
	 * <p>备注二</p>
	 * @author:  denghf
	 * @param:   @param Remarks2
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setRemarks2(String Remarks2) {
		this.Remarks2 = Remarks2;
	}
	
	
	/**
	 * <p>备注三</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-18 09:58:28    
	 */
	public String getRemarks3() {
		return Remarks3;
	}
	
	/**
	 * <p>备注三</p>
	 * @author:  denghf
	 * @param:   @param Remarks3
	 * @return:  void 
	 * @Date :   2017-08-18 09:58:28   
	 */
	public void setRemarks3(String Remarks3) {
		this.Remarks3 = Remarks3;
	}
	
	

}
