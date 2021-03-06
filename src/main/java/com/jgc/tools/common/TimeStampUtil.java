package com.jgc.tools.common;

public class TimeStampUtil {

	/**
	 * 上次获取的时间截
	 * 
	 */
	private static  volatile long lastTimestamp = -1L;

	public static long getTimestamp() {

		long timestamp = timeGen();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			// 阻塞到下一个毫秒,获得新的时间戳
			timestamp = tilNextMillis(lastTimestamp);
		}

		lastTimestamp = timestamp;
		
		return timestamp;
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * 
	 * @param lastTimestamp
	 *            上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected static long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected static long timeGen() {
		return System.currentTimeMillis();
	}

	private TimeStampUtil() {
	};
}
