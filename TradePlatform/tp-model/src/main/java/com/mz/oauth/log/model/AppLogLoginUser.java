/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月22日 上午11:19:10
 */
package com.mz.oauth.log.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年4月22日 上午11:19:10 
 */
@Table(name = "app_log_login_customer")
public class AppLogLoginUser extends BaseModel{
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name="customerId")
	private Long customerId;   //用户ID
	
	@Column(name="userName")
	private String userName;  //用户名
	
	@Column(name="loginTime")
	private Date loginTime;  //登录时间
	
	@Column(name="ip")
	private String ip;  //登录IP
	
	@Column(name="isLogin")
	private Integer isLogin;  //是否成功登录 0失败1成功

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getId() {
		return id;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIp() {
		return ip;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getIsLogin() {
		return isLogin;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setIsLogin(Integer isLogin) {
		this.isLogin = isLogin;
	}
	
	
	
	
}
