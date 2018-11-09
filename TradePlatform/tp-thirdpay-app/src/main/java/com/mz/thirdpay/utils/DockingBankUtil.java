package com.mz.thirdpay.utils;

import com.mz.app.log.service.AppLogThirdInterfaceService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import com.mz.app.log.model.AppLogThirdInterface;

public class DockingBankUtil {
	private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("YYYYMMDD");
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("YYYYMMDDHHMMSS");
	private static final String encoding = "GBK";
	
	/**
	 * 生成8位的业务流水号
	 * 
	 * @return
	 */
	public static String createThirdLogNo(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String rdNum=df.format(new Date());
        Random random = new Random();
        int ird = random.nextInt(99999999);
        String srd= String.format("%06d", ird);
        return rdNum + srd;	
	}
	
	//生成8位随机数
	public static String getOrderId(){
		String orderId ;
		java.util.Random r=new java.util.Random();
		while(true){
			int i=r.nextInt(99999999);
			if(i<0)i=-i;
			orderId = String.valueOf(i);
		//	System.out.println("---生成随机数---"+orderId);
			if(orderId.length()<8){
		//		System.out.println("---位数不够8位---"+orderId);
				continue;
			}
			if(orderId.length()>=8){
				orderId = orderId.substring(0,8);
		//		System.out.println("---生成8位流水---"+orderId);
				break;
			}
		  }
		return orderId;
	}
	public static String getNow(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        return df.format(new Date());
	}
	public static Boolean saveLog(Map<String,Object> returnMap,String req,String url,
      AppLogThirdInterfaceService appLogThirdInterfaceService){
		try {
			//保存调用日志
			AppLogThirdInterface log=new AppLogThirdInterface();
			log.setIp("47.75.200.109");
			log.setUrl(url);
			log.setParams(req);
			StringBuffer result = new StringBuffer();
			if(null != returnMap){
				if(returnMap.containsKey("TxnReturnCode")){
					if(returnMap.containsKey("TxnReturnCode")){
						result.append("TxnReturnCode:"+returnMap.get("TxnReturnCode").toString());
					}
					if( "000000".equals(returnMap.get("TxnReturnCode").toString())){
						if(returnMap.containsKey("CnsmrSeqNo")){
							result.append(",CnsmrSeqNo:"+returnMap.get("CnsmrSeqNo").toString());
						}
						if(returnMap.containsKey("TxnReturnMsg")){
							String str = returnMap.get("TxnReturnMsg").toString();
							if(str.length()<200){
								result.append(",TxnReturnMsg:"+str);
							}else{
								result.append(",TxnReturnMsg:"+str.substring(0,200));
							}
						}
					}else{
						if(returnMap.containsKey("TxnReturnMsg")){
							String str = returnMap.get("TxnReturnMsg").toString();
							if(str.length()<200){
								result.append(",TxnReturnMsg:"+str);
							}else{
								result.append(",TxnReturnMsg:"+str.substring(0,200));
							}
						}
					}
					
				}
				
			}
			log.setResult(result.toString());
			appLogThirdInterfaceService.save(log);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
