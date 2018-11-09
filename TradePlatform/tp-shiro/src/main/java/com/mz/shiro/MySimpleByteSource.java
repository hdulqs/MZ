package com.mz.shiro;

import java.io.Serializable;
import org.apache.shiro.util.SimpleByteSource;

/**
 * Created by Frank on 2018/9/20.
 */
public class MySimpleByteSource extends SimpleByteSource implements Serializable {

  private static final long serialVersionUID = 1L;

  public MySimpleByteSource(byte[] bytes) {
    super(bytes);
  }
}
