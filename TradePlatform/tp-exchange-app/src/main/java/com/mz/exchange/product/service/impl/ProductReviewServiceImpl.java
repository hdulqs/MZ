/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Wu Shuiming
 * @version:      V1.0 
 * @Date:        2016年3月24日 下午2:04:29
 */
package com.mz.exchange.product.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.exchange.product.model.ProductReview;
import com.mz.exchange.product.service.ProductReviewService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午2:04:29
 */
@Service("productReviewService")
public class ProductReviewServiceImpl extends BaseServiceImpl<ProductReview, Long>
		implements ProductReviewService {
	
	@Resource(name = "productReviewDao")
	@Override
	public void setDao(BaseDao<ProductReview, Long> dao) {
		super.dao = dao;
	}

	@Override
	public JsonResult passReview(Long[] ids ,int i) {
		
		JsonResult result = new JsonResult();
		if(ids.length>0){
			for(Long id :ids){
				ProductReview review = super.get(id);
				Integer status = review.getStatus();
				if(i==2){
					if(status != 2){
						review.setStatus(i);
						super.update(review);
					}
				}if(i==1){
					if(status == 0){
						review.setStatus(i);
						super.update(review);
					}
				}		
			}
			result.setSuccess(true);
			return result;
		}
		result.setMsg("所审核的评论不能为0");
		result.setSuccess(false);
		return result;
	}



	
}
