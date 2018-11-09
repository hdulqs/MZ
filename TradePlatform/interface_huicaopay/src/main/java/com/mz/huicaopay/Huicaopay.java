/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午9:07:19
 */
package com.mz.huicaopay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 智付
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月5日 上午9:07:19 
 */
@XmlRootElement(name="dinpay")
@XmlAccessorType(XmlAccessType.FIELD)
public class Huicaopay {
	
	
	 //充值地址
	static final String RECHARGEURL="https://pay.ecpss.com/sslpayment";
	 
	// 商户号
	private String  MerNo="";
	
	// 订单号
	private String BillNo="";
	
	// 交易钱数 
	private String Amount="";
	
	// 同步通知地址
	private String ReturnURL="";
	
	// 异步通知地址 
	private String AdviceURL="";
	
	// 签名信息 
	private String SignInfo="";
	
	// 请求时间 (格式为: YYYYMMDDHHMMSS )
	private String orderTime="";
	
	// 银行代码 (可选)
	private String defaultBankNumber="";
	
	// 备注 (可选)
	private String Remark=""; 
	
	// 物品信息(可选)
	private String products="";
	
	
	// ---------------------------  返回数据不同的参数   ----------------------------------------------
	// 状态码
	private String Succeed="";
	
	// 订单详情
	private String Result="";
	
	// 验证签名 [MD5(BillNo&Amount&Succeed&MD5key)]
	private String SignMD5info="";

	
	
	
	
	public String getMerNo() {
		return MerNo;
	}

	public void setMerNo(String merNo) {
		MerNo = merNo;
	}

	public String getBillNo() {
		return BillNo;
	}

	public void setBillNo(String billNo) {
		BillNo = billNo;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getReturnURL() {
		return ReturnURL;
	}

	public void setReturnURL(String returnURL) {
		ReturnURL = returnURL;
	}

	public String getAdviceURL() {
		return AdviceURL;
	}

	public void setAdviceURL(String adviceURL) {
		AdviceURL = adviceURL;
	}

	public String getSignInfo() {
		return SignInfo;
	}

	public void setSignInfo(String signInfo) {
		SignInfo = signInfo;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getDefaultBankNumber() {
		return defaultBankNumber;
	}

	public void setDefaultBankNumber(String defaultBankNumber) {
		this.defaultBankNumber = defaultBankNumber;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getSucceed() {
		return Succeed;
	}

	public void setSucceed(String succeed) {
		Succeed = succeed;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getSignMD5info() {
		return SignMD5info;
	}

	public void setSignMD5info(String signMD5info) {
		SignMD5info = signMD5info;
	}

	public static String getRechargeurl() {
		return RECHARGEURL;
	}

	
	
	
	
	
	
	
	
	
	
	
	

}
