package com.mz.web.remote;

import com.mz.exchange.account.model.ExApiApply;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.util.QueryFilter;
import com.mz.util.serialize.ObjectUtil;
import com.mz.exchange.account.service.ExApiApplyService;
import com.mz.exchange.transaction.service.ExDmTransactionService;
import com.mz.manage.remote.RemoteApiService;
import com.mz.manage.remote.model.ApiExApiApply;
import com.mz.manage.remote.model.Entrust;
import com.mz.trade.entrust.service.ExEntrustService;
import javax.annotation.Resource;


public class RemoteApiServiceImpl implements RemoteApiService {

  @Resource
  private ExApiApplyService exApiApplyService;


  @Resource
  private ExDmTransactionService exDmTransactionService;

  @Resource
  private ExEntrustService exEntrustService;

  @Override
  public ApiExApiApply getExApiApply(String accesskey, String ip) {
    QueryFilter qf = new QueryFilter(ExApiApply.class);
    qf.addFilter("customerId=", accesskey);
    qf.addFilter("ipAddress=", ip);
    ExApiApply exApiApply = exApiApplyService.get(qf);
    ApiExApiApply bean2bean = ObjectUtil.bean2bean(exApiApply, ApiExApiApply.class);
    return bean2bean;
  }

  @Override
  public ApiExApiApply getExApiApply(String accesskey) {
    QueryFilter qf = new QueryFilter(ExApiApply.class);
    qf.addFilter("customerId=", accesskey);
    ExApiApply exApiApply = exApiApplyService.get(qf);
    ApiExApiApply bean2bean = ObjectUtil.bean2bean(exApiApply, ApiExApiApply.class);
    return bean2bean;
  }


  @Override
  public Entrust selectExEntrust(String entrustNum) {
    QueryFilter qf = new QueryFilter(ExEntrust.class);
    qf.addFilter("entrustNum=", entrustNum);
    ExEntrust exEntrust = exEntrustService.get(qf);
    Entrust entrust = ObjectUtil.bean2bean(exEntrust, Entrust.class);
    return entrust;
  }


}
