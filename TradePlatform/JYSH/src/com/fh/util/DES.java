package com.fh.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

 
public class DES {
	
	public final static String DES_KEY_STRING = "e8bf6a9b";
	
	 public static String encrypt(String message) throws Exception {
	      
	 
	        return encrypt(message,DES_KEY_STRING);
	    }
    
    public static String encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
 
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
 
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
 
        return encodeBase64(cipher.doFinal(message.getBytes("UTF-8")));
    }
 
    public static String decrypt(String message, String key) throws Exception {
 
        byte[] bytesrc = decodeBase64(message);//convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
 
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
 
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }
 
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
 
        return digest;
    }
 
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
 
        return hexString.toString();
    }
 
     
    public static String encodeBase64(byte[] b) {
        return new String(Base64.encode(b));
    }
     
    public static byte[] decodeBase64(String base64String) {
        return Base64.decode(base64String);
    }
    
    
    static String url = "http://www.okmyj.com/mobile/index.php?act=nncai&op=register_sync&data=";
    
    public static void main(String[] args) throws Exception {
    	String str = "act=login&op=register_sync&Username=12345678901&Password=e10adc3949ba59abbe56e057f20f883e&member_pid=1&Member_mobile=12345678901";
    	String data = encrypt(str,DES_KEY_STRING);
    	//System.out.println(data);
		//System.out.println(decrypt(mw,DES_KEY_STRING));
    	
    	System.out.println(url + data + "&code=" + System.currentTimeMillis());
    	
    	
    	//http://www.okmyj.com/mobile/index.php?act=nncai&op=register_sync&data=DX4YcXBJZNvar+Sau3b5ZYSeIw/qi58+RByZfA5Ct/Qsc1RW6cblxP8PAZnrKrYoOH1qkcP/FiXdKlkc0E02WxISbAqCDTjN+3JT4eKLM1nL58D3MIMk/9LOyzkFdaNyOHK6xX9VMyQiCc24A7pQ3iFWR8yMfSDbswVA4Ky6r+SK03zC1zLsRA==&code=1234
	
    }
}
