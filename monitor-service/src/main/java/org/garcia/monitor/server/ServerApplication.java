package org.garcia.monitor.server;

import jakarta.annotation.Resource;
import org.garcia.monitor.server.tests.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("org.garcia.monitor.server.dao")
public class ServerApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServerApplication.class, args);
        System.out.println(run.getBean("oshiUtil"));
    }
}
