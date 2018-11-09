/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.annotation.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义注解，记录一下当前方法的中文名字</p>
 * @author         Yuan Zhicheng 
 * @Date           2015年9月16日 上午11:04:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Mark {

	String name();

}
