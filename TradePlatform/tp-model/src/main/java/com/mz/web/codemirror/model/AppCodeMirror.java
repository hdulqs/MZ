/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menw
 * @version:     V1.0 
 * @Date:        2017-07-13 18:27:13 
 */
package com.mz.web.codemirror.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppCodeMirror </p>
 * @author:         menw
 * @Date :          2017-07-13 18:27:13  
 */
@Table(name="app_codemirror")
public class AppCodeMirror extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "isDelete")
	private String isDelete;  //isDelete
	
	@Column(name= "name")
	private String name;  //name
	
	@Column(name= "furl")
	private String furl;  //furl
	
	@Column(name= "remark")
	private String remark;  //remark
	
	@Column(name= "type")
	private String type;  //type
	
	@Column(name= "orderNo")
	private String orderNo;  //orderNo
	
	@Column(name= "pid")
	private Long pid;  //pid
	
	@Column(name= "content")
	private String content;  //content
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  menw
	 * @return:  Long 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  menw
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>isDelete</p>
	 * @author:  menw
	 * @return:  String 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public String getIsDelete() {
		return isDelete;
	}
	
	/**
	 * <p>isDelete</p>
	 * @author:  menw
	 * @param:   @param isDelete
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	
	
	/**
	 * <p>name</p>
	 * @author:  menw
	 * @return:  String 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <p>name</p>
	 * @author:  menw
	 * @param:   @param name
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * <p>furl</p>
	 * @author:  menw
	 * @return:  String 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public String getFurl() {
		return furl;
	}
	
	/**
	 * <p>furl</p>
	 * @author:  menw
	 * @param:   @param furl
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setFurl(String furl) {
		this.furl = furl;
	}
	
	
	/**
	 * <p>remark</p>
	 * @author:  menw
	 * @return:  String 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * <p>remark</p>
	 * @author:  menw
	 * @param:   @param remark
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	/**
	 * <p>type</p>
	 * @author:  menw
	 * @return:  String 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * <p>type</p>
	 * @author:  menw
	 * @param:   @param type
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	/**
	 * <p>orderNo</p>
	 * @author:  menw
	 * @return:  String 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public String getOrderNo() {
		return orderNo;
	}
	
	/**
	 * <p>orderNo</p>
	 * @author:  menw
	 * @param:   @param orderNo
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
	/**
	 * <p>pid</p>
	 * @author:  menw
	 * @return:  Long 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public Long getPid() {
		return pid;
	}
	
	/**
	 * <p>pid</p>
	 * @author:  menw
	 * @param:   @param pid
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	
	
	/**
	 * <p>content</p>
	 * @author:  menw
	 * @return:  String 
	 * @Date :   2017-07-13 18:27:13    
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * <p>content</p>
	 * @author:  menw
	 * @param:   @param content
	 * @return:  void 
	 * @Date :   2017-07-13 18:27:13   
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
