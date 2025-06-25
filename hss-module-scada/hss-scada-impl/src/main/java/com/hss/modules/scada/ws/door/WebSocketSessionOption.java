package com.hss.modules.scada.ws.door;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * webSocketSession操作
 * @author hd
 */
@Slf4j
public class WebSocketSessionOption {


    private final Session session;
    private final ExecutorService executorService;
    private DoorMessageInfo device;

    public DoorMessageInfo getDevice() {
        return device;
    }

    public void setDevice(DoorMessageInfo device) {
        this.device = device;
    }

    public WebSocketSessionOption(Session session) {
        this.session = session;
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 获取sessionId
     * @return
     */
    public String getId() {
        return session.getId();
    }

    /**
     * 发送消息
     * @param msg 消息
     */
    public void send(String msg){
        executorService.execute(() -> {
            if (!session.isOpen()) {
                try {
                    session.close();
                } catch (IOException e) {
                    log.error("关闭webSocketSession失败", e);
                }
                return;
            }
            Future<Void> future = session.getAsyncRemote().sendText(msg);
            try {
                future.get();
            } catch (InterruptedException e) {
                if (log.isInfoEnabled()) {
                    log.info("线程被打断");
                }
            } catch (ExecutionException e) {
                log.error("发送消息错误", e);
            }

        });


    }

}
