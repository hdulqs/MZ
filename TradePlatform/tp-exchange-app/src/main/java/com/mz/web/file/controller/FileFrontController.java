package com.mz.web.file.controller;

import com.mz.core.annotation.NoLogin;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.util.QueryFilter;
import com.mz.util.file.FileUtil;
import com.mz.util.sys.ContextUtil;
import com.mz.web.file.model.AppFile;
import com.mz.web.util.FileInfo;
import com.mz.web.util.FileUpload;
import com.mz.web.file.service.AppFileService;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;


/**
 * 上传文件
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年12月8日 下午5:08:18
 */
@Controller
@RequestMapping("/filefront")
public class FileFrontController {

//	System.out.println(file.getContentType());// 获取文件MIME类型
//	System.out.println(file.getName());// 获取表单中文件组件的名字
//	System.out.println(file.getOriginalFilename());// 获取上传文件的原名
//	System.out.println(file.getSize());// 获取文件的字节大小，单位byte

  @Resource
  private AppFileService appFileService;

  /**
   * 上传临时文件
   */
  @RequestMapping("/upload")
  @ResponseBody
  @NoLogin
  public JsonResult upload(DefaultMultipartHttpServletRequest multipartRequest, HttpSession session,
      String otherString) {

    JsonResult json = new JsonResult();
    json.setSuccess(true);
    List<FileInfo> listInfo = new ArrayList<FileInfo>();

    if (multipartRequest != null) {
      //得到文件名称迭代器
      Iterator<String> iterator = multipartRequest.getFileNames();
      //遍历
      while (iterator.hasNext()) {
        //获得文件
        MultipartFile file = multipartRequest.getFile(iterator.next());
        //判断文件是否为空
        if (!file.isEmpty()) {
          try {

            FileInfo fileInfo = FileUpload.saveFile(file);
            listInfo.add(fileInfo);

          } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getLocalizedMessage());
            e.printStackTrace();
          }
        }
      }
    }

    json.setObj(listInfo);

    return json;
  }


  /**
   * 下载文件
   */
  @RequestMapping("/download")
  public void download(HttpServletRequest request, HttpServletResponse response) {

    String fileWebPath = request.getParameter("fileWebPath");
    if (!StringUtils.isEmpty(fileWebPath)) {
      QueryFilter filter = new QueryFilter(AppFile.class);
      filter.addFilter("fileWebPath=", fileWebPath);
      AppFile appFile = appFileService.get(filter);
      FileUpload.download(response, appFile);
    }
  }


  /**
   * 校验文件是否存在
   */
  @RequestMapping("/checkfile")
  @ResponseBody
  public JsonResult checkfile(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String fileWebPath = request.getParameter("fileWebPath");
    if (!StringUtils.isEmpty(fileWebPath)) {
      QueryFilter filter = new QueryFilter(AppFile.class);
      filter.addFilter("fileWebPath=", fileWebPath);
      AppFile appFile = appFileService.get(filter);
      if (appFile != null) {
        jsonResult.setSuccess(true);
      }
    } else {
      jsonResult.setMsg("文件不存在");
    }

    return jsonResult;
  }


  /**
   * 校验文件是否存在
   */
  @RequestMapping("/delete")
  @ResponseBody
  public JsonResult delete(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String fileWebPath = request.getParameter("fileWebPath");
    if (!StringUtils.isEmpty(fileWebPath)) {
      QueryFilter filter = new QueryFilter(AppFile.class);
      filter.addFilter("fileWebPath=", fileWebPath);
      AppFile appFile = appFileService.get(filter);
      if (appFile != null) {
        FileUtil.deleteFile(appFile.getFileLocalpath());
        appFileService.delete(appFile.getFileid());
        jsonResult.setSuccess(true);
        jsonResult.setMsg("删除成功");
      }
    } else {
      jsonResult.setMsg("文件路径不能为空");
    }
    return jsonResult;
  }


  /**
   * 前台删除文件方法
   */
  @RequestMapping("/deleteFront")
  @ResponseBody
  public JsonResult deleteFront(HttpServletRequest request) {
    JsonResult jsonResult = new JsonResult();
    String fileWebPath = request.getParameter("fileWebPath");
    if (!StringUtils.isEmpty(fileWebPath)) {
      //获取web项目根路径
      String realPath = ContextUtil.getRequest().getRealPath("/");
      //生成hryfile路径
      String rootPath = realPath
          .substring(0, StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2) + 1);
      boolean flag = FileUtil.deleteFile(rootPath + fileWebPath);
      if (flag) {
        jsonResult.setMsg("删除成功");
      } else {
        jsonResult.setMsg("删除失败");
      }
      jsonResult.setSuccess(flag);
    } else {
      jsonResult.setSuccess(true);
      jsonResult.setMsg("首次上传");
    }
    return jsonResult;
  }
}
