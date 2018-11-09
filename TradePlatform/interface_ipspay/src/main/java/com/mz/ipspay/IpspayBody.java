/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      zhanglei
 * @version:      V1.0 
 * @Date:        2016年11月28日 下午3:29:10
 */
package com.mz.ipspay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 环迅支付
 * <p>
 * TODO
 * </p>
 * @author: Zhang Lei
 * @Date : 2016年11月28日 下午3:24:09
 */
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class IpspayBody {
	/** 01#借记卡 02#信用卡 03#IPS 账户支付 */
	static final String GATEWAYTYPE_JJ = "01";
	static final String GATEWAYTYPE_XY = "02";
	static final String GATEWAYTYPE_ZH = "03";
	/** 币种:156#人民币  */
	static final String CURRENCYTYPE = "156";
	/** 语言  GB 中文（缺省）   */
	static final String LONG = "GB";
	/** 订单支付接口加密方式   5:订单支付采用 Md5 的摘要讣证方式 */
	static final String ORDERENCODETYPE = "5";
	/** 交易返回接口加密方式  16#交易返回采用Md5WithRsa 的签名讣证方式   17#交易返回采用 Md5 的摘要讣证方式*/
	static final String RETENCODETYPE_MD5WITHRSA = "16";
	static final String RETENCODETYPE_MD5 = "17";
	/** Server to Server 返回。 1：S2S 返回   */
	static final String RETTYPE = "1";
	/** 1：直连必填  不直连就不传   */
	static final String ISCREDIT = "1";
	/** 产品类型    1:个人网银  2:企业网银*/
	static final String PRODUCTTYPE_GR = "1";
	static final String PRODUCTTYPE_QY = "2";
	
	/** 商户订单号  30位以下，字母或数字 */
	private String MerBillNo;
	/** 支付方式 */
	private String GatewayType;
	/** 订单日期 格式：yyyyMMdd */
	private String Date;
	/** 币种  */
	private String CurrencyType;
	/** 订单金额 :保留 2 位小数*/
	private String Amount;
	/** 语言（可选）*/
	private String Lang;
	/** 支付结果成功返回的商户 URL*/
	private String Merchanturl;
	/** 支付结果失败返回的商户 URL（可选）*/
	private String FailUrl;
	/** 商户数据包，会原样返回（可选）*/
	private String Attach;
	/** 订单支付口加密方式*/
	private String OrderEncodeType;
	/** 交易返回接口加密方式 */
	private String RetEncodeType;
	/** 返回方式 */
	private String RetType;
	/** 异步S2S返回  当 RetType=1 时,本字段有效*/
	private String ServerUrl;
	/** 订单有效期（可选）*/
	private String BillEXP;
	/** 商品名称*/
	private String GoodsName;
	/** 直连选项 决定商户是否参用直连方式 （可选）*/
	private String IsCredit;
	/** 银行号（可选）*/
	private String BankCode;
	/** 产品类型（可选）*/
	private String ProductType;
	
	
	
	
	
	
	public String getMerBillNo() {
		return MerBillNo;
	}
	public void setMerBillNo(String merBillNo) {
		MerBillNo = merBillNo;
	}
	public String getGatewayType() {
		return GatewayType;
	}
	public void setGatewayType(String gatewayType) {
		GatewayType = gatewayType;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getCurrencyType() {
		return CurrencyType;
	}
	public void setCurrencyType(String currencyType) {
		CurrencyType = currencyType;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getLang() {
		return Lang;
	}
	public void setLang(String lang) {
		Lang = lang;
	}
	public String getMerchanturl() {
		return Merchanturl;
	}
	public void setMerchanturl(String merchanturl) {
		Merchanturl = merchanturl;
	}
	public String getFailUrl() {
		return FailUrl;
	}
	public void setFailUrl(String failUrl) {
		FailUrl = failUrl;
	}
	public String getAttach() {
		return Attach;
	}
	public void setAttach(String attach) {
		Attach = attach;
	}
	public String getOrderEncodeType() {
		return OrderEncodeType;
	}
	public void setOrderEncodeType(String orderEncodeType) {
		OrderEncodeType = orderEncodeType;
	}
	public String getRetEncodeType() {
		return RetEncodeType;
	}
	public void setRetEncodeType(String retEncodeType) {
		RetEncodeType = retEncodeType;
	}
	public String getRetType() {
		return RetType;
	}
	public void setRetType(String retType) {
		RetType = retType;
	}
	public String getServerUrl() {
		return ServerUrl;
	}
	public void setServerUrl(String serverUrl) {
		ServerUrl = serverUrl;
	}
	public String getBillEXP() {
		return BillEXP;
	}
	public void setBillEXP(String billEXP) {
		BillEXP = billEXP;
	}
	public String getGoodsName() {
		return GoodsName;
	}
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	public String getIsCredit() {
		return IsCredit;
	}
	public void setIsCredit(String isCredit) {
		IsCredit = isCredit;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getProductType() {
		return ProductType;
	}
	public void setProductType(String productType) {
		ProductType = productType;
	}

}
