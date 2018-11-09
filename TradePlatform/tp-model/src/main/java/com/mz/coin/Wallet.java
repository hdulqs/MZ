package com.mz.coin;

import java.io.Serializable;

/**
 * 
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年3月13日 下午7:37:42
 */
public class Wallet implements Serializable{
	private int id;
	/**
	 * 币种编码
	 */
	private String coinCode;
	/**
	 * 钱包总余额
	 */
	private String totalMoney;
	/**
	 * 提币地址金额
	 */
	private String withdrawalsAddressMoney;
	/**
	 * 提币地址
	 */
	private String withdrawalsAddress;
	/**
	 * 冷钱包地址
	 */
	private String coldwalletAddress;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCoinCode() {
		return coinCode;
	}
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getWithdrawalsAddressMoney() {
		return withdrawalsAddressMoney;
	}
	public void setWithdrawalsAddressMoney(String withdrawalsAddressMoney) {
		this.withdrawalsAddressMoney = withdrawalsAddressMoney;
	}
	public String getWithdrawalsAddress() {
		return withdrawalsAddress;
	}
	public void setWithdrawalsAddress(String withdrawalsAddress) {
		this.withdrawalsAddress = withdrawalsAddress;
	}
	public String getColdwalletAddress() {
		return coldwalletAddress;
	}
	public void setColdwalletAddress(String coldwalletAddress) {
		this.coldwalletAddress = coldwalletAddress;
	}
	
}
