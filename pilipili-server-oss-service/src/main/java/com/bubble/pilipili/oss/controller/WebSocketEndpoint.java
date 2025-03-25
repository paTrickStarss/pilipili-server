/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Bubble
 * @date 2025.03.22 11:16
 */
@Slf4j
@Component
@ServerEndpoint("/ws/oss/{uid}")
public class WebSocketEndpoint {

    private static final CopyOnWriteArraySet<Session> endpoints = new CopyOnWriteArraySet<>();

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        try {
            log.info("[WebSocketEndpoint] onOpen: {}", session.getId());
            endpoints.add(session);
            sessions.put(uid, session);
            checkEndpoints();
        } catch (Exception e) {
            log.error("[WebSocketEndpoint] onOpen Error: {}", e.getMessage());
            throw e;
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("uid") String uid) {
        try {
            log.info("[WebSocketEndpoint] onClose: {}", session.getId());
            endpoints.remove(session);
            sessions.remove(uid);
            checkEndpoints();
        } catch (Exception e) {
            log.error("[WebSocketEndpoint] onClose Error: {}", e.getMessage());
            throw e;
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("[WebSocketEndpoint] onMessage From: {}\nMessage: {}", session.getId(), message);
        String reply = "Server reply " + message + " for: " + session.getId();
        doSendSingleMessage(session, reply);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("[WebSocketEndpoint] onError From: {}\nError: {}", session.getId(), error.getMessage());
    }

    /**
     * 发送单播消息
     * @param uid
     * @param message
     */
    public void sendSingleMessage(String uid, String message) {
        Session session = sessions.get(uid);
        if (session == null) {
            log.warn("[WebSocketEndpoint] User session not found: {}", uid);
            return;
        }
        if (!session.isOpen()) {
            log.warn("[WebSocketEndpoint] User session not open: {}", uid);
            return;
        }
        doSendSingleMessage(session, message);
    }

    /**
     * 执行发送单播消息
     * @param session
     * @param message
     */
    private void doSendSingleMessage(Session session, String message) {
        try {
            log.info("[WebSocketEndpoint] sendSingleMessage to [{}]\nMessage: {}", session.getId(), message);
            session.getAsyncRemote().sendText(message);

        } catch (Exception e) {
            log.error("[WebSocketEndpoint] sendSingleMessage Error: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 发送多播消息
     * @param uidList
     * @param message
     */
    public void sendMultiMessage(List<String> uidList, String message) {
        uidList.forEach(uid -> {
            Session session = sessions.get(uid);
            if (session != null && session.isOpen()) {
                try {
                    doSendSingleMessage(session, message);
                } catch (Exception e) {
                    log.error("[WebSocketEndpoint] sendMultiMessage Error: {}", e.getMessage());
                    throw e;
                }
            }
        });
    }

    /**
     * 发送广播消息
     * @param message
     */
    public void sendBroadcastMessage(String message) {
        log.info("[WebSocketEndpoint] sendBroadcastMessage : {}", message);
        for (Session session : endpoints) {
            try {
                if (session != null && session.isOpen()) {
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                log.error("[WebSocketEndpoint] sendBroadcastMessage Error: {}", e.getMessage());
                throw e;
            }
        }
    }

    private void checkEndpoints() {
        log.info("Current Size: {}", endpoints.size());
    }
}
