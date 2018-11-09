package com.mz.dto.model;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2018年4月3日 上午10:48:31
 */
public class Token implements Serializable,Comparable<Token>{
	/**
	 * id
	 */
	private int	id;
	/**
	 * address
	 */
	private String 	address;
	/**
	 * 代币余额
	 */
	private BigDecimal 	tokenAssets;
	/**
	 * 以太坊余额
	 */
	private BigDecimal	etherAssets;
	/**
	 * 是否可归集
	 */
	private Boolean abledCollect;
	
	
	
	
	
	public Token(int id, String address, BigDecimal tokenAssets, BigDecimal etherAssets, Boolean abledCollect) {
		super();
		this.id = id;
		this.address = address;
		this.tokenAssets = tokenAssets;
		this.etherAssets = etherAssets;
		this.abledCollect = abledCollect;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getTokenAssets() {
		return tokenAssets;
	}
	public void setTokenAssets(BigDecimal tokenAssets) {
		this.tokenAssets = tokenAssets;
	}
	public BigDecimal getEtherAssets() {
		return etherAssets;
	}
	public void setEtherAssets(BigDecimal etherAssets) {
		this.etherAssets = etherAssets;
	}
	public Boolean getAbledCollect() {
		return abledCollect;
	}
	public void setAbledCollect(Boolean abledCollect) {
		this.abledCollect = abledCollect;
	}
	
	@Override
	public int compareTo(Token o) {
		int i=-1;
		if(this.tokenAssets!=null&&o.getTokenAssets()!=null){
			i = this.tokenAssets.compareTo(o.getTokenAssets());
	        if(i == 0&&this.etherAssets!=null&&o.getEtherAssets()!=null){  
	        	//如果代币余额相等，按照以太坊余额排序
	            i=this.etherAssets.compareTo(o.getEtherAssets());
	        }
		}
		return -i;
	}  
}
