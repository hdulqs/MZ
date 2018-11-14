package com.mz.front.coin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mq.service.MessageProducer;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import com.mz.front.annotation.ThirdInterFaceLog;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteInterfaceCallbackService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.LmcTransfer;
import com.mz.manage.remote.model.RemoteResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("coinController")
@RequestMapping("/market")
public class CoinController {

  @Resource
  private RemoteInterfaceCallbackService remoteInterfaceCallbackService;
  @Resource
  private RemoteAppTransactionManageService remoteAppTransactionManageService;

  /**
   * 注册类型属性编辑器
   */
  @InitBinder
  public void initBinder(ServletRequestDataBinder binder) {

    // 系统注入的只能是基本类型，如int，char，String

    /**
     * 自动转换日期类型的字段格式
     */
    binder.registerCustomEditor(Date.class, new DateTimePropertyEditorSupport());

    /**
     * 防止XSS攻击，并且带去左右空格功能
     */
    binder.registerCustomEditor(String.class, new StringPropertyEditorSupport(true, false));
  }


  @RequestMapping("findAllCoin")
  @ResponseBody
  public List<Coin> findAllCoin() {
    List<Coin> list = new ArrayList<Coin>();
    try {
      RemoteManageService manageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      RemoteResult result = manageService.finaCoins();
      if (result.getSuccess()) {
        return (List<Coin>) result.getObj();
      }
    } catch (Exception e) {
    }
    return list;
  }

  /**
   * 邻萌宝钱包-钱包转币接口(提币) 测试地址,正式环境删除.
   * <p> TODO</p>
   *
   * @author: Shangxl
   * @param: @param request
   * @param: @param transfer
   * @param: @return
   * @return: JsonResult
   * @Date :          2017年7月31日 下午3:06:08
   * @throws:
   */
  @RequestMapping("/lmcTransfer")
  @ResponseBody
  public JsonResult lmcTransfer(HttpServletRequest request, LmcTransfer transfer) {
    JsonResult jsonResult = new JsonResult();
    //业务
    transfer.setTransfer_type("I");
    transfer.setSymbol("ETH");
    transfer.setType("-1");
    transfer.setCoins("2");
    transfer.setFrom("0xc20d7efdfa46ca772aca7a55e30310c2688b097a");
    transfer.setTo("0x4c5AcDd0EeD1E02ecDc696dF3B70d8111ddC09F4");
    transfer.setTransfer_id("t_2011234432");
    transfer.setValidation_type("md5");
    transfer.setValidation_code("com.mz");
    transfer.setValidation_phone("15313152752");

    String[] rs = remoteAppTransactionManageService.lmcTransfer(transfer);
    jsonResult.setCode(rs[0]);
    jsonResult.setMsg(rs[1]);
    jsonResult.setObj(rs[2]);

    System.out.println("调用结束");
    return jsonResult;
  }

  /**
   * 转账回调接口(充币通知)
   * <p> TODO</p>
   *
   * @author: Shangxl
   * @param: @param request
   * @param: @param transfer
   * @param: @return
   * @return: JSONObject
   * @Date :          2017年7月28日 下午7:00:19
   * @throws:
   */
  @RequestMapping(value = "/transferCallBack", method = RequestMethod.GET)
  @ResponseBody
  @ThirdInterFaceLog
  public String transferCallBack(LmcTransfer transfer) {
    //记录调用日志
    try {

      System.out.println("@@@send chongbi rabbitmq message=======" + JSON.toJSONString(transfer));
      MessageProducer messageProducer = SpringContextUtil
          .getBean(MessageProducer.class);
      messageProducer.sendChongbiCallBack(JSON.toJSONString(transfer));

      JSONObject result = new JSONObject();
      result.put("code", "200");
      result.put("data", "true");
      result.put("msg", "success");
      return result.toJSONString();

      //return remoteInterfaceCallbackService.lmcTransferCallBack(transfer);
    } catch (Exception e) {
      System.out.println("服务器错误");
      e.printStackTrace();
      JSONObject result = new JSONObject();
      result.put("code", "500");
      result.put("data", "false");
      result.put("msg", "internal server error");
      return result.toJSONString();
    }
  }

  /**
   * LMC 查询账单总和
   * <p> TODO</p>
   *
   * @author: Shangxl
   * @param: @param request
   * @param: @param transfer
   * @param: @return
   * @return: JsonResult
   * @Date :          2017年7月31日 下午6:02:18
   * @throws:
   */
  @RequestMapping("/walletTransferSum")
  @ResponseBody
  public JsonResult walletTransferSum(HttpServletRequest request, LmcTransfer transfer) {
    JsonResult jsonResult = new JsonResult();
    String[] rs = remoteAppTransactionManageService.walletTransferSum(transfer);
    jsonResult.setCode(rs[0]);
    jsonResult.setObj(rs[1]);
    jsonResult.setMsg(rs[2]);
    return jsonResult;
  }


  /**
   * LMC 获取账单列表
   * <p> TODO</p>
   *
   * @author: Shangxl
   * @param: @param request
   * @param: @param transfer
   * @param: @return
   * @return: JsonResult
   * @Date :          2017年7月31日 下午6:02:18
   * @throws:
   */
  @RequestMapping("/listwalletTransfer")
  @ResponseBody
  public JsonResult listwalletTransfer(HttpServletRequest request, LmcTransfer transfer) {
    JsonResult jsonResult = new JsonResult();
    String[] rs = remoteAppTransactionManageService.listwalletTransfer(transfer);
    jsonResult.setCode(rs[0]);
    jsonResult.setObj(rs[1]);
    jsonResult.setMsg(rs[2]);
    return jsonResult;
  }

}
