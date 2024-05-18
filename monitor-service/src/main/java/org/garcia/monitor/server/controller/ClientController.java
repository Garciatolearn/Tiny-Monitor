package org.garcia.monitor.server.controller;

import common.core.jwt.JWTutils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.garcia.monitor.server.entity.Result;
import org.garcia.monitor.server.entity.Results;
import org.garcia.monitor.server.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;

@RestController
@RequestMapping("monitor-client")
public class ClientController {


    @Resource
    ClientService clientService;

    @Resource
    JWTutils jwTutils;

    @GetMapping("register")
    public Result<Void> registerClient(@RequestHeader("register_message") String token){
        return clientService.verifyRegisterToken(token) ? Results.success("注册主机成功") :
                Results.failure("注册失败,token错误");
    }

    @GetMapping("get_token")
    public Result<String> getToken(){
        String registerToken = clientService.createRegisterToken();
        return Results.success(registerToken,"注册成功,data为你的注册token");
    }

}
