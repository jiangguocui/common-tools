package com.jgc.tools.common;

public class SignatureUtils {

	private SignatureUtils() {
	};

	/**
	 * 生成签名	
	 * @param time　时间戳，单位毫秒
	 * @param random　随机数
	 * @param secretKey　服务端私钥
	 * @return
	 * @throws Exception
	 */
	public static String signature(long time, String random, String secretKey) throws Exception {

		String signature = String.format("%s:%s:%s", time, random, getSign(time, random, secretKey));

		return Base64Utils.encodeStr(signature.getBytes());
	}

	public static String getSign(long time, String random, String secretKey) throws Exception{
		StringBuilder sign = new StringBuilder();
		
		sign.append(time);
		sign.append("\n");
		sign.append(random);
		sign.append("\n");
		sign.append(secretKey);
		sign.append("\n");
		
		return EncryptUtil.encryptSHA256(sign.toString());
	}
	
	/**
	 * 	验证签名，
	 * @param signature　待验证签名
	 * @param secretKey　服务端私钥
	 * @param expire　过期时间，单位毫秒
	 * @return
	 */
	public static Boolean validateSign(String signature, String secretKey, int expire) {

		String sign = Base64Utils.decodeStr(signature);

		String[] signs = sign.split(":");

		if (signs.length < 3) {
			return false;
		}

		long curTimestamp = System.currentTimeMillis();
		long signTimestamp = Long.valueOf(signs[0]);
		if ((curTimestamp - signTimestamp) > expire) {
			return false;
		}

		String newSign = null;
		try {
			newSign = getSign(Long.valueOf(signs[0]), signs[1], secretKey);
		} catch (Exception e) {
			// TODO:记录日志
			return false;
		}

		if (!signs[2].equals(newSign)) {
			return false;
		}

		return true;
	}
	
	/**
	 * 验证签名	
	 * @param signature　待验证签名
	 * @param random	随机数
	 * @param secretKey　服务端私钥
	 * @param expire　过期时间，单位毫秒
	 * @return
	 */
	public static Boolean validateSign(String signature, String random, String secretKey, int expire) {

		String sign = Base64Utils.decodeStr(signature);

		String[] signs = sign.split(":");

		if (signs.length < 3) {
			return false;
		}

		long curTimestamp = System.currentTimeMillis();
		long signTimestamp = Long.valueOf(signs[0]);
		if ((curTimestamp - signTimestamp) > expire) {
			return false;
		}

		if (!random.equals(signs[1])) {
			return false;
		}
		
		String newSign = null;
		try {
			newSign = getSign(Long.valueOf(signs[0]), signs[1], secretKey);
		} catch (Exception e) {
			// TODO:记录日志
			return false;
		}

		if (!signs[2].equals(newSign)) {
			return false;
		}

		return true;
	}

}
