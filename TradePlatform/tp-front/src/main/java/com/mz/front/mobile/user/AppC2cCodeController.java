package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.util.shiro.PasswordHelper;
import com.mz.front.redis.model.UserRedis;
import com.mz.front.user.controller.C2cController;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.c2c.C2cOrder;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

;

@Controller
@RequestMapping(value = "/mobile/user/appC2c")
@Api(value = "C2C操作类", description = "C2C管理")
public class AppC2cCodeController {
    private final static Logger log = Logger.getLogger(C2cController.class);

    /**
     * 注册类型属性编辑器
     *
     * @param binder
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


    @Autowired
    private RedisService redisService;

    /**
     * 确认到账
     *
     * @param request
     * @param transactionNum
     * @return
     */
    @RequestMapping("c2c/confirm")
    @ResponseBody
    @ApiOperation(value = "确认到账", response = JsonResult.class, notes = "返回成功或失败，没obj")
    public JsonResult Confirm(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        String transactionNum = request.getParameter("transactionNum");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        boolean flag = remoteManageService.setc2cTransactionStatus(transactionNum, 2, null);
        if (flag) {
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setMsg("订单号错误");
        }
        return jsonResult;
    }

    /**
     * 支付完成
     *
     * @param request
     * @param transactionNum
     * @return
     */
    @RequestMapping("c2c/payc2cTransaction")
    @ApiOperation(value = "c2c支付完成", httpMethod = "POST", response = JsonResult.class, notes = "返回成功或失败，没obj")
    @ResponseBody
    public JsonResult payc2cTransaction(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        String transactionNum = request.getParameter("transactionNum");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        boolean flag = remoteManageService.setc2cTransactionStatus2(transactionNum, 2, null);
        if (flag) {
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setMsg("订单号错误");
        }
        return jsonResult;
    }

    /**
     * 查看c2c订单详情
     *
     * @param request
     * @param transactionNum
     * @return
     */
    @RequestMapping("c2c/getc2cTransaction")
    @ApiOperation(value = "c2c查看c2c订单详情", httpMethod = "POST", response = JsonResult.class, notes = "")
    @ResponseBody
    public JsonResult getc2cTransaction(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String transactionNum = request.getParameter("transactionNum");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String c2cOrderDetail = remoteManageService.getC2cOrderDetail(transactionNum);
        if (!StringUtils.isEmpty(c2cOrderDetail)) {
            JSONObject obj = JSON.parseObject(c2cOrderDetail);
            jsonResult.setObj(obj);
        }

        return jsonResult.setSuccess(true);
    }

    /**
     * 交易关闭
     *
     * @param request
     * @param transactionNum
     * @return
     */
    @RequestMapping("c2c/closec2cTransaction")
    @ApiOperation(value = "c2c交易关闭", httpMethod = "POST", response = JsonResult.class, notes = "")
    @ResponseBody
    public JsonResult closec2cTransaction(HttpServletRequest request) {

        String remark = request.getParameter("remark");
        JsonResult jsonResult = new JsonResult();
        String transactionNum = request.getParameter("transactionNum");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        boolean flag = remoteManageService.setc2cTransactionStatus2(transactionNum, 4, remark);
        if (flag) {
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setMsg("订单号错误");
        }
        return jsonResult;
    }

    /**
     * 交易失败
     *
     * @param request
     * @param transactionNum
     * @return
     */
    @RequestMapping("user/failc2cTransaction")
    @ApiOperation(value = "c2c交易失败", httpMethod = "POST", response = JsonResult.class, notes = "")
    @ResponseBody
    public JsonResult failc2cTransaction(HttpServletRequest request) {

        String remark = request.getParameter("remark");
        JsonResult jsonResult = new JsonResult();
        String transactionNum = request.getParameter("transactionNum");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        boolean flag = remoteManageService.setc2cTransactionStatus2(transactionNum, 3, remark);
        if (flag) {
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setMsg("订单号错误");
        }
        return jsonResult;
    }


    //c2c交易页面数据
    @RequestMapping("c2c")
    @ApiOperation(value = "c2c页面数据", httpMethod = "POST", response = JsonResult.class, notes = "tokenId,coinCode 交易的币种")
    @ResponseBody
    public JsonResult c2cinfo(HttpServletRequest request) {
        String coinCode = request.getParameter("coinCode");
        JsonResult jsonResult = new JsonResult();
        HashMap<String, Object> map = new HashMap<String, Object>();
        //查询所有开通c2c的币
        String str = redisService.get("cn:c2clist");
        if (!StringUtils.isEmpty(str)) {
            List<String> list = JSON.parseArray(str, String.class);
            map.put("coinList", list);
        }
        if (coinCode != null) {
            //当前激活的节点
            map.put("activeCoin", coinCode);

            //获得产品表配置信息
            String productinfoListall = redisService.get("cn:c2cCoinList");
            if (!StringUtils.isEmpty(productinfoListall)) {
                JSONArray parseArray = JSON.parseArray(productinfoListall);
                if (parseArray != null) {
                    for (int i = 0; i < parseArray.size(); i++) {
                        JSONObject jsonObject = parseArray.getJSONObject(i);
                        if (coinCode.equals(jsonObject.getString("coinCode"))) {
                            map.put("c2cBuyPrice", jsonObject.getBigDecimal("buyPrice"));
                            map.put("c2cSellPrice", jsonObject.getBigDecimal("sellPrice"));
                        }
                    }
                }
            }

            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");

            List<C2cOrder> buyList = remoteManageService.c2cNewBuyOrder();
            map.put("buyList", buyList);
            List<C2cOrder> sellList = remoteManageService.c2cNewSellOrder();
            map.put("sellList", sellList);

            //如果登录了，查询最近10条c2c记录
            String tokenId = request.getParameter("tokenId");
            RedisService redisService = SpringContextUtil.getBean("redisService");
            String value = redisService.get("mobile:" + tokenId);
            if (value != null) {
                String tel = JSONObject.parseObject(value).getString("mobile");
                User user = remoteManageService.selectByTel(tel);
                if (user != null) {
                    map.put("user", user);
                    List<C2cOrder> listOrder = remoteManageService.c2c10Order(user.getCustomerId(), coinCode);
                    if (listOrder != null) {
                        map.put("orderList", listOrder);
                    }
                }
            }
        }
        return jsonResult.setObj(map).setSuccess(true);
    }


