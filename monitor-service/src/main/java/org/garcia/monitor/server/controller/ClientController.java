package org.garcia.monitor.server.controller;

import common.core.jwt.JWTutils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.garcia.monitor.server.entity.Result;
import org.garcia.monitor.server.entity.Results;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;

@RestController
@RequestMapping("/monitor-client")
public class ClientController {

    @Resource
    JWTutils jwTutils;

    @GetMapping("register")
    public Result<Void> registerClient(@RequestHeader("Authentication") String jwt){

        return Results.success();
    }

}
