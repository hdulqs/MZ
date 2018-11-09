/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2016-11-18 11:51:49 
 */
package com.mz.web.kings.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> Kings </p>
 * @author:         shangxl
 * @Date :          2016-11-18 11:51:49  
 */
@Table(name="s_kings")
public class Kings extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;  //id
	
	@Column(name= "name")
	private String name;  //name
	
	@Column(name= "work")
	private String work;  //work
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2016-11-18 11:51:49    
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-11-18 11:51:49   
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	/**
	 * <p>name</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-18 11:51:49    
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <p>name</p>
	 * @author:  shangxl
	 * @param:   @param name
	 * @return:  void 
	 * @Date :   2016-11-18 11:51:49   
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * <p>work</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2016-11-18 11:51:49    
	 */
	public String getWork() {
		return work;
	}
	
	/**
	 * <p>work</p>
	 * @author:  shangxl
	 * @param:   @param work
	 * @return:  void 
	 * @Date :   2016-11-18 11:51:49   
	 */
	public void setWork(String work) {
		this.work = work;
	}
	
	

}
