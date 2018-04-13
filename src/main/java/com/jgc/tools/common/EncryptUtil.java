package com.jgc.tools.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;

public class EncryptUtil {

	private static final String KEY_SHA256 = "SHA-256";
	private static final String KEY_MD5 = "MD5";

	private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

	// 十六进制下数字到字符的映射数组
	private final static String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 标准MD5加密
	 * 
	 * @param content
	 * @return
	 */
	public static String encryptMD5STD(String content) {
		String resultString = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(content.getBytes());

			// 将得到的字节数组变成字符串返回
			resultString = byteArrayToHexString(md5.digest());
		} catch (Exception e) {
			logger.error("encryptMD5 fail", e);
		}

		return resultString.toLowerCase();
	}

	/**
	 * MD5+Salt加密
	 * 
	 * @param content
	 * @return
	 */
	public static String encryptMD5Salt(String content) {

		String resultString = "";
		String appkey = "fdjf,jkgfkl";

		byte[] a = appkey.getBytes();
		byte[] datSource = content.getBytes();
		byte[] b = new byte[a.length + 4 + datSource.length];

		int i;
		for (i = 0; i < datSource.length; i++) {
			b[i] = datSource[i];
		}

		b[i++] = (byte) 163;
		b[i++] = (byte) 172;
		b[i++] = (byte) 161;
		b[i++] = (byte) 163;

		for (int k = 0; k < a.length; k++) {
			b[i] = a[k];
			i++;
		}

		try {
			MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(b);

			// 将得到的字节数组变成字符串返回
			resultString = new HexBinaryAdapter().marshal(md5.digest());// 转为十六进制的字符串
		} catch (Exception e) {
			logger.error("encryptMD5 fail", e);
		}

		return resultString.toLowerCase();
	}

	public static String encryptSHA256(String content) throws Exception {

		MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA256);
		messageDigest.update(content.getBytes("UTF-8"));
		return byteArrayToHexString(messageDigest.digest());

	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param 字节数组
	 * @return 十六进制字符串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/** 将一个字节转化成十六进制形式的字符串 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HEX_DIGITS[d1] + HEX_DIGITS[d2];
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new String(EncryptUtil.encryptSHA256("3232ewewe")));
	}
}
