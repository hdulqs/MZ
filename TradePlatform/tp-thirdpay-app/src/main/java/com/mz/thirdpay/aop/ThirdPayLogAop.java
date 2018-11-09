/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月11日 下午2:37:53
 */
package com.mz.thirdpay.aop;

import com.mz.thirdpay.AppLogThirdPay;
import com.mz.thirdpay.biz.service.AppLogThirdPayService;
import com.mz.utils.CommonRequest;
import com.mz.core.thread.ThreadPool;
import javax.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Zhang Xiaofang
 * @Date : 2016年7月11日 下午2:37:53
 */
@Aspect
@Component
public class ThirdPayLogAop {

  @Resource
  private AppLogThirdPayService appLogThirdPayService;


  // controller层切点
  @Pointcut("@annotation(com.mz.core.annotation.ThirdPayControllerLog)")
  public void controllerAspect() {

  }


  /**
   * 前置通知 用于拦截Controller层记录用户的操作
   *
   * @param joinPoint 切点
   */
  @Before("controllerAspect()")
  public void doBefore(final JoinPoint joinPoint) {
    Runnable r = new Runnable() {
      @Override
      public void run() {
        CommonRequest commonRequest = null;
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
          for (int i = 0; i < joinPoint.getArgs().length; i++) {

            if (joinPoint.getArgs()[i] instanceof CommonRequest) {
              commonRequest = (CommonRequest) joinPoint.getArgs()[i];
              try {
                // ==========数据库日志=========
                AppLogThirdPay log = new AppLogThirdPay();
                log.setMoney(null == commonRequest.getAmount() ? "" : commonRequest.getAmount());
                log.setRequestNum(
                    null == commonRequest.getRequestNo() ? "0" : commonRequest.getRequestNo());
                log.setUserId(Long.valueOf(
                    null == commonRequest.getRequestUser() ? "0" : commonRequest.getRequestUser()));
                log.setThirdPayConfig(null == commonRequest.getRequestThirdPay() ? ""
                    : commonRequest.getRequestThirdPay());

                // 保存数据库
                appLogThirdPayService.save(log);

              } catch (Exception ex) {
                ex.printStackTrace();
              }
              break;
            }
          }
        }
      }
    };
    ThreadPool.exe(r);
  }

  @AfterReturning(value = "@annotation(com.mz.core.annotation.PayAfter)", argNames = "returnValue", returning = "returnValue")
  public void after(final JoinPoint joinPoint, final Object returnValue) {
    Runnable r = new Runnable() {
      @Override
      public void run() {

        if (null != returnValue) {
          if (returnValue instanceof CommonRequest) {
            CommonRequest commonRequest = (CommonRequest) returnValue;

            AppLogThirdPay appLogThirdPay = new AppLogThirdPay();
            appLogThirdPay
                .setMoney(null == commonRequest.getAmount() ? "" : commonRequest.getAmount());
            appLogThirdPay.setUserId(Long.valueOf(
                null == commonRequest.getRequestUser() ? "0" : commonRequest.getRequestUser()));
            appLogThirdPay.setThirdPayConfig(null == commonRequest.getRequestThirdPay() ? ""
                : commonRequest.getRequestThirdPay());
            appLogThirdPay.setRequestNum(
                null == commonRequest.getRequestNo() ? "0" : commonRequest.getRequestNo());
            appLogThirdPay.setResponseMsg(
                null == commonRequest.getResponseMsg() ? "" : commonRequest.getResponseMsg());
            appLogThirdPay.setRemark1(
                null == commonRequest.getResponseObj() ? "" : commonRequest.getResponseObj());
            appLogThirdPayService.save(appLogThirdPay);
          }
          if (returnValue instanceof CommonRequest) {
            CommonRequest commonRequest = (CommonRequest) returnValue;

            AppLogThirdPay appLogThirdPay = new AppLogThirdPay();
            appLogThirdPay
                .setMoney(null == commonRequest.getAmount() ? "" : commonRequest.getAmount());
            appLogThirdPay.setUserId(Long.valueOf(
                null == commonRequest.getRequestUser() ? "0" : commonRequest.getRequestUser()));
            appLogThirdPay.setThirdPayConfig(null == commonRequest.getRequestThirdPay() ? ""
                : commonRequest.getRequestThirdPay());
            appLogThirdPay.setRequestNum(
                null == commonRequest.getRequestNo() ? "0" : commonRequest.getRequestNo());
            appLogThirdPay.setResponseCode(
                null == commonRequest.getResponseCode() ? "" : commonRequest.getResponseCode());
            appLogThirdPay.setResponseMsg(
                null == commonRequest.getResponseMsg() ? "" : commonRequest.getResponseMsg());
            appLogThirdPay.setRemark1(
                null == commonRequest.getResponseObj() ? "" : commonRequest.getResponseObj());
            appLogThirdPayService.save(appLogThirdPay);
          }

        }

      }
    };

    com.mz.core.thread.ThreadPool.exe(r);
  }


}
