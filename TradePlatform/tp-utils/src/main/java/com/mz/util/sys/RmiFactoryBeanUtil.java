/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年12月2日 下午4:08:33
 */
package com.mz.util.sys;

import com.mz.util.properties.PropertiesUtils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2015年12月2日 下午4:08:33 
 */
public class RmiFactoryBeanUtil {
   public static Object getServiceProxy(Class<?> serviceInterface,String serviceName){
		   String rmiip="localhost:50505";
		   String ip_web=PropertiesUtils.RMI.getProperty("ip_web");
		   String ip_oauth=PropertiesUtils.RMI.getProperty("ip_oauth");
		   String ip_factoring=PropertiesUtils.RMI.getProperty("ip_factoring");
		   if(serviceInterface.getPackage().getName().contains(".web.")){
			   rmiip=ip_web;
		   }else if(serviceInterface.getPackage().getName().contains(".oauth.")){
			   rmiip=ip_oauth;
		   }else if(serviceInterface.getPackage().getName().contains(".factoring.")){
			   rmiip=ip_factoring;
		   }
		   RmiProxyFactoryBean rmiProxyFactoryBean =new RmiProxyFactoryBean();
		   rmiProxyFactoryBean.setServiceInterface(serviceInterface);
		   rmiProxyFactoryBean.setServiceUrl("rmi://"+rmiip+"/"+serviceName);
		   System.out.println("rmiProxyFactoryBean=="+rmiProxyFactoryBean.getServiceUrl());
		   rmiProxyFactoryBean.afterPropertiesSet();
		   rmiProxyFactoryBean.isSingleton();
		   
		   return rmiProxyFactoryBean.getObject();
   }
	   
	   
   
}
