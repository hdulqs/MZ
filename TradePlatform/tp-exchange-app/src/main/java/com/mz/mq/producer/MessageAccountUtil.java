package com.mz.mq.producer;

import com.alibaba.fastjson.JSON;
import com.mz.mq.producer.service.MessageProducer;
import com.mz.trade.redis.model.Accountadd;
import com.mz.util.sys.SpringContextUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class MessageAccountUtil {

  private static final Logger log = Logger.getLogger(MessageAccountUtil.class);

  /**
   * 充币方法
   *
   * @param exdigaccountId 充币账户
   * @param count 充币数量
   * @param transactionNum 订单号
   */
  public static void addCoin(Long exdigaccountId, BigDecimal count, String transactionNum) {

    try {
      MessageProducer messageProducer = SpringContextUtil.getBean(MessageProducer.class);

      Accountadd accountadd = new Accountadd();
      accountadd.setAccountId(exdigaccountId);
      accountadd.setMoney(count);
      accountadd.setMonteyType(1);
      accountadd.setAcccountType(1);
      accountadd.setRemarks(31);
      accountadd.setTransactionNum(transactionNum);

      List<Accountadd> list = new ArrayList<Accountadd>();
      list.add(accountadd);
      messageProducer.toAccount(JSON.toJSONString(list));
    } catch (Exception e) {
      log.error("充币失败");
    }


  }

}
