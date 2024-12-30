/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.gateway.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
public class MemorySessionManager {

    private static class lazyHolder {
        private static final MemorySessionManager INSTANCE = new MemorySessionManager();
    }
    public static MemorySessionManager getInstance() {
        return lazyHolder.INSTANCE;
    }

    private MemorySessionManager() {}

    private final Map<String, String> sessions = new HashMap<>();

//    public Map<String, String> getSessions() {
//        return sessions;
//    }

    public void updateSession(String userId, String jti) {
        sessions.put(userId, jti);
    }
    public void removeSession(String userId) {
        sessions.remove(userId);
    }
    public boolean isValid(String userId, String jti) {
        return sessions.containsKey(userId) && sessions.get(userId).equals(jti);
    }

    public boolean checkSession(String userId, String jti) {
        // TODO: 会话检查
        return true;
    }
}
