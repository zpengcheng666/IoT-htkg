package com.hss.modules.scada.ws.terminal;


import javax.websocket.Session;

/**
 * 终端ws服务类
 * @author hd
 */
public interface TerminalWebSocketService {


    /**
     * 初始化连接
     * @param terminalId 终端id
     * @param session 联系
     */
    void initOpen(String terminalId, Session session);

    /**
     * 关闭练级
     * @param terminalId 终端id
     * @param session 会话
     */
    void removeSession(String terminalId, Session session);



}
