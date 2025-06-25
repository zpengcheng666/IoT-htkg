package com.hss.modules.scada.mqtt;

import com.hss.core.common.exception.HssBootException;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.modules.scada.entity.ConWangGuan;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zpc
 * @version 1.0
 * @description: 自定义mqtt消息工厂
 * @date 2024/3/20 10:03
 */
@Component
@Slf4j
public class MqttFactory {
    @Value("${mqtt.consumer.consumerTopics}")
    private String[] consumerTopics;
    @Value("${mqtt.client.keepAliveInterval}")
    private int keepAliveInterval;

    @Value("${mqtt.client.connectionTimeout}")
    private int connectionTimeout;
    @Autowired
    private TaskExecutor sleepTaskExecutor;

    @Autowired
    private MqttMessageHandler messageHandler;

    /**
     * 网关id和客户端map
     */
    private final Map<String, MqttClient> gatewayIdClientMap = new ConcurrentHashMap<>();

    /**
     * 获取mqtt客户端
     *
     * @param gatewayId 网关id
     * @return
     */
    public MqttClient getClient(String gatewayId) {
        return gatewayIdClientMap.get(gatewayId);
    }

    /**
     * 添加mqtt链接
     *
     * @param gateway
     */
    public void gatewayRegister(ConWangGuan gateway) {
        try {
            MqttClientPersistence persistence = new MemoryPersistence();
            String url = "tcp://" + gateway.getIp() + ":18083";
//            String url = "tcp://" + "localhost" + ":1883";
            String clientId = "paho_" + (new Random().nextInt(9999 - 1000 + 1) + 1000);
            MqttClient client = new MqttClient(url, clientId, persistence);
            MqttCallback callBack = new MyMqttCallbackExtended(client, gateway.getId(), consumerTopics, messageHandler);
            client.setCallback(callBack);
            gatewayIdClientMap.put(gateway.getId(), client);
            MqttConnectOptions options = mqttConnectOptions(gateway.getUserName(), gateway.getPassword());
            MqttClientConnectTask mqttClientConnectTask = new MqttClientConnectTask(client, options, sleepTaskExecutor);
            mqttClientConnectTask.setId(getTaskId(gateway.getId()));
            mqttClientConnectTask.start();
        } catch (MqttException e) {
            log.error("创建mqtt客户端失败", e);
            throw new HssBootException("创建mqtt客户端失败");
        }
    }

    @NotNull
    private String getTaskId(String id) {
        return "mqttCon:" + id;
    }

    /**
     * 获取配置信息
     *
     * @param username
     * @param password
     * @return
     */
    private MqttConnectOptions mqttConnectOptions(String username, String password) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        if (password != null) {
            options.setPassword(password.toCharArray());
        }
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(connectionTimeout);
        options.setKeepAliveInterval(keepAliveInterval);
        return options;
    }

    /**
     * 更新特定的 WangGuan 信息。
     * 该方法首先通过 ID 删除原有的 WangGuan 信息，然后重新注册新的 WangGuan 信息。
     *
     * @param conWangGuan 包含需要更新的 WangGuan 信息的对象。
     */
    public void update(ConWangGuan conWangGuan) {
        // 删除旧的 WangGuan 信息
        remove(conWangGuan.getId());
        // 注册新的 WangGuan 信息
        gatewayRegister(conWangGuan);

    }


    /**
     * 删除指定ID的MQTT客户端连接。
     * 该方法首先通过ID从gatewayIdClientMap中查找对应的MQTT客户端，如果找到且该客户端当前连接状态为连接中，则尝试关闭连接。
     * 接着从gatewayIdClientMap中移除该ID对应的客户端，并从sleepTaskExecutor中移除与该ID相关的任务。
     *
     * @param id 客户端的唯一标识符，用于查找和标识需要被删除的MQTT客户端。
     */
    public void remove(String id) {
        // 通过ID从映射中获取MQTT客户端
        MqttClient mqttClient = gatewayIdClientMap.get(id);
        // 检查MQTT客户端是否存在
        if (mqttClient != null) {
            // 检查客户端是否连接中，如果是则尝试关闭连接
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.close();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            // 从映射中移除客户端，并从任务执行器中移除相关任务
            gatewayIdClientMap.remove(id);
            sleepTaskExecutor.remove(getTaskId(id));
        }
    }

}
