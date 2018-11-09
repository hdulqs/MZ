package com.mz.inpay.utils;

import com.mz.utils.CommonRequest;
import com.mz.inpay.InpayInterfaceUtil;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * InpayUtil.java
 * @author denghf
 * 2017年8月22日 下午1:34:05
 */
public class InpayUtil {

	
	/**
	 * 拼接参数
	 * @param commonRequest
	 * @return
	 */
	public static String common(CommonRequest commonRequest){
		Map<String, String> params = new HashMap<String, String>();
		//拼接后得到的参数
        String str = "";
		Field[] field = commonRequest.getClass().getDeclaredFields();        
		for(int j=0 ; j<field.length ; j++){    
	        String nameMax = field[j].getName();   
	        String nameMin = "";
	        //大写
	        nameMax = nameMax.substring(0,1).toUpperCase()+nameMax.substring(1);
	        nameMin = nameMax.substring(0,1).toLowerCase()+nameMax.substring(1); 
	        String type = field[j].getGenericType().toString();  
	        
	        Method m;
            String values;
            BigDecimal valueb = new BigDecimal("0");
            
	        if(type.equals("class java.lang.String")){
				try {
					m = commonRequest.getClass().getMethod("get"+nameMax);
					values = (String) m.invoke(commonRequest);
	                if(values != null && !"".equals(values)){  
	                	str += nameMin +"=" + values + "&";
	                	params.put(nameMin, values);
	                }
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} 
	        }else if(type.equals("class java.math.BigDecimal")){
	        	try {
					m = commonRequest.getClass().getMethod("get"+nameMax);
					valueb = (BigDecimal) m.invoke(commonRequest);
	                if(valueb != null && !"".equals(valueb)){  
	                	str += nameMin +"=" + valueb.setScale(2) + "&";
	                	params.put(nameMin, valueb.toString());
	                } 
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} 
	        }
		}
		String strs = getSignatureContent(params);
		return strs;
	}
	
	/**
	 * 应第三方要求 得排序 ABCD...XYZabcd...xyz
	 * @param properties
	 * @return
	 */
	public static String getSignatureContent(Map<String, String> map) {
		StringBuffer content = new StringBuffer();
		List keys = new ArrayList(map.keySet());
		Collections.sort(keys);
		
		for(int i = 0; i < keys.size(); i++){
			String key = (String) keys.get(i);
			String value = map.get(key);
			content.append(key + "=" + value + "&");
		}
		System.out.println(content.toString().substring(0, content.toString().length()-1));
		return content.toString().substring(0, content.toString().length()-1);
	 }
	
	/**
	 * 生成流水号
	 * @return
	 */
	public static String num(){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
		String time = format.format(new Date(System.currentTimeMillis()));
		String randomStr = RandomStringUtils.random(4, false, true);
		return time;
	}
	
	/**
	 * 读取pem里面的内容  私钥公钥
	 * @param file
	 * @return
	 */
    public static String method2(String key){
    	InputStream ins = InpayInterfaceUtil.class.getClassLoader().getResourceAsStream(key);
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while((line = br.readLine())!=null){
                sb.append(line);                
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            try {
                if(br!=null){
                    br.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    /**
     * 获取真实的IP
     * @param request
     * @return
     */
	public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(!"".equals(ip) && ip!=null && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(!"".equals(ip) && ip!=null && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    
	}
	
	/**
	 * 将实体对象封装成Map对象
	 * @param type
	 * @param obj
	 * @return
	 */
	public static Map<String,String> createMap(Class<?> type,Object obj) {
		Map<String,String> map = new HashMap<String,String>();
		try{
	        BeanInfo beanInfo = Introspector.getBeanInfo(type);
	        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
	        for(int i=0;i<propertyDescriptors.length;i++){
	        	 PropertyDescriptor descriptor=propertyDescriptors[i];
	        	 String propertyName = descriptor.getName();
	             if (!propertyName.equals("class")) {
	                 Method readMethod = descriptor.getReadMethod();
	                 Object result = readMethod.invoke(type.cast(obj), new Object[0]);
	                 //需要签名的数据必须是非null,可以是""
	                 if(null!=result){
	                	 map.put(propertyName,result.toString());
	                 }
	             }
	        }
		}catch(Exception e){
			e.printStackTrace();
			map=null;
		}
		return map;
	}
	
	/**
	 * 准备参数
	 * @param map
	 * @param sign
	 * @return
	 */
	public static Map<String, String> params(Map<String, String> map,String sign){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sign", sign);
		paramMap.put("signType", "RSA");
		for(Iterator it= map.entrySet().iterator();it.hasNext();){
			Map.Entry entry = (Map.Entry) it.next();
			paramMap.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return paramMap;
	}
	
}
