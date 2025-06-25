package com.hss.modules.scada.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;

/**
* @description: 消息回调
* @author zpc
* @date 2024/3/20 10:05
* @version 1.0
*/
@Slf4j
public class MyMqttCallbackExtended implements MqttCallbackExtended {

    /**
     * 客户端
     */
    private final MqttClient mqttClient;

    /**
     * 网关id
     */
    private final String gatewayId;

    /**
     * 订阅
     */
    private final String[] consumerTopics;

    /**
     * 处理程序
     */
    private final MqttMessageHandler messageHandler;


    public MyMqttCallbackExtended(MqttClient mqttClient, String gatewayId, String[] consumerTopics, MqttMessageHandler messageHandler) {
        this.mqttClient = mqttClient;
        this.gatewayId = gatewayId;
        this.consumerTopics = consumerTopics;
        this.messageHandler = messageHandler;
    }

    /**
     * 当链接完成时被调用，用于处理链接成功后的逻辑。
     * @author zpc
     * @param b 表示链接是否成功。如果为true，则链接成功；如果为false，则链接失败。
     * @param s 通常为链接失败时的错误信息，如果链接成功，此参数可能为null或空字符串。
     */
    @Override
    public void connectComplete(boolean b, String s) {
        try {
            // 尝试订阅mqtt客户端的消费主题
            mqttClient.subscribe(consumerTopics);
            // 如果链接成功，通知消息处理器链接已完成
            if (b) {
                messageHandler.connectComplete(gatewayId);
            }

        } catch (MqttException e) {
            // 订阅失败时，记录错误日志
            log.error("订阅mqtt失败", e);
        }
    }


    /**
     * 链接丢失
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("连接断开", throwable);
        try {
            messageHandler.connectionLost(gatewayId);
        } catch (Exception e) {
            log.error("执行链接断开回调失败", e);
        }
    }

    /**
     * 当收到特定主题的消息时被调用，用于处理接收到的消息。
     * @author zpc
     * @param topic 消息所属的主题。
     * @param message 接收到的MQTT消息对象，包含消息的负载。
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // 将消息负载转换为字符串
        String payLoad = new String(message.getPayload());
        // 检查消息负载是否为空，若为空则记录日志并返回，不处理该消息
        if (StringUtils.isEmpty(payLoad)) {
            log.info("从网关获取的数据为空，丢弃掉当前消息！");
            return;
        }
        // 调用消息处理器处理消息负载
        messageHandler.handleMessage(payLoad);
    }


    /**
     * 发送消息确认回调
     *
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
