/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * 与spring mvc结合，像前台返回json格式数据
 * 
 * 在controller类上面写@ResponseBody注解就可以
 * 
 * 
 * 
 * @author Yuan Zhicheng
 *
 */
public class HibernateAwareObjectMapper extends ObjectMapper {
	public HibernateAwareObjectMapper() {
		registerModule(new Hibernate4ModuleNoIntercept());
	}
}
