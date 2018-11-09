package com.mz.thirdpay.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SignUtil {
	private static final int BUFFSIZE = 8096;

	/** 默认初始向量 */
	private static byte[] DEFAULT_IV = new byte[] {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};

	/**
	 * 文件加密
	 *
	 * @paramsrcFile 源文件
	 * @paramencFile 加密后文件
	 * @parambkey 密码
	 * @paramalgorithm 加密算法
	 * @paramkeySpec 密钥算法
	 * @parambivSpec 向量
	 * @throwsException
	 */
	public static void encrypt(String srcFile, String encFile, byte[] bkey, String algorithm, String keySpec, byte[] bivSpec) throws Exception {
		SecretKeySpec key = new SecretKeySpec(bkey, keySpec);
		Cipher cipher = Cipher.getInstance(algorithm);
		if (bivSpec == null) {
			bivSpec = DEFAULT_IV;
		}
		IvParameterSpec ivSpec = new IvParameterSpec(bivSpec);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		InputStream ins = null;
		OutputStream outs = null;
		try {
			ins = new BufferedInputStream(new FileInputStream(srcFile));
			outs = new CipherOutputStream(new FileOutputStream(encFile), cipher);
			byte[] buf = new byte[BUFFSIZE];
			int count = -1;
			while ((count = ins.read(buf)) != -1) {
				outs.write(buf, 0, count);
				outs.flush();
			}
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (outs != null) {
				try {
					outs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 文件解密
	 *
	 * @param srcFile 源文件
	 * @param dstFile 解密后文件
	 * @param bkey 密码
	 * @param algorithm 加密算法
	 * @param keySpec 密钥算法
	 * @param bivSpec 向量
	 * @throwsException
	 */
	public static void decrypt(String srcFile, String dstFile, byte[] bkey, String algorithm, String keySpec, byte[] bivSpec) throws Exception {
		SecretKeySpec key = new SecretKeySpec(bkey, keySpec);
		Cipher cipher = Cipher.getInstance(algorithm);
		if (bivSpec == null) {
			bivSpec = DEFAULT_IV;
		}
		IvParameterSpec ivSpec = new IvParameterSpec(bivSpec);
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		InputStream ins = null;
		OutputStream outs = null;
		try {
			ins = new CipherInputStream(new BufferedInputStream(new FileInputStream(srcFile)), cipher);
			outs = new BufferedOutputStream(new FileOutputStream(dstFile));
			byte[] buf = new byte[BUFFSIZE];
			int count = -1;
			while ((count = ins.read(buf)) != -1) {
				outs.write(buf, 0, count);
				outs.flush();
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (outs != null) {
				try {
					outs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 文件压缩
	 *
	 * @paramsrcFile
	 * @paramdestFile
	 */
	public static void compress(String srcFile, String dstFile) throws Exception {
		InputStream ins = null;
		ZipOutputStream outs = null;
		try {
			outs = new ZipOutputStream(new FileOutputStream(new File(dstFile)));
			ins = new BufferedInputStream(new FileInputStream(srcFile), BUFFSIZE);
			byte[] buf = new byte[BUFFSIZE];
			ZipEntry entry = new ZipEntry(new File(srcFile).getName());
			outs.putNextEntry(new ZipEntry(entry));
			int count = 0;
			while ((count = ins.read(buf)) != -1) {
				outs.write(buf, 0, count);
				outs.flush();
			}
			outs.closeEntry();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (Exception e) {
				}
			}
			if (outs != null) {
				try {
					outs.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 文件解压缩
	 *
	 * @paramsrcFile
	 * @paramdstFile
	 */
	public static void uncompress(String srcFile, String dstFile) throws Exception {
		ZipFile zfile = null;
		OutputStream outs = null;
		try {
			zfile = new ZipFile(srcFile);
			Enumeration e = zfile.entries();
			if (!e.hasMoreElements()) {
				throw new Exception("压缩包条目为空");
			}
			ZipEntry entry = (ZipEntry) (e.nextElement());
			if (e.hasMoreElements()) {
				throw new Exception("压缩包中包含多个文件或文件夹");
			}
			InputStream ins = zfile.getInputStream(entry);
			outs = new BufferedOutputStream(new FileOutputStream(dstFile));
			byte[] buf = new byte[BUFFSIZE];
			int count = -1;
			while ((count = ins.read(buf)) != -1) {
				outs.write(buf, 0, count);
				outs.flush();
			}
			ins.close();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (zfile != null) {
				try {
					zfile.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (outs != null) {
				try {
					outs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
