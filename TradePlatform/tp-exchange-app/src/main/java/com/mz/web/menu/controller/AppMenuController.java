/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2015年10月13日 上午10:26:28
 */
package com.mz.web.menu.controller;

import com.mz.core.annotation.NoLogin;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.oauth.remote.user.RemoteAppUserService;
import com.mz.oauth.user.model.AppUser;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.web.menu.model.AppMenu;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.web.menu.service.AppMenuService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/menu/appmenu")
public class AppMenuController extends BaseController<AppMenu, Long> {

  @Resource(name = "appMenuService")
  @Override
  public void setService(BaseService<AppMenu, Long> service) {
    super.service = service;
  }

  @MethodName(name = "加载系统左边菜单")
  @RequestMapping("/loadSystemMenu")
  @ResponseBody
  public List<AppMenu> loadSystemMenu(HttpServletRequest request) {
    return ((AppMenuService) service).findSystemMenu(request);
  }

  @MethodName(name = "菜单管理功能,查出一颗菜单树")
  @RequestMapping("/listTree")
  @ResponseBody
  public List<AppMenu> listTree(HttpServletRequest request) {
    return ((AppMenuService) service).listTree(request);
  }

  @MethodName(name = "设置菜单是否可见")
  @RequestMapping("/setVisible")
  @ResponseBody
  public JsonResult setVisible(Long id) {
    return ((AppMenuService) service).setVisible(id);
  }

  @MethodName(name = "查出所有type为menu的菜单")
  @RequestMapping("/findMenu")
  @ResponseBody
  public PageResult findMenu(HttpServletRequest request) {
    QueryFilter queryFilter = new QueryFilter(AppMenu.class, request);
    queryFilter.addFilter("type=", "menu");
    String custTreeKeys = request.getParameter("custTreeKeys");
    if (!StringUtils.isEmpty(custTreeKeys)) {
      queryFilter.addFilter("mkey_NOTIN", custTreeKeys);
    }

    return super.findPage(queryFilter);

  }


  @MethodName(name = "测试testDubbo")
  @RequestMapping("/testDubbo")
  @ResponseBody
  @NoLogin
  public JsonResult testDubbo() {
    RemoteAppUserService remoteAppUserService = (RemoteAppUserService) ContextUtil
        .getBean("remoteAppUserService");
    List<AppUser> find = remoteAppUserService.find();
    JsonResult jsonResult = new JsonResult();
    jsonResult.setObj(find);
    return jsonResult;
  }


  /*
   * @MethodName(name="创建菜单备份")
   *
   * @RequestMapping("/createStandby") public ModelAndView createStandby(){
   *
   * boolean flag = ((AppMenuService)service).createStandby(); return
   * ModelAndView(REDIRECT+"/listView.do", DEFAULT_VIEW_PATH_TRUE, null);
   *
   * }
   */

  /*
   * @MethodName(name="删除菜单备份")
   *
   * @RequestMapping("/deleteStandby") public ModelAndView
   * deleteStandby(String fileName){
   *
   * if(StringUtil.isNull(fileName)){ URL url = this.getClass()
   * .getClassLoader().getResource(""); String standbyPath =
   * url.getFile()+FileUtil.standbyPath;
   * FileUtil.deleteFile(standbyPath+"/"+fileName); } return
   * ModelAndView(REDIRECT+"/listView.do", DEFAULT_VIEW_PATH_TRUE, null);
   *
   * }
   */

  /*
   * @MethodName(name="还原菜单备份")
   *
   * @RequestMapping("/restoreStandby") public ModelAndView
   * restoreStandby(String fileName){
   *
   * boolean flag = ((AppMenuService)service).restoreStandby(fileName); return
   * ModelAndView(REDIRECT+"/listView.do", DEFAULT_VIEW_PATH_TRUE, null);
   *
   * }
   */

}
