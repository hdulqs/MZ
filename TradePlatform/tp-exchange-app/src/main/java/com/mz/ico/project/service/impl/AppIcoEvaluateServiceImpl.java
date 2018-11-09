/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: denghf
 * @version: V1.0
 * @Date: 2017-08-16 20:24:46
 */
package com.mz.ico.project.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.ico.project.model.AppIcoEvaluate;
import com.mz.ico.project.service.AppIcoEvaluateService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppIcoEvaluateService </p>
 * @author: denghf
 * @Date :          2017-08-16 20:24:46  
 */
@Service("appIcoEvaluateService")
public class AppIcoEvaluateServiceImpl extends BaseServiceImpl<AppIcoEvaluate, Long> implements
    AppIcoEvaluateService {

  @Resource(name = "appIcoEvaluateDao")
  @Override
  public void setDao(BaseDao<AppIcoEvaluate, Long> dao) {
    super.dao = dao;
  }


}
