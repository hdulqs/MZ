/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Wu Shuiming
 * @version: V1.0
 * @Date: 2016年3月28日 下午7:09:39
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
 * @Date : 2016年3月28日 下午7:09:39
 */
@Controller
@RequestMapping("/account/exdmcoldaccountrecord")
public class ExDmColdAccountRecordController {

  @Resource
  private RemoteAppAccountRecordService remoteAppAccountRecordService;

  @MethodName(name = "分页查询ExDmColdAccountRecord")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    HashMap requestMap = DataUtil.getParameterMap(request);
    return remoteAppAccountRecordService.findExDmColdAccountRecordList(requestMap);
  }

	/*@MethodName(name = "分页查询ExDmColdAccountRecord")
	@RequestMapping("/searchlist")
	@ResponseBody
	public List<ExDmColdAccountRecord> searchlist(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(ExDmColdAccountRecord.class,
				request);
		List<ExDmColdAccountRecord> list = super.find(filter);
		return list;
	}*/

}
