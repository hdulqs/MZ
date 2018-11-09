/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Zhang Xiaofang
 * @version:      V1.0 
 * @Date:        2016年7月6日 下午3:06:26
 */
package com.mz;

import com.mz.utils.CommonRequest;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;


/**
 * 第三方支付接口(对接的每家第三方 都要引入这个jar包并实现这个接口)
 * 如果有新增的接口要在这里新增
 * <p> TODO</p>
 * @author:         Zhang Xiaofang 
 * @Date :          2016年7月6日 下午3:06:26 
 */
public interface ThirdPayInterfaceService {
	
      /**
       * 
       * <p>充值</p>
       * @author:         Zhang Xiaofang
       * @param <HttpServletResponse>
       * @param:    @return
       * @return: String 
       * @Date :          2016年7月6日 下午3:07:06   
       * @throws:
       */
	 
	  public CommonRequest recharge(HttpServletResponse response,CommonRequest  request);
	  
	  
	  /**
       * 
       * <p>提现</p>
       * @author:         Zhang Xiaofang
     * @param <HttpServletResponse>
       * @param:    @return
       * @return: String 
       * @Date :          2016年7月6日 下午3:07:06   
       * @throws:
       */
	  public  CommonRequest  withdraw(CommonRequest  request);
	  
	  
	  
	  /**
       * 
       * <p>查询订单信息</p>
       * @author:         Zhang Xiaofang
     * @param <HttpServletResponse>
       * @param:    @return
       * @return: String 
       * @Date :          2016年7月6日 下午3:07:06   
       * @throws:
       */
	  public  CommonRequest queryOrder(CommonRequest  request);
	  
	  
	  /**
       * 
       * <p>充值回调方法</p>
       * @author:         Zhang Xiaofang
       * @param 
       * @param:    @return
       * @return: String 
       * @Date :          2016年7月6日 下午3:07:06   
       * @throws:
       */
	 
	  public  CommonRequest  rechargeCallBack(Map<String, Object> map);
	  
	  
	  /**
       * 
       * <p>提现回调方法</p>
       * @author:         Zhang Xiaofang
     * @param 
       * @param:    @return
       * @return: String 
       * @Date :          2016年7月6日 下午3:07:06   
       * @throws:
       */
	  public  CommonRequest  withdrawCallBack(Map<String, Object> map);
	  
	  
	  
	  
	  /**
	   * 查询身份认证信息
	   * <p> TODO</p>
	   * @author:         Zhang Xiaofang
	   * @param:    @param request
	   * @param:    @return
	   * @return: CommonRequest 
	   * @Date :          2016年9月21日 上午10:16:29   
	   * @throws:
	   */
	  public  CommonRequest  checkIdentity(CommonRequest  request);
	  
	 
	  
	  
	  
	  
}
