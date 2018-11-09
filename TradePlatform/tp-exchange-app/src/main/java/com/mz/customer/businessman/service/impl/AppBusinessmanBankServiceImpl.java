/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-18 16:55:04
 */
package com.mz.customer.businessman.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.businessman.model.AppBusinessmanBank;
import com.mz.customer.businessman.service.AppBusinessmanBankService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppBusinessmanBankService </p>
 * @author: liushilei
 * @Date :          2017-12-18 16:55:04  
 */
@Service("appBusinessmanBankService")
public class AppBusinessmanBankServiceImpl extends
    BaseServiceImpl<AppBusinessmanBank, Long> implements AppBusinessmanBankService {

  @Resource(name = "appBusinessmanBankDao")
  @Override
  public void setDao(BaseDao<AppBusinessmanBank, Long> dao) {
    super.dao = dao;
  }


}
