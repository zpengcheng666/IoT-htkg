package com.hss.modules.alarm.event;

import org.springframework.context.ApplicationEvent;

/**
 * 大屏报警更新
 * @author hd
 */
public class TerminalAlarmUpdateEvent extends ApplicationEvent {
    public TerminalAlarmUpdateEvent(Object source) {
        super(source);
    }
}
