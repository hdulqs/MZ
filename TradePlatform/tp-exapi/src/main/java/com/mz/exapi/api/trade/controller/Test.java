package com.mz.exapi.api.trade.controller;

import com.mz.exapi.util.EncryptUtil;

import org.springframework.stereotype.Controller;

@Controller("testController")
public class Test {

  public static void main(String[] args) {
    String accesskey = "20b47d877c9b5305f317580eb6df1a7a";//私钥
    String publickey = "1" + "1" + "LTC_BTC" + "1";//公钥钥
    String sign = EncryptUtil.hmacSign("133", accesskey);
    //entrustPrice+type+coinCode+entrustCount
    System.out.println(sign);
  }

}
