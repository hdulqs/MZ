/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-07-19 13:40:56
 */
package com.mz.ico.project.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.project.model.AppIcoProject;
import com.mz.ico.project.service.AppIcoProjectService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoProjectService </p>
 * @author: shangxl
 * @Date :          2017-07-19 13:40:56  
 */
@Service("appIcoProjectService")
public class AppIcoProjectServiceImpl extends BaseServiceImpl<AppIcoProject, Long> implements
    AppIcoProjectService {

  @Resource(name = "appIcoProjectDao")
  @Override
  public void setDao(BaseDao<AppIcoProject, Long> dao) {
    super.dao = dao;
  }


}
