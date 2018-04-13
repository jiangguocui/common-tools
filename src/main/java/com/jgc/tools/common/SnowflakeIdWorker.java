package com.jgc.tools.common;

public class SnowflakeIdWorker {

    /** 
     * 开始时间截 (2016-01-01)
     */
    private final long twepoch = 1451577600000l;

    
    /**
     * 7bit的自增序列
     */
    private long sequence = 0L;
    
    private final long sequenceBits = 7L;
    
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    
    private final long timestampLeftShift = 22;
    
    /**
     * 4bit的机器码
     */
    private final long workerIdLeftShift = 18;

    private final long maxWorkerId = -1L ^ (-1L << 4);
    
    
    private final long maxBizId = -1L ^ (-1L << 4);
    
    /** 
     * 上次生成ID的时间截
     * 
     */
    private long lastTimestamp = -1L;

    /**
     * 租户id取模
     */
    private final long mod =128;

    /**
     * 获得下一个ID (该方法是线程安全的)
     * 
     * @return SnowflakeId
     */
    public synchronized long nextId(long workerId, long bizId, long bizTenantId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                    maxWorkerId));
        }
        if (bizId > maxBizId || bizId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",
                    maxBizId));
        }

        long timestamp = timeGen();
        
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列 
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            //TODO 随机数
            sequence = 0L;
        }
        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdLeftShift) | ((bizTenantId%mod) << 11) | (sequence << 4) | bizId;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * 
     * @param lastTimestamp
     *            上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * 
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
