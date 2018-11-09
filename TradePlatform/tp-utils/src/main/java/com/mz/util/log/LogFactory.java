/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月26日 上午10:32:03
 */
package com.mz.util.log;

import org.apache.log4j.Logger;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年5月26日 上午10:32:03 
 */
public class LogFactory {
	  private static final long serialVersionUID = 1L;  
	  private static Logger logger = Logger.getLogger(LogFactory.class);
	  
	  public static void info(Object message){
		  logger.info(message);
	  }
	  
	  public static void debug(Object message){
		  logger.debug(message);
	  }
	  
	  public static void error(Object message){
		  logger.error(message);
	  }
	  
	
}
