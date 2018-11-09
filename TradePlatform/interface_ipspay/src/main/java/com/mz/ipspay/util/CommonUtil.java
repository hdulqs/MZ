package com.mz.ipspay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class CommonUtil {
	protected transient static final Log logger = LogFactory.getLog(CommonUtil.class);
	//时间格式
	public static DateFormat df1 = new SimpleDateFormat("yyyyMMdd HHmmss");
	public static DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat df3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static DateFormat df4 = new SimpleDateFormat("HH:mm:ss");
	public static DateFormat df5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat df6 = new SimpleDateFormat("yyyyMMddHHmmss");
	public static DateFormat df7 = new SimpleDateFormat("yyyyMMdd");
	public static DateFormat df8 = new SimpleDateFormat("HHmmss");
	public static DateFormat df9 = new SimpleDateFormat("yyyyMMddHH:mm:ss");
	/**
	 * <p>判断对象是否为null或""</p>
	 * @author:YYQ
	 * @date:2016-8-10下午6:40:15
	 * @param obj 判断对象
	 * @return true:不为null或"",false:为null或""
	 */
	public static boolean isNotNull(Object obj){
		if(!"".equals(obj) && obj != null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * <p>根据某个时间，计算下一天的时间</p>
	 * @author:YYQ
	 * @date:2016-8-19上午11:41:04
	 * @param:time 时间
	 * @return 返回第下一天的时间
	 */
	public static Date getSecondDay(Date time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MINUTE,0);
		int day=calendar.get(Calendar.DATE); 
		calendar.set(Calendar.DATE,day+1); 
		return calendar.getTime();
	}
	/**
	 * String转化为boolean类型
	 * @param value
	 * @return
	 */
	public static boolean getBooleanValue(String value){
		return Boolean.valueOf(value);
	}
	 /**
     * 归属地查询
     * @param mobile
     * @return mobileAddress
     */
    @SuppressWarnings("unused")
    private static String getLocationByMobile(final String mobile) throws ParserConfigurationException, SAXException, IOException{ 
        String MOBILEURL = " http://www.youdao.com/smartresult-xml/search.s?type=mobile&q="; 
        String result = callUrlByGet(MOBILEURL + mobile, "GBK");
        StringReader stringReader = new StringReader(result); 
        InputSource inputSource = new InputSource(stringReader); 
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
        Document document = documentBuilder.parse(inputSource);
 
        if (!(document.getElementsByTagName("location").item(0) == null)) {
            return document.getElementsByTagName("location").item(0).getFirstChild().getNodeValue();
        }else{
            return "无此号记录！";
        }
    }
    /**
     * 获取URL返回的字符串
     * @param callurl
     * @param charset
     * @return
     */
    private static String callUrlByGet(String callurl,String charset){   
    	
        String result = "";   
        try {   
            URL url = new URL(callurl);   
            URLConnection connection = url.openConnection();   
            connection.connect();   
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));   
            String line;   
            while((line = reader.readLine())!= null){    
                result += line;   
                result += "\n";
            }
        } catch (Exception e) {   
        	logger.info(e.getMessage());
            return "";
        }
        return result;
    }
    /**
     * 手机号码归属地
     * @param tel  手机号码
     * @return 135XXXXXXXX,联通/移动/电信,湖北武汉
     * @throws Exception
     * @author 
     */
    public static String getMobileLocation(String tel) throws Exception{
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(tel);
        if(matcher.matches()){
            String url = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=" + tel;
            String result = callUrlByGet(url,"GBK");
            StringReader stringReader = new StringReader(result); 
            InputSource inputSource = new InputSource(stringReader); 
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
            Document document = documentBuilder.parse(inputSource);
            String retmsg = document.getElementsByTagName("retmsg").item(0).getFirstChild().getNodeValue();
            if(retmsg.equals("OK")){
                String supplier = document.getElementsByTagName("supplier").item(0).getFirstChild().getNodeValue().trim();
                String province = document.getElementsByTagName("province").item(0).getFirstChild().getNodeValue().trim();
                String city = document.getElementsByTagName("city").item(0).getFirstChild().getNodeValue().trim();
                if (province.equals("-") || city.equals("-")) {

//                    return (tel + "," + supplier + ","+ getLocationByMobile(tel));
                    return (getLocationByMobile(tel) + "," + supplier);
                }else {
//                    return (tel + "," + supplier + ","+ province + city);
                    //return (province + city + "," + supplier );
                	return (province + "-" + city);
                }
            }else {
                //return "无此号记录！";
            	return "nonExistent";
            }
        }else{
            //return tel+ "：手机号码格式错误！";
        	return "formatError";
        }

    }
    /**
     * @Description: 获取length位随机数字
     * @param: length  获取的位数
     * @return String  length位随机数
     * @throws
     * @date 2016-9-18
     */
    public static String getRandomNum(int length){
		try{
			if (length <= 0){
				return "";
			}
			Random r = new Random();
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < length; i++){
				result.append(Integer.toString(r.nextInt(10)));
			}
			return result.toString();
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return null;
		}
	}
    /**
     * @Description: 获取length位字符
     * @param: length 获取的位数
     * @return String  
     * @date 2016-9-18
     */
    public static String getRandomChar(int length){
    	try {
    		if (length <= 0){
				return "";
			}else{
				StringBuffer result = new StringBuffer();
				for(int i=0;i<length;i++){  
		            int intVal=(int)(Math.random()*26+97);  
		            result.append((char)intVal);  
		        } 
				return result.toString();
			}
		} catch (Exception e) {
			return null;
		}
    }
    /**
     * @Description: 获取charLen位字符和numberLen位数字的字符串
     * @param: numberLen  获取数字的位数
     * @param: charLen    获取字母的位数
     * @return String   字符串
     * @throws
     * @date 2016-9-18
     */
    public static String getRandomStr(int charLen,int numberLen){
    	return getRandomChar(charLen)+getRandomNum(numberLen);
    }
}
