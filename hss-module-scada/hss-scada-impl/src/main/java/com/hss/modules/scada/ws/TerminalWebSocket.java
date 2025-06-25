package com.hss.modules.scada.ws;

import com.hss.modules.scada.ws.terminal.TerminalWebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author hd
 */
@Component
@Slf4j
@ServerEndpoint("/wsTerminal/{terminalId}")
public class TerminalWebSocket {

    private static TerminalWebSocketService terminalWebSocketService;

    private String terminalId;
    private Session session;

    @Autowired
    public void setTerminalWebSocketService(TerminalWebSocketService terminalWebSocketService) {
        TerminalWebSocket.terminalWebSocketService = terminalWebSocketService;
    }



    /**
     *
     * @param session
     * @param terminalId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "terminalId") String terminalId) {
        this.session  = session;
        this.terminalId = terminalId;
        TerminalWebSocket.terminalWebSocketService.initOpen(terminalId, session);
    }



    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            TerminalWebSocket.terminalWebSocketService.removeSession(terminalId, session);
        } catch (Exception e) {
            log.error("【websocket】连接断开时异常", e);
        }
    }



    /**
     * 收到客户端消息后调用的方法
     *
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("ws接受客户端消息={}",message);
    }

    /**
     * 发送错误时的处理
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("ws错误，terminalId={},e={}",terminalId, error);
        TerminalWebSocket.terminalWebSocketService.removeSession(terminalId, session);

    }

}
