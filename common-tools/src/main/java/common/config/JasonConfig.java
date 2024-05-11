package common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(JasonConfig.JSON_PREFIX)
public class JasonConfig {

    public static final String JSON_PREFIX = "common.json";

    private boolean bootstrap = false;
}
