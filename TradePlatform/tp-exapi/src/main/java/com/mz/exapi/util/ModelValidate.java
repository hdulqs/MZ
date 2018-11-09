/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2016年5月16日 上午9:58:31
 */
package com.mz.exapi.util;

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2016年5月16日 上午9:58:31 
 */

import java.util.Iterator;  
import java.util.Set;  
  
import javax.validation.ConstraintViolation;  
import javax.validation.Validation;  
import javax.validation.Validator;  
  
  
public class ModelValidate {
  
      
    public static String validateModel(Object obj) {//验证某一个对象  
          
        StringBuffer buffer = new StringBuffer(64);//用于存储验证后的错误信息  
          
        Validator validator = Validation.buildDefaultValidatorFactory()  
                .getValidator();  
  
        Set<ConstraintViolation<Object>> constraintViolations = validator  
                .validate(obj);//验证某个对象,，其实也可以只验证其中的某一个属性的  
  
        Iterator<ConstraintViolation<Object>> iter = constraintViolations  
                .iterator();  
        while (iter.hasNext()) {  
        	ConstraintViolation<Object> a=  iter.next();
        	System.out.println("错误信息:"+a.getMessage()); 
            String message = a.getPropertyPath()+":"+a.getMessage();  
           
            buffer.append(message+","); 
            
          /*  System.out.println("错误信息:"+message); 
            System.out.println("对象属性:"+iter.next().getPropertyPath()); */
            
        //    break;
           /* 
            System.out.println("对象属性:"+iter.next().getPropertyPath());    
           System.out.println("国际化key:"+iter.next().getMessageTemplate());    
           System.out.println("错误信息:"+iter.next().getMessage());*/    
        }  
        return buffer.toString();  
    }  
      
}  

