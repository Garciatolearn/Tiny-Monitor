package org.garica.monitor.client.config;

import cn.hutool.core.bean.BeanUtil;
import common.core.json.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.garica.monitor.client.entity.ServerIndex;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.*;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class OshiClientConfig {

    private final ConfigurableApplicationContext applicationContext;
    private final ServerIndex serverIndex;
    private final JsonUtils jsonUtils;
    private final ThreadPoolTaskScheduler scheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void configOshi() {
        if (BeanUtil.isEmpty(serverIndex)){
            this.loadingProperties();
            this.saveProperties();
        }
        else if (serverIndex.validate()) {
            if(this.connectServer()) {
                scheduler.scheduleWithFixedDelay(updateRunTime(), Duration.ofSeconds(3));
            } else{
                log.error("连接失败");
                applicationContext.close();
            }
        } else {
            log.error("config/config.json不存在或其中参数错误");
            applicationContext.close();
        }
    }

    private void saveProperties(){
        File directory = new File("config");
        if(!directory.exists() && directory.mkdir()){
            log.info("conf文件夹不存在,尝试创建");
        };
        File file = new File("config/server.json");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("创建配置文件失败: {}",e.getMessage());
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
            writer.write(jsonUtils.toJson(serverIndex));
            log.info("文件创建成功,准备尝试连接....");
        } catch (IOException e) {
            log.error("打开配置文件失败: {}",e.getMessage());
        }
    }

    private void loadingProperties() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            System.out.print("请输入server端的ipv4(pattern:255.255.255): ");
            Optional.ofNullable(reader.readLine()).ifPresent(serverIndex::setIpv4);
            System.out.print("请输入server端的给予的注册token: ");
            Optional.ofNullable(reader.readLine()).ifPresent(serverIndex::setRegisterToken);
        } catch (IOException e) {
            log.error("滴嘟滴嘟,保存设置期间出现异常: {}",e.getMessage());
        }
    }

    public Runnable updateRunTime() {
        return () -> {
            log.info("正在连接中....");
        };
    }


    private boolean connectServer(){
        log.info("正在连接中......");
        return true;
    }

}
