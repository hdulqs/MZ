/**
 * Copyright:   北京互融时代软件有限公司
 * @author:         Liu Shilei 
 * @version:      V1.0 
 * @Date :          2015年10月21日 下午6:21:58
 */
package com.mz.core.aop;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.util.UUIDUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.oauth.user.model.AppUser;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * <p>过滤Controller访问权限的aop</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月21日 下午6:21:58
 */
@Aspect
@Component
public class MyRequiresPermissionsAop {

	@Before(value = "@annotation(com.mz.core.annotation.MyRequiresPermissions)")
	public void afterInsertMethod(JoinPoint joinPoint) {
		
		try {
			AppUser user = ContextUtil.getCurrentUser();
			if(user!=null&&user.getUsername().equals(PropertiesUtils.APP.getProperty("app.admin"))){
				return ;
			}
		} catch (Exception e) {
		}
		
		Class<? extends Object> clazz = joinPoint.getTarget().getClass();
		RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
		String[] value = requestMapping.value();
		String rootPath = value[0];
		String methodeName = joinPoint.getSignature().getName();
		try {
			//Method method = clazz.getMethod(methodeName);
			//获得当前类的所有方法，不包括继承方法
			Method[] declaredMethods = clazz.getDeclaredMethods();
			Method method = null;
			for(Method m : declaredMethods){
				if(methodeName.equals(m.getName())){
					method = m;
					break;
				}
			}
			String[] split = StringUtils.split(clazz.getName(), ".");
			String appKey = split[1];
			String methodTag = "";
			//获得自定义权限
			MyRequiresPermissions myRequiresPermissions = method.getAnnotation(MyRequiresPermissions.class);
			String[] myRequiresPermissionsValue = myRequiresPermissions.value();
			if(myRequiresPermissionsValue!=null&&myRequiresPermissionsValue.length>0){//数组不为空时一组权限鉴权
				
				String shiroUrl = "";
				for(int i = 0 ; i < myRequiresPermissionsValue.length ; i++ ){
					String shiroUrlkey  = appKey+rootPath+myRequiresPermissionsValue[i] ;
					shiroUrl += shiroUrlkey;
					if(i!=myRequiresPermissionsValue.length-1){
						shiroUrl += " or ";
					}
				}
				boolean permitted = SecurityUtils.getSubject().isPermitted(shiroUrl);
				if(!permitted){//如果permitted为false  是抛个随机的权限
					SecurityUtils.getSubject().checkPermissions(UUIDUtil.getUUID());
				}
				
			}else{//获得requestMapping   一个权限标签鉴权
				RequestMapping methodRequestMapping =method.getAnnotation(RequestMapping.class);
				if(methodRequestMapping.value()[0].contains("{")){
					methodTag  = methodRequestMapping.value()[0].substring(0, methodRequestMapping.value()[0].indexOf("{")-1);
				}else{
					methodTag = methodRequestMapping.value()[0];
				}
				String shiroUrl = appKey+rootPath+methodTag;
				SecurityUtils.getSubject().checkPermission(shiroUrl);
			}
			
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
