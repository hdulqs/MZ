/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月13日 上午10:26:28
 */
package com.mz.web.menu.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.menu.model.AppMenuTree;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.menu.service.AppMenuTreeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2015年10月13日 上午10:26:28
 */
@Controller
@RequestMapping("/menu/appmenutree")
public class AppMenuTreeController extends BaseController<AppMenuTree, Long> {

  @Resource(name = "appMenuTreeService")
  @Override
  public void setService(BaseService<AppMenuTree, Long> service) {
    super.service = service;
  }


  @MethodName(name = "增加应用root菜单")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult add(HttpServletRequest request) {
    String appName = request.getParameter("appName");
    String appKey = request.getParameter("appKey");

    QueryFilter queryFilter = new QueryFilter(AppMenuTree.class);
    queryFilter.addFilter("mkey=", appKey);
    List<AppMenuTree> find = find(queryFilter);
    if (find != null && find.size() > 0) {
      JsonResult jsonResult = new JsonResult();
      jsonResult.setMsg("应用Key值不能重复");
      return jsonResult;
    }

    //增加应用的时候，增加应用根节点   mkey 为自定义,  type:为root
    AppMenuTree appMenuTree = new AppMenuTree();
    appMenuTree.setIsOpen("0");
    appMenuTree.setIsOutLink("0");
    appMenuTree.setName(appName);
    appMenuTree.setAppName(appKey);
    appMenuTree.setMkey(appKey);
    appMenuTree.setType("root");
    return super.save(appMenuTree);
  }


  @MethodName(name = "菜单树增加节点")
  @RequestMapping(value = "/addnode", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult addnode(HttpServletRequest request) {
    return ((AppMenuTreeService) service).addnode(request);
  }


  @MethodName(name = "加载系统左边菜单")
  @RequestMapping("/loadSystemMenu")
  @ResponseBody
  public List<AppMenuTree> loadSystemMenu(HttpServletRequest request) {
    return ((AppMenuTreeService) service).findSystemMenu(request);
  }


  @MethodName(name = "列表")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppMenuTree.class, request);
    filter.addFilter("type=", "root");
    filter.setOrderby("orderNo asc");
    PageResult findPage = super.findPage(filter);
    return findPage;
  }

  @MethodName(name = "查询一个应用菜单树")
  @RequestMapping("/findByApp")
  @ResponseBody
  public List<AppMenuTree> findByApp(HttpServletRequest request) {
    String id = request.getParameter("id");
    String type = request.getParameter("type");
    if (!StringUtils.isEmpty(id)) {
      AppMenuTree root = service.get(Long.valueOf(id));
      QueryFilter filter = new QueryFilter(AppMenuTree.class, request);
      if (!StringUtils.isEmpty(type) && "select".equals(type)) {//选择上级
        filter.addFilter("type_in", "root,menus");
      } else if (!StringUtils.isEmpty(type) && "tree".equals(type)) {//配置应用树
        filter.addFilter("type_in", "root,menus,menu");
      }
      filter.addFilter("appName=", root.getAppName());
      filter.setOrderby("orderNo asc");
      return service.find(filter);
    }
    return null;
  }

  @MethodName(name = "查询一个每个角色对应的应用菜单树")
  @RequestMapping("/findRoleByApp")
  @ResponseBody
  public List<AppMenuTree> findRoleByApp(HttpServletRequest request) {
    String roleId = request.getParameter("roleId");
    String appName = request.getParameter("appName");
    if (!StringUtils.isEmpty(roleId) && !StringUtils.isEmpty(appName)) {
      return ((AppMenuTreeService) service).findRoleByApp(Long.valueOf(roleId), appName);
    }
    return null;
  }

  @MethodName(name = "查询一个每个角色对应的应用菜单树,和这个应用菜单树的全集")
  @RequestMapping("/findRoleByAppHas")
  @ResponseBody
  public Map<String, List<AppMenuTree>> findRoleByAppHas(HttpServletRequest request) {
    String roleId = request.getParameter("roleId");
    String appName = request.getParameter("appName");
    if (!StringUtils.isEmpty(roleId) && !StringUtils.isEmpty(appName)) {
      Map<String, List<AppMenuTree>> map = new HashMap<String, List<AppMenuTree>>();
      List<AppMenuTree> findRoleByApp = ((AppMenuTreeService) service)
          .findRoleByApp(Long.valueOf(roleId), appName);

      QueryFilter filter = new QueryFilter(AppMenuTree.class);
      filter.addFilter("appName=", appName);
      map.put("has", findRoleByApp);
      map.put("all", service.find(filter));
      return map;
    }
    return null;
  }


  @MethodName(name = "加载出所有的应用")
  @RequestMapping("/loadapp")
  @ResponseBody
  public List<AppMenuTree> loadapp(HttpServletRequest request) {
    return ((AppMenuTreeService) service).loadapp(request);

  }

  @MethodName(name = "增加应用root菜单")
  @RequestMapping(value = "/addconf", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult addconf(HttpServletRequest request) {
    return ((AppMenuTreeService) service).addConf(request);
  }

  @MethodName(name = "删除")
  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult remove(String id) {
    return ((AppMenuTreeService) service).remove(id);
  }

  @MethodName(name = "移动")
  @RequestMapping(value = "/move", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult move(String id, String type) {
    return ((AppMenuTreeService) service).move(id, type);
  }


  @MethodName(name = "修改")
  @RequestMapping(value = "/modify", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult modify(Long id, String name) {
    AppMenuTree appMenuTree = service.get(id);
    appMenuTree.setName(name);
    return super.update(appMenuTree);
  }


}
