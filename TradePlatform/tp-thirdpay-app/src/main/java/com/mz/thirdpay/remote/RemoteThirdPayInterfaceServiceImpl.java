/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月6日 下午4:26:15
 */
package com.mz.thirdpay.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dcfs.esb.ftp.client.https.FtpGet;
import com.dcfs.esb.ftp.server.msg.FileMsgBean;
import com.ecc.emp.data.KeyedCollection;
import com.mz.ThirdPayInterfaceService;
import com.mz.app.log.service.AppLogThirdInterfaceService;
import com.mz.core.annotation.PayAfter;
import com.mz.core.annotation.ThirdPayControllerLog;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.redis.common.utils.RedisService;
import com.mz.remote.RemoteThirdPayInterfaceService;
import com.mz.thirdpay.AppThirdPayConfig;
import com.mz.thirdpay.biz.service.AppThirdPayConfigService;
import com.mz.thirdpay.utils.Base64;
import com.mz.thirdpay.utils.DockingBankUtil;
import com.mz.thirdpay.utils.SignUtil;
import com.mz.thirdpay.utils.WebClient;
import com.mz.util.QueryFilter;
import com.mz.util.message.MessageConstant;
import com.mz.utils.CommonRequest;
import com.pabank.sdk.PABankSDK;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import com.mz.core.constant.StringConstant;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Zhang Xiaofang
 * @Date : 2016年7月6日 下午4:26:15
 */
