/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-14 13:38:51 
 */
package com.mz.manage.remote.ico.model;

import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p> AppIcoProject </p>
 * @author:         shangxl
 * @Date :          2017-07-14 13:38:51  
 */
public class AppIcoProjectDTO extends BaseModel {
	
	/**  
	 * @Fields : TODO   
	 */
	private static final long serialVersionUID = 1L;

	private Long id;  //id
	
	private String website;  //website
	
	private Integer step;  //项目资料填写到第几步 0.完善个人信息，1.项目基本信息 2.项目详细介绍 3.设置投资回报 4.提交审核 5.已完善
	
	private String projectName;  //项目名
	
	private Integer projectStage;  //产品所属阶段  0.尚未开启 1.产品开发中 2.产品已上市 3.已经盈利
	
	private Long customerId;  //用户ID
	
	private String trueName;  //真实姓名
	
	private String workAddress;  //办公地址
	
	private Integer teamSize;  //团队人数
	
	private Integer financingStage;  //融资阶段 0.未融资 1.D轮 2.C轮 3.B轮 4.A轮 5.天使轮
	
	private String info;  //项目简介
	
	private int icoDays;  //ico天数
	
	private String coinType;  //币种
	
	private Integer isLimitMoney;  //是否限制目标金额  0.否、1.是
	
	private BigDecimal sumMoney;  //目标金额
	
	private String linkman;  //联系人
	
	private String position;  //职位
	
	private String phone;  //联系人电话
	
	private Integer startDays;  //申请多少天内开始
	
	private Integer status;  //'项目状态 0.未提交 1.待审核 2.未通过 3.即将开始 4.进行中 5.已完成 6.失败'
	
	private Long support;  //支持人数
	
	private BigDecimal getMoney;  //已获取支持金额
	
	private Date startTime;  //项目开始时间
	
	private Date endTime;  //项目结束时间
	
	private String daysRemaining; //剩余天数
	
	private String distanceStartDate;//距离开始时间
	
	
	
	public String getDistanceStartDate() {
		return distanceStartDate;
	}

	public void setDistanceStartDate(String distanceStartDate) {
		this.distanceStartDate = distanceStartDate;
	}

	public String getDaysRemaining() {
		return daysRemaining;
	}

	public void setDaysRemaining(String daysRemaining) {
		this.daysRemaining = daysRemaining;
	}

	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>项目资料填写到第几步 0.完善个人信息，1.项目基本信息 2.项目详细介绍 3.设置投资回报 4.提交审核 5.已完善</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Integer getStep() {
		return step;
	}
	
