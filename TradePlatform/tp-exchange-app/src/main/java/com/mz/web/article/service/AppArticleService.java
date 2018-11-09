/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年4月29日 下午3:51:00
 */
package com.mz.web.article.service;

import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppArticle;
import com.mz.manage.remote.model.base.FrontPage;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年4月29日 下午3:51:00
 */
public interface AppArticleService extends BaseService<AppArticle, Long> {

  public PageResult findPageSql(QueryFilter filter, Long categoryId, String website);

  public String removeAll(Long[] ids);

  public List<AppArticle> findListById(Long id, Integer size);

  public AppArticle firstnews();

  //测试积分drools 无任何意义
  public AppArticle testAdd(AppArticle appArticle);

  public List<AppArticle> getByCategoryId(HttpServletRequest request);

  /**
   * 文章前台分页方法
   * @param params
   * @return
   */
  public FrontPage findFrontPage(Map<String, String> params);

}
