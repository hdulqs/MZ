/**
 * Copyright:   北京互融时代软件有限公司
 * @author:         Liu Shilei 
 * @version:      V1.0 
 * @Date :          2015年10月21日 下午6:21:58
 */
package com.mz.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月21日 下午6:21:58
 */
@Aspect
@Component
public class MyConcurrenceExceptionAop {

	@Before(value = "@annotation(com.mz.core.annotation.MyConcurrenceException)")
	public void afterInsertMethod(JoinPoint joinPoint) {
		Class<? extends Object> clazz = joinPoint.getTarget().getClass();
		RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
		String[] value = requestMapping.value();
		String rootPath = value[0];
		String methodeName = joinPoint.getSignature().getName();
	}
	
	@AfterThrowing(value = "@annotation(com.mz.core.annotation.MyConcurrenceException)")
	public void afterThrowing(){
		System.out.println("报异常了");
	}
	

}
