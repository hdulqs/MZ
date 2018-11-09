/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年10月27日  17:57:57
 */
package com.mz.core.mvc.controller.app;

import com.mz.core.mvc.service.app.CoreAppDictionaryService;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.app.model.AppDictionary;
import com.mz.core.mvc.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :   2015年10月27日  17:57:57     
 */

@Controller
@RequestMapping("/app/coreappdictionary")
public class CoreAppDictionaryController extends BaseController<AppDictionary, Long> {

  /*@Resource(name = "coreAppDictionaryService")*/
  @Override
  public void setService(BaseService<AppDictionary, Long> service) {
    super.service = service;
  }

  @Autowired
  CoreAppDictionaryService coreAppDictionaryService;

  /**
   *AppDictionaryController自己的initBinder
   *
   * @param binder
   */
  @InitBinder
  public void initBinderDemoUser(ServletRequestDataBinder binder) {

  }


}
