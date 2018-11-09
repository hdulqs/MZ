/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月13日 上午10:22:53
 */
package com.mz.web.menu.service;

import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.web.menu.model.AppMenuTree;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年10月13日 上午10:22:53 
 */
public interface AppMenuTreeService extends BaseService<AppMenuTree, Long> {

  /**
   * <p>添加配置</p>
   * @author: Liu Shilei
   * @param:    @param request
   * @param:    @return
   * @return: JsonResult
   * @Date :          2016年6月12日 上午11:22:29
   * @throws:
   */
  JsonResult addConf(HttpServletRequest request);

  /**
   * <p>删除应用菜单，级连删除</p>
   * @author: Liu Shilei
   * @param:    @param id
   * @param:    @return
   * @return: JsonResult
   * @Date :          2016年6月12日 下午2:14:27
   * @throws:
   */
  JsonResult remove(String id);

  /**
   * <p>加载系统菜单</p>
   * @author: Liu Shilei
   * @param:    @param request
   * @param:    @return
   * @return: List<AppMenuTree>
   * @Date :          2016年6月12日 下午2:59:59
   * @throws:
   */
  List<AppMenuTree> findSystemMenu(HttpServletRequest request);

  /**
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param id
   * @param:    @param type
   * @param:    @return
   * @return: JsonResult
   * @Date :          2016年6月12日 下午6:10:55
   * @throws:
   */
  JsonResult move(String id, String type);

  /**
   * <p>应用菜单树添加功能节点</p>
   * @author: Liu Shilei
   * @param:    @param request
   * @param:    @return
   * @return: JsonResult
   * @Date :          2016年6月13日 下午3:44:11
   * @throws:
   */
  JsonResult addnode(HttpServletRequest request);

  /**
   * <p>查询当前登录用户拥有的应用</p>
   * @author: Liu Shilei
   * @param:    @param request
   * @param:    @return
   * @return: List<AppMenuTree>
   * @Date :          2016年6月16日 下午4:09:36
   * @throws:
   */
  List<AppMenuTree> loadapp(HttpServletRequest request);

  /**
   * 查询生个角色下的对应的应用树
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param request
   * @param:    @return
   * @return: List<AppMenuTree>
   * @Date :          2016年8月1日 上午10:20:06
   * @throws:
   */
  List<AppMenuTree> findRoleByApp(Long roleId, String appName);

}
