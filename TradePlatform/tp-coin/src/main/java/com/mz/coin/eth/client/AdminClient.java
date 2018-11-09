package com.mz.coin.eth.client;

import com.mz.Constant;
import com.mz.util.log.LogFactory;
import com.mz.utils.Properties;
import java.util.HashMap;
import java.util.Map;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.http.HttpService;
/**
 * 
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年9月21日 上午9:45:04
 */
public class AdminClient {

	private static String  protocol= Properties.appcoinMap().get(Constant.ETHER.toLowerCase()+"_protocol");
	private static String  ip=Properties.appcoinMap().get(Constant.ETHER.toLowerCase()+"_ip");
	private static String  port=Properties.appcoinMap().get(Constant.ETHER.toLowerCase()+"_port");
    private volatile static Admin admin;
    private static Map<String,Admin> adminMap;
    

    /**
     * 建立链接
     * <p> TODO</p>
     * @author:         shangxl
     * @param:    @return
     * @return: JsonRpc2_0Admin 
     * @Date :          2018年3月2日 上午11:27:57   
     * @throws:
     */
    public static JsonRpc2_0Admin getClient(){
        if(admin==null){
            synchronized (Admin.class){
                if(admin==null){
                	String url=protocol+":"+ip+":"+port;
                	LogFactory.info("获取eth的jsonrpc连接===url="+url);
                	admin =Admin.build(new HttpService(url));
                }
            }
        }
        return (JsonRpc2_0Admin)admin;
    }
    
    
    /**
     * 根据参数建立链接
     * <p> TODO</p>
     * @author:         shangxl
     * @param:    @param protocol
     * @param:    @param ip
     * @param:    @param port
     * @param:    @return
     * @return: JsonRpc2_0Admin
     * @Date :          2018年3月2日 上午11:27:18   
     * @throws:
     */
    public static JsonRpc2_0Admin getClientByParam(String protocol,String ip,String port){
    	String url=protocol+":"+ip+":"+port;
    	//url充当key
        if(adminMap==null||adminMap.get(url)==null){
        	if(adminMap==null){
        		adminMap=new HashMap<String, Admin>();
        	}
        	try {
        		adminMap.put(url, Admin.build(new HttpService(url)));
			} catch (Exception e) {
				LogFactory.info("etc-rpc连接建立异常");
			}
        }
        return (JsonRpc2_0Admin)adminMap.get(url);
    }
}
