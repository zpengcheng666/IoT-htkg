package com.hss.modules.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author hd
 * 通知消息更新
 */
public class DoWorkUpdateEvent extends ApplicationEvent {
    public DoWorkUpdateEvent(Object source) {
        super(source);
    }
}
