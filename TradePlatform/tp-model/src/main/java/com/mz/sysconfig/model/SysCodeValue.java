/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月24日 下午3:07:59
 */
package com.mz.spotchange.model;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * ZONGWEI
 *
 * 系统代码
 * **/

@Table(name="sys_code_value")
public class SysCodeValue extends BaseModel {
	
	@Id
	@Column(name="code_id")
	private Long code_id ;
	
	@Column(name="code")
	private String code;
	
	@Column(name="code_name")
	private String code_name;

	@Column(name="code_description")
	private String code_description;

	@Column(name="value")
	private String value;

	@Column(name="parent_code")
	private String parent_code;

	@Column(name="parent_description")
	private String parent_description;

	@Column(name="enable_flag")
	private String enable_flag;

	@Column(name="parent_enable_flag")
	private String parent_enable_flag;

	public Long getCode_id() {
		return code_id;
	}

	public void setCode_id(Long code_id) {
		this.code_id = code_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode_name() {
		return code_name;
	}

	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}

	public String getCode_description() {
		return code_description;
	}

	public void setCode_description(String code_description) {
		this.code_description = code_description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public String getParent_description() {
		return parent_description;
	}

	public void setParent_description(String parent_description) {
		this.parent_description = parent_description;
	}

	public String getEnable_flag() {
		return enable_flag;
	}

	public void setEnable_flag(String enable_flag) {
		this.enable_flag = enable_flag;
	}

	public String getParent_enable_flag() {
		return parent_enable_flag;
	}

	public void setParent_enable_flag(String parent_enable_flag) {
		this.parent_enable_flag = parent_enable_flag;
	}
}
