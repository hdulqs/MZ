package com.mz.coin.tea;

import java.util.List;

/**
 * Created by Frank on 2018/9/4.
 */
public class Params {

  private String id;
  private String jspnrpc;
  private String method;
  private List<Object> params;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getJspnrpc() {
    return jspnrpc;
  }

  public void setJspnrpc(String jspnrpc) {
    this.jspnrpc = "1.0";
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public List<Object> getParams() {
    return params;
  }

  public void setParams(List<Object> params) {
    this.params = params;
  }
}
