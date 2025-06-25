package com.hss.modules.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author hd
 * 天气信息更新事件
 */
public class WeatherUpdateEvent extends ApplicationEvent {
    public WeatherUpdateEvent(Object source) {
        super(source);
    }
}
