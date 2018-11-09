/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年4月29日 下午5:05:45
 */
package com.mz.web.article.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.app.model.AppCategory;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.article.service.AppCategoryService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/article/appcategory")
public class AppCategoryController extends BaseController<AppCategory, Long> {

	@Resource(name = "appCategoryService")
	@Override
	public void setService(BaseService<AppCategory, Long> service) {
		super.service = service;
	}

	@MethodName(name = "修改一条文章类型数据")
	@RequestMapping("/modify")
	@ResponseBody
	public JsonResult modify(AppCategory appCategory) {
		return super.update(appCategory);
	}

	@MethodName(name = "增加一条文章类型数据")
	@RequestMapping("/add")
	@ResponseBody
	public JsonResult add(AppCategory appCategory) {
		if(appCategory.getPreateId()!=null){
			AppCategory parentCategory = service.get(appCategory.getPreateId());
			appCategory.setPreateName(parentCategory.getName());
			appCategory.setPreateId(parentCategory.getId());
		}
		return super.save(appCategory);
	}

	@MethodName(name = "删除一条文章类型数据")
	@RequestMapping("/remove/{id}")
	@ResponseBody
	public JsonResult remove(@PathVariable String id) {
		AppCategoryService aservice = (AppCategoryService) service;
		AppCategory category = aservice.get(Long.valueOf(id));
		if("最新动态".equals(category.getName())||
		   "新闻资讯".equals(category.getName())||
		   "行业动态".equals(category.getName())||
		   "帮助中心".equals(category.getName())||
		   "使用条款".equals(category.getName())||
		   "隐私政策".equals(category.getName())||
		   "联系我们".equals(category.getName())||
		   "Notice".equals(category.getName())||
		   "News".equals(category.getName())||
		   "Industry News".equals(category.getName())||
		   "Help".equals(category.getName())||
		   "Terms of use".equals(category.getName())||
		   "Privacy policy".equals(category.getName())||
		   "Contact us".equals(category.getName())
		){	
			JsonResult jsonResult = new JsonResult();
			jsonResult.setMsg("此分类不能删除");
			return jsonResult;
		}
		JsonResult result = super.delete(category.getId());
		return result;
	}

	@MethodName(name = "查看文章类型")
	@RequestMapping("/see/{id}")
	@ResponseBody
	public AppCategory see(@PathVariable String id) {
		AppCategoryService aservice = (AppCategoryService) service;
		AppCategory category = aservice.get(Long.valueOf(id));
		return category;
	}

	@MethodName(name = "加载所有文章类型列表数据")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppCategory.class, request);
		filter.addFilter("state=", 0);
		filter.setOrderby("preateName");
		return super.findPage(filter);
	}

	@MethodName(name = "加载所有文章类型列表数据")
	@RequestMapping("/selectlist")
	@ResponseBody
	public List<AppCategory> selectlist(HttpServletRequest request) {
		List<AppCategory> list = ((AppCategoryService)service).selectlist(request);
		return list;
	}
	
}
