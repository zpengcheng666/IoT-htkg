package com.hss.modules.scada.listener;

import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.mqtt.MqttFactory;
import com.hss.modules.scada.service.IConWangGuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @description: 系统启动加载网关连接
* @author zpc
* @date 2024/3/20 9:34
* @version 1.0
*/
@Component
public class GatewayConnetListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private MqttFactory mqttFactory;
    @Autowired
    private IConWangGuanService conWangGuanService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<ConWangGuan> list = conWangGuanService.list();
        for (ConWangGuan conWangGuan : list) {
            mqttFactory.gatewayRegister(conWangGuan);
        }
    }
}
