/**
 * Copyright:   风云科技
 *
 * @author: zongwei
 * @Date :          20180502
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
import com.mz.customer.businessman.model.OtcCoin;
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
 * Copyright:   风云科技
 * @author: zongwei
 * @Date :          20180502
 * @version: V1.0
 * @Date: 2017-12-07 14:06:38
 */
@Controller
@RequestMapping("/businessman/otccoin")
public class OtcCoinController extends BaseController<OtcCoin, Long> {

  @Resource(name = "otcCoinService")
  @Override
  public void setService(BaseService<OtcCoin, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看OtcCoin")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public OtcCoin see(@PathVariable Long id) {
    OtcCoin Otccoin = service.get(id);
    return Otccoin;
  }

  @MethodName(name = "增加OtcCoin")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, OtcCoin OtcCoin) {
    JsonResult save = super.save(OtcCoin);
    flushRedis();
    return save;

  }

  /**
   * 刷新OtcCoin的redis配置
   */
  private void flushRedis() {
    QueryFilter filter = new QueryFilter(OtcCoin.class);
    filter.addFilter("isOpen=", 1);
    List<OtcCoin> list = service.find(filter);
    ArrayList<String> Otcs = new ArrayList<String>();
    if (list != null && list.size() > 0) {
      for (OtcCoin cc : list) {
        Otcs.add(cc.getCoinCode());
      }
    }
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save("cn:Otclist", JSON.toJSONString(Otcs));
    redisService.save("cn:OtcCoinList", JSON.toJSONString(list));
  }

  @MethodName(name = "修改OtcCoin")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, OtcCoin OtcCoin) {
    JsonResult update = super.update(OtcCoin);
    flushRedis();
    return update;
  }

  @MethodName(name = "删除OtcCoin")
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
    String OtcTimeOut = request.getParameter("OtcTimeOut");
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save("cn:OtcTimeOut", OtcTimeOut);
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @RequestMapping(value = "/gettimeout")
  @ResponseBody
  public JsonResult gettimeout(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    String OtcTimeOut = redisService.get("cn:OtcTimeOut");
    jsonResult.setObj(OtcTimeOut);
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @MethodName(name = "列表OtcCoin")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(OtcCoin.class, request);
    filter.setOrderby(" id desc");
    PageResult findPage = super.findPage(filter);
    return findPage;
  }

}
