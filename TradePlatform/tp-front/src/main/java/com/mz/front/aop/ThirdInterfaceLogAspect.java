package com.mz.front.aop;

import com.mz.manage.remote.model.AppLogThirdInterfaceDTO;
import com.mz.manage.remote.model.LmcTransfer;
import com.mz.remote.RemoteThirdInterfaceService;
import com.mz.util.IpUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

@Component
@Aspect
public class ThirdInterfaceLogAspect {
	@Resource
	private RemoteThirdInterfaceService remoteThirdInterfaceService;
	
	
	// controller层切点
	@Pointcut("@annotation(com.mz.front.annotation.ThirdInterFaceLog)")
	public void controllerAspect() {

	}
	
	@Before("controllerAspect()")
	public void doBefore(final JoinPoint joinPoint) {
//		System.out.println("before");
	}
	/**
	 * 其他平台调用我方平台接口，记录日志
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param proceedingJoinPoint
	 * @param:    @return
	 * @param:    @throws Throwable
	 * @return: Object 
	 * @Date :          2017年8月9日 下午4:49:28   
	 * @throws:
	 */
	@Around(value = "controllerAspect()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object [] arr=proceedingJoinPoint.getArgs();
		LmcTransfer transfer=null;
		for(Object l:arr){
			if(l instanceof LmcTransfer){
				transfer=(LmcTransfer) l;
			}
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
		AppLogThirdInterfaceDTO logDTO=new AppLogThirdInterfaceDTO();
		String url=request.getRequestURL().toString();
		String ip=IpUtil.getIp(request);
		String account=transfer==null?null:transfer.getApp_key();
		logDTO.setUrl(url);
		logDTO.setIp(ip);
		logDTO.setAccount(account);
		logDTO.setParams(JSON.toJSONString(transfer));
		//controller刚进来保存日志
		logDTO=remoteThirdInterfaceService.saveLog(logDTO);
		Object obj=proceedingJoinPoint.proceed();
		if(obj!=null){
			logDTO.setResult(JSON.toJSONString(obj));
			//更新返回值
			remoteThirdInterfaceService.saveLog(logDTO);
		}
		return obj;
	}
	
	@AfterReturning(value = "controllerAspect()", argNames = "returnValue", returning = "returnValue")
	public void after(final JoinPoint joinPoint, final Object returnValue) {
//		System.out.println("after");
	}
}
