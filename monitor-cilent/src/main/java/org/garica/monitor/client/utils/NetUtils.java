package org.garica.monitor.client.utils;

import common.core.json.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.garica.monitor.client.entity.Result;
import org.garica.monitor.client.entity.Results;
import org.garica.monitor.client.entity.ServerIndex;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class NetUtils {

    final static String PROTOCOL = "http://";

    final static String DOMAIN_PATH = "/monitor-client/";

    HttpClient client = HttpClient.newHttpClient();

    @Resource
    @Lazy
    ServerIndex index;

    public void get(String url){

    }

    public Result<HttpResponse> toVerifyRegisterToken(){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    //todo 优化常量抽取到配置???
                    .uri(new URI(PROTOCOL+index.getIpv4()+":8080"+ DOMAIN_PATH + "register"))
                    .header("register_message", index.getRegisterToken())
                    .build();
            HttpResponse<String> send = client.send
                    (request, HttpResponse.BodyHandlers.ofString());
            return Results.success(send,"获取到连接结果",Result.NET_SUCCESS_CODE);
        }
        catch (Exception e) {
            log.error("连接出现错误: {}", e.getMessage());
            return Results.failure(null,"出现连接异常",Result.NET_ERROR_CODE);
        }
    }


}
