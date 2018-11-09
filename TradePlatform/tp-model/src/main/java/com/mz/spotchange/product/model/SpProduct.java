/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:     V1.0 
 * @Date:        2016-10-17 14:25:23 
 */
package com.mz.spotchange.product.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import com.mz.core.mvc.model.BaseModel;



import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> SpProduct </p>
 * @author:         Wu Shuiming
 * @Date :          2016-10-17 14:25:23  
 */
@Table(name="sp_product")
public class SpProduct extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //主键id
	
	@Column(name= "name")
	private String name;  //产品名称
	
	@Column(name= "coinCode")
	private String coinCode;  //产品代码
	
	@Column(name= "picturePath")
	private String picturePath;  //产品图片
	
	@Column(name= "belongTo")
	private Integer belongTo;  //归属哪个平台(0表示本平台  1 表示上交所)
	
	@Column(name= "states")
	private Integer states;  //状态参数(0准备   1交易状态    2表示退市  5表示已删状态)
	
	@Column(name= "sort")
	private Integer sort;  //排序字段
	
	@Column(name= "productReferral")
	private String productReferral;  //产品介绍
	
	@Column(name= "denoMinated")
	private String denoMinated;  //计价单位（元/克   元/千克）
	
	@Column(name= "contractUnit")
	private String contractUnit;  //合约单位（元/克   元/千克）
	
	@Column(name= "contractUnitCount")
	private BigDecimal contractUnitCount;  //
	
	@Column(name= "type")
	private String type;  //'AG,BU,CO,CU'
	@Column(name= "website")
	private String website; // 默认中国站 
	
	
	
	
	/**
	 * <p> TODO</p>
	 * @return:     BigDecimal
	 */
	public BigDecimal getContractUnitCount() {
		return contractUnitCount;
	}

	/** 
	 * <p> TODO</p>
	 * @return: BigDecimal
	 */
	public void setContractUnitCount(BigDecimal contractUnitCount) {
		this.contractUnitCount = contractUnitCount;
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
	public String getWebsite() {
		return website;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * <p>主键id</p>
	 * @author:  Wu Shuiming
	 * @return:  Long 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>主键id</p>
	 * @author:  Wu Shuiming
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>产品名称</p>
	 * @author:  Wu Shuiming
	 * @return:  String 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <p>产品名称</p>
	 * @author:  Wu Shuiming
	 * @param:   @param name
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	/**
	 * <p>产品图片</p>
	 * @author:  Wu Shuiming
	 * @return:  String 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public String getPicturePath() {
		return picturePath;
	}
	
	/**
	 * <p>产品图片</p>
	 * @author:  Wu Shuiming
	 * @param:   @param picturePath
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	
	
	/**
	 * <p>归属哪个平台(0表示本平台  1 表示上交所)</p>
	 * @author:  Wu Shuiming
	 * @return:  Integer 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public Integer getBelongTo() {
		return belongTo;
	}
	
	/**
	 * <p>归属哪个平台(0表示本平台  1 表示上交所)</p>
	 * @author:  Wu Shuiming
	 * @param:   @param belongTo
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setBelongTo(Integer belongTo) {
		this.belongTo = belongTo;
	}
	
	
	/**
	 * <p>状态参数(0 表示正在交易   1表示准备状态    2表示退市  5表示已删状态)</p>
	 * @author:  Wu Shuiming
	 * @return:  Integer 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public Integer getStates() {
		return states;
	}
	
	/**
	 * <p>状态参数(0 表示正在交易   1表示准备状态    2表示退市  5表示已删状态)</p>
	 * @author:  Wu Shuiming
	 * @param:   @param states
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setStates(Integer states) {
		this.states = states;
	}
	
	
	/**
	 * <p>排序字段</p>
	 * @author:  Wu Shuiming
	 * @return:  Integer 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public Integer getSort() {
		return sort;
	}
	
	/**
	 * <p>排序字段</p>
	 * @author:  Wu Shuiming
	 * @param:   @param sort
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
	/**
	 * <p>产品介绍</p>
	 * @author:  Wu Shuiming
	 * @return:  String 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public String getProductReferral() {
		return productReferral;
	}
	
	/**
	 * <p>产品介绍</p>
	 * @author:  Wu Shuiming
	 * @param:   @param productReferral
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setProductReferral(String productReferral) {
		this.productReferral = productReferral;
	}
	
	
	/**
	 * <p>计价单位（元/克   元/千克）</p>
	 * @author:  Wu Shuiming
	 * @return:  String 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public String getDenoMinated() {
		return denoMinated;
	}
	
	/**
	 * <p>计价单位（元/克   元/千克）</p>
	 * @author:  Wu Shuiming
	 * @param:   @param denoMinated
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setDenoMinated(String denoMinated) {
		this.denoMinated = denoMinated;
	}
	
	
	/**
	 * <p>合约单位（元/克   元/千克）</p>
	 * @author:  Wu Shuiming
	 * @return:  String 
	 * @Date :   2016-10-17 14:25:23    
	 */
	public String getContractUnit() {
		return contractUnit;
	}
	
	/**
	 * <p>合约单位（元/克   元/千克）</p>
	 * @author:  Wu Shuiming
	 * @param:   @param contractUnit
	 * @return:  void 
	 * @Date :   2016-10-17 14:25:23   
	 */
	public void setContractUnit(String contractUnit) {
		this.contractUnit = contractUnit;
	}
	
	

}
