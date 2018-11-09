/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年5月30日 下午5:30:12
 */
package com.mz.web.message.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppMessage;

/**
 *
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年5月30日 下午5:30:12
 */
public interface AppMessageService extends BaseService<AppMessage, Long> {

  public PageResult selectMessageListVoByList(QueryFilter filter, int state);

  /**
   *
   * 修改并保存消息
   *
   * 如果选择的发送人不为空 将默认是添加用户
   *
   * @author: Wu Shuiming
   * @version: V1.0
   * @date: 2016年8月17日 下午3:43:27
   */
  public JsonResult updateMessage(AppMessage appMessage, String[] userName);

  /**
   * 保存消息方法
   *
   * @author: Wu Shuiming
   * @version: V1.0
   * @date: 2016年8月17日 下午3:48:55
   */
  public JsonResult saveMessage(AppMessage appMessage, String[] userNames);

  public JsonResult saveMessageName(AppMessage appMessage, String receiveUserNames);

  /**
   * 删除多个消息
   */
  public JsonResult removeMessage(Long[] ids);

  /**
   *
   * 保存一条消息
   *
   * @author: Wu Shuiming
   * @version: V1.0
   * @date: 2016年8月17日 下午4:45:32
   */
  public JsonResult sendMessage(Long[] ids);

  public PageResult selectMessageVoByPage(QueryFilter filter);


}


