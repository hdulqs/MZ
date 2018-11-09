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
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.ArrayList;
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
@RequestMapping("/system/SysCodeValue")
public class SysCodeValueController extends
    BaseController<com.mz.spotchange.model.SysCodeValue, Long> {

  @Resource(name = "sysCodeValueService")
  @Override
  public void setService(BaseService<com.mz.spotchange.model.SysCodeValue, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看SysCodeValue")
  @RequestMapping(value = "/see/{code_id}")
  @MyRequiresPermissions
  @ResponseBody
  public com.mz.spotchange.model.SysCodeValue see(@PathVariable Long code_id) {
    com.mz.spotchange.model.SysCodeValue SysCodeValue = service.get(code_id);
    return SysCodeValue;
  }

  @MethodName(name = "增加SysCodeValue")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, com.mz.spotchange.model.SysCodeValue SysCodeValue) {

    QueryFilter filter = new QueryFilter(com.mz.spotchange.model.SysCodeValue.class);
    filter.addFilter("code=", SysCodeValue.getCode());
    List<com.mz.spotchange.model.SysCodeValue> sysCodeValue = super.find(filter);
    if (sysCodeValue.isEmpty()) {
      JsonResult jsonResult = super.save(SysCodeValue);
      sysCodeValueflushRedis
          ();
      return jsonResult;
    } else {
      JsonResult jsonResult = new JsonResult();
      jsonResult.setSuccess(false);
      jsonResult.setMsg("代码重复");
      return jsonResult;
    }

  }

  @MethodName(name = "修改SysCodeValue")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, com.mz.spotchange.model.SysCodeValue SysCodeValue) {
    JsonResult jsonResult = super.update(SysCodeValue);
    sysCodeValueflushRedis();
    return jsonResult;
  }

  @MethodName(name = "删除SysCodeValue")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    JsonResult jsonResult = super.deleteBatch(ids);
    sysCodeValueflushRedis();
    return jsonResult;
  }

  @MethodName(name = "列表SysCodeValue")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(com.mz.spotchange.model.SysCodeValue.class, request);
    PageResult pageResult = super.findPage(filter);
    return pageResult;
  }


  /**
   * 刷新OtcCoin的redis配置
   */
  private void sysCodeValueflushRedis() {
    QueryFilter filter = new QueryFilter(com.mz.spotchange.model.SysCodeValue.class);
    filter.addFilter("enable_flag=", "Y");
    List<com.mz.spotchange.model.SysCodeValue> list = super.find(filter);
    ArrayList<String> sysCodeValue = new ArrayList<String>();
    if (list != null && list.size() > 0) {
      for (com.mz.spotchange.model.SysCodeValue cc : list) {
        sysCodeValue.add(cc.getCode());
      }
    }
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save("cn:SysCodelist", JSON.toJSONString(sysCodeValue));
    redisService.save("cn:SysCodeValueList", JSON.toJSONString(list));
  }


}
