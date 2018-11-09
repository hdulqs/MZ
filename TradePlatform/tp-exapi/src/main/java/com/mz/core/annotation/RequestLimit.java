/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月9日 下午2:53:51
 */
package com.mz.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月9日 下午2:53:51 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
//最高优先级
public @interface RequestLimit {
    /**
     * 
     * 允许访问的次数，默认值MAX_VALUE
     */
    int count() default Integer.MAX_VALUE;

    /**
     * 
     * 时间段，单位为毫秒，默认值一分钟
     */
    int time() default 10;
}
