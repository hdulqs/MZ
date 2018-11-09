package com.fh.entity.systtyc;

import java.math.BigDecimal;

public class Ttyc_vip_user_amt_seq {
	private Integer uid;
	private String REMARK;
	private Integer AMOUNT;
	private String MNO;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	public Integer getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Integer aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getMNO() {
		return MNO;
	}
	public void setMNO(String mNO) {
		MNO = mNO;
	}
	public BigDecimal getPROPORTION() {
		return PROPORTION;
	}
	public void setPROPORTION(BigDecimal pROPORTION) {
		PROPORTION = pROPORTION;
	}
	private BigDecimal PROPORTION;
	
	
}
