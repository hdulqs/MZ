package com.mz.front.user.btcpost.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.ExDigitalmoneyAccountManage;
import com.mz.manage.remote.model.ExDmCustomerPublicKeyManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.UserInfo;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.SessionUtils;
import com.mz.util.sys.SpringContextUtil;;

@Controller
@RequestMapping("/user/btc")
public class BtcPostController {

    @Autowired
    private RedisService redisService;

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

    @RequestMapping("post")
    public ModelAndView post(HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User selectByTel = remoteManageService.selectByTel(user.getUsername());
        String isChongbi = parseObject.get("isChongbi").toString();
        if (isChongbi != null && "1".equals(isChongbi)) {
            RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
            List<ExDigitalmoneyAccountManage> list = remoteAppBankCardManageService.listexd(user.getCustomerId(), parseObject.get("language_code").toString());
            if (list != null && list.size() > 0) {
                if (locale.toString() == null) {

                } else {
                    if ("zh_CN".equals(locale.toString())) {

                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setCoinName(list.get(i).getCoinCode());
                        }
                    }
                }
                mav.addObject("list", list);
                mav.addObject("firstHot", list.get(0).getHotMoney());
                mav.addObject("firstCold", list.get(0).getColdMoney());
                mav.addObject("publicKey", list.get(0).getPublicKey());
                mav.addObject("coincode", list.get(0).getCoinCode());
            }

            mav.setViewName("front/user/btcpost");
            return mav;
        } else {
            //未实名，往实名页跳
            if (user != null && user != null) {
                if (selectByTel.getStates() == 0) {
                    mav.setViewName("front/user/identity");
                    return mav;
                } else if (selectByTel.getStates() == 1) {
                    RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
                    if (result != null && result.getSuccess()) {
                        UserInfo userInfo = (UserInfo) result.getObj();
                        if ("1".equals(userInfo.getType())) {
                            if ("en".equals(locale.toString())) {
                                userInfo.setPapersType("ID Card");
                            }
                        } else if ("2".equals(userInfo.getType())) {
                            if ("en".equals(locale.toString())) {
                                userInfo.setPapersType("Passport");
                            }
                        }
                        mav.addObject("info", result.getObj());
                    }
                    mav.setViewName("front/user/realinfo");
                    return mav;
                } else if (selectByTel.getStates() == 2) {
                    RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
                    List<ExDigitalmoneyAccountManage> list = remoteAppBankCardManageService.listexd(user.getCustomerId(), parseObject.get("language_code").toString());
                    if (list != null && list.size() > 0) {
                        if (locale.toString() == null) {

                        } else {
                            if ("zh_CN".equals(locale.toString())) {

                            } else {
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setCoinName(list.get(i).getCoinCode());
                                }
                            }
                        }
                        mav.addObject("list", list);
                        mav.addObject("firstHot", list.get(0).getHotMoney());
                        mav.addObject("firstCold", list.get(0).getColdMoney());
                        mav.addObject("publicKey", list.get(0).getPublicKey());
                        mav.addObject("coincode", list.get(0).getCoinCode());
                    }

                    mav.setViewName("front/user/btcpost");
                    return mav;
                } else if (selectByTel.getStates() == 3) {
                    mav.setViewName("front/user/realinfono");
                    return mav;
                }
            }
        }
        return mav;
    }


    @RequestMapping("get")
    public ModelAndView get(HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        ModelAndView mav = new ModelAndView();
        User user = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        User selectByTel = remoteManageService.selectByTel(user.getUsername());
        String config = redisService.get("configCache:all");
        JSONObject parseObject = JSONObject.parseObject(config);
        //判断是否开启强制手机认证
        String isOpenVerify = "";
        if (parseObject.get("isOpenVerify") != null) {
            isOpenVerify = parseObject.get("isOpenVerify").toString();
        }
        if (null != isOpenVerify && (!"".equals(isOpenVerify)) && isOpenVerify.equals("0")) {
            if (user.getGoogleState() == 0 && user.getPhoneState() == 0) {
                mav.setViewName("front/user/setphone");
                return mav;
            }
        }

        List<String> openTibiList = remoteManageService.findOpenTibi();


        String isTibi = parseObject.get("isTibi").toString();
        if (isTibi != null && "1".equals(isTibi)) {
            RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
            List<ExDigitalmoneyAccountManage> list = remoteAppBankCardManageService.listexd(user.getCustomerId(), parseObject.get("language_code").toString());
            if (list != null && list.size() > 0) {
                if (locale.toString() == null) {

                } else {
                    if ("zh_CN".equals(locale.toString())) {

                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setCoinName(list.get(i).getCoinCode());
                        }
                    }
                }

                for (int i = 0; i < list.size(); i++) {
                    if (!openTibiList.contains(list.get(i).getCoinCode())) {
                        list.remove(i);
                        i = i - 1;
                        continue;
                    }
                }

                mav.addObject("list", list);

                mav.addObject("firstHot", list.get(0).getHotMoney());
                mav.addObject("firstCold", list.get(0).getColdMoney());
                mav.addObject("publicKey", list.get(0).getPublicKey());
                mav.addObject("coinType", list.get(0).getCoinCode());
                mav.addObject("currencyType", list.get(0).getCurrencyType());
                mav.addObject("coincode", list.get(0).getCoinCode());
                mav.addObject("paceFeeRate", list.get(0).getPaceFeeRate());
                mav.addObject("leastPaceNum", list.get(0).getLeastPaceNum());
                mav.addObject("oneDayPaceNum", list.get(0).getOneDayPaceNum());

                RedisService redisService = SpringContextUtil.getBean("redisService");
                String coinCode = list.get(0).getCoinCode();
                String value = redisService.get("currecyType:" + coinCode);
                request.setAttribute("paceType", value);
                List<ExDmCustomerPublicKeyManage> list2 = remoteAppBankCardManageService.getList(user.getCustomerId(), list.get(0).getCoinCode());
                if (list2 != null && list2.size() > 0) {
                    mav.addObject("list2", list2);
                }
            }
            mav.setViewName("front/user/btcget");
            return mav;
        } else {
            if (selectByTel.getStates() == 0) {
                mav.setViewName("front/user/identity");
                return mav;
            } else if (selectByTel.getStates() == 1) {
                RemoteResult result = remoteManageService.getPersonInfo(user.getUserCode());
                if (result != null && result.getSuccess()) {
                    UserInfo userInfo = (UserInfo) result.getObj();
                    if ("1".equals(userInfo.getType())) {
                        if ("en".equals(locale.toString())) {
                            userInfo.setPapersType("ID Card");
                        }
                    } else if ("2".equals(userInfo.getType())) {
                        if ("en".equals(locale.toString())) {
                            userInfo.setPapersType("Passport");
                        }
                    }
                    mav.addObject("info", result.getObj());
                }
                mav.setViewName("front/user/realinfo");
                return mav;
            } else if (selectByTel.getStates() == 2) {
                RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
                List<ExDigitalmoneyAccountManage> list = remoteAppBankCardManageService.listexd(user.getCustomerId(), parseObject.get("language_code").toString());
                if (list != null && list.size() > 0) {
                    if (locale.toString() == null) {

                    } else {
                        if ("zh_CN".equals(locale.toString())) {

                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setCoinName(list.get(i).getCoinCode());
                            }
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        if (!openTibiList.contains(list.get(i).getCoinCode())) {
                            list.remove(i);
                            i = i - 1;
                            continue;
                        }
                    }
                    mav.addObject("list", list);

                    mav.addObject("firstHot", list.get(0).getHotMoney());
                    mav.addObject("firstCold", list.get(0).getColdMoney());
                    mav.addObject("publicKey", list.get(0).getPublicKey());
                    mav.addObject("coinType", list.get(0).getCoinCode());
                    mav.addObject("currencyType", list.get(0).getCurrencyType());
                    mav.addObject("coincode", list.get(0).getCoinCode());
                    mav.addObject("paceFeeRate", list.get(0).getPaceFeeRate());
                    mav.addObject("leastPaceNum", list.get(0).getLeastPaceNum());
                    mav.addObject("oneDayPaceNum", list.get(0).getOneDayPaceNum());

                    RedisService redisService = SpringContextUtil.getBean("redisService");
                    String coinCode = list.get(0).getCoinCode();
                    String value = redisService.get("currecyType:" + coinCode);
                    request.setAttribute("paceType", value);
                    List<ExDmCustomerPublicKeyManage> list2 = remoteAppBankCardManageService.getList(user.getCustomerId(), list.get(0).getCoinCode());
                    if (list2 != null && list2.size() > 0) {
                        mav.addObject("list2", list2);
                    }
                }
                mav.setViewName("front/user/btcget");
                return mav;
            } else if (selectByTel.getStates() == 3) {
                mav.setViewName("front/user/realinfono");
                return mav;
            }
        }
        return mav;

    }

    /**
     * 查询钱包地址
     *
     * @param request
     * @return
     */
    @RequestMapping("/findPublicKey")
    @ResponseBody
    public List<ExDmCustomerPublicKeyManage> findPublicKey(HttpServletRequest request) {

        User user = SessionUtils.getUser(request);
        if (user != null) {
            String coinCode = request.getParameter("coinCode");
            RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
            List<ExDmCustomerPublicKeyManage> list = remoteAppBankCardManageService.getList(user.getCustomerId(), coinCode);
            return list;
        }
        return null;

    }


    /**
     * 查询提币费率
     *
     * @param request
     * @return
     */
    @RequestMapping("/findcurrcy")
    @ResponseBody
    public JsonResult findcurrcy(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        String coinCode = request.getParameter("coinCode");
        RedisService redisService = SpringContextUtil.getBean("redisService");
        String paceCurrecy = redisService.get("paceCurrecy:" + coinCode);
        String currecyType = redisService.get("currecyType:" + coinCode);
        list.add(paceCurrecy);
        list.add(currecyType);

        return new JsonResult().setObj(list);
    }

    /**
     * 查询充值币的记录 交易类型(1充币 ，2提币)'
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public FrontPage list(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        String status = request.getParameter("status");
        RemoteAppTransactionManageService remoteAppTransactionManageService = SpringContextUtil.getBean("remoteAppTransactionManageService");
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        params.put("customerId", user.getCustomerId().toString());
        params.put("transactionType", "1");
        params.put("status", status);
        if ("0".equals(status)) {// 0查全部
            params.put("status", null);
        }
        return remoteAppTransactionManageService.findExdmtransaction(params);
    }


    /**
     * 获得币地址
     *
     * @param request
     * @return
     */
    @RequestMapping("/getPublicKey")
    @ResponseBody
    public JsonResult getPublicKey(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String accountId = request.getParameter("accountId");
        if (!StringUtils.isEmpty(accountId)) {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.getPublicKey(Long.valueOf(accountId));
            if (remoteResult != null && remoteResult.getSuccess()) {
                return jsonResult.setSuccess(true).setObj(remoteResult.getObj());
            } else {
                return jsonResult.setMsg(remoteResult.getMsg());
            }
        }

        return new JsonResult().setMsg("accountId is null");
    }

    /**
     * 生成币地址
     *
     * @param request
     * @return
     */
    @RequestMapping("/createPublicKey")
    @ResponseBody
    public JsonResult createPublicKey(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String accountId = request.getParameter("accountId");
        User user = SessionUtils.getUser(request);
        if(user.getStates() != 2){
            if(user.getStates() == 0) {
                return new JsonResult().setSuccess(false).setMsg("请个人中心实名认证！");
            }if(user.getPhoneState() ==1){
                return new JsonResult().setSuccess(false).setMsg("请等待实名认证审批完成！！");
            }else {
                return new JsonResult().setSuccess(false).setMsg("审核已拒绝，请联系管理员");
            }
        }
        if (!StringUtils.isEmpty(accountId)) {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.createPublicKey(Long.valueOf(accountId));
            if (remoteResult != null && remoteResult.getSuccess()) {
                return jsonResult.setSuccess(true).setObj(remoteResult.getObj());
            } else {
                return jsonResult.setMsg(SpringContextUtil.diff(remoteResult.getMsg()));
            }
        }

        return new JsonResult().setMsg("accountId is null");
    }


}
