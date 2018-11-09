/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 16:07:54 
 */
package com.mz.customer.deploy.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;
import com.mz.web.menu.model.AppMenuTree;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

/**
 * <p> AppCommendDeploy </p>
 * @author:         menwei
 * @Date :          2017-11-28 16:07:54  
 */
@Table(name="app_commend_deploy")
public class AppCommendDeploy extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id

	@Column(name="commendName")
	private String commendName;
	
	@Column(name= "RankRatio")
	private BigDecimal RankRatio;  //推荐奖励比例
	
	@Column(name= "StandardValue")
	private BigDecimal StandardValue;  //奖励最小值
	
	@Column(name= "transactionNumber")
	private Integer transactionNumber;  //交易笔数限制
	
	@Column(name= "costName")
	private String costName;  //costName
	
	@Column(name= "states")
	private Integer states;  //states是否开启
	
	@Column(name= "MaxHierarchy")
	private Integer MaxHierarchy;  //最大奖励层级
	
	@Column(name= "MinHierarchy")
	private Integer MinHierarchy;  //最小奖励层级
	
	
	@Column(name= "reserveMoney")
	private BigDecimal reserveMoney;  //平台扣取

	public String getCommendName() {
		return commendName;
	}

	public void setCommendName(String commendName) {
		this.commendName = commendName;
	}

	public BigDecimal getReserveMoney() {
		return reserveMoney;
	}

	public void setReserveMoney(BigDecimal reserveMoney) {
		this.reserveMoney = reserveMoney;
	}

	public Integer getMaxHierarchy() {
		return MaxHierarchy;
	}

	public void setMaxHierarchy(Integer maxHierarchy) {
		MaxHierarchy = maxHierarchy;
	}

	public Integer getMinHierarchy() {
		return MinHierarchy;
	}

	public void setMinHierarchy(Integer minHierarchy) {
		MinHierarchy = minHierarchy;
	}

	/**
	 * <p>id</p>
	 * @author:  menwei
	 * @return:  Long 
	 * @Date :   2017-11-28 16:07:54    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  menwei
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-11-28 16:07:54   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>推荐奖励比例</p>
	 * @author:  menwei
	 * @return:  BigDecimal 
	 * @Date :   2017-11-28 16:07:54    
	 */
	public BigDecimal getRankRatio() {
		return RankRatio;
	}
	
	/**
	 * <p>推荐奖励比例</p>
	 * @author:  menwei
	 * @param:   @param RankRatio
	 * @return:  void 
	 * @Date :   2017-11-28 16:07:54   
	 */
	public void setRankRatio(BigDecimal RankRatio) {
		this.RankRatio = RankRatio;
	}
	
	
	/**
	 * <p>奖励最小值</p>
	 * @author:  menwei
	 * @return:  BigDecimal 
	 * @Date :   2017-11-28 16:07:54    
	 */
	public BigDecimal getStandardValue() {
		return StandardValue;
	}
	
	/**
	 * <p>奖励最小值</p>
	 * @author:  menwei
	 * @param:   @param StandardValue
	 * @return:  void 
	 * @Date :   2017-11-28 16:07:54   
	 */
	public void setStandardValue(BigDecimal StandardValue) {
		this.StandardValue = StandardValue;
	}
	
	
	/**
	 * <p>transactionNumber</p>
	 * @author:  menwei
	 * @return:  Integer 
	 * @Date :   2017-11-28 16:07:54    
	 */
	public Integer getTransactionNumber() {
		return transactionNumber;
	}
	
	/**
	 * <p>transactionNumber</p>
	 * @author:  menwei
	 * @param:   @param transactionNumber
	 * @return:  void 
	 * @Date :   2017-11-28 16:07:54   
	 */
	public void setTransactionNumber(Integer transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	
	/**
	 * <p>costName</p>
	 * @author:  menwei
	 * @return:  String 
	 * @Date :   2017-11-28 16:07:54    
	 */
	public String getCostName() {
		return costName;
	}
	
	/**
	 * <p>costName</p>
	 * @author:  menwei
	 * @param:   @param costName
	 * @return:  void 
	 * @Date :   2017-11-28 16:07:54   
	 */
	public void setCostName(String costName) {
		this.costName = costName;
	}
	
	
	/**
	 * <p>states</p>
	 * @author:  menwei
	 * @return:  Integer 
	 * @Date :   2017-11-28 16:07:54    
	 */
	public Integer getStates() {
		return states;
	}
	
	/**
	 * <p>states</p>
	 * @author:  menwei
	 * @param:   @param states
	 * @return:  void 
	 * @Date :   2017-11-28 16:07:54   
	 */
	public void setStates(Integer states) {
		this.states = states;
	}
	
	

}
