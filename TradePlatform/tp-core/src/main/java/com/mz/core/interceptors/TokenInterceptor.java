/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2015年10月27日 下午4:33:53
 */
package com.mz.core.interceptors;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <p> TODO</p>     表单重复提交拦截器
 * @author:         Liu Shilei 
 * @Date :          2015年10月27日 下午4:33:53 
 */
public class TokenInterceptor extends HandlerInterceptorAdapter  {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	response.addHeader("Access-Control-Allow-Origin", "*");
    	
    	if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
             
            String clinetToken = request.getParameter("token");
            //如果页面提交的参数有token 就进行判断
            if (clinetToken!=null&&!"".equals(clinetToken)) {
                if (isRepeatSubmit(request)) {
                	request.getRequestDispatcher("/WEB-INF/view/error/repeatSubmit.jsp").forward(request, response);
                    return false;
                }
                request.getSession().removeAttribute("token");
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }
    
    /**
     * 判断是不是重复提交
     * <p> TODO</p>
     * @author:         Liu Shilei
     * @param:    @param request
     * @param:    @return
     * @return: boolean 
     * @Date :          2015年10月28日 上午10:18:20   
     * @throws:
     */
    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession().getAttribute("token");
        //session 中token 过期  说明页面超时
        if (serverToken == null) {
            return true;
        }
        //request中 token 为null 说明被伪造表单提交了
        String clinetToken = request.getParameter("token");
        if (clinetToken == null) {
            return true;
        }
        //不相等 说明重复提交了或被伪造提交了
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }
	
}


