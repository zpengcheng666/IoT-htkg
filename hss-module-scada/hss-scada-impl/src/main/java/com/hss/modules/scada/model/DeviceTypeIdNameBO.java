package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 设备类型id和名字
 * @author hd
 */
@Data
public class DeviceTypeIdNameBO {
    /**
     * id
     */
    String id;
    /**
     * 名字
     */
    String name;
}
