package com.hss.modules.scada.service.impl.advice;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.DeviceAttrModel;

/**
 * @ClassDescription:设备属性、查询、控制、检查进一步增强、扩展
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 14:35
 */
public interface SceneAttrAdvice {

    String INPUT_TYPE_SELECT = "select";
    String INPUT_TYPE_INPUT = "input";
    String INPUT_TYPE_CHECKBOX = "checkbox";

    default DeviceAttrModel adviceBase(DeviceTypeAttribute t, ConDeviceAttribute a, DeviceAttrModel m) {
        if (m == null) {
            return null;
        }
        return advice(t, a, m);
    }

    /**
     * 增强
     * @param t 属性类型
     * @param a 属性信息
     * @param m 显示模型
     * @return 显示模型
     */
    DeviceAttrModel advice(DeviceTypeAttribute t, ConDeviceAttribute a, DeviceAttrModel m);

}
