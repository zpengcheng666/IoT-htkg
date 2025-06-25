package com.hss.modules.scada.service;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.DeviceAttrData;

import java.util.List;

/**
* @description: 获取属性数据、更新属性信息、根据设备id获取列表、根据设备id和属性enName查询值
* @author zpc
* @date 2024/3/19 14:54
* @version 1.0
*/
public interface DeviceDataService {
    /**
     * 获取属性数据
     * @param attrId 属性id
     * @return 属性数据
     */
    DeviceAttrData getAttrValueByAttrId(String attrId);

    /**
     * 更新属性信息
     * @param attr
     */
    void updateAttrValueByAttr(ConDeviceAttribute attr);

    /**
     * 根据设备id获取列表
     * @param deviceId
     * @return
     */
    List<DeviceAttrData> listByDeviceId(String deviceId);

    /**
     * 根据设备id和属性enName查询值
     * @param deviceId 设备id
     * @param attrEnName 属性英文名称
     * @return 值
     */
    String getValueByDeviceIdAndAttrEnName(String deviceId, String attrEnName);

    /**
     * 获取信息
     * @param deviceId 设备id
     * @param attrEnName 属性英文名称
     * @return 记录信息
     */
    DeviceAttrData getByDeviceIdAndEnName(String deviceId, String attrEnName);
}
