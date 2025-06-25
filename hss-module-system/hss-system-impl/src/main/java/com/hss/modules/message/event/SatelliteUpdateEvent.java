package com.hss.modules.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author hd
 * 卫星临空更新事件
 */
public class SatelliteUpdateEvent extends ApplicationEvent {
    public SatelliteUpdateEvent(Object source) {
        super(source);
    }
}
