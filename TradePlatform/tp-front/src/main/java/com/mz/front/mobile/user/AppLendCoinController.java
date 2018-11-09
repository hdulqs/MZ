package com.mz.front.mobile.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.RemoteLendService;
import com.mz.manage.remote.model.Lend;
import com.mz.manage.remote.model.LendCanCoinCode;
import com.mz.manage.remote.model.LendIntent;
import com.mz.manage.remote.model.LendTimes;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.common.Constant;
import com.mz.util.sys.SpringContextUtil;;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping(value = "/mobile/user/applend")
@Api(value = "杠杆操作类")
public class AppLendCoinController {

    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/appLendTimesApply")
    @ApiOperation(value = "杠杆倍数界面数据(JsonResult + obj)", httpMethod = "POST", response = JsonResult.class, notes = "tokenId")
    @ResponseBody
    public JsonResult lendTimesApply(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        String tokenId = request.getParameter("tokenId");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (user != null) {
                RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
                //查询账户金额
                Map<String, String> myAccount = remoteLendService.getLengCoinCodeForRmbCenter(user.getCustomerId());
                map.put("coinNetAsset", myAccount.get("coinNetAsset"));
                map.put("rMBLendMoneyed", myAccount.get("rMBLendMoneyed"));
                map.put("canLendMoney", myAccount.get("canLendMoney"));
                map.put("rMBSum", myAccount.get("rMBSum"));
                map.put("lendRates", myAccount.get("lendRates"));
                map.put("rMBSum", myAccount.get("rMBSum"));
                map.put("coinCodeForRmb", myAccount.get("coinCodeForRmb"));
                map.put("lendTimes", myAccount.get("lendTimes"));
                //倍数管理
                String string = redisService.get("configCache:financeLendConfig");
                JSONObject obj = JSON.parseObject(string);
                String lendTimes = obj != null ? obj.get("lendTimesConfig").toString() : null;
                List<String> mutiple = null;
                if (!lendTimes.isEmpty()) {
                    String[] split = lendTimes.split(",");
                    mutiple = Arrays.asList(split);
                }
                map.put("mutiple", mutiple);
                return jsonResult.setObj(map).setSuccess(true);
            }
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }

    //申请杠杆倍数
    @RequestMapping(value = "/appLendTimesApplyAdd")
    @ApiOperation(value = "申请杠杆倍数(JsonResult)", httpMethod = "POST", response = JsonResult.class, notes = "tokenId,申请杠杆的倍数lendTimestr")
    @ResponseBody
    public JsonResult lendTimesApplyAdd(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            Map<String, Object> map = new HashMap<String, Object>();
            if (user != null) {
                String lendTimestr = request.getParameter("lendTimestr");
                if (StringUtil.isEmpty(lendTimestr)) {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg("失败，借款为空");
                    return jsonResult;
                }
                if (new BigDecimal(lendTimestr).compareTo(new BigDecimal(0)) <= 0) {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg("失败，借款金额小于等于0");
                    return jsonResult;
                }
                LendTimes lendTimes = new LendTimes();
                RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
                lendTimes.setCustomerId(user.getCustomerId());
                lendTimes.setUserCode(user.getUserCode());
                lendTimes.setUserName(user.getUsername());
                lendTimes.setLendTimes(new BigDecimal(lendTimestr));
                String[] relt = service.lendTimesApplyAdd(lendTimes);
                if (relt[0].equals(Constant.CODE_SUCCESS)) {
                    jsonResult.setSuccess(true).setMsg("申请成功，审核中");
                } else {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(SpringContextUtil.diff(relt[1], "zh_CN"));
                }

                return jsonResult;
            }
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }


    /**
     * 申请列表
     *
     * @return
     */
    @RequestMapping(value = "/appLendTimesApplyList")
    @ApiOperation(value = "申请杠杆倍数记录 (JsonResult + FrontPage + rows)", httpMethod = "POST", response = LendTimes.class, notes = "")
    @ResponseBody
    public JsonResult lendTimesApplyList(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            if (user != null) {
                RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
                Map<String, String> params = HttpServletRequestUtils.getParams(request);
                params.put("customerId", user.getCustomerId().toString());
                FrontPage findTrades = service.lendTimesApplyList(params);
                return jsonResult.setObj(findTrades).setSuccess(true);
            } else {
                return null;
            }
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }

