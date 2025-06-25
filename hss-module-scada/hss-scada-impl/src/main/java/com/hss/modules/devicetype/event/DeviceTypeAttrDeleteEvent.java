package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import org.springframework.context.ApplicationEvent;

/**
* @description: 属性删除事件
* @author zpc
* @date 2024/3/20 9:32
* @version 1.0
*/
public class DeviceTypeAttrDeleteEvent extends ApplicationEvent {
    public DeviceTypeAttrDeleteEvent(DeviceTypeAttribute source) {
        super(source);
    }
}
