/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月19日 下午6:29:33
 */
package com.mz.exapi.check;

import java.lang.reflect.Field;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月19日 下午6:29:33 
 */
class Fieldcomparator implements java.util.Comparator{
	 public int compare(Object o1,Object o2){
		 Field a1=(Field)o1;
		 Field a2=(Field)o2;
	  if(a1.getName().compareTo(a2.getName())>0){
	    return 1;
	  } else if(a1.getName().compareTo(a2.getName())==0){
	    return 0;
	  }else
	   return -1;
	 }
}