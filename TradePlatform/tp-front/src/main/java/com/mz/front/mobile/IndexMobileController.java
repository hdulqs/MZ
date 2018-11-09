package com.mz.front.mobile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.sms.SmsParam;
import com.mz.core.sms.SmsSendUtil;
import com.mz.core.constant.StringConstant;
import com.mz.exchange.kline2.model.LastKLinePayload;
import com.mz.front.index.controller.EmailRunnable;
import com.mz.front.index.controller.UserRedisRunnable;
import com.mz.front.kline.model.MarketTrades;
import com.mz.front.kline.model.MarketTradesSub;
import com.mz.manage.remote.RemoteManageService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.DrawPictureUtil;
import com.mz.util.IpUtil;
import com.mz.util.SessionUtils;
import com.mz.core.thread.ThreadPool;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.SpringContextUtil;
import com.mz.web.app.model.AppBanner;
import com.mz.manage.remote.RemoteAppArticleService;
import com.mz.manage.remote.RemoteBaseInfoService;
import com.mz.manage.remote.model.Article;
import com.mz.manage.remote.model.Coin;
import com.mz.manage.remote.model.Coin2;
import com.mz.manage.remote.model.CoinAccount;
import com.mz.manage.remote.model.MyAccountTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.SysCodeValueManage;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Controller
@RequestMapping(value = "/mobile/nouser")
@Api(value = "App操作类", description = "登录注册等,不需要登录的")
public class IndexMobileController {

  private final static Logger log = Logger.getLogger(IndexMobileController.class);

  @Autowired
  private RedisService redisService;
  @Autowired
  UserRedisRunnable userRedisRunnable;

  @RequestMapping(value = "/regreg", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "注册协议,获取邮箱和客服电话", httpMethod = "POST", response = JsonResult.class, notes = "语言languages")
  @ResponseBody
  public JsonResult regreg(HttpServletRequest request, @RequestParam String languages) {
    // 查询后台参数配置
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String config = redisService.get("configCache:all");
    Map<String, Object> map = new HashMap<String, Object>();
    if (!StringUtils.isEmpty(config)) {
      JSONObject parseObject = JSONObject.parseObject(config);
      if ("zh_CN".equals(languages.toString())) {
        map.put("regreg", parseObject.get("reg_cn"));
      } else if ("en".equals(languages.toString())) {
        map.put("regreg", parseObject.get("reg_en"));
      }
      map.put("servicePhone", parseObject.get("servicePhone"));
      map.put("serviceEmail", parseObject.get("serviceEmail"));

    }
    return jsonResult.setSuccess(true).setObj(map);
  }

  @RequestMapping(value = "/sendMsg", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "手机认证发送验证码", httpMethod = "POST", response = JsonResult.class, notes = "mobile 手机号")
  @ResponseBody
  public JsonResult sendMsg(HttpServletRequest request, @RequestParam String mobile) {
    String tokenId = request.getParameter("tokenId");
    RedisService redisService = (RedisService) SpringContextUtil.getBean("redisService");
    String value = redisService.get("mobile:" + tokenId);
    if (value != null) {
      String tel = mobile;
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      User user = remoteManageService.selectByTel(tel);
      if (user != null) {

        RemoteResult setPhone = remoteManageService.setPhone(mobile, user.getUsername());
        if (setPhone.getSuccess()) {
          user.setPhone(mobile);
          SessionUtils.updateRedis(user);
          return new JsonResult().setSuccess(true)
              .setMsg(SpringContextUtil.diff("phonesuccess", "zh_CN"));
        } else {
          return new JsonResult().setSuccess(false)
              .setMsg(SpringContextUtil.diff("phoneerror", "zh_CN"));

        }
      }
    }
    return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
  }


  @RequestMapping(value = "/appreg", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "注册", httpMethod = "POST", response = JsonResult.class, notes =
      "用户名username,"
          + "密码password,图形验证码registCode,邀请码referralCode,邀请码可为空,语言language")
  @ResponseBody
  public JsonResult appreg(HttpServletRequest request, HttpServletResponse response,
      @RequestParam String username,
      @RequestParam String password, @RequestParam String registCode
  ) {
    String referralCode = request.getParameter("referralCode");

    Locale locale = new Locale("zh_CN");

    // 用户名
    // 密码
    // 图形验证码
    // 邀请码

    if (StringUtils.isEmpty(username)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("emailisnull", "zh_CN"));
    }
    if (StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg("密码不能为空");
    }
/*		if (StringUtils.isEmpty(registCode)) {
			return new JsonResult().setMsg("图形验证码不能为空");
		}*/
    // redis图形验证码
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("regCode");

    if (!registCode.equalsIgnoreCase(value)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tx_error", "zh_CN"));
    }
	/*	if (StringUtils.isEmpty(registSmsCode)) {
			return new JsonResult().setMsg("手机验证码不能为空");
		}*/
/*		String telCode = redisService.get("registCode:" + username);
		if (!registSmsCode.equalsIgnoreCase(telCode)) {// 判断手机验证码
			return new JsonResult().setMsg("手机验证码错误");
		}
*/

