/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年10月27日 下午6:17:27
 */
package com.mz.core.mvc.service.app.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.app.CoreAppDictionaryService;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.web.app.model.AppDictionary;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 *
 * @author: Gao Mimi
 * @Date :          2015年10月27日 下午6:17:27
 */
@Service
public class CoreAppDictionaryServiceImpl extends BaseServiceImpl<AppDictionary, Long> implements
    CoreAppDictionaryService {

  @Resource(name = "coreAppDictionaryDao")
  @Override
  public void setDao(BaseDao<AppDictionary, Long> dao) {
    super.dao = dao;
  }


}
