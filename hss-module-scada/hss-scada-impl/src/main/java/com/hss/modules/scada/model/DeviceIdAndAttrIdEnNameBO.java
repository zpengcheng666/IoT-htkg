package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 设备id和属性id
 * @author hd
 */
@Data
public class DeviceIdAndAttrIdEnNameBO {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 属性id
     */
    private String attrId;

}
