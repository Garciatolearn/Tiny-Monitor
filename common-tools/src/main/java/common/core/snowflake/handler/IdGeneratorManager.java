package common.core.snowflake.handler;

import common.core.snowflake.core.IdGenerator;
import common.core.snowflake.core.serviceid.DefaultServiceIdGenerator;
import common.core.snowflake.core.serviceid.ServiceIdGenerator;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/22
 **/
public class IdGeneratorManager {

    private static Map<String, IdGenerator> MANAGER = new ConcurrentHashMap<>();

    static {
        MANAGER.put("default", new DefaultServiceIdGenerator());
    }

    public static void registerIdGenerator(@NonNull String resource, @NonNull IdGenerator idGenerator) {
        MANAGER.putIfAbsent(resource, idGenerator);
    }

    public static ServiceIdGenerator getIdGenerator(@NonNull String resource) {
        return Optional.ofNullable(MANAGER.get(resource)).map(each -> (ServiceIdGenerator) each).orElse(null);
    }
    public static ServiceIdGenerator getDefaultIdGenerator() {
        return getIdGenerator("default");
    }
}
