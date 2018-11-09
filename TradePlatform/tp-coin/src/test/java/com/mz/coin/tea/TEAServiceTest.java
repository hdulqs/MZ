package com.mz.coin.tea;

import com.mz.coin.tea.service.TEAService;
import com.mz.core.mvc.model.page.JsonResult;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Frank on 2018/9/1.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring*.xml")
public class TEAServiceTest {

  @Autowired
  TEAService teaService;

  @Test
  public void getnewaddress() {
    String address = teaService.getnewaddress();
    System.out.println(address);
    Assert.assertNotNull(address);
  }

  @Test
  public void getaddressbalances() {
    BigDecimal banlace = teaService.getaddressbalances("1CVY71Q9CWaQNAuQct65TWmnUcyw1DfNwi3phk");
    System.out.println(banlace);
    Assert.assertNotNull(banlace);
  }

  @Test
  public void sendassetfrom() {
    JsonResult result = teaService.sendassetfrom("1S8CEjWiM1macpcYdtGVcz3PsssE4kRhwWcJ3S",
        "1AQoSzYxkSsDtdnyZJSxHawAs4tdWeujQ2s67o", BigDecimal.TEN);
    System.out.println(result);
    Assert.assertNotNull(result);
  }

  @Test
  public void listaddresstransactions() {
    List<Transactions> transactions = teaService
        .listaddresstransactions("1a6HPgCiscV4z4KWQrFHjJpLSF9zexjsCVFkbj", 1000000, 0);
    Assert.assertNotNull(transactions);
  }

  @Test
  public void gettotalbalances() {
    BigDecimal banlace = teaService.gettotalbalances();
    System.out.println(banlace);
    Assert.assertNotNull(banlace);
  }

  @Test
  public void getaddresses() {
    List<String> addresses = teaService.getaddresses();
    Assert.assertNotNull(addresses);
  }

  @Test
  public void refreshUserCoin() {
    JsonResult jsonResult = teaService
        .refreshUserCoin("1b2T9RUYC2groQxANAY5vydpmx4tpJCr6bggi8", 3, 3);
    Assert.assertNotNull(jsonResult);
  }

  @Test
  public void coinSync() {
    teaService.coinSync();
  }

  @Test
  public void teaCollection() {
    teaService.collection();
  }
}