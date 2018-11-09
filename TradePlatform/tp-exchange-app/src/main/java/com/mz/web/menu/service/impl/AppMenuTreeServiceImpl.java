/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月13日 上午10:23:31
 */
package com.mz.web.menu.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.oauth.remote.user.RemoteAppRoleMenuTreeService;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.util.SortList;
import com.mz.util.date.DateUtil;
import com.mz.util.pinying4j.Pinying4jUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.web.menu.model.AppMenu;
import com.mz.web.menu.model.AppMenuCust;
import com.mz.web.menu.model.AppMenuTree;
import com.mz.web.menu.dao.AppMenuTreeDao;
import com.mz.web.menu.service.AppMenuCustService;
import com.mz.web.menu.service.AppMenuTreeService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2015年10月13日 上午10:23:31 
 */
@Service("appMenuTreeService")
public class AppMenuTreeServiceImpl extends BaseServiceImpl<AppMenuTree, Long> implements
    AppMenuTreeService {

  @Resource(name = "appMenuTreeDao")
  @Override
  public void setDao(BaseDao<AppMenuTree, Long> dao) {
    super.dao = dao;
  }

  @Resource
  private AppMenuCustService appMenuCustService;

  /**
   * 获得指定应用下 相同父级下最大的排序值菜单
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param appName
   * @param:    @param type
   * @param:    @param pkey
   * @param:    @return
   * @return: AppMenuTree
   * @Date :          2016年6月13日 下午3:48:17
   * @throws:
   */
  public int getLastOrder(String appName, String pkey) {
    QueryFilter filter = new QueryFilter(AppMenuTree.class);
    filter.addFilter("appName=", appName);
    filter.addFilter("pkey=", pkey);
    filter.setOrderby("orderNo desc");
    AppMenuTree appMenuTree = get(filter);
    if (null != appMenuTree) {
      return appMenuTree.getOrderNo();
    }
    return 0;
  }


  @Override
  public JsonResult addConf(HttpServletRequest request) {

    String superiorMenuId = request.getParameter("superiorMenuId");
    String functionIds = request.getParameter("functionIds");
    String name = request.getParameter("name");

    JsonResult jsonResult = new JsonResult();
    if (StringUtils.isEmpty(superiorMenuId)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("上级菜单不能为空");
    } else {
      //上级菜单
      AppMenuTree superiorMenu = super.get(Long.valueOf(superiorMenuId));

      //创建新菜单
      AppMenuTree menu1 = new AppMenuTree();
      menu1.setName(name);
      menu1.setMkey(
          Pinying4jUtil.getPinYin(name) + DateUtil.dateToString(new Date(), "MMddHHmmssS"));
      menu1.setPkey(superiorMenu.getMkey());
      menu1.setAppName(superiorMenu.getAppName());
      menu1.setType("menus");

      //查出最后一个菜单的orderNo
      int lastOrder = getLastOrder(superiorMenu.getAppName(), superiorMenu.getMkey());
      menu1.setOrderNo(lastOrder + 1);
      //保存新菜单
      super.save(menu1);

      //新菜单下添加功能
      if (!StringUtils.isEmpty(functionIds)) {
        String[] custIds = StringUtils.split(functionIds, ",");
        for (int i = 0; i < custIds.length; i++) {
          //保存功能菜单
          AppMenuCust appMenuCust = appMenuCustService.get(Long.valueOf(custIds[i]));
          AppMenuTree menu2 = new AppMenuTree();  //功能
          BeanUtil.copyNotNullProperties(appMenuCust, menu2);
          menu2.setId(null);
          menu2.setAppName(menu1.getAppName());
          menu2.setOkey(appMenuCust.getMkey());
          menu2.setMkey(menu1.getMkey() + "_" + appMenuCust.getMkey() + "_" + DateUtil
              .dateToString(new Date(), "MMddHHmmssS"));
          menu2.setPkey(menu1.getMkey());   //功能的父级为新菜单的mkey
          menu2.setOrderNo(i);
          super.save(menu2);   //保存菜单  ---一级

          //保存功能下的权限
          QueryFilter filter = new QueryFilter(AppMenuCust.class);
          filter.addFilter("pkey=", appMenuCust.getMkey());
          filter.addFilter("isVisible!=", "1");
          List<AppMenuCust> smenuList = appMenuCustService.find(filter);
          if (smenuList != null && smenuList.size() > 0) {
            for (AppMenuCust smenuCust : smenuList) {
              AppMenuTree menu3 = new AppMenuTree();
              BeanUtil.copyNotNullProperties(smenuCust, menu3);
              menu3.setId(null);
              menu3.setAppName(menu2.getAppName());
              menu3.setOkey(smenuCust.getMkey());
              menu3.setMkey(menu2.getMkey() + "_" + smenuCust.getMkey() + "_" + DateUtil
                  .dateToString(new Date(), "MMddHHmmssS"));
              menu3.setPkey(menu2.getMkey());
              super.save(menu3);  //保存权限  ---二级
            }
          }


        }
      }
      jsonResult.setSuccess(true);
    }

    return jsonResult;
  }

  @Override
  public JsonResult remove(String id) {

    JsonResult jsonResult = new JsonResult();

    if (StringUtils.isEmpty(id)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("id不能为空");
    } else {
      //list记录删除的appMenuTree Id
      ArrayList<Long> list = new ArrayList<Long>();
      diguiRemove(Long.valueOf(id), list);

      //远程调用解除app_role_menutree 角色 菜单关系表
      RpcContext.getContext().setAttachment("saasId", ContextUtil.getSaasId());
      RemoteAppRoleMenuTreeService remoteAppRoleMenuTreeService = (RemoteAppRoleMenuTreeService) ContextUtil
          .getBean("remoteAppRoleMenuTreeService");
      remoteAppRoleMenuTreeService.delete(list);

      jsonResult.setSuccess(true);
    }

    return jsonResult;
  }


  /**
   * 级联删除
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param id
   * @return: void
   * @Date :          2016年6月12日 下午2:25:45
   * @throws:
   */
  public void diguiRemove(Long id, ArrayList<Long> idList) {
    AppMenuTree root = get(id);
    if (root != null) {
      String pkey = root.getMkey();
      super.delete(id);  //删除自身
      idList.add(id);
      QueryFilter queryFilter = new QueryFilter(AppMenuTree.class);
      queryFilter.addFilter("pkey=", pkey);
      List<AppMenuTree> list = find(queryFilter);
      if (list != null && list.size() > 0) {
        for (AppMenuTree appMenuTree : list) {
          diguiRemove(appMenuTree.getId(), idList);
        }
      }
    }
  }

  @Override
  public List<AppMenuTree> findSystemMenu(HttpServletRequest request) {

    String key = request.getParameter("key");
    String type = request.getParameter("type");//标记这个service是哪个页面调用的

    //获得当前登录用户
    AppUser user = ContextUtil.getCurrentUser();
    Long id = user.getId();
    //查找权限名
    //String roleByUser = ((AppMenuTreeDao) dao).findRoleByUser(id);
    //最终返回的菜单

    //如果是admin账户返回所有菜单
    if (PropertiesUtils.APP.getProperty("app.admin").equals(user.getUsername())) {
      //查找所有菜单
      QueryFilter filter = new QueryFilter(AppMenu.class);
      filter.addFilter("type!=", "root");
      if ("loadMeny".equals(type)) {//如果是加载菜单栏的加上这个条件
        filter.addFilter("type!=", "function");
      }

      filter.addFilter("isVisible!=", "1");
      filter.addFilter("appName_in", key);
      filter.setOrderby("orderNo asc");
      List<AppMenuTree> allList = find(filter);
      return allList;
    }/* else if (roleByUser.contains("项目合作方")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			map.put("eqtype", "neq");
			map.put("type", new String[]{"root", "function"});
			map.put("appName", key);
			List<AppMenuTree> userList = ((AppMenuTreeDao) dao).findByUserId(map);
			//去掉重复的
			HashSet<AppMenuTree> set = new HashSet<AppMenuTree>(userList);
			userList.clear();
			userList.addAll(set);
			SortList<AppMenuTree> sortList = new SortList<AppMenuTree>();
			sortList.Sort(userList, "getOrderNo", null);
			return userList;
		} */ else {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("userId", user.getId());
      map.put("eqtype", "neq");
      map.put("type", new String[]{"root", "function"});
      map.put("appName", key);
      List<AppMenuTree> userList = ((AppMenuTreeDao) dao).findByUserId(map);
      //去掉重复的
      HashSet<AppMenuTree> set = new HashSet<AppMenuTree>(userList);
      userList.clear();
      userList.addAll(set);

      //最终返回的菜单list
      ArrayList<AppMenuTree> returnList = new ArrayList<AppMenuTree>();
      //查询一个菜单树的所有的menus
      QueryFilter filter = new QueryFilter(AppMenuTree.class);
      filter.addFilter("appName=", key);
      filter.addFilter("type=", "menus");
      filter.setOrderby(" orderNo ");
      List<AppMenuTree> menus = super.find(filter);
      //以下算法为了处理一个用户有多个角色的情况下，能保持一个应用菜单树的顺序结构
      for (AppMenuTree am : menus) {
        returnList.add(am);

        //查出子菜单并排序
        ArrayList<AppMenuTree> arrayList = new ArrayList<AppMenuTree>();
        for (AppMenuTree ul : userList) {
          if (ul.getPkey().equals(am.getMkey())) {
            arrayList.add(ul);
          }
        }
        SortList<AppMenuTree> sortList = new SortList<AppMenuTree>();
        sortList.Sort(arrayList, "getOrderNo", null);
        //全部添加
        returnList.addAll(arrayList);

      }
      return returnList;
    }


  }

  @Override
  public JsonResult move(String id, String type) {

    JsonResult jsonResult = new JsonResult();
    if (StringUtils.isEmpty(id)) {
      jsonResult.setSuccess(false);
    } else if (StringUtils.isEmpty(type)) {
      jsonResult.setSuccess(false);
    } else {
      AppMenuTree appMenuTree = this.get(Long.valueOf(id));

      QueryFilter filter = new QueryFilter(AppMenuTree.class);
      filter.addFilter("appName=", appMenuTree.getAppName());
      filter.addFilter("pkey=", appMenuTree.getPkey());
      if ("up".equals(type)) {
        filter.addFilter("orderNo<", appMenuTree.getOrderNo());
        filter.setOrderby("orderNo desc");
      } else {
        filter.addFilter("orderNo>", appMenuTree.getOrderNo());
        filter.setOrderby("orderNo asc");
      }
      AppMenuTree appMenuTree2 = get(filter);

      if (appMenuTree2 != null) {
        int orderNo = appMenuTree.getOrderNo();
        int orderNo2 = appMenuTree2.getOrderNo();

        appMenuTree.setOrderNo(orderNo2);
        appMenuTree2.setOrderNo(orderNo);
        update(appMenuTree);
        update(appMenuTree2);
      }

    }

    return jsonResult;
  }

  @Override
  public JsonResult addnode(HttpServletRequest request) {

    String id = request.getParameter("id");
    String functionIds = request.getParameter("functionIds");

    JsonResult jsonResult = new JsonResult();
    if (StringUtils.isEmpty(id)) {
      jsonResult.setSuccess(false);
    } else if (StringUtils.isEmpty(functionIds)) {
      jsonResult.setSuccess(false);
    } else {
      //父级菜单
      AppMenuTree parentMenu = get(Long.valueOf(id));
      //菜单下要添加的功能
      String[] strArr = StringUtils.split(functionIds, ",");

      int lastOrder = getLastOrder(parentMenu.getAppName(), parentMenu.getMkey());
      for (int i = 0; i < strArr.length; i++) {
        //查询功能
        AppMenuCust appMenuCust = appMenuCustService.get(Long.valueOf(strArr[i]));
        AppMenuTree menu1 = new AppMenuTree();
        //复制功能
        BeanUtil.copyNotNullProperties(appMenuCust, menu1);
        menu1.setId(null);
        menu1.setAppName(parentMenu.getAppName());
        menu1.setPkey(parentMenu.getMkey());
        menu1.setOkey(appMenuCust.getMkey());
        menu1.setMkey(parentMenu.getMkey() + "_" + appMenuCust.getMkey() + "_" + DateUtil
            .dateToString(new Date(), "MMddHHmmssS"));
        menu1.setOrderNo(lastOrder + i + 1);
        super.save(menu1);//保存功能

        //查询功能下的权限 ，并保存
        QueryFilter filter = new QueryFilter(AppMenuCust.class);
        filter.addFilter("pkey=", appMenuCust.getMkey());
        filter.addFilter("isVisible!=", "1");
        List<AppMenuCust> find = appMenuCustService.find(filter);
        if (find != null && find.size() > 0) {

          for (AppMenuCust smenuCust : find) {
            AppMenuTree menu2 = new AppMenuTree();
            BeanUtil.copyNotNullProperties(smenuCust, menu2);
            menu2.setId(null);
            menu2.setAppName(menu1.getAppName());
            menu2.setPkey(menu1.getMkey());
            menu2.setOkey(smenuCust.getMkey());
            menu2.setMkey(menu1.getMkey() + "_" + smenuCust.getMkey() + "_" + DateUtil
                .dateToString(new Date(), "MMddHHmmssS"));
            super.save(menu2);  //保存权限  ---二级
          }

        }


      }
      jsonResult.setSuccess(true);

    }
    return jsonResult;
  }


  @Override
  public List<AppMenuTree> loadapp(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppMenuTree.class);
    com.mz.oauth.user.model.AppUser user = ContextUtil.getCurrentUser();
    String t = request.getParameter("t");//当T等于1时,查询全部
    //if(PropertiesUtils.APP.getProperty("app.admin").equals(user.getUsername())){
    if ("1".equals(t) && !PropertiesUtils.APP.getProperty("app.admin").equals(user.getUsername())) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("userId", user.getId());
      map.put("eqtype", "eq");
      map.put("type", "root");
      List<AppMenuTree> list = ((AppMenuTreeDao) dao).findByUserId(map);

      //去掉重复
      HashSet<AppMenuTree> set = new HashSet<AppMenuTree>(list);
      ArrayList<AppMenuTree> arrayList = new ArrayList<AppMenuTree>(set);
      SortList<AppMenuTree> sortList = new SortList<AppMenuTree>();
      sortList.Sort(arrayList, "getId", null);
      return arrayList;
    } else {
      filter.addFilter("type=", "root");
      return super.find(filter);

    }
  }


  @Override
  public List<AppMenuTree> findRoleByApp(Long roleId, String appName) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("roleId", roleId);
    map.put("appName", appName);
    return ((AppMenuTreeDao) dao).findRoleByApp(map);
  }


}
