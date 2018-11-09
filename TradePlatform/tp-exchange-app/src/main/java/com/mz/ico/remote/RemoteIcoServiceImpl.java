package com.mz.ico.remote;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.mz.account.fund.model.AppOurAccount;
import com.mz.account.remote.RemoteAppOurAccountService;
import com.mz.core.mvc.model.page.PageFactory;
import com.mz.customer.person.model.AppPersonInfo;
import com.mz.ico.coin.model.AppIcoCoin;
import com.mz.ico.coin.model.AppIcoPay;
import com.mz.ico.coinAccount.model.AppIcoCoinAccount;
import com.mz.ico.coinAccountColdRecord.model.AppIcoCoinAccountColdRecord;
import com.mz.ico.coinAccountHotRecord.model.AppIcoCoinAccountHotRecord;
import com.mz.ico.coinTransaction.model.AppIcoCoinTransaction;
import com.mz.ico.project.model.AppIcoEvaluate;
import com.mz.ico.project.model.AppIcoProject;
import com.mz.ico.project.model.AppIcoProjectHomePage;
import com.mz.ico.project.model.AppIcoProjectRepay;
import com.mz.ico.project.model.AppIcoProjectShare;
import com.mz.ico.project.model.AppIcoProjectSuport;
import com.mz.ico.project.service.AppIcoProjectRepayService;
import com.mz.ico.project.service.AppIcoProjectSuportService;
import com.mz.shiro.PasswordHelper;
import com.mz.util.BeanUtil;
import com.mz.util.QueryFilter;
import com.mz.util.serialize.ObjectUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.customer.person.service.AppPersonInfoService;
import com.mz.ico.coin.service.AppIcoCoinService;
import com.mz.ico.coin.service.AppIcoPayService;
import com.mz.ico.coinAccount.service.AppIcoCoinAccountService;
import com.mz.ico.coinAccountColdRecord.service.AppIcoCoinAccountColdRecordService;
import com.mz.ico.coinAccountHotRecord.service.AppIcoCoinAccountHotRecordService;
import com.mz.ico.coinTransaction.service.AppIcoCoinTransactionService;
import com.mz.ico.project.service.AppIcoEvaluateService;
import com.mz.ico.project.service.AppIcoProjectHomePageService;
import com.mz.ico.project.service.AppIcoProjectService;
import com.mz.ico.project.service.AppIcoProjectShareService;
import com.mz.manage.remote.ico.RemoteIcoService;
import com.mz.manage.remote.ico.model.AppIcoCoinAccountDTO;
import com.mz.manage.remote.ico.model.AppIcoCoinTransactionDTO;
import com.mz.manage.remote.ico.model.AppIcoEvaluateDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectHomePageDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectRepayDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectSuportDTO;
import com.mz.manage.remote.ico.model.AppPersonInfoDTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.json.Json;

/**
 * <p> TODO</p>
 *
 * @author: Shangxl
 * @Date :          2017年7月14日 下午3:37:00
 */
public class RemoteIcoServiceImpl implements RemoteIcoService {

  @Resource
  private AppIcoProjectService appIcoProjectService;
  @Resource
  private AppPersonInfoService appPersonInfoService;
  @Resource
  private AppIcoProjectHomePageService appIcoProjectHomePageService;
  @Resource
  private AppIcoProjectRepayService appIcoProjectRepayService;
  @Resource
  private AppIcoProjectSuportService appIcoProjectSuportService;
  @Resource
  private AppIcoProjectShareService appIcoProjectShareService;
  @Resource
  private AppIcoPayService appIcoPayService;
  @Resource
  private AppIcoCoinAccountHotRecordService appIcoCoinAccountHotRecordService;

