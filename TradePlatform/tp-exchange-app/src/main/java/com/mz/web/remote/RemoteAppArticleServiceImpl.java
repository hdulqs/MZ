package com.mz.web.remote;

import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppArticle;
import com.mz.web.app.model.AppCategory;
import com.mz.web.app.model.AppFriendLink;
import com.mz.web.article.dao.AppArticleDao;
import com.mz.web.article.service.AppArticleService;
import com.mz.web.article.service.AppCategoryService;
import com.mz.web.article.service.AppFriendLinkService;
import com.mz.manage.remote.RemoteAppArticleService;
import com.mz.manage.remote.model.Article;
import com.mz.manage.remote.model.ArticleCategory;
import com.mz.manage.remote.model.FriendLink;
import com.mz.manage.remote.model.base.FrontPage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * @author denghf
 * @date 2017年7月8日16:45:22
 */
public class RemoteAppArticleServiceImpl implements RemoteAppArticleService{

	@Resource(name = "appArticleDao")
	private AppArticleDao appArticleDao;
	
	@Resource
	private AppArticleService appArticleService;
	
	@Resource
	private AppCategoryService appCategoryService;
	
	
	// 查询指定类别下的所有的文章
	@Override
	public List<Article> findArticListByIdLimit(Long categoryId,Integer size,String language){
		List<AppArticle> list_app = appArticleDao.findArticListByIdLimit(categoryId, size,language);
		List<Article> list_ = new ArrayList<Article>();
		for(AppArticle app:list_app){
			Article article = new Article();
			article.setId(app.getId());
			article.setTitle(app.getTitle());
			article.setWriter(app.getWriter());
			article.setHits(app.getHits());
			article.setModified(app.getModified());
			article.setContent(app.getContent());
			article.setCreated(app.getCreated());
			list_.add(article);
		}
		return list_;
	}
	
	@Resource(name = "appFriendLinkService")
	private AppFriendLinkService appFriendLinkService;

	@Override
	public List<FriendLink> findAllFriendLink() {
		QueryFilter qf = new QueryFilter(AppFriendLink.class);
		qf.addFilter("status=", 0);
		List<AppFriendLink> list_app = appFriendLinkService.find(qf);
		List<FriendLink> list_ = new ArrayList<FriendLink>();
		for(AppFriendLink app:list_app){
			FriendLink friendLink = new FriendLink();
			friendLink.setId(app.getId());
			friendLink.setIsPicture(app.getIsPicture());
			friendLink.setLinkUrl(app.getLinkUrl());
			friendLink.setName(app.getName());
			friendLink.setPicturePath(app.getPicturePath());
			friendLink.setStatus(app.getStatus());
			friendLink.setWebsite(app.getWebsite());
			list_.add(friendLink);
		}
		
		
		return list_;
	}

	@Override
	public FrontPage findAritcByType(Map<String, String> params) {
		return appArticleService.findFrontPage(params);
	}

	@Override
	public List<ArticleCategory> findArticCategory(String website,String parentName) {
		QueryFilter queryFilter = new QueryFilter(AppCategory.class);
		queryFilter.addFilter("website=", website);
		queryFilter.setOrderby("sort");
		queryFilter.addFilter("preateName=", "no");
		//queryFilter.addFilter("name!=", parentName);
		queryFilter.addFilter("name!=", "帮助中心");
		queryFilter.addFilter("name!=", "关于我们");
		queryFilter.addFilter("name!=", "Help");
		queryFilter.addFilter("name!=", "About");
		queryFilter.setOrderby(" id ");
		List<AppCategory> find = appCategoryService.find(queryFilter);
		ArrayList<ArticleCategory> list =new ArrayList<ArticleCategory>();
		if(find!=null&&find.size()>0){
			for(AppCategory category : find){
				ArticleCategory articleCategory = new ArticleCategory();
				articleCategory.setId(category.getId());
				articleCategory.setName(category.getName());
				articleCategory.setSort(articleCategory.getSort());
				list.add(articleCategory);
			}
		}
		return list;
		
		
	}

