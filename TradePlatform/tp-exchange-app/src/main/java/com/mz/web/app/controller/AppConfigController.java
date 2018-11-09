/**
 * Copyright:  北京互融时代软件有限公司
 * @author:    Gao Mimi
 * @version:   V1.0 
 * @Date:      2015年10月10日  18:41:55
 */
package com.mz.web.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.core.annotation.MyRequiresPermissions;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.AppConfig;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.redis.common.utils.RedisService;
import com.mz.util.QueryFilter;
import com.mz.util.sys.ContextUtil;
import com.mz.web.app.service.AppConfigService;
import com.mz.calculate.remote.settlement.RemoteAppReportSettlementService;
import com.mz.core.constant.StringConstant;
import com.mz.core.mvc.controller.base.BaseController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Gao Mimi
 * @Date : 2015年10月10日 18:41:55
 */
@Controller
@RequestMapping("/app/appconfig")
public class AppConfigController extends BaseController<AppConfig, Long> {

	@Autowired
	private RedisService redisService;
	
	
	@Resource(name = "appConfigService")
	@Override
	public void setService(BaseService<AppConfig, Long> service) {
		super.service = service;
	}

	private String site_name;

	/**
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @return: String
	 */
	public String getSite_name() {
		return site_name;
	}

	/**
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @return: String
	 */
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	/**
	 * User自己的initBinder
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinderDemoUser(ServletRequestDataBinder binder) {
	}

	
	@MethodName(name = "修改appConfig数据")
	@RequestMapping("/modify")
	@ResponseBody
	public JsonResult modify(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppConfig.class);
		JsonResult res = new JsonResult();
		String postdata = request.getParameter("postdata");
		String[] split = postdata.replace("#", "&").substring(1, postdata.length()-1).split("!@");
		for(int i=0;i<split.length;i++){
			if("configType".equals(split[i].split("!!=")[0])){
				if(split[i].split("!!=")[1]!=null){
					filter.addFilter("typekey=", split[i].split("!!=")[1]);
					List<AppConfig> list = super.find(filter);
					for (AppConfig con : list) {
						String confgiKey = con.getConfigkey();
						//System.out.println(confgiKey);
						if (null != con.getConfigkey() && !"".equals(confgiKey)) {
							for(int j=0;j<split.length;j++){
								String[] keysplit = split[j].split("!!=");
								if(keysplit[0].equals(confgiKey)){
									String key = "";
									if("write_proposal".equals(confgiKey)){
										key = split[j].split("!!=")[1].replace("^", "+");
									}else{
										key = split[j].split("!!=")[1];
									}
									//如果从页面上获取的值与数据库中获取的值不相等，就更新下数据库里的值
									if (null != key && !"".equals(key)) {
										if(!key.equals(con.getValue())){
											//数据库里原来的值
											String beforeValue=con.getValue();
											//修改后的值
											con.setValue(key);
											res = super.update(con);
											
											//当闭盘时间和闭市时间有修改时，调用修改的方法
											if(confgiKey.equals("openAndclosePlateTime")){
												
												String[] before=beforeValue.split(",");
												String[] after=key.split(",");
											    if(!before[after.length-1].equals(after[after.length-1])){
											    	RemoteAppReportSettlementService remoteAppReportSettlementService=(RemoteAppReportSettlementService)ContextUtil.getBean("remoteAppReportSettlementService");
													remoteAppReportSettlementService.changeClosePlateTime(key);
													
												}
												
											}if(confgiKey.equals("closeTime")){
												RemoteAppReportSettlementService remoteAppReportSettlementService=(RemoteAppReportSettlementService)ContextUtil.getBean("remoteAppReportSettlementService");
												remoteAppReportSettlementService.changeCloseTime(key);;
												
											}
										}
										
									}
								}
							}
						}

					}
					//修改完后更新缓存
					Map<String, String> map=new HashMap<String,String>();
					map.put("typekey", split[i].split("=")[1]);
					List<AppConfig> l =((AppConfigService) service).getConfig(map);
					String data=JSON.toJSONString(l);
					redisService.save(StringConstant.CONFIG_CACHE+":"+split[i].split("=")[1], data);
				}
			}
		}
		res.setSuccess(true);
		return res;
	}

	@MethodName(name = "增加appConfig数据")
	@RequestMapping("/add")
	@ResponseBody
	@MyRequiresPermissions
	public JsonResult add(AppConfig appConfig) {
		return super.save(appConfig);
	}

	@MethodName(name = "删除appConfig数据")
	@RequestMapping("/remove")
	@MyRequiresPermissions
	@ResponseBody
	public JsonResult remove(String[] ids, HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppConfig.class, request);
		// 数组转 字符串
		String id = StringUtils.join(ids, ",");
		if (ids != null) {
			filter.addFilter("Q_t.id_in_Long", "" + id);
			return super.delete(filter);
		}

		return null;
	}

	@MyRequiresPermissions
	@MethodName(name = "加载一条appConfig数据")
	@RequestMapping(value = "/load/{id}", method = RequestMethod.GET)
	@ResponseBody
	public AppConfig load(@PathVariable Long id) {
		AppConfig appConfig = service.get(id);
		return appConfig;
	}

	@MethodName(name = "查看appConfig")
	@RequestMapping("/see")
	@MyRequiresPermissions
	@ResponseBody
	public AppConfig see(Long id) {
		AppConfig appConfig = service.get(id);
		return appConfig;
	}

	@MethodName(name = "加载appConfig数据")
	@RequestMapping("/list")
	@ResponseBody
	@MyRequiresPermissions
	public List<AppConfig> list(HttpServletRequest request) {
		QueryFilter filter = new QueryFilter(AppConfig.class);
		filter.addFilter("ishidden=", "0");
		filter.setOrderby("created asc");
		List<AppConfig> list = super.find(filter);
		ArrayList<AppConfig> AppConfiglist = new ArrayList<AppConfig>();
		for (AppConfig appConfig : list) {
			if(appConfig.getConfigid()!=122&&appConfig.getConfigid()!=123){
				AppConfiglist.add(appConfig);
			}
		}
		return AppConfiglist;
	}

	@MethodName(name = "查询appConfig中的configkey")
	@RequestMapping("/find")
	@ResponseBody
	@MyRequiresPermissions
	public List<AppConfig> find(HttpServletRequest request) {
		return ((AppConfigService) service).findKey();
	}

	@MethodName(name = "查询appConfig中的数据")
	@RequestMapping("/dataByConfigKey")
	@ResponseBody
	public String dataByConfigKey(HttpServletRequest request) {
		String type=request.getParameter("type");
		String data="";
		String key=request.getParameter("key");
		Map<String,String> map=new HashMap<String, String>();
		//如果指定类型，就查询指定类型下的数据，如果没有指定就查询全部数据
		if(type!=null&&!"".equals(type)){
			

			data=redisService.get(StringConstant.CONFIG_CACHE+":"+type);
			JSONArray obj=JSON.parseArray(data);
			for(Object o:obj){
			JSONObject	 oo=JSON.parseObject(o.toString());
			map.put(oo.getString("configkey"), oo.getString("value"));
	
			}
			data=JSON.toJSONString(map);
		}else{
			
			

			data=redisService.get(StringConstant.CONFIG_CACHE+":all");
			if(null!=key&&!"".equals(key)){
			JSONObject object=JSON.parseObject(data);
			if(object.containsKey(key)){
				data=object.getString(key);
			}
				
			}
			
		}
	
		
		
		return data;
	}
	public static void main(String[] args) {
		String a = "a!!=1!@b!!=2!@c!!=12@qq.com!@d!!=4";
		String[] split = a.split("!@");
		for(int i=0;i<split.length;i++){
			String[] b = split[i].split("!!=");
			System.out.println();
		}
	}
}
