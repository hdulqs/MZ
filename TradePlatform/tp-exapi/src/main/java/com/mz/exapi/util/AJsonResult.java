/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.exapi.util;

import java.io.Serializable;

/**
 * 封装json结果集
 * 
 * 
 * 
 * @author Yuan Zhicheng
 *
 */
public class AJsonResult implements Serializable{

	private Boolean success = false;// 返回是否成功
	private String msg = "";// 返回信息
	private String code = "";// 返回信息
	private Object obj = null;// 返回其他对象信息

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
