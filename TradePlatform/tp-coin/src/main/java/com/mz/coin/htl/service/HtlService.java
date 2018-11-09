package com.mz.coin.htl.service;


import com.mz.coin.htl.HtlEntity;
import com.mz.core.mvc.model.page.JsonResult;
import java.util.List;
import java.util.Map;

public interface HtlService {

  /**
   * 验证地址是否是正确的 HTL 地址
   */
  boolean validateAddress(String validateaddress);

  /**
   * 根据账户地址，查询账户资产信息
   */
  Map getaccountstate(String address);

  /**
   * 根据指定的索引，返回对应的区块信息
   *
   * @param index 区块索引（区块高度） = 区块数 - 1。
   */
  List<HtlEntity> getblock(String index);


  /**
   * 获取主链中区块的数量
   */
  int getblockcount();


  /**
   * 向指定地址转账
   *
   * @param asset_id 资产id
   * @param to 收款地址
   * @param value 转账金额
   * @param fee 手续费，可选参数，默认为 0
   */
  Map sendfrom(String asset_id, String to, String value, String fee);

  JsonResult sendFrom(String amount, String toAddress, String memo);

  JsonResult htlrecordTransaction();
}
