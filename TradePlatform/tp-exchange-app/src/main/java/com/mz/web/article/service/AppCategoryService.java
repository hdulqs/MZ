/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年4月29日 下午3:48:47
 */
package com.mz.web.article.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.app.model.AppCategory;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年4月29日 下午3:48:47
 */
public interface AppCategoryService extends BaseService<AppCategory, Long> {

  /**
   * 查询分类树
   * @param request
   * @return
   */
  List<AppCategory> selectlist(HttpServletRequest request);

}
