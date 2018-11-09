package com.fh.controller.systtyc.ex_dm_customer_publickey;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.fh.service.systtyc.ex_dm_customer_publickey.Ex_dm_customer_publickeyService;

/** 
 * 类名称：Ex_dm_customer_publickeyController
 * 创建人：FH 
 * 创建时间：2018-06-26
 */
@Controller
@RequestMapping(value="/ex_dm_customer_publickey")
public class Ex_dm_customer_publickeyController extends BaseController {
	
	String menuUrl = "ex_dm_customer_publickey/list.do"; //菜单地址(权限用)
	@Resource(name="ex_dm_customer_publickeyService")
	private Ex_dm_customer_publickeyService ex_dm_customer_publickeyService;
	
	/**
	 * 新增
	 */
	/*@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增Ex_dm_customer_publickey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EX_DM_CUSTOMER_PUBLICKEY_ID", this.get32UUID());	//主键
		ex_dm_customer_publickeyService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	*//**
	 * 删除
	 *//*
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除Ex_dm_customer_publickey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			ex_dm_customer_publickeyService.delete(pd);
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
		logBefore(logger, "修改Ex_dm_customer_publickey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		ex_dm_customer_publickeyService.edit(pd);
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
		logBefore(logger, "列表Ex_dm_customer_publickey");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd.put("BZ", bz);
			page.setPd(pd);
			List<PageData>	varList = ex_dm_customer_publickeyService.list(page);	//列出Ex_dm_customer_publickey列表
			mv.setViewName("systtyc/ex_dm_customer_publickey/ex_dm_customer_publickey_list");
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
		logBefore(logger, "去新增Ex_dm_customer_publickey页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("systtyc/ex_dm_customer_publickey/ex_dm_customer_publickey_edit");
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
		logBefore(logger, "去修改Ex_dm_customer_publickey页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = ex_dm_customer_publickeyService.findById(pd);	//根据ID读取
			mv.setViewName("systtyc/ex_dm_customer_publickey/ex_dm_customer_publickey_edit");
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
		logBefore(logger, "批量删除Ex_dm_customer_publickey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				ex_dm_customer_publickeyService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, "导出Ex_dm_customer_publickey到excel");
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
			titles.add("用户账号");	//1
			titles.add("姓氏");	//2
			titles.add("名字");	//3
			titles.add("币种名称");	//4
			titles.add("取币地址");	//5
			titles.add("地址备注");	//6
			dataMap.put("titles", titles);
			List<PageData> varOList = ex_dm_customer_publickeyService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("PUBLICKEYNAME"));	//1
				vpd.put("var2", varOList.get(i).getString("SURNAME"));	//2
				vpd.put("var3", varOList.get(i).getString("TRUENAME"));	//3
				vpd.put("var4", varOList.get(i).getString("CURRENCYTYPE"));	//4
				vpd.put("var5", varOList.get(i).getString("PUBLICKEY"));	//5
				vpd.put("var6", varOList.get(i).getString("REMARK"));	//6
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
