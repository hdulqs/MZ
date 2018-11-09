package com.mz.front.user.lend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.manage.remote.model.Lend;
import com.mz.manage.remote.model.LendCanCoinCode;
import com.mz.manage.remote.model.LendIntent;
import com.mz.manage.remote.model.LendTimes;
import com.mz.manage.remote.model.User;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.manage.remote.RemoteLendService;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.SessionUtils;
import com.mz.util.common.Constant;
import com.mz.util.sys.SpringContextUtil;;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping("/lendcoin")
public class LendCoinController {

    private final static Logger log = Logger.getLogger(LendCoinController.class);
    @Autowired
    RedisService redisService;

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

    /**
     * 融基础币（人民币或者虚拟币）
     *
     * @return
     */
    @RequestMapping("lendMoney")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("front/user/lendMoney");
        User user = SessionUtils.getUser(request);
        if (user != null) {
            RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
            //查询账户金额
            Map<String, String> myAccount = remoteLendService.getLengCoinCodeForRmbCenter(user.getCustomerId());
            mav.addObject("coinNetAsset", myAccount.get("coinNetAsset"));
            mav.addObject("rMBLendMoneyed", myAccount.get("rMBLendMoneyed"));
            mav.addObject("canLendMoney", myAccount.get("canLendMoney"));
            mav.addObject("rMBSum", myAccount.get("rMBSum"));
            mav.addObject("lendRates", myAccount.get("lendRates"));
            mav.addObject("rMBSum", myAccount.get("rMBSum"));
            mav.addObject("coinCodeForRmb", myAccount.get("coinCodeForRmb"));
            mav.addObject("lendTimes", myAccount.get("lendTimes"));
            mav.addObject("netAsseToLend", myAccount.get("netAsseToLend"));
            //查询合同配置
            Locale locale = LocaleContextHolder.getLocale();
            RedisService redisService = SpringContextUtil.getBean("redisService");
            String config = redisService.get("configCache:financeLendConfig");
            if (!StringUtils.isEmpty(config)) {
                JSONObject parseObject = JSONObject.parseObject(config);
                if ("zh_CN".equals(locale.toString())) {
                    mav.addObject("protocol", parseObject.get("finance_cn"));
                } else if ("en".equals(locale.toString())) {
                    mav.addObject("protocol", parseObject.get("finance_en"));
                }
            }
        }
        return mav;
    }

    /**
     * 申请杠杆倍数
     *
     * @return
     */
    @RequestMapping("lendTimesApply")
    public ModelAndView lendTimesApply(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("front/user/lendTimesApply");
        User user = SessionUtils.getUser(request);
        if (user != null) {
            RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
            //查询账户金额
            Map<String, String> myAccount = remoteLendService.getLengCoinCodeForRmbCenter(user.getCustomerId());
            mav.addObject("coinNetAsset", myAccount.get("coinNetAsset"));
            mav.addObject("rMBLendMoneyed", myAccount.get("rMBLendMoneyed"));
            mav.addObject("canLendMoney", myAccount.get("canLendMoney"));
            mav.addObject("rMBSum", myAccount.get("rMBSum"));
            mav.addObject("lendRates", myAccount.get("lendRates"));
            mav.addObject("rMBSum", myAccount.get("rMBSum"));
            mav.addObject("coinCodeForRmb", myAccount.get("coinCodeForRmb"));
            mav.addObject("lendTimes", myAccount.get("lendTimes"));

            //倍数管理
            String string = redisService.get("configCache:financeLendConfig");
            JSONObject obj = JSON.parseObject(string);
            String lendTimes = obj!= null ? obj.get("lendTimesConfig").toString() : null;
            List<String> mutiple = null;
            if (!lendTimes.isEmpty()) {
                String[] split = lendTimes.split(",");
                mutiple = Arrays.asList(split);
            }
            mav.addObject("mutiple", mutiple);
        }
        return mav;
    }

    /**
     * /**
     * 借款列表
     *
     * @return
     */
    @RequestMapping(value = "/listpage", method = RequestMethod.POST)
    @ResponseBody
    public FrontPage listpage(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);

        if (user != null) {/*
			RemoteExDmLendService remoteExDmLendService = (RemoteExDmLendService) ContextUtil.getBean("remoteExDmLendService");
			RemoteQueryFilter remoteQueryFilter = new RemoteQueryFilter(ExDmLend.class, request);
			remoteQueryFilter.setSaasId(ContextUtil.getSaasId());
			remoteQueryFilter.addFilter("customerId=", appCustomer.getId());
			remoteQueryFilter.setOrderBy("lendTime desc");
			PageResult pageResult = remoteExDmLendService.listPage(remoteQueryFilter);
			return pageResult;
		*/
            RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
            Map<String, String> params = HttpServletRequestUtils.getParams(request);
            params.put("customerId", user.getCustomerId().toString());
            FrontPage findTrades = service.findExDmLend(params);
            return findTrades;
        } else {
            return null;
        }

    }


    //融资
    @RequestMapping("/add")
    @ResponseBody
    public JsonResult add(Lend exDmLend, HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        String lendCount = request.getParameter("lendCount");
        if (StringUtil.isEmpty(lendCount)) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg("失败，借款为空");
            return jsonResult;
        }
        if (exDmLend.getLendCount().compareTo(new BigDecimal(0)) <= 0) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg("失败，借款金额小于等于0");
            return jsonResult;
        }
        RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
        exDmLend.setCustomerId(user.getCustomerId());
        exDmLend.setUserCode(user.getUserCode());
        exDmLend.setUserName(user.getUsername());
        //	exDmLend.setSaasId(user.getSaasId());
        //	exDmLend.setCurrencyType("cny");
        //	exDmLend.setWebsite("cn");
        //默认先借money
        RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
