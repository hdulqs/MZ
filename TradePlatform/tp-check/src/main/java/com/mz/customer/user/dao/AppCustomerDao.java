/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月24日 下午3:16:42
 */
package com.mz.customer.user.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.customer.user.model.AppCustomer;
import java.util.List;
import java.util.Map;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2016年3月24日 下午3:16:42 
 */
public interface AppCustomerDao extends BaseDao<AppCustomer, Long> {

  public List<AppCustomer> getFundChangeCustomers(Map<String, Object> map);

}
