package com.sagari.service;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author biubiupiu~
 */
@SpringBootApplication
@EnableEurekaClient
@EnableApolloConfig
@EnableElasticsearchRepositories(basePackages = "com.sagari.service.es")
public class SagariServiceSearchImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagariServiceSearchImplApplication.class, args);
    }

}
