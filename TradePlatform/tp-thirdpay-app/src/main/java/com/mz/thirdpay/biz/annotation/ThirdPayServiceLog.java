/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月11日 下午2:23:27
 */
package com.mz.thirdpay.biz.annotation;
           
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月11日 下午2:23:27 
 */

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public  @interface ThirdPayServiceLog {
                  
}
