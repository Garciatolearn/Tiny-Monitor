package common.core.snowflake.core.serviceid;


import common.core.snowflake.core.IdGenerator;
import common.core.snowflake.core.snowflake.SnowFlakeInfo;
import common.core.snowflake.toolkit.SnowFlakeUtil;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/22
 **/
public class DefaultServiceIdGenerator implements ServiceIdGenerator{

    private final IdGenerator idGenerator;

    private long maxBizIdBitLen;

    public DefaultServiceIdGenerator(long serviceIdBitLen){
        idGenerator = SnowFlakeUtil.getSnowFlakeInstance();
        this.maxBizIdBitLen = (long) Math.pow(2,serviceIdBitLen);
    }

    public DefaultServiceIdGenerator(){
        //todo 基因
        this(4);
    }

    @Override
    public SnowFlakeInfo parseSnowflakeId(long snowFlakeId) {
        SnowFlakeInfo flakeInfo = SnowFlakeInfo.builder()
                .timeStamp((snowFlakeId >> TIMESTAMP_LEFT_SHIFT) + DEFAULT_TWEPOCH)
                .dataCenterId((int)((snowFlakeId >> DATA_CENTER_ID_SHIFT) & ~(-1L << DATA_CENTER_ID_BITS)))
                .workerId((int) ((snowFlakeId >> WORKER_ID_SHIFT) & ~(-1L << WORKER_ID_BITS)))
                .sequence((int)((snowFlakeId >> SEQUENCE_ACTUAL_BITS) & ~(-1L << SEQUENCE_BITS)))
                .build();
        return flakeInfo;
    }

    @Override
    public long nextId(long serviceId){
        long id = idGenerator.nextId();
        return id;
    }

    @Override
    public String nextIdStr(long serviceId) {
        return Long.toString(nextId(serviceId));
    }

    /**
     * 工作 ID 5 bit
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据中心 ID 5 bit
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 序列号 12 位，表示只允许 workerId 的范围为 0-4095
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 真实序列号 bit
     */
    private static final long SEQUENCE_ACTUAL_BITS = 8L;


    /**
     * 机器节点左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 默认开始时间
     */
    private static long DEFAULT_TWEPOCH = 1288834974657L;

    /**
     * 数据中心节点左移 17 位
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间毫秒数左移 22 位
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

}
