package com.mz.front.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.mvc.model.page.HttpServletRequestUtils;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.front.redis.model.UserRedis;
import com.mz.manage.remote.RemoteAppTransactionManageService;
import com.mz.manage.remote.RemoteManageService;
import com.mz.manage.remote.model.AppBankCardManage;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import com.mz.manage.remote.model.otc.OtcOrderTransactionMange;
import com.mz.manage.remote.model.otc.OtcTransactionOrder;
import com.mz.redis.common.utils.RedisService;
import com.mz.redis.common.utils.RedisUtil;
import com.mz.util.FileType;
import com.mz.util.FileUpload;
import com.mz.util.SessionUtils;
import com.mz.util.springmvcPropertyeditor.DateTimePropertyEditorSupport;
import com.mz.util.springmvcPropertyeditor.StringPropertyEditorSupport;
import com.mz.util.sys.SpringContextUtil;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public class OtcController {
    private final static Logger log = Logger.getLogger(OtcController.class);


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

    public static HttpServletRequest getRequest() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            return request;
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 上传图片
     *
     * @param files
     * @return
     */
    public String[] upload(@RequestParam("file") MultipartFile[] files) {
        String[] pathImg = new String[3];
        try {
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    // 获取文件名
                    if (file != null) {
                        String filename = file.getOriginalFilename();
                        // 上传图片
                        if (file != null && filename != null && filename.length() > 0) {
                            // 上传路径

                            String realPath = this.getRequest().getRealPath("/");
                            // 生成hryfile路径
                            String rootPath = realPath.substring(0,
                                    org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2)
                                            + 1);
                            System.out.println("rootPath" + rootPath);
                            // 新图片名称
                            String newFileName = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
                            pathImg[i] = "hryfilefront" + File.separator + newFileName;
                            File secondFolder = new File(rootPath + "hryfilefront");
                            // 存入本地
                            if (!secondFolder.exists()) {
                                secondFolder.mkdirs();
                            }
                            File newFile = new File(rootPath + "hryfilefront" + File.separator + newFileName);
                            file.transferTo(newFile);
                        }
                    } else {
                        pathImg[i] = "";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathImg;
    }


    @Autowired
    private RedisService redisService;

    /**
     * c2c入口进到c2c默认的币种页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("otc")
    public String otc(HttpServletRequest request, HttpServletResponse response) {
        String str = redisService.get("cn:Otclist");
        String coinCode = "USDT";
        if (!StringUtils.isEmpty(str)) {
            List<String> list = JSON.parseArray(str, String.class);
            if (list != null && list.size() > 0) {
                coinCode = list.get(0);
            }
        }
        return "redirect:/otc/" + coinCode + ".do?activeId=otc";
    }


    /**
     * 跳到c2c页面
     *
     * @param request
     * @param coinCode
     * @return
     */
    @RequestMapping("otc/{coinCode}")
    public ModelAndView otcinfo(HttpServletRequest request, @PathVariable String coinCode) {
        ModelAndView view = new ModelAndView("otc");

        //查询所有开通Otc的币
        String str = redisService.get("cn:Otclist");
        if (!StringUtils.isEmpty(str)) {
            List<String> list = JSON.parseArray(str, String.class);
            view.addObject("coinList", list);
        }
        //当前激活的节点
        view.addObject("activeCoin", coinCode);

        view.addObject("showColor", 7);

        //获得OtcCoin表配置信息
        String otcCoinList = redisService.get("cn:OtcCoinList");
        if (!StringUtils.isEmpty(otcCoinList)) {
            JSONArray parseArray = JSON.parseArray(otcCoinList);
            if (parseArray != null) {
                for (int i = 0; i < parseArray.size(); i++) {
                    JSONObject jsonObject = parseArray.getJSONObject(i);
                    if (coinCode.equals(jsonObject.getString("coinCode"))) {
                        view.addObject("otcpoundage_type", jsonObject.get("poundage_type"));
                        view.addObject("otcpoundage", jsonObject.getBigDecimal("poundage"));
                        view.addObject("otcBuyPrice", jsonObject.getBigDecimal("buyPrice"));
                        view.addObject("otcSellPrice", jsonObject.getBigDecimal("sellPrice"));
                        view.addObject("otcminBuyPrice", jsonObject.getBigDecimal("minbuyPrice"));
                        view.addObject("otcmaxBuyPrice", jsonObject.getBigDecimal("maxbuyPrice"));
                        view.addObject("otcminSellPrice", jsonObject.getBigDecimal("minsellPrice"));
                        view.addObject("otcmaxSellPrice", jsonObject.getBigDecimal("maxsellPrice"));
                    }
                }
            }
        }


        return view;
    }


    /**
     * OTC下单
     *
     * @param request
     * @return
     */
    @RequestMapping("user/otcCreateTransaction")
    @ResponseBody
    public JsonResult createTransaction(HttpServletRequest request) {

        String coinCode = request.getParameter("coinCode");
        String transactionType = request.getParameter("transactionType");
        String transactionCount = request.getParameter("transactionCount");
        String transactionPrice = request.getParameter("transactionPrice");


        User user = SessionUtils.getUser(request);
        if (user != null) {

            //判断是否实名
            if (user.getStates() != 2) {
                return new JsonResult().setMsg("请到个人中心进行实名!");
            }
            if (user.getPhoneState() != 1) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心绑定手机!");
            }
            if (user.getOpenOtcStates() != 1) {
                return new JsonResult().setSuccess(false).setMsg("请联系客服开通委托下单功能!");
            }

            OtcTransactionOrder otcOrder = new OtcTransactionOrder();
            //交易币种
            otcOrder.setCoinCode(coinCode);
            //交易单号
            otcOrder.setTransactionNum(UUID.randomUUID().toString().replace("-", ""));
            //交易数量
            otcOrder.setTransactionCount(new BigDecimal(transactionCount));
            //交易价格
            otcOrder.setTransactionPrice(new BigDecimal(transactionPrice));
            //用户名
            otcOrder.setUserName(user.getUsername());
            //交易类型1买，2卖
            otcOrder.setTransactionType(Integer.valueOf(transactionType));
            //customerId
            otcOrder.setCustomerId(user.getCustomerId());
            RedisUtil<UserRedis> redisUtil = new RedisUtil<UserRedis>(UserRedis.class);
            UserRedis userRedis = redisUtil.get(user.getCustomerId().toString());
            Long coinAccountId = userRedis.getDmAccountId(coinCode);
            //虚拟币账户id,要买的币，或者要卖的币
            otcOrder.setAccountId(coinAccountId);

            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = null;
            try {
                remoteResult = remoteManageService.createOtcOrder(otcOrder);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (remoteResult != null) {
                if (remoteResult.getSuccess()) {
                    return new JsonResult().setSuccess(true).setObj(remoteResult.getObj()).setMsg(SpringContextUtil.diff("success"));
                } else {
                    return new JsonResult().setMsg(remoteResult.getMsg());
                }
            } else {
                return new JsonResult().setMsg("remote错误");
            }

        }

        return new JsonResult();
    }


    @RequestMapping("user/getotcTransaction/detail")
    public ModelAndView getc2cTransaction(HttpServletRequest request) {
        String transactionNum = request.getParameter("transactionNum");
        ModelAndView view = new ModelAndView("/front/user/otcPayInfor");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        String c2cOrderDetail = remoteManageService.getC2cOrderDetail(transactionNum);
        if (!StringUtils.isEmpty(c2cOrderDetail)) {
            JSONObject obj = JSON.parseObject(c2cOrderDetail);
            view.addObject("obj", obj);
        }

        return view;
    }

    @RequestMapping("/user/otcPayInfor")
    public ModelAndView otcPayInfor(HttpServletRequest request) {
        String transactionorderid = request.getParameter("id");
        User selectuser = SessionUtils.getUser(request);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        OtcOrderTransactionMange otcOrderTransactionMange = remoteManageService.selectOtcOrderbyid(Integer.valueOf(transactionorderid).longValue());
        ModelAndView view = new ModelAndView("/front/user/otcPayInfor");
        JSONObject userinfo = new JSONObject();
        if (selectuser != null) {
            User user = new User();
            if (selectuser.getCustomerId().equals(otcOrderTransactionMange.getBuyCustomId())) {
                user = remoteManageService.selectByustomerId(otcOrderTransactionMange.getSellCustomId());
            } else {
                user = remoteManageService.selectByustomerId(otcOrderTransactionMange.getBuyCustomId());
            }

            RemoteAppTransactionManageService remoteAppBankCardManageService = (RemoteAppTransactionManageService) SpringContextUtil.getBean("remoteAppTransactionManageService");
            List<AppBankCardManage> list;
            if (selectuser.getCustomerId().equals(otcOrderTransactionMange.getBuyCustomId())) {
                list = remoteAppBankCardManageService.findByCustomerId(otcOrderTransactionMange.getSellCustomId());
            } else {
                list = remoteAppBankCardManageService.findByCustomerId(otcOrderTransactionMange.getBuyCustomId());
            }
            userinfo.put("username", user.getUsername());
            userinfo.put("name", user.getSurname() + user.getTruename());
            userinfo.put("phone", user.getPhone());

            if (list != null && !list.isEmpty()) {
                view.addObject("Bankinfo", list.get(0));
            }

            view.addObject("userinfo", userinfo);
            view.addObject("orderinfo", otcOrderTransactionMange);
        }
        return view;
    }

    @RequestMapping("/user/otcPay")
    public ModelAndView otcPay(HttpServletRequest request) {
        String transactionorderid = request.getParameter("id");
        String transactionNum = request.getParameter("transactionNum");
        ModelAndView view = new ModelAndView("/front/user/otcPay");
        view.addObject("transactionorderid", transactionorderid);
        return view;
    }

    @RequestMapping("/user/otcUndo")
    public ModelAndView otcUndo(HttpServletRequest request) {
        String transactionorderid = request.getParameter("id");
        String transactionNum = request.getParameter("transactionNum");
        ModelAndView view = new ModelAndView("/front/user/otcUndo");

        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        RemoteResult remoteResult = remoteManageService.getOtcbyid(Integer.valueOf(transactionorderid).longValue());

        view.addObject("transactionorderid", transactionorderid);
        view.addObject("orderinfo", remoteResult.getObj());
        return view;
    }

    @RequestMapping("/user/otcApplyArbitration")
    public ModelAndView otcApplyArbitration(HttpServletRequest request) {
        String transactionorderid = request.getParameter("id");
        String transactionNum = request.getParameter("transactionNum");
        ModelAndView view = new ModelAndView("/front/user/otcApplyArbitration");
        view.addObject("transactionorderid", transactionorderid);
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        RemoteResult remoteResult = remoteManageService.getOtcbyid(Integer.valueOf(transactionorderid).longValue());

        view.addObject("transactionorderid", transactionorderid);
        view.addObject("orderinfo", remoteResult.getObj());
        return view;
    }

    @RequestMapping("/user/otcApplyArbitrationInfo")
    public ModelAndView otcApplyArbitrationInfo(HttpServletRequest request) {
        String transactionorderid = request.getParameter("id");
        String transactionNum = request.getParameter("transactionNum");
        ModelAndView view = new ModelAndView("/front/user/otcApplyArbitrationInfo");
        RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
        RemoteResult remoteResult = remoteManageService.getOtcbyid(Integer.valueOf(transactionorderid).longValue());

        view.addObject("transactionorderid", transactionorderid);
        view.addObject("orderinfo", remoteResult.getObj());
        return view;
    }

    /**
     * @Description: 挂单列表
     * @Author: zongwei
     * @CreateDate: 2018/6/22 10:05
     * @UpdateUser: zongwei
     * @UpdateDate: 2018/6/22 10:05
     * @UpdateRemark: 创建
     * @Version: 1.0
     */
    @RequestMapping("/otc/getOtcTransactionAll")
    @ResponseBody
    public JsonResult getOtcTransactionAll(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        //if(user!=null){
        try {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.getOtclist(null, null, null);
            if (remoteResult.getSuccess()) {
                jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
            } else {
                jsonResult.setSuccess(false).setMsg("失败!");
            }
        } catch (Exception e) {
            jsonResult.setSuccess(false).setMsg("失败!");
        }
        //}
        return jsonResult;
    }

    /**
     * @Description: 买单挂单列表
     * @Author: zongwei
     * @CreateDate: 2018/6/22 10:05
     * @UpdateUser: zongwei
     * @UpdateDate: 2018/6/22 10:05
     * @UpdateRemark: 创建
     * @Version: 1.0
     */
    @RequestMapping("/otc/getOtcTransactionForBuy")
    @ResponseBody
    public JsonResult getOtcTransactionForBuy(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        String coinCode = null;
        coinCode = request.getParameter("coinCode");
        //if(user!=null){
        try {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.getOtclist("1", coinCode, "desc");
            if (remoteResult.getSuccess()) {
                jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
            } else {
                jsonResult.setSuccess(false).setMsg("失败!");
            }
        } catch (Exception e) {
            jsonResult.setSuccess(false).setMsg("失败!");
        }
        //}
        return jsonResult;
    }

    /**
     * @Description: 卖单挂单列表
     * @Author: zongwei
     * @CreateDate: 2018/6/22 10:05
     * @UpdateUser: zongwei
     * @UpdateDate: 2018/6/22 10:05
     * @UpdateRemark: 创建
     * @Version: 1.0
     */
    @RequestMapping("/otc/getOtcTransactionForSell")
    @ResponseBody
    public JsonResult getOtcTransactionForSell(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        String coinCode = null;
        coinCode = request.getParameter("coinCode");
        //if(user!=null){
        try {
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.getOtclist("2", coinCode, null);
            if (remoteResult.getSuccess()) {
                jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
            } else {
                jsonResult.setSuccess(false).setMsg("失败!");
            }
        } catch (Exception e) {
            jsonResult.setSuccess(false).setMsg("失败!");
        }
        //}
        return jsonResult;
    }

    @RequestMapping("/otc/createOrder")
    @ResponseBody
    public JsonResult createOrderTransaction(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Long id = new Long(request.getParameter("id"));
        BigDecimal transactioncount = new BigDecimal(request.getParameter("transactioncount"));
        User user = SessionUtils.getUser(request);
        if (user != null) {
            //判断是否实名
            if (user.getStates() != 2) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
            }
            if (user.getPhoneState() != 1) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心绑定手机!");
            }
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.createOrderTransaction(user.getCustomerId(), id, transactioncount);
            if (remoteResult.getSuccess()) {
                jsonResult.setSuccess(true).setMsg("成功!").setObj(remoteResult.getObj());
            } else {
                jsonResult.setSuccess(false).setMsg(remoteResult.getMsg());
            }
        } else {
            return new JsonResult().setSuccess(false).setMsg("未登陆或登陆已经失效！");
        }
        return jsonResult;
    }


    @RequestMapping("/otc/otcorderlistall")
    @ResponseBody
    public FrontPage otcorderlistall(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        String type = request.getParameter("transactionType");
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        if (user != null) {
            if ("0".equals(type)) {
                params.put("customId", user.getCustomerId().toString());
                RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
                FrontPage frontPage = remoteManageService.otcorderlistall(params);
                return frontPage;

            } else if ("1".equals(type)) {
                params.put("buyCustomId", user.getCustomerId().toString());
                RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
                FrontPage frontPage = remoteManageService.otcorderbuylist(params);
                return frontPage;

            } else if ("2".equals(type)) {
                params.put("sellCustomId", user.getCustomerId().toString());
                RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
                FrontPage frontPage = remoteManageService.otcorderselllist(params);
                return frontPage;
            }
        }
        return new FrontPage(null, 0, 1, 10);
    }

    @RequestMapping("/otc/otcorderselllist")
    @ResponseBody
    public FrontPage otcorderselllist(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        if (user != null) {
            params.put("sellCustomId", user.getCustomerId().toString());
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            FrontPage frontPage = remoteManageService.otcorderselllist(params);
            return frontPage;

        }
        return new FrontPage(null, 0, 1, 10);
    }

    @RequestMapping("/otc/otcorderbuylist")
    @ResponseBody
    public FrontPage otcorderbuylist(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        if (user != null) {
            params.put("buyCustomId", user.getCustomerId().toString());
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            FrontPage frontPage = remoteManageService.otcorderbuylist(params);
            return frontPage;
        }

        return new FrontPage(null, 0, 1, 10);

    }


    @RequestMapping("/otc/otcPayment")
    @ResponseBody
    public JsonResult otcPayment(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String paymenttype = request.getParameter("payment");
        String transactionorderid = request.getParameter("transactionorderid");
        MultipartFile img1 = multipartRequest.getFile("img1");
        MultipartFile img2 = multipartRequest.getFile("img2");
        MultipartFile img3 = multipartRequest.getFile("img3");
        if (img1.isEmpty() && img2.isEmpty() && img3.isEmpty()) {
            return jsonResult.setMsg("凭证必须上传！").setSuccess(false);
        }
        MultipartFile[] files = {img1, img2, img3};
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty() && file.getSize() > 0) {
                try {
                    InputStream inputStream = file.getInputStream();
                    String fileType = FileType.getFileType(inputStream);
                    if (fileType != null && (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif") || fileType.equals("bmp"))) {
                    } else {
                        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                    }
                } catch (Exception E) {
                    return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                }

            }
        }
        if (user != null) {
            //判断是否实名
            if (user.getStates() != 2) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
            }
            String[] pathImg = FileUpload.POSTFileQiniu(request,files);
            OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
            otcOrderTransactionMange.setImg1(pathImg[0]);
            otcOrderTransactionMange.setImg2(pathImg[1]);
            otcOrderTransactionMange.setImg3(pathImg[2]);
            otcOrderTransactionMange.setPaymentType(paymenttype);
            otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());
            otcOrderTransactionMange.setPaymentTime(new Date());

            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.otcPayment(otcOrderTransactionMange);
            if (remoteResult.getSuccess()) {
                return jsonResult.setMsg("操作成功！").setSuccess(true);
            } else {
                return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
            }

        } else {
            return jsonResult.setMsg("未登陆或登陆失效").setSuccess(false);
        }


    }


    @RequestMapping("/otc/otcApplyArbitration")
    @ResponseBody
    public JsonResult confirmotcApplyArbitration(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String appealReason = request.getParameter("appealReason");
        String transactionorderid = request.getParameter("transactionorderid");
        MultipartFile img4 = multipartRequest.getFile("img4");
        MultipartFile img5 = multipartRequest.getFile("img5");
        MultipartFile img6 = multipartRequest.getFile("img6");

        if (appealReason == null || appealReason.isEmpty()) {
            return jsonResult.setMsg("申诉原因不能为空！").setSuccess(false);
        }

        if (img4.isEmpty() && img5.isEmpty() && img6.isEmpty()) {
            return jsonResult.setMsg("凭证必须上传！").setSuccess(false);
        }

        MultipartFile[] files = {img4, img5, img6};

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty() && file.getSize() > 0) {
                try {
                    InputStream inputStream = file.getInputStream();
                    String fileType = FileType.getFileType(inputStream);
                    if (fileType != null && (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif") || fileType.equals("bmp"))) {
                    } else {
                        return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                    }
                } catch (Exception E) {
                    return new JsonResult().setSuccess(false).setMsg(SpringContextUtil.diff("picture_error"));
                }

            }
        }

        if (user != null) {
            //判断是否实名
            if (user.getStates() != 2) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
            }
            String[] pathImg = FileUpload.POSTFileQiniu(request,files);
            OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
            otcOrderTransactionMange.setImg4(pathImg[0]);
            otcOrderTransactionMange.setImg5(pathImg[1]);
            otcOrderTransactionMange.setImg6(pathImg[2]);
            otcOrderTransactionMange.setAppealReason(appealReason);
            otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());
            otcOrderTransactionMange.setPaymentTime(new Date());
            otcOrderTransactionMange.setAppealCustomId(user.getCustomerId());
            otcOrderTransactionMange.setAppealCustomName(user.getUsername());
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.confirmotcApplyArbitration(otcOrderTransactionMange);
            if (remoteResult.getSuccess()) {
                return jsonResult.setMsg("操作成功！").setSuccess(true);
            } else {
                return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
            }

        } else {
            return jsonResult.setMsg("未登陆或登陆失效").setSuccess(false);
        }

    }

    @RequestMapping("/otc/finishOtcOrder")
    @ResponseBody
    public JsonResult finishOtcOrder(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        String transactionorderid = request.getParameter("transactionorderid");

        if (user != null) {
            //判断是否实名
            if (user.getStates() != 2) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
            }
            OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
            otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());

            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.finishOtcOrder(otcOrderTransactionMange);
            if (remoteResult.getSuccess()) {
                return jsonResult.setMsg("操作成功！").setSuccess(true);
            } else {
                return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
            }

        } else {
            return jsonResult.setMsg("未登陆或登陆失效").setSuccess(false);
        }

    }

    @RequestMapping("/otc/otcUndo")
    @ResponseBody
    public JsonResult otcUndopro(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        String transactionorderid = request.getParameter("transactionorderid");
        String confirmcancelflag = request.getParameter("confirmcancelflag");

        if (!("true").equals(confirmcancelflag)) {
            return jsonResult.setMsg("请勾选确认！").setSuccess(false);
        }

        if (user != null) {
            //判断是否实名
            if (user.getStates() != 2) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
            }
            OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
            otcOrderTransactionMange.setId(Integer.valueOf(transactionorderid).longValue());
            otcOrderTransactionMange.setCancelBy(user.getCustomerId());
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.otcUndo(otcOrderTransactionMange);
            if (remoteResult.getSuccess()) {
                return jsonResult.setMsg("操作成功！").setSuccess(true);
            } else {
                return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
            }

        } else {
            return jsonResult.setMsg("未登陆或登陆失效").setSuccess(false);
        }

    }


    @RequestMapping("/otc/getOtclists")
    @ResponseBody
    public FrontPage getOtclists(HttpServletRequest request) {
        User user = SessionUtils.getUser(request);
        String type = request.getParameter("transactionType");
        Map<String, String> params = HttpServletRequestUtils.getParams(request);
        if (user != null) {

        }
        if ("0".equals(type)) {
            params.put("customerId", user.getCustomerId().toString());
            params.put("transactionType", null);
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            FrontPage frontPage = remoteManageService.getOtclists(params);
            return frontPage;

        } else if ("1".equals(type)) {
            params.put("customerId", user.getCustomerId().toString());
            params.put("transactionType", type);
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            FrontPage frontPage = remoteManageService.getOtclists(params);
            return frontPage;

        } else if ("2".equals(type)) {
            params.put("customerId", user.getCustomerId().toString());
            params.put("transactionType", type);
            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            FrontPage frontPage = remoteManageService.getOtclists(params);
            return frontPage;
        }
        return new FrontPage(null, 0, 1, 10);

    }
    /*
     * zongwei
     * 委托单撤销
     * */

    @RequestMapping("/otc/OtcListclose")
    @ResponseBody
    public JsonResult OtcListclose(HttpServletRequest request) {

        JsonResult jsonResult = new JsonResult();
        User user = SessionUtils.getUser(request);
        String transactionid = request.getParameter("transactionid");

        if (user != null) {
            //判断是否实名
            if (user.getStates() != 2) {
                return new JsonResult().setSuccess(false).setMsg("请到个人中心进行实名!");
            }
            OtcOrderTransactionMange otcOrderTransactionMange = new OtcOrderTransactionMange();
            otcOrderTransactionMange.setId(Integer.valueOf(transactionid).longValue());

            RemoteManageService remoteManageService = SpringContextUtil.getBean("remoteManageService");
            RemoteResult remoteResult = remoteManageService.OtcListclose(Integer.valueOf(transactionid).longValue());
            if (remoteResult.getSuccess()) {
                return jsonResult.setMsg("操作成功！").setSuccess(true);
            } else {
                return jsonResult.setMsg(remoteResult.getMsg()).setSuccess(false);
            }

        } else {
            return jsonResult.setMsg("未登陆或登陆失效").setSuccess(false);
        }

    }




}
