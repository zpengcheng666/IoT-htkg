package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import org.springframework.context.ApplicationEvent;

/**
* @description: 实行编辑事件
* @author zpc
* @date 2024/3/20 9:33
* @version 1.0
*/
public class DeviceTypeAttrEditEvent extends ApplicationEvent {
    public DeviceTypeAttrEditEvent(DeviceTypeAttribute source) {
        super(source);
    }
}
