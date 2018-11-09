/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      denghf
 * @version:     V1.0 
 * @Date:        2017-08-16 20:24:46 
 */
package com.mz.manage.remote.ico.model;

import com.mz.core.mvc.model.BaseModel;

/**
 * <p> AppIcoEvaluate </p>
 * @author:         denghf
 * @Date :          2017-08-16 20:24:46  
 */
public class AppIcoEvaluateDTO extends BaseModel {
	
	
	private Long id;  //id
	
	private String website;  //website
	
	private String customerId;  //用户id
	
	private Long projectId;  //评论产品的id
	
	private String userName;  //用户名
	
	private String content;  //评论内容
	
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
