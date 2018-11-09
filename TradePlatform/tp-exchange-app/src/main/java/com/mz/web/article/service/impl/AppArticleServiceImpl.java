/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年4月29日 下午3:53:31
 */
package com.mz.web.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppArticle;
import com.mz.manage.remote.model.Article;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.web.article.dao.AppArticleDao;
import com.mz.web.article.service.AppArticleService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年4月29日 下午3:53:31
 */
@Service("appArticleService")
public class AppArticleServiceImpl extends BaseServiceImpl<AppArticle, Long>
    implements AppArticleService {

	/*@KSession("article_ksession")
	private KieSession kSession;*/

  @Resource(name = "appArticleDao")
  @Override
  public void setDao(BaseDao<AppArticle, Long> dao) {
    super.dao = dao;
  }

  @Override
  public PageResult findPageSql(QueryFilter filter, Long categoryId, String website) {

    // ----------------------分页查询头部外壳------------------------------
    // 创建PageResult对象
    PageResult pageResult = new PageResult();
    Page<AppArticle> page = null;
    if (filter.getPageSize().compareTo(Integer.valueOf(-1)) == 0) {
      // pageSize = -1 时
      // pageHelper传pageSize参数传0查询全部
      page = PageHelper.startPage(filter.getPage(), 0);
    } else {
      page = PageHelper.startPage(filter.getPage(), filter.getPageSize());
    }
    // ----------------------分页查询头部外壳------------------------------

    ((AppArticleDao) dao).findPageBySql(categoryId, website);

    // ----------------------查询结束------------------------------

    // ----------------------分页查询底部外壳------------------------------
    // 设置分页数据
    pageResult.setRows(page.getResult());
    // 设置总记录数
    pageResult.setRecordsTotal(page.getTotal());
    pageResult.setDraw(filter.getDraw());
    pageResult.setPage(filter.getPage());
    pageResult.setPageSize(filter.getPageSize());
    // ----------------------分页查询底部外壳------------------------------

    return pageResult;
  }

  @Override
  public String removeAll(Long[] ids) {
    if (ids.length > 0) {
      for (int i = 0; i < ids.length; i++) {
        AppArticle article = super.get(ids[i]);
        delete(ids[i]);
      }
    }
    return "OK";
  }

  @Override
  public List<AppArticle> findListById(Long id, Integer size) {

    List<AppArticle> list = ((AppArticleDao) dao).findArticListById(id, size);

    return list;
  }

  @Override
  public AppArticle firstnews() {
    return ((AppArticleDao) dao).firstnews();
  }

  /* (non-Javadoc)
   * @see AppArticleService#testAdd(com.mz.web.app.model.AppArticle)
   */
  @Override
  public AppArticle testAdd(AppArticle appArticle) {

    //kSession.insert(appArticle);
    System.out.println("article对象输入");
    //kSession.fireAllRules();

    System.out.println("当前点击数==" + appArticle.getHits());

    return null;
  }

  /**
   * 根据文章类型id查询文章内容
   */
  @Override
  public List<AppArticle> getByCategoryId(HttpServletRequest request) {
    String categoryId = request.getParameter("categoryId");
    QueryFilter filter = new QueryFilter(AppArticle.class, request);
    filter.addFilter("categoryId=", categoryId);
    List<AppArticle> list = super.find(filter);
    return list;
  }

  @Override
  public FrontPage findFrontPage(Map<String, String> params) {

    Page page = PageFactory.getPage(params);
    //查询方法
    List<Article> list = ((AppArticleDao) dao).findFrontPageBySql(params);
    return new FrontPage(list, page.getTotal(), page.getPages(), page.getPageSize());


  }

}
