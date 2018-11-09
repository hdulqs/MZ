package com.mz.exchange.product;

import com.mz.exchange.product.model.ExProduct;
import com.mz.exchange.product.synced.SyncedUserForProduct;

/**
 * 发布产品
 *
 * @author CHINA_LSL
 */
public class PublishRunnable implements Runnable {

  private ExProduct exProduct;

  private PublishRunnable() {
  }

  public PublishRunnable(ExProduct exProduct) {
    this.exProduct = exProduct;
  }

  public void setExProduct(ExProduct exProduct) {
    this.exProduct = exProduct;
  }

  @Override
  public void run() {
    SyncedUserForProduct syncedUserForProduct = new SyncedUserForProduct();
    syncedUserForProduct.syncedUser(exProduct);
  }
}
