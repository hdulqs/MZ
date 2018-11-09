package com.mz.front.news.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteAppArticleService;
import com.mz.manage.remote.model.Article;
import com.mz.manage.remote.model.ArticleCategory;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.util.sys.SpringContextUtil;;

@Controller
@RequestMapping("/news")
public class NewsController {
	private final static Logger log = Logger.getLogger(NewsController.class);
	
	/**
	 * 注册类型属性编辑器
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {

		// 系统注入的只能是基本类型，如int，char，String

		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

		/**
		 * 防止XSS攻击，并且带去左右空格功能
		 */
		binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
	}
	
	
	/**
	 * 文章详情
	 * 
	 * @return
	 */
	@RequestMapping("info/{id}")
	public ModelAndView info(@PathVariable Long id) {
		Locale locale = LocaleContextHolder.getLocale();
		ModelAndView mav = new ModelAndView("newsinfo");
		if(StringUtils.isEmpty(id)){
			return mav;
		}
		//查询文章分类
		RemoteAppArticleService remoteAppArticleService = SpringContextUtil.getBean("remoteAppArticleService");
		List<ArticleCategory> category = remoteAppArticleService.findArticCategory("zh_CN".equals(locale.toString())?"cn":"en","zh_CN".equals(locale.toString())?"帮助中心":"Help");
		if(category!=null){
			mav.addObject("category", category);
			
			//查询文章
			Map<String, Article> map = remoteAppArticleService.getArtic(id.toString());
			Article artic =  map.get("article");
			if(artic!=null){
				mav.addObject("article",map.get("article"));
				mav.addObject("lastArticle",map.get("lastArticle"));
				mav.addObject("nextArticle",map.get("nextArticle"));
				
				for(ArticleCategory c : category){
					if(c.getId().toString().equals(artic.getCategoryId().toString())){
						mav.addObject("thiscategory", c);
					}
				}
			}
		}

		
		//开启页面静态化
		//mav.addObject("CREATE_HTML", true);
		return mav;
	}
	
	
	/**
	 * 帮助中心
	 * 
	 * @return
	 */
	@RequestMapping("help")
	public ModelAndView help(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("help");
		Locale locale = LocaleContextHolder.getLocale();
		String s = locale.toString();
		if ("zh_CN".equals(s)){
			s="cn";
		}
		String id = request.getParameter("id");
		String categoryId = request.getParameter("categoryId");
		//查询文章分类
		RemoteAppArticleService remoteAppArticleService = SpringContextUtil.getBean("remoteAppArticleService");
		//List<ArticleCategory> category = remoteAppArticleService.findHelpArticCategory("zh_CN".equals(locale.toString())?"cn":"en","zh_CN".equals(locale.toString())?"帮助中心":"Help");
		List<ArticleCategory> category = remoteAppArticleService.findHelpArticCategory(s,"zh_CN".equals(locale.toString())?"帮助中心":"Help");
		mav.addObject("category", category);
		
		if(!StringUtils.isEmpty(id)){
			Article helpArtic = remoteAppArticleService.getHelpArtic(id);
			mav.addObject("article",helpArtic);
		}else{
			if(category!=null&&category.size()>0){
				if(category.get(0)!=null){
					if(category.get(0).getArticles()!=null){
						/*if("en".equals(locale.toString())){
							Article helpArtic = remoteAppArticleService.getHelpArtic(category.get(0).getArticles().get(0).getId().toString());
							mav.addObject("article",helpArtic);
						}else{
							Article helpArtic = remoteAppArticleService.getHelpArtic(category.get(0).getArticles().get(0).getId().toString());
							mav.addObject("article",helpArtic);
						}*/
						List<Article> findArticListByIdLimit = remoteAppArticleService.findArticListByIdLimit(Long.valueOf(categoryId), 1, "zh_CN".equals(locale.toString())?"cn":"en");
						if(findArticListByIdLimit.size()>0){
							mav.addObject("article",findArticListByIdLimit.get(0));
						}
					}
				}
			}
		}
		mav.addObject("showColor", "4");
		//开启页面静态化
		//mav.addObject("CREATE_HTML", true);
		return mav;
	}
	

	/**
	 * 主页
	 * 
	 * @return
	 */
	@RequestMapping("index/{categoryid}")
	public ModelAndView index(@PathVariable Long categoryid,HttpServletRequest request) {
		Locale locale = LocaleContextHolder.getLocale();
		String s = locale.toString();
		if ("zh_CN".equals(s)){
			s="cn";
		}
		ModelAndView mav = new ModelAndView("news");
		
		String offset = request.getParameter("offset");
		String limit  = request.getParameter("limit");
		
		//查询文章分类
		RemoteAppArticleService remoteAppArticleService = SpringContextUtil.getBean("remoteAppArticleService");
		//List<ArticleCategory> category = remoteAppArticleService.findArticCategory("zh_CN".equals(locale.toString())?"cn":"en","zh_CN".equals(locale.toString())?"新闻资讯":"News");
		List<ArticleCategory> category = remoteAppArticleService.findArticCategory(s,"zh_CN".equals(locale.toString())?"新闻资讯":"News");
		if(category!=null&&category.size()>0){
			mav.addObject("category", category);
			
			//分页查询文章
			Map<String, String> params = HttpServletRequestUtils.getParams(request);
			if(StringUtils.isEmpty(offset)){
				params.put("offset", "0");
			}
			if(StringUtils.isEmpty(limit)){
				params.put("limit", "5");
			}
			
			if("zh_CN".equals(locale.toString())){
				
			}else{
				if(categoryid==5L){
					categoryid = 65L;
				}
			}
			
			if(StringUtils.isEmpty(categoryid)){//默认文章分类
				params.put("categoryid", category.get(0).getId().toString());
				//标记当前分类
				mav.addObject("thiscategory", category.get(0));
			}else{
				params.put("categoryid", categoryid.toString());
				//标记当前分类
				for(ArticleCategory c : category){
					if(c.getId().toString().equals(categoryid)){
						mav.addObject("thiscategory", c);
					}
				}
			}
			params.put("website","zh_CN".equals(locale.toString())?"cn":"en");
			FrontPage frontPage = remoteAppArticleService.findAritcByType(params);
			mav.addObject("frontPage", frontPage);
			mav.addObject("thispage", Integer.valueOf(params.get("offset"))/frontPage.getPageSize()+1);
			
			
		}
		mav.addObject("showColor", "3");
		//开启页面静态化
		//mav.addObject("CREATE_HTML", true);
		return mav;
	}
	
	/**
	 * 关于我们
	 * 
	 * @return
	 */
	@RequestMapping("aboutus")
	public ModelAndView aboutus(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("aboutus");
		Locale locale = LocaleContextHolder.getLocale();
		String s = locale.toString();
		if ("zh_CN".equals(s)){
			s="cn";
		}
		String id = request.getParameter("id");
		String categoryId = request.getParameter("categoryId");
		//查询文章分类
		RemoteAppArticleService remoteAppArticleService = SpringContextUtil.getBean("remoteAppArticleService");
		//List<ArticleCategory> category = remoteAppArticleService.findAboutsArticCategory("zh_CN".equals(locale.toString())?"cn":"en","zh_CN".equals(locale.toString())?"关于我们":"About");
		List<ArticleCategory> category = remoteAppArticleService.findAboutsArticCategory(s,"zh_CN".equals(locale.toString())?"关于我们":"About");
		mav.addObject("category", category);
		
		
		if(!StringUtils.isEmpty(id)){
			Article helpArtic = remoteAppArticleService.getHelpArtic(id);
			mav.addObject("article",helpArtic);
		}else{
			if(category!=null&&category.size()>0){
				if(category.get(0)!=null){
					if(category.get(0).getArticles()!=null){
						//Article helpArtic = remoteAppArticleService.getHelpArtic(category.get(0).getArticles().get(0).getId().toString());
						List<Article> findArticListByIdLimit = remoteAppArticleService.findArticListByIdLimit(Long.valueOf(categoryId), 1, "zh_CN".equals(locale.toString())?"cn":"en");
						if(findArticListByIdLimit.size()>0){
							mav.addObject("article",findArticListByIdLimit.get(0));
						}
					}
				}
			}
		}
		mav.addObject("showColor", "5");
		//开启页面静态化
		//mav.addObject("CREATE_HTML", true);
		return mav;
	}
	
} 
