/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
package com.mz.customer.businessman.model;

import static javax.persistence.GenerationType.IDENTITY;
import com.mz.core.mvc.model.BaseModel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p> C2cTransaction </p>
 * @author:         liushilei
 * @Date :          2017-12-07 14:06:38  
 */
@Table(name="c2c_transaction")
public class C2cTransaction extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "businessmanId")
	private Long businessmanId;  //商户id

	@Transient
	private String allName;//实名信息的姓名


	@Transient
	private String businessmanTrueName;//商户名称
	
	@Column(name= "businessmanBankId")
	private Long businessmanBankId;  //商户银行卡id
	
	@Column(name= "randomNum")
	private String randomNum;  //交易随机码
	
	@Column(name= "transactionNum")
	private String transactionNum;  //交易订单号
	
	@Column(name= "userName")
	private String userName;  //用户登录名
	
	@Column(name= "customerId")
	private Long customerId;  //用户id
	
	@Column(name= "customerBankId")
	private Long customerBankId;  //用户银行卡id
	
	@Column(name= "accountId")
	private Long accountId;  //虚拟账户id
	
	@Column(name= "transactionType")
	private Integer transactionType;  //交易类型  1买，2卖
	
	@Column(name= "transactionMoney")
	private BigDecimal transactionMoney;  //交易金额
	
	@Column(name= "transactionCount")
	private BigDecimal transactionCount;  //交易数量
	
	@Column(name= "transactionPrice")
	private BigDecimal transactionPrice;  //交易价格
	
	@Column(name= "status")
	private Integer status;  //1待审核 2已完成 3以否决
	
	@Column(name= "status2")
	private Integer status2;  //1未支付 2已支付 3交易失败 4交易关闭
	
	@Column(name= "remark")
	private String remark;  //备注
	
	@Column(name= "coinCode")
	private String coinCode;  //币的类型
	
	@Column(name= "fee")
	private BigDecimal fee;  //手续费
	
	@Column(name="transactionId")
	private Long transactionId; //币订单id

	@Column(name= "businessman")
	private String businessman;  //交易商
	
	@Column(name= "handleId")
	private Long handleId;  //管理人ID
	
	@Column(name= "handleName")
	private String handleName;  //管理人用户名

	@Column(name= "handleIp")
	private String handleIp;  //管理人IP
	
	@Transient
	private String statusByDes;	//交易状态 2018.4.25新加 by gt

	@Transient
	private String transactionTypeByDes; //交易类型 2018.4.25新加 by gt

	@Column(name= "cardBank")
	private String cardBank; //卖家开户银行 2018.4.25新加 by gt

	@Column(name= "cardName")
	private String cardName; //银行用户名

	@Column(name= "cardNumber")
	private String cardNumber; //卖家银行卡号 2018.4.25新加 by gt

    @Column(name= "subBank")
	private String subBank; //卖家开户支行 2018.4.25新加 by gt

	@Column(name= "BusinessmanBankName")
	private String BusinessmanBankName; //商家银行

	@Column(name= "BusinessmanBankcard")
	private String BusinessmanBankcard; //商家银行卡号

	@Column(name= "BusinessmanBankowner")
	private String BusinessmanBankowner; //商家

	public String getBusinessmanBankName() {
		return BusinessmanBankName;
	}

	public void setBusinessmanBankName(String businessmanBankName) {
		BusinessmanBankName = businessmanBankName;
	}

	public String getBusinessmanBankcard() {
		return BusinessmanBankcard;
	}

	public void setBusinessmanBankcard(String businessmanBankcard) {
		BusinessmanBankcard = businessmanBankcard;
	}

	public String getBusinessmanBankowner() {
		return BusinessmanBankowner;
	}

	public void setBusinessmanBankowner(String businessmanBankowner) {
		BusinessmanBankowner = businessmanBankowner;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public Long getHandleId() {
		return handleId;
	}

	public void setHandleId(Long handleId) {
		this.handleId = handleId;
	}

	public String getHandleName() {
		return handleName;
	}

	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}

	public String getHandleIp() {
		return handleIp;
	}

	public void setHandleIp(String handleIp) {
		this.handleIp = handleIp;
	}

	public String getCardBank() {
		return cardBank;
	}

	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getSubBank() {
		return subBank;
	}

	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}

	public String getStatusByDes() {
		return statusByDes;
	}

	public void setStatusByDes(String statusByDes) {
		this.statusByDes = statusByDes;
	}

	public String getTransactionTypeByDes() {
		return transactionTypeByDes;
	}

	public void setTransactionTypeByDes(String transactionTypeByDes) {
		this.transactionTypeByDes = transactionTypeByDes;
	}

	public String getAllName() {
		return allName;
	}

	public void setAllName(String allName) {
		this.allName = allName;
	}

	public String getBusinessman() {
		return businessman;
	}

	public void setBusinessman(String businessman) {
		this.businessman = businessman;
	}

	public Integer getStatus2() {
		return status2;
	}

	public void setStatus2(Integer status2) {
		this.status2 = status2;
	}

	public Long getBusinessmanId() {
		return businessmanId;
	}

	public void setBusinessmanId(Long businessmanId) {
		this.businessmanId = businessmanId;
	}

	public Long getBusinessmanBankId() {
		return businessmanBankId;
	}

	public void setBusinessmanBankId(Long businessmanBankId) {
		this.businessmanBankId = businessmanBankId;
	}

	public String getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}

	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <p>id</p>
	 * @author:  liushilei
	 * @param:   @param id
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * <p>交易订单号</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getTransactionNum() {
		return transactionNum;
	}
	
	/**
	 * <p>交易订单号</p>
	 * @author:  liushilei
	 * @param:   @param transactionNum
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	
	
	/**
	 * <p>用户登录名</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * <p>用户登录名</p>
	 * @author:  liushilei
	 * @param:   @param userName
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * <p>用户id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Long getCustomerId() {
		return customerId;
	}
	
	/**
	 * <p>用户id</p>
	 * @author:  liushilei
	 * @param:   @param customerId
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	/**
	 * <p>虚拟账户id</p>
	 * @author:  liushilei
	 * @return:  Long 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Long getAccountId() {
		return accountId;
	}
	
	/**
	 * <p>虚拟账户id</p>
	 * @author:  liushilei
	 * @param:   @param accountId
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	
	/**
	 * <p>交易类型(1线上充值,2线上提现 3线下充值 4线下取现...)</p>
	 * @author:  liushilei
	 * @return:  Integer 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Integer getTransactionType() {
		return transactionType;
	}
	
	/**
	 * <p>交易类型(1线上充值,2线上提现 3线下充值 4线下取现...)</p>
	 * @author:  liushilei
	 * @param:   @param transactionType
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	
	
	/**
	 * <p>交易金额</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public BigDecimal getTransactionMoney() {
		return transactionMoney;
	}
	
	/**
	 * <p>交易金额</p>
	 * @author:  liushilei
	 * @param:   @param transactionMoney
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setTransactionMoney(BigDecimal transactionMoney) {
		this.transactionMoney = transactionMoney;
	}
	
	
	/**
	 * <p>1待审核 2已完成 3以否决</p>
	 * @author:  liushilei
	 * @return:  Integer 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * <p>1待审核 2已完成 3以否决</p>
	 * @author:  liushilei
	 * @param:   @param status
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	/**
	 * <p>备注</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * <p>备注</p>
	 * @author:  liushilei
	 * @param:   @param remark
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	/**
	 * <p>币的类型(RMB USD)</p>
	 * @author:  liushilei
	 * @return:  String 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public String getCoinCode() {
		return coinCode;
	}
	
	/**
	 * <p>币的类型(RMB USD)</p>
	 * @author:  liushilei
	 * @param:   @param coinCode
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
	
	/**
	 * <p>手续费</p>
	 * @author:  liushilei
	 * @return:  BigDecimal 
	 * @Date :   2017-12-07 14:06:38    
	 */
	public BigDecimal getFee() {
		return fee;
	}
	
	/**
	 * <p>手续费</p>
	 * @author:  liushilei
	 * @param:   @param fee
	 * @return:  void 
	 * @Date :   2017-12-07 14:06:38   
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(BigDecimal transactionCount) {
		this.transactionCount = transactionCount;
	}

	public BigDecimal getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(BigDecimal transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public String getBusinessmanTrueName() {
		return businessmanTrueName;
	}

	public void setBusinessmanTrueName(String businessmanTrueName) {
		this.businessmanTrueName = businessmanTrueName;
	}

	public Long getCustomerBankId() {
		return customerBankId;
	}

	public void setCustomerBankId(Long customerBankId) {
		this.customerBankId = customerBankId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	
	
	
	
	

}
