package common.core.snowflake.toolkit;


import common.core.snowflake.core.snowflake.SnowFlake;
import common.core.snowflake.core.snowflake.SnowFlakeInfo;
import common.core.snowflake.handler.IdGeneratorManager;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/22
 **/
public final class SnowFlakeUtil {

    private static SnowFlake SNOWFLAKE;

    public static void initSnowFlake(SnowFlake snowFlake) {
        SNOWFLAKE = snowFlake;
    }

    public static SnowFlake getSnowFlakeInstance() {
        return SNOWFLAKE;
    }

    public static long nextId() {
       return SNOWFLAKE.nextId();
    }

    public static String nextIdStr() {
        return SNOWFLAKE.nextIdStr();
    }

    public static SnowFlakeInfo parseSnowFlakeId(long id) {
        return SNOWFLAKE.parseId(id);
    }

    public static SnowFlakeInfo parseSnowFlakeId(String id) {
        return SNOWFLAKE.parseId(Long.parseLong(id));
    }

    public static long nextIdService(String serviceId){
        return IdGeneratorManager.getDefaultIdGenerator().nextId(Long.parseLong(serviceId));
    }

    public static String nextIdStrService(String serviceId){
        return IdGeneratorManager.getDefaultIdGenerator().nextIdStr(Long.parseLong(serviceId));
    }



}
