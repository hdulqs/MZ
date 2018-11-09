/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年6月27日 下午6:39:43
 */
package com.mz.change.remote.account;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.exchange.product.model.ProductReview;
import com.mz.util.RemoteQueryFilter;
import java.util.List;

/**
 * <p> TODO</p>
 * @author: Wu Shuiming
 * @Date :          2016年6月27日 下午6:39:43 
 */
public interface RemoteProductReviewService {

  // 根据币的代码查询当前币的所有评论
  public List<ProductReview> findReviewByCoincode(RemoteQueryFilter rFilter);

  // 给某一条评论点赞(id为消息id  a --- 1或 -1 两个值    s判断是点赞还是批评like  hate )
  public String giveReviewpraise(Long id, int a, String s);

  public JsonResult saveProductReview(ProductReview productReview);


}     
