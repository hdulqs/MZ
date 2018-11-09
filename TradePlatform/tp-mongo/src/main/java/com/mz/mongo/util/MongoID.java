/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月18日 上午10:30:16
 */
package com.mz.mongo.util;

import com.mz.core.mvc.model.BaseModel;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>mongoDB ID自增长表</p>
 * @author:         Liu Shilei 
 * @Date :          2016年4月18日 上午10:30:16 
 */
@Table(name = "autoincrement")
public class MongoID extends BaseModel {
	
	@Id
	@Column(name="id")
	private String id ;
	
	@Column(name="tableName")
	private String tableName;
	 
	@Column(name="tableId")
	private Long   tableId;

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getId() {
		return id;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTableName() {
		return tableName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getTableId() {
		return tableId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}
	
	
	

}
