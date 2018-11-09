package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.AppTransactionManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.sys.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

;

@Controller
@RequestMapping("/mobile/user/apprmbdeposit")
@Api(value = "App操作类", description = "人民币充值")
public class AppRmbdepositController {

  @RequestMapping(value = "/selectRedisBank", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "人民币充值银行查询，充值手续费率，最小充值金额", httpMethod = "POST", response = JsonResult.class, notes = "人民币充值银行查询,充值手续费率rechargeFeeRate,最小充值金额minRechargeMoney")
  @ResponseBody
  public JsonResult selectRedisBank(HttpServletRequest request) {
    JsonResult js = new JsonResult();
    HashMap<String, Object> map = new HashMap<String, Object>();
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        String key = redisService.get("DIC:bank");
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);
        map.put("rechargeFeeRate", parseObject.get("rechargeFeeRate"));
        map.put("minRechargeMoney", parseObject.get("minRechargeMoney"));
        map.put("key", key);
        return js.setSuccess(true).setObj(map);
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

  @RequestMapping(value = "/rmbdeposit")
  @ApiOperation(value = "生成充值汇款单", httpMethod = "POST", response = JsonResult.class, notes =
      "surname汇款人姓，remitter汇款人名,bankCode银行卡号，bankAmount充值金额"
          + "，bankName汇款银行")
  @ResponseBody
  public JsonResult rmbdeposit(HttpServletRequest request) {
    String surname = request.getParameter("surname");
    String remitter = request.getParameter("remitter");
    String bankCode = request.getParameter("bankCode");
    String bankAmount = request.getParameter("bankAmount");
    String bankName = request.getParameter("bankName");

    String tokenId = request.getParameter("tokenId");
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = JSONObject.parseObject(value).getString("mobile");
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        AppTransactionManage appTransaction = new AppTransactionManage();
        RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil
            .getBean("remoteAppTransactionManageService");
        RemoteResult remoteResult = remoteAppTransactionManageService
            .generateRmbdeposit(surname, remitter, bankCode, bankAmount, bankName, appTransaction,
                user);
        return new JsonResult().setSuccess(true).setObj(remoteResult.getObj());
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }

}
