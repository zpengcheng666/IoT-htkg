package com.hss.modules.system.event;

import org.springframework.context.ApplicationEvent;

/**
* @description: 终端修改事件
* @author zpc
* @date 2024/3/20 16:23
* @version 1.0
*/
public class TerminalUpdateEvent extends ApplicationEvent {
    public TerminalUpdateEvent(Object source) {
        super(source);
    }
}
