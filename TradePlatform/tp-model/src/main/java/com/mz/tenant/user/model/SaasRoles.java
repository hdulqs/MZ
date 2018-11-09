/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月21日 上午9:39:13
 */
package com.mz.tenant.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.MgrBaseModel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * <p> TODO</p>     用户角色表
 * @author:         Liu Shilei 
 * @Date :          2015年9月21日 上午9:39:13 
 */
@Entity
@Table(name = "saas_roles")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SaasRoles  extends MgrBaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Transient
	private Set<SaasUser> saasUserSet = new HashSet<SaasUser>(0);
	
	@Transient
	private Set<SaasResource> saasResourceSet = new HashSet<SaasResource>(0);
	
	//角色名称
	@Column(name="name")
	private String name;
	
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
	 * @return:     Set<SaasUser>
	 */
	public Set<SaasUser> getSaasUserSet() {
		return saasUserSet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<SaasUser>
	 */
	public void setSaasUserSet(Set<SaasUser> saasUserSet) {
		this.saasUserSet = saasUserSet;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Set<SaasResource>
	 */
	public Set<SaasResource> getSaasResourceSet() {
		return saasResourceSet;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Set<SaasResource>
	 */
	public void setSaasResourceSet(Set<SaasResource> saasResourceSet) {
		this.saasResourceSet = saasResourceSet;
	}




	
	
	
	
	
}
