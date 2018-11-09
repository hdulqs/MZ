/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月24日 下午1:45:20
 */
package com.mz.exchange.product.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.product.model.ProductReview;

/**
 *
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午1:47:26
 */
public interface ProductReviewService extends BaseService<ProductReview, Long> {

  // 批量通过评论(批量修改status的值为 i)
  public JsonResult passReview(Long[] ids, int i);


}
