package com.mz.thirdpay.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.app.log.model.AppLogThirdInterface;
import com.mz.app.log.service.AppLogThirdInterfaceService;
import com.mz.manage.remote.model.AppLogThirdInterfaceDTO;
import com.mz.manage.remote.model.LmcTransfer;
import com.mz.remote.RemoteThirdInterfaceService;
import com.mz.util.StringUtil;
import com.mz.util.http.HttpConnectionUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.md5.Md5Encrypt;
import com.mz.util.properties.PropertiesUtils;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>
 * TODO
 * </p>
 * 
 * @author: Shangxl
 * @Date : 2017年7月27日 下午2:56:10
 */
public class RemoteThirdInterfaceServiceImpl implements RemoteThirdInterfaceService {

	@Resource
	private AppLogThirdInterfaceService appLogThirdInterfaceService;
	@Override
	public String createCoinAddress(String Account_id, String Symbol) {
		try {
			String Secret = PropertiesUtils.APP.getProperty("app.LMC_Secret");
			String App_key = PropertiesUtils.APP.getProperty("app.LMC_App_key");
			//Url域名
			String Url = PropertiesUtils.APP.getProperty("app.LMC_Url");
			//创建钱包地址
			String CreateAddressUrl = PropertiesUtils.APP.getProperty("app.LMC_CreateAddressUrl");
			//加密key
			String secret=Secret;
			//app_key
			String app_key=App_key;
			//Url
			String strUrl=Url+CreateAddressUrl;
			//account_id  必须大于10000
			String account_id=Account_id;
			
			Long time=System.currentTimeMillis()/1000;
			//请求时间戳
			String request_time=time.toString();
			//币编码
			String symbol=Symbol;
			String []s={app_key,account_id,request_time,symbol,secret};
			//数组字典排序
			String auth_code= StringUtil.stringSort(s, "_");
			//MD5加密
			auth_code= Md5Encrypt.md5(auth_code);
			Map<String,String> map=new HashMap<String, String>();
			map.put("auth_code", auth_code);
			map.put("account_id", account_id);
			map.put("symbol", symbol);
			map.put("request_time", request_time);
			map.put("app_key", app_key);
			
			String param=StringUtil.string2params(map);
			System.out.println(strUrl+"?"+param);
			String result= HttpConnectionUtil.postSend(strUrl, param);
			LogFactory.info("调用钱包生成地址接口返回：："+result);
			//处理垃圾数据
			result=StringUtil.string2json(result);
			//保存调用日志
			AppLogThirdInterface log=new AppLogThirdInterface();
			log.setIp("47.75.200.109");
			log.setUrl(strUrl);
			log.setParams(param);
			log.setResult(result);
			appLogThirdInterfaceService.save(log);
			//保存日志end
			LogFactory.info("创建钱包地址："+result);
			if(StringUtils.isNotEmpty(result)){
				JSONObject obj=JSON.parseObject(result, JSONObject.class);
				if(obj!=null&&"200".equals(obj.get("code").toString())){
					JSONObject data=(JSONObject) obj.get("data");
					return data.toJSONString();
				}
			}
		} catch (Exception e) {
			LogFactory.info("创建钱包地址错误，信息如下：");
			e.printStackTrace();
		}
		JSONObject error=new JSONObject();
		error.put("symbol", Symbol);
		error.put("account_id", Account_id);
		error.put("address", "");
		return error.toJSONString();
	}
	
	
	@Override
	public String[] lmcTransfer(LmcTransfer transfer) {
		String[]result=new String[2];
		try {
			String Secret = PropertiesUtils.APP.getProperty("app.LMC_Secret");
			String App_key = PropertiesUtils.APP.getProperty("app.LMC_App_key");
			//转币
			String strUrl=PropertiesUtils.APP.getProperty("app.LMC_Url")+PropertiesUtils.APP.getProperty("app.LMC_Wallet_Transfer");
			transfer.setApp_key(App_key);
			transfer.setRequest_time((System.currentTimeMillis()/1000)+"");
			//参数
			//app_key
			String app_key=transfer.getApp_key();
			String request_time=transfer.getRequest_time();
			String transfer_type=transfer.getTransfer_type();
			String symbol=transfer.getSymbol();
			String type=transfer.getType();
			String coins=transfer.getCoins();
			String from_wallet=transfer.getFrom();
			String to_wallet=transfer.getTo();
			String transfer_id=transfer.getTransfer_id();
			String validation_type=transfer.getValidation_type();
			String validation_code=transfer.getValidation_code();
			String validation_phone=transfer.getValidation_phone();
			
			String[] s = {app_key,request_time,transfer_type,symbol,type,coins,from_wallet,to_wallet,transfer_id,
					validation_type,validation_code,validation_phone,Secret};
			//判断参数是否有效
			if(StringUtil.containEmpty(s)){
				result[0]="0";
				result[1]="参数无效";
				return result;
			}
			//数组字典排序
			String auth_code=StringUtil.stringSort(s, "_");
			auth_code=Md5Encrypt.md5(auth_code);
			Map<String,String> map=new HashMap<String, String>();
			map.put("auth_code", auth_code);
			map.put("app_key", app_key);
			map.put("request_time", request_time);
			map.put("transfer_type", transfer_type);
			map.put("symbol", symbol);
			map.put("type", type);
			map.put("coins", coins);
			map.put("from_wallet", from_wallet);
			map.put("to_wallet", to_wallet);
			map.put("transfer_id", transfer_id);
			map.put("validation_type",validation_type );
			map.put("validation_code",validation_code );
			map.put("validation_phone", validation_phone);
			String param=StringUtil.string2params(map);
			LogFactory.info(strUrl+"?"+param);
			String back=HttpConnectionUtil.postSend(strUrl, param);
			LogFactory.info("提币接口返回结果："+back);
			back=StringUtil.string2json(back);
			//保存调用日志
			AppLogThirdInterface log=new AppLogThirdInterface();
			log.setIp("47.75.200.109");
			log.setUrl(strUrl);
			log.setParams(param);
			log.setResult(back);
			appLogThirdInterfaceService.save(log);
			//保存日志end
			if(StringUtils.isNotEmpty(back)){
				JSONObject rs=JSON.parseObject(back);
				if(rs!=null){
					JSONObject data=rs.getJSONObject("data");
					if(data!=null){
						result[0]="8";
						result[1]=data.getString("transfer_id");
					}else{
						result[0]=rs.getString("code");
						result[1]=rs.getString("msg");
					}
					return result;
				}
			}
		} catch (Exception e) {
			LogFactory.info("提币业务处理服务器错误，信息如下：");
			e.printStackTrace();
		}
		result[0]="404";
		result[1]="未知错误";
		return result;
	}
	
