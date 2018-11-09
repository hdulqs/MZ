package com.mz.web.remote;

import com.mz.web.app.model.AppMessage;
import com.mz.web.app.model.MessageAsCustomer;
import com.mz.web.message.service.AppMessageService;
import com.mz.web.message.service.MessageAsCustomerService;
import com.mz.manage.remote.RemoteOamessageService;
import com.mz.manage.remote.model.Oamessage;
import com.mz.manage.remote.model.base.FrontPage;
import java.util.Map;
import javax.annotation.Resource;

/**
 * 站内信消息
 *
 * @author tzw
 *
 * 2017年7月19日
 */
public class RemoteOamessageServiceImpl implements RemoteOamessageService {

  @Resource
  private MessageAsCustomerService messageAsCustomerService;
  @Resource
  private AppMessageService appMessageService;

  /**
   * 获取当前登录用户所有站内信
   *
   * @return 2017年7月19日 tzw
   */
  @Override
  public FrontPage findOamessage(Map<String, String> params) {

    return messageAsCustomerService.findOamessage(params);
  }


  /**
   * 根据id获取站内信并且设置已读
   *
   * @return 2017年7月21日 tzw
   */
  @Override
  public Oamessage read(Long sid) {
    Oamessage oamessage = new Oamessage();
    MessageAsCustomer message = messageAsCustomerService.get(sid);
    //设置消息为已读
    message.setState(2);
    messageAsCustomerService.update(message);

    //获取消息详情
    if (message.getMessageId() != null) {
      AppMessage appMessage = appMessageService.get(message.getMessageId());
      if (appMessage != null) {
        oamessage.setContent(appMessage.getContent());
        oamessage.setSendDate(appMessage.getSendDate());
        oamessage.setSortTitle(oamessage.getSortTitle());
        oamessage.setTitle(appMessage.getTitle());
      }
    }
    return oamessage;
  }

  /**
   * 根据id获取消息(用户操作时校验是否本人操作)
   *
   * @return 2017年7月21日 tzw
   */
  @Override
  public Oamessage get(Long sid) {
    Oamessage oamessage = new Oamessage();
    MessageAsCustomer message = messageAsCustomerService.get(sid);
    if (message != null) {
      oamessage.setCustomerId(message.getCustomerId());
      oamessage.setCustomerName(message.getCustomerName());
      oamessage.setState(message.getState());
    }
    //获取消息详情
    if (message.getMessageId() != null) {
      AppMessage appMessage = appMessageService.get(message.getMessageId());
      if (appMessage != null) {
        oamessage.setContent(appMessage.getContent());
        oamessage.setSendDate(appMessage.getSendDate());
        oamessage.setSortTitle(oamessage.getSortTitle());
        oamessage.setTitle(appMessage.getTitle());
      }
    }
    return oamessage;
  }
}
