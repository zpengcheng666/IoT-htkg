package com.hss.modules.inOutPosition.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author hd
 * 大屏进出记录变更
 */
public class TerminalInOutUpdateEvent extends ApplicationEvent {
    public TerminalInOutUpdateEvent(Object source) {
        super(source);
    }
}
