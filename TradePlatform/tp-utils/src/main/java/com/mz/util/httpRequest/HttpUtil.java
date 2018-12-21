package com.mz.util.httpRequest;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
    /**
     * 获取主站访问地址
     * @param request
     * @return
     */
    public static String getAppUrl(HttpServletRequest request) {
        String property = request.getRequestURL().toString();
        return property.substring(0, property.indexOf(request.getRequestURI()));
    }
}
