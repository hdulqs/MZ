/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年12月28日 下午5:05:32
 */
package com.mz.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

/**
 * 重写退出方法
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年12月28日 下午5:05:32
 */
public class MyLogoutFilter extends LogoutFilter {

  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    Subject subject = getSubject(request, response);
    String redirectUrl = request.getScheme() + "://" + request.getServerName() + "/oauth/index";
    String backUrl = request.getParameter("backUrl");
    try {
      subject.logout();
      if (StringUtils.isEmpty(backUrl)) {
        issueRedirect(request, response, redirectUrl);
      } else {
        issueRedirect(request, response, backUrl);
      }
    } catch (SessionException ise) {
      ise.printStackTrace();
    }
    return false;
  }

}
