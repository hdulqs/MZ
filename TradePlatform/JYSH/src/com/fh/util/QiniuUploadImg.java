package com.fh.util;

import javax.servlet.http.HttpServletRequest;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.fh.util.constant.ToolUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class QiniuUploadImg {
    /**
     * 七牛云图片上传
     *
     * @param base64  图片base64
     * @param request
     * @return
     */
    public static HashMap<String, String> uploadQiniu(String base64, HttpServletRequest request) {
        HashMap json = new HashMap<String, String>();
        // ============七牛云================
        // 构造一个带指定Zone对象的配置类 zone0()华东 zone1()华北 zone2()华南 zoneNa0()北美
        Configuration cfg = new Configuration(Zone.zone2());
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // ...生成上传凭证，然后准备上传
        String bucket = "nncai";
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(ToolUtil.accessKey, ToolUtil.secretKey);
        String upToken = auth.uploadToken(bucket);

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 项目临时存储图片文件夹目录

            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + ToolUtil.imgulr + ToolUtil.imgname + ToolUtil.imgypt;
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();

            // 随机生成图片名称
            long log = System.currentTimeMillis();
            StringBuffer buffer = new StringBuffer("NNTB");
            buffer.append(log);
            String PictureName = buffer.toString();

            // 上传到七牛
            Response respons = uploadManager.put(path, PictureName, upToken);
            // 上传到七牛后删除临时保存到项目的文件
            File delFile = new File(path);
            delFile.delete();
            json.put("path", ToolUtil.http + PictureName);
            json.put("ok", "true");
            System.out.println(respons.bodyString());

        } catch (Exception e) {
            json.put("ok", "false");
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 七牛云图片修改
     *
     * @param base64
     * @param path
     * @return
     */
    public static HashMap<String, String> editQiniu(String base64, String path, HttpServletRequest request) {
        HashMap json = new HashMap<String, String>();
        long log = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer("NNTB");
        buffer.append(log);
        String PictureName = buffer.toString();
        String iconpath = "";
        // ============七牛云================
        // 构造一个带指定Zone对象的配置类 zone0()华东 zone1()华北 zone2()华南 zoneNa0()北美
        Configuration cfg = new Configuration(Zone.zone2());
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // ...生成上传凭证，然后准备上传
        String bucket = "nncai";
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(ToolUtil.accessKey, ToolUtil.secretKey);
        String upToken = auth.uploadToken(bucket);

        if (base64 == null || base64.isEmpty()) {
            iconpath = path;
        } else {
            // 七牛云上传图片先删除
            BucketManager bucketManager = new BucketManager(auth, cfg);
            try {
                String[] keyList = new String[]{path};
                BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
                batchOperations.addDeleteOp(bucket, keyList);
                Response response1 = bucketManager.batch(batchOperations);
                BatchStatus[] batchStatusList = response1
                        .jsonToObject(BatchStatus[].class);
                for (int i = 0; i < keyList.length; i++) {
                    BatchStatus status = batchStatusList[i];
                    // 200-删除成功 400-请求报文格式错误 401-管理凭证无效 599-服务端操作失败 612-待删除资源不存在
                    if (status.code == 200) {
                        System.out.println("红包设置删除成功！！！" + path);
                    } else {
                        System.out.println(status.toString());
                    }
                }
            } catch (QiniuException ex) {
                System.err.println(ex.response.toString());
            }

            BASE64Decoder decoder = new BASE64Decoder();
            try {
                // Base64解码
                byte[] b = decoder.decodeBuffer(base64);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {// 调整异常数据
                        b[i] += 256;
                    }
                }
                // 项目临时存储图片文件夹目录

                String realpath = request.getSession().getServletContext()
                        .getRealPath("/")
                        + ToolUtil.imgulr + ToolUtil.imgname + ToolUtil.imgypt;
                // 生成jpeg图片
                OutputStream out = new FileOutputStream(realpath);
                out.write(b);
                out.flush();
                out.close();

                // 上传到七牛
                Response respons = uploadManager
                        .put(realpath, PictureName, upToken);
                // 上传到七牛后删除临时保存到项目的文件
                File delFile = new File(realpath);
                delFile.delete();
                System.out.println(respons.bodyString());
                iconpath = ToolUtil.http + PictureName;
            } catch (Exception e) {
                json.put("ok", "false");
                e.printStackTrace();
            }
        }
        json.put("path", iconpath);
        json.put("ok", "true");
        return json;
    }

    /**
     * 七牛云图片删除
     *
     * @param path
     * @return
     */
    public static HashMap<String, String> delQiniu(String path) {
        HashMap json = new HashMap<String, String>();
        // ============七牛云================
        // 构造一个带指定Zone对象的配置类 zone0()华东 zone1()华北 zone2()华南 zoneNa0()北美
        Configuration cfg = new Configuration(Zone.zone2());
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // ...生成上传凭证，然后准备上传
        String bucket = "nncai";
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(ToolUtil.accessKey, ToolUtil.secretKey);

        // 七牛云上传图片先删除
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            String[] keyList = new String[]{path};
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response1 = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response1
                    .jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                // 200-删除成功 400-请求报文格式错误 401-管理凭证无效 599-服务端操作失败 612-待删除资源不存在
                if (status.code == 200) {
                    System.out.println("红包设置图片删除成功！！！" + path);
                } else {
                    System.out.println(status.toString());
                }
            }
        } catch (QiniuException ex) {
            json.put("ok", "false");
            System.err.println(ex.response.toString());
        }

        json.put("ok", "true");
        return json;
    }
}
