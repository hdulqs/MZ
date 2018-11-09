/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-26 20:15:54 
 */
package com.mz.ico.project.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppIcoProjectShare </p>
 * @author:         shangxl
 * @Date :          2017-07-26 20:15:54  
 */
@Table(name="app_ico_projectShare")
public class AppIcoProjectShare extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "website")
	private String website;  //website
	
	@Column(name= "projectId")
	private Long projectId;  //projectId
	
	@Column(name= "customerId")
	private Long customerId;  //customerId
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-26 20:15:54    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-07-26 20:15:54   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-26 20:15:54    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-07-26 20:15:54   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>projectId</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-26 20:15:54    
	 */
	public Long getProjectId() {
		return projectId;
	}
	
	/**
	 * <p>projectId</p>
	 * @author:  shangxl
	 * @param:   @param projectId
	 * @return:  void 
	 * @Date :   2017-07-26 20:15:54   
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	
	/**
	 * <p>customerId</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-26 20:15:54    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>customerId</p>
	 * @author:  shangxl
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-07-26 20:15:54   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	

}
