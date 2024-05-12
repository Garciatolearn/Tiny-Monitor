package org.garica.monitor.client.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.garica.monitor.client.entity.ServerIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class OshiClientConfig {

    private final ConfigurableApplicationContext applicationContext;
    private final ServerIndex serverIndex;

    @EventListener(ApplicationReadyEvent.class)
    public void configOshi() {
        if (serverIndex.validate()) {
            System.out.println(serverIndex);
        } else {
            applicationContext.close();
        }
    }
}
