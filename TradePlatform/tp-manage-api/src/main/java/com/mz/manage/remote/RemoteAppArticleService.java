package com.mz.manage.remote;

import com.mz.manage.remote.model.Article;
import com.mz.manage.remote.model.ArticleCategory;
import com.mz.manage.remote.model.FriendLink;
import java.util.List;
import java.util.Map;

import com.mz.manage.remote.model.base.FrontPage;

public interface RemoteAppArticleService {

	public List<Article> findArticListByIdLimit(Long categoryId,Integer size,String language);
	
	/**
	 * 查询所有的友情链接
	 * @return
	 */
	public List<FriendLink> findAllFriendLink();
	
	/**
	 * 分页查询文章
	 * @param params
	 * @return
	 */
	public FrontPage findAritcByType(Map<String, String> params);
	
	/**
	 * 查询全部分类
	 * @return
	 */
	public List<ArticleCategory> findArticCategory(String website,String parentName);
	
	/**
	 * 获取文章详情
	 * 返回三个对象  一个上一条，要查的一条，一下条
	 * @param id
	 * @return
	 */
	public  Map<String,Article>  getArtic(String id);
	
	/**
	 * 获取帮助中心文章详情
	 * @return
	 */
	public  Article  getHelpArtic(String id);
	
	/**
	 * 查询帮助中心树型结构 ,包含文章
	 * @return
	 */
	public List<ArticleCategory> findHelpArticCategory(String website,String parentName);
	
	/**
	 * 查询关于我们树型结构 ,包含文章
	 * @return
	 */
	public List<ArticleCategory> findAboutsArticCategory(String website,String parentName);
}
