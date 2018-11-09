/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年4月6日 下午4:16:01
 */
package com.mz.exchange.remote.account;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.customer.agents.model.AppAgentsCustromer;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.exchange.product.model.ExCointoCoin;
import com.mz.exchange.product.model.ExProduct;
import com.mz.util.RemoteQueryFilter;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年4月6日 下午4:16:01
 */
public interface RemoteExProductService {

  // 根据状态值来查询所有的ExProduct
  public List<ExProduct> findByIssueState(Integer i);

  public List<ExProduct> findByIssueState(RemoteQueryFilter rfilter);

  public boolean findByCode(String s);

  public ExProduct findByCoinCode(String c, String sassId);

  public ExProduct findByCoinCode(RemoteQueryFilter rfilter);

  public List<ExProduct> findByIssueState(Integer i, String saasId);

  /**
   * 开通虚拟币账户
   * @param appCustomer
   */
  public JsonResult openDmAccount(AppCustomer appCustomer, AppPersonInfo appPersonInfo,
      AppAgentsCustromer agentsCustromer, String website, String currencyType);

  /**
   * 通过用户的id查出跟用户有关的所有产品
   *
   * @author: Wu Shuiming
   * @version: V1.0
   * @date: 2016年7月26日 下午7:06:53
   */
  public List<ExProduct> findProduct(Long id);

  /**
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param filter
   * @param:    @return
   * @return: ExProduct
   * @Date :          2016年8月29日 上午11:57:01
   * @throws:
   */
  public ExProduct get(RemoteQueryFilter filter);

  /**
   * 查询下拉框的产品
   *
   * @return
   */
  public List<ExProduct> getSelectProduct();

  /**
   * 添加均价
   * <p> TODO</p>
   * @author: Zeng Hao
   * @param:    @param exProduct
   * @param:    @return
   * @return: ExProduct
   * @Date :          2016年12月7日 下午2:32:28
   * @throws:
   */
  public void addAveragePrice(ExProduct exProduct);

  /**
   * 手动生成比地址
   * <p> TODO</p>
   * @author: Zhang Lei
   * @param:    @param coin
   * @return: void
   * @Date :          2017年4月11日 下午3:19:41
   * @throws:
   */
  public void handToCoinAdress(String coin);

  /**
   *
   * @param toProductcoinCode
   * @param fromProductcoinCode
   * @param issueState
   * @return
   */
  public List<ExCointoCoin> getExCointoCoinlist(String toProductcoinCode,
      String fromProductcoinCode, Integer issueState);
}
