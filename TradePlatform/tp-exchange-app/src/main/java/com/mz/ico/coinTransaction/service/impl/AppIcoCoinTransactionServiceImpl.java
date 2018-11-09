/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-08-18 14:08:35
 */
package com.mz.ico.coinTransaction.service.impl;

import com.github.pagehelper.Page;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.coinTransaction.model.AppIcoCoinTransaction;
import com.mz.ico.coinTransaction.dao.AppIcoCoinTransactionDao;
import com.mz.ico.coinTransaction.service.AppIcoCoinTransactionService;
import com.mz.manage.remote.model.base.FrontPage;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoCoinTransactionService </p>
 * @author: shangxl
 * @Date :          2017-08-18 14:08:35  
 */
@Service("appIcoCoinTransactionService")
public class AppIcoCoinTransactionServiceImpl extends
    BaseServiceImpl<AppIcoCoinTransaction, Long> implements AppIcoCoinTransactionService {

  @Resource(name = "appIcoCoinTransactionDao")
  @Override
  public void setDao(BaseDao<AppIcoCoinTransaction, Long> dao) {
    super.dao = dao;
  }

  public FrontPage findIcotransaction(Map<String, String> params) {
    Page page = PageFactory.getPage(params);
    List<AppIcoCoinTransaction> list = ((AppIcoCoinTransactionDao) dao).findIcotransaction(params);

    return new FrontPage(list, page.getTotal(), page.getPages(), page.getPageSize());
  }
}
