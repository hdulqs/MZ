/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午2:04:29
 */
package com.mz.exchange.remote.exEntrust;

import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.product.model.ProductCommon;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.change.remote.exEntrust.RemoteExExEntrustService;
import com.mz.exchange.entrust.service.ExExEntrustService;
import com.mz.trade.entrust.service.ExEntrustPlanService;
import com.mz.trade.entrust.service.ExEntrustService;
import com.mz.trade.entrust.service.ExOrderInfoService;
import com.mz.trade.entrust.service.ExOrderService;
import javax.annotation.Resource;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:04:29
 */

public class RemoteExExEntrustServiceImpl implements RemoteExExEntrustService {

  @Resource
  private ExEntrustService entrustService;
  @Resource
  private ExOrderInfoService exOrderInfoService;
  @Resource
  private ExOrderService exOrderService;
  @Resource
  private ExDigitalmoneyAccountService exDigitalmoneyAccountService;
  @Resource
  private ExEntrustPlanService exEntrustPlanService;
  @Resource
  private ExExEntrustService exExEntrustService;

  @Override
  public ProductCommon getProductCommon(String coinCode) {
    // TODO Auto-generated method stub
    return new ProductCommon();
  }

  @Override
  public String[] canceldeductMoney(ExEntrust exEntrust) {
    // TODO Auto-generated method stub
    return null;
  }
}
