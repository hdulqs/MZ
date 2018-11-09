/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月13日 上午10:19:54
 */
package com.mz.web.menu.dao;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.web.menu.model.AppMenuTree;
import java.util.List;
import java.util.Map;

/**
 *
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年10月13日 上午10:19:54
 */
public interface AppMenuTreeDao extends BaseDao<AppMenuTree, Long> {

  /**
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param map
   * @return: void
   * @Date :          2016年6月16日 下午4:38:37
   * @throws:
   */
  List<AppMenuTree> findByUserId(Map<String, Object> map);

  /**
   * 查询一个角色对应的应用树
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param map
   * @param:    @return
   * @return: List<AppMenuTree>
   * @Date :          2016年8月1日 上午10:21:39
   * @throws:
   */
  List<AppMenuTree> findRoleByApp(Map<String, Object> map);
  /**
   * 查询用户对应的权限名字
   * @param id
   * @return
   */
  //String findRoleByUser(Long id);

}