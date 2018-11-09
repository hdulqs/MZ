/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月28日 下午7:10:41
 */
package com.mz.exchange.account.controller;

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
 * <p>
 * TODO
 * </p>
 *
 * @author: Wu Shuiming
 * @Date : 2016年3月28日 下午7:10:41
 */
@Controller
@RequestMapping("/account/exdmhotaccountrecord")
public class ExDmHotAccountRecordColler {

  @Resource
  private RemoteAppAccountRecordService remoteAppAccountRecordService;

  @RequestMapping("/list")
  @MethodName(name = "分页查询ExDmHotAccountRecord")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    HashMap<String, String> parameterMap = DataUtil.getParameterMap(request);
    return remoteAppAccountRecordService.findExDmHotAccountRecordList(parameterMap);
  }

}
