package common.core.snowflake.core.snowflake;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import common.core.snowflake.core.IdGenerator;


import java.io.Serializable;
import java.util.Date;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/20
 **/
public class SnowFlake implements Serializable , IdGenerator {

    private final long serialVersionUID = 1L;

    private static long DEFAULT_TWEPOCH = 1288834974657L;

    /**
     * 回拨时间
     */
    private static long DEFAULT_TIME_OFFSET = 2L;

    private static final long WORKER_ID_BITS = 5L;

    private static final long DATA_CENTER_ID_BITS = 5L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT = WORKER_ID_BITS + DATA_CENTER_ID_BITS + SEQUENCE_BITS;

    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 初始化时间点
     */
    private final long epoch;

    private final long workerId;

    private final long dataCenterId;

    private final boolean useSystemClock;

    /**
     * 允许时间回拨的毫秒数
     */
    private final long timeOffset;

    /**
     * 当在低频模式下时，序号始终为0，导致生成ID始终为偶数<br>
     * 此属性用于限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题。<br>
     * 注意次数必须小于{@link #SEQUENCE_MASK}，{@code 0}表示不使用随机数。<br>
     * 这个上限不包括值本身。
     */
    private final long randomSequenceLimit;

    private long sequence = 0L;

    private long lastTimeStamp = -1L;

    public SnowFlake() {
        this(IdUtil.getWorkerId(IdUtil.getDataCenterId(MAX_DATA_CENTER_ID), MAX_WORKER_ID));
    }

    public SnowFlake(long workerId) {
        this(workerId, IdUtil.getDataCenterId(MAX_WORKER_ID));
    }

    public SnowFlake(long workerId, long dataCenterId) {
        this(workerId, dataCenterId, false);
    }

    public SnowFlake(long workerId, long dataCenterId, boolean isUseSystemClock) {
        this(null, workerId, dataCenterId, isUseSystemClock);
    }

    public SnowFlake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock) {
        this(epochDate, workerId, dataCenterId, isUseSystemClock, DEFAULT_TIME_OFFSET);
    }

    public SnowFlake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock, long timeOffset) {
        this(epochDate, workerId, dataCenterId, isUseSystemClock, timeOffset, 0);
    }

    public SnowFlake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock, long timeOffset, long randomSequenceLimit) {
        this.epoch = (epochDate != null) ? epochDate.getTime() : DEFAULT_TWEPOCH;
        this.workerId = Assert.checkBetween(workerId, 0, MAX_WORKER_ID);
        this.dataCenterId = Assert.checkBetween(dataCenterId, 0, MAX_DATA_CENTER_ID);
        this.useSystemClock = isUseSystemClock;
        this.timeOffset = timeOffset;
        this.randomSequenceLimit = Assert.checkBetween(randomSequenceLimit, 0, SEQUENCE_MASK);

    }

    public long getWorkerId(long id) {
        return id >> WORKER_ID_SHIFT & ~(-1L << WORKER_ID_BITS);
    }

    public long getDataCenterId(long id) {
        return id >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS);
    }

    public long getGenerateDateTime(long id){
        return (id >> TIMESTAMP_LEFT_SHIFT & ~(-1L << 41L)) + epoch;
    }
    @Override
    public String nextIdStr(){
        return Long.toString(nextId());
    }

    private Long genTime(){
        return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
    }

    /**
     * 循环等待下一个时间
     * 防止时间回拨
     * @param lastTimeStamp
     * @return nextTimeStamp
     */
    private long tilNextMillis(Long lastTimeStamp){
        long timeStamp = genTime();
        while (timeStamp == lastTimeStamp){
            timeStamp = genTime();
        }
        if (timeStamp < lastTimeStamp) {
            throw new IllegalStateException(StrUtil.format("Clock moved backwards, refusing to generate id for {}ms",
                    lastTimeStamp - timeStamp));
        }
        return timeStamp;
    }

    @Override
    public synchronized long nextId(){
        long timeStamp = genTime();
        if (timeStamp < this.lastTimeStamp ) {
            if(this.lastTimeStamp - timeStamp < timeOffset) {
                //容忍指定回拨,避免NTP校时造成的异常
                timeStamp = lastTimeStamp;
            } else {
                throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimeStamp - timeStamp));
            }
        }
        if (timeStamp == this.lastTimeStamp){
            final long sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timeStamp = tilNextMillis(lastTimeStamp);
            }
            this.sequence = sequence;
        } else{
            if (randomSequenceLimit > 1) {
                sequence = RandomUtil.randomLong(randomSequenceLimit);
            } else {
                sequence = 0L;
            }
        }
        lastTimeStamp = timeStamp;
        return (timeStamp - epoch) << TIMESTAMP_LEFT_SHIFT | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
    }


    public SnowFlakeInfo parseId(long snowFlakeId){
        SnowFlakeInfo flakeInfo = SnowFlakeInfo.builder()
                .timeStamp((snowFlakeId >> TIMESTAMP_LEFT_SHIFT) + epoch)
                .dataCenterId((int)((snowFlakeId >> DATA_CENTER_ID_SHIFT) & MAX_DATA_CENTER_ID))
                .workerId((int) ((snowFlakeId >> WORKER_ID_SHIFT) & MAX_WORKER_ID))
                .sequence((int)(snowFlakeId & SEQUENCE_MASK))
                .build();
        return flakeInfo;
    }


}
