/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月9日 下午6:17:53
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
 * @Date :          2015年12月9日 下午6:17:53 
 */
@Entity
@Table(name = "app_user_role")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AppUserRole extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "userId", unique = false, nullable = false)
	private Long userId;
	@Column(name = "roleId", unique = false, nullable = false)
	private Long roleId;

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
	public Long getRoleId() {
		return roleId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
	
	
}
