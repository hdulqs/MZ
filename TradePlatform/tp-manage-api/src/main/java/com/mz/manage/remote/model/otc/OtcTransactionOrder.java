package com.mz.manage.remote.model.otc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class OtcTransactionOrder implements Serializable {




	private String saasId;

	/**
	 * 创建时间
	 */

	private Date created;
	/**
	 * 修改时间
	 */

	private Date modified;

	private Long id;  //id


	private Long businessmanId;  //商户id


	private String allName;//实名信息的姓名



	private String businessmanTrueName;//商户名称


	private Long businessmanBankId;  //商户银行卡id


	private String randomNum;  //交易随机码


	private String transactionNum;  //交易订单号


	private String userName;  //用户登录名


	private Long customerId;  //用户id


	private Long customerBankId;  //用户银行卡id


	private Long accountId;  //虚拟账户id


	private Integer transactionType;  //交易类型  1买，2卖


	private BigDecimal transactionMoney;  //交易金额


	private BigDecimal transactionCount;  //交易数量


	private BigDecimal transactionPrice;  //交易价格


	private Integer status;  //1进行中 2已完成 3已取消,4 部分交易,5 全部交易 6 部分完成


	private Integer status2;  //1未支付 2已支付 3交易失败 4交易关闭


	private String remark;  //备注


	private String coinCode;  //币的类型


	private BigDecimal fee;  //手续费


	private Long transactionId; //币订单id


	private String businessman;  //交易商


	private String statusByDes;	//交易状态 2018.4.25新加 by gt


	private String transactionTypeByDes; //交易类型 2018.4.25新加 by gt


	private String cardBank; //卖家开户银行 2018.4.25新加 by gt

	private String cardNumber; //卖家银行卡号 2018.4.25新加 by gt

	private String subBank; //卖家开户支行 2018.4.25新加 by gt


	private BigDecimal dealQuantity;  //成交量


	private Date finishTime;  //完成时间


	private BigDecimal businessQuantity;



	private BigDecimal surplusQuantity;  //剩余量


	private String businessFlag;  //交易标识   如果是Y 可以挂单


	private String poundage_type;  //手续费类型


	private BigDecimal poundage;  //手续费比例或固定手续费


	private Long createBy;  //创建人


	private String cancelFlag;  //取消标识


	private Long cancelBy;  //取消人


	private Date cancelDate;  //取消人


	private  BigDecimal frozenQuantity; //因数量不足剩余数量


	private  BigDecimal returnQuantity; //返还数量

	public String getSaasId() {
		return saasId;
	}

	public void setSaasId(String saasId) {
		this.saasId = saasId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public BigDecimal getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(BigDecimal returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public BigDecimal getFrozenQuantity() {
		return frozenQuantity;
	}

	public void setFrozenQuantity(BigDecimal frozenQuantity) {
		this.frozenQuantity = frozenQuantity;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public Long getCancelBy() {
		return cancelBy;
	}

	public void setCancelBy(Long cancelBy) {
		this.cancelBy = cancelBy;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getPoundage_type() {
		return poundage_type;
	}

	public void setPoundage_type(String poundage_type) {
		this.poundage_type = poundage_type;
	}

	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	public String getBusinessFlag() {
		return businessFlag;
	}

	public void setBusinessFlag(String businessFlag) {
		this.businessFlag = businessFlag;
	}

	public BigDecimal getDealQuantity() {
		return dealQuantity;
	}

	public void setDealQuantity(BigDecimal dealQuantity) {
		this.dealQuantity = dealQuantity;
	}

	public BigDecimal getSurplusQuantity() {
		return surplusQuantity;
	}

	public void setSurplusQuantity(BigDecimal surplusQuantity) {
		this.surplusQuantity = surplusQuantity;
	}



	public BigDecimal getBusinessQuantity() {
		return businessQuantity;
	}

	public void setBusinessQuantity(BigDecimal businessQuantity) {
		this.businessQuantity = businessQuantity;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
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
