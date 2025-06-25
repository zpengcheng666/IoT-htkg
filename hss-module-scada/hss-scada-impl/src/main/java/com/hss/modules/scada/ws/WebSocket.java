package com.hss.modules.scada.ws;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hss.modules.scada.model.CommandType;
import com.hss.modules.scada.model.DeviceModel;
import com.hss.modules.scada.model.ScadaWsMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zpc
 * @version 1.0
 * @description: 根据场景id查询并发送消息数据
 * @date 2024/3/19 15:30
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{stageId}")
public class WebSocket {

    /**
     * session
     */
    private WsSession wsSession;

    /**
     * 场景id
     */
    private String stageId;

    public static ConcurrentHashMap<String, Map<String, WsSession>> SCENE_SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "stageId") String stageId) {
        try {
            this.wsSession = new WsSession(session);
            this.stageId = stageId;
            Map<String, WsSession> map = WebSocket.SCENE_SESSION_MAP.computeIfAbsent(stageId, key -> new ConcurrentHashMap<>());
            map.put(session.getId(), wsSession);
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

    /**
     * 移除session
     */
    private void removeSession() {
        Map<String, WsSession> map = WebSocket.SCENE_SESSION_MAP.get(stageId);
        map.remove(wsSession.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("【websocket消息】收到客户端消息{}:", message);
        //心跳处理
        if (null != message && message.indexOf(CommandType.HEARTCMD) != -1) {
            //心跳标识
            String CONTROLCMD = message.split("HEARTCMD")[1];
            log.info("接收到客户端发送的心跳：" + CONTROLCMD);
            String msg = buildMessage(ScadaWsMessageType.HEART, "'心跳正常'");
            wsSession.sendMessage(msg);
        }
    }

    /**
     * 发送错误时的处理
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("webSocket异常，stageId={},e={}", stageId, error);
        if (session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("webSocket关闭session失败", e);
            }
        }
        removeSession();
    }

    public void sendMessage(String messageType, DeviceModel model, String stageId) {
        String messageStr = buildMessage(messageType, model);
        sendMessage(messageStr, stageId);
    }

    /**
     * 发送消息
     * @param message
     * @param stageId
     */
    public static void sendMessage(String message, String stageId) {
        Map<String, WsSession> map = SCENE_SESSION_MAP.get(stageId);
        if (map != null && !map.isEmpty()) {
            for (WsSession wsSession : map.values()) {
                wsSession.sendMessage(message);
            }
        }
    }

    /**
     * 构建消息体
     * @param messageType
     * @param messageContent
     */
    private static String buildMessage(String messageType, Object messageContent) {
        ScadaWsData scadaWsData = new ScadaWsData();
        scadaWsData.setMessageType(messageType);
        scadaWsData.setMessageContent(messageContent);
        return JSONObject.toJSONString(scadaWsData, SerializerFeature.DisableCircularReferenceDetect);
    }
}
