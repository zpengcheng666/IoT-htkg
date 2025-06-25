package com.hss.modules.scada.ws;

import com.alibaba.fastjson.JSONObject;
import com.hss.modules.message.dto.AlarmWebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/wsAlarm/{terminalId}")
public class AlarmWebSocket {

    /**
     * session 客户端连接都会创建一个this
     */
    private Session session;

    /**
     * session容器
     */
    public static ConcurrentHashMap<String, Session> SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "terminalId") String terminalId) {
        try {
            this.session = session;
            SESSIONS.put(session.getId(), session);
        } catch (Exception e) {
            log.error("【websocket】建立连接的时候异常", e);
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            removeSession();
        } catch (Exception e) {
            log.error("【websocket】连接断开时异常", e);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
    }


    @OnError
    public void onError(Session session, Throwable error) {
        log.error("alarmWebSocket错误", error);
        if (session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("alarmWebSocket错误关闭session失败", e);
            }
        }
        removeSession();
    }


    private void removeSession() {
        SESSIONS.remove(session.getId());
    }

    public  static void sentMessage(AlarmWebSocketMessage message) {
        if (SESSIONS.isEmpty()) {
            return;
        }
        String jsonMsg = JSONObject.toJSONString(message);
        for (Session session : SESSIONS.values()) {
            if (session != null && session.isOpen()) {
                try {
                    synchronized (session) {
                        session.getBasicRemote().sendText(jsonMsg);
                    }
                } catch (Exception e) {
                    log.error("【websocket】发送消息时异常", e);
                }
            }


        }
    }

}
