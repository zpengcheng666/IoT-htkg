package com.hss.modules.scada.process;

import com.hss.modules.scada.model.DeviceModel;
import lombok.Data;

/**
* @description: 缓存设备信息
* @author zpc
* @date 2024/3/20 9:45
* @version 1.0
*/
@Data
public class ScadaCacheDevice {

    /**
     * 设备信息
     */
    private DeviceModel deviceModel;

    /**
     * 场景id
     */
    private String sceneId;
}
