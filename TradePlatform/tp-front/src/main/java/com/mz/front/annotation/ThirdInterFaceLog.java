package com.mz.front.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 调用第三方接口日志
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年8月8日 下午7:48:48
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThirdInterFaceLog {
	
}
