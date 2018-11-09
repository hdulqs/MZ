/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date :          2015年10月21日 下午6:21:58
 */
package com.mz.exapi.aop;

import com.mz.core.annotation.RequestLimit;
import com.mz.core.exception.RequestLimitException;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.IpUtil;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

/**
 *
 * <p>
 * TODO
 * </p>
 *
 * @author: Gao Mimi
 * @Date : 2016年5月10日 下午7:06:33
 */
@Aspect
@Component
public class RequestLimitAop {

  private static Log logger = LogFactory.getLog(RequestLimitAop.class);
  @Autowired
  private RedisService redisService;

  @Before("within(@org.springframework.stereotype.Controller *) && @annotation(limit)")
  public void requestLimit(final JoinPoint joinPoint, RequestLimit limit)
      throws RequestLimitException {

    try {
      Object[] args = joinPoint.getArgs();
      HttpServletRequest request = null;
      for (int i = 0; i < args.length; i++) {
        if (args[i] instanceof HttpServletRequest) {
          request = (HttpServletRequest) args[i];
          break;
        }
      }
      if (request == null) {
        throw new RequestLimitException("方法中缺失HttpServletRequest参数");
      }
      String ip = IpUtil.getIp(request);
      String url = request.getRequestURL().toString();
      String key = "req_limit_".concat(url).concat(ip);
      String value = redisService.get(key);
      if (StringUtil.isEmpty(value)) {
        redisService.save(key, "1", limit.time());
      } else {
        Long count = Long.valueOf(value);
        if (count == limit.count()) {
          System.out.println("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
          logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
          throw new RequestLimitException();
        }
        count++;
        String countstr = count.toString();
        redisService.save(key, countstr, limit.time());

      }

    } catch (RequestLimitException e) {
      throw e;
    } catch (Exception e) {
      logger.error("发生异常: ", e);
    }
  }
}
