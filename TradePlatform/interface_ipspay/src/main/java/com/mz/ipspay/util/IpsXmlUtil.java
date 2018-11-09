package com.mz.ipspay.util;

import com.mz.ipspay.IpspayBody;
import com.mz.ipspay.IpspayHead;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 环迅的报文组装工具类
 * <p> TODO</p>
 * @author:         Zhang Lei 
 * @Date :          2016年12月1日 下午8:00:44
 */
@SuppressWarnings("all")
public class IpsXmlUtil {
	/**
	 * 转化报文体对象
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param ips
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年12月3日 上午10:44:26   
	 * @throws:
	 */
	public static String bodyBeanToXML(IpspayBody ips) {  
        try {  
            JAXBContext context = JAXBContext.newInstance(IpspayBody.class); 
            Marshaller marshaller = context.createMarshaller(); 
            //编码格式
            marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");//
            //是否省略xml头信息<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            //是否格式化生成的xml串
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);  
            StringWriter writer = new StringWriter();
            marshaller.marshal(ips, writer);  
            return writer.toString();
        } catch (JAXBException e) {  
        	throw new RuntimeException(e);
        }  
  
    }  
	
	
	/**
	 * 转化报文头对象
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @param ips
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年12月3日 上午10:44:06   
	 * @throws:
	 */
	public static String headBeanToXML(IpspayHead ips) {  
		try {  
			JAXBContext context = JAXBContext.newInstance(IpspayHead.class); 
			Marshaller marshaller = context.createMarshaller(); 
			//编码格式
			marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");//
			//是否省略xml头信息<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			//是否格式化生成的xml串
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);  
			StringWriter writer = new StringWriter();
			marshaller.marshal(ips, writer);  
			return writer.toString();
		} catch (JAXBException e) {  
			throw new RuntimeException(e);
		}  
		
	}  
	
	
	
	/**
	 * xml转map 不带属性
	 * 
	 * @param xmlStr
	 * @param needRootKey  是否需要在返回的map里加根节点键
	 * @return
	 * @throws DocumentException
	 */
	public static Map xml2map(String xmlStr, boolean needRootKey) {
		Document doc;
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			doc = DocumentHelper.parseText(xmlStr);
			Element root = doc.getRootElement();
			map = (Map<String, Object>) getxml2map(root,map);
			if (root.elements().size() == 0 && root.attributes().size() == 0) {
				return map;
			}
			if (needRootKey) {
				// 在返回的map里加根节点键（如果需要）
				Map<String, Object> rootMap = new HashMap<String, Object>();
				rootMap.put(root.getName(), map);
				return rootMap;
			}
			return map;
		} catch (DocumentException e) {
			System.out.println("解析环迅回执报文异常！！！");
			e.printStackTrace();
		}
		return map;
	}
	
	
	
    /** 
    * xml转map 只获取最深一级的节点
    * 注意报文格式，可能转不了
    * @param e 
    * @return 
    */  
   private static Map getxml2map(Element e,Map map) {  
       List list = e.elements();  
       if (list.size() > 0) {  
           for (int i = 0; i < list.size(); i++) {  
               Element iter = (Element) list.get(i);  
               List mapList = new ArrayList();  
               if (iter.elements().size() > 0) {  
                   Map m = getxml2map(iter,map);  
               } else {  
                   if (map.get(iter.getName()) == null) {  
                	   map.put(iter.getName(), iter.getText());  
                	   //System.out.println("2:"+iter.getName()+":"+iter.getText());
                   }  
               }  
           }  
       } 
       return map;  
   }  
   
   /**
    * 根据回执的报文截取报文体，用来验证签名
    * <p> TODO</p>
    * @author:         Zhang Lei
    * @param:    @param resMsg
    * @param:    @return
    * @return: String 
    * @Date :          2016年12月3日 下午4:21:26   
    * @throws:
    */
   public static String getBodyXml(String resMsg) {
		//根据<body>标签截取
	   	String msg=resMsg.substring(resMsg.indexOf("<body>"), resMsg.indexOf("</body>")+7);
		return msg;
	}

   
   
   
	public static void main(String[] args) {
		/*String resMsg="<Ips><GateWayRsp><head><ReferenceID>123</ReferenceID><RspCode>123</RspCode><RspMsg>123</RspMsg><ReqDate>123</ReqDate><RspDate>123</RspDate><Signature>123</Signature></head><body><MerBillNo>01161203150201362340</MerBillNo><CurrencyType>123</CurrencyType><Amount>123</Amount><Date>123</Date><Status>123</Status><Msg>123</Msg><Attach>123</Attach><IpsBillNo>123</IpsBillNo><IpsTradeNo>123</IpsTradeNo><RetEncodeType>123</RetEncodeType><BankBillNo>123</BankBillNo><ResultType>123</ResultType><IpsBillTime>123</IpsBillTime></body></GatewayRsp></Ips>";
		String msg=resMsg.substring(resMsg.indexOf("<body>"), resMsg.indexOf("</body>")+7);
		System.out.println(msg);*/
		
		String msg="<Ips><GateWayRsp><head><ReferenceID>123</ReferenceID><RspCode>123</RspCode><RspMsg>123</RspMsg><ReqDate>123</ReqDate><RspDate>123</RspDate><Signature>123</Signature></head><body><MerBillNo>01161203150201362340</MerBillNo><CurrencyType>123</CurrencyType><Amount>123</Amount><Date>123</Date><Status>Y</Status><Msg>123</Msg><Attach>123</Attach><IpsBillNo>123</IpsBillNo><IpsTradeNo>123</IpsTradeNo><RetEncodeType>123</RetEncodeType><BankBillNo>123</BankBillNo><ResultType>123</ResultType><IpsBillTime>123</IpsBillTime></body></GateWayRsp></Ips>";
		
		System.out.println(IpsXmlUtil.xml2map(msg,false));
	
	
	
	}

   
   
   
   
   
   
   
   
   
   
   
   
   
   
   /* public void XMLStringToBean(){  
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><student><age>22</age><classroom><grade>4</grade><id>1</id><name>软件工程</name></classroom><id>101</id><name>张三</name></student>";  
        try {  
            JAXBContext context = JAXBContext.newInstance(Student.class);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            Student student = (Student)unmarshaller.unmarshal(new StringReader(xmlStr));  
            System.out.println(student.getAge());  
            System.out.println(student.getClassroom().getName());  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
          
    }  */
	//获取对象的所有属性和值
		/*public static void reflect(IEntity e) throws Exception{  
	        Class cls = e.getClass();  
	        Field[] fields = cls.getDeclaredFields();  
	        for(int i=0; i<fields.length; i++){  
	            Field f = fields[i];  
	            f.setAccessible(true);  
	            System.out.println("属性名:" + f.getName() + " 属性值:" + f.get(e));  
	        }   
	    }  */
}
