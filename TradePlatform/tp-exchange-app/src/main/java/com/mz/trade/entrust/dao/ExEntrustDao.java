/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2015年11月06日  14:57:13
 */
package com.mz.trade.entrust.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.manage.remote.model.Entrust;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月24日 下午1:34:18
 */
public interface ExEntrustDao extends BaseDao<ExEntrust, Long> {

  List<ExEntrust> getbuyExEntrustChange(Map<String, Object> map);

  List<ExEntrust> getsellExEntrustChange(Map<String, Object> map);

  List<ExEntrust> listMatchBySellLimitedPrice(Map<String, Object> map);

  List<ExEntrust> listMatchByBuyLimitedPrice(Map<String, Object> map);

  List<ExEntrust> getExEntrustBuyDeph(Map<String, Object> map);

  List<ExEntrust> getExEntrustSellDeph(Map<String, Object> map);


  public Map<String, BigDecimal> getExEntrustmMostPrice(Map<String, Object> map);

  //查找货币冠军
  public List<ExEntrust> getFirstCoin();

  public List<String> getFirstCoinNum();

  //前台分页查询
  public List<Entrust> findFrontPageBySql(Map<String, String> params);

  /***
   * 通过id得到几个列
   * @return
   */
  public ExEntrust getById(@Param(value = "id") Long id);

  List<ExEntrust> getExEdBycustomerId(Map<String, Object> map);

  List<ExEntrust> getExIngBycustomerId(Map<String, Object> map);
}
