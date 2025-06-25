package com.hss.modules.scada.event;

import org.springframework.context.ApplicationEvent;

/**
* @description: 绑定关系变更触发事件
* @author zpc
* @date 2024/3/20 9:29
* @version 1.0
*/
public class ScadaDeviceAttrPointEvent extends ApplicationEvent {
    public ScadaDeviceAttrPointEvent(Object source) {
        super(source);
    }
}
