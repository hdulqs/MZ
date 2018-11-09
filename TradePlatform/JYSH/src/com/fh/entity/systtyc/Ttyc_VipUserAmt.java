package com.fh.entity.systtyc;

import java.math.BigDecimal;

public class Ttyc_VipUserAmt {
	private Integer uid;
	private Integer amountt;
	private String mno;
	private String remark;
	
	private BigDecimal niudoufee;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getAmountt() {
		return amountt;
	}

	public void setAmountt(Integer amountt) {
		this.amountt = amountt;
	}

	public String getMno() {
		return mno;
	}

	public void setMno(String mno) {
		this.mno = mno;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getNiudoufee() {
		return niudoufee;
	}

	public void setNiudoufee(BigDecimal niudoufee) {
		this.niudoufee = niudoufee;
	} 
	
}
