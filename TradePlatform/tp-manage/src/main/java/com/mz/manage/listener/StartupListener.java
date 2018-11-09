/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2015年9月16日 上午11:04:39
 */
package com.mz.manage.listener;

import com.mz.core.listener.StartLoad;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class StartupListener implements CommandLineRunner {

  @Override
  public void run(String... strings) {
    LogFactory.info("----------------------------------------------------------------------------");
    LogFactory.info("---------------------------------加载应用app---------------------------------");
    LogFactory.info("------------------" + PropertiesUtils.APP.getProperty("app.loadApp") + "----");
    //加载每个应用的启动方法
    StartLoad.reflectLoad(PropertiesUtils.APP.getProperty("app.loadApp"));
  }
}
