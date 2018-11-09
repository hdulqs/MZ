package com.mz.front.user.entrust.model;

import java.math.BigDecimal;
import java.util.Date;


public class Entrust implements Comparable<Entrust> {
	
	private String entrust_type;//委托类型,buy买,sell卖
	
	private String entrust_number; //委托单号
	
	private String account_id;  //账户id
	
	private String customer_id;  //客户id
	
	private String coin_code;  //货币代码
	
	private BigDecimal entrust_price;  //委托价格
	
	private BigDecimal entrust_amout;  //委托数量
	
	private Date entrust_time;  //委托时间
	
	
	
	
	
	
	

	public String getEntrust_type() {
		return entrust_type;
	}

	public void setEntrust_type(String entrust_type) {
		this.entrust_type = entrust_type;
	}

	public String getEntrust_number() {
		return entrust_number;
	}

	public void setEntrust_number(String entrust_number) {
		this.entrust_number = entrust_number;
	}

	/**
	 * <p>账户id</p>
	 * @author:  
	 * @return:  String 
	 * @Date :   2017-08-21 19:49:42    
	 */
	public String getAccount_id() {
		return account_id;
	}
	
	/**
	 * <p>账户id</p>
	 * @author:  
	 * @param:   @param account_id
	 * @return:  void 
	 * @Date :   2017-08-21 19:49:42   
	 */
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	
	
	/**
	 * <p>客户id</p>
	 * @author:  
	 * @return:  String 
	 * @Date :   2017-08-21 19:49:42    
	 */
	public String getCustomer_id() {
		return customer_id;
	}
	
	/**
	 * <p>客户id</p>
	 * @author:  
	 * @param:   @param customer_id
	 * @return:  void 
	 * @Date :   2017-08-21 19:49:42   
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	
	
	/**
	 * <p>货币代码</p>
	 * @author:  
	 * @return:  String 
	 * @Date :   2017-08-21 19:49:42    
	 */
	public String getCoin_code() {
		return coin_code;
	}
	
	/**
	 * <p>货币代码</p>
	 * @author:  
	 * @param:   @param coin_code
	 * @return:  void 
	 * @Date :   2017-08-21 19:49:42   
	 */
	public void setCoin_code(String coin_code) {
		this.coin_code = coin_code;
	}
	
	
	/**
	 * <p>委托价格</p>
	 * @author:  
	 * @return:  BigDecimal 
	 * @Date :   2017-08-21 19:49:42    
	 */
	public BigDecimal getEntrust_price() {
		return entrust_price;
	}
	
	/**
	 * <p>委托价格</p>
	 * @author:  
	 * @param:   @param entrust_price
	 * @return:  void 
	 * @Date :   2017-08-21 19:49:42   
	 */
	public void setEntrust_price(BigDecimal entrust_price) {
		this.entrust_price = entrust_price;
	}
	
	
	/**
	 * <p>委托数量</p>
	 * @author:  
	 * @return:  BigDecimal 
	 * @Date :   2017-08-21 19:49:42    
	 */
	public BigDecimal getEntrust_amout() {
		return entrust_amout;
	}
	
	/**
	 * <p>委托数量</p>
	 * @author:  
	 * @param:   @param entrust_amout
	 * @return:  void 
	 * @Date :   2017-08-21 19:49:42   
	 */
	public void setEntrust_amout(BigDecimal entrust_amout) {
		this.entrust_amout = entrust_amout;
	}
	
	
	/**
	 * <p>委托时间</p>
	 * @author:  
	 * @return:  Date 
	 * @Date :   2017-08-21 19:49:42    
	 */
	public Date getEntrust_time() {
		return entrust_time;
	}
	
	/**
	 * <p>委托时间</p>
	 * @author:  
	 * @param:   @param entrust_time
	 * @return:  void 
	 * @Date :   2017-08-21 19:49:42   
	 */
	public void setEntrust_time(Date entrust_time) {
		this.entrust_time = entrust_time;
	}

	@Override
	public int compareTo(Entrust o) {
		
		if(this.entrust_price.compareTo(o.getEntrust_price())==1){  
            return -1;  
        }else if(this.entrust_price.compareTo(o.getEntrust_price())==-1){  
            return 1;  
        }else{  
            return 0;  
        }  
		
		
	}
	
	
	
	
	
	

}
