/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-09-18 15:00:44 
 */
package com.mz.coin.coin.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.mz.core.mvc.model.BaseModel;

/**
 * <p> AppCoinTransaction </p>
 * @author:         shangxl
 * @Date :          2017-09-18 15:00:44  
 */
@Table(name="app_coin_transaction")
public class AppCoinTransaction extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	/**
	 * id
	 */
	private Long id;
	
	@Column(name= "hash_")
	/**
	 * hash
	 */
	private String hash_;
	
	@Column(name= "nonce")
	/**
	 * nonce
	 */
	private String nonce;
	
	@Column(name= "blockHash")
	/**
	 * blockHash
	 */
	private String blockHash;
	
	@Column(name= "blockNumber")
	/**
	 * blockNumber
	 */
	private String blockNumber;
	
	@Column(name= "transactionIndex")
	/**
	 * transactionIndex
	 */
	private String transactionIndex;
	
	@Column(name= "from_")
	/**
	 * from
	 */
	private String from_;
	
	@Column(name= "to_")
	/**
	 * to
	 */
	private String to_;
	
	@Column(name= "amount")
	/**
	 * amount
	 */
	private BigDecimal amount;
	
	@Column(name= "gas")
	/**
	 * gas
	 */
	private BigDecimal gas;
	
	@Column(name= "gasPrice")
	/**
	 * gasPrice
	 */
	private BigDecimal gasPrice;
	
	@Column(name= "input")
	/**
	 * input
	 */
	private String input;
	
	@Column(name= "coinType")
	/**
	 * coinType
	 */
	private String coinType;
	
	@Column(name="isconsume")
	/**
	 * 是否确认
	 */
	private int isconsume;
	
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @return:  Long 
	 * @Date :   2017-09-18 15:00:44    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  shangxl
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-09-18 15:00:44   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>hash</p>
	 * @author:  shangxl
	 * @return:  String
	 * @Date :   2017-09-18 15:00:44    
	 */
	public String getHash_() {
		return hash_;
	}
	
	/**
	 * <p>hash</p>
	 * @author:  shangxl
	 * @param:   @param hash
	 * @return:  void 
	 * @Date :   2017-09-18 15:00:44   
	 */
	public void setHash_(String hash_) {
		this.hash_ = hash_;
	}
	
	
	/**
	 * <p>nonce</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-09-18 15:00:44    
	 */
	public String getNonce() {
		return nonce;
	}
	
	/**
	 * <p>nonce</p>
	 * @author:  shangxl
	 * @param:   @param nonce
	 * @return:  void 
	 * @Date :   2017-09-18 15:00:44   
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
	
	/**
	 * <p>blockHash</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-09-18 15:00:44    
	 */
	public String getBlockHash() {
		return blockHash;
	}
	
	/**
	 * <p>blockHash</p>
	 * @author:  shangxl
	 * @param:   @param blockHash
	 * @return:  void 
	 * @Date :   2017-09-18 15:00:44   
	 */
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
	
	
	/**
	 * <p>blockNumber</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-09-18 15:00:44    
	 */
	public String getBlockNumber() {
		return blockNumber;
	}
	
	/**
	 * <p>blockNumber</p>
	 * @author:  shangxl
	 * @param:   @param blockNumber
	 * @return:  void 
	 * @Date :   2017-09-18 15:00:44   
	 */
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}
	
	
	/**
	 * <p>transactionIndex</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-09-18 15:00:44    
	 */
	public String getTransactionIndex() {
		return transactionIndex;
	}
	
	/**
	 * <p>transactionIndex</p>
	 * @author:  shangxl
	 * @param:   @param transactionIndex
	 * @return:  void 
	 * @Date :   2017-09-18 15:00:44   
	 */
	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}
	
	
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
	
	/**
	 * <p>input</p>
	 * @author:  shangxl
	 * @return:  String 
	 * @Date :   2017-09-18 15:00:44    
	 */
	public String getInput() {
		return input;
	}
	
	/**
	 * <p>input</p>
	 * @author:  shangxl
	 * @param:   @param input
	 * @return:  void 
	 * @Date :   2017-09-18 15:00:44   
	 */
	public void setInput(String input) {
		this.input = input;
	}
	
	public String getCoinType() {
		return coinType;
	}

	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}


	public BigDecimal getGas() {
		return gas;
	}

	public BigDecimal getGasPrice() {
		return gasPrice;
	}


	public void setGas(BigDecimal gas) {
		this.gas =  Convert.fromWei(gas.toString(), Unit.ETHER);;
	}

	public void setGasPrice(BigDecimal gasPrice) {
		this.gasPrice = Convert.fromWei(gasPrice.toString(), Unit.ETHER);
	}

	public int getIsconsume() {
		return isconsume;
	}

	public void setIsconsume(int isconsume) {
		this.isconsume = isconsume;
	}

	public String getFrom_() {
		return from_;
	}

	public void setFrom_(String from_) {
		this.from_ = from_;
	}

	public String getTo_() {
		return to_;
	}

	public void setTo_(String to_) {
		this.to_ = to_;
	}


}
