package com.hss.modules.door.event;

import org.springframework.context.ApplicationEvent;

/**
 * 大屏刷卡信息
 * @author hd
 */
public class TerminalDoorCardUpdateEvent extends ApplicationEvent {
    public TerminalDoorCardUpdateEvent(Object source) {
        super(source);
    }
}
