/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年9月17日 上午11:52:41
 */
package com.mz.util.sys;

import java.util.Locale;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p> 此类不供业务系统调用</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年9月17日 上午11:52:41
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  /**
   * 实现ApplicationContextAware接口的回调方法，设置上下文环境
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    if (SpringContextUtil.applicationContext == null) {
      this.applicationContext = applicationContext;
    }
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 获取注入对象
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param name
   * @param: @return
   * @return: Object
   * @Date :          2015年9月17日 上午11:56:57
   * @throws:
   */
  public static <T> T getBean(String name) {
    try {
      return (T) applicationContext.getBean(name);
    } catch (NoSuchBeanDefinitionException e) {
    }
    return null;
  }

  public static <T> T getBean(Class<T> tClass) {
    try {
      return applicationContext.getBean(tClass);
    } catch (NoSuchBeanDefinitionException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 国际化
   *
   * @param code key值
   */
  public static String diff(String code) {
    String message = "";
    Locale locale = LocaleContextHolder.getLocale();

    try {
      String[] split = code.split("~");
      if (split.length > 1) {
        message = applicationContext.getMessage(split[0], new Object[]{split[1]}, locale);
        if (message == null || "".equals(message)) {
          return "错误001";
        }
      } else {
        message = applicationContext.getMessage(code, null, locale);
        if (message == null || "".equals(message)) {
          return "错误001";
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "错误002";

    }
    return message;
  }

  /**
   * 国际化
   *
   * @param language 当前语言
   * @param code key值
   */
  public static String diff(String code, String language) {
    String message = "";
    //Locale locale = LocaleContextHolder.getLocale();
    String[] strings = language.split("_");
    Locale locale = null;

    if (strings.length > 1) {
      locale = new Locale(strings[0], strings[1]);
    } else {
      locale = new Locale(strings[0]);
    }

    try {
      if (code == null || code.equals("")) {
        return "";
      }
      String[] split = code.split("~");
      if (split.length > 1) {
        message = applicationContext.getMessage(split[0], new Object[]{split[1]}, locale);
        if (message == null || "".equals(message)) {
          return "错误001";
        }
      } else {
        message = applicationContext.getMessage(code, null, locale);
        if (message == null || "".equals(message)) {
          return "错误001";
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "错误002";

    }
    return message;
  }


}
