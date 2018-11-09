/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Shangxl
 * @version:      V1.0 
 * @Date:        2017年12月4日 下午4:25:32
 */
package com.mz.coin;

import java.io.Serializable;

/**
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年12月4日 下午4:25:32 
 */
public class TvtxLedgerEntries implements Serializable{
	private String to_account_name;
	private String amount;
	private String from_account;
	private String from_account_name;
	private String memo;
	private String running_balances;
	private String to_account;
	
	
	public String getTo_account_name() {
		return to_account_name;
	}
	public void setTo_account_name(String to_account_name) {
		this.to_account_name = to_account_name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFrom_account() {
		return from_account;
	}
	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}
	public String getFrom_account_name() {
		return from_account_name;
	}
	public void setFrom_account_name(String from_account_name) {
		this.from_account_name = from_account_name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getRunning_balances() {
		return running_balances;
	}
	public void setRunning_balances(String running_balances) {
		this.running_balances = running_balances;
	}
	public String getTo_account() {
		return to_account;
	}
	public void setTo_account(String to_account) {
		this.to_account = to_account;
	}
}
