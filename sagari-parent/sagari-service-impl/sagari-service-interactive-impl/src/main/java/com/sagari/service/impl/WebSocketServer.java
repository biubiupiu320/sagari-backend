package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sagari.service.entity.Letter;
import com.sagari.service.mapper.LetterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author biubiupiu~
 */
@Component
@ServerEndpoint("/letter/{userId}")
public class WebSocketServer {
    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static Map<Integer, WebSocketServer> clients = new ConcurrentHashMap<>();
    private Session session;
    private Integer userId;
    public static LetterMapper letterMapper;

    @OnOpen
    public void onOpen(@PathParam("userId") Integer userId, Session session) {
        this.userId = userId;
        this.session = session;
        clients.put(userId, this);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error(error.getMessage());
    }

    @OnClose
    public void onClose() {
        clients.remove(userId);
    }

    @OnMessage
    public void onMessage(String data, Session session) {
        JSONObject jsonObject = JSON.parseObject(data);
        Letter letter = jsonObject.toJavaObject(Letter.class);
        letter.setCreateTime(System.currentTimeMillis());
        letter.setDel(false);
        try {
            if (letterMapper.createMsg(letter) > 0) {
                letter.setStatus(2);
                sendMessage(letter.getToId(), JSON.toJSONString(letter));
            } else {
                letter.setStatus(0);
            }
            sendMessage(letter.getFromId(), JSON.toJSONString(letter));
        } catch (Exception e) {
            letter.setStatus(0);
            sendMessage(letter.getFromId(), JSON.toJSONString(letter));
        }

    }

    public void sendMessage(Integer toId, String data) {
        WebSocketServer connect = clients.get(toId);
        if (connect != null) {
            connect.session.getAsyncRemote().sendText(data);
        }
    }
}
