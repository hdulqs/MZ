/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-12-04 17:19:36 
 */
package com.mz.coin.transaction.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p> AppCoinTransactionTv </p>
 * @author:         shangxl
 * @Date :          2017-12-04 17:19:36  
 */
@Table(name="app_coin_transaction_tv")
public class AppCoinTransactionTv extends BaseModel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "trxId")
	private String trxId;  //trxId
	
	@Column(name= "blockNum")
	private Integer blockNum;  //blockNum
	
	@Column(name= "fee")
	private BigDecimal fee;  //fee
	
	@Column(name= "isconfirmed")
	private Integer isconfirmed;  //isconfirmed
	
	@Column(name= "amount")
	private BigDecimal amount;  //amount
	
	@Column(name= "fromAddress")
	private String fromAddress;  //fromAddress
	
	@Column(name= "fromAccount")
	private String fromAccount;  //fromAccount
	
	@Column(name= "memo")
	private String memo;  //memo
	
	@Column(name= "toAccount")
	private String toAccount;  //toAccount
	
	@Column(name= "isUse")
	private int isUse;  //isUse  0、未被消费  1、已被消费
	
	@Column(name= "type")
	private String type;  //币种类型
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>trxId</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public String getTrxId() {
		return trxId;
	}
	
	/**
	 * <p>trxId</p>
	 * @author:  shangxl
	 * @param:   @param trxId
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	
	
	/**
	 * <p>blockNum</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public Integer getBlockNum() {
		return blockNum;
	}
	
	/**
	 * <p>blockNum</p>
	 * @author:  shangxl
	 * @param:   @param blockNum
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setBlockNum(Integer blockNum) {
		this.blockNum = blockNum;
	}
	
	
	/**
	 * <p>fee</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public BigDecimal getFee() {
		return fee;
	}
	
	/**
	 * <p>fee</p>
	 * @author:  shangxl
	 * @param:   @param fee
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	
	/**
	 * <p>isconfirmed</p>
	 * @author:  shangxl
	 * @return:  Integer 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public Integer getIsconfirmed() {
		return isconfirmed;
	}
	
	/**
	 * 区块链是否确认
	 * <p>isconfirmed</p>
	 * @author:  shangxl
	 * @param:   @param isconfirmed
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setIsconfirmed(Integer isconfirmed) {
		this.isconfirmed = isconfirmed;
	}
	
	
	/**
	 * <p>amount</p>
	 * @author:  shangxl
	 * @return:  BigDecimal 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	/**
	 * <p>amount</p>
	 * @author:  shangxl
	 * @param:   @param amount
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	/**
	 * <p>fromAddress</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public String getFromAddress() {
		return fromAddress;
	}
	
	/**
	 * <p>fromAddress</p>
	 * @author:  shangxl
	 * @param:   @param fromAddress
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	
	/**
	 * <p>fromAccount</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public String getFromAccount() {
		return fromAccount;
	}
	
	/**
	 * <p>fromAccount</p>
	 * @author:  shangxl
	 * @param:   @param fromAccount
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	
	
	/**
	 * <p>memo</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public String getMemo() {
		return memo;
	}
	
	/**
	 * <p>memo</p>
	 * @author:  shangxl
	 * @param:   @param memo
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	/**
	 * <p>toAccount</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-12-04 17:19:36    
	 */
	public String getToAccount() {
		return toAccount;
	}
	
	/**
	 * <p>toAccount</p>
	 * @author:  shangxl
	 * @param:   @param toAccount
	 * @return:  void 
	 * @Date :   2017-12-04 17:19:36   
	 */
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public int getIsUse() {
		return isUse;
	}

	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
