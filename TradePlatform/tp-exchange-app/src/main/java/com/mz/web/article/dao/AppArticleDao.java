/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年4月29日 下午3:43:55
 */
package com.mz.web.article.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.web.app.model.AppArticle;
import com.mz.web.app.model.AppArticleVo;
import com.mz.web.app.model.vo.AppArticlePageVo;
import com.mz.manage.remote.model.Article;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年4月29日 下午3:43:55
 */
public interface AppArticleDao extends BaseDao<AppArticle, Long> {

	// 查询分页根据类型的id 查出所有的文章信息(不包括文章内容)
	List<AppArticle> findPageBySql(Long categoryId,String website);

	// 查询上一页
	AppArticle findUpActicle(Long acticleId);

	// 查询下一页
	AppArticle findNextActicle(Long acticleId);

	List<AppArticleVo> findArtricAndCategory();
	
	List<AppArticle> findArticList(String categoryName,int size);
	
	
	List<AppArticle> findArticListById(Long id,Integer size);

	AppArticle firstnews();
	
	public AppArticlePageVo findArticListByPage(@Param(value="id") Long id,@Param(value="stat") Integer stat,@Param(value="end") Integer end);
	
	List<AppArticle> findArticListByIdLimit(Long categoryId,Integer size,String language);
	
	/**
	 * 前台分页方法
	 * @param params
	 * @return
	 */
	List<Article> findFrontPageBySql(Map<String, String> params);
}
