/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年4月19日 下午4:10:07
 */
package com.mz.core.quartz;

import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import java.lang.reflect.Method;
import org.springframework.util.StringUtils;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年4月19日 下午4:10:07 
 */
public class TaskUtils {

	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	public static void invokMethod(ScheduleJob scheduleJob) {
		Object object = null;
		Class clazz = null;
        //springId不为空先按springId查找bean
		if (!StringUtils.isEmpty(scheduleJob.getSpringId())) {
			object = ContextUtil.getBean(scheduleJob.getSpringId());
		} else if (!StringUtils.isEmpty(scheduleJob.getBeanClass())) {
			try {
				clazz = Class.forName(scheduleJob.getBeanClass());
				object = clazz.newInstance();
			} catch (Exception e) {
				LogFactory.info("定时器不启动错误！！！ ");
				e.printStackTrace();
			}

		}
		if (object == null) {
			LogFactory.info("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
			return;
		}
		clazz = object.getClass();
		Method method = null;
		try {
			Object[] methodArgs = scheduleJob.getMethodArgs();
			if(methodArgs!=null){
				Class[] argsClass = new Class[methodArgs.length];  
			    for (int i = 0;i < methodArgs.length; i++) {  
			         argsClass[i] = methodArgs[i].getClass();  
			    }
				method = clazz.getDeclaredMethod(scheduleJob.getMethodName(),argsClass);
			}else{
				method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
			}
		} catch (NoSuchMethodException e) {
			LogFactory.info("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (method != null) {
			try {
				method.invoke(object,scheduleJob.getMethodArgs());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
