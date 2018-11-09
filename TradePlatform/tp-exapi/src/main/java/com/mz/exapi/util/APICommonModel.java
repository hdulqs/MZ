/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月16日 上午11:24:10
 */
package com.mz.exapi.util;

import javax.validation.constraints.NotNull;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月16日 上午11:24:10 
 */
public class APICommonModel extends APIBaseModel{
	//签名顺序就按正常字段先后顺序，private不参加签名   
    @NotNull
	private String coinCode;

	public String getCoinCode() {
		return coinCode;
	}
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	
  

}