	@Override
	public Map<String,Article> getArtic(String id) {
		
		Map<String,Article>  map= new HashMap<String,Article>();
		
		AppArticle appArticle = appArticleService.get(Long.valueOf(id));
		if(appArticle==null){
			return map;
		}
		//查询上一条
		QueryFilter lastQuery = new QueryFilter(AppArticle.class);
		lastQuery.addFilter("id<", id);
		lastQuery.addFilter("categoryId=", appArticle.getCategoryId());
		lastQuery.setOrderby("id desc");
		AppArticle lastArticle = appArticleService.get(lastQuery);
		if(lastArticle!=null){
			Article a = new Article();
			a.setId(lastArticle.getId());
			a.setTitle(lastArticle.getTitle());
			map.put("lastArticle", a);
		}else{
			map.put("lastArticle", null);
		}
		
		//查询当前这一条
		
		Article b = new Article();
		b.setCategoryId(appArticle.getCategoryId());
		b.setCategoryName(appArticle.getCategoryName());
		b.setSeoTitle(appArticle.getSeoTitle());
		b.setSeoDescribe(appArticle.getSeoDescribe());
		b.setContent(appArticle.getContent());
		b.setWriter(appArticle.getWriter());
		b.setHits(appArticle.getHits());
		b.setTitle(appArticle.getTitle());
		b.setModified(appArticle.getModified());
		b.setOutLink(appArticle.getOutLink());
		map.put("article", b);
		
		//查询下一条
		QueryFilter nextQuery = new QueryFilter(AppArticle.class);
		nextQuery.addFilter("id>", id);
		nextQuery.addFilter("categoryId=", appArticle.getCategoryId());
		AppArticle nextArticle = appArticleService.get(nextQuery);
		if(nextArticle!=null){
			Article a = new Article();
			a.setId(nextArticle.getId());
			a.setTitle(nextArticle.getTitle());
			map.put("nextArticle", a);
		}else{
			map.put("nextArticle", null);
		}
		
		
		return map;
	}

	@Override
	public List<ArticleCategory> findHelpArticCategory(String website,String parentName) {
		
		//查帮助中心下所有的分类
		QueryFilter queryFilter = new QueryFilter(AppCategory.class);
		queryFilter.addFilter("website=", website);
		queryFilter.addFilter("preateName=", parentName);
		queryFilter.setOrderby("sort");
		List<AppCategory> find = appCategoryService.find(queryFilter);
		
		ArrayList<ArticleCategory> list =new ArrayList<ArticleCategory>();
			
		
		//查分类下的所有文章
		if(find!=null&&find.size()>0){
			for(AppCategory category : find){
				//封装数据
				ArticleCategory articleCategory = new ArticleCategory();
				articleCategory.setId(category.getId());
				articleCategory.setName(category.getName());
				articleCategory.setSort(articleCategory.getSort());
				
				QueryFilter filter = new QueryFilter(AppArticle.class);
				filter.addFilter("categoryId=",category.getId());
				filter.setOrderby("sort");
				List<AppArticle> articleList = appArticleService.find(filter);
				if(articleList!=null&&articleList.size()>0){
					ArrayList<Article> articles = new ArrayList<Article>();
					for(AppArticle appArticle : articleList){
						Article a = new Article();
						a.setId(appArticle.getId());
						a.setTitle(appArticle.getTitle());
						articles.add(a);
					}
					articleCategory.setArticles(articles);
				}
				
				list.add(articleCategory);
			}
		}
		
		
		return list;
	}

	@Override
	public Article getHelpArtic(String id) {
		AppArticle appArticle = appArticleService.get(Long.valueOf(id));		
		Article b = new Article();
		b.setCategoryId(appArticle.getCategoryId());
		b.setCategoryName(appArticle.getCategoryName());
		b.setSeoTitle(appArticle.getSeoTitle());
		b.setSeoDescribe(appArticle.getSeoDescribe());
		b.setContent(appArticle.getContent());
		b.setWriter(appArticle.getWriter());
		b.setHits(appArticle.getHits());
		b.setTitle(appArticle.getTitle());
		b.setModified(appArticle.getModified());
		b.setOutLink(appArticle.getOutLink());
		//增加点击量
		Integer hits = appArticle.getHits()+1;
		appArticle.setHits(hits);
		appArticleService.update(appArticle);
		return b;
	}
	
	@Override
	public List<ArticleCategory> findAboutsArticCategory(String website,String parentName) {
		
		//查帮助中心下所有的分类
		QueryFilter queryFilter = new QueryFilter(AppCategory.class);
		queryFilter.addFilter("website=", website);
		queryFilter.addFilter("preateName=", parentName);
		queryFilter.setOrderby("sort");
		List<AppCategory> find = appCategoryService.find(queryFilter);
		
		ArrayList<ArticleCategory> list =new ArrayList<ArticleCategory>();
			
		
		//查分类下的所有文章
		if(find!=null&&find.size()>0){
			for(AppCategory category : find){
				//封装数据
				ArticleCategory articleCategory = new ArticleCategory();
				articleCategory.setId(category.getId());
				articleCategory.setName(category.getName());
				articleCategory.setSort(articleCategory.getSort());
				
				QueryFilter filter = new QueryFilter(AppArticle.class);
				filter.addFilter("categoryId=",category.getId());
				filter.setOrderby("sort");
				List<AppArticle> articleList = appArticleService.find(filter);
				if(articleList!=null&&articleList.size()>0){
					ArrayList<Article> articles = new ArrayList<Article>();
					for(AppArticle appArticle : articleList){
						Article a = new Article();
						a.setId(appArticle.getId());
						a.setTitle(appArticle.getTitle());
						articles.add(a);
					}
					articleCategory.setArticles(articles);
				}
				
				list.add(articleCategory);
			}
		}
		
		
		return list;
	}
}
