package com.sagari.service;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author biubiupiu~
 */
@SpringBootApplication
@EnableEurekaClient
@EnableApolloConfig
@EnableSwagger2Doc
public class SagariServiceFileImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagariServiceFileImplApplication.class, args);
    }

}
