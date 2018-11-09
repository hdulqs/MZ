package com.fh.controller.systtyc.app_bank_card;

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
import com.fh.service.systtyc.app_bank_card.App_bank_cardService;

/** 
 * 类名称：App_bank_cardController
 * 创建人：FH 
 * 创建时间：2018-06-26
 */
@Controller
@RequestMapping(value="/app_bank_card")
public class App_bank_cardController extends BaseController {
	
	String menuUrl = "app_bank_card/list.do"; //菜单地址(权限用)
	@Resource(name="app_bank_cardService")
	private App_bank_cardService app_bank_cardService;
	
	/**
	 * 新增
	 *//*
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增App_bank_card");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("APP_BANK_CARD_ID", this.get32UUID());	//主键
		app_bank_cardService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	*//**
	 * 删除
	 *//*
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除App_bank_card");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			app_bank_cardService.delete(pd);
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
		logBefore(logger, "修改App_bank_card");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		app_bank_cardService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	*/
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page,HttpServletRequest request){
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User)session.getAttribute(Const.SESSION_USER);
		String bz = user.getBZ();
		logBefore(logger, "列表App_bank_card");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd.put("BZ", bz);
			page.setPd(pd);
			List<PageData>	varList = app_bank_cardService.list(page);	//列出App_bank_card列表
			mv.setViewName("systtyc/app_bank_card/app_bank_card_list");
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
		logBefore(logger, "去新增App_bank_card页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("systtyc/app_bank_card/app_bank_card_edit");
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
		logBefore(logger, "去修改App_bank_card页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = app_bank_cardService.findById(pd);	//根据ID读取
			mv.setViewName("systtyc/app_bank_card/app_bank_card_edit");
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
		logBefore(logger, "批量删除App_bank_card");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				app_bank_cardService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, "导出App_bank_card到excel");
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
			titles.add("用户手机号");	//1
			titles.add("姓氏");	//2
			titles.add("姓名");	//3
			titles.add("开户省份");	//4
			titles.add("开户城市");	//5
			titles.add("开户银行	");	//6
			titles.add("开户支行");	//7
			titles.add("银行卡号");	//8
			titles.add("手机号");	//9
			titles.add("支付宝账号");	//10
			titles.add("支付宝收款码");	//11
			titles.add("微信账号");	//12
			titles.add("微信收款码");	//13
			dataMap.put("titles", titles);
			List<PageData> varOList = app_bank_cardService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("USERNAME"));	//1
				vpd.put("var2", varOList.get(i).getString("SURNAME"));	//2
				vpd.put("var3", varOList.get(i).getString("TRUENAME"));	//3
				vpd.put("var4", varOList.get(i).getString("BANKPROVINCE"));	//4
				vpd.put("var5", varOList.get(i).getString("BANKADDRESS"));	//5
				vpd.put("var6", varOList.get(i).getString("CARDBANK"));	//6
				vpd.put("var7", varOList.get(i).getString("SUBBANK"));	//7
				vpd.put("var8", varOList.get(i).getString("CARDNUMBER"));	//8
				vpd.put("var9", varOList.get(i).getString("MOBILE"));	//9
				vpd.put("var10", varOList.get(i).getString("ALIPAY"));	//10
				vpd.put("var11", varOList.get(i).getString("ALIPAYPICTURE"));	//11
				vpd.put("var12", varOList.get(i).getString("WECHAT"));	//12
				vpd.put("var13", varOList.get(i).getString("WECHATPICTURE"));	//13
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
