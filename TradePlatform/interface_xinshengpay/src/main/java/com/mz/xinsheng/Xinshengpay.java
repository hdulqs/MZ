package com.mz.xinsheng;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 现代金控
 * <p>
 * TODO
 * </p>
 * 
 * @author: Zhang Lei
 * @Date : 2016年11月28日 下午3:24:09
 */
@XmlRootElement(name = "shanfupay")
@XmlAccessorType(XmlAccessType.FIELD)
public class Xinshengpay {
	/**版本号*/
	static final String VER= "1.0";
	/**充值地址*/
	static final String RECHARGEURL = "";
	/**提现地址*/
	static final String WITHDRAWURL = "";
	/**查询地址*/
	static final String QUERYORDERURL = "";
	/**字符集*/
	static final String CHARSET_GBK = "00";
	static final String CHARSET_GB2312 = "01";
	static final String CHARSET_UTF8 = "02";
	/**币种*/
	static final String CUR = "CNY";
	/**订单有效期单位*/
	static final String VALIDUNIT_MIN = "00";
	static final String VALIDUNIT_HOUR = "01";
	static final String VALIDUNIT_DAY = "02";
	static final String VALIDUNIT_MONTH = "03";

	

	/**字符集*/
	private String charset;
	/**接口版本*/
	private String version;
	/**商户证书*/
	private String merchantCert;
	/**商户签名*/
	private String merchantSign;
	/**签名方式*/
	private String signType;
	/**业务类型*/
	private String service;
	/**页面通知url*/
	private String pageReturnUrl;
	/**后台通知url*/
	private String offlineNotifyUrl;
	/**客户端ip*/
	private String clientIP;
	/**请求号*/
	private String requestId;
	/**购买者标识*/
	private String purchaserId;
	/**合作商户编号*/
	private String merchantId;
	/**合作商户展示名称*/
	private String merchantName;
	/**订单号*/
	private String orderId;
	/**订单时间*/
	private String orderTime;
	/**订单总金额*/
	private String totalAmount;
	/**交易币种*/
	private String currency;
	/**订单有效期单位*/
	private String validUnit;
	/**订单有效期数量*/
	private String validNum;
	/**商品展示url*/
	private String showUrl;
	/**商品名称*/
	private String productName;
	/**商品编号*/
	private String productId;
	/**商品描述*/
	private String productDesc;
	/**原样返回的商户数据*/
	private String backParam;
	
	
	/**以下参数在入金充值中未用到，暂时保留*/
	private String bankAbbr;
	private String cardType;
	private String refundAmount;
	private String cardByName;
	private String cardByNo;
	private String expireDate;
	private String cvv2;
	private String idType;
	private String idNumber;
	private String mobile;
	private String autoSend;
	private String phone;
	private String smsCode;
	
	
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMerchantCert() {
		return merchantCert;
	}
	public void setMerchantCert(String merchantCert) {
		this.merchantCert = merchantCert;
	}
	public String getMerchantSign() {
		return merchantSign;
	}
	public void setMerchantSign(String merchantSign) {
		this.merchantSign = merchantSign;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getOfflineNotifyUrl() {
		return offlineNotifyUrl;
	}
	public void setOfflineNotifyUrl(String offlineNotifyUrl) {
		this.offlineNotifyUrl = offlineNotifyUrl;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getPurchaserId() {
		return purchaserId;
	}
	public void setPurchaserId(String purchaserId) {
		this.purchaserId = purchaserId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getValidUnit() {
		return validUnit;
	}
	public void setValidUnit(String validUnit) {
		this.validUnit = validUnit;
	}
	public String getValidNum() {
		return validNum;
	}
	public void setValidNum(String validNum) {
		this.validNum = validNum;
	}
	public String getShowUrl() {
		return showUrl;
	}
	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getBackParam() {
		return backParam;
	}
	public void setBackParam(String backParam) {
		this.backParam = backParam;
	}
	public String getBankAbbr() {
		return bankAbbr;
	}
	public void setBankAbbr(String bankAbbr) {
		this.bankAbbr = bankAbbr;
	}
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	public String getCardByName() {
		return cardByName;
	}
	public void setCardByName(String cardByName) {
		this.cardByName = cardByName;
	}
	public String getCardByNo() {
		return cardByNo;
	}
	public void setCardByNo(String cardByNo) {
		this.cardByNo = cardByNo;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getCvv2() {
		return cvv2;
	}
	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAutoSend() {
		return autoSend;
	}
	public void setAutoSend(String autoSend) {
		this.autoSend = autoSend;
	}
	
	
	public String getPageReturnUrl() {
		return pageReturnUrl;
	}
	public void setPageReturnUrl(String pageReturnUrl) {
		this.pageReturnUrl = pageReturnUrl;
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "ShopModel [charset=" + charset + ", version=" + version + ", merchantCert=" + merchantCert
				+ ", merchantSign=" + merchantSign + ", signType=" + signType + ", service=" + service
				+ ", offlineNotifyUrl=" + offlineNotifyUrl + ", clientIP=" + clientIP + ", requestId=" + requestId
				+ ", purchaserId=" + purchaserId + ", merchantId=" + merchantId + ", merchantName=" + merchantName
				+ ", orderId=" + orderId + ", orderTime=" + orderTime + ", totalAmount=" + totalAmount + ", currency="
				+ currency + ", validUnit=" + validUnit + ", validNum=" + validNum + ", showUrl=" + showUrl
				+ ", productName=" + productName + ", productId=" + productId + ", productDesc=" + productDesc
				+ ", backParam=" + backParam + ", bankAbbr=" + bankAbbr + ", cardType=" + cardType + ", refundAmount="
				+ refundAmount + ", cardByName=" + cardByName + ", cardByNo=" + cardByNo + ", expireDate=" + expireDate
				+ ", cvv2=" + cvv2 + ", idType=" + idType + ", idNumber=" + idNumber + ", mobile=" + mobile
				+ ", autoSend=" + autoSend + ", phone=" + phone + ", smsCode=" + smsCode + "]";
	}

	
}
