/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0
 * @Date:        2015年10月12日 下午3:03:29
 */
package com.mz.web.app.controller;

import com.mz.core.annotation.base.MethodName;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * <p> TODO</p>
 * @author:         Yuan Zhicheng
 * @Date :          2015年10月12日 下午3:03:29
 */
@Controller
@RequestMapping("/")
public class MainController {
    @RequestMapping("/index")
    @MethodName(name = "管理系统首页")
    public String index() {
        return "/index";
    }
}
