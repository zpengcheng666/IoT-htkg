package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import org.springframework.context.ApplicationEvent;

/**
 * 存储策略添加事件
 * @author hd
 */
public class DeviceTypeStoreStrategyAddEvent extends ApplicationEvent {
    public DeviceTypeStoreStrategyAddEvent(DeviceTypeStoreStrategy source) {
        super(source);
    }
}
