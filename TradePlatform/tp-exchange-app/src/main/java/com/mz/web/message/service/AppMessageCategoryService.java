/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年5月30日 下午5:28:56
 */
package com.mz.web.message.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.app.model.AppMessageCategory;

/**
 *
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年5月30日 下午5:28:56
 */
public interface AppMessageCategoryService extends BaseService<AppMessageCategory, Long> {

  public JsonResult removeCategory(Long[] ids);


}


