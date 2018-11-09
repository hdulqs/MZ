package com.mz.shanfupay;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年9月19日 上午10:51:49
 */


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShanfuResponse {
	/**充值地址*/
	static final String RECHARGEURL = "http://gateway.safeepay.com/interface.html";
	/**业务类型*/
	static final String CMD = "Buy";
	/**币种*/
	static final String CUR = "CNY";
	/**支付结果  固定值 “1”, 代表支付成功*/
	static final String CODE_SUCCESS = "1";

	
	
	/**商户编号*/
	private String p1_MerId; 
	/**业务类型*/
	private String r0_Cmd;
	/**支付结果*/
	private String r1_Code; 
	/**闪付支付交易流水号*/
	private String r2_TrxId;
	/**支付金额*/
	private String r3_Amt;
	/**交易币种(可选)*/
	private String r4_Cur;
	/**商品名称(可选)*/
	private String r5_Pid;
	/**商户订单号(可选)*/
	private String r6_Order;
	/**闪付支付会员ID(可选)*/
	private String r7_Uid;
	/**商户扩展信息(可选)*/
	private String r8_MP;
	/**交易结果返回类型(可选)*/
	private String r9_BType;
	/**签名密钥*/
	private String hmac; // 密钥
	
	
	
	
	
	public String getP1_MerId() {
		return p1_MerId;
	}
	public void setP1_MerId(String p1_MerId) {
		this.p1_MerId = p1_MerId;
	}
	
	public String getR0_Cmd() {
		return r0_Cmd;
	}
	public void setR0_Cmd(String r0_Cmd) {
		this.r0_Cmd = r0_Cmd;
	}
	public String getR1_Code() {
		return r1_Code;
	}
	public void setR1_Code(String r1_Code) {
		this.r1_Code = r1_Code;
	}
	public String getR2_TrxId() {
		return r2_TrxId;
	}
	public void setR2_TrxId(String r2_TrxId) {
		this.r2_TrxId = r2_TrxId;
	}
	public String getR3_Amt() {
		return r3_Amt;
	}
	public void setR3_Amt(String r3_Amt) {
		this.r3_Amt = r3_Amt;
	}
	public String getR4_Cur() {
		return r4_Cur;
	}
	public void setR4_Cur(String r4_Cur) {
		this.r4_Cur = r4_Cur;
	}
	public String getR5_Pid() {
		return r5_Pid;
	}
	public void setR5_Pid(String r5_Pid) {
		this.r5_Pid = r5_Pid;
	}
	public String getR6_Order() {
		return r6_Order;
	}
	public void setR6_Order(String r6_Order) {
		this.r6_Order = r6_Order;
	}
	public String getR7_Uid() {
		return r7_Uid;
	}
	public void setR7_Uid(String r7_Uid) {
		this.r7_Uid = r7_Uid;
	}
	public String getR8_MP() {
		return r8_MP;
	}
	public void setR8_MP(String r8_MP) {
		this.r8_MP = r8_MP;
	}
	public String getR9_BType() {
		return r9_BType;
	}
	public void setR9_BType(String r9_BType) {
		this.r9_BType = r9_BType;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	
	
}