    /**
     * 融虚拟币
     *
     * @return
     */
    @RequestMapping(value = "/appLendcoinMoney")
    @ApiOperation(value = "融币页面数据 (JsonResult + obj)", httpMethod = "POST", response = JsonResult.class, notes = "")
    @ResponseBody
    public JsonResult lendcoinMoney(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
            String coinCode = request.getParameter("coinCode");
            //查询账户金额
            Map<String, String> myAccount = remoteLendService.getLengCoinCodeCenter(user.getCustomerId(), coinCode);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("coinNetAsset", myAccount.get("coinNetAsset"));
            map.put("rMBLendMoneyed", myAccount.get("rMBLendMoneyed"));
            map.put("canLendMoney", myAccount.get("canLendMoney"));
            map.put("rMBSum", myAccount.get("rMBSum"));
            map.put("lendRates", myAccount.get("lendRates"));
            map.put("rMBSum", myAccount.get("rMBSum"));
            map.put("coinCodeForRmb", myAccount.get("coinCodeForRmb"));
            map.put("lendTimes", myAccount.get("lendTimes"));
            map.put("netAsseToLend", myAccount.get("netAsseToLend"));
            List<LendCanCoinCode> list = remoteLendService.getLendCanCoinCode();
            map.put("list", list);

            //查询合同配置
            Locale locale = LocaleContextHolder.getLocale();
            String config = redisService.get("configCache:all");
            if (!StringUtils.isEmpty(config)) {
                JSONObject parseObject = JSONObject.parseObject(config);
                if ("zh_CN".equals(locale.toString())) {
                    map.put("protocol", parseObject.get("finance_cn"));
                } else if ("en".equals(locale.toString())) {
                    map.put("protocol", parseObject.get("finance_en"));
                }
            }
            return jsonResult.setObj(map).setSuccess(true);
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }

    @RequestMapping("/appCoinlist")
    @ApiOperation(value = "融币页面数据切换	", httpMethod = "POST", response = JsonResult.class, notes = "")
    @ResponseBody
    public JsonResult coinlist(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            if (user != null) {
                RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
                String lendCoin = request.getParameter("lendCoin");
                Map<String, String> myAccount = remoteLendService.getLengCoinCodeCenter(user.getCustomerId(), lendCoin);
                jsonResult.setObj(myAccount);
                jsonResult.setSuccess(true);

                return jsonResult;
            }
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }


    @RequestMapping(value = "/appLendcoinMoneyList")
    @ApiOperation(value = "融币数据记录", httpMethod = "POST", response = JsonResult.class, notes = "")
    @ResponseBody
    public JsonResult listpage(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String tokenId = request.getParameter("tokenId");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
            Map<String, String> params = HttpServletRequestUtils.getParams(request);
            params.put("customerId", user.getCustomerId().toString());
            FrontPage findTrades = service.findExDmLend(params);
            return jsonResult.setObj(findTrades).setSuccess(true);
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }

