/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Gao Mimi
 * @version:      V1.0 
 * @Date:        2015年10月12日 下午5:19:56
 */
package com.mz.core.servlet;

import com.mz.core.constant.StringConstant;
import com.mz.util.VerifyCodeUtil;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;  

/**
 * <p> TODO</p>
 * @author:         Gao Mimi 
 * @Date :          2015年10月12日 下午5:19:56 
 */
public class AuthImage extends HttpServlet implements Servlet {  
    static final long serialVersionUID = 1L;  
  
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        response.setContentType("image/jpeg");  
        //生成随机字串  
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);  
        //存入会话session  
        HttpSession session = request.getSession(true);  
        session.setAttribute(StringConstant.CAPTHCHA_RAND, verifyCode.toLowerCase());  
        //生成图片  
        int w = 200, h = 80;  
        VerifyCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode);  
  
    }  
}  