package com.hss.modules.scada.model;

import lombok.Data;

/**
 * 网关和设备关联关系
 * @author hd
 */
@Data
public class GatewayDevice {

    /**
     * 网关id
     */
    private String gatewayId;

    /**
     * 设备id
     */
    private String deviceId;

}
