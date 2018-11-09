/**
 * Copyright:   风云科技
 *
 * @author: zongwei
 * @Date :          20180502
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.businessman.model.OtcCoin;
import com.mz.customer.businessman.service.OtcCoinService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> otcCoinService </p>
 * @author: zongwei
 * @Date :          20180502
 */
@Service("otcCoinService")
public class OtcCoinServiceImpl extends BaseServiceImpl<OtcCoin, Long> implements OtcCoinService {

  @Resource(name = "otcCoinDao")
  @Override
  public void setDao(BaseDao<OtcCoin, Long> dao) {
    super.dao = dao;
  }

}
