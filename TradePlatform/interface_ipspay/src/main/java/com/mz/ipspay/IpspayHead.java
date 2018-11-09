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
@XmlRootElement(name = "head")
@XmlAccessorType(XmlAccessType.FIELD)
public class IpspayHead {
	/** 版本号 */
	static final String VERSION = "v1.0.0";
	/** 环迅充值地址 */
	static final String RECHARGEURL = "https://newpay.ips.com.cn/psfp-entry/gateway/payment.do";

	/** 版本号(可选) */
	private String Version;
	/** 商户号 */
	private String MerCode;
	/** 商户名(可选) */
	private String MerName;
	/** 账户号 */
	private String Account;
	/** 消息编号(可选) */
	private String MsgId;
	/** 商户请求时间  格式：yyyyMMddHHmmss */
	private String ReqDate;
	/** 数字签名 */
	private String Signature;
	
	
	
	
	
	
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getMerCode() {
		return MerCode;
	}
	public void setMerCode(String merCode) {
		MerCode = merCode;
	}
	public String getMerName() {
		return MerName;
	}
	public void setMerName(String merName) {
		MerName = merName;
	}
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getReqDate() {
		return ReqDate;
	}
	public void setReqDate(String reqDate) {
		ReqDate = reqDate;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
}
