package com.mz.front.user.entrust.model;

import java.math.BigDecimal;
import java.util.Date;


/**
 * <p> EntrustInfo </p>
 * @author:         liushilei
 * @Date :          2017-08-21 19:50:04  
 */
public class EntrustInfo {
	
	//标记为买单还是卖单
	private String entrust_type;
	
	private String sell_id;  //卖方id
	
	private String buy_id;  //买方id
	
	private String coin_code;  //货币代码
	
	private BigDecimal dealAmout; //成交数量
	
	private BigDecimal buy_entrust_number;  //买方单号
	
	private String buy_account_id;  //买方账户id
	
	private BigDecimal buy_entrust_price;  //买方委托价格
	
	private BigDecimal buy_entrust_amout;  //买方委托数量
	
	private Date buy_entrust_time;  //买方委托时间
	
	private Integer buy_entrust_state;  //买方委托状态
	
	
	//--------------------------------------------------------------
	
	private BigDecimal sell_entrust_number;  //卖方单号
	
	private String sell_account_id;  //卖方账户id
	
	private BigDecimal sell_entrust_price;  //卖方委托价格
	
	private BigDecimal sell_entrust_amout;  //卖方委托数量
	
	private Date sell_entrust_time;  //卖方委托时间
	
	private Integer sell_entrust_state;  //卖方委托状态
	
	
	private String remark;  //操作说明
	
	
	
	
	public String getEntrust_type() {
		return entrust_type;
	}

	public void setEntrust_type(String entrust_type) {
		this.entrust_type = entrust_type;
	}

	/**
	 * <p>卖方id</p>
	 * @author:  
	 * @return:  String 
	 * @Date :   2017-08-21 19:50:04    
	 */
	public String getSell_id() {
		return sell_id;
	}
	
	/**
	 * <p>卖方id</p>
	 * @author:  
	 * @param:   @param sell_id
	 * @return:  void 
	 * @Date :   2017-08-21 19:50:04   
	 */
	public void setSell_id(String sell_id) {
		this.sell_id = sell_id;
	}
	
	
	/**
	 * <p>买方id</p>
	 * @author:  
	 * @return:  String 
	 * @Date :   2017-08-21 19:50:04    
	 */
	public String getBuy_id() {
		return buy_id;
	}
	
	/**
	 * <p>买方id</p>
	 * @author:  
	 * @param:   @param buy_id
	 * @return:  void 
	 * @Date :   2017-08-21 19:50:04   
	 */
	public void setBuy_id(String buy_id) {
		this.buy_id = buy_id;
	}
	
	
	/**
	 * <p>货币代码</p>
	 * @author:  
	 * @return:  String 
	 * @Date :   2017-08-21 19:50:04    
	 */
	public String getCoin_code() {
		return coin_code;
	}
	
	/**
	 * <p>货币代码</p>
	 * @author:  
	 * @param:   @param coin_code
	 * @return:  void 
	 * @Date :   2017-08-21 19:50:04   
	 */
	public void setCoin_code(String coin_code) {
		this.coin_code = coin_code;
	}

	public BigDecimal getBuy_entrust_price() {
		return buy_entrust_price;
	}

	public void setBuy_entrust_price(BigDecimal buy_entrust_price) {
		this.buy_entrust_price = buy_entrust_price;
	}

	public BigDecimal getBuy_entrust_amout() {
		return buy_entrust_amout;
	}

	public void setBuy_entrust_amout(BigDecimal buy_entrust_amout) {
		this.buy_entrust_amout = buy_entrust_amout;
	}

	public Date getBuy_entrust_time() {
		return buy_entrust_time;
	}

	public void setBuy_entrust_time(Date buy_entrust_time) {
		this.buy_entrust_time = buy_entrust_time;
	}

	public Integer getBuy_entrust_state() {
		return buy_entrust_state;
	}

	public void setBuy_entrust_state(Integer buy_entrust_state) {
		this.buy_entrust_state = buy_entrust_state;
	}

	public BigDecimal getSell_entrust_price() {
		return sell_entrust_price;
	}

	public void setSell_entrust_price(BigDecimal sell_entrust_price) {
		this.sell_entrust_price = sell_entrust_price;
	}

	public BigDecimal getSell_entrust_amout() {
		return sell_entrust_amout;
	}

	public void setSell_entrust_amout(BigDecimal sell_entrust_amout) {
		this.sell_entrust_amout = sell_entrust_amout;
	}

	public Date getSell_entrust_time() {
		return sell_entrust_time;
	}

	public void setSell_entrust_time(Date sell_entrust_time) {
		this.sell_entrust_time = sell_entrust_time;
	}

	public Integer getSell_entrust_state() {
		return sell_entrust_state;
	}

	public void setSell_entrust_state(Integer sell_entrust_state) {
		this.sell_entrust_state = sell_entrust_state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getBuy_entrust_number() {
		return buy_entrust_number;
	}

	public void setBuy_entrust_number(BigDecimal buy_entrust_number) {
		this.buy_entrust_number = buy_entrust_number;
	}

	public String getBuy_account_id() {
		return buy_account_id;
	}

	public void setBuy_account_id(String buy_account_id) {
		this.buy_account_id = buy_account_id;
	}

	public BigDecimal getSell_entrust_number() {
		return sell_entrust_number;
	}

	public void setSell_entrust_number(BigDecimal sell_entrust_number) {
		this.sell_entrust_number = sell_entrust_number;
	}

	public String getSell_account_id() {
		return sell_account_id;
	}

	public void setSell_account_id(String sell_account_id) {
		this.sell_account_id = sell_account_id;
	}

	public BigDecimal getDealAmout() {
		return dealAmout;
	}

	public void setDealAmout(BigDecimal dealAmout) {
		this.dealAmout = dealAmout;
	}
	
	
	
	
	

}
