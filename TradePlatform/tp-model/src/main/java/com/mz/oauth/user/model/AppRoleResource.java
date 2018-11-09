/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月9日 下午6:33:16
 */
package com.mz.oauth.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月9日 下午6:33:16 
 */
@Table(name = "app_role_resource")
public class AppRoleResource extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "roleId", unique = false, nullable = false)
	private Long roleId;
	
	@Column(name = "resourceId", unique = false, nullable = false)
	private Long resourceId;
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
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getResourceId() {
		return resourceId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	
	
	

}
