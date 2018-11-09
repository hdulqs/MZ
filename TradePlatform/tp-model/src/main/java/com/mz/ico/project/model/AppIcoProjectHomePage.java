/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-21 16:51:55 
 */
package com.mz.ico.project.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppIcoProjectHomePage </p>
 * @author:         shangxl
 * @Date :          2017-07-21 16:51:55  
 */
@Table(name="app_ico_projectHomePage")
public class AppIcoProjectHomePage extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "website")
	private String website;  //website
	
	@Column(name= "projectId")
	private Long projectId;  //项目id
	
	@Column(name= "logoUrl")
	private String logoUrl;  //项目logo地址
	
	@Column(name= "content")
	private String content;  //文章主页内容
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-21 16:51:55    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-07-21 16:51:55   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-21 16:51:55    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-07-21 16:51:55   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>项目id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-21 16:51:55    
	 */
	public Long getProjectId() {
		return projectId;
	}
	
	/**
	 * <p>项目id</p>
	 * @author:  shangxl
	 * @param:   @param projectId
	 * @return:  void 
	 * @Date :   2017-07-21 16:51:55   
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	
	/**
	 * <p>项目logo地址</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-21 16:51:55    
	 */
	public String getLogoUrl() {
		return logoUrl;
	}
	
	/**
	 * <p>项目logo地址</p>
	 * @author:  shangxl
	 * @param:   @param logoUrl
	 * @return:  void 
	 * @Date :   2017-07-21 16:51:55   
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	
	/**
	 * <p>文章主页内容</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-21 16:51:55    
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * <p>文章主页内容</p>
	 * @author:  shangxl
	 * @param:   @param content
	 * @return:  void 
	 * @Date :   2017-07-21 16:51:55   
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
