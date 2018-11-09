/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
package com.mz.core.listener;


import com.mz.core.interceptors.CommonDataInterceptor;
import com.mz.core.listener.BaseListener;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置通用数据的Filter
 * <p/>
 * 使用Filter时 文件上传时 getParameter时为null 所以改成Interceptor
 * <p/>
 * 1、ctx---->request.contextPath
 * 2、currentURL---->当前地址
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015年9月16日 上午11:04:39
 */
public class CommonDataListener extends BaseListener {

    private CommonDataInterceptor setCommonDataInterceptor = new CommonDataInterceptor();

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            //使用Filter时 文件上传时 getParameter时为null 所以改成Interceptor
            setCommonDataInterceptor.preHandle(request, response, null);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        chain.doFilter(request, response);
    }
}
