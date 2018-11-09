/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月19日 下午4:21:27
 */
package com.mz.customer.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.account.fund.model.AppAccount;
import com.mz.account.remote.RemoteAppAccountService;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.customer.agents.model.AppAgentsCustromer;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.customer.user.model.AppCustomer;
import com.mz.customer.user.model.AppCustomerSimple;
import com.mz.exchange.account.model.ExApiApply;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.product.model.ExProduct;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisTradeService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.remote.RemoteThirdInterfaceService;
import com.mz.shiro.PasswordHelper;
import com.mz.util.QueryFilter;
import com.mz.util.RemoteQueryFilter;
import com.mz.util.StringUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.annotation.AppManageLogsAop;
import com.mz.change.remote.account.RemoteExApiApplyService;
import com.mz.core.mvc.controller.base.BaseController;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.customer.remote.RemoteAppAgentsService;
import com.mz.customer.remote.RemoteAppCustomerService;
import com.mz.customer.remote.RemoteAppPersonInfoService;
import com.mz.customer.user.service.AppCustomerService;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.exchange.product.service.ExProductService;
import com.mz.exchange.remote.account.RemoteExProductService;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.trade.redis.model.EntrustByUser;
import com.mz.trade.redis.model.EntrustTrade;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2016年3月19日 下午4:21:27
 */
@Controller
@RequestMapping("/user/appcustomer")
public class AppCustomerController extends BaseController<AppCustomer, Long> {

  public static final String CHINA = "0086";

  @Resource
  private AppPersonInfoService appPersonInfoService;
  @Resource
  private RemoteThirdInterfaceService remoteThirdInterfaceService;

  @Resource(name = "appCustomerService")
  @Override
  public void setService(BaseService<AppCustomer, Long> service) {
    super.service = service;
  }

  @MethodName(name = "查询客户登录list")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
    redisService.save("a1", "a1");
    String a1 = redisService.get("a1");
    System.out.println(a1);

    RedisTradeService redisTradeService = (RedisTradeService) ContextUtil
        .getBean("redisTradeService");
    redisTradeService.save("abcdefg1", "aa2");
    redisTradeService.save("abcdefg2", "aa2");
    Set<String> keys = redisTradeService.keys("abcdefg");

