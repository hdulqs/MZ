package com.mz.exchange.kline2.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketPayload implements Serializable {

  private String symbolId = "btccny";

  //最新成交价
  private BigDecimal priceNew = BigDecimal.ZERO;
  //开盘价
  private BigDecimal priceOpen = BigDecimal.ZERO;
  //最高价
  private BigDecimal priceHigh = BigDecimal.ZERO;
  //最低价
  private BigDecimal priceLow = BigDecimal.ZERO;
  //收盘价
  private BigDecimal priceLast = BigDecimal.ZERO;
  //与最新成交价相等
  private BigDecimal level = BigDecimal.ZERO;
  //成交量
  private BigDecimal amount = BigDecimal.ZERO;
  //总成交量
  private BigDecimal totalAmount = BigDecimal.ZERO;
  //待定。。。
  private Object amp = null;


  private BigDecimal yestdayPriceLast = BigDecimal.ONE;

  private Integer commissionRatio = 0;
  private Integer poor = 0;
  private Integer updownVolume = 0;
  private Integer updownRatio = 0;
  private Integer priceAverage = 0;
  private Integer volumeRatio = 0;
  private Integer turnVolume = 0;
  private Integer turnoverRate = 0;
  private Integer outerDisc = 0;
  private Integer innerDisc = 0;

  //总成交量
  private BigDecimal totalVolume;

  //成交数据
  private MarketPayloadTrades trades = new MarketPayloadTrades();
  //委托买
  private MarketPayloadBids bids = new MarketPayloadBids();
  //委托卖
  private MarketPayloadAsks asks = new MarketPayloadAsks();

  //usdt对人民币汇率
  private BigDecimal usdtToRmb = new BigDecimal(6.6);
  //usdt 数量
  private BigDecimal usdtCount = new BigDecimal(0);

}
