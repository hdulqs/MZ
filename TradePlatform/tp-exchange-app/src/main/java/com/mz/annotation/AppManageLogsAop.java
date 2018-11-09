package com.mz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @Description:    后台日志
* @Author:         zongwei
* @CreateDate:     2018/7/10 10:52
* @UpdateUser:    zongwei
* @UpdateDate:     2018/7/10 10:52
* @UpdateRemark:   创建
* @Version:        1.0
*/
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppManageLogsAop {
    String name() default ""; //用于写方法作用
    String remark() default ""; //用于写方法作用
}
