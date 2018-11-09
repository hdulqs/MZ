/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年9月13日 下午5:08:40
 */
package com.mz.web.file.controller;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mz.baidu.ueditor.ActionEnter;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年9月13日 下午5:08:40 
 */
@Controller
@RequestMapping("/baidu")
public class BaiduController {

	
	@RequestMapping("/baidu")
	public void baidu(HttpServletRequest request,HttpServletResponse response){
		
	    try {
			request.setCharacterEncoding( "utf-8" );
			response.setHeader("Content-Type" , "text/html");
			
			String rootPath = request.getRealPath( "/" );
			PrintWriter writer = response.getWriter();
			writer.write( new ActionEnter( request, rootPath ).exec());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
