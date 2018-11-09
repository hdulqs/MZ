package com.mz.coin.tea;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.mz.Constant;
import com.mz.utils.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Frank on 2018/9/3.
 */
public class TEAClientProxy {

  private static final Logger logger = LoggerFactory.getLogger(TEAClientProxy.class);

  private static final String coinType = "TEA";
  private static final Map<String, String> propertiesMap = Properties.appcoinMap();
  private static final String url = propertiesMap.get(coinType.toLowerCase() + "_url");
  private static final String rpcUser = propertiesMap.get(coinType.toLowerCase() + "_rpc_user");
  private static final String rpcPwd = propertiesMap.get(coinType.toLowerCase() + "_rpc_password");
  private static final String jspnrpc = propertiesMap.get(coinType.toLowerCase() + "_rpc_jspnrpc");

  public String invoke(String method, List paramsObject) {
    Params params = new Params();
    params.setJspnrpc(jspnrpc);
    String uuid = UUID.randomUUID().toString();
    params.setId(uuid);
    params.setMethod(method);
    params.setParams(paramsObject);
    String paramsJson = JSON.toJSONString(params);
    logger.info("TEA请求" + method + "(" + paramsJson + ")");
    String response = HttpRequest.post(url).basicAuth(rpcUser, rpcPwd).timeout(5000).
        body(paramsJson).execute().body();
    logger.info("TEA返回" + response);
    return response;
  }
}
