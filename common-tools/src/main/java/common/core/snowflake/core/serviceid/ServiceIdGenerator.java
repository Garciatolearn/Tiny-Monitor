package common.core.snowflake.core.serviceid;


import common.core.snowflake.core.IdGenerator;
import common.core.snowflake.core.snowflake.SnowFlakeInfo;

public interface ServiceIdGenerator extends IdGenerator {

    default long nextId(long serviceId) {
        return 0L;
    };

    default long nextId(String serviceId) {
        return 0L;
    }

    default String nextIdStr(long serviceId) {
        return null;
    }


    default String nextIdStr(String serviceId) {
        return null;
    }


    SnowFlakeInfo parseSnowflakeId(long snowflakeId);
}
