/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: GaoMimi
 * @version: V1.0
 * @Date: 2016-10-11 14:37:42
 */
package com.mz.account.fund.service.impl;

import com.mz.account.fund.model.AppAccountSureold;
import com.mz.account.fund.service.AppAccountSureoldService;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppAccountSureoldService </p>
 * @author: GaoMimi
 * @Date :          2016-10-11 14:37:42  
 */
@Service("appAccountSureoldService")
public class AppAccountSureoldServiceImpl extends
    BaseServiceImpl<AppAccountSureold, Long> implements AppAccountSureoldService {

  @Resource(name = "appAccountSureoldDao")
  @Override
  public void setDao(BaseDao<AppAccountSureold, Long> dao) {
    super.dao = dao;
  }


}
