/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-07-31 10:05:11
 */
package com.mz.web.open.controller;

import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.util.UUIDUtil;
import com.mz.web.open.model.SysOpenUser;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-07-31 10:05:11
 */
@Controller
@RequestMapping("/open/sysopenuser")
public class SysOpenUserController extends BaseController<SysOpenUser, Long> {

  @Resource(name = "sysOpenUserService")
  @Override
  public void setService(BaseService<SysOpenUser, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看SysOpenUser")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public SysOpenUser see(@PathVariable Long id) {
    SysOpenUser sysOpenUser = service.get(id);
    return sysOpenUser;
  }

  @MethodName(name = "增加SysOpenUser")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, SysOpenUser sysOpenUser) {
    if (StringUtils.isEmpty(sysOpenUser.getAppKey())) {
      sysOpenUser.setAppKey(UUIDUtil.getUUID());
    }
    if (StringUtils.isEmpty(sysOpenUser.getSecret())) {
      sysOpenUser.setSecret(UUIDUtil.getUUID());
    }
    return super.save(sysOpenUser);
  }

  @MethodName(name = "修改SysOpenUser")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, SysOpenUser sysOpenUser) {
    return super.update(sysOpenUser);
  }

  @MethodName(name = "删除SysOpenUser")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表SysOpenUser")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(SysOpenUser.class, request);
    return super.findPage(filter);
  }


}
