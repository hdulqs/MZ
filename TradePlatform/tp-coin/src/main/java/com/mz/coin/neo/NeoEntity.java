package com.mz.coin.neo;
/**
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2018年3月14日 上午9:42:34
 */
public class NeoEntity {
	/**
	 * 第几条记录
	 */
	private String n;
	/**
	 * 交易id（资产id）
	 */
	private String asset;
	/**
	 * 交易数量
	 */
	private String value;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * txId
	 */
	private String txId;
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	

}
