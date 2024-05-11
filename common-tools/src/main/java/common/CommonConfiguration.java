package common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import common.config.JWTConfig;
import common.config.JasonConfig;
import common.config.OshiConfig;
import common.core.json.JsonUtils;
import common.core.jwt.JWTutils;
import common.core.oshi.OshiUtils;
import common.core.snowflake.config.DistributedAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConfigurationPropertiesScan
@Import(DistributedAutoConfiguration.class)
public class CommonConfiguration {

    @ConditionalOnProperty(prefix = JWTConfig.JWT_PREFIX,name = "bootstrap")
    @Bean
    public JWTutils JWTutils(JWTConfig config){
        Algorithm algorithm = Algorithm.HMAC256(config.getSecretKey());
        return new JWTutils(config,algorithm, JWT.require(algorithm).build());
    }

    @ConditionalOnProperty(prefix = OshiConfig.OSHI_PREFIX,name = "bootstrap")
    @Bean
    public OshiUtils oshiUtils(){
        return new OshiUtils();
    }

    @ConditionalOnProperty(prefix = JasonConfig.JSON_PREFIX,name = "bootstrap")
    @Bean
    public JsonUtils jsonUtils(){
        return new JsonUtils();
    }


}
