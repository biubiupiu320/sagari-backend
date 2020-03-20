package com.sagari.service;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author biubiupiu~
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableApolloConfig
@EnableSwagger2Doc
@MapperScan(basePackages = "com.sagari.service.mapper")
public class SagariServiceInteractiveImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagariServiceInteractiveImplApplication.class, args);
    }

}
