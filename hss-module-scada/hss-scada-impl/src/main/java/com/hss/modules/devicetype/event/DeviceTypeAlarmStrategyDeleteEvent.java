package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import org.springframework.context.ApplicationEvent;

/**
 * 报警策略删除事件
 * @author hd
 */
public class DeviceTypeAlarmStrategyDeleteEvent extends ApplicationEvent {
    public DeviceTypeAlarmStrategyDeleteEvent(DeviceTypeAlarmStrategy source) {
        super(source);
    }
}
