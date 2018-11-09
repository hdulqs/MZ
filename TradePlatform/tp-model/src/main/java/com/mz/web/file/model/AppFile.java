/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年09月28日  18:10:04
 */
 package com.mz.web.file.model;


import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p> TODO</p>
 * @author:       Gao Mimi    
 * @Date :        2015年09月28日  18:10:04
 */

@Table(name="app_file")
public class AppFile  extends BaseModel {

    @Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "fileid", unique = true, nullable = false)
    protected Long fileid;
    
    @Column(name="filename")
	protected String filename;//文件名,展示名称
    
    @Column(name="fileTrueName") //文件真实名称
    private String fileTrueName;
    
    @Column(name="mark")
	protected String mark; //唯一标识，由业务表的表名加id,,标识文件关联表
    
    @Column(name="fileLocalpath")   //本地路径
	protected String fileLocalpath;
    
    @Column(name="fileWebPath")   //文件的web路径  存放于web服务器下的路径
	protected String fileWebPath;
    
    @Column(name="fileRemotePath")  //远程路径
	protected String fileRemotePath;
    
    @Column(name="extendname")    
	protected String extendname;    //文件类型,扩展名   jpeg,png,doc,docx......
    
    @Column(name="filesize")
	protected java.math.BigDecimal filesize;   //文件大小
    
    @Column(name="creatorid")
	protected Long creatorid;   
    
    @Column(name="remark")
	protected String remark;
    
    @Transient
    private String username;
    

	/**
	 * 主键id	 * @return Integer
       
     * @hibernate.id column="fileid" type="java.lang.Integer" generator-class="native"
	 */

	public Long getFileid() {
		return this.fileid;
	}
	
	/**
	 * Set the fileid
	 */	
	public void setFileid(Long aValue) {
		this.fileid = aValue;
	}	

	/**
	 * 	 * @return String
	 * @hibernate.property column="filename" type="java.lang.String" length="225" not-null="false" unique="false"
	 */

	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * Set the filename
	 */	
	public void setFilename(String aValue) {
		this.filename = aValue;
	}	

		

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileLocalpath() {
		return fileLocalpath;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileLocalpath(String fileLocalpath) {
		this.fileLocalpath = fileLocalpath;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileRemotePath() {
		return fileRemotePath;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileRemotePath(String fileRemotePath) {
		this.fileRemotePath = fileRemotePath;
	}

	/**
	 * 附件扩展名	 * @return String
	 * @hibernate.property column="extendname" type="java.lang.String" length="10" not-null="false" unique="false"
	 */

	public String getExtendname() {
		return this.extendname;
	}
	
	/**
	 * Set the extendname
	 */	
	public void setExtendname(String aValue) {
		this.extendname = aValue;
	}	

	/**
	 * 近件大小	 * @return java.math.BigDecimal
	 * @hibernate.property column="filesize" type="java.math.BigDecimal" length="20" not-null="false" unique="false"
	 */

	public java.math.BigDecimal getFilesize() {
		return this.filesize;
	}
	
	/**
	 * Set the filesize
	 */	
	public void setFilesize(java.math.BigDecimal aValue) {
		this.filesize = aValue;
	}	

	/**
	 * 创建人id	 * @return Integer
	 * @hibernate.property column="creatorid" type="java.lang.Integer" length="10" not-null="false" unique="false"
	 */

	public Long getCreatorid() {
		return this.creatorid;
	}
	
	/**
	 * Set the creatorid
	 */	
	public void setCreatorid(Long aValue) {
		this.creatorid = aValue;
	}	

	/**
	 * 创建时间	 * @return java.util.Date
	 * @hibernate.property column="createtime" type="java.util.Date" length="19" not-null="false" unique="false"
	 */

	/**
	 * 附件备注	 * @return String
	 * @hibernate.property column="remark" type="java.lang.String" length="300" not-null="false" unique="false"
	 */

	public String getRemark() {
		return this.remark;
	}
	
	/**
	 * Set the remark
	 */	
	public void setRemark(String aValue) {
		this.remark = aValue;
	}	


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMark() {
		return mark;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileTrueName() {
		return fileTrueName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileTrueName(String fileTrueName) {
		this.fileTrueName = fileTrueName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFileWebPath() {
		return fileWebPath;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFileWebPath(String fileWebPath) {
		this.fileWebPath = fileWebPath;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getUsername() {
		return username;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setUsername(String username) {
		this.username = username;
	}	

	
	



}
