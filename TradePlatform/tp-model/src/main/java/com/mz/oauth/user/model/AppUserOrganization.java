/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月9日 下午6:35:18
 */
package com.mz.oauth.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

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
 * @Date :          2015年12月9日 下午6:35:18 
 */
@Entity
@Table(name = "app_user_organization")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AppUserOrganization extends BaseModel{

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "userId", unique = false, nullable = false)
	private Long userId;
	
	@Column(name = "organizationId", unique = false, nullable = false)
	private Long organizationId;
	
	//company     所属公司
	//subcompany  所属分公司
	//shop        所属门店
	//department  所属部门
	@Column(name = "type", unique = false, nullable = false)
	private String type;  
	

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
	public Long getUserId() {
		return userId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getOrganizationId() {
		return organizationId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
