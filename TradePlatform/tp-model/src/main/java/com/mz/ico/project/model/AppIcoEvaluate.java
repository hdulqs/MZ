/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      denghf
 * @version:     V1.0 
 * @Date:        2017-08-16 20:24:46 
 */
package com.mz.ico.project.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppIcoEvaluate </p>
 * @author:         denghf
 * @Date :          2017-08-16 20:24:46  
 */
@Table(name="app_ico_evaluate")
public class AppIcoEvaluate extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "website")
	private String website;  //website
	
	@Column(name= "customerId")
	private String customerId;  //用户id
	
	@Column(name= "projectId")
	private Long projectId;  //评论产品的id
	
	@Column(name= "userName")
	private String userName;  //用户名
	
	@Column(name= "content")
	private String content;  //评论内容
	
	@Column(name= "support")
	private Integer support;  //点赞人数
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  denghf
	 * @return:  Long 
	 * @Date :   2017-08-16 20:24:46    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  denghf
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-08-16 20:24:46   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-16 20:24:46    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  denghf
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-08-16 20:24:46   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-16 20:24:46    
	 */
	public String getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  denghf
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-08-16 20:24:46   
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>评论产品的id</p>
	 * @author:  denghf
	 * @return:  Long 
	 * @Date :   2017-08-16 20:24:46    
	 */
	public Long getProjectId() {
		return projectId;
	}
	
	/**
	 * <p>评论产品的id</p>
	 * @author:  denghf
	 * @param:   @param projectId
	 * @return:  void 
	 * @Date :   2017-08-16 20:24:46   
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	
	/**
	 * <p>用户名</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-16 20:24:46    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>用户名</p>
	 * @author:  denghf
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2017-08-16 20:24:46   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>评论内容</p>
	 * @author:  denghf
	 * @return:  String 
	 * @Date :   2017-08-16 20:24:46    
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * <p>评论内容</p>
	 * @author:  denghf
	 * @param:   @param content
	 * @return:  void 
	 * @Date :   2017-08-16 20:24:46   
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	
	/**
	 * <p>点赞人数</p>
	 * @author:  denghf
	 * @return:  Integer 
	 * @Date :   2017-08-16 20:24:46    
	 */
	public Integer getSupport() {
		return support;
	}
	
	/**
	 * <p>点赞人数</p>
	 * @author:  denghf
	 * @param:   @param support
	 * @return:  void 
	 * @Date :   2017-08-16 20:24:46   
	 */
	public void setSupport(Integer support) {
		this.support = support;
	}
	
	

}
