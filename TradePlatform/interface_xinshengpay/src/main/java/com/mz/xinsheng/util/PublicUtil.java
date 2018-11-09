package com.mz.xinsheng.util;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class PublicUtil {
	/*
	 * map to String
	 */
	public static String mapToStringAndTrim(SortedMap<String, String> sortedMap){
		StringBuffer sb = new StringBuffer();
		Iterator it = sortedMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String key = entry.getKey().toString().trim();
			if(entry.getValue()==null){
				continue;
			}
			String value = entry.getValue().toString().trim();
			if (!"".equals(value) && value!=null) {
				sb.append(key+"="+value+"&");
			}
		}
		return sb.substring(0,sb.length()-1);
	}
	
	public static String md5Encode(String inStr,String unicode) throws Exception {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		byte[] byteArray = inStr.getBytes(unicode);
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	
	public static String getOrderID(){
    	String format="yyyyMMddHHmmss";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	String time= sdf.format(new Date());
    	int num = (int)(Math.random()*100);
    	return time+String.valueOf(num);
    }
	
	public static String sendPost(String url, String param,String unicode) {
		OutputStreamWriter out = null;
		BufferedReader read = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection  conn = (HttpURLConnection )realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Charset", "gbk");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(param.length()));
            
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new OutputStreamWriter(conn.getOutputStream(),unicode);
			out.write(param);
			out.flush();
			read = new BufferedReader(new InputStreamReader(conn.getInputStream(),unicode));
			String line;
			while ((line = read.readLine()) != null) {
				result += line;
			}
		}
		catch (Exception e) {
			System.out.println("error!" + e);
			e.printStackTrace();
		}
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(read!=null){
					read.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 * 
	 * @param imgPath
	 * 
	 */

	public static void encoderQRCode(String content, String path)
			throws Exception {
		Qrcode qrcodeHandler = new Qrcode();

		qrcodeHandler.setQrcodeErrorCorrect('M');

		qrcodeHandler.setQrcodeEncodeMode('B');

		qrcodeHandler.setQrcodeVersion(7);
		
		System.out.println(content);

		byte[] contentBytes = content.getBytes("gb2312");

		BufferedImage bufImg = new BufferedImage(140, 140,BufferedImage.TYPE_INT_RGB);

		Graphics2D gs = bufImg.createGraphics();

		gs.setBackground(Color.WHITE);

		gs.clearRect(0, 0, 140, 140);

		// 设定图像颜色> BLACK

		gs.setColor(Color.BLACK);

		// 设置偏移量 不设置可能导致解析出错

		int pixoff = 2;

		// 输出内容> 二维码

		if (contentBytes.length > 0 && contentBytes.length < 120) {

			boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);

			for (int i = 0; i < codeOut.length; i++) {

				for (int j = 0; j < codeOut.length; j++) {

					if (codeOut[j][i]) {

						gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);

					}
				}
			}
		} 
		else {
			System.err.println("QRCode content bytes length = "

			+ contentBytes.length + " not in [ 0,120 ]. ");

		}
		gs.dispose();
		bufImg.flush();
		// 生成二维码QRCode图片
		ImageIO.write(bufImg, "png", new File(path));
	}
	/**
	 * RSA 方式验签
	 * @param signData 
	 * @param serverSign
	 * @param serverCert
	 * @param unicode
	 * @return
	 */
	/*public static boolean RSABack(String signData,String serverSign,String serverCert ,String unicode){
		RSASignUtil util = new RSASignUtil( ReadConfig.rootPath);
		 boolean  flag = util.verify(signData,serverSign,serverCert,unicode);
		 return flag;
	}*/
	/**
	 * 
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 * 
	 * @param imgPath
	 * 
	 */

	/*public static void encoderQRCodeIO(String content, HttpServletResponse resp)
			throws Exception {
		Qrcode qrcodeHandler = new Qrcode();

		qrcodeHandler.setQrcodeErrorCorrect('M');

		qrcodeHandler.setQrcodeEncodeMode('B');

		qrcodeHandler.setQrcodeVersion(7);

		System.out.println(content);

		byte[] contentBytes = content.getBytes("gb2312");

		BufferedImage bufImg = new BufferedImage(140, 140,

		BufferedImage.TYPE_INT_RGB);

		Graphics2D gs = bufImg.createGraphics();

		gs.setBackground(Color.WHITE);

		gs.clearRect(0, 0, 140, 140);

		// 设定图像颜色> BLACK

		gs.setColor(Color.BLACK);

		// 设置偏移量 不设置可能导致解析出错

		int pixoff = 2;

		// 输出内容> 二维码

		if (contentBytes.length > 0 && contentBytes.length < 120) {

			boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);

			for (int i = 0; i < codeOut.length; i++) {

				for (int j = 0; j < codeOut.length; j++) {

					if (codeOut[j][i]) {

						gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);

					}
				}
			}
		} else {
			System.err.println("QRCode content bytes length = "

			+ contentBytes.length + " not in [ 0,120 ]. ");

		}
		gs.dispose();
		bufImg.flush();
		// 生成二维码QRCode图片
		resp.getOutputStream().flush();
		ImageIO.write(bufImg, "png", resp.getOutputStream());
		
	}*/
}
