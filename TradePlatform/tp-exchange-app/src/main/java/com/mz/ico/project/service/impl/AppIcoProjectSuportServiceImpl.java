/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-07-26 18:09:22
 */
package com.mz.ico.project.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.project.model.AppIcoProjectSuport;
import com.mz.ico.project.service.AppIcoProjectSuportService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoProjectSuportService </p>
 * @author: shangxl
 * @Date :          2017-07-26 18:09:22  
 */
@Service("appIcoProjectSuportService")
public class AppIcoProjectSuportServiceImpl extends
    BaseServiceImpl<AppIcoProjectSuport, Long> implements AppIcoProjectSuportService {

  @Resource(name = "appIcoProjectSuportDao")
  @Override
  public void setDao(BaseDao<AppIcoProjectSuport, Long> dao) {
    super.dao = dao;
  }


}
