package com.hss.modules.scada.ws;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/3/6 10:25
 */
@Slf4j
public class WsSession {
    private final Session session;

    public WsSession(Session session) {
        this.session = session;
    }

    public synchronized void sendMessage(String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            log.error("发送ws消息失败", e);
        }
    }
    public String getId() {
        return session.getId();
    }
}
