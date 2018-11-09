/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午9:39:13
 */
package com.mz.oauth.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * <p> TODO</p>     用户角色表
 * @author:         Liu Shilei 
 * @Date :          2015年9月21日 上午9:39:13 
 */
@Table(name = "app_role")
public class AppRole  extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Version
	@Column(name = "version",length = 11)
	private int version;
	
	//角色名称
	@Column(name = "name")
	private String name;
	
	//角色描述
	@Column(name = "remark")
	private String remark;
	
	//删除标识  0没有删除  1删除
	@Column(name = "isDelete")
	private String isDelete = "0";
	
	//角色类型  默认正常类型normal   
	//      分公司管理者角色  subcompany
	@Column(name = "type")
	private String type ="normal"; 
	
	//test money
	@Column(name = "money")
	private BigDecimal money = new BigDecimal("0");
	
	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getMoney() {
		return money;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	/**
	 * <p> TODO</p>
	 * @return:     int
	 */
	public int getVersion() {
		return version;
	}

	/** 
	 * <p> TODO</p>
	 * @return: int
	 */
	public void setVersion(int version) {
		this.version = version;
	}




	@Transient
	private Set<AppResource>  appResourceSet;
	
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
	 * @return:     Set<AppResource>
	 */
	public Set<AppResource> getAppResourceSet() {
		return appResourceSet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<AppResource>
	 */
	public void setAppResourceSet(Set<AppResource> appResourceSet) {
		this.appResourceSet = appResourceSet;
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
	
	
	
	
	
	
	
	
	
}
