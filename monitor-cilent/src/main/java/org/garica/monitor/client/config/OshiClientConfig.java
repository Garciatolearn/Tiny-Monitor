package org.garica.monitor.client.config;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import common.core.json.JsonUtils;
import common.core.oshi.OshiUtils;
import common.core.oshi.entity.ComputerDetail;
import common.core.oshi.entity.RuntimeDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.garica.monitor.client.entity.Result;
import org.garica.monitor.client.entity.ServerIndex;
import org.garica.monitor.client.utils.NetUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.*;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
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
    private final NetUtils netUtils;
    private final OshiUtils oshiUtils;

    @EventListener(ApplicationReadyEvent.class)
    public void configOshi() {
        if (BeanUtil.isEmpty(serverIndex)){
            this.loadingProperties();
            this.saveProperties();
        }
        if (serverIndex.validate()) {
            if(this.connectServer()) {
                scheduler.scheduleWithFixedDelay(updateRunTime(), Duration.ofSeconds(3));
            } else{
                log.error("连接失败,可能是服务端or验证码错误,详情请看log日志");
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
        }
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
        log.info("您没有设置相对应的配置文件,现在帮你进行配置;");
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
            RuntimeDetail runtimeDetail = oshiUtils.getRuntimeDetail();
            System.out.println(runtimeDetail);
        };
    }


    private boolean connectServer(){
        log.info("正在连接中......");
        Result<HttpResponse> verifyResult = netUtils.toVerifyRegisterToken();
        if(Objects.equals(verifyResult.getCode(), Result.NET_ERROR_CODE)){
            log.error("出现连接上的错误");
            return false;
        } else {
            //todo 重构响应头消息
            String body = (String) verifyResult.getData().body();
            log.info("获取的response信息为: {}", body);
            try {
                Result result = jsonUtils.toObject(body, Result.class);
                if (result.getCode().equals(Result.ERROR_CODE)){
                    log.error("连接服务端出现异常: {}",result.getMessage());
                    return false;
                }
                return true;
            } catch (JsonProcessingException e) {
                log.error("响应体结构转换异常: {}",e.getMessage());
                return false;
            }
        }
    }

}
