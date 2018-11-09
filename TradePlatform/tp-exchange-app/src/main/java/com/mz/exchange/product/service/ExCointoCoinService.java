/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: gaomimi
 * @version: V1.0
 * @Date: 2017-07-06 19:40:34
 */
package com.mz.exchange.product.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ExCointoCoin;
import java.util.List;

/**
 * <p> ExCointoCoinService </p>
 * @author: gaomimi
 * @Date :          2017-07-06 19:40:34  
 */
public interface ExCointoCoinService extends BaseService<ExCointoCoin, Long> {

  public List<ExCointoCoin> getBylist(String toProductcoinCode, String fromProductcoinCode,
      Integer issueState);

  public List<ExCointoCoin> getBylist(Integer state);

  public void initRedisCode();
}
