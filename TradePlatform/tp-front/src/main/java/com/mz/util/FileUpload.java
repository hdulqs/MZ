/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:     	   2016年6月20日 下午5:00:25
 */
package com.mz.util;


import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.mz.util.properties.PropertiesUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;


/**
* @Description:     七牛云以文件方式上传图片
* @Author:         zongwei
* @CreateDate:     2018/7/2 12:21
* @UpdateUser:    zongwei
* @UpdateDate:     2018/7/2 12:21
* @UpdateRemark:   创建
* @Version:        1.0
*/
public class FileUpload {


	//七牛云信息  add by zongwei  20180702
	public static String accessKey=PropertiesUtils.APP.getProperty("app.qiniuyunaccessKey");
	public static String secretKey=PropertiesUtils.APP.getProperty("app.qiniuyunsecretKey");
	public static String http=PropertiesUtils.APP.getProperty("app.qiniuyunhttp");
	public static String fileprefix=PropertiesUtils.APP.getProperty("app.qiniuyunfileprefix");



	/**
	 * 七牛云以文件方式上传图片
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static synchronized String[] POSTFileQiniu(HttpServletRequest request, MultipartFile[] files) {
		//String format=".jpg";//图片类型
		String[] pathImg = new String[files.length];
		//============七牛云================
		// 构造一个带指定Zone对象的配置类   zone0()华东  zone1()华北  zone2()华南 zoneNa0()北美
		Configuration cfg = new Configuration(Zone.zone2());
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		// ...生成上传凭证，然后准备上传
		String bucket = "nncai";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		//String key = null;
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		// 创建一个通用的多部分解析器.
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 设置编码
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		// 判断是否有文件上传
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
			// iter里面没有值
			try {
				if (files != null && files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						MultipartFile file = files[i];
						if (file != null && !file.isEmpty()) {
							String filename = file.getOriginalFilename();
							String newFileName = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
							String realPath = request.getSession().getServletContext().getRealPath("/");
							// 生成hryfile路径
							String rootPath = realPath.substring(0,
									org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf(realPath, File.separator, 2)
											+ 1);
							String path = rootPath  +fileprefix+ File.separator +newFileName;
							newFileName = fileprefix + newFileName;
							File localFile = new File(path);
							// 存入本地
							File secondFolder = new File(rootPath + fileprefix);
							// 存入本地
							if (!secondFolder.exists()) {
								secondFolder.mkdirs();
							}
							//上传图片保存到本地
							file.transferTo(localFile);
							// 上传到七牛
							Response respons = uploadManager.put(path, newFileName, upToken);

							// 上传到七牛后删除临时保存到项目的文件
							File delFile = new File(path);
							//删除本地图片
							delFile.delete();
							System.out.println(respons.bodyString());
							pathImg[i] = http + newFileName;
						} else {
							pathImg[i] = "";
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return pathImg;
		}

		return pathImg;
	}

}
