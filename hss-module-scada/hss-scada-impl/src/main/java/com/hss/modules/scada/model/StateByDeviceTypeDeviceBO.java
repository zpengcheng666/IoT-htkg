package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 根据设备类型统计
 * @author hd
 */
@Data
public class StateByDeviceTypeDeviceBO {
    /**
     * 数量
     */
    Integer count;
    /**
     * 设备类型id
     */
    String deviceTypeId;
}
