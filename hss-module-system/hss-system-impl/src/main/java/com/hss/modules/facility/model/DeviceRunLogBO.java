package com.hss.modules.facility.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/26 15:58
 */
@Data
@AllArgsConstructor
public class DeviceRunLogBO {


    /**
     * 设备id
     */
    @NotNull
    private String deviceId;

    /**
     * 开始运行日期
     */
    @NotNull
    private Date startDate;

    /**
     * 结束运行日期
     */
    @Nullable
    private Date endDate;





}
