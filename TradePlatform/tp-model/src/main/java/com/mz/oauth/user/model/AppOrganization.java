/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月2日 上午11:14:48
 */
package com.mz.oauth.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月2日 上午11:14:48
 */
@Table(name = "app_organization")
public class AppOrganization extends BaseModel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	/**  
	 *   root  集团   --根节点 
	 *   company 公司
	 *   shop  门店
	 *   department 部门
	 *   
	 */
	@Column(name="type")
	private String type;
	
	//父级组织机构
	@Column(name="pid")
	private Long pid;
	
	//删除标识 0未删除  1删除
	@Column(name="isDelete")
	private  String isDelete = "0";
	
	//排序号
	@Column(name="orderNo")
	private String orderNo = "1" ; 
	
	//logol图片路径
	@Column(name="logoPath")
	private String logoPath;
	
	//集团名称/分公司名称/部门名称
	@Column(name="name")
	private String name ;
	
	//集团简称/分公司简称/部门简称
	@Column(name="shortName")
	private String shortName;
	
	//分公司编号/部门编号
	@Column(name="companyNo")
	private String companyNo;
	
	//分公司英文简称
	@Column(name="englishName")
	private String englishName;
	
	//联系人
	@Column(name="person")
	private String person;
	
	//联系人电话
	@Column(name="mobile")
	private String mobile;
	
	//传真
	@Column(name="fax")
	private String fax;
	
	//成立日期
	@Column(name="setDate")
	private Date setDate;
	
	//地址
	@Column(name="address")
	private String address;
	
	//邮政编码
	@Column(name="postalcode")
	private String postalcode;
	
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
	public String getType() {
		return type;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getShortName() {
		return shortName;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCompanyNo() {
		return companyNo;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getEnglishName() {
		return englishName;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPerson() {
		return person;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPerson(String person) {
		this.person = person;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMobile() {
		return mobile;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFax() {
		return fax;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}


	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getSetDate() {
		return setDate;
	}


	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setSetDate(Date setDate) {
		this.setDate = setDate;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getAddress() {
		return address;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setAddress(String address) {
		this.address = address;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPostalcode() {
		return postalcode;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
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
	public String getLogoPath() {
		return logoPath;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}


	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getPid() {
		return pid;
	}


	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setPid(Long pid) {
		this.pid = pid;
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
	public String getOrderNo() {
		return orderNo;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
	
}
