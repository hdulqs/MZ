/**
 * <p> otc_order_transaction </p>
 * @author:         zongwei
 * @Date :          20180526
 */
package com.mz.customer.businessman.model;

import com.mz.core.mvc.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * <p> otc_order_transaction </p>
 * @author:         zongwei
 * @Date :          20180526
 */
@Table(name="otc_order_transaction")
public class OtcOrderTransaction extends BaseModel {
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;  //id
	
	@Column(name= "businessmanId")
	private Long businessmanId;  //商户id





	@Transient
	private String businessmanTrueName;//商户名称
	
	@Column(name= "businessmanBankId")
	private Long businessmanBankId;  //商户银行卡id
	
	@Column(name= "randomNum")
	private String randomNum;  //交易随机码
	
	@Column(name= "transactionNum")
	private String transactionNum;  //委托订单号

	@Column(name= "transactionOrderNum")
	private String transactionOrderNum;  //交易订单号
	
	@Column(name= "userName")
	private String userName;  //用户登录名
	
	@Column(name= "sellCustomId")
	private Long sellCustomId;  //卖方的id

	@Column(name= "buyCustomId")
	private Long buyCustomId;  //买方的id


	@Transient
	private String sellCustomname;  //卖方的id



	@Transient
	private String buyCustomname;  //买方的id

	@Transient
	private String buyallName;//实名信息的姓名

	@Transient
	private String sellallName;//实名信息的姓名


	
	@Column(name= "customerBankId")
	private Long customerBankId;  //用户银行卡id



	@Column(name= "buyAccountId")
	private Long buyAccountId;  //虚拟账户id

	@Column(name= "sellAaccountId")
	private Long sellAaccountId;  //虚拟账户id
	
	@Column(name= "transactionType")
	private Integer transactionType;  //交易类型  1买，2卖
	
	@Column(name= "transactionMoney")
	private BigDecimal transactionMoney;  //交易金额
	
	@Column(name= "transactionCount")
	private BigDecimal transactionCount;  //交易数量
	
	@Column(name= "transactionPrice")
	private BigDecimal transactionPrice;  //交易价格
	
	@Column(name= "status")
	private Integer status;  //1未付款 2已付款 3已取消 4 已完成 5 已关闭 6 申诉中
	
	@Column(name= "status2")
	private Integer status2;  //1未支付 2已支付 3交易失败 4交易关闭
	
	@Column(name= "remark")
	private String remark;  //备注
	
	@Column(name= "coinCode")
	private String coinCode;  //币的类型
	
	@Column(name= "fee")
	private BigDecimal fee;  //手续费
	
	@Column(name="transactionId")
	private Long transactionId; //委托订单id

	@Column(name= "businessman")
	private String businessman;  //交易商

	@Transient
	private String statusByDes;	//交易状态 2018.4.25新加 by gt

	@Transient
	private String transactionTypeByDes; //交易类型 2018.4.25新加 by gt

	@Transient
	private String cardBank; //卖家开户银行 2018.4.25新加 by gt
	@Transient
	private String cardNumber; //卖家银行卡号 2018.4.25新加 by gt
	@Transient
	private String subBank; //卖家开户支行 2018.4.25新加 by gt

	@Column(name= "img1")
	private String img1;  //付款完成照片

	@Column(name= "img2")
	private String img2;  //付款完成照片

	@Column(name= "img3")
	private String img3;  //付款完成照片

	@Column(name= "img4")
	private String img4;  //申诉照片

	@Column(name= "img5")
	private String img5;  //申诉照片


	@Column(name= "img6")
	private String img6;  //申诉照片

	@Column(name= "dealQuantity")
	private BigDecimal dealQuantity;  //成交量

	@Column(name= "appealFlag")
	private String appealFlag;  //申诉标识

	@Column(name= "appealReason")
	private String appealReason;  //申诉说明

	@Column(name= "paymentType")
	private String paymentType;  //付款类型

