package com.mz.coin.htl;

import com.mz.coin.htl.service.HtlService;
import com.mz.coin.htl.service.HtlServiceImpl;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Frank on 2018/10/11.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring*.xml")
public class HTLServiceTest {

  public void testGetPublicKey() {
    HtlService htlService = new HtlServiceImpl();
    String address = ((HtlServiceImpl) htlService).getPublicKey("13925708874");
    System.out.println(address);
  }

  public void testGetBlock() {
    HtlService htlService = new HtlServiceImpl();
    htlService.getblock("1");
  }

  @Test
  public void testHtlConsumeTx() {
    HtlService htlService = new HtlServiceImpl();
    htlService.htlrecordTransaction();
  }

  public static void testGetWalletInfo() {
    new HtlServiceImpl().getWalletInfo();
  }

  public void testGetBalance() {
    HtlService htlService = new HtlServiceImpl();
    BigDecimal balance = ((HtlServiceImpl) htlService)
        .getBalance("AUuLNRs52SZUPpjGUJteJkG93Y47LAHzr1");
    System.out.println(balance);
  }

  public static void testSendFrom() {
    HtlService htlService = new HtlServiceImpl();
    htlService.sendFrom("10", "ARuMMrqQbRbkrHZRE4h5MSJ9oQb6WKqMq4", null);
  }

  public static void main(String[] args) {
    testSendFrom();
  }
}
