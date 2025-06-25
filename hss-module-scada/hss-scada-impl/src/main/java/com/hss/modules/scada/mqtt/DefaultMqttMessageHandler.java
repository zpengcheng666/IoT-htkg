package com.hss.modules.scada.mqtt;

import com.hss.modules.scada.process.ScadaDataHandlerService;
import com.hss.modules.scada.service.IGatewayAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* @description: mqtt消息处理、网关链接断开、网关上线
* @author zpc
* @date 2024/3/20 9:58
* @version 1.0
*/
@Component
@Slf4j
public class DefaultMqttMessageHandler implements MqttMessageHandler {

    @Autowired
    private ScadaDataHandlerService scadaDataHandlerService;
    @Autowired
    private IGatewayAttachmentService gatewayAttachmentService;

    @Override
    public void handleMessage(String messageContent) {
        try {
            scadaDataHandlerService.parseMessage(messageContent);
        } catch (Throwable e) {
            log.error("处理mqtt消息出错msg={},e={}",messageContent,e);
        }
    }

    @Override
    public void connectionLost(String gatewayId) {
        log.error("网关断开链接id={}", gatewayId);
        gatewayAttachmentService.down(gatewayId);
    }

    @Override
    public void connectComplete(String gatewayId) {
        gatewayAttachmentService.up(gatewayId);
    }
}
