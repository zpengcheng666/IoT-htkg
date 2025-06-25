package com.hss.modules.scada.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 发送mqtt消息,生产者
 * @author hd
 */
//@Component
@Slf4j
public class MqttProducer {

    @Value("${mqtt.producer.defaultQos}")
    private int defaultProducerQos;

    @Value("${mqtt.producer.defaultRetained}")
    private boolean defaultRetained;

    @Value("${mqtt.producer.defaultTopic}")
    private String defaultTopic;

    @Autowired
    private MqttClient mqttClient;

    public void send(String payload) {
        this.send(defaultTopic, payload);
    }

    public void send(String topic, String payload) {
        this.send(topic, defaultProducerQos, payload);
    }

    public void send(String topic, int qos, String payload) {
        this.send(topic, qos, defaultRetained, payload);
    }

    public void send(String topic, int qos, boolean retained, String payload) {
        try {
            mqttClient.publish(topic, payload.getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
