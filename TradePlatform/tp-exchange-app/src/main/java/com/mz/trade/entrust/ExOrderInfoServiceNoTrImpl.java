/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午2:04:29
 */
package com.mz.trade.entrust;

import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.util.QueryFilter;
import com.mz.util.date.DateUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.remote.RemoteAppTradeFactorageService;
import java.util.List;
import javax.annotation.Resource;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Gao Mimi
 * @Date : 2016年4月12日 下午4:45:50
 */

public class ExOrderInfoServiceNoTrImpl implements ExOrderInfoServiceNoTr {

  @Resource
  public ExOrderInfoService exOrderInfoService;

  @Override
  public void timingCulAtferMoney() {
    QueryFilter qf = new QueryFilter(ExOrderInfo.class);
    qf.addFilter("isCulAtferMoney=", 0);
    List<ExOrderInfo> list = exOrderInfoService.find(qf);
    for (ExOrderInfo l : list) {
      System.out.println("接收到交易佣金订单号为   " + l.getOrderNum() + " ----- " + DateUtil.getNewDate());
      try {
        RemoteAppTradeFactorageService remoteAppTradeFactorageService = (RemoteAppTradeFactorageService) ContextUtil
            .getBean("remoteAppTradeFactorageService");
        // 保存交易单佣金 买卖方
        Boolean boolean1 = remoteAppTradeFactorageService.saveTrade(l.getOrderNum());
        if (boolean1) {
          System.out.println("交易佣金保存成功  " + l.getOrderNum() + " ---- " + DateUtil.getNewDate());
          l.setIsCulAtferMoney(1);
          exOrderInfoService.update(l);
        } else {
          l.setIsCulAtferMoney(1);
          exOrderInfoService.update(l);
          System.out.println("交易佣金保存失败  " + l.getOrderNum() + " ---- " + DateUtil.getNewDate());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
