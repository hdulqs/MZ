/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月19日 下午3:40:24
 */
package com.mz.core.quartz;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年4月19日 下午3:40:24 
 */
@Table(name="ScheduleJob")
public class ScheduleJob implements Serializable {
	public static final String STATUS_RUNNING = "1";  
    public static final String STATUS_NOT_RUNNING = "0";  
    public static final String CONCURRENT_IS = "1";  
    public static final String CONCURRENT_NOT = "0";  
    
    @Id
	@Column(name = "jobId", unique = true, nullable = false)
    private Long jobId;  
    
    @Column(name = "createTime")
    private Date createTime;  
    
    @Column(name = "updateTime")
    private Date updateTime;  
    /** 
     * 任务名称 
     */  
    @Column(name = "jobName")
    private String jobName;  
    /** 
     * 任务分组 
     */  
    @Column(name = "jobGroup")
    private String jobGroup;  
    /** 
     * 任务状态 是否启动任务 
     */  
    @Column(name = "jobStatus")
    private String jobStatus;  
    /** 
     * cron表达式 
     */  
    @Column(name = "cronExpression")
    private String cronExpression;  
    /** 
     * 描述 
     */  
    @Column(name = "description")
    private String description;  
    /** 
     * 任务执行时调用哪个类的方法 包名+类名 
     */  
    @Column(name = "beanClass")
    private String beanClass;  
    /** 
     * 任务是否有状态 
     */  
    @Column(name = "isConcurrent")
    private String isConcurrent;  
    /** 
     * config bean
     */  
    @Column(name = "springId")
    private String springId;  
    /** 
     * 任务调用的方法名 
     */  
    @Column(name = "methodName")
    private String methodName;
    
    /**
     * 方法参数
     */
    @Column(name = "methodArgs")
    private Object[] methodArgs;
    
    @Column(name="scheduleTime")
    private String scheduleTime;
    
    
	/**
	 * <p> TODO</p>
	 * @return:     Long
	 */
	public Long getJobId() {
		return jobId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Long
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getJobName() {
		return jobName;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getJobGroup() {
		return jobGroup;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getJobStatus() {
		return jobStatus;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCronExpression() {
		return cronExpression;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getDescription() {
		return description;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBeanClass() {
		return beanClass;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsConcurrent() {
		return isConcurrent;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsConcurrent(String isConcurrent) {
		this.isConcurrent = isConcurrent;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSpringId() {
		return springId;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSpringId(String springId) {
		this.springId = springId;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMethodName() {
		return methodName;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * <p> TODO</p>
	 * @return:     Object[]
	 */
	public Object[] getMethodArgs() {
		return methodArgs;
	}
	/** 
	 * <p> TODO</p>
	 * @return: Object[]
	 */
	public void setMethodArgs(Object[] methodArgs) {
		this.methodArgs = methodArgs;
	}
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getScheduleTime() {
		return scheduleTime;
	}
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	
	
	
}
