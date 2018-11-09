package com.mz.aop;

import com.alibaba.fastjson.JSON;
import com.mz.manage.logs.model.AppManageLogs;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.httpRequest.IpUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.annotation.AppManageLogsAop;
import com.mz.manage.logs.service.AppManageLogsService;
import com.mz.manage.remote.model.LmcTransfer;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class AppManageLogsAspect {
	@Resource
	private AppManageLogsService appManageLogsService;
	
	
	// controller的层切点
	@Pointcut("@annotation(com.mz.annotation.AppManageLogsAop)")
	public void controllerAspect() {

	}
	
	@Before("controllerAspect()")
	public void doBefore(final JoinPoint joinPoint) {
//		System.out.println("before");
	}
	/**
	* @Description:    后台操作日志
	* @Author:         zongwei
	* @CreateDate:     2018/7/10 10:53
	* @UpdateUser:    zongwei
	* @UpdateDate:     2018/7/10 10:53
	* @UpdateRemark:   创建
	* @Version:        1.0
	*/
	@Around(value = "controllerAspect()&&@annotation(LogsArg)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint,AppManageLogsAop LogsArg) throws Throwable {
		Object [] arr=proceedingJoinPoint.getArgs();
		LmcTransfer transfer=null;
		for(Object l:arr){
			if(l instanceof LmcTransfer){
				transfer=(LmcTransfer) l;
			}
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		AppManageLogs appManageLogs = new AppManageLogs();
		String url=request.getRequestURL().toString();
		String ip=IpUtil.getIp(request);
		String account=transfer==null?null:transfer.getApp_key();

		AppUser user = ContextUtil.getCurrentUser();
        String targetName = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = proceedingJoinPoint.getSignature().getName();

		appManageLogs.setRequestUrl(url);
		appManageLogs.setIp(ip);
		appManageLogs.setClassName(targetName);
		appManageLogs.setMethodName(methodName);
		appManageLogs.setUserName(user.getUsername());
		appManageLogs.setRemark(LogsArg.remark());
		try {
			appManageLogs.setArgsContent(JSON.toJSONString(arr));
		}catch (Exception e){
			appManageLogs.setArgsContent(JSON.toJSONString(transfer));
		}


		//controller刚进来保存日志
		appManageLogsService.save(appManageLogs);
		Object obj=proceedingJoinPoint.proceed();
		if(obj!=null){
			appManageLogs.setReturnValue(JSON.toJSONString(obj));
			//更新返回值
			appManageLogsService.update(appManageLogs);
		}
		return obj;
	}
	@After("controllerAspect()")
	public void Afters(JoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		StringBuffer arg = new StringBuffer();
		for (int i = 0; i < args.length; ++i) {
			System.out.println("\t==>参数[" + i + "]:\t" + args[i].toString());
			arg.append(args[i].toString());
		}

	}
	
	@AfterReturning(value = "controllerAspect()", argNames = "joinPoint,returnValue", returning = "returnValue")
	public void after(final JoinPoint joinPoint, final Object returnValue) {
//		System.out.println("after");
	}
}
