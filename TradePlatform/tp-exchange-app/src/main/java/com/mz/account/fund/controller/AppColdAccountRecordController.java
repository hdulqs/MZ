/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年3月31日 下午6:55:57
 */
package com.mz.account.fund.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.remote.fund.RemoteAppAccountRecordService;
import com.mz.web.util.DataUtil;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2016年3月31日 下午6:55:57
 */
@Controller
@RequestMapping("/fund/appColdAccountRecord")
public class AppColdAccountRecordController {

  @Resource
  private RemoteAppAccountRecordService remoteAppAccountRecordService;

  @MethodName(name = "查询账户list")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    HashMap requestMap = DataUtil.getParameterMap(request);
    return remoteAppAccountRecordService.findAppColdAccountRecordList(requestMap);
  }

}
