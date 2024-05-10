package common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(OshiConfig.OSHI_PREFIX)
@Data
public class OshiConfig {
    public final static String OSHI_PREFIX = "common.oshi";

    private boolean bootstrap = false;
}
