/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年3月3日 下午2:11:28
 */
package com.mz.oauth.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年3月3日 下午2:11:28 
 */
@Entity
@Table(name = "app_user_info")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AppUserInfo extends BaseModel{
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "userId", unique = false, nullable = false)
	private Long  userId;
	
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
	private String sex;
	
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
	
	

}