public class RemoteThirdPayInterfaceServiceImpl extends PABankSDK implements
    RemoteThirdPayInterfaceService {

  @Resource
  private AppLogThirdInterfaceService appLogThirdInterfaceService;

  @Resource
  private AppThirdPayConfigService appThirdPayConfigService;

  @Autowired
  private RedisService redisService;

  public static Properties THIRD = new Properties();
  public static Properties ART_THIRD = new Properties();
  public static String IMPL = null;
  public static String IMPL_SHANFUPAY = null;
  public static String IMPL_GOPAY = null;
  public static String IMPL_YINWAN = null;
  public static String IMPL_DINPAY = null;
  public static String IMPL_HUICAOPAY = null;
  public static String IMPL_IPSPAY = null;
  public static String IMPL_JINKONGPAY = null;
  public static String IMPL_INPAY = null;
  public static String IMPL_INWANPAY = null;
  private static String MRCHCODE = "";// 测试商户号(平台号)
  private static String MERCHANT_NUMBER = "";// 跨行支付测试商户号
  private static String FUNDSUMMARYACCTNO = "";// 测试资金汇总账户

  static {
    // 获取当前第三方的实现类
    try {
      InputStream in = RemoteThirdPayInterfaceServiceImpl.class
          .getResourceAsStream("/thirdpayConfig/thirdPayConfig.properties");
      InputStream ins = RemoteThirdPayInterfaceServiceImpl.class
          .getResourceAsStream("/thirdPayConfig.properties");
      THIRD.load(in);
      ART_THIRD.load(ins);
      if (THIRD.containsKey("thirdImpl")) {
        IMPL = THIRD.getProperty("thirdImpl");
      }
      if (THIRD.containsKey("thirdImpl_shanfupay")) {// 闪付的URL
        IMPL_SHANFUPAY = THIRD.getProperty("thirdImpl_shanfupay");
      }
      if (THIRD.containsKey("thirdImpl_yinwanpay")) {// 银湾付的URL
        IMPL_INWANPAY = THIRD.getProperty("thirdImpl_yinwanpay");
      }
      if (THIRD.containsKey("thirdImpl_gopay")) {// 国付宝的URL
        IMPL_GOPAY = THIRD.getProperty("thirdImpl_gopay");
      }
      if (THIRD.containsKey("thirdImpl_dinpay")) {// 智付的URL
        IMPL_DINPAY = THIRD.getProperty("thirdImpl_dinpay");
      }
      if (THIRD.containsKey("thirdImpl_huicaopay")) {// huicao的URL
        IMPL_HUICAOPAY = THIRD.getProperty("thirdImpl_huicaopay");
      }
      if (THIRD.containsKey("thirdImpl_ipspay")) {// 环迅的URL
        IMPL_IPSPAY = THIRD.getProperty("thirdImpl_ipspay");
      }
      if (THIRD.containsKey("thirdImpl_jinkongpay")) {// 现代金控的URL
        IMPL_JINKONGPAY = THIRD.getProperty("thirdImpl_jinkongpay");
      }

      MRCHCODE = ART_THIRD.getProperty("mrchcode");
      MERCHANT_NUMBER = ART_THIRD.getProperty("merchant_number");
      FUNDSUMMARYACCTNO = ART_THIRD.getProperty("fund_summary_acctno");

      // 初始化配置
      PABankSDK.init(ART_THIRD.getProperty("certificate_path"));
      System.out.println("certificate_path::::::::" + ART_THIRD.getProperty("certificate_path"));
      // 验证开发者
      PABankSDK.getInstance().approveDev();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 充值（入金）回调
   */
  @PayAfter
  @Override
  public CommonRequest rechargeCallBack(Map<String, Object> map) {
    CommonRequest result = new CommonRequest();
    ThirdPayInterfaceService thirdPayInterfaceService = null;
    try {
      if (map.containsKey("thirdPayConfig") && map.get("thirdPayConfig") != null) {
        String thirdPayConfig = (String) map.get("thirdPayConfig");
        if ("shanfupay".equals(thirdPayConfig)) {
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL_SHANFUPAY)
              .newInstance();
        } else if ("gopay".equals(thirdPayConfig)) {
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL_GOPAY)
              .newInstance();
        } else if ("yinwanpay".equals(thirdPayConfig)) {
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL_INWANPAY)
              .newInstance();
          thirdPayInterfaceService.rechargeCallBack(map);

        } else if ("dinpay".equals(thirdPayConfig)) {
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL_DINPAY)
              .newInstance();
        } else if ("huicaopay".equals(thirdPayConfig)) {
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL_HUICAOPAY)
              .newInstance();
        } else if ("ipspay".equals(thirdPayConfig)) {
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL_IPSPAY)
              .newInstance();
        } else if (thirdPayConfig.startsWith("jinkongpay")) {
          // 需要向map中多加个参数，用以区分是哪种充值回调
          // 微信，支付宝，有收银台的，没收银台的
          map.put("rechargeCallBackType", thirdPayConfig.split("_")[1]);
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL_JINKONGPAY)
              .newInstance();
        } else if (thirdPayConfig.startsWith("inpay")) {
          // 需要向map中多加个参数，用以区分是哪种充值回调
          // 微信，支付宝，有收银台的，没收银台的
          // map.put("rechargeCallBackType",
          // thirdPayConfig.split("_")[1]);
          thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL).newInstance();
        }
      } else {
        result.setResponseMsg("充值回调方法未传递第三方标识");
        result.setResponseCode("exception");
        return result;
      }

      CommonRequest req = thirdPayInterfaceService.rechargeCallBack(map);

      result.setResponseCode(req.getResponseCode());
      result.setResponseObj(req.getResponseObj());
      result.setResponseMsg(req.getResponseMsg());
      result.setRequestNo(req.getRequestNo());
      result.setQueryOrderNo(req.getQueryOrderNo());
      result.setAmount(req.getAmount());
    } catch (Exception e) {
      result.setResponseMsg("充值回调业务处理异常,请检查manage第三方配置！");
      result.setResponseCode("exception");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 后台进行提现操作
   */
  @PayAfter
  @Override
  public CommonRequest withdraw(CommonRequest request) {
    CommonRequest result = new CommonRequest();
    ThirdPayInterfaceService thirdPayInterfaceService = null;
    String thirdName = "";
    String thirdPayImpl = "";
    String withdrawInterface = "";
    withdrawInterface = THIRD.getProperty("withdrawInterface");
    thirdPayImpl = THIRD.getProperty("thirdImpl_" + withdrawInterface);
    if (null != withdrawInterface && !"".equals(withdrawInterface)) {
      if (getIsOpen("withdrawInterface").equals("0")) {
        if (null != thirdPayImpl && !"".equals(thirdPayImpl)) {
          try {
            thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(thirdPayImpl)
                .newInstance();
          } catch (Exception e) {
            result.setResponseCode("fail");
            result.setResponseMsg(thirdName + "第三方配置信息有误");
          }
          CommonRequest req = new CommonRequest();
          req.setBaseUrl(request.getBaseUrl());
          req.setAmount(request.getAmount());
          req.setRequestNo(request.getRequestNo());
          req.setBankAccNum(request.getBankAccNum());
          req.setBankAcctName(request.getBankAcctName());
          req.setBankBranchName(request.getBankBranchName());
          req.setBankCity(request.getBankCity());
          req.setIdCard(request.getIdCard());
          req.setBankName(request.getBankName());
          req.setBankProvince(request.getBankProvince());
          req.setTransactionDateTime(request.getTransactionDateTime());
          req.setRequestUser(request.getRequestUser());
          CommonRequest ret = thirdPayInterfaceService.withdraw(req);
          result.setRequestNo(ret.getRequestNo());
          result.setResponseCode(ret.getResponseCode());
          result.setResponseMsg(ret.getResponseMsg());
          result.setRequestUrl(ret.getRequestUrl());
          result.setRequestUser(ret.getRequestUser());
          result.setRequestThirdPay(ret.getRequestThirdPay());
          result.setResponseObj(ret.getResponseObj());
          result.setAmount(request.getAmount());
        } else {
          result.setResponseCode("fail");
          result.setResponseMsg(thirdName + "第三方配置信息有误");
        }
      } else {
        result.setResponseCode(MessageConstant.TEST);
        result.setResponseMsg(thirdName + "第三方接口关闭");
      }

    }

    return result;
  }

  /**
   * 查询闪付的提现流水
   * <p>
   * TODO
   * </p>
   *
   * @author: Zhang Lei
   * @param: @param request
   * @param: @return
   * @return: JsonResult
   * @Date : 2016年12月9日 下午8:39:39
   * @throws:
   */
  @Override
  public JsonResult shanFuQueryOrder(CommonRequest request) {
    JsonResult jsonResult = new JsonResult();
    ThirdPayInterfaceService thirdPayInterfaceService = null;
    String withdrawInterface = "";
    String thirdPayImpl = "";
    try {
      withdrawInterface = THIRD.getProperty("withdrawInterface");
      thirdPayImpl = THIRD.getProperty("thirdImpl_" + withdrawInterface);
      if (null != withdrawInterface && !"".equals(withdrawInterface)) {

        CommonRequest req = new CommonRequest();
        req.setTrueName(request.getTrueName());
        req.setIdCard(request.getIdCard());
        req.setRequestNo(request.getRequestNo());
        thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(thirdPayImpl)
            .newInstance();
        CommonRequest ret = thirdPayInterfaceService.queryOrder(req);
        if (ret != null && "success".equals(ret.getResponseCode())) {
          jsonResult.setSuccess(true);
          jsonResult.setObj(ret.getResponseObj());
          jsonResult.setMsg(ret.getResponseMsg());
        } else if (ret != null && "fail".equals(ret.getResponseCode())) {
          jsonResult.setSuccess(false);
          jsonResult.setObj(ret.getResponseObj());
          jsonResult.setMsg(ret.getResponseMsg());
        } else {
          jsonResult.setSuccess(null);
        }
      } else {
        request.setResponseMsg("第三方配置信息有误");
      }
    } catch (Exception e) {
      e.printStackTrace();
      request.setResponseMsg("第三方暂未对接");
    }
    return jsonResult;
  }
  // ===============================================================================

  @ThirdPayControllerLog
  @PayAfter
  @Override
  public JsonResult recharge(HttpServletResponse response, CommonRequest request) {
    return null;
  }

  /**
   * 后台进行充值（入金）操作
   */
  @ThirdPayControllerLog
  @PayAfter
  @Override
  public JsonResult recharge(CommonRequest request) {
    JsonResult jsonResult = new JsonResult();
    ThirdPayInterfaceService thirdPayInterfaceService;
    try {
      thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL).newInstance();
      HttpServletResponse response = null;
      CommonRequest req = new CommonRequest();
      req.setAmount(request.getAmount());
      req.setRequestNo(request.getRequestNo());
      CommonRequest ret = thirdPayInterfaceService.recharge(response, req);
      jsonResult.setSuccess(true);
      jsonResult.setObj(ret.getResponseObj());
      jsonResult.setMsg(ret.getResponseMsg());
    } catch (Exception e) {
      jsonResult.setSuccess(false);
      jsonResult.setMsg("第三方暂未对接");
    }
    return jsonResult;
  }

  @ThirdPayControllerLog
  @PayAfter
  @Override
  public JsonResult queryOrder(CommonRequest request) {
    JsonResult jsonResult = new JsonResult();
    ThirdPayInterfaceService thirdPayInterfaceService = null;
    AppThirdPayConfig appThirdPayConfig = null;
    String thirdName = "";
    try {
      QueryFilter filter = new QueryFilter(AppThirdPayConfig.class);
      filter.addFilter("currentType", "1");
      appThirdPayConfig = appThirdPayConfigService.get(filter);
      if (null != appThirdPayConfig.getThirdPayName()) {
        thirdName = appThirdPayConfig.getThirdPayName();
      }
      if (null != appThirdPayConfig.getRemark1() && !"".equals(appThirdPayConfig.getRemark1())) {

        thirdPayInterfaceService = (ThirdPayInterfaceService) Class
            .forName(appThirdPayConfig.getRemark1()).newInstance();

        CommonRequest req = new CommonRequest();
        req.setQueryOrderNo(request.getQueryOrderNo());
        req.setQueryOrderDate(request.getQueryOrderDate());
        req.setRequestNo(request.getRequestNo());
        CommonRequest ret = thirdPayInterfaceService.queryOrder(req);

        jsonResult.setSuccess(true);
        jsonResult.setObj(ret);
        jsonResult.setMsg(ret.toString());

      } else {
        jsonResult.setSuccess(true);
        jsonResult.setObj("");
        jsonResult.setMsg(thirdName + "第三方配置信息有误");
      }
    } catch (Exception e) {
      jsonResult.setSuccess(true);
      jsonResult.setObj("");
      jsonResult.setMsg(thirdName + "第三方暂未对接");
      // TODO Auto-generated catch block
      // e.printStackTrace();
    }

    return jsonResult;
  }

  @PayAfter
  @Override
  public CommonRequest withdrawCallBack(Map<String, Object> map) {

    CommonRequest result = new CommonRequest();

    ThirdPayInterfaceService thirdPayInterfaceService;
    try {
      thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL).newInstance();
      CommonRequest req = thirdPayInterfaceService.withdrawCallBack(map);
      result.setResponseCode(req.getResponseCode());
      result.setResponseObj(req.getResponseObj());
      result.setResponseMsg(req.getResponseMsg());
      result.setRequestNo(req.getRequestNo());
      result.setAmount(req.getAmount());

    } catch (Exception e) {
      result.setResponseMsg("提现回调业务处理异常");
      result.setResponseCode("exception");
      // e.printStackTrace();
    }
    return result;
  }

  // 后台有开关功能 查询接口是否开启
  public String getIsOpen(String key) {
    //
    String value = "1";
    if (null != key && !"".equals(key)) {
      String data = "";
      data = redisService.get(StringConstant.CONFIG_CACHE + ":all");
      if (null != data && !"".equals(data)) {
        JSONObject obj = JSON.parseObject(data);
        if (obj.containsKey(key)) {
          value = obj.get(key).toString();
        }

      }
    }
    return value;
  }

  // 查询身份证信息
  @Override
  public JsonResult checkIdentity(CommonRequest request) {
    JsonResult jsonResult = new JsonResult();
    ThirdPayInterfaceService thirdPayInterfaceService;
    if ("0".equals(getIsOpen("checkIdentityInterface"))) {
      try {
        thirdPayInterfaceService = (ThirdPayInterfaceService) Class.forName(IMPL).newInstance();

        CommonRequest req = new CommonRequest();
        req.setTrueName(request.getTrueName());
        req.setIdCard(request.getIdCard());
        req.setRequestNo(request.getRequestNo());
        CommonRequest ret = thirdPayInterfaceService.checkIdentity(req);
        if ("success".equals(ret.getResponseCode())) {
          jsonResult.setSuccess(true);
          jsonResult.setObj(ret.getResponseObj());
          jsonResult.setMsg(ret.getResponseMsg());
        } else {
          jsonResult.setSuccess(false);

        }

      } catch (Exception e) {
        jsonResult.setSuccess(false);
        jsonResult.setMsg("第三方暂未对接");
        e.printStackTrace();
      }

    } else {
      jsonResult.setSuccess(true);
    }
    return jsonResult;
  }

  /**
   * 会员开立子账户
   *
   * @return SubAcctNo 会员子账号 TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
   */
  public JsonResult openCustAcctId(Long customerAccount, String customerName, Long phone,
      String email) {
    JsonResult jsonResult = new JsonResult();
    try {
      String thirdLogNo = DockingBankUtil.createThirdLogNo();
      // JSON请求报文 流水号 商户号 功能标志 资金汇总账号 交易网会员代码 会员属性
      String req = "{\"CnsmrSeqNo\":\"" + thirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
          + "\",\"FunctionFlag\":\"1\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO
          + "\",\"TranNetMemberCode\":\"" + customerAccount
          + "\",\"MemberProperty\":\"00\",\"UserNickname\":\"" + customerName + "\",\"phone\":\""
          + phone + "\",\"email\":\"" + email + "\"}";
      // 服务调用，传入参数请求报文与服务ID
      Map<String, Object> returnMap = PABankSDK.getInstance().apiInter(req, "OpenCustAcctId");
      DockingBankUtil.saveLog(returnMap, req, "OpenCustAcctId", appLogThirdInterfaceService);
      if (null != returnMap) {
        if ("000000".equals(returnMap.get("TxnReturnCode").toString())) {
          jsonResult.setSuccess(true);
          jsonResult.setObj(returnMap.get("SubAcctNo"));
          jsonResult.setMsg(returnMap.get("TxnReturnMsg").toString());
        } else {
          jsonResult.setSuccess(false);
          jsonResult.setMsg(returnMap.get("TxnReturnMsg").toString());
        }
        System.out.println(thirdLogNo + "===会员开立子账户===OpenCustAcctId:::::" + returnMap);
      }
    } catch (Exception e) {
      e.getMessage();
      jsonResult.setSuccess(false);
      jsonResult.setMsg(e.getMessage());
    }

    return jsonResult;
  }

  /**
   * SubAcctNo : 3279000000006086 TranNetMemberCode : 8001012244 UserNickname : 测试 3279000000011077
   */
  @SuppressWarnings("all")
  public static void main(String[] args) {
    String createThirdLogNo = DockingBankUtil.createThirdLogNo();
    String orderId = DockingBankUtil.getOrderId();
    System.out.println(createThirdLogNo);
    System.out.println("orderId::::" + orderId);
    // 初始化配置
    PABankSDK.init("D:/bankConfig/config.properties");
    // 验证开发者
    PABankSDK.getInstance().approveDev();
    //查询会员资金
//		String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"3279\",\"FundSummaryAcctNo\":\"15000081960389\",\"SubAcctNo\":\"3279000000011128\",\"QueryFlag\":\"2\",\"PageNum\":\"1\"}";
    //会员交易
    String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo
        + "\",\"MrchCode\":\"3279\",\"FunctionFlag\":\"3\",\"FundSummaryAcctNo\":\"15000081960389\",\"OutSubAcctNo\":\"3279000000011208\",\"OutMemberCode\":\"8001002018\",\"OutSubAcctName\":\"灵卉\",\"InSubAcctNo\":\"3279000000011128\",\"InMemberCode\":\"8001005242\",\"InSubAcctName\":\"金子\",\"TranAmt\":\"100.00\",\"TranFee\":\"0.00\",\"TranType\":\"01\",\"Ccy\":\"RMB\",\"OrderNo\":\"66257858\",\"OrderContent\":\"测试\",\"Remark\":\"66257858\"}";
//		String req2 = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"3279\",\"QueryFlag\":\"2\",\"FundSummaryAcctNo\":\"15000081960389\",\"SubAcctNo\":\"3279000000011177\",\"PageNum\":\"1\"}";
    //查询会员信息
    //	String req2 = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"3279\",\"FundSummaryAcctNo\":\"15000081960389\",\"TranNetMemberCode\":\"8001002018\"}";
    //	String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"3279\",\"FundSummaryAcctNo\":\"15000081960389\",\"FunctionFlag\":\"1\",\"SubAcctNo\":\"3279000000011177\",\"MemberProperty\":\"00\"}";
    //冻结资金
//		String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"3279\",\"FundSummaryAcctNo\":\"15000081960389\",\"FunctionFlag\":\"1\",\"SubAcctNo\":\"3279000000011128\",\"TranNetMemberCode\":\"8001005242\",\"TranAmt\":\"100.00\",\"TranCommission\":\"0.00\",\"Ccy\":\"RMB\",\"OrderNo\":\"40161530\"}";

    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "MemberTransaction");
