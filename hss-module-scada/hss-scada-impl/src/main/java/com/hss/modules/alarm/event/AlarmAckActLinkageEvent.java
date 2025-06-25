package com.hss.modules.alarm.event;

import org.springframework.context.ApplicationEvent;

/**
 * 报警确认触发联动事件
 * @author hd
 */
public class AlarmAckActLinkageEvent extends ApplicationEvent {
    public AlarmAckActLinkageEvent(Object source) {
        super(source);
    }
}
