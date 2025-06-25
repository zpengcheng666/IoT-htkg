package com.hss.modules.scada.event;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

/**
* @description: 接受mqtt消息事件
* @author zpc
* @date 2024/3/20 9:36
* @version 1.0
*/
public class ScadaDataMqttReadEvent extends ApplicationEvent {
    public ScadaDataMqttReadEvent(Map<String, List<ConDeviceAttribute>> source) {
        super(source);
    }
}
