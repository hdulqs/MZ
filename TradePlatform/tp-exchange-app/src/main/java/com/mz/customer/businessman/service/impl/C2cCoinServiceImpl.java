/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.businessman.model.C2cCoin;
import com.mz.customer.businessman.service.C2cCoinService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> c2cCoinService </p>
 * @author: liushilei
 * @Date :          2017-12-07 14:06:38
 */
@Service("c2cCoinService")
public class C2cCoinServiceImpl extends BaseServiceImpl<C2cCoin, Long> implements C2cCoinService {

  @Resource(name = "c2cCoinDao")
  @Override
  public void setDao(BaseDao<C2cCoin, Long> dao) {
    super.dao = dao;
  }

}
