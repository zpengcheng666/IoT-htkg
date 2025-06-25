package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import org.springframework.context.ApplicationEvent;

/**
 * 存储策略删除事件
 * @author hd
 */
public class DeviceTypeStoreStrategyDeleteEvent extends ApplicationEvent {
    public DeviceTypeStoreStrategyDeleteEvent(DeviceTypeStoreStrategy source) {
        super(source);
    }
}
