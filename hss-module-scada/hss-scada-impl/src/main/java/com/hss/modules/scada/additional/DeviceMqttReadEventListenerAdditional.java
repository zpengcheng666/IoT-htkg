package com.hss.modules.scada.additional;

import com.hss.modules.scada.entity.ConDeviceAttribute;

import java.util.List;

/**
* @description: 接受mqtt消息后需要执行的额外动作
* @author zpc
* @date 2024/3/19 16:20
* @version 1.0
*/
public interface DeviceMqttReadEventListenerAdditional {

    /**
     * 执行动作附加造作
     * @param deviceId 设备id
     * @param attrs 属性列表
     */
    void additionalOperate(String deviceId, List<ConDeviceAttribute> attrs);
}
