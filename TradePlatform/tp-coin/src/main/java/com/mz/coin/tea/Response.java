package com.mz.coin.tea;

/**
 * Created by Frank on 2018/9/5.
 */
public class Response {

  private Object result;
  private String error;
  private String id;

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
