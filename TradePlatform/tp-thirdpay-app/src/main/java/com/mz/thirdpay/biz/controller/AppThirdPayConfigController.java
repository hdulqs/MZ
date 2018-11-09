/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Zhang Xiaofang
 * @version: V1.0
 * @Date: 2016年7月12日 下午4:33:25
 */
package com.mz.thirdpay.biz.controller;

import com.alibaba.fastjson.JSON;
import com.mz.core.annotation.NoLogin;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.thirdpay.AppThirdPayConfig;
import com.mz.thirdpay.biz.service.AppThirdPayConfigService;
import com.mz.util.QueryFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> TODO</p>
 *
 * @author: Zhang Xiaofang
 * @Date :          2016年7月12日 下午4:33:25
 */

@Controller
@RequestMapping("/pay/appthirdpayconfig")
public class AppThirdPayConfigController {

  @Resource
  private AppThirdPayConfigService appThirdPayConfigService;

  @RequestMapping("/select")
  @MethodName(name = "查询所有第三方支付的数据")
  @NoLogin
  public List<AppThirdPayConfig> select() {
    List<AppThirdPayConfig> list = appThirdPayConfigService.findAll();
    return list;
  }

  @ResponseBody
  @RequestMapping("/type")
  @MethodName(name = "查询所有第三方支付的名字")
  public List<AppThirdPayConfig> type() {
    List<AppThirdPayConfig> list = appThirdPayConfigService.findType();
    return list;
  }


  @ResponseBody
  @RequestMapping("/current")
  @MethodName(name = "查询当前第三方支付")
  public AppThirdPayConfig current() {
    QueryFilter queryFilter = new QueryFilter(AppThirdPayConfig.class);
    queryFilter.addFilter("currentThird=", 1);
    AppThirdPayConfig appThirdPayConfig = appThirdPayConfigService.get(queryFilter);
    return appThirdPayConfig;
  }


  @ResponseBody
  @RequestMapping("/modify")
  @MethodName(name = "修改当前第三方支付")
  public AppThirdPayConfig modify(HttpServletRequest request) {
    //修改当前第三方
    QueryFilter query = new QueryFilter(AppThirdPayConfig.class);
    query.addFilter("currentThird=", "1");
    AppThirdPayConfig config = appThirdPayConfigService.get(query);

    String thirdPay = request.getParameter("thirdPayConfig");
    QueryFilter queryFilter = new QueryFilter(AppThirdPayConfig.class);
    queryFilter.addFilter("thirdPayConfig=", thirdPay);
    AppThirdPayConfig appThirdPayConfig = appThirdPayConfigService.get(queryFilter);
    appThirdPayConfig.setCurrentThird("1");
    String path = AppThirdPayConfigController.class.getClassLoader().getResource("").getPath();
    String pathSub = path.substring(1, path.length());
    System.out.println(pathSub);
    //修改配置文件
    String environment = request.getParameter("environment");
    //当选择的支付环境与查询出来的支付环境不一致时 进行切换
    if (!environment.equals(appThirdPayConfig.getThirdPayEnvironment())) {
      String currentType = appThirdPayConfig.getPropertiesURL();

      if (null != currentType && !"".equals(currentType)) {
        try {
          //读取原来配置文件到beforeP
          InputStream beforeIn = this.getClass().getClassLoader()
              .getResourceAsStream(currentType);
          Properties beforeP = new Properties();
          beforeP.load(beforeIn);
          beforeIn.close();

          //把原来的配置文件放到中间文件里  beforeP--->middleP
          FileOutputStream middleF = new FileOutputStream(
              pathSub + "thirdpayConfig/middle/middle.properties");
          beforeP.store(middleF, "Comment");
          middleF.close();

          //读取另一个文件的内容
          InputStream another = this.getClass().getClassLoader()
              .getResourceAsStream("/" + appThirdPayConfig.getTestPropertiesURL());

          Properties anotherP = new Properties();
          anotherP.load(another);
          another.close();

          //把另一个文件放到原来的文件中  anotherP---->beforeP
          FileOutputStream currentF = new FileOutputStream(
              pathSub + appThirdPayConfig.getPropertiesURL());
          anotherP.store(currentF, "Comment");
          currentF.close();

          //读取中间的文件
          InputStream middleIn = this.getClass().getClassLoader()
              .getResourceAsStream("/thirdpayConfig/middle/middle.properties");
          Properties middleP = new Properties();
          middleP.load(middleIn);
          middleIn.close();

          //把中间的文件放到另一个文件   middleP---->anotherP
          FileOutputStream anotherNow = new FileOutputStream(
              pathSub + appThirdPayConfig.getTestPropertiesURL());
          middleP.store(anotherNow, "Comment");
          anotherNow.close();

        } catch (IOException e) {

          // TODO Auto-generated catch block
          e.printStackTrace();
        }


      }
    }
    config.setCurrentThird("0");
    appThirdPayConfigService.update(config);
    appThirdPayConfig.setThirdPayEnvironment(environment);
    appThirdPayConfigService.update(appThirdPayConfig);

    return appThirdPayConfig;
  }


  @MethodName(name = "查询appConfig中的configkey")
  @RequestMapping("/getInfo")
  @ResponseBody
  public Properties getInfo(HttpServletRequest request) {
    String currentType = request.getParameter("currentType");
    Properties p = new Properties();
    if (null != currentType && !"".equals(currentType)) {

      JsonResult jsonResult = new JsonResult();
      InputStream in = this.getClass().getClassLoader()
          .getResourceAsStream(currentType);

      try {
        p.load(in);

        Object jString = JSON.toJSON(p);
        jsonResult.setObj(jString);
        jsonResult.setSuccess(true);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }


    }

    return p;
  }
}
