/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年10月27日  17:57:57
 */
package com.mz.web.dictionary.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.dictionary.model.AppDicOnelevel;
import java.util.List;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :      2015年10月27日  17:57:57
 */
public interface AppDicOnelevelService extends BaseService<AppDicOnelevel, Long> {

  /**
   *
   * <p> 删除字典项</p>
   * @author: Gao Mimi
   * @param:    @param id
   * @param:    @return
   * @return: JsonResult
   * @Date :          2016年6月17日 下午4:15:34
   * @throws:
   */
  public JsonResult removeDic(String id);

  /**
   *
   * <p> 添加字典项</p>
   * @author: Gao Mimi
   * @param:    @param appDictionary
   * @param:    @return
   * @return: String
   * @Date :          2016年6月17日 下午4:15:55
   * @throws:
   */
  public String addDic(AppDicOnelevel appDictionary);

  /**
   *
   * <p> 上移或者下移字段项</p>
   * @author: Gao Mimi
   * @param:    @param id
   * @param:    @param type
   * @param:    @return
   * @return: JsonResult
   * @Date :          2016年6月17日 下午4:16:10
   * @throws:
   */
  public JsonResult move(String id, String type);

  /**
   *
   * <p> 缓存到redis里</p>
   * @author: Gao Mimi
   * @param:    @param pDicKey
   * @return: void
   * @Date :          2016年6月17日 下午4:16:36
   * @throws:
   */
  public void dicToredis(String pDicKey);

  /**
   * 根据父id找到所有的兄弟姐妹
   * <p> TODO</p>
   * @author: Gao Mimi
   * @param:    @param pDicKey
   * @param:    @return
   * @return: List<AppDicOnelevel>
   * @Date :          2016年6月21日 下午3:36:09
   * @throws:
   */
  public List<AppDicOnelevel> findListBypDicKey(String pDicKey);

  /**
   * 根据父id找到所有的兄弟姐妹 自己改造的
   * <p> TODO</p>
   * @author: Gao Mimi
   * @param:    @param pDicKey
   * @param:    @return
   * @return: List<AppDicOnelevel>
   * @Date :          2016年6月21日 下午3:36:09
   * @throws:
   */
  public List<AppDicOnelevel> findListBypDicKey2(String pDicKey);

  /**
   * 找到父节点
   * <p> TODO</p>
   * @author: Gao Mimi
   * @param:    @param pDicKey
   * @param:    @return
   * @return: AppDicOnelevel
   * @Date :          2016年6月21日 下午4:23:56
   * @throws:
   */
  public AppDicOnelevel getParent(String pDicKey);

}