    @RequestMapping(value = "/appAddCoin")
    @ApiOperation(value = "融币申请(JsonResult + obj)", httpMethod = "POST", response = JsonResult.class, notes = "")
    @ResponseBody
    public JsonResult addCoin(HttpServletRequest request) {
        String tokenId = request.getParameter("tokenId");
        JsonResult jsonResult = new JsonResult();
        Lend exDmLend = new Lend();
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            if (user != null) {
                String lendCoin = request.getParameter("lendCoin");
                String lendCount = request.getParameter("lendCount");
                if (StringUtil.isEmpty(lendCount)) {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg("失败，借款为空");
                    return jsonResult;
                }
                exDmLend.setLendCount(new BigDecimal(lendCount));
                if (exDmLend.getLendCount().compareTo(new BigDecimal(0)) <= 0) {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg("失败，借款金额小于等于0");
                    return jsonResult;
                }
                RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
                exDmLend.setCustomerId(user.getCustomerId());
                exDmLend.setUserCode(user.getUserCode());
                exDmLend.setUserName(user.getUsername());
                exDmLend.setLendCoin(lendCoin);
                exDmLend.setLendCoinType("2");
                //默认先借money

                String[] relt = service.saveExDmLend(exDmLend);
                if (relt[0].equals(Constant.CODE_SUCCESS)) {
                    jsonResult.setSuccess(true).setMsg("申请成功");
                    //查询账户金额
                    Map<String, String> myAccount = service.getLengCoinCodeCenter(user.getCustomerId(), lendCoin);
                    jsonResult.setObj(myAccount);
                } else {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(relt[1]);
                }

                //查询账户金额
                String coinCode = request.getParameter("lendCoinCode");
                RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
                Map<String, String> myAccount = remoteLendService.getLengCoinCodeCenter(user.getCustomerId(), lendCoin);
                jsonResult.setObj(myAccount);
            }
            return jsonResult;
        }
        return new JsonResult().setSuccess(false).setMsg("请登录或重新登录");
    }


    //还款页面数据
    @RequestMapping(value = "/appGetRepaymentInfo")
    @ApiOperation(value = "还款页面数据", httpMethod = "POST", response = JsonResult.class, notes = "id")
    @ResponseBody
    public JsonResult getRepaymentInfo(String id) {
        RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
        JsonResult jsonResult = new JsonResult();
        String st = service.repaymentInfo(Long.valueOf(id));
        return jsonResult.setSuccess(true).setObj(st);
    }

    //还款
    @RequestMapping(value = "/appRepayment")
    @ApiOperation(value = "还款申请(jsonResult)", httpMethod = "POST", response = JsonResult.class, notes = "id,type,repaymentMoney")
    @ResponseBody
    public JsonResult repayment(HttpServletRequest req) {
        JsonResult jsonResult = new JsonResult();
        Long id = Long.valueOf(req.getParameter("id"));
        String type = req.getParameter("type");
        String repaymentMoney = req.getParameter("repaymentMoney");
        BigDecimal brepaymentMoney = new BigDecimal("0");
        if (type.equals("part")) {
            if (StringUtil.isEmpty(repaymentMoney)) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg("金额不能为空");
                return jsonResult;
            }
            brepaymentMoney = new BigDecimal(repaymentMoney);
            if (brepaymentMoney.compareTo(new BigDecimal("0")) == 0) {

                jsonResult.setSuccess(false);
                jsonResult.setMsg("金额不能为空");
                return jsonResult;
            }

        }
        RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
        String[] relt = service.repayment(id, type, brepaymentMoney);
        if (relt[0].equals(Constant.CODE_SUCCESS)) {
            jsonResult.setSuccess(true);
            jsonResult.setMsg("还款成功");
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(relt[1]);
        }
        return jsonResult;
    }

    @RequestMapping(value = "/appFildIntentlist")
    @ApiOperation(value = "还款明细", httpMethod = "POST", response = LendIntent.class, notes = "")
    @ResponseBody
    public List<LendIntent> fildIntentlist(HttpServletRequest request) {
        String tokenId = request.getParameter("tokenId");
        JsonResult jsonResult = new JsonResult();
        Lend exDmLend = new Lend();
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String value = redisService.get("mobile:" + tokenId);
        if (value != null) {
            String tel = JSONObject.parseObject(value).getString("mobile");
            RemoteManageService remoteManageService = (RemoteManageService) SpringContextUtil.getBean("remoteManageService");
            User user = remoteManageService.selectByTel(tel);
            RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
            List<LendIntent> list = service.fildIntentlist(Long.valueOf(request.getParameter("lendId")));
            List<LendIntent> lendList = new ArrayList<>();
            if ("zh_CN".equals("zh_CN")) {
                for (LendIntent lend : list) {
                    if (lend.getIntentType().equals("principal")) {
                        lend.setIntentType("本金");
                    } else if (lend.getIntentType().equals("interest")) {
                        lend.setIntentType("利息");
                    }
                    lendList.add(lend);
                }
                return lendList;
            }
            return list;
        }
        return null;
    }


}
