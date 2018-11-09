/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月18日 上午10:18:24
 */
package com.mz.tenant.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年9月18日 上午10:18:24 
 */
@Table(name = "saas_user")
public class SaasUser  extends BaseModel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	//用户名
	@Column(name="username")
	private String username ;
	
	//密码
	@Column(name="password")
	private String password;
	
	//AppUser 前缀
	@Column(name="appuserprefix")
	private String appuserprefix;
	
	/**  
	 * @Fields : 用户密码加密盐   
	 */
	@Column(name="salt")
	private String salt;
	
	//-----------------------------------------------------------------
	//是否开通appUser 账号    0没开通1开通
	@Column(name="isOpenAppUser")
	private String isOpenAppUser = "0";  
	
	
	//姓名
	@Column(name="name")
	private String name;
	
	//年龄
	@Column(name="age")
	private Integer age;
	
	//性别
	@Column(name="sex")
	private String sex;
	
	//生日
	@Column(name="birthday")
	private Date birthday;
	
	//手机号
	@Column(name="mobilePhone")
	private String mobilePhone;
	
	//QQ号
	@Column(name="qqNumber")
	private String qqNumber;
	
	//微信号
	@Column(name="weixinNumber")
	private String weixinNumber;
	
	//单位或公司
	@Column(name="company")
	private String company;
	
	//邮箱
	@Column(name="email")
	private String email;
	
	@Transient
	private Set<SaasRoles> saasRoleSet = new HashSet<SaasRoles>(0);
	
	
	
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
	 * @return:     String
	 */
	public String getUsername() {
		return username;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPassword() {
		return password;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSalt() {
		return salt;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getName() {
		return name;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getAge() {
		return age;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSex() {
		return sex;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getBirthday() {
		return birthday;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getQqNumber() {
		return qqNumber;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getWeixinNumber() {
		return weixinNumber;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setWeixinNumber(String weixinNumber) {
		this.weixinNumber = weixinNumber;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCompany() {
		return company;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getEmail() {
		return email;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Set<SaasRoles>
	 */
	public Set<SaasRoles> getSaasRoleSet() {
		return saasRoleSet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<SaasRoles>
	 */
	public void setSaasRoleSet(Set<SaasRoles> saasRoleSet) {
		this.saasRoleSet = saasRoleSet;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getAppuserprefix() {
		return appuserprefix;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setAppuserprefix(String appuserprefix) {
		this.appuserprefix = appuserprefix;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsOpenAppUser() {
		return isOpenAppUser;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsOpenAppUser(String isOpenAppUser) {
		this.isOpenAppUser = isOpenAppUser;
	}
	
	
	
	
	
}
