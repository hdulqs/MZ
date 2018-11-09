/**
 * <p> TODO</p>
 *
 * @author: Gao Mimi
 * @Date :          2016年4月12日 下午4:45:50
 */
package com.mz.exchange.lend.service;


import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.exchange.lend.model.ExDmLend;
import com.mz.exchange.lend.model.ExDmLendIntent;
import com.mz.util.QueryFilter;
import com.mz.manage.remote.model.base.FrontPage;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :          2016年4月12日 下午4:45:50 
 */
public interface ExDmLendIntentService extends BaseService<ExDmLendIntent, Long> {

  public ExDmLendIntent create(ExDmLend exDmLend, BigDecimal repayCount, String intentType);

  /**
   *
   * <p> TODO</p>
   * @author: Gao Mimi
   * @param:    @param filter
   * @param:    @return
   * @return: PageResult
   * @Date :          2016年6月28日 下午3:44:47
   * @throws:
   */
  public FrontPage listIntentPage(Map<String, String> params);


  /**
   *
   * map里传的参数为  用户的名  转入开始的时间  结束时间  申请类型
   *
   * @param filter
   * @return
   */
  public PageResult findPageBySql(QueryFilter filter);


}
