package com.mz.coin.tea;

import java.math.BigDecimal;

/**
 * Created by Frank on 2018/9/4.
 */
public class Assets {

  private BigDecimal qty;
  private String assetref;
  private String name;

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getAssetref() {
    return assetref;
  }

  public void setAssetref(String assetref) {
    this.assetref = assetref;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
