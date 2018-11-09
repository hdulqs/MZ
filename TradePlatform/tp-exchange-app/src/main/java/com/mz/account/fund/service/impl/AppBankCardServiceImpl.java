/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月31日 下午6:52:11
 */
package com.mz.account.fund.service.impl;

import com.mz.account.fund.model.AppBankCard;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.account.fund.service.AppBankCardService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2016年3月31日 下午6:52:11 
 */
@Service("appBankCardService")
public class AppBankCardServiceImpl extends BaseServiceImpl<AppBankCard, Long> implements
    AppBankCardService {

  @Resource(name = "appBankCardDao")
  @Override
  public void setDao(BaseDao<AppBankCard, Long> dao) {
    super.dao = dao;
  }

}
