/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-11-13 19:17:02
 */
package com.mz.web.ExDmTransfColdDetail.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.StringUtil;
import com.mz.util.http.HttpConnectionUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.security.Check;
import com.mz.web.ExDmTransfColdDetail.model.ExDmTransfColdDetail;
import com.mz.core.mvc.controller.base.BaseController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright:   北京互融时代软件有限公司
 * @author: shangxl
 * @version: V1.0
 * @Date: 2017-11-13 19:17:02
 */
@Controller
@RequestMapping("/exDmTransfColdDetail/exdmtransfcolddetail")
public class ExDmTransfColdDetailController extends BaseController<ExDmTransfColdDetail, Long> {

  @Autowired
  private RedisService redisService;

  @Resource(name = "exDmTransfColdDetailService")
  @Override
  public void setService(BaseService<ExDmTransfColdDetail, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查看ExDmTransfColdDetail")
  @RequestMapping(value = "/see/{id}")
  @MyRequiresPermissions
  @ResponseBody
  public ExDmTransfColdDetail see(@PathVariable Long id) {
    ExDmTransfColdDetail exDmTransfColdDetail = service.get(id);
    return exDmTransfColdDetail;
  }

  @MethodName(name = "增加ExDmTransfColdDetail")
  @RequestMapping(value = "/add")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult add(HttpServletRequest request, ExDmTransfColdDetail exDmTransfColdDetail) {
    return super.save(exDmTransfColdDetail);
  }

  @MethodName(name = "修改ExDmTransfColdDetail")
  @RequestMapping(value = "/modify")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult modify(HttpServletRequest request, ExDmTransfColdDetail exDmTransfColdDetail) {
    return super.update(exDmTransfColdDetail);
  }

  @MethodName(name = "删除ExDmTransfColdDetail")
  @RequestMapping(value = "/remove/{ids}")
  @MyRequiresPermissions
  @ResponseBody
  public JsonResult remove(@PathVariable String ids) {
    return super.deleteBatch(ids);
  }

  @MethodName(name = "列表ExDmTransfColdDetail")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(ExDmTransfColdDetail.class, request);
    return super.findPage(filter);
  }


  @MethodName(name = "查询钱包余额列表")
  @RequestMapping("/listWalletBalance")
  @ResponseBody
  public PageResult listWalletBalance(HttpServletRequest request) {
    String url = PropertiesUtils.APP.getProperty("app.coinip");
    url = url + "/coin/coin/listWalletBalance";
    String result = HttpConnectionUtil.getSend(url, null);
    List<Object> list = new ArrayList<>();
    if (StringUtils.isNotEmpty(result)) {
      list = JSON.parseArray(result, Object.class);
    }
    PageResult pageResult = new PageResult();
    pageResult.setDraw(1);
    pageResult.setPage(1);
    pageResult.setTotalPage(1L);
    pageResult.setRecordsTotal(Long.valueOf(list.size()));
    pageResult.setRecordsFiltered(Long.valueOf(list.size()));
    pageResult.setRows(list);
    return pageResult;
  }


  @MethodName(name = "toColdAccount")
  @RequestMapping(value = "/toColdAccount", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult toColdAccount(HttpServletRequest request) {
    JsonResult jr = new JsonResult();
    String result = null;
    String type = request.getParameter("type");
    String amount = request.getParameter("amount");
    if (StringUtil.isNull(type) && StringUtil.isNull(amount)) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("type", type);
      map.put("amount", amount);
      String param = StringUtil.string2params(map);
      String url = PropertiesUtils.APP.getProperty("app.coinip");
      url = url + "/coin/coin/toColdAccount";
      try {
        result = HttpConnectionUtil.getSend(url, param);
      } catch (Exception e) {
        jr.setMsg("后台异常");
      }
      if (StringUtils.isNotEmpty(result)) {
        jr = JSON.parseObject(result, JsonResult.class);
        if (jr.getSuccess()) {
          Map<String, String> data = (Map<String, String>) jr.getObj();
          String fromAddress = "";
          String toAddress = "";
          String txHash = "";
          if (data.get("fromAddress") != null) {
            fromAddress = data.get("fromAddress").toString();
          }
          if (data.get("toAddress") != null) {
            toAddress = data.get("toAddress").toString();
          }
          if (data.get("txHash") != null) {
            txHash = data.get("txHash").toString();
          }
          String money = amount;
          ExDmTransfColdDetail coldDetail = new ExDmTransfColdDetail();
          coldDetail.setCoinCode(type);
          coldDetail.setFromAddress(fromAddress);
          coldDetail.setToAddress(toAddress);
          coldDetail.setAmount(new BigDecimal(money));
          coldDetail.setTx(txHash);
          coldDetail.setOperator("admin");
          JsonResult saveResult = this.save(coldDetail);
          if (!saveResult.getSuccess()) {
            jr.setMsg("明细保存失败！");
            jr.setSuccess(false);
          }
        }
      }
    }
    return jr;
  }

