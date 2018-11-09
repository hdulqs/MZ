/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-21 17:52:29 
 */
package com.mz.ico.project.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppIcoProjectRepay </p>
 * @author:         shangxl
 * @Date :          2017-07-21 17:52:29  
 */
@Table(name="app_ico_projectRepay")
public class AppIcoProjectRepay extends BaseModel {
	/**  
	 * @Fields : TODO   
	 */
	private static final long serialVersionUID = 3874293104733831323L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "website")
	private String website;  //website
	
	@Column(name= "projectId")
	private Long projectId;  //项目id
	
	@Column(name= "investCoinCode")
	private String investCoinCode;  //投资币种
	
	@Column(name= "isLimitedMoney")
	private Integer isLimitedMoney;  //是否限制投资币量 0.不限 1.限制
	
	@Column(name= "money")
	private BigDecimal money;  //投资币量
	
	@Column(name= "repayExplain")
	private String repayExplain;  //回报说明
	
	@Column(name= "isLimitedSize")
	private Integer isLimitedSize;  //是否限制人数 0.不限 1.限制
	
	@Column(name= "size")
	private Integer size;  //名额数
	
	@Column(name= "isLimitedSupport")
	private Integer isLimitedSupport;  //是否支持限制   0.否  1.是
	
	@Column(name= "support")
	private Integer support;  //每个用户购买此回报次数
	
	@Column(name= "isFreeShipping")
	private Integer isFreeShipping;  //是否包邮： 0.包邮  1.大陆包邮
	
	@Column(name= "paybackTime")
	private Integer paybackTime;  //回报时间：预计项目ico成功之后多少天
	
	
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * <p>website</p>
	 * @author:  shangxl
	 * @param:   @param website
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	/**
	 * <p>项目id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Long getProjectId() {
		return projectId;
	}
	
	/**
	 * <p>项目id</p>
	 * @author:  shangxl
	 * @param:   @param projectId
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	
	/**
	 * <p>投资币种</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public String getInvestCoinCode() {
		return investCoinCode;
	}
	
	/**
	 * <p>投资币种</p>
	 * @author:  shangxl
	 * @param:   @param investCoinCode
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setInvestCoinCode(String investCoinCode) {
		this.investCoinCode = investCoinCode;
	}
	
	
	/**
	 * <p>是否限制投资币量 0.不限 1.限制</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Integer getIsLimitedMoney() {
		return isLimitedMoney;
	}
	
	/**
	 * <p>是否限制投资币量 0.不限 1.限制</p>
	 * @author:  shangxl
	 * @param:   @param isLimitedMoney
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setIsLimitedMoney(Integer isLimitedMoney) {
		this.isLimitedMoney = isLimitedMoney;
	}
	
	
	/**
	 * <p>投资币量</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public BigDecimal getMoney() {
		return money;
	}
	
	/**
	 * <p>投资币量</p>
	 * @author:  shangxl
	 * @param:   @param money
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	
	/**
	 * <p>回报说明</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public String getRepayExplain() {
		return repayExplain;
	}
	
	/**
	 * <p>回报说明</p>
	 * @author:  shangxl
	 * @param:   @param repayExplain
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setRepayExplain(String repayExplain) {
		this.repayExplain = repayExplain;
	}
	
	
	/**
	 * <p>是否限制人数 0.不限 1.限制</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Integer getIsLimitedSize() {
		return isLimitedSize;
	}
	
	/**
	 * <p>是否限制人数 0.不限 1.限制</p>
	 * @author:  shangxl
	 * @param:   @param isLimitedSize
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setIsLimitedSize(Integer isLimitedSize) {
		this.isLimitedSize = isLimitedSize;
	}
	
	
	/**
	 * <p>名额数</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Integer getSize() {
		return size;
	}
	
	/**
	 * <p>名额数</p>
	 * @author:  shangxl
	 * @param:   @param size
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	
	
	/**
	 * <p>是否支持限制   0.否  1.是</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Integer getIsLimitedSupport() {
		return isLimitedSupport;
	}
	
	/**
	 * <p>是否支持限制   0.否  1.是</p>
	 * @author:  shangxl
	 * @param:   @param isLimitedSupport
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setIsLimitedSupport(Integer isLimitedSupport) {
		this.isLimitedSupport = isLimitedSupport;
	}
	
	
	/**
	 * <p>每个用户购买此回报次数</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Integer getSupport() {
		return support;
	}
	
	/**
	 * <p>每个用户购买此回报次数</p>
	 * @author:  shangxl
	 * @param:   @param support
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setSupport(Integer support) {
		this.support = support;
	}
	
	
	/**
	 * <p>是否包邮： 0.包邮  1.大陆包邮</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Integer getIsFreeShipping() {
		return isFreeShipping;
	}
	
	/**
	 * <p>是否包邮： 0.包邮  1.大陆包邮</p>
	 * @author:  shangxl
	 * @param:   @param isFreeShipping
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setIsFreeShipping(Integer isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	
	
	/**
	 * <p>回报时间：预计项目ico成功之后多少天</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-07-21 17:52:29    
	 */
	public Integer getPaybackTime() {
		return paybackTime;
	}
	
	/**
	 * <p>回报时间：预计项目ico成功之后多少天</p>
	 * @author:  shangxl
	 * @param:   @param paybackTime
	 * @return:  void 
	 * @Date :   2017-07-21 17:52:29   
	 */
	public void setPaybackTime(Integer paybackTime) {
		this.paybackTime = paybackTime;
	}
	
	

}
