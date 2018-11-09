package com.mz.manage.remote;

import com.mz.manage.remote.model.Oamessage;
import java.util.Map;

import com.mz.manage.remote.model.base.FrontPage;

/**
 * 站内信消息接口
 * @author tzw
 *
 * 2017年7月19日
 */
public interface RemoteOamessageService {

	/**
	 * 获取当前登录用户所有站内信
	 * @param params
	 * @return
	 * 2017年7月19日
	 * tzw
	 */
	public FrontPage findOamessage(Map<String, String> params);
	
	/**
	 * 根据id获取站内信并且设置已读
	 * @param sid
	 * @return
	 * 2017年7月21日
	 * tzw
	 */
	public Oamessage read(Long sid);
	
	/**
	 * 根据id获取消息
	 * (有用户id以及用户名，用户操作时校验是否本人操作)
	 * @param sid
	 * @return
	 * 2017年7月21日
	 * tzw
	 */
	public Oamessage get(Long sid);
	
}
