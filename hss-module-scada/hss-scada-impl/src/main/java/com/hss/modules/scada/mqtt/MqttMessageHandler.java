package com.hss.modules.scada.mqtt;

/**
 * mqtt数据处理
 * @author hd
 */
public interface MqttMessageHandler {

    /**
     * 接受消息接口
     * @param message 消息
     */
    void handleMessage(String message);

    /**
     * 链接断开接口
     * @param gatewayId 网关id
     */
    void connectionLost(String gatewayId);

    /**
     * 连接成功
     * @param gatewayId
     */
    void connectComplete(String gatewayId);

}
