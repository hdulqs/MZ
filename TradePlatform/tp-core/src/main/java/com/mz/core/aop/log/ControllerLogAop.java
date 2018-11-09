/**
 * Copyright:   北京互融时
 *
 * 代软件有限公司
 *
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2015年9月16日 上午11:04:39
 */
package com.mz.core.aop.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.log.AppLog;
import com.mz.core.mvc.service.log.AppLogService;
import com.mz.core.thread.ThreadPool;
import com.mz.util.JsonPropertyFilter;
import com.mz.util.UUIDUtil;
import com.mz.util.date.DateUtil;
import com.mz.util.httpRequest.IpUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用于拦截所有@RequestMapping注解的方法，也就是Controller的方法
 *
 * 将方法名称、传递的参数、返回值、IP等等信息存放在数据库表中
 *
 *
 *
 * @author Yuan Zhicheng
 *
 */
@Aspect
@Component
public class ControllerLogAop {

  @Autowired
  private AppLogService appLogService;

  @AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)", argNames = "returnValue", returning = "returnValue")
  public void afterInsertMethod(final JoinPoint joinPoint, final Object returnValue) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    final String ip = IpUtil.getIp(request);
    final String userName = (String) request.getSession().getAttribute("userName");
    final String requestURL = request.getRequestURL().toString();
    Runnable r = new Runnable() {
      @Override
      public void run() {
        Signature signature = joinPoint.getSignature();
        if (signature.getDeclaringTypeName()
            .equals("com.mz.core.mvc.controller.log.ControllerLogController")
            || signature.getDeclaringTypeName().equals("com.mz.web.app.controller.AppLogController")
            || signature.getDeclaringTypeName().contains("com.mz.exmain")
            || signature.getDeclaringTypeName().contains("com.mz.frontoauth")
            || signature.getName().contains("list")
        ) {// 这个类的方法不需要记录日志
          return;
        }

        AppLog controllerLog = new AppLog();
        //系统生成时间  String类型 方便mongodb排序
        controllerLog.setSystemTime(DateUtil.dateToString(new Date()));
        //ID
        controllerLog.setId(UUIDUtil.getUUID());
        //请求地址
        controllerLog.setRequestUrl(requestURL);
        //操作人账号
        controllerLog.setUserName(userName);
        //IP
        controllerLog.setIp(ip);
        //类名
        controllerLog.setClassName(signature.getDeclaringTypeName());
        //方法名
        controllerLog.setMethodName(signature.getName());
        //方法全名
        controllerLog.setMethodFullName(signature.toLongString());
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        MethodName methodNameAnnotation = method.getAnnotation(MethodName.class);
        if (methodNameAnnotation != null) {
          //中文方法名
          controllerLog.setMethodCnName(methodNameAnnotation.name());
        } else {
          controllerLog.setMethodCnName("方法上并没有添加@MethodName(name = '中文方法名')的注解");
        }

        List<Object> argsList = new ArrayList<Object>();
        JsonPropertyFilter filter = new JsonPropertyFilter();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
          Object arg = joinPoint.getArgs()[i];
          if (null != arg) {
            // String argClassName = arg.getClass().getSimpleName();
            if (arg instanceof HttpServletResponse) {
              argsList.add("HttpServletResponse");
            } else if (arg instanceof HttpServletRequest) {
              argsList.add("HttpServletRequest");
            } else if (arg instanceof HttpSession) {
              argsList.add("HttpSession");
            } else {
              argsList.add(arg);
            }
          }
        }
        //请求参数
        controllerLog.setArgsContent(
            JSON.toJSONString(argsList, filter, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect));
        //返回值
        //	controllerLog.setReturnValue(JSON.toJSONString(returnValue, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
        //appName
        controllerLog.setAppName(PropertiesUtils.APP.getProperty("app.name"));

        //日志保存到mongo当中
        String isShowLog = PropertiesUtils.APP.getProperty("app.isShowLog");

        if (isShowLog != null && isShowLog.contains("yes")) {
          AppLogService logService = (AppLogService) ContextUtil.getBean("appLogService");
          logService.save(controllerLog);
//					MongoUtil<AppLog, Long> mongoUtil = new MongoUtil<AppLog, Long>(AppLog.class);
//					mongoUtil.save(controllerLog);
        }

      }
    };
    ThreadPool.exe(r);
  }

}
