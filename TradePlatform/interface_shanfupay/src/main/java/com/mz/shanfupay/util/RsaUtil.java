package com.mz.shanfupay.util;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.ArrayUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RsaUtil {

	
	
	public static String BuildMysign(Map<String, String> sArray, String key) {
		String prestr = CreateLinkString(sArray); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		prestr = prestr + key; // 把拼接后的字符串再与安全校验码直接连接起来
		String mysign = Md5Encrypt.md5(prestr);
		return mysign;
	}

	/**
	 * 功能：除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> ParaFilter(Map<String, String> sArray) {
		List<String> keys = new ArrayList<String>(sArray.keySet());
		Map<String, String> sArrayNew = new HashMap<String, String>();
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) sArray.get(key);

			sArrayNew.put(key, value);
		}

		return sArrayNew;
	}

	/**
	 * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串 需要排序并参与字符拼接的参数组
	 * 
	 * @return 拼接后字符串
	 */
	public static String CreateLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符o
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}
	
	
	
	/**
	 * Rsa 加密
	 * @param param 要加密的 数据
	 * @param path 证书文件路径(商户公钥)
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws CertificateException 
	 * @throws FileNotFoundException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static String encrypt(String param,String path) throws UnsupportedEncodingException, CertificateException, FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		byte[] msg = param.getBytes("utf-8"); //待加密的数据
		
		// 用证书的公钥加密
		CertificateFactory cff = CertificateFactory.getInstance("X.509");
		InputStream fis1 = RsaUtil.class.getResourceAsStream("/key/safeepay.cer");
//		FileInputStream fis1 = new FileInputStream(path + "WEB-INF"
//				+ File.separator + "key" + File.separator + "safeepay.cer"); // 证书文件
		
		Certificate cf = cff.generateCertificate(fis1);
		PublicKey pk1 = cf.getPublicKey(); //得到证书文件携带的公钥
		Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // 定义算法：RSA
		byte[] dataReturn = null;
		c1.init(Cipher.PUBLIC_KEY, pk1);
		for (int i = 0; i < msg.length; i += 100) {
			byte[] doFinal = c1.doFinal(ArrayUtils.subarray(msg, i, i + 100));

			dataReturn = ArrayUtils.addAll(dataReturn, doFinal);
		}

		BASE64Encoder encoder = new BASE64Encoder();

		String afjmText = encoder.encode(dataReturn);

		return afjmText;
	}
	
	
	
	/**
	 * 解密
	 * @param return_data 返回的 数据
	 * @param path 秘钥文件
	 * @return
	 * @throws Exception 
	 */
	public static String decrypt(byte[] return_data, String path) throws Exception{
		final String KEYSTORE_PASSWORD = "clientok";
		final String KEYSTORE_ALIAS = "clientok";
		KeyStore ks = KeyStore.getInstance("PKCS12");
		InputStream fis = RsaUtil.class.getResourceAsStream("/key/clientok.p12");
//		FileInputStream fis=new FileInputStream("F:/workspace_new/hurong_manage/src/main/resources/key/clientok.p12");
		char[] nPassword = null;
		if ((KEYSTORE_PASSWORD == null) || KEYSTORE_PASSWORD.trim().equals("")) {
			nPassword = null;
		} else {
			nPassword = KEYSTORE_PASSWORD.toCharArray();
		}
		ks.load(fis, nPassword);
		fis.close();
		PrivateKey prikey = (PrivateKey) ks.getKey(KEYSTORE_ALIAS, nPassword);
		Cipher rc2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rc2.init(Cipher.DECRYPT_MODE, prikey);
		StringBuilder bf_r = new StringBuilder();
		byte[] bs = null;
		for (int i = 0; i < return_data.length; i += 128) {
			byte[] subarray = ArrayUtils.subarray(return_data, i, i + 128);
			byte[] doFinal = rc2.doFinal(subarray);
			bs = ArrayUtils.addAll(bs, doFinal);
		}
		bf_r.append(new String(bs, "utf-8"));
		return bf_r.toString();
	}
	
	
	
	public static void main(String[] args) throws Exception {

		// TODO Auto-generated method stub
		String detail = "AdCs3Nhzt2nhVVBQ07vnU9QgQqCX09xtQzAdPVDq+8gMSIh2K4fcZJPr05/j6yOeaAMDTXCdtSKN\nxiwHN1Np62Z5pbsf1rWJ+YArCwsxgi11q+ZGo3pwbW0kiION9eu2/4gqW1VJ/QpM+bPUdrjNyw+Y\nx+jFlUbP1Q605OEQdzaH/AdzP5yG+ldilSxtQ8wPZVYC/hlthMEQPNRcOYHccb//Bf8LuMBZbndF\nYtWAKHUgqj6VBe8t91PINqDKJAevaUM6Ms/BL15EQSF/oBm/8reihOo44+SgH1xG/NJf0UsddzUw\nVdSvg5fOoQ7lyzplh2lqx2RvrB2aodDQi6Mu/oNr+fKMmoL2iAidklmy2fEbtDld2Uiyka3j2Zux\nz9ZGo2/zZFt3IAAjg72temUa7oI2ok0PFrgse3pvg0demugV/NcouzQ+wmabiJw+QwbGuaMenYLb\np7vYjwHlmteM5zBLb89JfR4HH17U6ZMLVTKb8tCoUrFSIlWsAvc1lRv0";

		System.out.println(detail);
		//进行解密
		String paydata;
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] return_data = decoder.decodeBuffer(detail);
		//paydata = RsaUtil.decrypt(re,"");
		
		
		
		final String KEYSTORE_PASSWORD = "clientok";
		final String KEYSTORE_ALIAS = "clientok";
		KeyStore ks = KeyStore.getInstance("PKCS12");
//		InputStream fis = RsaUtil.class.getResourceAsStream("/key/clientok.p12");
		FileInputStream fis=new FileInputStream("F:/workspace_new/hurong_manage/src/main/resources/key/clientok.p12");
		char[] nPassword = null;
		if ((KEYSTORE_PASSWORD == null) || KEYSTORE_PASSWORD.trim().equals("")) {
			nPassword = null;
		} else {
			nPassword = KEYSTORE_PASSWORD.toCharArray();
		}
		ks.load(fis, nPassword);
		fis.close();
		PrivateKey prikey = (PrivateKey) ks.getKey(KEYSTORE_ALIAS, nPassword);
		Cipher rc2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rc2.init(Cipher.DECRYPT_MODE, prikey);
		StringBuilder bf_r = new StringBuilder();
		byte[] bs = null;
		for (int i = 0; i < return_data.length; i += 128) {
			byte[] subarray = ArrayUtils.subarray(return_data, i, i + 128);
			byte[] doFinal = rc2.doFinal(subarray);
			bs = ArrayUtils.addAll(bs, doFinal);
		}
		bf_r.append(new String(bs, "utf-8"));
		System.out.println(bf_r.toString());	
	
		
	}
	
}
