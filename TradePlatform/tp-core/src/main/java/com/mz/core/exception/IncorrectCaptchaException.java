/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年10月13日 下午4:58:40
 */
package com.mz.core.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2015年10月13日 下午4:58:40 
 */
public class IncorrectCaptchaException extends AuthenticationException {  
	  
    public IncorrectCaptchaException() {  
        super();  
    }  
  
    public IncorrectCaptchaException(String message, Throwable cause) {  
        super(message, cause);  
    }  
  
    public IncorrectCaptchaException(String message) {  
        super(message);  
    }  
  
    public IncorrectCaptchaException(Throwable cause) {  
        super(cause);  
    }  
}  
