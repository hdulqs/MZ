/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menw
 * @version: V1.0
 * @Date: 2017-07-13 18:27:13
 */
package com.mz.web.codemirror.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.web.codemirror.model.AppCodeMirror;
import com.mz.web.codemirror.service.AppCodeMirrorService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> AppCodeMirrorService </p>
 * @author: menw
 * @Date :          2017-07-13 18:27:13  
 */
@Service("appCodeMirrorService")
public class AppCodeMirrorServiceImpl extends BaseServiceImpl<AppCodeMirror, Long> implements
    AppCodeMirrorService {

  @Resource(name = "appCodeMirrorDao")
  @Override
  public void setDao(BaseDao<AppCodeMirror, Long> dao) {
    super.dao = dao;
  }


}
