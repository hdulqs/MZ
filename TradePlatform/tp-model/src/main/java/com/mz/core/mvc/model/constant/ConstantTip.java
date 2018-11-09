/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年9月28日 下午1:37:09
 */
package com.mz.core.mvc.model.constant;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * <p> TODO</p>  提示常量类
 * @author:         Liu Shilei 
 * @Date :          2015年9月28日 下午1:37:09 
 */
@Entity
@Table(name = "constantTip")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ConstantTip {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	private String tipkey;
	
	private String tipvalue;

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
	public String getTipkey() {
		return tipkey;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTipkey(String tipkey) {
		this.tipkey = tipkey;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTipvalue() {
		return tipvalue;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTipvalue(String tipvalue) {
		this.tipvalue = tipvalue;
	}


	
	
	
	
}
