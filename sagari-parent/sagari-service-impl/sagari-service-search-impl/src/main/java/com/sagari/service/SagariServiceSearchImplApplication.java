package com.sagari.service;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author biubiupiu~
 */
@SpringBootApplication
@EnableEurekaClient
@EnableApolloConfig
@EnableFeignClients
@EnableSwagger2Doc
@EnableElasticsearchRepositories(basePackages = "com.sagari.service.es")
public class SagariServiceSearchImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagariServiceSearchImplApplication.class, args);
    }

}
