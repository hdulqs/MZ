package com.mz.amqp.common;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Frank on 2018/11/5.
 */
@Getter @Setter
public class Message {

  private String key;
  private BigDecimal tradeMoney;
  private String tradeType;

}
