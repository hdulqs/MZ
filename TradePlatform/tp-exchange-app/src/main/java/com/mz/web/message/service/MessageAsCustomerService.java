/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年5月30日 下午5:30:12
 */
package com.mz.web.message.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.app.model.MessageAsCustomer;
import com.mz.manage.remote.model.base.FrontPage;
import java.util.Map;

/**
 * <p> TODO</p>
 *
 * @author: Wu Shuiming
 * @Date :          2016年5月30日 下午5:30:12
 */
public interface MessageAsCustomerService extends BaseService<MessageAsCustomer, Long> {

  public void sendAll(Long messagId);

  public void sendPartial(Long messagId, String[] userNames);

  public void sendPartialName(Long messagId, String receiveUserNames);

  /**
   * 获取当前登录用户所有站内信
   *
   * @return 2017年7月20日 tzw
   */
  public FrontPage findOamessage(Map<String, String> params);


}


