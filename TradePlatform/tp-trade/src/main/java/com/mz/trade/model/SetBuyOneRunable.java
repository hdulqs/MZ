package com.mz.trade.model;

import com.mz.trade.entrust.model.ExOrderInfo;
import java.util.List;

public class SetBuyOneRunable implements Runnable {


  private List<ExOrderInfo> exorderlist;

  public SetBuyOneRunable(List<ExOrderInfo> exorderlist) {
    this.exorderlist = exorderlist;
  }

  @Override
  public void run() {
    RedisLastKLine.savePeriodLastKLineList(this.exorderlist);
  }

}
