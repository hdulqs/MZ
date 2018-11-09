/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午1:45:20
 */
package com.mz.change.remote.exEntrust;

import com.mz.exchange.product.model.ProductCommon;
import com.mz.trade.entrust.model.ExEntrust;

/**
 *
 * <p>
 * TODO
 * </p>
 *
 * @author: Gao MIMI
 * @Date : 2016年3月24日 下午1:47:26
 */
public interface RemoteExExEntrustService {

  /**
   *
   * <p> TODO</p>
   * @author: Gao Mimi
   * @param:    @param coinCode
   * @param:    @return
   * @return: ProductCommon
   * @Date :          2016年8月25日 下午4:23:17
   * @throws:
   */
  public ProductCommon getProductCommon(String coinCode);

  /**
   *
   * <p> 撤销</p>
   * @author: Gao Mimi
   * @param:    @param exEntrust
   * @param:    @return
   * @return: String[]
   * @Date :          2016年8月25日 下午5:05:07
   * @throws:
   */
  public String[] canceldeductMoney(ExEntrust exEntrust);
}
