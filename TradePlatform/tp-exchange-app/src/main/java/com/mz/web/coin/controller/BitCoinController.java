package com.mz.web.coin.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.exchange.product.model.ExProduct;
import com.mz.util.StringUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.web.util.CoinInterfaceUtil;
import com.mz.exchange.remote.account.RemoteExProductService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/coin/bitCoinController")
public class BitCoinController {


  @MethodName(name = "创建一个公钥")
  @RequestMapping("/createPublick")
  @ResponseBody
  public JsonResult createPublick(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(false);

    String type = req.getParameter("type");
    String userName = req.getParameter("userName");
    String rechargeResult = CoinInterfaceUtil.create(userName, type);
    Map<String, Object> map = StringUtil.str2map(rechargeResult);
    if ("success".equals(map.get("code"))) {
      jsonResult.setSuccess(true);
      jsonResult.setMsg("创建成功");
      jsonResult.setObj(map.get("address"));
      return jsonResult;
    }
    jsonResult.setMsg("创建账户失败");
    jsonResult.setObj("wwwwwww");
    return jsonResult;
  }


  @MethodName(name = "查询某个用户的余额")
  @RequestMapping("/balanceUserName")
  @ResponseBody
  public JsonResult balanceUserName(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);
    String type = req.getParameter("type");
    String userName = req.getParameter("userName");
    String rechargeResult = CoinInterfaceUtil.balance(userName, type);
    jsonResult.setObj(rechargeResult);
    return jsonResult;
  }


  @MethodName(name = "查询某个币的所有交易记录")
  @RequestMapping("/selectList")
  @ResponseBody
  public JsonResult selectList(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);
    String type = req.getParameter("type");
    String userName = req.getParameter("userName");
    String rechargeResult = CoinInterfaceUtil.listForUser(type, userName);
    //	Map<String, Object> map = StringUtil.str2map(rechargeResult);
    jsonResult.setObj(rechargeResult);
    return jsonResult;
  }


  @MethodName(name = "查询某个币的所有交易记录")
  @RequestMapping("/sendFrom")
  @ResponseBody
  public JsonResult sendFrom(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true);
    String type = req.getParameter("type");
    String account = req.getParameter("account");
    String address = req.getParameter("address");
    String amount = req.getParameter("amount");
    // String userName = req.getParameter("userName");
    String rechargeResult = CoinInterfaceUtil.sendFrom(account, address, amount, type);
    Map<String, Object> map = StringUtil.str2map(rechargeResult);
    if ("8".equals(map.get("code"))) {
      jsonResult.setSuccess(true);
      jsonResult.setMsg("充值成功");
      return jsonResult;
    }
    if ("18".equals(map.get("code"))) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("接口未开放");
      return jsonResult;
    }
    jsonResult.setObj(rechargeResult);
    return jsonResult;
  }


  /**
   * 查询某个币的所有用户
   */
  @RequestMapping("/getAllUsers")
  @ResponseBody
  public Map<String, Object> getAllUsers(HttpServletRequest req) {
    String type = req.getParameter("type");
    if (null != type && "" != type) {
      Map<String, Object> map = CoinInterfaceUtil.getAllUsers(type);
      return map;
    }
    return null;
  }


  @MethodName(name = "查询正在发行的产品")
  @RequestMapping("/findProductSelect")
  @ResponseBody
  public List<ExProduct> findProductSelect() {
    RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil
        .getBean("remoteExProductService");
    List<ExProduct> list = remoteExProductService.findByIssueState(1);
    return list;
  }


}



