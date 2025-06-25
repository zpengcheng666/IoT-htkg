package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import org.springframework.context.ApplicationEvent;

/**
 * 报警策略添加事件
 * @author hd
 */
public class DeviceTypeAlarmStrategyAddEvent extends ApplicationEvent {
    public DeviceTypeAlarmStrategyAddEvent(DeviceTypeAlarmStrategy source) {
        super(source);
    }
}
