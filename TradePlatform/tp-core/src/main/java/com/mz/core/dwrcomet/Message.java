/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月9日10:23:02
 */
package com.mz.core.dwrcomet;

import java.util.Date;

/**
 * 聊天消息
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年10月9日 上午10:22:23
 */
public class Message
{
    
	private String id;
	
	private String sendUserId;
	
	private String receiveUserId;
	
	private String text;
	
	
	
	private Date date;

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getId() {
		return id;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getSendUserId() {
		return sendUserId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getReceiveUserId() {
		return receiveUserId;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getText() {
		return text;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Date
	 */
	public Date getDate() {
		return date;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	
	
}
