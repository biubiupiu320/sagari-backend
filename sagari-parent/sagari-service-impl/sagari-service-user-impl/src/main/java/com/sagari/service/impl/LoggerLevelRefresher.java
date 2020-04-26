package com.sagari.service.impl;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author biubiupiu~
 */
@Service
public class LoggerLevelRefresher implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final Logger log = LoggerFactory.getLogger(LoggerLevelRefresher.class);

    @ApolloConfig
    private Config config;

    @PostConstruct
    private void initialize() {
        refreshLoggingLevels(config.getPropertyNames());
    }

    @ApolloConfigChangeListener(interestedKeyPrefixes = {"logging.level."})
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels(changeEvent.changedKeys());
    }

    private void refreshLoggingLevels(Set<String> changedKeys) {
        log.info("Refreshing logging levels");

        /**
         * refresh logging levels
         * @see org.springframework.cloud.logging.LoggingRebinder#onApplicationEvent
         */
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));

        log.info("Logging levels refreshed");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}