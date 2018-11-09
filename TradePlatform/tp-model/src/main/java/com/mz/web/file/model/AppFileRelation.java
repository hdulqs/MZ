/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年09月28日  18:10:04
 */
 package com.mz.web.file.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;


/**
 * 文件关系表
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月20日 下午3:05:35
 */
/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月21日 下午5:35:23 
 */
@Table(name="app_file_relation")
public class AppFileRelation  extends BaseModel {

    @Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
    protected Long id;
    
    @Column(name="fileId")
	protected Long fileId;    //文件名
    
    @Column(name="mkey")   //关系级别
	protected String mkey; //唯一标识，由业务表的表名加id
    
    @Column(name="pkey")   //上级关系
	protected String pkey;
    
    @Column(name="orgId")
	protected Long orgId;   //组织机构ID
    
    @Column(name="userId") 
	protected Long userId;   //用户Id
    
    
    @Column(name="type")
	protected String type;   //挂载节点，，挂载app_organization ,挂载app_user
    
    @Transient
    private String name;
    
    @Transient
    private String orgpId;


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
	 * @return:     Long
	 */
	public Long getFileId() {
		return fileId;
	}


	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMkey() {
		return mkey;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMkey(String mkey) {
		this.mkey = mkey;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPkey() {
		return pkey;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}


	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getOrgId() {
		return orgId;
	}


	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}


	


	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getUserId() {
		return userId;
	}


	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getType() {
		return type;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getName() {
		return name;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrgpId() {
		return orgpId;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrgpId(String orgpId) {
		this.orgpId = orgpId;
	}

	
	

}
