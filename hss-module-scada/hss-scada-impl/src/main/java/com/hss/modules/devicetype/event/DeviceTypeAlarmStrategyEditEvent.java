package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import org.springframework.context.ApplicationEvent;

/**
 * 报警策略编辑事件
 * @author hd
 */
public class DeviceTypeAlarmStrategyEditEvent extends ApplicationEvent {
    public DeviceTypeAlarmStrategyEditEvent(DeviceTypeAlarmStrategy source) {
        super(source);
    }
}
