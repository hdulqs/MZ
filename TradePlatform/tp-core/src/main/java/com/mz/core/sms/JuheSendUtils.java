/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年9月7日 下午6:50:07
 */
package com.mz.sms;

import com.alibaba.fastjson.JSON;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.sms.send.model.AppJuheSend;
import com.mz.core.mvc.service.appJuheSend.AppJuheSendService;
import com.mz.util.QueryFilter;
import com.mz.util.http.HttpConnectionUtil;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>聚合数据接口</p>
 * @author:         Liu Shilei 
 * @Date :          2016年9月7日 下午6:50:07 
 */
public class JuheSendUtils {
	
	/**
	 * 身份认证
	 */
	public final static String CARD = "juhe_card";
	
	/**
	 * 银行卡认证
	 */
	public final static String BANK = "juhe_bank";
	
	/**
	 * 
	 * <p>效验身份证</p>
	 * @author:         Liu Shilei
	 * @param:    @param name
	 * @param:    @param idCard
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2016年9月7日 下午6:51:30   
	 * @throws:
	 */
	public static boolean checkCard(String name,String idCard){
		
		try {
			String url = PropertiesUtils.APP.getProperty("app.smsUrl")+"/sdk/checkCard";
			String smsKey = PropertiesUtils.APP.getProperty("app.smsKey");
			
			String param = "smsKey="+smsKey+"&name="+URLEncoder.encode(name + "", "UTF-8")+"&idCard="+idCard;
			
			//先保存
			AppJuheSendService appJuheSendService=(AppJuheSendService) ContextUtil.getBean("appJuheSendService");
			AppJuheSend appJuheSend = new AppJuheSend();
			appJuheSend.setPostParam(param);
			appJuheSend.setType(getSendTypeValue(CARD));
			appJuheSend.setServerUrl(url);
			appJuheSendService.save(appJuheSend);
			
			//再次查询获取id
			QueryFilter filter=new QueryFilter(AppJuheSend.class);
			filter.addFilter("postParam=", param);
			filter.setOrderby(" id desc");
			
			List<AppJuheSend> list=appJuheSendService.find(filter);
			if(list!=null&&list.size()>0){
				AppJuheSend old=list.get(0);
				appJuheSend.setId(old.getId());
			}
			//发送实名认证请求
			String postSend = HttpConnectionUtil.getSend(url, param);
			//修改实名认证状态
			AppJuheSend appJuheSend2=appJuheSendService.get(Long.valueOf(appJuheSend.getId()));
			appJuheSend2.setThirdPartyResult(postSend);
			
			JsonResult parseObject = JSON.parseObject(postSend, JsonResult.class);
			if(parseObject!=null&&parseObject.getSuccess()){
				appJuheSend2.setThrough(true);
				appJuheSendService.update(appJuheSend2);
				return true;
			}
			appJuheSend2.setThrough(false);
			appJuheSendService.update(appJuheSend2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 聚合实名认证接口
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param name
	 * @param:    @param idCard
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年3月2日 下午2:27:24   
	 * @throws:
	 */
	public static boolean auth (String name,String idCard,String url,String smsKey){
		
		try {
			String param = "key="+smsKey+"&idcard="+idCard+"&realname="+URLEncoder.encode(name + "", "UTF-8");
			//先保存
			AppJuheSendService appJuheSendService=(AppJuheSendService) ContextUtil.getBean("appJuheSendService");
			AppJuheSend appJuheSend = new AppJuheSend();
			appJuheSend.setPostParam(param);
			appJuheSend.setType(getSendTypeValue(CARD));
			appJuheSend.setServerUrl(url);
			appJuheSendService.save(appJuheSend);
			
			
			//再次查询获取id
			QueryFilter filter=new QueryFilter(AppJuheSend.class);
			filter.addFilter("postParam=", param);
			filter.setOrderby(" id desc");
			
			List<AppJuheSend> list=appJuheSendService.find(filter);
			if(list!=null&&list.size()>0){
				AppJuheSend old=list.get(0);
				appJuheSend.setId(old.getId());
			}
			//再调用 
			String postSend = HttpConnectionUtil.getSend(url, param);
			System.out.println("实名认证调用结果："+postSend);
			AppJuheSend appJuheSend2=appJuheSendService.get(Long.valueOf(appJuheSend.getId()));
			appJuheSend2.setThirdPartyResult(postSend);
			//第三方返回的json不规范，暂无办法解析
			if(postSend.contains("\"res\":1")){
				appJuheSend2.setThrough(true);
				appJuheSendService.update(appJuheSend2);
				return true;
			}
			appJuheSend2.setThrough(false);
			appJuheSendService.update(appJuheSend2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param hrySmstype
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年9月6日 下午7:19:53   
	 * @throws:
	 */
	public static String getSendTypeValue(String hrySmstype) {
		if(CARD.equals(hrySmstype)){
			return "身份认证";
		}else if(BANK.equals(hrySmstype)){
			return "银行卡认证";
		}
		return "";
	}
	
}
