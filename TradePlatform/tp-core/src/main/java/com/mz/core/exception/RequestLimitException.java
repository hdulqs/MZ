/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月9日 下午3:06:02
 */
package com.mz.core.exception;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月9日 下午3:06:02 
 */
public class RequestLimitException extends Exception {
	  private static final long serialVersionUID = 1364225358754654702L;
	  public RequestLimitException() {
	    super("HTTP请求超出设定的限制");
	  }
	  public RequestLimitException(String message) {
	    super(message);
	  }
	}