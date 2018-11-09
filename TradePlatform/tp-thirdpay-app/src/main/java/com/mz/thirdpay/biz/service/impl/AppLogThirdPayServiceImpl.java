/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月5日 下午5:52:54
 */
package com.mz.thirdpay.biz.service.impl;

import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.thirdpay.AppLogThirdPay;
import com.mz.thirdpay.biz.service.AppLogThirdPayService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p> TODO</p>
 * @author: Zhang Xiaofang
 * @Date :          2016年7月5日 下午5:52:54 
 */
@Service("appLogThirdPayService")
public class AppLogThirdPayServiceImpl extends BaseServiceImpl<AppLogThirdPay, Long> implements
    AppLogThirdPayService {

  @Resource(name = "appLogThirdPayDao")
  @Override
  public void setDao(BaseDao<AppLogThirdPay, Long> dao) {

    super.dao = dao;
  }

}
