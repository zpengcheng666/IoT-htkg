package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 设备需要绑定的属性和已经报警的属性
 * @author hd
 */
@Data
public class DeviceAttrCountConfigAndBinding {


    /**
     * 需要绑定的属性
     */
    private Integer attrCount;

    /**
     * 已经绑定的属性
     */
    private Integer bindingCount;

}
