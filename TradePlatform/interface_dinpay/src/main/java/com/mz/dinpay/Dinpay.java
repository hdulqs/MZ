/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月5日 上午9:07:19
 */
package com.mz.dinpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 智付
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月5日 上午9:07:19 
 */
@XmlRootElement(name="dinpay")
@XmlAccessorType(XmlAccessType.FIELD)
public class Dinpay {
	
	
	 //充值地址
	 static final String RECHARGEURL="https://pay.dinpay.com/gateway?input_charset=";
	 
	 
	 //查询身份证信息接口 (收费接口 查询一次6元)
	 static final String IDENTITYRL="https://identiy.dinpay.com/IdentityCheck";
	 
	 //查询接口service
	 static final String CHECKIDENTITY="identity_check";
	 
	 //二次查询
	 static final String IDENTITYQUERY="https://identiy.dinpay.com/IdentityQuery";
	 //二次查询
	 static final String IDENTITY_QUERY = "identity_query";
	
	//商户号
	private String  merchant_code;
	
	
	//业务类型  direct_pay   b2b_pay
	private String  service_type;
	
	
	//接口版本
	private String  interface_version;
	
	
	//参数编码字符集
	private String  input_charset;
	
	
	//服务器异步通知地址
	private  String  notify_url;
	
	
	//签名方式
	private String  sign_type;
	
	
	//签名
	private String  sign;
	
	
	//页面跳转同步通知地址
	private String  return_url;
	
	
	//支付类型 b2c platefform dcard express wxpay
	private String  pay_type;
	
	
	//客户端ip
	private String  client_ip;
	
	
	//订单号
	private String  order_no;
	
	
	//商户订单时间  yyyy-MM-dd HH:mm:ss
	private String  order_time;
	
	
	//商户订单总金额
	private String  order_amount;
	
	
	//商品名称
	private String  product_name;
	
	
	//通知类型  page_notify   offline_notify
	private String  notify_type;
	
	
	//通知校验id
	private String  notify_id;
	
	
	//智付交易订单号
	private String  trade_no;
	
	
	//智付交易订单时间
	private String  trade_time;
	
	
	//交易状态
	private String  trade_status;
	
	
	//公用回传参数
	private String  extra_return_param;
	
	
	//银行交易流水号
	private String  bank_seq_no;
	
	
	
	//商家流水号
	private String  merchant_serial_no;
	
	
	//身份证号码
    private String  id_no;
    
	//响应信息
    private Response  response;
    
  

	//状态  0 通过  1 不通过  2 处理中
    private String  status;
    
    /**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMerchant_serial_no() {
		return merchant_serial_no;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getStatus() {
		return status;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getId_no() {
		return id_no;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getReal_name() {
		return real_name;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMerchant_serial_no(String merchant_serial_no) {
		this.merchant_serial_no = merchant_serial_no;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setId_no(String id_no) {
		this.id_no = id_no;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}


	//姓名
    private String  real_name;


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getMerchant_code() {
		return merchant_code;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getService_type() {
		return service_type;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getInterface_version() {
		return interface_version;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getInput_charset() {
		return input_charset;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getNotify_url() {
		return notify_url;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSign_type() {
		return sign_type;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSign() {
		return sign;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getReturn_url() {
		return return_url;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getPay_type() {
		return pay_type;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getClient_ip() {
		return client_ip;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrder_no() {
		return order_no;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrder_time() {
		return order_time;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getOrder_amount() {
		return order_amount;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getProduct_name() {
		return product_name;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getNotify_type() {
		return notify_type;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getNotify_id() {
		return notify_id;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTrade_no() {
		return trade_no;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTrade_time() {
		return trade_time;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getTrade_status() {
		return trade_status;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getExtra_return_param() {
		return extra_return_param;
	}


	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getBank_seq_no() {
		return bank_seq_no;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setService_type(String service_type) {
		this.service_type = service_type;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setInterface_version(String interface_version) {
		this.interface_version = interface_version;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setExtra_return_param(String extra_return_param) {
		this.extra_return_param = extra_return_param;
	}


	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setBank_seq_no(String bank_seq_no) {
		this.bank_seq_no = bank_seq_no;
	}
	
	  /**
		 * <p> TODO</p>
		 * @return:     Response
		 */
		public Response getResponse() {
			return response;
		}


		/** 
		 * <p> TODO</p>
		 * @return: Response
		 */
		public void setResponse(Response response) {
			this.response = response;
		}

public static void main(String[] args) {
	
}	
	
	
}
