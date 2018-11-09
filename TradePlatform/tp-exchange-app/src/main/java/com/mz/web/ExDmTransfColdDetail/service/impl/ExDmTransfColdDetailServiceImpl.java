/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-11-13 19:17:02
 */
package com.mz.web.ExDmTransfColdDetail.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.web.ExDmTransfColdDetail.model.ExDmTransfColdDetail;
import com.mz.web.ExDmTransfColdDetail.service.ExDmTransfColdDetailService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> ExDmTransfColdDetailService </p>
 * @author: shangxl
 * @Date :          2017-11-13 19:17:02  
 */
@Service("exDmTransfColdDetailService")
public class ExDmTransfColdDetailServiceImpl extends
    BaseServiceImpl<ExDmTransfColdDetail, Long> implements ExDmTransfColdDetailService {

  @Resource(name = "exDmTransfColdDetailDao")
  @Override
  public void setDao(BaseDao<ExDmTransfColdDetail, Long> dao) {
    super.dao = dao;
  }


}