    QueryFilter filter = new QueryFilter(AppCustomer.class, request);
    PageResult findPageBySql = ((AppCustomerService) service).findPageBySql(filter);
    return findPageBySql;
  }

  @MethodName(name = "查询客户姓名和ID")
  @RequestMapping("/findAll")
  @ResponseBody
  public List<AppCustomerSimple> findAll(HttpServletRequest request) {
    return ((AppCustomerService) service).findAppCustomerSimple(request);
  }


  @MethodName(name = "查看")
  @RequestMapping(value = "/see/{id}", method = RequestMethod.GET)
  @ResponseBody
  @MyRequiresPermissions
  public AppCustomer see(@PathVariable Long id) {
    List<AppCustomer> appCustomer = ((AppCustomerService) service).findById(id);
    if (appCustomer.size() > 0) {
      return appCustomer.get(0);
    }
    return null;
  }

  @MethodName(name = "禁用一个用户")
  @RequestMapping(value = "/forbidden/{id}", method = RequestMethod.GET)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult forbidden(@PathVariable Long id) {
    AppCustomerService appCustomerService = (AppCustomerService) service;
    JsonResult result = appCustomerService.storpCustomer(id, true);
    return result;
  }

  @MethodName(name = "锁定某个用户")
  @RequestMapping(value = "/lock", method = RequestMethod.GET)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult lock(int time, Long id) {

    Date date = this.getDate(time);
    AppCustomerService appCustomerService = (AppCustomerService) service;
    JsonResult result = appCustomerService.lockCustomer(date, id, true);
    return result;
  }

  @MethodName(name = "解除禁用一个用户")
  @RequestMapping(value = "/permissible/{id}", method = RequestMethod.GET)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult permissible(@PathVariable Long id) {
    AppCustomerService appCustomerService = (AppCustomerService) service;
    JsonResult result = appCustomerService.storpCustomer(id, false);
    return result;
  }

  @MethodName(name = "解除锁定某个用户")
  @RequestMapping(value = "/unlock/{id}", method = RequestMethod.GET)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult unlock(@PathVariable Long id) {

    AppCustomerService appCustomerService = (AppCustomerService) service;
    JsonResult result = appCustomerService.lockCustomer(null, id, false);
    return result;
  }

  @MethodName(name = "开启交易")
  @RequestMapping(value = "/open/{id}")
  @ResponseBody
  public JsonResult open(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    if (appCustomer != null) {
      appCustomer.setIsChange(Integer.valueOf(0));
      if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + appCustomer.getUuid());
        if (value != null) {
          appCustomer.setCustomerId(id);
          service.updateNull(appCustomer);
          JSONObject jsonv = JSON.parseObject(value);
          User user = (User) JSON
              .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")), User.class);
          if (user != null) {
            redisService.save("mobile:" + user.getUuid(),
                "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(appCustomer)
                    .toString() + "}", 1800);
          }
          jsonResult.setSuccess(true);
        } else {
          service.updateNull(appCustomer);
        }
      } else {
        service.updateNull(appCustomer);
      }
      return jsonResult;
    } else {
      jsonResult.setMsg("不存在该用户");
    }
    return jsonResult;
  }

  @MethodName(name = "关闭交易")
  @RequestMapping(value = "/close/{id}")
  @ResponseBody
  public JsonResult close(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    if (appCustomer != null) {
      appCustomer.setIsChange(Integer.valueOf(1));
      if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + appCustomer.getUuid());
        if (value != null) {
          appCustomer.setCustomerId(id);
          service.updateNull(appCustomer);
          JSONObject jsonv = JSON.parseObject(value);
          User user = (User) JSON
              .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")), User.class);
          if (user != null) {
            redisService.save("mobile:" + user.getUuid(),
                "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(appCustomer)
                    .toString() + "}", 1800);
          }
          jsonResult.setSuccess(true);
        }
      }
      return jsonResult;
    } else {
      jsonResult.setMsg("ID为空");
    }
    return jsonResult;
  }

  @MethodName(name = "设置用户的角色")
  @RequestMapping(value = "/setCustomerType/{id}")
  @ResponseBody
  @MyRequiresPermissions(value = {"/setCustomerType_zhsz"})
  @AppManageLogsAop(name = "setCustomerType", remark = "设置用户的角色")
  public JsonResult setCustomerType(@PathVariable Long id, Integer customerType) {
    JsonResult result = new JsonResult();
    try {
      QueryFilter filter = new QueryFilter(AppPersonInfo.class);
      filter.addFilter("customerId=", id);
      AppPersonInfo appPersonInfo = appPersonInfoService.get(filter);
      appPersonInfo.setCustomerType(customerType);
      appPersonInfoService.update(appPersonInfo);
    } catch (Exception e) {
      result.setSuccess(false);
    }
    result.setSuccess(true);
    return result;
  }


  @MethodName(name = "重置用户密码")
  @RequestMapping(value = "/setpw", method = RequestMethod.POST)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult setpw(Long id, String password) {
    JsonResult result = new JsonResult();
    try {
      AppCustomer appCustomer = service.get(id);
      PasswordHelper passwordHelper = new PasswordHelper();
      String encryString = passwordHelper.encryString(password, appCustomer.getSalt());
      appCustomer.setPassWord(encryString);
      service.update(appCustomer);
    } catch (Exception e) {
      result.setSuccess(false);
    }
    result.setSuccess(true);
    return result;
  }

  @MethodName(name = "修改提现审核额度")
  @RequestMapping(value = "/setWithdrawCheckMoney", method = RequestMethod.POST)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult setWithdrawCheckMoney(Long id, BigDecimal money) {
    JsonResult result = new JsonResult();
    //先判断money的值
    if (money != null && (money.compareTo(new BigDecimal("0")) >= 0 || "-1"
        .equals(money.toString()))) {
      QueryFilter filter = new QueryFilter(AppPersonInfo.class);
      filter.addFilter("customerId=", id);
      AppPersonInfo appPersonInfo = appPersonInfoService.get(filter);
      appPersonInfo.setWithdrawCheckMoney(money);
      appPersonInfoService.update(appPersonInfo);
      result.setSuccess(true);
    } else {
      result.setSuccess(false);
      result.setMsg("填写金额格式有误！");
    }
    return result;
  }

  @MethodName(name = "手动添加会员信息")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult add(AppCustomer appCustomer, HttpServletRequest request) {

    String trueName = request.getParameter("trueName");
    String cardId = request.getParameter("cardId");
    String cardType = request.getParameter("cardType");
    String country = request.getParameter("country");
    if (!StringUtil.isNull(country)) {//默认国家0086（中国）
      country = CHINA;
    }
    JsonResult result = new JsonResult();
    try {
      RemoteAppCustomerService remoteAppCustomerService = (RemoteAppCustomerService) ContextUtil
          .getBean("remoteAppCustomerService");
      JsonResult regist = remoteAppCustomerService.regist(appCustomer);
      AppCustomer _appCustomer = (AppCustomer) regist.getObj();
      LogFactory.info("我是手动添加的会员" + appCustomer.getUserName());

      RemoteAppPersonInfoService remoteAppPersonInfoService = (RemoteAppPersonInfoService) ContextUtil
          .getBean("remoteAppPersonInfoService");
      AppPersonInfo _appPersonInfo = (AppPersonInfo) _appCustomer.getAppPersonInfo();
      _appPersonInfo.setCardType(Integer.valueOf(cardType));
      _appPersonInfo.setCardId(cardId);
      _appPersonInfo.setTrueName(trueName);
      _appPersonInfo.setCountry(country);
      _appPersonInfo.setRealTime(new Date());
      remoteAppPersonInfoService.update(_appPersonInfo);

      // 开通人民币账户
      RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil
          .getBean("remoteAppAccountService");
      remoteAppAccountService
          .openAccount(_appCustomer, _appPersonInfo, ContextUtil.getCurrencyType(),
              ContextUtil.getWebsite());

      //开通虚拟账户
      RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil
          .getBean("remoteExProductService");
      remoteExProductService.openDmAccount(_appCustomer, _appPersonInfo, null, "cn", "cny");

      // 开通人民币账户、币账户之后，进行实名设置
      _appCustomer.setIsReal(1);
      remoteAppCustomerService.update(_appCustomer);
      return regist;
    } catch (Exception e) {
      e.printStackTrace();
      result.setSuccess(false);
    }
    result.setSuccess(true);
    return result;
  }


  public Date getDate(int i) {
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, i);
    Date date2 = cal.getTime();
    return date2;

  }

  @MethodName(name = "验证身份证号是否存在")
  @RequestMapping(value = "/checkCard", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult checkCard(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    String id = req.getParameter("id");
    String cardId = req.getParameter("cardId");
    try {
      RemoteAppPersonInfoService remoteAppPersonInfoService = (RemoteAppPersonInfoService) ContextUtil
          .getBean("remoteAppPersonInfoService");

      RemoteQueryFilter filter = new RemoteQueryFilter(AppPersonInfo.class);
      filter.addFilter("id!=", Long.valueOf(id));
      filter.addFilter("cardId=", cardId);

      AppPersonInfo appPersonInfo = remoteAppPersonInfoService.get(filter);
      if (null == appPersonInfo) {
        jsonResult.setSuccess(true);
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("此身份证号已被注册，请重新输入，身份证位数必须为18位");
      }
      return jsonResult;
    } catch (Exception e) {
      jsonResult.setMsg("查询错误");
      jsonResult.setSuccess(false);
      return jsonResult;
    }
  }

  @MethodName(name = "修改信息")
  @RequestMapping(value = "/modifyInfo", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult modifyInfo(HttpServletRequest req, AppPersonInfo personInfo) {
    JsonResult jsonResult = new JsonResult();
    try {
      appPersonInfoService.update(personInfo);
      jsonResult.setSuccess(true);
      jsonResult.setMsg("处理成功");
      return jsonResult;
    } catch (Exception e) {
      jsonResult.setMsg("出错了");
      jsonResult.setSuccess(false);
      return jsonResult;
    }
  }

  /*
   *author zongwei
   *Date   20180511
   *        修改身份证信息
   */
  @MethodName(name = "修改信息")
  @RequestMapping(value = "/modifycardIdInfo", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult modifycardIdInfo(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    String id = req.getParameter("id");
    String cardId = req.getParameter("cardId");
    String trueName = req.getParameter("trueName");
    String surname = req.getParameter("surname");
    String cardType1 = req.getParameter("cardType");
    int cardType = Integer.parseInt(cardType1);
    try {

      RemoteAppPersonInfoService remoteAppPersonInfoService = (RemoteAppPersonInfoService) ContextUtil
          .getBean("remoteAppPersonInfoService");

      RemoteQueryFilter filter = new RemoteQueryFilter(AppPersonInfo.class);
      filter.addFilter("id=", Long.valueOf(id));
      AppPersonInfo appPersonInfo = remoteAppPersonInfoService.get(filter);
      if (appPersonInfo == null) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("用户信息不存在！");
        return jsonResult;
      }
      appPersonInfo.setCardId(cardId);
      appPersonInfo.setTrueName(trueName);
      appPersonInfo.setSurname(surname);
      appPersonInfo.setCardType(cardType);
      appPersonInfoService.update(appPersonInfo);
      jsonResult.setSuccess(true);
      jsonResult.setMsg("处理成功");
      return jsonResult;
    } catch (Exception e) {
      jsonResult.setMsg("出错了");
      jsonResult.setSuccess(false);
      return jsonResult;
    }
  }


  @MethodName(name = "批量禁止交易")
  @RequestMapping(value = "/closeBatch", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult closeBatch(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    String ids = req.getParameter("ids");
    if (ids != null && ids.length() > 0) {
      String[] idss = ids.split(",");
      for (int i = 0; i < idss.length; i++) {
        AppCustomer appCustomer = service.get(Long.valueOf(idss[i]));
        if (appCustomer != null) {
          appCustomer.setIsChange(Integer.valueOf(1));
          //service.update(appCustomer);
          if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
            RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
            String value = redisService.get("mobile:" + appCustomer.getUuid());
            if (value != null) {
              appCustomer.setCustomerId(Long.valueOf(idss[i]));
              service.updateNull(appCustomer);
              JSONObject jsonv = JSON.parseObject(value);
              User user = (User) JSON
                  .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")),
                      User.class);
              if (user != null) {
                redisService.save("mobile:" + user.getUuid(),
                    "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON
                        .toJSON(appCustomer).toString() + "}", 1800);
              }
              jsonResult.setSuccess(true);
            } else {
              service.updateNull(appCustomer);
            }
          } else {
            service.updateNull(appCustomer);
          }
        }
      }
      jsonResult.setSuccess(true);
      jsonResult.setMsg("处理成功");
      return jsonResult;
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("处理失败");
      return jsonResult;
    }
  }

  @MethodName(name = "批量开启交易")
  @RequestMapping(value = "/openBatch", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult openBatch(HttpServletRequest req) {
    JsonResult jsonResult = new JsonResult();
    String ids = req.getParameter("ids");
    if (ids != null && ids.length() > 0) {
      String[] idss = ids.split(",");
      for (int i = 0; i < idss.length; i++) {
        AppCustomer appCustomer = service.get(Long.valueOf(idss[i]));
        if (appCustomer != null) {
          appCustomer.setIsChange(Integer.valueOf(0));
          if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
            RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
            String value = redisService.get("mobile:" + appCustomer.getUuid());
            if (value != null) {
              appCustomer.setCustomerId(Long.valueOf(idss[i]));
              service.updateNull(appCustomer);
              JSONObject jsonv = JSON.parseObject(value);
              User user = (User) JSON
                  .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")),
                      User.class);
              if (user != null) {
                redisService.save("mobile:" + user.getUuid(),
                    "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON
                        .toJSON(appCustomer).toString() + "}", 1800);
              }
              jsonResult.setSuccess(true);
            } else {
              service.updateNull(appCustomer);
            }
          } else {
            service.updateNull(appCustomer);
          }
        }
      }
      jsonResult.setSuccess(true);
      jsonResult.setMsg("处理成功");
      return jsonResult;
    } else {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("处理失败");
      return jsonResult;
    }
  }

  /**
   * 设置授权码，前台通过该授权码进行代理商的申请
   * <p> TODO</p>
   *
   * @author: Zhang Lei
   * @param: @param id
   * @param: @param code
   * @param: @return
   * @return: JsonResult
   * @Date :          2017年3月13日 下午6:09:16
   * @throws:
   */
  @MethodName(name = "设置授权码")
  @RequestMapping(value = "/setAuthorizationCode", method = RequestMethod.POST)
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult setAuthorizationCode(Long id, String code) {
    JsonResult result = new JsonResult();
    AppPersonInfoService appPersonInfoService = (AppPersonInfoService) ContextUtil
        .getBean("appPersonInfoService");
    try {
      AppPersonInfo appperson = appPersonInfoService.getByCustomerId(id);
      if (appperson != null && code != null && !"".equals(code)) {
        appperson.setJkApplyAuthorizationCode(code);
        appPersonInfoService.update(appperson);
        result.setSuccess(true);
        result.setMsg("生成成功！");
      } else {
        result.setSuccess(false);
        result.setMsg("传递参数不合法或没有查到该用户！");
      }
    } catch (Exception e) {
      result.setSuccess(false);
      result.setMsg("生成授权码异常！");
    }
    return result;
  }

  @MethodName(name = "审核认证")
  @RequestMapping(value = "/audit/{id}")
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult audit(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    if (appCustomer.getStates() == 1) {
      appCustomer.setStates(2);
      //User user=new User();
      RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
      if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
        String value = redisService.get("mobile:" + appCustomer.getUuid());
        if (value != null) {
          service.update(appCustomer);
          JSONObject jsonv = JSON.parseObject(value);
          User user = (User) JSON
              .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")), User.class);
          if (user != null) {
            appCustomer.setCustomerId(user.getCustomerId());

            AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(appCustomer.getId());
            appCustomer.setTruename(appPersonInfo.getTrueName());
            appCustomer.setSurname(appPersonInfo.getSurname());
            redisService.save("mobile:" + user.getUuid(),
                "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(appCustomer)
                    .toString() + "}", 1800);

            String config = redisService.get("configCache:all");
            JSONObject parseObject = JSONObject.parseObject(config);
            //人民币账户
						/*RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil.getBean("remoteAppAccountService");
						remoteAppAccountService.openAccount(appCustomer,appPersonInfo,parseObject.get("language_code").toString(),ContextUtil.getWebsite());*/
						
						/*RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil.getBean("remoteExProductService");
						remoteExProductService.openDmAccount(appCustomer,appPersonInfo,null,ContextUtil.getWebsite(),parseObject.get("language_code").toString());*/
            AppAgentsCustromer appAgentsCustromer = new AppAgentsCustromer();
            appAgentsCustromer.setStates(2);
            appAgentsCustromer.setCustomerName(appCustomer.getUserName());
            RemoteAppAgentsService remoteAppAgentsService = (RemoteAppAgentsService) ContextUtil
                .getBean("remoteAppAgentsService");
            JsonResult result = remoteAppAgentsService.updateAgents(appAgentsCustromer);

//                        giveCoin(user.getCustomerId(),parseObject.get("language_code").toString());

            try {
              //重置redis
              UserRedis userRedis = new UserRedis();
              userRedis.setId(appCustomer.getCustomerId().toString());
              RemoteManageService remoteManageService = (RemoteManageService) ContextUtil
                  .getBean("remoteManageService");
              Map<String, Long> map = remoteManageService
                  .findAllAccountId(appCustomer.getCustomerId().toString());
              userRedis.setAccountId(map.get("accountId") == null ? null : map.get("accountId"));
              //获取完后，移除
              map.remove("accountId");
              userRedis.setDmAccountId(map);

              RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
              redisUtil.put(userRedis, userRedis.getId());
              //缓存委托历史记录和当前记录
              RedisUtil<EntrustByUser> redisUtilEntrustByUser = new RedisUtil<EntrustByUser>(
                  EntrustByUser.class);
              List<Map<String, List<EntrustTrade>>> listml = remoteManageService
                  .findExEntrustBycust(Long.valueOf(id));
              EntrustByUser ebu = new EntrustByUser();
              ebu.setCustomerId(Long.valueOf(id));
              ebu.setEntrustedmap(listml.get(0));
              ebu.setEntrustingmap(listml.get(1));
              redisUtilEntrustByUser.put(ebu, appCustomer.getCustomerId().toString());
            } catch (Exception e) {
              System.out.println("审核通过，缓存失败");
              e.printStackTrace();
            }
          }
        } else {
          appcustomerUpdate(appCustomer, redisService);
        }
      } else {
        appcustomerUpdate(appCustomer, redisService);
      }
      jsonResult.setSuccess(true);
    } else {
      jsonResult.setSuccess(false);
    }
    return jsonResult;
  }


  @MethodName(name = "审核拒绝")
  @RequestMapping(value = "/auditback/{id}")
  @ResponseBody
  @MyRequiresPermissions
  public JsonResult auditback(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    Integer states = appCustomer.getStates();
    if (states == 1) {
      AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(id);
      appPersonInfo.setTrueName(null);
      appPersonInfo.setCardId(null);
      appPersonInfo.setPersonBank(null);
      appPersonInfo.setPersonCard(null);
      appPersonInfo.setFrontpersonCard(null);
      appPersonInfo.setPapersType(null);
      appPersonInfo.setSurname(null);
      appCustomer.setStates(3);
      service.update(appCustomer);
      RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
      if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
        String value = redisService.get("mobile:" + appCustomer.getUuid());
        if (value != null) {
          appPersonInfoService.updateNull(appPersonInfo);
          service.update(appCustomer);
          appCustomer.setCustomerId(id);
          JSONObject jsonv = JSON.parseObject(value);
          User user = (User) JSON
              .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")), User.class);
          if (user != null) {
            redisService.save("mobile:" + user.getUuid(),
                "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(appCustomer)
                    .toString() + "}", 1800);
          }
        } else {
          appcustomerDelete(appCustomer, redisService);

        }
      } else {
        appcustomerDelete(appCustomer, redisService);
      }

      jsonResult.setSuccess(true);
    } else {
      jsonResult.setSuccess(false);
    }
    return jsonResult;
  }

  /**
   * 审核通过 2017-10-31 09:57:43 -- denghf
   */
  public void appcustomerUpdate(AppCustomer appCustomer, RedisService redisService) {
    service.update(appCustomer);

    //人民币账户
    AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(appCustomer.getId());
    RemoteAppAccountService remoteAppAccountService = (RemoteAppAccountService) ContextUtil
        .getBean("remoteAppAccountService");
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    remoteAppAccountService
        .openAccount(appCustomer, appPersonInfo, parseObject.get("language_code").toString(),
            ContextUtil.getWebsite());

    RemoteExProductService remoteExProductService = (RemoteExProductService) ContextUtil
        .getBean("remoteExProductService");
    remoteExProductService.openDmAccount(appCustomer, appPersonInfo, null, ContextUtil.getWebsite(),
        parseObject.get("language_code").toString());
    AppAgentsCustromer appAgentsCustromer = new AppAgentsCustromer();
    appAgentsCustromer.setStates(2);
    appAgentsCustromer.setCustomerName(appCustomer.getUserName());
    RemoteAppAgentsService remoteAppAgentsService = (RemoteAppAgentsService) ContextUtil
        .getBean("remoteAppAgentsService");
    JsonResult result = remoteAppAgentsService.updateAgents(appAgentsCustromer);

    //giveCoin(appCustomer.getId(),parseObject.get("language_code").toString());
  }

  /**
   * 审核拒绝 2017-10-31 09:57:43 -- denghf
   */
  public void appcustomerDelete(AppCustomer appCustomer, RedisService redisService) {
    AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(appCustomer.getId());
    appPersonInfo.setTrueName(null);
    appPersonInfo.setCardId(null);
    appPersonInfo.setPersonBank(null);
    appPersonInfo.setPersonCard(null);
    appPersonInfo.setFrontpersonCard(null);
    appPersonInfo.setPapersType(null);
    appCustomer.setStates(3);
    appPersonInfoService.updateNull(appPersonInfo);
    service.update(appCustomer);
  }


  @MethodName(name = "审核删除所有信息")
  @RequestMapping(value = "/auditall/{id}")
  @ResponseBody
  public JsonResult auditall(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    if (appCustomer.getStates() == 2) {
      AppPersonInfo appPersonInfo = appPersonInfoService.getByCustomerId(id);
      appPersonInfo.setTrueName(null);
      appPersonInfo.setSurname(null);
      appPersonInfo.setCardId(null);
      appPersonInfo.setPersonBank(null);
      appPersonInfo.setPersonCard(null);
      appPersonInfo.setFrontpersonCard(null);
      appCustomer.setStates(3);
      //service.update(appCustomer);
      if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + appCustomer.getUuid());
        if (value != null) {
          appPersonInfoService.updateNull(appPersonInfo);
          service.update(appCustomer);
          JSONObject jsonv = JSON.parseObject(value);
          User user = (User) JSON
              .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")), User.class);
          if (user != null) {
            redisService.save("mobile:" + user.getUuid(),
                "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(appCustomer)
                    .toString() + "}", 1800);
          }
        }
      }
      jsonResult.setSuccess(true);
    } else {
      jsonResult.setSuccess(false);
    }
    return jsonResult;
  }


  @MethodName(name = "账户激活")
  @RequestMapping(value = "/isHasemail/{id}")
  @ResponseBody
  public JsonResult isHasemail(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    if (appCustomer.getHasEmail().equals(0)) {
      appCustomer.setHasEmail(1);
      service.update(appCustomer);
      jsonResult.setSuccess(true);
    } else {
      jsonResult.setSuccess(false);
    }
    return jsonResult;
  }


  /**
   * 送币 2017-10-31 09:57:43 -- denghf
   */
  public void giveCoin(Long id, String language) {
    // 查出全部发行的产品例表
    ExProductService exProductService = (ExProductService) ContextUtil.getBean("exProductService");
    ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
        .getBean("exDigitalmoneyAccountService");
    QueryFilter filter = new QueryFilter(ExProduct.class);
    filter.addFilter("issueState=", Integer.valueOf(1));
    filter.setOrderby("isRecommend DESC");
    List<ExProduct> list = exProductService.find(filter);

    for (ExProduct exProduct : list) {
      QueryFilter filterAppAccount = new QueryFilter(AppAccount.class);
      filterAppAccount.addFilter("customerId=", id);
      filterAppAccount.addFilter("coinCode=", exProduct.getCoinCode());
      ExDigitalmoneyAccount _digitalmoneyAccount = exDigitalmoneyAccountService
          .get(filterAppAccount);
      if (_digitalmoneyAccount != null) {
        if (exProduct.getGiveCoin().compareTo(new BigDecimal(0)) > 0) {
          _digitalmoneyAccount.setHotMoney(exProduct.getGiveCoin());//注册送币
          exDigitalmoneyAccountService.update(_digitalmoneyAccount);

          //记录流水
          exDigitalmoneyAccountService.saveRecord(_digitalmoneyAccount, 1);
        }
      }
      //ThreadPool.exe(new OpenCoin(language, exProduct.getCoinCode(), _digitalmoneyAccount.getUserName(), _digitalmoneyAccount.getId()));
    }
  }

  @MethodName(name = "申请APIkey")
  @RequestMapping(value = "/applyApi")
  @ResponseBody
  public JsonResult applyApi(HttpServletRequest request) {
    String id = request.getParameter("id");
    String ip = request.getParameter("ip");
    JsonResult jsonResult = new JsonResult();
    RemoteExApiApplyService remoteExApiApplyService = (RemoteExApiApplyService) ContextUtil
        .getBean("remoteExApiApplyService");
    ExApiApply exApiApply = new ExApiApply();
    exApiApply.setCustomerId(Long.valueOf(id));
    exApiApply.setIpAddress(ip);

    Map<String, String> map = remoteExApiApplyService.createKey(exApiApply);

    List<ExApiApply> findList = remoteExApiApplyService.findList(Long.valueOf(id));
    if (findList.size() > 1) {
      for (int i = 1; i < findList.size(); i++) {
        remoteExApiApplyService.delete(findList.get(i).getId());
      }
    }

    jsonResult.setSuccess(true);
    jsonResult.setMsg(findList.get(0).getAccessKey());

    return jsonResult;
  }

  public static String getIp(HttpServletRequest request) {

    String ip = request.getHeader("X-Forwarded-For");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddr();

  }


  @MethodName(name = "关闭谷歌认证")
  @RequestMapping(value = "/offgoogle/{id}")
  @ResponseBody
  public JsonResult offgoogle(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    Integer googleState = appCustomer.getGoogleState();
    if (googleState == 1) {
      appCustomer.setGoogleKey(null);
      appCustomer.setGoogleState(0);
      //service.update(appCustomer);
      if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + appCustomer.getUuid());
        if (value != null) {
          service.updateNull(appCustomer);
          appCustomer.setCustomerId(id);
          JSONObject jsonv = JSON.parseObject(value);
          User user = (User) JSON
              .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")), User.class);
          if (user != null) {
            redisService.save("mobile:" + user.getUuid(),
                "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(appCustomer)
                    .toString() + "}", 1800);
          }
        }
      }

      jsonResult.setSuccess(true);
    } else {
      jsonResult.setSuccess(false);
    }
    return jsonResult;
  }


  @MethodName(name = "关闭手机认证")
  @RequestMapping(value = "/offphone/{id}")
  @ResponseBody
  public JsonResult offphone(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    Integer phoneState = appCustomer.getPhoneState();
    if (phoneState == 1) {
      appCustomer.setPhone(null);
      appCustomer.setPhoneState(0);
      appCustomer.setOpenPhone(1);
      appCustomer.setPassDate(new Date());
      appCustomer.setCheckStates(0);
      if (appCustomer.getUuid() != null && !"".equals(appCustomer.getUuid())) {
        RedisService redisService = (RedisService) ContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + appCustomer.getUuid());
        if (value != null) {
          service.updateNull(appCustomer);
          appCustomer.setCustomerId(id);
          JSONObject jsonv = JSON.parseObject(value);
          User user = (User) JSON
              .parseObject(com.alibaba.fastjson.JSON.toJSONString(jsonv.get("user")), User.class);
          if (user != null) {
            redisService.save("mobile:" + user.getUuid(),
                "{\"mobile\":\"" + user.getUsername() + "\",\"user\":" + JSON.toJSON(appCustomer)
                    .toString() + "}", 1800);
          }
        } else {
          service.updateNull(appCustomer);
          appCustomer.setCustomerId(id);
        }
      }

      jsonResult.setSuccess(true);
    } else {
      jsonResult.setSuccess(false);
    }
    return jsonResult;
  }





  /*	*/

  /**
   * 查询代理商
   *//*
	public List<AgentsForMoney> commendfind(String username) {
		//String username = request.getParameter("username");
		RemoteAppAgentsService remoteAppAgentsService = (RemoteAppAgentsService)ContextUtil.getBean("remoteAppAgentsService");

		List<AgentsForMoney> findAgentsForMoneyList = remoteAppAgentsService.findAgentsForMoneyList(username);
		
		return findAgentsForMoneyList;
	}
	
	
	
*/


  /*
   * 导入用户信息
   */
  @MethodName(name = "添加非标用户")
  @RequestMapping(value = "/addUser")
  @ResponseBody
  public JsonResult addCustomer() {
    JsonResult jsonResult = new JsonResult();
    Workbook workbook = null;
    String mobile = "";
    String password = "";
    try {
      String realPath = this.getRequest().getRealPath("/");
      System.out.println(realPath);
      // 生成hryfile路径
      String rootPath = realPath.substring(0,
          org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2) + 1);
      System.out.println(rootPath);
      workbook = Workbook.getWorkbook(
          new File(rootPath + File.separator + "hryfile" + File.separator + "test.xls"));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Sheet sheet[] = workbook.getSheets();
    for (int a = 0; a < sheet.length; a++) {
      System.out.println(sheet.length);
      for (int i = 1; i < sheet[a].getRows(); i++) {
        mobile = sheet[a].getCell(3, i).getContents();
        password = "85ea42562e733c094a193f469ad440d6";

        String referralCode = null;
        String country = "+86";

        RemoteManageService remoteManageService = (RemoteManageService) ContextUtil
            .getBean("remoteManageService");
        RemoteResult regist = remoteManageService
            .registMobile1(mobile, password, referralCode, country, "USD");

      }
    }

    return jsonResult;
  }

  /*
   * 导入用户信息
   */
  @MethodName(name = "实名认证")
  @RequestMapping(value = "/addidenty")
  @ResponseBody
  public JsonResult addCustomer1() {
    JsonResult jsonResult = new JsonResult();
    Workbook workbook = null;
    String userName = "";
    String trueName = "";
    String sex = "";
    String surname = "";
    String country = "";
    String cardType = "";
    String cardId = "";
    String[] pathImg = new String[3];
    try {
      String realPath = this.getRequest().getRealPath("/");
      // 生成hryfile路径
      String rootPath = realPath.substring(0,
          org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2)
              + 1);
      workbook = Workbook.getWorkbook(
          new File(rootPath + File.separator + "hryfile" + File.separator + "test.xls"));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Sheet sheet[] = workbook.getSheets();
    for (int a = 0; a < sheet.length; a++) {
      for (int i = 1; i < sheet[a].getRows(); i++) {

        userName = sheet[a].getCell(3, i).getContents();
        trueName = sheet[a].getCell(1, i).getContents()
            .substring(1, (sheet[a].getCell(1, i).getContents()).length());
        sex = sheet[a].getCell(2, i).getContents();
        if (sex == "1") {
          sex = "男";
        }
        if (sex == "2") {
          sex = "女";
        }
        if ((sex == "") || (sex == null)) {
          sex = "男";
        }

        surname = sheet[a].getCell(1, i).getContents().substring(0, 1);
        country = "中国大陆";
        cardType = "0";
        //获取身份证号
        cardId = sheet[a].getCell(8, i).getContents();
        //获取图片的地址
        String str1 = sheet[a].getCell(9, i).getContents();
        String str2 = sheet[a].getCell(10, i).getContents();
        String str3 = sheet[a].getCell(11, i).getContents();
        List<String> list = new ArrayList<String>();
        list.add(str1);
        list.add(str2);
        list.add(str3);
        pathImg = list.toArray(new String[0]);

        RemoteManageService remoteManageService = (RemoteManageService) ContextUtil
            .getBean("remoteManageService");
        //RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        //调用实名认证接口
        RemoteResult realname = remoteManageService
            .xstar(userName, trueName, sex, surname, country, cardType, cardId, pathImg, "1",
                "USD");


      }
    }

    return jsonResult;
  }

  public static HttpServletRequest getRequest() {
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes())
          .getRequest();
      return request;
    } catch (Exception e) {
    }
    return null;

  }

  /**
   * @Description: 关闭OTC冻结
   * @Author: zongwei
   * @CreateDate: 2018/6/19 17:05
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/6/19 17:05
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  @MethodName(name = "OTC解冻")
  @RequestMapping(value = "/offotcFrozen/{id}")
  @ResponseBody
  public JsonResult offotcFrozen(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    appCustomer.setOtcFrozenCout(new BigDecimal(0));
    appCustomer.setOtcFrozenDate(null);
    service.updateNull(appCustomer);
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  /**
   * @Description: 开通OTC委托
   * @Author: zongwei
   * @CreateDate: 2018/6/19 17:05
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/6/19 17:05
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  @MethodName(name = "开通OTC委托")
  @RequestMapping(value = "/openotc/{id}")
  @ResponseBody
  @AppManageLogsAop(name = "openotc", remark = "开通OTC委托")
  public JsonResult openotc(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    appCustomer.setOpenOtcStates(1);
    service.update(appCustomer);
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  /**
   * @Description: 关闭OTC委托
   * @Author: zongwei
   * @CreateDate: 2018/6/19 17:05
   * @UpdateUser: zongwei
   * @UpdateDate: 2018/6/19 17:05
   * @UpdateRemark: 创建
   * @Version: 1.0
   */
  @MethodName(name = "关闭OTC委托")
  @RequestMapping(value = "/closeotc/{id}")
  @ResponseBody
  @AppManageLogsAop(name = "closeotc", remark = "关闭OTC委托")
  public JsonResult closeotc(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    appCustomer.setOpenOtcStates(0);
    service.update(appCustomer);
    jsonResult.setSuccess(true);
    return jsonResult;
  }

  @MethodName(name = "清除邮箱绑定")
  @RequestMapping(value = "/clearmail/{id}")
  @ResponseBody
  @AppManageLogsAop(name = "clearMail", remark = "清除邮箱绑定")
  public JsonResult clearMail(@PathVariable Long id) {
    JsonResult jsonResult = new JsonResult();
    AppCustomer appCustomer = service.get(id);
    appCustomer.setMailStates(0);
    appCustomer.setMail(null);
    service.updateNull(appCustomer);
    jsonResult.setSuccess(true);
    return jsonResult;
  }
}
