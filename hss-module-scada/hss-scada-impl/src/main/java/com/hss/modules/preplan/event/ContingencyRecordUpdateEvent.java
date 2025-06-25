package com.hss.modules.preplan.event;

import org.springframework.context.ApplicationEvent;

/**
 * 应急处置记录更新
 * @author hd
 */
public class ContingencyRecordUpdateEvent extends ApplicationEvent {
    public ContingencyRecordUpdateEvent(Object source) {
        super(source);
    }
}
