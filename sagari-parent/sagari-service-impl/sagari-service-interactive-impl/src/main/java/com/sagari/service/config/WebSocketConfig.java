package com.sagari.service.config;

import com.sagari.service.impl.WebSocketServer;
import com.sagari.service.mapper.LetterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author biubiupiu~
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setMapper(LetterMapper letterMapper) {
        WebSocketServer.letterMapper = letterMapper;
    }
}
