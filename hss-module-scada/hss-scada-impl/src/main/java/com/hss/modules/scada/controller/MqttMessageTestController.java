package com.hss.modules.scada.controller;

import com.alibaba.fastjson.JSONObject;
import com.hss.core.common.api.vo.Result;
import com.hss.modules.scada.model.DefaultMqttMessage;
import com.hss.modules.scada.mqtt.MqttProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @description: Mqtt消息管理,消息发布与订阅
* @author zpc
* @date 2024/3/19 14:51
* @version 1.0
*/
@Slf4j
@Api(tags = "Mqtt消息管理")
@RequestMapping("/api/scada")
public class MqttMessageTestController {
    @Autowired
    private MqttProducer mqttProducer;

    @Autowired
    private MqttClient mqttClient;

    @ApiOperation(value = "send", notes = "send")
    @GetMapping("send/topic/{topic}")
    public Result<String> send(@PathVariable String topic, DefaultMqttMessage model){
        String payload = JSONObject.toJSONString(model);
        mqttProducer.send(payload);
        return Result.OK("OK");
    }

    @ApiOperation(value = "subscribe", notes = "subscribe")
    @GetMapping("subscribe/{topic}")
    public Result<String> subscribe(@PathVariable String topic){
        try {
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        return Result.OK("OK");
    }
}