    if (!"".equals(referralCode) && referralCode != null) {
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      RemoteResult selectPhone = remoteManageService.selectAgent(referralCode);
      if (!selectPhone.getSuccess()) {
        return new JsonResult().setSuccess(false)
            .setMsg(SpringContextUtil.diff("dailisbucunzai", "zh_CN"));
      }
    }
    try {
      RemoteManageService manageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");

      //后台配置的法币类型
      String language_code = null;
      String config = redisService.get("configCache:all");
      if (!StringUtils.isEmpty(config)) {
        JSONObject parseObject = JSONObject.parseObject(config);
        language_code = parseObject.get("language_code").toString();
      }

      RemoteResult regist = manageService.regist(username, password, referralCode, language_code);
      if (regist != null) {
        if (regist.getSuccess()) {
          String url =
              PropertiesUtils.APP.getProperty("app.url") + "/activation/" + username + "/" + regist
                  .getObj();
          // 发送邮件
          StringBuffer sb = new StringBuffer();
          sb.append(SpringContextUtil.diff("dear", "zh_CN") + " " + username + "<br><br>"
              + SpringContextUtil
              .diff("regestone", "zh_CN") + "<br><br>" + SpringContextUtil
              .diff("regesttwo", "zh_CN")
              + "<br><br>" + SpringContextUtil.diff("browseropen", "zh_CN") + "<br><br>");
          sb.append("<a href='" + url + "'>" + url + "</a>");
          sb.append("<br><br>");

          //sb.append("/activation/"+email+"/"+regist.getObj()+"<br><br>"+"<input type='button'  value='提交' onClick='location.href="+PropertiesUtils.APP.getProperty("app.url")+"/activation/"+email+"/"+regist.getObj()+"' />");
          //sb.append(SpringContextUtil.diff("Resetendemail")+"<br>"+SpringContextUtil.diff("Resetendem"));
          String type = "1";
          ThreadPool.exe(new EmailRunnable(username, sb.toString(), type, locale));
          return new JsonResult().setSuccess(true)
              .setMsg(SpringContextUtil.diff("reg_success", "zh_CN"));
        } else {
          return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg(), "zh_CN"));
        }
      }

    } catch (Exception e) {
      log.error("注册方法远程调用出错");
    }
    return new JsonResult();
  }

  @RequestMapping(value = "/applogin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "登录", httpMethod = "POST", response = JsonResult.class, notes = "用户名username和密码password必填")
  @ResponseBody
  public JsonResult applogin(HttpServletRequest request, HttpServletResponse response,
      @RequestParam String username,
      @RequestParam String password) {

    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg("用户或密码不能为空!");
    }

    try {
      //生成tokenid
      UUID uuid = UUID.randomUUID();
      RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
          .getBean("remoteManageService");
      RedisService redisService = (RedisService) SpringContextUtil.getBean("redisService");
      User selectByTel = remoteManageService.selectByTel(username);
      //add by zongwei begin  20180426 如果查询的用户信息为空，判断为用户不存在
      try {
        if (selectByTel.getUsername() == null) {
          return new JsonResult().setSuccess(false).setMsg("用户不存在!");
        }
      } catch (Exception e) {
        return new JsonResult().setSuccess(false).setMsg("用户不存在!");
      }
      //add by zongwei end  20180426 如果查询的用户信息为空，判断为用户不存在
      //以及存在用户表中的uuid 旧的uuid
      String uuid2 = selectByTel.getUuid();
      //查不来不为空则把他在redis中的缓存删除
      if (uuid2 != null || !"".equals(uuid2)) {
        redisService.delete("mobile:" + uuid2);
      }
      RemoteResult login = remoteManageService.login(username, password, uuid.toString());

      if (login != null && login.getSuccess()) {
        User user = (User) login.getObj();
        //判断手机认证状态(0否，1是)(+)
        if (user.getCheckStates() == 1) {
          return new JsonResult().setSuccess(true).setObj(user);
        }
        //判断谷歌认证状态（0否，1是）
        if (user.getGoogleState() == 1) {
          return new JsonResult().setSuccess(true).setObj(user);
        }
        ////判断谷歌和手机同时认证状态（0否，1是）
        if (user.getPhone_googleState() == 1) {
          return new JsonResult().setSuccess(true).setObj(user);
        }

        userRedisRunnable.process(user.getCustomerId().toString());

        log.info(username + "|登录成功!");
        // 存入redis 给手机端提供token
        user.setTokenId(uuid.toString());
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(24 * 60 * 60);
        redisService.save("mobile:" + uuid,
            "{\"mobile\":\"" + username + "\",\"user\":" + JSON.toJSON(login.getObj()).toString()
                + "}",
            86400);
        log.info("UUID=" + uuid);
        redisService.save("online:username:" + username, "1", 1800);
        Cookie cookie = new Cookie("tokenId", uuid.toString());
        cookie.setMaxAge(86400);
        cookie.setPath("/");
        response.addCookie(cookie);
        if (user != null) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("user", login.getObj());
          map.put("UUID", uuid);
          return new JsonResult().setSuccess(true).setObj(map);
        } else {
          return new JsonResult().setSuccess(false).setObj(user);

        }

      }
      return new JsonResult().setSuccess(false)
          .setMsg(SpringContextUtil.diff(login.getMsg(), "zh_CN"));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("登录方法远程调用出错");
    }

    return new JsonResult().setSuccess(false).setMsg("用户或密码错误!");
  }

  @RequestMapping(value = "/apparticle", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "最新公告", httpMethod = "POST", response = JsonResult.class, notes = "最新公告,type传4, num")
  @ResponseBody
  public JsonResult apparticle(HttpServletRequest request, @RequestParam String type) {
    JsonResult jsonResult = new JsonResult();
    String num = request.getParameter("num");
    // 新闻
    RemoteAppArticleService remoteAppArticleService = SpringContextUtil
        .getBean("remoteAppArticleService");
    List<Article> listArticle = remoteAppArticleService
        .findArticListByIdLimit(Long.valueOf(type), Integer.valueOf(num), "cn" + "");// 最新动态
    return jsonResult.setSuccess(true).setObj(listArticle);
  }


  @RequestMapping(value = "/pageAppArticle", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "分页查询", httpMethod = "POST", response = JsonResult.class, notes = "type 文章类型 , limit 每页多少条 , offset 第几页")
  @ResponseBody
  public JsonResult pageAppArticle(HttpServletRequest request, @RequestParam String type,
      @RequestParam String limit, @RequestParam String offset) {
    JsonResult jsonResult = new JsonResult();
    Map<String, String> params = new HashMap<String, String>();
    params.put("type", type);
    params.put("page", offset);
    params.put("pageSize", limit);
    params.put("offset", offset);
    params.put("limit", limit);
    params.put("categoryid", type);
    params.put("website", "cn");
    // 新闻
    RemoteAppArticleService remoteAppArticleService = SpringContextUtil
        .getBean("remoteAppArticleService");
    FrontPage listArticle = remoteAppArticleService.findAritcByType(params);// 最新动态
    return jsonResult.setSuccess(true).setObj(listArticle);
  }

  @RequestMapping(value = "/appbanner", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "banner图", httpMethod = "POST", response = JsonResult.class, notes = "banner图")
  @ResponseBody
  public JsonResult appbanner() {
    JsonResult jsonresult = new JsonResult();
    List<AppBanner> list = new ArrayList<AppBanner>();
    List<AppBanner> list2 = new ArrayList<AppBanner>();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String text = redisService.get(StringConstant.CACHE_BANNER);
    if (!StringUtils.isEmpty(text)) {
      list = JSON.parseArray(text, AppBanner.class);
    }
    for (AppBanner appBanner : list) {
      Integer type = appBanner.getApplicationType();
      if (type == 1) {
        list2.add(appBanner);
      }
    }
    jsonresult.setSuccess(true).setObj(list2);

    return jsonresult;
  }

  @RequestMapping(value = "/appmarketlist", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "首页成交记录", httpMethod = "POST", response = JsonResult.class, notes = "首页成交记录")
  @ResponseBody
  public List<JSONObject> index(HttpServletRequest request) {
    Locale locale = LocaleContextHolder.getLocale();
    ArrayList<JSONObject> list = new ArrayList<JSONObject>();
    // 去redis查询产品数量
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String productListStr = redisService.get("cn:coinInfoList");
    if (!StringUtils.isEmpty(productListStr)) {
      List<Coin> productList = JSON.parseArray(productListStr, Coin.class);

      for (Coin coin : productList) {
        JSONObject data = new JSONObject();
        //法币位数
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);
        int keepDecimalForRmb = 2;
        String str1 = parseObject.getString("keepDecimalForRmb");
        String language_code = parseObject.getString("language_code");
        if (!StringUtils.isEmpty(str1)) {
          keepDecimalForRmb = Integer.valueOf(str1).intValue();
        }
        data.put("language_keepDecimalFor", keepDecimalForRmb);
        data.put("language_code", language_code);

        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        Integer keepDecimalForCoin = remoteManageService.getKeepDecimalForCoin(coin.getCoinCode());
        data.put("KeepDecimalForCoin", keepDecimalForCoin);
        String coinCode = coin.getCoinCode() + "_" + coin.getFixPriceCoinCode();
        // 获得每个时间段的最新价 最高价，开盘价，收盘价
        String periodLastKLineListStr = redisService.get(coinCode + ":PeriodLastKLineList");
        if (!StringUtils.isEmpty(periodLastKLineListStr)) {
          List<LastKLinePayload> periodList = com.alibaba.fastjson.JSON
              .parseArray(periodLastKLineListStr, LastKLinePayload.class);
          if (periodList != null) {
            for (LastKLinePayload l : periodList) {
              if ("1day".equals(l.getPeriod())) {
                BigDecimal dayTotalDealAmount = l.getDayTotalDealAmount();
                data.put("totalAmount", dayTotalDealAmount);
                break;
              }
            }
          }
        }

        data.put("coinCode", coinCode);
        if (coin.getFixPriceCoinCode().equals(language_code)) {
          data.put("price_keepDecimalFor", keepDecimalForRmb);
        } else {
          data.put("price_keepDecimalFor",
              remoteManageService.getKeepDecimalForCoin(coin.getFixPriceCoinCode()));
        }

        if (locale.toString() == null) {
          data.put("name", coin.getName() + coin.getCoinCode());
        } else {
          if ("zh_CN".equals(locale.toString())) {
            //data.put("name", coin.getName()+coin.getCoinCode());
            data.put("name", coin.getCoinCode());
          } else {
            data.put("name", coin.getCoinCode());
          }
        }
        data.put("picturePath", coin.getPicturePath());

        String currentExchangPrice_str = redisService
            .get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode() + ":currentExchangPrice");
        if (!StringUtils.isEmpty(currentExchangPrice_str)) {
          data.put("currentExchangPrice", new BigDecimal(currentExchangPrice_str));
        } else {
          data.put("currentExchangPrice", 0);
        }

        // 昨日收盘价
        String coinStr = redisService.get("cn:coinInfoList2");
        BigDecimal yesterdayPrice = new BigDecimal(0);
        if (!StringUtils.isEmpty(coinStr)) {
          List<Coin2> coins = JSON.parseArray(coinStr, Coin2.class);
          for (Coin2 c : coins) {
            if (coinCode.equals(c.getCoinCode() + "_" + c.getFixPriceCoinCode())) {
              if (!StringUtils.isEmpty(c.getYesterdayPrice())) {
                yesterdayPrice = new BigDecimal(c.getYesterdayPrice());
              }
            }
          }
        }
        data.put("yesterdayPrice", yesterdayPrice);

        String str = redisService
            .get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode() + ":PeriodLastKLineList");
        if (str != null) {
          JSONArray jsonv = JSON.parseArray(str);
          if (jsonv.getString(5) != null) {
            JSONObject jsonv_ = JSON.parseObject(jsonv.getString(5));
            if ("1day".equals(jsonv_.getString("period"))) {

              BigDecimal currentExchangPrice = new BigDecimal(0);
              //上一笔交易价格
              String orders = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()
                  + ":pushNewListRecordMarketDesc");
              if (!StringUtils.isEmpty(orders)) {
                MarketTrades marketTrades = com.alibaba.fastjson.JSON
                    .parseObject(orders, MarketTrades.class);
                // 最新价格
                if (marketTrades != null) {
                  List<MarketTradesSub> trades = marketTrades.getTrades();
                  if (trades != null) {
                    if (trades.size() > 1) {
                      MarketTradesSub marketTradesSub0 = trades.get(0);
                      data.put("currentExchangPrice", marketTradesSub0.getPrice());
                      currentExchangPrice = marketTradesSub0.getPrice();

                      MarketTradesSub marketTradesSub1 = trades.get(1);
                      data.put("lastExchangPrice", marketTradesSub1.getPrice());
                    } else {

                      MarketTradesSub marketTradesSub0 = trades.get(0);
                      data.put("currentExchangPrice", marketTradesSub0.getPrice());
                      currentExchangPrice = marketTradesSub0.getPrice();

                      data.put("lastExchangPrice", marketTradesSub0.getPrice());
                    }
                  } else {
                    data.put("lastExchangPrice", "1");
                  }
                } else {
                  data.put("lastExchangPrice", "1");
                }
              } else {
                data.put("lastExchangPrice", "1");
              }
              data.put("transactionSum", jsonv_.getString("amount"));
              data.put("maxPrice", jsonv_.getString("priceHigh"));
              data.put("minPrice", jsonv_.getString("priceLow"));
              // 开盘价
              data.put("openPrice", new BigDecimal(jsonv_.getString("priceOpen")));

              if (BigDecimal.ZERO.compareTo(yesterdayPrice) != 0) {
                if (BigDecimal.ZERO.compareTo(currentExchangPrice) != 0) {
                  BigDecimal divide = (currentExchangPrice.subtract(yesterdayPrice))
                      .divide(yesterdayPrice, 5, BigDecimal.ROUND_HALF_DOWN)
                      .multiply(new BigDecimal(100));
                  data.put("RiseAndFall", divide);
                } else {
                  data.put("RiseAndFall", 0);
                }
              } else {
                data.put("RiseAndFall", 0);
              }


            } else {
              data.put("lastExchangPrice", 0);
              data.put("transactionSum", 0);
              data.put("maxPrice", 0);
              data.put("minPrice", 0);
              // 开盘价
              data.put("openPrice", new BigDecimal(0));
              data.put("lastEndPrice", 0);
              data.put("RiseAndFall", 0);
            }
          } else {
            data.put("lastExchangPrice", 0);
            data.put("transactionSum", 0);
            data.put("maxPrice", 0);
            data.put("minPrice", 0);
            // 开盘价
            data.put("openPrice", new BigDecimal(0));

            data.put("lastEndPrice", 0);
            data.put("RiseAndFall", 0);
          }
        } else {
          data.put("lastExchangPrice", 0);
          data.put("transactionSum", 0);
          data.put("maxPrice", 0);
          data.put("minPrice", 0);
          // 开盘价
          data.put("openPrice", new BigDecimal(0));

          data.put("lastEndPrice", 0);
          data.put("RiseAndFall", 0);
        }
        list.add(data);

      }
    }
    return list;
  }


  /**
   * app新首页数据推送  view_v1版本
   */
  @RequestMapping(value = "/indexv1", method = RequestMethod.POST)
  @ApiOperation(value = "首页成交记录v1", httpMethod = "POST", response = JsonResult.class, notes = "首页成交记录v1")
  @ResponseBody
  public JSONArray indexv1(HttpServletRequest request) {

    /*获得usdt对人民币价格*/
    BigDecimal usdttormb = new BigDecimal(1);
    String financeConfig = redisService.get("configCache:financeConfig");
    if (!StringUtils.isEmpty(financeConfig)) {
      JSONArray parseArray = JSON.parseArray(financeConfig);

      if (parseArray != null) {
        for (int i = 0; i < parseArray.size(); i++) {
          JSONObject jsonObject = parseArray.getJSONObject(i);
          if ("usdttormb".equals(jsonObject.getString("configkey"))) {
            String value = jsonObject.getString("value");
            if (!StringUtils.isEmpty(value)) {
              usdttormb = new BigDecimal(value);
            }
          }
        }
      }

    }

    JSONArray jsonArray = new JSONArray();

    // 去redis查询产品数量
    String productListStr = redisService.get("cn:coinInfoList");
    if (!StringUtils.isEmpty(productListStr)) {
      List<Coin> productList = JSON.parseArray(productListStr, Coin.class);

      //统计交易区数量
      Set<String> coinarea = new LinkedHashSet<String>();
      //查询用户自选区
      User user = (User) request.getSession().getAttribute("user");
      //用户自选交易对
      List<String> mycoins = null;
      if (user != null) {
        RemoteBaseInfoService remoteBaseInfoService = SpringContextUtil
            .getBean("remoteBaseInfoService");
        mycoins = remoteBaseInfoService.findCustomerCollection(user.getCustomerId());
        JSONObject obj = new JSONObject();
        obj.put("areaname", "mycollection");
        obj.put("areanameview", SpringContextUtil.diff("zixuan"));

        //创建list
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        if (mycoins != null && mycoins.size() > 0) {
          for (String coinCode : mycoins) {
            //遍历
            for (Coin coin : productList) {
              if (coinCode.equals(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode())) {
                JSONObject data = createData(coin);
                data.put("usdttormb", usdttormb);
                data.put("isShoucang", true);
                list.add(data);
              }
            }
          }
        }

        obj.put("data", list);
        //加入map中
        jsonArray.add(obj);
      }
      //增加交易区
      for (Coin coin : productList) {
        coinarea.add(coin.getFixPriceCoinCode());
      }

      if (coinarea.size() > 0) {
        Iterator<String> it = coinarea.iterator();
        while (it.hasNext()) {
          String areaname = it.next();

          JSONObject obj = new JSONObject();
          obj.put("areaname", areaname);
          obj.put("areanameview", areaname + "  " + SpringContextUtil.diff("jiaoyiqu"));

          //创建list
          ArrayList<JSONObject> list = new ArrayList<JSONObject>();
          //遍历
          for (Coin coin : productList) {
            if (areaname.equals(coin.getFixPriceCoinCode())) {
              JSONObject data = createData(coin);
              data.put("usdttormb", usdttormb);
              if (mycoins != null && mycoins.size() > 0) {
                for (String coinCode : mycoins) {
                  if (coinCode.equals(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode())) {
                    data.put("isShoucang", true);
                  }
                }
              }

              list.add(data);
            }
          }
          obj.put("data", list);
          //加入map中
          jsonArray.add(obj);
        }
      }


    }
    return jsonArray;
  }

  /**
   * app新首页数据推送  view_v1版本
   */
  @RequestMapping(value = "/indexv2", method = RequestMethod.POST)
  @ApiOperation(value = "首页成交记录v2", httpMethod = "POST", response = JsonResult.class, notes = "首页成交记录v1")
  @ResponseBody
  public JsonResult indexv2(HttpServletRequest request) {

    /*获得usdt对人民币价格*/
    BigDecimal usdttormb = new BigDecimal(1);
    String financeConfig = redisService.get("configCache:financeConfig");
    if (!StringUtils.isEmpty(financeConfig)) {
      JSONArray parseArray = JSON.parseArray(financeConfig);

      if (parseArray != null) {
        for (int i = 0; i < parseArray.size(); i++) {
          JSONObject jsonObject = parseArray.getJSONObject(i);
          if ("usdttormb".equals(jsonObject.getString("configkey"))) {
            String value = jsonObject.getString("value");
            if (!StringUtils.isEmpty(value)) {
              usdttormb = new BigDecimal(value);
            }
          }
        }
      }

    }

    JSONArray jsonArray = new JSONArray();

    // 去redis查询产品数量
    String productListStr = redisService.get("cn:coinInfoList");
    if (!StringUtils.isEmpty(productListStr)) {
      List<Coin> productList = JSON.parseArray(productListStr, Coin.class);

      //统计交易区数量
      Set<String> coinarea = new LinkedHashSet<String>();
      //查询用户自选区
      User user = (User) request.getSession().getAttribute("user");
      //用户自选交易对
      List<String> mycoins = null;
      if (user != null) {
        RemoteBaseInfoService remoteBaseInfoService = SpringContextUtil
            .getBean("remoteBaseInfoService");
        mycoins = remoteBaseInfoService.findCustomerCollection(user.getCustomerId());
        JSONObject obj = new JSONObject();
        obj.put("areaname", "mycollection");
        obj.put("areanameview", SpringContextUtil.diff("zixuan"));

        //创建list
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        if (mycoins != null && mycoins.size() > 0) {
          for (String coinCode : mycoins) {
            //遍历
            for (Coin coin : productList) {
              if (coinCode.equals(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode())) {
                JSONObject data = createData(coin);
                data.put("usdttormb", usdttormb);
                data.put("isShoucang", true);
                list.add(data);
              }
            }
          }
        }

        obj.put("data", list);
        //加入map中
        jsonArray.add(obj);
      }
      //增加交易区
      for (Coin coin : productList) {
        coinarea.add(coin.getFixPriceCoinCode());
      }

      if (coinarea.size() > 0) {
        Iterator<String> it = coinarea.iterator();
        while (it.hasNext()) {
          String areaname = it.next();

          JSONObject obj = new JSONObject();
          obj.put("areaname", areaname);
          obj.put("areanameview", areaname + "  " + SpringContextUtil.diff("jiaoyiqu"));

          //创建list
          ArrayList<JSONObject> list = new ArrayList<JSONObject>();
          //遍历
          for (Coin coin : productList) {
            if (areaname.equals(coin.getFixPriceCoinCode())) {
              JSONObject data = createData(coin);
              data.put("usdttormb", usdttormb);
              if (mycoins != null && mycoins.size() > 0) {
                for (String coinCode : mycoins) {
                  if (coinCode.equals(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode())) {
                    data.put("isShoucang", true);
                  }
                }
              }

              list.add(data);
            }
          }
          obj.put("data", list);
          //加入map中
          jsonArray.add(obj);
        }
      }


    }
    JsonResult jsonResult = new JsonResult();
    jsonResult.setSuccess(true).setObj(jsonArray);
    return jsonResult;
  }

  /**
   * 手机注册ajax方法
   */
  @RequestMapping("registService2")
  @ApiOperation(value = "手机注册", httpMethod = "POST", response = JsonResult.class, notes = "参数  mobile : 手机,password : md5.md5(密码),registCode : 图像验证码,registSmsCode : 短信验证码,country : 地区号,referralCode : 推荐码")
  @ResponseBody
  @ApiImplicitParams({
      @ApiImplicitParam(name = "mobile", value = "mobile", required = true, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "country", value = "country", required = true, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "password", value = "password", required = true, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "registCode", value = "registCode", required = true, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "registSmsCode", value = "registSmsCode", required = true, dataType = "string", paramType = "query")
  })
  public JsonResult registService2(HttpServletRequest request, HttpServletResponse response) {
    String language = (String) request.getAttribute("language");
    Locale locale = LocaleContextHolder.getLocale();

    // 手机号
    String mobile = request.getParameter("mobile");
    // 国家
    String country = request.getParameter("country");
    // 密码
    String password = request.getParameter("password");
    // 图形验证码
    String registCode = request.getParameter("registCode");
    // 手机验证码
    String registSmsCode = request.getParameter("registSmsCode");
    // 邀请码
    String referralCode = request.getParameter("referralCode");

    if (StringUtils.isEmpty(mobile)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("telphone_is_not_null"));
    } else {// 去空格
      mobile = mobile.trim().replace(" ", "");
    }
    if (StringUtils.isEmpty(password)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("pwd_is_not_null"));
    }
