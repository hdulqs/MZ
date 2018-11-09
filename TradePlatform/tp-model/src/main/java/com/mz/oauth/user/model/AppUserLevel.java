/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年2月17日 下午2:50:22
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
 * @Date :          2016年2月17日 下午2:50:22 
 */
@Entity
@Table(name = "app_user_level")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AppUserLevel extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	//userId
	@Column(name="userId")
	private Long userId;
	//上级ID
	@Column(name="superiorId")
	private Long superiorId;
	
	//用户名称
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
	public Long getSuperiorId() {
		return superiorId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setSuperiorId(Long superiorId) {
		this.superiorId = superiorId;
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
	
	
	
	
	

}
