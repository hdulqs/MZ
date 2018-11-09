/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年4月29日 下午5:02:03
 */
package com.mz.web.article.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppArticle;
import com.mz.web.app.model.AppCategory;
import com.mz.web.initBinder.StringToArtic;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.article.service.AppArticleService;
import com.mz.web.article.service.AppCategoryService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年4月29日 下午5:02:03
 */

@Controller
@RequestMapping("/article/apparticle")
public class AppArticleController extends BaseController<AppArticle, Long> {

	@Resource(name = "appArticleService")
	@Override
	public void setService(BaseService<AppArticle, Long> service) {
		super.service = service;
	}

	@Resource(name = "appCategoryService")
	private AppCategoryService categoryService;
	
	
	@InitBinder
	public void initBinderAppArticle(WebDataBinder binder) {
		binder.registerCustomEditor(AppArticle.class, "nextAppArticle", new StringToArtic());
		binder.registerCustomEditor(AppArticle.class, "upAppArticle", new StringToArtic());
	}
	

	@MethodName(name = "修改一条文章数据")
	@RequestMapping("/modify")
	@ResponseBody
	// BindingResult result
	public JsonResult modify(AppArticle appArticle){
		JsonResult jsoreult = super.update(appArticle);
		return jsoreult;
	}

	@MethodName(name = "增加一条文章数据")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(AppArticle appArticle) {

		Long id = appArticle.getCategoryId();

		AppCategory category = categoryService.get(id);

		String name = category.getName();

		appArticle.setCategoryName(name);

		JsonResult jsonResult = super.save(appArticle);

		return jsonResult;
	}
	
	@MethodName(name = "测试drools 无任何意义")
	@RequestMapping("/testAdd")
	@ResponseBody
	public JsonResult testAdd() {
		AppArticleService aservice = (AppArticleService) service;
		AppArticle appArticle=new AppArticle();
		appArticle.setTitle("test Drools");
		appArticle.setSeoTitle("123");
		appArticle.setHits(0);
		AppArticle s = aservice.testAdd(appArticle);
		return null;
	}

	@MethodName(name = "删除一条文章数据数据")
	@RequestMapping("/remove")
	@ResponseBody
	public JsonResult remove(@RequestParam(value = "ids[]") Long[] ids) {
		AppArticleService aservice = (AppArticleService) service;
		JsonResult jsonResult = new JsonResult();
		String s = aservice.removeAll(ids);
		if("OK".equals(s)){
			jsonResult.setSuccess(true);
			jsonResult.setMsg("删除成功");
		}else{
			jsonResult.setSuccess(false);
			jsonResult.setMsg("删除失败");
		}
		return jsonResult;
	}

	@MethodName(name = "查看一篇文章")
	@RequestMapping("/see/{id}")
	@ResponseBody
	public AppArticle see(@PathVariable Long id) {
		return service.get(id);
	}

	@MethodName(name = "加载文章列表数据")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppArticle.class, request);
		filter.addFilter("status=", 0);
		filter.setOrderby("created  desc");
		
		return super.findPage(filter);
	}
	

	@MethodName(name = "加载文章列表数据")
	@RequestMapping("/list2")
	@ResponseBody
	public PageResult list2(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppArticle.class, request);
		filter.addFilter("status=", 0);
		return super.findPage(filter);
	}
}
