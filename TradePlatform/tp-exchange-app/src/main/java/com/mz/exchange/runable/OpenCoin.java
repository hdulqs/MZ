package com.mz.exchange.runable;

import com.alibaba.fastjson.JSONObject;
import com.mz.exchange.account.model.ExDigitalmoneyAccount;
import com.mz.exchange.account.service.ExDigitalmoneyAccountService;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.exchange.purse.CoinInterfaceUtil;
import org.springframework.util.StringUtils;

public class OpenCoin implements Runnable {

  private String website;
  private String coinCode;
  private String userName;
  private Long exdmaccountId;

  public OpenCoin(String website, String coinCode, String userName, Long exdmaccountId) {
    this.website = website;
    this.coinCode = coinCode;
    this.userName = userName;
    this.exdmaccountId = exdmaccountId;
  }

  @Override
  public void run() {
    try {
      String ss = "";
      LogFactory.info("站点----" + website);
      if (ContextUtil.EN.equals(website)) {// 如果是国际站进来的,用户名拼接"-USD"
        ss = CoinInterfaceUtil.create(userName + "-USD", null, coinCode);
      } else {
        ss = CoinInterfaceUtil.create(userName, null, coinCode);
      }
      JSONObject parseObject = null;
      if (!StringUtils.isEmpty(ss)) {
        parseObject = JSONObject.parseObject(ss);
      }
      String address = "";
      if (parseObject != null) {
        address = parseObject.get("address").toString();
      }

      if (!address.equals("")) {
        ExDigitalmoneyAccountService exDigitalmoneyAccountService = (ExDigitalmoneyAccountService) ContextUtil
            .getBean("exDigitalmoneyAccountService");
        ExDigitalmoneyAccount exDigitalmoneyAccount = exDigitalmoneyAccountService
            .get(exdmaccountId);
        exDigitalmoneyAccount.setPublicKey(address);
        exDigitalmoneyAccountService.update(exDigitalmoneyAccount);
        LogFactory.info("手机号为：" + exDigitalmoneyAccount.getUserName() + ",币地址：" + address);
      } else {
        LogFactory.info("开通" + coinCode + "钱包出错");
      }
    } catch (Exception e) {
      LogFactory.error("远程调用开通钱包失败");
      e.printStackTrace();
    }
  }
}
