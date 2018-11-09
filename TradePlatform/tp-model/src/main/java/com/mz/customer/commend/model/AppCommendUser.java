/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 15:30:37 
 */
package com.mz.customer.commend.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import com.mz.core.mvc.model.BaseModel;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p> AppCommendUser </p>
 * @author:         menwei
 * @Date :          2017-11-28 15:30:37  
 */
@Table(name="app_commend_user")
	
public class AppCommendUser extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "pid")
	private Long pid;  //pid
	
	@Column(name= "username")
	private String username;  //username
	
	@Column(name= "pname")
	private String pname;  //pname
	
	@Column(name= "uid")
	private Long uid;  //用户id
	
	
	@Column(name= "sid")
	private String sid;  //sid
	
	@Column(name= "isFrozen")
	private int isFrozen;
	
	
	@Column(name= "receCode")
	private String receCode;
	
	
	@Column(name= "maxId")
	private Long maxId;
	
	@Transient
	private int oneNumber;
	
	@Transient
	private int twoNumber;
	
	@Transient
	private int threeNumber;
	
	@Transient
	private int laterNumber;
	
	
	@Transient
	private Integer comLeven;
	
	@Column(name= "aloneMoney")
	private BigDecimal aloneMoney;//转换usdt
	
	
	
	
	@Transient
	private BigDecimal commendMoney;
	
	
	
	@Transient
	private BigDecimal onecommendMoney;



	@Transient
	private Integer exorderCount;


	@Transient
	private BigDecimal moneyNum;

	@Transient
	private String coinCode;


	@Transient
	private String Ratetype;

	@Transient
	private BigDecimal userMoney;

	@Transient
	private BigDecimal moneydic;  //平台奖励
	
	/*
	 * 新增总数
	 */
	@Transient
	private int selectAll;
	
	/*
	 * 新增激活总数
	 */
	@Transient
	private int conut;


	/*
	 * 新增激活总数
	 */
	public int getConut() {
		return conut;
	}

	/*
	 * 新增激活总数
	 */
	public void setConut(int conut) {
		this.conut = conut;
	}

	/*
	 * 新增总数
	 */
	public void setSelectAll(int selectAll) {
		// TODO Auto-generated method stub
		this.selectAll = selectAll;
		
	}
	
	/*
	 * 新增总数
	 */
	public int getSelectAll() {
		return selectAll;
	}

	public BigDecimal getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(BigDecimal userMoney) {
		this.userMoney = userMoney;
	}

	public BigDecimal getOnecommendMoney() {
		return onecommendMoney;
	}

	public void setOnecommendMoney(BigDecimal onecommendMoney) {
		this.onecommendMoney = onecommendMoney;
	}

	public String getRatetype() {
		return Ratetype;
	}

	public void setRatetype(String ratetype) {
		Ratetype = ratetype;
	}

	public BigDecimal getAloneMoney() {
		return aloneMoney;
	}

	public void setAloneMoney(BigDecimal aloneMoney) {
		this.aloneMoney = aloneMoney;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public BigDecimal getMoneyNum() {
		return moneyNum;
	}

	public void setMoneyNum(BigDecimal moneyNum) {
		this.moneyNum = moneyNum;
	}

	public Integer getExorderCount() {
		return exorderCount;
	}

	public void setExorderCount(Integer exorderCount) {
		this.exorderCount = exorderCount;
	}

	public BigDecimal getCommendMoney() {
		return commendMoney;
	}

	public void setCommendMoney(BigDecimal commendMoney) {
		this.commendMoney = commendMoney;
	}

	public Integer getComLeven() {
		return comLeven;
	}

	public void setComLeven(Integer comLeven) {
		this.comLeven = comLeven;
	}

	public Long getMaxId() {
		return maxId;
	}

	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}

	public String getReceCode() {
		return receCode;
	}

	public void setReceCode(String receCode) {
		this.receCode = receCode;
	}

	public int getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(int isFrozen) {
		this.isFrozen = isFrozen;
	}

	public int getOneNumber() {
		return oneNumber;
	}

	public void setOneNumber(int oneNumber) {
		this.oneNumber = oneNumber;
	}

	public int getTwoNumber() {
		return twoNumber;
	}

	public void setTwoNumber(int twoNumber) {
		this.twoNumber = twoNumber;
	}

	public int getThreeNumber() {
		return threeNumber;
	}

	public void setThreeNumber(int threeNumber) {
		this.threeNumber = threeNumber;
	}

	public int getLaterNumber() {
		return laterNumber;
	}

	public void setLaterNumber(int laterNumber) {
		this.laterNumber = laterNumber;
	}

	
	
	
	


	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public BigDecimal getMoneydic() {
		return moneydic;
	}

	public void setMoneydic(BigDecimal moneydic) {
		this.moneydic = moneydic;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * <p>id</p>
	 * @author:  menwei
	 * @return:  Long 
	 * @Date :   2017-11-28 15:30:37    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  menwei
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-11-28 15:30:37   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>pid</p>
	 * @author:  menwei
	 * @return:  Long 
	 * @Date :   2017-11-28 15:30:37    
	 */
	public Long getPid() {
		return pid;
	}
	
	/**
	 * <p>pid</p>
	 * @author:  menwei
	 * @param:   @param pid
	 * @return:  void 
	 * @Date :   2017-11-28 15:30:37   
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	
	
	/**
	 * <p>username</p>
	 * @author:  menwei
	 * @return:  String 
	 * @Date :   2017-11-28 15:30:37    
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * <p>username</p>
	 * @author:  menwei
	 * @param:   @param username
	 * @return:  void 
	 * @Date :   2017-11-28 15:30:37   
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	/**
	 * <p>pname</p>
	 * @author:  menwei
	 * @return:  String 
	 * @Date :   2017-11-28 15:30:37    
	 */
	public String getPname() {
		return pname;
	}
	
	/**
	 * <p>pname</p>
	 * @author:  menwei
	 * @param:   @param pname
	 * @return:  void 
	 * @Date :   2017-11-28 15:30:37   
	 */
	public void setPname(String pname) {
		this.pname = pname;
	}

	
	
	

}
