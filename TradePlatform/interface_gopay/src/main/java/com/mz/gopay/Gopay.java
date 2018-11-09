/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午9:07:19
 */
package com.mz.gopay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月5日 上午9:07:19 
 */
@XmlRootElement(name="GopayTranRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Gopay {

	

	
	
	/**
     * 国付宝提供给商户调试的网关地址
     */
    public static String GOPAY_GATEWAY = "https://gatewaymer.gopay.com.cn/Trans/WebClientAction.do";
	
    /**
     * 国付宝服务器时间，反钓鱼时使用
     */
	public static String GOPAY_SERVER_TIME_URL = "https://gateway.gopay.com.cn/time.do";
	
	/**
     * 字符编码格式，目前支持 GBK 或 UTF-8
     */
	public static String CHARSET_GBK = "1";

	public static String CHARSET_UTF8 = "2";
	
	
	/**
     * 网关语言版本 1,中文  2,英文
     */
	public static String Language_CH = "1";
	public static String Language_EN = "2";
	
	/**
     *  加密方式
     */
	public static String MD5 = "1";
	public static String SHA= "2";
	
	/**
     * 国付接口版本
     */
    public static String VERSION = "2.2";
	
	/**
     * 国付宝网关接口代码
     */
    public static String GATEWAY_CODE = "8888";
    public static String WEICIN_CODE = "SC01";
    
	/**
     * 国付宝直付接口代码
     */
    public static String PAY_CODE = "4025";
	
    public static String COIN_RMB = "156";
	
	/*
	 * 网关版本号
	 */
	public String version;
	
	/*
	 * 字符集  1:GBK,2:UTF-8
	 */
	public String Charset;
	
	/*
	 * 网关语言版本  1:中文,2:英文
	 */
	public String Language;
	
	/*
	 * 报文加密方式  1:MD5  2:SHA
	 */
	public String signType;
	
	/*
	 * 交易代码（网关接口必须为8888）
	 */
	public String tranCode;
	
	/*
	 * 商户代码
	 */
	public String merchantID;
	
	/*
	 * 订单号
	 */
	public String merOrderNum;
	
	/*
	 * 交易金额
	 */
	public String tranAmt;
	
	/*
	 * 商户提取佣金金额
	 */
	public String feeAmt;
	
	/*
	 * 币种 目前只支持人民币（156）
	 */
	public String currencyType;
	
	/*
	 * 商户前台通知地址
	 */
	public String frontMerUrl;
	
	/*
	 *商户后台通知地址
	 */
	public String backgroundMerUrl;
	/*
	 *交易时间
	 */
	public String tranDateTime;
	/*
	 *国付宝转入账户
	 */
	public String virCardNoIn;
	/*
	 *用户浏览器ip
	 */
	public String tranIP;
	/*
	 *订单是否允许重复提交 0不允许 1允许
	 */
	public String isRepeatSubmit;
	
	
	/*
	 * 密文串
	 */
	public  String signValue;
	
	
	/*
	 * 防钓鱼必填字段
	 * 服务器时间
	 * 从https://gateway.gopay.com.cn/time.do中获取
	 */
	public  String gopayServerTime;
	

	
	//直连银行必填字段
	
	/*
	 * 银行代码
	 */
	public  String  bankCode;
	
	/*
	 * 用户类型  
	 * 1:个人支付
	 * 2:企业支付
	 * 3:信用卡快捷支付
	 * 4:借记卡快捷支付
	 */
	public  String  userType;
	
	/*
	 *国付宝内部订单号
	 */
	public  String  orderId;
	

	/*
	 *网关发往银行的流水号
	 */
	public  String  gopayOutOrderId;
	
	/*
	 * 响应码
	 */
	public  String  respCode;
	
	/*
	 * 企业ID
	 */
	public  String  customerId;
	
	/*
	 * 国付宝账户
	 */
	public  String  payAcctId;
	
	/*
	 * 企业后台URL
	 */
	public  String  merURL;
	
	/*
	 * 收款人银行开户名
	 */
	public  String  recvBankAcctName;
	
	/*
	 * 收款方银行所在省份
	 */
	public  String  recvBankProvince;
	
	/*
	 * 收款方银行所在城市
	 */
	public  String  recvBankCity;
	
	/*
	 * 收款方银行名称
	 */
	public  String  recvBankName;
	
	/*
	 * 收款方银行网点名称
	 */
	public  String  recvBankBranchName;
	
	/*
	 * 公私标识   1对公 2对私
	 */
	public  String  corpPersonFlag;
	
	/*
	 * 编码方式  1GBK 2UTF-8
	 */
	public  String  merchantEncode;
	
	/*
	 * 是否企业审核
	 */
	public  String  approve;
	
	/*
	 * 交易备注
	 */
	public  String  description;
	
	/*
	 * 收款方银行账号
	 */
	public  String  recvBankAcctNum;
	
	/*
	 * 原订单号
	 */
	public  String  orgOrderNum;
	
	/*
	 * 原交易时间
	 */
	public  String  orgtranDateTime;
	
	/*
	 * 原交易状态
	 */
	public  String  orgTxnStat;
	
	/*
	 * 错误代码
	 */
	public  String  errCode;
	
	
	/*
	 * 错误信息
	 */
	public  String  errMessage;
	
	/*
	 * 调用类型(格式：字符串WX_WEB 调用微信扫码预下单接口本域指明了交易调用类型)
	 */
	public  String  callType;
	/*
	 * 商品描述
	 */
	public  String  goodsDetail;
	
	
	
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getGoodsDetail() {
		return goodsDetail;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCallType() {
		return callType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}

	/*
	 * 商户识别码
	 */
	public  String  verficationCode;
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getVerficationCode() {
		return verficationCode;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setVerficationCode(String verficationCode) {
		this.verficationCode = verficationCode;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getErrMessage() {
		return errMessage;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrgOrderNum() {
		return orgOrderNum;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrgtranDateTime() {
		return orgtranDateTime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrgTxnStat() {
		return orgTxnStat;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrgOrderNum(String orgOrderNum) {
		this.orgOrderNum = orgOrderNum;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrgtranDateTime(String orgtranDateTime) {
		this.orgtranDateTime = orgtranDateTime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrgTxnStat(String orgTxnStat) {
		this.orgTxnStat = orgTxnStat;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRecvBankAcctNum() {
		return recvBankAcctNum;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRecvBankAcctNum(String recvBankAcctNum) {
		this.recvBankAcctNum = recvBankAcctNum;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMerURL() {
		return merURL;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMerURL(String merURL) {
		this.merURL = merURL;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCustomerId() {
		return customerId;
	}

	

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPayAcctId() {
		return payAcctId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPayAcctId(String payAcctId) {
		this.payAcctId = payAcctId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	
	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCharset() {
		return Charset;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getLanguage() {
		return Language;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSignType() {
		return signType;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTranCode() {
		return tranCode;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMerchantID() {
		return merchantID;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMerOrderNum() {
		return merOrderNum;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTranAmt() {
		return tranAmt;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFeeAmt() {
		return feeAmt;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCurrencyType() {
		return currencyType;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getFrontMerUrl() {
		return frontMerUrl;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBackgroundMerUrl() {
		return backgroundMerUrl;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTranDateTime() {
		return tranDateTime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getVirCardNoIn() {
		return virCardNoIn;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTranIP() {
		return tranIP;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getIsRepeatSubmit() {
		return isRepeatSubmit;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSignValue() {
		return signValue;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getGopayServerTime() {
		return gopayServerTime;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getUserType() {
		return userType;
	}

	
	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCharset(String charset) {
		Charset = charset;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getVersion() {
		return version;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setLanguage(String language) {
		Language = language;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSignType(String signType) {
		this.signType = signType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMerOrderNum(String merOrderNum) {
		this.merOrderNum = merOrderNum;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setFrontMerUrl(String frontMerUrl) {
		this.frontMerUrl = frontMerUrl;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBackgroundMerUrl(String backgroundMerUrl) {
		this.backgroundMerUrl = backgroundMerUrl;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTranDateTime(String tranDateTime) {
		this.tranDateTime = tranDateTime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setVirCardNoIn(String virCardNoIn) {
		this.virCardNoIn = virCardNoIn;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTranIP(String tranIP) {
		this.tranIP = tranIP;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setIsRepeatSubmit(String isRepeatSubmit) {
		this.isRepeatSubmit = isRepeatSubmit;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setGopayServerTime(String gopayServerTime) {
		this.gopayServerTime = gopayServerTime;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getGopayOutOrderId() {
		return gopayOutOrderId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRespCode() {
		return respCode;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setGopayOutOrderId(String gopayOutOrderId) {
		this.gopayOutOrderId = gopayOutOrderId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	
	

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRecvBankAcctName() {
		return recvBankAcctName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRecvBankProvince() {
		return recvBankProvince;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRecvBankCity() {
		return recvBankCity;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRecvBankName() {
		return recvBankName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getRecvBankBranchName() {
		return recvBankBranchName;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCorpPersonFlag() {
		return corpPersonFlag;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMerchantEncode() {
		return merchantEncode;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getApprove() {
		return approve;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getDescription() {
		return description;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRecvBankAcctName(String recvBankAcctName) {
		this.recvBankAcctName = recvBankAcctName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRecvBankProvince(String recvBankProvince) {
		this.recvBankProvince = recvBankProvince;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRecvBankCity(String recvBankCity) {
		this.recvBankCity = recvBankCity;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRecvBankName(String recvBankName) {
		this.recvBankName = recvBankName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setRecvBankBranchName(String recvBankBranchName) {
		this.recvBankBranchName = recvBankBranchName;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCorpPersonFlag(String corpPersonFlag) {
		this.corpPersonFlag = corpPersonFlag;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMerchantEncode(String merchantEncode) {
		this.merchantEncode = merchantEncode;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setApprove(String approve) {
		this.approve = approve;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	



}
