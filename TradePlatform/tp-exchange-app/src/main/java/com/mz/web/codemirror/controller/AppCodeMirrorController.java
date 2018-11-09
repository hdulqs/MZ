/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: menw
 * @version: V1.0
 * @Date: 2017-07-13 18:27:13
 */
package com.mz.web.codemirror.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.web.codemirror.model.AppCodeMirror;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author: menw
 * @version: V1.0
 * @Date: 2017-07-13 18:27:13
 */
@Controller
@RequestMapping("/codemirror/appcodemirror")
public class AppCodeMirrorController extends BaseController<AppCodeMirror, Long> {

  @Resource(name = "appCodeMirrorService")

  @Override
  public void setService(BaseService<AppCodeMirror, Long> service) {
    super.service = service;
  }

  StringBuffer s = new StringBuffer();

  @MethodName(name = "查看AppCodeMirror")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public AppCodeMirror see(@PathVariable Long id) {
    AppCodeMirror appCodeMirror = service.get(id);
    return appCodeMirror;
  }


  @MethodName(name = "增加AppCodeMirror")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, AppCodeMirror appCodeMirror) {
    String remark = request.getParameter("superiorMenuId");
    //清空
    s = new StringBuffer("");
    Long pid = appCodeMirror.getPid();
    String namea = appCodeMirror.getName();
    QueryFilter filter = new QueryFilter(AppCodeMirror.class, request);
    filter.addFilter("pid=", pid);
    furl(pid, namea, request);
    s.append("" + namea);
    appCodeMirror.setFurl(s.toString());
    appCodeMirror.setRemark(remark);
    String ss = s.toString();
    String replaceAll = ss.replaceAll("/", ":");
    String key = "FrontFtl:" + replaceAll;
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String jsonString = JSON.toJSONString(appCodeMirror.getContent());
    redisService.save(key, jsonString);
    return super.save(appCodeMirror);

  }

  //递归文本路径
  public String furl(Long pid, String namea, HttpServletRequest request) {
    String nam = "";
    QueryFilter filter = new QueryFilter(AppCodeMirror.class, request);
    filter.addFilter("id=", pid);
    if (pid == null) {
      return null;
    } else {

      List<AppCodeMirror> find = service.find(filter);
      Long pid2 = find.get(0).getPid();
      String name = find.get(0).getName();
      s.insert(0, name + "/");
      return furl(pid2, nam, request);
    }

  }

  @MethodName(name = "删除AppCodeMirror")
  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(String id) {
    QueryFilter filter = new QueryFilter(AppCodeMirror.class);
    filter.addFilter("Id=", id);
    List<AppCodeMirror> find = service.find(filter);
    String furl = find.get(0).getFurl();
    String replaceAll = furl.replaceAll("/", ":");
    String key = "FrontFtl:" + replaceAll;
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.delete(key);
    return super.deleteBatch(id);
  }

  @MethodName(name = "修改AppCodeMirror")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, AppCodeMirror appCodeMirror) {
    Long id = appCodeMirror.getId();
    QueryFilter filter = new QueryFilter(AppCodeMirror.class, request);
    filter.addFilter("Id=", id);
    List<AppCodeMirror> find = service.find(filter);
    String furl = find.get(0).getFurl();
    String replaceAll = furl.replaceAll("/", ":");
    String key = "FrontFtl:" + replaceAll;
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save(key, appCodeMirror.getContent());
    return super.update(appCodeMirror);
  }

  @MethodName(name = "列表AppCodeMirror")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppCodeMirror.class, request);
    return super.findPage(filter);
  }


  @MethodName(name = "查询一个应用菜单树")
  @RequestMapping("/findByApp")
  @ResponseBody
  public List<AppCodeMirror> findByApp(HttpServletRequest request) {
    String id = request.getParameter("id");
    QueryFilter filter = new QueryFilter(AppCodeMirror.class, request);
    filter.setOrderby("orderNo asc");
    return service.find(filter);
  }


  @MethodName(name = "查询一个应用菜单树")
  @RequestMapping("/findById")
  @ResponseBody
  public List<AppCodeMirror> findById(HttpServletRequest request) {
    String id = request.getParameter("id");
    QueryFilter filter = new QueryFilter(AppCodeMirror.class, request);
    filter.addFilter("Id=", id);
    List<AppCodeMirror> find = service.find(filter);
    String content = service.find(filter).get(0).getContent();
    String htmlUnescape = HtmlUtils.htmlUnescape(content);
    AppCodeMirror AppCodeMirror = new AppCodeMirror();
    AppCodeMirror.setContent(htmlUnescape);
    AppCodeMirror.setRemark(find.get(0).getRemark());
    find.set(0, AppCodeMirror);
    return find;
  }
}
