package com.hss.modules.scada.service;

import com.hss.modules.scada.model.GatewayDeviceUpdate;

/**
* @description: 网关附加操作
* @author zpc
* @date 2024/3/19 15:04
* @version 1.0
*/
public interface IGatewayAttachmentService {

    /**
     * 网关掉线
     * @param gatewayId 网关id
     */
    void down(String gatewayId);

    /**
     * 网关在线
     * @param gatewayId
     */
    void up(String gatewayId);

    /**
     * 获取值
     * @param attrEnName 属性英文名称
     * @param value 值
     * @param device 设备id
     * @return 值
     */
    String getValue(String attrEnName, String value, String device);

    /**
     * 更新
     * @param source
     */
    void deviceUpdate(GatewayDeviceUpdate source);
}