	/**
	 * <p>项目资料填写到第几步 0.完善个人信息，1.项目基本信息 2.项目详细介绍 3.设置投资回报 4.提交审核 5.已完善</p>
	 * @author:  shangxl
	 * @param:   @param step
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setStep(Integer step) {
		this.step = step;
	}
	
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * <p>产品所属阶段  0.尚未开启 1.产品开发中 2.产品已上市 3.已经盈利</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Integer getProjectStage() {
		return projectStage;
	}
	
	/**
	 * <p>产品所属阶段  0.尚未开启 1.产品开发中 2.产品已上市 3.已经盈利</p>
	 * @author:  shangxl
	 * @param:   @param projectStage
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setProjectStage(Integer projectStage) {
		this.projectStage = projectStage;
	}
	
	
	/**
	 * <p>真实姓名</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getTrueName() {
		return trueName;
	}
	
	/**
	 * <p>真实姓名</p>
	 * @author:  shangxl
	 * @param:   @param trueName
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
	 * <p>办公地址</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getWorkAddress() {
		return workAddress;
	}
	
	/**
	 * <p>办公地址</p>
	 * @author:  shangxl
	 * @param:   @param workAddress
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	
	
	/**
	 * <p>团队人数</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Integer getTeamSize() {
		return teamSize;
	}
	
	/**
	 * <p>团队人数</p>
	 * @author:  shangxl
	 * @param:   @param teamSize
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setTeamSize(Integer teamSize) {
		this.teamSize = teamSize;
	}
	
	
	/**
	 * <p>融资阶段 0.未融资 1.D轮 2.C轮 3.B轮 4.A轮 5.天使轮</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Integer getFinancingStage() {
		return financingStage;
	}
	
	/**
	 * <p>融资阶段 0.未融资 1.D轮 2.C轮 3.B轮 4.A轮 5.天使轮</p>
	 * @author:  shangxl
	 * @param:   @param financingStage
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setFinancingStage(Integer financingStage) {
		this.financingStage = financingStage;
	}
	
	
	/**
	 * <p>项目简介</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getInfo() {
		return info;
	}
	
	/**
	 * <p>项目简介</p>
	 * @author:  shangxl
	 * @param:   @param info
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	
	/**
	 * <p>ico天数</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public int getIcoDays() {
		return icoDays;
	}
	
	/**
	 * <p>ico天数</p>
	 * @author:  shangxl
	 * @param:   @param icoDays
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setIcoDays(int icoDays) {
		this.icoDays = icoDays;
	}
	
	
	/**
	 * <p>币种</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getCoinType() {
		return coinType;
	}
	
	/**
	 * <p>币种</p>
	 * @author:  shangxl
	 * @param:   @param coinType
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}
	
	
	/**
	 * <p>是否限制目标金额  0.否、1.是</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Integer getIsLimitMoney() {
		return isLimitMoney;
	}
	
	/**
	 * <p>是否限制目标金额  0.否、1.是</p>
	 * @author:  shangxl
	 * @param:   @param isLimitMoney
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setIsLimitMoney(Integer isLimitMoney) {
		this.isLimitMoney = isLimitMoney;
	}
	
	
	/**
	 * <p>目标金额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public BigDecimal getSumMoney() {
		return sumMoney;
	}
	
	/**
	 * <p>目标金额</p>
	 * @author:  shangxl
	 * @param:   @param sumMoney
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}
	
	
	/**
	 * <p>联系人</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getLinkman() {
		return linkman;
	}
	
	/**
	 * <p>联系人</p>
	 * @author:  shangxl
	 * @param:   @param linkman
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	
	
	/**
	 * <p>职位</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getPosition() {
		return position;
	}
	
	/**
	 * <p>职位</p>
	 * @author:  shangxl
	 * @param:   @param position
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	
	
	/**
	 * <p>联系人电话</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * <p>联系人电话</p>
	 * @author:  shangxl
	 * @param:   @param phone
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	/**
	 * <p>申请多少天内开始</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Integer getStartDays() {
		return startDays;
	}
	
	/**
	 * <p>申请多少天内开始</p>
	 * @author:  shangxl
	 * @param:   @param startDays
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setStartDays(Integer startDays) {
		this.startDays = startDays;
	}
	
	
	/**
	 * <p>'项目状态 0.未提交 1.待审核 2.未通过 3.即将开始 4.进行中 5.已完成 6.失败'</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * <p>'项目状态 0.未提交 1.待审核 2.未通过 3.即将开始 4.进行中 5.已完成 6.失败'</p>
	 * @author:  shangxl
	 * @param:   @param status
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	/**
	 * <p>支持人数</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Long getSupport() {
		return support;
	}
	
	/**
	 * <p>支持人数</p>
	 * @author:  shangxl
	 * @param:   @param support
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setSupport(Long support) {
		this.support = support;
	}
	
	
	/**
	 * <p>已获取支持金额</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public BigDecimal getGetMoney() {
		return getMoney;
	}
	
	/**
	 * <p>已获取支持金额</p>
	 * @author:  shangxl
	 * @param:   @param getMoney
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setGetMoney(BigDecimal getMoney) {
		this.getMoney = getMoney;
	}
	
	
	/**
	 * <p>项目开始时间</p>
	 * @author:  shangxl
	 * @return:  Date 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Date getStartTime() {
		return startTime;
	}
	
	/**
	 * <p>项目开始时间</p>
	 * @author:  shangxl
	 * @param:   @param startTime
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	/**
	 * <p>项目结束时间</p>
	 * @author:  shangxl
	 * @return:  Date 
	 * @Date :   2017-07-19 13:40:56    
	 */
	public Date getEndTime() {
		return endTime;
	}
	
	/**
	 * <p>项目结束时间</p>
	 * @author:  shangxl
	 * @param:   @param endTime
	 * @return:  void 
	 * @Date :   2017-07-19 13:40:56   
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	


}
