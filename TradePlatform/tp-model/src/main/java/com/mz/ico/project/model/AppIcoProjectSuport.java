/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-26 18:09:21 
 */
package com.mz.ico.project.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppIcoProjectSuport </p>
 * @author:         shangxl
 * @Date :          2017-07-26 18:09:21  
 */
@Table(name="app_ico_projectSuport")
public class AppIcoProjectSuport extends BaseModel {
	
	
	/**  
	 * @Fields : TODO   
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "website")
	private String website;  //website
	
	@Column(name= "customerId")
	private String customerId;  //用户id
	
	@Column(name= "projectId")
	private Long projectId;  //项目id
	
	@Column(name= "userName")
	private String userName;  //用户名
	
	@Column(name= "coinType")
	private String coinType;  //支持币种  cny人民币
	
	@Column(name= "money")
	private BigDecimal money;  //支持币个数
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-26 18:09:21    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-07-26 18:09:21   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-26 18:09:21    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-07-26 18:09:21   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-26 18:09:21    
	 */
	public String getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-07-26 18:09:21   
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * <p>用户名</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-26 18:09:21    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>用户名</p>
	 * @author:  shangxl
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2017-07-26 18:09:21   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>支持币种  cny人民币</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-26 18:09:21    
	 */
	public String getCoinType() {
		return coinType;
	}
	
	/**
	 * <p>支持币种  cny人民币</p>
	 * @author:  shangxl
	 * @param:   @param coinType
	 * @return:  void 
	 * @Date :   2017-07-26 18:09:21   
	 */
	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}
	
	
	/**
	 * <p>支持币个数</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-07-26 18:09:21    
	 */
	public BigDecimal getMoney() {
		return money;
	}
	
	/**
	 * <p>支持币个数</p>
	 * @author:  shangxl
	 * @param:   @param money
	 * @return:  void 
	 * @Date :   2017-07-26 18:09:21   
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	

}
