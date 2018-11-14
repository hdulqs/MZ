package com.fh.util.constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import net.sf.json.JSONObject;


/**
 * 七牛云 工具类
 * @author Administrator
 *
 */
public class ToolUtil {
	
	//七牛云信息
	public static String accessKey = "eExtr3e6u7n-ZYOwpvjm04KfB6s334tYp2jSuTdY";
	
	public static String secretKey = "wHY419uHwTxXTNjLhVqg39lhhFOmxBCoBe2pe4mT";
	
	public static String http = "https://cdn.szfysoft.com/";
	//图片类型
	public static String imgypt = ".jpg";
	//图片名称
	public static String imgname = "wang";
	
	//图片临时存放地址
	public static String imgulr = "uploadFiles/"; //图片上传路径
	
	//服务器
	public static final String IMGURL = "http://120.79.20.44:8080/image/";//接口路径
	public static final String TRUEUPLOAD = "trueUpload";//上传图片接口 参数:base64:图片base64  type:系统类型
	public static final String IMGTYPE = ".png";// 图片类型
		/**
	 * 上传图片
	 * @param base64
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getUploadPictures(String base64){			//返回图片路径
        Map<String,String> params = new HashMap<String,String>();//参数
        params.put("base64", base64);  
        params.put("type", "NNC"); 
        String resultPost = HttpRequest.post(ToolUtil.IMGURL+ToolUtil.TRUEUPLOAD, params);//post请求方式  
        //解析json数据
        Map<String, Object> map = JSONObject.fromObject(resultPost);
		return String.valueOf(map.get("url"));
	}

	/**
	 * 七牛云上传
	 * @param base64 图片base64
	 * @param request
	 * @param Tname 类型名称
	 * @return
	 */
	public static String getQiniu(String base64,HttpServletRequest request,String Tname){
		String format=".jpg";//图片类型
		//图片地址
		String name="";
		//============七牛云================
		// 构造一个带指定Zone对象的配置类   zone0()华东  zone1()华北  zone2()华南 zoneNa0()北美
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
        	  //Base64解码  
            byte[] b = decoder.decodeBuffer(base64);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {//调整异常数据  
                    b[i]+=256;  
                }  
            }  
            // 项目临时存储图片文件夹目录
			String path = request.getSession().getServletContext().getRealPath("/")+ "admin\\"+"dd"+format;
            //生成jpeg图片  
            OutputStream out = new FileOutputStream(path);      
            out.write(b);  
            out.flush();  
            out.close();  
            
            //随机生成图片名称
			long log=System.currentTimeMillis();
			StringBuffer buffer = new StringBuffer(Tname);
			buffer.append(log);
			String PictureName = buffer.toString();
			// 上传到七牛
			Response respons = uploadManager.put(path,PictureName, upToken);
			// 上传到七牛后删除临时保存到项目的文件
			File delFile = new File(path);
			delFile.delete();
			System.out.println(respons.bodyString());
			//图片地址
			name=ToolUtil.http+PictureName;
		}catch (Exception e){
			e.printStackTrace();
		}
		return name;
		
	}
	
	public static void main(String[] args) {
		BigDecimal bignum1 = new BigDecimal("1505096.54");
		BigDecimal bignum2 = new BigDecimal("0.2");
		BigDecimal bignum3 = null;
		
		//加法
		bignum3 =  bignum1.add(bignum2); 	 
		System.out.println("加是：" + bignum3);
		
		//减法
		bignum3 = bignum1.subtract(bignum2);
		System.out.println("减  是：" + bignum3);
		
		//乘法
		bignum3 = bignum1.multiply(bignum2);
		System.out.println("乘  是：" + bignum3);
		
		//除法
		bignum3 = bignum1.divide(bignum2);
		System.out.println("除  是：" + bignum3);
	}
	
}
