package com.mz.coin.tea;

/**
 * Created by Frank on 2018/9/1.
 */
public enum Method {
  GETNEWADDRESS("getnewaddress"), GETADDRESSBALANCES("getaddressbalances"),
  SENDASSETFROM("sendassetfrom"), GETTOTALBALANCES("gettotalbalances"),
  LISTADDRESSTRANSACTIONS("listaddresstransactions"), GETADDRESSES("getaddresses"),
  LISTWALLETTRANSACTIONS("listwallettransactions");

  private String value;

  Method(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
