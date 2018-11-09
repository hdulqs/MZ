package com.mz.coin;

import java.io.Serializable;

/**
 * Tv币tx记录
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年12月4日 下午4:18:19
 */
public class Tvtx implements Serializable{

	private String block_num;

	private String block_position;

	private String expiration_timestamp;

	private String fee;

	private String is_confirmed;

	private String is_market;

	private String is_market_cancel;

	private String is_virtual;

	private String ledger_entries;

	private String timestamp;

	private String trx_id;

	private String trx_type;

	public String getBlock_num() {
		return block_num;
	}

	public void setBlock_num(String block_num) {
		this.block_num = block_num;
	}

	public String getBlock_position() {
		return block_position;
	}

	public void setBlock_position(String block_position) {
		this.block_position = block_position;
	}

	public String getExpiration_timestamp() {
		return expiration_timestamp;
	}

	public void setExpiration_timestamp(String expiration_timestamp) {
		this.expiration_timestamp = expiration_timestamp;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getIs_confirmed() {
		return is_confirmed;
	}

	public void setIs_confirmed(String is_confirmed) {
		this.is_confirmed = is_confirmed;
	}

	public String getIs_market() {
		return is_market;
	}

	public void setIs_market(String is_market) {
		this.is_market = is_market;
	}

	public String getIs_market_cancel() {
		return is_market_cancel;
	}

	public void setIs_market_cancel(String is_market_cancel) {
		this.is_market_cancel = is_market_cancel;
	}

	public String getIs_virtual() {
		return is_virtual;
	}

	public void setIs_virtual(String is_virtual) {
		this.is_virtual = is_virtual;
	}

	public String getLedger_entries() {
		return ledger_entries;
	}

	public void setLedger_entries(String ledger_entries) {
		this.ledger_entries = ledger_entries;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}

	public String getTrx_type() {
		return trx_type;
	}

	public void setTrx_type(String trx_type) {
		this.trx_type = trx_type;
	}
	
}
