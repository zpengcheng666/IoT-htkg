package com.hss.modules.facility.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/26 16:01
 */
public class DeviceRunLogEvent extends ApplicationEvent {
    public DeviceRunLogEvent(@NotNull String deviceId,@NotNull Date startDate, @Nullable Date endDate) {
        super(new DeviceRunLogBO(deviceId, startDate, endDate));
    }

    @Override
    public DeviceRunLogBO getSource() {
        return (DeviceRunLogBO)super.getSource();
    }
}
