package com.mz.manage.remote;

import java.util.Map;

public interface RemoteSmsService {
	
	/**
	 * 发送短信
	 * @param json
	 */
	public void sendsms( String params,String phone);
	
	
	public void sendsmsHai( String params,String phoneType,String phone);

    /**
    * @Description:    根据系统代码获取手机模板发送短信 syscode 系统代码  #替换字# map
    * @Author:         zongwei
    * @CreateDate:     2018/6/11 11:48
    * @UpdateUser:    zongwei
    * @UpdateDate:     2018/6/11 11:48
    * @UpdateRemark:   创建
    * @Version:        1.0
    */
	public void sendSmsInfo(String params, String phoneType, String phone,String syscode,Map<String, Object> map);

}
