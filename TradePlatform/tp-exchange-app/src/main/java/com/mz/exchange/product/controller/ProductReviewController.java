/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:09:25
 */
package com.mz.exchange.product.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ProductReview;
import com.mz.util.QueryFilter;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.exchange.product.service.ProductReviewService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:09:25
 */
@Controller
@RequestMapping("/product/productReview")
public class ProductReviewController extends BaseController<ProductReview, Long> {

	@Resource(name = "productReviewService")
	@Override
	public void setService(BaseService<ProductReview, Long> service) {
		super.service = service;
	}

	@MethodName(name = "查询product所有的")
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ProductReview.class, request);
		PageResult findPage = super.findPage(filter);
		return findPage;
	}


	@MethodName(name = "根据id删除数据(将IssueState设置成3)")
	@RequestMapping("/pass/{ids}")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult pass(@PathVariable Long[] ids) {
		
		ProductReviewService productReviewService = (ProductReviewService)service;
		if(ids.length >0){
			JsonResult result = productReviewService.passReview(ids ,1);
			if(result.getSuccess()){
				result.setMsg("批量通过成功");
				return result;
			}
		}
		JsonResult result = new JsonResult();
		result.setMsg("所选的评论不能为空");
		result.setSuccess(false);
		return result;
	}

	@MethodName(name = "批量删除数据")
	@RequestMapping("/delete/{ids}")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult delete(@PathVariable Long[] ids) {
		ProductReviewService productReviewService = (ProductReviewService)service;
		if(ids.length >0){
			JsonResult result = productReviewService.passReview(ids ,2);
			if(result.getSuccess()){
				result.setMsg("批量删除成功");
				return result;
			}
		}
		JsonResult result = new JsonResult();
		result.setMsg("所选的评论不能为空");
		result.setSuccess(false);
		return result;
	}

	@MethodName(name = "查看一条产品评论数据")
	@RequestMapping(value = "/see/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ProductReview see(@PathVariable Long id) {

		return service.get(id);
	}
	
	
	
}
