package com.jgc.tools.common;

//短链生成
public class ShortChainUtils {

	private static char[] charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * 62的7次方，保证８位字符串的最小值
	 */
	private static final long MIN = 3521614606208L;

	private static final long SUFFIX_ID = ((1L << 46) - 1);

	private static final long PRE_ID = (3L << 46);

	private static final long MAX_ID = 211106232532991L;

	public static String next(long id) {

		if (id < 0 || id > MAX_ID) {
			throw new IllegalArgumentException("Illegal id: " + id);
		}

		// 非线性
		id = swap(id, 7, 0);
		id = swap(id, 6, 1);

		// Id后46位
		long value = id & SUFFIX_ID;

		// Id后46位的最低八位和高1-9位交换
		value = (((value >> 8) | ((value & 255) << 38)));

		// Id前两位|Id后４６位
		id = (id & PRE_ID) | value;

		// 加上最小值, 保证生成的62进制最少8位
		id += MIN;

		String str = convertDecimalToBase62(id);

		return str;
	}

	/**
	 * xxxx xxxx xxxx x0xx xxxx xx0x xxxx xxxx|0000 0000 0000 0b00 0000 00a0
	 * 0000 0000
	 */
	private static long swap(long value, int x, int y) {

		return (value & (~(1 << x)) & (~(1 << y))) | (((value >> x) & 1) << y) | (((value >> y) & 1) << x);
	}

	private static String convertDecimalToBase62(long number) {
		Long rest = number;
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {

			char aa = charSet[(int) (rest - (rest / 62) * 62)];
			result.append(aa);
			rest = rest / 62;
		}

		return result.reverse().toString();

	}

	private ShortChainUtils() {
	};
}
