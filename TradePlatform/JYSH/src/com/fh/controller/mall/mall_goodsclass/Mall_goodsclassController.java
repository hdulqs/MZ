package com.fh.controller.mall.mall_goodsclass;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.pool.Size;
import oracle.net.aso.e;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.FileUpload;
import com.fh.util.ObjectExcelView;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.PathUtil;
import com.fh.util.Tools;
import com.fh.util.Jurisdiction;
import com.fh.service.mall.mall_goodsclass.Mall_goodsclassService;
import com.google.gson.JsonArray;

/** 
 * 类名称：Mall_goodsclassController
 * 创建人：FH 
 * 创建时间：2017-07-19
 */
@Controller
@RequestMapping(value="/mall_goodsclass")
public class Mall_goodsclassController extends BaseController {
	
	String menuUrl = "mall_goodsclass/list.do"; //菜单地址(权限用)
	@Resource(name="mall_goodsclassService")
	private Mall_goodsclassService mall_goodsclassService;
	
	static Map<Long,String> goodsclassMap = new HashMap<Long,String>();
	
	public Map<Long,String> getGoodsclassMap(){
		
			try {
				PageData pd =new PageData();
				List<PageData> page = mall_goodsclassService.listAll(pd);
				for (int i = 0; i < page.size(); i++) {
					goodsclassMap.put((Long)page.get(i).get("ID"), page.get(i).getString("CLASSNAME"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return goodsclassMap;
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(HttpServletRequest request,@RequestParam(value="ICON_PATH",required = false) MultipartFile file,
			@RequestParam(value = "CLASSNAME", required = false) String CLASSNAME,
			@RequestParam(value = "PARENT_ID", required = false) String PARENT_ID,
			@RequestParam(value = "SON_ID", required = false) String SON_ID,
			@RequestParam(value = "DISPLAY", required = false) String DISPLAY,
			@RequestParam(value = "RECOMMEND", required = false) String RECOMMEND,
			@RequestParam(value = "SEQUENCE", required = false) int SEQUENCE
			) throws Exception{
		logBefore(logger, "新增Mall_goodsclass");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		Date date = new Date();
		try {
			if(null!=file && !file.isEmpty()){
				String fileName = Tools.date2Str(date, "yyyyMMddHHmmss");
				
				String filePath = PathUtil.getClasspath() + Const.FILESCICON;		//文件上传路径
				fileName = FileUpload.fileUp(file, filePath, fileName);				//执行上传
				pd.put("PARENT_ID",0);
				pd.put("LEVEL", 0);
				if(!"".equals(PARENT_ID) && PARENT_ID!=null){
					pd.put("ID", PARENT_ID);
					pd= mall_goodsclassService.findById(pd);
					int level=(Integer)pd.get("LEVEL");
					pd= new PageData();
					pd.put("PARENT_ID",PARENT_ID);
					pd.put("LEVEL", level+1);
				}
				pd.put("ICON_PATH", Const.FILESCICON+fileName);
				pd.put("CLASSNAME", CLASSNAME);
				if(DISPLAY==null){
					pd.put("DISPLAY", 0);
				}else {
					pd.put("DISPLAY", 1);
				}
				if(RECOMMEND==null){
					pd.put("RECOMMEND", 0);
				}else {
					pd.put("RECOMMEND", 1);
				}
				pd.put("SEQUENCE", SEQUENCE);
				mall_goodsclassService.save(pd);
				
				if(!("请选择").equals(SON_ID) && !("").equals(SON_ID) && SON_ID!=null){
					pd.put("SON_ID", SON_ID);
					mall_goodsclassService.editParent_id(pd);
				}
				
				mv.addObject("msg","success");
				mv.setViewName("save_result");
		}else{
			System.out.println("上传失败");
		}
		} catch (Exception e) {
		e.printStackTrace();
	}
		return mv;
}	






	/**
	 * 删除
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除Mall_goodsclass");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			mall_goodsclassService.delete(pd);
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
	public ModelAndView edit(HttpServletRequest request,@RequestParam(value="ICON_PATH",required = false) MultipartFile file,
			@RequestParam(value = "CLASSNAME", required = false) String CLASSNAME,
			@RequestParam(value = "PARENT_ID", required = false) String PARENT_ID,
			@RequestParam(value = "DISPLAY", required = false) String DISPLAY,
			@RequestParam(value = "RECOMMEND", required = false) String RECOMMEND,
			@RequestParam(value = "SEQUENCE", required = false) int SEQUENCE,
			@RequestParam(value = "ID", required = false) int ID
			) throws Exception{
		logBefore(logger, "修改Mall_goodsclass");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Date date = new Date();
		if(null!=file && !file.isEmpty()){
			String fileName = Tools.date2Str(date, "yyyyMMddHHmmss");
			String filePath = PathUtil.getClasspath() + Const.FILESCICON;		//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, fileName);				//执行上传
			pd.put("ICON_PATH", Const.FILESCICON+fileName);
		}
		pd.put("ID", ID);
		pd.put("CLASSNAME", CLASSNAME);
		pd.put("SEQUENCE", SEQUENCE);
		if(!PARENT_ID.equals("无数据")){
			pd.put("PARENT_ID", PARENT_ID);
		}
		if(DISPLAY==null){
			pd.put("DISPLAY", 0);
		}else {
			pd.put("DISPLAY", 1);
		}
		if(RECOMMEND==null){
			pd.put("RECOMMEND", 0);
		}else {
			pd.put("RECOMMEND", 1);
		}
		
		
		mall_goodsclassService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表Mall_goodsclass");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			getGoodsclassMap();
			pd = this.getPageData();
			page.setPd(pd);
			List<PageData>	varList = mall_goodsclassService.list(page);	//列出Mall_goodsclass列表
			mv.setViewName("mall/mall_goodsclass/mall_goodsclass_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
//			mv.addObject("m", getGoodsclassMap());
			mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/ztree")
	public ModelAndView ztree(Page page){
		logBefore(logger, "列表Mall_goodsclass");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			getGoodsclassMap();
			pd = this.getPageData();
			page.setPd(pd);
			page.setShowCount(100000);
			List<PageData>	varList = mall_goodsclassService.ztreelist(page);	//列出Mall_goodsclass列表
			 JSONArray json = new JSONArray();
	         for(int i=0;i<varList.size();i++){
	             JSONObject jo = new JSONObject();
	             jo.put("ADDTIME", varList.get(i).get("ADDTIME").toString());
	             jo.put("name", varList.get(i).get("name").toString());
	             jo.put("DISPLAY", varList.get(i).get("DISPLAY"));
	             jo.put("LEVEL", varList.get(i).get("LEVEL"));
	             jo.put("RECOMMEND", varList.get(i).get("RECOMMEND"));
	             jo.put("SEQUENCE", varList.get(i).get("SEQUENCE"));
	             jo.put("pId", varList.get(i).get("pId"));
	             jo.put("ICON_PATH", varList.get(i).get("ICON_PATH"));
	             jo.put("id", varList.get(i).get("id"));
	             json.add(i, jo);
	         }
	         
			mv.setViewName("mall/mall_goodsclass/mall_goodsclass_ztree");
			mv.addObject("varList", json);
			mv.addObject("pd", pd);
//			mv.addObject("m", getGoodsclassMap());
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
		logBefore(logger, "去新增Mall_goodsclass页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			if(!pd.getString("ID").equals("undefined")){
				pd = mall_goodsclassService.findById(pd);
				String sele="<option value='"+pd.get("ID")+"'selected='selected'>"+pd.getString("CLASSNAME")+"</option>"; 
				mv.setViewName("mall/mall_goodsclass/mall_goodsclass_zladd");
				mv.addObject("option", sele);
			}else {
				mv.setViewName("mall/mall_goodsclass/mall_goodsclass_add");
			}
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
		logBefore(logger, "去修改Mall_goodsclass页面");
		ModelAndView mv = this.getModelAndView();
		StringBuffer shang = new StringBuffer();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = mall_goodsclassService.findById(pd);	//根据ID读取自己
			mv.setViewName("mall/mall_goodsclass/mall_goodsclass_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
			pd.put("LEVEL", (Integer)pd.get("LEVEL")-1);
			List<PageData> pdg=mall_goodsclassService.fenleilistAll(pd);
			if (pdg.size()>0) {
				for (int i = 0; i < pdg.size(); i++) {
					if(pdg.get(i).get("ID").toString().equals(pd.get("PARENT_ID").toString())){
						shang.append("<option value='"+pdg.get(i).get("ID")+"'selected='selected'>"+pdg.get(i).getString("CLASSNAME")+"</option>"); 
					}else{
						shang.append("<option value='"+pdg.get(i).get("ID")+"'>"+pdg.get(i).getString("CLASSNAME")+"</option>"); 
					}
				}
			}else {
				shang.append("<option>无数据</option>");
			}
			mv.addObject("shang", shang);
			
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
		logBefore(logger, "批量删除Mall_goodsclass");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "dell")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				mall_goodsclassService.deleteAll(ArrayDATA_IDS);
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
	
	
	/**
	 * 下拉选项列表
	 */
	@RequestMapping(value="/optionList")
	public void optionList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		logBefore(logger, "下拉列表Mall_goodsclass");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		StringBuffer sbf=new StringBuffer();
		sbf.append("<option>请选择</option>");
		try{
			List<PageData>	varList = mall_goodsclassService.fenleilistAll(pd);	//查询所有分类信息
			if (!varList.isEmpty()) {
				for (int i = 0; i < varList.size(); i++) {
					sbf.append("<option value='"+varList.get(i).get("ID")+"'>"+varList.get(i).getString("CLASSNAME")+"</option>"); 
				}
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		response.getWriter().write(sbf.toString());
	}
	

	
	/**
	 * 选择上级选项触发下级下拉选项
	 */
	@RequestMapping(value="/SselectList")
	public void SselectList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		logBefore(logger, "选择上级选项触发下级下拉选项Mall_goodsclass");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		pd= mall_goodsclassService.findById(pd);	//查询分类级别
		StringBuffer sbf=new StringBuffer();
		if("1".equals(pd.get("LEVEL").toString())){
			sbf.append("<option>不可选</option>");
		}else {
				List<PageData>	levl1 = mall_goodsclassService.level1listAll(pd);	//查询等级一的数据
				if(levl1 !=null && levl1.size()>0){
					StringBuffer DATA_ID=new StringBuffer();
					for (int i = 0; i < levl1.size(); i++) {
						DATA_ID.append(levl1.get(i).get("ID").toString());
						DATA_ID.append(",");
					}
					String DATA_IDS=DATA_ID.toString();
					DATA_IDS = DATA_IDS.substring(0,DATA_IDS.length()-1);
					
					String ArrayDATA_IDS[] = DATA_IDS.split(",");
					List<PageData>	varList=mall_goodsclassService.DATA_IDSlistAll(ArrayDATA_IDS);//查询所有分类信息
					
					if (!varList.isEmpty()) {
						sbf.append("<option>请选择</option>");
						for (int i = 0; i < varList.size(); i++) {
							sbf.append("<option value='"+varList.get(i).get("ID")+"'>"+varList.get(i).getString("CLASSNAME")+"</option>"); 
						}
					}else {
						sbf.append("<option>无数据</option>");
					}
				}else {
					sbf.append("<option>无数据</option>");
				}
		}
		response.getWriter().write(sbf.toString());
	}

	
	
	/*
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, "导出Mall_goodsclass到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("添加日期");	//1
			titles.add("分类名称");	//2
			titles.add("是否显示");	//3
			titles.add("级别");	//4
			titles.add("是否推荐");	//5
			titles.add("序号");	//6
			titles.add("父ID");	//7
			titles.add("图标地址");	//8
			dataMap.put("titles", titles);
			List<PageData> varOList = mall_goodsclassService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("ADDTIME"));	//1
				vpd.put("var2", varOList.get(i).getString("CLASSNAME"));	//2
				vpd.put("var3", varOList.get(i).get("DISPLAY").toString());	//3
				vpd.put("var4", varOList.get(i).get("LEVEL").toString());	//4
				vpd.put("var5", varOList.get(i).get("RECOMMEND").toString());	//5
				vpd.put("var6", varOList.get(i).get("SEQUENCE").toString());	//6
				vpd.put("var7", varOList.get(i).get("PARENT_ID").toString());	//7
				vpd.put("var8", varOList.get(i).getString("ICON_PATH"));	//8
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