    //买卖下单
    @RequestMapping("c2c/AppCreateTransaction")
    @ApiOperation(value = "c2c买卖下单", httpMethod = "POST", response = JsonResult.class, notes = "tokenId,coinCode 交易的币种,transactionType 买1卖2，transactionCount 数量，transactionPrice 价格 accountPassWord 交易密码，verifyCode 短信验证码 ")
    @ResponseBody
    public JsonResult createTransaction(HttpServletRequest request) {

        String coinCode = request.getParameter("coinCode");
        String transactionType = request.getParameter("transactionType");
        String transactionCount = request.getParameter("transactionCount");
        String transactionPrice = request.getParameter("transactionPrice");
        String accountPassWord = request.getParameter("accountPassWord");
        String verifyCode = request.getParameter("verifyCode");

        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            User user = remoteManageService.selectByTel(tel);
            if (user != null) {

                //判断是否实名
                if(user.getStates()!=2){
                    if(user.getStates() == 1){
                        return new JsonResult().setMsg("实名认证审核中!");
                    }else {
                        return new JsonResult().setMsg("请到个人中心进行实名!");
                    }
                }


                if(("2").equals(transactionType)) {
                    //判断是否手机认证
                    if (user.getPhoneState() != 1) {
                        return new JsonResult().setMsg("请到个人中心进行绑定手机!");
                    }

                    //
                    if (user.getAccountPassWord() == null || "".equals(user.getAccountPassWord()) ) {
                        return new JsonResult().setSuccess(false).setObj(user).setMsg("请到个人中心设置交易密码！");
                    }

                    String session_verifyCode = redisService.get("SMS:smsphone:" + user.getPhone());
                    if (!verifyCode.equals(session_verifyCode)) {
                        return new JsonResult().setMsg(SpringContextUtil.diff("短信验证错误或已失效！"));
                    }

                    if (user.getAccountPassWord() != null && !"".equals(user.getAccountPassWord())) {
                        PasswordHelper passwordHelper = new PasswordHelper();
                        String pw = passwordHelper.encryString(accountPassWord, user.getSalt());
                        if (!pw.equals(user.getAccountPassWord())) {
                            return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("mimacuowu"));
                        }
                    }
                }

                C2cOrder c2cOrder = new C2cOrder();
                //交易币种
                c2cOrder.setCoinCode(coinCode);
                //交易单号
                c2cOrder.setTransactionNum(UUID.randomUUID().toString().replace("-", ""));
                //交易数量
                c2cOrder.setTransactionCount(new BigDecimal(transactionCount));
                //交易价格
                c2cOrder.setTransactionPrice(new BigDecimal(transactionPrice));
                //用户名
                c2cOrder.setUserName(user.getUsername());
                //交易类型1买，2卖
                c2cOrder.setTransactionType(Integer.valueOf(transactionType));
                //customerId
                c2cOrder.setCustomerId(user.getCustomerId());
                RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
                UserRedis userRedis = redisUtil.get(user.getCustomerId().toString());
                Long coinAccountId = userRedis.getDmAccountId(coinCode);
                //虚拟币账户id,要买的币，或者要卖的币
                c2cOrder.setAccountId(coinAccountId);

                RemoteResult remoteResult = null;
                try {
                    remoteResult = remoteManageService.createC2cOrder(c2cOrder);
                } catch (Exception e) {
                }
                if (remoteResult != null) {
                    if (remoteResult.getSuccess()) {
                        //买卖单号
                        String num = remoteResult.getObj().toString();
                        String c2cOrderDetail = remoteManageService.getC2cOrderDetail(num);

                        JSONObject obj = null;
                        if (!StringUtils.isEmpty(c2cOrderDetail)) {
                            obj = JSON.parseObject(c2cOrderDetail);
                        }
                        return new JsonResult().setSuccess(true).setObj(obj);
                    } else {
                        return new JsonResult().setMsg(remoteResult.getMsg());
                    }
                } else {
                    return new JsonResult().setMsg("remote错误");
                }

            }

            return new JsonResult();
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }

    @RequestMapping("/c2c/c2clist")
    @ApiOperation(value = "c2clist", httpMethod = "POST", response = FrontPage.class, notes = "JsonResult + FrontPage + rows")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String type = request.getParameter("transactionType");
        String tokenId = request.getParameter("tokenId");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            User user = remoteManageService.selectByTel(tel);
            Map<String, String> params = HttpServletRequestUtils.getParams(request);
            if ("0".equals(type)) {// 0查全部
                params.put("transactionType", null);
            }
            params.put("customerId", user.getCustomerId().toString());
            FrontPage findTrades = remoteManageService.c2clist(params);
            return new JsonResult().setSuccess(true).setObj(findTrades);
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }
}
