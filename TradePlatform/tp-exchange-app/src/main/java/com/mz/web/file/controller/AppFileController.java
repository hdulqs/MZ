/**
 * Copyright:  北京互融时代软件有限公司
 *
 * @author: Gao Mimi
 * @version: V1.0
 * @Date: 2015年09月28日  18:10:04
 */
package com.mz.web.file.controller;

import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.core.mvc.service.base.BaseService;
import com.mz.util.QueryFilter;
import com.mz.web.file.model.AppFile;
import com.mz.web.file.service.AppFileService;
import com.mz.web.util.FtpUtils;
import com.mz.core.mvc.controller.base.BaseController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Gao Mimi
 * @Date : 2015年09月28日 18:10:04
 */

@Controller
@RequestMapping("/file/appfile")
public class AppFileController extends BaseController<AppFile, Long> {

  @Resource(name = "appFileService")
  @Override
  public void setService(BaseService<AppFile, Long> service) {
    super.service = service;
  }

  @Resource(name = "appFileService")
  private AppFileService appFileService;

  /**
   * 自己的initBinder
   *
   * @param binder
   */
  @InitBinder
  public void initBinderappFile(ServletRequestDataBinder binder) {

  }

  @MethodName(name = "上传文件")
  @RequestMapping(value = "/upload")
  @ResponseBody
  public String[] upload(@RequestParam(value = "file", required = false) MultipartFile file,
      HttpServletRequest request, ModelMap model, String mark) {
    // JsonResult JsonResult=new JsonResult();
    String isFileFtp = "0";//ContextUtil.getAppConfig("isFileFtp");
    String[] result = null;
    if (isFileFtp.equals("0")) {
      // 上传到本地
      result = appFileService.uploadLocal(file, mark, "aa\\bb");

    } else if (isFileFtp.equals("1")) {
      // 上传到ftp
      result = appFileService.uploadRemote(file, mark, "aa/bb",
          new FtpUtils().getFtpUtil());

    }

    return result;
  }

  @MethodName(name = "上传")
  @RequestMapping(value = "/download")
  @ResponseBody
  public JsonResult download(HttpServletRequest request, String fileid) {
    JsonResult JsonResult = new JsonResult();
    String isFileFtp = null;//ContextUtil.getAppConfig("isFileFtp");
    // String[] result = null;
    if (isFileFtp.equals("0")) {
      // 从本地下载

    } else if (isFileFtp.equals("1")) {
      // 从ftp下载

    }
    return JsonResult;
  }

  @MethodName(name = "删除")
  @RequestMapping("/delete")
  @ResponseBody
  public String delete(Long id, HttpServletRequest request) {
    appFileService.delete(id);
    return "success";
  }

  @MethodName(name = "列表")
  @RequestMapping("/list")
  @ResponseBody
  public PageResult list(HttpServletRequest request) {
    QueryFilter filter = new QueryFilter(AppFile.class, request);
    filter.setOrderby("created desc");
    return super.findPage(filter);
  }


}