	@Column(name= "paymentTime")
	private Date paymentTime;  //付款时间

	@Column(name= "finishTime")
	private Date finishTime;  //完成时间


	@Column(name= "surplusQuantity")
	private BigDecimal surplusQuantity;  //剩余量

	@Column(name= "appealCustomId")
	private Long appealCustomId;  //申诉方的id

	@Column(name= "appealCustomName")
	private String appealCustomName;  //申诉方的id


	@Column(name= "createBy")
	private Long createBy;  //创建人

	@Column(name= "cancelFlag")
	private String cancelFlag;  //取消标识

	@Column(name= "cancelBy")
	private Long cancelBy;  //取消人

	@Column(name= "cancelDate")
	private Date cancelDate;  //取消人

	@Column(name = "appealHandle")
	private String appealHandle; //申诉处理

	public String getAppealHandle() {
		return appealHandle;
	}

	public void setAppealHandle(String appealHandle) {
		this.appealHandle = appealHandle;
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

	public String getTransactionOrderNum() {

		return transactionOrderNum;
	}

	public void setTransactionOrderNum(String transactionOrderNum) {
		this.transactionOrderNum = transactionOrderNum;
	}

	public Long getAppealCustomId() {
		return appealCustomId;
	}

	public void setAppealCustomId(Long appealCustomId) {
		this.appealCustomId = appealCustomId;
	}

	public String getAppealCustomName() {
		return appealCustomName;
	}

	public void setAppealCustomName(String appealCustomName) {
		this.appealCustomName = appealCustomName;
	}

	public String getSellCustomname() {
		return sellCustomname;
	}

	public void setSellCustomname(String sellCustomname) {
		this.sellCustomname = sellCustomname;
	}

	public String getBuyCustomname() {
		return buyCustomname;
	}

	public void setBuyCustomname(String buyCustomname) {
		this.buyCustomname = buyCustomname;
	}

	public String getBuyallName() {
		return buyallName;
	}

	public void setBuyallName(String buyallName) {
		this.buyallName = buyallName;
	}

	public String getSellallName() {
		return sellallName;
	}

	public void setSellallName(String sellallName) {
		this.sellallName = sellallName;
	}


	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
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


	public Long getSellCustomId() {
		return sellCustomId;
	}

	public void setSellCustomId(Long sellCustomId) {
		this.sellCustomId = sellCustomId;
	}

	public Long getBuyCustomId() {
		return buyCustomId;
	}

	public void setBuyCustomId(Long buyCustomId) {
		this.buyCustomId = buyCustomId;
	}

	public Long getBuyAccountId() {
		return buyAccountId;
	}

	public void setBuyAccountId(Long buyAccountId) {
		this.buyAccountId = buyAccountId;
	}

	public Long getSellAaccountId() {
		return sellAaccountId;
	}

	public void setSellAaccountId(Long sellAaccountId) {
		this.sellAaccountId = sellAaccountId;
	}

	public String getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		this.img1 = img1;
	}

	public String getImg2() {
		return img2;
	}

	public void setImg2(String img2) {
		this.img2 = img2;
	}

	public String getImg3() {
		return img3;
	}

	public void setImg3(String img3) {
		this.img3 = img3;
	}

	public String getImg4() {
		return img4;
	}

	public void setImg4(String img4) {
		this.img4 = img4;
	}

	public String getImg5() {
		return img5;
	}

	public void setImg5(String img5) {
		this.img5 = img5;
	}

	public String getImg6() {
		return img6;
	}

	public void setImg6(String img6) {
		this.img6 = img6;
	}

	public String getAppealFlag() {
		return appealFlag;
	}

	public void setAppealFlag(String appealFlag) {
		this.appealFlag = appealFlag;
	}

	public String getAppealReason() {
		return appealReason;
	}

	public void setAppealReason(String appealReason) {
		this.appealReason = appealReason;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
}
