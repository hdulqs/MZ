/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年11月8日 下午4:11:34
 */
package com.mz.core.mvc.controller.export;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.util.excelUtil.ExcelUtil;
import com.mz.util.file.FileUtil;
import com.mz.util.http.HttpConnectionUtil;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Liu Shilei
 * @Date : 2016年11月8日 下午4:11:34
 */
@Controller
@RequestMapping("/export/exportutil")
public class ExportUtilController {
	
	@MethodName(name = "导出excel...loadding")
	@RequestMapping("/loadding")
	public String loadding(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object>  map = new HashMap<String, Object>();
		
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = StringUtils.trim(request.getParameter(name));
			if(StringUtils.isNotBlank(value)) {
				
				map.put(name, value);
			}
		}
		request.setAttribute("map", map);
		
		return "loadding";
	}
	
	@MethodName(name = "下载excel")
	@RequestMapping("/down")
	@ResponseBody
	public void  down(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath = request.getParameter("excel_filePath");
		String fileName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
		//下载
		FileUtil.download(new File(filePath), fileName, request, response);
		System.out.println("hello"+filePath);
		//下载完删除文件
		//FileUtil.deleteFile(path);
	}
	
	
	@MethodName(name = "导出excel")
	@RequestMapping("/excel")
	@ResponseBody
	public JsonResult excel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JsonResult jsonResult = new JsonResult();
		//表头
		String titles = request.getParameter("excel_titles");
		//显示的列
		String cvs = request.getParameter("excel_cvs");
		//sessionId
		String sessionId = request.getParameter("excel_sessionId");
		//url
		String url = request.getParameter("excel_url");
		//文件名称
		String filename = request.getParameter("excel_filename");
		String[] headers = titles.split(",");
		String[] columns = cvs.split(",");
		
		//获得渲染规则
		//分别为  "参数位置 ":"计算规则:规则明细"
		//组成 1:VALUE_CONVERT:1=甲类,2=乙类,3=丙类,else=
		//或  1:VALUE_OPERATION: transactionMoney-fee
		//规则用  & 连接
		String renderRules = request.getParameter("excel_renderRules");
		HashMap<String, String[]> rulesMap=new HashMap<String, String[]>();
		ArrayList<Integer> noshowLines=new ArrayList<Integer>();//不显示的列
		
		
		if(renderRules!=null && !"".equals(renderRules)){
			if(renderRules.contains("&")){
				String[] rules1=renderRules.split("&");
				for (int i = 0; i < rules1.length; i++) {
					String[] rules2=rules1[i].split(":");
					if(rules2.length==3){
						String[] mmm=new String[2];
						mmm[0]=rules2[1];
						mmm[1]=rules2[2];
						rulesMap.put(rules2[0], mmm);
					}else{
						System.out.println("渲染条件为:"+renderRules+",不符合规则，请重新填写！");
						jsonResult.setMsg("渲染条件为:"+renderRules+",不符合规则，请重新填写！");
						jsonResult.setSuccess(false);
						return jsonResult;
					}
				}
			}else{
				String[] rules2=renderRules.split(":");
				if(rules2.length==3){
					String[] mmm=new String[2];
					mmm[0]=rules2[1];
					mmm[1]=rules2[2];
					rulesMap.put(rules2[0], mmm);
				}else{
					System.out.println("渲染条件为:"+renderRules+",不符合规则，请重新填写！");
					jsonResult.setMsg("渲染条件为:"+renderRules+",不符合规则，请重新填写！");
					jsonResult.setSuccess(false);
					return jsonResult;
				}
			}
		}
		
		String param = "start=0&length=-1&";
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = StringUtils.trim(request.getParameter(name));
			if(!name.contains("excel_")){
				if(StringUtils.isNotBlank(value)) {
					param +=name+"="+value+"&";
				}
			}
		}
		
		// 通过httpClient调用
		// 加上cookie名称，进行免登录
		String _param = param+"msid="+sessionId;
		if(url.contains("?")){//如果url后面传了参数则对参数截取重新拼装
			String urlParam = url.substring(url.indexOf("?")+1);
			System.out.println(url);
			url = url.substring(0,url.indexOf("?"));
			System.out.println(url);
			_param = urlParam + "&" + _param;
		}
		System.out.println(_param);
		String postSend = HttpConnectionUtil.getSend(url, _param);
	//	String postSend = HttpConnectionUtil.postSend(url, _param);

		// 得到结果转换成pageResult
		PageResult pageResult = JSON.parseObject(postSend, PageResult.class);
		List rows = pageResult.getRows();
		// 循环分析对象中是否包含对象属性,如果有则对a.b类型的字段名，进行附值操作
		if(rows!=null&&rows.size()>0){
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> object = (Map<String, Object>) rows.get(i);
				for (int j = 0; j < columns.length; j++) {
					String c=columns[j];
					if (c.contains(".")) {
						String[] split = c.split("\\.");
						if (split.length == 2) {// 一级对象，包对象 a.b类型
							Map<String, Object> son = (Map<String, Object>) object.get(split[0]);
							object.put(c, son.get(split[1]));
						}
						if (split.length == 3) {// 二级对象，包对象 a.b.c类型
							Map<String, Object> son = (Map<String, Object>) object.get(split[0]);
							Map<String, Object> son2 = (Map<String, Object>) son.get(split[1]);
							object.put(c, son2.get(split[2]));
						}
	
					}
					
					//渲染赋值
					if(rulesMap.containsKey(j+"")){
						String[] ruleAndValue=rulesMap.get(j+"");
						String rule=ruleAndValue[0];//规则
						String ruleValue=ruleAndValue[1];//规则详情
						
						Object oldValue=object.get(c);
						if(ExcelUtil.VALUE_CONVERT.equals(rule)){//单纯值的替换
							String value=ExcelUtil.getConvertValue(ruleValue,oldValue);
							object.put(c, value);
						}else if(ExcelUtil.VALUE_OPERATION.equals(rule)){//值得只加或只减运算
							BigDecimal value=ExcelUtil.getOperationValue(ruleValue,object);
							//四舍五入保留三位小数
							double dou= value.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
							object.put(c, dou);
						}else if(ExcelUtil.NOT_SHOW.equals(rule) && !noshowLines.contains(j)){//不显示的列
							noshowLines.add(j);
						}
					}
					
					
					
				}
			}
		}
		try {
			//去掉不显示的列
			if(noshowLines!=null && noshowLines.size()>0){
				headers=removeNoshowLines(headers,noshowLines);
				columns=removeNoshowLines(columns,noshowLines);
			}
			//生成并返回文件路径filename
			String path = com.mz.util.excelUtil.ExcelUtil.exportExcel2(headers, columns, pageResult.getRows(), filename+".xls");
			
			//String substring = path.substring(path.lastIndexOf("/")+1, path.length());
			
			jsonResult.setMsg(path);
			jsonResult.setSuccess(true);
			return jsonResult;
			//下载
			//FileUtil.download(new File(path), filename+".xls", request, response);
			//下载完删除文件
			//FileUtil.deleteFile(path);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return jsonResult;
	}

	
	
	/**
	 * 去掉不显示的列
	 * <p> TODO</p>
	 * @author:         Zhang Lei
	 * @param:    @return
	 * @return: String[] 
	 * @Date :          2017年1月9日 下午5:35:15   
	 * @throws:
	 */
	private String[] removeNoshowLines(String[] strs,ArrayList<Integer> list){
		String[] val=new String[strs.length-list.size()];
		int num=0;
		for (int i = 0; i < strs.length; i++) {
			if (list.contains(i)) {
				
			}else{
				val[num]=strs[i];
				num++;
			}
		}
		return val;
	}
	
	
	
}
