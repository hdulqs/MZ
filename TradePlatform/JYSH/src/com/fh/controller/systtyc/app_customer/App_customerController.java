package com.fh.controller.systtyc.app_customer;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.system.User;
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.Jurisdiction;
import com.fh.service.systtyc.app_customer.App_customerService;

/** 
 * 类名称：App_customerController
 * 创建人：FH 
 * 创建时间：2018-06-23
 */
@Controller
@RequestMapping(value="/app_customer")
public class App_customerController extends BaseController {
	
	String menuUrl = "app_customer/list.do"; //菜单地址(权限用)
	@Resource(name="app_customerService")
	private App_customerService app_customerService;
	
	/**
	 * 新增
	 */
	/*@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增App_customer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("APP_CUSTOMER_ID", this.get32UUID());	//主键
		app_customerService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	*//**
	 * 删除
	 *//*
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除App_customer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			app_customerService.delete(pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
	}
	
	*//**
	 * 修改
	 *//*
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, "修改App_customer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		app_customerService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}*/
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User)session.getAttribute(Const.SESSION_USER);
		String bz = user.getBZ();
		logBefore(logger, "列表App_customer");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
		
			pd = this.getPageData();
			pd.put("BZ", bz);
			page.setPd(pd);
			List<PageData>	varList = app_customerService.list(page);	//列出App_customer列表
			mv.setViewName("systtyc/app_customer/app_customer_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	/*@RequestMapping(value="/goAdd")
	public ModelAndView goAdd(){
		logBefore(logger, "去新增App_customer页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("systtyc/app_customer/app_customer_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	*//**
	 * 去修改页面
	 *//*
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit(){
		logBefore(logger, "去修改App_customer页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = app_customerService.findById(pd);	//根据ID读取
			mv.setViewName("systtyc/app_customer/app_customer_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	*//**
	 * 批量删除
	 *//*
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除App_customer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				app_customerService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}*/
	
	/*
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, "导出App_customer到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User)session.getAttribute(Const.SESSION_USER);
		String bz = user.getBZ();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("BZ", bz);
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("会员账号");	//1
			titles.add("姓氏");	//2
			titles.add("名字");	//2
			titles.add("证件类型");	//2
			titles.add("证件号");	//2
			titles.add("绑定手机号");	//2
			titles.add("注册时间");	//2
			titles.add("是否开启谷歌认证");	//2
			titles.add("是否开启手机认证");	//2
			titles.add("是否实名");	//2
			titles.add("账户角色");	//2
			titles.add("是否禁用");	//2
			titles.add("是否开启交易");	//2
			titles.add("是否激活");	//2
			dataMap.put("titles", titles);
			List<PageData> varOList = app_customerService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("username"));	//1
				vpd.put("var2", varOList.get(i).getString("surname"));	//1
				vpd.put("var3", varOList.get(i).getString("trueName"));	//1
				if (varOList.get(i).get("cardType").toString().equals("0")) {
					vpd.put("var4", "身份证");
				}else {
					vpd.put("var4","其他" );
				}
				
				vpd.put("var5", varOList.get(i).getString("cardId"));	//1
				vpd.put("var6", varOList.get(i).getString("phone"));	//1
				vpd.put("var7", varOList.get(i).get("created").toString());	//1
				if (varOList.get(i).get("googleState").toString().equals("0")) {
					vpd.put("var8", "未认证");	
				}else {
					vpd.put("var8", "已认证");	
			
				}
				if (varOList.get(i).get("phoneState").toString().equals("0")) {
					vpd.put("var9", "未认证");	
				}else {
					vpd.put("var9", "已认证");	
				}
				//实名认证
				if (varOList.get(i).get("states").toString().equals("0")) {
					vpd.put("var10", "未实名");	
				}else if (varOList.get(i).get("states").toString().equals("1")) {
					vpd.put("var10", "待审核");	
				}else if (varOList.get(i).get("states").toString().equals("2")) {
					vpd.put("var10", "已实名");	
				}else{
					vpd.put("var10", "已拒绝");	
				}
				//账户角色 
				if (varOList.get(i).get("customerType").toString().equals("1")) {
					vpd.put("var11", "普通账户");	
				}else {
					vpd.put("var11", "操盘账户");	
				}
				//是否禁用
				if (varOList.get(i).get("isDelete").toString().equals("1")) {
					vpd.put("var12", "已禁用");	//1
				}else {
					vpd.put("var12", "未禁用");	//1
				}
				//是否开启交易 
				if (varOList.get(i).get("isChange").toString().equals("1")) {
					vpd.put("var13", "未开启");	//1
				}else {
					vpd.put("var13", "已开启");	//1
				}
				//是否激活
				if (varOList.get(i).get("hasEmail").toString().equals("1")) {
					vpd.put("var14", "已激活");	//1
				}else {
					vpd.put("var14", "未激活");	//1
				}
				varList.add(vpd);
		}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/* ===============================权限================================== */
	public Map<String, String> getHC(){
		Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>)session.getAttribute(Const.SESSION_QX);
	}
	/* ===============================权限================================== */
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
