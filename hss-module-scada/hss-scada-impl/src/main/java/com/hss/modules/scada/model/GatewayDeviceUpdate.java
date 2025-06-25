package com.hss.modules.scada.model;

import lombok.Data;

/**
 * @author hd
 */
@Data
public class GatewayDeviceUpdate {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 老的点位id
     */
    private String oldPointId;

    /**
     * 新的点位id
     */
    private String newPointId;
}
