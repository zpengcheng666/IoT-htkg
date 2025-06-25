package com.hss.modules.devicetype.event;

import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import org.springframework.context.ApplicationEvent;

/**
* @description: 实行编辑事件
* @author zpc
* @date 2024/3/20 9:34
* @version 1.0
*/
public class DeviceTypeNameChangeEvent extends ApplicationEvent {
    public DeviceTypeNameChangeEvent(DeviceTypeManagement source) {
        super(source);
    }
}
