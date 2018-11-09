/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年11月24日 下午7:04:39
 */
package com.mz.util.reflect;

import com.mz.util.StringUtil;
import com.mz.util.date.DateUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Table;

import org.nutz.lang.FailToGetValueException;
import org.nutz.lang.Mirror;
import org.springframework.util.StringUtils;

/**
 * <p>反射工具类</p>
 * @author:         Liu Shilei 
 * @Date :          2015年11月24日 下午7:04:39 
 */
public class ReflectUtil {
	
	/**
	 * 给BaseModel属性赋值
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param obj
	 * @return: void 
	 * @Date :          2015年11月24日 下午7:12:17   
	 * @throws:
	 */
	public static void setBaseModelPropertyValue(Object obj){

		Class<? extends Object> clazz = obj.getClass();
		String saasId = "saasId";
		try {
			
			Method method = clazz.getMethod("get" + StringUtil.upperFirst(saasId));
			if(method!=null){
				String value = (String) method.invoke(obj); // 调用getter方法获取属性值
				if (value == null) {
					method = clazz.getMethod("set" + StringUtil.upperFirst(saasId), String.class);
					method.invoke(obj, ContextUtil.getSaasId());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	/**
	 * BaseModel保存和更新时填充的属性
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param t
	 * @return: void 
	 * @Date :          2016年4月13日 上午10:14:53   
	 * @throws:
	 */
	public static void save(Object t){
		Mirror<?> mirror = Mirror.me(t.getClass());
		
		try {
			//如果创建时间存在就不能再次修改
			Object modified = mirror.getValue(t, "modified");
			if(StringUtils.isEmpty(modified)){
				mirror.setValue(t, "created", DateUtil.getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			mirror.setValue(t, "modified", DateUtil.getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} catch (FailToGetValueException e) {
			System.out.println("not find modified column");
		}
		
		try {
			//如果saasId存在不用来获取saasId
			String saasId = (String)mirror.getValue(t, "saasId");
			if(StringUtils.isEmpty(saasId)){
					String systemsaasId = ContextUtil.getSaasId();
					mirror.setValue(t, "saasId", systemsaasId);
			}
		} catch (Exception e) {
			System.out.println("not find saasId column");
		}
	}
	
	
	/**
	 * 获得model对象的@Table注解的name属性
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param t
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年4月13日 上午10:17:39   
	 * @throws:
	 */
	public static String getTableName(Object t){
		Mirror<?> mirror = Mirror.me(t.getClass());
		Table table = mirror.getAnnotation(Table.class);
		if(table!=null){
			return table.name();
		}else{
			return null;
		}
	}
	
	/**
	 * 获得class对象的@Table注解的name属性
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param t
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年4月13日 上午10:17:39   
	 * @throws:
	 */
	public static String getTableName(Class t){
		Mirror<?> mirror = Mirror.me(t);
		Table table = mirror.getAnnotation(Table.class);
		if(table!=null){
			return table.name();
		}else{
			return null;
		}
	}
	
	
	public static Class getClass(Object t){
		Mirror<?> mirror = Mirror.me(t.getClass());
		return mirror.getClass();
	}
}
