package com.hss.modules.scada.model;

import lombok.Data;

import java.util.List;

/**
 * 读取网关数据
 * @author 26060
 */
@Data
public class GateWayVariableBO {
    /**
     * 设备id
     */
    private String DeviceID;

    /**
     * 设备名称
     */
    private String DeviceName;


    /**
     * 变量列表
     */
    private List<GatewayVarable> DeviceVar;

}
