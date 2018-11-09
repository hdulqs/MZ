/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2015年9月16日 上午11:04:39
 */
package com.mz.front.listener;

import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;

;

/**
 * @author CHINA_LSL
 */
public class StartupListener extends ContextLoaderListener {

  public void contextInitialized(ServletContextEvent event) {
    super.contextInitialized(event);

  }

  @Override
  public void contextDestroyed(ServletContextEvent arg0) {
    // TODO Auto-generated method stub
    RedisService redisService = SpringContextUtil.getBean("redisService");
    Set<String> keys = redisService.keys("static_html*");
    if (keys != null && keys.size() > 0) {
      Iterator<String> it = keys.iterator();
      while (it.hasNext()) {
        redisService.delete(it.next());
      }
    }
  }
}
