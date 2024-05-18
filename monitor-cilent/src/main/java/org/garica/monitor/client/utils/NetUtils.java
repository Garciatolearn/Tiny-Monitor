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

    final static String PORT_SERVER = "8080";

    HttpClient client = HttpClient.newHttpClient();

    @Resource
    @Lazy
    ServerIndex index;

    private HttpResponse<String> get(String url) throws IOException, URISyntaxException, InterruptedException {
        return get(url,null);
    }

    private HttpResponse<String> get(String url,String... headers) throws IOException, InterruptedException,
            URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(PROTOCOL+index.getIpv4()+PORT_SERVER+DOMAIN_PATH+url))
                .GET()
                .headers(headers)
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public Result<HttpResponse> toVerifyRegisterToken(){
        try {
            HttpResponse<String> send = get("register","register_message",index.getRegisterToken());
            return Results.success(send,"获取到连接结果",Result.NET_SUCCESS_CODE);
        }
        catch (Exception e) {
            log.error("连接出现错误: {}", e.getMessage());
            return Results.failure(null,"出现连接异常",Result.NET_ERROR_CODE);
        }
    }


}
