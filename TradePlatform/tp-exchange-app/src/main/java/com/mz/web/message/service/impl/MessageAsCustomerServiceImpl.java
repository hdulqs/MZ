/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年5月30日 下午5:41:43
 */
package com.mz.web.message.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.user.model.AppCustomer;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.model.MessageAsCustomer;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.manage.remote.model.Oamessage;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.web.message.dao.MessageAsCustomerDao;
import com.mz.web.message.service.MessageAsCustomerService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 *
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年5月30日 下午5:41:43
 */
@Service("messageAsCustomerService")
public class MessageAsCustomerServiceImpl extends
    BaseServiceImpl<MessageAsCustomer, Long> implements MessageAsCustomerService {

  @Resource(name = "messageAsCustomerDao")
  @Override
  public void setDao(BaseDao<MessageAsCustomer, Long> dao) {
    super.dao = dao;
  }

  // 给当前所有用户添加一条消息
  @Override
  public void sendAll(Long messagId) {

    RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil
        .getBean("remoteAppCustomerService");

    RemoteQueryFilter remoteQueryFilter = new RemoteQueryFilter(AppCustomer.class);

    remoteQueryFilter.addFilter("isDelete!=", 1);
    remoteQueryFilter.setSaasId(ContextUtil.getSaasId());

    List<AppCustomer> list = remoteAppCustomerService.find(remoteQueryFilter);

    for (AppCustomer customer : list) {

      Long id = customer.getId();
      String name = customer.getUserName();

      MessageAsCustomer messageAsCustomer = new MessageAsCustomer();

      messageAsCustomer.setCustomerId(id);
      messageAsCustomer.setCustomerName(name);
      messageAsCustomer.setMessageId(messagId);

      super.save(messageAsCustomer);

    }


  }

  // 给部分人发送消息
  @Override
  public void sendPartial(Long messagId, String[] userNames) {
    RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil
        .getBean("remoteAppCustomerService");
    RemoteQueryFilter filter = null;
    for (int i = 0; i < userNames.length; i++) {
      filter = new RemoteQueryFilter(AppCustomer.class);
      filter.addFilter("userName=", userNames[i]);
      AppCustomer customer = remoteAppCustomerService.getByQueryFilter(filter);
      if (customer != null) {
        String name = customer.getUserName();

        MessageAsCustomer messageAsCustomer = new MessageAsCustomer();
        messageAsCustomer.setCustomerName(name);
        messageAsCustomer.setCustomerId(customer.getId());
        messageAsCustomer.setMessageId(messagId);
        // 给一个人保存一条消息
        super.save(messageAsCustomer);
      }
    }


  }

  @Override
  public void sendPartialName(Long messagId, String receiveUserNames) {

    RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil
        .getBean("remoteAppCustomerService");
    String[] str = receiveUserNames.split(",");
    for (int i = 0; i < str.length; i++) {
      RemoteQueryFilter qf = new RemoteQueryFilter(AppCustomer.class);
      qf.addFilter("userName=", str[i]);
      AppCustomer customer = remoteAppCustomerService.getByQueryFilter(qf);
      if (null != customer) {
        String name = customer.getUserName();
        MessageAsCustomer messageAsCustomer = new MessageAsCustomer();
        messageAsCustomer.setCustomerName(name);
        messageAsCustomer.setCustomerId(customer.getId());
        messageAsCustomer.setMessageId(messagId);
        // 给一个人保存一条消息
        super.save(messageAsCustomer);
      }
    }
  }

  @Override
  public FrontPage findOamessage(Map<String, String> params) {
    Page page = getPage(params);
    //查询方法
    List<Oamessage> list = ((MessageAsCustomerDao) dao).findFrontPageBySql(params);

    return new FrontPage(list, page.getTotal(), page.getPages(), page.getPageSize());
  }


  public static Page getPage(Map<String, String> map) {
    Page page = null;
    Integer offset = 0;
    Integer limit = 10;
    if (StringUtil.isNotEmpty(map.get("offset"))) {
      offset = Integer.valueOf(map.get("offset"));
    }
    if (StringUtils.isNotEmpty(map.get("limit"))) {
      limit = Integer.valueOf(map.get("limit"));
    }
    if (limit == -1) {
      page = PageHelper.startPage(offset / limit + 1, 0);
    } else {
      page = PageHelper.startPage(offset / limit + 1, limit);
    }
    return page;
  }
}