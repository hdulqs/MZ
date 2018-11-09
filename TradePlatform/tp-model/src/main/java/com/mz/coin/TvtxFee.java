package com.mz.coin;

/**
 * 
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2018年3月7日 下午7:01:14
 */
public class TvtxFee implements java.io.Serializable{
	private String amount;
	private String asset_id;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}
}
