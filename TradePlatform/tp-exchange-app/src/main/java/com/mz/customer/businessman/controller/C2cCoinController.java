/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
package com.mz.customer.businessman.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.businessman.model.C2cCoin;
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
 * Copyright: 北京互融时代软件有限公司
 *
 * @author: liushilei
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
@Controller
@RequestMapping("/businessman/c2ccoin")
public class C2cCoinController extends BaseController<C2cCoin, Long> {

  @Resource(name = "c2cCoinService")
  @Override
  public void setService(BaseService<C2cCoin, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看C2cCoin")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public C2cCoin see(@PathVariable Long id) {
    C2cCoin c2ccoin = service.get(id);
    return c2ccoin;
  }

  @MethodName(name = "增加C2cCoin")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, C2cCoin C2cCoin) {
    JsonResult save = super.save(C2cCoin);
    flushRedis();
    return save;

  }

  /**
   * 刷新c2cCoin的redis配置
   */
  private void flushRedis() {
    QueryFilter filter = new QueryFilter(C2cCoin.class);
    filter.addFilter("isOpen=", 1);
    List<C2cCoin> list = service.find(filter);
    ArrayList<String> c2cs = new ArrayList<String>();
    if (list != null && list.size() > 0) {
      for (C2cCoin cc : list) {
        c2cs.add(cc.getCoinCode());
      }
    }
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save("cn:c2clist", JSON.toJSONString(c2cs));
    redisService.save("cn:c2cCoinList", JSON.toJSONString(list));
  }

  @MethodName(name = "修改C2cCoin")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, C2cCoin C2cCoin) {
    JsonResult update = super.update(C2cCoin);
    flushRedis();
    return update;
  }

  @MethodName(name = "删除C2cCoin")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }


  @RequestMapping(value = "/settimeout")
  @ResponseBody
  public JsonResult settimeout(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String c2cTimeOut = request.getParameter("c2cTimeOut");
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save("cn:c2cTimeOut", c2cTimeOut);
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @RequestMapping(value = "/gettimeout")
  @ResponseBody
  public JsonResult gettimeout(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String c2cTimeOut = redisService.get("cn:c2cTimeOut");
    jsonResult.setObj(c2cTimeOut);
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @MethodName(name = "列表C2cCoin")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(C2cCoin.class, request);
    filter.setOrderby(" id desc");
    PageResult findPage = super.findPage(filter);
    return findPage;
  }

}
