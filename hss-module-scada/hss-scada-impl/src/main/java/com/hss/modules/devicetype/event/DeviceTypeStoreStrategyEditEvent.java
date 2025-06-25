package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import org.springframework.context.ApplicationEvent;

/**
 * 存储策略编辑事件
 * @author hd
 */
public class DeviceTypeStoreStrategyEditEvent extends ApplicationEvent {
    public DeviceTypeStoreStrategyEditEvent(DeviceTypeStoreStrategy source) {
        super(source);
    }
}
