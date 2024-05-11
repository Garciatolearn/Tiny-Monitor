package common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = JWTConfig.JWT_PREFIX)
@Data
public class JWTConfig {
    public static final String JWT_PREFIX = "common.jwt";

    private  String secretKey = "Monitor";

    private ChronoUnit timeUnit = ChronoUnit.MINUTES;

    private long time = 10L;

    boolean bootstrap = false;
}
