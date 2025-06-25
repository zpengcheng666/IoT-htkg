package com.hss.modules.message.event;

import org.springframework.context.ApplicationEvent;
/**
* @description: 卫星临空消息通知
* @author zpc
* @date 2024/3/21 9:17
* @version 1.0
*/
public class SatelliteArrivedEvent extends ApplicationEvent {
    public SatelliteArrivedEvent(Object source) {
        super(source);
    }
}
