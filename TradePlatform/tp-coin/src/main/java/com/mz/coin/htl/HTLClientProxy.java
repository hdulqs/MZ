package com.mz.coin.htl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.mz.util.log.LogFactory;
import com.mz.utils.Properties;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Frank on 2018/9/3.
 */
public class HTLClientProxy {

  private static final Logger logger = LoggerFactory.getLogger(HTLClientProxy.class);

  private static final String coinType = "HTL";
  private static final Map<String, String> propertiesMap = Properties.appcoinMap();
  private static final String url =
      propertiesMap.get(coinType.toLowerCase() + "_protocol") + "://" +
          propertiesMap.get(coinType.toLowerCase() + "_ip") + ":" +
          propertiesMap.get(coinType.toLowerCase() + "_port");
  private static final String jspnrpc = propertiesMap.get(coinType.toLowerCase() + "_rpc_jspnrpc");

  public static String invoke(String method, List paramsObject) {
    Params params = new Params();
    params.setJspnrpc(jspnrpc);
    String uuid = UUID.randomUUID().toString();
    params.setId(uuid);
    params.setMethod(method);
    params.setParams(paramsObject);
    String paramsJson = JSON.toJSONString(params);
    LogFactory.info("HTL请求" + method + "(" + paramsJson + ")");
    String response = HttpRequest.post(url).timeout(5000).
        body(paramsJson).execute().body();
    LogFactory.info("HTl返回" + response);
    return response;
  }
}