  @Resource
  private AppIcoEvaluateService appIcoEvaluateService;
  @Resource
  private AppIcoCoinTransactionService appIcoCoinTransactionService;
  @Resource
  private AppIcoCoinAccountService appIcoCoinAccountService;
  @Resource
  private AppIcoCoinService appIcoCoinService;
  @Resource
  private AppIcoCoinAccountColdRecordService appIcoCoinAccountColdRecordService;
  @Resource
  private RemoteAppOurAccountService remoteAppOurAccountService;

  @Override
  public FrontPage listProject(Map<String, String> map) {
    Page page = PageFactory.getPage(map);
    QueryFilter filter = new QueryFilter(AppIcoProject.class);
    filter.addFilter("status!=", Integer.valueOf(7));//排除删除
    if (StringUtils.isNotEmpty(map.get("status"))) {
      filter.addFilter("status=", map.get("status"));
    }
    List<AppIcoProject> list = appIcoProjectService.find(filter);
    String liststr = Json.toJson(list);
    List<AppIcoProjectDTO> result = JSON.parseArray(liststr, AppIcoProjectDTO.class);
    FrontPage frontPage = new FrontPage(result, page.getTotal(), page.getPages(),
        page.getPageSize());
    return frontPage;
  }

  @Override
  public FrontPage listIsuportProject(Map<String, String> map) {
    //第一步：获取支持的项目Id
    QueryFilter filter = new QueryFilter(AppIcoProjectSuport.class);
    String customerId = "customerId=";
    if (StringUtils.isNotEmpty(map.get(customerId))) {
      filter.addFilter(customerId, map.get(customerId));
    }
    List<AppIcoProjectSuport> suports = appIcoProjectSuportService.find(filter);
    StringBuffer projectId_in = new StringBuffer();
    for (AppIcoProjectSuport l : suports) {
      projectId_in.append(l.getProjectId()).append(",");
    }
    if (projectId_in.length() > 0) {
      projectId_in.deleteCharAt(projectId_in.length() - 1);
    }
    //第二步：查询支持的项目
    Page page = PageFactory.getPage(map);
    QueryFilter filter2 = new QueryFilter(AppIcoProject.class);
    filter2.addFilter("status!=", Integer.valueOf(7));//排除删除
    if (StringUtils.isNotEmpty(projectId_in)) {
      filter2.addFilter("id_in", projectId_in);
    }
    List<AppIcoProject> list = appIcoProjectService.find(filter2);
    String liststr = Json.toJson(list);
    List<AppIcoProjectDTO> result = JSON.parseArray(liststr, AppIcoProjectDTO.class);
    FrontPage frontPage = new FrontPage(result, page.getTotal(), page.getPages(),
        page.getPageSize());
    return frontPage;
  }


  @Override
  public FrontPage listIshareProject(Map<String, String> map) {
    //第一步：查询出我的分享记录
    QueryFilter filter = new QueryFilter(AppIcoProjectShare.class);
    String customerId = "customerId=";
    if (StringUtils.isNotEmpty(map.get(customerId))) {
      filter.addFilter(customerId, map.get(customerId));
    }
    List<AppIcoProjectShare> suports = appIcoProjectShareService.find(filter);
    StringBuffer projectId_in = new StringBuffer();
    for (AppIcoProjectShare l : suports) {
      projectId_in.append(l.getProjectId()).append(",");
    }
    if (projectId_in.length() > 0) {
      projectId_in.deleteCharAt(projectId_in.length() - 1);
    }

    //第二步：根据分享记录查询出项目
    Page page = PageFactory.getPage(map);
    QueryFilter filter2 = new QueryFilter(AppIcoProject.class);
    filter2.addFilter("status!=", Integer.valueOf(7));//排除删除
    if (StringUtils.isNotEmpty(projectId_in)) {
      filter2.addFilter("id_in", projectId_in);
    }
    List<AppIcoProject> list = appIcoProjectService.find(filter2);
    String liststr = Json.toJson(list);
    List<AppIcoProjectDTO> result = JSON.parseArray(liststr, AppIcoProjectDTO.class);
    FrontPage frontPage = new FrontPage(result, page.getTotal(), page.getPages(),
        page.getPageSize());
    return frontPage;
  }


