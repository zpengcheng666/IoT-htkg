package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 设备类型策略管理列表查询
 * @author hd
 */
@Data
public class DeviceTypeStrategyList {

    /**
     * 策略id
     */
    private String id;

    /**
     * 存储/报警策略
     */
    private String strategy;
    /**
     * 策略名称
     */
    private String name;

    /**
     * 策略类型名称
     */
    private String typeName;
}