	@Override
	public String [] walletTransferSum(LmcTransfer transfer){
		String [] result=new String[3];
		String strUrl=PropertiesUtils.APP.getProperty("app.LMC_Url")+PropertiesUtils.APP.getProperty("app.LMC_Wallet_Transfer_Sum");
		String Secret = PropertiesUtils.APP.getProperty("app.LMC_Secret");
		String App_key = PropertiesUtils.APP.getProperty("app.LMC_App_key");
		String app_key=App_key;
		String symbol=transfer.getSymbol();
		String from_wallet=transfer.getFrom();
		String to_wallet=transfer.getTo();
		String start_time=transfer.getStart_time();
		String end_time=transfer.getEnd_time();
		String request_time=transfer.getRequest_time();
		String[] s = {app_key,symbol,from_wallet,to_wallet,start_time,end_time,request_time,Secret};
		//数组字典排序
		String auth_code=StringUtil.stringSort(s, "_");
		auth_code=Md5Encrypt.md5(auth_code);
		Map<String,String> map=new HashMap<String, String>();
		map.put("app_key", app_key);
		map.put("symbol", symbol);
		map.put("from_wallet", from_wallet);
		map.put("to_wallet", to_wallet);
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		map.put("request_time", request_time);
		map.put("auth_code", auth_code);
		String param=StringUtil.string2params(map);
		LogFactory.info(strUrl+"?"+param);
		String back=HttpConnectionUtil.postSend(strUrl, param);
		LogFactory.info(back);
		//保存调用日志
		AppLogThirdInterface log=new AppLogThirdInterface();
		log.setUrl(strUrl);
		log.setParams(param);
		log.setResult(back);
		appLogThirdInterfaceService.save(log);
		//保存日志end
		JSONObject rs=JSON.parseObject(back);
		result[0]=rs.getString("code");
		result[1]=rs.getString("data");
		result[2]=rs.getString("msg");
		return result;
	}
	
	@Override
	public String[] listwalletTransfer(LmcTransfer transfer) {
		String [] result=new String[3];
		String strUrl=PropertiesUtils.APP.getProperty("app.LMC_Url")+PropertiesUtils.APP.getProperty("app.LMC_Wallet_Transfer_Sum");
		String Secret = PropertiesUtils.APP.getProperty("app.LMC_Secret");
		String App_key = PropertiesUtils.APP.getProperty("app.LMC_App_key");
		String app_key=App_key;
		String symbol=transfer.getSymbol();
		String tranfer_id=transfer.getTransfer_id();
		String from_wallet=transfer.getFrom();
		String to_wallet=transfer.getTo();
		String start_time=transfer.getStart_time();
		String end_time=transfer.getEnd_time();
		String request_time=transfer.getRequest_time();
		String[] s = {app_key,symbol,tranfer_id,from_wallet,to_wallet,start_time,end_time,request_time,Secret};
		//数组字典排序
		String auth_code=StringUtil.stringSort(s, "_");
		auth_code=Md5Encrypt.md5(auth_code);
		Map<String,String> map=new HashMap<String, String>();
		map.put("app_key", app_key);
		map.put("symbol", symbol);
		map.put("tranfer_id", tranfer_id);
		map.put("from_wallet", from_wallet);
		map.put("to_wallet", to_wallet);
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		map.put("request_time", request_time);
		map.put("auth_code", auth_code);
		String param=StringUtil.string2params(map);
		LogFactory.info(strUrl+"?"+param);
		String back=HttpConnectionUtil.postSend(strUrl, param);
		LogFactory.info(back);
		//保存调用日志
		AppLogThirdInterface log=new AppLogThirdInterface();
		log.setUrl(strUrl);
		log.setParams(param);
		log.setResult(back);
		appLogThirdInterfaceService.save(log);
		//end 保存log
		JSONObject rs=JSON.parseObject(back);
		result[0]=rs.getString("code");
		result[1]=rs.getString("data");
		result[2]=rs.getString("msg");
		return result;
	}
	
	@Override
	public AppLogThirdInterfaceDTO saveLog(AppLogThirdInterfaceDTO logDTO){
		try {
			String log_=JSON.toJSONString(logDTO);
			AppLogThirdInterface log=JSON.parseObject(log_, AppLogThirdInterface.class); 
			if(log.getId()==null){
				appLogThirdInterfaceService.save(log);
			}else{
				appLogThirdInterfaceService.update(log);
			}
			logDTO.setId(log.getId());
			return logDTO;
		} catch (Exception e) {
			LogFactory.info("更新或保存接口日志错误，错误信息如下：");
			e.printStackTrace();
			return null;
		}
	}
}
