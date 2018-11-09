/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月18日 上午10:18:24
 */
package com.mz.oauth.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.util.Date;
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
@Table(name = "app_user")
public class AppUser extends BaseModel {
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
	
	//appuser登录时的账号前缀
	//此字段在去掉saas平台后就没用了，现将此字段做为，现货后台账户类型用  2016/10/19
	//hr 平台账户
	//vip 会员账户
	//agency 代理商账户
	@Column(name="appuserprefix")
	private String appuserprefix;
	
	//删除标识 0未删除  1删除
	@Column(name="isDelete")
	private String isDelete = "0"; 
	
	//禁用标识 0未禁用  1禁用
	@Column(name="isLock")
	private String isLock = "0";
	
	
	/**  
	 * @Fields : 用户密码加密盐   
	 */
	@Column(name="salt")
	private String salt;
	
	//个人形象图路径
	@Column(name="picturePath")
	private String picturePath;
	
	//姓名
	@Column(name="name")
	private String name;
	
	//员工编号
	@Column(name="number")
	private String number;
	
	//邮箱
	@Column(name="email")
	private String email;
	
	//入职时间
	@Column(name="workDate")
	private Date workDate;
	
	//家庭住址
	@Column(name="homeAddress")
	private String homeAddress;
	
	//年龄
	@Column(name="age")
	private Integer age;
	
	//性别
	@Column(name="sex")
	private String sex;   //  0 男，1女
	
	//生日
	@Column(name="birthday")
	private Date birthday;
	
	//手机号
	@Column(name="mobilePhone")
	private String mobilePhone;
	
	//第二联系电话
	@Column(name="secondPhone")
	private String secondPhone;
	
	//QQ号
	@Column(name="qqNumber")
	private String qqNumber;
	
	//微信号
	@Column(name="weixinNumber")
	private String weixinNumber;
	
	//备注
	@Column(name="remark")
	private String remark;
	
	//公司List
	@Transient
	private Set<AppOrganization>  companySet;   
	//门店List
	@Transient
	private Set<AppOrganization>  shopSet;  
	//部门List
	@Transient
	private Set<AppOrganization>  departmentSet;   
	//角色List
	@Transient
	private Set<AppRole> appRoleSet;
	
	@Transient
	private String roleName;
	
	@Transient
	private String departmentName;
	
	
//	{ name : 上明  , departmentList[{id:1},{id:2}]}
	
//	departmentList.id
	
//	departmentList : "1,2,3"
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
	public String getNumber() {
		return number;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * @return:     Date
	 */
	public Date getWorkDate() {
		return workDate;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getHomeAddress() {
		return homeAddress;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSecondPhone() {
		return secondPhone;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSecondPhone(String secondPhone) {
		this.secondPhone = secondPhone;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRemark() {
		return remark;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPicturePath() {
		return picturePath;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsDelete() {
		return isDelete;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsLock() {
		return isLock;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Set<AppOrganization>
	 */
	public Set<AppOrganization> getCompanySet() {
		return companySet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<AppOrganization>
	 */
	public void setCompanySet(Set<AppOrganization> companySet) {
		this.companySet = companySet;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Set<AppOrganization>
	 */
	public Set<AppOrganization> getShopSet() {
		return shopSet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<AppOrganization>
	 */
	public void setShopSet(Set<AppOrganization> shopSet) {
		this.shopSet = shopSet;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Set<AppOrganization>
	 */
	public Set<AppOrganization> getDepartmentSet() {
		return departmentSet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<AppOrganization>
	 */
	public void setDepartmentSet(Set<AppOrganization> departmentSet) {
		this.departmentSet = departmentSet;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Set<AppRole>
	 */
	public Set<AppRole> getAppRoleSet() {
		return appRoleSet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<AppRole>
	 */
	public void setAppRoleSet(Set<AppRole> appRoleSet) {
		this.appRoleSet = appRoleSet;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRoleName() {
		return roleName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	
	
	
	
}
