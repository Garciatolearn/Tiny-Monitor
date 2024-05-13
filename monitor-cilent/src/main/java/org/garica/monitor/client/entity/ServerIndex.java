package org.garica.monitor.client.entity;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.garica.monitor.client.config.JsonPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@PropertySource(value = "file:config/server.json", ignoreResourceNotFound = true,factory = JsonPropertySourceFactory.class)
@Component
@ConfigurationProperties
public class ServerIndex {

    String ipv4;

    String registerToken;
    public boolean validate() {
        if (StrUtil.isBlankIfStr(ipv4) || !ReUtil.isMatch(PatternPool.IPV4,ipv4)) {
            log.error("错误的服务器地址(ipv4): {}", ipv4);
            return false;
        }
        if (StrUtil.isBlankIfStr(registerToken)) {
            log.error("错误的registerToken: {}", registerToken);
            return false;
        }
        log.info("配置读取成功,进行连接尝试.....: ipv4: {} ; token: {}",ipv4,registerToken);
        return true;
    }

}
