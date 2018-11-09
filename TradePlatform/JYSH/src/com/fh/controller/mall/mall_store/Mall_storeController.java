package com.fh.controller.mall.mall_store;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

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
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.Jurisdiction;
import com.fh.service.mall.mall_store.Mall_storeService;

/** 
 * 类名称：StoreController
 * 创建人：FH 
 * 创建时间：2017-07-15
 */
@Controller
@RequestMapping(value="/mall_store")
public class Mall_storeController extends BaseController {
	
	String menuUrl = "mall_store/list.do"; //菜单地址(权限用)
	@Resource(name="mall_storeService")
	private Mall_storeService mall_storeService;
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增Store");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mall_storeService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除Store");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			mall_storeService.delete(pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, "修改Store");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mall_storeService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	
	/**
	 * 修改店铺推荐状态
	 */
	@RequestMapping(value="/editRecommend")
	@ResponseBody
	public JSONObject editRecommend() throws Exception{
		logBefore(logger, "修改店铺推荐状态");
		JSONObject json = new JSONObject();
		PageData pd = new PageData();
		pd = this.getPageData();
		String a=pd.getString("STORE_RECOMMEND");
		pd.put("STORE_RECOMMEND", Integer.valueOf(a));
		try {
			mall_storeService.editRecommend(pd);
			json.put("ok", "true");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("ok", "false");
			json.put("msg", "错误！请联系管理员。");
		}
		return json;
	}
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表Store");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			page.setPd(pd);
			List<PageData>	varList = mall_storeService.list(page);	//列出Store列表
			mv.setViewName("mall/mall_store/mall_store_list");
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
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd(){
		logBefore(logger, "去新增Store页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("mall/mall_store/mall_store_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit(){
		logBefore(logger, "去修改Store页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = mall_storeService.findById(pd);	//根据ID读取
			mv.setViewName("mall/mall_store/mall_store_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除Store");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				mall_storeService.deleteAll(ArrayDATA_IDS);
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
	}
	
	/*
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, "导出Store到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("创建时间");	//1
			titles.add("店铺地址");	//2
			titles.add("店铺描述");	//3
			titles.add("店铺名称");	//4
			titles.add("店主姓名");	//5
			titles.add("店主银行卡");	//6
			titles.add("店铺QQ");	//7
			titles.add("是否推荐");	//8
			titles.add("SEO");	//9
			titles.add("SEO");	//10
			titles.add("店铺状态");	//11
			titles.add("店铺电话");	//12
			titles.add("店铺邮编");	//13
			titles.add("店铺模板");	//14
			titles.add("省份ID");	//15
			titles.add("市ID");	//16
			titles.add("区ID");	//17
			titles.add("店铺类型");	//18
			titles.add("店铺LOGO");	//19
			titles.add("收藏人数");	//20
			titles.add("店铺纬度");	//21
			titles.add("店铺经度");	//22
			titles.add("店铺旺旺");	//23
			titles.add("地图类型");	//24
			titles.add("合同开始日期");	//25
			titles.add("合同结束日期");	//26
			titles.add("用户ID");	//27
			dataMap.put("titles", titles);
			List<PageData> varOList = mall_storeService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("ADDTIME"));	//1
				vpd.put("var2", varOList.get(i).getString("STORE_ADDRESS"));	//2
				vpd.put("var3", varOList.get(i).getString("STORE_INFO"));	//3
				vpd.put("var4", varOList.get(i).getString("STORE_NAME"));	//4
				vpd.put("var5", varOList.get(i).getString("STORE_OWER"));	//5
				vpd.put("var6", varOList.get(i).getString("STORE_OWER_CARD"));	//6
				vpd.put("var7", varOList.get(i).getString("STORE_QQ"));	//7
				vpd.put("var8", varOList.get(i).get("STORE_RECOMMEND").toString());	//8
				vpd.put("var9", varOList.get(i).getString("STORE_SEO_DESCRIPTION"));	//9
				vpd.put("var10", varOList.get(i).getString("STORE_SEO_KEYWORDS"));	//10
				vpd.put("var11", varOList.get(i).get("STORE_STATUS").toString());	//11
				vpd.put("var12", varOList.get(i).getString("STORE_TELEPHONE"));	//12
				vpd.put("var13", varOList.get(i).getString("STORE_ZIP"));	//13
				vpd.put("var14", varOList.get(i).getString("TEMPLATE"));	//14
				vpd.put("var15", varOList.get(i).get("AREA0ID").toString());	//15
				vpd.put("var16", varOList.get(i).get("AREA1ID").toString());	//16
				vpd.put("var17", varOList.get(i).get("AREA2ID").toString());	//17
				vpd.put("var18", varOList.get(i).getString("STORE_TYPE"));	//18
				vpd.put("var19", varOList.get(i).getString("STORE_LOGO"));	//19
				vpd.put("var20", varOList.get(i).get("FAVORITE_COUNT").toString());	//20
				vpd.put("var21", varOList.get(i).getString("STORE_LAT"));	//21
				vpd.put("var22", varOList.get(i).getString("STORE_LNG"));	//22
				vpd.put("var23", varOList.get(i).getString("STORE_WW"));	//23
				vpd.put("var24", varOList.get(i).getString("MAP_TYPE"));	//24
				vpd.put("var25", varOList.get(i).get("DELIVERY_BEGIN_TIME").toString());	//25
				vpd.put("var26", varOList.get(i).getString("DELIVERY_END_TIME"));	//26
				vpd.put("var27", varOList.get(i).get("UID").toString());	//27
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