//		Map<String,String> lengCoinCodeForRmb = remoteLendService.getCoinCodeForRmb();
        //	String coinCodeForRmb=lengCoinCodeForRmb.get("coinCodeForRmb");
        exDmLend.setLendCoinType("1");
        //	exDmLend.setLendCoin(coinCodeForRmb);
        String[] relt = service.saveExDmLend(exDmLend);
        if (relt[0].equals(Constant.CODE_SUCCESS)) {
            jsonResult.setSuccess(true);
            //查询账户金额
            Map<String, String> myAccount = remoteLendService.getLengCoinCodeForRmbCenter(user.getCustomerId());
            jsonResult.setObj(myAccount);
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(relt[1]);
        }

        return jsonResult;
    }

    //	@MethodName(name = "融资融币的相关数据")
    @RequestMapping(value = "/getRepaymentInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getRepaymentInfo(@PathVariable("id") Long id) {
        RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
        String st = service.repaymentInfo(id);
        return st;
    }

    //	@MethodName(name = "还款")
    @RequestMapping(value = "/repayment")
    @ResponseBody
    public JsonResult repayment(HttpServletRequest req) {
        JsonResult jsonResult = new JsonResult();
        Long id = Long.valueOf(req.getParameter("id"));
        String type = req.getParameter("type");
        String repaymentMoney = req.getParameter("repaymentMoney");
		
	/*	Long id=Long.valueOf("2");
		String type="part";
		String repaymentMoney="100";*/
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
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(relt[1]);
        }
        return jsonResult;
    }

    //	@MethodName(name = "还款列表")
    @RequestMapping(value = "/fildIntentlist")
    @ResponseBody
    public List<LendIntent> fildIntentlist(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        Locale locale = LocaleContextHolder.getLocale();
        if (user != null) {
            RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
            List<LendIntent> list = service.fildIntentlist(Long.valueOf(request.getParameter("lendId")));
            List<LendIntent> lendList = new ArrayList<>();
            if ("zh_CN".equals(locale.toString())) {
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


    /**
     * 借款列表
     *
     * @return
     */
    @RequestMapping(value = "/lendTimesApplyList")
    @ResponseBody
    public FrontPage lendTimesApplyList(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);

        if (user != null) {
            RemoteLendService service = SpringContextUtil.getBean("remoteLendService");
            Map<String, String> params = HttpServletRequestUtils.getParams(request);
            params.put("customerId", user.getCustomerId().toString());
            FrontPage findTrades = service.lendTimesApplyList(params);
            return findTrades;
        } else {
            return null;
        }

    }

    //申请杠杆倍数
    @RequestMapping("/lendTimesApplyAdd")
    @ResponseBody
    public JsonResult lendTimesApplyAdd(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
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
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(SpringContextUtil.diff(relt[1]));
        }

        return jsonResult;
    }

    /**
     * 融虚拟币
     *
     * @return
     */
    @RequestMapping("lendcoinMoney")
    public ModelAndView lendcoinMoney(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("front/user/lendcoinMoney");
        User user = SessionUtils.getUser(request);
        if (user != null) {
            RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
            String coinCode = request.getParameter("coinCode");
            //查询账户金额
            Map<String, String> myAccount = remoteLendService.getLengCoinCodeCenter(user.getCustomerId(), coinCode);
            mav.addObject("coinNetAsset", myAccount.get("coinNetAsset"));
            mav.addObject("rMBLendMoneyed", myAccount.get("rMBLendMoneyed"));
            mav.addObject("canLendMoney", myAccount.get("canLendMoney"));
            mav.addObject("rMBSum", myAccount.get("rMBSum"));
            mav.addObject("lendRates", myAccount.get("lendRates"));
            mav.addObject("rMBSum", myAccount.get("rMBSum"));
            mav.addObject("coinCodeForRmb", myAccount.get("coinCodeForRmb"));
            mav.addObject("lendTimes", myAccount.get("lendTimes"));
            mav.addObject("netAsseToLend", myAccount.get("netAsseToLend"));
            List<LendCanCoinCode> list = remoteLendService.getLendCanCoinCode();
            mav.addObject("list", list);

            //查询合同配置
            Locale locale = LocaleContextHolder.getLocale();
            RedisService redisService = SpringContextUtil.getBean("redisService");
            String config = redisService.get("configCache:all");
            if (!StringUtils.isEmpty(config)) {
                JSONObject parseObject = JSONObject.parseObject(config);
                if ("zh_CN".equals(locale.toString())) {
                    mav.addObject("protocol", parseObject.get("finance_cn"));
                } else if ("en".equals(locale.toString())) {
                    mav.addObject("protocol", parseObject.get("finance_en"));
                }
            }
        }

        return mav;
    }

    //融币（包括基础币和其他的币）
    @RequestMapping("/addCoin")
    @ResponseBody
    public JsonResult addCoin(HttpServletRequest request) {
        Lend exDmLend = new Lend();
        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
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
            //	exDmLend.setSaasId(user.getSaasId());
            //	exDmLend.setCurrencyType("cny");
            //	exDmLend.setWebsite("cn");
            //默认先借money

            String[] relt = service.saveExDmLend(exDmLend);
            if (relt[0].equals(Constant.CODE_SUCCESS)) {
                jsonResult.setSuccess(true);
                //查询账户金额
                Map<String, String> myAccount = service.getLengCoinCodeCenter(user.getCustomerId(), lendCoin);
                jsonResult.setObj(myAccount);
            } else {
                jsonResult.setSuccess(false);
                jsonResult.setMsg(relt[1]);
            }

//		jsonResult.setSuccess(true);
            //查询账户金额
            String coinCode = request.getParameter("lendCoinCode");
            RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
            Map<String, String> myAccount = remoteLendService.getLengCoinCodeCenter(user.getCustomerId(), lendCoin);
            jsonResult.setObj(myAccount);
        }
        return jsonResult;
    }

    //能融币的列表
    @RequestMapping("/coinlist")
    @ResponseBody
    public JsonResult coinlist(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        JsonResult jsonResult = new JsonResult();
        if (user != null) {
            RemoteLendService remoteLendService = SpringContextUtil.getBean("remoteLendService");
            String lendCoin = request.getParameter("lendCoin");
            Map<String, String> myAccount = remoteLendService.getLengCoinCodeCenter(user.getCustomerId(), lendCoin);
            jsonResult.setObj(myAccount);
            jsonResult.setSuccess(true);

        }
        return jsonResult;
    }
}
