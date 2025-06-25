package com.hss.modules.alarm.event;

import org.springframework.context.ApplicationEvent;

/**
 * 报警确认触发联动事件
 * @author hd
 */
public class AlarmAckEvent extends ApplicationEvent {
    public AlarmAckEvent(Object source) {
        super(source);
    }
}
