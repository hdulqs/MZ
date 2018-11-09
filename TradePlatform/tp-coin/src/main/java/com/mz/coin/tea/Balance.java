package com.mz.coin.tea;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Frank on 2018/9/5.
 */
public class Balance {

  private BigDecimal amount;
  private List<Assets> assets;

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public List<Assets> getAssets() {
    return assets;
  }

  public void setAssets(List<Assets> assets) {
    this.assets = assets;
  }
}