//			returnMap = PABankSDK.getInstance().apiInter(req, "CustAcctIdBalanceQuery");

//			returnMap = PABankSDK.getInstance().apiInter(req2, "QueryCustAcctIdByThirdCustId");
//			returnMap = PABankSDK.getInstance().apiInter(req, "MembershipTrancheFreeze");

      System.out.println(returnMap);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 冻结用户资金 不验证短信码
   *
   * @param FunctionFlag 功能标志          1：冻结（会员→担保）2：解冻（担保→会员）3：清分+冻结 4：见证+收单的冻结资金解冻
   * @param customerAccount 平台账号
   * @param SubAcctNo 子账户
   * @param TranAmt 冻结金额
   * @param TranCommission 解冻时，将根据该金额收取手续费，若无手续费则送0。
   * @param OrderContent 订单说明,不必填
   */
  @Override
  public Map<String, Object> MembershipTrancheFreeze(Integer FunctionFlag, Long customerAccount,
      Long SubAcctNo, BigDecimal TranAmt, BigDecimal TranCommission, String OrderContent) {

    String createThirdLogNo = DockingBankUtil.createThirdLogNo();
    String orderId = DockingBankUtil.getOrderId();
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
          + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"FunctionFlag\":\""
          + FunctionFlag + "\",\"TranNetMemberCode\":\"" + customerAccount + "\",\"SubAcctNo\":\""
          + SubAcctNo + "\",\"TranAmt\":\"" + TranAmt + "\",\"TranCommission\":\"" + TranCommission
          + "\",\"Ccy\":\"RMB\",\"OrderNo\":\"" + orderId + "\",\"OrderContent\":\"" + OrderContent
          + "\",\"Remark\":\"" + orderId + "\"}";
      returnMap = PABankSDK.getInstance().apiInter(req, "MembershipTrancheFreeze");
      if (null != returnMap) {
        if (returnMap.containsKey("TxnReturnCode") && "000000"
            .equals(returnMap.get("TxnReturnCode"))) {
          returnMap.put("orderNum", orderId);
          DockingBankUtil
              .saveLog(returnMap, req, "MembershipTrancheFreeze", appLogThirdInterfaceService);
          System.out
              .println(createThirdLogNo + "===冻结用户资金方法==MembershipTrancheFreeze:::::" + returnMap);
          return returnMap;
        } else {
          System.out
              .println(createThirdLogNo + "===冻结用户资金方法==MembershipTrancheFreeze:::::" + returnMap);
          DockingBankUtil
              .saveLog(returnMap, req, "MembershipTrancheFreeze", appLogThirdInterfaceService);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 查询银行清分状态 FunctionFlag 功能标志 1:全部，2：指定时间段  必填 StartDate    若是指定时间段查询，则必输，当查询全部时，不起作用  yyyyMMdd
   * EndDate	                 同上 PageNum      页码 必填
   */
  @Override
  public Map<String, Object> BankClearQuery(Integer FunctionFlag, String StartDate, String EndDate,
      Integer PageNum) {
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      String thirdLogNo = DockingBankUtil.createThirdLogNo();
      String req = "{\"CnsmrSeqNo\":\"" + thirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
          + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"FunctionFlag\":\""
          + FunctionFlag + "\",\"StartDate\":\"" + StartDate + "\",\"EndDate\":\"" + EndDate
          + "\",\"PageNum\":\"" + PageNum + "\"}";
      // 服务调用，传入参数请求报文与服务ID
      returnMap = PABankSDK.getInstance().apiInter(req, "BankClearQuery");
      DockingBankUtil
          .saveLog(returnMap, req, "BindRelateAcctUnionPay", appLogThirdInterfaceService);
      System.out.println(thirdLogNo + "===查询银行清分状态===BankClearQuery:::::" + returnMap);
      if (null != returnMap && returnMap.containsKey("TxnReturnCode") && "000000"
          .equals(returnMap.get("TxnReturnCode").toString())) {
        return returnMap;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 查询平台余额
   *
   * SupAcctIdBalanceQuery
   */
  @Override
  public Map<String, Object> supAcctIdBalanceQuery() {
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      String thirdLogNo = DockingBankUtil.createThirdLogNo();
      String req = "{\"CnsmrSeqNo\":\"" + thirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
          + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\"}";
      // 服务调用，传入参数请求报文与服务ID
      returnMap = PABankSDK.getInstance().apiInter(req, "SupAcctIdBalanceQuery");
      DockingBankUtil
          .saveLog(returnMap, req, "BindRelateAcctUnionPay", appLogThirdInterfaceService);
      System.out.println(thirdLogNo + "===查询平台余额==SupAcctIdBalanceQuery::::::" + returnMap);
      if (null != returnMap && returnMap.containsKey("TxnReturnCode") && "000000"
          .equals(returnMap.get("TxnReturnCode").toString())) {
        return returnMap;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * 会员绑定提现账户-银联鉴权 绑定银行卡
   *
   * @param SubAcctNo 子账户账号 3279000000006047
   * @param customerAccount 账号
   * @param customerName 会员名称
   * @param MemberGlobalType 会员证件类型
   * @param MemberGlobalId 会员证件号码
   * @param MemberAcctNo 会员账号
   * @param BankType 银行类型 1 本行 2 他行
   * @param AcctOpenBranchName 开户行名称
   * @param EiconBankBranchId 超级网银号
   * @param CnapsBranchId 大小额联行号
   * @param Mobile 手机号 用来接收短信
   * @return TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
   */
  @SuppressWarnings("all")
  @Override
  public Map<String, Object> bindRelateAcctUnionPay(Long SubAcctNo, Long customerAccount,
      String customerName, Integer MemberGlobalType, String MemberGlobalId, String MemberAcctNo,
      String BankType, String AcctOpenBranchName, String EiconBankBranchId, String CnapsBranchId,
      Long Mobile) {

    String thirdLogNo = DockingBankUtil.createThirdLogNo();
    System.out.println("流水号::::" + thirdLogNo);
    // JSON请求报文 流水号 商户号 资金汇总账号 子账户账号 交易网会员代码 会员名称 会员证件类型 会员证件号码 银行卡号码 银行 类型
    // 开户行名称 超级网银号 大小额联行号 手机号
    String req = "{\"CnsmrSeqNo\":\"" + thirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
        + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"SubAcctNo\":\"" + SubAcctNo
        + "\",\"TranNetMemberCode\":\"" + customerAccount + "\",\"MemberName\":\"" + customerName
        + "\",\"MemberGlobalType\":\"" + MemberGlobalType + "\",\"MemberGlobalId\":\""
        + MemberGlobalId + "\",\"MemberAcctNo\":\"" + MemberAcctNo + "\",\"BankType\":\"" + BankType
        + "\",\"AcctOpenBranchName\":\"" + AcctOpenBranchName + "\",\"EiconBankBranchId\":\""
        + EiconBankBranchId + "\",\"CnapsBranchId\":\"\",\"Mobile\":\"" + Mobile + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "BindRelateAcctUnionPay");
      System.out.println(thirdLogNo + "===会员绑定提现账户====BindRelateAcctUnionPay:::::" + returnMap);
      DockingBankUtil
          .saveLog(returnMap, req, "BindRelateAcctUnionPay", appLogThirdInterfaceService);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 会员绑定提现账户-银联鉴权 绑定银行卡 回填短信验证码
   *
   * @param customerAccount 账号
   * @param SubAcctNo 会员子账号号码
   * @param MemberAcctNo 银行卡号
   * @param MessageCheckCode 短信验证码
   * @return TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
   */
  @Override
  public Map<String, Object> bindRelateAccReUnionPay(Long SubAcctNo, Long customerAccount,
      String MemberAcctNo, String MessageCheckCode) {
    String thirdLogNo = DockingBankUtil.createThirdLogNo();
    // JSON请求报文 流水号 商户号 资金汇总账号 交易网站账号 子账户账号 银行卡号码 短信
    String req = "{\"CnsmrSeqNo\":\"" + thirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
        + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"TranNetMemberCode\":\""
        + customerAccount + "\",\"SubAcctNo\":\"" + SubAcctNo + "\",\"MemberAcctNo\":\""
        + MemberAcctNo + "\",\"MessageCheckCode\":\"" + MessageCheckCode + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "BindRelateAccReUnionPay");
      System.out
          .println(thirdLogNo + "===会员绑定提现账户回填短信码====BindRelateAccReUnionPay:::::" + returnMap);
      DockingBankUtil
          .saveLog(returnMap, req, "BindRelateAccReUnionPay", appLogThirdInterfaceService);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 会员解绑提现账户
   *
   * @param customerAccount 账号
   * @param SubAcctNo 会员子账号号码
   * @param MemberAcctNo 银行卡号
   * @return TxnReturnCode 000000成功 其他都是失败 CnsmrSeqNo 请求流水 TxnReturnMsg 返回消息
   */
  @Override
  public Map<String, Object> unbindRelateAcct(Long SubAcctNo, Long customerAccount,
      String MemberAcctNo) {
    String thirdLogNo = DockingBankUtil.createThirdLogNo();
    // JSON请求报文 流水号 商户号 功能标志 1解绑 资金汇总账号 交易网会员代码 子账户账号 银行卡号
    String req =
        "{\"CnsmrSeqNo\":\"" + DockingBankUtil.createThirdLogNo() + "\",\"MrchCode\":\"" + MRCHCODE
            + "\",\"FunctionFlag\":\"1\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO
            + "\",\"TranNetMemberCode\":\"" + customerAccount + "\",\"SubAcctNo\":\"" + SubAcctNo
            + "\",\"MemberAcctNo\":\"" + MemberAcctNo + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "UnbindRelateAcct");
      System.out.println(thirdLogNo + "===会员解绑提现账户==UnbindRelateAcct::::" + returnMap);
      DockingBankUtil.saveLog(returnMap, req, "UnbindRelateAcct", appLogThirdInterfaceService);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 充值 在途
   *
   * @param transactionMoney 充值金额
   * @param issInsCode 发卡机构代码
   */
  @Override
  public String[] memberRecharge(BigDecimal transactionMoney, String issInsCode, Long SubAccNo,
      String path, Long customerAccount) {
    HashMap<String, String> map = new HashMap<>();
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String timestamp = formatter.format(date);
    String datetamp = timestamp.substring(0, 8);
    String orderId = MERCHANT_NUMBER + datetamp + DockingBankUtil.getOrderId();
    try {
      PayclientInterfaceUtil util = new PayclientInterfaceUtil();
      KeyedCollection signDataput = new KeyedCollection("signDataput");
      String orig = "";
      String sign = "";
      String encoding = "GBK";
      KeyedCollection inputOrig = getInputOrig(transactionMoney, issInsCode, orderId, SubAccNo,
          customerAccount);
      signDataput = util.getSignData(inputOrig);

      orig = (String) signDataput.getDataValue("orig");
      sign = (String) signDataput.getDataValue("sign");

      orig = PayclientInterfaceUtil.Base64Encode(orig, encoding);
      sign = PayclientInterfaceUtil.Base64Encode(sign, encoding);

      orig = URLEncoder.encode(orig, encoding);
      sign = URLEncoder.encode(sign, encoding);

      map.put("orig", orig);
      map.put("sign", sign);
      //	map.put("pageFlag", "Y");

      map.put("returnurl", path + "/appTransaction/rechargeReturn");
      map.put("NOTIFYURL", path + "/appTransaction/rechargeNotify");

      String[] operateParameter = WebClient
          .operateParameter(ART_THIRD.getProperty("pay_path"), map, "utf-8");
      String[] parmeter = new String[3];
      parmeter[0] = operateParameter[0];
      parmeter[1] = operateParameter[1];
      parmeter[2] = orderId;
      String params =
          "transactionMoney:" + transactionMoney + ",SubAccNo:" + SubAccNo + ",orderId:" + orderId;
      DockingBankUtil
          .saveLog(null, params, "https://my-uat1.orangebank.com.cn/khpayment/khPayment_sfj.do",
              appLogThirdInterfaceService);

      return parmeter;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 生成充值所需的orig
   */
  private KeyedCollection getInputOrig(BigDecimal transactionMoney, String issInsCode,
      String orderId, Long SubAccNo, Long customerAccount) {
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String timestamp = formatter.format(date);
    String datetamp = timestamp.substring(0, 8);

    KeyedCollection inputOrig = new KeyedCollection("inputOrig");

    // 商户号 *
    inputOrig.put("masterId", MERCHANT_NUMBER);
    // 订单号，严格遵守格式：商户号+8位日期YYYYMMDD+8位流水 *
    inputOrig.put("orderId", orderId);
    // 币种，目前只支持RMB *
    inputOrig.put("currency", "RMB");
    // 下单时间，YYYYMMDDHHMMSS *
    inputOrig.put("paydate", timestamp);
    // 订单金额，12整数，2小数 *
    inputOrig.put("amount", transactionMoney);
    // 备注字段
    inputOrig.put("remark", getRemark(SubAccNo));
    // 订单款项描述（商户自定）*
    inputOrig.put("objectName", "KHpaygate");
    // 订单有效期(毫秒)，0不生效
    inputOrig.put("validtime", "0");
    //用户账号
    inputOrig.put("customerId", customerAccount);
    // 支付方式，01：B2B网关 银行卡 ，02：B2C网关 网银
    //	inputOrig.put("payType", "02");
    // 发卡机构代码
    //	inputOrig.put("issInsCode", "CEB");
    // 支付卡类型，01：借记卡，02：信用卡,00:混合(b2b都送00)
    //	inputOrig.put("payCardType", "00");

    return inputOrig;
  }

  /**
   * 获取充值所需的remark字段
   */
  @SuppressWarnings("all")
  private String getRemark(Long SubAccNo) {
    ArrayList listsum = new ArrayList();
    Map<String, Object> dataMap0 = new HashMap<String, Object>();
    ArrayList list = new ArrayList();
    Map<String, String> dataMap = new HashMap<String, String>();

    dataMap.put("SubAccNo", SubAccNo.toString());// 子帐号
    dataMap.put("PayModel", "1");// 0-冻结支付 1-普通支付 支付模式，根据需要模式赋值0或者1
    dataMap.put("TranFee", "0.00");// 手续费
    list.add(dataMap);

    dataMap0.put("oderlist", list);
    dataMap0.put("remarktype", "SDS0100000");// 统一赋值为SDS0100000
    dataMap0.put("plantCode", MRCHCODE);// 见证系统提供 平台代码
    dataMap0.put("SFJOrdertype", "0");// 目前统一赋值为0 0-普通支付订单，1-子订单信息
    listsum.add(dataMap0);

    return JSON.toJSONString(listsum);
  }

  /**
   * 查询绑定银行卡信息
   */
  @Override
  public void memberBindQuery() {
    try {
      // 查询绑卡信息
      String req = "{\"CnsmrSeqNo\":\"" + DockingBankUtil.createThirdLogNo() + "\",\"MrchCode\":\""
          + MRCHCODE + "\",\"QueryFlag\":\"2\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO
          + "\",\"PageNum\":\"1\"}";
      // 服务调用，传入参数请求报文与服务ID
      Map<String, Object> returnMap = PABankSDK.getInstance().apiInter(req, "MemberBindQuery");

      if (null != returnMap.get("StartRecordNo") && StringUtils
          .isNotEmpty(returnMap.get("StartRecordNo").toString())) {
        Integer StartRecordNo = Integer.valueOf(returnMap.get("StartRecordNo").toString());
      }

      if (null != returnMap.get("TotalNum") && StringUtils
          .isNotEmpty(returnMap.get("TotalNum").toString())) {
        Integer TotalNum = Integer.valueOf(returnMap.get("TotalNum").toString());
      }

      System.out.println(returnMap.get("TranItemArray"));
      JSONArray ja = (JSONArray) returnMap.get("TranItemArray"); // 一个未转化的字符串
      Iterator<Object> it = ja.iterator();

      while (it.hasNext()) {
        JSONObject ob = (JSONObject) it.next();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 提现 支持手续费
   *
   * @param SubAcctNo 子账户账号
   * @param customerAccount 会员账号
   * @param customerName 会员名称
   * @param TakeCashAcctNo 绑定银行卡号
   * @param TakeCashAcctName 绑定银行卡开户人名称
   * @param CashAmt 提现金额
   * @param TakeCashCommission 手续费
   */
  @Override
  public Map<String, Object> memberWithdrawCash(Long SubAcctNo, Long customerAccount,
      String customerName, String TakeCashAcctNo, String TakeCashAcctName, BigDecimal CashAmt,
      BigDecimal TakeCashCommission) {
    String MessageOrderNo = applicationTextMsgDynamicCode(SubAcctNo, customerAccount, CashAmt);
    String createThirdLogNo = DockingBankUtil.createThirdLogNo();
    // 流水 商户号 资金汇总账户 子账户账号 用户账号 用户名称 绑定的银行卡卡号 银行卡 开户人姓名 币种 提现金额 手续费 短信指令号
    // 通过接口获取
    String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
        + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"SubAcctNo\":\"" + SubAcctNo
        + "\",\"TranNetMemberCode\":\"" + customerAccount + "\",\"SubAcctName\":\"" + customerName
        + "\",\"TakeCashAcctNo\":\"" + TakeCashAcctNo + "\",\"TakeCashAcctName\":\""
        + TakeCashAcctName + "\",\"Ccy\":\"RMB\",\"CashAmt\":\"" + CashAmt
        + "\",\"TakeCashCommission\":\"" + TakeCashCommission + "\",\"MessageOrderNo\":\""
        + MessageOrderNo + "\",\"MessageCheckCode\":\"\",\"Remark\":\"" + createThirdLogNo + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    try {
      Map<String, Object> returnMap = PABankSDK.getInstance().apiInter(req, "MemberWithdrawCash");
      System.out.println(createThirdLogNo + "===提现处理::::" + returnMap);
      DockingBankUtil.saveLog(returnMap, req, "MemberWithdrawCash", appLogThirdInterfaceService);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取提现验证码
   *
   * @param SubAcctNo 会员子账号
   * @param TranNetMemberCode 平台会员账号
   * @param TranAmt 交易金额
   * @return 短信指令号
   */
  @Override
  public String applicationTextMsgDynamicCode(Long SubAcctNo, Long TranNetMemberCode,
      BigDecimal TranAmt) {
    String thirdLogNo = DockingBankUtil.createThirdLogNo();
    // 提现短信验证码 ApplicationTextMsgDynamicCode
    String req =
        "{\"CnsmrSeqNo\":\"" + DockingBankUtil.createThirdLogNo() + "\",\"MrchCode\":\"" + MRCHCODE
            + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"SubAcctNo\":\"" + SubAcctNo
            + "\",\"TranNetMemberCode\":\"" + TranNetMemberCode
            + "\",\"TranType\":\"1\",\"TranAmt\":\"" + TranAmt + "\"}";

    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "ApplicationTextMsgDynamicCode");
      DockingBankUtil
          .saveLog(returnMap, req, "ApplicationTextMsgDynamicCode", appLogThirdInterfaceService);
      System.out
          .println(thirdLogNo + "==获取提现验证码==ApplicationTextMsgDynamicCode::::::::" + returnMap);
      return (String) returnMap.get("MessageOrderNo");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 查询会员账户信息
   *
   * @param QueryFlag 查询标志2：普通会员子账号 3：功能子账号
   */
  public Map<String, Object> custAcctIdBalanceQuery(Long SubAcctNo, Integer PageNum,
      Integer QueryFlag) {
    String createThirdLogNo = DockingBankUtil.createThirdLogNo();
    String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
        + "\",\"QueryFlag\":\"" + QueryFlag + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO
        + "\",\"SubAcctNo\":\"" + SubAcctNo + "\",\"PageNum\":\"" + PageNum + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "CustAcctIdBalanceQuery");
      DockingBankUtil
          .saveLog(returnMap, req, "CustAcctIdBalanceQuery", appLogThirdInterfaceService);
      System.out.println(createThirdLogNo + "===查询会员账户信息==CustAcctIdBalanceQuery:::" + returnMap);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 会员间交易(不验证)
   *
   * @param FunctionFlag 功能标志
   * @param OutSubAcctNo 付款方子账户
   * @param OutMemberCode 付款方会员账户
   * @param OutSubAcctName 付款方名称
   * @param InSubAcctNo 收款方子账户
   * @param InMemberCode 收款方会员账户
   * @param InSubAcctName 收款方名称
   * @param TranAmt 交易金额
   * @param TranFee 交易手续费
   */
//	@Override
  public Map<String, Object> memberTransaction(Integer FunctionFlag, Long OutSubAcctNo,
      Long OutMemberCode, String OutSubAcctName, Long InSubAcctNo, Long InMemberCode,
      String InSubAcctName, BigDecimal TranAmt, BigDecimal TranFee) {
    String createThirdLogNo = DockingBankUtil.createThirdLogNo();
    String OrderNo = DockingBankUtil.getOrderId();
    // 功能标志: 6 直接支付 转出子账户(付款方) 资金汇总账户 付款方 账号
    String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
        + "\",\"FunctionFlag\":\"" + FunctionFlag + "\",\"FundSummaryAcctNo\":\""
        + FUNDSUMMARYACCTNO + "\",\"OutSubAcctNo\":\"" + OutSubAcctNo + "\",\"OutMemberCode\":\""
        + OutMemberCode + "\",\"OutSubAcctName:\"" + OutSubAcctName + "\",\"InSubAcctNo\":\""
        + InSubAcctNo + "\",\"InMemberCode\":\"" + InMemberCode + "\",\"InSubAcctName\":\""
        + InSubAcctName + "\",\"TranAmt\":\"" + TranAmt + "\",\"TranFee\":\"" + TranFee
        + "\",\"TranType\":\"01\",\"Ccy\":\"RMB\",\"OrderNo\":\"" + OrderNo + "\",\"Remark\":\""
        + OrderNo + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "MemberTransaction");
      System.out.println(createThirdLogNo + "===会员间交易(不验证)==MemberTransaction:::" + returnMap);
      DockingBankUtil.saveLog(returnMap, req, "MemberTransaction", appLogThirdInterfaceService);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 查询单笔交易结果
   *
   * @param FunctionFlag 2：会员间交易 3：提现 4：充值
   * @param TranNetSeqNo 交易网流水号
   * @param SubAcctNo 会员子账户
   */
  @Override
  public Map<String, Object> singleTransactionStatusQuery(Integer FunctionFlag, String TranNetSeqNo,
      Long SubAcctNo) {
    String createThirdLogNo = DockingBankUtil.createThirdLogNo();

    String req = "{\"CnsmrSeqNo\":\"" + createThirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
        + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"FunctionFlag\":\""
        + FunctionFlag + "\",\"TranNetSeqNo\":\"" + TranNetSeqNo + "\",\"SubAcctNo\":\"" + SubAcctNo
        + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "SingleTransactionStatusQuery");
      DockingBankUtil
          .saveLog(returnMap, req, "SingleTransactionStatusQuery", appLogThirdInterfaceService);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 查询对账文件信息
   *
   * @param fileDate 日期
   * @param fileType 充值 CZ 提现 TX
   */
  @Override
  public Map<String, Object> queryAccountFileInfo(String fileDate, String fileType) {
    String thirdLogNo = DockingBankUtil.createThirdLogNo();
    System.out.println(thirdLogNo);
    // 查询对账文件信息 ReconciliationDocumentQuery 充值文件-CZ 提现文件-TX
    String req = "{\"CnsmrSeqNo\":\"" + thirdLogNo + "\",\"MrchCode\":\"" + MRCHCODE
        + "\",\"FundSummaryAcctNo\":\"" + FUNDSUMMARYACCTNO + "\",\"FileType\":\"" + fileType
        + "\",\"FileDate\":\"" + fileDate + "\"}";
    // 服务调用，传入参数请求报文与服务ID
    Map<String, Object> returnMap;
    try {
      returnMap = PABankSDK.getInstance().apiInter(req, "ReconciliationDocumentQuery");
      System.out.println(fileType + "===查询对账文件信息 ==" + returnMap);
      DockingBankUtil
          .saveLog(returnMap, req, "SingleTransactionStatusQuery", appLogThirdInterfaceService);
      return returnMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 下载对账文件
   *
   * @param remoteFile 对账文件 服务器路径
   * @param localFile 本地路径
   * @param privateAuth 提取码
   * @param RandomPassword 随机密码  用来解密对账文件
   */
  @Override
  public boolean downloadFile(String remoteFile, String localFile, String privateAuth,
      String RandomPassword) {
    FtpGet ftpGet = null;
    FileMsgBean bean = null;
    try {
      bean = new FileMsgBean();
      ftpGet = new FtpGet(remoteFile, localFile, false, null, privateAuth, bean);
      ftpGet.doGetFile();

      String ALG = "DesEde/CBC/PKCS5Padding";
      String key = RandomPassword;
      String srcFile = localFile;//+".enc";
      String zipFile = srcFile + ".zip";
      String desFile = srcFile + ".pre";//;
      byte[] bkey = Base64.decode(key.getBytes());
      // 解密
      SignUtil.decrypt(srcFile, zipFile, bkey, ALG, "DesEde", null);
      // 解压
      SignUtil.uncompress(zipFile, desFile);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      if (ftpGet != null) {
        ftpGet.close(true);
      }
    }
  }

}