//		if (StringUtils.isEmpty(registCode)) {
//			return new JsonResult().setMsg(SpringContextUtil.diff("tx_is_not_null"));
//		}
//		// session图形验证码
//		String session_registcode =  redisService.get("regCode");
//		if (!registCode.equalsIgnoreCase(session_registcode)) {
//			return new JsonResult().setMsg(SpringContextUtil.diff("tx_error"));
//		}
    // session手机验证码
    String session_registSmsCode = redisService.get("mobile_registSmsCode:" + mobile);
    if (!registSmsCode.equalsIgnoreCase(session_registSmsCode)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_code_error"));
    }

    if (!"".equals(referralCode) && referralCode != null) {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult selectPhone = remoteManageService.selectAgent(referralCode);
      if (!selectPhone.getSuccess()) {
        return new JsonResult().setMsg(SpringContextUtil.diff("dailisbucunzai"));
      }
    }
    try {
      RedisService redisService = SpringContextUtil.getBean("redisService");
      String config = redisService.get("configCache:all");
      JSONObject parseObject = JSONObject.parseObject(config);
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService.registMobile(mobile, password, referralCode, country,
          parseObject.get("language_code").toString());

      if (regist != null) {
        if (regist.getSuccess()) {
          // 删除验证码
          request.getSession().removeAttribute("registCode");
          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
        } else {
          return new JsonResult().setMsg(SpringContextUtil.diff(regist.getMsg()));
        }
      }
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
    } catch (Exception e) {
      log.error("注册方法远程调用出错");
    }

    return new JsonResult();

  }

  private JsonResult verificationOrder(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String ip = IpUtil.getIp(request);
    RedisService redisService = SpringContextUtil.getBean("redisService");
    // Integer telTime =
    // Integer.valueOf(PropertiesUtils.APP.getProperty("telTime"));
    // Integer telCount =
    // Integer.valueOf(PropertiesUtils.APP.getProperty("telCount"));
    Integer telTime = 5;
    Integer telCount = 3;
    String telValue = redisService.get("tel:" + ip);
    if (telValue == null || "".equals(telValue)) {
      redisService.save("tel:" + ip, "1", telTime);
    } else {
      Integer num = Integer.valueOf(telValue);
      if (num >= telCount) {
        jsonResult.setCode("0000");
        jsonResult.setMsg(SpringContextUtil.diff("sms_tooMuch"));
        jsonResult.setSuccess(false);
        return jsonResult;
      }
      num++;
      redisService.save("tel:" + ip, String.valueOf(num), telTime);
    }

    return jsonResult.setSuccess(true);

  }

  /**
   * 获得手机二次认证短信
   */
  @RequestMapping(value = "/smsPhone", method = RequestMethod.POST)
  @ApiOperation(value = "获得手机二次认证短信", httpMethod = "POST", response = JsonResult.class, notes = "username 用户名")
  @ResponseBody
  public JsonResult smsPhone(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String username = "";
    username = request.getParameter("username");
    String type = request.getParameter("type");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    String mobile = "";
    String addressqu = "";
    String address = null;
    if (username != null) {
      String language = (String) request.getAttribute("language");
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      User user = remoteManageService.selectByTel(username);
      mobile = user.getPhone();
      addressqu = user.getCountry();
    } else {
      User user = SessionUtils.getUser(request);
      mobile = user.getPhone();
      username = user.getUsername();
      addressqu = user.getCountry();
    }

    if (StringUtils.isEmpty(mobile)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg(SpringContextUtil.diff("withdrawal_no_null"));
    }
    String mobi = mobile.replace(" ", "");
    // 手机号
    String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);

    // 地区截取