  @MethodName(name = "查询代币资产")
  @RequestMapping("/listTokenAssets")
  @ResponseBody
  public PageResult listTokenAssets(HttpServletRequest request) {
    String coinType = request.getParameter("coinType");
    PageResult pageResult = new PageResult();
    List<Object> list = new ArrayList<>();
    String draw = request.getParameter("draw");
    pageResult.setDraw(Integer.valueOf(draw));
    pageResult.setPage(1);
    pageResult.setTotalPage(1L);
    if (StringUtils.isNotEmpty(coinType)) {
      String url = PropertiesUtils.APP.getProperty("app.coinip");
      url = url + "/coin/coin/listTokenAddressAssets";
      Map<String, String> map = new HashMap<String, String>();
      map.put("coinType", coinType);
      String param = StringUtil.string2params(map);
      String result = HttpConnectionUtil.postSend(url, param);
      if (StringUtils.isNotEmpty(result)) {
        JsonResult data = JSON.parseObject(result, JsonResult.class);
        if (data.getSuccess() && data.getObj() != null && StringUtils
            .isNotEmpty(data.getObj().toString())) {
          list = JSON.parseArray(data.getObj().toString(), Object.class);
        }
      }
    }
    pageResult.setRecordsTotal(Long.valueOf(list.size()));
    pageResult.setRecordsFiltered(Long.valueOf(list.size()));
    pageResult.setRows(list);
    return pageResult;
  }

  @MethodName(name = "充值旷工费")
  @RequestMapping(value = "/rechargeTxFee2TokenAddress", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult rechargeTxFee2TokenAddress(HttpServletRequest request) {
    JsonResult result = new JsonResult();
    String address = request.getParameter("address");
    String amount = request.getParameter("amount");
    String coinType = request.getParameter("coinType");
    String url = PropertiesUtils.APP.getProperty("app.coinip");
    url = url + "/coin/coin/rechargeTxFee2TokenAddress";
    Map<String, String> map = new HashMap<String, String>();
    String params[] = {address, amount, coinType};
    String authcode = Check.authCode(params);
    map.put("address", address);
    map.put("amount", amount);
    map.put("coinType", coinType);
    map.put("authcode", authcode);
    String param = StringUtil.string2params(map);
    String data = HttpConnectionUtil.postSend(url, param);
    if (StringUtils.isNotEmpty(data)) {
      result = JSON.parseObject(data, JsonResult.class);
      if (result.getSuccess()) {
        result.setMsg("充值成功");
      }
    } else {
      result.setMsg("网络错误");
    }
    return result;
  }

  @MethodName(name = "代币归集")
  @RequestMapping(value = "/tokenCollect", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult tokenCollect(HttpServletRequest request) {
    JsonResult result = new JsonResult();
    String coinType = request.getParameter("coinType");
    String address = request.getParameter("address");
    String amount = request.getParameter("amount");
    String url = PropertiesUtils.APP.getProperty("app.coinip");
    url = url + "/coin/coin/tokenCollect";
    Map<String, String> map = new HashMap<String, String>();
    map.put("coinType", coinType);
    map.put("address", address);
    map.put("amount", amount);
    String param = StringUtil.string2params(map);
    String data = HttpConnectionUtil.postSend(url, param);
    result = JSON.parseObject(data, JsonResult.class);
    if (result.getSuccess()) {
      result.setMsg("归集成功");
    }
    return result;
  }

}
