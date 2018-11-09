/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2016-10-20 15:00:55 
 */
package com.mz.spotchange.user.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> SpVipRole </p>
 * @author:         liushilei
 * @Date :          2016-10-20 15:00:55  
 */
@Table(name="sp_vip_role")
public class SpVipRole extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "roleId")
	private Long roleId;  //roleId
	
	@Column(name= "name")
	private String name;  //角色名称
	
	@Column(name= "type")
	private String type;  //角色类型
	
	@Column(name= "remark")
	private String remark;  //角色类型
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2016-10-20 15:00:55    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-10-20 15:00:55   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>roleId</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2016-10-20 15:00:55    
	 */
	public Long getRoleId() {
		return roleId;
	}
	
	/**
	 * <p>roleId</p>
	 * @author:  liushilei
	 * @param:   @param roleId
	 * @return:  void 
	 * @Date :   2016-10-20 15:00:55   
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
	/**
	 * <p>角色名称</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-10-20 15:00:55    
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <p>角色名称</p>
	 * @author:  liushilei
	 * @param:   @param name
	 * @return:  void 
	 * @Date :   2016-10-20 15:00:55   
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * <p>角色类型</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-10-20 15:00:55    
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * <p>角色类型</p>
	 * @author:  liushilei
	 * @param:   @param type
	 * @return:  void 
	 * @Date :   2016-10-20 15:00:55   
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	/**
	 * <p>角色类型</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2016-10-20 15:00:55    
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * <p>角色类型</p>
	 * @author:  liushilei
	 * @param:   @param remark
	 * @return:  void 
	 * @Date :   2016-10-20 15:00:55   
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