//		String address = mobile.substring(0, mobile.indexOf(" "));
    address = mobile.substring(0, mobile.indexOf(" "));
    addressqu = address.substring(address.lastIndexOf("+") + 1);

    // 设置短信验证码到session中
    SmsParam smsParam = new SmsParam();
    smsParam.setHryMobilephone(mobile);

    if (addressqu.equals("86")) {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, phone);
      RedisService redisService = SpringContextUtil.getBean("redisService");
      if (null != type && "change".equals(type)) {
        redisService.save("genghuan" + username, sendSmsCodeHai, 120);
      }

      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      // request.getSession().setAttribute("accountpwSmsCode",
      // SmsSendUtil.sendSmsCodeHai(smsParam, addressqu,phone));
    } else {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, addressqu, mobi);
      RedisService redisService = SpringContextUtil.getBean("redisService");
      redisService.save("SMS:smsphone:" + mobile, sendSmsCodeHai, 120);
      // request.getSession().setAttribute("accountpwSmsCode",
      // SmsSendUtil.sendSmsCodeHai(smsParam, addressqu,mobi));

    }
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    return jsonResult;
  }

  @RequestMapping(value = "/registSmsCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "注册短信验证码发送", httpMethod = "POST", response = JsonResult.class, notes = "country 地区号, userName用户名")
  @ResponseBody
  @ApiImplicitParams({
      @ApiImplicitParam(name = "country", value = "country", required = true, dataType = "string", paramType = "query")})
  public JsonResult registSmsCode(HttpServletRequest request, @RequestParam String userName) {

    JsonResult jsonResult = new JsonResult();
    String country = request.getParameter("country"); // 国家
    // 获得图形验证码
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("regCode");

    if (StringUtils.isEmpty(userName)) {// 手机号判空
      jsonResult.setSuccess(false);
      jsonResult.setMsg("手机号不能为空");
    }
// else if (StringUtils.isEmpty(registCode)) {// 验证码判空
//			jsonResult.setSuccess(false);
//			jsonResult.setMsg("图形验证码不能为空");
//		} else if (StringUtils.isEmpty(registCode) || !registCode.equalsIgnoreCase(value)) {// 验证码判正确性
//			jsonResult.setMsg("图形验证码不正确");
//			jsonResult.setSuccess(false);
//			return jsonResult;
//		}
    else {
      // 设置短信验证码到session中
      SmsParam smsParam = new SmsParam();
      if (StringUtils.isEmpty(country)) {
        smsParam.setHryMobilephone(userName);
      } else {
        smsParam.setHryMobilephone(country + " " + userName);
      }
//			smsParam.setHrySmstype(SmsSendUtil.REGIST);
//			String sendSmsCode = SmsSendUtil.sendSmsCode(smsParam);
      String sendSmsCode;
      if (country.equals("+86")) {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
        sendSmsCode = SmsSendUtil.sendSmsCode(smsParam, country.split("/+")[0], userName);
      } else {
        smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
        sendSmsCode = SmsSendUtil.sendSmsCode(smsParam, country.split("/+")[0], userName);
      }

      log.info("注册验证码：【" + sendSmsCode + "】");
      redisService.save("mobile_registSmsCode:" + userName, sendSmsCode, 120);
      jsonResult.setSuccess(true);
      jsonResult.setMsg("短信发送成功!");
    }
    return jsonResult;
  }

  @RequestMapping(value = "/regCode", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "注册页面的图形验证码", httpMethod = "GET", response = JsonResult.class, notes = "注册页面的图形验证码")
  @ResponseBody
  public JsonResult regCode(HttpServletResponse response, HttpServletRequest request) {
    DrawPictureUtil drawPictureUtil = new DrawPictureUtil("regCode", 100, 30);
    drawPictureUtil.darw(request, response);
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("regCode");
    return new JsonResult().setSuccess(true).setObj(value);
  }

  @RequestMapping(value = "/getContent", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "网站公告详情页", httpMethod = "POST", response = JsonResult.class, notes = "网站公告详情页")
  @ResponseBody
  public JsonResult getContent(String id) {
    if (id != null && !"".equals(id)) {
      RemoteAppArticleService remoteAppArticleService = SpringContextUtil
          .getBean("remoteAppArticleService");
      Article article = remoteAppArticleService.getHelpArtic(id);
      return new JsonResult().setSuccess(true).setObj(article);
    }
    return new JsonResult();
  }

  @RequestMapping(value = "/forgetSmsCode", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "找回密码发送验证码", httpMethod = "POST", response = JsonResult.class, notes = "email为邮箱")
  @ResponseBody
  public JsonResult forgetService(HttpServletRequest request) {
    String ip = IpUtil.getIp(request);
    log.info("找回密码ip地址" + ip);
    System.out.println("找回密码ip地址" + ip);

    // 邮箱
    String email = request.getParameter("email");
    if (StringUtils.isEmpty(email)) {
      return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
    }

    int count = 0;
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String val = redisService.get("ip:forpwd:" + ip);
    if (!StringUtils.isEmpty(val)) {
      String num = redisService.get("ip:forpwd:" + ip);
      count = Integer.parseInt(num);
    } else {
      redisService.save("ip:forpwd:" + ip, "1", 60 * 60 * 24);
    }

    if (count <= 5) {

      String language = (String) request.getAttribute("language");
      Locale locale = LocaleContextHolder.getLocale();
      if (StringUtils.isEmpty(email)) {
        return new JsonResult().setMsg(SpringContextUtil.diff("tel_is_not_null"));
      }
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

      User selectByTel = remoteManageService.selectByTel(email);
      if (StringUtils.isEmpty(selectByTel.getCustomerId())) {
        return new JsonResult().setMsg(SpringContextUtil.diff("userisnull"));

      }
      RemoteManageService manageService = SpringContextUtil.getBean("remoteManageService");
      RemoteResult regist = manageService.forget(email, "");
      if (regist != null) {
        if (regist.getSuccess()) {
          StringBuffer sb = new StringBuffer();
          sb.append(SpringContextUtil.diff("dear") + " " + email + "<br><br>" + SpringContextUtil
              .diff("Resetfirstemail") + "<br><br>" + SpringContextUtil.diff("Resettwoemail")
              + "<br><br>");
          sb.append(PropertiesUtils.APP.getProperty("app.url"));
          sb.append(
              "/resetPassword/" + "/" + regist.getObj() + "/" + locale.toString() + "<br><br>");
          String type = "3";
          ThreadPool.exe(new EmailRunnable(email, sb.toString(), type, locale));
          // 发送邮件
          // 计时，time秒后 找回密码链接失效
          String config = redisService.get("configCache:all");
          JSONObject parseObject = JSONObject.parseObject(config);
          Integer time =
              parseObject.get("resetPwdTime") == null ? 60 : parseObject.getInteger("resetPwdTime");
          redisService.save("forget:" + regist.getObj().toString(), email, time * 60);

          redisService.save("ip:forpwd:" + ip, count + 1 + "", 60 * 60 * 24);

          return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
        }
      } else {
        return new JsonResult().setSuccess(false).setMsg("失败");

      }
      return new JsonResult().setSuccess(true).setMsg(SpringContextUtil.diff("reg_success"));
    }
    return new JsonResult().setSuccess(false).setMsg("找回加次数过多，请一天后再试");

  }

  @RequestMapping(value = "/firststep", method = RequestMethod.POST)
  @ApiOperation(value = "找回密码第一步", httpMethod = "POST", response = JsonResult.class, notes =
      "tel为手机号码,forgetpwdCode为输入验证码,"
          + "跳转到第二步,5分钟内没有修改密码操作，即为超时失效")
  @ResponseBody
  public JsonResult firststep(HttpServletRequest request, @RequestParam String tel,
      @RequestParam String forgetpwdCode) {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    String value = redisService.get("smsfindloginpw" + tel);

    if (StringUtils.isEmpty(tel)) {
      return jsonResult.setSuccess(false).setMsg("手机号不能为空");
    } else if (StringUtils.isEmpty(forgetpwdCode)) {
      return jsonResult.setSuccess(false).setMsg("验证码不能为空");
    } else if (forgetpwdCode.equalsIgnoreCase(value)) {
      return jsonResult.setSuccess(false).setMsg("验证码不正确");
    } else {
      redisService.delete("smsfindloginpw" + tel);
      redisService.save("forpwd:" + tel, tel, 300);

      return jsonResult.setSuccess(true).setObj(tel);
    }
  }

  @RequestMapping(value = "/fndpw", method = RequestMethod.POST)
  @ApiOperation(value = "找回密码", httpMethod = "POST", response = JsonResult.class, notes = "password为密码,username为手机号码,forgetpwdCode验证码")
  @ResponseBody
  public JsonResult fndpw(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");

    // 手机号码
    String username = request.getParameter("username");
    // 密码
    String password = request.getParameter("password");

    // 密码
    String forgetpwdCode = request.getParameter("forgetpwdCode");

    String redis_forgetpwdCode = redisService.get("SMS:smsfindloginpw:" + username);

    if (StringUtils.isEmpty(username)) {
      return jsonResult.setSuccess(false).setMsg("手机号不能为空");
    } else if (StringUtils.isEmpty(forgetpwdCode)) {
      return jsonResult.setSuccess(false).setMsg("验证码不能为空");
    }

    if (!forgetpwdCode.equals(redis_forgetpwdCode)) {
      return jsonResult.setSuccess(false).setMsg("验证码不正确！");
    }

    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    remoteManageService.updatepwd(password, username);
    return jsonResult.setSuccess(true);

  }

  @RequestMapping(value = "/secondstep", method = RequestMethod.POST)
  @ApiOperation(value = "找回密码第二步", httpMethod = "POST", response = JsonResult.class, notes = "passwd为密码,tel为手机号码")
  @ResponseBody
  public JsonResult secondstep(HttpServletRequest request, @RequestParam String passwd,
      @RequestParam String tel) {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");

    String backTel = redisService.get("forpwd:" + tel);
    if (backTel == null) {
      return jsonResult.setSuccess(false).setMsg("已超时");
    } else {
      RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
      remoteManageService.updatepwd(passwd, backTel);
      return jsonResult.setSuccess(true);
    }
  }

  @RequestMapping(value = "/logo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "获取logo地址", httpMethod = "POST", response = JsonResult.class, notes = "获取logo地址")
  @ResponseBody
  public JsonResult logo() {
    JsonResult jsonResult = new JsonResult();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    //获取logo地址
    String config = redisService.get("configCache:all");
    JSONObject parseObject = JSONObject.parseObject(config);
    String siteLogo = (String) parseObject.get("siteLogo");
    return new JsonResult().setObj(siteLogo);
  }

  @RequestMapping(value = "/buy_selling", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "买卖委托记录", httpMethod = "POST", response = JsonResult.class, notes = "买卖委托记录")
  @ResponseBody
  public JsonResult buy_selling(@RequestParam String symbol) {
    RedisService redisService = SpringContextUtil.getBean("redisService");
    //获取logo地址
    String pushEntrusMarket = redisService.get(symbol + ":pushEntrusMarket");

    return new JsonResult().setObj(pushEntrusMarket);
  }


  @RequestMapping(value = "/appgetAccountInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "根据币种查找委托手续费", httpMethod = "POST", response = JsonResult.class, notes = "")
  @ResponseBody
  public JsonResult appgetAccountInfo(HttpServletRequest request, @RequestParam String symbol) {
    if (symbol == null || symbol.equals("")) {
      return new JsonResult().setSuccess(false).setMsg("货币类型不能为空");
    }
    String cion[] = symbol.split("_");
    JSONObject json = new JSONObject();
    RedisService redisService = SpringContextUtil.getBean("redisService");
    RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil
        .getBean("remoteManageService");
    String tokenId = request.getParameter("tokenId");
    if (tokenId != null && !tokenId.equals("")) {
      String value = redisService.get("mobile:" + tokenId);
      String tel = JSONObject.parseObject(value).getString("mobile");

      User user = remoteManageService.selectByTel(tel);
      if (user != null) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询账户金额
        MyAccountTO myAccount = remoteManageService.myAccount(user.getCustomerId());
        json.put("	", myAccount);
        // 查询币账户
        List<CoinAccount> findCoinAccount = remoteManageService
            .findCoinAccount(user.getCustomerId());
        for (CoinAccount coinAccount : findCoinAccount) {
          if (coinAccount.getCoinCode().equals(cion[0])) {
            json.put("coinAccount", coinAccount);
          }
          if (coinAccount.getCoinCode().equals(cion[1])) {
            json.put("myAccount", coinAccount);
          }
        }

      }
    }
    if (symbol.contains("_")) {

      try {
        RemoteResult result = remoteManageService.appgetAccountInfo(cion[0], cion[1]);
        if (result.getSuccess()) {
          JSONObject coinFee = JSONObject.parseObject(result.getObj().toString());
          json.put("coinFee", coinFee);
        }
      } catch (Exception e) {
      }
    }
    return new JsonResult().setSuccess(true).setObj(json);
  }


  @Autowired
  private CookieLocaleResolver resolver;

  /**
   * 语言切换
   */
  @RequestMapping("language")
  public ModelAndView language(HttpServletRequest request, HttpServletResponse response) {
    String language = request.getParameter("language").toLowerCase();
    String split_ = request.getParameter("split");

    if (language == null || language.equals("")) {
      return new ModelAndView("redirect:/");
    } else {
      String[] split = language.split("_");
      if (split.length > 1) {
        resolver.setLocale(request, response, new Locale(split[0], split[1]));
      } else {
        resolver.setLocale(request, response, new Locale(split[0]));
      }
      return new ModelAndView("redirect:" + split_);
    }
  }

  /**
   * 创建交易区的一个交易对的data
   */
  private JSONObject createData(Coin coin) {

    //交易币的位数
    Integer keepCoin = coin.getKeepDecimalForCoin();
    //定价币的位数
    Integer keepCurrency = coin.getKeepDecimalForCurrency();
    int zeroLength = 2;
    //交易币的格式化
    String keepCoinFormat = "0.00";
    //定价币的格式化
    String keepCurrencyFormat = "0.00";
    if (keepCoin > zeroLength) {
      keepCoinFormat = "0.";
      for (int i = 1; i <= keepCoin; i++) {
        keepCoinFormat = keepCoinFormat += "0";
      }
    }
    if (keepCurrency > zeroLength) {
      keepCurrencyFormat = "0.";
      for (int i = 1; i <= keepCurrency; i++) {
        keepCurrencyFormat = keepCurrencyFormat += "0";
      }
    }
    DecimalFormat decimalFormatCoin = new DecimalFormat(keepCoinFormat);
    DecimalFormat decimalFormatCurrency = new DecimalFormat(keepCurrencyFormat);

    JSONObject data = new JSONObject();
    data.put("coinCode", coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
    data.put("name", coin.getCoinCode() + "_" + coin.getFixPriceCoinCode());
    data.put("picturePath", coin.getPicturePath());

    String currentExchangPrice_str = redisService
        .get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode() + ":currentExchangPrice");
    if (!StringUtils.isEmpty(currentExchangPrice_str)) {
      data.put("currentExchangPrice",
          decimalFormatCurrency.format(new BigDecimal(currentExchangPrice_str)));
      if ("USDT".equals(coin.getFixPriceCoinCode())) {
        data.put("usdtcount", new BigDecimal(currentExchangPrice_str));
      } else {
        //如果当前币对usdt有价格
        String usdtprice = redisService
            .get(coin.getFixPriceCoinCode() + "_USDT" + ":currentExchangPrice");
        if (!StringUtils.isEmpty(usdtprice)) {
          data.put("usdtcount",
              new BigDecimal(currentExchangPrice_str).multiply(new BigDecimal(usdtprice)));
        } else {
          data.put("usdtcount", 0);
        }
      }
    } else {
      data.put("usdtcount", 0);
      data.put("currentExchangPrice", 0);
    }

    // 昨日收盘价
    String coinStr = redisService.get("cn:coinInfoList2");
    String coinCode = coin.getCoinCode() + "_" + coin.getFixPriceCoinCode();
    BigDecimal yesterdayPrice = new BigDecimal(0);
    if (!StringUtils.isEmpty(coinStr)) {
      List<Coin2> coins = JSON.parseArray(coinStr, Coin2.class);
      for (Coin2 c : coins) {
        if (coinCode.equals(c.getCoinCode() + "_" + c.getFixPriceCoinCode())) {
          if (!StringUtils.isEmpty(c.getYesterdayPrice())) {
            yesterdayPrice = new BigDecimal(c.getYesterdayPrice());
          }
        }
      }
    }
    data.put("yesterdayPrice", decimalFormatCurrency.format(yesterdayPrice));

    String str = redisService
        .get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode() + ":PeriodLastKLineList");
    if (str != null) {
      JSONArray jsonv = JSON.parseArray(str);
      //System.out.println(jsonv.getString(5));
      if (jsonv.getString(5) != null) {
        JSONObject jsonv_ = JSON.parseObject(jsonv.getString(5));
        if ("1day".equals(jsonv_.getString("period"))) {

          BigDecimal currentExchangPrice = new BigDecimal(0);
          //上一笔交易价格
          String orders = redisService.get(coin.getCoinCode() + "_" + coin.getFixPriceCoinCode()
              + ":pushNewListRecordMarketDesc");
          if (!StringUtils.isEmpty(orders)) {
            MarketTrades marketTrades = com.alibaba.fastjson.JSON
                .parseObject(orders, MarketTrades.class);
            // 最新价格
            if (marketTrades != null) {
              List<MarketTradesSub> trades = marketTrades.getTrades();
              if (trades != null) {
                if (trades.size() > 1) {
                  MarketTradesSub marketTradesSub0 = trades.get(0);
                  data.put("currentExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub0.getPrice()));
                  currentExchangPrice = marketTradesSub0.getPrice();

                  MarketTradesSub marketTradesSub1 = trades.get(1);
                  data.put("lastExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub1.getPrice()));
                } else {

                  MarketTradesSub marketTradesSub0 = trades.get(0);
                  data.put("currentExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub0.getPrice()));
                  currentExchangPrice = marketTradesSub0.getPrice();

                  data.put("lastExchangPrice",
                      decimalFormatCurrency.format(marketTradesSub0.getPrice()));
                }
              } else {
                data.put("lastExchangPrice", "1");
              }
            } else {
              data.put("lastExchangPrice", "1");
            }
          } else {
            data.put("lastExchangPrice", "1");
          }
          //当日成交总量
          data.put("transactionSum",
              decimalFormatCoin.format(new BigDecimal(jsonv_.getString("amount"))));

          data.put("maxPrice", jsonv_.getString("priceHigh"));
          data.put("minPrice", jsonv_.getString("priceLow"));
          // 开盘价
          data.put("openPrice",
              decimalFormatCurrency.format(new BigDecimal(jsonv_.getString("priceOpen"))));

          if (BigDecimal.ZERO.compareTo(yesterdayPrice) != 0) {
            if (BigDecimal.ZERO.compareTo(currentExchangPrice) != 0) {
              BigDecimal divide = (currentExchangPrice.subtract(yesterdayPrice))
                  .divide(yesterdayPrice, 5, BigDecimal.ROUND_HALF_DOWN)
                  .multiply(new BigDecimal(100));
              data.put("RiseAndFall", divide);
            } else {
              data.put("RiseAndFall", 0);
            }
          } else {
            data.put("RiseAndFall", 0);
          }


        } else {
          data.put("lastExchangPrice", 0);
          data.put("transactionSum", 0);
          data.put("maxPrice", 0);
          data.put("minPrice", 0);
          // 开盘价
          data.put("openPrice", new BigDecimal(0));
          data.put("lastEndPrice", 0);
          data.put("RiseAndFall", 0);
        }
      } else {
        data.put("lastExchangPrice", 0);
        data.put("transactionSum", 0);
        data.put("maxPrice", 0);
        data.put("minPrice", 0);
        // 开盘价
        data.put("openPrice", new BigDecimal(0));

        data.put("lastEndPrice", 0);
        data.put("RiseAndFall", 0);
      }
    } else {
      data.put("lastExchangPrice", 0);
      data.put("transactionSum", 0);
      data.put("maxPrice", 0);
      data.put("minPrice", 0);
      // 开盘价
      data.put("openPrice", new BigDecimal(0));

      data.put("lastEndPrice", 0);
      data.put("RiseAndFall", 0);
    }

    return data;

  }

  /**
   * 忘记密码获取验证码
   */
  @RequestMapping(value = "/sendForgetSmsCode", method = RequestMethod.POST)
  @ApiOperation(value = "手机忘记密码发送认证码", httpMethod = "POST", response = JsonResult.class, notes = "phoneNum 手机号,address 地区如+86")
  @ResponseBody
  public JsonResult sendForgetSmsCode(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String username = "";
    username = request.getParameter("phoneNum");
    String address = request.getParameter("address");
    if (!verificationOrder(request).getSuccess()) {
      return verificationOrder(request);
    }

    String mobile = "";
    String addressqu = "";

    String language = (String) request.getAttribute("language");
    RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
    User user = remoteManageService.selectByTel(username);
    if (user.getUsername() == null) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("账户不存在，请注册！");
      return jsonResult;
    }
    mobile = user.getPhone();
    addressqu = user.getCountry();

    if (StringUtils.isEmpty(mobile)) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg(SpringContextUtil.diff("withdrawal_no_null"));
    }
    String mobi = mobile.replace(" ", "");
    // 手机号
    String phone = mobile.substring(mobile.lastIndexOf(" ") + 1);

    // 地区截取
    //String address = mobile.substring(0, mobile.indexOf(" "));

    // 设置短信验证码到session中
    SmsParam smsParam = new SmsParam();
    smsParam.setHryMobilephone(mobile);

    if (address.equals("+86")) {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_PHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, phone);
      redisService.save("SMS:smsfindloginpw:" + username, sendSmsCodeHai, 120);
    } else {
      smsParam.setHrySmstype(SmsSendUtil.TAKE_ENPHONE);
      String sendSmsCodeHai = SmsSendUtil.sendSmsCode(smsParam, address, mobi);
      redisService.save("SMS:smsfindloginpw:" + username, sendSmsCodeHai, 120);

    }
    jsonResult.setSuccess(true);
    jsonResult.setMsg(SpringContextUtil.diff("sms_success"));

    return jsonResult;
  }


  @RequestMapping(value = "/selectIosVersion", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
  @ApiOperation(value = "查询IOS版本", httpMethod = "GET", response = JsonResult.class, notes = "查询IOS版本")
  @ResponseBody
  public JsonResult selectIosVersion(HttpServletRequest request) {
    String syscode = request.getParameter("syscode");
    if (null == syscode) {
      syscode = "Ios_Version";
    }
    String sysCodeValueLists = redisService.get("cn:SysCodeValueList");
    String value = null;
    String codename = null;
    String Codedescription = null;
    String Enable_flag = null;
    if (!StringUtils.isEmpty(sysCodeValueLists)) {
      List<SysCodeValueManage> sysCodeValuelist = JSONArray
          .parseArray(sysCodeValueLists, SysCodeValueManage.class);
      for (SysCodeValueManage sysCodeValue : sysCodeValuelist) {
        if (sysCodeValue.getCode().equals(syscode)) {
          value = sysCodeValue.getValue();
          codename = sysCodeValue.getCode_name();
          Codedescription = sysCodeValue.getCode_description();
          Enable_flag = sysCodeValue.getEnable_flag();
        }
      }
    }
    JSONObject data = new JSONObject();
    data.put("syscode", syscode);
    data.put("codename", codename);
    data.put("codedescription", Codedescription);
    data.put("value", value);
    data.put("Enable_flag", Enable_flag);
    return new JsonResult().setSuccess(true).setObj(data);
  }


}
