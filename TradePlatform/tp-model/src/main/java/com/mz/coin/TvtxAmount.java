package com.mz.coin;

import java.io.Serializable;

/**
 * Tv amount
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年12月4日 下午5:27:06
 */
public class TvtxAmount implements Serializable{
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
