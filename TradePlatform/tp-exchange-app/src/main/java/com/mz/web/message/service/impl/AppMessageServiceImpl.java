/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Wu Shuiming
 * @version:   V1.0 
 * @Date:      2016年5月30日 下午5:41:43
 */
package com.mz.web.message.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mz.core.mvc.dao.base.BaseDao;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.impl.BaseServiceImpl;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.model.AppMessage;
import com.mz.web.app.model.MessageAsCustomer;
import com.mz.web.app.model.vo.MessageListVo;
import com.mz.web.message.dao.AppMessageDao;
import com.mz.web.message.service.AppMessageService;
import com.mz.web.message.service.MessageAsCustomerService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * <p> TODO</p>
 * @author:         Wu Shuiming
 * @Date :          2016年5月30日 下午5:41:43
 */
@Service("appMessageService")
public class AppMessageServiceImpl extends BaseServiceImpl<AppMessage, Long> implements AppMessageService{

	
	@Resource(name = "appMessageDao")
	@Override
	public void setDao(BaseDao<AppMessage, Long> dao) {
		super.dao = dao;
	}

	@Resource(name = "messageAsCustomerService")
	public MessageAsCustomerService messageAsCustomerService;
	
	@Override
	public PageResult selectMessageListVoByList(QueryFilter filter,int state) {
		
		PageResult pageResult = new PageResult();
		Page<MessageListVo> page = null;
		if (filter.getPageSize().compareTo(Integer.valueOf(-1)) == 0) {
			// pageSize = -1 时
			// pageHelper传pageSize参数传0查询全部
			page = PageHelper.startPage(filter.getPage(), 0);
		} else {
			page = PageHelper.startPage(filter.getPage(), filter.getPageSize());
		}
		// 查询数据
		((AppMessageDao)dao).findMessageListVoList(state);
		
		pageResult.setRows(page.getResult());
		// 设置总记录数
		pageResult.setRecordsTotal(page.getTotal());
		pageResult.setDraw(filter.getDraw());
		pageResult.setPage(filter.getPage());
		pageResult.setPageSize(filter.getPageSize());
		
		return pageResult ;
	}

	
	/**
	 * 
	 * 修改并保存消息 
	 * 
	 * 如果选择的发送人不为空 将默认是添加用户 
	 * 
	 */
	@Override
	public JsonResult updateMessage(AppMessage appMessage,String[] ids){
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		
		/*super.update(appMessage);
		
		if(ids.length>0){
			
			if(appMessage.getIsAll() == 1){
				// 删除之前保存的用户
				for(int i = 0;i<ids.length;i++){
					messageAsCustomerService.delete(ids[i]);
				}
				// 重新给所有人发
				messageAsCustomerService.sendAll(appMessage.getId());
				jsonResult.setMsg("修改成功");
				return jsonResult;
				
			}else{
				
				QueryFilter filter = new QueryFilter(MessageAsCustomer.class);
				filter.addFilter("messageId=", appMessage.getId());
				List<MessageAsCustomer> list = messageAsCustomerService.find(filter);
			
				if(list.size()>0){
					// 如果不是群发那就只需要删除部分重复的用户就够了
					for(MessageAsCustomer messageAsCustomer : list){
						Long id = messageAsCustomer.getId();
						for(int i=0; i<ids.length;i++ ){
							if(ids[i]==id){
								ids[i] = 0l ;
							}
						}
					}
				}
			}
			messageAsCustomerService.sendPartial(appMessage.getId(), ids);
		}
		jsonResult.setMsg("修改成功");*/
		return jsonResult;
	}


	/**
	 * 保存消息
	 */
	@Override
	public JsonResult saveMessage(AppMessage appMessage, String[] userNames) {
	
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		super.save(appMessage);
		// 发送消息	                           
		Long id = appMessage.getId();                               
		Integer all = appMessage.getIsAll();
		if(all==1){
			messageAsCustomerService.sendAll(id);
		}else if(all==0&&userNames.length>0){
			messageAsCustomerService.sendPartial(id, userNames);
		}
		jsonResult.setMsg("保存成功");
		return jsonResult;
	}
	/**
	 * 保存消息
	 */
	@Override
	public JsonResult saveMessageName(AppMessage appMessage,String receiveUserNames) {
	
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		super.save(appMessage);
		// 发送消息	                           
		Long id = appMessage.getId();                               
		Integer all = appMessage.getIsAll();
		if(all == 1){
			messageAsCustomerService.sendAll(id);
		}if(all == 0){
			if(null!=receiveUserNames&&!"".equals(receiveUserNames)){
				messageAsCustomerService.sendPartialName(id, receiveUserNames);
			}
		}
		jsonResult.setMsg("保存成功");
		return jsonResult;
	}

	/**
	 * 删除多个消息
	 */
	@Override
	public JsonResult removeMessage(Long[] ids) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		if(ids.length>0){
			for(Long id: ids) {
				AppMessage message = super.get(id);
				message.setIsSend(2);
				super.update(message);
			}
		}
		jsonResult.setMsg("删除成功");
		return jsonResult;
	}


	/**
	 * 发送一条或多条消息
	 * 
	 */
	@Override
	public JsonResult sendMessage(Long[] ids) {
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		
		if(ids.length>0){
			for(int i = 0; i<ids.length;i++){
				AppMessage message = super.get(ids[i]);
				QueryFilter filter = new QueryFilter(MessageAsCustomer.class);
				filter.addFilter("messageId=", ids[i]);
				
				
				List<MessageAsCustomer> list = messageAsCustomerService.find(filter);
				if(list.size()>0){
					message.setIsSend(1);
					message.setSendDate(new Date());
					message.setSendUserName(ContextUtil.getCurrentUserName());
					super.update(message);
					jsonResult.setMsg("消息发送成功");
				}
			}
			jsonResult.setMsg("所有消息发送成功");
			return jsonResult;
			
		}else{
			jsonResult.setSuccess(false);
			jsonResult.setMsg("所选的消息为0");
			return jsonResult;
		}
	}
	
	
	
// ---------------------------------------------------------------------------------
	
	
	
	@Override
	@SuppressWarnings("rawtypes")
	public PageResult selectMessageVoByPage(QueryFilter filter){
		PageResult page = this.selectMessageByPage(filter);
		List list = page.getRows();
		if(list.size()>0){
			for(int i = 0; i<list.size();i++){
				MessageListVo messageVo = (MessageListVo)list.get(i);
				List<MessageAsCustomer> list2 = ((AppMessageDao)dao).selectMessageAsCustromer(messageVo.getId());
				messageVo.setList(list2);
			}
		}
		return page;
	}
	
	
	
	

	
	
	public PageResult selectMessageByPage(QueryFilter filter){
		
		PageResult pageResult = new PageResult();
		Page<MessageListVo> page = null;
		if (filter.getPageSize().compareTo(Integer.valueOf(-1)) == 0) {
			// pageSize = -1 时
			// pageHelper传pageSize参数传0查询全部
			page = PageHelper.startPage(filter.getPage(), 0);
		} else {
			page = PageHelper.startPage(filter.getPage(), filter.getPageSize());
		}
		// 查询数据
		((AppMessageDao)dao).selectMessageVo();
		
		
		pageResult.setRows(page.getResult());
		pageResult.setRecordsTotal(page.getTotal());
		pageResult.setDraw(filter.getDraw());
		pageResult.setPage(filter.getPage());
		pageResult.setPageSize(filter.getPageSize());
		
		return pageResult ;
	}
	
	
	
	
	
	
}