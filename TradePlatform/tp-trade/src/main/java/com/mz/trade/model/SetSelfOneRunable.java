package com.mz.trade.model;

import com.mz.trade.entrust.model.ExOrderInfo;
import java.util.List;

public class SetSelfOneRunable implements Runnable {


  private List<ExOrderInfo> exorderlist;

  public SetSelfOneRunable(List<ExOrderInfo> exorderlist) {
    this.exorderlist = exorderlist;
  }

  @Override
  public void run() {
    RedisLastKLine.savePeriodLastKLineList(this.exorderlist);
  }

}
