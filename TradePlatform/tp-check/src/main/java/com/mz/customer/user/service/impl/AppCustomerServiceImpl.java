/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月24日 下午4:21:38
 */
package com.mz.customer.user.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.customer.user.model.AppCustomer;
import com.mz.customer.user.dao.AppCustomerDao;
import com.mz.customer.user.service.AppCustomerService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2016年3月24日 下午4:21:38 
 */
@Service("appCustomerService")
public class AppCustomerServiceImpl extends BaseServiceImpl<AppCustomer, Long> implements
    AppCustomerService {

  @Resource(name = "appCustomerDao")
  @Override
  public void setDao(BaseDao<AppCustomer, Long> dao) {
    super.dao = dao;
  }

  @Override
  public List<AppCustomer> getFundChangeCustomers(Map<String, Object> map) {
    return ((AppCustomerDao) dao).getFundChangeCustomers(map);
  }


}
