package com.hss.modules.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author hd
 * 通知消息更新
 */
public class NoticUpdateEvent extends ApplicationEvent {
    public NoticUpdateEvent(Object source) {
        super(source);
    }
}