  @Override
  public List<AppPersonInfoDTO> getPersonInfo(Map<String, String> map) {
    List<AppPersonInfoDTO> result = new ArrayList<AppPersonInfoDTO>();
    if (map != null && map.size() > 0) {
      QueryFilter filter = new QueryFilter(AppPersonInfo.class);
      for (Map.Entry<String, String> entry : map.entrySet()) {
        filter.addFilter(entry.getKey(), entry.getValue());
      }
      List<AppPersonInfo> list = appPersonInfoService.find(filter);
      String liststr = JSON.toJSONString(list);
      result = JSON.parseArray(liststr, AppPersonInfoDTO.class);
    }
    return result;
  }

  @Override
  public AppIcoProjectDTO saveProjectStep(AppIcoProjectDTO projectDTO) {
    AppIcoProject project = JSON.parseObject(JSON.toJSONString(projectDTO), AppIcoProject.class);
    if (project.getId() == null || project.getId() == -1) {
      project.setId(null);
      appIcoProjectService.save(project);
    } else {
      appIcoProjectService.update(project);
    }
    projectDTO = (AppIcoProjectDTO) BeanUtil.convert(projectDTO, project);
    return projectDTO;
  }

  @Override
  public AppIcoProjectDTO getProject(Map<String, Object> map) {
    if (map != null && map.size() > 0) {
      QueryFilter filter = new QueryFilter(AppIcoProject.class);
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        filter.addFilter(entry.getKey(), entry.getValue());
      }
      AppIcoProject project = appIcoProjectService.get(filter);
      if (project != null) {
        AppIcoProjectDTO obj = JSON.parseObject(JSON.toJSONString(project), AppIcoProjectDTO.class);
        return obj;
      }
    }
    return null;
  }

  @Override
  public boolean saveProjectHomePage(AppIcoProjectHomePageDTO homePageDTO) {
    AppIcoProjectHomePage appIcoProjectHomePage = JSON
        .parseObject(JSON.toJSONString(homePageDTO), AppIcoProjectHomePage.class);
    if (homePageDTO.getId() != null) {
      appIcoProjectHomePageService.update(appIcoProjectHomePage);
    } else {
      appIcoProjectHomePageService.save(appIcoProjectHomePage);
    }
    return true;
  }

  @Override
  public AppIcoProjectHomePageDTO getProjectHomePageDTO(Map<String, Object> map) {
    AppIcoProjectHomePageDTO homePageDTO = new AppIcoProjectHomePageDTO();
    if (map != null && map.size() > 0) {
      QueryFilter filter = new QueryFilter(AppIcoProjectHomePage.class);
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        filter.addFilter(entry.getKey(), entry.getValue());
      }
      AppIcoProjectHomePage homePage = appIcoProjectHomePageService.get(filter);
      homePageDTO = JSON.parseObject(JSON.toJSONString(homePage), AppIcoProjectHomePageDTO.class);
    }
    return homePageDTO;
  }

  @Override
  public boolean saveProjectRepay(AppIcoProjectRepayDTO projectRepayDTO) {
    AppIcoProjectRepay appIcoProjectRepay = JSON
        .parseObject(JSON.toJSONString(projectRepayDTO), AppIcoProjectRepay.class);
    //业务暂时设置每一个项目对应一个回报
    QueryFilter filter = new QueryFilter(AppIcoProjectRepay.class);
    filter.addFilter("projectId=", projectRepayDTO.getProjectId());
    AppIcoProjectRepay old = appIcoProjectRepayService.get(filter);
    if (old != null) {
      appIcoProjectRepay = (AppIcoProjectRepay) BeanUtil.convert(old, appIcoProjectRepay);
      appIcoProjectRepayService.update(appIcoProjectRepay);
    } else {
      appIcoProjectRepayService.save(appIcoProjectRepay);
    }
    return true;
  }

  @Override
  public List<AppIcoProjectRepayDTO> listProjectRepayDTO(Map<String, String> map) {
    List<AppIcoProjectRepayDTO> icoProjectRepayDTOs = new ArrayList<AppIcoProjectRepayDTO>();
    if (map != null && map.size() > 0) {
      QueryFilter filter = new QueryFilter(AppIcoProjectRepay.class);
      for (Map.Entry<String, String> entry : map.entrySet()) {
        filter.addFilter(entry.getKey(), entry.getValue());
      }
      List<AppIcoProjectRepay> list = appIcoProjectRepayService.find(filter);
      icoProjectRepayDTOs = JSON.parseArray(JSON.toJSONString(list), AppIcoProjectRepayDTO.class);
    }
    return icoProjectRepayDTOs;
  }

  @Override
  public boolean remoteProject(Map<String, Object> map) {
    QueryFilter filter = new QueryFilter(AppIcoProject.class);
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      filter.addFilter(entry.getKey(), entry.getValue());
    }
    AppIcoProject old = appIcoProjectService.get(filter);
    if (old != null) {
      old.setStatus(Integer.valueOf(7));
      appIcoProjectService.update(old);
      return true;
    }
    return false;
  }

  /**
   * <p> TODO</p>
   *
   * @author: Shangxl
   * @param: @param map
   * @param: @return
   * @return: List<AppIcoProjectDTO>
   * @Date :          2017年7月26日 下午5:01:57
   * @throws:
   */
  @Override
  public List<AppIcoProjectDTO> browseProject(Map<String, Object> map) {
    QueryFilter filter = new QueryFilter(AppIcoProject.class);
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      filter.addFilter(entry.getKey(), entry.getValue());
    }
    List<AppIcoProject> list = appIcoProjectService.find(filter);
    if (list != null && list.size() > 0) {
      return JSON.parseArray(JSON.toJSONString(list), AppIcoProjectDTO.class);
    }
    return null;
  }

  @Override
  public List<AppIcoProjectSuportDTO> listProjectSuport(Map<String, Object> map) {
    QueryFilter filter = new QueryFilter(AppIcoProjectSuport.class);
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      filter.addFilter(entry.getKey(), entry.getValue());
    }
    List<AppIcoProjectSuport> list = appIcoProjectSuportService.find(filter);
    if (list != null && list.size() > 0) {
      return JSON.parseArray(JSON.toJSONString(list), AppIcoProjectSuportDTO.class);
    }
    return null;
  }

  public List<AppIcoEvaluateDTO> listEvaluate(Long project) {
    QueryFilter filter = new QueryFilter(AppIcoEvaluate.class);
    filter.addFilter("projectId=", project);
    List<AppIcoEvaluate> list = appIcoEvaluateService.find(filter);
    List<AppIcoEvaluateDTO> listEvaluate = JSONObject
        .parseArray(JSON.toJSONString(list), AppIcoEvaluateDTO.class);
    return listEvaluate;
  }

  @Override
  public AppIcoCoinTransactionDTO getTransaction(String userName, String txid) {
    QueryFilter filter = new QueryFilter(AppIcoCoinTransaction.class);
    AppIcoCoinTransactionDTO appIcoCoinTransactionDTO = null;
    filter.addFilter("orderNo=", txid);
    filter.addFilter("customerName=", userName);
    AppIcoCoinTransaction coinTransaction = appIcoCoinTransactionService.get(filter);
    if (coinTransaction != null) {
      appIcoCoinTransactionDTO = ObjectUtil
          .bean2bean(coinTransaction, AppIcoCoinTransactionDTO.class);
    }
    return appIcoCoinTransactionDTO;
  }

  public AppIcoCoinAccountDTO appIcoCoinAccountDTO(Long id) {
    AppIcoCoinAccount appIcoCoinAccount = appIcoCoinAccountService
        .get(new QueryFilter(AppIcoCoinAccount.class).addFilter("customerId=", id));
    return JSON.parseObject(JSON.toJSONString(appIcoCoinAccount), AppIcoCoinAccountDTO.class);
  }

  /**
   * 立即支付
   */
  public RemoteResult immediatePayment(User user, Long projectId, BigDecimal getMoney,
      String proportions) {
    AppIcoProject appIcoProject = appIcoProjectService.get(projectId);
    //保存订单
    AppIcoPay appIcoPay = new AppIcoPay();
    appIcoPay.setStep(3);
    appIcoPay.setCustomerId(user.getCustomerId());
    appIcoPay.setProjectId(projectId);
    appIcoPay.setProjectName(appIcoProject.getProjectName());
    appIcoPay.setCoinType(appIcoProject.getCoinType());
    appIcoPay.setGetMoney(getMoney);
    appIcoPay.setPayMoney(getMoney);
    appIcoPay.setPayType(appIcoProject.getCoinType() + "在线支付");
    appIcoPay.setProportion(proportions);
    appIcoPayService.save(appIcoPay);
    //更改账户可用金额
    AppIcoCoinAccount appIcoCoinAccount = appIcoCoinAccountService.get(
        new QueryFilter(AppIcoCoinAccount.class).addFilter("customerId=", user.getCustomerId()));

    appIcoCoinAccount.setHotMoney(appIcoCoinAccount.getHotMoney().subtract(getMoney));
    appIcoCoinAccountService.update(appIcoCoinAccount);
    //更改项目表
    BigDecimal addMoney = appIcoProject.getGetMoney().add(getMoney);
    if (addMoney.compareTo(appIcoProject.getSumMoney()) > 0) {
      return new RemoteResult().setSuccess(false).setMsg("总额超出目标金额,请重新填写!");
    } else {
      appIcoProject.setGetMoney(addMoney);
      appIcoProjectService.update(appIcoProject);
      return new RemoteResult().setSuccess(true).setObj(appIcoCoinAccount);
    }
  }

  @Override
  public String saveTransaction(AppIcoCoinTransactionDTO dto) {
    StringBuffer ret = new StringBuffer("{\"success\":\"true\",\"msg\":");
    try {
      AppIcoCoinTransaction icoCoinTransaction = ObjectUtil
          .bean2bean(dto, AppIcoCoinTransaction.class);
      appIcoCoinTransactionService.save(icoCoinTransaction);
      ret.append("\"保存成功 \" ");
    } catch (Exception e) {
      ret.append("\"异常\"：" + e.getMessage() + "\"");
    } finally {
      ret.append("}");
    }

    return ret.toString();
  }

  @Override
  public String rechargeCoin(AppIcoCoinTransactionDTO transaction) {
    StringBuffer ret = new StringBuffer("{\"success\":\"true\",\"msg\":");
    try {
      Integer i = transaction.getStatus();
      if (i == 2) {
        ret.append("\"该订单已通过\"");
      }

      AppIcoCoinAccount appIcoCoinAccount = appIcoCoinAccountService.getByCustomerIdAndType(
          transaction.getCustomerId(), transaction.getCoinCode(), "cny", "cn");

      BigDecimal hotMoney = appIcoCoinAccount.getHotMoney();
      BigDecimal transactionMoney = transaction.getTransactionMoney();
      BigDecimal k = hotMoney.add(transactionMoney);
      appIcoCoinAccount.setHotMoney(k);
      // 修改可用余额
      appIcoCoinAccountService.update(appIcoCoinAccount);

      // 保存可用余额流水
      AppIcoCoinAccountHotRecord hotRecord = new AppIcoCoinAccountHotRecord();
      hotRecord.setAccountId(appIcoCoinAccount.getId());
      hotRecord.setCustomerId(transaction.getCustomerId());
      hotRecord.setUserName(appIcoCoinAccount.getUserName());
      hotRecord.setRecordType(1);
      hotRecord.setTransactionMoney(transaction.getTransactionMoney());
      hotRecord.setStatus(5);
      hotRecord.setRemark("可用余额流水已记录成功 ");
      hotRecord.setCurrencyType(transaction.getCurrencyType());
      hotRecord.setCoinCode(transaction.getCoinCode());
      hotRecord.setWebsite(transaction.getWebsite());
      hotRecord.setTransactionNum(transaction.getTransactionNum());
      hotRecord.setSaasId(RpcContext.getContext().getAttachment("saasId"));
      appIcoCoinAccountHotRecordService.save(hotRecord);

      // 修改订单
      transaction.setStatus(2);
      transaction.setUserId(appIcoCoinAccount.getCustomerId());
      AppIcoCoinTransaction t = ObjectUtil.bean2bean(transaction, AppIcoCoinTransaction.class);
      appIcoCoinTransactionService.update(t);

      ret.append("\"记账成功\"");

    } catch (Exception e) {
      ret.append("\"异常 :" + e.getMessage() + "  \"");
    } finally {
      ret.append("}");
    }
    return ret.toString();
  }

  /**
   * 查询当前账户下的币账户
   */
  public List<AppIcoCoinAccountDTO> listAccount(Long customerId) {
    List<AppIcoCoinAccount> list = appIcoCoinAccountService
        .find(new QueryFilter(AppIcoCoinAccount.class).addFilter("customerId=", customerId));
    List<AppIcoCoinAccountDTO> listdto = ObjectUtil.beanList(list, AppIcoCoinAccountDTO.class);

    for (AppIcoCoinAccountDTO obj : listdto) {
      AppIcoCoin appIcoCoin = appIcoCoinService
          .get(new QueryFilter(AppIcoCoin.class).addFilter("coinCode=", obj.getCoinCode()));
      //提币手续费
      obj.setPaceFeeRate(appIcoCoin.getPaceFeeRate());
      //单次最小提币
      obj.setLeastPaceNum(
          appIcoCoin.getLeastPaceNum() == null ? new BigDecimal(0) : appIcoCoin.getLeastPaceNum());
      //一日最大提币
      obj.setOneDayPaceNum(appIcoCoin.getOneDayPaceNum() == null ? new BigDecimal(0)
          : appIcoCoin.getOneDayPaceNum());
    }
    return listdto;
  }

  /**
   * 查询充币记录
   */
  public FrontPage findIcotransaction(Map<String, String> params) {
    return appIcoCoinTransactionService.findIcotransaction(params);
  }

  public static String transactionNum(String bussType) {
    SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
    String time = format.format(new Date(System.currentTimeMillis()));
    String randomStr = RandomStringUtils.random(4, false, true);
    if (null == bussType) {
      bussType = "00";
    }
    return bussType + time + randomStr;
  }

  /**
   * 提币
   */
  public RemoteResult getIco(User user, String coinCode, BigDecimal money, String pwd,
      String btcKey) {
    PasswordHelper passwordHelper = new PasswordHelper();
    String encryString = passwordHelper.encryString(pwd, user.getSalt());
    if (!user.getAccountPassWord().equals(encryString)) {
      return new RemoteResult().setSuccess(false).setMsg("交易密码不正确");
    }

    QueryFilter qf = new QueryFilter(AppIcoCoinAccount.class);
    qf.addFilter("customerId=", user.getCustomerId());
    qf.addFilter("coinCode=", coinCode);
    List<AppIcoCoinAccount> list = appIcoCoinAccountService.find(qf);

    //查询ico 我方账户
    String currencyType = ContextUtil.getCurrencyType();
    AppOurAccount ourAccount = remoteAppOurAccountService
        .getOurAccount(currencyType, coinCode, "3");

    if (list != null && list.size() > 0) {
      AppIcoCoin appIcoCoin = appIcoCoinService
          .get(new QueryFilter(AppIcoCoin.class).addFilter("coinCode=", coinCode));
      //加一条资金明细
      AppIcoCoinTransaction appIcoCoinTransaction = new AppIcoCoinTransaction();
      appIcoCoinTransaction.setCustomerId(user.getCustomerId());
      appIcoCoinTransaction.setCustomerName(user.getUsername());
      appIcoCoinTransaction.setAccountId(list.get(0).getId());
      appIcoCoinTransaction.setTransactionNum(transactionNum("01"));
      appIcoCoinTransaction.setTransactionType(2);
      appIcoCoinTransaction.setTransactionMoney(money);
      appIcoCoinTransaction.setStatus(1);
      appIcoCoinTransaction.setCurrencyType("cny");
      appIcoCoinTransaction.setCoinCode(coinCode);
      appIcoCoinTransaction.setFee(money.multiply(appIcoCoin.getPaceFeeRate()));
      appIcoCoinTransaction.setTrueName(user.getTruename());
      appIcoCoinTransaction.setInAddress(btcKey);
      appIcoCoinTransaction.setOurAccountNumber(ourAccount.getAccountNumber());
      appIcoCoinTransactionService.save(appIcoCoinTransaction);
      //热钱包
      AppIcoCoinAccountHotRecord appIcoCoinAccountHotRecord = new AppIcoCoinAccountHotRecord();
      appIcoCoinAccountHotRecord.setAccountId(list.get(0).getId());
      appIcoCoinAccountHotRecord.setCustomerId(user.getCustomerId());
      appIcoCoinAccountHotRecord.setUserName(user.getUsername());
      appIcoCoinAccountHotRecord.setRecordType(2);
      appIcoCoinAccountHotRecord.setTransactionMoney(money);
      appIcoCoinAccountHotRecord.setTransactionNum(transactionNum("01"));
      appIcoCoinAccountHotRecord.setStatus(0);
      appIcoCoinAccountHotRecord.setCurrencyType(coinCode);
      appIcoCoinAccountHotRecord.setCoinCode(coinCode);
      appIcoCoinAccountHotRecord.setTrueName(user.getTruename());
      appIcoCoinAccountHotRecordService.save(appIcoCoinAccountHotRecord);
      //冷钱包
      AppIcoCoinAccountColdRecord appIcoCoinAccountColdRecord = new AppIcoCoinAccountColdRecord();
      appIcoCoinAccountColdRecord.setAccountId(list.get(0).getId());
      appIcoCoinAccountColdRecord.setCustomerId(user.getCustomerId());
      appIcoCoinAccountColdRecord.setUserName(user.getUsername());
      appIcoCoinAccountColdRecord.setRecordType(1);
      appIcoCoinAccountColdRecord.setTransactionMoney(money);
      appIcoCoinAccountColdRecord.setTransactionNum(transactionNum("01"));
      appIcoCoinAccountColdRecord.setStatus(0);
      appIcoCoinAccountColdRecord.setCurrencyType(coinCode);
      appIcoCoinAccountColdRecord.setCoinCode(coinCode);
      appIcoCoinAccountColdRecord.setTrueName(user.getTruename());
      appIcoCoinAccountColdRecordService.save(appIcoCoinAccountColdRecord);
      //币账户记录
      list.get(0).setHotMoney(list.get(0).getHotMoney().subtract(money));
      list.get(0).setColdMoney(list.get(0).getColdMoney().add(money));
      appIcoCoinAccountService.update(list.get(0));
      return new RemoteResult().setSuccess(true);
    }
    return new RemoteResult().setSuccess(false).setMsg("币账户不存在");
  }


  @Override
  public AppIcoCoinAccountDTO getcoinAccountByPublicKey(String publicKey) {
    QueryFilter queryFilter = new QueryFilter(AppIcoCoinAccount.class);
    queryFilter.addFilter("publicKey=", publicKey);
    return ObjectUtil
        .bean2bean(appIcoCoinAccountService.get(queryFilter), AppIcoCoinAccountDTO.class);
  }
}
