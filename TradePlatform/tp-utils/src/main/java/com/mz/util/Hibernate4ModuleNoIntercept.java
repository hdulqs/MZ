/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年12月10日 下午3:19:15
 */
package com.mz.util;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.hibernate4.HibernateAnnotationIntrospector;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年12月10日 下午3:19:15 
 */
public class Hibernate4ModuleNoIntercept extends Hibernate4Module{
	
	@Override
    protected AnnotationIntrospector annotationIntrospector() {
        HibernateAnnotationIntrospector ai = new HibernateAnnotationIntrospector();
        ai.setUseTransient(false);
        return ai;
    }
	
}
