package com.mz.xinsheng;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年9月19日 上午10:51:49
 */


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class XinshengResponse {
	/**业务类型*/
	static final String CMD = "Buy";
	/**币种*/
	static final String CUR = "CNY";
	/**支付结果  固定值 “1”, 代表支付成功*/
	static final String CODE_SUCCESS = "SUCCESS";

	
	
}
