package common.core.snowflake.config;


import common.core.snowflake.core.snowflake.LocalRedisWorkIdChoose;
import common.core.snowflake.core.snowflake.RandomWorkIdChoose;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/22
 **/
@Configuration
public class DistributedAutoConfiguration {

    @Resource
    ApplicationContext applicationContext;
    @Bean
    @ConditionalOnProperty("spring.data.redis.host")
    public LocalRedisWorkIdChoose localRedisWorkIdChoose(){
        return new LocalRedisWorkIdChoose(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean(LocalRedisWorkIdChoose.class)
    public RandomWorkIdChoose randomWorkIdChoose(){
        return new RandomWorkIdChoose();
    }
}
