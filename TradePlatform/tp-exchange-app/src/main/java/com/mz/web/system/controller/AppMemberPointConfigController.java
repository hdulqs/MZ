/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-07-31 10:05:11
 */
package com.mz.web.system.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.spotchange.model.AppMemberPointConfig;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: java类作用描述
 * @Author: zongwei
 * @CreateDate: 2018/6/6 17:28
 * @UpdateUser: zongwei
 * @UpdateDate: 2018/6/6 17:28
 * @UpdateRemark: 创建
 * @Version: 1.0
 */
@Controller
@RequestMapping("/system/AppMemberPointConfig")
public class AppMemberPointConfigController extends
    BaseController<com.mz.spotchange.model.AppMemberPointConfig, Long> {

  @Resource(name = "appMemberPointConfigService")
  @Override
  public void setService(BaseService<com.mz.spotchange.model.AppMemberPointConfig, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看AppMemberPointConfig")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public com.mz.spotchange.model.AppMemberPointConfig see(@PathVariable Long id) {
    AppMemberPointConfig AppMemberPointConfig = service.get(id);
    return AppMemberPointConfig;
  }

  @MethodName(name = "增加AppMemberPointConfig")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request,
      AppMemberPointConfig AppMemberPointConfig) {

    QueryFilter filter = new QueryFilter(com.mz.spotchange.model.AppMemberPointConfig.class);
    List<AppMemberPointConfig> sysCodeValues = super.find(filter);
    if (sysCodeValues.isEmpty()) {
      JsonResult jsonResult = super.save(AppMemberPointConfig);
      appMemberPointConfigflushRedis
          ();
      return jsonResult;
    } else {
      JsonResult jsonResult = new JsonResult();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("配置重复");
      return jsonResult;
    }

  }

  @MethodName(name = "修改SysCodeValue")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request,
      AppMemberPointConfig AppMemberPointConfig) {
    JsonResult jsonResult = super.update(AppMemberPointConfig);
    appMemberPointConfigflushRedis();
    return jsonResult;
  }

  @MethodName(name = "删除SysCodeValue")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    JsonResult jsonResult = super.deleteBatch(ids);
    appMemberPointConfigflushRedis();
    return jsonResult;
  }

  @MethodName(name = "列表SysCodeValue")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppMemberPointConfig.class,
        request);
    PageResult pageResult = super.findPage(filter);
    return pageResult;
  }


  private void appMemberPointConfigflushRedis() {
    QueryFilter filter = new QueryFilter(AppMemberPointConfig.class);
    List<AppMemberPointConfig> list = super.find(filter);
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save("cn:AppMemberPointConfig", JSON.toJSONString(list));
  }


}
