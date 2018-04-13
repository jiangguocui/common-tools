package com.jgc.tools.common;

import java.util.Random;

public class RandomCodeUtils {

	private RandomCodeUtils() {
	};

	/**
	 * 生成指定位数随机码
	 * 
	 * @param len
	 * @return
	 */
	public static String generateRandomCode(int len) {

		Random random = new Random();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int number = random.nextInt(75) + 48;// [48,122]

			if (number >= 58 && number <= 64) {// 跳过特殊字符
				number = number + random.nextInt(20) + 7;
			}

			if (number >= 91 && number <= 96) {// 跳过特殊字符
				number = number + random.nextInt(21) + 6;
			}
			sb.append((char) number);
		}

		return sb.toString();
	}
}
