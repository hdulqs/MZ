/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年6月20日 下午5:00:25
 */
package com.mz.web.util;

import com.mz.util.UUIDUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.properties.PropertiesUtils;
import com.mz.util.sys.ContextUtil;
import com.mz.web.file.model.AppFile;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p> TODO</p>
 * @author: Liu Shilei
 * @Date :          2016年6月20日 下午5:00:25 
 */
public class FileUpload {

  //七牛云信息  add by zongwei  20180702
  public static final String accessKey = PropertiesUtils.APP.getProperty("app.qiniuyunaccessKey");
  public static final String secretKey = PropertiesUtils.APP.getProperty("app.qiniuyunsecretKey");
  public static final String http = PropertiesUtils.APP.getProperty("app.qiniuyunhttp");
  public static final String fileprefix = PropertiesUtils.APP.getProperty("app.qiniuyunfileprefix");


  /**
   * 生成文件存储路径,,生成方式为2层hash打散生成文件夹
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param fileName
   * @param:    @return
   * @return: String
   * @Date :          2016年6月20日 下午5:35:27
   * @throws:
   */
  public static void createFileSavePath(FileInfo fileInfo) {
    //获取web项目根路径
    String realPath = ContextUtil.getRequest().getRealPath("/");
    //生成hryfile路径
    String rootPath = realPath
        .substring(0, StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2) + 1);
    LogFactory.info(rootPath);
    //根据文件名哈希2级打散文件路径
    int hashcode = fileInfo.getFileTrueName().hashCode();
    String dir1 = Integer.toHexString(hashcode & 0xf);
    String dir2 = Integer.toHexString(hashcode >>> 4 & 0xf);
    String hashPath = "hryfile" + File.separator + dir1 + File.separator + dir2;
    String path = rootPath + hashPath;
    //设置hash路径为web访问路径
    fileInfo.setFileWebPath(hashPath + "\\" + fileInfo.getFileTrueName());
    System.out.println(fileInfo.getFileWebPath());
    fileInfo.setFileWebPath(fileInfo.getFileWebPath().replaceAll("\\\\", "/"));
    fileInfo.setFileLocalPath(path + File.separator + fileInfo.getFileTrueName());
    //创建目录
    File dirFile = new File(path);
    dirFile.mkdirs();
  }

  /**
   * 保存文件
   * <p> TODO</p>
   * @author: Liu Shilei
   * @param:    @param file
   * @return: void
   * @Date :          2016年6月21日 下午7:32:25
   * @throws:
   */
  public static FileInfo saveFile(MultipartFile file) {
    try {
      //生成本地存储文件名称
      String fileName = UUIDUtil.getUUID() + "." + file.getOriginalFilename().split("\\.")[1];
      FileInfo fileInfo = new FileInfo();
      fileInfo.setFileTrueName(fileName);
      //生成本地存储路径
      FileUpload.createFileSavePath(fileInfo);
      //保存文件
      File uploadFile = new File(fileInfo.getFileLocalPath());
      file.transferTo(uploadFile);// 保存到一个目标文件中。
      //文件上传七牛云
      fileInfo.setFileRemotePath(fileprefix + fileInfo.getFileTrueName());
      //
      Boolean flag = QiniuFileUpload(fileInfo.getFileLocalPath(), fileInfo.getFileRemotePath());
      if (!flag) {
        return null;
      }
      fileInfo.setFileRemotePath(http + fileInfo.getFileRemotePath());
      //返回文件信息对象
      fileInfo.setFileWebPath(fileInfo.getFileRemotePath());
      fileInfo.setFileName(file.getOriginalFilename());
      fileInfo.setFilesize(file.getSize());
      fileInfo.setExtendname(file.getContentType());
      return fileInfo;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public static void download(HttpServletResponse response, AppFile appFile) {
    InputStream in = null;
    OutputStream os = null;
    try {
      // 设置2个头
      response.addHeader("content-type", "image/jpeg");
      response.addHeader("content-disposition", "attachment;filename=dmv.jpg");
      // 创建一个流
      in = new FileInputStream(appFile.getFileLocalpath());
      // 设置文件以附件的形式打开.
      response.setHeader("Content-Disposition",
          "attachment;filename=" + URLEncoder.encode(appFile.getFilename(), "UTF-8"));
      // 输入流 和 输出流
      os = response.getOutputStream();
      int len = 0;
      byte[] b = new byte[1024];
      while ((len = in.read(b)) != -1) {
        os.write(b, 0, len);
      }
      os.close();
      in.close();

    } catch (Exception e) {
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (os != null) {
          os.close();
        }
      } catch (Exception e2) {

      }
    }
  }


  public static boolean QiniuFileUpload(String path, String newFileName) {

    Configuration cfg = new Configuration(Zone.autoZone());
    // ...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    // ...生成上传凭证，然后准备上传
    String bucket = fileprefix;
    // 默认不指定key的情况下，以文件内容的hash值作为文件名
    //String key = null;
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);

    try {
      Response respons = uploadManager.put(path, newFileName, upToken);
      if (respons.statusCode == 200) {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
    // 上传到七牛后删除临时保存到项目的文件
    File delFile = new File(path);
    //删除本地图片
    delFile.delete();
    return false;
  }

}
